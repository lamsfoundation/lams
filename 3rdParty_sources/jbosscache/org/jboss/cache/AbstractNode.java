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
import static org.jboss.cache.AbstractNode.NodeFlags.REMOVED;
import static org.jboss.cache.AbstractNode.NodeFlags.RESIDENT;
import org.jboss.cache.lock.IdentityLock;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Base class for {@link UnversionedNode}.
 *
 * @author manik
 */
public abstract class AbstractNode<K, V>
{
   protected ConcurrentMap children; // purposefully NOT genericized yet, since UnversionedNode and PessimisticUnversionedNode will both store different types.

   protected Fqn fqn;
   private static final Log log = LogFactory.getLog(AbstractNode.class);
   /**
    * Flags placed on the node.  Replaces older 'boolean' flags.
    */
   // NOTE: this is a lot more efficient than an EnumSet, expecially when initialising and copying.
   protected short flags = 0;

   /**
    * These flags were originally stored as booleans on the UnversionedNode class.  They have been replaced with an enum
    * and an EnumSet, which is much more space-efficient for very little cost in lookups.
    */
   public static enum NodeFlags
   {
      /**
       * All children are loaded from the cache loader if this flag is present.
       */
      CHILDREN_LOADED(1),
      /**
       * Data is loaded from the cache loader if this flag is present.
       */
      DATA_LOADED(1<<1),
      /**
       * Node is write-locked when children are added or removed if this flag is enabled.
       */
      LOCK_FOR_CHILD_INSERT_REMOVE(1<<2),
      /**
       * Node is valid if this flag is present.
       */
      VALID(1<<3),
      /**
       * Node has been removed.
       */
      REMOVED(1<<4),
      /**
       * NOde is resident and excluded from evictions
       */
      RESIDENT(1<<5),
      /**
       * Specific to Optimistic Locking Workspace nodes - set if a node has been modified in a workspace.
       */
      MODIFIED_IN_WORKSPACE(1<<6),
      /**
       * Specific to Optimistic Locking Workspace nodes - set if a node has been created in a workspace.
       */
      CREATED_IN_WORKSPACE(1<<7),
      /**
       * Specific to Optimistic Locking Workspace nodes - set if a node has added or removed children in a workspace.
       */
      CHILDREN_MODIFIED_IN_WORKSPACE(1<<8),
      /**
       * Specific to Optimistic Locking Workspace nodes - set if an implicit version is associated with this node.
       */
      VERSIONING_IMPLICIT(1<<9),
      /**
       * Specific to Optimistic Locking Workspace nodes - set if a node has been resurrected in a workspace.
       */
      RESURRECTED_IN_WORKSPACE(1<<10);

      protected final short mask;

      NodeFlags(int mask)
      {
         this.mask = (short) mask;
      }
   }

   /**
    * Tests whether a flag is set.
    *
    * @param flag flag to test
    * @return true if set, false otherwise.
    */
   protected final boolean isFlagSet(NodeFlags flag)
   {
      return (flags & flag.mask) != 0;
   }

   /**
    * Utility method for setting or unsetting a flag.  If status is true, the NodeFlag specified is added to the {@link #flags}
    * encoded short.  If status is false, the NodeFlag is removed from the encoded short.
    *
    * @param flag  flag to set or unset
    * @param value true to set the flag, false to unset the flag.
    */
   protected final void setFlag(NodeFlags flag, boolean value)
   {
      if (value)
         setFlag(flag);
      else
         unsetFlag(flag);
   }

   /**
    * Unility method that sets the value of the given flag to true.
    *
    * @param flag flag to set
    */
   protected final void setFlag(NodeFlags flag)
   {
      flags |= flag.mask;
   }

   /**
    * Utility method that sets the value of the flag to false.
    *
    * @param flag flag to unset
    */
   protected final void unsetFlag(NodeFlags flag)
   {
      flags &= ~flag.mask;
   }

   public boolean isRemoved()
   {
      return isFlagSet(REMOVED);
   }

   public void setResident(boolean resident)
   {
      setFlag(RESIDENT, resident);
   }

   public void setRemoved(boolean marker)
   {
      markAsRemoved(marker, false);
   }

   public abstract void markAsRemoved(boolean marker, boolean recursive);

   public boolean isResident()
   {
      return isFlagSet(RESIDENT);
   }

   @SuppressWarnings("deprecation")
   public IdentityLock getLock()
   {
      throw new UnsupportedOperationException("Not supported in this implementation!");
   }

   // versioning
   public void setVersion(DataVersion version)
   {
      throw new UnsupportedOperationException("Versioning not supported");
   }

   public DataVersion getVersion()
   {
      throw new UnsupportedOperationException("Versioning not supported");
   }

   @Override
   public boolean equals(Object another)
   {
      if (another instanceof AbstractNode)
      {
         AbstractNode<?, ?> anotherNode = (AbstractNode) another;
         return fqn == null && anotherNode.fqn == null || !(fqn == null || anotherNode.fqn == null) && fqn.equals(anotherNode.fqn);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return fqn.hashCode();
   }

   // ----- default no-op impls of child manipulation methods

   public InternalNode<K, V> getChild(Fqn f)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public InternalNode<K, V> getChild(Object childName)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public Set<InternalNode<K, V>> getChildren()
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public Set<InternalNode<K, V>> getChildren(boolean includeMarkedForRemoval)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public Map<Object, InternalNode<K, V>> getChildrenMap()
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void addChild(Object nodeName, InternalNode<K, V> nodeToAdd)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void addChild(InternalNode<K, V> child)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void addChild(InternalNode<K, V> child, boolean safe)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void setChildrenMap(ConcurrentMap<Object, InternalNode<K, V>> children)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public NodeSPI<K, V> getChildDirect(Fqn fqn)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public NodeSPI<K, V> getChildDirect(Object childName)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public Set<NodeSPI<K, V>> getChildrenDirect()
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public Set<NodeSPI<K, V>> getChildrenDirect(boolean includeMarkedForRemoval)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public Map<Object, Node<K, V>> getChildrenMapDirect()
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void setChildrenMapDirect(Map<Object, Node<K, V>> children)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void addChildDirect(Object nodeName, Node<K, V> nodeToAdd)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public void addChildDirect(NodeSPI<K, V> child)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public NodeSPI<K, V> addChildDirect(Fqn f)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public NodeSPI<K, V> addChildDirect(Fqn f, boolean notify)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public NodeSPI<K, V> addChildDirect(Object o, boolean notify)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }

   public NodeSPI<K, V> getOrCreateChild(Object childName, GlobalTransaction gtx)
   {
      throw new UnsupportedOperationException("Not supported in " + getClass().getSimpleName());
   }
}
