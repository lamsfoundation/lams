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
package org.jboss.cache.mvcc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeFactory;
import org.jboss.cache.NodeNotExistsException;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.lock.LockManager;
import static org.jboss.cache.lock.LockType.WRITE;
import org.jboss.cache.lock.TimeoutException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility functions to manipulate wrapping {@link org.jboss.cache.InternalNode}s as {@link
 * org.jboss.cache.mvcc.ReadCommittedNode} or {@link org.jboss.cache.mvcc.RepeatableReadNode}s.  Would also entail
 * locking, if necessary.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
@NonVolatile
public class MVCCNodeHelper {
   DataContainer dataContainer;
   NodeFactory nodeFactory;
   private static final Log log = LogFactory.getLog(MVCCNodeHelper.class);
   private static final boolean trace = log.isTraceEnabled();
   private long defaultLockAcquisitionTimeout;
   private LockManager lockManager;
   private Configuration configuration;
   private boolean writeSkewCheck;
   private boolean lockParentForChildInsertRemove;

   @Inject
   public void injectDependencies(DataContainer dataContainer, NodeFactory nodeFactory, LockManager lockManager, Configuration configuration) {
      this.nodeFactory = nodeFactory;
      this.dataContainer = dataContainer;
      this.configuration = configuration;
      this.lockManager = lockManager;
   }

   @Start
   public void start() {
      defaultLockAcquisitionTimeout = configuration.getLockAcquisitionTimeout();
      writeSkewCheck = configuration.isWriteSkewCheck();
      lockParentForChildInsertRemove = configuration.isLockParentForChildInsertRemove();
   }


   /**
    * Attempts to provide the context with a set of wrapped nodes based on the Collection of fqns passed in.  If the
    * nodes already exist in the context then the node is not wrapped again.
    * <p/>
    * {@link InternalNode}s are wrapped using {@link org.jboss.cache.NodeFactory#createWrappedNode(org.jboss.cache.InternalNode, org.jboss.cache.InternalNode)}  and as such, null internal nodes are treated according to isolation level used.
    * See {@link org.jboss.cache.NodeFactory#createWrappedNode(org.jboss.cache.InternalNode, org.jboss.cache.InternalNode)}  for details on this behaviour.
    * <p/>
    * Note that if the context has the {@link org.jboss.cache.config.Option#isForceWriteLock()} option set, then write
    * locks are acquired and the node is copied.
    * <p/>
    *
    * @param ctx  current invocation context
    * @param fqns collection of Fqns.  Should not be null.
    * @throws InterruptedException if write locks are forced and the lock manager is interrupted.
    */
   public void wrapNodesForReading(InvocationContext ctx, Collection<Fqn> fqns) throws InterruptedException {
      boolean forceWriteLock = ctx.getOptionOverrides().isForceWriteLock();

      // does the node exist in the context?
      for (Fqn f : fqns) wrapNodeForReading(ctx, f, forceWriteLock, true);
   }

   /**
    * Similar to {@link #wrapNodesForReading(org.jboss.cache.InvocationContext, java.util.Collection)} except that this
    * version takes a single Fqn parameter to wrap a single node.
    *
    * @param ctx          current invocation context
    * @param fqn          fqn to fetch and wrap
    * @param putInContext
    * @return read committed node, or null if one is not found.
    * @throws InterruptedException if write locks are forced and the lock manager is interrupted.
    */
   public NodeSPI wrapNodeForReading(InvocationContext ctx, Fqn fqn, boolean putInContext) throws InterruptedException {
      return wrapNodeForReading(ctx, fqn, ctx.getOptionOverrides().isForceWriteLock(), putInContext);
   }

