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

import org.jboss.cache.mvcc.ReadCommittedNode;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;

import java.util.Map;

/**
 * An interface for a factory that creates nodes.  This used to be a concrete class prior to 3.0.0.  Made into an
 * interface to simplify logic of different locking schemes and node types.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public interface NodeFactory<K, V>
{
   ReadCommittedNode createWrappedNode(InternalNode<K, V> node, InternalNode<K, V> parent);

   ReadCommittedNode createWrappedNodeForRemoval(Fqn fqn, InternalNode<K, V> node, InternalNode<K, V> parent);

   WorkspaceNode<K, V> createWrappedNode(NodeSPI<K, V> dataNode, TransactionWorkspace workspace);

   /**
    * Creates a new node and populates its attributes.
    * <p/>
    * Note that the data map passed in must not be null, and must not be referenced anywhere else as a defensive copy
    * is NOT made when injecting it into the node.
    *
    * @param fqn
    * @param parent
    * @param data
    * @return a new node
    */
   NodeSPI<K, V> createNode(Fqn fqn, NodeSPI<K, V> parent, Map<K, V> data);

   /**
    * Creates a new node and populates its attributes.
    * <p/>
    * Note that the data map passed in must not be null, and must not be referenced anywhere else as a defensive copy
    * is NOT made when injecting it into the node.
    *
    * @param childName
    * @param parent
    * @param data
    * @return a new node
    */
   NodeSPI<K, V> createNode(Object childName, NodeSPI<K, V> parent, Map<K, V> data);

   /**
    * Creates a new, empty node.
    *
    * @param fqn
    * @param parent
    * @return a new node
    */
   NodeSPI<K, V> createNode(Fqn fqn, NodeSPI<K, V> parent);

   /**
    * Creates a new, empty node.
    *
    * @param childName
    * @param parent
    * @return a new node
    */
   NodeSPI<K, V> createNode(Object childName, NodeSPI<K, V> parent);

   /**
    * Creates a new node, and optionally attaches the node to its parent.
    * <p/>
    * The assumption here is that any locks are acquired to prevent concurrent creation of the same node.  Implementations
    * of the NodeFactory should not attempt to synchronize or guard against concurrent creation.
    * <p/>
    *
    * @param fqn            fqn of node to create.  Must not be null or root.
    * @param parent         parent to attach to.  Must not be null, even if attachToParent is false.
    * @param ctx            invocation context to register with.  Must not be null.
    * @param attachToParent if true, the node is registered in the parent's child map.  If false, it is not.
    * @return a new node, or the existing node if one existed.
    */
   InternalNode<K, V> createChildNode(Fqn fqn, InternalNode<K, V> parent, InvocationContext ctx, boolean attachToParent);

   NodeSPI<K, V> createRootNode();

   /**
    * Creates an internal node.  Similar to {@link #createNode(Fqn, NodeSPI)} except that the resultant node is not wrapped
    * in a {@link org.jboss.cache.invocation.NodeInvocationDelegate}.
    *
    * @param childFqn
    * @return a new node
    */
   InternalNode<K, V> createInternalNode(Fqn childFqn);
}
