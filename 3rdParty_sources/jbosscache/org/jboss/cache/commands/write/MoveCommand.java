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
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.read.AbstractDataCommand;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#move(org.jboss.cache.Fqn, org.jboss.cache.Fqn)}
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class MoveCommand extends AbstractDataCommand implements WriteCommand
{
   public static final int METHOD_ID = 36;
   protected static final Log log = LogFactory.getLog(MoveCommand.class);
   protected static final boolean trace = log.isTraceEnabled();

   /* dependencies */
   protected Notifier notifier;

   /* params */
   protected Fqn to;
   protected GlobalTransaction globalTransaction;

   public MoveCommand()
   {
   }

   public void initialize(Notifier notifier, DataContainer dataContainer)
   {
      this.notifier = notifier;
      this.dataContainer = dataContainer;
   }

   public MoveCommand(Fqn from, Fqn to)
   {
      this.fqn = from;
      this.to = to;
   }

   public GlobalTransaction getGlobalTransaction()
   {
      return globalTransaction;
   }

   public void setGlobalTransaction(GlobalTransaction globalTransaction)
   {
      this.globalTransaction = globalTransaction;
   }

   /**
    * Moves a node, from <tt>fqn</tt> to <tt>to</tt>, and returns null.
    *
    * @param ctx invocation context
    * @return null
    */
   public Object perform(InvocationContext ctx)
   {
      if (fqn.isDirectChildOf(to))
      {
         if (log.isDebugEnabled()) log.debug("Attempting to move " + fqn + " onto itself.  Nothing to do.");
         return null;
      }

      NodeSPI node = ctx.lookUpNode(fqn);

      if (node == null || node.isDeleted())
      {
         if (trace) log.trace("Node " + fqn + " does not exist when attempting to move node!  Not doing anything.");
         return null;
      }

      if (trace) log.trace("Moving " + fqn + " to sit under " + to);

      // the actual move algorithm.
      NodeSPI newNode = ctx.lookUpNode(Fqn.fromRelativeElements(to, fqn.getLastElement()));
      Fqn newNodeFqn = newNode.getFqn();

      // at this stage all child node objects we need have been created and are available in the ctx.
      // we just need to mark old ones as deleted, new ones as created, and move data across.
      notifier.notifyNodeMoved(fqn, newNodeFqn, true, ctx);
      moveRecursively(node, newNode, ctx);
      notifier.notifyNodeMoved(fqn, newNodeFqn, false, ctx);
      return null;
   }

   @SuppressWarnings("unchecked")
   private void moveRecursively(NodeSPI oldNode, NodeSPI newNode, InvocationContext ctx)
   {
      if (trace) log.trace("Moving " + oldNode.getFqn() + " to " + newNode.getFqn());
      // start deep.
      Map<Object, InternalNode> children = oldNode.getDelegationTarget().getChildrenMap();
      if (!children.isEmpty())
      {
         for (InternalNode child : children.values())
         {
            Fqn childFqn = child.getFqn();
            Fqn newChildFqn = childFqn.replaceAncestor(oldNode.getFqn(), newNode.getFqn());
            moveRecursively(ctx.lookUpNode(childFqn), ctx.lookUpNode(newChildFqn), ctx);
         }
      }

      // now swap the data for the current node.
      newNode.getDelegationTarget().putAll(oldNode.getDelegationTarget().getData());
      oldNode.markAsDeleted(true);
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitMoveCommand(ctx, this);
   }

   public Fqn getTo()
   {
      return to;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      return new Object[]{fqn, to};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
      to = (Fqn) args[1];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      MoveCommand that = (MoveCommand) o;

      if (to != null ? !to.equals(that.to) : that.to != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (to != null ? to.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "MoveCommand{" +
            "fqn=" + fqn +
            ", to=" + to +
            '}';
   }
}
