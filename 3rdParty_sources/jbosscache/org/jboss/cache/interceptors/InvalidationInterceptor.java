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

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.AbstractVisitor;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.legacy.write.VersionedInvalidateCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DefaultDataVersion;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.OptimisticTransactionContext;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.transaction.TransactionTable;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This interceptor acts as a replacement to the replication interceptor when
 * the CacheImpl is configured with ClusteredSyncMode as INVALIDATE.
 * <p/>
 * The idea is that rather than replicating changes to all caches in a cluster
 * when CRUD (Create, Remove, Update, Delete) methods are called, simply call
 * evict(Fqn) on the remote caches for each changed node.  This allows the
 * remote node to look up the value in a shared cache loader which would have
 * been updated with the changes.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public class InvalidationInterceptor extends BaseRpcInterceptor
{
   private long invalidations = 0;
   protected Map<GlobalTransaction, List<WriteCommand>> txMods;
   protected boolean optimistic;
   private CommandsFactory commandsFactory;
   private boolean statsEnabled;

   @Inject
   public void injectDependencies(CommandsFactory commandsFactory)
   {
      this.commandsFactory = commandsFactory;
   }

   @Start
   void initTxMap()
   {
      optimistic = configuration.getNodeLockingScheme() == NodeLockingScheme.OPTIMISTIC;
      if (optimistic) txMods = new ConcurrentHashMap<GlobalTransaction, List<WriteCommand>>();
      this.setStatisticsEnabled(configuration.getExposeManagementStatistics());
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleWriteMethod(ctx, command.getFqn(), null, command);
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      // these are always local more, as far as invalidation is concerned
      if (ctx.getTransaction() != null) ctx.getTransactionContext().addLocalModification(command);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleWriteMethod(ctx, command.getFqn(), null, command);
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleWriteMethod(ctx, command.getFqn(), null, command);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleWriteMethod(ctx, command.getFqn(), null, command);
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      return handleWriteMethod(ctx, command.getTo(), command.getFqn(), command);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleWriteMethod(ctx, command.getFqn(), null, command);
   }

   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      Object retval = invokeNextInterceptor(ctx, command);
      Transaction tx = ctx.getTransaction();
      if (tx != null)
      {
         if (trace) log.trace("Entering InvalidationInterceptor's prepare phase");
         // fetch the modifications before the transaction is committed (and thus removed from the txTable)
         GlobalTransaction gtx = ctx.getGlobalTransaction();
         TransactionContext transactionContext = ctx.getTransactionContext();
         if (transactionContext == null)
            throw new IllegalStateException("cannot find transaction transactionContext for " + gtx);

         if (transactionContext.hasModifications())
         {
            List<WriteCommand> mods;
            if (transactionContext.hasLocalModifications())
            {
               mods = new ArrayList<WriteCommand>(command.getModifications());
               mods.removeAll(transactionContext.getLocalModifications());
            }
            else
            {
               mods = command.getModifications();
            }
            broadcastInvalidate(mods, tx, ctx);
         }
         else
         {
            if (trace) log.trace("Nothing to invalidate - no modifications in the transaction.");
         }
      }
      return retval;
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      Object retval = invokeNextInterceptor(ctx, command);
      Transaction tx = ctx.getTransaction();
      if (tx != null)
      {
         // here we just record the modifications but actually do the invalidate in commit.
         GlobalTransaction gtx = ctx.getGlobalTransaction();
         TransactionContext transactionContext = ctx.getTransactionContext();
         if (transactionContext == null)
            throw new IllegalStateException("cannot find transaction transactionContext for " + gtx);

         if (transactionContext.hasModifications())
         {
            List<WriteCommand> mods = new ArrayList<WriteCommand>(transactionContext.getModifications());
            if (transactionContext.hasLocalModifications()) mods.removeAll(transactionContext.getLocalModifications());
            txMods.put(gtx, mods);
         }
      }
      return retval;
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      Object retval = invokeNextInterceptor(ctx, command);
      Transaction tx = ctx.getTransaction();
      if (tx != null && optimistic)
      {
         GlobalTransaction gtx = ctx.getGlobalTransaction();
         List<WriteCommand> modifications = txMods.remove(gtx);
         broadcastInvalidate(modifications, tx, ctx);
         if (trace) log.trace("Committing.  Broadcasting invalidations.");
      }
      return retval;
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      Object retval = invokeNextInterceptor(ctx, command);
      Transaction tx = ctx.getTransaction();
      if (tx != null && optimistic)
      {
         GlobalTransaction gtx = ctx.getGlobalTransaction();
         txMods.remove(gtx);
         log.debug("Caught a rollback.  Clearing modification in txMods");
      }
      return retval;
   }

   /**
    * @param from    is only present for move operations, else pass it in as null
    * @param command
    */
   private Object handleWriteMethod(InvocationContext ctx, Fqn targetFqn, Fqn from, VisitableCommand command)
         throws Throwable
   {
      Object retval = invokeNextInterceptor(ctx, command);
      Transaction tx = ctx.getTransaction();
      Option optionOverride = ctx.getOptionOverrides();
      if (log.isDebugEnabled()) log.debug("Is a CRUD method");
      Set<Fqn> fqns = new HashSet<Fqn>();
      if (from != null)
      {
         fqns.add(from);
      }
      fqns.add(targetFqn);
      if (!fqns.isEmpty())
      {
         // could be potentially TRANSACTIONAL.  Ignore if it is, until we see a prepare().
         if (tx == null || !TransactionTable.isValid(tx))
         {
            // the no-tx case:
            //replicate an evict call.
            for (Fqn fqn : fqns) invalidateAcrossCluster(fqn, null, isSynchronous(optionOverride), ctx);
         }
         else
         {
            if (isLocalModeForced(ctx)) ctx.getTransactionContext().addLocalModification((WriteCommand) command);
         }
      }
      return retval;
   }

   private void broadcastInvalidate(List<WriteCommand> modifications, Transaction tx, InvocationContext ctx) throws Throwable
   {
      if (ctx.getTransaction() != null && !isLocalModeForced(ctx))
      {
         if (modifications == null || modifications.isEmpty()) return;
         InvalidationFilterVisitor filterVisitor = new InvalidationFilterVisitor(modifications.size());
         filterVisitor.visitCollection(null, modifications);

         if (filterVisitor.containsPutForExternalRead)
         {
            log.debug("Modification list contains a putForExternalRead operation.  Not invalidating.");
         }
         else
         {
            try
            {
               TransactionWorkspace workspace = optimistic ? getWorkspace(ctx) : null;
               for (Fqn fqn : filterVisitor.result) invalidateAcrossCluster(fqn, workspace, defaultSynchronous, ctx);
            }
            catch (Throwable t)
            {
               log.warn("Unable to broadcast evicts as a part of the prepare phase.  Rolling back.", t);
               try
               {
                  tx.setRollbackOnly();
               }
               catch (SystemException se)
               {
                  throw new RuntimeException("setting tx rollback failed ", se);
               }
               if (t instanceof RuntimeException)
                  throw (RuntimeException) t;
               else
                  throw new RuntimeException("Unable to broadcast invalidation messages", t);
            }
         }
      }
   }

   public static class InvalidationFilterVisitor extends AbstractVisitor
   {
      Set<Fqn> result;
      public boolean containsPutForExternalRead;

      public InvalidationFilterVisitor(int maxSetSize)
      {
         result = new HashSet<Fqn>(maxSetSize);
      }

      @Override
      public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
      {
         result.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
      {
         containsPutForExternalRead = true;
         return null;
      }

      @Override
      public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
      {
         result.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
      {
         result.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
      {
         result.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
      {
         result.add(command.getFqn());
         return null;
      }

      @Override
      public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
      {
         result.add(command.getFqn());
         // now if this is a "move" operation, then we also have another Fqn -
         Object le = command.getFqn().getLastElement();
         Fqn parent = command.getTo();
         result.add(Fqn.fromRelativeElements(parent, le));
         return null;
      }
   }


   protected void invalidateAcrossCluster(Fqn fqn, TransactionWorkspace workspace, boolean synchronous, InvocationContext ctx) throws Throwable
   {
      if (!isLocalModeForced(ctx))
      {
         // increment invalidations counter if statistics maintained
         incrementInvalidations();
         InvalidateCommand command = commandsFactory.buildInvalidateCommand(fqn);
         DataVersion dataVersion = getNodeVersion(workspace, fqn);
         if (dataVersion != null) ((VersionedInvalidateCommand) command).setDataVersion(dataVersion);
         if (log.isDebugEnabled()) log.debug("Cache [" + rpcManager.getLocalAddress() + "] replicating " + command);
         // voila, invalidated!
         replicateCall(ctx, command, synchronous, ctx.getOptionOverrides());
      }
   }

   private void incrementInvalidations()
   {
      if (getStatisticsEnabled()) invalidations++;
   }

   protected DataVersion getNodeVersion(TransactionWorkspace w, Fqn f)
   {
      if (w == null) return null;
      WorkspaceNode wn = w.getNode(f);
      if (wn == null) return null; // JBCACHE-1297
      DataVersion v = wn.getVersion();

      if (wn.isVersioningImplicit())
      {
         // then send back an incremented version
         v = ((DefaultDataVersion) v).increment();
      }

      return v;
   }

   protected TransactionWorkspace getWorkspace(InvocationContext ctx)
   {
      OptimisticTransactionContext entry = (OptimisticTransactionContext) ctx.getTransactionContext();
      return entry.getTransactionWorkSpace();
   }

   @ManagedOperation
   public void resetStatistics()
   {
      invalidations = 0;
   }

   @ManagedOperation
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = new HashMap<String, Object>();
      retval.put("Invalidations", invalidations);
      return retval;
   }

   @ManagedAttribute
   public boolean getStatisticsEnabled()
   {
      return this.statsEnabled;
   }

   @ManagedAttribute
   public void setStatisticsEnabled(boolean enabled)
   {
      this.statsEnabled = enabled;
   }

   @ManagedAttribute(description = "number of invalidations")
   public long getInvalidations()
   {
      return invalidations;
   }
}
