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
package org.jboss.cache.invocation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Node;
import org.jboss.cache.NodeNotValidException;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.config.Option;
import org.jboss.cache.lock.NodeLock;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The delegate that users (and interceptor authors) interact with when they obtain a node from the cache or another node.
 * This wrapper delegates calls down the interceptor chain.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
public class NodeInvocationDelegate<K, V> extends AbstractInvocationDelegate implements NodeSPI<K, V>
{
   private static final Log log = LogFactory.getLog(NodeInvocationDelegate.class);
   private static final boolean trace = log.isTraceEnabled();

   protected volatile InternalNode<K, V> node;
   private CacheSPI<K, V> spi;

   public NodeInvocationDelegate(InternalNode<K, V> node)
   {
      this.node = node;
   }

   public InternalNode getDelegationTarget()
   {
      return node;
   }

   public void injectDependencies(CacheSPI<K, V> spi)
   {
      this.spi = spi;
   }

   public boolean isChildrenLoaded()
   {
      return node.isChildrenLoaded();
   }

   public void setChildrenLoaded(boolean loaded)
   {
      node.setChildrenLoaded(loaded);
   }

   public boolean isDataLoaded()
   {
      return node.isDataLoaded();
   }

   public void setDataLoaded(boolean dataLoaded)
   {
      node.setDataLoaded(dataLoaded);
   }

   public Map<Object, Node<K, V>> getChildrenMapDirect()
   {
      return node.getChildrenMapDirect();
   }

   public void setChildrenMapDirect(Map<Object, Node<K, V>> children)
   {
      node.setChildrenMapDirect(children);
   }

   public NodeSPI<K, V> getOrCreateChild(Object name, GlobalTransaction tx)
   {
      return node.getOrCreateChild(name, tx);
   }

   @SuppressWarnings("deprecation")
   public NodeLock getLock()
   {
      return node.getLock();
   }

   public void setFqn(Fqn f)
   {
      node.setFqn(f);
   }

   public boolean isDeleted()
   {
      return node.isRemoved();
   }

   public void markAsDeleted(boolean marker)
   {
      node.setRemoved(marker);
   }

   public void markAsDeleted(boolean marker, boolean recursive)
   {
      node.markAsRemoved(marker, recursive);
   }

   public void addChild(Object nodeName, Node<K, V> nodeToAdd)
   {
      node.addChildDirect(nodeName, nodeToAdd);
   }

   public void printDetails(StringBuilder sb, int indent)
   {
      node.printDetails(sb, indent);
   }

   public void print(StringBuilder sb, int indent)
   {
      throw new CacheException("This method is deprecated!");
   }

   public void setVersion(DataVersion version)
   {
      node.setVersion(version);
   }

   public DataVersion getVersion()
   {
      return node.getVersion();
   }

   public Set<NodeSPI<K, V>> getChildrenDirect()
   {
      return node.getChildrenDirect();
   }

   public void removeChildrenDirect()
   {
      node.removeChildren();
   }

   public Set<NodeSPI<K, V>> getChildrenDirect(boolean includeMarkedAsDeleted)
   {
      return node.getChildrenDirect(includeMarkedAsDeleted);
   }

   public NodeSPI<K, V> getChildDirect(Object childName)
   {
      return node.getChildDirect(childName);
   }

   public NodeSPI<K, V> addChildDirect(Fqn childName)
   {
      return node.addChildDirect(childName);
   }

   public NodeSPI<K, V> addChildDirect(Fqn f, boolean notify)
   {
      return node.addChildDirect(f, notify);
   }

   public NodeSPI<K, V> addChildDirect(Object childName, boolean notify)
   {
      return node.addChildDirect(childName, notify);
   }

   public void addChildDirect(NodeSPI<K, V> child)
   {
      node.addChildDirect(child);
   }

   public NodeSPI<K, V> getChildDirect(Fqn childName)
   {
      return node.getChildDirect(childName);
   }

   public boolean removeChildDirect(Fqn fqn)
   {
      return node.removeChild(fqn);
   }

   public boolean removeChildDirect(Object childName)
   {
      return node.removeChild(childName);
   }

   public V removeDirect(K key)
   {
      return node.remove(key);
   }

   public V putDirect(K key, V value)
   {
      return node.put(key, value);
   }

