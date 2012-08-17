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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.jboss.cache.AbstractNode.NodeFlags.*;
import org.jboss.cache.marshall.MarshalledValue;
import org.jboss.cache.util.FastCopyHashMap;
import org.jboss.cache.util.Immutables;
import org.jboss.cache.util.concurrent.SelfInitializingConcurrentHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Basic data node class.  Throws {@link UnsupportedOperationException} for version-specific methods like {@link #getVersion()} and
 * {@link #setVersion(org.jboss.cache.optimistic.DataVersion)}, defined in {@link org.jboss.cache.NodeSPI}.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.0.0
 */
public class UnversionedNode<K, V> extends AbstractNode<K, V> implements InternalNode<K, V>
{
   /**
    * Debug log.
    */
   protected static Log log = LogFactory.getLog(UnversionedNode.class);
   protected static final boolean trace = log.isTraceEnabled();

   /**
    * Map of general data keys to values.
    */
   protected Map<K, V> data;
   protected NodeSPI<K, V> delegate;
   protected CacheSPI<K, V> cache;

   /**
    * Constructs a new node with an FQN of Root.
    */
   public UnversionedNode()
   {
      this.fqn = Fqn.ROOT;
      initFlags();
   }

   /**
    * Constructs a new node a given Fqn
    *
    * @param fqn fqn of the node
    */
   public UnversionedNode(Fqn fqn)
   {
      this.fqn = fqn;
      initFlags();
   }

   public UnversionedNode(Fqn fqn, CacheSPI<K, V> cache, boolean lockForChildInsertRemove)
   {
      initFlags();
      this.cache = cache;
      setLockForChildInsertRemove(lockForChildInsertRemove);
      this.fqn = fqn;

      // if this is a root node, create the child map.
      if (fqn.isRoot())
      {
         children = new ConcurrentHashMap<Object, InternalNode<K, V>>(64, .5f, 16);
      }
      else
      {
         // this always needs to be initialized.  The actual cost of the ConcurrentHashMap, however, is deferred.
         children = new SelfInitializingConcurrentHashMap<Object, InternalNode<K, V>>();
      }
   }

   public UnversionedNode(Fqn fqn, CacheSPI<K, V> cache, boolean lockForChildInsertRemove, Map<K, V> data)
   {
      this(fqn, cache, lockForChildInsertRemove);
      if (data != null) this.data = new FastCopyHashMap<K, V>(data);
   }

   /**
    * This method initialises flags on the node, by setting DATA_LOADED to true and VALID to true and all other flags to false.
    * The flags are defined in the {@link org.jboss.cache.AbstractNode.NodeFlags} enum.
    */
   protected void initFlags()
   {
      setFlag(DATA_LOADED);
      setFlag(VALID);
   }

   public NodeSPI<K, V> getDelegate()
   {
      return delegate;
   }

   public void setDelegate(NodeSPI<K, V> delegate)
   {
      this.delegate = delegate;
   }

   /**
    * Returns a parent by checking the TreeMap by name.
    */
   public NodeSPI<K, V> getParent()
   {
      if (fqn.isRoot())
      {
         return null;
      }
      return cache.peek(fqn.getParent(), true);
   }

   // does not need to be synchronized since this will only be accessed by a single thread in MVCC thanks to the write lock.
//   private void initDataMap()
//   {
//      if (data == null) data = new FastCopyHashMap<K, V>();
//   }

   public CacheSPI<K, V> getCache()
   {
      return cache;
   }

   public boolean isChildrenLoaded()
   {
      return isFlagSet(CHILDREN_LOADED);
   }

   public void setChildrenLoaded(boolean childrenLoaded)
   {
      setFlag(CHILDREN_LOADED, childrenLoaded);
   }

   public V get(K key)
   {
      return data == null ? null : data.get(key);
   }

   public Map<K, V> getData()
   {
      if (data == null) return Collections.emptyMap();
      return data;
   }

   public V put(K key, V value)
   {
      if (data == null)
      {
         // new singleton map!
         data = Collections.singletonMap(key, value);
         return null;
      }
      if (data.size() == 1 && data.containsKey(key))
      {
         V oldVal = data.get(key);
         data = Collections.singletonMap(key, value);
         return oldVal;
      }
      upgradeDataMap();
      return data.put(key, value);
   }

   @Override
   public InternalNode<K, V> getChild(Fqn f)
   {
      if (fqn.size() == 1)
      {
         return getChild(fqn.getLastElement());
      }
      else
      {
         InternalNode<K, V> currentNode = this;
         for (int i = 0; i < fqn.size(); i++)
         {
            Object nextChildName = fqn.get(i);
            currentNode = currentNode.getChild(nextChildName);
            if (currentNode == null) return null;
         }
         return currentNode;
      }
   }

   @Override
   public InternalNode<K, V> getChild(Object childName)
   {
      if (childName == null) return null;
      return children().get(childName);
   }

   @Override
   public Set<InternalNode<K, V>> getChildren()
   {
      // strip out deleted child nodes...
      if (children.isEmpty()) return Collections.emptySet();

      Set<InternalNode<K, V>> exclDeleted = new HashSet<InternalNode<K, V>>();
      for (InternalNode<K, V> n : children().values())
      {
         if (!n.isRemoved()) exclDeleted.add(n);
      }
      exclDeleted = Collections.unmodifiableSet(exclDeleted);
      return exclDeleted;
   }

   @Override
   public Set<InternalNode<K, V>> getChildren(boolean includeMarkedForRemoval)
   {
      if (includeMarkedForRemoval)
      {
         if (!children.isEmpty())
         {
            return Immutables.immutableSetConvert(children().values());
         }
         else
         {
            return Collections.emptySet();
         }
      }
      else
      {
         return getChildren();
      }
   }

   @Override
   public ConcurrentMap<Object, InternalNode<K, V>> getChildrenMap()
   {
      return children();
   }

   @Override
   public void setChildrenMap(ConcurrentMap<Object, InternalNode<K, V>> children)
   {
      this.children = children;
   }

   @Override
   public void addChild(Object nodeName, InternalNode<K, V> nodeToAdd)
   {
      if (nodeName != null)
      {
         children().put(nodeName, nodeToAdd);
      }
   }

   @Override
   public void addChild(InternalNode<K, V> child)
   {
      addChild(child, false);
   }

   @Override
   public void addChild(InternalNode<K, V> child, boolean safe)
   {
      Fqn<?> childFqn = child.getFqn();
      if (safe || childFqn.isDirectChildOf(fqn))
      {
         children().put(childFqn.getLastElement(), child);
      }
      else
      {
         throw new CacheException("Attempting to add a child [" + childFqn + "] to [" + fqn + "].  Can only add direct children.");
      }
   }

   public V remove(K key)
   {
      if (data == null) return null;
      V value;
      if (data instanceof FastCopyHashMap)
      {
         value = data.remove(key);
         downgradeDataMapIfNeeded();
      }
      else
      {
         // singleton maps cannot remove!
         value = data.get(key);
         data = null;
      }
      return value;
   }

   public void printDetails(StringBuilder sb, int indent)
   {
      printDetailsInMap(sb, indent);
   }

   /**
    * Returns a debug string.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getSimpleName());
      if (!isValid()) sb.append(" (invalid) ");

      if (isRemoved())
      {
         sb.append(" (deleted) [ ").append(fqn);
      }
      else
      {
         sb.append("[ ").append(fqn);
      }

      if (this instanceof VersionedNode)
      {
         sb.append(" version=").append(this.getVersion());
      }

      if (data != null)
      {
         if (trace)
         {
            sb.append(" data=").append(data.keySet());
         }
         else
         {
            sb.append(" data=[");
            Set keys = data.keySet();
            int i = 0;
            for (Object o : keys)
            {
               i++;
               sb.append(o);

               if (i == 5)
               {
                  int more = keys.size() - 5;
                  if (more > 1)
                  {
                     sb.append(", and ");
                     sb.append(more);
                     sb.append(" more");
                     break;
                  }
               }
               else
               {
                  sb.append(", ");
               }
            }
            sb.append("]");
         }
      }
      if (!children.isEmpty())
      {
         if (trace)
         {
            sb.append(" children=").append(getChildrenNames());
         }
         else
         {
            sb.append(" children=[");
            Set names = getChildrenNames();
            int i = 0;
            for (Object o : names)
            {
               i++;
               sb.append(o);

               if (i == 5)
               {
                  int more = names.size() - 5;
                  if (more > 1)
                  {
                     sb.append(", and ");
                     sb.append(more);
                     sb.append(" more");
                     break;
                  }
               }
               else
               {
                  sb.append(", ");
               }
            }
            sb.append("]");
         }
      }
      sb.append("]");
      return sb.toString();
   }

   public void clear()
   {
      data = null;
//      if (data != null) data.clear();
   }

   @SuppressWarnings("unchecked")
   public Set<Object> getChildrenNames()
   {
      return children.isEmpty() ? Collections.emptySet() : Immutables.immutableSetCopy(children.keySet());
   }

   public Set<K> getKeys()
   {
      if (data == null)
      {
         return Collections.emptySet();
      }
      return Immutables.immutableSetCopy(data.keySet());
   }

   public boolean containsKey(K key)
   {
      return data != null && data.containsKey(key);
   }

   public boolean removeChild(Object childName)
   {
      return children.remove(childName) != null;
   }

   public boolean removeChild(Fqn f)
   {
      if (f.size() == 1)
      {
         return removeChild(f.getLastElement());
      }
      else
      {
         NodeSPI<K, V> child = getChildDirect(f);
         return child != null && child.getParentDirect().removeChildDirect(f.getLastElement());
      }
   }

   public void putAll(Map<? extends K, ? extends V> data)
   {
      if (data != null)
      {
         if (this.data == null)
            this.data = copyDataMap(data);
         else
         if (this.data.size() == 1 && data.size() == 1 && this.data.keySet().iterator().next().equals(data.keySet().iterator().next()))
         {
            // replace key!
            Entry<? extends K, ? extends V> e = data.entrySet().iterator().next();

            // These casts are a work-around for an eclipse compiler bug, please don't remove ;)
            this.data = Collections.singletonMap((K)e.getKey(), (V) e.getValue());
         }
         else
         {
            // size. Do we need to update the existing data map to a FCHM?
            upgradeDataMap();
            this.data.putAll(data);
         }
      }
   }

   protected final void upgradeDataMap()
   {
      if (data != null && !(data instanceof FastCopyHashMap)) data = new FastCopyHashMap<K, V>(data);
   }

   protected final void downgradeDataMapIfNeeded()
   {
      if (data.size() == 1 && data instanceof FastCopyHashMap)
      {
         Entry<K, V> e = data.entrySet().iterator().next();
         data = Collections.singletonMap(e.getKey(), e.getValue());
      }
   }

   public void removeChildren()
   {
      children.clear();
   }

   public void markAsRemoved(boolean marker, boolean recursive)
   {
      setFlag(REMOVED, marker);

      if (recursive)
      {
         for (InternalNode<K, V> child : children().values()) child.markAsRemoved(marker, true);
      }
   }

   protected final void printIndent(StringBuilder sb, int indent)
   {
      if (sb != null)
      {
         for (int i = 0; i < indent; i++)
         {
            sb.append(" ");
         }
      }
   }

   /**
    * Returns the name of this node.
    */
   public Fqn getFqn()
   {
      return fqn;
   }

   public void setFqn(Fqn fqn)
   {
      if (trace)
      {
         log.trace(getFqn() + " set FQN " + fqn);
      }
      this.fqn = fqn;

      // invoke children
      for (Map.Entry<Object, InternalNode<K, V>> me : children().entrySet())
      {
         InternalNode<K, V> n = me.getValue();
         Fqn cfqn = Fqn.fromRelativeElements(fqn, me.getKey());
         n.setFqn(cfqn);
      }
   }

   public boolean hasChildren()
   {
      return !children.isEmpty();
   }

   /**
    * Adds details of the node into a map as strings.
    */
   protected void printDetailsInMap(StringBuilder sb, int indent)
   {
      printIndent(sb, indent);
      indent += 2;// increse it
      sb.append(Fqn.SEPARATOR);
      if (!fqn.isRoot()) sb.append(fqn.getLastElement());
      sb.append("  ");
      sb.append(data);
      for (InternalNode n : children().values())
      {
         sb.append("\n");
         n.printDetails(sb, indent);
      }
   }

   /**
    * Returns true if the data was loaded from the cache loader.
    */
   public boolean isDataLoaded()
   {
      return isFlagSet(DATA_LOADED);
   }

   /**
    * Sets if the data was loaded from the cache loader.
    */
   public void setDataLoaded(boolean dataLoaded)
   {
      setFlag(DATA_LOADED, dataLoaded);
   }

   public boolean isValid()
   {
      return isFlagSet(VALID);
   }

   public void setValid(boolean valid, boolean recursive)
   {
      setFlag(VALID, valid);

      if (trace) log.trace("Marking node " + getFqn() + " as " + (valid ? "" : "in") + "valid");
      if (recursive)
      {
         for (InternalNode<K, V> child : children().values())
         {
            child.setValid(valid, recursive);
         }
      }
   }

   public boolean isLockForChildInsertRemove()
   {
      return isFlagSet(LOCK_FOR_CHILD_INSERT_REMOVE);
   }

   public void setLockForChildInsertRemove(boolean lockForChildInsertRemove)
   {
      setFlag(LOCK_FOR_CHILD_INSERT_REMOVE, lockForChildInsertRemove);
   }

   @SuppressWarnings("unchecked")
   public InternalNode<K, V> copy()
   {
      UnversionedNode<K, V> n = new UnversionedNode<K, V>(fqn, cache, isFlagSet(LOCK_FOR_CHILD_INSERT_REMOVE));
      n.data = copyDataMap(data);
      copyInternals(n);
      return n;
   }

   protected void copyInternals(UnversionedNode n)
   {
      // direct reference to child map
      n.children = children;
      n.delegate = delegate;
      n.flags = flags;
   }

   public void setInternalState(Map state)
   {
      if (data == null)
      {
         data = copyDataMap(state);
      }
      else
      {
         // don't bother doing anything here
         putAll(state);
      }
   }

   protected final Map copyDataMap(Map<? extends K, ? extends V> toCopyFrom)
   {
      if (toCopyFrom != null && toCopyFrom.size() > 0)
      {
         Map map;
         if (toCopyFrom instanceof FastCopyHashMap)
         {
            map = (FastCopyHashMap<K, V>) ((FastCopyHashMap<K, V>) toCopyFrom).clone();
         }
         else if (toCopyFrom.size() == 1)
         {
            Entry<? extends K, ? extends V> e = toCopyFrom.entrySet().iterator().next();
            map = Collections.singletonMap(e.getKey(), e.getValue());
         }
         else
         {
            map = new FastCopyHashMap<K, V>(toCopyFrom);
         }
         return map;
      }
      return null;
   }

   public Map getInternalState(boolean onlyInternalState)
   {
      if (onlyInternalState)
         return new HashMap<K, V>(0);
      // don't bother doing anything here
      if (data == null) return new HashMap(0);
      return new HashMap<K, V>(data);
   }

   public void releaseObjectReferences(boolean recursive)
   {
      if (recursive)
      {
         for (InternalNode<K, V> child : children().values())
         {
            child.releaseObjectReferences(recursive);
         }
      }

      if (data != null)
      {
         for (K key : data.keySet())
         {
            // get the key first, before attempting to serialize stuff since data.get() may deserialize the key if doing
            // a hashcode() or equals().

            Object value = data.get(key);
            if (key instanceof MarshalledValue)
            {
               ((MarshalledValue) key).compact(true, true);
            }

            if (value instanceof MarshalledValue)
            {
               ((MarshalledValue) value).compact(true, true);
            }

         }
      }
   }

   /**
    * @return genericized version of the child map
    */
   @SuppressWarnings("unchecked")
   private ConcurrentMap<Object, InternalNode<K, V>> children()
   {
      return children;
   }
}
