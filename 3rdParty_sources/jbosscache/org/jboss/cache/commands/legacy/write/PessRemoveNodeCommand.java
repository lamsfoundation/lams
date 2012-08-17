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
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.legacy.ReversibleCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.HashMap;
import java.util.Map;

/**
 * A version of {@link org.jboss.cache.commands.write.RemoveNodeCommand} which can be rolled back, for use with
 * pessimistic locking where changes are made directly on the data structures and may need to be reversed.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessRemoveNodeCommand extends RemoveNodeCommand implements ReversibleCommand
{
   protected Map originalData;

   public PessRemoveNodeCommand(GlobalTransaction globalTransaction, Fqn fqn)
   {
      super(globalTransaction, fqn);
   }

   public PessRemoveNodeCommand()
   {
   }

   @Override
   public Object perform(InvocationContext ctx)
   {
      NodeSPI targetNode = peekVersioned(ctx);
      if (targetNode != null)
      {
         Map data = targetNode.getDataDirect();
         if (data != null && !data.isEmpty()) originalData = new HashMap(data);
      }
      boolean found = (Boolean) super.perform(ctx);

      // now record rollback info.
      if (globalTransaction != null && found)
      {
         NodeSPI parentNode = targetNode.getParentDirect();
         prepareForRollback(parentNode);
      }
      return found;
   }

   @Override
   protected void recursivelyMarkAsRemoved(NodeSPI node, InvocationContext ctx)
   {
      node.markAsDeleted(true, true);
   }

   private void prepareForRollback(NodeSPI parentNode)
   {
      parentFqn = parentNode.getFqn();
      Map targetData = targetNode.getDataDirect();
      if (!targetData.isEmpty())
      {
         originalData = new HashMap(targetNode.getDataDirect());
      }
   }

   public void rollback()
   {
      if (targetNode != null)
      {
         Object childName = targetNode.getFqn().getLastElement();
         if (trace)
         {
            log.trace("rollback(parent: " + parentFqn + ", child: " + childName + ", node=" + targetNode + ")");
         }

         if (parentFqn == null || childName == null)
         {
            log.error("parent fqn or childName or childNode was null");
            return;
         }
         NodeSPI parentNode = dataContainer.peek(parentFqn);
         if (parentNode == null)
         {
            log.warn("node " + parentFqn + " not found");
            return;
         }
         parentNode.addChild(childName, targetNode);
         targetNode.markAsDeleted(false, true);
         targetNode.clearDataDirect();
         if (originalData != null) targetNode.putAllDirect(originalData);
         targetNode.setValid(true, true);
      }
   }
}
