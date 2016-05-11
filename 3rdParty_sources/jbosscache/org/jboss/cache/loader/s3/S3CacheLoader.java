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

import net.jcip.annotations.ThreadSafe;
import net.noderunner.amazon.s3.Bucket;
import net.noderunner.amazon.s3.Connection;
import net.noderunner.amazon.s3.Entry;
import net.noderunner.amazon.s3.GetStreamResponse;
import net.noderunner.amazon.s3.ListResponse;
import net.noderunner.amazon.s3.Response;
import net.noderunner.amazon.s3.S3Object;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.loader.AbstractCacheLoader;

import java.io.BufferedInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Uses the Amazon S3 service for storage.
 * See http://aws.amazon.com/ for information.
 * Does not support transaction isolation.
 * <p/>
 * Data is stored in a single bucket location.
 * The FQN comprises the key of the storage, the data the data itself.
 * <p/>
 * Internal structure:
 * <pre>
 * A/
 * B/_rootchild
 * C/_rootchild/_child1
 * C/_rootchild/_child2
 * C/_rootchild/_child3
 * B/_root2
 * </pre>
 * The FQN component type is either prefixed with a _ for String, or a primitive type prefix.
 * <p/>
 * All put and many remove operations require fetching and merging data before storing data,
 * which increases latency. This fetching can be turned off. See {@link S3LoaderConfig#getOptimize()}.
 * <p/>
 * Parent nodes are added to the store as needed.
 * For example, when doing a put("/a/b/c"), the nodes "/a/b" and "/a" are created
 * if they do not exist. To prevent unnecessary checks of the store,
 * a local cache is kept of these "parent nodes". With multiple sites removing
 * parent nodes, this can potentially need to inconsistencies. To disable caching,
 * set {@link S3LoaderConfig#setParentCache} to false.
 * <p/>
 *
 * @author Elias Ross
 *
 */
@ThreadSafe
@SuppressWarnings("unchecked")
public class S3CacheLoader extends AbstractCacheLoader
{
   private static final Log log = LogFactory.getLog(S3CacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();

   /**
    * Max number of dummy parent nodes to cache.
    */
   private static final int PARENT_CACHE_SIZE = 100;

   /**
    * Configuration.
    */
   private S3LoaderConfig config;

   /**
    * Unit separator.
    */
   private final char SEP = Fqn.SEPARATOR.charAt(0);

   /**
    * Zero depth prefix.
    */
   private final char DEPTH_0 = 'A';

   /**
    * Stateless connection; thread safe.
    */
   private Connection connection;

   /**
    * Map classes to characters.
    */
   private static Map<Class<?>, Character> prefix = new HashMap<Class<?>, Character>();

   static
   {
      prefix.put(Byte.class, 'B');
      prefix.put(Character.class, 'C');
      prefix.put(Double.class, 'D');
      prefix.put(Float.class, 'F');
      prefix.put(Integer.class, 'I');
      prefix.put(Long.class, 'J');
      prefix.put(Short.class, 'S');
      prefix.put(Boolean.class, 'Z');
   }

   /**
    * Empty parent nodes whose existence is cached.
    * Empty parents are required to ensure {@link #getChildrenNames(Fqn)}
    * and recursive {@link #remove(Fqn)} work correctly.
    */
   private Set<Fqn> parents = Collections.synchronizedSet(new HashSet<Fqn>());

   /**
    * Empty HashMap, serialized, lazy created.
    */
   private S3Object dummyObj;

   /**
    * This cache loader is stateless, but as part of initialization access the service.
    * Creates a new bucket, if necessary.
    */
   @Override
   public void start() throws Exception
   {
      log.debug("Starting");
      try
      {
         this.connection = config.getConnection();
         Response create = connection.create(getBucket(), config.getLocation());
         if (!create.isOk())
            throw new S3Exception("Unable to create bucket: " + create);
         log.info("S3 accessed successfully. Bucket created: " + create);
      }
      catch (Exception e)
      {
         destroy();
         throw e;
      }
   }

   /**
    * Closes the connection; shuts down the HTTP connection pool.
    */
   @Override
   public void stop()
   {
      log.debug("stop");
      connection.shutdown();
   }

   /**
    * Sets the configuration string for this cache loader.
    */
   public void setConfig(IndividualCacheLoaderConfig base)
   {
      if (base instanceof S3LoaderConfig)
      {
         this.config = (S3LoaderConfig) base;
      }
      else
      {
         config = new S3LoaderConfig(base);
      }

      if (trace)
         log.trace("config=" + config);
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }

   private String key(Fqn fqn)
   {
      return key(fqn.size(), fqn).toString();
   }

   private String children(Fqn fqn)
   {
      return key(fqn.size() + 1, fqn).append(SEP).toString();
   }

   private StringBuilder key(int depth, Fqn fqn)
   {
      StringBuilder sb = new StringBuilder();
      List l = fqn.peekElements();
      sb.append((char) (DEPTH_0 + depth));
      for (Object o : l)
      {
         sb.append(SEP);
         if (o == null)
            sb.append("_null");
         else if (o instanceof String)
            sb.append("_").append(o);
         else
         {
            // TODO
            Character c = prefix.get(o.getClass());
            if (c == null)
               throw new IllegalArgumentException("not supported " + o.getClass());
            sb.append(c.charValue()).append(o);
         }
      }
      return sb;
   }

   /**
    * Returns an unmodifiable set of relative children names, or
    * returns null if the parent node is not found or if no children are found.
    */
   public Set<String> getChildrenNames(Fqn name) throws Exception
   {
      String children = children(name);
      ListResponse response = connection.list(getBucket(), children);
      if (trace)
      {
         log.trace("getChildrenNames " + name + " response=" + response);
      }
      if (response.isNotFound())
         return null;
      if (!response.isOk())
         throw new Exception("List failed " + response);

      Set<String> set = new HashSet<String>();
      for (Entry e : response.getEntries())
      {
         // TODO decode prefix
         set.add(e.getKey().substring(children.length() + 1));
      }

      if (set.isEmpty())
      {
         return null;
      }

      return Collections.unmodifiableSet(set);
   }

   /**
    * Returns a map containing all key-value pairs for the given FQN, or null
    * if the node is not present.
    */
   public Map get(Fqn name) throws Exception
   {
      GetStreamResponse response = connection.getStream(getBucket(), key(name));
      try
      {
         if (trace)
         {
            log.trace("get " + name + " response=" + response);
         }

         if (response.isNotFound())
            return null;
         if (!response.isOk())
            throw new S3Exception("get failed " + response);

         BufferedInputStream is = new BufferedInputStream(response.getInputStream());
         Map map = (Map) getMarshaller().objectFromStream(is);
         response.release();
         return map;
      }
      finally
      {
         response.release();
      }
   }

   private Bucket getBucket()
   {
      return config.getBucket();
   }

   /**
    * Returns whether the given node exists.
    */
   public boolean exists(Fqn name) throws Exception
   {
      Response response = connection.head(getBucket(), key(name));
      if (trace)
      {
         log.trace("exists " + name + " response=" + response);
      }
      return response.isOk();
   }

   private S3Object wrap(Map map) throws Exception
   {
      byte[] b = getMarshaller().objectToByteBuffer(map);
      return new S3Object(b);
   }

   /**
    * Stores a single FQN-key-value record.
    * This is slow, so avoid this method.
    */
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      Map map = get(name);
      Object oldValue;
      if (map != null)
      {
         oldValue = map.put(key, value);
      }
      else
      {
         map = new HashMap(Collections.singletonMap(key, value));
         oldValue = null;
      }
      put0(name, map);
      return oldValue;
   }

   /**
    * Puts by replacing all the contents of the node.
    */
   private void put0(Fqn name, Map map) throws Exception
   {
      put0(name, wrap(map));
   }

   private void put0(Fqn name, S3Object obj) throws Exception
   {
      Response response = connection.put(getBucket(), key(name), obj);
      if (trace)
      {
         log.trace("put " + name + " obj=" + obj + " response=" + response);
      }
      ensureParent(name);
      if (!response.isOk())
         throw new S3Exception("Put failed " + response);
   }

   private S3Object getDummy() throws Exception
   {
      if (dummyObj != null)
         return dummyObj;
      return dummyObj = wrap(new HashMap(0));
   }

   /**
    * Ensures a parent node exists.
    * Calls recursively to initialize parents as necessary.
    */
   private void ensureParent(Fqn name) throws Exception
   {
      if (name.size() <= 1)
         return;
      Fqn parent = name.getParent();
      boolean cache = config.getParentCache();
      if (cache && parents.contains(parent))
         return;
      // potential race condition between exists and put
      if (!exists(parent))
         put0(parent, getDummy());
      if (cache)
      {
         parents.add(parent);
         if (parents.size() > PARENT_CACHE_SIZE)
         {
            parents.clear();
         }
      }
      ensureParent(parent);
   }

   /**
    * Removes a key from an FQN.
    * Not very fast.
    */
   public Object remove(Fqn name, Object key) throws Exception
   {
      Map map = get(name);
      Object oldValue;
      if (map != null)
      {
         oldValue = map.remove(key);
      }
      else
      {
         oldValue = null;
      }
      put0(name, map);
      return oldValue;
   }

   /**
    * Stores a map of key-values for a given FQN, but does not delete existing
    * key-value pairs (that is, it does not erase).
    */
   public void put(Fqn name, Map<Object, Object> values) throws Exception
   {
      if (values == null)
         values = Collections.emptyMap();
      put0(name, values);
   }

   /**
    * Deletes the node for a given FQN and all its descendant nodes.
    */
   public void remove(Fqn name) throws Exception
   {
      /*
      if (name.isRoot())
      {
        log.trace("optimized delete");
       connection.delete(getBucket()).assertOk();
       connection.create(getBucket()).assertOk();
        log.trace("done");
       return;
      }
      */
      Set<String> children = getChildrenNames(name);
      if (children != null)
      {
         log.trace("remove children: " + children);
         for (String child : children)
         {
            remove(Fqn.fromRelativeElements(name, child));
         }
      }
      Response response = connection.delete(getBucket(), key(name));
      if (trace)
      {
         log.trace("delete " + name + " response=" + response);
      }
      if (!response.isOk() && !response.isNotFound())
         throw new S3Exception("delete failed " + response);
      parents.remove(name);
   }

   /**
    * Clears the map for the given node, but does not remove the node.
    */
   public void removeData(Fqn name) throws Exception
   {
      put0(name, getDummy());
   }

}