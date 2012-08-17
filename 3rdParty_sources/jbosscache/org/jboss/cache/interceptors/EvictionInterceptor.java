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

import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Region;
import org.jboss.cache.RegionManager;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.eviction.EvictionEvent;
import static org.jboss.cache.eviction.EvictionEvent.Type.*;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.CommandInterceptor;

/**
 * Eviction Interceptor.
 * <p/>
 * This interceptor is used to handle eviction events.
 *
 * @author Daniel Huang
 * @author Mircea.Markus@jboss.com
 * @version $Revision$
 */
public class EvictionInterceptor extends CommandInterceptor
{
   protected RegionManager regionManager;

   private DataContainer dataContainer;

   @Inject
   public void initialize(DataContainer dataContainer)
   {
      this.dataContainer = dataContainer;
   }

   /**
    * this method is for ease of unit testing. thus package access.
    * <p/>
    * Not to be attempted to be used anywhere else.
    */
   @Inject
   void setRegionManager(RegionManager regionManager)
   {
      this.regionManager = regionManager;
   }

   @Override
   public Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      Fqn fqn = command.getFqn();
      Object retVal = invokeNextInterceptor(ctx, command);
      // See if the node still exists; i.e. was only data removed
      // because it still has children.
      // If yes, put an ADD event in the queue so the node gets revisited
      boolean nodeIsNowAbsent = (retVal != null && (Boolean) retVal);
      if (!nodeIsNowAbsent)
      {
         Region r;
         if (fqn != null && (r = getRegion(fqn)) != null)
         {
            registerEvictionEventToRegionManager(fqn, ADD_NODE_EVENT, 0, r);
         }
      }
      return retVal;
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return visitPutKeyValueCommand(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      Region r;
      if (command.getFqn() != null && command.getKey() != null && (r = getRegion(command.getFqn())) != null)
      {
         registerEvictionEventToRegionManager(command.getFqn(), ADD_ELEMENT_EVENT, 1, r);
      }
      return retVal;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      Fqn fqn = command.getFqn();
      Region r;
      if (fqn != null && (r = getRegion(fqn)) != null)
      {
         if (command.getData() == null)
         {
            if (trace)
            {
               log.trace("Putting null data under fqn " + fqn + ".");
            }
         }
         else
         {
            int size;
            synchronized (command.getData())
            {
               size = command.getData().size();
            }
            registerEvictionEventToRegionManager(fqn, ADD_NODE_EVENT, size, r);
         }
      }
      return retVal;
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      if (retVal == null)
      {
         if (trace)
         {
            log.trace("No event added. Element does not exist");
         }

      }
      else
      {
         Fqn fqn = command.getFqn();
         Region r;
         if (fqn != null && command.getKey() != null && (r = getRegion(fqn)) != null)
         {
            registerEvictionEventToRegionManager(fqn, REMOVE_ELEMENT_EVENT, 1, r);
         }
      }
      return retVal;
   }

   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      return handleGetNodeOrDataCommands(retVal, command.getFqn());
   }

   private Object handleGetNodeOrDataCommands(Object retVal, Fqn fqn)
   {
      if (retVal == null)
      {
         if (trace)
         {
            log.trace("No event added. Node does not exist");
         }
      }
      else
      {
         Region r;
         if (fqn != null && (r = getRegion(fqn)) != null)
         {
            registerEvictionEventToRegionManager(fqn, VISIT_NODE_EVENT, 0, r);
         }
      }
      return retVal;
   }

   @Override
   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      return handleGetNodeOrDataCommands(retVal, command.getFqn());
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      Fqn fqn = command.getFqn();
      Region r;
      if (retVal == null)
      {
         if (trace)
         {
            log.trace("No event added. Element does not exist");
         }
      }
      else if (fqn != null && command.getKey() != null && (r = getRegion(fqn)) != null)
      {
         registerEvictionEventToRegionManager(fqn, VISIT_NODE_EVENT, 0, r);
      }
      return retVal;
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      Region r;
      if (command.getFqn() != null && (r = getRegion(command.getFqn())) != null)
      {
         registerEvictionEventToRegionManager(command.getFqn(), REMOVE_NODE_EVENT, 0, r);
      }
      return retVal;
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      Region r;
      if (command.getFqn() != null && (r = getRegion(command.getFqn())) != null)
      {
         registerEvictionEventToRegionManager(command.getFqn(), REMOVE_NODE_EVENT, 0, r);
      }
      return retVal;
   }

   private void registerEvictionEventToRegionManager(Fqn fqn, EvictionEvent.Type type, int elementDifference, Region region)
   {
      //we do not trigger eviction events for resident nodes
      if (dataContainer.isResident(fqn))
      {
         if (trace) log.trace("Ignoring Fqn " + fqn + " as it is marked as resident");
         return;
      }
      region.registerEvictionEvent(fqn, type, elementDifference);

      if (trace) log.trace("Registering event " + type + " on node " + fqn);
   }

   protected Region getRegion(Fqn fqn)
   {
      return regionManager.getRegion(fqn, Region.Type.EVICTION, false);
   }
}
