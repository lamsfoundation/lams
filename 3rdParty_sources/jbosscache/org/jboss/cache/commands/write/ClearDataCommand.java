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

import java.util.Map;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#clearData(String)}}
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class ClearDataCommand extends AbstractVersionedDataCommand
{
   public static final int METHOD_ID = 7;
   public static final int VERSIONED_METHOD_ID = 42;

   protected static final Log log = LogFactory.getLog(ClearDataCommand.class);
   protected static final boolean trace = log.isTraceEnabled();

   public ClearDataCommand(GlobalTransaction gtx, Fqn fqn)
   {
      this.globalTransaction = gtx;
      this.fqn = fqn;
   }

   public ClearDataCommand()
   {
   }

   /**
    * Clears the data map in the node referenced by the specified Fqn.
    *
    * @param ctx invocation context
    * @return null
    */
   public Object perform(InvocationContext ctx)
   {
      if (trace) log.trace("perform(" + globalTransaction + ", \"" + fqn + "\")");

      NodeSPI targetNode = peekVersioned(ctx);
      if (targetNode == null)
      {
         if (log.isDebugEnabled()) log.debug("node " + fqn + " not found");
         return null;
      }

      Map data = targetNode.getDataDirect();
      notifier.notifyNodeModified(fqn, true, NodeModifiedEvent.ModificationType.REMOVE_DATA, data, ctx);
      targetNode.clearDataDirect();
      notifier.notifyNodeModified(fqn, false, NodeModifiedEvent.ModificationType.REMOVE_DATA, data, ctx);
      return null;
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitClearDataCommand(ctx, this);
   }

   public int getCommandId()
   {
      return isVersioned() ? VERSIONED_METHOD_ID : METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      if (isVersioned())
      {
         return new Object[]{globalTransaction, fqn, true, dataVersion};
      }
      else
      {
         return new Object[]{globalTransaction, fqn, true};
      }
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
      fqn = (Fqn) args[1];
      if (isVersionedId(commandId)) dataVersion = (DataVersion) args[3];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      ClearDataCommand that = (ClearDataCommand) o;
      return !(globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null);
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (globalTransaction != null ? globalTransaction.hashCode() : 0);
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
      return "RemoveDataCommand{" +
            "fqn=" + fqn +
            ", dataVersion=" + dataVersion +
            ", globalTransaction=" + globalTransaction +
            '}';
   }
}
