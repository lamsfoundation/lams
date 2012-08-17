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

import org.jboss.cache.CacheException;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import static org.jboss.cache.config.Configuration.CacheMode;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.optimistic.DataVersioningException;
import org.jboss.cache.optimistic.DefaultDataVersion;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validates the data in the {@link TransactionWorkspace} against data in the underlying data structure
 * (versions only) and then applies changes to the underlying data structure.  This is only triggered when commit,
 * rollback or prepare method calls are encountered.  Other method calls are directly passed up the interceptor chain,
 * untouched.  Note that prepare/commit/rollbacks are <b>not</b> passed up the interceptor chain after being processed.
 * <p/>
 * When preparting, this interceptor does nothing more than validate versions.
 * The validation scheme used is based on the {@link org.jboss.cache.optimistic.DataVersion} implementation used.
 * {@link org.jboss.cache.optimistic.DataVersion#newerThan(org.jboss.cache.optimistic.DataVersion)} is used to determine
 * whether the version of one instance is newer than the version of another.  It is up to the {@link org.jboss.cache.optimistic.DataVersion}
 * implementation to deal with attempting to compare incompatible version types (and potentially throwing {@link org.jboss.cache.optimistic.DataVersioningException}s.
 * <p/>
 * Upon successful commit, changes in the workspace are applied to the underlying data structure in the cache.
 * <p/>
 * On rollback clears the nodes in the workspace and leaves the underlying data structure untouched.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @author Steve Woodcock (<a href="mailto:stevew@jofti.com">stevew@jofti.com</a>)
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticValidatorInterceptor extends OptimisticInterceptor
{
   private boolean useTombstones;

   private DataContainer dataContainer;

   @Inject
   public void initialize(DataContainer dataContainer)
   {
      this.dataContainer = dataContainer;
   }

   @Inject
   void init()
   {
      CacheMode mode = configuration.getCacheMode();
      useTombstones = (mode == CacheMode.INVALIDATION_ASYNC) || (mode == CacheMode.INVALIDATION_SYNC);
   }

   @Override
   public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);

      // There is no guarantee that this collection is in any order!
      Collection<WorkspaceNode> nodes = workspace.getNodes().values();

      //we ought to have all necessary locks here so lets try and validate
      if (log.isDebugEnabled()) log.debug("Validating " + nodes.size() + " nodes.");
      for (WorkspaceNode workspaceNode : nodes)
      {
         if (workspaceNode.isDirty())
         {
            Fqn fqn = workspaceNode.getFqn();
            if (trace) log.trace("Validating version for node [" + fqn + "]");

            NodeSPI underlyingNode;
            underlyingNode = dataContainer.peek(fqn, true, true);

            // if this is a newly created node then we expect the underlying node to be null.
            // also, if the node has been deleted in the WS and the underlying node is null, this *may* be ok ... will test again later when comparing versions
            // if not, we have a problem...
            if (underlyingNode == null && !workspaceNode.isCreated() && !workspaceNode.isRemoved())
            {
               throw new DataVersioningException("Underlying node for " + fqn + " is null, and this node wasn't newly created in this transaction!  We have a concurrent deletion event.");
            }

            // needs to have been created AND modified - we allow concurrent creation if no data is put into the node
            if (underlyingNode != null && underlyingNode.isValid() && workspaceNode.isCreated() && workspaceNode.isModified())
            {
               throw new DataVersioningException("Transaction attempted to create " + fqn + " anew.  It has already been created since this transaction started, by another (possibly remote) transaction.  We have a concurrent creation event.");
            }

            if (underlyingNode != null && !underlyingNode.isValid())
            {
               // we havea  tombstone
               if (!workspaceNode.isCreated() && !workspaceNode.isRemoved())
                  throw new DataVersioningException("Underlying node doesn't exist but a tombstone does; workspace node should be marked as created!");
               if (underlyingNode.getVersion().newerThan(workspaceNode.getVersion()))
               {
                  // we have an out of date node here
                  throw new DataVersioningException("Version mismatch for node " + fqn + ": underlying node with version " + workspaceNode.getNode().getVersion() + " is newer than workspace node, with version " + workspaceNode.getVersion());
               }
            }

            if (!workspaceNode.isCreated() && (workspaceNode.isRemoved() || workspaceNode.isModified()))
            {
               // if the real node is null, throw a DVE
               if (underlyingNode == null)
               {
                  // but not if the WSN has also been deleted
                  if (!workspaceNode.isRemoved())
                     throw new DataVersioningException("Unable to compare versions since the underlying node has been deleted by a concurrent transaction!");
                  else if (trace)
                     log.trace("The data node [" + fqn + "] is null, but this is ok since the workspace node is marked as deleted as well");
               }
               // if there is a DataVersion type mismatch here, leave it up to the DataVersion impl to barf if necessary.  - JBCACHE-962
               else if (underlyingNode.getVersion().newerThan(workspaceNode.getVersion()))
               {
                  // we have an out of date node here
                  throw new DataVersioningException("Version mismatch for node " + fqn + ": underlying node with version " + workspaceNode.getNode().getVersion() + " is newer than workspace node, with version " + workspaceNode.getVersion());
               }
            }
         }
         else
         {
            if (trace) log.trace("Node [" + workspaceNode.getFqn() + "] doesn't need validating as it isn't dirty");
         }
      }
      log.debug("Successfully validated nodes");
      return invokeNextInterceptor(ctx, command);
   }

   @SuppressWarnings("unchecked")
   private void commitTransaction(InvocationContext ctx)
   {
      GlobalTransaction gtx = getGlobalTransaction(ctx);
      TransactionWorkspace workspace;
      workspace = getTransactionWorkspace(ctx);

      if (log.isDebugEnabled()) log.debug("Commiting successfully validated changes for GlobalTransaction " + gtx);
      Collection<WorkspaceNode> workspaceNodes = workspace.getNodes().values();
      for (WorkspaceNode workspaceNode : workspaceNodes)
      {
         NodeSPI underlyingNode = workspaceNode.getNode();
         // short circuit if this node is deleted?
         if (workspaceNode.isRemoved())
         {
            if (trace) log.trace("Workspace node " + workspaceNode.getFqn() + " deleted; removing");

            if (underlyingNode.getFqn().isRoot())
            {
               throw new CacheException("An illegal attempt to delete the root node!");
            }
            else
            {
               // mark it as invalid so any direct references are marked as such
               underlyingNode.setValid(false, true);
               // we need to update versions here, too
               performVersionUpdate(underlyingNode, workspaceNode);

               if (!useTombstones)
               {
                  // don't retain the tombstone
                  NodeSPI parent = underlyingNode.getParentDirect();
                  if (parent == null)
                  {
                     throw new CacheException("Underlying node " + underlyingNode + " has no parent");
                  }

                  parent.removeChildDirect(underlyingNode.getFqn().getLastElement());
               }
            }
         }
         else
         {
            boolean updateVersion = false;
            if (workspaceNode.isChildrenModified() || workspaceNode.isResurrected()) // if it is newly created make sure we remove all underlying children that may exist, to solve a remove-and-create-in-tx condition
            {
               if (trace) log.trace("Updating children since node has modified children");
               // merge children.
               List<Set<Fqn>> deltas = workspaceNode.getMergedChildren();

               if (trace) log.trace("Applying children deltas to parent node " + underlyingNode.getFqn());

               if (workspaceNode.isResurrected())
               {
                  // mark it as invalid so any direct references are marked as such
                  Map childNode = underlyingNode.getChildrenMapDirect();
                  for (Object o : childNode.values())
                  {
                     NodeSPI cn = (NodeSPI) o;
                     cn.setValid(false, true);
                     if (!useTombstones) underlyingNode.removeChildDirect(cn.getFqn().getLastElement());
                  }
               }

               for (Fqn child : deltas.get(0))
               {
                  NodeSPI childNode = workspace.getNode(child).getNode();
                  underlyingNode.addChildDirect(childNode);
                  childNode.setValid(true, false);
               }

               for (Fqn child : deltas.get(1))
               {
                  // mark it as invalid so any direct references are marked as such
                  NodeSPI childNode = underlyingNode.getChildDirect(child.getLastElement());
                  if (childNode != null) childNode.setValid(false, true);

                  if (!useTombstones) underlyingNode.removeChildDirect(child.getLastElement());
               }

               updateVersion = underlyingNode.isLockForChildInsertRemove();

               // do we need to notify listeners of a modification??  If all we've done is added children then don't
               // notify.
            }

            if (workspaceNode.isModified())
            {
               if (trace) log.trace("Merging data since node is dirty");
               Map mergedData = workspaceNode.getMergedData();
               underlyingNode.clearDataDirect();
               underlyingNode.putAllDirect(mergedData);

               // mark node and any parents as valid- if available.  Versioning parents are tough though - leave as old versions?
               validateNodeAndParents(underlyingNode);
               updateVersion = true;
            }

            if (updateVersion)
            {
               performVersionUpdate(underlyingNode, workspaceNode);
            }
         }
      }
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      commitTransaction(ctx);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      TransactionWorkspace workspace;
      workspace = getTransactionWorkspace(ctx);
      workspace.clearNodes();
      return invokeNextInterceptor(ctx, command);
   }


   private void validateNodeAndParents(NodeSPI node)
   {
      node.setValid(true, false);
      if (!node.getFqn().isRoot()) validateNodeAndParents(node.getParentDirect());
   }

   private void performVersionUpdate(NodeSPI underlyingNode, WorkspaceNode workspaceNode)
   {
      if (workspaceNode.isVersioningImplicit())
      {
         if (trace) log.trace("Versioning is implicit; incrementing.");
         underlyingNode.setVersion(((DefaultDataVersion) workspaceNode.getVersion()).increment());
      }
      else
      {
         if (trace) log.trace("Versioning is explicit; not attempting an increment.");
         underlyingNode.setVersion(workspaceNode.getVersion());
      }

      if (trace)
         log.trace("Setting version of node " + underlyingNode.getFqn() + " from " + workspaceNode.getVersion() + " to " + underlyingNode.getVersion());
   }

}
