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
package org.jboss.cache;

import org.jboss.cache.lock.NodeLock;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * An internal node interface, that represents nodes that directly form the tree structure in the cache.  This is as opposed
 * to {@link Node} and its {@link NodeSPI} sub-interface, which are typically implemented by delegates which then delegate calls
 * to internal nodes, potentially after passing the call up an interceptor chain.
 * <p/>
 * All calls on an InternalNode are executed directly on the data structure.  Usually, XXXDirect() calls on {@link NodeSPI}
 * delegate to calls on an InternalNode, for example, {@link NodeSPI#putDirect(Object, Object)} delegating to {@link #put(Object, Object)}.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public interface InternalNode<K, V>
{
   // --------------- basic access to he internal state of the node.  -------------------------
   V get(K key);

   Map<K, V> getData();

   V put(K key, V value);

   void putAll(Map<? extends K, ? extends V> data);

   V remove(K key);

   void clear();

   Set<K> getKeys();

   boolean containsKey(K key);

   void setInternalState(Map state);

   Map getInternalState(boolean onlyInternalState);

   void releaseObjectReferences(boolean recursive);

   // --------------- node naming and tree structure ---------------------
   /**
    * @return the node's Fqn
    */
   Fqn getFqn();

   /**
    * Sets the node's Fqn
    *
    * @param fqn Fqn to set to
    */
   void setFqn(Fqn fqn);

   boolean hasChildren();

   // ***************** Old legacy methods that assume that the child map maintained contains NodeSPIs. These are still retained to support Optimistic and Pessimistic Locking ***************

   @Deprecated
   NodeSPI<K, V> getChildDirect(Fqn fqn);

   @Deprecated
   NodeSPI<K, V> getChildDirect(Object childName);

   @Deprecated
   Set<NodeSPI<K, V>> getChildrenDirect();

   @Deprecated
   Set<NodeSPI<K, V>> getChildrenDirect(boolean includeMarkedForRemoval);

   @Deprecated
   Map<Object, Node<K, V>> getChildrenMapDirect();

   @Deprecated
   void setChildrenMapDirect(Map<Object, Node<K, V>> children);

   @Deprecated
   void addChildDirect(Object nodeName, Node<K, V> nodeToAdd);

   @Deprecated
   void addChildDirect(NodeSPI<K, V> child);

   @Deprecated
   NodeSPI<K, V> addChildDirect(Fqn f);

   @Deprecated
   NodeSPI<K, V> addChildDirect(Fqn f, boolean notify);

   @Deprecated
   NodeSPI<K, V> addChildDirect(Object o, boolean notify);

   /**
    * @deprecated should use the {@link org.jboss.cache.NodeFactory} instead.
    */
   @Deprecated
   NodeSPI<K, V> getOrCreateChild(Object childName, GlobalTransaction gtx);

   // ***************** End old legacy methods.  See new versions below, which are supported by MVCC. *********************************

   InternalNode<K, V> getChild(Fqn f);

   InternalNode<K, V> getChild(Object childName);

   Set<InternalNode<K, V>> getChildren();

   Set<InternalNode<K, V>> getChildren(boolean includeMarkedForRemoval);

   ConcurrentMap<Object, InternalNode<K, V>> getChildrenMap();

   void setChildrenMap(ConcurrentMap<Object, InternalNode<K, V>> children);

   void addChild(Object nodeName, InternalNode<K, V> nodeToAdd);

   void addChild(InternalNode<K, V> child);

   /**
    * Same as above, except that if safe is true, any Fqn ancestry checking is skipped.  Don't set safe to true unless
    * you really know what you are doing!
    *
    * @param child child to add
    * @param safe  safety flag
    */
   void addChild(InternalNode<K, V> child, boolean safe);

   // *****************End new methods *****************


   Set<Object> getChildrenNames();

   void removeChildren();

   boolean removeChild(Object childName);

   boolean removeChild(Fqn f);


   /**
    * Creates a new instance of the same type and copies internal state.  Note that a shallow copy is made for all fields
    * except the data map, where a new map is created.
    *
    * @return a copy.
    */
   InternalNode<K, V> copy();


   // ------------- Getters and setters for various flags -------------
   boolean isChildrenLoaded();

   void setChildrenLoaded(boolean flag);

   boolean isDataLoaded();

   void setDataLoaded(boolean dataLoaded);

   boolean isValid();

   void setValid(boolean valid, boolean recursive);

   boolean isLockForChildInsertRemove();

   void setLockForChildInsertRemove(boolean lockForChildInsertRemove);

   boolean isRemoved();

   void setRemoved(boolean marker);

   void markAsRemoved(boolean marker, boolean recursive);

   void setResident(boolean resident);

   boolean isResident();

   // ------------- Utility stuff, mainly for backward compatibility -----------------

   void printDetails(StringBuilder sb, int indent);

   NodeSPI<K, V> getParent();

   CacheSPI<K, V> getCache();

   void setVersion(DataVersion version);

   DataVersion getVersion();

   NodeLock getLock();
}
