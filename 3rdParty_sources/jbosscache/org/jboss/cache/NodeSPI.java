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

import net.jcip.annotations.NotThreadSafe;
import org.jboss.cache.lock.NodeLock;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;
import java.util.Set;

/**
 * A more detailed interface to {@link Node}, which is used when writing plugins for or extending JBoss Cache.  References are usually
 * obtained by calling methods on {@link org.jboss.cache.CacheSPI}.
 * <p/>
 * <B><I>You should NEVER attempt to directly cast a {@link Node} instance to this interface.  In future, the implementation may not allow it.</I></B>
 * <p/>
 * This interface contains overridden method signatures of some methods from {@link Node}, overridden to ensure return
 * types of {@link Node} are replaced with {@link NodeSPI}.
 * <p/>
 * <b><i>An important note</i></b> on the xxxDirect() methods below.  These methods are counterparts to similarly named
 * methods in {@link Node} - e.g., {@link NodeSPI#getDirect(Object)} is a direct access counterpart to {@link Node#get(Object)},
 * the difference being that:
 * <p/>
 * <ul>
 * <li>{@link Node#get(Object)} - Passes the call up the interceptor stack, applies all aspects including node locking, cache loading, passivation, etc etc.</li>
 * <li>{@link NodeSPI#getDirect(Object)} - directly works on the underlying data in the node.</li>
 * </ul>
 * <p/>
 * The big difference with the direct access methods are that it is the onus of the caller to ensure proper locks are obtained
 * prior to the call.  A proper call should have gone through a locking-capable interceptor first and based on the cache
 * configuration's locking policy, an appropriate lock should be obtained prior to the call.  These direct access methods will
 * throw {@link org.jboss.cache.lock.LockingException}s if appropriate locks haven't been obtained by the caller.
 * <p/>
 * It is important to node that the direct <b>read</b> methods, such as getDataDirect(), return unmodifiable collections.
 * In addition to being unmodifiable, they are also defensively copied from the underlying data map to ensure view consistency.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see Node
 * @see org.jboss.cache.CacheSPI
 * @since 2.0.0
 */
@NotThreadSafe
public interface NodeSPI<K, V> extends Node<K, V>
{
   /**
    * Returns true if the children of this node were loaded from a cache loader.
    *
    * @return true if the children of this node were loaded from a cache loader.
    */
   boolean isChildrenLoaded();

   /**
    * Sets if the children of this node were loaded from a cache loader.
    *
    * @param loaded true if loaded, false otherwise
    */
   void setChildrenLoaded(boolean loaded);

   /**
    * Returns true if the data was loaded from the cache loader.
    *
    * @return true if the data was loaded from the cache loader.
    */
   boolean isDataLoaded();

   /**
    * Sets if the data was loaded from the cache loader.
    *
    * @param dataLoaded true if loaded, false otherwise
    */
   void setDataLoaded(boolean dataLoaded);

   /**
    * Returns a map to access the raw children.
    * This method may return a null if the node does not have any children.  It is important to note that this method
    * returns a direct reference to the underlying child map and is intended for internal use only.  Incorrect use
    * may result in very inconsistent state of the cache.
    *
    * @return Map, keyed by child name, values Nodes.
    */
   Map<Object, Node<K, V>> getChildrenMapDirect();

   /**
    * Sets the node's children explictly.
    * This method will remove all children currently associated with this node and add all the children passed in.
    *
    * @param children cannot be null
    */
   void setChildrenMapDirect(Map<Object, Node<K, V>> children);

   /**
    * Returns an existing child or creates a new one using a global transaction.
    *
    * @param name name of child to create
    * @param tx   transaction under which to create child
    * @return newly created node
    * @deprecated should use the {@link org.jboss.cache.NodeFactory} instead.
    */
   @Deprecated
   NodeSPI<K, V> getOrCreateChild(Object name, GlobalTransaction tx);

   /**
    * Returns a lock for this node.
    *
    * @return node lock
    * @deprecated this will be removed in 3.0.0.  Please use methods on the {@link org.jboss.cache.lock.LockManager} to lock and unlock nodes.
    */
   @Deprecated
   NodeLock getLock();

   /**
    * Sets the FQN of this node and resets the names of all children as well.
    *
    * @param f fqn to set
    */
   void setFqn(Fqn f);

   /**
    * Returns true if the instance has been deleted in the current transaction.
    *
    * @return true if the instance has been deleted in the current transaction.
    */
   boolean isDeleted();

   /**
    * Marks the node as being deleted (or not) in the current transaction.  This is not recursive, child nodes are not affected.
    *
    * @param marker true if the node has been deleted, false if not.
    */
   void markAsDeleted(boolean marker);

   /**
    * Same as {@link #markAsDeleted(boolean)} except that the option to recurse into children is provided.
    *
    * @param marker    true if the node has been deleted, false if not.
    * @param recursive if true, child nodes (and their children) are marked as well.
    */
   void markAsDeleted(boolean marker, boolean recursive);

