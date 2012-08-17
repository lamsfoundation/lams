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

import org.jboss.cache.marshall.NodeData;

import java.util.List;
import java.util.Set;

/**
 * This class defines the functionality needed for node manipulation.
 *
 * @author Mircea.Markus@jboss.com
 * @see DataContainerImpl
 * @since 2.2
 */
public interface DataContainer
{
   /**
    * Retrieves the root node.
    *
    * @return the root node
    * @deprecated use Cache.getRoot();
    */
   @Deprecated
   NodeSPI getRoot();

   /**
    * Adds the specified Fqn to the list of Fqns to be considered "internal".
    *
    * @param fqn fqn to add to list
    */
   void registerInternalFqn(Fqn fqn);

   /**
    * Finds a node given a fully qualified name, directly off the interceptor chain.  In the event of an exception,
    * returns null.  Does not include invalid or deleted nodes.
    *
    * @param fqn Fully qualified name for the corresponding node.
    * @return Node referenced by the given Fqn, or null if the node cannot be found or if there is an exception.
    * @deprecated Note that this only supports legacy locking schemes (OL and PL) and will be removed when those schemes are removed.
    */
   @Deprecated
   NodeSPI peek(Fqn fqn);

   /**
    * Same as calling <tt>peek(fqn, includeDeletedNodes, false)</tt>.
    *
    * @param fqn                 Fqn to find
    * @param includeDeletedNodes if true, deleted nodes are considered
    * @return the node, if found, or null otherwise.
    * @deprecated Note that this only supports legacy locking schemes (OL and PL) and will be removed when those schemes are removed.
    */
   @Deprecated
   NodeSPI peek(Fqn fqn, boolean includeDeletedNodes);

   /**
    * Peeks for a specified node.  This involves a direct walk of the tree, starting at the root, until the required node
    * is found.  If the node is not found, a null is returned.
    *
    * @param fqn                 Fqn of the node to find
    * @param includeDeletedNodes if true, deleted nodes are also considered
    * @param includeInvalidNodes if true, invalid nodes are also considered
    * @return the node, if found, or null otherwise.
    * @deprecated Note that this only supports legacy locking schemes (OL and PL) and will be removed when those schemes are removed.
    */
   @Deprecated
   NodeSPI peek(Fqn fqn, boolean includeDeletedNodes, boolean includeInvalidNodes);

   /**
    * Tests if an Fqn exists and is valid and not deleted.
    *
    * @param fqn the fqn representing the node to test
    * @return true if the node exists, false otherwise.
    */
   boolean exists(Fqn fqn);

   /**
    * Returns true if the Fqn exists, is valid and is not deleted, and the node has children.
    *
    * @param fqn the fqn to test
    * @return true if the Fqn exists, is valid and is not deleted, and the node has children.
    */
   boolean hasChildren(Fqn fqn);

   /**
    * Prepares a list of {@link NodeData} objects for a specified node and all its children.
    *
    * @param list    List of NodeData objects, which will be added to.
    * @param node    node to recursively add to the list
    * @param mapSafe if true, the node's data map reference is passed to the NodeData instance created.  Otherwise, the map is copied.
    * @return the same list passed in
    */
   List<NodeData> buildNodeData(List<NodeData> list, NodeSPI node, boolean mapSafe);

   /**
    * Generates a list of nodes for eviction.  This filters out nodes that cannot be evicted, such as those which are
    * marked as resident.  See {@link NodeSPI#setResident(boolean)}.
    *
    * @param fqn       the node to consider for eviction
    * @param recursive if recursive, child nodes are also considered
    * @return a list of Fqns that can be considered for eviction
    */
   List<Fqn> getNodesForEviction(Fqn fqn, boolean recursive);

   /**
    * Returns a Set<Fqn> of Fqns of the topmost node of internal regions that
    * should not included in standard state transfers. Will include
    * {@link org.jboss.cache.buddyreplication.BuddyManager#BUDDY_BACKUP_SUBTREE} if buddy replication is
    * enabled.
    *
    * @return an unmodifiable Set<Fqn>.  Will not return <code>null</code>.
    */
   Set<Fqn> getInternalFqns();

