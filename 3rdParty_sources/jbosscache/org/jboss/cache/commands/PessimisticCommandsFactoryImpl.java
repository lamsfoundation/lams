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
import org.jboss.cache.commands.legacy.read.PessGetChildrenNamesCommand;
import org.jboss.cache.commands.legacy.write.PessClearDataCommand;
import org.jboss.cache.commands.legacy.write.PessMoveCommand;
import org.jboss.cache.commands.legacy.write.PessPutDataMapCommand;
import org.jboss.cache.commands.legacy.write.PessPutForExternalReadCommand;
import org.jboss.cache.commands.legacy.write.PessPutKeyValueCommand;
import org.jboss.cache.commands.legacy.write.PessRemoveKeyCommand;
import org.jboss.cache.commands.legacy.write.PessRemoveNodeCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;

/**
 * This specific implementation of {@link CommandsFactory} specifically creates
 * pessimistic commands where appropriate, with the ability to roll back.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.commands.legacy.ReversibleCommand
 * @since 3.0
 * @deprecated will be removed with possimistic locking
 */
@Deprecated
@SuppressWarnings("deprecation")
public class PessimisticCommandsFactoryImpl extends OptimisticCommandsFactoryImpl
{
   @Override
   public GetChildrenNamesCommand buildGetChildrenNamesCommand(Fqn fqn)
   {
      GetChildrenNamesCommand command = new PessGetChildrenNamesCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }

   @Override
   public PutDataMapCommand buildPutDataMapCommand(GlobalTransaction gtx, Fqn fqn, Map data)
   {
      PutDataMapCommand cmd = new PessPutDataMapCommand(gtx, fqn, data);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public PutKeyValueCommand buildPutKeyValueCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value)
   {
      PutKeyValueCommand cmd = new PessPutKeyValueCommand(gtx, fqn, key, value);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public PutForExternalReadCommand buildPutForExternalReadCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value)
   {
      PutForExternalReadCommand cmd = new PessPutForExternalReadCommand(gtx, fqn, key, value);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public RemoveNodeCommand buildRemoveNodeCommand(GlobalTransaction gtx, Fqn fqn)
   {
      RemoveNodeCommand cmd = new PessRemoveNodeCommand(gtx, fqn);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public ClearDataCommand buildClearDataCommand(GlobalTransaction gtx, Fqn fqn)
   {
      ClearDataCommand cmd = new PessClearDataCommand(gtx, fqn);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public RemoveKeyCommand buildRemoveKeyCommand(GlobalTransaction tx, Fqn fqn, Object key)
   {
      RemoveKeyCommand cmd = new PessRemoveKeyCommand(tx, fqn, key);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public MoveCommand buildMoveCommand(Fqn from, Fqn to)
   {
      MoveCommand cmd = new PessMoveCommand(from, to);
      cmd.initialize(notifier, dataContainer);
      return cmd;
   }

   @Override
   public InvalidateCommand buildInvalidateCommand(Fqn fqn)
   {
      InvalidateCommand command = new InvalidateCommand(fqn);
      command.initialize(cacheSpi, dataContainer, notifier);
      return command;
   }

   @Override
   public ReplicableCommand fromStream(int id, Object[] parameters)
   {
      ReplicableCommand command;
      boolean skipSetParams = false;
      switch (id)
      {
         case GetChildrenNamesCommand.METHOD_ID:
         {
            GetChildrenNamesCommand returnValue = new PessGetChildrenNamesCommand();
            returnValue.initialize(dataContainer);
            command = returnValue;
            break;
         }
         case MoveCommand.METHOD_ID:
         {
            MoveCommand returnValue = new PessMoveCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case PutDataMapCommand.METHOD_ID:
         case PutDataMapCommand.ERASE_METHOD_ID:
         case PutDataMapCommand.ERASE_VERSIONED_METHOD_ID:
         case PutDataMapCommand.VERSIONED_METHOD_ID:
         {
            PutDataMapCommand returnValue = new PessPutDataMapCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case PutKeyValueCommand.METHOD_ID:
         case PutKeyValueCommand.VERSIONED_METHOD_ID:
         {
            PutKeyValueCommand returnValue = new PessPutKeyValueCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case PutForExternalReadCommand.METHOD_ID:
         case PutForExternalReadCommand.VERSIONED_METHOD_ID:
         {
            PutForExternalReadCommand returnValue = new PessPutForExternalReadCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case ClearDataCommand.METHOD_ID:
         case ClearDataCommand.VERSIONED_METHOD_ID:
         {
            ClearDataCommand returnValue = new PessClearDataCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case RemoveKeyCommand.METHOD_ID:
         case RemoveKeyCommand.VERSIONED_METHOD_ID:
         {
            RemoveKeyCommand returnValue = new PessRemoveKeyCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }

         case RemoveNodeCommand.METHOD_ID:
         case RemoveNodeCommand.VERSIONED_METHOD_ID:
         {
            RemoveNodeCommand returnValue = new PessRemoveNodeCommand();
            returnValue.initialize(notifier, dataContainer);
            command = returnValue;
            break;
         }
         case InvalidateCommand.METHOD_ID:
         {
            InvalidateCommand returnValue = new InvalidateCommand(null);
            returnValue.initialize(cacheSpi, dataContainer, notifier);
            command = returnValue;
            break;
         }
         default:
            // pass up to superclass
            command = super.fromStream(id, parameters);
            skipSetParams = true;
      }

      if (!skipSetParams) command.setParameters(id, parameters);
      return command;
   }
}
