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
package org.jboss.cache.lock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map which reduces concurrency and potential memory leaks for non-static ThreadLocals.
 * http://www.me.umn.edu/~shivane/blogs/cafefeed/2004/06/of-non-static-threadlocals-and-memory.html
 *
 * @author Brian Dueck
 *
 */
public class ThreadLocalMap<K, V> implements Map<K, V>
{

   private final ThreadLocal<Map<K, V>> threadLocal = new ThreadLocal<Map<K, V>>()
   {
      @Override
      protected Map<K, V> initialValue()
      {
         return new HashMap<K, V>();
      }
   };

   private Map<K, V> getThreadLocalMap()
   {
      return threadLocal.get();
   }

   public V put(K key, V value)
   {
      return getThreadLocalMap().put(key, value);
   }

   public V get(Object key)
   {
      return getThreadLocalMap().get(key);
   }

   public V remove(Object key)
   {
      return getThreadLocalMap().remove(key);
   }

   public int size()
   {
      return getThreadLocalMap().size();
   }

   public void clear()
   {
      getThreadLocalMap().clear();
   }

   public boolean isEmpty()
   {
      return getThreadLocalMap().isEmpty();
   }

   public boolean containsKey(Object arg0)
   {
      return getThreadLocalMap().containsKey(arg0);
   }

   public boolean containsValue(Object arg0)
   {
      return getThreadLocalMap().containsValue(arg0);
   }

   public Collection<V> values()
   {
      return getThreadLocalMap().values();
   }

   public void putAll(Map<? extends K, ? extends V> arg0)
   {
      getThreadLocalMap().putAll(arg0);
   }

   public Set<Map.Entry<K, V>> entrySet()
   {
      return getThreadLocalMap().entrySet();
   }

   public Set<K> keySet()
   {
      return getThreadLocalMap().keySet();
   }

}