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
package org.jboss.cache;

import org.apache.commons.logging.LogFactory;
import org.jboss.cache.lock.IdentityLock;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DefaultDataVersion;

import java.util.Map;

/**
 * VersionedNode extends the {@link org.jboss.cache.UnversionedNode} by adding a {@link org.jboss.cache.optimistic.DataVersion} property.
 * <p/>
 * Unlike {@link org.jboss.cache.UnversionedNode}, this node supports {@link #getVersion} and {@link #setVersion(org.jboss.cache.optimistic.DataVersion)}
 * defined in {@link org.jboss.cache.NodeSPI}
 * <p/>
 * Typically used when the cache mode configured is {@link org.jboss.cache.config.Configuration.NodeLockingScheme#OPTIMISTIC}
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @since 2.0.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class VersionedNode<K, V> extends PessimisticUnversionedNode<K, V>
{
   private static final String DATA_VERSION_INTERNAL_KEY = "_JBOSS_INTERNAL_OPTIMISTIC_DATA_VERSION";
   private DataVersion version; // make sure this is NOT initialized to anything, even a null!  Since the UnversionedNode constructor may set this value based on a data version passed along in the data map.

   static
   {
      log = LogFactory.getLog(VersionedNode.class);
   }

   /**
    * Although this object has a reference to the CacheImpl, the optimistic
    * node is actually disconnected from the CacheImpl itself.
    * The parent could be looked up from the TransactionWorkspace.
    */
   private NodeSPI<K, V> parent;

   public VersionedNode(Fqn fqn, NodeSPI<K, V> parent, Map<K, V> data, CacheSPI<K, V> cache)
   {
      super(fqn.getLastElement(), fqn, data, cache);
      if (parent == null && !fqn.isRoot()) throw new NullPointerException("parent");
      if (version == null) version = DefaultDataVersion.ZERO;
      this.parent = parent;
   }

   /**
    * Returns the version id of this node.
    *
    * @return the version
    */
   @Override
   public DataVersion getVersion()
   {
      return version;
   }

   /**
    * Returns the parent.
    */
   @Override
   public NodeSPI<K, V> getParent()
   {
      return parent;
   }

   /**
    * Sets the version id of this node.
    *
    * @param version
    */
   @Override
   public void setVersion(DataVersion version)
   {
      this.version = version;
   }

   /**
    * Optimistically locked nodes (VersionedNodes) will always use repeatable read.
    */
   @Override
   protected synchronized void initLock()
   {
      if (lock == null) lock = new IdentityLock(lockStrategyFactory, delegate);
   }

   @Override
   public Map getInternalState(boolean onlyInternalState)
   {
      Map state = super.getInternalState(onlyInternalState);
      state.put(DATA_VERSION_INTERNAL_KEY, version);
      return state;
   }

   @Override
   public void setInternalState(Map state)
   {
      if (state != null)
      {
         DataVersion dv = (DataVersion) state.remove(DATA_VERSION_INTERNAL_KEY);
         if (dv != null) version = dv;
      }
      super.setInternalState(state);
   }

   @Override
   public VersionedNode copy()
   {
      VersionedNode n = new VersionedNode(fqn, getParent(), data, cache);
      copyInternals(n);
      n.version = version;
      return n;
   }
}