   public void putAllDirect(Map<? extends K, ? extends V> data)
   {
      node.putAll(data);
   }

   public Map<K, V> getDataDirect()
   {
      return node.getData();
   }

   public V getDirect(K key)
   {
      return node.get(key);
   }

   public void clearDataDirect()
   {
      node.clear();
   }

   public boolean containsKeyDirect(K key) {
       return node.containsKey(key);
   }

   public Set<K> getKeysDirect()
   {
      return node.getKeys();
   }

   public Set<Object> getChildrenNamesDirect()
   {
      return node.getChildrenNames();
   }

   public CacheSPI<K, V> getCache()
   {
      return spi;
   }

   public NodeSPI<K, V> getParent()
   {
      Fqn f = getFqn();
      if (f.isRoot()) return this;
      return spi.getNode(f.getParent());
   }

   public NodeSPI<K, V> getParentDirect()
   {
      return node.getParent();
   }

   public Set<Node<K, V>> getChildren()
   {
      assertValid();
      if (spi == null) return Collections.emptySet();
      Set<Node<K, V>> children = new HashSet<Node<K, V>>();
      for (Object c : spi.getChildrenNames(getFqn()))
      {
         Node<K, V> n = spi.getNode(Fqn.fromRelativeElements(getFqn(), c));
         if (n != null) children.add(n);
      }
      return Collections.unmodifiableSet(children);
   }

   public Set<Object> getChildrenNames()
   {
      assertValid();
      return spi.getChildrenNames(getFqn());
   }

   public boolean isLeaf()
   {
      assertValid();
      return getChildrenNames().isEmpty();
   }

   public Map<K, V> getData()
   {
      assertValid();
      if (spi == null) return Collections.emptyMap();
      return spi.getData(getFqn());
   }

   public Set<K> getKeys()
   {
      assertValid();
      Set<K> keys = spi.getKeys(getFqn());
      if (keys == null)
         return Collections.emptySet();
      else
         return Collections.unmodifiableSet(keys);
   }

   public Fqn getFqn()
   {
      return node.getFqn();
   }

   public Node<K, V> addChild(Fqn f)
   {
      // TODO: Revisit.  Is this really threadsafe?  See comment in putIfAbsent() - same solution should be applied here too.
      assertValid();
      Fqn nf = Fqn.fromRelativeFqn(getFqn(), f);
      Option o1 = spi.getInvocationContext().getOptionOverrides().copy();
      Node<K, V> child = getChild(f);

      if (child == null)
      {
         if (trace) log.trace("Child is null; creating.");
         Option o2 = o1.copy();
         spi.getInvocationContext().setOptionOverrides(o1);
         spi.put(nf, null);
         if (trace) log.trace("Created.  Now getting again.");
         spi.getInvocationContext().setOptionOverrides(o2);
         child = getChild(f);
         if (trace) log.trace("Got child " + child);
      }
      return child;
   }

   public boolean removeChild(Fqn f)
   {
      assertValid();
      return spi.removeNode(Fqn.fromRelativeFqn(getFqn(), f));
   }

   public boolean removeChild(Object childName)
   {
      assertValid();
      return spi.removeNode(Fqn.fromRelativeElements(getFqn(), childName));
   }

   public Node<K, V> getChild(Fqn f)
   {
      assertValid();
      return spi.getNode(Fqn.fromRelativeFqn(getFqn(), f));
   }

   public Node<K, V> getChild(Object name)
   {
      assertValid();
      return spi.getNode(Fqn.fromRelativeElements(getFqn(), name));
   }

   public V put(K key, V value)
   {
      assertValid();
      return spi.put(getFqn(), key, value);
   }

   public V putIfAbsent(K k, V v)
   {
      assertValid();
      // TODO: Refactor this!!  Synchronized block here sucks, this could lead to a deadlock since the locking interceptors will not use the same mutex.
      // will only work once we make calls directly on the UnversionedNode in the CallInterceptor rather than multiple calls via the CacheImpl.
      synchronized (this)
      {
         if (!getKeys().contains(k))
            return put(k, v);
         else
            return get(k);
      }
   }

