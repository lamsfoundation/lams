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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Minimizes Map memory usage by changing the map
 * instance based on the number of stored entries.
 *
 * @author Elias Ross
 */
public class MinMapUtil
{

   private static Class<?> singleton = Collections.singletonMap(null, null).getClass();

   private static Class<?> empty = Collections.emptyMap().getClass();

   private MinMapUtil()
   {
   }

   /**
    * Puts a mapping into a map, returns a map with the mapping.
    *
    * @param map destination
    */
   public static <K, V> Map<K, V> put(Map<K, V> map, K key, V value)
   {
      int size = map.size();
      if (size == 0)
         return Collections.singletonMap(key, value);
      if (size == 1)
      {
         HashMap<K, V> map2 = new HashMap<K, V>(map);
         map2.put(key, value);
         return map2;
      }
      else
      {
         map.put(key, value);
         return map;
      }
   }

   /**
    * Puts a number of entries into a map, returns a map.
    *
    * @param dest destination map
    * @param src  source map
    */
   public static <K, V> Map<K, V> putAll(Map<K, V> dest, Map<K, V> src)
   {
      if (src == null)
      {
         return dest;
      }
      int srcSize = src.size();
      if (srcSize == 0)
      {
         return dest;
      }
      if (srcSize == 1)
      {
         Entry<K, V> next = src.entrySet().iterator().next();
         return Collections.singletonMap(next.getKey(), next.getValue());
      }
      Class<?> c = dest.getClass();
      if (c == empty || c == singleton)
         return new HashMap<K, V>(src);
      dest.putAll(src);
      return dest;
   }

   /**
    * Removes a mapping by key from a map, returns the map.
    */
   public static <K, V> Map<K, V> remove(Map<K, V> map, K key)
   {
      int size = map.size();
      if (size == 0)
      {
         return map;
      }
      if (size == 1)
      {
         if (map.containsKey(key))
            return Collections.emptyMap();
         else
            return map;
      }
      map.remove(key);
      return map;
   }

}
