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
package org.jboss.cache.loader.tcp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Cache;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.jmx.CacheJmxWrapperMBean;
import org.jboss.cache.util.concurrent.ConcurrentHashSet;
import org.jboss.cache.util.concurrent.SynchronizedRestarter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TCP-IP based CacheServer, setCache TcpDelegatingCacheLoader with host and port of this server
 *
 * @author Bela Ban
 * @author Brian Stansberry
 *
 */
public class TcpCacheServer implements TcpCacheServerMBean
{
   private ServerSocket srv_sock;
   private InetAddress bind_addr = null;
   private int port = 7500;
   private CacheSPI cache;
   private CacheJmxWrapperMBean wrapper;
   private String config;
   private boolean running = true;
   private final Set<Connection> conns = new ConcurrentHashSet<Connection>();
   private final SynchronizedRestarter restarter = new SynchronizedRestarter();
   /**
    * whether or not to start the server thread as a daemon.  Should be false if started from the command line, true if started as an MBean.
    */
   boolean daemon = true;
   private static final Log log = LogFactory.getLog(TcpCacheServer.class);
   private final static boolean trace = log.isTraceEnabled();


   public String getBindAddress()
   {
      return bind_addr != null ? bind_addr.toString() : "n/a";
   }

   public void setBindAddress(String bind_addr) throws UnknownHostException
   {
      if (bind_addr != null)
      {
         this.bind_addr = InetAddress.getByName(bind_addr);
      }
   }

   public int getPort()
   {
      return port;
   }

   public void setPort(int port)
   {
      this.port = port;
   }

   public String getConfig()
   {
      return config;
   }

   public void setConfig(String config)
   {
      this.config = config;
   }

   public Cache getCache()
   {
      return cache;
   }

   public void setCache(CacheSPI cache)
   {
      this.cache = cache;
   }

   public void setCacheJmxWrapper(CacheJmxWrapperMBean wrapper)
   {
      this.wrapper = wrapper;
   }

   public void start() throws Exception
   {
      if (cache == null)
      {
         // cache not directly set; get from wrapper or create from config
         if (wrapper != null)
         {
            cache = (CacheSPI) wrapper.getCache();

            if (cache == null)
            {
               throw new CacheException("cache cannot be obtained from CacheJmxWrapperMBean;" +
                     " be sure start() is invoked on wrapper before it is invoked on the" +
                     " TcpCacheServer");
            }
         }
         else if (config != null)
         {
            cache = (CacheSPI) new DefaultCacheFactory<Object, Object>().createCache(this.config);
         }
      }

      if (cache == null)
      {
         throw new CacheException("cache reference is not set");
      }


      srv_sock = new ServerSocket(port, 10, bind_addr);
      log.info("TcpCacheServer listening on : " + srv_sock.getInetAddress() + ":" + srv_sock.getLocalPort());

      running = true;

      Thread serverThread = new Thread("TcpCacheServer")
      {
         @Override
         public void run()
         {
            boolean attemptRestart = false;
            try
            {
               while (running)
               {
                  Socket client_sock = srv_sock.accept();
                  Connection conn = new Connection(client_sock, cache);
                  conns.add(conn);
                  conn.start();
               }
            }
            catch (SocketException se)
            {
               if (!running)
               {
                  // this is because of the stop() lifecycle method being called.
                  // ignore.
                  log.info("Shutting down TcpCacheServer");
               }
               else
               {
                  log.error("Caught exception! Attempting a server restart", se);
                  attemptRestart = true;
               }
            }
            catch (IOException e)
            {
               log.error("Caught exception! Attempting a server restart", e);
               attemptRestart = true;
            }

            if (attemptRestart)
            {
               try
               {
                  restarter.restartComponent(TcpCacheServer.this);
               }
               catch (Exception e)
               {
                  throw new CacheException("Unable to restart TcpCacheServer", e);
               }
            }
         }
      };
      serverThread.setDaemon(daemon);
      serverThread.start();

   }

