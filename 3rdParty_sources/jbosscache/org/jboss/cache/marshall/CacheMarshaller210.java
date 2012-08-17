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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * An evolution of {@link org.jboss.cache.marshall.CacheMarshaller200}, created to fix <a href="http://jira.jboss.org/jira/browse/JBCACHE-1211">JBCACHE-1211</a>.
 * <p/>
 * To prevent ints taking too much space, they are written as variable-length ints.  Details <a href="http://lucene.apache.org/java/docs/fileformats.html#VInt">here</a> on VInts.
 *
 * @author Manik Surtani
 * @see <a href="http://lucene.apache.org/java/docs/fileformats.html#VInt">VInt</a>
 * @see <a href="http://lucene.apache.org/java/docs/fileformats.html#VLong">VLong</a>
 * @since 2.1.0
 */
public class CacheMarshaller210 extends CacheMarshaller200
{
   public CacheMarshaller210()
   {
      initLogger();
      // disabled, since this is always disabled in JBC 2.0.0.
      // Java ObjectOutputStreams will have its own built-in ref counting.  No need to repeat this.
      useRefs = false;
   }

   /**
    * This version of writeReference is written to solve JBCACHE-1211, where references are encoded as ints rather than shorts.
    *
    * @param out       stream to write to
    * @param reference reference to write
    * @throws IOException propagated from OOS
    * @see <a href="http://jira.jboss.org/jira/browse/JBCACHE-1211">JBCACHE-1211</a>
    */
   @Override
   protected void writeReference(ObjectOutputStream out, int reference) throws IOException
   {
      writeUnsignedInt(out, reference);
   }

   /**
    * This version of readReference is written to solve JBCACHE-1211, where references are encoded as ints rather than shorts.
    *
    * @param in stream to read from
    * @return reference
    * @throws IOException propagated from OUS
    * @see <a href="http://jira.jboss.org/jira/browse/JBCACHE-1211">JBCACHE-1211</a>
    */
   @Override
   protected int readReference(ObjectInputStream in) throws IOException
   {
      return readUnsignedInt(in);
   }

   /**
    * Reads an int stored in variable-length format.  Reads between one and
    * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
    * supported.
    */
   @Override
   protected int readUnsignedInt(ObjectInputStream in) throws IOException
   {
      byte b = in.readByte();
      int i = b & 0x7F;
      for (int shift = 7; (b & 0x80) != 0; shift += 7)
      {
         b = in.readByte();
         i |= (b & 0x7FL) << shift;
      }
      return i;
   }

   /**
    * Writes an int in a variable-length format.  Writes between one and
    * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
    * supported.
    *
    * @param i int to write
    */
   @Override
   protected void writeUnsignedInt(ObjectOutputStream out, int i) throws IOException
   {
      while ((i & ~0x7F) != 0)
      {
         out.writeByte((byte) ((i & 0x7f) | 0x80));
         i >>>= 7;
      }
      out.writeByte((byte) i);
   }

   /**
    * Reads an int stored in variable-length format.  Reads between one and
    * nine bytes.  Smaller values take fewer bytes.  Negative numbers are not
    * supported.
    */
   @Override
   protected long readUnsignedLong(ObjectInputStream in) throws IOException
   {
      byte b = in.readByte();
      long i = b & 0x7F;
      for (int shift = 7; (b & 0x80) != 0; shift += 7)
      {
         b = in.readByte();
         i |= (b & 0x7FL) << shift;
      }
      return i;
   }

   /**
    * Writes an int in a variable-length format.  Writes between one and
    * nine bytes.  Smaller values take fewer bytes.  Negative numbers are not
    * supported.
    *
    * @param i int to write
    */
   @Override
   protected void writeUnsignedLong(ObjectOutputStream out, long i) throws IOException
   {
      while ((i & ~0x7F) != 0)
      {
         out.writeByte((byte) ((i & 0x7f) | 0x80));
         i >>>= 7;
      }
      out.writeByte((byte) i);
   }
}
