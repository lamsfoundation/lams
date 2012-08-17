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

import org.jboss.cache.CacheException;
import org.jboss.util.stream.MarshalledValueInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Wrapper that wraps cached data, providing lazy deserialization using the calling thread's context class loader.
 * <p/>
 * The {@link org.jboss.cache.interceptors.MarshalledValueInterceptor} handles transparent
 * wrapping/unwrapping of cached data.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.interceptors.MarshalledValueInterceptor
 * @since 2.1.0
 */
public class MarshalledValue implements Externalizable
{
   protected Object instance;
   protected byte[] raw;
   private int cachedHashCode = 0;
   // by default equals() will test on the istance rather than the byte array if conversion is required.
   private transient boolean equalityPreferenceForInstance = true;

   public MarshalledValue(Object instance) throws NotSerializableException
   {
      if (instance == null) throw new NullPointerException("Null values cannot be wrapped as MarshalledValues!");

      if (instance instanceof Serializable)
         this.instance = instance;
      else
         throw new NotSerializableException("Marshalled values can only wrap Objects that are serializable!  Instance of " + instance.getClass() + " won't Serialize.");
   }

   public MarshalledValue()
   {
      // empty ctor for serialization
   }

   public void setEqualityPreferenceForInstance(boolean equalityPreferenceForInstance)
   {
      this.equalityPreferenceForInstance = equalityPreferenceForInstance;
   }

   public synchronized void serialize()
   {
      if (raw == null)
      {
         try
         {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(instance);
            oos.close();
            baos.close();
            // Do NOT set instance to null over here, since it may be used elsewhere (e.g., in a cache listener).
            // this will be compacted by the MarshalledValueInterceptor when the call returns.
//            instance = null;
            raw = baos.toByteArray();
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to marshall value " + instance, e);
         }
      }
   }

   public synchronized void deserialize()
   {
      if (instance == null)
      {
         try
         {
            ByteArrayInputStream bais = new ByteArrayInputStream(raw);
            // use a MarshalledValueInputStream since it needs to be aware of any context class loaders on the current thread.
            ObjectInputStream ois = new MarshalledValueInputStream(bais);
            instance = ois.readObject();
            ois.close();
            bais.close();
//            raw = null;
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to unmarshall value", e);
         }
      }
   }

   /**
    * Compacts the references held by this class to a single reference.  If only one representation exists this method
    * is a no-op unless the 'force' parameter is used, in which case the reference held is forcefully switched to the
    * 'preferred representation'.
    * <p/>
    * Either way, a call to compact() will ensure that only one representation is held.
    * <p/>
    *
    * @param preferSerializedRepresentation if true and both representations exist, the serialized representation is favoured.  If false, the deserialized representation is preferred.
    * @param force                          ensures the preferred representation is maintained and the other released, even if this means serializing or deserializing.
    */
   public void compact(boolean preferSerializedRepresentation, boolean force)
   {
      // reset the equalityPreference
      equalityPreferenceForInstance = true;
      if (force)
      {
         if (preferSerializedRepresentation && raw == null) serialize();
         else if (!preferSerializedRepresentation && instance == null) deserialize();
      }

      if (instance != null && raw != null)
      {
         // need to lose one representation!

         if (preferSerializedRepresentation)
         {
            instance = null;
         }
         else
         {
            raw = null;
         }
      }
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {
      if (raw == null) serialize();
      out.writeInt(raw.length);
      out.write(raw);
      out.writeInt(hashCode());
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      int size = in.readInt();
      raw = new byte[size];
      cachedHashCode = 0;
      in.readFully(raw);
      cachedHashCode = in.readInt();
   }

   public Object get() throws IOException, ClassNotFoundException
   {
      if (instance == null) deserialize();
      return instance;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      MarshalledValue that = (MarshalledValue) o;

      // if both versions are serialized or deserialized, just compare the relevant representations.
      if (raw != null && that.raw != null) return Arrays.equals(raw, that.raw);
      if (instance != null && that.instance != null) return instance.equals(that.instance);

      // if conversion of one representation to the other is necessary, then see which we prefer converting.
      if (equalityPreferenceForInstance)
      {
         if (instance == null) deserialize();
         if (that.instance == null) that.deserialize();
         return instance.equals(that.instance);
      }
      else
      {
         if (raw == null) serialize();
         if (that.raw == null) that.serialize();
         return Arrays.equals(raw, that.raw);
      }
   }

   @Override
   public int hashCode()
   {
      if (cachedHashCode == 0)
      {
         // always calculate the hashcode based on the instance since this is where we're getting the equals()
         if (instance == null) deserialize();
         cachedHashCode = instance.hashCode();
         if (cachedHashCode == 0) // degenerate case
         {
            cachedHashCode = 0xFEED;
         }
      }
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      return "MarshalledValue(cachedHashCode=" + cachedHashCode + "; serialized=" + (raw != null) + ")";
   }
}
