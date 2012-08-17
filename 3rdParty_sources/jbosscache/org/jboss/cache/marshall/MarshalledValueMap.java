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
package org.jboss.cache.marshall;

import net.jcip.annotations.Immutable;
import org.jboss.cache.CacheException;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Map that is able to wrap/unwrap MarshalledValues in keys or values.  Note that calling keySet(), entrySet() or values()
 * could be expensive if this map is large!!
 * <p/>
 * Also note that this is an immutable Map.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see MarshalledValue
 * @since 2.1.0
 */
@Immutable
public class MarshalledValueMap implements Map, Externalizable
{
   Map delegate;
   Map<Object, Object> unmarshalled;

   public MarshalledValueMap()
   {
      // for externalization
   }

   public MarshalledValueMap(Map delegate)
   {
      this.delegate = delegate;
   }

   @SuppressWarnings("unchecked")
   protected synchronized Map getUnmarshalledMap()
   {
      if (unmarshalled == null) unmarshalled = unmarshalledMap(delegate.entrySet());
      return unmarshalled;
   }

   public int size()
   {
      return delegate.size();
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public boolean containsKey(Object key)
   {
      return getUnmarshalledMap().containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return getUnmarshalledMap().containsValue(value);
   }

   public Object get(Object key)
   {
      return getUnmarshalledMap().get(key);
   }

   public Object put(Object key, Object value)
   {
      throw new UnsupportedOperationException("This is an immutable map!");
   }

   public Object remove(Object key)
   {
      throw new UnsupportedOperationException("This is an immutable map!");
   }

   public void putAll(Map t)
   {
      throw new UnsupportedOperationException("This is an immutable map!");
   }

   public void clear()
   {
      throw new UnsupportedOperationException("This is an immutable map!");
   }

   public Set keySet()
   {
      return getUnmarshalledMap().keySet();
   }

   public Collection values()
   {
      return getUnmarshalledMap().values();
   }

   public Set entrySet()
   {
      return getUnmarshalledMap().entrySet();
   }

   @SuppressWarnings("unchecked")
   protected Map unmarshalledMap(Set entries)
   {
      if (entries == null || entries.isEmpty()) return Collections.emptyMap();
      Map map = new HashMap(entries.size());
      for (Object e : entries)
      {
         Map.Entry entry = (Map.Entry) e;
         map.put(getUnmarshalledValue(entry.getKey()), getUnmarshalledValue(entry.getValue()));
      }
      return map;
   }

   private Object getUnmarshalledValue(Object o)
   {
      try
      {
         return o instanceof MarshalledValue ? ((MarshalledValue) o).get() : o;
      }
      catch (Exception e)
      {
         throw new CacheException("Unable to unmarshall value", e);
      }
   }

   @Override
   public boolean equals(Object other)
   {
      if (other instanceof Map)
      {
         return getUnmarshalledMap().equals(other);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return getUnmarshalledMap().hashCode();
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {
      out.writeObject(delegate);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      delegate = (Map) in.readObject();
   }
}
