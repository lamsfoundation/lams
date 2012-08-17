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
package org.jboss.cache.buddyreplication;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.AbstractVisitor;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
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

import java.util.ArrayList;
import java.util.List;

/**
 * For each command the fqns are changed such that they are under the current buddy group's backup subtree
 * (e.g., /_buddy_backup_/my_host:7890/) rather than the root (/).
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class Fqn2BuddyFqnVisitor extends AbstractVisitor
{
   private BuddyFqnTransformer buddyFqnTransformer;

   private final String buddyGroupName;

   CommandsFactory factory;

   public Fqn2BuddyFqnVisitor(String buddyGroupName)
   {
      this.buddyGroupName = buddyGroupName == null ? "null" : buddyGroupName;
   }

   public Fqn2BuddyFqnVisitor(String buddyGroupName, CommandsFactory cf)
   {
      this.buddyGroupName = buddyGroupName == null ? "null" : buddyGroupName;
      this.factory = cf;
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand commitCommand) throws Throwable
   {
      return commitCommand;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildPutDataMapCommand(null, transformed, command.getData());
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildPutKeyValueCommand(null, transformed, command.getKey(), command.getValue());
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildPutForExternalReadCommand(null, transformed, command.getKey(), command.getValue());
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildRemoveNodeCommand(command.getGlobalTransaction(), transformed);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildClearDataCommand(command.getGlobalTransaction(), transformed);
   }

   @Override
   public Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      Fqn fqn = getBackupFqn(command.getFqn());
      return factory.buildEvictFqnCommand(fqn);
   }

   @Override
   public Object visitInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildInvalidateCommand(transformed);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildRemoveKeyCommand(null, transformed, command.getKey());
   }

   @Override
   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildGetDataMapCommand(transformed);
   }

   @Override
   public Object visitExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildEvictFqnCommand(transformed);
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildGetKeyValueCommand(transformed, command.getKey(), command.isSendNodeEvent());
   }

   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildGetNodeCommand(transformed);
   }

   @Override
   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildGetKeysCommand(transformed);
   }

   @Override
   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildGetChildrenNamesCommand(transformed);
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      Fqn transformedFrom = getBackupFqn(command.getFqn());
      Fqn transformedTo = getBackupFqn(command.getTo());
      return factory.buildMoveCommand(transformedFrom, transformedTo);
   }

   @Override
   public Object visitGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      Fqn transformed = getBackupFqn(command.getFqn());
      return factory.buildGravitateDataCommand(transformed, command.isSearchSubtrees());
   }

   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      List<WriteCommand> toTransform = command.getModifications();
      List<WriteCommand> transformedCommands = transformBatch(toTransform);
      return factory.buildPrepareCommand(command.getGlobalTransaction(), transformedCommands, command.getLocalAddress(), command.isOnePhaseCommit());
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      return factory.buildRollbackCommand(null);
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      List<WriteCommand> transformed = transformBatch(command.getModifications());
      return factory.buildOptimisticPrepareCommand(command.getGlobalTransaction(), transformed, command.getLocalAddress(), command.isOnePhaseCommit());
   }

   @Override
   public Object visitCreateNodeCommand(InvocationContext ctx, CreateNodeCommand command) throws Throwable
   {
      return factory.buildCreateNodeCommand(getBackupFqn(command.getFqn()));
   }

   public List<WriteCommand> transformBatch(List<WriteCommand> toTransform) throws Throwable
   {
      List<WriteCommand> transformedCommands = new ArrayList<WriteCommand>(toTransform.size());
      for (WriteCommand com : toTransform)
      {
         transformedCommands.add((WriteCommand) com.acceptVisitor(null, this));
      }
      return transformedCommands;
   }


   /**
    * Assumes the backup Fqn if the current instance is the data owner.
    */
   public Fqn getBackupFqn(Fqn originalFqn)
   {
      return buddyFqnTransformer.getBackupFqn(buddyGroupName, originalFqn);
   }

   public void setBuddyFqnTransformer(BuddyFqnTransformer buddyFqnTransformer)
   {
      this.buddyFqnTransformer = buddyFqnTransformer;
   }
}
