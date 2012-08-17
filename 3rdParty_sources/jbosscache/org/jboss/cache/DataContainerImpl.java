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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;
import org.jboss.cache.jmx.annotations.MBean;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.lock.LockManager;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.util.CachePrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A container for the root node in the cache, which also provides helpers for efficiently accessing nodes, walking trees, etc.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
@NonVolatile
@MBean(objectName = "DataContainer", description = "Core container for all cached items")
public class DataContainerImpl implements DataContainer
{
   private static final Log log = LogFactory.getLog(DataContainerImpl.class);
   private static boolean trace = log.isTraceEnabled();

   /**
    * Root node.
    */
   private volatile NodeSPI root;
   private volatile InternalNode rootInternal;

   /**
    * Set<Fqn> of Fqns of the topmost node of internal regions that should
    * not included in standard state transfers.
    */
   private final Set<Fqn> internalFqns = new HashSet<Fqn>();
   private NodeFactory nodeFactory;
   private LockManager lockManager;
   private BuddyFqnTransformer buddyFqnTransformer;
   private Configuration config;
   private boolean usingMvcc;
   volatile boolean started = false;
   private static final InternalNode[] NULL_ARRAY = {null, null};

   @Inject
   public void injectDependencies(NodeFactory nodeFactory, LockManager lockManager, BuddyFqnTransformer transformer, Configuration configuration)
   {
      started = false;
      setDependencies(nodeFactory, lockManager);

      // We need to create a root node even at this stage since certain components rely on this being available before
      // start() is called.
      // TODO: Investigate which components rely on this being available before start(), and why!
      //TODO - remove setDependencies method at this point
      createRootNode();
      this.buddyFqnTransformer = transformer;
      config = configuration;
   }

   public void setDependencies(NodeFactory nodeFactory, LockManager lockManager)
   {
      this.nodeFactory = nodeFactory;
      this.lockManager = lockManager;
   }

   @Start(priority = 12)
   public void start()
   {
      createRootNode();
      started = true;
   }

   public void createRootNode()
   {
      usingMvcc = config != null && config.getNodeLockingScheme() == NodeLockingScheme.MVCC;
      if (trace) log.trace("Starting data container.  Using MVCC? " + usingMvcc);
      // create a new root temporarily.
      NodeSPI tempRoot = nodeFactory.createRootNode();
      // if we don't already have a root or the new (temp) root is of a different class (optimistic vs pessimistic) to
      // the current root, then we use the new one.

      Class currentRootType = root == null ? null : root.getDelegationTarget().getClass();
      Class tempRootType = tempRoot.getDelegationTarget().getClass();

      if (!tempRootType.equals(currentRootType))
      {
         if (trace) log.trace("Setting root node to an instance of " + tempRootType);
         setRoot(tempRoot);
      }

      if (usingMvcc && rootInternal == null) setRoot(root); // sets the "internal root"
   }

   @Stop(priority = 100)
   public void stop()
   {
      started = false;
      // empty in-memory state
      if (root != null)
      {
         root.clearDataDirect();
         root.removeChildrenDirect();
      }
      else if (rootInternal != null)
      {
         rootInternal.clear();
         rootInternal.removeChildren();
      }
   }

   @Deprecated
   public NodeSPI getRoot()
   {
      return root;
   }

   /**
    * Sets the root node reference to the node passed in.
    *
    * @param root node
    */
   public void setRoot(Object root)
   {
      if (root == null) throw new CacheException("Attempting to set a null node as a root node!");
      // Mega-Ugh!
      if (usingMvcc && root instanceof InternalNode)
      {
         if (log.isDebugEnabled()) log.debug("Setting rootInternal to " + root);
         rootInternal = (InternalNode) root;
         this.root = null;
      }
      else
      {
         this.root = (NodeSPI) root;
         if (usingMvcc)
         {
            if (log.isDebugEnabled()) log.debug("Setting rootInternal to " + this.root.getDelegationTarget());
            rootInternal = this.root.getDelegationTarget();
            this.root = null;
         }
      }
   }

