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
package org.jboss.cache.interceptors;

import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.PrePostProcessingCommandInterceptor;
import org.jboss.cache.lock.LockManager;
import org.jboss.cache.mvcc.MVCCNodeHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Interceptor to implement <a href="http://wiki.jboss.org/wiki/JBossCacheMVCC">MVCC</a> functionality.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see <a href="http://wiki.jboss.org/wiki/JBossCacheMVCC">MVCC designs</a>
 * @since 3.0
 */
public class MVCCLockingInterceptor extends PrePostProcessingCommandInterceptor
{
   LockManager lockManager;
   DataContainer dataContainer;
   MVCCNodeHelper helper;

   @Inject
   public void setDependencies(LockManager lockManager, DataContainer dataContainer, MVCCNodeHelper helper)
   {
      this.lockManager = lockManager;
      this.dataContainer = dataContainer;
      this.helper = helper;
   }

   @Override
   protected boolean doBeforeCall(InvocationContext ctx, VisitableCommand command)
   {
      if (ctx.getOptionOverrides().isSuppressLocking() && log.isWarnEnabled())
      {
         log.warn("Lock suppression not supported with MVCC!");
      }
      return true;
   }

   @Override
   public Object handlePutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      helper.wrapNodeForWriting(ctx, command.getFqn(), true, true, false, false, false); // get the node and stick it in the context.
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handlePutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      helper.wrapNodeForWriting(ctx, command.getFqn(), true, true, false, false, false); // get the node and stick it in the context.
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handlePutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      ctx.getOptionOverrides().setLockAcquisitionTimeout(0);
      helper.wrapNodeForWriting(ctx, command.getFqn(), true, true, false, false, false); // get the node and stick it in the context.
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      helper.wrapNodesRecursivelyForRemoval(ctx, command.getFqn());
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      helper.wrapNodeForWriting(ctx, command.getFqn(), true, false, false, false, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      // set lock acquisition timeout to 0 - we need to fail fast.
      ctx.getOptionOverrides().setLockAcquisitionTimeout(0);
      if (command.isRecursive())
      {
         handleRecursiveEvict(ctx, command);
      }
      else
      {
         handleNonrecursiveEvict(ctx, command);
      }

      return invokeNextInterceptor(ctx, command);
   }

   @SuppressWarnings("unchecked")
   private void handleRecursiveEvict(InvocationContext ctx, EvictCommand command) throws InterruptedException
   {
      List<Fqn> fqnsToEvict;
      if (command.getFqn().isRoot())
      {
         // if this is the root node, do not attempt to lock this for writing but instead just get all direct children of root.
         Map<Object, InternalNode> children = dataContainer.peekInternalNode(Fqn.ROOT, false).getChildrenMap();
         if (!children.isEmpty())
         {
            fqnsToEvict = new LinkedList<Fqn>();
            // lock recursively.
            for (InternalNode child : children.values())
            {
               fqnsToEvict.addAll(helper.wrapNodesRecursivelyForRemoval(ctx, child.getFqn()));
            }
         }
         else
         {
            fqnsToEvict = Collections.emptyList();
         }
      }
      else
      {
         // lock current node recursively.
         fqnsToEvict = helper.wrapNodesRecursivelyForRemoval(ctx, command.getFqn());
      }

      // set these in the evict command so that the command is aware of what needs to be evicted.
      command.setNodesToEvict(fqnsToEvict);
   }

   @SuppressWarnings("unchecked")
   private void handleNonrecursiveEvict(InvocationContext ctx, EvictCommand command) throws InterruptedException
   {
      if (command.getFqn().isRoot())
      {
         // if this is the root node, do not attempt to lock this for writing but instead just get all direct children of root.
         Map<Object, InternalNode> children = dataContainer.peekInternalNode(Fqn.ROOT, false).getChildrenMap();
         if (!children.isEmpty())
         {
            for (InternalNode child : children.values())
            {
               helper.wrapNodeForWriting(ctx, child.getFqn(), true, false, false, true, true);
            }
         }
      }
      else
      {
         // just wrap the node for writing.  Do not create if absent.
         helper.wrapNodeForWriting(ctx, command.getFqn(), true, false, false, true, true);
      }
   }

