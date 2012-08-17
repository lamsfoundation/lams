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

import net.jcip.annotations.ThreadSafe;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jgroups.Address;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for a Cache where data mappings are grouped and stored in a tree data
 * structure consisting of {@link Node}s.
 * <p/>
 * This is the central construct and basic client API of JBoss Cache and is used for
 * cache-wide operations.
 * <p/>
 * The cache is constructed using a {@link CacheFactory} and is started
 * using {@link #start}, if not already started by the CacheFactory.
 * <p/>
 * Once constructed, the Cache interface can be used to create or access {@link Node}s, which contain data.  Once references
 * to {@link Node}s are obtained, data can be stored in them,
 * <p/>
 * As a convenience (and mainly to provide a familiar API to the older JBoss Cache 1.x.x releases) methods are provided that
 * operate directly on nodes.
 * <ul>
 * <li>{@link #put(Fqn,Object,Object)} </li>
 * <li>{@link #put(Fqn,java.util.Map)}  </li>
 * <li>{@link #get(Fqn,Object)}  </li>
 * <li>{@link #remove(Fqn,Object)}  </li>
 * <li>{@link #removeNode(Fqn)}  </li>
 * </ul>
 * <p/>
 * A simple example of usage:
 * <pre>
 *    // creates with default settings and starts the cache
 *    Cache cache = new DefaultCacheFactory().createCache();
 *    Fqn personRecords = Fqn.fromString("/org/mycompany/personRecords");
 * <p/>
 *    Node rootNode = cache.getRoot();
 *    Node personRecordsNode = rootNode.addChild(personRecords);
 * <p/>
 *    // now add some person records.
 *    Fqn peterGriffin = Fqn.fromString("/peterGriffin");
 *    Fqn stewieGriffin = Fqn.fromString("/stewieGriffin");
 * <p/>
 *    // the addChild() API uses relative Fqns
 *    Node peter = personRecordsNode.addChild(peterGriffin);
 *    Node stewie = personRecordsNode.addChild(stewieGriffin);
 * <p/>
 *    peter.put("name", "Peter Griffin");
 *    peter.put("ageGroup", "MidLifeCrisis");
 *    peter.put("homicidal", Boolean.FALSE);
 * <p/>
 *    stewie.put("name", "Stewie Griffin");
 *    stewie.put("ageGroup", "Infant");
 *    stewie.put("homicidal", Boolean.TRUE);
 * <p/>
 *    peter.getFqn().toString(); // will print out /org/mycompany/personRecords/peterGriffin
 *    stewie.getFqn().toString(); // will print out /org/mycompany/personRecords/stewieGriffin
 * <p/>
 *    peter.getFqn().getParent().equals(stewie.getFqn().getParent()); // will return true
 * <p/>
 * </pre>
 * <p/>
 * For more information, please read the JBoss Cache user guide and tutorial, available on <a href="http://labs.jboss.com/portal/jbosscache/docs/index.html" target="_BLANK">the JBoss Cache documentation site</a>,
 * and look through the examples <a href="http://labs.jboss.com/portal/jbosscache/download/index.html" target="_BLANK">shipped with the JBoss Cache distribution</a>.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see Node
 * @see CacheFactory
 * @since 2.0.0
 */
@ThreadSafe
public interface Cache<K, V> extends Lifecycle
{
   /**
    * Retrieves the configuration of this cache.
    *
    * @return the configuration.
    */
   Configuration getConfiguration();

   /**
    * Returns the root node of this cache.
    *
    * @return the root node
    */
   Node<K, V> getRoot();

   /**
    * Adds a {@link org.jboss.cache.notifications.annotation.CacheListener}-annotated object to the entire cache.  The object passed in needs to be properly annotated with the
    * {@link org.jboss.cache.notifications.annotation.CacheListener} annotation otherwise an {@link org.jboss.cache.notifications.IncorrectCacheListenerException} will be thrown.
    *
    * @param listener listener to add
    */
   void addCacheListener(Object listener);

   /**
    * Removes a {@link org.jboss.cache.notifications.annotation.CacheListener}-annotated object from the cache.  The object passed in needs to be properly annotated with the
    * {@link org.jboss.cache.notifications.annotation.CacheListener} annotation otherwise an {@link org.jboss.cache.notifications.IncorrectCacheListenerException} will be thrown.
    *
    * @param listener listener to remove
    */
   void removeCacheListener(Object listener);

   /**
    * Retrieves an immutable {@link List} of objects annotated as {@link org.jboss.cache.notifications.annotation.CacheListener}s attached to the cache.
    *
    * @return an immutable {@link List} of objects annotated as {@link org.jboss.cache.notifications.annotation.CacheListener}s attached to the cache.
    */
   Set<Object> getCacheListeners();

   /**
    * Associates the specified value with the specified key for a {@link Node} in this cache.
    * If the {@link Node} previously contained a mapping for this key, the old value is replaced by the specified value.
    *
    * @param fqn   <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to be accessed.
    * @param key   key with which the specified value is to be associated.
    * @param value value to be associated with the specified key.
    * @return previous value associated with specified key, or <code>null</code> if there was no mapping for key.
    *         A <code>null</code> return can also indicate that the Node previously associated <code>null</code> with the specified key, if the implementation supports null values.
    * @throws IllegalStateException if the cache is not in a started state.
    */
   V put(Fqn fqn, K key, V value);

   /**
    * Convenience method that takes a string representation of an Fqn.  Otherwise identical to {@link #put(Fqn, Object, Object)}
    *
    * @param fqn   String representation of the Fqn
    * @param key   key with which the specified value is to be associated.
    * @param value value to be associated with the specified key.
    * @return previous value associated with specified key, or <code>null</code> if there was no mapping for key.
    *         A <code>null</code> return can also indicate that the Node previously associated <code>null</code> with the specified key, if the implementation supports null values.
    * @throws IllegalStateException if the cache is not in a started state
    */

   V put(String fqn, K key, V value);

   /**
    * Under special operating behavior, associates the value with the specified key for a node identified by the Fqn passed in.
    * <ul>
    * <li> Only goes through if the node specified does not exist; no-op otherwise.</i>
    * <li> Force asynchronous mode for replication to prevent any blocking.</li>
    * <li> invalidation does not take place. </li>
    * <li> 0ms lock timeout to prevent any blocking here either. If the lock is not acquired, this method is a no-op, and swallows the timeout exception.</li>
    * <li> Ongoing transactions are suspended before this call, so failures here will not affect any ongoing transactions.</li>
    * <li> Errors and exceptions are 'silent' - logged at a much lower level than normal, and this method does not throw exceptions</li>
    * </ul>
    * This method is for caching data that has an external representation in storage, where, concurrent modification and
    * transactions are not a consideration, and failure to put the data in the cache should be treated as a 'suboptimal outcome'
    * rather than a 'failing outcome'.
    * <p/>
    * An example of when this method is useful is when data is read from, for example, a legacy datastore, and is cached before
    * returning the data to the caller.  Subsequent calls would prefer to get the data from the cache and if the data doesn't exist
    * in the cache, fetch again from the legacy datastore.
    * <p/>
    * See <a href="http://jira.jboss.com/jira/browse/JBCACHE-848">JBCACHE-848</a> for details around this feature.
    * <p/>
    *
    * @param fqn   <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to be accessed.
    * @param key   key with which the specified value is to be associated.
    * @param value value to be associated with the specified key.
    * @throws IllegalStateException if {@link #getCacheStatus()} would not return {@link org.jboss.cache.CacheStatus#STARTED}.
    */
   void putForExternalRead(Fqn fqn, K key, V value);

   /**
    * Copies all of the mappings from the specified map to a {@link Node}.
    *
    * @param fqn  <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to copy the data to
    * @param data mappings to copy
    * @throws IllegalStateException if the cache is not in a started state
    */
   void put(Fqn fqn, Map<? extends K, ? extends V> data);

   /**
    * Convenience method that takes a string representation of an Fqn.  Otherwise identical to {@link #put(Fqn, java.util.Map)}
    *
    * @param fqn  String representation of the Fqn
    * @param data data map to insert
    * @throws IllegalStateException if the cache is not in a started state
    */
   void put(String fqn, Map<? extends K, ? extends V> data);

   /**
    * Removes the mapping for this key from a Node.
    * Returns the value to which the Node previously associated the key, or
    * <code>null</code> if the Node contained no mapping for this key.
    *
    * @param fqn <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to be accessed.
    * @param key key whose mapping is to be removed from the Node
    * @return previous value associated with specified Node's key
    * @throws IllegalStateException if the cache is not in a started state
    */
   V remove(Fqn fqn, K key);

   /**
    * Convenience method that takes a string representation of an Fqn.  Otherwise identical to {@link #remove(Fqn, Object)}
    *
    * @param fqn string representation of the Fqn to retrieve
    * @param key key to remove
    * @return old value removed, or null if the fqn does not exist
    * @throws IllegalStateException if the cache is not in a started state
    */
   V remove(String fqn, K key);

   /**
    * Removes a {@link Node} indicated by absolute {@link Fqn}.
    *
    * @param fqn {@link Node} to remove
    * @return true if the node was removed, false if the node was not found
    * @throws IllegalStateException if the cache is not in a started state
    */
   boolean removeNode(Fqn fqn);

   /**
    * Convenience method that takes a string representation of an Fqn.  Otherwise identical to {@link #removeNode(Fqn)}
    *
    * @param fqn string representation of the Fqn to retrieve
    * @return true if the node was found and removed, false otherwise
    * @throws IllegalStateException if the cache is not in a started state
    */
   boolean removeNode(String fqn);

   /**
    * A convenience method to retrieve a node directly from the cache.  Equivalent to calling cache.getRoot().getChild(fqn).
    *
    * @param fqn fqn of the node to retrieve
    * @return a Node object, or a null if the node does not exist.
    * @throws IllegalStateException if the cache is not in a started state
    */
   Node<K, V> getNode(Fqn fqn);

   /**
    * Convenience method that takes a string representation of an Fqn.  Otherwise identical to {@link #getNode(Fqn)}
    *
    * @param fqn string representation of the Fqn to retrieve
    * @return node, or null if the node does not exist
    * @throws IllegalStateException if the cache is not in a started state
    */
   Node<K, V> getNode(String fqn);

   /**
    * Returns all children of a given node.  Returns an empty set if there are no children.
    * The set is unmodifiable.
    *
    * @param fqn The fully qualified name of the node
    * @return Set an unmodifiable set of children names, Object.
    */
   Set<Object> getChildrenNames(Fqn fqn);

   /**
    * Convenience method that takes a String representation of an Fqn.  Otherwise identical to {@link #getChildrenNames(Fqn)}
    *
    * @param fqn as a string
    * @return Set an unmodifiable set of children names, Object.
    */
   Set<String> getChildrenNames(String fqn);

   /**
    * Tests if a node is a leaf, i.e., doesn't have any children
    * @param fqn fqn to test
    * @return true if it is a leaf, false otherwise
    */
   boolean isLeaf(Fqn fqn);

   /**
    * Tests if a node is a leaf, i.e., doesn't have any children
    * @param fqn fqn to test
    * @return true if it is a leaf, false otherwise
    */   
   boolean isLeaf(String fqn);

   /**
    * Convenience method that allows for direct access to the data in a {@link Node}.
    *
    * @param fqn <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to be accessed.
    * @param key key under which value is to be retrieved.
    * @return returns data held under specified key in {@link Node} denoted by specified Fqn.
    * @throws IllegalStateException if the cache is not in a started state
    */
   V get(Fqn fqn, K key);

   /**
    * Convenience method that takes a string representation of an Fqn.  Otherwise identical to {@link #get(Fqn, Object)}
    *
    * @param fqn string representation of the Fqn to retrieve
    * @param key key to fetch
    * @return value, or null if the fqn does not exist.
    * @throws IllegalStateException if the cache is not in a started state
    */
   V get(String fqn, K key);

   /**
    * Eviction call that evicts the specified {@link Node} from memory.
    *
    * @param fqn       <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to be evicted.
    * @param recursive evicts children as well
    * @throws IllegalStateException if the cache is not in a started state
    */
   void evict(Fqn fqn, boolean recursive);

   /**
    * Eviction call that evicts the specified {@link Node} from memory.  Not recursive.
    *
    * @param fqn <b><i>absolute</i></b> {@link Fqn} to the {@link Node} to be evicted.
    * @throws IllegalStateException if the cache is not in a started state
    */
   void evict(Fqn fqn);

   /**
    * Retrieves a {@link Region} for a given {@link Fqn}.  If the region does not exist,
    * and <li>createIfAbsent</li> is true, then one is created.
    * <p/>
    * If not, parent Fqns will be consulted in turn for registered regions, gradually working up to
    * Fqn.ROOT.  If no regions are defined in any of the parents either, a null is returned.
    *
    * @param fqn            Fqn that is contained in a region.
    * @param createIfAbsent If true, will create a new associated region if not found.
    * @return a MarshRegion. Null if none is found.
    * @see Region
    */
   Region getRegion(Fqn fqn, boolean createIfAbsent);

   /**
    * Removes a region denoted by the Fqn passed in.
    *
    * @param fqn of the region to remove
    * @return true if a region did exist and was removed; false otherwise.
    */
   boolean removeRegion(Fqn fqn);

   /**
    * Lifecycle method that initializes configuration state, the root node, etc.
    *
    * @throws CacheException if there are creation problems
    */
   void create() throws CacheException;

   /**
    * Lifecycle method that starts the cache loader,
    * starts cache replication, starts the region manager, etc., and (if configured) warms the cache using a
    * state transfer or cache loader preload.
    *
    * @throws CacheException if there are startup problems
    */
   void start() throws CacheException;

   /**
    * Lifecycle method that stops the cache, including replication,
    * clustering, cache loading, notifications, etc., and clears all cache in-memory state.
    * <p/>
    * State can be reconstituted by using either a cache loader or state transfer when the cache starts again.
    */
   void stop();

   /**
    * Lifecycle method that destroys the cache and removes any interceptors/configuration elements.
    * Cache can then be restarted (potentially after reconfiguring) using {@link #create()} and {@link #start()}.
    */
   void destroy();

   /**
    * Gets where the cache currently is its lifecycle transitions.
    *
    * @return the CacheStatus. Will not return <code>null</code>.
    */
   CacheStatus getCacheStatus();

   /**
    * @return the current invocation context for the current invocation and cache instance.
    * @throws IllegalStateException if the cache has been destroyed.
    */
   InvocationContext getInvocationContext();

   /**
    * Sets the passed in {@link org.jboss.cache.InvocationContext} as current.
    *
    * @param ctx invocation context to use
    * @throws IllegalStateException if the cache has been destroyed.
    */
   void setInvocationContext(InvocationContext ctx);

   /**
    * Returns the local address of this cache in a cluster, or <code>null</code>
    * if running in local mode.
    *
    * @return the local address of this cache in a cluster, or <code>null</code>
    *         if running in local mode.
    */
   Address getLocalAddress();

   /**
    * Returns a list of members in the cluster, or <code>null</code>
    * if running in local mode.
    *
    * @return a {@link List} of members in the cluster, or <code>null</code>
    *         if running in local mode.
    */
   List<Address> getMembers();

   /**
    * Moves a part of the cache to a different subtree.
    * <p/>
    * E.g.:
    * <p/>
    * assume a cache structure such as:
    * <p/>
    * <pre>
    *  /a/b/c
    *  /a/b/d
    *  /a/b/e
    * <p/>
    * <p/>
    *  Fqn f1 = Fqn.fromString("/a/b/c");
    *  Fqn f2 = Fqn.fromString("/a/b/d");
    * <p/>
    *  cache.move(f1, f2);
    * </pre>
    * <p/>
    * Will result in:
    * <pre>
    * <p/>
    * /a/b/d/c
    * /a/b/e
    * <p/>
    * </pre>
    * <p/>
    * and now
    * <p/>
    * <pre>
    *  Fqn f3 = Fqn.fromString("/a/b/e");
    *  Fqn f4 = Fqn.fromString("/a");
    *  cache.move(f3, f4);
    * </pre>
    * <p/>
    * will result in:
    * <pre>
    * /a/b/d/c
    * /a/e
    * </pre>
    * No-op if the node to be moved is the root node.
    * <p/>
    * <b>Note</b>: As of 3.0.0 and when using MVCC locking, more specific behaviour is defined as follows:
    * <ul>
    * <li>A no-op if the node is moved unto itself.  E.g., <tt>move(fqn, fqn.getParent())</tt> will not do anything.</li>
    * <li>If a target node does not exist it will be created silently, to be more consistent with other APIs such as <tt>put()</tt> on a nonexistent node.</li>
    * <li>If the source node does not exist this is a no-op, to be more consistent with other APIs such as <tt>get()</tt> on a nonexistent node.</li>
    * </ul>
    *
    * @param nodeToMove the Fqn of the node to move.
    * @param newParent  new location under which to attach the node being moved.
    * @throws NodeNotExistsException may throw one of these if the target node does not exist or if a different thread has moved this node elsewhere already.
    * @throws IllegalStateException  if {@link #getCacheStatus()} would not return {@link CacheStatus#STARTED}.
    */
   void move(Fqn nodeToMove, Fqn newParent) throws NodeNotExistsException;

   /**
    * Convenience method that takes in string representations of Fqns.  Otherwise identical to {@link #move(Fqn, Fqn)}
    *
    * @throws IllegalStateException if {@link #getCacheStatus()} would not return {@link CacheStatus#STARTED}.
    */
   void move(String nodeToMove, String newParent) throws NodeNotExistsException;

   /**
    * Returns the version of the cache as a string.
    *
    * @return the version string of the cache.
    * @see Version#printVersion
    */
   String getVersion();

   /**
    * Retrieves a defensively copied data map of the underlying node.  A convenience method to retrieving a node and
    * getting data from the node directly.
    *
    * @param fqn
    * @return map of data, or an empty map
    * @throws CacheException
    * @throws IllegalStateException if {@link #getCacheStatus()} would not return {@link CacheStatus#STARTED}.
    */
   Map<K, V> getData(Fqn fqn);

   /**
    * Convenience method that takes in a String represenation of the Fqn.  Otherwise identical to {@link #getKeys(Fqn)}.
    */
   Set<K> getKeys(String fqn);

   /**
    * Returns a set of attribute keys for the Fqn.
    * Returns null if the node is not found, otherwise a Set.
    * The set is a copy of the actual keys for this node.
    * <p/>
    * A convenience method to retrieving a node and
    * getting keys from the node directly.
    *
    * @param fqn name of the node
    * @throws IllegalStateException if {@link #getCacheStatus()} would not return {@link CacheStatus#STARTED}.
    */
   Set<K> getKeys(Fqn fqn);

   /**
    * Convenience method that takes in a String represenation of the Fqn.  Otherwise identical to {@link #clearData(Fqn)}.
    *
    * @throws IllegalStateException if {@link #getCacheStatus()} would not return {@link CacheStatus#STARTED}.
    */
   void clearData(String fqn);

   /**
    * Removes the keys and properties from a named node.
    * <p/>
    * A convenience method to retrieving a node and
    * getting keys from the node directly.
    *
    * @param fqn name of the node
    * @throws IllegalStateException if {@link #getCacheStatus()} would not return {@link CacheStatus#STARTED}.
    */
   void clearData(Fqn fqn);

   /**
    * Starts a batch.  This is a lightweight batching mechanism that groups cache writes together and finally performs the
    * write, persistence and/or replication when {@link #endBatch(boolean)} is called rather than for each invocation on the
    * cache.
    * <p/>
    * Note that if there is an existing transaction in scope and the cache has been configured to use a JTA compliant
    * transaction manager, calls to {@link #startBatch()} and {@link #endBatch(boolean)} are ignored and treated as no-ops.
    * <p/>
    *
    * @see #endBatch(boolean)
    * @since 3.0
    */
   void startBatch();

   /**
    * Ends an existing ongoing batch.  A no-op if a batch has not been started yet.
    * <p/>
    * Note that if there is an existing transaction in scope and the cache has been configured to use a JTA compliant
    * transaction manager, calls to {@link #startBatch()} and {@link #endBatch(boolean)} are ignored and treated as no-ops.
    * <p/>
    *
    * @param successful if <tt>true</tt>, changes made in the batch are committed.  If <tt>false</tt>, they are discarded.
    * @see #startBatch()
    * @since 3.0
    */
   void endBatch(boolean successful);

   /**
    * Adds a custom interceptor to the interceptor chain, at specified position, where the first interceptor in the chain
    * is at position 0 and the last one at getInterceptorChain().size() - 1.
    *
    * @param i        the interceptor to add
    * @param position the position to add the interceptor
    * @since 3.0
    */
   void addInterceptor(CommandInterceptor i, int position);

   /**
    * Adds a custom interceptor to the interceptor chain, after an instance of the specified interceptor type.  Throws a
    * cache exception if it cannot find an interceptor of the specified type.
    *
    * @param i                interceptor to add
    * @param afterInterceptor interceptor type after which to place custom interceptor
    * @since 3.0
    */
   void addInterceptor(CommandInterceptor i, Class<? extends CommandInterceptor> afterInterceptor);

   /**
    * Removes the interceptor at a specified position, where the first interceptor in the chain
    * is at position 0 and the last one at getInterceptorChain().size() - 1.
    *
    * @param position the position at which to remove an interceptor
    * @since 3.0
    */
   void removeInterceptor(int position);

   /**
    * Removes the interceptor of specified type.
    *
    * @param interceptorType type of interceptor to remove
    * @since 3.0
    */
   void removeInterceptor(Class<? extends CommandInterceptor> interceptorType);
}
