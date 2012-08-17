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
package org.jboss.cache.loader.s3;

import net.noderunner.amazon.s3.Bucket;
import net.noderunner.amazon.s3.CallingFormat;
import net.noderunner.amazon.s3.Connection;
import org.jboss.cache.CacheException;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.lang.reflect.Field;
import java.util.Properties;


/**
 * Amazon S3 loader configuration.
 *
 * @author Elias Ross
 */
public class S3LoaderConfig extends IndividualCacheLoaderConfig
{
   private static final long serialVersionUID = 4626734068542420865L;

   private String accessKeyId;

   private String secretAccessKey;

   private boolean secure;

   private String server = Connection.DEFAULT_HOST;

   private int port;

   private transient Bucket bucket = new Bucket("jboss-cache");

   private transient CallingFormat callingFormat = CallingFormat.SUBDOMAIN;

   private String location = Connection.LOCATION_DEFAULT;

   private boolean optimize = false;

   private boolean parentCache = true;

   public S3LoaderConfig()
   {
      setClassName(S3CacheLoader.class.getName());
   }

   /**
    * For use by {@link S3CacheLoader}.
    *
    * @param base generic config object created by XML parsing.
    */
   S3LoaderConfig(IndividualCacheLoaderConfig base)
   {
      setClassName(S3CacheLoader.class.getName());
      populateFromBaseConfig(base);
   }

   /**
    * Returns a new connection.
    */
   Connection getConnection()
   {
      return new Connection(getAccessKeyId(), getSecretAccessKey(),
            isSecure(), getServer(), getPort(), getCallingFormat());
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      if (props == null)
         return;
      setAccessKeyId(props.getProperty("cache.s3.accessKeyId"));
      setSecretAccessKey(props.getProperty("cache.s3.secretAccessKey"));
      setSecure(props.getProperty("cache.s3.secure"));
      setServer(props.getProperty("cache.s3.server", Connection.DEFAULT_HOST));
      setPort(props.getProperty("cache.s3.port"));
      setBucket(props.getProperty("cache.s3.bucket"));
      setCallingFormat(props.getProperty("cache.s3.callingFormat"));
      setOptimize(props.getProperty("cache.s3.optimize"));
      setParentCache(props.getProperty("cache.s3.parentCache"));
      setLocation(props.getProperty("cache.s3.location"));
   }

   private void setLocation(String s)
   {
      if (s != null)
         this.location = s;
   }

   private void setParentCache(String s)
   {
      if (s != null)
         setParentCache(Boolean.valueOf(s));
   }

   private void setOptimize(String s)
   {
      if (s != null)
         setOptimize(Boolean.valueOf(s));
   }

   private void setCallingFormat(String s)
   {
      if (s != null)
      {
         try
         {
            Field field = CallingFormat.class.getDeclaredField(s);
            setCallingFormat((CallingFormat) field.get(null));
         }
         catch (Exception e)
         {
            throw new CacheException(e);
         }
      }
   }

   private void setBucket(String s)
   {
      if (s != null)
         setBucket(new Bucket(s));
   }

   private void setSecure(String s)
   {
      if (s != null)
         setSecure(Boolean.valueOf(s));
   }

   private void setPort(String s)
   {
      if (s != null)
         setPort(Integer.parseInt(s));
      else
         setPort(secure ? Connection.SECURE_PORT : Connection.INSECURE_PORT);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof S3LoaderConfig && equalsExcludingProperties(obj))
      {
         // TODO
         return this == obj;
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return 31 * hashCodeExcludingProperties();
      // TODO
   }

   @Override
   public S3LoaderConfig clone() throws CloneNotSupportedException
   {
      return (S3LoaderConfig) super.clone();
   }

   /**
    * Returns accessKeyId.
    */
   public String getAccessKeyId()
   {
      return accessKeyId;
   }

   /**
    * Sets accessKeyId.
    */
   public void setAccessKeyId(String accessKeyId)
   {
      this.accessKeyId = accessKeyId.trim();
   }

   /**
    * Returns secretAccessKey.
    */
   public String getSecretAccessKey()
   {
      return secretAccessKey.trim();
   }

   /**
    * Sets secretAccessKey.
    */
   public void setSecretAccessKey(String secretAccessKey)
   {
      this.secretAccessKey = secretAccessKey;
   }

   /**
    * Returns secure.
    */
   public boolean isSecure()
   {
      return secure;
   }

   /**
    * Sets secure.
    */
   public void setSecure(boolean secure)
   {
      this.secure = secure;
   }

   /**
    * Returns server.
    */
   public String getServer()
   {
      return server;
   }

   /**
    * Sets server.
    */
   public void setServer(String server)
   {
      this.server = server;
   }

   /**
    * Returns port.
    */
   public int getPort()
   {
      return port;
   }

   /**
    * Sets port.
    */
   public void setPort(int port)
   {
      this.port = port;
   }

   /**
    * Returns bucket.
    */
   public Bucket getBucket()
   {
      return bucket;
   }

   /**
    * Sets bucket.
    */
   public void setBucket(Bucket bucket)
   {
      this.bucket = bucket;
   }

   /**
    * Returns callingFormat.
    */
   public CallingFormat getCallingFormat()
   {
      return callingFormat;
   }

   /**
    * Sets callingFormat.
    */
   public void setCallingFormat(CallingFormat callingFormat)
   {
      this.callingFormat = callingFormat;
   }

   /**
    * Returns a debug string.
    */
   @Override
   public String toString()
   {
      return super.toString() +
            " keyid=" + accessKeyId +
            " secret=" + "***" +
            " secure=" + secure +
            " server=" + server +
            " port=" + port +
            " bucket=" + bucket +
            " cf=" + callingFormat +
            " location=" + location;
   }

   /**
    * Returns true if certain CRUD operations are optimized to not do extra queries.
    * If true, then the behavior will be:
    * <ul>
    * <li>put(Fqn, Map) will always overwrite
    * <li>put(Fqn, Object, Object) will always overwrite, return null
    * <li>remove(Object) will return null
    * </ul>
    */
   public boolean getOptimize()
   {
      return optimize;
   }

   /**
    * Sets optimize.
    */
   public void setOptimize(boolean optimize)
   {
      this.optimize = optimize;
   }

   /**
    * Returns true if the existence of parent nodes should be cached.
    */
   public boolean getParentCache()
   {
      return parentCache;
   }

   /**
    * Sets parentCache.
    */
   public void setParentCache(boolean parentCache)
   {
      this.parentCache = parentCache;
   }

   /**
    * Returns location.
    */
   public String getLocation()
   {
      return location;
   }
}