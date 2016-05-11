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
package org.jboss.cache.interceptors;

import org.jboss.cache.CacheException;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.base.JmxStatsCommandInterceptor;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.mvcc.MVCCNodeHelper;
import org.jboss.cache.mvcc.NullMarkerNode;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.transaction.TransactionTable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Loads nodes that don't exist at the time of the call into memory from the CacheLoader
 *
 * @author Bela Ban
 *
 */
public class CacheLoaderInterceptor extends JmxStatsCommandInterceptor
{
   private long cacheLoads = 0;
   private long cacheMisses = 0;
   private CacheLoaderManager clm;

   protected TransactionTable txTable = null;
   protected CacheLoader loader;
   protected DataContainer dataContainer;
   protected Notifier notifier;

   protected boolean isActivation = false;
//   protected boolean usingVersionedInvalidation = false;

   protected MVCCNodeHelper helper;


   /**
    * True if CacheStoreInterceptor is in place.
    * This allows us to skip loading keys for remove(Fqn, key) and put(Fqn, key).
    * It also affects removal of node data and listing children.
    */
   protected boolean useCacheStore = true;

   @Inject
   protected void injectDependencies(TransactionTable txTable, CacheLoaderManager clm, Configuration configuration,
                                     DataContainer dataContainer, Notifier notifier, MVCCNodeHelper helper)
   {
      this.txTable = txTable;
      this.clm = clm;
//      CacheMode mode = configuration.getCacheMode();
//      usingVersionedInvalidation = mode.isInvalidation();
      this.dataContainer = dataContainer;
      this.notifier = notifier;
      this.helper = helper;
   }

