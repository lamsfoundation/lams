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
package org.jboss.cache.optimistic;

import org.jboss.cache.Fqn;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Contains a mapping of Fqn to {@link WorkspaceNode}s.
 * Each entry corresponds to a series of changed nodes within the transaction.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @author Steve Woodcock (<a href="mailto:stevew@jofti.com">stevew@jofti.com</a>)
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class TransactionWorkspaceImpl<K, V> implements TransactionWorkspace<K, V>
{

   private Map<Fqn, WorkspaceNode<K, V>> nodes;
   private boolean versioningImplicit = true;

   public TransactionWorkspaceImpl()
   {
      nodes = new HashMap<Fqn, WorkspaceNode<K, V>>();
   }

   /**
    * Returns the nodes.
    */
   public Map<Fqn, WorkspaceNode<K, V>> getNodes()
   {
      return nodes;
   }

   public void clearNodes()
   {
      nodes.clear();
   }

   /**
    * Sets the nodes.
    */
   public void setNodes(Map<Fqn, WorkspaceNode<K, V>> nodes)
   {
      this.nodes = nodes;
   }

   public WorkspaceNode<K, V> getNode(Fqn fqn)
   {
      return nodes.get(fqn);
   }

   public void addNode(WorkspaceNode<K, V> node)
   {
      nodes.put(node.getFqn(), node);
   }

   public WorkspaceNode<K, V> removeNode(Fqn fqn)
   {
      return nodes.remove(fqn);
   }

   public SortedMap<Fqn, WorkspaceNode<K, V>> getNodesAfter(Fqn fqn)
   {
      SortedMap<Fqn, WorkspaceNode<K, V>> sm = new TreeMap<Fqn, WorkspaceNode<K, V>>();
      sm.putAll(nodes);
      return sm.tailMap(fqn);
   }

   public boolean isVersioningImplicit()
   {
      return versioningImplicit;
   }

   public void setVersioningImplicit(boolean versioningImplicit)
   {
      this.versioningImplicit = versioningImplicit;
   }

   public boolean hasNode(Fqn fqn)
   {
      return nodes.containsKey(fqn);
   }

   /**
    * Returns debug information.
    */
   @Override
   public String toString()
   {
      return "Workspace nodes=" + nodes;
   }
}
