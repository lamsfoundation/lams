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

import java.util.Map;
import java.util.SortedMap;

/**
 * Used to contain a copy of the tree being worked on within the scope of a given transaction.
 * Maintains {@link WorkspaceNode}s rather than conventional
 * <p/>
 * Also see {@link org.jboss.cache.transaction.OptimisticTransactionContext}, which creates and maintains
 * an instance of TransactionWorkspace for each
 * transaction running.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @author Steve Woodcock (<a href="mailto:stevew@jofti.com">stevew@jofti.com</a>)
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public interface TransactionWorkspace<K, V>
{
   /**
    * @return Returns a map of {@link WorkspaceNode}s, keyed on {@link Fqn}
    */
   Map<Fqn, WorkspaceNode<K, V>> getNodes();

   /**
    * @param nodes The nodes to set. Takes {@link WorkspaceNode}s.
    */
   void setNodes(Map<Fqn, WorkspaceNode<K, V>> nodes);

   WorkspaceNode<K, V> getNode(Fqn fqn);

   /**
    * Is thread safe so you dont need to deal with synchronising access to this method.
    *
    * @param node
    */
   void addNode(WorkspaceNode<K, V> node);

   /**
    * Is thread safe so you dont need to deal with synchronising access to this method.
    */
   WorkspaceNode<K, V> removeNode(Fqn fqn);

   /**
    * Returns all nodes equal to or after the given node.
    */
   SortedMap<Fqn, WorkspaceNode<K, V>> getNodesAfter(Fqn fqn);

   /**
    * Tests if versioning is implicit for a given tx.
    * If set to true, the interceptor chain will handle versioning (implicit to JBossCache).
    * If set to false, DataVersions will have to come from the caller.
    */
   boolean isVersioningImplicit();

   /**
    * Sets if versioning is implicit for a given tx.
    * If set to true, the interceptor chain will handle versioning (implicit to JBossCache).
    * If set to false, DataVersions will have to come from the caller.
    */
   void setVersioningImplicit(boolean versioningImplicit);

   /**
    * returns true if the workspace contains a node with specified Fqn
    */
   boolean hasNode(Fqn fqn);

   /**
    * Empties the collection of nodes held by this workspace.
    */
   void clearNodes();
}
