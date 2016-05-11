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
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.transaction.GlobalTransaction;

import javax.transaction.Transaction;

/**
 * Always at the end of the chain, directly in front of the cache. Simply calls into the cache using reflection.
 * If the call resulted in a modification, add the Modification to the end of the modification list
 * keyed by the current transaction.
 * <p/>
 * Although always added to the end of an optimistically locked chain as well, calls should not make it down to
 * this interceptor unless it is a call the OptimisticNodeInterceptor knows nothing about.
 *
 * @author Bela Ban
 *
 */
public class CallInterceptor extends CommandInterceptor
{
   private boolean notOptimisticLocking;

   @Start
   protected void start()
   {
      notOptimisticLocking = configuration.getNodeLockingScheme() != NodeLockingScheme.OPTIMISTIC;
   }

   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      if (trace) log.trace("Suppressing invocation of method handlePrepareCommand.");
      return null;
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      if (trace) log.trace("Suppressing invocation of method handleOptimisticPrepareCommand.");
      return null;
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      if (trace) log.trace("Suppressing invocation of method handleCommitCommand.");
      return null;
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      if (trace) log.trace("Suppressing invocation of method handleRollbackCommand.");
      return null;
   }

   @Override
   public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      if (trace) log.trace("Executing command: " + command + ".");
      return invokeCommand(ctx, command);
   }

   private Object invokeCommand(InvocationContext ctx, ReplicableCommand command)
         throws Throwable
   {
      Object retval;
      try
      {
         retval = command.perform(ctx);
      }
      catch (Throwable t)
      {
         Transaction tx = ctx.getTransaction();
         if (ctx.isValidTransaction())
         {
            tx.setRollbackOnly();
         }
         throw t;
      }
      return retval;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      return handleAlterCacheMethod(ctx, command);
   }

   /**
    * only add the modification to the modification list if we are using pessimistic locking.
    * Optimistic locking calls *should* not make it this far down the interceptor chain, but just
    * in case a method has been invoked that the OptimisticNodeInterceptor knows nothing about, it will
    * filter down here.
    */
   private Object handleAlterCacheMethod(InvocationContext ctx, WriteCommand command)
         throws Throwable
   {
      Object result = invokeCommand(ctx, command);
      if (notOptimisticLocking && ctx.isValidTransaction())
      {
         GlobalTransaction gtx = ctx.getGlobalTransaction();
         if (gtx == null)
         {
            if (log.isDebugEnabled())
            {
               log.debug("didn't find GlobalTransaction for " + ctx.getTransaction() + "; won't add modification to transaction list");
            }
         }
         else
         {
            ctx.getTransactionContext().addModification(command);
         }
      }
      return result;
   }
}
