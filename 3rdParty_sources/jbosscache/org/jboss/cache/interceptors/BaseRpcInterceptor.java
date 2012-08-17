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
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.cluster.ReplicationQueue;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.transaction.TransactionTable;
import org.jgroups.Address;

import javax.transaction.Transaction;
import java.util.List;
import java.util.Vector;

/**
 * Acts as a base for all RPC calls - subclassed by
 * {@link ReplicationInterceptor} and {@link OptimisticReplicationInterceptor}.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public abstract class BaseRpcInterceptor extends CommandInterceptor
{
   private BuddyManager buddyManager;
   private boolean usingBuddyReplication;
   private ReplicationQueue replicationQueue;
   protected TransactionTable txTable;
   private CommandsFactory commandsFactory;

   protected RPCManager rpcManager;
   protected boolean defaultSynchronous;

   @Inject
   public void injectComponents(RPCManager rpcManager, BuddyManager buddyManager, ReplicationQueue replicationQueue,
                                TransactionTable txTable, CommandsFactory commandsFactory)
   {
      this.rpcManager = rpcManager;
      this.replicationQueue = replicationQueue;
      this.buddyManager = buddyManager;
      this.txTable = txTable;
      this.commandsFactory = commandsFactory;
   }

   @Start
   void init()
   {
      usingBuddyReplication = configuration.getBuddyReplicationConfig() != null && configuration.getBuddyReplicationConfig().isEnabled();
      defaultSynchronous = configuration.getCacheMode().isSynchronous();
   }

   /**
    * Checks whether any of the responses are exceptions. If yes, re-throws
    * them (as exceptions or runtime exceptions).
    */
   protected void checkResponses(List rsps) throws Throwable
   {
      if (rsps != null)
      {
         for (Object rsp : rsps)
         {
            if (rsp != null && rsp instanceof Throwable)
            {
               // lets print a stack trace first.
               if (log.isDebugEnabled())
                  log.debug("Received Throwable from remote node", (Throwable) rsp);
               throw (Throwable) rsp;
            }
         }
      }
   }

   protected void replicateCall(InvocationContext ctx, ReplicableCommand call, boolean sync, Option o, boolean useOutOfBandMessage) throws Throwable
   {
      replicateCall(ctx, null, call, sync, o, useOutOfBandMessage);
   }

   protected void replicateCall(InvocationContext ctx, ReplicableCommand call, boolean sync, Option o) throws Throwable
   {
      replicateCall(ctx, null, call, sync, o, false);
   }

   protected void replicateCall(InvocationContext ctx, Vector<Address> recipients, ReplicableCommand c, boolean sync, Option o, boolean useOutOfBandMessage) throws Throwable
   {
      long syncReplTimeout = configuration.getSyncReplTimeout();

      // test for option overrides
      if (o != null)
      {
         if (o.isForceAsynchronous()) sync = false;
         else if (o.isForceSynchronous()) sync = true;

         if (o.getSyncReplTimeout() > 0) syncReplTimeout = o.getSyncReplTimeout();
      }

      // tx-level overrides are more important
      Transaction tx = ctx.getTransaction();
      if (tx != null)
      {
         TransactionContext transactionContext = ctx.getTransactionContext();
         if (transactionContext != null)
         {
            if (transactionContext.isForceAsyncReplication()) sync = false;
            else if (transactionContext.isForceSyncReplication()) sync = true;
         }
      }

      replicateCall(recipients, c, sync, true, useOutOfBandMessage, false, syncReplTimeout);
   }

   protected void replicateCall(Vector<Address> recipients, ReplicableCommand call, boolean sync, boolean wrapCacheCommandInReplicateMethod, boolean useOutOfBandMessage, boolean isBroadcast, long timeout) throws Throwable
   {
      if (trace) log.trace("Broadcasting call " + call + " to recipient list " + recipients);

      if (!sync && replicationQueue != null && !usingBuddyReplication)
      {
         if (trace) log.trace("Putting call " + call + " on the replication queue.");
         replicationQueue.add(commandsFactory.buildReplicateCommand(call));
      }
      else
      {
         if (usingBuddyReplication && !isBroadcast) call = buddyManager.transformFqns((VisitableCommand) call);

         Vector<Address> callRecipients = recipients;
         if (callRecipients == null)
         {
            callRecipients = usingBuddyReplication && !isBroadcast ? buddyManager.getBuddyAddressesAsVector() : null;
            if (trace)
               log.trace("Setting call recipients to " + callRecipients + " since the original list of recipients passed in is null.");
         }

         ReplicableCommand toCall = wrapCacheCommandInReplicateMethod ? commandsFactory.buildReplicateCommand(call) : call;

         List rsps = rpcManager.callRemoteMethods(callRecipients,
               toCall,
               sync, // is synchronised?
               timeout,
               useOutOfBandMessage
         );
         if (trace) log.trace("responses=" + rsps);
         if (sync) checkResponses(rsps);
      }
   }

   /**
    * It does not make sense replicating a transaction method(commit, rollback, prepare) if one of the following is true:
    * <pre>
    *    - call was not initiated here, but on other member of the cluster
    *    - there is no transaction. Why broadcast a commit or rollback if there is no transaction going on?
    *    - the current transaction did not modify any data
    * </pre>
    */
   protected boolean skipReplicationOfTransactionMethod(InvocationContext ctx)
   {
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      return ctx.getTransaction() == null || gtx == null || gtx.isRemote() || ctx.getOptionOverrides().isCacheModeLocal() || !ctx.getTransactionContext().hasModifications();
   }

   /**
    * The call runs in a transaction and it was initiated on this node of the cluster.
    */
   protected boolean isTransactionalAndLocal(InvocationContext ctx)
   {
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      boolean isInitiatedHere = gtx != null && !gtx.isRemote();
      return isInitiatedHere && (ctx.getTransaction() != null);
   }

   protected boolean isSynchronous(Option option)
   {
      if (option != null)
      {
         if (option.isForceSynchronous())
            return true;
         else if (option.isForceAsynchronous())
            return false;
      }
      return defaultSynchronous;
   }

   protected boolean isLocalModeForced(InvocationContext ctx)
   {
      if (ctx.getOptionOverrides() != null && ctx.getOptionOverrides().isCacheModeLocal())
      {
         if (trace) log.trace("LOCAL mode forced on invocation.  Suppressing clustered events.");
         return true;
      }
      return false;
   }
}