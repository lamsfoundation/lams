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
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.buddyreplication.GravitateResult;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.marshall.Marshaller;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.statetransfer.StateTransferManager;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionTable;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.List;
import java.util.Set;

/**
 * A more detailed interface to {@link Cache}, which is used when writing plugins for or extending JBoss Cache.  A reference
 * to this interface should only be obtained when it is passed in to your code, for example when you write an
 * {@link org.jboss.cache.interceptors.base.CommandInterceptor} or {@link CacheLoader}.
 * <p/>
 * <B><I>You should NEVER attempt to directly cast a {@link Cache} instance to this interface.  In future, the implementation may not allow it.</I></B>
 * <p/>
 * This interface contains overridden method signatures of some methods from {@link Cache}, overridden to ensure return
 * types of {@link Node} are replaced with {@link NodeSPI}.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see NodeSPI
 * @see Cache
 * @see org.jboss.cache.loader.CacheLoader
 * @since 2.0.0
 */
@ThreadSafe
public interface CacheSPI<K, V> extends Cache<K, V>
{
   /**
    * Overrides {@link org.jboss.cache.Cache#getRoot()} to return a {@link org.jboss.cache.NodeSPI} instead of a {@link org.jboss.cache.Node}.
    */
   NodeSPI<K, V> getRoot();

   /**
    * Overrides {@link Cache#getNode(String)} to return a {@link org.jboss.cache.NodeSPI} instead of a {@link org.jboss.cache.Node}.
    *
    * @param s string representation of an Fqn
    * @return a NodeSPI
    */
   NodeSPI<K, V> getNode(String s);

   /**
    * Overrides {@link Cache#getNode(Fqn)} to return a {@link org.jboss.cache.NodeSPI} instead of a {@link org.jboss.cache.Node}.
    *
    * @param f an Fqn
    * @return a NodeSPI
    */
   NodeSPI<K, V> getNode(Fqn f);


   /**
    * Retrieves a reference to a running {@link javax.transaction.TransactionManager}, if one is configured.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return a TransactionManager
    */
   TransactionManager getTransactionManager();

   /**
    * Retrieves the current Interceptor chain.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return an immutable {@link List} of {@link org.jboss.cache.interceptors.base.CommandInterceptor}s configured for this cache, or
    *         <code>null</code> if {@link Cache#create() create()} has not been invoked
    *         and the interceptors thus do not exist.
    */
   List<CommandInterceptor> getInterceptorChain();

   /**
    * Retrieves an instance of a {@link Marshaller}, which is capable of
    * converting Java objects to bytestreams and back in an efficient manner, which is
    * also interoperable with bytestreams produced/consumed by other versions of JBoss
    * Cache.
    * <p/>
    * The use of this marshaller is the <b>recommended</b> way of creating efficient,
    * compatible, byte streams from objects.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return an instance of {@link Marshaller}
    */
   Marshaller getMarshaller();

   /**
    * Retrieves the current CacheCacheLoaderManager instance associated with the current Cache instance.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return Retrieves a reference to the currently configured {@link org.jboss.cache.loader.CacheLoaderManager} if one or more cache loaders are configured, null otherwise.
    */
   CacheLoaderManager getCacheLoaderManager();

   /**
    * Retrieves the current BuddyManager instance associated with the current Cache instance.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return an instance of {@link BuddyManager} if buddy replication is enabled, null otherwise.
    */
   BuddyManager getBuddyManager();

   /**
    * Retrieves the current TransactionTable instance associated with the current Cache instance.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return the current {@link TransactionTable}
    */
   TransactionTable getTransactionTable();

   /**
    * Gets a handle of the RPC manager.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return the {@link org.jboss.cache.RPCManager} configured.
    */
   RPCManager getRPCManager();

   /**
    * Retrieves the current StateTransferManager instance associated with the current Cache instance.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return the current {@link org.jboss.cache.statetransfer.StateTransferManager}
    */
   StateTransferManager getStateTransferManager();

   /**
    * Retrieves the current RegionManager instance associated with the current Cache instance.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return the {@link RegionManager}
    */
   RegionManager getRegionManager();


   /**
    * Retrieves the current Notifier instance associated with the current Cache instance.
    * <p/>
    * From 2.1.0, Interceptor authors should obtain this by injection rather than this method.  See the
    * {@link org.jboss.cache.factories.annotations.Inject} annotation.
    *
    * @return the notifier attached with this instance of the cache.  See {@link org.jboss.cache.notifications.Notifier}, a class
    *         that is responsible for emitting notifications to registered CacheListeners.
    */
   Notifier getNotifier();

