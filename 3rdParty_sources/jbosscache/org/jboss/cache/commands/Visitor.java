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

/**
 * This interface is the core of JBoss Cache, where each {@link VisitableCommand} can be visited by a Visitor implementation.
 * Visitors which are accepted by the {@link org.jboss.cache.commands.VisitableCommand} are able to modify the command
 * based on any logic encapsulated by the visitor.
 *
 * @author Mircea.Markus@jboss.com
 * @author Manik Surtani
 * @since 2.2.0
 */
public interface Visitor
{
   /**
    * Visits a PutDataMapCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable;

   /**
    * Visits a PutKeyValueCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable;

   /**
    * Visits a PutForExternalReadCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable;

   /**
    * Visits a RemoveNodeCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable;

   /**
    * Visits a RemoveDataCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable;

   /**
    * Visits a EvictCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable;

   /**
    * Visits a InvalidateCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable;

   /**
    * Visits a RemoveKeyCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable;

   /**
    * Visits a GetDataMapCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable;

   /**
    * Visits a RemoteExistsCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable;

   /**
    * Visits a GetKeyValueCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable;

   /**
    * Visits a GetNodeCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable;

   /**
    * Visits a GetKeysCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable;

   /**
    * Visits a GetChildrenNamesCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable;

   /**
    * Visits a MoveCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable;

   /**
    * Visits a GravitateDataCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable;

   /**
    * Visits a PrepareCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable;

   /**
    * Visits a RollbackCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable;

   /**
    * Visits a CommitCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable;

   /**
    * Visits a OptimisticPrepareCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    */
   Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable;


   /**
    * Visits a CreateNodeCommand.
    *
    * @param ctx     invocation context
    * @param command command to visit
    * @return response from the visit
    * @throws Throwable in the event of problems.
    * @deprecated in 3.0.  Will be removed once optimistic and pessimistic locking is removed.
    */
   @Deprecated
   Object visitCreateNodeCommand(InvocationContext ctx, CreateNodeCommand command) throws Throwable;
}
