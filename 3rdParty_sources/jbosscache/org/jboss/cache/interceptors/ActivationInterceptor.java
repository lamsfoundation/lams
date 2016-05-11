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

import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.mvcc.ReadCommittedNode;

import javax.transaction.TransactionManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads nodes that don't exist at the time of the call into memory from the CacheLoader.
 * If the nodes were evicted earlier then we remove them from the cache loader after
 * their attributes have been initialized and their children have been loaded in memory.
 *
 * @author <a href="mailto:{hmesha@novell.com}">{Hany Mesha}</a>
 *
 */
public class ActivationInterceptor extends CacheLoaderInterceptor
{

   protected TransactionManager txMgr = null;
   private long activations = 0;

   /**
    * List<Transaction> that we have registered for
    */
   protected ConcurrentHashMap transactions = new ConcurrentHashMap(16);
   protected static final Object NULL = new Object();

   public ActivationInterceptor()
   {
      isActivation = true;
      useCacheStore = false;
   }

   @Inject
   public void injectTransactionManager(TransactionManager txMgr)
   {
      this.txMgr = txMgr;
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      Object returnValue = super.visitClearDataCommand(ctx, command);
      if (trace)
      {
         log.trace("This is a remove data operation; removing the data from the loader, no activation processing needed.");
      }
      loader.removeData(command.getFqn());
      return returnValue;
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      Object returnValue = super.visitRemoveNodeCommand(ctx, command);
      if (trace)
      {
         log.trace("This is a remove operation; removing the node from the loader, no activation processing needed.");
      }
      loader.remove(command.getFqn());
      return returnValue;
   }

   @Override
   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      Object returnValue = super.visitGetChildrenNamesCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      Object returnValue = super.visitGetKeysCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      Object returnValue = super.visitGetNodeCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      Object returnValue = super.visitGetKeyValueCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return visitPutKeyValueCommand(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      Object returnValue = super.visitPutKeyValueCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      Object returnValue = super.visitPutDataMapCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      Object returnValue = super.visitRemoveKeyCommand(ctx, command);
      removeNodeFromCacheLoader(ctx, command.getFqn(), true);
      return returnValue;
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      Object returnValue = super.visitMoveCommand(ctx, command);
      if (trace)
      {
         log.trace("This is a move operation; removing the FROM node from the loader, no activation processing needed.");
      }
      loader.remove(command.getFqn());
      removeNodeFromCacheLoader(ctx, command.getFqn().getParent(), true);
      removeNodeFromCacheLoader(ctx, command.getTo(), true);
      return returnValue;
   }

   private boolean wasLoadedIntoMemory(InvocationContext ctx, Fqn fqn)
   {
      Set<Fqn> fqnsLoaded = ctx.getFqnsLoaded();
      return fqnsLoaded != null && fqnsLoaded.contains(fqn);
   }

   /**
    * Remove the node from the cache loader if it exists in memory,
    * its attributes have been initialized, its children have been loaded,
    * AND it was found in the cache loader (nodeLoaded = true).
    */
   private void removeNodeFromCacheLoader(InvocationContext ctx, Fqn fqn, boolean checkIfLoaded) throws Throwable
   {
      if (fqn != null)
      {
         boolean remove = false;
         if (!checkIfLoaded || wasLoadedIntoMemory(ctx, fqn))
         {
            InternalNode n;
            if (((n = findNode(ctx, fqn)) != null) && n.isDataLoaded() && loader.exists(fqn))
            {
               // node not null and attributes have been loaded?
               if (n.hasChildren())
               {
                  boolean result = childrenLoaded(n);
                  if (result)
                  {
                     log.debug("children all initialized");
                     remove(fqn);
                     remove = true;
                  }
               }
               else if (loaderNoChildren(fqn))
               {
                  if (log.isDebugEnabled()) log.debug("no children " + n);
                  remove(fqn);
                  remove = true;
               }
            }
         }

         if (!fqn.isRoot() && remove)
         {
            // check fqn parent, since the parent may not be needed on disk anymore
            removeNodeFromCacheLoader(ctx, fqn.getParent(), false);
         }
      }
   }

   private InternalNode findNode(InvocationContext ctx, Fqn fqn)
   {
      ReadCommittedNode n = (ReadCommittedNode) ctx.lookUpNode(fqn);
      if (n == null || n.isNullNode())
      {
         return dataContainer.peekInternalNode(fqn, true);
      }
      else
      {
         return n.getDelegationTarget();
      }
   }

   private boolean childrenLoaded(InternalNode<?, ?> node) throws Exception
   {
      if (!node.isChildrenLoaded())
      {
         if (loader.getChildrenNames(node.getFqn()) != null) return false;
      }
      for (InternalNode child : node.getChildren())
      {
         if (!child.isDataLoaded())
         {
            return false;
         }

         if (child.hasChildren())
         {
            if (!childrenLoaded(child))
            {
               return false;
            }
         }
         else if (!loaderNoChildren(child.getFqn()))
         {
            return false;
         }
      }
      return true;

   }

   private void remove(Fqn fqn) throws Exception
   {
      loader.remove(fqn);
      if (getStatisticsEnabled()) activations++;
   }

   /**
    * Returns true if the loader indicates no children for this node.
    * Return false on error.
    */
   private boolean loaderNoChildren(Fqn fqn)
   {
      try
      {
         Set childrenNames = loader.getChildrenNames(fqn);
         return (childrenNames == null);
      }
      catch (Exception e)
      {
         log.error("failed getting the children names for " + fqn + " from the cache loader", e);
         return false;
      }
   }

   @ManagedAttribute(description = "number of cache node activations")
   public long getActivations()
   {
      return activations;
   }

   @ManagedOperation
   public void resetStatistics()
   {
      super.resetStatistics();
      activations = 0;
   }

   @ManagedOperation
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = super.dumpStatistics();
      if (retval == null)
      {
         retval = new HashMap<String, Object>();
      }
      retval.put("Activations", activations);
      return retval;
   }

   @Override
   protected void recordNodeLoaded(InvocationContext ctx, Fqn fqn)
   {
      ctx.addFqnLoaded(fqn);
   }
}
