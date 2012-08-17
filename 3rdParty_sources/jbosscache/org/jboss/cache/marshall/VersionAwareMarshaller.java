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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.io.ByteBuffer;
import org.jboss.cache.io.ExposedByteArrayOutputStream;
import org.jboss.cache.util.Util;
import org.jboss.util.stream.MarshalledValueInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * A delegate to various other marshallers like {@link org.jboss.cache.marshall.CacheMarshaller200}.
 * This delegating marshaller adds versioning information to the stream when marshalling objects and
 * is able to pick the appropriate marshaller to delegate to based on the versioning information when
 * unmarshalling objects.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class VersionAwareMarshaller extends AbstractMarshaller
{
   private static final Log log = LogFactory.getLog(VersionAwareMarshaller.class);
   private static final int VERSION_200 = 20;
   private static final int VERSION_210 = 21;
   private static final int VERSION_220 = 22;
   private static final int VERSION_300 = 30;
   private static final int VERSION_310 = 31;
   private static final int CUSTOM_MARSHALLER = 999;

   private ComponentRegistry componentRegistry;

   Marshaller defaultMarshaller;
   final Map<Integer, Marshaller> marshallers = new HashMap<Integer, Marshaller>();
   private int versionInt;

   @Inject
   void injectComponents(ComponentRegistry componentRegistry)
   {
      this.componentRegistry = componentRegistry;
   }

   @Start
   public void initReplicationVersions()
   {
      String replVersionString = configuration.getReplVersionString();

      // this will cause the correct marshaller to be created and put in the map of marshallers
      defaultMarshaller = configuration.getMarshaller();
      if (defaultMarshaller == null)
      {
         String marshallerClass = configuration.getMarshallerClass();
         if (marshallerClass != null)
         {
            if (trace)
            {
               log.trace("Cache marshaller implementation specified as " + marshallerClass + ".  Overriding any version strings passed in. ");
            }
            try
            {
               defaultMarshaller = (Marshaller) Util.loadClass(marshallerClass).newInstance();
            }
            catch (Exception e)
            {
               log.warn("Unable to instantiate marshaller of class " + marshallerClass, e);
               log.warn("Falling back to using the default marshaller for version string " + replVersionString);
            }
         }
      }

      if (defaultMarshaller == null)
      {
         // "Rounds down" the replication version passed in to the MINOR version.
         // E.g., 1.4.1.SP3 -> 1.4.0
         versionInt = toMinorVersionInt(replVersionString);
         this.defaultMarshaller = getMarshaller(versionInt);
      }
      else
      {
         if (log.isDebugEnabled()) log.debug("Using the marshaller passed in - " + defaultMarshaller);
         versionInt = getCustomMarshallerVersionInt();
         marshallers.put(versionInt, defaultMarshaller);
      }


      if (log.isDebugEnabled())
      {
         log.debug("Started with version " + replVersionString + " and versionInt " + versionInt);
         log.debug("Using default marshaller class " + this.defaultMarshaller.getClass());
      }
   }

   protected int getCustomMarshallerVersionInt()
   {
      if (defaultMarshaller.getClass().equals(CacheMarshaller210.class)) return VERSION_210;
      if (defaultMarshaller.getClass().equals(CacheMarshaller200.class)) return VERSION_200;
      return CUSTOM_MARSHALLER;
   }

   /**
    * Converts versions to known compatible version ids.
    * <p/>
    * Typical return values:
    * <p/>
    * < 1.4.0 = "1"
    * 1.4.x = "14"
    * 1.5.x = "15"
    * 2.0.x = "20"
    * 2.1.x = "21"
    * <p/>
    * etc.
    *
    * @param version
    * @return a version integer
    */
   private int toMinorVersionInt(String version)
   {
      try
      {
         StringTokenizer strtok = new StringTokenizer(version, ".");

         // major, minor, micro, patch
         String[] versionComponents = {null, null, null, null};
         int i = 0;
         while (strtok.hasMoreTokens())
         {
            versionComponents[i++] = strtok.nextToken();
         }

         int major = Integer.parseInt(versionComponents[0]);
         int minor = Integer.parseInt(versionComponents[1]);

         return (major > 1 || minor > 3) ? (10 * major) + minor : 1;
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Unsupported replication version string " + version);
      }
   }

   @Override
   public ByteBuffer objectToBuffer(Object obj) throws Exception
   {
      ExposedByteArrayOutputStream baos = new ExposedByteArrayOutputStream(128);
      ObjectOutputStream out = new ObjectOutputStream(baos);

      out.writeShort(versionInt);
      if (trace) log.trace("Wrote version " + versionInt);

      //now marshall the contents of the object
      defaultMarshaller.objectToObjectStream(obj, out);
      out.close();

      // and return bytes.
      return new ByteBuffer(baos.getRawBuffer(), 0, baos.size());
   }

   @Override
   public Object objectFromByteBuffer(byte[] bytes, int offset, int len) throws Exception
   {
      Marshaller marshaller;
      int versionId;
      ObjectInputStream in = new MarshalledValueInputStream(new ByteArrayInputStream(bytes, offset, len));

      try
      {
         versionId = in.readShort();
         if (trace) log.trace("Read version " + versionId);
      }
      catch (Exception e)
      {
         log.error("Unable to read version id from first two bytes of stream, barfing.");
         throw e;
      }

      marshaller = getMarshaller(versionId);

      return marshaller.objectFromObjectStream(in);
   }

   @Override
   public RegionalizedMethodCall regionalizedMethodCallFromByteBuffer(byte[] buf) throws Exception
   {
      Marshaller marshaller;
      int versionId;
      ObjectInputStream in = new MarshalledValueInputStream(new ByteArrayInputStream(buf));

      try
      {
         versionId = in.readShort();
         if (trace) log.trace("Read version " + versionId);
      }
      catch (Exception e)
      {
         log.error("Unable to read version id from first two bytes of stream, barfing.");
         throw e;
      }

      marshaller = getMarshaller(versionId);

      return marshaller.regionalizedMethodCallFromObjectStream(in);
   }

   @Override
   public Object objectFromStream(InputStream is) throws Exception
   {
      if (is instanceof ByteArrayInputStream)
      {
         int avbl = is.available();
         byte[] bytes = new byte[avbl];
         is.read(bytes, 0, avbl);
         return objectFromByteBuffer(bytes);
      }
      else
      {
         // actually attempt to "stream" this stuff.  We need to revert to an old-fashioned Object Input Stream since
         // we don't have a reusable implementation for non-byte-backed streams as yet.
         short versionId;
         Marshaller marshaller;

         ObjectInputStream in = new MarshalledValueInputStream(is);

         try
         {
            versionId = in.readShort();
            if (trace) log.trace("Read version " + versionId);
         }
         catch (Exception e)
         {
            log.error("Unable to read version id from first two bytes of stream, barfing.");
            throw e;
         }

         marshaller = getMarshaller(versionId);

         return marshaller.objectFromObjectStream(in);
      }
   }

   public void objectToObjectStream(Object obj, ObjectOutputStream out, Fqn region) throws Exception
   {
      out.writeShort(versionInt);
      if (trace) log.trace("Wrote version " + versionInt);
      defaultMarshaller.objectToObjectStream(obj, out, region);
   }

   /**
    * Lazily instantiates and loads the relevant marshaller for a given version.
    *
    * @param versionId
    * @return appropriate marshaller for the version requested.
    */
   Marshaller getMarshaller(int versionId)
   {
      Marshaller marshaller;
      AbstractMarshaller am;
      boolean knownVersion = false;
      switch (versionId)
      {
         case VERSION_200:
            marshaller = marshallers.get(VERSION_200);
            if (marshaller == null)
            {
               am = new CacheMarshaller200();
               marshaller = am;
               componentRegistry.wireDependencies(am);
               am.init();
               marshallers.put(VERSION_200, marshaller);
            }
            break;
         case VERSION_220:
         case VERSION_210:
            marshaller = marshallers.get(VERSION_210);
            if (marshaller == null)
            {
               am = new CacheMarshaller210();
               marshaller = am;
               componentRegistry.wireDependencies(am);
               am.init();
               marshallers.put(VERSION_210, marshaller);
            }
            break;
         case VERSION_310:
         case VERSION_300:
            knownVersion = true;
         default:
            if (!knownVersion && log.isWarnEnabled())
            {
               log.warn("Unknown replication version [" + versionId + "].  Falling back to the default marshaller installed.");
            }
            marshaller = marshallers.get(VERSION_300);
            if (marshaller == null)
            {
               am = new CacheMarshaller300();
               marshaller = am;
               componentRegistry.wireDependencies(am);
               am.init();
               marshallers.put(VERSION_300, marshaller);
            }
            break;
      }
      return marshaller;
   }

   public void objectToObjectStream(Object obj, ObjectOutputStream out) throws Exception
   {
      out.writeShort(versionInt);
      if (trace) log.trace("Wrote version " + versionInt);
      defaultMarshaller.objectToObjectStream(obj, out);
   }

   public Object objectFromObjectStream(ObjectInputStream in) throws Exception
   {
      Marshaller marshaller;
      int versionId;
      try
      {
         versionId = in.readShort();
         if (trace) log.trace("Read version " + versionId);
      }
      catch (Exception e)
      {
         log.error("Unable to read version id from first two bytes of stream, barfing.");
         throw e;
      }

      marshaller = getMarshaller(versionId);

      return marshaller.objectFromObjectStream(in);
   }
}
