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
package org.jboss.cache.commands.legacy.read;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessGetChildrenNamesCommand extends GetChildrenNamesCommand
{
   public PessGetChildrenNamesCommand()
   {
   }

   public PessGetChildrenNamesCommand(Fqn fqn)
   {
      super(fqn);
   }

   /**
    * Retrieves the names of children for a specific Fqn.
    *
    * @param ctx invocation context
    * @return a Set<Object> of child names, for a given Fqn, or null if the Fqn refers to a node that does not exist.
    */
   @SuppressWarnings("unchecked")
   @Override
   public Object perform(InvocationContext ctx)
   {
      NodeSPI<?, ?> n = fqn == null ? null : ctx.lookUpNode(fqn);
      if (n == null || n.isDeleted()) return null;
      Map<Object, ? extends Node<?, ?>> childrenMap = n.getChildrenMapDirect();
      Collection<NodeSPI<?, ?>> children = (Collection<NodeSPI<?, ?>>) (childrenMap.isEmpty() ? Collections.emptySet() : childrenMap.values());

      return getCorrectedChildNames(children);
   }

   private Set<Object> getCorrectedChildNames(Collection<NodeSPI<?, ?>> children)
   {
      // prune deleted children - JBCACHE-1136
      Set<Object> childNames = new HashSet<Object>();
      for (NodeSPI child : children)
      {
         if (!child.isDeleted())
         {
            Object e = child.getFqn().getLastElement();
            childNames.add(e);
         }
      }
      return childNames;
   }
}
