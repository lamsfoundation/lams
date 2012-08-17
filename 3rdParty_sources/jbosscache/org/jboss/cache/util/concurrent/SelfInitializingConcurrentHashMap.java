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
package org.jboss.cache.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Primarily used to hold child maps for nodes.  Underlying CHM is null initially, and once threads start
 * writing to this map, the CHM is initialized.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class SelfInitializingConcurrentHashMap<K, V> implements ConcurrentMap<K, V>
{
   private volatile ConcurrentMap<K, V> delegate;

   // -------------- initialization methods and helpers ----------------------
   private ConcurrentMap<K, V> getDelegate()
   {
      if (delegate == null) init();
      return delegate;
   }

   private synchronized void init()
   {
      // Reminiscent of DCL but the delegate here is volatile so construction reordering should not affect.
      if (delegate == null) delegate = new ConcurrentHashMap<K, V>(1, 0.75f, 4);
   }

   // -------------- Public API methods that will trigger initialization ----------------------

   public final V put(K key, V value)
   {
      return getDelegate().put(key, value);
   }

   public final V remove(Object key)
   {
      return getDelegate().remove(key);
   }

   public final void putAll(Map<? extends K, ? extends V> m)
   {
      getDelegate().putAll(m);
   }

   public final V putIfAbsent(K key, V value)
   {
      return getDelegate().putIfAbsent(key, value);
   }

   public final boolean replace(K key, V oldValue, V newValue)
   {
      return getDelegate().replace(key, oldValue, newValue);
   }

   public final V replace(K key, V value)
   {
      return getDelegate().replace(key, value);
   }

   // -------------- Public API methods that won't trigger initialization ----------------------

   public final boolean remove(Object key, Object value)
   {
      return delegate != null && delegate.remove(key, value);
   }

   public final int size()
   {
      return delegate == null ? 0 : delegate.size();
   }

   public final boolean isEmpty()
   {
      return delegate == null || delegate.isEmpty();
   }

   public final boolean containsKey(Object key)
   {
      return delegate != null && delegate.containsKey(key);
   }

   public final boolean containsValue(Object value)
   {
      return delegate != null && delegate.containsValue(value);
   }

   public final V get(Object key)
   {
      return delegate == null ? null : delegate.get(key);
   }

   public final void clear()
   {
      if (delegate != null) delegate.clear();
   }

   public final Set<K> keySet()
   {
      if (delegate == null || delegate.isEmpty()) return Collections.emptySet();
      return delegate.keySet();
   }

   public final Collection<V> values()
   {
      if (delegate == null || delegate.isEmpty()) return Collections.emptySet();
      return delegate.values();
   }

   public final Set<Entry<K, V>> entrySet()
   {
      if (delegate == null || delegate.isEmpty()) return Collections.emptySet();
      return delegate.entrySet();
   }

   @Override
   public String toString()
   {
      return "SelfInitializingConcurrentHashMap{" +
            "delegate=" + delegate +
            '}';
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SelfInitializingConcurrentHashMap that = (SelfInitializingConcurrentHashMap) o;
      return !(delegate != null ? !delegate.equals(that.delegate) : that.delegate != null);
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (delegate != null ? delegate.hashCode() : 0);
      return result;
   }
}