   @Override
   public Object handleInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      // this should be handled the same as a recursive evict command.
      ctx.getOptionOverrides().setLockAcquisitionTimeout(0);
      if (!command.getFqn().isRoot()) helper.wrapNodesRecursivelyForRemoval(ctx, command.getFqn());
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      helper.wrapNodeForWriting(ctx, command.getFqn(), true, false, false, false, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      // Nodes we need to get WLs for:

      // 1) node we are moving FROM, and its parent and ALL children.  Same as removeNode.
      List<Fqn> nodeAndChildren = helper.wrapNodesRecursivelyForRemoval(ctx, command.getFqn());

      Fqn newParent = command.getTo();
      Fqn oldParent = command.getFqn().getParent();

      // 2)  The new parent.
      helper.wrapNodeForWriting(ctx, newParent, true, true, false, false, false);

      if (!oldParent.equals(newParent))
      {
         // the nodeAndChildren list contains all child nodes, including the node itself.
         // 3)  now obtain locks on the new places these new nodes will occupy.
         for (Fqn f : nodeAndChildren)
         {
            Fqn newChildFqn = f.replaceAncestor(oldParent, newParent);
            helper.wrapNodeForWriting(ctx, newChildFqn, true, true, true, false, false);
         }
      }

      // now pass up the chain.
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      helper.wrapNodeForReading(ctx, command.getFqn(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object handleRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      Object retval = null;
      try
      {
         retval = invokeNextInterceptor(ctx, command);
      }
      finally
      {
         transactionalCleanup(false, ctx);
      }
      return retval;
   }

   @Override
   public Object handleCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      Object retval = null;
      try
      {
         retval = invokeNextInterceptor(ctx, command);
      }
      finally
      {
         transactionalCleanup(true, ctx);
      }
      return retval;
   }

   @Override
   public Object handlePrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      Object retval = null;
      try
      {
         retval = invokeNextInterceptor(ctx, command);
      }
      finally
      {
         if (command.isOnePhaseCommit()) transactionalCleanup(true, ctx);
      }
      return retval;
   }

   @SuppressWarnings("unchecked")
   protected void doAfterCall(InvocationContext ctx, VisitableCommand command)
   {
      // for non-transactional stuff.
      if (ctx.getTransactionContext() == null)
      {
         List<Fqn> locks;
         if (!(locks = ctx.getLocks()).isEmpty())
         {
            cleanupLocks(locks, ctx, Thread.currentThread(), true);
         }
         else
         {
            if (trace) log.trace("Nothing to do since there are no modifications in scope.");
         }
      }
      else
      {
         if (trace) log.trace("Nothing to do since there is a transaction in scope.");
      }
   }

   private void cleanupLocks(List<Fqn> locks, InvocationContext ctx, Object owner, boolean commit)
   {
      // clean up.
      // unlocking needs to be done in reverse order.
      ListIterator<Fqn> it = locks.listIterator(locks.size());

      if (commit)
      {
         while (it.hasPrevious())
         {
            Fqn f = it.previous();
            NodeSPI rcn = ctx.lookUpNode(f);
            // could be null with read-committed
            if (rcn != null) rcn.commitUpdate(ctx, dataContainer);
            // and then unlock
            if (trace) log.trace("Releasing lock on [" + f + "] for owner " + owner);
            lockManager.unlock(f, owner);
         }
      }
      else
      {
         while (it.hasPrevious())
         {
            Fqn f = it.previous();
            NodeSPI rcn = ctx.lookUpNode(f);
            // could be null with read-committed
            if (rcn != null) rcn.rollbackUpdate();
            // and then unlock
            if (trace) log.trace("Releasing lock on [" + f + "] for owner " + owner);
            lockManager.unlock(f, owner);
         }
      }
      ctx.clearLocks();
   }

   @SuppressWarnings("unchecked")
   private void transactionalCleanup(boolean commit, InvocationContext ctx)
   {
      if (ctx.getTransactionContext() != null)
      {
         List<Fqn> locks = ctx.getTransactionContext().getLocks();
         if (!locks.isEmpty()) cleanupLocks(locks, ctx, ctx.getGlobalTransaction(), commit);
      }
      else
      {
         throw new IllegalStateException("Attempting to do a commit or rollback but there is no transactional context in scope. " + ctx);
      }
   }
}
