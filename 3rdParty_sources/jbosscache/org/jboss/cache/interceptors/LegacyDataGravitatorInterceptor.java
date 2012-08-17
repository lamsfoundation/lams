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
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.buddyreplication.GravitateResult;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.DataCommand;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.remote.DataGravitationCleanupCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jgroups.Address;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.blocks.RspFilter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Data Gravitator interceptor intercepts cache misses and attempts to
 * gravitate data from other parts of the cluster.
 * <p/>
 * Only used if Buddy Replication is enabled.  Also, the interceptor only kicks
 * in if an {@link org.jboss.cache.config.Option} is passed in to force Data
 * Gravitation for a specific invocation or if <b>autoDataGravitation</b> is
 * set to <b>true</b> when configuring Buddy Replication.
 * <p/>
 * See the JBoss Cache User Guide for more details on configuration options.
 * There is a section dedicated to Buddy Replication in the Replication
 * chapter.
 * <p/>
 * In terms of functionality, if a gravitation call has occured and a cleanup call is needed (based on
 * how BR is configured), a cleanup call will be broadcast immediately after the gravitation call (no txs)
 * or if txs are used, an <i>asynchronous</i> call is made to perform the cleanup <i>outside</i> the scope
 * of the tx that caused the gravitation event.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @deprecated will be removed with optimistic and pessimistic locking.
 */
@Deprecated
public class LegacyDataGravitatorInterceptor extends BaseRpcInterceptor
{
   private BuddyManager buddyManager;
   /**
    * Map that contains commands that need cleaning up.  This is keyed on global transaction, and contains a list of
    * cleanup commands corresponding to all gravitate calls made during the course of the transaction in question.
    */
   private Map<GlobalTransaction, List<ReplicableCommand>> cleanupCommands = new ConcurrentHashMap<GlobalTransaction, List<ReplicableCommand>>();
   private DataContainer dataContainer;
   private CommandsFactory commandsFactory;
   private CacheSPI cacheSPI;
   private BuddyFqnTransformer buddyFqnTransformer;

   @Inject
   public void injectComponents(BuddyManager buddyManager, DataContainer dataContainer, CommandsFactory commandsFactory, CacheSPI cacheSPI, BuddyFqnTransformer transformer)
   {
      this.buddyManager = buddyManager;
      this.dataContainer = dataContainer;
      this.commandsFactory = commandsFactory;
      this.cacheSPI = cacheSPI;
      buddyFqnTransformer = transformer;
   }

