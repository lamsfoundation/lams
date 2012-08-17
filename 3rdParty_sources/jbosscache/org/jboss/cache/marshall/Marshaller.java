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

import org.jboss.cache.Fqn;
import org.jboss.cache.io.ByteBuffer;
import org.jgroups.blocks.RpcDispatcher;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A marshaller is a class that is able to marshall and unmarshall objects efficiently.
 * <p/>
 * The reason why this is implemented specially in JBoss Cache rather than resorting to
 * Java serialization or even the more efficient JBoss serialization is that a lot of efficiency
 * can be gained when a majority of the serialization that occurs has to do with a small set
 * of known types such as {@link org.jboss.cache.Fqn} or {@link org.jboss.cache.commands.ReplicableCommand}, and class type information
 * can be replaced with simple magic numbers.
 * <p/>
 * Unknown types (typically user data) falls back to JBoss serialization.
 * <p/>
 * In addition, using a marshaller allows adding additional data to the byte stream, such as context
 * class loader information on which class loader to use to deserialize the object stream, or versioning
 * information to allow streams to interoperate between different versions of JBoss Cache (see {@link VersionAwareMarshaller}
 * <p/>
 * This interface implements the JGroups building-block interface {@link org.jgroups.blocks.RpcDispatcher.Marshaller} which
 * is used to marshall {@link org.jboss.cache.commands.ReplicableCommand}s, their parameters and their response values.
 * <p/>
 * The interface is also used by the {@link org.jboss.cache.loader.CacheLoader} framework to efficiently serialize data to be persisted, as well as
 * the {@link org.jboss.cache.statetransfer.StateTransferManager} when serializing the cache for transferring state en-masse.
 *
 * @author <a href="mailto://manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 */
public interface Marshaller extends RpcDispatcher.Marshaller2
{
   /**
    * Marshalls an object to a given {@link ObjectOutputStream}
    *
    * @param obj object to marshall
    * @param out stream to marshall to
    * @throws Exception
    */
   void objectToObjectStream(Object obj, ObjectOutputStream out) throws Exception;

   /**
    * Unmarshalls an object from an {@link ObjectInputStream}
    *
    * @param in stream to unmarshall from
    * @throws Exception
    */
   Object objectFromObjectStream(ObjectInputStream in) throws Exception;

   /**
    * Unmarshalls an object from an {@link java.io.InputStream}
    *
    * @param is stream to unmarshall from
    * @return Object from stream passed in.
    * @throws Exception
    */
   Object objectFromStream(InputStream is) throws Exception;

   /**
    * Overloaded form of {@link #objectToObjectStream(Object,java.io.ObjectOutputStream)} which adds a hint to the {@link Fqn} region
    *
    * @param obj    object to marshall
    * @param region fqn that this object pertains to
    * @param out    stream to marshall to
    * @throws Exception
    */
   void objectToObjectStream(Object obj, ObjectOutputStream out, Fqn region) throws Exception;

   /**
    * Returns a RegionalizedMethodCall from a byte buffer.  Only use if you <i>know</i> that the byte buffer contains a
    * MethodCall and that you are using region-based marshalling, otherwise use {@link #objectFromByteBuffer(byte[])}
    *
    * @param buffer byte buffer
    * @return a RegionalizedMethodCall
    * @throws Exception if there are issues
    * @since 2.1.1
    */
   RegionalizedMethodCall regionalizedMethodCallFromByteBuffer(byte[] buffer) throws Exception;

   /**
    * Returns a RegionalizedMethodCall from an object input stream.  Only use if you <i>know</i> that the byte buffer contains a
    * MethodCall and that you are using region-based marshalling, otherwise use {@link #objectFromObjectStream(java.io.ObjectInputStream)}
    *
    * @param in object inout stream
    * @return a RegionalizedMethodCall
    * @throws Exception if there are issues
    * @since 2.1.1
    */
   RegionalizedMethodCall regionalizedMethodCallFromObjectStream(ObjectInputStream in) throws Exception;

   /**
    * A specialized form of {@link org.jgroups.blocks.RpcDispatcher.Marshaller2#objectToBuffer(Object)} that returns an instance
    * of {@link ByteBuffer} instead of {@link org.jgroups.util.Buffer}.
    *
    * @param o object to marshall
    * @return a ByteBuffer
    * @throws Exception
    */
   ByteBuffer objectToBuffer(Object o) throws Exception;
}
