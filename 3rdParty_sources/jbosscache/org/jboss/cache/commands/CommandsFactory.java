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

import org.jboss.cache.Fqn;
import org.jboss.cache.buddyreplication.BuddyGroup;
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
import org.jboss.cache.transaction.GlobalTransaction;
import org.jgroups.Address;

import java.util.List;
import java.util.Map;

/**
 * Factory for all types of cache commands.
 * Here are some of the purposes of this class:
 * <pre>
 *   - not creating <code>CacheCommands</code> directly (i.e. through new usage) as this would reduce unit testability
 *   - reduce the coupling between commands and other components. e.g. considering a commands that needs to knwo whether
 *     locking type is optimistic, we will pass in a 'optimistic' boolean flag rather than entire Configuration object
 * </pre>
 * <p/>
 * <b>Note:</b> As of 3.0, this is now an interface.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public interface CommandsFactory
{
   PutDataMapCommand buildPutDataMapCommand(GlobalTransaction gtx, Fqn fqn, Map data);

   PutKeyValueCommand buildPutKeyValueCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value);

   PutForExternalReadCommand buildPutForExternalReadCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value);

   ReplicateCommand buildReplicateCommand(ReplicableCommand command);

   ReplicateCommand buildReplicateCommand(List<ReplicableCommand> modifications);

   PrepareCommand buildPrepareCommand(GlobalTransaction gtx, WriteCommand command, boolean onePhaseCommit);

   PrepareCommand buildPrepareCommand(GlobalTransaction gtx, List<WriteCommand> modifications, Address address, boolean onePhaseCommit);

   CommitCommand buildCommitCommand(GlobalTransaction gtx);

   DataGravitationCleanupCommand buildDataGravitationCleanupCommand(Fqn primaryFqn, Fqn backupFqn);

   GravitateDataCommand buildGravitateDataCommand(Fqn fqn, Boolean searchSubtrees);

   RemoveNodeCommand buildRemoveNodeCommand(GlobalTransaction gtx, Fqn fqn);

   ClearDataCommand buildClearDataCommand(GlobalTransaction gtx, Fqn fqn);

   EvictCommand buildEvictFqnCommand(Fqn fqn);

   InvalidateCommand buildInvalidateCommand(Fqn fqn);

   RemoveKeyCommand buildRemoveKeyCommand(GlobalTransaction tx, Fqn fqn, Object key);

   GetDataMapCommand buildGetDataMapCommand(Fqn fqn);

   ExistsCommand buildExistsNodeCommand(Fqn fqn);

   GetKeyValueCommand buildGetKeyValueCommand(Fqn fqn, Object key, boolean sendNodeEvent);

   GetNodeCommand buildGetNodeCommand(Fqn fqn);

   GetKeysCommand buildGetKeysCommand(Fqn fqn);

   GetChildrenNamesCommand buildGetChildrenNamesCommand(Fqn fqn);

   MoveCommand buildMoveCommand(Fqn from, Fqn to);

   RollbackCommand buildRollbackCommand(GlobalTransaction gtx);

   OptimisticPrepareCommand buildOptimisticPrepareCommand(GlobalTransaction gtx, List<WriteCommand> modifications, Address address, boolean onePhaseCommit);

   AnnounceBuddyPoolNameCommand buildAnnounceBuddyPoolNameCommand(Address address, String buddyPoolName);

   RemoveFromBuddyGroupCommand buildRemoveFromBuddyGroupCommand(String groupName);

   AssignToBuddyGroupCommand buildAssignToBuddyGroupCommand(BuddyGroup group, Map<Fqn, byte[]> state);

   ClusteredGetCommand buildClusteredGetCommand(Boolean searchBackupSubtrees, DataCommand dataCommand);

   CreateNodeCommand buildCreateNodeCommand(Fqn fqn);

   /**
    * Builds a cache command based on the ID passed in and an object array of parameters
    *
    * @param id         id of the command to build
    * @param parameters parameters attached to the command
    * @return a newly constructed cache command
    */
   ReplicableCommand fromStream(int id, Object[] parameters);

   StateTransferControlCommand buildStateTransferControlCommand(boolean b);
}
