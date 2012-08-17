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
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Node;
import org.jboss.cache.NodeFactory;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.Region;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.eviction.EvictionEvent;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.marshall.NodeDataExceptionMarker;
import org.jboss.cache.marshall.NodeDataMarker;
import org.jboss.cache.notifications.event.NodeModifiedEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated
public class LegacyStateTransferIntegrator implements StateTransferIntegrator
{

   private static final Log log = LogFactory.getLog(LegacyStateTransferIntegrator.class);
   private static final boolean trace = log.isTraceEnabled();
   private CacheSPI cache;
   private NodeFactory factory;
   private Set<Fqn> internalFqns;
   private Configuration cfg;
   private boolean usePut;    // for JBCACHE-131

   @Inject
   public void inject(CacheSPI<?, ?> cache, NodeFactory nodefactory, Configuration cfg)
   {
      this.cache = cache;
      this.factory = nodefactory;
      this.cfg = cfg;
   }

   @Start(priority = 14)
   public void start()
   {
      this.internalFqns = cache.getInternalFqns();
      usePut = cfg.getCacheLoaderConfig() != null && !cfg.getCacheLoaderConfig().isFetchPersistentState() &&
            !cfg.getCacheLoaderConfig().isShared();
   }

   public void integrateState(ObjectInputStream ois, Object target, Fqn targetFqn, boolean integratePersistentState) throws Exception
   {
      // pop version from the stream first!
      short version = (Short) cache.getMarshaller().objectFromObjectStream(ois);
      log.info("Using version " + version);
      integrateTransientState(ois, (NodeSPI) target);
      // read another marker for the dummy associated state
      if (trace) log.trace("Reading marker for nonexistent associated state");
      cache.getMarshaller().objectFromObjectStream(ois);
      if (integratePersistentState) integratePersistentState(ois, targetFqn);
   }

   protected void integrateTransientState(ObjectInputStream in, NodeSPI target) throws Exception
   {
      boolean transientSet = false;
      try
      {
         if (log.isTraceEnabled())
         {
            log.trace("integrating transient state for " + target);
         }

         integrateTransientState(target, in);

         transientSet = true;

         if (log.isTraceEnabled())
         {
            log.trace("transient state successfully integrated");
         }

         notifyAllNodesCreated(cache.getInvocationContext(), target);
      }
      catch (Exception e)
      {
         throw new CacheException(e);
      }
      finally
      {
         if (!transientSet)
         {
            target.clearDataDirect();
            target.removeChildrenDirect();
         }
      }
   }

