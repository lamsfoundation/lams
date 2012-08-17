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

import net.jcip.annotations.ThreadSafe;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.Lifecycle;
import org.jboss.cache.Modification;
import org.jboss.cache.RegionManager;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link org.jboss.cache.loader.CacheLoader} implementation persists and load keys to and from
 * secondary storage, such as a database or filesystem.  Typically,
 * implementations store a series of keys and values (an entire {@link Map})
 * under a single {@link Fqn}.  Loading and saving properties of an entire
 * {@link Map} should be atomic.
 * <p/>
 * Lifecycle: First an instance of the loader is created, then the
 * configuration ({@link #setConfig(org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig)}) and cache ({@link
 * #setCache(CacheSPI)}) are set. After this, {@link #create()} is called.
 * Then {@link #start()} is called. When re-deployed, {@link #stop()} will be
 * called, followed by another {@link #start()}. Finally, when shut down,
 * {@link #destroy()} is called, after which the loader is unusable.
 * <p/>
 * An {@link org.jboss.cache.loader.AbstractCacheLoader} is provided as a convenient starting place
 * when implementing your own {@link org.jboss.cache.loader.CacheLoader}.
 * <p/>
 * It is important to note that all implementations are thread safe, as concurrent reads and writes, potentially even to
 * the same {@link Fqn}, are possible.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see CacheSPI
 * @see org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig
 * @see org.jboss.cache.loader.AbstractCacheLoader
 * @since 2.0.0
 */
@ThreadSafe
public interface CacheLoader extends Lifecycle
{
   /**
    * Sets the configuration.  This is called before {@link #create()} and {@link #start()}.
    *
    * @param config May be an instance of the {@link org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig} base
    *               class, in which case the cache loader should use the
    *               {@link org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig#getProperties()}
    *               method to find configuration information. Alternatively,
    *               may be a type-specific subclass of {@link org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig},
    *               if there is one.
    */
   void setConfig(IndividualCacheLoaderConfig config);

   /**
    * Gets the configuration.
    *
    * @return the configuration, represented by a {@link org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig} object.
    */
   IndividualCacheLoaderConfig getConfig();

   /**
    * Sets the {@link CacheSPI} that is maintaining this CacheLoader.
    * This method allows this CacheLoader to set a reference to the {@link CacheSPI}.
    * This method is called be called after the CacheLoader instance has been constructed.
    *
    * @param c The cache on which this loader works
    */
   void setCache(CacheSPI c);


   /**
    * Returns a set of children node names.
    * All names are <em>relative</em> to this parent {@link Fqn}.
    * Returns null if the named node is not found or there are no children.
    * The returned set must not be modifiable.  Implementors can use
    * {@link java.util.Collections#unmodifiableSet(java.util.Set)} to make the set unmodifiable.
    * <p/>
    * Implementors may impose restrictions on the contents of an Fqn (such as Strings-only) and as such, indirectly
    * impose the same restriction on the contents of a Set returned by getChildrenNames().
    * <p/>
    *
    * @param fqn The {@link Fqn} of the parent
    * @return Set a set of children.  Returns null if no children nodes are
    *         present, or the parent is not present
    */
   Set<?> getChildrenNames(Fqn fqn) throws Exception;

   /**
    * Returns all keys and values from the persistent store, given a {@link org.jboss.cache.Fqn}
    *
    * @param name the {@link Fqn} to search for.
    * @return Map<Object,Object> keys and values for the given node. Returns
    *         null if the node is not found.  If the node is found but has no
    *         attributes, this method returns an empty Map.
    */
   Map<Object, Object> get(Fqn name) throws Exception;


   /**
    * Returns true if the CacheLoader has a node with a {@link Fqn}.
    *
    * @return true if node exists, false otherwise
    */
   boolean exists(Fqn name) throws Exception;


   /**
    * Puts a key and value into the attribute map of a given node.  If the
    * node does not exist, all parent nodes from the root down are created
    * automatically.  Returns the old value.
    */
   Object put(Fqn name, Object key, Object value) throws Exception;

   /**
    * Puts all entries of the map into the existing map of the given node,
    * overwriting existing keys, but not clearing the existing map before
    * insertion.
    * This is the same behavior as {@link Map#putAll}.
    * If the node does not exist, all parent nodes from the root down are created automatically
    * <p/>
    * <b>Note</b> since <i>3.0</i>, as an optimization, this method will require a definitive attribute map and
    * not just a subset.  This will allow cache loader implementations to overwrite rather than merge, if that is
    * deemed more efficient.  This will not break backward compatibility since performing a merge will not cause
    * any loss of data even though it is an unnecessary step.
    *
    * @param name       The fully qualified name of the node
    * @param attributes A Map of attributes. Can be null
    */
   void put(Fqn name, Map<Object, Object> attributes) throws Exception;

   /**
    * Applies all modifications to the backend store.
    * Changes may be applied in a single operation.
    *
    * @param modifications A List<Modification> of modifications
    */
   void put(List<Modification> modifications) throws Exception;

   /**
    * Removes the given key and value from the attributes of the given node.
    * Does nothing if the node doesn't exist
    * Returns the removed value.
    */
   Object remove(Fqn fqn, Object key) throws Exception;

   /**
    * Removes the given node and all its subnodes, does nothing if the node does not exist.
    *
    * @param fqn the {@link Fqn} of the node
    */
   void remove(Fqn fqn) throws Exception;


   /**
    * Removes all attributes from a given node, but doesn't delete the node
    * itself or any subnodes.
    *
    * @param fqn the {@link Fqn} of the node
    */
   void removeData(Fqn fqn) throws Exception;


   /**
    * Prepares a list of modifications. For example, for a DB-based CacheLoader:
    * <ol>
    * <li>Create a local (JDBC) transaction
    * <li>Associate the local transaction with <code>tx</code> (tx is the key)
    * <li>Execute the corresponding SQL statements against the DB (statements derived from modifications)
    * </ol>
    * For non-transactional CacheLoader (e.g. file-based), the implementation could attempt to implement its own transactional
    * logic, attempting to write data to a temp location (or memory) and writing it to the proper location upon commit.
    *
    * @param tx            The transaction, indended to be used by implementations as an identifier of the transaction (and not necessarily a JTA {@link javax.transaction.Transaction} object)
    * @param modifications A {@link List} containing {@link  org.jboss.cache.Modification}s, for the given transaction
    * @param one_phase     Persist immediately and (for example) commit the local JDBC transaction as well. When true,
    *                      we won't get a {@link #commit(Object)} or {@link #rollback(Object)} method call later
    * @throws Exception
    */
   void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception;

   /**
    * Commits the transaction. A DB-based CacheLoader would look up the local
    * JDBC transaction asociated with <code>tx</code> and commit that
    * transaction.  Non-transactional CacheLoaders could simply write the data
    * that was previously saved transiently under the given <code>tx</code>
    * key, to (for example) a file system.
    * <p/>
    * <b>Note</b> this only holds if the previous prepare() did not define <pre>one_phase=true</pre>
    *
    * @param tx transaction to commit
    */
   void commit(Object tx) throws Exception;

   /**
    * Rolls the transaction back. A DB-based CacheLoader would look up the
    * local JDBC transaction asociated with <code>tx</code> and roll back that
    * transaction.
    *
    * @param tx transaction to roll back
    */
   void rollback(Object tx);

   /**
    * Fetches the entire state for this cache from secondary storage (disk, database)
    * and writes it to a provided ObjectOutputStream. State written to the provided
    * ObjectOutputStream parameter is used for initialization of a new CacheImpl instance.
    * When the state gets transferred to the new cache instance its cacheloader calls
    * {@link #storeEntireState(ObjectInputStream)}
    * <p/>
    * Implementations of this method should not catch any exception or close the
    * given ObjectOutputStream parameter. In order to ensure cacheloader interoperability
    * contents of the cache are written to the ObjectOutputStream as a sequence of
    * NodeData objects.
    * <p/>
    * Default implementation is provided by {@link AbstractCacheLoader} and ensures cacheloader
    * interoperability. Implementors are encouraged to consider extending AbstractCacheLoader
    * prior to implementing completely custom cacheloader.
    *
    * @param os ObjectOutputStream to write state
    * @see AbstractCacheLoader#loadEntireState(ObjectOutputStream)
    * @see org.jboss.cache.marshall.NodeData
    */
   void loadEntireState(ObjectOutputStream os) throws Exception;

   /**
    * Stores the entire state for this cache by reading it from a provided ObjectInputStream.
    * The state was provided to this cache by calling {@link #loadEntireState(ObjectOutputStream)}}
    * on some other cache instance. State currently in storage gets overwritten.
    * <p/>
    * Implementations of this method should not catch any exception or close the
    * given ObjectInputStream parameter. In order to ensure cacheloader interoperability
    * contents of the cache are read from the ObjectInputStream as a sequence of
    * NodeData objects.
    * <p/>
    * Default implementation is provided by {@link AbstractCacheLoader} and ensures cacheloader
    * interoperability. Implementors are encouraged to consider extending AbstractCacheLoader
    * prior to implementing completely custom cacheloader.
    *
    * @param is ObjectInputStream to read state
    * @see AbstractCacheLoader#storeEntireState(ObjectInputStream)
    * @see org.jboss.cache.marshall.NodeData
    */
   void storeEntireState(ObjectInputStream is) throws Exception;

   /**
    * Fetches a portion of the state for this cache from secondary storage (disk, database)
    * and writes it to a provided ObjectOutputStream. State written to the provided
    * ObjectOutputStream parameter is used for activation of a portion of a new CacheImpl instance.
    * When the state gets transferred to the new cache instance its cacheloader calls
    * {@link #storeState(Fqn,ObjectInputStream)}.
    * <p/>
    * Implementations of this method should not catch any exception or close the
    * given ObjectOutputStream parameter. In order to ensure cacheloader interoperability
    * contents of the cache are written to the ObjectOutputStream as a sequence of
    * NodeData objects.
    * <p/>
    * Default implementation is provided by {@link AbstractCacheLoader} and ensures cacheloader
    * interoperability. Implementors are encouraged to consider extending AbstractCacheLoader
    * prior to implementing completely custom cacheloader.
    *
    * @param subtree Fqn naming the root (i.e. highest level parent) node of
    *                the subtree for which state is requested.
    * @param os      ObjectOutputStream to write state
    * @see AbstractCacheLoader#loadState(Fqn,ObjectOutputStream)
    * @see org.jboss.cache.Region#activate()
    * @see org.jboss.cache.marshall.NodeData
    */
   void loadState(Fqn subtree, ObjectOutputStream os) throws Exception;

   /**
    * Stores the given portion of the cache tree's state in secondary storage.
    * Overwrite whatever is currently in secondary storage.  If the transferred
    * state has Fqns equal to or children of parameter <code>subtree</code>,
    * then no special behavior is required.  Otherwise, ensure that
    * the state is integrated under the given <code>subtree</code>. Typically
    * in the latter case <code>subtree</code> would be the Fqn of the buddy
    * backup region for
    * a buddy group; e.g.
    * <p/>
    * If the the transferred state had Fqns starting with "/a" and
    * <code>subtree</code> was "/_BUDDY_BACKUP_/192.168.1.2:5555" then the
    * state should be stored in the local persistent store under
    * "/_BUDDY_BACKUP_/192.168.1.2:5555/a"
    * <p/>
    * Implementations of this method should not catch any exception or close the
    * given ObjectInputStream parameter. In order to ensure cacheloader interoperability
    * contents of the cache are read from the ObjectInputStream as a sequence of
    * NodeData objects.
    * <p/>
    * Default implementation is provided by {@link AbstractCacheLoader} and ensures cacheloader
    * interoperability. Implementors are encouraged to consider extending AbstractCacheLoader
    * prior to implementing completely custom cacheloader.
    *
    * @param is      ObjectInputStream to read state
    * @param subtree Fqn naming the root (i.e. highest level parent) node of
    *                the subtree included in <code>state</code>.  If the Fqns
    *                of the data included in <code>state</code> are not
    *                already children of <code>subtree</code>, then their
    *                Fqns should be altered to make them children of
    *                <code>subtree</code> before they are persisted.
    * @see AbstractCacheLoader#storeState(Fqn,ObjectInputStream)
    * @see org.jboss.cache.marshall.NodeData
    */
   void storeState(Fqn subtree, ObjectInputStream is) throws Exception;

   /**
    * Sets the {@link org.jboss.cache.RegionManager} this object should use to manage
    * marshalling/unmarshalling of different regions using different
    * classloaders.
    * <p/>
    * <strong>NOTE:</strong> This method is only intended to be used
    * by the <code>CacheSPI</code> instance this cache loader is
    * associated with.
    * </p>
    *
    * @param manager the region manager to use, or <code>null</code>.
    */
   void setRegionManager(RegionManager manager);
}
