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
package org.jboss.cache.util;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.loader.CacheLoader;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Adaptors for {@link Cache} classes, such as {@link Node}.
 * This is useful for integration of JBoss Cache into existing applications.
 * <p/>
 * Example use:
 * <pre>
 * Cache c = ...;
 * Map m = Caches.asMap(c);
 * m.put("a", "b"); // null
 * m.containsKey("a"); // true
 * m.remove("a"); // "b"
 * </pre>
 */
public class Caches
{

   private Caches()
   {
   }

   /**
    * Returns a {@link Map} from the root node.
    *
    * @param cache cache to wrap as a map
    * @return a map representation of the cache
    * @see #asMap(Node)
    */
   public static <K, V> Map<K, V> asMap(Cache<K, V> cache)
   {
      if (cache == null) throw new NullPointerException("cache");
      return asMap(cache.getRoot());
   }

   /**
    * Returns a {@link Map}, where map keys are named children of the given Node,
    * and values are kept under a single key for this node.
    * The map may be safely concurrently modified through this Map or externally,
    * and its contents reflect the cache state and
    * existing data of the Node.
    * This means that {@link ConcurrentModificationException} is never thrown
    * and all methods are thread safe.
    * <p/>
    * The map is not serializable.
    * <p/>
    * Usage note:  As a single node is used for every key, it is most efficient to store
    * data for a single entity (e.g. Person) in a single object.
    * <p/>
    * Also, when using a {@link CacheLoader} for storage, keys used must be valid as
    * part of the {@link Fqn} used in calls. Generally speaking, simple string values are
    * preferred.
    *
    * @param node node in a cache to wrap
    * @return a Map representation of the cache
    */
   public static <K, V> Map<K, V> asMap(Node<K, V> node)
   {
      return new MapNode<K, V>(node);
   }

   /**
    * Returns a {@link Map}, where map data is put and returned directly from a single Node.
    * This method is "simple" as data is kept under a single node.
    * Note that storing all data in a single Node can be inefficient when using persistence,
    * replication, or transactions.
    * The map may be safely concurrently modified through this Map or externally.
    * This means that {@link ConcurrentModificationException} is never thrown
    * and all methods are thread safe.
    * <p/>
    * The methods {@link Map#entrySet} and {@link Map#values} and {@link Map#keySet}
    * do not allow for modification of the Node.
    * Further all these methods return a collection which is a snapshot (copy)
    * of the data at time of calling. This may be very inefficient.
    * <p/>
    * The map is not serializable.
    *
    * @param node node to wrap
    * @return Map representation of the cache
    */
   public static <K, V> Map<K, V> asSimpleMap(Node<K, V> node)
   {
      return new SimpleMapNode<K, V>(node);
   }

   /**
    * Returns a {@link Set}, where set entries are data entries of the given Node.
    * This method is "simple" as data is kept under a single node.
    * <p/>
    * Note that storing all data in a single Node can be inefficient when using persistence,
    * replication, or transactions.
    * The set may be safely concurrently modified through this Map or externally.
    * This means that {@link ConcurrentModificationException} is never thrown
    * and all methods are thread safe.
    * <p/>
    * The set is not serializable.
    *
    * @param node node to wrap
    * @return a Set representation of the values in a node
    */
   public static <K, V> Set<V> asSimpleSet(Node<K, V> node)
   {
      return new SimpleSetNode<V>(node);
   }

   /**
    * Returns a {@link Map}, where map entries are partitioned into
    * children nodes, within a cache node.
    * The default child selector divides the data into 128 child nodes based on hash code.
    * Note that for large data sets, the number of child nodes should be increased.
    *
    * @param node node to cache under
    * @return a Map representation of the cache
    */
   @SuppressWarnings("unchecked")
   public static <K, V> Map<K, V> asPartitionedMap(Node<K, V> node)
   {
      return new PartitionedMapNode<K, V>(node, HashKeySelector.DEFAULT);
   }

