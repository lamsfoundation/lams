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

import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a type of {@link org.jboss.cache.Node} that is to be copied into a {@link TransactionWorkspace} for optimistically locked
 * nodes.  Adds versioning and dirty flags over conventional Nodes.
 * <p/>
 * Typically used when the cache mode configured is {@link org.jboss.cache.config.Configuration.NodeLockingScheme#OPTIMISTIC}
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @author Steve Woodcock (<a href="mailto:stevew@jofti.com">stevew@jofti.com</a>)
 * @since 1.3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public interface WorkspaceNode<K, V>// extends Node<K, V>
{
   Fqn getFqn();

   /**
    * Returns 2 Sets - a set of children added (first set) and a set of children removed.
    *
    * @return a merged map of child names and Nodes
    */
   List<Set<Fqn>> getMergedChildren();

   /**
    * Retrieves the data version of the in-memory node.
    *
    * @return A data version
    */
   DataVersion getVersion();

   /**
    * Sets the data version of this workspace node.
    *
    * @param version a {@link org.jboss.cache.optimistic.DataVersion} implementation.
    */
   void setVersion(DataVersion version);

   /**
    * A node is considered modified if its data map has changed.  If children are added or removed, the node is not
    * considered modified - instead, see {@link #isChildrenModified()}.
    *
    * @return true if the data map has been modified
    */
   boolean isModified();

   /**
    * A convenience method that returns whether a node is dirty, i.e., it has been created, deleted or modified.
    * Noe that adding or removing children does not constitute a node being dirty - see {@link #isChildrenModified()}
    *
    * @return true if the node has been either created, deleted or modified.
    */
   boolean isDirty();

   /**
    * Attempts to merge data changed during the current transaction with the data in the underlying tree.
    *
    * @return a merged map of key/value pairs.
    */
   Map<K, V> getMergedData();

   /**
    * Retrieves a reference to the underlying {@link NodeSPI} instance.
    *
    * @return a node
    */
   NodeSPI<K, V> getNode();

   /**
    * Retrieves a TransactionWorkspace instance associated with the current transaction, which the current WorkspaceNode instance
    * lives in.
    *
    * @return a TransactionWorkspace.
    */
   TransactionWorkspace getTransactionWorkspace();

   /**
    * @return true if the instance was created in the current transaction; i.e., it did not exist in the underlying data tree.
    */
   boolean isCreated();

   /**
    * Marks the instance as having been created in the current transaction.
    */
   void markAsCreated();

   /**
    * Creates a child node.
    *
    * @param child_name Object name of the child to create
    * @param parent     A reference to the parent node
    * @param cache      CacheSPI instance to create this node in
    * @param version    DataVersion to apply to the child.  If null, {@link DefaultDataVersion#ZERO} will be used.
    * @return a NodeSPI pointing to the newly created child.
    */
   NodeSPI createChild(Object child_name, NodeSPI<K, V> parent, CacheSPI<K, V> cache, DataVersion version);

   /**
    * Tests whether versioning for the WorkspaceNode instance in the current transaction is implicit (i.e., using {@link org.jboss.cache.optimistic.DefaultDataVersion}
    * rather than a custom {@link org.jboss.cache.optimistic.DataVersion} passed in using {@link org.jboss.cache.config.Option#setDataVersion(DataVersion)})
    *
    * @return true if versioning is implicit.
    */
   boolean isVersioningImplicit();

   /**
    * Sets whether versioning for the WorkspaceNode instance in the current transaction is implicit (i.e., using {@link org.jboss.cache.optimistic.DefaultDataVersion}
    * rather than a custom {@link org.jboss.cache.optimistic.DataVersion} passed in using {@link org.jboss.cache.config.Option#setDataVersion(DataVersion)})
    *
    * @param b set to true if versioning is implicit, false otherwise.
    */
   void setVersioningImplicit(boolean b);

   /**
    * Overrides {@link Node#getChild(Object)} to return a {@link org.jboss.cache.NodeSPI} rather than a {@link org.jboss.cache.Node}
    *
    * @param o node name
    * @return a child node
    */
   NodeSPI<K, V> getChildDirect(Object o);

   /**
    * Overrides {@link Node#getChild(Fqn)} to return a {@link org.jboss.cache.NodeSPI} rather than a {@link org.jboss.cache.Node}
    *
    * @param f node fqn
    * @return a child node
    */
   NodeSPI<K, V> getChildDirect(Fqn f);

   /**
    * Adds a given WorkspaceNode to the current node's child map
    *
    * @param workspaceNode
    */
   void addChild(WorkspaceNode<K, V> workspaceNode);

   /**
    * @return true if children have been added to or removed from this node.  Not the same as 'dirty'.
    */
   boolean isChildrenModified();

   /**
    * @return true if the child map has been loaded from the underlying data structure into the workspace.
    */
   boolean isChildrenLoaded();

   /**
    * @return teur if this node has been resurrected, i.e., it was deleted and re-added within the same transaction
    */
   boolean isResurrected();

   /**
    * Marks a node as resurrected, i.e., deleted and created again within the same transaction
    *
    * @param resurrected
    */
   void markAsResurrected(boolean resurrected);

   boolean isRemoved();

   void setRemoved(boolean marker);

   void markAsRemoved(boolean marker, boolean recursive);

   void clearData();

   Map<K, V> getData();

   V remove(K removeKey);

   V get(K key);

   Set<K> getKeys();

   Set<Object> getChildrenNames();

   boolean removeChild(Object nodeName);

   void putAll(Map<K, V> data);

   V put(K key, V value);
}
