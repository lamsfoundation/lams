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
package org.jboss.cache.marshall;

import org.jboss.cache.RPCManager;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.util.concurrent.ReclosableLatch;
import org.jgroups.Channel;
import org.jgroups.MembershipListener;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.blocks.RpcDispatcher;

/**
 * Extends {@link org.jgroups.blocks.RpcDispatcher} and adds the possibility that the marshaller may throw {@link org.jboss.cache.marshall.InactiveRegionException}s.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 */
public class InactiveRegionAwareRpcDispatcher extends CommandAwareRpcDispatcher
{
   org.jboss.cache.marshall.Marshaller requestMarshaller;

   /**
    * Only provide the flavour of the {@link RpcDispatcher} constructor that we care about.
    */
   public InactiveRegionAwareRpcDispatcher(Channel channel, MessageListener l, MembershipListener l2, Object serverObj,
                                           InvocationContextContainer container, InterceptorChain interceptorChain,
                                           ComponentRegistry componentRegistry, RPCManager manager)
   {
      super(channel, l, l2, serverObj, container, interceptorChain, componentRegistry, manager);
   }

   @Override
   public void setRequestMarshaller(Marshaller m)
   {
      super.setRequestMarshaller(m);
      requestMarshaller = (org.jboss.cache.marshall.Marshaller) m;
   }


   /**
    * Message contains MethodCall. Execute it against *this* object and return result.
    * Use MethodCall.invoke() to do this. Return result.
    */
   @Override
   public Object handle(Message req)
   {
      if (isValid(req))
      {
         RegionalizedMethodCall rmc;
         ReplicableCommand command;

         try
         {
            // we will ALWAYS be using the marshaller to unmarshall requests.
            rmc = requestMarshaller.regionalizedMethodCallFromByteBuffer(req.getBuffer());
            command = rmc.command;
         }
         catch (Throwable e)
         {
            if (e instanceof InactiveRegionException)
            {
               if (trace) log.trace("Exception from marshaller: " + e.getMessage());
               return null;
            }

            if (trace) log.error("exception unmarshalling object", e);
            return e;
         }

         try
         {
            Object retVal = executeCommand(command, req);
            return new RegionalizedReturnValue(retVal, rmc);
         }
         catch (Throwable x)
         {
            if (trace) log.trace("Problems invoking command", x);
            return x;
         }
      }
      else
      {
         return null;
      }
   }
}
