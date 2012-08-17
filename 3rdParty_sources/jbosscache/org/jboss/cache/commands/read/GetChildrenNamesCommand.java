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

import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.mvcc.ReadCommittedNode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This command retrieves the names of children for a specific Fqn, as defined by {@link org.jboss.cache.Node#getChildrenNames()}
 * <p/>
 * This is the equivalent of the old MethodCallDefinitions.getChildrenNamesMethodLocal method call from 2.1.x.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2.0
 */
public class GetChildrenNamesCommand extends AbstractDataCommand
{
   public static final int METHOD_ID = 23;

   public GetChildrenNamesCommand()
   {
   }

   public GetChildrenNamesCommand(Fqn fqn)
   {
      this.fqn = fqn;
   }

   /**
    * Retrieves the names of children for a specific Fqn.
    *
    * @param ctx invocation context
    * @return a Set<Object> of child names, for a given Fqn, or null if the Fqn refers to a node that does not exist.
    */
   @SuppressWarnings("unchecked")
   public Object perform(InvocationContext ctx)
   {
      ReadCommittedNode n = (ReadCommittedNode) (fqn == null ? null : ctx.lookUpNode(fqn));
      if (n == null || n.isDeleted()) return null;
      Map<Object, InternalNode<?, ?>> childrenMap = n.getDelegationTarget().getChildrenMap();
      Collection<InternalNode> children = (Collection<InternalNode>) (childrenMap.isEmpty() ? Collections.emptySet() : childrenMap.values());

      return getCorrectedChildNames(children, ctx);
   }

   /**
    * Cleans the collection of children passed in to extract child names into a Set.  This implementation provides
    * additional filtering such as removing known deleted children and adding known created ones.
    *
    * @param children children set.  Must not be null.
    * @param ctx      invocation context
    * @return a set of valid children names.
    */
   @SuppressWarnings("unchecked")
   private Set<Object> getCorrectedChildNames(Collection<InternalNode> children, InvocationContext ctx)
   {
      Set<Object> childNames = new HashSet<Object>();

      for (InternalNode realChild : children)
      {
         Fqn childFqn = realChild.getFqn();
         NodeSPI childNode = ctx.lookUpNode(childFqn);

         // deletion flag should be checked on what we get from the ctx.
         // if childNode is null then it hasn't been removed in the current scope and hence should be included in this list.
         if (childNode == null || !childNode.isDeleted()) childNames.add(childFqn.getLastElement());
      }

      // now check for any *new* children added.
      for (Map.Entry<Fqn, NodeSPI> n : ctx.getLookedUpNodes().entrySet())
      {
         Fqn childFqn = n.getKey();

         if (childFqn.isDirectChildOf(fqn))
         {
            ReadCommittedNode childNode = (ReadCommittedNode) n.getValue();
            if (childNode != null && childNode.isCreated()) childNames.add(childFqn.getLastElement());
         }
      }
      return childNames;
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitGetChildrenNamesCommand(ctx, this);
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }
}