   public V replace(K key, V value)
   {
      assertValid();
      // TODO: Refactor this!!  Synchronized block here sucks, this could lead to a deadlock since the locking interceptors will not use the same mutex.
      // will only work once we make calls directly on the UnversionedNode in the CallInterceptor rather than multiple calls via the CacheImpl.
      synchronized (this)
      {
         if (getKeys().contains(key))
         {
            return put(key, value);
         }
         else
            return null;
      }
   }

   public boolean replace(K key, V oldValue, V newValue)
   {
      assertValid();
      // TODO: Refactor this!!  Synchronized block here sucks, this could lead to a deadlock since the locking interceptors will not use the same mutex.
      // will only work once we make calls directly on the UnversionedNode in the CallInterceptor rather than multiple calls via the CacheImpl.
      synchronized (this)
      {
         if (oldValue.equals(get(key)))
         {
            put(key, newValue);
            return true;
         }
         else
            return false;
      }
   }

   public void putAll(Map<? extends K, ? extends V> data)
   {
      assertValid();
      spi.put(getFqn(), data);
   }

   public void replaceAll(Map<? extends K, ? extends V> data)
   {
      assertValid();
      spi.clearData(getFqn());
      spi.put(getFqn(), data);
   }

   public V get(K key)
   {
      assertValid();
      return spi.get(getFqn(), key);
   }

   public V remove(K key)
   {
      assertValid();
      return spi.remove(getFqn(), key);
   }

   public void clearData()
   {
      assertValid();
      spi.clearData(getFqn());
   }

   public int dataSize()
   {
      assertValid();
      return spi.getKeys(getFqn()).size();
   }

   public boolean hasChild(Fqn f)
   {
      // TODO: This could be made more efficient when calls are made directly on the node
      assertValid();
      return getChild(f) != null;
   }

   public boolean hasChild(Object o)
   {
      // TODO: This could be made more efficient when calls are made directly on the node
      assertValid();
      return getChild(o) != null;
   }

   public boolean isValid()
   {
      return node.isValid();
   }

   public boolean isResident()
   {
      return node.isResident();
   }

   public void setResident(boolean resident)
   {
      node.setResident(resident);
   }

   public boolean isLockForChildInsertRemove()
   {
      return node.isLockForChildInsertRemove();
   }

   public void setLockForChildInsertRemove(boolean lockForChildInsertRemove)
   {
      node.setLockForChildInsertRemove(lockForChildInsertRemove);
   }

   public void releaseObjectReferences(boolean recursive)
   {
      node.releaseObjectReferences(recursive);
   }

   public boolean hasChildrenDirect()
   {
      return node.hasChildren();
   }

   public Map getInternalState(boolean onlyInternalState)
   {
      return node.getInternalState(onlyInternalState);
   }

   public void setInternalState(Map state)
   {
      node.setInternalState(state);
   }

   public void setValid(boolean valid, boolean recursive)
   {
      node.setValid(valid, recursive);
   }

   protected void assertValid()
   {
      if (!getFqn().isRoot() && !node.isValid())
         throw new NodeNotValidException("Node " + getFqn() + " is not valid.  Perhaps it has been moved or removed.");
   }

   @Override
   public String toString()
   {
      return node == null ? "null" : node.toString();
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      NodeInvocationDelegate that = (NodeInvocationDelegate) o;

      if (node != null ? !node.equals(that.node) : that.node != null) return false;

      return true;
   }

   public int hashCode()
   {
      return (node != null ? node.hashCode() : 0);
   }

   // -------------- NO OP methods so that subclasses can work.  Specifically for MVCC, todo: rethink once we drop PL/OL support -----------
   public boolean isNullNode()
   {
      throw new UnsupportedOperationException();
   }

   public void markForUpdate(DataContainer container, boolean writeSkewCheck)
   {
      throw new UnsupportedOperationException();
   }

   public void commitUpdate(InvocationContext ctx, DataContainer container)
   {
      throw new UnsupportedOperationException();
   }

   public boolean isChanged()
   {
      throw new UnsupportedOperationException();
   }

   public boolean isCreated()
   {
      throw new UnsupportedOperationException();
   }

   public void setCreated(boolean b)
   {
      throw new UnsupportedOperationException();
   }

   public void rollbackUpdate()
   {
      throw new UnsupportedOperationException();
   }

}
