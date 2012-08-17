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

import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.invocation.NodeInvocationDelegate;
import org.jboss.cache.mvcc.ReadCommittedNode;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;

import java.util.Map;

/**
 * Generates new nodes based on the {@link CacheSPI} configuration.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public abstract class AbstractNodeFactory<K, V> implements NodeFactory<K, V>
{
   protected CacheSPI<K, V> cache;
   protected boolean useVersionedNode;
   protected Configuration configuration;
   protected InvocationContextContainer invocationContextContainer;
   protected InterceptorChain interceptorChain;
   protected CommandsFactory commandsFactory;
   protected ComponentRegistry componentRegistry;
   protected DataContainer dataContainer;


   @Inject
   void injectComponentRegistry(ComponentRegistry componentRegistry, DataContainer dataContainer)
   {
      this.componentRegistry = componentRegistry;
      this.dataContainer = dataContainer;
   }

   @Inject
   public void injectDependencies(CacheSPI<K, V> cache, Configuration configuration,
                                  InvocationContextContainer invocationContextContainer,
                                  InterceptorChain interceptorChain, CommandsFactory commandsFactory)
   {
      this.cache = cache;
      this.configuration = configuration;
      this.invocationContextContainer = invocationContextContainer;
      this.interceptorChain = interceptorChain;
      this.commandsFactory = commandsFactory;
   }

   private NodeSPI<K, V> initializeNodeInvocationDelegate(UnversionedNode<K, V> internal)
   {
      // always assume that new nodes do not have data loaded
      internal.setDataLoaded(false);
      NodeSPI<K, V> nid = createNodeInvocationDelegate(internal, false);

      // back reference
      internal.setDelegate(nid);
      return nid;
   }

   public NodeSPI<K, V> createNode(Fqn fqn, NodeSPI<K, V> parent, Map<K, V> data)
   {
      UnversionedNode<K, V> internal = createInternalNode(fqn.getLastElement(), fqn, parent, data);
      return initializeNodeInvocationDelegate(internal);
   }

   public NodeSPI<K, V> createNode(Object childName, NodeSPI<K, V> parent, Map<K, V> data)
   {
      UnversionedNode<K, V> internal = createInternalNode(childName, Fqn.fromRelativeElements(parent.getFqn(), childName), parent, data);
      return initializeNodeInvocationDelegate(internal);
   }

   public NodeSPI<K, V> createNode(Fqn fqn, NodeSPI<K, V> parent)
   {
      UnversionedNode<K, V> internal = createInternalNode(fqn.getLastElement(), fqn, parent, null);
      return initializeNodeInvocationDelegate(internal);
   }

   public InternalNode<K, V> createInternalNode(Fqn fqn)
   {
      throw new UnsupportedOperationException("Unsupported in this implementation (" + getClass().getSimpleName() + ")!");
   }

   public NodeSPI<K, V> createNode(Object childName, NodeSPI<K, V> parent)
   {
      UnversionedNode<K, V> internal = createInternalNode(childName, Fqn.fromRelativeElements(parent.getFqn(), childName), parent, null);
      return initializeNodeInvocationDelegate(internal);
   }

   protected UnversionedNode<K, V> createInternalNode(Object childName, Fqn fqn, NodeSPI<K, V> parent, Map<K, V> data)
   {
      throw new UnsupportedOperationException("Unsupported in this implementation (" + getClass().getSimpleName() + ")!");
   }

   public WorkspaceNode<K, V> createWrappedNode(NodeSPI<K, V> dataNode, TransactionWorkspace workspace)
   {
      throw new UnsupportedOperationException("Unsupported in this implementation (" + getClass().getSimpleName() + ")!");
   }

   public ReadCommittedNode createWrappedNode(InternalNode<K, V> node, InternalNode<K, V> parent)
   {
      throw new UnsupportedOperationException("Unsupported in this implementation (" + getClass().getSimpleName() + ")!");
   }

   public ReadCommittedNode createWrappedNodeForRemoval(Fqn fqn, InternalNode<K, V> node, InternalNode<K, V> parent)
   {
      throw new UnsupportedOperationException("Unsupported in this implementation (" + getClass().getSimpleName() + ")!");
   }

   public NodeSPI<K, V> createRootNode()
   {
      return createNode(Fqn.ROOT, null);
   }

   private NodeSPI<K, V> createNodeInvocationDelegate(InternalNode<K, V> internalNode, boolean wrapWithNodeReference)
   {
      if (wrapWithNodeReference)
         throw new UnsupportedOperationException("wrapWithNodeReferences is not supported in this impl!");
      NodeInvocationDelegate<K, V> nid = new NodeInvocationDelegate<K, V>(internalNode);
      nid.initialize(configuration, invocationContextContainer, componentRegistry, interceptorChain);
      nid.injectDependencies(cache);
      return nid;
   }

   public InternalNode<K, V> createChildNode(Fqn fqn, InternalNode<K, V> parent, InvocationContext ctx, boolean attachToParent)
   {
      throw new UnsupportedOperationException("Unsupported in this implementation (" + getClass().getSimpleName() + ")!");
   }
}
