/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cache.lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.factories.annotations.Inject;
import static org.jboss.cache.lock.LockType.WRITE;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionTable;

import java.util.List;

/**
 * Contains specific methods for the PessimisticLockInterceptor.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @deprecated will be removed with pessimistic locking
 */
@Deprecated
@SuppressWarnings("deprecation")
public class PessimisticNodeBasedLockManager extends NodeBasedLockManager
{
   private static final Log log = LogFactory.getLog(PessimisticNodeBasedLockManager.class);
   private static final boolean trace = log.isTraceEnabled();
   private CommandsFactory commandsFactory;

   @Inject
   void injectCommandsFactory(CommandsFactory commandsFactory)
   {
      this.commandsFactory = commandsFactory;
   }

   /**
    * A specific lock method for the PessimisticLockInterceptor.  It should *not* be used anywhere else as it has very
    * peculiar and specific characteristics.
    * <p/>
    * For implementations of this LockManager interface that are not intended for use with the PessimisticLockInterceptor,
    * it is okay not to implement this method (a no-op).
    * <p/>
    *
    * @param fqn                      Fqn to lock
    * @param lockType                 Type of lock to acquire
    * @param ctx                      invocation context
    * @param createIfNotExists        if true, nodes will be created if they do not exist.
    * @param zeroLockTimeout          if true uses 0 as a lock acquisition timeout
    * @param acquireWriteLockOnParent if true, write locks are acquired on parent nodes when child nodes need write locks.
    * @param reverseRemoveCheck       if true, nodes that have been marked as removed in the current transaction may be reversed.
    * @param createdNodes             a list to which nodes created in this method may be added.
    * @param skipNotification         if true, node creation notifications are suppressed.
    * @return true if successful; false otherwise.
    * @throws InterruptedException if interrupted
    */
   public boolean lockPessimistically(InvocationContext ctx, Fqn fqn, LockType lockType,
                                      boolean createIfNotExists, boolean zeroLockTimeout, boolean acquireWriteLockOnParent,
                                      boolean reverseRemoveCheck, List<NodeSPI> createdNodes, boolean skipNotification) throws InterruptedException
   {
      if (fqn == null || configuration.getIsolationLevel() == IsolationLevel.NONE || ctx.isLockingSuppressed())
         return false;

      boolean created;
      long timeout = zeroLockTimeout ? 0 : ctx.getLockAcquisitionTimeout(lockAcquisitionTimeout);
      // make sure we can bail out of this loop
      long cutoffTime = System.currentTimeMillis() + timeout;
      boolean firstTry = true;
      do
      {
         // this is an additional check to make sure we don't try for too long.
         if (!firstTry && System.currentTimeMillis() > cutoffTime)
         {
            throw new TimeoutException("Unable to acquire lock on Fqn " + fqn + " after " + timeout + " millis");
         }

         created = lock(ctx, fqn, lockType, createIfNotExists, timeout, acquireWriteLockOnParent, reverseRemoveCheck, createdNodes, skipNotification);
         firstTry = false;
      }
      while (createIfNotExists && (dataContainer.peek(fqn, false, false) == null));// keep trying until we have the lock (fixes concurrent remove())
      return created;
   }