   public boolean isResident(Fqn fqn)
   {
      if (usingMvcc)
      {
         InternalNode in = peekInternalNode(fqn, false);
         return in != null && in.isResident();
      }
      else
      {
         NodeSPI<?, ?> nodeSPI = peek(fqn, false, false);
         return nodeSPI != null && nodeSPI.isResident();
      }
   }

   public void registerInternalFqn(Fqn fqn)
   {
      internalFqns.add(fqn);
   }

   public NodeSPI peek(Fqn fqn)
   {
      return peek(fqn, false, false);
   }

   public NodeSPI peek(Fqn fqn, boolean includeDeletedNodes)
   {
      return peek(fqn, includeDeletedNodes, false);
   }

   public NodeSPI peek(Fqn fqn, boolean includeDeletedNodes, boolean includeInvalidNodes)
   {
      if (trace)
      {
         log.trace("peek " + fqn + ", includeDeletedNodes:" + includeDeletedNodes + ", includeInvalidNodes:" + includeInvalidNodes);
      }
      if (fqn == null || fqn.size() == 0) return getRoot();
      NodeSPI n = getRoot();
      int fqnSize = fqn.size();
      for (int i = 0; i < fqnSize; i++)
      {
         Object obj = fqn.get(i);
         n = n.getChildDirect(obj);
         if (n == null)
         {
            return null;
         }
         else if (!includeDeletedNodes && n.isDeleted())
         {
            return null;
         }
         else if (!includeInvalidNodes && !n.isValid())
         {
            return null;
         }
      }
      return n;
   }

   public boolean exists(Fqn fqn)
   {
      return usingMvcc ? peekInternalNode(fqn, false) != null : peek(fqn, false, false) != null;
   }

   public boolean hasChildren(Fqn fqn)
   {
      if (fqn == null) return false;

      if (usingMvcc)
      {
         InternalNode in = peekInternalNode(fqn, false);
         return in != null && in.hasChildren();
      }
      else
      {
         NodeSPI n = peek(fqn);
         return n != null && n.hasChildrenDirect();
      }
   }

   public List<NodeData> buildNodeData(List<NodeData> list, NodeSPI node, boolean mapSafe)
   {
      if (usingMvcc)
      {
         return buildNodeData(list, node.getDelegationTarget(), node.getData(), mapSafe);
      }
      else
      {
         return buildNodeDataLegacy(list, node, mapSafe);
      }
   }

   private List<NodeData> buildNodeData(List<NodeData> list, InternalNode<?, ?> node, Map dataInNode, boolean mapSafe)
   {
      NodeData data = new NodeData(buddyFqnTransformer.getActualFqn(node.getFqn()), dataInNode, mapSafe);
      list.add(data);
      for (InternalNode childNode : node.getChildrenMap().values())
      {
         buildNodeData(list, childNode, childNode.getData(), true);
      }
      return list;
   }

   @Deprecated
   private List<NodeData> buildNodeDataLegacy(List<NodeData> list, NodeSPI node, boolean mapSafe)
   {
      NodeData data = new NodeData(buddyFqnTransformer.getActualFqn(node.getFqn()), node.getDataDirect(), mapSafe);
      list.add(data);
      for (Object childNode : node.getChildrenDirect())
      {
         buildNodeData(list, (NodeSPI) childNode, true);
      }
      return list;
   }

   public List<Fqn> getNodesForEviction(Fqn fqn, boolean recursive)
   {
      List<Fqn> result = new ArrayList<Fqn>();
      if (usingMvcc)
      {
         InternalNode node = peekInternalNode(fqn, false);
         if (recursive)
         {
            if (node != null) recursiveAddEvictionNodes(node, result);
         }
         else
         {
            if (node == null)
            {
               result.add(fqn);
               return result;
            }
            if (fqn.isRoot())
            {
               for (Object childName : node.getChildrenNames())
               {
                  if (!node.isResident()) result.add(Fqn.fromRelativeElements(fqn, childName));
               }
            }
            else if (!node.isResident())
            {
               result.add(fqn);
            }
         }
         return result;
      }
      else
      {
         NodeSPI node = peek(fqn, false);
         if (recursive)
         {
            if (node != null) recursiveAddEvictionNodes(node, result);
         }
         else
         {
            if (node == null)
            {
               result.add(fqn);
               return result;
            }
            if (fqn.isRoot())
            {
               for (Object childName : node.getChildrenNamesDirect())
               {
                  if (!node.isResident()) result.add(Fqn.fromRelativeElements(fqn, childName));
               }
            }
            else if (!node.isResident())
            {
               result.add(fqn);
            }
         }
         return result;
      }
   }

