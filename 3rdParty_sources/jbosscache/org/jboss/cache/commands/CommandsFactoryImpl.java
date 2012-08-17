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
package org.jboss.cache.commands;

import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.RPCManager;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.buddyreplication.BuddyGroup;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.remote.AnnounceBuddyPoolNameCommand;
import org.jboss.cache.commands.remote.AssignToBuddyGroupCommand;
import org.jboss.cache.commands.remote.ClusteredGetCommand;
import org.jboss.cache.commands.remote.DataGravitationCleanupCommand;
import org.jboss.cache.commands.remote.RemoveFromBuddyGroupCommand;
import org.jboss.cache.commands.remote.ReplicateCommand;
import org.jboss.cache.commands.remote.StateTransferControlCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionTable;
import org.jgroups.Address;

import javax.transaction.TransactionManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation to use for most commands and most locking schemes.
 */
public class CommandsFactoryImpl implements CommandsFactory
{
   protected RPCManager rpcManager;
   protected DataContainer dataContainer;
   protected Notifier notifier;
   protected InterceptorChain invoker;
   protected BuddyManager buddyManager;
   protected TransactionTable transactionTable;
   protected CacheSPI cacheSpi;
   protected Configuration configuration;
   protected TransactionManager txManager;
   protected BuddyFqnTransformer buddyFqnTransformer;

   @Inject
   public void initialize(RPCManager rpc, DataContainer dataContainer, Notifier notifier, BuddyManager buddyManager,
                          InterceptorChain invoker, TransactionTable transactionTable, CacheSPI cacheSpi,
                          Configuration configuration, TransactionManager txManager, BuddyFqnTransformer buddyFqnTransformer)
   {
      this.rpcManager = rpc;
      this.dataContainer = dataContainer;
      this.notifier = notifier;
      this.buddyManager = buddyManager;
      this.invoker = invoker;
      this.transactionTable = transactionTable;
      this.cacheSpi = cacheSpi;
      this.configuration = configuration;
      this.txManager = txManager;
      this.buddyFqnTransformer = buddyFqnTransformer;
   }

   public StateTransferControlCommand buildStateTransferControlCommand(boolean enabled)
   {
      return new StateTransferControlCommand(enabled);
   }

