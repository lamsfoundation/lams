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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.loader.tcp.TcpCacheOperations;
import org.jboss.cache.util.concurrent.SynchronizedRestarter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DelegatingCacheLoader implementation which delegates to a remote (not in the same VM)
 * CacheImpl using TCP/IP for communication. Example configuration for connecting to a TcpCacheServer
 * running at myHost:12345:<pre>
 * <attribute name="CacheLoaderClass">org.jboss.cache.loader.TcpDelegatingCacheLoader</attribute>
 * <attribute name="CacheLoaderConfig">
 * host=localhost
 * port=2099
 * </attribute>
 * </pre>
 *
 * @author Bela Ban
 *
 */
public class TcpDelegatingCacheLoader extends AbstractCacheLoader
{
   volatile private Socket sock;
   private TcpDelegatingCacheLoaderConfig config;
   volatile ObjectInputStream in;
   volatile ObjectOutputStream out;
   private static final Log log = LogFactory.getLog(TcpDelegatingCacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();
   private final SynchronizedRestarter restarter = new SynchronizedRestarter();
   private static Method GET_CHILDREN_METHOD, GET_METHOD, PUT_KEY_METHOD, PUT_DATA_METHOD, REMOVE_KEY_METHOD, REMOVE_METHOD, PUT_MODS_METHOD, EXISTS_METHOD, REMOVE_DATA_METHOD;

   static
   {
      try
      {
         GET_CHILDREN_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_getChildrenNames", Fqn.class);
         GET_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_get", Fqn.class);
         EXISTS_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_exists", Fqn.class);
         PUT_KEY_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_put", Fqn.class, Object.class, Object.class);
         PUT_DATA_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_put", Fqn.class, Map.class);
         REMOVE_KEY_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_remove", Fqn.class, Object.class);
         REMOVE_DATA_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_removeData", Fqn.class);
         REMOVE_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_remove", Fqn.class);
         PUT_MODS_METHOD = TcpDelegatingCacheLoader.class.getDeclaredMethod("_put", List.class);

      }
      catch (Exception e)
      {
         log.fatal("Unable to initialise reflection methods", e);
      }
   }


   /**
    * Allows configuration via XML config file.
    */
   public void setConfig(IndividualCacheLoaderConfig base)
   {
      if (base instanceof TcpDelegatingCacheLoaderConfig)
      {
         this.config = (TcpDelegatingCacheLoaderConfig) base;
      }
      else
      {
         config = new TcpDelegatingCacheLoaderConfig(base);
      }
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }

   /**
    * Invokes the specified Method with the specified parameters, catching SocketExceptions and attempting to reconnect
    * to the TcpCacheServer if necessary.
    *
    * @param m      method to invoke
    * @param params parameters
    * @return method return value
    */
   protected Object invokeWithRetries(Method m, Object... params)
   {
      long endTime = System.currentTimeMillis() + config.getTimeout();
      do
      {
         try
         {
            if (trace) log.trace("About to invoke operation " + m);
            Object rv = m.invoke(this, params);
            if (trace) log.trace("Completed invocation of " + m);
            return rv;
         }
         catch (IllegalAccessException e)
         {
            log.error("Should never get here!", e);
         }
         catch (InvocationTargetException e)
         {
            if (e.getCause() instanceof IOException)
            {
               try
               {
                  // sleep 250 ms
                  if (log.isDebugEnabled()) log.debug("Caught IOException.  Retrying.", e);
                  Thread.sleep(config.getReconnectWaitTime());
                  restarter.restartComponent(this);
               }
               catch (InterruptedException e1)
               {
                  Thread.currentThread().interrupt();
               }
               catch (Exception e1)
               {
                  if (trace) log.trace("Unable to reconnect", e1);
               }
            }
            else
            {
               throw new CacheException("Problems invoking method call!", e);
            }
         }
      }
      while (System.currentTimeMillis() < endTime);
      throw new CacheException("Unable to communicate with TCPCacheServer(" + config.getHost() + ":" + config.getPort() + ") after " + config.getTimeout() + " millis, with reconnects every " + config.getReconnectWaitTime() + " millis.");
   }

   // ------------------ CacheLoader interface methods, which delegate to retry-aware methods

   public Set<?> getChildrenNames(Fqn fqn) throws Exception
   {

      return (Set<?>) invokeWithRetries(GET_CHILDREN_METHOD, fqn);
   }

   @SuppressWarnings("unchecked")
   public Map<Object, Object> get(Fqn name) throws Exception
   {
      return (Map<Object, Object>) invokeWithRetries(GET_METHOD, name);
   }

   public boolean exists(Fqn name) throws Exception
   {
      return (Boolean) invokeWithRetries(EXISTS_METHOD, name);
   }

   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      return invokeWithRetries(PUT_KEY_METHOD, name, key, value);
   }

