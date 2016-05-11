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

import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Modification;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.AbstractVisitor;
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
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.base.SkipCheckChainedInterceptor;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Writes modifications back to the store on the way out: stores modifications back
 * through the CacheLoader, either after each method call (no TXs), or at TX commit.
 *
 * @author Bela Ban
 *
 */
public class CacheStoreInterceptor extends SkipCheckChainedInterceptor
{
   private CacheLoaderConfig loaderConfig = null;
   protected TransactionManager txMgr = null;
   private HashMap txStores = new HashMap();
   private long cacheStores = 0;
   CacheLoader loader;
   private CacheLoaderManager loaderManager;
   private boolean statsEnabled;

   public CacheStoreInterceptor()
   {
      log = LogFactory.getLog(getClass());
      trace = log.isTraceEnabled();
   }

   @Inject
   protected void init(CacheLoaderManager loaderManager, TransactionManager txManager, CacheLoaderConfig clConfig)
   {
      // never inject a CacheLoader at this stage - only a CacheLoaderManager, since the CacheLoaderManager only creates a CacheLoader instance when it @Starts.
      this.loaderManager = loaderManager;
      this.loaderConfig = clConfig;
      txMgr = txManager;
   }

   @Start
   protected void start()
   {
      // this should only happen after the CacheLoaderManager has started, since the CacheLoaderManager only creates the CacheLoader instance in its @Start method.
      loader = loaderManager.getCacheLoader();
      this.setStatisticsEnabled(configuration.getExposeManagementStatistics());
   }

   /**
    * if this is a shared cache loader and the call is of remote origin, pass up the chain
    */
   @Override
   public boolean skipInterception(InvocationContext ctx, VisitableCommand command)
   {
      if ((!ctx.isOriginLocal() && loaderConfig.isShared()) || ctx.getOptionOverrides().isSuppressPersistence())
      {
         if (trace)
         {
            log.trace("Passing up method call and bypassing this interceptor since the cache loader is shared and this call originated remotely.");
         }
         return true;
      }
      return false;
   }

   @Override
   protected Object handleCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      if (inTransaction())
      {
         if (ctx.getTransactionContext().hasAnyModifications())
         {
            // this is a commit call.
            GlobalTransaction gtx = command.getGlobalTransaction();
            if (trace) log.trace("Calling loader.commit() for gtx " + gtx);
            try
            {
               loader.commit(gtx);
            }
            finally
            {
               if (getStatisticsEnabled())
               {
                  Integer puts = (Integer) txStores.get(gtx);
                  if (puts != null)
                  {
                     cacheStores = cacheStores + puts;
                  }
                  txStores.remove(gtx);
               }
            }
            return invokeNextInterceptor(ctx, command);
         }
         else
         {
            if (trace) log.trace("Commit called with no modifications; ignoring.");
         }
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      if (inTransaction())
      {
         if (trace) log.trace("transactional so don't put stuff in the cloader yet.");
         if (ctx.getTransactionContext().hasAnyModifications())
         {
            GlobalTransaction gtx = command.getGlobalTransaction();
            // this is a rollback method
            try
            {
               loader.rollback(gtx);
            }
            catch (Exception e)
            {
               log.info("Problems rolling back transaction " + gtx + " on cache loader.  Perhaps the prepare phase hasn't been initiated on this loader?", e);
            }
            if (getStatisticsEnabled()) txStores.remove(gtx);
         }
         else
         {
            if (trace) log.trace("Rollback called with no modifications; ignoring.");
         }
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      return handlePrepareCommand(ctx, command);
   }

   @Override
   protected Object handlePrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      if (inTransaction())
      {
         if (trace) log.trace("transactional so don't put stuff in the cloader yet.");
         prepareCacheLoader(command.getGlobalTransaction(), ctx.getTransactionContext(), command.isOnePhaseCommit());
      }
      return invokeNextInterceptor(ctx, command);
   }

   /**
    * remove() methods need to be applied to the CacheLoader before passing up the call: a listener might
    * access an element just removed, causing the CacheLoader to *load* the element before *removing* it.
    */
   @Override
   protected Object handleRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      if (!inTransaction())
      {
         loader.remove(command.getFqn());
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      if (!inTransaction())
      {
         Object returnValue = loader.remove(command.getFqn(), command.getKey());
         invokeNextInterceptor(ctx, command);
         return returnValue;
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleRemoveDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      if (!inTransaction())
      {
         loader.removeData(command.getFqn());
         // we need to mark this node as data loaded
         NodeSPI n = ctx.lookUpNode(command.getFqn());
         if (n != null)
         {
            n.setDataLoaded(true);
         }
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      Object returnValue = invokeNextInterceptor(ctx, command);
      if (inTransaction())
      {
         return returnValue;
      }
      Fqn newNodeFqn = Fqn.fromRelativeElements(command.getTo(), command.getFqn().getLastElement());
      recursiveMove(command.getFqn(), newNodeFqn);
      loader.remove(command.getFqn());
      return returnValue;
   }

   @Override
   protected Object handlePutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      Object returnValue = invokeNextInterceptor(ctx, command);
      if (inTransaction())
      {
         return returnValue;
      }
      if (command.isErase())
      {
         loader.removeData(command.getFqn());
      }
      storeStateForPutDataMap(command.getFqn(), ctx);
      if (getStatisticsEnabled()) cacheStores++;

      return returnValue;
   }

   protected void storeStateForPutDataMap(Fqn f, InvocationContext ctx) throws Exception
   {
      loader.put(f, ctx.lookUpNode(f).getDelegationTarget().getData());
   }

   @Override
   protected Object handlePutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      Object returnValue = invokeNextInterceptor(ctx, command);
      if (inTransaction())
      {
         return returnValue;
      }
      returnValue = loader.put(command.getFqn(), command.getKey(), command.getValue());
      if (getStatisticsEnabled()) cacheStores++;

      return returnValue;
   }

