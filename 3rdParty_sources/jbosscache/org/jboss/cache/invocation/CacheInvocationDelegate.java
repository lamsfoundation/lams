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
package org.jboss.cache.invocation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.*;
import org.jboss.cache.batch.BatchContainer;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.buddyreplication.GravitateResult;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.marshall.Marshaller;
import org.jboss.cache.mvcc.MVCCNodeHelper;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.statetransfer.StateTransferManager;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionTable;
import org.jboss.cache.util.Immutables;
import org.jgroups.Address;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * The delegate that users (and ChainedInterceptor authors) interact with when they create a cache by using a cache factory.
 * This wrapper delegates calls down the interceptor chain.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
@NonVolatile
public class CacheInvocationDelegate<K, V> extends AbstractInvocationDelegate implements CacheSPI<K, V>
{
   private static final Log log = LogFactory.getLog(CacheInvocationDelegate.class);

   // this stuff is needed since the SPI has methods to retrieve these.
   private StateTransferManager stateTransferManager;
   private CacheLoaderManager cacheLoaderManager;
   private Notifier notifier;
   private TransactionManager transactionManager;
   private BuddyManager buddyManager;
   private TransactionTable transactionTable;
   private RPCManager rpcManager;
   private RegionManager regionManager;
   private Marshaller marshaller;
   private DataContainer dataContainer;
   private CommandsFactory commandsFactory;
   private MVCCNodeHelper mvccHelper;
   private boolean usingMvcc;
   private BatchContainer batchContainer;

   @Inject
   public void initialize(StateTransferManager stateTransferManager, CacheLoaderManager cacheLoaderManager, Notifier notifier,
                          TransactionManager transactionManager, BuddyManager buddyManager, TransactionTable transactionTable,
                          RPCManager rpcManager, RegionManager regionManager, Marshaller marshaller,
                          CommandsFactory commandsFactory, DataContainer dataContainer, MVCCNodeHelper mvccHelper, BatchContainer batchContainer)
   {
      this.stateTransferManager = stateTransferManager;
      this.cacheLoaderManager = cacheLoaderManager;
      this.notifier = notifier;
      this.transactionManager = transactionManager;
      this.buddyManager = buddyManager;
      this.transactionTable = transactionTable;
      this.rpcManager = rpcManager;
      this.regionManager = regionManager;
      this.marshaller = marshaller;
      this.dataContainer = dataContainer;
      this.commandsFactory = commandsFactory;
      this.mvccHelper = mvccHelper;
      this.batchContainer = batchContainer;
   }

   @Start
   void setNodeLockingScheme()
   {
      usingMvcc = configuration.getNodeLockingScheme() == NodeLockingScheme.MVCC;
   }

   private void reset()
   {
      this.usingMvcc = false;
      this.stateTransferManager = null;
      this.cacheLoaderManager = null;
      this.transactionManager = null;
      this.buddyManager = null;
      this.transactionTable = null;
      this.rpcManager = null;
      this.marshaller = null;
      this.dataContainer = null;
      this.commandsFactory = null;
   }

   @Override
   public String toString()
   {
      return dataContainer == null ? super.toString() : dataContainer.toString();
   }

   public Configuration getConfiguration()
   {
      return configuration;
   }

   public NodeSPI<K, V> getRoot()
   {
      return getNode(Fqn.ROOT);
   }

   public TransactionManager getTransactionManager()
   {
      return transactionManager;
   }

   public void addInterceptor(CommandInterceptor i, int position)
   {
      invoker.addInterceptor(i, position);
   }

   public void addInterceptor(CommandInterceptor i, Class<? extends CommandInterceptor> afterInterceptor)
   {
      invoker.addAfterInterceptor(i, afterInterceptor);
   }

   public List<CommandInterceptor> getInterceptorChain()
   {
      return invoker.asList();
   }

   public void removeInterceptor(int position)
   {
      invoker.removeInterceptor(position);
   }

   public void removeInterceptor(Class<? extends CommandInterceptor> interceptorType)
   {
      invoker.removeInterceptor(interceptorType);
   }

   public CacheLoaderManager getCacheLoaderManager()
   {
      return cacheLoaderManager;
   }

   public BuddyManager getBuddyManager()
   {
      return buddyManager;
   }

   public TransactionTable getTransactionTable()
   {
      return transactionTable;
   }

   public RPCManager getRPCManager()
   {
      return rpcManager;
   }

   public StateTransferManager getStateTransferManager()
   {
      return stateTransferManager;
   }

   public String getClusterName()
   {
      return configuration.getClusterName();
   }

