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

import org.jboss.cache.CacheException;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.factories.annotations.Start;
import static org.jboss.cache.lock.LockType.READ;
import static org.jboss.cache.lock.LockType.WRITE;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.BatchModeTransactionManager;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;

/**
 * Locks nodes during transaction boundaries.  Only affects prepare/commit/rollback method calls; other method calls
 * are simply passed up the interceptor stack.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:stevew@jofti.com">Steve Woodcock (stevew@jofti.com)</a>
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticLockingInterceptor extends OptimisticInterceptor
{
   @Start
   void init()
   {
      if (txManager == null || txManager.getClass().equals(BatchModeTransactionManager.class))
         log.fatal("No transaction manager lookup class has been defined. Transactions cannot be used and thus OPTIMISTIC locking cannot be used!  Expect errors!!");
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      //try and acquire the locks - before passing on
      GlobalTransaction gtx = getGlobalTransaction(ctx);

      boolean succeeded = false;
      try
      {
         TransactionWorkspace<?, ?> workspace = getTransactionWorkspace(ctx);
         if (log.isDebugEnabled()) log.debug("Locking nodes in transaction workspace for GlobalTransaction " + gtx);

         for (WorkspaceNode workspaceNode : workspace.getNodes().values())
         {
            NodeSPI node = workspaceNode.getNode();

            boolean isWriteLockNeeded = workspaceNode.isDirty() || (workspaceNode.isChildrenModified() && (configuration.isLockParentForChildInsertRemove() || node.isLockForChildInsertRemove()));

            boolean acquired = lockManager.lockAndRecord(node, isWriteLockNeeded ? WRITE : READ, ctx);
            if (acquired)
            {
               if (trace) log.trace("Acquired lock on node " + node.getFqn());
            }
            else
            {
               throw new CacheException("Unable to acquire lock on node " + node.getFqn());
            }

         }

         // locks have acquired so lets pass on up
         Object retval = invokeNextInterceptor(ctx, command);
         succeeded = true;
         return retval;
      }
      catch (Throwable e)
      {
         succeeded = false;
         log.debug("Caught exception attempting to lock nodes ", e);
         //we have failed - set to rollback and throw exception
         throw e;
      }
      finally
      {
         if (!succeeded) unlock(ctx);
      }
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      return transactionFinalized(ctx, command);
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      return transactionFinalized(ctx, command);
   }

   private Object transactionFinalized(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      Object retval = null;
      // we need to let the stack run its commits or rollbacks first -
      // we unlock last - even if an exception occurs
      try
      {
         retval = invokeNextInterceptor(ctx, command);
      }
      finally
      {
         unlock(ctx);
      }
      return retval;
   }

   /**
    * Releases all locks held by the specified global transaction.
    *
    * @param ctx Invocation Context
    */
   private void unlock(InvocationContext ctx)
   {
      try
      {
         TransactionContext transactionContext = ctx.getTransactionContext();
         if (transactionContext != null)
         {
            lockManager.unlock(ctx);
         }
      }
      catch (Exception e)
      {
         // we have failed to unlock - now what?
         log.error("Failed to unlock nodes after a commit or rollback!  Locks are possibly in a very inconsistent state now!", e);
      }
   }

}