   @Override
   protected Object handlePutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handlePutKeyValueCommand(ctx, command);
   }

   protected boolean inTransaction() throws SystemException
   {
      return txMgr != null && txMgr.getTransaction() != null;
   }

   private void recursiveMove(Fqn fqn, Fqn newFqn) throws Exception
   {
      loader.put(newFqn, loader.get(fqn));
      //recurse
      Set childrenNames = loader.getChildrenNames(fqn);
      if (childrenNames != null)
      {
         for (Object child : childrenNames)
         {
            recursiveMove(Fqn.fromRelativeElements(fqn, child), Fqn.fromRelativeElements(newFqn, child));
         }
      }
   }

   protected StoreModificationsBuilder prepareCacheLoader(GlobalTransaction gtx, TransactionContext transactionContext, boolean onePhase) throws Throwable
   {
      if (transactionContext == null)
      {
         throw new Exception("transactionContext for transaction " + gtx + " not found in transaction table");
      }
      List<WriteCommand> modifications = transactionContext.getModifications();
      if (modifications.size() == 0)
      {
         if (trace) log.trace("Transaction has not logged any modifications!");
         return null;
      }
      if (trace) log.trace("Cache loader modification list: " + modifications);
      StoreModificationsBuilder modsBuilder = new StoreModificationsBuilder(getStatisticsEnabled());
      for (WriteCommand cacheCommand : modifications)
      {
         cacheCommand.acceptVisitor(null, modsBuilder);
      }
      if (trace)
      {
         log.trace("Converted method calls to cache loader modifications.  List size: " + modsBuilder.modifications.size());
      }
      if (modsBuilder.modifications.size() > 0)
      {
         loader.prepare(gtx, modsBuilder.modifications, onePhase);

         if (getStatisticsEnabled() && modsBuilder.putCount > 0 && !onePhase)
         {
            txStores.put(gtx, modsBuilder.putCount);
         }
      }
      return modsBuilder;
   }

   public static class StoreModificationsBuilder extends AbstractVisitor
   {

      boolean generateStatistics;

      int putCount;

      Set<Fqn> affectedFqns = new HashSet<Fqn>();

      List<Modification> modifications = new ArrayList<Modification>();

      public StoreModificationsBuilder(boolean generateStatistics)
      {
         this.generateStatistics = generateStatistics;
      }

      @Override
      public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
      {
         if (generateStatistics) putCount++;
         modifications.add(new Modification(Modification.ModificationType.PUT_DATA, command.getFqn(), command.getData()));
         affectedFqns.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
      {
         if (generateStatistics) putCount++;
         modifications.add(new Modification(Modification.ModificationType.PUT_KEY_VALUE, command.getFqn(),
               command.getKey(), command.getValue()));
         affectedFqns.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
      {
         modifications.add(new Modification(Modification.ModificationType.REMOVE_KEY_VALUE, command.getFqn(), command.getKey()));
         affectedFqns.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
      {
         modifications.add(new Modification(Modification.ModificationType.REMOVE_DATA, command.getFqn()));
         affectedFqns.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
      {
         modifications.add(new Modification(Modification.ModificationType.REMOVE_NODE, command.getFqn()));
         affectedFqns.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
      {
         Fqn moveFrom = command.getFqn();
         affectedFqns.add(command.getFqn());
         affectedFqns.add(moveFrom);
         Modification mod = new Modification(Modification.ModificationType.MOVE, moveFrom, command.getTo());
         modifications.add(mod);
         return null;
      }

   }

   @ManagedOperation
   public void resetStatistics()
   {
      cacheStores = 0;
   }

   @ManagedOperation
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = new HashMap<String, Object>();
      retval.put("CacheLoaderStores", cacheStores);
      return retval;
   }

   @ManagedAttribute
   public boolean getStatisticsEnabled()
   {
      return statsEnabled;
   }

   @ManagedAttribute
   public void setStatisticsEnabled(boolean enabled)
   {
      this.statsEnabled = enabled;
   }

   @ManagedAttribute(description = "number of cache loader stores")
   public long getCacheLoaderStores()
   {
      return cacheStores;
   }

}