   private void recursiveAddEvictionNodes(NodeSPI<?, ?> node, List<Fqn> result)
   {
      for (NodeSPI<?, ?> child : node.getChildrenDirect())
      {
         recursiveAddEvictionNodes(child, result);
      }
      Fqn fqn = node.getFqn();
      if (!fqn.isRoot() && !node.isResident())
      {
         result.add(fqn);
      }
   }

   private void recursiveAddEvictionNodes(InternalNode<?, ?> node, List<Fqn> result)
   {
      for (InternalNode child : node.getChildren())
      {
         recursiveAddEvictionNodes(child, result);
      }
      Fqn fqn = node.getFqn();
      if (!fqn.isRoot() && !node.isResident())
      {
         result.add(fqn);
      }
   }

   @Override
   public String toString()
   {
      return toString(false);
   }

   public Set<Fqn> getInternalFqns()
   {
      return Collections.unmodifiableSet(internalFqns);
   }

   /**
    * Returns a debug string with optional details of contents.
    *
    * @param details if true, details are printed
    * @return detailed contents of the container
    */
   @SuppressWarnings("deprecation")
   public String toString(boolean details)
   {
      StringBuilder sb = new StringBuilder();
      int indent = 0;

      if (!details)
      {
         sb.append(getClass().getName()).append(" [").append(getNumberOfNodes()).append(" nodes, ");
         sb.append(getNumberOfLocksHeld()).append(" locks]");
      }
      else
      {
         if (root == null)
         {
            return sb.toString();
         }
         if (started && !usingMvcc)
         {
            for (Object n : root.getChildrenDirect())
            {
               ((NodeSPI) n).print(sb, indent);
               sb.append("\n");
            }
         }
      }
      return sb.toString();
   }

   public int getNumberOfLocksHeld()
   {
         return started ? numLocks(root) : -1;
   }

   private int numLocks(NodeSPI n)
   {
      if (!started) return 0;
      int num = 0;
      if (n != null)
      {
         if (lockManager.isLocked(n))
         {
            num++;
            if (trace) log.trace(n.getFqn() + " locked");
         }
         for (Object cn : n.getChildrenDirect(true))
         {
            num += numLocks((NodeSPI) cn);
         }
      }
      return num;
   }

   @ManagedOperation(description = "Returns the number of nodes in the data container")
   public int getNumberOfNodes()
   {
      if (started)
      {
         if (!usingMvcc) return numNodes(root) - 1;
         return numNodesMvcc(rootInternal) - 1;
      }
      else
      {
         return -1;
      }
   }

   private int numNodesMvcc(InternalNode node)
   {
      int count = 1; //for 'node'
      if (node != null)
      {
         Set<InternalNode> children = node.getChildren();
         for (InternalNode child : children)
         {
            count += numNodesMvcc(child);
         }
      }
      return count;
   }

   private int numNodes(NodeSPI n)
   {
      int count = 1;// for n
      if (n != null)
      {
         for (Object child : n.getChildrenDirect())
         {
            count += numNodes((NodeSPI) child);
         }
      }
      return count;
   }

   /**
    * Prints information about the contents of the nodes in the cache's current
    * in-memory state.  Does not load any previously evicted nodes from a
    * cache loader, so evicted nodes will not be included.
    *
    * @return details
    */
   @ManagedOperation(description = "Prints details of the data container")
   public String printDetails()
   {
      StringBuilder sb = new StringBuilder();
      if (root == null)
      {
         rootInternal.printDetails(sb, 0);
      }
      else
      {
         root.printDetails(sb, 0);
      }
      sb.append("\n");
      return sb.toString();
   }

   @ManagedOperation(description = "Prints details of the data container, formatted as an HTML String")
   public String printDetailsAsHtml()
   {
      return CachePrinter.formatHtml(printDetails());
   }


