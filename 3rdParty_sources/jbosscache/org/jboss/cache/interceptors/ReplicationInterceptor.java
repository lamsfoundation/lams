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

import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;

/**
 * Takes care of replicating modifications to other nodes in a cluster. Also
 * listens for prepare(), commit() and rollback() messages which are received
 * 'side-ways' (see docs/design/Refactoring.txt).
 *
 * @author Bela Ban
 *
 */
public class ReplicationInterceptor extends BaseRpcInterceptor
{

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      if (!skipReplicationOfTransactionMethod(ctx))
         replicateCall(ctx, command, configuration.isSyncCommitPhase(), ctx.getOptionOverrides(), true);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      TransactionContext transactionContext = ctx.getTransactionContext();
      if (transactionContext.hasLocalModifications())
      {
         PrepareCommand replicablePrepareCommand = command.copy(); // makre sure we remove any "local" transactions
         replicablePrepareCommand.removeModifications(transactionContext.getLocalModifications());
         command = replicablePrepareCommand;
      }

      if (!skipReplicationOfTransactionMethod(ctx)) runPreparePhase(command, command.getGlobalTransaction(), ctx);
      return retVal;
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      if (!skipReplicationOfTransactionMethod(ctx) && !ctx.isLocalRollbackOnly())
      {
         replicateCall(ctx, command, configuration.isSyncRollbackPhase(), ctx.getOptionOverrides());
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      boolean local = isLocalModeForced(ctx);
      if (local && ctx.getTransaction() == null) return invokeNextInterceptor(ctx, command);
      if (isTransactionalAndLocal(ctx))
      {
         Object returnValue = invokeNextInterceptor(ctx, command);
         ctx.getTransactionContext().setForceAsyncReplication(true);
         if (local) ctx.getTransactionContext().addLocalModification(command);
         return returnValue;
      }
      else
      {
         return handleCrudMethod(ctx, command, true);
      }
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleCrudMethod(ctx, command, false);
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleCrudMethod(ctx, command, false);
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      return handleCrudMethod(ctx, command, false);
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleCrudMethod(ctx, command, false);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleCrudMethod(ctx, command, false);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleCrudMethod(ctx, command, false);
   }

   /**
    * If we are within one transaction we won't do any replication as replication would only be performed at commit time.
    * If the operation didn't originate locally we won't do any replication either.
    */
   private Object handleCrudMethod(InvocationContext ctx, VisitableCommand command, boolean forceAsync)
         throws Throwable
   {
      boolean local = isLocalModeForced(ctx);
      if (local && ctx.getTransaction() == null) return invokeNextInterceptor(ctx, command);
      // FIRST pass this call up the chain.  Only if it succeeds (no exceptions) locally do we attempt to replicate.
      Object returnValue = invokeNextInterceptor(ctx, command);
      if (ctx.getTransaction() == null && ctx.isOriginLocal())
      {
         if (trace)
         {
            log.trace("invoking method " + command.getClass().getSimpleName() + ", members=" + rpcManager.getMembers() + ", mode=" +
                  configuration.getCacheMode() + ", exclude_self=" + true + ", timeout=" +
                  configuration.getSyncReplTimeout());
         }

         replicateCall(ctx, command, !forceAsync && isSynchronous(ctx.getOptionOverrides()), ctx.getOptionOverrides());
      }
      else
      {
         if (local) ctx.getTransactionContext().addLocalModification((WriteCommand) command);
      }
      return returnValue;
   }

   /**
    * Calls prepare(GlobalTransaction,List,org.jgroups.Address,boolean)) in all members except self.
    * Waits for all responses. If one of the members failed to prepare, its return value
    * will be an exception. If there is one exception we rethrow it. This will mark the
    * current transaction as rolled back, which will cause the
    * afterCompletion(int) callback to have a status
    * of <tt>MARKED_ROLLBACK</tt>. When we get that call, we simply roll back the
    * transaction.<br/>
    * If everything runs okay, the afterCompletion(int)
    * callback will trigger the @link #runCommitPhase(GlobalTransaction)).
    * <br/>
    *
    * @throws Exception
    */
   protected void runPreparePhase(PrepareCommand prepareMethod, GlobalTransaction gtx, InvocationContext ctx) throws Throwable
   {
      boolean async = configuration.getCacheMode() == Configuration.CacheMode.REPL_ASYNC;
      if (trace)
      {
         log.trace("(" + rpcManager.getLocalAddress() + "): running remote prepare for global tx " + gtx + " with async mode=" + async);
      }

      // this method will return immediately if we're the only member (because exclude_self=true)
      replicateCall(ctx, prepareMethod, !async, ctx.getOptionOverrides());
   }
}