   @Start
   protected void startInterceptor()
   {
      loader = clm.getCacheLoader();
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         if (command.isErase())
         {
            replace(ctx, command.getFqn());
         }
         else
         {
            loadIfNeeded(ctx, command.getFqn(), null, true, true, false, false, false, false, true);
         }
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         loadIfNeeded(ctx, command.getFqn(), command.getKey(), false, useCacheStore, !useCacheStore, false, false, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return visitPutKeyValueCommand(ctx, command);
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         if (command.getTo() != null)
         {
            loadIfNeeded(ctx, command.getTo(), null, false, false, true, false, true, false, true);
         }
         loadIfNeeded(ctx, command.getFqn(), null, false, false, true, true, true, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         loadIfNeeded(ctx, command.getFqn(), command.getKey(), false, false, true, false, false, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }


   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         loadIfNeeded(ctx, command.getFqn(), null, false, false, true, false, false, true, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      Fqn fqn = command.getFqn();
      if (fqn != null)
      {
         loadIfNeeded(ctx, fqn, null, false, false, false, false, false, true, true);
         loadChildren(fqn, dataContainer.peekInternalNode(fqn, true), false, false, ctx);
      }
      return invokeNextInterceptor(ctx, command);
   }


   @Override
   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         loadIfNeeded(ctx, command.getFqn(), null, true, false, true, false, false, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      if (command.getFqn() != null)
      {
         loadIfNeeded(ctx, command.getFqn(), null, true, false, true, false, false, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      // clean up nodesCreated map
      if (trace) log.trace("Removing temporarily created nodes");

      // this needs to be done in reverse order.
      List list = ctx.getTransactionContext().getDummyNodesCreatedByCacheLoader();
      if (list != null && list.size() > 0)
      {
         ListIterator i = list.listIterator(list.size());
         while (i.hasPrevious())
         {
            Fqn fqn = (Fqn) i.previous();
            try
            {
               dataContainer.evict(fqn, false);
            }
            catch (CacheException e)
            {
               if (trace) log.trace("Unable to evict node " + fqn, e);
            }
         }
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      if (configuration.getNodeLockingScheme() == NodeLockingScheme.OPTIMISTIC && command.getFqn() != null)
      {
         loadIfNeeded(ctx, command.getFqn(), null, false, false, false, false, false, false, false);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      if (command.getFqn() != null && !useCacheStore)
      {
         loadIfNeeded(ctx, command.getFqn(), command.getKey(), false, false, false, false, false, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      Fqn fqn = command.getFqn();
      if (fqn != null && !useCacheStore)
      {
         loadIfNeeded(ctx, fqn, null, false, true, false, false, false, false, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   private void replace(InvocationContext ctx, Fqn fqn) throws InterruptedException
   {
      NodeSPI n = helper.wrapNodeForReading(ctx, fqn, true);
      if (n instanceof NullMarkerNode)
      {
         ctx.getLookedUpNodes().remove(fqn);
      }
      n = helper.wrapNodeForWriting(ctx, fqn, true, true, true, false, false);
      n.setDataLoaded(true);
   }

   private void loadIfNeeded(InvocationContext ctx, Fqn fqn, Object key, boolean allKeys, boolean initNode, boolean acquireWriteLock, boolean recursive, boolean isMove, boolean bypassLoadingData, boolean shouldLoadIfNodeIsNull) throws Throwable
   {
      NodeSPI n = helper.wrapNodeForReading(ctx, fqn, true);
      if (n instanceof NullMarkerNode)
      {
         ctx.getLookedUpNodes().remove(fqn);
         n = null;
      }
      boolean mustLoad = mustLoad(fqn, n, key, allKeys || isMove, shouldLoadIfNodeIsNull);

      if (trace) log.trace("load element " + fqn + " mustLoad=" + mustLoad);

      if (mustLoad)
      {
         if (acquireWriteLock || initNode)
         {
            boolean isNew = n == null;
            n = helper.wrapNodeForWriting(ctx, fqn, true, false, true, false, true); // won't create any nodes but will acquire locks.
            if (isNew && n != null) n.setDataLoaded(false);
         }

         // This is really convoluted
         if (n == null || !n.isDeleted())
         {
            boolean exists;
            Map nodeData = null;
            if (bypassLoadingData)
            {
               exists = loader.exists(fqn);
            }
            else
            {
               nodeData = loadData(ctx, fqn);
               exists = nodeData != null;
            }
            if (n == null && exists)
            {
               // just create a dummy node in memory
               n = helper.wrapNodeForWriting(ctx, fqn, true, true, true, false, false);
               n.setDataLoaded(false);
            }
            if (nodeData != null && n != null)
            {
               setNodeState(ctx, fqn, n, nodeData);
            }
         }
      }

      // The complete list of children aren't known without loading them
      if (recursive && n != null)
      {
         loadChildren(fqn, n.getDelegationTarget(), recursive, isMove, ctx);
      }
   }

   /**
    * Load the children.
    *
    * @param node may be null if the node was not found.
    * @param ctxt
    */
   private void loadChildren(Fqn fqn, InternalNode node, boolean recursive, boolean isMove, InvocationContext ctxt) throws Throwable
   {

      if (node != null && node.isChildrenLoaded())
      {
         if (trace) log.trace("Children already loaded!");
         return;
      }
      Set childrenNames;
      try
      {
         childrenNames = loader.getChildrenNames(fqn);
      }
      catch (Exception e)
      {
         if (log.isInfoEnabled()) log.info("Cache loader was unable to load state", e);
         // return!
         return;
      }

      if (trace)
      {
         log.trace("load children " + fqn + " children=" + childrenNames);
      }

      // For getChildrenNames null means no children
      if (childrenNames == null)
      {
         if (node != null)
         {
            if (useCacheStore)
            {
               node.removeChildren();
            }
            node.setChildrenLoaded(true);
         }
         return;
      }

      // Create if node had not been created already
      if (node == null)
      {
         NodeSPI temp = helper.wrapNodeForWriting(ctxt, fqn, true, true, true, false, false);
         node = temp.getDelegationTarget();
      }

      // Create one DataNode per child, mark as UNINITIALIZED
      for (Object name : childrenNames)
      {
         Fqn childFqn = Fqn.fromRelativeElements(fqn, name);

         // create child if it didn't exist
         NodeSPI child = helper.wrapNodeForWriting(ctxt, childFqn, true, true, true, false, false);
         if (child.isCreated()) child.setDataLoaded(false);
         if ((isMove || isActivation) && recursive)
         {
            // load data for children as well!
            child.setInternalState(loadData(ctxt, child.getFqn()));
            child.setDataLoaded(true);
         }

         if (recursive)
         {
            loadChildren(child.getFqn(), child.getDelegationTarget(), true, isMove, ctxt);
         }
      }
      node.setChildrenLoaded(true);
   }

   private boolean mustLoad(Fqn fqn, NodeSPI n, Object key, boolean allKeys, boolean shouldLoadIfNodeIsNull)
   {
      if (n == null)
      {
         if (trace) log.trace("Node [" + fqn + "] is null in memory.  Must load? " + shouldLoadIfNodeIsNull);
         return shouldLoadIfNodeIsNull;
      }

      // check this first!!!
      if (!n.isValid()) // && configuration.getNodeLockingScheme().isVersionedScheme())
      {
         // attempt to load again; this only happens if we have tombstones lying around, or we are using invalidation.
         if (trace)
         {
            log.trace("Loading node [" + fqn + "] again from cache loader since in-memory node is marked as invalid");
         }
         return true;
      }

      // JBCACHE-1172 Skip single-key optimization if request needs all keys
      if (!allKeys)
      {
         // if we are not looking for a specific key don't bother loading!
         if (key == null)
         {
            if (trace) log.trace("Don't load [" + fqn + "], key requested is null");
            return false;
         }
         
         if (n.containsKeyDirect(key))
         {
            if (trace) log.trace("Don't load [" + fqn + "], already have necessary key in memory");
            return false;
         }
      }
      if (!n.isDataLoaded())
      {
         if (trace) log.trace("Must load node [" + fqn + "], uninitialized");
         return true;
      }

      if (trace) log.trace("Don't load node [" + fqn + "], by default");
      return false;
   }

   /**
    * Loads a node from disk; if it exists creates parent TreeNodes.
    * If it doesn't exist on disk but in memory, clears the
    * uninitialized flag, otherwise returns null.
    */
   private void setNodeState(InvocationContext ctx, Fqn fqn, NodeSPI n, Map nodeData) throws Exception
   {
      if (trace) log.trace("setNodeState node is " + n);
      if (nodeData != null)
      {
         notifier.notifyNodeLoaded(fqn, true, Collections.emptyMap(), ctx);
         if (isActivation)
         {
            notifier.notifyNodeActivated(fqn, true, Collections.emptyMap(), ctx);
         }

         n.setInternalState(nodeData);

         // set this node as valid?
         //         if (usingVersionedInvalidation) n.setValid(true, false);
         n.setValid(true, false);

         notifier.notifyNodeLoaded(fqn, false, nodeData, ctx);
         if (isActivation)
         {
            notifier.notifyNodeActivated(fqn, false, nodeData, ctx);
         }
      }
      if (!n.isDataLoaded())
      {
         if (trace) log.trace("Setting dataLoaded to true");
         n.setDataLoaded(true);
      }
   }

   private Map loadData(InvocationContext ctx, Fqn fqn) throws Exception
   {
      if (trace) log.trace("Attempting to load data for " + fqn);
      Map nodeData = loader.get(fqn);
      boolean nodeExists = (nodeData != null);
      if (trace) log.trace("Node " + fqn + " exists? " + nodeExists);
      if (nodeExists) recordNodeLoaded(ctx, fqn);
      if (getStatisticsEnabled())
      {
         if (nodeExists)
         {
            cacheLoads++;
         }
         else
         {
            cacheMisses++;
         }
      }
      return nodeData;
   }

   @ManagedAttribute(description = "number of cache loader node loads")
   public long getCacheLoaderLoads()
   {
      return cacheLoads;
   }

   @ManagedAttribute(description = "number of cache loader node misses")
   public long getCacheLoaderMisses()
   {
      return cacheMisses;
   }

   @ManagedOperation
   @Override
   public void resetStatistics()
   {
      cacheLoads = 0;
      cacheMisses = 0;
   }

   @ManagedOperation
   @Override
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = new HashMap<String, Object>();
      retval.put("CacheLoaderLoads", cacheLoads);
      retval.put("CacheLoaderMisses", cacheMisses);
      return retval;
   }

   protected void recordNodeLoaded(InvocationContext ctx, Fqn fqn)
   {
      // this is a no-op.  Only used by subclasses.
   }
}
