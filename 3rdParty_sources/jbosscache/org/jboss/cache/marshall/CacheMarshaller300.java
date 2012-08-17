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

import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Map;

/**
 * Adds special treatment of arrays over and above the superclass.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class CacheMarshaller300 extends CacheMarshaller210
{
   @Override
   protected void marshallObject(Object o, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      if (o != null && o.getClass().isArray() && isKnownType(o.getClass().getComponentType()))
      {
         marshallArray(o, out, refMap);
      }
      else
      {
         super.marshallObject(o, out, refMap);
      }
   }

   protected boolean isKnownType(Class c)
   {
      return (c.equals(Object.class) ||
            c.isPrimitive() || c.equals(Character.class) || c.equals(Integer.class) || c.equals(Long.class) ||
            c.equals(Byte.class) || c.equals(Boolean.class) || c.equals(Short.class) || c.equals(Float.class) ||
            c.equals(Double.class));
   }

   protected void marshallArray(Object o, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      out.writeByte(MAGICNUMBER_ARRAY);
      Class arrayTypeClass = o.getClass().getComponentType();
      int sz = Array.getLength(o);
      writeUnsignedInt(out, sz);
      boolean isPrim = arrayTypeClass.isPrimitive();

      if (!isPrim && arrayTypeClass.equals(Object.class))
      {
         out.writeByte(MAGICNUMBER_OBJECT);
         for (int i = 0; i < sz; i++) marshallObject(Array.get(o, i), out, refMap);
      }
      else if (arrayTypeClass.equals(byte.class) || arrayTypeClass.equals(Byte.class))
      {
         out.writeByte(MAGICNUMBER_BYTE);
         out.writeBoolean(isPrim);
         if (isPrim)
            out.write((byte[]) o);
         else
            for (int i = 0; i < sz; i++) out.writeByte((Byte) Array.get(o, i));
      }
      else if (arrayTypeClass.equals(int.class) || arrayTypeClass.equals(Integer.class))
      {
         out.writeByte(MAGICNUMBER_INTEGER);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeInt(Array.getInt(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeInt((Integer) Array.get(o, i));
      }

      else if (arrayTypeClass.equals(long.class) || arrayTypeClass.equals(Long.class))
      {
         out.writeByte(MAGICNUMBER_LONG);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeLong(Array.getLong(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeLong((Long) Array.get(o, i));
      }
      else if (arrayTypeClass.equals(boolean.class) || arrayTypeClass.equals(Boolean.class))
      {
         out.writeByte(MAGICNUMBER_BOOLEAN);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeBoolean(Array.getBoolean(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeBoolean((Boolean) Array.get(o, i));
      }
      else if (arrayTypeClass.equals(char.class) || arrayTypeClass.equals(Character.class))
      {
         out.writeByte(MAGICNUMBER_CHAR);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeChar(Array.getChar(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeChar((Character) Array.get(o, i));
      }
      else if (arrayTypeClass.equals(short.class) || arrayTypeClass.equals(Short.class))
      {
         out.writeByte(MAGICNUMBER_SHORT);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeShort(Array.getShort(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeShort((Short) Array.get(o, i));
      }
      else if (arrayTypeClass.equals(float.class) || arrayTypeClass.equals(Float.class))
      {
         out.writeByte(MAGICNUMBER_FLOAT);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeFloat(Array.getFloat(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeFloat((Float) Array.get(o, i));
      }
      else if (arrayTypeClass.equals(double.class) || arrayTypeClass.equals(Double.class))
      {
         out.writeByte(MAGICNUMBER_DOUBLE);
         out.writeBoolean(isPrim);
         if (isPrim)
            for (int i = 0; i < sz; i++) out.writeDouble(Array.getDouble(o, i));
         else
            for (int i = 0; i < sz; i++) out.writeDouble((Double) Array.get(o, i));
      }
      else throw new CacheException("Unknown array type!");
   }
}