   @Override
   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      return handleGetMethod(ctx, command);
   }

   @Override
   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      return handleGetMethod(ctx, command);
   }

   @Override
   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      return handleGetMethod(ctx, command);
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      return handleGetMethod(ctx, command);
   }

   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      return handleGetMethod(ctx, command);
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      try
      {
         return invokeNextInterceptor(ctx, command);
      }
      finally
      {
         cleanupCommands.remove(ctx.getGlobalTransaction());
      }
   }


   /**
    * Make sure you also run a cleanup if we have an 1pc.
    */
   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      if (command.isOnePhaseCommit())
      {
         return dataGravitationCleanupOnCommit(ctx, command);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      return dataGravitationCleanupOnCommit(ctx, command);
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      return visitPrepareCommand(ctx, command);
   }

   private Object dataGravitationCleanupOnCommit(InvocationContext ctx, VisitableCommand command)
         throws Throwable
   {
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      try
      {
         doCommit(gtx);
         return invokeNextInterceptor(ctx, command);
      }
      finally
      {
         cleanupCommands.remove(gtx);
      }
   }

   /**
    * @param ctx invocation context
    * @param fqn fqn to test
    * @return true if the node does not exist; false otherwise.
    */
   protected boolean nodeDoesNotExist(InvocationContext ctx, Fqn fqn)
   {
      return !dataContainer.exists(fqn);
   }

   private Object handleGetMethod(InvocationContext ctx, DataCommand command) throws Throwable
   {
      if (isGravitationEnabled(ctx))
      {
         // test that the Fqn being requested exists locally in the cache.
         if (trace) log.trace("Checking local existence of requested fqn " + command.getFqn());
         if (buddyFqnTransformer.isBackupFqn(command.getFqn()))
         {
            log.info("Is call for a backup Fqn, not performing any gravitation.  Direct calls on internal backup nodes are *not* supported.");
         }
         else
         {
            if (nodeDoesNotExist(ctx, command.getFqn()))
            {
               // gravitation is necessary.

               if (trace) log.trace("Gravitating from local backup tree");
               BackupData data = localBackupGet(command.getFqn(), ctx);

               if (data == null)
               {
                  if (trace) log.trace("Gravitating from remote backup tree");
                  // gravitate remotely.
                  data = remoteBackupGet(command.getFqn());
               }

               if (data != null)
               {
                  if (trace)
                     log.trace("Passing the put call locally to make sure state is persisted and ownership is correctly established.");
                  // store the gravitated node locally.  This will cause it being backed up in the current instance's buddy.
                  createNode(data.backupData);
                  cleanBackupData(data, ctx);
                  wrapIfNeeded(ctx, data.primaryFqn);
               }
            }
            else
            {
               if (trace) log.trace("No need to gravitate; have this already.");
            }
         }
      }
      else
      {
         if (trace)
         {
            log.trace("Suppressing data gravitation for this call.");
         }
      }
      return invokeNextInterceptor(ctx, command);
   }

   protected void wrapIfNeeded(InvocationContext ctx, Fqn fqnToWrap) throws InterruptedException
   {
      // no op
   }

   private boolean isGravitationEnabled(InvocationContext ctx)
   {
      boolean enabled = ctx.isOriginLocal();
      if (enabled && !buddyManager.isAutoDataGravitation())
      {
         enabled = ctx.getOptionOverrides().getForceDataGravitation();
      }

      return enabled;
   }

   private void doCommit(GlobalTransaction gtx) throws Throwable
   {
      if (cleanupCommands.containsKey(gtx))
      {
         if (trace) log.trace("Broadcasting cleanup commands for gtx " + gtx);

         for (ReplicableCommand command : cleanupCommands.get(gtx))
         {
            try
            {
               doCleanup(command);
            }
            catch (Throwable th)
            {
               log.warn("Problems performing gravitation cleanup.  Cleanup command: " + command, th);
            }
         }
      }
      else
      {
         if (trace) log.trace("No cleanups to broadcast in commit phase for gtx " + gtx);
      }
   }

   private BackupData remoteBackupGet(Fqn name) throws Exception
   {
      BackupData result = null;
      GravitateResult gr = gravitateData(name);
      if (gr.isDataFound())
      {
         if (trace)
         {
            log.trace("Got response " + gr);
         }

         result = new BackupData(name, gr);
      }
      return result;
   }

   private void cleanBackupData(BackupData backup, InvocationContext ctx) throws Throwable
   {

      DataGravitationCleanupCommand cleanupCommand = commandsFactory.buildDataGravitationCleanupCommand(backup.primaryFqn, backup.backupFqn);
      GlobalTransaction gtx = ctx.getGlobalTransaction();

      if (gtx == null)
      {
         // broadcast removes
         // remove main Fqn
         if (trace) log.trace("Performing cleanup on [" + backup.primaryFqn + "]");
         // remove backup Fqn
         doCleanup(cleanupCommand);
      }
      else
      {
         if (trace)
            log.trace("Data gravitation performed under global transaction " + gtx + ".  Not broadcasting cleanups until the tx commits.  Recording cleanup command for later use.");
         List<ReplicableCommand> commands;
         if (cleanupCommands.containsKey(gtx))
         {
            commands = cleanupCommands.get(gtx);
         }
         else
         {
            commands = new LinkedList<ReplicableCommand>();
         }

         commands.add(cleanupCommand);
         cleanupCommands.put(gtx, commands);
      }
   }

   private void doCleanup(ReplicableCommand cleanupCommand) throws Throwable
   {
      // cleanup commands are always ASYNCHRONOUS and is broadcast to *everyone* (even members of the current buddy
      // group as they may be members of > 1 buddy group)
      replicateCall(null, cleanupCommand, false, false, false, true, -1);
   }

   @SuppressWarnings("deprecation")
   private GravitateResult gravitateData(Fqn fqn) throws Exception
   {
      if (trace) log.trace("Requesting data gravitation for Fqn " + fqn);

      List<Address> mbrs = rpcManager.getMembers();
      Boolean searchSubtrees = buddyManager.isDataGravitationSearchBackupTrees();
      GravitateDataCommand command = commandsFactory.buildGravitateDataCommand(fqn, searchSubtrees);
      // doing a GET_ALL is crappy but necessary since JGroups' GET_FIRST could return null results from nodes that do
      // not have either the primary OR backup, and stop polling other valid nodes.
      List resps = rpcManager.callRemoteMethods(null, command, GroupRequest.GET_ALL, buddyManager.getBuddyCommunicationTimeout(), new ResponseValidityFilter(rpcManager.getMembers().size(), rpcManager.getLocalAddress()), false);

      if (trace) log.trace("got responses " + resps);

      if (resps == null)
      {
         if (mbrs.size() > 1) log.error("No replies to call " + command);
         return GravitateResult.noDataFound();
      }

      // test for and remove exceptions
      GravitateResult result = GravitateResult.noDataFound();
      for (Object o : resps)
      {
         if (o instanceof Throwable)
         {
            if (log.isDebugEnabled())
            {
               log.debug("Found remote Throwable among responses - removing from responses list", (Exception) o);
            }
         }
         else if (o != null)
         {
            result = (GravitateResult) o;
            if (result.isDataFound())
            {
               break;
            }
         }
         else if (!configuration.isUseRegionBasedMarshalling())
         {
            // Null is OK if we are using region based marshalling; it
            // is what is returned if a region is inactive. Otherwise
            // getting a null is an error condition
            log.error("Unexpected null response to call " + command + ".");
         }

      }

      return result;
   }

   @SuppressWarnings("unchecked")
   private void createNode(List<NodeData> nodeData) throws CacheException
   {
      for (NodeData data : nodeData)
      {
         cacheSPI.put(data.getFqn(), data.getAttributes());
      }
   }

   private BackupData localBackupGet(Fqn fqn, InvocationContext ctx) throws CacheException
   {
      GravitateResult result = cacheSPI.gravitateData(fqn, true, ctx);// a "local" gravitation
      boolean found = result.isDataFound();
      BackupData data = null;

      if (found)
      {
         Fqn backupFqn = result.getBuddyBackupFqn();
         data = new BackupData(fqn, result);
         // now the cleanup
         if (buddyManager.isDataGravitationRemoveOnFind())
         {
            // Remove locally only; the remote call will
            // be broadcast later
            ctx.getOptionOverrides().setCacheModeLocal(true);
            cacheSPI.removeNode(backupFqn);
         }
         else
         {
            cacheSPI.evict(backupFqn, true);
         }
      }

      if (trace) log.trace("Retrieved data " + data + " found = " + found);
      return data;
   }

   private static class BackupData
   {
      Fqn primaryFqn;
      Fqn backupFqn;
      List<NodeData> backupData;

      public BackupData(Fqn primaryFqn, GravitateResult gr)
      {
         this.primaryFqn = primaryFqn;
         this.backupFqn = gr.getBuddyBackupFqn();
         this.backupData = gr.getNodeData();
      }

      public String toString()
      {
         return "BackupData{" +
               "primaryFqn=" + primaryFqn +
               ", backupFqn=" + backupFqn +
               ", backupData=" + backupData +
               '}';
      }
   }

   public static class ResponseValidityFilter implements RspFilter
   {
      private boolean validResponseFound;
      int memberCount;

      public ResponseValidityFilter(int memberCount, Address localAddress)
      {
         this.memberCount = memberCount;
      }

      public boolean isAcceptable(Object object, Address address)
      {
         if (object instanceof GravitateResult)
         {
            memberCount--;
         }
         // always return true to make sure a response is logged by the JGroups RpcDispatcher.
         return true;
      }

      public boolean needMoreResponses()
      {
         return memberCount > 1;
      }
   }
}