   /**
    * Adds or replaces a child by name.
    *
    * @param nodeName  child node name (not an FQN)
    * @param nodeToAdd child node
    */
   void addChild(Object nodeName, Node<K, V> nodeToAdd);

   /**
    * Prints details of this node to the StringBuilder passed in.
    *
    * @param sb     StringBuilder to print to
    * @param indent depth of this node in the tree.  Used to indent details by prepending spaces.
    */
   void printDetails(StringBuilder sb, int indent);

   /**
    * Prints basic information of this node to the StringBuilder passed in.
    *
    * @param sb     StringBuilder to print to
    * @param indent depth of this node in the tree.  Used to indent details by prepending spaces.
    */
   @Deprecated
   void print(StringBuilder sb, int indent);

   // versioning
   /**
    * Sets the data version of this node if versioning is supported.
    *
    * @param version data version to apply
    * @throws UnsupportedOperationException if versioning is not supported
    */
   void setVersion(DataVersion version);

   /**
    * Returns the data version of this node if versioning is supported.
    *
    * @return data version
    * @throws UnsupportedOperationException if versioning is not supported
    */
   DataVersion getVersion();


   // ------- these XXXDirect() methods work directly on the node and bypass the interceptor chain.
   /**
    * Functionally the same as {@link #getChildren()} except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @return set of child nodes.
    * @see #getChildren()
    */
   Set<NodeSPI<K, V>> getChildrenDirect();

   /**
    * Directly removes all children for this node.
    * The only direct method that does not have a non-direct counterpart.
    */
   void removeChildrenDirect();

   /**
    * Retrieves children (directly), optionally including any marked as deleted nodes.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method.
    *
    * @param includeMarkedAsDeleted if true, the returned set will include nodes marked as deleted
    * @return a set of nodes
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    */
   Set<NodeSPI<K, V>> getChildrenDirect(boolean includeMarkedAsDeleted);

   /**
    * Retrives a child directly by name.
    * Functionally the same as {@link #getChild(Object)} except that it bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method.
    *
    * @param childName name of child
    * @return child node
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #getChild(Object)
    */
   NodeSPI<K, V> getChildDirect(Object childName);

   /**
    * Adds a child directly to a Node.
    * Functionally the same as {@link #addChild(Fqn)} except that it bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method.
    *
    * @param childName name of child
    * @return child node
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #addChild(Fqn)
    */
   NodeSPI<K, V> addChildDirect(Fqn childName);

   /**
    * Same as {@link #addChildDirect(Fqn)} except that it optionally allows you to suppress notification events for
    * the creation of this node.
    *
    * @param f      name of child
    * @param notify if true, notification events are sent; if false, they are not
    * @return child node
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #addChild(Fqn)
    */
   NodeSPI<K, V> addChildDirect(Fqn f, boolean notify);

   /**
    * Same as {@link #addChildDirect(Fqn, boolean)}  except that it just takes a child name
    *
    * @param childName name of child
    * @param notify    if true, notification events are sent; if false, they are not
    * @return child node
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #addChild(Fqn)
    */
   NodeSPI<K, V> addChildDirect(Object childName, boolean notify);

   /**
    * Directly adds the node passed in to the children map of the current node.  Will throw a CacheException if
    * <tt>child.getFqn().getParent().equals(getFqn())</tt> returns false.
    *
    * @param child child to add
    */
   void addChildDirect(NodeSPI<K, V> child);

   /**
    * Retrives a child directly by fully qualified name.
    * Functionally the same as {@link #getChild(Fqn)} except that it bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method.
    *
    * @param childName name of child
    * @return child node
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #getChild(Fqn)
    */
   NodeSPI<K, V> getChildDirect(Fqn childName);

   /**
    * Removes a child directly from a node.
    * Functionally the same as {@link #removeChild(Fqn)} except that it bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    *
    * @param fqn of child.
    * @return true if the node was found, false otherwise
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #removeChild(Fqn)
    */
   boolean removeChildDirect(Fqn fqn);

   /**
    * Removes a child directly from a node.
    * Functionally the same as {@link #removeChild(Object)} except that bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method.
    *
    * @param childName of child.
    * @return true if the node was found, false otherwise
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #removeChild(Object)
    */
   boolean removeChildDirect(Object childName);


   /**
    * Removes a data key directly from a node.
    * Functionally the same as {@link #remove(Object)} except that it bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method.
    *
    * @param key to remove
    * @return the old data contained under the key
    * @throws org.jboss.cache.lock.LockingException
    *          if locking was not obtained
    * @see #remove(Object)
    */
   V removeDirect(K key);

   /**
    * Functionally the same as {@link #put(Object,Object)} except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @param key   of data
    * @param value of data
    * @return the previous value under the key passed in, or <tt>null</tt>
    * @see #put(Object,Object)
    */
   V putDirect(K key, V value);

   /**
    * Functionally the same as {@link #putAll(Map)} except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @param data to put
    * @see #putAll(Map)
    */
   void putAllDirect(Map<? extends K, ? extends V> data);