   @SuppressWarnings("unchecked")
   private NodeSPI wrapNodeForReading(InvocationContext ctx, Fqn f, boolean writeLockForced, boolean putInContext) throws InterruptedException {
      NodeSPI n;
      if (writeLockForced) {
         if (trace) log.trace("Forcing lock on reading node " + f);
         return wrapNodeForWriting(ctx, f, true, false, false, false, false);
      } else if ((n = ctx.lookUpNode(f)) == null) {
         if (trace) log.trace("Node " + f + " is not in context, fetching from container.");
         // simple implementation.  Peek the node, wrap it, put wrapped node in the context.
         InternalNode[] nodes = dataContainer.peekInternalNodeAndDirectParent(f, false);
         ReadCommittedNode wrapped = nodeFactory.createWrappedNode(nodes[0], nodes[1]);  // even though parents aren't needed for reading, we hold on to this ref in case the node is later written to.
         if (putInContext && wrapped != null) ctx.putLookedUpNode(f, wrapped);
         return wrapped;
      } else {
         if (trace) log.trace("Node " + f + " is already in context.");
         return n;
      }
   }

   /**
    * Attempts to lock a node if the lock isn't already held in the current scope, and records the lock in the context.
    *
    * @param ctx context
    * @param fqn Fqn to lock
    * @return true if a lock was needed and acquired, false if it didn't need to acquire the lock (i.e., lock was
    *         already held)
    * @throws InterruptedException if interrupted
    * @throws TimeoutException     if we are unable to acquire the lock after a specified timeout.
    */
   private boolean acquireLock(InvocationContext ctx, Fqn fqn) throws InterruptedException, TimeoutException {
      // don't EVER use lockManager.isLocked() since with lock striping it may be the case that we hold the relevant
      // lock which may be shared with another Fqn that we have a lock for already.
      // nothing wrong, just means that we fail to record the lock.  And that is a problem.
      // Better to check our records and lock again if necessary.
      if (!ctx.hasLock(fqn)) {
         if (!lockManager.lockAndRecord(fqn, WRITE, ctx)) {
            Object owner = lockManager.getWriteOwner(fqn);
            throw new TimeoutException("Unable to acquire lock on Fqn [" + fqn + "] after [" + ctx.getLockAcquisitionTimeout(defaultLockAcquisitionTimeout) + "] milliseconds for requestor [" + lockManager.getLockOwner(ctx) + "]! Lock held by [" + owner + "]");
         }
         return true;
      }
      return false;
   }

   /**
    * First checks in contexts for the existence of the node.  If it does exist, it will return it, acquiring a lock if
    * necessary.  Otherwise, it will peek in the dataContainer, wrap the node, lock if necessary, and add it to the
    * context. If it doesn't even exist in the dataContainer and createIfAbsent is true, it will create a new node and
    * add it to the data structure.  It will lock the node, and potentially the parent as well, if necessary.  If the
    * parent is locked, it too will be added to the context if it wasn't there already.
    *
    * @param context             invocation context
    * @param fqn                 to retrieve
    * @param lockForWriting      if true, a lock will be acquired.
    * @param createIfAbsent      if true, will be created if absent.
    * @param includeInvalidNodes if true, invalid nodes are included.
    * @param forRemoval          if true, the parent may also be locked if locking parents for removal is necessary.
    * @param force               if true, will force the write lock even if the node is null.
    * @return a wrapped node, or null.
    * @throws InterruptedException if interrupted
    */
   @SuppressWarnings("unchecked")
   public ReadCommittedNode wrapNodeForWriting(InvocationContext context, Fqn fqn, boolean lockForWriting, boolean createIfAbsent, boolean includeInvalidNodes, boolean forRemoval, boolean force) throws InterruptedException
   {
      return wrapNodeForWriting(context, fqn, lockForWriting,  createIfAbsent, includeInvalidNodes, forRemoval, force, false);
   }

