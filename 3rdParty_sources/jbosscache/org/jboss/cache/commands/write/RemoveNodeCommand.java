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
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;

/**
 * Implements functionality defined by {@link org.jboss.cache.CacheSPI#removeNode(org.jboss.cache.Fqn)}
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class RemoveNodeCommand extends AbstractVersionedDataCommand
{
   public static final int METHOD_ID = 5;
   public static final int VERSIONED_METHOD_ID = 40;
   protected static final Log log = LogFactory.getLog(RemoveNodeCommand.class);
   protected static final boolean trace = log.isTraceEnabled();

   /*parameters*/
   private boolean skipSendingNodeEvents = false;
   protected Fqn parentFqn;
   protected NodeSPI targetNode;

   public RemoveNodeCommand(GlobalTransaction globalTransaction, Fqn fqn)
   {
      this.globalTransaction = globalTransaction;
      this.fqn = fqn;
   }

   public RemoveNodeCommand()
   {
   }

   /**
    * Removes the node referenced by the specified Fqn.
    */
   public Object perform(InvocationContext ctx)
   {
      if (trace) log.trace("perform(" + globalTransaction + ", " + fqn + ")");

      // Find the node
      targetNode = peekVersioned(ctx);
      if (targetNode == null || targetNode.isDeleted())
      {
         if (trace) log.trace("node " + fqn + " not found");
         return false;
      }
      notifyBeforeRemove(targetNode, ctx);

      boolean found = targetNode.isValid() && !targetNode.isDeleted();
      recursivelyMarkAsRemoved(targetNode, ctx);

      // make sure we clear all data on this node!
      targetNode.clearDataDirect();

      notifyAfterRemove(ctx);
      return found;
   }

   /**
    * Recursively marks a node as removed.
    *
    * @param node Node to mark
    * @param ctx  Invocation context
    */
   protected void recursivelyMarkAsRemoved(NodeSPI node, InvocationContext ctx)
   {
      node.markAsDeleted(true);
      Fqn parentFqn = node.getFqn();
      // recursion has to happen like this since child nodes are in the ctx.
      Map<Fqn, NodeSPI> nodes = ctx.getLookedUpNodes();
      for (Map.Entry<Fqn, NodeSPI> entry : nodes.entrySet())
      {
         if (entry.getKey().isChildOf(parentFqn)) entry.getValue().markAsDeleted(true);
      }
   }

   private void notifyBeforeRemove(NodeSPI n, InvocationContext ctx)
   {
      if (!skipSendingNodeEvents)
      {
         notifier.notifyNodeRemoved(fqn, true, n.getDataDirect(), ctx);
      }
   }

   private void notifyAfterRemove(InvocationContext ctx)
   {
      if (!skipSendingNodeEvents)
      {
         notifier.notifyNodeRemoved(fqn, false, null, ctx);
      }
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitRemoveNodeCommand(ctx, this);
   }

   public boolean isSkipSendingNodeEvents()
   {
      return skipSendingNodeEvents;
   }

   public int getCommandId()
   {
      return isVersioned() ? VERSIONED_METHOD_ID : METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      if (isVersioned())
         return new Object[]{globalTransaction, fqn, true, skipSendingNodeEvents, dataVersion};
      else
         return new Object[]{globalTransaction, fqn, true, skipSendingNodeEvents};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
      fqn = (Fqn) args[1];
      skipSendingNodeEvents = (Boolean) args[3];
      if (isVersionedId(commandId)) dataVersion = (DataVersion) args[4];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      RemoveNodeCommand that = (RemoveNodeCommand) o;

      if (skipSendingNodeEvents != that.skipSendingNodeEvents) return false;
      if (globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null)
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (globalTransaction != null ? globalTransaction.hashCode() : 0);
      result = 31 * result + (skipSendingNodeEvents ? 1 : 0);
      return result;
   }

   @Override
   protected boolean isVersionedId(int id)
   {
      return id == VERSIONED_METHOD_ID;
   }

   public void setSkipSendingNodeEvents(boolean skipSendingNodeEvents)
   {
      this.skipSendingNodeEvents = skipSendingNodeEvents;
   }

   @Override
   public String toString()
   {
      return "RemoveNodeCommand{" +
            "fqn=" + fqn +
            ", dataVersion=" + dataVersion +
            ", globalTransaction=" + globalTransaction +
            ", skipSendingNodeEvents=" + skipSendingNodeEvents +
            ", parentFqn=" + parentFqn +
            ", targetNode=" + targetNode +
            '}';
   }
}
