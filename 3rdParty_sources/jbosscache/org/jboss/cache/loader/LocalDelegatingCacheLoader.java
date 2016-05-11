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
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * DelegatingCacheLoader implementation which delegates to a local (in the same VM) CacheImpl. Sample code:
 * <pre>
 * CacheImpl firstLevel=new CacheImpl();
 * CacheImpl secondLevel=new CacheImpl();
 * DelegatingCacheLoader l=new DelegatingCacheLoader(secondLevel);
 * l.setCache(firstLevel);
 * firstLevel.setCacheLoader(l);
 * secondLevel.start();
 * firstLevel.start();
 * </pre>
 *
 * @author Bela Ban
 * @author Daniel Gredler
 *
 */
public class LocalDelegatingCacheLoader extends AbstractCacheLoader
{

   IndividualCacheLoaderConfig config;
   CacheSPI delegate = null;

   public void setConfig(IndividualCacheLoaderConfig config)
   {
      this.config = config;
      if (config instanceof LocalDelegatingCacheLoaderConfig)
      {
         delegate = (CacheSPI) ((LocalDelegatingCacheLoaderConfig) config).getDelegate();
      }
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }

   public Set<?> getChildrenNames(Fqn fqn) throws Exception
   {
      Node node = delegate.getRoot().getChild(fqn);
      if (node == null) return null;

      Set cn = node.getChildrenNames();

      // the cache loader contract is a bit different from the cache when it comes to dealing with childrenNames
      if (cn.isEmpty()) return null;
      return cn;
   }

   public Map<Object, Object> get(Fqn name) throws Exception
   {
      NodeSPI n = (NodeSPI) delegate.getRoot().getChild(name);
      if (n == null) return null;
      // after this stage we know that the node exists.  So never return a null - at worst, an empty map.
      Map<Object, Object> m = n.getData();
      if (m == null) m = Collections.emptyMap();
      return m;
   }

   public boolean exists(Fqn name) throws Exception
   {
      return delegate.getNode(name) != null;
   }

   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      return delegate.put(name, key, value);
   }

   public void put(Fqn name, Map<Object, Object> attributes) throws Exception
   {
      delegate.put(name, attributes);
   }

   public Object remove(Fqn fqn, Object key) throws Exception
   {
      return delegate.remove(fqn, key);
   }

   public void remove(Fqn fqn) throws Exception
   {
      delegate.removeNode(fqn);
   }

   public void removeData(Fqn fqn) throws Exception
   {
      Node node = delegate.getRoot().getChild(fqn);
      if (node != null) node.clearData();
   }

   protected void setDelegateCache(CacheSPI delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public void loadEntireState(ObjectOutputStream os) throws Exception
   {
      try
      {
         //         // We use the lock acquisition timeout rather than the
         //         // state transfer timeout, otherwise we'd never try
         //         // to break locks before the requesting node gives up
         //         return cache._getState(Fqn.fromString(SEPARATOR),
         //            cache.getLockAcquisitionTimeout(),
         //            true,
         //            false);
         // Until flush is in place, use the old mechanism
         // where we wait the full state retrieval timeout
         delegate.getStateTransferManager().getState(os, Fqn.ROOT, delegate.getConfiguration().getStateRetrievalTimeout(), true, false);
      }
      catch (Exception e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         throw new RuntimeException("Caught exception getting state from delegate", t);
      }
   }

   @Override
   public void loadState(Fqn subtree, ObjectOutputStream os) throws Exception
   {
      throw new UnsupportedOperationException("setting and loading state for specific Fqns not supported");
   }

   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      delegate.getStateTransferManager().setState(is, Fqn.ROOT);

   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      throw new UnsupportedOperationException("setting and loading state for specific Fqns not supported");
   }

}
