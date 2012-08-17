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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Wraps an existing map, which is not modified, reflecting modifications
 * in an internal modification set.
 * <p/>
 * This is to minimize the amount of data copying, for instance in the
 * case few changes are applied.
 * <p/>
 * Example usage:
 * <pre>
 * HashMap&lt;String, String> hm = new HashMap&lt;String, String>();
 * hm.put("a", "apple");
 * DeltaMap<String, String> dm = DeltaMap.create(hm);
 * dm.remove("a");
 * assert hm.containsKey("a");
 * assert !dm.containsKey("a");
 * dm.commit();
 * assert !hm.containsKey("a");
 * </pre>
 *
 * @author Elias Ross
 * @param <K> key type
 * @param <V> value type
 */
public class DeltaMap<K, V> extends AbstractMap<K, V>
{

   /**
    * Wrapped instance.
    */
   private Map<K, V> original;

   /**
    * Keys excluded.
    */
   private Set<K> exclude;

   /**
    * Keys changed.
    * This may contain new entries or entries modified.
    */
   private Map<K, V> changed = new HashMap<K, V>();

   /**
    * Constructs a new DeltaMap.
    */
   private DeltaMap(Map<K, V> original, Set<K> exclude)
   {
      if (original == null)
         throw new NullPointerException("original");
      if (exclude == null)
         throw new NullPointerException("exclude");
      this.original = original;
      this.exclude = exclude;
   }

   /**
    * Creates and returns a DeltaMap for an original map.
    *
    * @param original will not be modified, except by {@link #commit()}
    * @return a new instance
    */
   public static <K, V> DeltaMap<K, V> create(Map<K, V> original)
   {
      return new DeltaMap<K, V>(original, new HashSet<K>());
   }

   /**
    * Creates and returns a DeltaMap for an empty map.
    *
    * @return a new instance
    */
   public static <K, V> DeltaMap<K, V> create()
   {
      return create(new HashMap<K, V>(0));
   }

   /**
    * Creates and returns a DeltaMap for an original map, excluding some key mappings.
    *
    * @param original will not be modified, except by {@link #commit()}
    * @param exclude  entries not to include
    * @return a new instance
    */
   public static <K, V> DeltaMap<K, V> excludeKeys(Map<K, V> original, Set<K> exclude)
   {
      return new DeltaMap<K, V>(original, exclude);
   }

   /**
    * Creates and returns a DeltaMap for an original map, excluding some key mappings.
    */
   public static <K, V> DeltaMap<K, V> excludeKeys(Map<K, V> original, K... exclude)
   {
      return excludeKeys(original, new HashSet<K>(Arrays.asList(exclude)));
   }

   @Override
   public Set<java.util.Map.Entry<K, V>> entrySet()
   {
      return new AbstractSet<Entry<K, V>>()
      {

         @Override
         public Iterator<java.util.Map.Entry<K, V>> iterator()
         {
            return new WrappedIterator();
         }

         @Override
         public int size()
         {
            return DeltaMap.this.size();
         }

      };
   }

   @Override
   public int size()
   {
      int size = original.size();
      for (Object o : changed.keySet())
      {
         if (!original.containsKey(o))
            size++;
      }
      for (Object o : exclude)
      {
         if (original.containsKey(o))
            size--;
      }
      return size;
   }

   @Override
   public boolean containsKey(Object key)
   {
      if (exclude.contains(key))
         return false;
      if (changed.containsKey(key))
         return true;
      return original.containsKey(key);
   }

   @Override
   public V get(Object key)
   {
      if (exclude.contains(key))
         return null;
      if (changed.containsKey(key))
         return changed.get(key);
      return original.get(key);
   }

   @Override
   public V put(K key, V value)
   {
      V old;
      if (changed.containsKey(key))
         old = changed.get(key);
      else
         old = original.get(key);
      changed.put(key, value);
      if (exclude.contains(key))
      {
         exclude.remove(key);
         return null;
      }
      return old;
   }

   @Override
   @SuppressWarnings("unchecked")
   public V remove(Object key)
   {
      if (changed.containsKey(key))
      {
         if (original.containsKey(key))
            exclude.add((K) key);
         return changed.remove(key);
      }
      if (exclude.contains(key))
      {
         return null;
      }
      if (original.containsKey(key))
      {
         exclude.add((K) key);
         return original.get(key);
      }
      return null;
   }

   /**
    * Commits the changes to the original map.
    * Clears the list of removed keys.
    */
   public void commit()
   {
      original.keySet().removeAll(exclude);
      original.putAll(changed);
      exclude.clear();
      changed.clear();
   }

   /**
    * Returns true if the internal map was modified.
    */
   public boolean isModified()
   {
      return !changed.isEmpty() || !exclude.isEmpty();
   }

   /**
    * Iterator that skips over removed entries.
    *
    * @author Elias Ross
    */
   private class WrappedIterator implements Iterator<Entry<K, V>>
   {

      private boolean orig = true;
      private boolean nextSet = false;
      private Entry<K, V> next;
      private Iterator<Entry<K, V>> i = original.entrySet().iterator();

      private boolean redef(Entry<K, V> e)
      {
         K key = e.getKey();
         return exclude.contains(key) || changed.containsKey(key);
      }

      public boolean hasNext()
      {
         if (nextSet)
            return true;
         if (orig)
         {
            while (true)
            {
               if (!i.hasNext())
               {
                  orig = false;
                  i = changed.entrySet().iterator();
                  return hasNext();
               }
               next = i.next();
               if (!redef(next))
               {
                  nextSet = true;
                  return true;
               }
            }
         }
         if (!i.hasNext())
            return false;
         next = i.next();
         nextSet = true;
         return true;
      }

      public java.util.Map.Entry<K, V> next()
      {
         if (!hasNext())
            throw new NoSuchElementException();
         try
         {
            return next;
         }
         finally
         {
            nextSet = false;
         }
      }

      public void remove()
      {
         DeltaMap.this.remove(next.getKey());
      }

   }

   /**
    * Returns a debug string.
    */
   public String toDebugString()
   {
      return "DeltaMap original=" + original + " exclude=" + exclude + " changed=" + changed;
   }

   @Override
   public void clear()
   {
      exclude.addAll(original.keySet());
      changed.clear();
   }

   /**
    * Returns the original wrapped Map.
    */
   public Map<K, V> getOriginal()
   {
      return original;
   }

   /**
    * Sets the original values of this delta map.
    */
   public void setOriginal(Map<K, V> original)
   {
      if (original == null)
         throw new NullPointerException("original");
      this.original = original;
   }

   /**
    * Returns a Map of the entries changed, not including those removed.
    */
   public Map<K, V> getChanged()
   {
      return changed;
   }

   /**
    * Returns the entries removed, including entries excluded by the constructor.
    */
   public Set<K> getRemoved()
   {
      return exclude;
   }

}