   /**
    * Functionally the same as {@link #getData()}  except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * Note that this returns a reference to access the node's data.
    * This data should only be modified by the cache itself.
    * This method should never return null.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @return map containing data
    * @see #getData()
    */
   Map<K, V> getDataDirect();

   /**
    * Functionally the same as {@link #get(Object)}   except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @param key data to get
    * @return value under key
    * @see #get(Object)
    */
   V getDirect(K key);

   /**
    * Returns true if a mapping exists for this key. Returns false if no
    * mapping exists.
    *
    * @param key The key checked for inclusion in the node data.
    * @return true if a mapping exists for the key, false if not.
    */
   boolean containsKeyDirect(K key);


   /**
    * Functionally the same as {@link #clearData()}  except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @see #clearData()
    */
   void clearDataDirect();


   /**
    * Functionally the same as {@link #getKeys()} except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @return set of keys
    * @see #getKeys()
    */
   Set<K> getKeysDirect();

   /**
    * Functionally the same as {@link #getChildrenNames()}  except that it operates directly on the node and bypasses the
    * interceptor chain.
    * <p/>
    * The caller needs to ensure a proper lock has been obtained prior to calling this method, otherwise a
    * {@link org.jboss.cache.lock.LockingException} will be thrown.
    * <p/>
    *
    * @return set of children names
    * @see #getChildrenNames()
    */
   Set<Object> getChildrenNamesDirect();

   /**
    * Retrieves a reference to the cache in which this Node resides.
    *
    * @return a cache
    */
   CacheSPI<K, V> getCache();

   // ----------- these methods override their corresponding methods in Node, so that the return types are NodeSPI rather than Node.

   /**
    * Returns the parent node as a {@link NodeSPI}, instead
    * of {@link Node} from {@link Node#getParent()}, and is otherwise identical.
    *
    * @return parent node
    * @see Node#getParent()
    */
   NodeSPI<K, V> getParentDirect();

   /**
    * @return true if the node has one or more child nodes; false otherwise.
    */
   boolean hasChildrenDirect();

   /**
    * Very similar to {@link #getDataDirect()}, except that this method may also encode some internal data as attributes in the map,
    * using special <tt>_JBOSS_INTERNAL_XXX</tt> Strings as keys.  Designed to be used by {@link org.jboss.cache.statetransfer.StateTransferGenerator}
    * and {@link org.jboss.cache.interceptors.CacheStoreInterceptor} which attempt to serialize nodes into a stream for storage or transfer.
    *
    * @param onlyInternalState if true, the map will only contain internal state and no other data.
    * @return a map containing data as well as additional information about this node.
    * @since 2.1.0
    */
   Map getInternalState(boolean onlyInternalState);

   /**
    * Very similar to {@link #putAllDirect(java.util.Map)} except that this method first scans the map for any internal attributes
    * using special <tt>_JBOSS_INTERNAL_XXX</tt> Strings as keys, and uses these to set internal attributes before passing
    * the remaining attributes to {@link #putAllDirect(java.util.Map)}.  Designed to be used by {@link org.jboss.cache.statetransfer.StateTransferIntegrator}
    * and {@link org.jboss.cache.interceptors.CacheLoaderInterceptor} classes which attempt to create nodes based on a data stream.
    *
    * @param state state to be applied
    * @since 2.1.0
    */
   void setInternalState(Map state);

   /**
    * Sets the validity of a node.  By default, all nodes are valid unless they are deleted, invalidated or moved, either
    * locally or remotely.  To be used in conjunction with {@link #isValid()}.
    *
    * @param valid     if true, the node is marked as valid; if false, the node is invalid.
    * @param recursive if true, the validity flag passed in is applied to all children as well.
    * @since 2.1.0
    */
   void setValid(boolean valid, boolean recursive);

   /**
    * @return true if the node is a null marker node.  Only used with MVCC.
    */
   boolean isNullNode();

   /**
    * Marks a node for updating.  Internally, this creates a copy of the delegate and performs any checks necessary to
    * maintain isolation level.
    * <p/>
    * Only used with MVCC.
    *
    * @param container      data container
    * @param writeSkewCheck if true, and the node supports write skew checking, nodes are tested for write skews.
    */
   void markForUpdate(DataContainer container, boolean writeSkewCheck);

   /**
    * Commits any updates made on this node to the underlying data structure, making it visible to all other transactions.
    * <p/>
    * Only used with MVCC.
    *
    * @param ctx       invocation context
    * @param container data container
    */
   void commitUpdate(InvocationContext ctx, DataContainer container);

   /**
    * Only used with MVCC.
    * <p/>
    *
    * @return true if this node has been marked for update, false otherwise.
    */
   boolean isChanged();

   /**
    * Only used with MVCC.
    * <p/>
    *
    * @return true if this node has been newly created in the current scope.
    */
   boolean isCreated();

   InternalNode getDelegationTarget();

   /**
    * Sets the created flag on a node.
    * <p/>
    * Only used with MVCC.
    *
    * @param created flag
    */
   void setCreated(boolean created);

   /**
    * Rolls back any changes made to a node.
    * <p/>
    * Only used with MVCC.
    */
   void rollbackUpdate();
}
