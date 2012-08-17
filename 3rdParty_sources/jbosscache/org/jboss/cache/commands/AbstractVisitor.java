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

import org.jboss.cache.InvocationContext;
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

import java.util.Collection;

/**
 * An abstract implementation of a Visitor that delegates all visit calls to a default handler which can be overridden.
 *
 * @author Mircea.Markus@jboss.com
 * @author Manik Surtani
 * @since 2.2.0
 */
public abstract class AbstractVisitor implements Visitor
{
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   public Object visitCreateNodeCommand(InvocationContext ctx, CreateNodeCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   /**
    * A default handler for all commands visited.  This is called for any visit method called, unless a visit command is
    * appropriately overridden.
    *
    * @param ctx     invocation context
    * @param command command to handle
    * @return return value
    * @throws Throwable in the case of a problem
    */
   protected Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      return null;
   }

   /**
    * Helper method to visit a collection of VisitableCommands.
    *
    * @param ctx     Invocation context
    * @param toVisit collection of commands to visit
    * @throws Throwable in the event of problems
    */
   public void visitCollection(InvocationContext ctx, Collection<? extends VisitableCommand> toVisit) throws Throwable
   {
      for (VisitableCommand command : toVisit)
      {
         command.acceptVisitor(ctx, this);
      }
   }
}