   /**
    * Returns a {@link Map}, where map entries are partitioned
    * into children, within a cache node, by key hash code.
    * <p/>
    * The map is not serializable.
    * <p/>
    * Usage note:  This is a performance (and size) compromise between {@link #asMap(Node)}
    * and {@link #asSimpleMap(Node)}. For applications using a {@link org.jboss.cache.loader.CacheLoader},
    * {@link #asMap(Node)} is a better choice.
    * <p/>
    *
    * @param node node to cache under
    * @param ss   selector strategy that chooses a segment based on key
    * @return a Map representation of the cache
    */
   public static <K, V> Map<K, V> asPartitionedMap(Node<K, V> node, ChildSelector<K> ss)
   {
      return new PartitionedMapNode<K, V>(node, ss);
   }

   /**
    * Returns a {@link Map}, where map entries are partitioned into child nodes,
    * within the cache root, by key hash code.
    *
    * @param cache cache to use
    * @return a Map representation of the cache
    */
   public static <K, V> Map<K, V> asPartitionedMap(Cache<K, V> cache)
   {
      return asPartitionedMap(cache.getRoot());
   }

   /**
    * Computes an improved hash code from an object's hash code.
    */
   static protected final int hashCode(int i)
   {
      i ^= i >>> 20 ^ i >>> 12;
      return i ^ i >>> 7 ^ i >>> 4;
   }

   /**
    * Returns a segment ({@link Node#getChild(Object) child node name})
    * to use based on the characteristics of a key.
    * <p/>
    * Here is an example class which selects a child based on a person's department:
    * <pre>
    * public static class DepartmentSelector implements ChildSelector&lt;Person&gt;
    * {
    *    public Object childName(Person key)
    *    {
    *       return key.getDepartment();
    *    }
    * }
    * </pre>
    */
   public interface ChildSelector<T>
   {
      /**
       * Returns a child node name for a key.
       *
       * @param key for calls to {@link Map#put}, {@link Map#get} etc.
       * @return node name
       */
      Fqn childName(T key);
   }

   /**
    * Class that returns a child name to use based on the hash code of a key.
    */
   public static class HashKeySelector<T> implements ChildSelector<T>
   {

      static ChildSelector DEFAULT = new HashKeySelector(128);

      protected int segments;

      /**
       * Constructs with N segments, where N must be a power of 2.
       *
       * @param segments Number of hash segments
       */
      public HashKeySelector(int segments)
      {
         this.segments = segments;
         if (Integer.bitCount(segments) != 1)
            throw new IllegalArgumentException();
         if (segments <= 0)
            throw new IllegalArgumentException();
      }

      /**
       * Returns the segment for this key, in the inclusive range 0 to {@link #segments} - 1.
       */
      protected final int segmentFor(T key)
      {
         if (key == null)
            return 0;
         int hc = key.hashCode();
         return Caches.hashCode(hc) & (segments - 1);
      }

      /**
       * Returns the node name for this segment.
       */
      protected final Fqn childName(int segment)
      {
         return Fqn.fromElements(Integer.toString(segment));
      }

      /**
       * Returns the node name for this key.
       * By default, returns a String containing the segment.
       */
      public final Fqn childName(T key)
      {
         return childName(segmentFor(key));
      }

      @Override
      public String toString()
      {
         return super.toString() + " segments=" + segments;
      }

   }

   static class MapNode<K, V> extends AbstractMap<K, V>
   {

      public static final String KEY = "K";

      private Node node; // purposefully un-genericized

      public MapNode(Node<K, V> node)
      {
         if (node == null)
            throw new NullPointerException("node");
         this.node = node;
      }

      @Override
      public Set<Map.Entry<K, V>> entrySet()
      {
         return new AbstractSet<Map.Entry<K, V>>()
         {

            @Override
            public Iterator<Map.Entry<K, V>> iterator()
            {
               final Iterator<Node<K, V>> i = set().iterator();
               return new Iterator<Map.Entry<K, V>>()
               {

                  Object name;

                  boolean next = false;

                  public boolean hasNext()
                  {
                     return i.hasNext();
                  }

                  @SuppressWarnings("unchecked")
                  public Entry<K, V> next()
                  {
                     Node n = i.next();
                     this.name = n.getFqn().getLastElement();
                     this.next = true;
                     Object key = n.get(KEY);
                     return new SimpleImmutableEntry(name, key);
                  }

                  public void remove()
                  {
                     if (!next)
                        throw new IllegalStateException();
                     node.removeChild(name);
                  }

                  @Override
                  public String toString()
                  {
                     return "Itr name=" + name;
                  }

               };
            }

            @SuppressWarnings("unchecked")
            private Set<Node<K, V>> set()
            {
               return node.getChildren();
            }

            @Override
            public int size()
            {
               return set().size();
            }

         };
      }