   public ReadCommittedNode wrapNodeForWriting(InvocationContext context, Fqn fqn, boolean lockForWriting, boolean createIfAbsent, boolean includeInvalidNodes, boolean forRemoval, boolean force, boolean childIsNull) throws InterruptedException
   {
      Fqn parentFqn = null;
      ReadCommittedNode n = (ReadCommittedNode) context.lookUpNode(fqn);
      if (createIfAbsent && n != null && n.isNullNode()) n = null;
      if (n != null) // exists in context!  Just acquire lock if needed, and wrap.
      {
         // acquire lock if needed
         if (lockForWriting && acquireLock(context, fqn)) {
            // create a copy of the underlying node
            n.markForUpdate(dataContainer, writeSkewCheck);
         }
         if (trace) log.trace("Retrieving wrapped node " + fqn);
         if (n.isDeleted() && createIfAbsent) {
            if (trace) log.trace("Node is deleted in current scope.  Need to un-delete.");
            n.markAsDeleted(false);
            n.setValid(true, false);
            n.clearData(); // a delete and re-add should flush any old state on the node!
            // has the parent been deleted too?  :-(
            wrapNodeForWriting(context, fqn.getParent(), true, true, includeInvalidNodes, false, force);
         }
      } else {
         // else, fetch from dataContainer.
         InternalNode[] nodes = dataContainer.peekInternalNodeAndDirectParent(fqn, includeInvalidNodes);
         InternalNode in = nodes[0];
         if (in != null) {
            // exists in cache!  Just acquire lock if needed, and wrap.
            // do we need a lock?
            boolean needToCopy = false;
            if (lockForWriting && acquireLock(context, fqn)) {
               needToCopy = true;
            }
            n = nodeFactory.createWrappedNode(in, nodes[1]);
            context.putLookedUpNode(fqn, n);
            if (needToCopy) n.markForUpdate(dataContainer, writeSkewCheck);
         } else if (createIfAbsent) // else, do we need to create one?
         {
            parentFqn = fqn.getParent();
            NodeSPI parent = wrapNodeForWriting(context, parentFqn, false, createIfAbsent, false, false, false);
            // do we need to lock the parent to create children?
            boolean parentLockNeeded = isParentLockNeeded(parent.getDelegationTarget());
            // get a lock on the parent.
            if (parentLockNeeded && acquireLock(context, parentFqn)) {
               ReadCommittedNode parentRCN = (ReadCommittedNode) context.lookUpNode(parentFqn);
               parentRCN.markForUpdate(dataContainer, writeSkewCheck);
            }

            // now to lock and create the node.  Lock first to prevent concurrent creation!
            acquireLock(context, fqn);
            in = nodeFactory.createChildNode(fqn, null, context, false);

            n = nodeFactory.createWrappedNode(in, parent.getDelegationTarget());
            n.setCreated(true);
            n.setDataLoaded(true); // created here so we are loading it here
            context.putLookedUpNode(fqn, n);
            n.markForUpdate(dataContainer, writeSkewCheck);
         }
      }


      // see if we need to force the lock on nonexistent nodes.
      if (n == null && force)
      {
         parentFqn = fqn.getParent();
         if (isParentLockNeeded(parentFqn, context) && !childIsNull) wrapNodeForWriting(context, parentFqn, true, false, includeInvalidNodes, false, force, true);
         acquireLock(context, fqn);
      }

      // now test if we need to lock the parent as well.
      if ((n != null || force) && forRemoval && (parentFqn == null ? parentFqn = fqn.getParent() : parentFqn) != null && isParentLockNeeded(parentFqn, context) && !childIsNull)
         wrapNodeForWriting(context, parentFqn, true, false, includeInvalidNodes, false, force, n == null);

      return n;
   }

