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
import org.jboss.cache.commands.AbstractVisitor;
import org.jboss.cache.commands.VersionedDataCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.OptimisticTransactionContext;
import org.jboss.cache.transaction.TransactionContext;

import javax.transaction.Transaction;
import java.util.List;

/**
 * A new interceptor to simplify functionality in the {@link org.jboss.cache.interceptors.TxInterceptor}.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticTxInterceptor extends TxInterceptor
{
   protected final ModificationsReplayVisitor replayVisitor = new ModificationsReplayVisitor();

   public OptimisticTxInterceptor()
   {
      optimistic = true;
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      // nothing really different from a pessimistic prepare command.
      return visitPrepareCommand(ctx, command);
   }

   @Override
   public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      try
      {
         Transaction tx = ctx.getTransaction();
         boolean implicitTransaction = tx == null;
         if (implicitTransaction)
         {
            tx = createLocalTx();
            // we need to attach this tx to the InvocationContext.
            ctx.setTransaction(tx);
         }

         try
         {
            Object retval = attachGtxAndPassUpChain(ctx, command);
            if (implicitTransaction)
            {
               copyInvocationScopeOptionsToTxScope(ctx);
               copyForcedCacheModeToTxScope(ctx);
               txManager.commit();
            }
            return retval;
         }
         catch (Throwable t)
         {
            if (implicitTransaction)
            {
               log.warn("Rolling back, exception encountered", t);
               try
               {
                  copyInvocationScopeOptionsToTxScope(ctx);
                  copyForcedCacheModeToTxScope(ctx);
                  txManager.rollback();
               }
               catch (Throwable th)
               {
                  log.warn("Roll back failed encountered", th);
               }
               throw t;
            }
         }
      }
      catch (Throwable th)
      {
         ctx.throwIfNeeded(th);
      }

      return null;
   }

   private void copyForcedCacheModeToTxScope(InvocationContext ctx)
   {
      Option optionOverride = ctx.getOptionOverrides();
      if (optionOverride != null
            && (optionOverride.isForceAsynchronous() || optionOverride.isForceSynchronous()))
      {
         TransactionContext transactionContext = ctx.getTransactionContext();
         if (transactionContext != null)
         {
            if (optionOverride.isForceAsynchronous())
               transactionContext.setForceAsyncReplication(true);
            else
               transactionContext.setForceSyncReplication(true);
         }
      }
   }

   @Override
   protected PrepareCommand buildPrepareCommand(GlobalTransaction gtx, List modifications, boolean onePhaseCommit)
   {
      // optimistic locking NEVER does one-phase prepares.
      return commandsFactory.buildOptimisticPrepareCommand(gtx, modifications, rpcManager.getLocalAddress(), false);
   }

   /**
    * Replays modifications by passing them up the interceptor chain.
    *
    * @throws Throwable
    */
   @Override
   protected void replayModifications(InvocationContext ctx, Transaction ltx, PrepareCommand command) throws Throwable
   {
      if (log.isDebugEnabled()) log.debug("Handling optimistic remote prepare " + ctx.getGlobalTransaction());

      // invoke all modifications by passing them up the chain, setting data versions first.
      try
      {
         replayVisitor.visitCollection(ctx, command.getModifications());
      }
      catch (Throwable t)
      {
         log.error("Prepare failed!", t);
         throw t;
      }
   }

   @Override
   protected void cleanupStaleLocks(InvocationContext ctx) throws Throwable
   {
      super.cleanupStaleLocks(ctx);
      TransactionContext transactionContext = ctx.getTransactionContext();
      if (transactionContext != null)
      {
         ((OptimisticTransactionContext) transactionContext).getTransactionWorkSpace().clearNodes();
      }
   }

   private class ModificationsReplayVisitor extends AbstractVisitor
   {
      @Override
      public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
      {
         Object result = invokeNextInterceptor(ctx, command);
         assertTxIsStillValid(ctx.getTransaction());
         return result;
      }

      @Override
      public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
      {
         return handleDataVersionCommand(ctx, command);
      }

      @Override
      public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
      {
         return handleDataVersionCommand(ctx, command);
      }

      @Override
      public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
      {
         return handleDataVersionCommand(ctx, command);
      }

      @Override
      public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
      {
         return handleDataVersionCommand(ctx, command);
      }

      @Override
      public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
      {
         return handleDataVersionCommand(ctx, command);
      }

      private Object handleDataVersionCommand(InvocationContext ctx, VersionedDataCommand command) throws Throwable
      {
         Option originalOption = ctx.getOptionOverrides();
         if (command.isVersioned())
         {
            Option option = new Option();
            option.setDataVersion(command.getDataVersion());
            ctx.setOptionOverrides(option);
         }
         Object retval;
         try
         {
            retval = invokeNextInterceptor(ctx, command);
            assertTxIsStillValid(ctx.getTransaction());
         }
         catch (Throwable t)
         {
            log.error("method invocation failed", t);
            throw t;
         }
         finally
         {
            ctx.setOptionOverrides(originalOption);
         }
         return retval;
      }
   }

}
