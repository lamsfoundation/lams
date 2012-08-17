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
package org.jboss.cache.mvcc;

import net.jcip.annotations.ThreadSafe;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.lock.NodeLock;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * A node reference that delegates all calls to a different {@link org.jboss.cache.InternalNode}.  Simple indirection
 * is all this class does, allowing other processes to change the delegate.
 * <p/>
 * The delegate is a volatile field so the class is thread safe.
 * <p/>
 * This is used to wrap all {@link org.jboss.cache.invocation.NodeInvocationDelegate}s in the {@link org.jboss.cache.DataContainer}
 * when using {@link org.jboss.cache.config.Configuration.NodeLockingScheme#MVCC} and {@link org.jboss.cache.lock.IsolationLevel#READ_COMMITTED}.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.mvcc.ReadCommittedNode
 * @since 3.0
 */
@ThreadSafe
public class NodeReference<K, V> implements InternalNode<K, V>
{
   transient volatile InternalNode<K, V> delegate;

   public NodeReference(InternalNode<K, V> delegate)
   {
      this.delegate = delegate;
   }

   /**
    * @return the InternalNode being delegated to.
    */
   public final InternalNode<K, V> getDelegate()
   {
      return delegate;
   }

   /**
    * Sets the internal node to delegate to.
    *
    * @param delegate node to delegate to.
    */
   public final void setDelegate(InternalNode<K, V> delegate)
   {
      this.delegate = delegate;
   }

   public final NodeSPI<K, V> getParent()
   {
      return delegate.getParent();
   }

   public final CacheSPI<K, V> getCache()
   {
      return delegate.getCache();
   }

   public final boolean isChildrenLoaded()
   {
      return delegate.isChildrenLoaded();
   }

   public final void setChildrenLoaded(boolean flag)
   {
      delegate.setChildrenLoaded(flag);
   }

   public final V get(K key)
   {
      return delegate.get(key);
   }

   public final Map<K, V> getData()
   {
      return delegate.getData();
   }

   public final V put(K key, V value)
   {
      return delegate.put(key, value);
   }

   public final NodeSPI<K, V> getOrCreateChild(Object child_name, GlobalTransaction gtx)
   {
      return delegate.getOrCreateChild(child_name, gtx);
   }

   public final InternalNode<K, V> getChild(Fqn f)
   {
      return delegate.getChild(f);
   }

   public final InternalNode<K, V> getChild(Object childName)
   {
      return delegate.getChild(childName);
   }

   public final Set<InternalNode<K, V>> getChildren()
   {
      return delegate.getChildren();
   }

   public final Set<InternalNode<K, V>> getChildren(boolean includeMarkedForRemoval)
   {
      return delegate.getChildren(includeMarkedForRemoval);
   }

   public final ConcurrentMap<Object, InternalNode<K, V>> getChildrenMap()
   {
      return delegate.getChildrenMap();
   }

   public final void setChildrenMap(ConcurrentMap<Object, InternalNode<K, V>> children)
   {
      delegate.setChildrenMap(children);
   }

   public final void addChild(Object nodeName, InternalNode<K, V> nodeToAdd)
   {
      delegate.addChild(nodeName, nodeToAdd);
   }

   public final void addChild(InternalNode<K, V> child)
   {
      delegate.addChild(child);
   }

   public final void addChild(InternalNode<K, V> child, boolean safe)
   {
      delegate.addChild(child, safe);
   }

   public final V remove(K key)
   {
      return delegate.remove(key);
   }

   public final NodeSPI<K, V> addChildDirect(Fqn f)
   {
      return delegate.addChildDirect(f);
   }

   public final NodeSPI<K, V> addChildDirect(Fqn f, boolean notify)
   {
      return delegate.addChildDirect(f, notify);
   }

   public final NodeSPI<K, V> addChildDirect(Object o, boolean notify)
   {
      return delegate.addChildDirect(o, notify);
   }

   public final void clear()
   {
      delegate.clear();
   }

   public final NodeSPI<K, V> getChildDirect(Fqn fqn)
   {
      return delegate.getChildDirect(fqn);
   }

   public final Set<Object> getChildrenNames()
   {
      return delegate.getChildrenNames();
   }

   public final Set<K> getKeys()
   {
      return delegate.getKeys();
   }

   public final boolean containsKey(K key)
   {
       return delegate.containsKey(key);
   }

   public final void setInternalState(Map state)
   {
      delegate.setInternalState(state);
   }

   public final boolean removeChild(Object childName)
   {
      return delegate.removeChild(childName);
   }

   public final boolean removeChild(Fqn f)
   {
      return delegate.removeChild(f);
   }

   public final Map<Object, Node<K, V>> getChildrenMapDirect()
   {
      return delegate.getChildrenMapDirect();
   }

   public final void setChildrenMapDirect(Map<Object, Node<K, V>> children)
   {
      delegate.setChildrenMapDirect(children);
   }

   public final void putAll(Map<? extends K, ? extends V> data)
   {
      delegate.putAll(data);
   }

   public final void removeChildren()
   {
      delegate.removeChildren();
   }

   public final void setVersion(DataVersion version)
   {
      delegate.setVersion(version);
   }

   public final DataVersion getVersion()
   {
      return delegate.getVersion();
   }

   public final Fqn getFqn()
   {
      return delegate.getFqn();
   }

   public final void setFqn(Fqn fqn)
   {
      delegate.setFqn(fqn);
   }

   public final NodeSPI<K, V> getChildDirect(Object childName)
   {
      return delegate.getChildDirect(childName);
   }

   public final Set<NodeSPI<K, V>> getChildrenDirect()
   {
      return delegate.getChildrenDirect();
   }

   public final boolean hasChildren()
   {
      return delegate.hasChildren();
   }

   public final Set<NodeSPI<K, V>> getChildrenDirect(boolean includeMarkedForRemoval)
   {
      return delegate.getChildrenDirect(includeMarkedForRemoval);
   }

   public final boolean isDataLoaded()
   {
      return delegate.isDataLoaded();
   }

   public final void setDataLoaded(boolean dataLoaded)
   {
      delegate.setDataLoaded(dataLoaded);
   }

   public final boolean isValid()
   {
      return delegate.isValid();
   }

   public final void setValid(boolean valid, boolean recursive)
   {
      delegate.setValid(valid, recursive);
   }

   public final boolean isLockForChildInsertRemove()
   {
      return delegate.isLockForChildInsertRemove();
   }

   public final void setLockForChildInsertRemove(boolean lockForChildInsertRemove)
   {
      delegate.setLockForChildInsertRemove(lockForChildInsertRemove);
   }

   public final Map getInternalState(boolean onlyInternalState)
   {
      return delegate.getInternalState(onlyInternalState);
   }

   public final void releaseObjectReferences(boolean recursive)
   {
      delegate.releaseObjectReferences(recursive);
   }

   public final boolean isRemoved()
   {
      return delegate.isRemoved();
   }

   public final void setRemoved(boolean marker)
   {
      delegate.setRemoved(marker);
   }

   public final void markAsRemoved(boolean marker, boolean recursive)
   {
      delegate.markAsRemoved(marker, recursive);
   }

   public final void setResident(boolean resident)
   {
      delegate.setResident(resident);
   }

   public final boolean isResident()
   {
      return delegate.isResident();
   }

   public final InternalNode<K, V> copy()
   {
      InternalNode<K, V> cloneDelegate = delegate.copy();
      return new NodeReference<K, V>(cloneDelegate);
   }

   public final NodeLock getLock()
   {
      return delegate.getLock();
   }

   public final void addChildDirect(Object nodeName, Node<K, V> nodeToAdd)
   {
      delegate.addChildDirect(nodeName, nodeToAdd);
   }

   public final void addChildDirect(NodeSPI<K, V> child)
   {
      delegate.addChildDirect(child);
   }

   public final void printDetails(StringBuilder sb, int indent)
   {
      delegate.printDetails(sb, indent);
   }

   @Override
   public final String toString()
   {
      return "NodeReference{" +
            "delegate=" + delegate +
            '}';
   }

   @Override
   public final boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      NodeReference that = (NodeReference) o;

      if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;

      return true;
   }

   @Override
   public final int hashCode()
   {
      return (delegate != null ? delegate.hashCode() : 0);
   }
}