   /**
    * The same as {@link #wrapNodeForWriting(org.jboss.cache.InvocationContext, org.jboss.cache.Fqn, boolean, boolean,
    * boolean, boolean, boolean)} except that it takes in an {@link org.jboss.cache.InternalNode} instead of a {@link
    * Fqn}.  Saves on a lookup.
    * <p/>
    * Also assumes that the node exists, and hence will not be created.
    * <p/>
    *
    * @param context invocation context
    * @param node    node to wrap
    * @return a wrapped node, or null.
    * @throws InterruptedException if interrupted
    */
   @SuppressWarnings("unchecked")
   public NodeSPI wrapNodeForWriting(InvocationContext context, InternalNode node, InternalNode parent) throws InterruptedException {
      Fqn fqn = node.getFqn();
      NodeSPI n = context.lookUpNode(fqn);
      if (n != null) // exists in context!  Just acquire lock if needed, and wrap.
      {
         // acquire lock if needed
         if (acquireLock(context, fqn)) {
            // create a copy of the underlying node
            n.markForUpdate(dataContainer, writeSkewCheck);
         }
         if (trace) log.trace("Retrieving wrapped node " + fqn);
      } else {
         // exists in cache!  Just acquire lock if needed, and wrap.
         // do we need a lock?
         boolean needToCopy = false;
         if (acquireLock(context, fqn)) {
            needToCopy = true;
         }
         n = nodeFactory.createWrappedNode(node, parent);
         context.putLookedUpNode(fqn, n);
         if (needToCopy) n.markForUpdate(dataContainer, writeSkewCheck);
      }

      return n;
   }

   /**
    * Wraps a node and all its subnodes and adds them to the context, acquiring write locks for them all.
    *
    * @param ctx context
    * @param fqn fqn to wrap
    * @return a list of Fqns of locks acquired in this call.
    * @throws InterruptedException if the lock manager is interrupted.
    */
   @SuppressWarnings("unchecked")
   public List<Fqn> wrapNodesRecursivelyForRemoval(InvocationContext ctx, Fqn fqn) throws InterruptedException {
      // when removing a node we want to get a lock on the Fqn anyway and return the wrapped node.
      if (fqn.isRoot()) throw new CacheException("Attempting to remove Fqn.ROOT!");

      Fqn parentFqn = fqn.getParent();
      // inspect parent
      boolean needToCopyParent = false;
      boolean parentLockNeeded = isParentLockNeeded(parentFqn, ctx);
      ReadCommittedNode parent = null;
      if (parentLockNeeded) {
         needToCopyParent = acquireLock(ctx, parentFqn);
         // Ensure the node is in the context.
         parent = wrapAndPutInContext(ctx, parentFqn, needToCopyParent);
      }

      boolean needToCopyNode = acquireLock(ctx, fqn);

      // Ensure the node is in the context.
      ReadCommittedNode node = wrapAndPutInContext(ctx, fqn, needToCopyNode);

      if (node == null || node.isNullNode()) {
         // node does not exist; return an empty list since there is nothing to remove!
         return Collections.emptyList();
      } else {
         // update child ref on parent to point to child as this is now a copy.
         if (parentLockNeeded && (needToCopyNode || needToCopyParent)) {
            if (parent == null) throw new NodeNotExistsException("Parent node " + parentFqn + " does not exist!");
            parent.getDelegationTarget().addChild(node.getDelegationTarget());
         }

         // now deal with children.
         Map<Object, InternalNode<?, ?>> childMap = node.getDelegationTarget().getChildrenMap();
         List<Fqn> fqnsToBeRemoved = new LinkedList<Fqn>();
         fqnsToBeRemoved.add(fqn);

         if (!childMap.isEmpty()) {
            for (InternalNode n : childMap.values()) lockForWritingRecursive(n.getFqn(), ctx, fqnsToBeRemoved);
         }

         return fqnsToBeRemoved;
      }
   }

   /**
    * Locks a node recursively for writing, not creating if it doesn't exist.
    *
    * @param fqn     Fqn to lock
    * @param ctx     invocation context to add wrapped node to
    * @param fqnList fqnList to update - this list should not be null but should be initially empty and will be
    *                populated with a list of all Fqns locked in this call.
    * @throws InterruptedException if interrupted
    */
   @SuppressWarnings("unchecked")
   private void lockForWritingRecursive(Fqn fqn, InvocationContext ctx, List<Fqn> fqnList) throws InterruptedException {
      acquireLock(ctx, fqn); // lock node
      if (fqnList != null) fqnList.add(fqn);

      // now wrap and add to the context
      ReadCommittedNode rcn = wrapNodeForWriting(ctx, fqn, true, false, true, false, false);
      if (rcn != null) {
         rcn.markForUpdate(dataContainer, writeSkewCheck);
         Map<Object, InternalNode<?, ?>> children = rcn.getDelegationTarget().getChildrenMap();
         for (InternalNode child : children.values())
            lockForWritingRecursive(child, rcn.getInternalParent(), ctx, fqnList);
      }
   }

