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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;
import net.jcip.annotations.ThreadSafe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

/**
 * Optimized version of the {@link JdbmCacheLoader} implementation which is better
 * for storing data under a single node. Speed is improved.
 * <p/>
 * Data is stored using an FQN to Map association, where the depth is prefixed to the FQN:
 * <pre>
 * 0/ = NodeData
 * 1/node1 = Node Data
 * 1/node2 = Node Data
 * 2/node2/node3 = Node Data
 * 2/node2/node4 = Node Data
 * </pre>
 * <p/>
 * Browse operations lock the entire tree; eventually the JDBM team plans to fix this.
 *
 * @author Elias Ross
 *
 */
@ThreadSafe
public class JdbmCacheLoader2 extends JdbmCacheLoader
{
   private static final Log log = LogFactory.getLog(JdbmCacheLoader2.class);
   private static final boolean trace = log.isTraceEnabled();

   /**
    * Max number of dummy parent nodes to cache.
    */
   private static final int PARENT_CACHE_SIZE = 100;

   /**
    * Empty parent nodes whose existence is cached.
    * Empty parents are required to ensure {@link #getChildrenNames(Fqn)}
    * and recursive {@link #remove(Fqn)} work correctly.
    */
   private Set<Fqn> parents = Collections.synchronizedSet(new HashSet<Fqn>());

   @Override
   JdbmCacheLoader2Config createConfig(IndividualCacheLoaderConfig base)
   {
      return new JdbmCacheLoader2Config(base);
   }

   /**
    * Adds a depth number to the start of the FQN.
    */
   private Fqn withDepth(Fqn name) {
       return withDepth(name, name.size());
   }
   
   /**
    * Adds a depth number to the start of the FQN.
    */
   private Fqn withDepth(Fqn name, int depth) {
       Fqn n = Fqn.fromElements(depth);
       return Fqn.fromRelativeList(n, name.peekElements());
   }

   @Override
   Set<Object> getChildrenNames0(Fqn name) throws IOException
   {
      Fqn name2 = withDepth(name, name.size() + 1);
      TupleBrowser browser = tree.browse(name2);
      Tuple t = new Tuple();

      Set<Object> set = new HashSet<Object>();

      while (browser.getNext(t))
      {
         Fqn fqn = (Fqn) t.getKey();
         if (!fqn.isChildOf(name2))
         {
            break;
         }
         set.add(fqn.getLastElement());
      }

      if (set.isEmpty())
      {
         return null;
      }

      return Collections.unmodifiableSet(set);
   }

   @Override
   public Map get(Fqn name)
         throws Exception
   {
      checkOpen();
      checkNonNull(name, "name");

      return (Map) tree.find(withDepth(name));
   }

   @Override
   public boolean exists(Fqn name) throws IOException
   {
      return tree.find(withDepth(name)) != null;
   }

   @Override
   Object put0(Fqn name, Object key, Object value) throws Exception
   {
      checkNonNull(name, "name");
      ensureParent(name);
      Fqn dname = withDepth(name);
      Map map = (Map) tree.find(dname);
      Object oldValue = null;
      if (map != null) {
	      oldValue = map.put(key, value);
	      tree.insert(dname, map, true);
      } else {
          map = new HashMap();
          map.put(key, value);
	      tree.insert(dname, map, false);
      }
      return oldValue;
   }

   @Override
   void put0(Fqn name, Map<?, ?> values) throws IOException
   {
      if (trace)
      {
         log.trace("put " + name + " values=" + values);
      }
      ensureParent(name);
      Fqn dname = withDepth(name);
      if (values == null)
          values = emptyMap();
      else
         values = new HashMap(values);
      tree.insert(dname, values, true);
   }
   
   private Map emptyMap() {
       return new HashMap(0);
   }

   /**
    * Ensures a parent node exists.
    * Calls recursively to initialize parents as necessary.
    */
   private void ensureParent(Fqn name) throws IOException
   {
      if (name.size() <= 1)
         return;
      Fqn parent = name.getParent();
      if (parents.contains(parent))
         return;
      if (!exists(parent))
         put0(parent, emptyMap());
      parents.add(parent);
      if (parents.size() > PARENT_CACHE_SIZE)
      {
         parents.clear();
      }
   }

   @Override
   void erase0(Fqn name, boolean prune)
         throws IOException
   {
      if (trace)
      {
         log.trace("erase " + name + " prune=" + prune);
      }
      if (!prune) {
         put0(name, emptyMap());
         return;
      }
      Set children = getChildrenNames(name);
      if (children != null)
      {
         log.trace("remove children: " + children);
         for (Object child : children)
         {
            erase0(Fqn.fromRelativeElements(name, child), true);
         }
      }
      parents.remove(name);
      try
      {
	      tree.remove(withDepth(name));
      }
      catch (IllegalArgumentException e)
      {
         log.trace("remove non-existant key? " + e);
      }
   }
   
   @Override
   Object eraseKey0(Fqn name, Object key)
         throws IOException
   {
      Fqn dname = withDepth(name);
      Map map = (Map) tree.find(dname);
      Object oldValue = null;
      if (map != null) {
	      oldValue = map.remove(key);
	      tree.insert(dname, map, true);
      }
      return oldValue;
   }

}
