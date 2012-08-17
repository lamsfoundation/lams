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

import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.RegionManager;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AbstractDelegatingCacheLoader provides standard functionality for a cache loader that simply delegates each
 * operation defined in the cache loader interface to the underlying cache loader, basically acting as a proxy to the
 * real cache loader.
 * <p/>
 * Any cache loader implementation that extends this class would be required to override any of the methods in
 * order to provide a different or added behaviour.
 *
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public abstract class AbstractDelegatingCacheLoader extends AbstractCacheLoader
{
   private CacheLoader cacheLoader;

   public AbstractDelegatingCacheLoader(CacheLoader cl)
   {
      cacheLoader = cl;
   }

   public CacheLoader getCacheLoader()
   {
      return cacheLoader;
   }

   public void setCacheLoader(CacheLoader cacheLoader)
   {
      this.cacheLoader = cacheLoader;
   }

   public void setConfig(IndividualCacheLoaderConfig config)
   {
      cacheLoader.setConfig(config);
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return cacheLoader.getConfig();
   }

   @Override
   public void setCache(CacheSPI c)
   {
      super.setCache(c);
      cacheLoader.setCache(c);
   }

   public Set getChildrenNames(Fqn fqn) throws Exception
   {
      return cacheLoader.getChildrenNames(fqn);
   }

   public Map get(Fqn name) throws Exception
   {
      return cacheLoader.get(name);
   }

   public boolean exists(Fqn name) throws Exception
   {
      return cacheLoader.exists(name);
   }

   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      return cacheLoader.put(name, key, value);
   }

   public void put(Fqn name, Map attributes) throws Exception
   {
      cacheLoader.put(name, attributes);
   }

   @Override
   public void put(List<Modification> modifications) throws Exception
   {
      cacheLoader.put(modifications);
   }

   public Object remove(Fqn fqn, Object key) throws Exception
   {
      return cacheLoader.remove(fqn, key);
   }

   public void remove(Fqn fqn) throws Exception
   {
      cacheLoader.remove(fqn);
   }

   public void removeData(Fqn fqn) throws Exception
   {
      cacheLoader.removeData(fqn);
   }

   @Override
   public void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception
   {
      cacheLoader.prepare(tx, modifications, one_phase);
   }

   @Override
   public void commit(Object tx) throws Exception
   {
      cacheLoader.commit(tx);
   }

   @Override
   public void rollback(Object tx)
   {
      cacheLoader.rollback(tx);
   }

   @Override
   public void loadEntireState(ObjectOutputStream os) throws Exception
   {
      cacheLoader.loadEntireState(os);
   }

   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      cacheLoader.storeEntireState(is);
   }

   @Override
   public void loadState(Fqn subtree, ObjectOutputStream os) throws Exception
   {
      cacheLoader.loadState(subtree, os);
   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      cacheLoader.storeState(subtree, is);
   }

   @Override
   public void setRegionManager(RegionManager manager)
   {
      cacheLoader.setRegionManager(manager);
   }

   @Override
   public void create() throws Exception
   {
      cacheLoader.create();
   }

   @Override
   public void start() throws Exception
   {
      cacheLoader.start();
   }

   @Override
   public void stop()
   {
      cacheLoader.stop();
   }

   @Override
   public void destroy()
   {
      cacheLoader.destroy();
   }
}
