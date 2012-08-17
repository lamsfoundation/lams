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
package org.jboss.cache.mvcc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.AbstractNodeFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.UnversionedNode;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.lock.IsolationLevel;

import java.util.Map;

/**
 * Creates nodes specific to MVCC logic.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class MVCCNodeFactory<K, V> extends AbstractNodeFactory<K, V>
{
   private boolean useRepeatableRead;
   private static final Log log = LogFactory.getLog(MVCCNodeFactory.class);
   private static final boolean trace = log.isTraceEnabled();
   private boolean lockChildForInsertRemove;

   /**
    * Initialises the node factory with the configuration from the cache.
    */
   @Start
   public void init()
   {
      useRepeatableRead = configuration.getIsolationLevel() == IsolationLevel.REPEATABLE_READ;
      lockChildForInsertRemove = configuration.isLockParentForChildInsertRemove();
   }

   /**
    * Creates an MVCC wrapped node - either a {@link org.jboss.cache.mvcc.ReadCommittedNode} or its subclass, a
    * {@link org.jboss.cache.mvcc.RepeatableReadNode} based on cache configuration.  If a null is passed in as the InternalNode,
    * this method will return a special {@link org.jboss.cache.mvcc.NullMarkerNode} instance if using repeatable read,
    * or a null if read committed.
    *
    * @param node internal node to wrap.
    * @return a ReadCommittedNode
    */
   @Override
   public ReadCommittedNode createWrappedNode(InternalNode<K, V> node, InternalNode<K, V> parent)
   {
      return createWrappedNode(null, node, parent, false);
   }

   @Override
   public ReadCommittedNode createWrappedNodeForRemoval(Fqn fqn, InternalNode<K, V> node, InternalNode<K, V> parent)
   {
      return createWrappedNode(fqn, node, parent, true);
   }   

   @SuppressWarnings("unchecked")
   private ReadCommittedNode createWrappedNode(Fqn fqn, InternalNode<K, V> node, InternalNode<K, V> parent, boolean forRemoval)
   {
      ReadCommittedNode rcn;

      if (node == null)
      {
         if (useRepeatableRead)
         {
            if (forRemoval)
            {
               // create but do not return this just yet as it needs to be initialized
               rcn = new NullMarkerNodeForRemoval(parent, fqn);
            }
            else
            {
               return NullMarkerNode.getInstance();
            }
         }
         else
         {
            // if we are using read-committed, just return a null
            return null;
         }
      }
      else
      {
         rcn = useRepeatableRead ? new RepeatableReadNode(node, parent) : new ReadCommittedNode(node, parent);
      }

      rcn.initialize(configuration, invocationContextContainer, componentRegistry, interceptorChain);
      rcn.injectDependencies(cache);
      return rcn;
   }

   @Override
   public NodeSPI<K, V> createNode(Fqn fqn, NodeSPI<K, V> parent, Map<K, V> data)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public NodeSPI<K, V> createNode(Fqn fqn, NodeSPI<K, V> parent)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   @SuppressWarnings("unchecked")
   public NodeSPI<K, V> createRootNode()
   {
      return createWrappedNode(createInternalNode(Fqn.ROOT), null);
   }

   @Override
   public InternalNode<K, V> createInternalNode(Fqn fqn)
   {
      UnversionedNode<K, V> un = new UnversionedNode<K, V>(fqn, cache, lockChildForInsertRemove);

      // always assume that new nodes don't have their data loaded, unless root.
      if (!fqn.isRoot()) un.setDataLoaded(false);

      return useRepeatableRead ? un : new NodeReference<K, V>(un);
   }

   @Override
   public NodeSPI<K, V> createNode(Object childName, NodeSPI<K, V> parent, Map<K, V> data)
   {
      return createNode(Fqn.fromRelativeElements(parent.getFqn(), childName), parent, data);
   }

   @Override
   public NodeSPI<K, V> createNode(Object childName, NodeSPI<K, V> parent)
   {
      return createNode(Fqn.fromRelativeElements(parent.getFqn(), childName), parent);
   }

   @Override
   @SuppressWarnings("unchecked")
   public InternalNode<K, V> createChildNode(Fqn fqn, InternalNode<K, V> parent, InvocationContext ctx, boolean attachToParent)
   {
      InternalNode<K, V> child;
      if (fqn == null) throw new IllegalArgumentException("null child fqn");

      child = dataContainer.peekInternalNode(fqn, false);

      if (child == null)
      {
         cache.getNotifier().notifyNodeCreated(fqn, true, ctx);
         InternalNode<K, V> newChild = createInternalNode(fqn);
         if (attachToParent) parent.addChild(newChild);
         // addChild actually succeeded!
         child = newChild;

         if (trace) log.trace("Created new child with fqn [" + fqn + "]");

         // notify if we actually created a new child
         cache.getNotifier().notifyNodeCreated(fqn, false, ctx);
      }
      return child;
   }
}
