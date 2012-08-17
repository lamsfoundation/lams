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
import org.jboss.cache.commands.legacy.read.LegacyGravitateDataCommand;
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
import org.jboss.cache.commands.legacy.write.LegacyEvictCommand;
import org.jboss.cache.commands.legacy.write.VersionedInvalidateCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.InvalidateCommand;

/**
 * Extends the default commands factory impl for optimistic locking.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed with opt locking
 */
@Deprecated
@SuppressWarnings("deprecation")
public class OptimisticCommandsFactoryImpl extends CommandsFactoryImpl
{
   @Override
   public EvictCommand buildEvictFqnCommand(Fqn fqn)
   {
      EvictCommand command = new LegacyEvictCommand(fqn);
      command.initialize(notifier, dataContainer);
      return command;
   }

   @Override
   public InvalidateCommand buildInvalidateCommand(Fqn fqn)
   {
      VersionedInvalidateCommand command = new VersionedInvalidateCommand(fqn);
      command.initialize(txManager);
      command.initialize(cacheSpi, dataContainer, notifier);
      return command;
   }

   @Override
   public GravitateDataCommand buildGravitateDataCommand(Fqn fqn, Boolean searchSubtrees)
   {
      LegacyGravitateDataCommand command = new LegacyGravitateDataCommand(fqn, searchSubtrees, rpcManager.getLocalAddress());
      command.initialize(dataContainer, cacheSpi, buddyFqnTransformer);
      return command;
   }

   @Override
   public CreateNodeCommand buildCreateNodeCommand(Fqn fqn)
   {
      CreateNodeCommand command = new CreateNodeCommand(fqn);
      command.initialize(dataContainer);
      return command;
   }


   @Override
   public ReplicableCommand fromStream(int id, Object[] parameters)
   {
      ReplicableCommand command;
      boolean skipSetParams = false;
      switch (id)
      {
         case CreateNodeCommand.METHOD_ID:
         {
            CreateNodeCommand returnValue = new CreateNodeCommand(null);
            returnValue.initialize(dataContainer);
            command = returnValue;
            break;
         }
         case GravitateDataCommand.METHOD_ID:
         {
            LegacyGravitateDataCommand returnValue = new LegacyGravitateDataCommand(rpcManager.getLocalAddress());
            returnValue.initialize(dataContainer, cacheSpi, buddyFqnTransformer);
            command = returnValue;
            break;
         }
         case InvalidateCommand.METHOD_ID:
         {
            VersionedInvalidateCommand returnValue = new VersionedInvalidateCommand(null);
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