   /**
    * Identical to {@link #lockForWritingRecursive(org.jboss.cache.Fqn, org.jboss.cache.InvocationContext,
    * java.util.List)} except that it uses an {@link org.jboss.cache.InternalNode} instead of an {@link Fqn} - saves a
    * lookup.
    *
    * @param node    node to lock recursively
    * @param ctx     invocation context
    * @param fqnList list of Fqns to add to
    * @throws InterruptedException if interrupted
    */
   @SuppressWarnings("unchecked")
   private void lockForWritingRecursive(InternalNode node, InternalNode parent, InvocationContext ctx, List<Fqn> fqnList) throws InterruptedException {
      Fqn fqn = node.getFqn();
      acquireLock(ctx, fqn); // lock node
      if (fqnList != null) fqnList.add(fqn);

      // now wrap and add to the context
      NodeSPI rcn = wrapNodeForWriting(ctx, node, parent);
      if (rcn != null) {
         rcn.markForUpdate(dataContainer, writeSkewCheck);
         Map<Object, InternalNode<?, ?>> children = node.getChildrenMap();
         for (InternalNode child : children.values()) lockForWritingRecursive(child, node, ctx, fqnList);
      }
   }


   /**
    * Wraps a node and puts it in the context, optionally copying the node for updating if <tt>forUpdate</tt> is
    * <tt>true</tt>. If the node is already in the context, a new wrapped node is not created, but the existing one is
    * still checked for changes and potentially marked for update if <tt>forUpdate</tt> is <tt>true</tt>.
    *
    * @param ctx       invocation context to add node to
    * @param fqn       fqn of node to add
    * @param forUpdate if true, the wrapped node is marked for update before adding to the context.
    * @return the ReadCommittedNode wrapper, or null if the node does not exist.
    */
   @SuppressWarnings("unchecked")
   private ReadCommittedNode wrapAndPutInContext(InvocationContext ctx, Fqn fqn, boolean forUpdate) {
      ReadCommittedNode node = (ReadCommittedNode) ctx.lookUpNode(fqn);
      if (node == null || node.isNullNode()) {
         InternalNode[] nodes = dataContainer.peekInternalNodeAndDirectParent(fqn, false);
         node = nodeFactory.createWrappedNodeForRemoval(fqn, nodes[0], nodes[1]);
         ctx.putLookedUpNode(fqn, node);
      }

      // node could be null if using read-committed
      if (forUpdate && node != null && !node.isChanged()) node.markForUpdate(dataContainer, writeSkewCheck);

      return node;
   }

   /**
    * An overloaded version of {@link #isParentLockNeeded(org.jboss.cache.Fqn, org.jboss.cache.InvocationContext)} which
    * takes in an {@link org.jboss.cache.InternalNode} instead of a {@link Fqn}.
    *
    * @param parent parent node to test
    * @return true if parent lock is needed, false otherwise.
    */
   private boolean isParentLockNeeded(InternalNode parent) {
      return lockParentForChildInsertRemove || (parent != null && parent.isLockForChildInsertRemove());
   }

   /**
    * Tests if locking the parent is necessary when locking a specific node.
    *
    * @param parent Fqn of parent node to check
    * @param ctx    invocation context
    * @return true if parent lock is needed, false otherwise.
    */
   private boolean isParentLockNeeded(Fqn parent, InvocationContext ctx) {
      ReadCommittedNode parentNodeTmp = (ReadCommittedNode) ctx.lookUpNode(parent);
      InternalNode in = parentNodeTmp == null ? dataContainer.peekInternalNode(parent, true) : parentNodeTmp.getDelegationTarget();
      return isParentLockNeeded(in);
   }
}