   /**
    * @return the name of the cluster.  Null if running in local mode.
    */
   String getClusterName();

   /**
    * @return the number of attributes in the cache.
    */
   int getNumberOfAttributes();

   /**
    * @return the number of nodes in the cache.
    */
   int getNumberOfNodes();

   /**
    * Returns the global transaction for this local transaction.
    * Optionally creates a new global transaction if it does not exist.
    *
    * @param tx                the current transaction
    * @param createIfNotExists if true creates a new transaction if none exists
    * @return a GlobalTransaction
    */
   GlobalTransaction getCurrentTransaction(Transaction tx, boolean createIfNotExists);

   /**
    * Returns the transaction associated with the current thread.
    * If a local transaction exists, but doesn't yet have a mapping to a
    * GlobalTransaction, a new GlobalTransaction will be created and mapped to
    * the local transaction.  Note that if a local transaction exists, but is
    * not ACTIVE or PREPARING, null is returned.
    *
    * @return A GlobalTransaction, or null if no (local) transaction was associated with the current thread
    */
   GlobalTransaction getCurrentTransaction();

   /**
    * Returns a node without accessing the interceptor chain.  Does not return any nodes marked as invalid.  Note that this call works
    * directly on the cache data structure and will not pass through the interceptor chain.  Hence node locking, cache
    * loading or activation does not take place, and so the results of this call should not be treated as definitive.  Concurrent node
    * removal, passivation, etc. may affect the results of this call.
    *
    * @param fqn                 the Fqn to look up.
    * @param includeDeletedNodes if you intend to see nodes marked as deleted within the current tx, set this to true
    * @return a node if one exists or null
    */
   NodeSPI<K, V> peek(Fqn fqn, boolean includeDeletedNodes);

   /**
    * Returns a node without accessing the interceptor chain, optionally returning nodes that are marked as invalid ({@link org.jboss.cache.Node#isValid()} == false).
    * Note that this call works
    * directly on the cache data structure and will not pass through the interceptor chain.  Hence node locking, cache
    * loading or activation does not take place, and so the results of this call should not be treated as definitive.  Concurrent node
    * removal, passivation, etc. may affect the results of this call.
    *
    * @param fqn                 the Fqn to look up.
    * @param includeDeletedNodes if you intend to see nodes marked as deleted within the current tx, set this to true
    * @param includeInvalidNodes if true, nodes marked as being invalid are also returned.
    * @return a node if one exists or null
    */
   NodeSPI<K, V> peek(Fqn fqn, boolean includeDeletedNodes, boolean includeInvalidNodes);

   /**
    * Used with buddy replication's data gravitation interceptor.  If marshalling is necessary, ensure that the cache is
    * configured to use {@link org.jboss.cache.config.Configuration#useRegionBasedMarshalling} and the {@link org.jboss.cache.Region}
    * pertaining to the Fqn passed in is activated, and has an appropriate ClassLoader.
    *
    * @param fqn                       the fqn to gravitate
    * @param searchBuddyBackupSubtrees if true, buddy backup subtrees are searched and if false, they are not.
    * @param ctx
    * @return a GravitateResult which contains the data for the gravitation
    */
   GravitateResult gravitateData(Fqn fqn, boolean searchBuddyBackupSubtrees, InvocationContext ctx);

   /**
    * Returns a Set<Fqn> of Fqns of the topmost node of internal regions that
    * should not included in standard state transfers. Will include
    * {@link BuddyManager#BUDDY_BACKUP_SUBTREE} if buddy replication is
    * enabled.
    *
    * @return an unmodifiable Set<Fqn>.  Will not return <code>null</code>.
    */
   Set<Fqn> getInternalFqns();

   int getNumberOfLocksHeld();

   /**
    * Helper method that does a peek and ensures that the result of the peek is not null.  Note that this call works
    * directly on the cache data structure and will not pass through the interceptor chain.  Hence node locking, cache
    * loading or activation does not take place, and so the results of this call should not be treated as definitive.
    *
    * @param fqn Fqn to peek
    * @return true if the peek returns a non-null value.
    */
   boolean exists(Fqn fqn);

   /**
    * A convenience method that takes a String representation of an Fqn.  Otherwise identical to {@link #exists(Fqn)}.
    * Note that this call works
    * directly on the cache data structure and will not pass through the interceptor chain.  Hence node locking, cache
    * loading or activation does not take place, and so the results of this call should not be treated as definitive.
    */
   boolean exists(String fqn);

   /**
    * Returns the component registry associated with this cache instance.
    *
    * @see org.jboss.cache.factories.ComponentRegistry
    */
   ComponentRegistry getComponentRegistry();
}