   public int getNumberOfAttributes()
   {
      return dataContainer.getNumberOfAttributes();
   }

   public int getNumberOfNodes()
   {
      return dataContainer.getNumberOfNodes();
   }

   public RegionManager getRegionManager()
   {
      return regionManager;
   }

   public GlobalTransaction getCurrentTransaction(Transaction tx, boolean createIfNotExists)
   {
      return transactionTable.getCurrentTransaction(tx, createIfNotExists);
   }

   public GlobalTransaction getCurrentTransaction()
   {
      return transactionTable.getCurrentTransaction();
   }

   public Set<Fqn> getInternalFqns()
   {
      return dataContainer.getInternalFqns();
   }

   public int getNumberOfLocksHeld()
   {
      return dataContainer.getNumberOfLocksHeld();
   }

   public boolean exists(String fqn)
   {
      return exists(Fqn.fromString(fqn));
   }

   public boolean exists(Fqn fqn)
   {
      if (usingMvcc)
      {
         InvocationContext ctx = invocationContextContainer.get();
         cacheStatusCheck(ctx);
         ExistsCommand command = commandsFactory.buildExistsNodeCommand(fqn);
         return (Boolean) invoker.invoke(ctx, command);
      }
      else
      {
         return peek(fqn, false) != null;
      }
   }

   public Notifier getNotifier()
   {
      return notifier;
   }

   public Marshaller getMarshaller()
   {
      return marshaller;
   }

   public GravitateResult gravitateData(Fqn fqn, boolean searchBuddyBackupSubtrees, InvocationContext ctx)
   {
      cacheStatusCheck(ctx);
      GravitateDataCommand command = commandsFactory.buildGravitateDataCommand(fqn, searchBuddyBackupSubtrees);
      return (GravitateResult) invoker.invoke(ctx, command);
   }

   @SuppressWarnings("unchecked")
   public NodeSPI<K, V> peek(Fqn fqn, boolean includeDeletedNodes, boolean includeInvalidNodes)
   {
      // TODO: clean this up somehow!  Anyway, this method should NOT be used outside of testing frameworks.
      return (usingMvcc)
            ? mvccPeek(fqn)
            : (NodeSPI<K, V>) dataContainer.peek(fqn, includeDeletedNodes, includeInvalidNodes);
   }

   @SuppressWarnings("unchecked")
   public NodeSPI<K, V> peek(Fqn fqn, boolean includeDeletedNodes)
   {
      // TODO: clean this up somehow!  Anyway, this method should NOT be used outside of testing frameworks.
      return (usingMvcc)
            ? mvccPeek(fqn)
            : (NodeSPI<K, V>) dataContainer.peek(fqn, includeDeletedNodes);
   }

   @SuppressWarnings("unchecked")
   private NodeSPI<K, V> mvccPeek(Fqn f)
   {
      NodeSPI<K, V> n;
      try
      {
         n = mvccHelper.wrapNodeForReading(getInvocationContext(), f, false);
      }
      catch (InterruptedException e)
      {
         throw new CacheException(e);
      }
      if (n == null || n.isNullNode()) return null;
      return n;
   }

   public void addCacheListener(Object listener)
   {
      notifier.addCacheListener(listener);
   }

   public void removeCacheListener(Object listener)
   {
      notifier.removeCacheListener(listener);
   }

   public Set<Object> getCacheListeners()
   {
      return notifier.getCacheListeners();
   }

   public void create() throws CacheException
   {
      componentRegistry.create();
   }

   public void start() throws CacheException
   {
      componentRegistry.start();
   }

   public void stop()
   {
      componentRegistry.stop();
   }

   public void destroy()
   {
      reset();
      componentRegistry.destroy();
   }

   public CacheStatus getCacheStatus()
   {
      return componentRegistry.getState();
   }

   public InvocationContext getInvocationContext()
   {
      assertIsConstructed();
      return invocationContextContainer.get();
   }

   public void setInvocationContext(InvocationContext ctx)
   {
      assertIsConstructed();
      // assume a null ctx is meant to "un-set" the context?
      if (ctx == null)
      {
         invocationContextContainer.remove();
      }
      else
      {
         invocationContextContainer.set(ctx);
      }
   }

   public Address getLocalAddress()
   {
      if (rpcManager == null) return null;
      return rpcManager.getLocalAddress();
   }

   public List<Address> getMembers()
   {
      if (rpcManager == null) return null;
      return rpcManager.getMembers();
   }

   public String getVersion()
   {
      return Version.printVersion();
   }

