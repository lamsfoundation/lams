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
package org.jboss.cache.mvcc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeNotExistsException;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.invocation.NodeInvocationDelegate;
import static org.jboss.cache.mvcc.ReadCommittedNode.Flags.*;

/**
 * A node delegate that encapsulates read committed semantics when writes are initiated, committed or rolled back.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class ReadCommittedNode extends NodeInvocationDelegate
{
   private static final Log log = LogFactory.getLog(ReadCommittedNode.class);
   private static final boolean trace = log.isTraceEnabled();

   protected volatile InternalNode backup;
   protected byte flags = 0;

   protected InternalNode parent;

   protected static enum Flags
   {
      CHANGED(1), CREATED(1<<1), DELETED(1<<2), ORIG_DATA_LOADED(1<<3), ORIG_CHILDREN_LOADED(1<<4);
      final byte mask;

      Flags(int mask)
      {
         this.mask = (byte) mask;
      }
   }

   @SuppressWarnings("unchecked")
   public ReadCommittedNode(InternalNode node, InternalNode parent)
   {
      super(node);
      this.parent = parent;
   }

   public InternalNode getInternalParent()
   {
      return parent;
   }

   /**
    * Tests whether a flag is set.
    *
    * @param flag flag to test
    * @return true if set, false otherwise.
    */
   protected final boolean isFlagSet(Flags flag)
   {
      return (flags & flag.mask) != 0;
   }

   /**
    * Unility method that sets the value of the given flag to true.
    *
    * @param flag flag to set
    */
   protected final void setFlag(Flags flag)
   {
      flags |= flag.mask;
   }

   /**
    * Utility method that sets the value of the flag to false.
    *
    * @param flag flag to unset
    */
   protected final void unsetFlag(Flags flag)
   {
      flags &= ~flag.mask;
   }

   @Override
   public boolean isNullNode()
   {
      return false;
   }

   @Override
   public void markForUpdate(DataContainer container, boolean writeSkewCheck)
   {
      if (isFlagSet(CHANGED)) return; // already copied

      setFlag(CHANGED);  // mark as changed
      if (!isFlagSet(CREATED)) // if newly created, then nothing to copy.
      {
         backup = node;
         // don't copy the NodeReference but the InternalNode that the NodeReference delegates to.
         InternalNode backupDelegationTarget = ((NodeReference) backup).getDelegate();
         node = backupDelegationTarget.copy();
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public void commitUpdate(InvocationContext ctx, DataContainer container)
   {
      // only do stuff if there are changes.
      if (isFlagSet(CHANGED))
      {
         Fqn fqn = getFqn();
         if (trace)
         {
            log.trace("Updating node [" + fqn + "].  deleted=" + isDeleted() + " valid=" + isValid() + " changed=" + isChanged() + " created=" + isFlagSet(CREATED));
         }

         // check if node has been deleted.
         if (isFlagSet(DELETED))
         {
            if (!fqn.isRoot())
            {
               InternalNode parent = lookupParent(fqn, ctx, container);
               parent.removeChild(fqn.getLastElement());
               setValid(false, false);
               updateNode(fqn, ctx, container);
            }
            else
            {
               // should never get here.  Other layers should prevent a delete on root.
               log.warn("Attempting to remove the root node.  Not doing anything!");
            }
         }
         else if (isFlagSet(CREATED))
         {
            // add newly created nodes to parents.
            InternalNode parent = lookupParent(fqn, ctx, container);
            parent.addChild(node, true); // we know this is safe since we calculated the parent from the child.  No need to have the parent re-do checks when adding the child again.
         }
         else
         {
            // Only content has been updated, just update refs.
            updateNode(fqn, ctx, container);
         }

         // reset internal flags and refs to backups, etc.
         reset();
      }
   }

   private void reset()
   {
      backup = null;
      flags = 0;
   }

   /**
    * Performs a lookup for the parent node of the Fqn passed in.  The context is checked first, and failing that, the
    * data container is consulted.
    *
    * @param fqn       Fqn whos parent to locate
    * @param ctx       invocation context
    * @param container data container
    * @return Parent node, never null.
    * @throws NodeNotExistsException if the parent node cannot be found in any scope or data container.
    */
   protected final InternalNode lookupParent(Fqn fqn, InvocationContext ctx, DataContainer container) throws NodeNotExistsException
   {
      if (parent != null) return parent;

      InternalNode retval;
      Fqn parentFqn = fqn.getParent();
      NodeSPI parent = ctx.lookUpNode(parentFqn);
      // first check if the parent is cached in the context.
      if (parent != null)
      {
         retval = parent.getDelegationTarget();
      }
      else
      {
         // if not, get it from the data container.  No need to wrap here, we're just going to update the parent's child map.
         retval = container.peekInternalNode(parentFqn, false);
      }
      if (retval == null)
      {
         throw new NodeNotExistsException("Node " + parentFqn + " cannot be found in any context or data container!");
      }
      return retval;
   }

   /**
    * Updates state changes on the current node in the underlying data structure.
    *
    * @param ctx           invocation context
    * @param dataContainer data container
    */
   @SuppressWarnings("unchecked")
   protected void updateNode(Fqn fqn, InvocationContext ctx, DataContainer dataContainer)
   {
      // swap refs
      if (!isFlagSet(CREATED)) ((NodeReference) backup).setDelegate(node);
      node = backup;
   }

   @Override
   public void rollbackUpdate()
   {
      node = backup;
      if (node != null)
      {
         super.setChildrenLoaded(isFlagSet(ORIG_CHILDREN_LOADED));
         super.setDataLoaded(isFlagSet(ORIG_DATA_LOADED));
      }
      reset();
   }

   @Override
   public boolean isChanged()
   {
      return isFlagSet(CHANGED);
   }

   @Override
   public boolean isCreated()
   {
      return isFlagSet(CREATED);
   }

   @Override
   public void setCreated(boolean created)
   {
      if (created)
      {
         setFlag(CREATED);
      }
      else
      {
         unsetFlag(CREATED);
      }
   }

   // do not delegate deletion flags to the InternalNode since this will cause problems with concurrent readers.  Maintain
   // deletion information here and update on commit.

   @Override
   public boolean isDeleted()
   {
      return isFlagSet(DELETED);
   }

   @Override
   public void markAsDeleted(boolean deleted)
   {
      if (deleted)
      {
         setFlag(DELETED);
      }
      else
      {
         unsetFlag(DELETED);
      }
   }

   @Override
   public void markAsDeleted(boolean deleted, boolean recursive)
   {
      throw new UnsupportedOperationException("Recursive deletion not allowed!");
   }

   @Override
   public void setChildrenLoaded(boolean loaded)
   {
      if (node.isChildrenLoaded()) setFlag(ORIG_CHILDREN_LOADED);
      super.setChildrenLoaded(loaded);
   }

   @Override
   public void setDataLoaded(boolean loaded)
   {
      if (node.isDataLoaded()) setFlag(ORIG_DATA_LOADED);
      super.setDataLoaded(loaded);
   }
}
