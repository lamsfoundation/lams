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
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.transaction.TransactionTable;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * Class providing some base functionality around manipulating transactions and global transactions withing invocation contexts.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 */
public abstract class BaseTransactionalContextInterceptor extends CommandInterceptor
{
   protected TransactionTable txTable;
   protected TransactionManager txManager;

   @Inject
   void injectDependencies(TransactionTable txTable, TransactionManager txManager)
   {
      this.txManager = txManager;
      this.txTable = txTable;
   }

   protected void copyInvocationScopeOptionsToTxScope(InvocationContext ctx)
   {
      // notify the transaction tCtx that this override is in place.
      TransactionContext tCtx = txTable.get(ctx.getGlobalTransaction());
      if (tCtx != null)
      {
         Option txScopeOption = new Option();
         txScopeOption.setCacheModeLocal(ctx.getOptionOverrides() != null && ctx.getOptionOverrides().isCacheModeLocal());
         txScopeOption.setSkipCacheStatusCheck(ctx.getOptionOverrides() != null && ctx.getOptionOverrides().isSkipCacheStatusCheck());
         tCtx.setOption(txScopeOption);
      }
   }

   protected void setTransactionalContext(Transaction tx, GlobalTransaction gtx, TransactionContext tCtx, InvocationContext ctx)
   {
      if (trace)
      {
         log.trace("Setting up transactional context.");
         log.trace("Setting tx as " + tx + " and gtx as " + gtx);
      }
      ctx.setTransaction(tx);
      ctx.setGlobalTransaction(gtx);
      if (tCtx == null)
      {
         if (gtx != null)
         {
            ctx.setTransactionContext(txTable.get(gtx));
         }
         else if (tx == null)
         {
            // then nullify the transaction tCtx as well
            ctx.setTransactionContext(null);
         }
      }
      else
      {
         ctx.setTransactionContext(tCtx);
      }
   }

   /**
    * Returns true if transaction is rolling back, false otherwise
    */
   protected boolean isRollingBack(Transaction tx)
   {
      if (tx == null) return false;
      int status;
      try
      {
         status = tx.getStatus();
         return status == Status.STATUS_ROLLING_BACK || status == Status.STATUS_ROLLEDBACK;
      }
      catch (SystemException e)
      {
         log.error("failed getting transaction status", e);
         return false;
      }
   }
}