   /**
    * Acquires locks on the node and on its parrents. Read locks are acquired for exsiting ancestors, with two exceptions:
    * 1) createIfNotExists is true. If an ancestor is created on the fly, then an WL is acquired by default
    * 2) acquireWriteLockOnParent is true. If so AND {@link org.jboss.cache.Node#isLockForChildInsertRemove()} then a read
    * lock will be aquired for the parent of the node.
    *
    * @param createIfNotExists  if true, then missing nodes will be cretaed on the fly. If false, method returns if we
    *                           reach a node that does not exists
    * @param reverseRemoveCheck if true, will reverse removes if needed.
    * @param createdNodes       a list to which any nodes created can register their Fqns so that calling code is aware of which nodes have been newly created.
    * @param skipNotification
    */
   private boolean lock(InvocationContext ctx, Fqn fqn, LockType lockType, boolean createIfNotExists, long timeout,
                        boolean acquireWriteLockOnParent, boolean reverseRemoveCheck, List<NodeSPI> createdNodes, boolean skipNotification)
         throws TimeoutException, LockingException, InterruptedException
   {
      Thread currentThread = Thread.currentThread();
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      boolean created = false;
      // if the tx associated with the current thread is rolling back, barf! JBCACHE-923
      if (gtx != null) TransactionTable.assertTransactionValid(ctx);

      Object owner = (gtx != null) ? gtx : currentThread;
      NodeSPI currentNode;
      if (trace) log.trace("Attempting to lock node " + fqn + " for owner " + owner);
      long expiryTime = System.currentTimeMillis() + timeout;
      currentNode = rootNode;
      NodeSPI parent = null;
      Object childName = null;
      int currentIndex = -1;
      int targetFqnSize = fqn.size();

      do
      {
         if (currentNode == null)
         {
            if (createIfNotExists)
            {
               // if the new node is to be marked as deleted, do not notify!
               currentNode = parent.addChildDirect(childName, !skipNotification);
               created = true;
               if (trace) log.trace("Child node was null, so created child node " + childName);
               if (createdNodes != null) createdNodes.add(currentNode);
            }
            else
            {
               if (trace)
                  log.trace("failed to find or create child " + childName + " of node " + parent);
               return false;
            }
         }
         else
         {
            if (!currentNode.isValid() && createIfNotExists) currentNode.setValid(true, false);
         }

         LockType lockTypeRequired = LockType.READ;
         if (created || writeLockNeeded(ctx, lockType, currentIndex, acquireWriteLockOnParent, createIfNotExists, fqn, currentNode))
         {
            lockTypeRequired = WRITE;
         }

         Fqn currentNodeFqn = currentNode.getFqn();
         // actually acquire the lock we need.  This method blocks.
         acquireNodeLock(ctx, currentNode, owner, lockTypeRequired, timeout);

         LockUtil.manageReverseRemove(ctx, currentNode, reverseRemoveCheck, createdNodes, commandsFactory);
         // make sure the lock we acquired isn't on a deleted node/is an orphan!!
         // look into invalidated nodes as well
         NodeSPI repeek = dataContainer.peek(currentNodeFqn, true, true);
         if (currentNode != repeek)
         {
            if (trace)
               log.trace("Was waiting for and obtained a lock on a node that doesn't exist anymore!  Attempting lock acquisition again.");
            // we have an orphan!! Lose the unnecessary lock and re-acquire the lock (and potentially recreate the node).
            // check if the parent exists!!
            // look into invalidated nodes as well
            currentNode.getLock().releaseAll(owner);
            if (parent == null || dataContainer.peek(parent.getFqn(), true, true) == null)
            {
               // crap!
               if (trace)
                  log.trace("Parent has been deleted again.  Go through the lock method all over again.");
               currentNode = rootNode;
               currentIndex = -1;
               parent = null;
            }
            else
            {
               currentNode = parent;
               currentIndex--;
               parent = null;
               if (System.currentTimeMillis() > expiryTime)
               {
                  throw new TimeoutException("Unable to acquire lock on child node " + Fqn.fromRelativeElements(currentNode.getFqn(), childName) + " after " + timeout + " millis.");
               }
               if (trace) log.trace("Moving one level up, current node is :" + currentNode);
            }
         }
         else
         {
            // we have succeeded in acquiring this lock. Increment the current index since we have gained one level of depth in the tree.
            currentIndex++;

            // now test if this is the final level and if we can quit the loop:
            //if (currentNodeFqn.equals(fqn))//we've just processed the last child
            if (currentIndex == targetFqnSize)
            {
               break;
            }
            if (!fqn.isChildOrEquals(currentNode.getFqn())) // Does this ever happen?  Perhaps with a move(), I suppose?  - MS
            {
               String message = new StringBuilder("currentNode instance changed the FQN(").append(currentNode.getFqn())
                     .append(") and do not match the FQN on which we want to acquire lock(").append(fqn).append(")").toString();
               log.trace(message);
               throw new LockingException(message);
            }
            parent = currentNode;

            childName = fqn.get(currentIndex);
            currentNode = currentNode.getChildDirect(childName);
         }
      }
      while (true);
      return created;
   }

   /**
    * Used by lock()
    * Determins whter an arbitrary node from the supplied fqn needs an write lock.
    */
   private boolean writeLockNeeded(InvocationContext ctx, LockType lockType, int currentNodeIndex, boolean acquireWriteLockOnParent, boolean createIfNotExists, Fqn targetFqn, NodeSPI currentNode)
   {
      int treeNodeSize = targetFqn.size();
      // write lock forced!!
      boolean isTargetNode = currentNodeIndex == (treeNodeSize - 1);
      if (isTargetNode && ctx.getOptionOverrides().isForceWriteLock()) return true;
      //this can be injected, from the caller as a param named wlParent
      if (currentNode.isLockForChildInsertRemove())
      {
         if (acquireWriteLockOnParent && currentNodeIndex == treeNodeSize - 2)
         {
            return true;// we're doing a remove and we've reached the PARENT node of the target to be removed.
         }
         if (!isTargetNode && dataContainer.peek(targetFqn.getAncestor(currentNodeIndex + 2), false, false) == null)
         {
            return createIfNotExists;// we're at a node in the tree, not yet at the target node, and we need to create the next node.  So we need a WL here.
         }
      }
      return lockType == WRITE && isTargetNode;//write lock explicitly requested and this is the target to be written to.
   }

   private void acquireNodeLock(InvocationContext ctx, NodeSPI node, Object owner, LockType lockType, long lockTimeout) throws LockingException, TimeoutException, InterruptedException
   {
      NodeLock lock = node.getLock();
      boolean acquired = lock.acquire(owner, lockTimeout, lockType);
      // Record the lock for release on method return or tx commit/rollback
      if (acquired) ctx.addLock(lock);
   }
}