      @Override
      public void clear()
      {
         for (Object o : node.getChildrenNames())
            node.removeChild(o);
      }

      @Override
      public boolean containsKey(Object arg0)
      {
         return node.getChild(arg0) != null;
      }

      @Override
      @SuppressWarnings("unchecked")
      public V get(Object arg0)
      {
         Node child = node.getChild(arg0);
         if (child == null)
            return null;
         return (V) child.get(KEY);
      }

      @Override
      public boolean isEmpty()
      {
         return node.getChildrenNames().isEmpty();
      }

      @Override
      public Set<K> keySet()
      {

         return new AbstractSet<K>()
         {

            private Set set()
            {
               return node.getChildrenNames();
            }

            @Override
            public Iterator<K> iterator()
            {
               final Iterator i = set().iterator();
               return new Iterator<K>()
               {

                  K child;

                  public boolean hasNext()
                  {
                     return i.hasNext();
                  }

                  @SuppressWarnings("unchecked")
                  public K next()
                  {
                     child = (K) i.next();
                     return child;
                  }

                  public void remove()
                  {
                     if (child == null)
                        throw new IllegalStateException();
                     node.removeChild(child);
                     // since set is read-only, invalidate
                  }

               };
            }

            @Override
            public boolean remove(Object key)
            {
               return node.removeChild(key);
            }

            @Override
            public int size()
            {
               return set().size();
            }

         };

      }

      @Override
      @SuppressWarnings("unchecked")
      public V put(K arg0, V arg1)
      {
         return (V) node.addChild(Fqn.fromElements(arg0)).put(KEY, arg1);
      }

      @Override
      @SuppressWarnings("unchecked")
      public V remove(Object arg0)
      {
         Node child = node.getChild(arg0);
         if (child == null)
            return null;
         V o = (V) child.remove(KEY);
         node.removeChild(arg0);
         return o;
      }

      @Override
      public int size()
      {
         return node.getChildrenNames().size();
      }

   }

   static class SimpleMapNode<K, V> extends AbstractMap<K, V>
   {

      private Node<K, V> node;

      public SimpleMapNode(Node<K, V> node)
      {
         if (node == null)
            throw new NullPointerException("node");
         this.node = node;
      }

      @Override
      public void clear()
      {
         node.clearData();
      }

      @Override
      public boolean containsKey(Object key)
      {
         return node.getKeys().contains(key);
      }

      @Override
      public boolean containsValue(Object value)
      {
         return node.getData().containsValue(value);
      }

      /**
       * getData returns a snapshot of the data.
       */
      @Override
      public Set<Map.Entry<K, V>> entrySet()
      {
         return node.getData().entrySet();
      }

      @Override
      @SuppressWarnings("unchecked")
      public V get(Object key)
      {
         return node.get((K) key);
      }

      @Override
      public Set<K> keySet()
      {
         return node.getKeys();
      }

      @Override
      public V put(K key, V value)
      {
         return node.put(key, value);
      }

      @Override
      @SuppressWarnings("unchecked")
      public void putAll(Map<? extends K, ? extends V> map)
      {
         node.putAll((Map) map);
      }

      @Override
      @SuppressWarnings("unchecked")
      public V remove(Object key)
      {
         return node.remove((K) key);
      }

      @Override
      public int size()
      {
         return node.dataSize();
      }

   }

   static class SimpleSetNode<K> extends AbstractSet<K> implements java.util.Set<K>
   {

      private Node node;

      private static final String VALUE = "V";

      public <K, V> SimpleSetNode(Node<K, V> node)
      {
         if (node == null)
            throw new NullPointerException("node");
         this.node = node;
      }

      @Override
      public void clear()
      {
         node.clearData();
      }

      @Override
      public boolean contains(Object key)
      {
         return node.getKeys().contains(key);
      }

      @Override
      @SuppressWarnings("unchecked")
      public boolean remove(Object key)
      {
         return node.remove(key) != null;
      }

      @Override
      public int size()
      {
         return node.dataSize();
      }