   /**
    * Returns the number of read or write locks held across the entire cache.
    */
   int getNumberOfLocksHeld();

   /**
    * Returns an <em>approximation</em> of the total number of nodes in the
    * cache. Since this method doesn't acquire any locks, the number might be
    * incorrect, or the method might even throw a
    * ConcurrentModificationException
    */
   int getNumberOfNodes();

   /**
    * Returns an <em>approximation</em> of the total number of attributes in
    * this sub cache.
    */
   int getNumberOfAttributes(Fqn fqn);

   /**
    * Returns an <em>approximation</em> of the total number of attributes in
    * the cache. Since this method doesn't acquire any locks, the number might
    * be incorrect, or the method might even throw a
    * ConcurrentModificationException
    *
    * @return number of attribs
    */
   int getNumberOfAttributes();

   /**
    * Removes the actual node from the tree data structure.
    *
    * @param f               the Fqn of the node to remove
    * @param skipMarkerCheck if true, skips checking the boolean {@link org.jboss.cache.NodeSPI#isDeleted()} flag and deletes the node anyway.
    * @return Returns true if the node was found and removed, false if not.
    */
   boolean removeFromDataStructure(Fqn f, boolean skipMarkerCheck);

   /**
    * Evicts the given node. If recursive is set to true then all child nodes are recusively evicted.
    */
   void evict(Fqn fqn, boolean recursive); // TODO: See if this is still needed here

   /**
    * <pre>
    * Following scenarios define how eviction works.
    * 1. If the given node is a leaf then it is entirely removed from the data structure. The node is marked as invalid.</li>
    * 2. If the given node is a leaf then only the data map is cleared.
    * 3. If the given node is the root node then the cildren nodes are evicted. For each child node 1. or 2. applies
    * </pre>
    *
    * @return true if the FQN is leaf and was removed; false if is an intermediate FQN and only contained data
    *         is droped.
    */
   boolean evict(Fqn fqn); // TODO: See if this is still needed here

   /**
    * Traverses the tree to the given Fqn, creating nodes if needed.  Returns a list of nodes created, as well as a reference to the last node.
    * <p/>
    * E.g.,
    * <code>
    * Object[] results = createNode(myFqn);
    * results[0] // this is a List&lt;NodeSPI&gt; of nodes <i>created</i> in getting to the target node.
    * results[1] // is a NodeSPI reference to the target node, regardless of whether it was <i>created</i> or just <i>found</i>.
    * </code>
    *
    * @param fqn fqn to find
    * @return see above.
    */
   Object[] createNodes(Fqn fqn);

   /**
    * Similar to {@link #peek(Fqn)} except that the underlying node is NOT wrapped as a {@link org.jboss.cache.NodeSPI}.
    *
    * @param f                   fqn to peek
    * @param includeInvalidNodes if true, invalid nodes will be considered as well.
    * @return internal node
    */
   InternalNode peekInternalNode(Fqn f, boolean includeInvalidNodes);

   /**
    * Similar to {@link #peekInternalNode(Fqn, boolean)} except that the node AND its *direct* parent are retrieved.
    *
    * @param fqn                 fqn to find
    * @param includeInvalidNodes if true, invalid nodes are considered.
    * @return an array of InternalNodes, containing 2 elements.  Element [0] is the node being peeked, and element [1] is its direct parent.
    */
   public InternalNode[] peekInternalNodeAndDirectParent(Fqn fqn, boolean includeInvalidNodes);

   /**
    * Sets a new root node
    *
    * @param nodeInvocationDelegate
    */
   // TODO: 3.0.0: FIx this so that it can take in a NodeSPI for OL/PL and an InternalNode for MVCC
   void setRoot(Object nodeInvocationDelegate);

   /**
    * @param fqn fqn to check
    * @return true if the node exists and is marked as resident, false otherwise.
    */
   boolean isResident(Fqn fqn);

   /**
    * @return a string representation of the contents of the data container
    */
   String printDetails();
}
