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
import org.jboss.cache.RPCManager;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionTable;

import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * Always place this interceptor at the start of the interceptor chain to ensure invocation contexts and set up and cleaned up correctly.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public class InvocationContextInterceptor extends BaseTransactionalContextInterceptor
{
   private RPCManager rpcManager;

   @Inject
   public void setDependencies(RPCManager rpcManager)
   {
      this.rpcManager = rpcManager;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), false);
   }

   @Override
   public Object visitExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      return handleAll(ctx, command, null, false);
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), false);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), false);
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), false);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), false);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), false);
   }

   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), true);
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), true);
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), true);
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      return handleAll(ctx, command, command.getGlobalTransaction(), true);
   }

   @Override
   public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      return handleAll(ctx, command, null, false);
   }

   @SuppressWarnings("deprecation")
   private Object handleAll(InvocationContext ctx, VisitableCommand command, GlobalTransaction gtx, boolean scrubContextOnCompletion) throws Throwable
   {
      Option optionOverride = ctx.getOptionOverrides();
      boolean suppressExceptions = false;
      Transaction suspendedTransaction = null;
      boolean resumeSuspended = false;

      if (trace) log.trace("Invoked with command " + command + " and InvocationContext [" + ctx + "]");

      try
      {
         if (txManager != null)
         {
            Transaction tx = getTransaction();
            GlobalTransaction realGtx = getGlobalTransaction(tx, gtx);
            if (tx == null && realGtx != null && realGtx.isRemote()) tx = txTable.getLocalTransaction(gtx);
            setTransactionalContext(tx, realGtx, null, ctx);
         }
         else
         {
            setTransactionalContext(null, null, null, ctx);
         }

         if (optionOverride != null && optionOverride.isFailSilently())
         {
            log.debug("FAIL_SILENTLY Option is present - suspending any ongoing transaction.");
            suppressExceptions = true;
            if (ctx.getTransaction() != null)
            {
               suspendedTransaction = txManager.suspend();
               setTransactionalContext(null, null, null, ctx);
               if (trace) log.trace("Suspending transaction " + suspendedTransaction);
               resumeSuspended = true;
            }
            else
            {
               if (trace) log.trace("No ongoing transaction to suspend");
            }
         }


         Object retval;
         try
         {
            return invokeNextInterceptor(ctx, command);
         }
         catch (Throwable th)
         {
            retval = th;
            // if fail silently return a null
            if (suppressExceptions) return null;
            Throwable t = (Throwable) retval;
            if (t instanceof RuntimeException && t.getCause() != null)
            {
               throw t.getCause();
            }
            else
            {
               throw t;
            }
         }
         // assume we're the first interceptor in the chain.  Handle the exception-throwing.
      }
      finally
      {
         // TODO: scope upgrading should happen transparently
         /*
          * we should scrub txs after every call to prevent race conditions
          * basically any other call coming in on the same thread and hijacking any running tx's
          * was highlighted in JBCACHE-606
          */
         if (scrubContextOnCompletion) setTransactionalContext(null, null, null, ctx);

         // clean up any invocation-scope options set up
         if (trace) log.trace("Resetting invocation-scope options");
         ctx.getOptionOverrides().reset();

         // make sure we wipe fqns loaded in the context otherwise this will leak.
         // TODO: This should ideally be done by calling InvocationContext.reset() on exit
         ctx.setFqnsLoaded(null);

         // if this is a prepare, opt prepare or
         if (resumeSuspended)
         {
            txManager.resume(suspendedTransaction);
         }
         else if (ctx.getTransaction() != null && (TransactionTable.isValid(ctx.getTransaction())))
         {
            copyInvocationScopeOptionsToTxScope(ctx);
         }

         // reset the context to prevent leakage of internals
         ctx.setCommand(null);
         ctx.setMethodCall(null);

         // TODO: Calling ctx.reset() here breaks stuff.  Check whether this is just becuse UTs expect stuff in the ctx or whether this really breaks functionality.
//         ctx.reset();
         // instead, for now, just wipe contents of the looked up node map
         ctx.clearLookedUpNodes();
      }
   }

   private GlobalTransaction getGlobalTransaction(Transaction tx, GlobalTransaction gtx)
   {
      if (gtx == null) gtx = txTable.getCurrentTransaction(tx, false);
      if (gtx != null) gtx.setRemote(isRemoteGlobalTx(gtx));
      return gtx;
   }

   private Transaction getTransaction() throws SystemException
   {
      // this creates a context if one did not exist.
      if (txManager == null)
      {
         if (trace) log.trace("no transaction manager configured, setting tx as null.");
         return null;
      }
      else
      {
         return txManager.getTransaction();
      }
   }

   /**
    * Tests if a global transaction originated from a different cache in the cluster
    *
    * @param gtx
    * @return true if the gtx is remote, false if it originated locally.
    */
   private boolean isRemoteGlobalTx(GlobalTransaction gtx)
   {
      return gtx != null && (gtx.getAddress() != null) && (!gtx.getAddress().equals(rpcManager.getLocalAddress()));
   }
}
