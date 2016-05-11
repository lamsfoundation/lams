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
package org.jboss.cache.loader.jdbm;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.btree.BTree;
import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.FqnComparator;
import org.jboss.cache.Modification;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.loader.AbstractCacheLoader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A persistent <code>CacheLoader</code> based on the JDBM project.
 * See http://jdbm.sourceforge.net/ .
 * Does not support transaction isolation.
 * <p/>
 * <p>The configuration string format is:</p>
 * <pre>environmentDirectoryName[#databaseName]</pre>
 * <p>where databaseName, if omitted, defaults to the ClusterName property
 * of the Cache.</p>
 * <p/>
 * Data is sorted out like:
 * <pre>
 * / = N
 * /node1 = N
 * /node1/K/k1 = v1
 * /node1/K/k2 = v2
 * /node2 = N
 * /node2/node3 = N
 * /node2/node3/K/k1 = v1
 * /node2/node3/K/k2 = v2
 * /node2/node4 = N
 * </pre>
 * N represents a node, K represents a key block. k and v represent key/value
 * pairs.
 * <p/>
 * The browse operations lock the entire tree; eventually the JDBM team plans to fix this.
 *
 * @author Elias Ross
 *
 */
@ThreadSafe
public class JdbmCacheLoader extends AbstractCacheLoader
{
   private static final Log log = LogFactory.getLog(JdbmCacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();

   private static final String KEYS = "K";
   private static final String NODE = "N";
   private static final String NAME = "JdbmCacheLoader";

   private JdbmCacheLoaderConfig config;
   RecordManager recman;
   BTree tree;

   /*
    * Service implementation -- lifecycle methods.
    * Note that setConfig() and setCache() are called before create().
    */

   @Override
   public void create() throws Exception
   {
      checkNotOpen();
   }

   /**
    * Opens the environment and the database specified by the configuration
    * string.  The environment and databases are created if necessary.
    */
   @Override
   public void start()
         throws Exception
   {

      log.trace("Starting " + getClass().getSimpleName() + " instance.");
      checkNotOpen();
      checkNonNull(cache, "CacheSPI object is required");

      String locationStr = config.getLocation();
      if (locationStr == null)
      {
         locationStr = System.getProperty("java.io.tmpdir");
         config.setLocation(locationStr);
      }

      // JBCACHE-1448 db name parsing fix courtesy of Ciro Cavani
      /* Parse config string. */
      int offset = locationStr.indexOf('#');
      String cacheDbName;
      if (offset >= 0 && offset < locationStr.length() - 1)
      {
         cacheDbName = locationStr.substring(offset + 1);
         locationStr = locationStr.substring(0, offset);
      }
      else
      {
         cacheDbName = cache.getClusterName();
         if (cacheDbName == null) cacheDbName = "CacheInstance-" + System.identityHashCode(cache);
      }

      // test location
      File location = new File(locationStr);
      if (!location.exists())
      {
         boolean created = location.mkdirs();
         if (!created) throw new IOException("Unable to create cache loader location " + location);

      }
      if (!location.isDirectory())
      {
         throw new IOException("Cache loader location [" + location + "] is not a directory!");
      }

      try
      {
         openDatabase(new File(location, cacheDbName));
      }
      catch (Exception e)
      {
         destroy();
         throw e;
      }
   }

   /**
    * Opens all databases and initializes database related information.
    */
   private void openDatabase(File f)
         throws Exception
   {
      Properties props = new Properties();
      // Incorporate properties from setConfig() ?
      // props.put(RecordManagerOptions.SERIALIZER, RecordManagerOptions.SERIALIZER_EXTENSIBLE);
      // props.put(RecordManagerOptions.PROFILE_SERIALIZATION, "false");
      recman = RecordManagerFactory.createRecordManager(f.toString(), props);
      long recid = recman.getNamedObject(NAME);
      log.debug(NAME + " located as " + recid);
      if (recid == 0)
      {
         tree = BTree.createInstance(recman, new JdbmFqnComparator());
         recman.setNamedObject(NAME, tree.getRecid());
      }
      else
      {
         tree = BTree.load(recman, recid);
      }

      log.info("JDBM database " + f + " opened with " + tree.size() + " entries");
   }

   /**
    * Closes all databases, ignoring exceptions, and nulls references to all
    * database related information.
    */
   private void closeDatabases()
   {
      if (recman != null)
      {
         try
         {
            recman.close();
         }
         catch (Exception shouldNotOccur)
         {
            log.warn("Caught unexpected exception", shouldNotOccur);
         }
      }
      recman = null;
      tree = null;
   }

   /**
    * Closes the databases and environment, and nulls references to them.
    */
   @Override
   public void stop()
   {
      log.debug("stop");
      closeDatabases();
   }

   /*
    * CacheLoader implementation.
    */

   /**
    * Sets the configuration string for this cache loader.
    */
   public void setConfig(IndividualCacheLoaderConfig base)
   {
      checkNotOpen();

      if (base instanceof JdbmCacheLoaderConfig)
      {
         this.config = (JdbmCacheLoaderConfig) base;
      }
      else
      {
         config = createConfig(base);
      }

      if (trace) log.trace("Configuring cache loader with location = " + config.getLocation());
   }

   JdbmCacheLoaderConfig createConfig(IndividualCacheLoaderConfig base)
   {
      return new JdbmCacheLoaderConfig(base);
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }

   /**
    * Sets the CacheImpl owner of this cache loader.
    */
   @Override
   public void setCache(CacheSPI c)
   {
      super.setCache(c);
      checkNotOpen();
   }

   /**
    * Returns a special FQN for keys of a node.
    */
   private Fqn keys(Fqn name)
   {
      return Fqn.fromRelativeElements(name, KEYS);
   }

   /**
    * Returns a special FQN for key of a node.
    */
   private Fqn key(Fqn name, Object key)
   {
      return Fqn.fromRelativeElements(name, KEYS, nullMask(key));
   }

   /**
    * Returns an unmodifiable set of relative children names, or
    * returns null if the parent node is not found or if no children are found.
    * This is a fairly expensive operation, and is assumed to be performed by
    * browser applications.  Calling this method as part of a run-time
    * transaction is not recommended.
    */
   public Set<?> getChildrenNames(Fqn name)
         throws IOException
   {

      if (trace)
      {
         log.trace("getChildrenNames " + name);
      }

      synchronized (tree)
      {
         return getChildrenNames0(name);
      }
   }

   Set<Object> getChildrenNames0(Fqn name) throws IOException
   {
      TupleBrowser browser = tree.browse(name);
      Tuple t = new Tuple();

      if (browser.getNext(t))
      {
         if (!t.getValue().equals(NODE))
         {
            log.trace(" not a node");
            return null;
         }
      }
      else
      {
         log.trace(" no nodes");
         return null;
      }

      Set<Object> set = new HashSet<Object>();

      // Want only /a/b/c/X nodes
      int depth = name.size() + 1;
      while (browser.getNext(t))
      {
         Fqn fqn = (Fqn) t.getKey();
         int size = fqn.size();
         if (size < depth)
         {
            break;
         }
         if (size == depth && t.getValue().equals(NODE))
         {
            set.add(fqn.getLastElement());
         }
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
    * This operation is always non-transactional, even in a transactional
    * environment.
    */
   public Map get(Fqn name)
         throws Exception
   {

      checkOpen();
      checkNonNull(name, "name");

      if (tree.find(name) == null)
      {
         if (trace)
         {
            log.trace("get, no node: " + name);
         }
         return null;
      }

      Fqn keys = keys(name);
      Tuple t = new Tuple();
      Map map = new HashMap();

      synchronized (tree)
      {
         TupleBrowser browser = tree.browse(keys);
         while (browser.getNext(t))
         {
            Fqn fqn = (Fqn) t.getKey();
            if (!fqn.isChildOf(keys))
            {
               break;
            }
            Object k = fqn.getLastElement();
            Object v = t.getValue();
            map.put(nullUnmask(k), nullUnmask(v));
         }
      }

      if (trace)
      {
         log.trace("get " + name + " map=" + map);
      }

      return map;
   }

   /**
    * Returns whether the given node exists.
    */
   public boolean exists(Fqn name) throws IOException
   {
      return tree.find(name) != null;
   }

   void commit() throws IOException
   {
      recman.commit();
   }

   /**
    * Stores a single FQN-key-value record.
    * Intended to be used in a non-transactional environment, but will use
    * auto-commit in a transactional environment.
    */
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      try
      {
         return put0(name, key, value);
      }
      finally
      {
         commit();
      }
   }

   Object put0(Fqn name, Object key, Object value) throws Exception
   {
      checkNonNull(name, "name");
      makeNode(name);
      Fqn rec = key(name, key);
      Object oldValue = insert(rec, value);
      if (trace)
      {
         log.trace("put " + rec + " value=" + value + " old=" + oldValue);
      }
      return oldValue;
   }

   /**
    * Stores a map of key-values for a given FQN, but does not delete existing
    * key-value pairs (that is, it does not erase).
    * Intended to be used in a non-transactional environment, but will use
    * auto-commit in a transactional environment.
    */
   public void put(Fqn name, Map values) throws Exception
   {
      put0(name, values);
      commit();
   }

   void put0(Fqn name, Map<?, ?> values) throws Exception
   {
      if (trace)
      {
         log.trace("put " + name + " values=" + values);
      }
      makeNode(name);
      if (values == null)
      {
         return;
      }
      
      Fqn keys = keys(name);
      Tuple t = new Tuple();
      List<Object> removeList = new ArrayList<Object>();
      synchronized (tree)
      {
         TupleBrowser browser = tree.browse(keys);
         while (browser.getNext(t))
         {
            Fqn fqn = (Fqn) t.getKey();
            if (!fqn.isChildOf(keys))
            {
               break;
            }
            Object k = fqn.getLastElement();
            if (!values.containsKey(nullUnmask(k)))
               removeList.add(fqn);
         }
      }

      for (Map.Entry me : values.entrySet())
      {
         Fqn rec = key(name, me.getKey());
         insert(rec, nullMask(me.getValue()));
      }
      for (Object o : removeList)
      {
         tree.remove(o);
      }
   }

   /**
    * Marks a FQN as a node.
    */
   private void makeNode(Fqn fqn) throws IOException
   {
      if (exists(fqn))
      {
         return;
      }
      int size = fqn.size();
      // TODO should not modify so darn often
      for (int i = size; i >= 0; i--)
      {
         Fqn child = fqn.getAncestor(i);
         Object existing = tree.insert(child, NODE, false);
         if (existing != null)
         {
            break;
         }
      }
   }

   private Object insert(Fqn fqn, Object value) throws IOException
   {
      return nullUnmask(tree.insert(fqn, nullMask(value), true));
   }

   /**
    * Erase a FQN and children.
    * Does not commit.
    */
   private void erase0(Fqn name)
         throws IOException
   {
      erase0(name, true);
   }

   void erase0(Fqn name, boolean self)
         throws IOException
   {
      if (trace)
      {
         log.trace("erase " + name + " self=" + self);
      }
      List<Object> removeList = new ArrayList<Object>();
      synchronized (tree)
      {
         TupleBrowser browser = tree.browse(name);
         Tuple t = new Tuple();
         boolean first = true;
         while (browser.getNext(t))
         {
	        if (first && !self)
	        {
	           first = false;
	           continue;
	        }
            Fqn fqn = (Fqn) t.getKey();
            if (!fqn.isChildOrEquals(name))
            {
               break;
            }
            removeList.add(fqn);
         }
      }
      for (Object o : removeList)
         tree.remove(o);
   }

   /**
    * Erase a FQN's key.
    * Does not commit.
    */
   Object eraseKey0(Fqn name, Object key)
         throws IOException
   {
      if (trace)
      {
         log.trace("eraseKey " + name + " key " + key);
      }
      Fqn fqnKey = key(name, key);
      try
      {
         return tree.remove(fqnKey);
      }
      catch (IllegalArgumentException e)
      {
         // Seems to be harmless
         // log.warn("IllegalArgumentException for " + fqnKey);
         // dump();
         return null;
      }
   }

   /**
    * Applies the given modifications.
    * Intended to be used in a non-transactional environment, but will use
    * auto-commit in a transactional environment.
    */
   @Override
   public void put(List<Modification> modifications)
         throws Exception
   {
      checkOpen();
      checkNonNull(modifications, "modifications");

      for (Modification m : modifications)
      {
         switch (m.getType())
         {
            case PUT_DATA:
               put0(m.getFqn(), m.getData());
               break;
            case PUT_DATA_ERASE:
               erase0(m.getFqn(), false);
               put0(m.getFqn(), m.getData());
               break;
            case PUT_KEY_VALUE:
               put0(m.getFqn(), m.getKey(), m.getValue());
               break;
            case REMOVE_DATA:
               erase0(m.getFqn(), false);
               break;
            case REMOVE_KEY_VALUE:
               eraseKey0(m.getFqn(), m.getKey());
               break;
            case REMOVE_NODE:
               erase0(m.getFqn());
               break;
            case MOVE:
               move(m.getFqn(), m.getFqn2());
               break;
            default:
               throw new CacheException("Unknown modification " + m.getType());
         }
      }
      commit();
   }

   /**
    * Deletes the node for a given FQN and all its descendent nodes.
    * Intended to be used in a non-transactional environment, but will use
    * auto-commit in a transactional environment.
    */
   public void remove(Fqn name)
         throws IOException
   {
      erase0(name);
      commit();
   }

   /**
    * Deletes a single FQN-key-value record.
    * Intended to be used in a non-transactional environment, but will use
    * auto-commit in a transactional environment.
    */
   public Object remove(Fqn name, Object key)
         throws Exception
   {

      try
      {
         return eraseKey0(name, key);
      }
      finally
      {
         commit();
      }
   }

   /**
    * Clears the map for the given node, but does not remove the node.
    */
   public void removeData(Fqn name)
         throws Exception
   {
      erase0(name, false);
   }

   /**
    * Throws an exception if the environment is not open.
    */
   void checkOpen()
   {
      if (tree == null)
      {
         throw new IllegalStateException(
               "Operation not allowed before calling create()");
      }
   }

   /**
    * Throws an exception if the environment is not open.
    */
   protected void checkNotOpen()
   {
      if (tree != null)
      {
         throw new IllegalStateException(
               "Operation not allowed after calling create()");
      }
   }

   /**
    * Throws an exception if the parameter is null.
    */
   protected void checkNonNull(Object param, String paramName)
   {
      if (param == null)
      {
         throw new NullPointerException(
               "Parameter must not be null: " + paramName);
      }
   }

   private Object nullMask(Object o)
   {
      return (o == null) ? Null.NULL : o;
   }

   private Object nullUnmask(Object o)
   {
      return (o == Null.NULL) ? null : o;
   }

   /**
    * Dumps the tree to debug.
    */
   public void dump() throws IOException
   {
      dump(Fqn.ROOT);
   }

   /**
    * Dumps the tree past the key to debug.
    */
   public void dump(Object key) throws IOException
   {
      TupleBrowser browser = tree.browse(key);
      Tuple t = new Tuple();
      log.debug("contents: " + key);
      while (browser.getNext(t))
      {
         log.debug(t.getKey() + "\t" + t.getValue());
      }
      log.debug("");
   }
   
   /**
    * Returns the number of database record nodes.
    */
   public int size() {
      return tree.size();
   }

   @Override
   public String toString()
   {
      BTree bt = tree;
      int size = (bt == null) ? -1 : bt.size();
      return "JdbmCacheLoader locationStr=" + config.getLocation() +
            " size=" + size;
   }

}

class JdbmFqnComparator extends FqnComparator implements Serializable
{
   private static final long serialVersionUID = 1000;
}