   /**
    * Returns lock information.
    *
    * @return lock info
    */
   public String printLockInfo()
   {
      return lockManager.printLockInfo(root);
   }

   public int getNumberOfAttributes(Fqn fqn)
   {
      return usingMvcc ? numAttributes(peekInternalNode(fqn, false)) : numAttributes(peek(fqn));
   }

   private int numAttributes(NodeSPI n)
   {
      int count = 0;
      for (Object child : n.getChildrenDirect())
      {
         count += numAttributes((NodeSPI) child);
      }
      count += n.getDataDirect().size();
      return count;
   }

   private int numAttributesMvcc(InternalNode n)
   {
      int count = 0;
      for (Object child : n.getChildren())
      {
         count += numAttributesMvcc((InternalNode) child);
      }
      count += n.getData().size();
      return count;
   }

   private int numAttributes(InternalNode n)
   {
      int count = 0;
      for (Object child : n.getChildren())
      {
         count += numAttributes((NodeSPI) child);
      }
      count += n.getData().size();
      return count;
   }

   @ManagedOperation(description = "Returns the number of attributes in all nodes in the data container")
   public int getNumberOfAttributes()
   {
      return usingMvcc ? numAttributesMvcc(rootInternal) : numAttributes(root);
   }

   public boolean removeFromDataStructure(Fqn f, boolean skipMarkerCheck)
   {
      return usingMvcc ? removeMvcc(f, skipMarkerCheck) : removeLegacy(f, skipMarkerCheck);
   }

   private boolean removeMvcc(Fqn f, boolean skipMarkerCheck)
   {
      InternalNode n = peekInternalNode(f, true);
      if (n == null)
      {
         return false;
      }

      if (trace) log.trace("Performing a real remove for node " + f + ", marked for removal.");
      if (skipMarkerCheck || n.isRemoved())
      {
         if (n.getFqn().isRoot())
         {
            // do not actually delete; just remove deletion marker
            n.setRemoved(true);

            // mark the node to be removed (and all children) as invalid so anyone holding a direct reference to it will
            // be aware that it is no longer valid.
            n.setValid(false, true);
            n.setValid(true, false);

            // but now remove all children, since the call has been to remove("/")
            n.removeChildren();
            return true;
         }
         else
         {
            // mark the node to be removed (and all children) as invalid so anyone holding a direct reference to it will
            // be aware that it is no longer valid.
            n.setValid(false, true);
            InternalNode parent = peekInternalNode(f.getParent(), true);
            return parent.removeChild(n.getFqn().getLastElement());
         }
      }
      else
      {
         if (log.isDebugEnabled()) log.debug("Node " + f + " NOT marked for removal as expected, not removing!");
         return false;
      }
   }


   private boolean removeLegacy(Fqn f, boolean skipMarkerCheck)
   {
      NodeSPI n = peek(f, true);
      if (n == null)
      {
         return false;
      }

      if (trace) log.trace("Performing a real remove for node " + f + ", marked for removal.");
      if (skipMarkerCheck || n.isDeleted())
      {
         if (n.getFqn().isRoot())
         {
            // do not actually delete; just remove deletion marker
            n.markAsDeleted(true);

            // mark the node to be removed (and all children) as invalid so anyone holding a direct reference to it will
            // be aware that it is no longer valid.
            n.setValid(false, true);
            n.setValid(true, false);

            // but now remove all children, since the call has been to remove("/")
            n.removeChildrenDirect();
            return true;
         }
         else
         {
            // mark the node to be removed (and all children) as invalid so anyone holding a direct reference to it will
            // be aware that it is no longer valid.
            n.setValid(false, true);
            NodeSPI parent = peek(f.getParent(), true);
            return parent.removeChildDirect(n.getFqn().getLastElement());
         }
      }
      else
      {
         if (log.isDebugEnabled()) log.debug("Node " + f + " NOT marked for removal as expected, not removing!");
         return false;
      }
   }

   public void evict(Fqn fqn, boolean recursive)
   {
      List<Fqn> toEvict = getNodesForEviction(fqn, recursive);
      for (Fqn aFqn : toEvict)
      {
         evict(aFqn);
      }
   }

