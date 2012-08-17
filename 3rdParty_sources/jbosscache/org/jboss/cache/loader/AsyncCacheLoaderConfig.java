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
package org.jboss.cache.loader;

import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.util.Properties;

public class AsyncCacheLoaderConfig extends IndividualCacheLoaderConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 5038037589485991681L;

   private int batchSize = 100;
   private boolean returnOld = true;
   private int queueSize = 0;
   private boolean useAsyncPut = true;
   private int threadPoolSize = 1;

   /**
    * Default constructor.
    */
   public AsyncCacheLoaderConfig()
   {
      setClassName(AsyncCacheLoader.class.getName());
   }

   /**
    * For use by {@link AsyncCacheLoader}.
    *
    * @param base generic config object created by XML parsing.
    */
   AsyncCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      setClassName(AsyncCacheLoader.class.getName());
      populateFromBaseConfig(base);
   }

   public int getThreadPoolSize()
   {
      return threadPoolSize;
   }

   public void setThreadPoolSize(int threadPoolSize)
   {
      testImmutability("threadPoolSize");
      this.threadPoolSize = threadPoolSize;
   }

   public int getBatchSize()
   {
      return batchSize;
   }

   public void setBatchSize(int batchSize)
   {
      testImmutability("batchSize");
      this.batchSize = batchSize;
   }

   public int getQueueSize()
   {
      return queueSize;
   }

   public void setQueueSize(int queueSize)
   {
      testImmutability("queueSize");
      this.queueSize = queueSize;
   }

   public boolean getReturnOld()
   {
      return returnOld;
   }

   public void setReturnOld(boolean returnOld)
   {
      testImmutability("returnOld");
      this.returnOld = returnOld;
   }

   public boolean getUseAsyncPut()
   {
      return useAsyncPut;
   }

   public void setUseAsyncPut(boolean useAsyncPut)
   {
      testImmutability("useAsyncPut");
      this.useAsyncPut = useAsyncPut;
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      String s;

      s = props.getProperty("cache.async.batchSize");
      if (s != null) batchSize = Integer.parseInt(s);
      if (batchSize <= 0) throw new IllegalArgumentException("Invalid batch size: " + batchSize);

      s = props.getProperty("cache.async.threadPoolSize");
      if (s != null) threadPoolSize = Integer.parseInt(s);
      if (threadPoolSize <= 0) throw new IllegalArgumentException("Invalid thread pool size: " + threadPoolSize);


      s = props.getProperty("cache.async.returnOld");
      if (s != null) returnOld = Boolean.valueOf(s);

      s = props.getProperty("cache.async.queueSize");
      if (s != null) queueSize = Integer.parseInt(s);

      s = props.getProperty("cache.async.put");
      if (s != null) useAsyncPut = Boolean.valueOf(s);


   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof AsyncCacheLoaderConfig && equalsExcludingProperties(obj))
      {
         AsyncCacheLoaderConfig other = (AsyncCacheLoaderConfig) obj;
         return (batchSize == other.batchSize)
               && (queueSize == other.queueSize)
               && (returnOld == other.returnOld)
               && (useAsyncPut == other.useAsyncPut);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = hashCodeExcludingProperties();
      result = 31 * result + batchSize;
      result = 31 * result + queueSize;
      result = 31 * result + (returnOld ? 0 : 1);
      result = 31 * result + (useAsyncPut ? 0 : 1);
      return result;
   }

   @Override
   public AsyncCacheLoaderConfig clone() throws CloneNotSupportedException
   {
      return (AsyncCacheLoaderConfig) super.clone();
   }


}