   public PutDataMapCommand buildPutDataMapCommand(GlobalTransaction gtx, Fqn fqn, Map data)
   {
      PutDataMapCommand cmd = new PutDataMapCommand(gtx, fqn, data);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public PutKeyValueCommand buildPutKeyValueCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value)
   {
      PutKeyValueCommand cmd = new PutKeyValueCommand(gtx, fqn, key, value);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public PutForExternalReadCommand buildPutForExternalReadCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value)
   {
      PutForExternalReadCommand cmd = new PutForExternalReadCommand(gtx, fqn, key, value);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public ReplicateCommand buildReplicateCommand(ReplicableCommand command)
   {
      ReplicateCommand cmd = new ReplicateCommand(command);
      cmd.initialize(invoker);
      return cmd;
   }

   public ReplicateCommand buildReplicateCommand(List<ReplicableCommand> modifications)
   {
      ReplicateCommand cmd = new ReplicateCommand(modifications);
      cmd.initialize(invoker);
      return cmd;
   }

   public PrepareCommand buildPrepareCommand(GlobalTransaction gtx, WriteCommand command, boolean onePhaseCommit)
   {
      return buildPrepareCommand(gtx, Collections.singletonList(command), rpcManager.getLocalAddress(), onePhaseCommit);
   }

   public PrepareCommand buildPrepareCommand(GlobalTransaction gtx, List<WriteCommand> modifications, Address address, boolean onePhaseCommit)
   {
      return new PrepareCommand(gtx, modifications, address, onePhaseCommit);
   }

   public CommitCommand buildCommitCommand(GlobalTransaction gtx)
   {
      return new CommitCommand(gtx);
   }

   public DataGravitationCleanupCommand buildDataGravitationCleanupCommand(Fqn primaryFqn, Fqn backupFqn)
   {
      DataGravitationCleanupCommand command = new DataGravitationCleanupCommand(primaryFqn, backupFqn);
      command.initialize(buddyManager, invoker, transactionTable, this, dataContainer, buddyFqnTransformer);
      return command;
   }

   public GravitateDataCommand buildGravitateDataCommand(Fqn fqn, Boolean searchSubtrees)
   {
      GravitateDataCommand command = new GravitateDataCommand(fqn, searchSubtrees, rpcManager.getLocalAddress());
      command.initialize(dataContainer, cacheSpi, buddyFqnTransformer);
      return command;
   }

   public EvictCommand buildEvictFqnCommand(Fqn fqn)
   {
      EvictCommand command = new EvictCommand(fqn);
      command.initialize(notifier, dataContainer);
      return command;
   }

   public InvalidateCommand buildInvalidateCommand(Fqn fqn)
   {
      InvalidateCommand command = new InvalidateCommand(fqn);
      command.initialize(cacheSpi, dataContainer, notifier);
      return command;
   }

   public GetDataMapCommand buildGetDataMapCommand(Fqn fqn)
   {
      GetDataMapCommand command = new GetDataMapCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }

   public ExistsCommand buildExistsNodeCommand(Fqn fqn)
   {
      ExistsCommand command = new ExistsCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }

   public GetKeyValueCommand buildGetKeyValueCommand(Fqn fqn, Object key, boolean sendNodeEvent)
   {
      GetKeyValueCommand command = new GetKeyValueCommand(fqn, key, sendNodeEvent);
      command.initialize(dataContainer, notifier);
      return command;
   }

   public GetNodeCommand buildGetNodeCommand(Fqn fqn)
   {
      GetNodeCommand command = new GetNodeCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }

   public GetKeysCommand buildGetKeysCommand(Fqn fqn)
   {
      GetKeysCommand command = new GetKeysCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }

   public GetChildrenNamesCommand buildGetChildrenNamesCommand(Fqn fqn)
   {
      GetChildrenNamesCommand command = new GetChildrenNamesCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }

   public RollbackCommand buildRollbackCommand(GlobalTransaction gtx)
   {
      return new RollbackCommand(gtx);
   }

   public OptimisticPrepareCommand buildOptimisticPrepareCommand(GlobalTransaction gtx, List<WriteCommand> modifications, Address address, boolean onePhaseCommit)
   {
      return new OptimisticPrepareCommand(gtx, modifications, address, onePhaseCommit);
   }

   public AnnounceBuddyPoolNameCommand buildAnnounceBuddyPoolNameCommand(Address address, String buddyPoolName)
   {
      AnnounceBuddyPoolNameCommand command = new AnnounceBuddyPoolNameCommand(address, buddyPoolName);
      command.initialize(buddyManager);
      return command;
   }

   public RemoveFromBuddyGroupCommand buildRemoveFromBuddyGroupCommand(String groupName)
   {
      RemoveFromBuddyGroupCommand command = new RemoveFromBuddyGroupCommand(groupName);
      command.initialize(buddyManager);
      return command;
   }

   public AssignToBuddyGroupCommand buildAssignToBuddyGroupCommand(BuddyGroup group, Map<Fqn, byte[]> state)
   {
      AssignToBuddyGroupCommand command = new AssignToBuddyGroupCommand(group, state);
      command.initialize(buddyManager);
      return command;
   }

   public ClusteredGetCommand buildClusteredGetCommand(Boolean searchBackupSubtrees, DataCommand dataCommand)
   {
      ClusteredGetCommand command = new ClusteredGetCommand(searchBackupSubtrees, dataCommand);
      command.initialize(dataContainer, invoker);
      return command;
   }

   public RemoveNodeCommand buildRemoveNodeCommand(GlobalTransaction gtx, Fqn fqn)
   {
      RemoveNodeCommand cmd = new RemoveNodeCommand(gtx, fqn);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public ClearDataCommand buildClearDataCommand(GlobalTransaction gtx, Fqn fqn)
   {
      ClearDataCommand cmd = new ClearDataCommand(gtx, fqn);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public RemoveKeyCommand buildRemoveKeyCommand(GlobalTransaction tx, Fqn fqn, Object key)
   {
      RemoveKeyCommand cmd = new RemoveKeyCommand(tx, fqn, key);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public MoveCommand buildMoveCommand(Fqn from, Fqn to)
   {
      MoveCommand cmd = new MoveCommand(from, to);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   public CreateNodeCommand buildCreateNodeCommand(Fqn fqn)
   {
      throw new UnsupportedOperationException("Not supported in MVCC!");
   }

   public ReplicableCommand fromStream(int id, Object[] parameters)
   {
      ReplicableCommand command;
      switch (id)
      {
         case ExistsCommand.METHOD_ID:
         {
            ExistsCommand result = new ExistsCommand();
            result.initialize(dataContainer);
            command = result;
            break;
         }
         case GetChildrenNamesCommand.METHOD_ID:
         {
            GetChildrenNamesCommand returnValue = new GetChildrenNamesCommand();
            returnValue.initialize(dataContainer);
            command = returnValue;
            break;
         }
         case GetDataMapCommand.METHOD_ID:
         {
            GetDataMapCommand returnValue = new GetDataMapCommand();
            returnValue.initialize(dataContainer);
            command = returnValue;
            break;
         }
         case GetKeysCommand.METHOD_ID:
         {
            GetKeysCommand returnValue = new GetKeysCommand();
            returnValue.initialize(dataContainer);
            command = returnValue;
            break;
         }
         case GetKeyValueCommand.METHOD_ID:
         {
            GetKeyValueCommand returnValue = new GetKeyValueCommand();
            returnValue.initialize(dataContainer, notifier);
            command = returnValue;
            break;
         }
         case GetNodeCommand.METHOD_ID:
         {
            GetNodeCommand returnValue = new GetNodeCommand();
            returnValue.initialize(dataContainer);
            command = returnValue;
            break;
         }
         case MoveCommand.METHOD_ID:
         {
            MoveCommand returnValue = new MoveCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case PutDataMapCommand.METHOD_ID:
         case PutDataMapCommand.ERASE_METHOD_ID:
         case PutDataMapCommand.ERASE_VERSIONED_METHOD_ID:
         case PutDataMapCommand.VERSIONED_METHOD_ID:
         {
            PutDataMapCommand returnValue = new PutDataMapCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case PutKeyValueCommand.METHOD_ID:
         case PutKeyValueCommand.VERSIONED_METHOD_ID:
         {
            PutKeyValueCommand returnValue = new PutKeyValueCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case PutForExternalReadCommand.METHOD_ID:
         case PutForExternalReadCommand.VERSIONED_METHOD_ID:
         {
            PutForExternalReadCommand returnValue = new PutForExternalReadCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case ClearDataCommand.METHOD_ID:
         case ClearDataCommand.VERSIONED_METHOD_ID:
         {
            ClearDataCommand returnValue = new ClearDataCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case RemoveKeyCommand.METHOD_ID:
         case RemoveKeyCommand.VERSIONED_METHOD_ID:
         {
            RemoveKeyCommand returnValue = new RemoveKeyCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }

         case RemoveNodeCommand.METHOD_ID:
         case RemoveNodeCommand.VERSIONED_METHOD_ID:
         {
            RemoveNodeCommand returnValue = new RemoveNodeCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case CreateNodeCommand.METHOD_ID:
         {
            throw new UnsupportedOperationException("CreateNodeCommand is not supported in MVCC!");
         }
         // --- transactional method calls

         case PrepareCommand.METHOD_ID:
         {
            command = new PrepareCommand();
            break;
         }

         case OptimisticPrepareCommand.METHOD_ID:
         {
            command = new OptimisticPrepareCommand();
            break;
         }

         case CommitCommand.METHOD_ID:
         {
            command = new CommitCommand();
            break;
         }

         case RollbackCommand.METHOD_ID:
         {
            command = new RollbackCommand();
            break;
         }

         // --- replicate methods
         case ReplicateCommand.MULTIPLE_METHOD_ID:
         case ReplicateCommand.SINGLE_METHOD_ID:
         {
            ReplicateCommand returnValue = new ReplicateCommand();
            returnValue.initialize(invoker);
            command = returnValue;
            break;
         }

         case InvalidateCommand.METHOD_ID:
         {
            InvalidateCommand returnValue = new InvalidateCommand();
            returnValue.initialize(cacheSpi, dataContainer, notifier);
            command = returnValue;
            break;
         }

         case ClusteredGetCommand.METHOD_ID:
         {
            ClusteredGetCommand returnValue = new ClusteredGetCommand();
            returnValue.initialize(dataContainer, invoker);
            command = returnValue;
            break;
         }
         // ---- Buddy replication - group organisation commands
         case AnnounceBuddyPoolNameCommand.METHOD_ID:
         {
            AnnounceBuddyPoolNameCommand returnValue = new AnnounceBuddyPoolNameCommand();
            returnValue.initialize(buddyManager);
            command = returnValue;
            break;
         }
         case AssignToBuddyGroupCommand.METHOD_ID:
         {
            AssignToBuddyGroupCommand returnValue = new AssignToBuddyGroupCommand();
            returnValue.initialize(buddyManager);
            command = returnValue;
            break;
         }
         case RemoveFromBuddyGroupCommand.METHOD_ID:
         {
            RemoveFromBuddyGroupCommand returnValue = new RemoveFromBuddyGroupCommand();
            returnValue.initialize(buddyManager);
            command = returnValue;
            break;
         }
         case DataGravitationCleanupCommand.METHOD_ID:
         {
            DataGravitationCleanupCommand returnValue = new DataGravitationCleanupCommand();
            returnValue.initialize(buddyManager, invoker, transactionTable, this, dataContainer, buddyFqnTransformer);
            command = returnValue;
            break;
         }
         case GravitateDataCommand.METHOD_ID:
         {
            GravitateDataCommand returnValue = new GravitateDataCommand(rpcManager.getLocalAddress());
            returnValue.initialize(dataContainer, cacheSpi, buddyFqnTransformer);
            command = returnValue;
            break;
         }
         case StateTransferControlCommand.METHOD_ID:
         {
            StateTransferControlCommand cmd = new StateTransferControlCommand();
            cmd.init(rpcManager);
            command = cmd;
            break;
         }
         default:
            throw new CacheException("Unknown command id " + id + "!");
      }

      command.setParameters(id, parameters);
      return command;
   }
}