   public boolean evict(Fqn fqn)
   {
      if (!exists(fqn)) return true;
      if (hasChildren(fqn))
      {
         if (trace)
         {
            log.trace("removing DATA as node has children: evict(" + fqn + ")");
         }
         if (usingMvcc)
         {
            removeData(fqn);
         }
         else
         {
            removeDataLegacy(fqn);
         }
         return false;
      }
      else
      {
         if (trace) log.trace("removing NODE as it is a leaf: evict(" + fqn + ")");
         if (usingMvcc)
         {
            removeNode(fqn);
         }
         else
         {
            removeNodeLegacy(fqn);
         }
         return true;
      }
   }

   private void removeNodeLegacy(Fqn fqn)
   {
      NodeSPI targetNode = peek(fqn, false, true);
      if (targetNode == null) return;
      NodeSPI parentNode = targetNode.getParentDirect();
      targetNode.setValid(false, false);
      if (parentNode != null)
      {
         parentNode.removeChildDirect(fqn.getLastElement());
         parentNode.setChildrenLoaded(false);
      }
   }

   private void removeNode(Fqn fqn)
   {
      InternalNode targetNode = peekInternalNode(fqn, true);
      if (targetNode == null) return;
      InternalNode parentNode = peekInternalNode(fqn.getParent(), true);
      targetNode.setValid(false, false);
      if (parentNode != null)
      {
         parentNode.removeChild(fqn.getLastElement());
         parentNode.setChildrenLoaded(false);
      }
   }

   private void removeDataLegacy(Fqn fqn)
   {
      NodeSPI n = peek(fqn);
      if (n == null)
      {
         log.warn("node " + fqn + " not found");
         return;
      }
      n.clearDataDirect();
      n.setDataLoaded(false);
   }

   private void removeData(Fqn fqn)
   {
      InternalNode n = peekInternalNode(fqn, false);
      if (n == null)
      {
         log.warn("node " + fqn + " not found");
         return;
      }
      n.clear();
      n.setDataLoaded(false);
   }

   public Object[] createNodes(Fqn fqn)
   {
      List<NodeSPI> result = new ArrayList<NodeSPI>(fqn.size());
      Fqn tmpFqn = Fqn.ROOT;

      int size = fqn.size();

      // root node
      NodeSPI n = root;
      for (int i = 0; i < size; i++)
      {
         Object childName = fqn.get(i);
         tmpFqn = Fqn.fromRelativeElements(tmpFqn, childName);

         NodeSPI childNode;
         Map children = n.getChildrenMapDirect();
         childNode = (NodeSPI) children.get(childName);

         if (childNode == null)
         {
            childNode = n.addChildDirect(Fqn.fromElements(childName));
            result.add(childNode);
         }

         n = childNode;
      }
      return new Object[]{result, n};
   }

   public InternalNode peekInternalNode(Fqn fqn, boolean includeInvalidNodes)
   {
      if (fqn == null || fqn.size() == 0) return rootInternal;
      InternalNode n = rootInternal;
      int fqnSize = fqn.size();
      for (int i = 0; i < fqnSize; i++)
      {
         Object obj = fqn.get(i);
         n = n.getChild(obj);
         if (n == null)
         {
            return null;
         }
         else if (!includeInvalidNodes && !n.isValid())
         {
            return null;
         }
      }
      return n;
   }

   public InternalNode[] peekInternalNodeAndDirectParent(Fqn fqn, boolean includeInvalidNodes)
   {
      if (fqn == null || fqn.size() == 0) return new InternalNode[]{rootInternal, null};
      InternalNode n = rootInternal;
      InternalNode directParent = null;
      int fqnSize = fqn.size();
      for (int i = 0; i < fqnSize; i++)
      {
         directParent = n;
         Object obj = fqn.get(i);
         n = directParent.getChild(obj);
         if (n == null)
         {
            return NULL_ARRAY;
         }
         else if (!includeInvalidNodes && !n.isValid())
         {
            return NULL_ARRAY;
         }
      }
      return new InternalNode[]{n, directParent};
   }

   public void setBuddyFqnTransformer(BuddyFqnTransformer buddyFqnTransformer)
   {
      this.buddyFqnTransformer = buddyFqnTransformer;
   }
}
