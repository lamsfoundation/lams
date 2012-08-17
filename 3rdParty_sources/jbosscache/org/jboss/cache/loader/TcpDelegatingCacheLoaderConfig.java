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
import org.jboss.cache.util.Util;

import java.util.Properties;

public class TcpDelegatingCacheLoaderConfig extends IndividualCacheLoaderConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -3138555335000168505L;

   private String host = "localhost";
   private int port = 7500;
   private int timeout = 5000;
   private int reconnectWaitTime = 500;
   private int readTimeout = 0;

   public TcpDelegatingCacheLoaderConfig()
   {
      setClassName(TcpDelegatingCacheLoader.class.getName());
   }

   /**
    * For use by {@link TcpDelegatingCacheLoader}.
    *
    * @param base generic config object created by XML parsing.
    */
   TcpDelegatingCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      setClassName(TcpDelegatingCacheLoader.class.getName());
      populateFromBaseConfig(base);
   }

   /**
    * For use by {@link TcpDelegatingCacheLoader}.
    *
    * @param host    hostname of the delegate
    * @param port    port the delegate is listening on
    * @param timeout after which to throw an IOException
    */
   TcpDelegatingCacheLoaderConfig(String host, int port, int timeout)
   {
      setClassName(TcpDelegatingCacheLoader.class.getName());
      this.host = host;
      this.port = port;
      this.timeout = timeout;
   }

   public String getHost()
   {
      return host;
   }

   public void setHost(String host)
   {
      testImmutability("host");
      this.host = host;
   }

   public int getPort()
   {
      return port;
   }

   public void setPort(int port)
   {
      testImmutability("port");
      this.port = port;
   }

   public int getTimeout()
   {
      return timeout;
   }

   public void setTimeout(int timeout)
   {
      testImmutability("timeout");
      this.timeout = timeout;
   }

   public int getReconnectWaitTime()
   {
      return reconnectWaitTime;
   }

   public void setReconnectWaitTime(int reconnectWaitTime)
   {
      testImmutability("reconnectWaitTime");
      this.reconnectWaitTime = reconnectWaitTime;
   }

   public int getReadTimeout()
   {
      return readTimeout;
   }

   public void setReadTimeout(int readTimeout)
   {
      testImmutability("readTimeout");
      this.readTimeout = readTimeout;
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      String s = props.getProperty("host");
      if (s != null && s.length() > 0)
      {
         this.host = s;
      }
      s = props.getProperty("port");
      if (s != null && s.length() > 0)
      {
         this.port = Integer.parseInt(s);
      }

      s = props.getProperty("timeout");
      if (s != null && s.length() > 0)
      {
         this.timeout = Integer.parseInt(s);
      }

      s = props.getProperty("reconnectWaitTime");
      if (s != null && s.length() > 0)
      {
         this.reconnectWaitTime = Integer.parseInt(s);
      }

      s = props.getProperty("readTimeout");
      if (s != null && s.length() > 0)
      {
         this.readTimeout = Integer.parseInt(s);
      }
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof TcpDelegatingCacheLoaderConfig && equalsExcludingProperties(obj))
      {
         TcpDelegatingCacheLoaderConfig other = (TcpDelegatingCacheLoaderConfig) obj;

         return Util.safeEquals(host, other.host)
               && (port == other.port) && (timeout == other.timeout)
               && (reconnectWaitTime == other.reconnectWaitTime)
               && (readTimeout == other.readTimeout);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = hashCodeExcludingProperties();
      result = 31 * result + (host == null ? 0 : host.hashCode());
      result = 31 * result + port;
      result = 31 * result + timeout;
      result = 31 * result + reconnectWaitTime;
      result = 31 * result + readTimeout;

      return result;
   }

   @Override
   public TcpDelegatingCacheLoaderConfig clone() throws CloneNotSupportedException
   {
      return (TcpDelegatingCacheLoaderConfig) super.clone();
   }
}