   public void move(Fqn nodeToMove, Fqn newParent) throws NodeNotExistsException
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      MoveCommand command = commandsFactory.buildMoveCommand(nodeToMove, newParent);
      invoker.invoke(ctx, command);
   }

   public void move(String nodeToMove, String newParent) throws NodeNotExistsException
   {
      move(Fqn.fromString(nodeToMove), Fqn.fromString(newParent));
   }

   public boolean removeRegion(Fqn fqn)
   {
      return regionManager.removeRegion(fqn);
   }

   public Region getRegion(Fqn fqn, boolean createIfAbsent)
   {
      return regionManager.getRegion(fqn, createIfAbsent);
   }

   public void evict(Fqn fqn, boolean recursive)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      EvictCommand c = commandsFactory.buildEvictFqnCommand(fqn);
      c.setRecursive(recursive);
      invoker.invoke(ctx, c);
   }

   public void evict(Fqn fqn)
   {
      evict(fqn, false);
   }

   @SuppressWarnings("unchecked")
   public V get(Fqn fqn, K key)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GetKeyValueCommand command = commandsFactory.buildGetKeyValueCommand(fqn, key, true);
      return (V) invoker.invoke(ctx, command);
   }

   public V get(String fqn, K key)
   {
      return get(Fqn.fromString(fqn), key);
   }

   public boolean removeNode(Fqn fqn)
   {
      // special case if we are removing the root.  Remove all children instead.
      if (fqn.isRoot())
      {
         boolean result = true;
         // we need to preserve options
         InvocationContext ctx = getInvocationContext();
         Option o = ctx.getOptionOverrides();
         Set<Fqn> internalFqns = getInternalFqns();
         for (Object childName : peek(fqn, false, false).getChildrenNames())
         {
            if (!internalFqns.contains(Fqn.fromElements(childName)))
            {
               ctx.setOptionOverrides(o);
               result = removeNode(Fqn.fromRelativeElements(fqn, childName)) && result;
            }
         }
         return result;
      }
      else
      {
         InvocationContext ctx = invocationContextContainer.get();
         cacheStatusCheck(ctx);
         GlobalTransaction tx = transactionTable.getCurrentTransaction();
         RemoveNodeCommand command = commandsFactory.buildRemoveNodeCommand(tx, fqn);
         Object retval = invoker.invoke(ctx, command);
         return retval != null && (Boolean) retval;
      }
   }

   public boolean removeNode(String fqn)
   {
      return removeNode(Fqn.fromString(fqn));
   }

   @SuppressWarnings("unchecked")
   public NodeSPI<K, V> getNode(Fqn fqn)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GetNodeCommand command = commandsFactory.buildGetNodeCommand(fqn);
      return (NodeSPI<K, V>) invoker.invoke(ctx, command);
   }

   public NodeSPI<K, V> getNode(String fqn)
   {
      return getNode(Fqn.fromString(fqn));
   }

   @SuppressWarnings("unchecked")
   public V remove(Fqn fqn, K key) throws CacheException
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GlobalTransaction tx = transactionTable.getCurrentTransaction();
      RemoveKeyCommand command = commandsFactory.buildRemoveKeyCommand(tx, fqn, key);
      return (V) invoker.invoke(ctx, command);
   }

   public V remove(String fqn, K key)
   {
      return remove(Fqn.fromString(fqn), key);
   }

   public void put(Fqn fqn, Map<? extends K, ? extends V> data)
   {
      invokePut(fqn, data, false);
   }

   public void put(String fqn, Map<? extends K, ? extends V> data)
   {
      put(Fqn.fromString(fqn), data);
   }

   public void putForExternalRead(Fqn fqn, K key, V value)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      // if the node exists then this should be a no-op.
      if (peek(fqn, false, false) == null)
      {
         getInvocationContext().getOptionOverrides().setFailSilently(true);
         getInvocationContext().getOptionOverrides().setForceAsynchronous(true);
         PutForExternalReadCommand command = commandsFactory.buildPutForExternalReadCommand(null, fqn, key, value);
         invoker.invoke(ctx, command);
      }
      else
      {
         if (log.isDebugEnabled())
         {
            log.debug("putForExternalRead() called with Fqn " + fqn + " and this node already exists.  This method is hence a no op.");
         }
      }
   }

   @SuppressWarnings("unchecked")
   public V put(Fqn fqn, K key, V value)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GlobalTransaction tx = transactionTable.getCurrentTransaction();
      PutKeyValueCommand command = commandsFactory.buildPutKeyValueCommand(tx, fqn, key, value);
      return (V) invoker.invoke(ctx, command);
   }

   public V put(String fqn, K key, V value)
   {
      return put(Fqn.fromString(fqn), key, value);
   }


   @SuppressWarnings("unchecked")
   public Map<K, V> getData(Fqn fqn)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GetDataMapCommand command = commandsFactory.buildGetDataMapCommand(fqn);
      return (Map<K, V>) invoker.invoke(ctx, command);
   }

   public Set<K> getKeys(String fqn)
   {
      return getKeys(Fqn.fromString(fqn));
   }

   @SuppressWarnings("unchecked")
   public Set<K> getKeys(Fqn fqn)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GetKeysCommand command = commandsFactory.buildGetKeysCommand(fqn);
      return (Set<K>) invoker.invoke(ctx, command);
   }

   /**
    * Removes the keys and properties from a node.
    */
   public void clearData(String fqn) throws CacheException
   {
      clearData(Fqn.fromString(fqn));
   }

   /**
    * Removes the keys and properties from a named node.
    */
   public void clearData(Fqn fqn)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GlobalTransaction tx = getCurrentTransaction();
      invoker.invoke(ctx, commandsFactory.buildClearDataCommand(tx, fqn));
   }

   public void startBatch()
   {
      if (!configuration.isInvocationBatchingEnabled())
      {
         throw new ConfigurationException("Invocation batching not enabled in current configuration!  Please use the <invocationBatching /> element.");
      }
      batchContainer.startBatch();
   }

   public void endBatch(boolean successful)
   {
      if (!configuration.isInvocationBatchingEnabled())
      {
         throw new ConfigurationException("Invocation batching not enabled in current configuration!  Please use the <invocationBatching /> element.");
      }
      batchContainer.endBatch(successful);
   }

   public boolean isLeaf(String fqn) throws NodeNotExistsException
   {
      return isLeaf(Fqn.fromString(fqn));
   }

   public boolean isLeaf(Fqn fqn) throws NodeNotExistsException
   {
      Set<Object> names = getChildrenNamesInternal(fqn);
      if (names == null) throw new NodeNotExistsException("Node " + fqn + " does not exist!");
      return names.isEmpty();
   }


   public Set<Object> getChildrenNames(Fqn fqn)
   {
      Set<Object> names = getChildrenNamesInternal(fqn);
      return names == null ? Collections.emptySet() : names;
   }

   /**
    * Will return a null if the node doesnt exist!
    * @param fqn to check
    * @return set or null
    */
   @SuppressWarnings("unchecked")
   private Set<Object> getChildrenNamesInternal(Fqn fqn)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      GetChildrenNamesCommand command = commandsFactory.buildGetChildrenNamesCommand(fqn);
      Set<Object> retval = (Set<Object>) invoker.invoke(ctx, command);

      // this is needed to work around JBCACHE-1480
      if (retval != null && !retval.isEmpty())
      {
         for (Iterator i = retval.iterator(); i.hasNext();)
         {
            Object child = getNode(Fqn.fromRelativeElements(fqn, i.next()));
            if (child == null) i.remove();
         }
      }

      if (retval != null)
      {
         retval = Immutables.immutableSetWrap(retval); // this is already copied in the command
      }

      return retval;
   }

   @SuppressWarnings("unchecked")
   public Set<String> getChildrenNames(String fqn)
   {
      return (Set) getChildrenNames(Fqn.fromString(fqn));
   }

   public ComponentRegistry getComponentRegistry()
   {
      return componentRegistry;
   }

   public DataContainer getDataContainer()
   {
      return dataContainer;
   }

   protected void cacheStatusCheck(InvocationContext ctx)
   {
      assertIsConstructed();
      if (!ctx.getOptionOverrides().isSkipCacheStatusCheck() && !componentRegistry.invocationsAllowed(true))
      {
         throw new IllegalStateException("Cache not in STARTED state!");
      }
   }

   private void invokePut(Fqn fqn, Map<? extends K, ? extends V> data, boolean erase)
   {
      InvocationContext ctx = invocationContextContainer.get();
      cacheStatusCheck(ctx);
      PutDataMapCommand command = commandsFactory.buildPutDataMapCommand(null, fqn, data);
      command.setErase(erase);
      invoker.invoke(ctx, command);
   }


   // TODO: Add these to the public interface in 3.1.0.
   public void setData(Fqn fqn, Map<? extends K, ? extends V> data)
   {
      invokePut(fqn, data, true);
   }

   public void setData(String fqn, Map<? extends K, ? extends V> data)
   {
      setData(Fqn.fromString(fqn), data);
   }
}
