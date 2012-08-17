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
package org.jboss.cache.statetransfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.Node;
import org.jboss.cache.RPCManager;
import org.jboss.cache.RPCManagerImpl.FlushTracker;
import org.jboss.cache.Version;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.marshall.NodeDataExceptionMarker;
import org.jboss.cache.transaction.TransactionLog;

import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultStateTransferGenerator implements StateTransferGenerator
{

   public static final short STATE_TRANSFER_VERSION = Version.getVersionShort("2.0.0.GA");

   private static final Log log = LogFactory.getLog(DefaultStateTransferGenerator.class);
   private static final boolean trace = log.isTraceEnabled();

   private CacheSPI cache;
   private RPCManager rpcManager;

   private Set<Fqn> internalFqns;

   private boolean nonBlocking;
   private long flushTimeout;
   private int maxNonProgressingLogWrites = 5;
   private TransactionLog txLog;



   @Inject
   public void inject(CacheSPI cache, RPCManager rpcManager, Configuration configuration, TransactionLog txLog)
   {
      this.cache = cache;
      this.nonBlocking = true;

      this.flushTimeout = configuration.getStateRetrievalTimeout();
      this.nonBlocking = configuration.isNonBlockingStateTransfer();
      this.txLog = txLog;
      this.rpcManager = rpcManager;
   }

   @Start(priority = 14)
   void start()
   {
      this.internalFqns = cache.getInternalFqns();
   }

   public void generateState(ObjectOutputStream out, Object rootNode, boolean generateTransient,
                             boolean generatePersistent, boolean suppressErrors) throws Exception
   {
      Fqn fqn = getFqn(rootNode);
      boolean activated = false;
      CacheLoader cacheLoader = cache.getCacheLoaderManager() == null ? null : cache.getCacheLoaderManager().getCacheLoader();
      boolean needToGeneratePersistentState = generatePersistent && cacheLoader != null;
      try
      {
         cache.getMarshaller().objectToObjectStream(STATE_TRANSFER_VERSION, out);

         // activate the tx log only if we need to generate either transient or persistent state
         if (nonBlocking && (generateTransient || needToGeneratePersistentState))
         {
            activated = txLog.activate();
            if (! activated) throw new StateProviderBusyException("Busy performing state transfer for someone else");
            if (trace) log.trace("Transaction log activated!");
         }

         if (generateTransient)
         {
            //transient + marker
            if (trace) log.trace("writing transient state for " + fqn);
            marshallTransientState((InternalNode) rootNode, out);

            if (trace) log.trace("transient state succesfully written");

            //associated + marker
            if (trace) log.trace("writing associated state");

            delimitStream(out);

            if (trace) log.trace("associated state succesfully written");
         }
         else
         {
            //we have to write two markers for transient and associated
            delimitStream(out);
            delimitStream(out);
         }

         if (needToGeneratePersistentState) writePersistentData(out, fqn, cacheLoader);

         delimitStream(out);

         if (nonBlocking && generateTransient)
         {
            writeTxLog(out);
         }

      }
      catch (Exception e)
      {
         cache.getMarshaller().objectToObjectStream(new NodeDataExceptionMarker(e, cache.getLocalAddress()), out);
         throw e;
      }
      finally
      {
         if (activated)
            txLog.deactivate();
      }
   }

   private void writePersistentData(ObjectOutputStream out, Fqn fqn, CacheLoader cacheLoader) throws Exception
   {
      if (trace) log.trace("writing persistent state for " + fqn + ", using " + cache.getCacheLoaderManager().getCacheLoader().getClass());

      if (fqn.isRoot())
      {
         cacheLoader.loadEntireState(out);
      }
      else
      {
         cacheLoader.loadState(fqn, out);
      }

      if (trace) log.trace("persistent state succesfully written");
   }

   private void writeTxLog(ObjectOutputStream out) throws Exception
   {
      FlushTracker flushTracker = rpcManager.getFlushTracker();

      try
      {
         if (trace) log.trace("Transaction log size is " + txLog.size());
         for (int nonProgress = 0, size = txLog.size(); size > 0;)
         {
            if (trace) log.trace("Tx Log remaining entries = " + size);
            txLog.writeCommitLog(cache.getMarshaller(), out);
            int newSize = txLog.size();

            // If size did not decrease then we did not make progress, and could be wasting
            // our time. Limit this to the specified max.
            if (newSize >= size && ++nonProgress >= maxNonProgressingLogWrites)
               break;

            size = newSize;
         }

         // Wait on incoming and outgoing threads to line-up in front of
         // the flush gate.
         flushTracker.lockSuspendProcessingLock();

         // Signal to sender that we need a flush to get a consistent view
         // of the remaining transactions.
         delimitStream(out);
         out.flush();
         if (trace) log.trace("Waiting for a FLUSH");
         flushTracker.waitForFlushStart(flushTimeout);
         if (trace) log.trace("FLUSH received, proceeding with writing commit log");
         // Write remaining transactions
         txLog.writeCommitLog(cache.getMarshaller(), out);
         delimitStream(out);

         // Write all non-completed prepares
         txLog.writePendingPrepares(cache.getMarshaller(), out);
         delimitStream(out);
         out.flush();
      }
      finally
      {
         flushTracker.unlockSuspendProcessingLock();
      }
   }

   private Fqn getFqn(Object o)
   {
      if (o instanceof Node) return ((Node) o).getFqn();
      if (o instanceof InternalNode) return ((InternalNode) o).getFqn();
      throw new IllegalArgumentException();
   }

   /**
    * Places a delimiter marker on the stream
    *
    * @param out stream
    * @throws java.io.IOException if there are errs
    */
   protected void delimitStream(ObjectOutputStream out) throws Exception
   {
      cache.getMarshaller().objectToObjectStream(DefaultStateTransferManager.STREAMING_DELIMITER_NODE, out);
   }

   /**
    * Do a preorder traversal: visit the node first, then the node's children
    *
    * @param out
    * @throws Exception
    */
   protected void marshallTransientState(InternalNode node, ObjectOutputStream out) throws Exception
   {
      List<NodeData> nodeData = new LinkedList<NodeData>();
      generateNodeDataList(node, nodeData);
      cache.getMarshaller().objectToObjectStream(nodeData, out, node.getFqn());
   }

   protected void generateNodeDataList(InternalNode<?, ?> node, List<NodeData> list) throws Exception
   {
      if (internalFqns.contains(node.getFqn()))
      {
         return;
      }

      Map attrs;
      NodeData nd;

      // first handle the current node
      attrs = node.getInternalState(false);

      if (attrs.size() == 0)
      {
         nd = new NodeData(node.getFqn());
      }
      else
      {
         nd = new NodeData(node.getFqn(), attrs, true);
      }

      list.add(nd);

      // then visit the children
      for (InternalNode child : node.getChildren()) generateNodeDataList(child, list);
   }
}
