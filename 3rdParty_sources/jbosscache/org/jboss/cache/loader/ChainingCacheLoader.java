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

import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.RegionManager;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This decorator is used whenever more than one cache loader is configured.  READ operations are directed to
 * each of the cache loaders (in the order which they were configured) until a non-null (or non-empty in the case
 * of retrieving collection objects) result is achieved.
 * <p/>
 * WRITE operations are propagated to ALL registered cacheloaders that specified set ignoreModifications to false.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public class ChainingCacheLoader extends AbstractCacheLoader
{
   private final List<CacheLoader> cacheLoaders = new ArrayList<CacheLoader>(2);
   private final List<CacheLoader> writeCacheLoaders = new ArrayList<CacheLoader>(2);
   private final List<CacheLoaderConfig.IndividualCacheLoaderConfig> cacheLoaderConfigs = new ArrayList<CacheLoaderConfig.IndividualCacheLoaderConfig>(2);
   private ComponentRegistry registry;

   /**
    * Sets the configuration. Will be called before {@link #create()} and {@link #start()}
    *
    * @param config ignored
    */
   public void setConfig(IndividualCacheLoaderConfig config)
   {
      // don't do much here?
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return null;
   }

   @Inject
   public void injectDependencies(ComponentRegistry registry)
   {
      this.registry = registry;
   }


   /**
    * Returns a list of children names, all names are <em>relative</em>. Returns null if the parent node is not found.
    * The returned set must not be modified, e.g. use Collections.unmodifiableSet(s) to return the result
    *
    * @param fqn The FQN of the parent
    * @return Set<?>. A list of children. Returns null if no children nodes are present, or the parent is
    *         not present
    */
   public Set<?> getChildrenNames(Fqn fqn) throws Exception
   {
      Set<?> answer = null;
      for (CacheLoader l : cacheLoaders)
      {
         answer = l.getChildrenNames(fqn);
         if (answer != null && answer.size() > 0) break;
      }
      return answer;
   }

   /**
    * Returns all keys and values from the persistent store, given a fully qualified name
    *
    * @param name
    * @return Map<Object,Object> of keys and values for the given node. Returns null if the node was not found, or
    *         if the node has no attributes
    * @throws Exception
    */
   public Map get(Fqn name) throws Exception
   {
      Map answer = null;
      for (CacheLoader l : cacheLoaders)
      {
         answer = l.get(name);
         if (answer != null) break;
      }
      return answer;
   }

   /**
    * Checks whether the CacheLoader has a node with Fqn
    *
    * @param name
    * @return True if node exists, false otherwise
    */
   public boolean exists(Fqn name) throws Exception
   {
      boolean answer = false;
      for (CacheLoader l : cacheLoaders)
      {
         answer = l.exists(name);
         if (answer) break;
      }
      return answer;
   }

   /**
    * Inserts key and value into the attributes hashmap of the given node. If the node does not exist, all
    * parent nodes from the root down are created automatically. Returns the old value
    */
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      Object answer = null;
      Iterator<CacheLoader> i = writeCacheLoaders.iterator();
      boolean isFirst = true;
      while (i.hasNext())
      {
         CacheLoader l = i.next();
         Object tAnswer = l.put(name, key, value);
         if (isFirst)
         {
            answer = tAnswer;
            isFirst = false;
         }

      }
      return answer;
   }

   /**
    * Inserts all elements of attributes into the attributes hashmap of the given node, overwriting existing
    * attributes, but not clearing the existing hashmap before insertion (making it a union of existing and
    * new attributes)
    * If the node does not exist, all parent nodes from the root down are created automatically
    *
    * @param name       The fully qualified name of the node
    * @param attributes A Map of attributes. Can be null
    */
   public void put(Fqn name, Map attributes) throws Exception
   {
      for (CacheLoader l : writeCacheLoaders)
      {
         l.put(name, attributes);
      }
   }

   /**
    * Inserts all modifications to the backend store. Overwrite whatever is already in
    * the datastore.
    *
    * @param modifications A List<Modification> of modifications
    * @throws Exception
    */
   @Override
   public void put(List<Modification> modifications) throws Exception
   {
      for (CacheLoader l : writeCacheLoaders)
      {
         l.put(modifications);
      }
   }

   /**
    * Removes the given key and value from the attributes of the given node. No-op if node doesn't exist.
    * Returns the first response from the loader chain.
    */
   public Object remove(Fqn name, Object key) throws Exception
   {
      Object answer = null;
      Iterator<CacheLoader> i = writeCacheLoaders.iterator();
      boolean isFirst = true;
      while (i.hasNext())
      {
         CacheLoader l = i.next();
         Object tAnswer = l.remove(name, key);
         if (isFirst)
         {
            answer = tAnswer;
            isFirst = false;
         }
      }
      return answer;
   }

   /**
    * Removes the given node. If the node is the root of a subtree, this will recursively remove all subnodes,
    * depth-first
    */
   public void remove(Fqn name) throws Exception
   {
      for (CacheLoader l : writeCacheLoaders) l.remove(name);
   }

   /**
    * Removes all attributes from a given node, but doesn't delete the node itself
    *
    * @param name
    * @throws Exception
    */
   public void removeData(Fqn name) throws Exception
   {
      for (CacheLoader l : writeCacheLoaders)
      {
         l.removeData(name);
      }
   }

   /**
    * Prepare the modifications. For example, for a DB-based CacheLoader:
    * <ol>
    * <li>Create a local (JDBC) transaction
    * <li>Associate the local transaction with <code>tx</code> (tx is the key)
    * <li>Execute the coresponding SQL statements against the DB (statements derived from modifications)
    * </ol>
    * For non-transactional CacheLoader (e.g. file-based), this could be a null operation
    *
    * @param tx            The transaction, just used as a hashmap key
    * @param modifications List<Modification>, a list of all modifications within the given transaction
    * @param one_phase     Persist immediately and (for example) commit the local JDBC transaction as well. When true,
    *                      we won't get a {@link #commit(Object)} or {@link #rollback(Object)} method call later
    * @throws Exception
    */
   @Override
   public void prepare(Object tx, List modifications, boolean one_phase) throws Exception
   {
      for (CacheLoader l : writeCacheLoaders)
      {
         l.prepare(tx, modifications, one_phase);
      }
   }

   /**
    * Commit the transaction. A DB-based CacheLoader would look up the local JDBC transaction asociated
    * with <code>tx</code> and commit that transaction<br/>
    * Non-transactional CacheLoaders could simply write the data that was previously saved transiently under the
    * given <code>tx</code> key, to (for example) a file system (note this only holds if the previous prepare() did
    * not define one_phase=true
    *
    * @param tx
    */
   @Override
   public void commit(Object tx) throws Exception
   {
      for (CacheLoader l : writeCacheLoaders)
      {
         l.commit(tx);
      }
   }

   /**
    * Roll the transaction back. A DB-based CacheLoader would look up the local JDBC transaction asociated
    * with <code>tx</code> and roll back that transaction
    *
    * @param tx
    */
   @Override
   public void rollback(Object tx)
   {
      for (CacheLoader l : writeCacheLoaders)
      {
         l.rollback(tx);
      }
   }


   /**
    * Creates individual cache loaders.
    *
    * @throws Exception
    */
   @Override
   public void create() throws Exception
   {
      Iterator<CacheLoader> it = cacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> cfgIt = cacheLoaderConfigs.iterator();
      while (it.hasNext() && cfgIt.hasNext())
      {
         CacheLoader cl = it.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = cfgIt.next();
         cl.setConfig(cfg);
         registry.wireDependencies(cl);
         cl.create();
      }
   }

   @Override
   public void start() throws Exception
   {
      for (CacheLoader cacheLoader : cacheLoaders)
      {
         cacheLoader.start();
      }
   }

   @Override
   public void stop()
   {
      for (CacheLoader cacheLoader : cacheLoaders)
      {
         cacheLoader.stop();
      }
   }

   @Override
   public void destroy()
   {
      for (CacheLoader cacheLoader : cacheLoaders)
      {
         cacheLoader.destroy();
      }
   }

   /**
    * No-op, as this class doesn't directly use the ERegionManager.
    */
   @Override
   public void setRegionManager(RegionManager manager)
   {
      // no-op -- we don't do anything with the region manager
   }

   @Override
   public void loadEntireState(ObjectOutputStream os) throws Exception
   {
      Iterator<CacheLoader> i = cacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> cfgs = cacheLoaderConfigs.iterator();
      while (i.hasNext() && cfgs.hasNext())
      {
         CacheLoader l = i.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = cfgs.next();
         if (cfg.isFetchPersistentState())
         {
            l.loadEntireState(os);
            break;
         }
      }
   }

   @Override
   public void loadState(Fqn subtree, ObjectOutputStream os) throws Exception
   {
      Iterator<CacheLoader> i = cacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> cfgs = cacheLoaderConfigs.iterator();
      while (i.hasNext() && cfgs.hasNext())
      {
         CacheLoader l = i.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = cfgs.next();
         if (cfg.isFetchPersistentState())
         {
            l.loadState(subtree, os);
            break;
         }
      }
   }

   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      Iterator<CacheLoader> i = writeCacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> cfgs = cacheLoaderConfigs.iterator();
      while (i.hasNext())
      {
         CacheLoader l = i.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = cfgs.next();
         if (cfg.isFetchPersistentState())
         {
            l.storeEntireState(is);
            break;
         }
      }

   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      Iterator<CacheLoader> i = writeCacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> cfgs = cacheLoaderConfigs.iterator();
      while (i.hasNext())
      {
         CacheLoader l = i.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = cfgs.next();
         if (cfg.isFetchPersistentState())
         {
            l.storeState(subtree, is);
            break;
         }
      }
   }

   /**
    * Returns the number of cache loaders in the chain.
    */
   public int getSize()
   {
      return cacheLoaders.size();
   }

   /**
    * Returns a List<CacheLoader> of individual cache loaders configured.
    */
   public List<CacheLoader> getCacheLoaders()
   {
      return Collections.unmodifiableList(cacheLoaders);
   }

   /**
    * Adds a cache loader to the chain (always added at the end of the chain)
    *
    * @param l   the cache loader to add
    * @param cfg and its configuration
    */
   public void addCacheLoader(CacheLoader l, CacheLoaderConfig.IndividualCacheLoaderConfig cfg)
   {
      synchronized (this)
      {
         cacheLoaderConfigs.add(cfg);
         cacheLoaders.add(l);

         if (!cfg.isIgnoreModifications())
         {
            writeCacheLoaders.add(l);
         }
      }
   }

   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder("ChainingCacheLoader{");
      Iterator<CacheLoader> i = cacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> c = cacheLoaderConfigs.iterator();
      int count = 0;
      while (i.hasNext() && c.hasNext())
      {
         CacheLoader loader = i.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = c.next();

         buf.append(++count);
         buf.append(": IgnoreMods? ");
         buf.append(cfg.isIgnoreModifications());
         buf.append(" CLoader: ");
         buf.append(loader);
         buf.append("; ");
      }
      buf.append("}");
      return buf.toString();
   }

   public void purgeIfNecessary() throws Exception
   {
      Iterator<CacheLoader> loaders = cacheLoaders.iterator();
      Iterator<CacheLoaderConfig.IndividualCacheLoaderConfig> configs = cacheLoaderConfigs.iterator();

      while (loaders.hasNext() && configs.hasNext())
      {
         CacheLoader myLoader = loaders.next();
         CacheLoaderConfig.IndividualCacheLoaderConfig myConfig = configs.next();

         if (!myConfig.isIgnoreModifications() && myConfig.isPurgeOnStartup()) myLoader.remove(Fqn.ROOT);
      }


   }
}
