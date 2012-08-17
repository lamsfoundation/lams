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
import org.jboss.cache.commands.write.EvictCommand;

import java.util.Collection;

/**
 * Evict command for legacy locking schemes like OL and PL.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class LegacyEvictCommand extends EvictCommand
{
   public LegacyEvictCommand(Fqn fqn)
   {
      super(fqn);
   }

   public LegacyEvictCommand()
   {
   }

   @Override
   protected NodeSPI lookupForEviction(InvocationContext ctx, Fqn fqn)
   {
      return dataContainer.peek(fqn, false, true);
   }

   @Override
   protected Collection<Fqn> getRecursiveEvictionNodes()
   {
      return dataContainer.getNodesForEviction(fqn, true);
   }

   @Override
   protected boolean evictNode(Fqn fqn, InvocationContext ctx, NodeSPI node)
   {
      notifier.notifyNodeEvicted(fqn, true, ctx);
      try
      {
         if (node == null) return true;
         if (node.hasChildrenDirect() || fqn.isRoot())
         {
            if (trace) log.trace("removing DATA as node has children: evict(" + fqn + ")");
            node.clearDataDirect();
            node.setDataLoaded(false);
            return false;
         }
         else
         {
            if (trace) log.trace("removing NODE as it is a leaf: evict(" + fqn + ")");
            NodeSPI parentNode = lookupForEviction(ctx, fqn.getParent());

            if (parentNode != null)
            {
               parentNode.removeChildDirect(fqn.getLastElement());
               parentNode.setChildrenLoaded(false);
            }
            node.setValid(false, false);
            node.markAsDeleted(true);
            return true;
         }
      }
      finally
      {
         notifier.notifyNodeEvicted(fqn, false, ctx);
      }
   }
}
