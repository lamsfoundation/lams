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
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.AbstractVisitor;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VersionedDataCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DefaultDataVersion;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.OptimisticTransactionContext;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.util.concurrent.ConcurrentHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Replication interceptor for the optimistically locked interceptor chain.  Responsible for replicating
 * state to remote nodes.  Unlike its cousin, the {@link org.jboss.cache.interceptors.ReplicationInterceptor}, this interceptor
 * only deals with transactional calls.  Just like all things to do with Optimistic Locking, it is a requirement that
 * everything is done in a transaction and the transaction context is available via {@link org.jboss.cache.InvocationContext#getTransaction()}
 * and {@link org.jboss.cache.InvocationContext#getGlobalTransaction()}.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:stevew@jofti.com">Steve Woodcock (stevew@jofti.com)</a>
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticReplicationInterceptor extends BaseRpcInterceptor
{
   /**
    * record of local broacasts - so we do not broadcast rollbacks/commits that resuted from local prepare failures  we
    * really just need a set here, but concurrent CopyOnWriteArraySet has poor performance when writing.
    */
   private final Set<GlobalTransaction> broadcastTxs = new ConcurrentHashSet<GlobalTransaction>();

   private CommandsFactory commandsFactory;

   @Inject
   public void initialize(CommandsFactory commandsFactory)
   {
      this.commandsFactory = commandsFactory;
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      // pass up the chain.
      Object retval = invokeNextInterceptor(ctx, command);
      if (!skipReplicationOfTransactionMethod(ctx))
      {
         GlobalTransaction gtx = getGlobalTransaction(ctx);
         TransactionContext transactionContext = ctx.getTransactionContext();
         if (transactionContext.hasLocalModifications())
         {
            OptimisticPrepareCommand replicablePrepareCommand = command.copy(); // makre sure we remove any "local" transactions
            replicablePrepareCommand.removeModifications(transactionContext.getLocalModifications());
            command = replicablePrepareCommand;
         }

         // replicate the prepare call.
         broadcastPrepare(command, gtx, ctx);
      }
      return retval;
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      //lets broadcast the commit first
      Throwable remoteCommitException = null;
      GlobalTransaction gtx = getGlobalTransaction(ctx);
      if (!gtx.isRemote() && ctx.isOriginLocal() && broadcastTxs.contains(gtx))
      {
         try
         {
            if (!skipReplicationOfTransactionMethod(ctx)) broadcastCommit(gtx, ctx);
         }
         catch (Throwable t)
         {
            log.error("A problem occurred with remote commit", t);
            remoteCommitException = t;
         }
      }

      Object retval = invokeNextInterceptor(ctx, command);
      if (remoteCommitException != null)
      {
         throw remoteCommitException;
      }
      return retval;
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      //    lets broadcast the rollback first
      GlobalTransaction gtx = getGlobalTransaction(ctx);
      Throwable remoteRollbackException = null;
      if (!gtx.isRemote() && ctx.isOriginLocal() && broadcastTxs.contains(gtx))
      {
         //we dont do anything
         try
         {
            if (!skipReplicationOfTransactionMethod(ctx)) broadcastRollback(gtx, ctx);
         }
         catch (Throwable t)
         {
            log.error(" a problem occurred with remote rollback", t);
            remoteRollbackException = t;
         }

      }
      Object retval = invokeNextInterceptor(ctx, command);
      if (remoteRollbackException != null)
      {
         throw remoteRollbackException;
      }
      return retval;
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      ctx.getTransactionContext().setForceAsyncReplication(true);
      return handleDefault(ctx, command);
   }

   public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      if (isLocalModeForced(ctx) && command instanceof WriteCommand)
         ctx.getTransactionContext().addLocalModification((WriteCommand) command);
      return invokeNextInterceptor(ctx, command);
   }


   private GlobalTransaction getGlobalTransaction(InvocationContext ctx)
   {
      // get the current globalTransaction
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      if (gtx == null)
      {
         throw new CacheException("failed to get global transaction");
      }
      return gtx;
   }

   protected void broadcastPrepare(OptimisticPrepareCommand command, GlobalTransaction gtx, InvocationContext ctx) throws Throwable
   {
      // this method will return immediately if we're the only member
      if (rpcManager.getMembers() != null && rpcManager.getMembers().size() > 1)
      {
         // Map method calls to data versioned equivalents.
         // See JBCACHE-843 and docs/design/DataVersioning.txt
         DataVersionPopulator populator = new DataVersionPopulator(getTransactionWorkspace(ctx), command.getModifications().size());

         // visit all elements in the collection and apply the DataVersionPopulator to ensure all commands have data versions set.
         populator.visitCollection(null, command.getModifications());

         ReplicableCommand toBroadcast = commandsFactory.buildOptimisticPrepareCommand(gtx, populator.versionedCommands, command.getLocalAddress(), command.isOnePhaseCommit());

         //record the things we have possibly sent
         broadcastTxs.add(gtx);
         if (log.isDebugEnabled())
         {
            log.debug("(" + rpcManager.getLocalAddress() + "): broadcasting prepare for " + gtx + " (" + command.getModificationsCount() + " modifications");
         }
         replicateCall(ctx, toBroadcast, defaultSynchronous, ctx.getOptionOverrides());
      }
      else
      {
         //no members, ignoring
         if (log.isDebugEnabled())
         {
            log.debug("(" + rpcManager.getLocalAddress() + "):not broadcasting prepare as members are " + rpcManager.getMembers());
         }
      }
   }


   protected void broadcastCommit(GlobalTransaction gtx, InvocationContext ctx) throws Throwable
   {
      boolean remoteCallSync = configuration.isSyncCommitPhase();

      // Broadcast commit() to all members (exclude myself though)
      if (rpcManager.getMembers() != null && rpcManager.getMembers().size() > 1)
      {
         try
         {
            broadcastTxs.remove(gtx);
            CommitCommand commitCommand = commandsFactory.buildCommitCommand(gtx);

            if (log.isDebugEnabled())
               log.debug("running remote commit for " + gtx + " and coord=" + rpcManager.getLocalAddress());

            // for an optimistic commit we don't need to force an OOB message since O/L means we have non-blocking reads.
            replicateCall(ctx, commitCommand, remoteCallSync, ctx.getOptionOverrides(), false);
         }
         catch (Exception e)
         {
            log.error("Commit failed", e);
            throw e;
         }
      }
   }

   protected void broadcastRollback(GlobalTransaction gtx, InvocationContext ctx) throws Throwable
   {
      boolean remoteCallSync = configuration.isSyncRollbackPhase();

      if (rpcManager.getMembers() != null && rpcManager.getMembers().size() > 1)
      {
         // Broadcast rollback() to all other members (excluding myself)
         try
         {
            broadcastTxs.remove(gtx);
            RollbackCommand rollbackCommand = commandsFactory.buildRollbackCommand(null);

            if (log.isDebugEnabled())
               log.debug("running remote rollback for " + gtx + " and coord=" + rpcManager.getLocalAddress());
            replicateCall(ctx, rollbackCommand, remoteCallSync, ctx.getOptionOverrides());
         }
         catch (Exception e)
         {
            log.error("Rollback failed", e);
            throw e;
         }
      }
   }

   public class DataVersionPopulator extends AbstractVisitor
   {
      final TransactionWorkspace workspace;
      final List<WriteCommand> versionedCommands;

      public DataVersionPopulator(TransactionWorkspace workspace, int numCommands)
      {
         this.workspace = workspace;
         versionedCommands = new ArrayList<WriteCommand>(numCommands);
      }

      private void setDataVersion(VersionedDataCommand clone, Fqn fqn)
      {
         DataVersion versionToBroadcast = getVersionToBroadcast(workspace, fqn);
         clone.setDataVersion(versionToBroadcast);
         versionedCommands.add(clone);
      }

      @Override
      public Object visitGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
      {
         return command;
      }

      @Override
      public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
      {
         VersionedDataCommand clone = commandsFactory.buildPutDataMapCommand(null, command.getFqn(), command.getData());
         setDataVersion(clone, command.getFqn());
         return null;
      }

      @Override
      public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
      {
         VersionedDataCommand clone = commandsFactory.buildPutKeyValueCommand(null, command.getFqn(), command.getKey(), command.getValue());
         setDataVersion(clone, command.getFqn());
         return null;
      }

      @Override
      public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
      {
         VersionedDataCommand clone = commandsFactory.buildPutForExternalReadCommand(null, command.getFqn(), command.getKey(), command.getValue());
         setDataVersion(clone, command.getFqn());
         return null;
      }

      @Override
      public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
      {
         VersionedDataCommand clone = commandsFactory.buildRemoveNodeCommand(command.getGlobalTransaction(), command.getFqn());
         setDataVersion(clone, command.getFqn());
         return null;
      }

      @Override
      public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
      {
         VersionedDataCommand clone = commandsFactory.buildRemoveKeyCommand(null, command.getFqn(), command.getKey());
         setDataVersion(clone, command.getFqn());
         return null;
      }

      @Override
      public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
      {
         VersionedDataCommand clone = commandsFactory.buildClearDataCommand(command.getGlobalTransaction(), command.getFqn()
         );
         setDataVersion(clone, command.getFqn());
         return null;
      }

      @Override
      public Object visitCreateNodeCommand(InvocationContext ctx, CreateNodeCommand command) throws Throwable
      {
         versionedCommands.add(command);
         return command;
      }

      @Override
      public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
      {
         versionedCommands.add(command);
         return command;
      }

      @Override
      public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
      {
         throw new CacheException("Not handling " + command + "!");
      }

      /**
       * Digs out the DataVersion for a given Fqn.  If the versioning is explicit, it is passed as-is.  If implicit, it is
       * cloned and then incremented, and the clone is returned.
       */
      private DataVersion getVersionToBroadcast(TransactionWorkspace w, Fqn f)
      {
         WorkspaceNode n = w.getNode(f);
         if (n == null)
         {
            if (trace) log.trace("Fqn " + f + " not found in workspace; not using a data version.");
            return null;
         }
         if (n.isVersioningImplicit())
         {
            DefaultDataVersion v = (DefaultDataVersion) n.getVersion();
            if (trace)
               log.trace("Fqn " + f + " has implicit versioning.  Broadcasting an incremented version.");

            // potential bug here - need to check if we *need* to increment at all, because of Configuration.isLockParentForChildInsertRemove()
            return v.increment();
         }
         else
         {
            if (trace) log.trace("Fqn " + f + " has explicit versioning.  Broadcasting the version as-is.");
            return n.getVersion();
         }
      }

   }

   protected TransactionWorkspace getTransactionWorkspace(InvocationContext ctx) throws CacheException
   {
      OptimisticTransactionContext transactionContext = (OptimisticTransactionContext) ctx.getTransactionContext();
      if (transactionContext == null)
      {
         throw new CacheException("unable to map global transaction " + ctx + " to transaction entry");
      }
      // try and get the workspace from the transaction
      return transactionContext.getTransactionWorkSpace();
   }

}