   protected void integratePersistentState(ObjectInputStream in, Fqn targetFqn) throws Exception
   {
      if (trace) log.trace("Reading persistent state from stream");
      CacheLoaderManager loaderManager = cache.getCacheLoaderManager();
      CacheLoader loader = loaderManager == null ? null : loaderManager.getCacheLoader();
      if (loader == null)
      {
         if (log.isTraceEnabled())
         {
            log.trace("cache loader is null, will not attempt to integrate persistent state");
         }
      }
      else
      {

         if (log.isTraceEnabled())
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
               if (log.isTraceEnabled())
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
   private void notifyAllNodesCreated(InvocationContext ctx, NodeSPI curr)
   {
      if (curr == null) return;
      ctx.setOriginLocal(false);
      cache.getNotifier().notifyNodeCreated(curr.getFqn(), true, ctx);
      cache.getNotifier().notifyNodeCreated(curr.getFqn(), false, ctx);
      // AND notify that they have been modified!!
      if (!curr.getKeysDirect().isEmpty())
      {
         cache.getNotifier().notifyNodeModified(curr.getFqn(), true, NodeModifiedEvent.ModificationType.PUT_MAP, Collections.emptyMap(), ctx);
         cache.getNotifier().notifyNodeModified(curr.getFqn(), false, NodeModifiedEvent.ModificationType.PUT_MAP, curr.getDataDirect(), ctx);
      }
      ctx.setOriginLocal(true);

      Set<NodeSPI> children = curr.getChildrenDirect();
      for (NodeSPI n : children)
      {
         notifyAllNodesCreated(ctx, n);
      }
   }

   private void integrateTransientState(NodeSPI target, ObjectInputStream in) throws Exception
   {
      if (trace) log.trace("Reading transient state from stream");
      target.removeChildrenDirect();

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
               Map attributes = nd.getAttributes();
               if (usePut)
               {
                  cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
                  cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
                  target.clearData();
                  cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
                  cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
                  target.putAll(attributes);
               }
               else
               {
                  target.setInternalState(attributes);
               }

               // Check whether this is an integration into the buddy backup
               // subtree
               Fqn tferFqn = nd.getFqn();
               Fqn tgtFqn = target.getFqn();
               boolean move = tgtFqn.isChildOrEquals(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN)
                     && !tferFqn.isChildOrEquals(tgtFqn);
               // If it is an integration, calculate how many levels of offset
               int offset = move ? tgtFqn.size() - tferFqn.size() : 0;

               integrateStateTransferChildren(target, offset, nodeDataIterator);

               integrateRetainedNodes(target);
            }
         }

         // set these flags to false if we have persistent state!
         target.setDataLoaded(false);
         target.setChildrenLoaded(false);


         // read marker off stack
         if (trace) log.trace("Reading marker from stream");
         cache.getMarshaller().objectFromObjectStream(in);
      }
   }

   @SuppressWarnings("unchecked")
   private List<NodeData> readNodesAsList(ObjectInputStream in) throws Exception
   {
      Object obj = cache.getMarshaller().objectFromObjectStream(in);
      if (obj instanceof NodeDataMarker) return null;

      return (List<NodeData>) obj;
   }

   private NodeData integrateStateTransferChildren(NodeSPI parent, int offset, Iterator<NodeData> nodeDataIterator)
         throws IOException, ClassNotFoundException
   {
      int parent_level = parent.getFqn().size();
      int target_level = parent_level + 1;
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
            fqn = Fqn.fromRelativeFqn(parent.getFqn().getAncestor(offset), fqn);
         }
         size = fqn.size();
         if (size <= parent_level)
         {
            return nd;
         }
         else if (size > target_level)
         {
            throw new IllegalStateException("NodeData " + fqn + " is not a direct child of " + parent.getFqn());
         }

         Map attrs = nd.getAttributes();

         // We handle this NodeData.  Create a TreeNode and
         // integrate its data
         NodeSPI target;
         if (usePut)
         {
            cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
            cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
            cache.clearData(fqn);
            cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
            cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
            cache.put(fqn, attrs);
            target = cache.getNode(fqn);
         }
         else
         {
            target = factory.createNode(fqn, parent, attrs);
            parent.addChild(fqn.getLastElement(), target);
            // JBCACHE-913
            Region region = cache.getRegion(fqn, false);
            if (region != null && region.getEvictionRegionConfig() != null)
            {
               region.registerEvictionEvent(fqn, EvictionEvent.Type.ADD_NODE_EVENT, attrs == null ? 0 : attrs.size());
            }
         }

         // Recursively call, which will walk down the tree
         // and return the next NodeData that's a child of our parent
         nd = integrateStateTransferChildren(target, offset, nodeDataIterator);
      }
      if (nd != null && nd.isExceptionMarker())
      {
         NodeDataExceptionMarker ndem = (NodeDataExceptionMarker) nd;
         throw new CacheException("State provider node " + ndem.getCacheNodeIdentity()
               + " threw exception during loadState", ndem.getCause());
      }
      return null;
   }

   private Set<Node> retainInternalNodes(Node target)
   {
      Set<Node> result = new HashSet<Node>();
      Fqn targetFqn = target.getFqn();
      for (Fqn internalFqn : internalFqns)
      {
         if (internalFqn.isChildOf(targetFqn))
         {
            Node internalNode = getInternalNode(target, internalFqn);
            if (internalNode != null)
            {
               result.add(internalNode);
            }
         }
      }

      return result;
   }

   private Node getInternalNode(Node parent, Fqn internalFqn)
   {
      Object name = internalFqn.get(parent.getFqn().size());
      cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
      Node result = parent.getChild(name);
      if (result != null && internalFqn.size() < result.getFqn().size())
      {
         // need to recursively walk down the tree
         result = getInternalNode(result, internalFqn);
      }

      return result;
   }

   private void integrateRetainedNodes(NodeSPI target)
   {
      Set<Node> retainedNodes = retainInternalNodes(target);
      Fqn rootFqn = target.getFqn();
      for (Node retained : retainedNodes)
      {
         if (retained.getFqn().isChildOf(rootFqn))
         {
            integrateRetainedNode(target, retained);
         }
      }
   }

   private void integrateRetainedNode(NodeSPI ancestor, Node descendant)
   {
      Fqn descFqn = descendant.getFqn();
      Fqn ancFqn = ancestor.getFqn();
      Object name = descFqn.get(ancFqn.size());
      NodeSPI child = (NodeSPI) ancestor.getChild(name);
      if (ancFqn.size() == descFqn.size() + 1)
      {
         if (child == null)
         {
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
            child = factory.createNode(name, ancestor);
            ancestor.addChild(name, child);
         }

         // Keep walking down the tree
         integrateRetainedNode(child, descendant);
      }
   }
}