      @Override
      @SuppressWarnings("unchecked")
      public boolean add(K arg0)
      {
         return node.put(arg0, VALUE) == null;
      }

      @Override
      public Iterator<K> iterator()
      {
         final Iterator i = node.getKeys().iterator();
         return new Iterator<K>()
         {
            K key;

            boolean next = false;

            public boolean hasNext()
            {
               return i.hasNext();
            }

            @SuppressWarnings("unchecked")
            public K next()
            {
               key = (K) i.next();
               next = true;
               return key;
            }

            @SuppressWarnings("unchecked")
            public void remove()
            {
               if (!next)
                  throw new IllegalStateException();
               node.remove(key);
            }

         };
      }

   }

   static class PartitionedMapNode<K, V> extends AbstractMap<K, V>
   {

      private NodeSPI node;

      private ChildSelector<K> selector;

      public PartitionedMapNode(Node<K, V> node, ChildSelector<K> selector)
      {
         this.node = (NodeSPI) node;
         this.selector = selector;
      }

      @Override
      public Set<Map.Entry<K, V>> entrySet()
      {
         return new AbstractSet<Map.Entry<K, V>>()
         {

            Iterator<Node> ci = node.getChildren().iterator();

            @Override
            public Iterator<Entry<K, V>> iterator()
            {
               return new Iterator<Entry<K, V>>()
               {

                  Iterator ni;

                  {
                     nextChild();
                     findNext();
                  }

                  @SuppressWarnings("unchecked")
                  private void nextChild()
                  {
                     ni = new SimpleMapNode(ci.next()).entrySet().iterator();
                  }

                  private void findNext()
                  {
                     while (!ni.hasNext())
                     {
                        if (!ci.hasNext())
                           return;
                        nextChild();
                     }
                  }

                  public boolean hasNext()
                  {
                     return ni.hasNext();
                  }

                  @SuppressWarnings("unchecked")
                  public Entry<K, V> next()
                  {
                     Entry<K, V> n = (Entry<K, V>) ni.next();
                     findNext();
                     return n;
                  }

                  public void remove()
                  {
                     ni.remove();
                  }

               };
            }

            @Override
            public int size()
            {
               return PartitionedMapNode.this.size();
            }

         };
      }

      @Override
      @SuppressWarnings("unchecked")
      public Set keySet()
      {
         return new AbstractSet<Map.Entry<K, V>>()
         {

            @Override
            @SuppressWarnings("unchecked")
            public Iterator<Entry<K, V>> iterator()
            {
               return (Iterator<Entry<K, V>>) PartitionedMapNode.super.keySet().iterator();
            }

            @Override
            public boolean remove(Object o)
            {
               boolean key = PartitionedMapNode.this.containsKey(o);
               PartitionedMapNode.this.remove(o);
               return key;
            }

            @Override
            public boolean contains(Object o)
            {
               return PartitionedMapNode.this.containsKey(o);
            }

            @Override
            public int size()
            {
               return PartitionedMapNode.super.keySet().size();
            }

         };
      }

      @Override
      public void clear()
      {
         for (Object o : node.getChildrenNames())
            node.getChild(o).clearData();
      }

      @SuppressWarnings("unchecked")
      private Fqn fqnFor(Object o)
      {
         return Fqn.fromRelativeFqn(node.getFqn(), selector.childName((K) o));
      }

      @Override
      @SuppressWarnings("unchecked")
      public boolean containsKey(Object o)
      {
         Fqn fqn = fqnFor(o);
         Set keys = node.getCache().getKeys(fqn);
         return keys != null && keys.contains(o);
      }

      @Override
      @SuppressWarnings("unchecked")
      public V get(Object key)
      {
         return (V) node.getCache().get(fqnFor(key), key);
      }

      @SuppressWarnings("unchecked")
      public Object put(Object key, Object value)
      {
         return node.getCache().put(fqnFor(key), key, value);
      }

      @SuppressWarnings("unchecked")
      public V remove(Object key)
      {
         return (V) node.getCache().remove(fqnFor(key), key);
      }

      public int size()
      {
         int size = 0;
         for (Object o : node.getChildrenNames())
         {
            Node child = node.getChild(o);
            size += child.dataSize();
         }
         return size;
      }
   }
}
