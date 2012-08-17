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
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;

import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

/**
 * Provides ignoreModifications features to all cache loaders.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.1.0
 */
public class ReadOnlyDelegatingCacheLoader extends AbstractDelegatingCacheLoader
{
   private static final Log log = LogFactory.getLog(ReadOnlyDelegatingCacheLoader.class);

   public ReadOnlyDelegatingCacheLoader(CacheLoader cl)
   {
      super(cl);
   }

   @Override
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
      Map map = get(name);
      return (map == null) ? null : map.get(key);
   }

   @Override
   public void put(Fqn name, Map attributes) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void put(List<Modification> modifications) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public Object remove(Fqn fqn, Object key) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
      Map map = get(fqn);
      return (map == null) ? null : map.get(key);
   }

   @Override
   public void remove(Fqn fqn) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void removeData(Fqn fqn) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void commit(Object tx) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void rollback(Object tx)
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      log.trace("Not delegating write operation to underlying cache loader");
   }
}
