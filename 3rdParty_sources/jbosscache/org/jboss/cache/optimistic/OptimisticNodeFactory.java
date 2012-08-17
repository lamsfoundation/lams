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
package org.jboss.cache.optimistic;

import org.jboss.cache.AbstractNodeFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.UnversionedNode;
import org.jboss.cache.VersionedNode;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.lock.LockStrategyFactory;

import java.util.Map;

/**
 * Node factory specific to optimistic locking.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticNodeFactory<K, V> extends AbstractNodeFactory<K, V>
{
   private LockStrategyFactory lockStrategyFactory;

   @Inject
   private void injectLockStrategyFactory(LockStrategyFactory lockStrategyFactory)
   {
      this.lockStrategyFactory = lockStrategyFactory;
   }

   @Override
   protected UnversionedNode<K, V> createInternalNode(Object childName, Fqn fqn, NodeSPI<K, V> parent, Map<K, V> data)
   {
      VersionedNode<K, V> internal = new VersionedNode<K, V>(fqn, parent, data, cache);
      internal.injectDependencies(cache, commandsFactory, this);
      internal.injectLockStrategyFactory(lockStrategyFactory);
      return internal;
   }

   @Override
   public WorkspaceNode<K, V> createWrappedNode(NodeSPI<K, V> dataNode, TransactionWorkspace workspace)
   {
      return new WorkspaceNodeImpl<K, V>(dataNode, workspace, this);
   }
}
