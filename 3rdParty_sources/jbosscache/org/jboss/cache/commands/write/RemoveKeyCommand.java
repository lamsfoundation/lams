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
package org.jboss.cache.commands.write;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.notifications.event.NodeModifiedEvent;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Collections;
import java.util.Map;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#remove(org.jboss.cache.Fqn, Object)}
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class RemoveKeyCommand extends AbstractVersionedDataCommand
{
   public static final int METHOD_ID = 6;
   public static final int VERSIONED_METHOD_ID = 41;

   private static final Log log = LogFactory.getLog(RemoveKeyCommand.class);
   private static final boolean trace = log.isTraceEnabled();

   /* parameters */
   protected Object key;

   public RemoveKeyCommand(GlobalTransaction gtx, Fqn fqn, Object key)
   {
      this.globalTransaction = gtx;
      this.fqn = fqn;
      this.key = key;
   }

   public RemoveKeyCommand()
   {
   }

   /**
    * Removes the specified key from the data map in the node referenced by the specified Fqn.
    *
    * @param ctx invocation context
    * @return the value being removed, if any, otherwise a null.
    */
   public Object perform(InvocationContext ctx)
   {
      if (trace) log.trace("perform(" + globalTransaction + ", \"" + fqn + "\", key=" + key + ")");

      NodeSPI n = ctx.lookUpNode(fqn);
      if (n == null)
      {
         if (log.isDebugEnabled()) log.debug("node " + fqn + " not found");
         return null;
      }
      if (notifier.shouldNotifyOnNodeModified())
      {
         notifier.notifyNodeModified(fqn, true, NodeModifiedEvent.ModificationType.REMOVE_DATA, n.getDataDirect(), ctx);
      }
      Object oldValue = n.removeDirect(key);
      if (notifier.shouldNotifyOnNodeModified())
      {
         Map removedData = Collections.singletonMap(key, oldValue);
         notifier.notifyNodeModified(fqn, false, NodeModifiedEvent.ModificationType.REMOVE_DATA, removedData, ctx);
      }
      return oldValue;
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitRemoveKeyCommand(ctx, this);
   }

   public Object getKey()
   {
      return key;
   }

   public void setKey(Object key)
   {
      this.key = key;
   }

   public int getCommandId()
   {
      return isVersioned() ? VERSIONED_METHOD_ID : METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      if (isVersioned())
         return new Object[]{globalTransaction, fqn, key, true, dataVersion};
      else
         return new Object[]{globalTransaction, fqn, key, true};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
      fqn = (Fqn) args[1];
      key = args[2];
      if (isVersionedId(commandId)) dataVersion = (DataVersion) args[4];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      RemoveKeyCommand that = (RemoveKeyCommand) o;

      if (globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null)
         return false;
      if (key != null ? !key.equals(that.key) : that.key != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (globalTransaction != null ? globalTransaction.hashCode() : 0);
      result = 31 * result + (key != null ? key.hashCode() : 0);
      return result;
   }

   @Override
   protected boolean isVersionedId(int id)
   {
      return id == VERSIONED_METHOD_ID;
   }

   @Override
   public String toString()
   {
      return "RemoveKeyCommand{" +
            "fqn=" + fqn +
            ", dataVersion=" + dataVersion +
            ", globalTransaction=" + globalTransaction +
            ", key=" + key +
            '}';
   }
}
