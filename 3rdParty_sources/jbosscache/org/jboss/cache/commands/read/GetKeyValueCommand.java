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
package org.jboss.cache.commands.read;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.notifications.Notifier;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#get(String, Object)}
 * <p/>
 * This is the equivalent of the old MethodCallDefinitions.getKeyValueMethodLocal method call from 2.1.x.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2.0
 */
public class GetKeyValueCommand extends AbstractDataCommand
{
   public static final int METHOD_ID = 26;
   private static final Log log = LogFactory.getLog(GetKeyValueCommand.class);
   private static final boolean trace = log.isTraceEnabled();
   private Notifier notifier;

   private Object key;
   boolean sendNodeEvent;

   public GetKeyValueCommand(Fqn fqn, Object key, boolean sendNodeEvent)
   {
      this.fqn = fqn;
      this.key = key;
      this.sendNodeEvent = sendNodeEvent;
   }

   public GetKeyValueCommand()
   {
   }

   public void initialize(DataContainer dataContainer, Notifier notifier)
   {
      this.dataContainer = dataContainer;
      this.notifier = notifier;
   }

   /**
    * Retrieves the value stored under a specified key in a node referenced by the specified Fqn.
    *
    * @return an Object of type V, stored under a specific key in a node for a given Fqn, or null if the Fqn refers to a node that does not exist.
    */
   public Object perform(InvocationContext ctx)
   {
      NodeSPI n = ctx.lookUpNode(fqn);
      if (n == null)
      {
         if (trace) log.trace("Node not found");
         return null;
      }
      if (n.isDeleted())
      {
         if (trace) log.trace("Node has been deleted and is of type " + n.getClass().getSimpleName());
         return null;
      }
      if (sendNodeEvent) notifier.notifyNodeVisited(fqn, true, ctx);
      Object result = n.getDirect(key);
      if (trace) log.trace("Found value " + result);
      if (sendNodeEvent) notifier.notifyNodeVisited(fqn, false, ctx);
      return result;
   }


   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitGetKeyValueCommand(ctx, this);
   }

   public Object getKey()
   {
      return key;
   }

   public boolean isSendNodeEvent()
   {
      return sendNodeEvent;
   }

   public void setKey(Object key)
   {
      this.key = key;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      return new Object[]{fqn, key, sendNodeEvent};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
      key = args[1];
      sendNodeEvent = (Boolean) args[2];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      GetKeyValueCommand that = (GetKeyValueCommand) o;

      if (sendNodeEvent != that.sendNodeEvent) return false;
      if (key != null ? !key.equals(that.key) : that.key != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (key != null ? key.hashCode() : 0);
      result = 31 * result + (sendNodeEvent ? 1 : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "GetKeyValueCommand{" +
            "fqn=" + fqn +
            ", key=" + key +
            ", sendNodeEvent=" + sendNodeEvent +
            '}';
   }
}
