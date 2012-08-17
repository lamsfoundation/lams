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

import org.jboss.util.stream.MarshalledValueInputStream;
import org.jboss.util.stream.MarshalledValueOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Utility methods related to marshalling and unmarshalling objects.
 *
 * @author <a href="mailto://brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public class MarshallUtil
{

   /**
    * Creates an object from a byte buffer using
    * {@link MarshalledValueInputStream}.
    *
    * @param bytes serialized form of an object
    * @return the object, or <code>null</code> if <code>bytes</code>
    *         is <code>null</code>
    */
   public static Object objectFromByteBuffer(byte[] bytes) throws Exception
   {
      if (bytes == null)
      {
         return null;
      }

      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      MarshalledValueInputStream input = new MarshalledValueInputStream(bais);
      Object result = input.readObject();
      input.close();
      return result;
   }

   /**
    * Creates an object from a byte buffer using
    * {@link MarshalledValueInputStream}.
    *
    * @param bytes serialized form of an object
    * @return the object, or <code>null</code> if <code>bytes</code>
    *         is <code>null</code>
    */
   public static Object objectFromByteBuffer(InputStream bytes) throws Exception
   {
      if (bytes == null)
      {
         return null;
      }

      MarshalledValueInputStream input = new MarshalledValueInputStream(bytes);
      Object result = input.readObject();
      input.close();
      return result;
   }

   /**
    * Serializes an object into a byte buffer using
    * {@link org.jboss.util.stream.MarshalledValueOutputStream}.
    *
    * @param obj an object that implements Serializable or Externalizable
    * @return serialized form of the object
    */
   public static byte[] objectToByteBuffer(Object obj) throws Exception
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      MarshalledValueOutputStream out = new MarshalledValueOutputStream(baos);
      out.writeObject(obj);
      out.close();
      return baos.toByteArray();
   }

}
