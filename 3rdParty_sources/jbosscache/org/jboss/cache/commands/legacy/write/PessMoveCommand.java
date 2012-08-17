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
package org.jboss.cache.commands.legacy.write;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeNotExistsException;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.legacy.ReversibleCommand;
import org.jboss.cache.commands.write.MoveCommand;

/**
 * A version of {@link org.jboss.cache.commands.write.MoveCommand} which can be rolled back, for use with
 * pessimistic locking where changes are made directly on the data structures and may need to be reversed.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessMoveCommand extends MoveCommand implements ReversibleCommand
{
   public PessMoveCommand()
   {
   }

   public PessMoveCommand(Fqn from, Fqn to)
   {
      super(from, to);
   }

   /**
    * Moves a node, from <tt>fqn</tt> to <tt>to</tt>, and returns null.
    *
    * @param ctx invocation context
    * @return null
    */
   @Override
   public Object perform(InvocationContext ctx)
   {
      move(fqn, to, false, ctx);
      return null;
   }

   private void adjustFqn(NodeSPI node, Fqn newBase)
   {
      Fqn newFqn = Fqn.fromRelativeElements(newBase, node.getFqn().getLastElement());
      node.setFqn(newFqn);
   }

   private void move(Fqn toMoveFqn, Fqn newParentFqn, boolean skipNotifications, InvocationContext ctx)
   {
      // the actual move algorithm.
      // ctx *could* be null if this is a rollback!!!  Sucks big time.
      NodeSPI newParent = ctx == null ? dataContainer.peek(newParentFqn) : ctx.lookUpNode(newParentFqn);
      if (newParent == null || newParent.isDeleted())
      {
         throw new NodeNotExistsException("New parent node " + newParentFqn + " does not exist when attempting to move node!!");
      }

      // ctx *could* be null if this is a rollback!!!  Sucks big time.
      NodeSPI node = ctx == null ? dataContainer.peek(toMoveFqn) : ctx.lookUpNode(toMoveFqn);

      if (node == null || node.isDeleted())
      {
         throw new NodeNotExistsException("Node " + toMoveFqn + " does not exist when attempting to move node!!");
      }

      if (trace) log.trace("Moving " + fqn + " to sit under " + to);

      NodeSPI oldParent = node.getParentDirect();
      Object nodeName = toMoveFqn.getLastElement();

      // now that we have the parent and target nodes:
      // first correct the pointers at the pruning point
      oldParent.removeChildDirect(nodeName);
      newParent.addChild(nodeName, node);
      // parent pointer is calculated on the fly using Fqns.

      // notify
      if (!skipNotifications)
         notifier.notifyNodeMoved(toMoveFqn, Fqn.fromRelativeElements(newParentFqn, toMoveFqn.getLastElement()), true, ctx);

      // now adjust Fqns of node and all children.
      adjustFqn(node, newParent.getFqn());

      if (!skipNotifications)
         notifier.notifyNodeMoved(toMoveFqn, Fqn.fromRelativeElements(newParentFqn, toMoveFqn.getLastElement()), false, ctx);
   }

   public void rollback()
   {
      move(Fqn.fromRelativeElements(to, fqn.getLastElement()), fqn.getParent(), true, null);
   }
}
