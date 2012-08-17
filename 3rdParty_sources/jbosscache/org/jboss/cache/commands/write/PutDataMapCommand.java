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
import org.jboss.cache.NodeNotExistsException;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.notifications.event.NodeModifiedEvent;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Collections;
import java.util.Map;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#put(String, java.util.Map)}.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class PutDataMapCommand extends AbstractVersionedDataCommand
{
   public static final int METHOD_ID = 1;
   public static final int ERASE_METHOD_ID = 2;
   public static final int VERSIONED_METHOD_ID = 37;
   public static final int ERASE_VERSIONED_METHOD_ID = 38;

   protected static final Log log = LogFactory.getLog(PutDataMapCommand.class);
   protected static final boolean trace = log.isTraceEnabled();

   /* parameters*/
   protected Map data;
   
   /* true to erase existing data */
   protected boolean erase;

   public PutDataMapCommand(GlobalTransaction globalTransaction, Fqn fqn, Map data)
   {
      this.globalTransaction = globalTransaction;
      this.fqn = fqn;
      this.data = data;
   }

   public PutDataMapCommand()
   {
   }

   /**
    * Adds the provided data map to the data map in the node referenced by the specified Fqn.
    */
   public Object perform(InvocationContext ctx)
   {
      if (trace)
      {
         log.trace("perform(" + globalTransaction + ", \"" + fqn + "\", " + data + ")");
      }
//      NodeSPI nodeSPI = dataContainer.peekStrict(globalTransaction, fqn, false);
      NodeSPI nodeSPI = ctx.lookUpNode(fqn);
      if (nodeSPI == null) throw new NodeNotExistsException("Node " + fqn + " does not exist!");
      Map existingData = nodeSPI.getDataDirect();

      if (notifier.shouldNotifyOnNodeModified())
      {
         notifier.notifyNodeModified(fqn, true, NodeModifiedEvent.ModificationType.PUT_MAP, existingData == null ? Collections.emptyMap() : existingData, ctx);
      }

      if (erase)
      {
	      nodeSPI.clearDataDirect();
      }
      nodeSPI.putAllDirect(data);
      if (notifier.shouldNotifyOnNodeModified())
      {
         notifier.notifyNodeModified(fqn, false, NodeModifiedEvent.ModificationType.PUT_MAP, nodeSPI.getDataDirect(), ctx);
      }
      return null;
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitPutDataMapCommand(ctx, this);
   }

   public Map getData()
   {
      return data;
   }

   public void setData(Map data)
   {
      this.data = data;
   }

   public int getCommandId()
   {
      if (isVersioned())
      {
         return erase ? ERASE_VERSIONED_METHOD_ID : VERSIONED_METHOD_ID;
      }
      else
      {
         return erase ? ERASE_METHOD_ID : METHOD_ID;
      }
   }

   @Override
   public Object[] getParameters()
   {
      if (isVersioned())
         return new Object[]{globalTransaction, fqn, data, false, dataVersion};
      else
         return new Object[]{globalTransaction, fqn, data, false};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
      fqn = (Fqn) args[1];
      data = (Map) args[2];
      if (isVersionedId(commandId)) dataVersion = (DataVersion) args[4];
   }

   @Override
   protected boolean isVersionedId(int id)
   {
      return id == VERSIONED_METHOD_ID || id == ERASE_VERSIONED_METHOD_ID;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      PutDataMapCommand that = (PutDataMapCommand) o;
      if (data != null ? !data.equals(that.data) : that.data != null) return false;
      if (erase != that.erase) return false;
      if (globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null)
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (globalTransaction != null ? globalTransaction.hashCode() : 0);
      result = 31 * result + (data != null ? data.hashCode() : 0);
      if (erase)
	      result++;
      return result;
   }

   /**
    * Sets a flag indicating the node data should be erased.
    */
   public void setErase(boolean erase)
   {
      this.erase = erase;
   }
   
   /**
    * Returns a flag indicating the node data should be erased.
    */
   public boolean isErase()
   {
      return erase;
   }

   @Override
   public String toString()
   {
      return "PutDataMapCommand{" +
            "fqn=" + fqn +
            ", dataVersion=" + dataVersion +
            ", data=" + data +
            ", globalTransaction=" + globalTransaction +
            ", erase=" + erase +
            '}';
   }

}
