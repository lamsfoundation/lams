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
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.RPCManager;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.remote.StateTransferControlCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.marshall.NodeDataExceptionMarker;
import org.jboss.cache.marshall.NodeDataMarker;
import org.jboss.cache.notifications.event.NodeModifiedEvent;
import org.jboss.cache.transaction.TransactionLog;
import org.jboss.cache.transaction.TransactionLog.LogEntry;
import org.jgroups.Address;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DefaultStateTransferIntegrator implements StateTransferIntegrator
{

   private static final Log log = LogFactory.getLog(DefaultStateTransferIntegrator.class);
   private static final boolean trace = log.isTraceEnabled();

   private CacheSPI<?, ?> cache;

   private Set<Fqn> internalFqns;
   private Configuration cfg;
   private RPCManager rpcManager;
   private TransactionLog txLog;
   private boolean needToPersistState;   // for JBCACHE-131
   private boolean nonBlocking;
   private InvocationContextContainer container;
   private InterceptorChain chain;
   private ComponentRegistry registry;
   private CommandsFactory commandsFactory;

   @Inject
   public void inject(CacheSPI<?, ?> cache, Configuration cfg, RPCManager rpcManager, TransactionLog txLog,
                      InvocationContextContainer container, InterceptorChain chain, ComponentRegistry registry,
                      CommandsFactory commandsFactory)
   {
      this.cache = cache;
      this.cfg = cfg;
      this.rpcManager = rpcManager;
      this.nonBlocking = cfg.isNonBlockingStateTransfer();
      this.txLog = txLog;
      this.container = container;
      this.chain = chain;
      this.registry = registry;
      this.commandsFactory = commandsFactory;
   }

   @Start(priority = 14)
   public void start()
   {
      this.internalFqns = cache.getInternalFqns();
      needToPersistState = cfg.getCacheLoaderConfig() != null && !cfg.getCacheLoaderConfig().isFetchPersistentState() &&
            !cfg.getCacheLoaderConfig().isShared();
   }

   public void integrateState(ObjectInputStream ois, Object target, Fqn targetRoot, boolean integratePersistentState) throws Exception
   {
      // pop version from the stream first!
      short version = (Short) cache.getMarshaller().objectFromObjectStream(ois);
      log.info("Using version " + version);
      integrateTransientState(ois, (InternalNode) target);
      if (trace) log.trace("Reading marker for nonexistent associated state");
      cache.getMarshaller().objectFromObjectStream(ois);
      if (integratePersistentState)
      {
         integratePersistentState(ois, targetRoot);
      }

      // Delimiter
      verifyMarker(cache.getMarshaller().objectFromObjectStream(ois));

      if (nonBlocking)
         integrateTxLog(ois);
   }

   /**
    * Mimics a partial flush between the current instance and the address to flush, by opening and closing the necessary
    * latches on both ends.
    * @param addressToFlush address to flush in addition to the current address
    * @param block if true, mimics setting a flush.  Otherwise, mimics un-setting a flush.
    * @throws Exception if there are issues
    */
   private void mimicPartialFlushViaRPC(Address addressToFlush, boolean block) throws Exception
   {
      StateTransferControlCommand cmd = commandsFactory.buildStateTransferControlCommand(block);
      Vector<Address> recipient = new Vector<Address>();
      recipient.add(addressToFlush);
      if (!block) rpcManager.getFlushTracker().unblock();
      rpcManager.callRemoteMethods(recipient, cmd, true, cfg.getStateRetrievalTimeout(), true);
      if (block) rpcManager.getFlushTracker().block();
   }

   private void integrateTxLog(ObjectInputStream ois) throws Exception
   {
      if (trace)
         log.trace("Integrating transaction log");

      processCommitLog(ois);

      mimicPartialFlushViaRPC(rpcManager.getLastStateTransferSource(), true);

      try
      {
         if (trace)
            log.trace("Retrieving/Applying post-flush commits");
         processCommitLog(ois);

         if (trace)
            log.trace("Retrieving/Applying pending prepares");
         Object object = cache.getMarshaller().objectFromObjectStream(ois);
         while (object instanceof PrepareCommand)
         {
            PrepareCommand command = (PrepareCommand)object;
            if (! txLog.hasPendingPrepare(command))
            {
               InvocationContext ctx = container.get();
               ctx.setOriginLocal(false);
               ctx.getOptionOverrides().setCacheModeLocal(true);
               ctx.getOptionOverrides().setSkipCacheStatusCheck(true);
               chain.invoke(ctx, command);
            }
            object = cache.getMarshaller().objectFromObjectStream(ois);
         }
         verifyMarker(object);

         // Block all remote commands once transfer is complete,
         // and before FLUSH completes
         registry.setStatusCheckNecessary(true);
      }
      finally
      {
         if (trace) log.trace("Stopping partial flush");
//         channel.stopFlush(targets);
         mimicPartialFlushViaRPC(rpcManager.getLastStateTransferSource(), false);
      }
   }

   private void processCommitLog(ObjectInputStream ois) throws Exception
   {
      Object object = cache.getMarshaller().objectFromObjectStream(ois);
      while (object instanceof LogEntry)
      {
         List<WriteCommand> mods = ((LogEntry)object).getModifications();
         if (trace) log.trace("Mods = " + mods);
         for (WriteCommand mod : mods)
         {
            InvocationContext ctx = container.get();
            ctx.setOriginLocal(false);
            ctx.getOptionOverrides().setCacheModeLocal(true);
            ctx.getOptionOverrides().setSkipCacheStatusCheck(true);
            chain.invoke(ctx, mod);
         }

         object = cache.getMarshaller().objectFromObjectStream(ois);
      }
      verifyMarker(object);
   }

   private void verifyMarker(Object object)
   {
      if (object instanceof NodeDataExceptionMarker)
      {
         NodeDataExceptionMarker e = (NodeDataExceptionMarker)object;
         throw new CacheException("Error in state transfer stream", e.getCause());
      }
      else if (! (object instanceof NodeDataMarker))
      {
         throw new CacheException("Invalid object unmarshalled");
      }
   }

   protected void integrateTransientState(ObjectInputStream in, InternalNode target) throws Exception
   {
      boolean transientSet = false;
      try
      {
         if (trace)
         {
            log.trace("integrating transient state for " + target);
         }

         integrateTransientState(target.getFqn(), in);

         transientSet = true;

         if (trace)
         {
            log.trace("transient state successfully integrated");
         }

         notifyAllNodesCreated(cache.getInvocationContext(), target);
      }
      catch (CacheException ce)
      {
         throw ce;
      }
      catch (Exception e)
      {
         throw new CacheException(e);
      }
      finally
      {
         if (!transientSet)
         {
            target.clear();
            target.removeChildren();
         }
      }
   }

   protected void integratePersistentState(ObjectInputStream in, Fqn targetFqn) throws Exception
   {
      CacheLoaderManager loaderManager = cache.getCacheLoaderManager();
      CacheLoader loader = loaderManager == null ? null : loaderManager.getCacheLoader();
      if (loader == null)
      {
         if (trace)
         {
            log.trace("cache loader is null, will not attempt to integrate persistent state");
         }
      }
      else
      {

         if (trace)
         {
            log.trace("integrating persistent state using " + loader.getClass().getName());
         }

         boolean persistentSet = false;
         try
         {
            if (targetFqn.isRoot())
            {
               loader.storeEntireState(in);
            }
            else
            {
               loader.storeState(targetFqn, in);
            }
            persistentSet = true;
         }
         catch (ClassCastException cce)
         {
            log.error("Failed integrating persistent state. One of cacheloaders is not"
                  + " adhering to state stream format. See JBCACHE-738.");
            throw cce;
         }
         finally
         {
            if (!persistentSet)
            {
               log.warn("persistent state integration failed, removing all nodes from loader");
               loader.remove(targetFqn);
            }
            else
            {
               if (trace)
               {
                  log.trace("persistent state integrated successfully");
               }
            }
         }
      }
   }

   /**
    * Generates NodeAdded notifications for all nodes of the tree. This is
    * called whenever the tree is initially retrieved (state transfer)
    */
   private void notifyAllNodesCreated(InvocationContext ctx, InternalNode curr)
   {
      if (curr == null) return;
      ctx.setOriginLocal(false);
      cache.getNotifier().notifyNodeCreated(curr.getFqn(), true, ctx);
      cache.getNotifier().notifyNodeCreated(curr.getFqn(), false, ctx);
      // AND notify that they have been modified!!
      if (!curr.getKeys().isEmpty())
      {
         cache.getNotifier().notifyNodeModified(curr.getFqn(), true, NodeModifiedEvent.ModificationType.PUT_MAP, Collections.emptyMap(), ctx);
         cache.getNotifier().notifyNodeModified(curr.getFqn(), false, NodeModifiedEvent.ModificationType.PUT_MAP, curr.getData(), ctx);
      }
      ctx.setOriginLocal(true);

      Set<InternalNode> children = curr.getChildren();
      for (InternalNode n : children) notifyAllNodesCreated(ctx, n);
   }

   private void prepareContextOptions()
   {
      cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
      cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
      cache.getInvocationContext().getOptionOverrides().setSuppressPersistence(!needToPersistState);
   }

   private void integrateTransientState(Fqn target, ObjectInputStream in) throws Exception
   {
      prepareContextOptions();
      NodeSPI targetNode = cache.getNode(target);
      for (Object childname : targetNode.getChildrenNames())
      {
         prepareContextOptions();
         targetNode.removeChild(childname);
      }

      // set these flags to false if we have persistent state!
      targetNode.setDataLoaded(false);
      targetNode.setChildrenLoaded(false);

      List<NodeData> list = readNodesAsList(in);
      if (list != null)
      {
         // if the list was null we read an EOF marker!!  So don't bother popping it off the stack later.
         Iterator<NodeData> nodeDataIterator = list.iterator();

         // Read the first NodeData and integrate into our target
         if (nodeDataIterator.hasNext())
         {
            NodeData nd = nodeDataIterator.next();

            //are there any transient nodes at all?
            if (nd != null && !nd.isMarker())
            {
               // with MVCC these calls should ALWAYS go up the interceptor chain since no other locking
               // takes place elsewhere.
               prepareContextOptions();
               cache.clearData(target);
               prepareContextOptions();
               cache.put(target, nd.getAttributes());

               // Check whether this is an integration into the buddy backup
               // subtree
               Fqn tferFqn = nd.getFqn();
               boolean move = target.isChildOrEquals(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN)
                     && !tferFqn.isChildOrEquals(target);
               // If it is an integration, calculate how many levels of offset
               int offset = move ? target.size() - tferFqn.size() : 0;

               integrateStateTransferChildren(target, offset, nodeDataIterator);

               integrateRetainedNodes(target);
            }
         }

         // read marker off stack
//         cache.getMarshaller().objectFromObjectStream(in);
      }
   }

   @SuppressWarnings("unchecked")
   private List<NodeData> readNodesAsList(ObjectInputStream in) throws Exception
   {
      Object obj = cache.getMarshaller().objectFromObjectStream(in);
      if (obj instanceof NodeDataExceptionMarker)
      {
         Throwable cause = ((NodeDataExceptionMarker)obj).getCause();
         if (cause instanceof Exception)
            throw (Exception) cause;

         throw new CacheException(cause);
      }
      if (obj instanceof NodeDataMarker) return null;

      return (List<NodeData>) obj;
   }

   private NodeData integrateStateTransferChildren(Fqn parentFqn, int offset, Iterator<NodeData> nodeDataIterator)
         throws IOException, ClassNotFoundException
   {
      int parentLevel = parentFqn.size();
      int targetLevel = parentLevel + 1;
      Fqn fqn;
      int size;
      NodeData nd = nodeDataIterator.hasNext() ? nodeDataIterator.next() : null;
      while (nd != null && !nd.isMarker())
      {
         fqn = nd.getFqn();
         // If we need to integrate into the buddy backup subtree,
         // change the Fqn to fit under it
         if (offset > 0)
         {
            fqn = Fqn.fromRelativeFqn(parentFqn.getAncestor(offset), fqn);
         }
         size = fqn.size();
         if (size <= parentLevel)
         {
            return nd;
         }
         else if (size > targetLevel)
         {
            throw new IllegalStateException("NodeData " + fqn + " is not a direct child of " + parentFqn);
         }

         Map attrs = nd.getAttributes();

         prepareContextOptions();
         cache.clearData(fqn);
         prepareContextOptions();
         cache.put(fqn, attrs);
         cache.getNode(fqn).setDataLoaded(false);
         cache.getNode(fqn).setChildrenLoaded(false);

         // Recursively call, which will walk down the tree
         // and return the next NodeData that's a child of our parent
         nd = integrateStateTransferChildren(fqn, offset, nodeDataIterator);
      }
      if (nd != null && nd.isExceptionMarker())
      {
         NodeDataExceptionMarker ndem = (NodeDataExceptionMarker) nd;
         throw new CacheException("State provider node " + ndem.getCacheNodeIdentity()
               + " threw exception during loadState", ndem.getCause());
      }
      return null;
   }

   private Set<Fqn> retainInternalNodes(Fqn target)
   {
      Set<Fqn> result = new HashSet<Fqn>();
      for (Fqn internalFqn : internalFqns)
      {
         if (internalFqn.isChildOf(target))
         {
            prepareContextOptions();
            Node node = getInternalNode(cache.getNode(target), internalFqn);
            if (node != null)
            {
               result.add(node.getFqn());
            }
         }
      }

      return result;
   }

   private Node getInternalNode(Node parentNode, Fqn internalFqn)
   {
      Fqn parentFqn = parentNode.getFqn();
      Object name = internalFqn.get(parentFqn.size());
      prepareContextOptions();
      Node result = parentNode.getChild(name);
      if (result != null && internalFqn.size() < result.getFqn().size())
      {
         // need to recursively walk down the tree
         result = getInternalNode(result, internalFqn);
      }

      return result;
   }

   private void integrateRetainedNodes(Fqn target)
   {
      Set<Fqn> retainedNodes = retainInternalNodes(target);
      for (Fqn retained : retainedNodes)
      {
         if (retained.isChildOf(target))
         {
            integrateRetainedNode(target, retained);
         }
      }
   }


   // TODO: What is this rubbish?!??
   private void integrateRetainedNode(Fqn ancFqn, Fqn descFqn)
   {
      prepareContextOptions();
      InternalNode ancestor = cache.getNode(ancFqn).getDelegationTarget();
      Object name = descFqn.get(ancFqn.size());
      InternalNode child = ancestor.getChild(name);
      if (ancFqn.size() == descFqn.size() + 1)
      {
         if (child == null)
         {
            prepareContextOptions();
            InternalNode descendant = cache.getNode(descFqn).getDelegationTarget();
            prepareContextOptions();
            ancestor.addChild(name, descendant);
         }
         else
         {
            log.warn("Received unexpected internal node " + descFqn + " in transferred state");
         }
      }
      else
      {
         if (child == null)
         {
            // Missing level -- have to create empty node
            // This shouldn't really happen -- internal fqns should
            // be immediately under the root
//            child = factory.createInternalNode(name, ancestor);
//            ancestor.addChild(name, child);

            // since this shouldn't happen, deal with it. - Manik - Jul08
            throw new NullPointerException("Child is null");
         }

         // Keep walking down the tree
         integrateRetainedNode(child.getFqn(), descFqn);
      }
   }
}
