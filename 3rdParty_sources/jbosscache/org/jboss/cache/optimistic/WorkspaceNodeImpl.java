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
package org.jboss.cache.optimistic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.AbstractNode;
import static org.jboss.cache.AbstractNode.NodeFlags.*;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.NodeFactory;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.VersionedNode;
import org.jboss.cache.invocation.NodeInvocationDelegate;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wraps an ordinary {@link Node} and adds versioning and other meta data to it.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @author Steve Woodcock (<a href="mailto:stevew@jofti.com">stevew@jofti.com</a>)
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class WorkspaceNodeImpl<K, V> extends AbstractNode<K, V> implements WorkspaceNode<K, V>
{

   private static final Log log = LogFactory.getLog(WorkspaceNodeImpl.class);
   private static final boolean trace = log.isTraceEnabled();

   private NodeSPI<K, V> node;
   private TransactionWorkspace workspace;
   private DataVersion version = DefaultDataVersion.ZERO;
   private Map<Object, NodeSPI<K, V>> optimisticChildNodeMap;
   private Set<Fqn> childrenAdded;
   private Set<Fqn> childrenRemoved;
   private Map<K, V> optimisticDataMap;
   private NodeFactory<K, V> nodeFactory;

   /**
    * Constructs with a node and workspace.
    */
   public WorkspaceNodeImpl(NodeSPI<K, V> node, TransactionWorkspace workspace, NodeFactory<K, V> nodeFactory)
   {
      NodeInvocationDelegate delegate = (NodeInvocationDelegate) node;
      if (!(delegate.getDelegationTarget() instanceof VersionedNode))
      {
         throw new IllegalArgumentException("node " + delegate.getDelegationTarget() + " not VersionedNode");
      }
      this.node = node;
      this.workspace = workspace;
      Map<K, V> nodeData = node.getDataDirect();
      if (!nodeData.isEmpty()) optimisticDataMap = new HashMap<K, V>(nodeData);
      this.version = node.getVersion();
      if (version == null)
      {
         throw new IllegalStateException("VersionedNode version null");
      }

      initFlags();
      this.nodeFactory = nodeFactory;
   }

   protected void initFlags()
   {
      setFlag(VERSIONING_IMPLICIT);
   }

   protected Set<Fqn> getChildrenAddedSet()
   {
      if (childrenAdded == null) childrenAdded = new HashSet<Fqn>();
      return childrenAdded;
   }

   protected Set<Fqn> getChildrenRemovedSet()
   {
      if (childrenRemoved == null) childrenRemoved = new HashSet<Fqn>();
      return childrenRemoved;
   }

   public boolean isChildrenModified()
   {
      return isFlagSet(CHILDREN_MODIFIED_IN_WORKSPACE);
   }

   public boolean isChildrenLoaded()
   {
      return optimisticChildNodeMap != null;
   }

   public boolean isResurrected()
   {
      return isFlagSet(RESURRECTED_IN_WORKSPACE);
   }

   public void markAsResurrected(boolean resurrected)
   {
      setFlag(RESURRECTED_IN_WORKSPACE, resurrected);
   }

   @Override
   public void markAsRemoved(boolean marker, boolean recursive)
   {
      setFlag(NodeFlags.REMOVED, marker);

      if (recursive && children != null)
      {
         synchronized (this)
         {
            for (Object child : children.values())
            {
               ((NodeSPI) child).markAsDeleted(marker, true);
            }
         }
      }

      if (marker)
      {
         if (childrenAdded != null) childrenAdded.clear();
         if (childrenRemoved != null) childrenRemoved.clear();
      }
   }

   /**
    * Returns true if this node is modified.
    */
   public boolean isModified()
   {
      return isFlagSet(MODIFIED_IN_WORKSPACE);
   }

   /**
    * A convenience method that returns whether a node is dirty, i.e., it has been created, deleted or modified.
    *
    * @return true if the node has been either created, deleted or modified.
    */
   public boolean isDirty()
   {
      return isModified() || isCreated() || isRemoved();
   }

   public Fqn getFqn()
   {
      return node.getFqn();
   }

   public void putAll(Map<K, V> data)
   {
      realPut(data, false);
      setFlag(MODIFIED_IN_WORKSPACE);
   }

   public void replaceAll(Map<K, V> data)
   {
      clearData();
      putAll(data);
   }

   public V put(K key, V value)
   {
      setFlag(MODIFIED_IN_WORKSPACE);
      if (optimisticDataMap == null) optimisticDataMap = new HashMap<K, V>();
      return optimisticDataMap.put(key, value);

   }

   public V remove(K key)
   {
      setFlag(MODIFIED_IN_WORKSPACE);
      if (optimisticDataMap == null) return null;
      return optimisticDataMap.remove(key);

   }

   public V get(K key)
   {
      return optimisticDataMap == null ? null : optimisticDataMap.get(key);
   }

   public Set<K> getKeys()
   {
      if (optimisticDataMap == null) return Collections.emptySet();
      return optimisticDataMap.keySet();
   }

   //not able to delete from this
   public Set<Object> getChildrenNames()
   {
      if (optimisticChildNodeMap == null)
      {
         initialiseChildMap();
      }

      Set<Object> names = new HashSet<Object>(optimisticChildNodeMap.keySet());

      // invoke deltas
      if (childrenAdded != null) for (Fqn child : childrenAdded) names.add(child.getLastElement());
      if (childrenRemoved != null) for (Fqn child : childrenRemoved) names.remove(child.getLastElement());

      return names;
   }

   @SuppressWarnings("unchecked")
   private void initialiseChildMap()
   {
      Map<Object, Node<K, V>> childrenMap = node.getChildrenMapDirect();
      this.optimisticChildNodeMap = new HashMap(childrenMap);
   }

   private void realPut(Map<K, V> data, boolean eraseData)
   {
      realPut(data, eraseData, true);
   }

   private void realPut(Map<K, V> data, boolean eraseData, boolean forceDirtyFlag)
   {
      if (forceDirtyFlag) setFlag(MODIFIED_IN_WORKSPACE);
      if (eraseData && optimisticDataMap != null)
      {
         optimisticDataMap.clear();
      }
      if (data != null)
      {
         if (optimisticDataMap == null) optimisticDataMap = new HashMap<K, V>();
         optimisticDataMap.putAll(data);
      }
   }

   public Node<K, V> getParent()
   {
      return node.getParent();
   }

   @SuppressWarnings("unchecked")
   public NodeSPI<K, V> createChild(Object childName, NodeSPI<K, V> parent, CacheSPI<K, V> cache, DataVersion version)
   {
      if (childName == null)
      {
         return null;
      }

      NodeSPI<K, V> child = nodeFactory.createNode(childName, parent);
      getChildrenAddedSet().add(child.getFqn());
      if (childrenRemoved != null) childrenRemoved.remove(child.getFqn());
      setFlag(CHILDREN_MODIFIED_IN_WORKSPACE);
      return child;
   }

   public boolean isVersioningImplicit()
   {
      return isFlagSet(VERSIONING_IMPLICIT);
   }

   public void setVersioningImplicit(boolean versioningImplicit)
   {
      setFlag(VERSIONING_IMPLICIT, versioningImplicit);
   }

   public NodeSPI<K, V> getNode()
   {
      return node;
   }

   @Override
   public DataVersion getVersion()
   {
      return version;
   }

   @Override
   public void setVersion(DataVersion version)
   {
      this.version = version;
   }

   @SuppressWarnings("unchecked")
   public List<Set<Fqn>> getMergedChildren()
   {
      List l = new ArrayList(2);

      if (childrenAdded != null)
         l.add(childrenAdded);
      else
         l.add(Collections.emptySet());

      if (childrenRemoved != null)
         l.add(childrenRemoved);
      else
         l.add(Collections.emptySet());

      return l;
   }

   public Map<K, V> getMergedData()
   {
      if (optimisticDataMap == null) return Collections.emptyMap();
      return optimisticDataMap;
   }

   public TransactionWorkspace getTransactionWorkspace()
   {
      return workspace;
   }

   public boolean isCreated()
   {
      return isFlagSet(CREATED_IN_WORKSPACE);
   }

   public void markAsCreated()
   {
      setFlag(CREATED_IN_WORKSPACE);
      // created != modified!!!
   }

   public Map<K, V> getData()
   {
      if (optimisticDataMap == null)
      {
         return Collections.emptyMap();
      }
      else
      {
         return Collections.unmodifiableMap(optimisticDataMap);
      }
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      if (isRemoved()) sb.append("del ");
      if (isModified()) sb.append("modified ");
      if (isCreated()) sb.append("new ");
      return getClass().getSimpleName() + " [ fqn=" + getFqn() + " " + sb + "ver=" + version + " " + (isVersioningImplicit() ? "implicit" : "explicit") + "]";
   }

   @Override
   public NodeSPI<K, V> addChildDirect(Fqn f)
   {
      CacheSPI cache = getCache();
      NodeSPI<K, V> newNode = null;
      GlobalTransaction gtx = cache.getInvocationContext().getGlobalTransaction();

      if (f.size() == 1)
      {
         newNode = createChild(f.get(0), node, getCache(), version);
      }
      else
      {
         // recursively create children
         NodeSPI<K, V> currentParent = this.getNode();
         for (Object o : f.peekElements())
         {
            if (currentParent instanceof WorkspaceNode)
            {
               newNode = ((WorkspaceNode<K, V>) currentParent).getNode().getOrCreateChild(o, gtx);
            }
            else
            {
               if (currentParent instanceof WorkspaceNode)
               {
                  newNode = ((WorkspaceNode<K, V>) currentParent).getNode().getOrCreateChild(o, gtx);
               }
               else
               {
                  newNode = currentParent.getOrCreateChild(o, gtx);
               }
            }
            currentParent = newNode;
         }
      }
      return newNode;
   }

   public void addChild(WorkspaceNode<K, V> child)
   {
      getChildrenAddedSet().add(child.getFqn());
      if (childrenRemoved != null) childrenRemoved.remove(child.getFqn());
      if (trace) log.trace("Adding child " + child.getFqn());
   }

   public void clearData()
   {
      if (optimisticDataMap != null)
      {
         optimisticDataMap.clear();
         setFlag(MODIFIED_IN_WORKSPACE);
      }
   }

   public int dataSize()
   {
      return optimisticDataMap == null ? 0 : optimisticDataMap.size();
   }

   public boolean hasChild(Object o)
   {
      throw new UnsupportedOperationException();
   }

   public boolean isValid()
   {
      throw new UnsupportedOperationException();
   }

   public boolean isLockForChildInsertRemove()
   {
      throw new UnsupportedOperationException();
   }

   public void setLockForChildInsertRemove(boolean lockForChildInsertRemove)
   {
      throw new UnsupportedOperationException();
   }

   public void releaseObjectReferences(boolean recursive)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public NodeSPI<K, V> getChildDirect(Fqn f)
   {
      if (f.size() > 1)
      {
         throw new UnsupportedOperationException("Workspace node does not support fetching indirect children");
      }
      return getChildDirect(f.getLastElement());
   }

   @Override
   public Set getChildren()
   {
      throw new UnsupportedOperationException();
   }

   public boolean hasChild(Fqn f)
   {
      throw new UnsupportedOperationException();
   }

   public NodeSPI<K, V> getNodeSPI()
   {
      throw new UnsupportedOperationException("WorkspaceNode has no access to a NodeSPI");
   }

   public V putIfAbsent(K k, V v)
   {
      throw new UnsupportedOperationException();
   }

   public V replace(K key, V value)
   {
      throw new UnsupportedOperationException();
   }

   public boolean replace(K key, V oldValue, V newValue)
   {
      throw new UnsupportedOperationException();
   }

   public boolean removeChild(Fqn f)
   {
      if (f.size() > 1) throw new UnsupportedOperationException("Workspace nodes can only remove direct children!");
      Object key = f.getLastElement();
      return removeChild(key);
   }

   public boolean removeChild(Object childName)
   {
      //NodeSPI n = node.getChildDirect(childName);
      Fqn childFqn = Fqn.fromRelativeElements(getFqn(), childName);
      /*if (n != null)
      {*/
      getChildrenRemovedSet().add(childFqn);
      if (childrenAdded != null) childrenAdded.remove(childFqn);
      setFlag(CHILDREN_MODIFIED_IN_WORKSPACE);
      return node.getChildDirect(childName) != null;
      /*}
      else
      {
         return false;
      }*/
   }

   protected CacheSPI<K, V> getCache()
   {
      return node.getCache();
   }
}
