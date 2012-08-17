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
package org.jboss.cache.commands.write;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.commands.read.AbstractDataCommand;
import org.jboss.cache.mvcc.ReadCommittedNode;
import org.jboss.cache.notifications.Notifier;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#evict(org.jboss.cache.Fqn)}
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class EvictCommand extends AbstractDataCommand
{
   public static final int METHOD_ID = 8;
   public static final int VERSIONED_METHOD_ID = 9;

   private boolean recursive = false;

   protected Notifier notifier;
   protected static final Log log = LogFactory.getLog(EvictCommand.class);
   protected static final boolean trace = log.isTraceEnabled();
   private List<Fqn> nodesToEvict;

   public EvictCommand(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public EvictCommand()
   {
   }

   public void initialize(Notifier notifier, DataContainer dataContainer)
   {
      super.initialize(dataContainer);
      this.notifier = notifier;
   }

   public List<Fqn> getNodesToEvict()
   {
      return nodesToEvict;
   }

   public void setNodesToEvict(List<Fqn> nodesToEvict)
   {
      this.nodesToEvict = nodesToEvict;
   }

   /**
    * Evicts a node.
    * <p/>
    * See {@link org.jboss.cache.interceptors.EvictionInterceptor#visitEvictFqnCommand(org.jboss.cache.InvocationContext , EvictCommand)}
    * which is where the return value is used
    *
    * @return true if the node is now absent from the cache.  Returns false if the node still exists; i.e. was only data removed because it still has children.
    */
   public Object perform(InvocationContext ctx)
   {
      NodeSPI node = lookupForEviction(ctx, fqn);
      if ((node == null || node.isDeleted() || node.isResident()) && !recursiveEvictOnRoot(node))
      {
         return true;
      }
      else if (recursive)
      {
         Collection<Fqn> nodesToEvict = getRecursiveEvictionNodes();
         for (Fqn aFqn : nodesToEvict)
         {
            evictNode(aFqn, ctx, lookupForEviction(ctx, aFqn));
         }
         return !nodesToEvict.isEmpty();
      }
      else
      {
         return evictNode(fqn, ctx, node);
      }
   }

   private boolean recursiveEvictOnRoot(NodeSPI node)
   {
      return node == null && fqn.isRoot() && recursive && !nodesToEvict.isEmpty();
   }

   protected Collection<Fqn> getRecursiveEvictionNodes()
   {
      Collections.sort(nodesToEvict);
      Collections.reverse(nodesToEvict);
      return nodesToEvict;
   }

   protected boolean evictNode(Fqn fqn, InvocationContext ctx, NodeSPI node)
   {
      notifier.notifyNodeEvicted(fqn, true, ctx);
      try
      {
         if (node == null) return true;
         if (node.hasChildrenDirect() || fqn.isRoot())
         {
            if (trace) log.trace("removing DATA as node has children: evict(" + fqn + ")");
            node.clearDataDirect();
            node.setDataLoaded(false);
            return false;
         }
         else
         {
            if (trace) log.trace("removing NODE as it is a leaf: evict(" + fqn + ")");
            InternalNode parentNode = lookupInAllScopes(ctx, fqn.getParent());

            if (parentNode != null)
            {
               parentNode.removeChild(fqn.getLastElement());
               parentNode.setChildrenLoaded(false);
            }
            node.setValid(false, false);
            node.markAsDeleted(true);
            node.setDataLoaded(false);
            node.getDelegationTarget().clear();
            return true;
         }
      }
      finally
      {
         notifier.notifyNodeEvicted(fqn, false, ctx);
      }
   }

   private InternalNode lookupInAllScopes(InvocationContext ctx, Fqn fqn)
   {
      ReadCommittedNode nodeSPI = (ReadCommittedNode) lookupForEviction(ctx, fqn);
      if (nodeSPI == null)
      {
         return dataContainer.peekInternalNode(fqn, true);
      }
      return nodeSPI.getDelegationTarget();
   }

   protected NodeSPI lookupForEviction(InvocationContext ctx, Fqn fqn)
   {
      return ctx.lookUpNode(fqn);
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitEvictFqnCommand(ctx, this);
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public boolean isRecursive()
   {
      return recursive;
   }

   public void setRecursive(boolean recursive)
   {
      this.recursive = recursive;
   }

   @Override
   public Object[] getParameters()
   {
      throw new UnsupportedOperationException(getClass().getSimpleName() + " is not meant to be marshalled and replicated!");
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      throw new UnsupportedOperationException(getClass().getSimpleName() + " is not meant to be marshalled and replicated!");
   }


   @Override
   public String toString()
   {
      return getClass().getSimpleName() + "{" +
            "fqn=" + fqn +
            ", recursive=" + recursive +
            "}";
   }

   public void rollback()
   {
      // this is a no-op.
   }
}