   public void put(Fqn name, Map<Object, Object> attributes) throws Exception
   {
      invokeWithRetries(PUT_DATA_METHOD, name, attributes);
   }

   @Override
   public void put(List<Modification> modifications) throws Exception
   {
      invokeWithRetries(PUT_MODS_METHOD, modifications);
   }

   public Object remove(Fqn fqn, Object key) throws Exception
   {
      return invokeWithRetries(REMOVE_KEY_METHOD, fqn, key);
   }

   public void remove(Fqn fqn) throws Exception
   {
      invokeWithRetries(REMOVE_METHOD, fqn);
   }

   public void removeData(Fqn fqn) throws Exception
   {
      invokeWithRetries(REMOVE_DATA_METHOD, fqn);
   }

   // ------------------ Retry-aware CacheLoader interface method counterparts

   protected Set<?> _getChildrenNames(Fqn fqn) throws Exception
   {
      Set cn;
      synchronized (this)
      {
         out.reset();
         out.writeByte(TcpCacheOperations.GET_CHILDREN_NAMES);
         out.writeObject(fqn);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
         cn = (Set) retval;
      }

      // the cache loader contract is a bit different from the cache when it comes to dealing with childrenNames
      if (cn.isEmpty()) return null;
      return cn;
   }

   @SuppressWarnings("unchecked")
   protected Map<Object, Object> _get(Fqn name) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.GET);
         out.writeObject(name);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
         return (Map) retval;
      }
   }

   protected boolean _exists(Fqn name) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.EXISTS);
         out.writeObject(name);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
         return (Boolean) retval;
      }
   }

   protected Object _put(Fqn name, Object key, Object value) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.PUT_KEY_VAL);
         out.writeObject(name);
         out.writeObject(key);
         out.writeObject(value);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
         return retval;
      }
   }

   protected void _put(Fqn name, Map<Object, Object> attributes) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.PUT);
         out.writeObject(name);
         out.writeObject(attributes);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
      }
   }

   protected void _put(List<Modification> modifications) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.PUT_LIST);
         int length = modifications.size();
         out.writeInt(length);
         for (Modification m : modifications)
         {
            m.writeExternal(out);
         }
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
      }
   }

   protected Object _remove(Fqn fqn, Object key) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.REMOVE_KEY);
         out.writeObject(fqn);
         out.writeObject(key);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
         return retval;
      }
   }

   protected void _remove(Fqn fqn) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.REMOVE);
         out.writeObject(fqn);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
      }
   }

   protected void _removeData(Fqn fqn) throws Exception
   {
      synchronized (this)
      {
         out.reset();

         out.writeByte(TcpCacheOperations.REMOVE_DATA);
         out.writeObject(fqn);
         out.flush();
         Object retval = in.readObject();
         if (retval instanceof Exception)
         {
            throw (Exception) retval;
         }
      }
   }

   // ----------------- Lifecycle and no-op methods


   @Override
   public void start() throws IOException
   {
      try
      {
         InetSocketAddress address = new InetSocketAddress(config.getHost(), config.getPort());
         sock = new Socket();
         sock.setSoTimeout(config.getReadTimeout());
         sock.connect(address, config.getReadTimeout());
         out = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
         out.flush();
         in = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));
      }
      catch (ConnectException ce)
      {
         log.info("Unable to connect to TCP socket on interface " + config.getHost() + " and port " + config.getPort());
         throw ce;
      }
   }

   @Override
   public void stop()
   {
      try
      {
         if (in != null) in.close();
      }
      catch (IOException e)
      {
         if (trace) log.trace("Unable to close resource", e);
      }
      try
      {
         if (out != null) out.close();
      }
      catch (IOException e)
      {
         if (trace) log.trace("Unable to close resource", e);
      }
      try
      {
         if (sock != null) sock.close();
      }
      catch (IOException e)
      {
         if (trace) log.trace("Unable to close resource", e);
      }
   }

   @Override
   public void loadEntireState(ObjectOutputStream os) throws Exception
   {
      throw new UnsupportedOperationException("operation is not currently supported - need to define semantics first");
   }

   @Override
   public void loadState(Fqn subtree, ObjectOutputStream os) throws Exception
   {
      throw new UnsupportedOperationException("operation is not currently supported - need to define semantics first");
   }

   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      throw new UnsupportedOperationException("operation is not currently supported - need to define semantics first");
   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      throw new UnsupportedOperationException("operation is not currently supported - need to define semantics first");
   }
}
