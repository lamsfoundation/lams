/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.annotations.Compat;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.transaction.GlobalTransaction;

import javax.transaction.Transaction;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Compat
@Deprecated
public class LegacyCacheStoreInterceptor extends CacheStoreInterceptor
{
   /**
    * Used with optimistic locking to persist version information on Fqns that have changed.
    */
   private Map<GlobalTransaction, Set<Fqn>> preparingTxs;
   private boolean optimistic;

   @Start
   void checkOptimistic()
   {
      optimistic = configuration.getNodeLockingScheme() == Configuration.NodeLockingScheme.OPTIMISTIC;
      if (optimistic) preparingTxs = new ConcurrentHashMap<GlobalTransaction, Set<Fqn>>();

   }

   @Override
   protected void storeStateForPutDataMap(Fqn f, InvocationContext ctx) throws Exception
   {
      loader.put(f, ctx.lookUpNode(f).getDataDirect());
   }

   @Override
   protected Object handleOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      if (inTransaction())
      {
         if (trace) log.trace("transactional so don't put stuff in the cloader yet.");
         GlobalTransaction gtx = command.getGlobalTransaction();
         StoreModificationsBuilder smb = prepareCacheLoader(gtx, ctx.getTransactionContext(), command.isOnePhaseCommit());
         if (smb != null && !smb.modifications.isEmpty()) preparingTxs.put(gtx, smb.affectedFqns);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      try
      {
         return super.handleRollbackCommand(ctx, command);
      }
      finally
      {
         if (optimistic)
         {
            GlobalTransaction gtx = ctx.getGlobalTransaction();
            preparingTxs.remove(gtx);
         }
      }
   }

   @Override
   protected Object handleCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      Object returnValue = super.handleCommitCommand(ctx, command);

      // persist additional internal state, if any, and then clean up internal resources
      // specifically for optimistic locking to store versioning info AFTER a tx has committed.  Hacky.
      // note - do NOT do this in a finally block.  If the commit fails we shouldn't do this.
      if (optimistic)
      {
         // if the commit fails, preparingTxs will be cleaned up in a call to handleRollbackCommand()
         Set<Fqn> affectedFqns = preparingTxs.remove(ctx.getGlobalTransaction());
         if (affectedFqns != null && !affectedFqns.isEmpty())
         {
            storeInternalState(affectedFqns, ctx);
         }
      }
      return returnValue;
   }

   @SuppressWarnings("unchecked")
   private void storeInternalState(Set<Fqn> affectedFqns, InvocationContext ctx) throws Exception
   {
      // we need to suspend any txs here since they would be in the tx-committed state and if the loader attempts to
      // use JTA (E.g., a JDBC CL using connections from a tx aware datasource) it will fail since the tx is in an
      // illegal state to perform writes.  See JBCACHE-1408.
      Transaction tx = txMgr.suspend();
      try
      {
         for (Fqn f : affectedFqns)
         {
            // NOT going to store tombstones!!
            NodeSPI n = ctx.lookUpNode(f);
            if (n != null && !n.isDeleted())
            {
               Map<Object, Object> internalState = n.getInternalState(false);
               loader.put(f, internalState);
            }
         }
      }
      finally
      {
         txMgr.resume(tx);
      }
   }
}
