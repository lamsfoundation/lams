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

import static org.jboss.cache.AbstractNode.NodeFlags.VALID;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
import org.jboss.cache.lock.IdentityLock;
import org.jboss.cache.lock.LockStrategyFactory;
import org.jboss.cache.marshall.MarshalledValue;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.util.FastCopyHashMap;
import org.jboss.cache.util.Immutables;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * UnversionedNode specific to pessimistic locking, with legacy code.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class PessimisticUnversionedNode<K, V> extends UnversionedNode<K, V>
{
   /**
    * Lock manager that manages locks to be acquired when accessing the node inside a transaction. Lazy set just in case
    * locking is not needed.
    */
   protected transient IdentityLock lock = null;
   protected LockStrategyFactory lockStrategyFactory;
   CommandsFactory commandsFactory;
   protected NodeFactory<K, V> nodeFactory;

   public PessimisticUnversionedNode(Object name, Fqn fqn, Map<K, V> data, CacheSPI<K, V> cache)
   {
      super(fqn, cache, false);
      if (!fqn.isRoot() && !name.equals(fqn.getLastElement()))
         throw new IllegalArgumentException("Child " + name + " must be last part of " + fqn);


      if (data != null && !data.isEmpty())
         setInternalState(data);
      else
         this.data = new FastCopyHashMap<K, V>();

      setLockForChildInsertRemove(cache != null && cache.getConfiguration() != null && cache.getConfiguration().isLockParentForChildInsertRemove());


   }

   /**
    * @return a genericized version of the child map.
    */
   @SuppressWarnings("unchecked")
   private ConcurrentMap<Object, Node<K, V>> children()
   {
      return children;
   }

   // ------ lock-per-node paradigm

   public void injectLockStrategyFactory(LockStrategyFactory lockStrategyFactory)
   {
      this.lockStrategyFactory = lockStrategyFactory;
   }

   public void injectDependencies(CacheSPI<K, V> spi, CommandsFactory commandsFactory, NodeFactory<K, V> nodeFactory)
   {
      this.cache = spi;
      this.commandsFactory = commandsFactory;
      this.nodeFactory = nodeFactory;
   }


   protected synchronized void initLock()
   {
      if (lock == null)
      {
         lock = new IdentityLock(lockStrategyFactory, delegate);
      }
   }

   @Override
   public IdentityLock getLock()
   {
      initLock();
      return lock;
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder(super.toString());
      if (lock != null)
      {
         if (lock.isReadLocked())
         {
            sb.append(" RL");
         }
         if (lock.isWriteLocked())
         {
            sb.append(" WL");
         }
      }
      return sb.toString();
   }

   @SuppressWarnings("unchecked")
   @Override
   public InternalNode<K, V> copy()
   {
      PessimisticUnversionedNode<K, V> n = new PessimisticUnversionedNode<K, V>(fqn.getLastElement(), fqn, data, cache);
      copyInternals(n);
      n.children = children;
      n.lockStrategyFactory = lockStrategyFactory;
      n.commandsFactory = commandsFactory;
      n.nodeFactory = nodeFactory;
      return n;
   }

   // ------ legacy addChild methods that used a lot of implicit locks.

   @Override
   public void addChildDirect(NodeSPI<K, V> child)
   {
      Fqn childFqn = child.getFqn();
      if (childFqn.isDirectChildOf(fqn))
      {
         synchronized (this)
         {
            children().put(child.getFqn().getLastElement(), child);
         }
      }
      else
         throw new CacheException("Attempting to add a child [" + child.getFqn() + "] to [" + getFqn() + "].  Can only add direct children.");
   }

   private NodeSPI<K, V> getOrCreateChild(Object childName, GlobalTransaction gtx, boolean createIfNotExists, boolean notify)
   {
      NodeSPI<K, V> child;
      if (childName == null)
      {
         throw new IllegalArgumentException("null child name");
      }

      child = (NodeSPI<K, V>) children().get(childName);
      InvocationContext ctx = cache.getInvocationContext();
      if (createIfNotExists && child == null)
      {
         // construct the new child outside the synchronized block to avoid
         // spending any more time than necessary in the synchronized section
         Fqn childFqn = Fqn.fromRelativeElements(fqn, childName);
         NodeSPI<K, V> newChild = nodeFactory.createNode(childFqn, delegate);
         if (newChild == null)
         {
            throw new IllegalStateException();
         }
         synchronized (this)
         {
            // check again to see if the child exists
            // after acquiring exclusive lock
            child = (NodeSPI<K, V>) children().get(childName);
            if (child == null)
            {
               if (notify) cache.getNotifier().notifyNodeCreated(childFqn, true, ctx);
               child = newChild;
               children().put(childName, child);
            }
         }

         // notify if we actually created a new child
         if (newChild == child)
         {
            if (trace) log.trace("created child: fqn=" + childFqn);

            if (gtx != null)
            {
               CreateNodeCommand createNodeCommand = commandsFactory.buildCreateNodeCommand(childFqn);
               ctx.getTransactionContext().addLocalModification(createNodeCommand);
            }
            if (notify) cache.getNotifier().notifyNodeCreated(childFqn, false, ctx);
         }
      }
      return child;
   }

   @Override
   public NodeSPI<K, V> addChildDirect(Fqn f, boolean notify)
   {
      if (f.size() == 1)
      {
         GlobalTransaction gtx = cache.getInvocationContext().getGlobalTransaction();
         return getOrCreateChild(f.getLastElement(), gtx, true, notify);
      }
      else
      {
         throw new UnsupportedOperationException("Cannot directly create children which aren't directly under the current node.");
      }
   }

   @Override
   public NodeSPI<K, V> addChildDirect(Object o, boolean notify)
   {
      GlobalTransaction gtx = cache.getInvocationContext().getGlobalTransaction();
      return getOrCreateChild(o, gtx, true, notify);
   }

   @Override
   public NodeSPI<K, V> getChildDirect(Fqn fqn)
   {
      if (fqn.size() == 1)
      {
         return getChildDirect(fqn.getLastElement());
      }
      else
      {
         NodeSPI<K, V> currentNode = delegate;
         for (int i = 0; i < fqn.size(); i++)
         {
            Object nextChildName = fqn.get(i);
            currentNode = currentNode.getChildDirect(nextChildName);
            if (currentNode == null) return null;
         }
         return currentNode;
      }
   }

   @Override
   public NodeSPI<K, V> getChildDirect(Object childName)
   {
      if (childName == null) return null;
      return (NodeSPI<K, V>) children().get(childName);
   }

   @Override
   public Set<NodeSPI<K, V>> getChildrenDirect()
   {
      // strip out deleted child nodes...
      if (children == null || children.size() == 0) return Collections.emptySet();

      Set<NodeSPI<K, V>> exclDeleted = new HashSet<NodeSPI<K, V>>();
      for (Node<K, V> n : children().values())
      {
         NodeSPI<K, V> spi = (NodeSPI<K, V>) n;
         if (!spi.isDeleted()) exclDeleted.add(spi);
      }
      exclDeleted = Collections.unmodifiableSet(exclDeleted);
      return exclDeleted;
   }

   @Override
   public Map<Object, Node<K, V>> getChildrenMapDirect()
   {
      return children();
   }

   @Override
   public void setChildrenMapDirect(Map<Object, Node<K, V>> children)
   {
      if (children == null)
         this.children = null;
      else
      {
         this.children.clear();
         this.children().putAll(children);
      }
   }

   @Override
   public void addChildDirect(Object nodeName, Node<K, V> nodeToAdd)
   {
      if (nodeName != null)
      {
         children().put(nodeName, nodeToAdd);
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public Set<NodeSPI<K, V>> getChildrenDirect(boolean includeMarkedForRemoval)
   {
      if (includeMarkedForRemoval)
      {
         if (children != null && !children.isEmpty())
         {
            return Immutables.immutableSetConvert(children.values());
         }
         else
         {
            return Collections.emptySet();
         }
      }
      else
      {
         return getChildrenDirect();
      }
   }

   @Override
   public NodeSPI<K, V> addChildDirect(Fqn f)
   {
      return addChildDirect(f, true);
   }

   @Override
   public NodeSPI<K, V> getOrCreateChild(Object childName, GlobalTransaction gtx)
   {
      return getOrCreateChild(childName, gtx, true, true);
   }

   @Override
   public void markAsRemoved(boolean marker, boolean recursive)
   {
      setFlag(NodeFlags.REMOVED, marker);

      if (recursive && children != null)
      {
         synchronized (this)
         {
            for (Node<?, ?> child : children().values())
            {
               ((NodeSPI) child).markAsDeleted(marker, true);
            }
         }
      }
   }

   @Override
   public void setValid(boolean valid, boolean recursive)
   {
      setFlag(VALID, valid);

      if (trace) log.trace("Marking node " + getFqn() + " as " + (valid ? "" : "in") + "valid");
      if (recursive)
      {
         for (Node<K, V> child : children().values())
         {
            ((NodeSPI<K, V>) child).setValid(valid, recursive);
         }
      }
   }

   /**
    * Adds details of the node into a map as strings.
    */
   @Override
   protected void printDetailsInMap(StringBuilder sb, int indent)
   {
      printIndent(sb, indent);
      indent += 2;// increse it
      sb.append(Fqn.SEPARATOR);
      if (!fqn.isRoot()) sb.append(fqn.getLastElement());
      sb.append("  ");
      sb.append(data);
      for (Node n : children().values())
      {
         sb.append("\n");
         ((NodeSPI) n).printDetails(sb, indent);
      }
   }


   @Override
   public void setFqn(Fqn fqn)
   {
      if (trace)
      {
         log.trace(getFqn() + " set FQN " + fqn);
      }
      this.fqn = fqn;

      if (children == null)
      {
         return;
      }

      // invoke children
      for (Map.Entry<Object, ? extends Node<K, V>> me : children().entrySet())
      {
         NodeSPI<K, V> n = (NodeSPI<K, V>) me.getValue();
         Fqn cfqn = Fqn.fromRelativeElements(fqn, me.getKey());
         n.setFqn(cfqn);
      }
   }

   @Override
   public void releaseObjectReferences(boolean recursive)
   {
      if (recursive && children != null)
      {
         for (Node<K, V> child : children().values())
         {
            child.releaseObjectReferences(recursive);
         }
      }

      if (data != null)
      {
         for (K key : data.keySet())
         {
            // get the key first, before attempting to serialize stuff since data.get() may deserialize the key if doing
            // a hashcode() or equals().

            Object value = data.get(key);
            if (key instanceof MarshalledValue)
            {
               ((MarshalledValue) key).compact(true, true);
            }

            if (value instanceof MarshalledValue)
            {
               ((MarshalledValue) value).compact(true, true);
            }

         }
      }
   }

}