   public void stop()
   {
      running = false;
      synchronized (conns)
      {
         // Connection.close() removes conn from the list,
         // so copy off the list first to avoid ConcurrentModificationException
         List<Connection> copy = new ArrayList<Connection>(conns);
         for (Connection conn : copy) conn.close();
         conns.clear();
      }

      if (srv_sock != null)
      {
         try
         {
            srv_sock.close();
         }
         catch (IOException e)
         {
            // nada
         }
         srv_sock = null;
      }
   }


   public String getConnections()
   {
      synchronized (conns)
      {
         StringBuilder sb = new StringBuilder();
         sb.append(conns.size()).append(" connections:\n");
         for (Connection c : conns)
         {
            sb.append(c).append("\n");
         }
         return sb.toString();
      }
   }


   public void create()
   {
   }

   public void destroy()
   {
   }


   private class Connection implements Runnable
   {
      private Socket sock = null;
      private ObjectInputStream input = null;
      private ObjectOutputStream output = null;
      private CacheSPI c;
      private Thread t = null;

      public Connection(Socket sock, CacheSPI cache) throws IOException
      {
         this.sock = sock;

         output = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
         output.flush();

         input = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));

         c = cache;
      }


      public void start()
      {
         t = new Thread(this, "TcpCacheServer.Connection(" + sock.getPort() + ")");
         t.setDaemon(true);
         t.start();
      }

      public void close()
      {
         t = null;
         try
         {
            if (output != null) output.close();
         }
         catch (Throwable th)
         {
            if (trace) log.trace("Unable to close resource", th);
         }
         try
         {
            if (input != null) input.close();
         }
         catch (Throwable th)
         {
            if (trace) log.trace("Unable to close resource", th);
         }
         try
         {
            if (sock != null) sock.close();
         }
         catch (Throwable th)
         {
            if (trace) log.trace("Unable to close resource", th);
         }

         // remove self from connections list
         conns.remove(this);
      }

      public void run()
      {
         int op;
         Fqn fqn;
         Object key, val, retval;
         NodeSPI n;
         boolean flag;

         while (t != null && t.equals(Thread.currentThread()) && t.isAlive())
         {
            try
            {
               if (trace) log.trace("Reading next byte");
               op = input.readByte();
            }
            catch (IOException e)
            {
               log.debug("Client closed socket");
               close();
               break;
            }

            try
            {
               if (trace) log.trace("Resetting output");
               output.reset();
               switch (op)
               {
                  case TcpCacheOperations.GET_CHILDREN_NAMES:
                     fqn = (Fqn) input.readObject();
                     Node node = c.getRoot().getChild(fqn);
                     Set<Object> children = node == null ? Collections.emptySet() : new HashSet(node.getChildrenNames());
                     output.writeObject(children);
                     break;
                  case TcpCacheOperations.GET_KEY:
                     fqn = (Fqn) input.readObject();
                     key = input.readObject();
                     retval = c.get(fqn, key);
                     output.writeObject(retval);
                     break;
                  case TcpCacheOperations.GET:
                     fqn = (Fqn) input.readObject();
                     n = c.getNode(fqn);
                     if (n == null)
                     {
                        // node doesn't exist - return null
                        output.writeObject(null);
                        break;
                     }
                     Map map = n.getData();
                     if (map == null)
                     {
                        map = Collections.emptyMap();
                     }
                     output.writeObject(map);
                     break;
                  case TcpCacheOperations.EXISTS:
                     fqn = (Fqn) input.readObject();
                     flag = c.getRoot().hasChild(fqn);
                     output.writeObject(flag);
                     break;
                  case TcpCacheOperations.PUT_KEY_VAL:
                     fqn = (Fqn) input.readObject();
                     key = input.readObject();
                     val = input.readObject();
                     retval = c.put(fqn, key, val);
                     output.writeObject(retval);
                     break;
                  case TcpCacheOperations.PUT:
                     fqn = (Fqn) input.readObject();
                     map = (Map) input.readObject();
                     c.put(fqn, map);
                     output.writeObject(Boolean.TRUE);
                     break;

                  case TcpCacheOperations.PUT_LIST:
                     int length = input.readInt();
                     retval = Boolean.TRUE;
                     if (length > 0)
                     {
                        Modification mod;
                        List<Modification> mods = new ArrayList<Modification>(length);
                        for (int i = 0; i < length; i++)
                        {
                           mod = new Modification();
                           mod.readExternal(input);
                           mods.add(mod);
                        }
                        try
                        {
                           handleModifications(mods);
                        }
                        catch (Exception ex)
                        {
                           retval = ex;
                        }
                     }
                     output.writeObject(retval);
                     break;
                  case TcpCacheOperations.REMOVE_KEY:
                     fqn = (Fqn) input.readObject();
                     key = input.readObject();
                     retval = c.remove(fqn, key);
                     output.writeObject(retval);
                     break;
                  case TcpCacheOperations.REMOVE:
                     fqn = (Fqn) input.readObject();
                     c.removeNode(fqn);
                     output.writeObject(Boolean.TRUE);
                     break;
                  case TcpCacheOperations.REMOVE_DATA:
                     fqn = (Fqn) input.readObject();
                     node = c.getRoot().getChild(fqn);
                     if (node != null)
                     {
                        node.clearData();
                        output.writeObject(true);
                     }
                     else
                     {
                        output.writeObject(false);
                     }

                     break;
                  case TcpCacheOperations.LOAD_ENTIRE_STATE:
                     ObjectOutputStream os = (ObjectOutputStream) input.readObject();

                     if (c.getCacheLoaderManager() != null)
                     {
                        c.getCacheLoaderManager().getCacheLoader().loadEntireState(os);
                     }
                     output.writeObject(Boolean.TRUE);
                     break;
                  case TcpCacheOperations.STORE_ENTIRE_STATE:
                     ObjectInputStream is = (ObjectInputStream) input.readObject();
                     if (c.getCacheLoaderManager() != null)
                     {
                        c.getCacheLoaderManager().getCacheLoader().storeEntireState(is);
                     }
                     output.writeObject(Boolean.TRUE);
                     break;
                  default:
                     log.error("Operation " + op + " unknown");
                     break;
               }
               if (trace) log.trace("Flushing stream");
               output.flush();
            }
            catch (Exception e)
            {
               log.debug(e, e);
               try
               {
                  output.writeObject(e);
                  output.flush();
               }
               catch (IOException e1)
               {
                  log.error(e1, e1);
               }
            }
         }
      }


      @Override
      public String toString()
      {
         StringBuilder sb = new StringBuilder();
         if (sock != null)
         {
            sb.append(sock.getRemoteSocketAddress());
         }
         return sb.toString();
      }

      protected void handleModifications(List<Modification> modifications) throws CacheException
      {

         for (Modification m : modifications)
         {
            switch (m.getType())
            {
               case PUT_DATA:
                  c.put(m.getFqn(), m.getData());
                  break;
               case PUT_DATA_ERASE:
                  c.put(m.getFqn(), m.getData());
                  break;
               case PUT_KEY_VALUE:
                  c.put(m.getFqn(), m.getKey(), m.getValue());
                  break;
               case REMOVE_DATA:
                  Node n = c.getRoot().getChild(m.getFqn());
                  if (n != null) n.clearData();
                  break;
               case REMOVE_KEY_VALUE:
                  c.remove(m.getFqn(), m.getKey());
                  break;
               case REMOVE_NODE:
                  c.removeNode(m.getFqn());
                  break;
               case MOVE:
                  c.move(m.getFqn(), m.getFqn2());
                  break;
               default:
                  log.error("modification type " + m.getType() + " not known");
                  break;
            }
         }
      }


   }


   public static void main(String[] args) throws Exception
   {
      String bind_addr = null;
      int port = 7500;
      TcpCacheServer server;
      String config = null;

      for (int i = 0; i < args.length; i++)
      {
         if (args[i].equals("-bind_addr"))
         {
            bind_addr = args[++i];
            continue;
         }
         if (args[i].equals("-port"))
         {
            port = Integer.parseInt(args[++i]);
            continue;
         }
         if (args[i].equals("-config"))
         {
            config = args[++i];
            continue;
         }
         help();
         return;
      }
      server = new TcpCacheServer();
      server.daemon = false;
      server.setBindAddress(bind_addr);
      server.setPort(port);
      server.setConfig(config);
      server.create();
      server.start();
   }


   private static void help()
   {
      System.out.println("TcpCacheServer [-bind_addr <address>] [-port <port>] [-config <config file>] [-help]");
   }
}
