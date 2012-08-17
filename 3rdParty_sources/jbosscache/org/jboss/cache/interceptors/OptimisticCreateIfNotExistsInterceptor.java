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
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeFactory;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DefaultDataVersion;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to create new {@link NodeSPI} instances in the main data structure and then copy it into the
 * {@link TransactionWorkspace} as {@link WorkspaceNode}s as needed.  This is only invoked if nodes needed do not exist
 * in the underlying structure, they are added and the corresponding {@link org.jboss.cache.optimistic.WorkspaceNode#isCreated()}
 * would return <tt>true</tt> to denote that this node has been freshly created in the underlying structure.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:stevew@jofti.com">Steve Woodcock (stevew@jofti.com)</a>
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticCreateIfNotExistsInterceptor extends OptimisticInterceptor
{
   /**
    * A reference to the node factory registered with the cache instance, used to create both WorkspaceNodes as well as
    * NodeSPI objects in the underlying data structure.
    */
   private NodeFactory nodeFactory;

   private DataContainer dataContainer;

   private CacheSPI cache;

   private long lockAcquisitionTimeout;

   @Inject
   void injectDependencies(NodeFactory nodeFactory, DataContainer dataContainer, CacheSPI cacheSPI)
   {
      this.nodeFactory = nodeFactory;
      this.dataContainer = dataContainer;
      this.cache = cacheSPI;
   }

   @Start
   void init()
   {
      lockAcquisitionTimeout = configuration.getLockAcquisitionTimeout();
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      createNode(ctx, command.getFqn(), false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      createNode(ctx, command.getFqn(), false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      createNode(ctx, command.getFqn(), false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      List<Fqn> fqns = new ArrayList<Fqn>();
      fqns.add(command.getTo());
      //  peek into Node and get a hold of all child fqns as these need to be in the workspace.
      NodeSPI node = dataContainer.peek(command.getFqn(), true, true);
      greedyGetFqns(fqns, node, command.getTo());
      if (trace) log.trace("Adding Fqns " + fqns + " for a move() operation.");
      for (Fqn f : fqns)
      {
         createNode(ctx, f, true);
      }
      return invokeNextInterceptor(ctx, command);
   }

   /**
    * The only method that should be creating nodes.
    *
    * @param targetFqn
    * @throws CacheException
    */
   private void createNode(InvocationContext ctx, Fqn targetFqn, boolean suppressNotification) throws CacheException
   {
      if (dataContainer.peek(targetFqn, false, false) != null) return;
      // we do nothing if targetFqn is null
      if (targetFqn == null) return;

      boolean debug = log.isDebugEnabled();

      GlobalTransaction gtx = getGlobalTransaction(ctx);
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);

      WorkspaceNode workspaceNode;

      List<Fqn> nodesCreated = new ArrayList<Fqn>();

      DataVersion version = null;
      if (ctx.getOptionOverrides() != null && ctx.getOptionOverrides().getDataVersion() != null)
      {
         version = ctx.getOptionOverrides().getDataVersion();
         workspace.setVersioningImplicit(false);
      }

      // start with the ROOT node and then work our way down to the node necessary, creating nodes along the way.
      workspaceNode = workspace.getNode(Fqn.ROOT);
      if (debug) log.debug("GlobalTransaction: " + gtx + "; Root: " + workspaceNode);

      // we do not have the root in the workspace!  Put it into thr workspace now.
      if (workspaceNode == null)
      {
         NodeSPI node = dataContainer.getRoot();
         workspaceNode = lockAndCreateWorkspaceNode(nodeFactory, node, workspace, gtx, lockAcquisitionTimeout);
         workspace.addNode(workspaceNode);
         log.debug("Created root node in workspace.");
      }
      else
      {
         log.debug("Found root node in workspace.");
      }

      // iterate through the target Fqn's elements.
      int targetFqnSize = targetFqn.size(), currentDepth = 1;
      for (Object childName : targetFqn.peekElements())
      {
         boolean isTargetFqn = (currentDepth == targetFqnSize);
         currentDepth++;

         // current workspace node canot be null.
         // try and get the child of current node

         if (debug) log.debug("Attempting to get child " + childName);
         NodeSPI currentNode = workspaceNode.getNode().getChildDirect(childName);

         if (currentNode == null)
         {
            // first test that it exists in the workspace and has been created in thix tx!
            WorkspaceNode peekInWorkspace = workspace.getNode(Fqn.fromRelativeElements(workspaceNode.getFqn(), childName));
            if (peekInWorkspace != null && peekInWorkspace.isCreated())
            {
               // exists in workspace and has just been created.
               currentNode = peekInWorkspace.getNode();
               if (peekInWorkspace.isRemoved())
               {
                  peekInWorkspace.setRemoved(false);
                  // add in parent again
                  workspaceNode.addChild(peekInWorkspace);
               }
            }
         }

         if (currentNode == null)
         {
            // no child exists with this name; create it in the underlying data structure and then add it to the workspace.
            if (trace) log.trace("Creating new child, since it doesn't exist in the cache.");
            // we put the parent node into the workspace as we are changing its children.
            // at this point "workspaceNode" refers to the parent of the current node.  It should never be null if
            // you got this far!
            if (workspaceNode.isRemoved())
            {
               //add a new one or overwrite an existing one that has been deleted
               if (trace)
                  log.trace("Parent node doesn't exist in workspace or has been deleted.  Adding to workspace.");
               workspace.addNode(workspaceNode);
               if (!(workspaceNode.getVersion() instanceof DefaultDataVersion))
                  workspaceNode.setVersioningImplicit(false);
            }
            else
            {
               if (trace) log.trace("Parent node exists: " + workspaceNode);
            }

            // get the version passed in, if we need to use explicit versioning.
            DataVersion versionToPassIn = null;
            if (isTargetFqn && !workspace.isVersioningImplicit()) versionToPassIn = version;

            NodeSPI newUnderlyingChildNode = workspaceNode.createChild(childName, workspaceNode.getNode(), cache, versionToPassIn);

            // now assign "workspaceNode" to the new child created.
            workspaceNode = lockAndCreateWorkspaceNode(nodeFactory, newUnderlyingChildNode, workspace, gtx, lockAcquisitionTimeout);
            workspaceNode.setVersioningImplicit(versionToPassIn == null || !isTargetFqn);
            if (trace)
               log.trace("setting versioning of " + workspaceNode.getFqn() + " to be " + (workspaceNode.isVersioningImplicit() ? "implicit" : "explicit"));

            // now add the wrapped child node into the transaction space
            workspace.addNode(workspaceNode);
            workspaceNode.markAsCreated();
            // save in list so we can broadcast our created nodes outside
            // the synch block
            nodesCreated.add(workspaceNode.getFqn());
         }
         else
         {
            // node does exist but might not be in the workspace
            workspaceNode = workspace.getNode(currentNode.getFqn());
            // wrap it up so we can put it in later if we need to
            if (workspaceNode == null)
            {
               if (trace)
                  log.trace("Child node " + currentNode.getFqn() + " doesn't exist in workspace or has been deleted.  Adding to workspace in gtx " + gtx);

               workspaceNode = lockAndCreateWorkspaceNode(nodeFactory, currentNode, workspace, gtx, lockAcquisitionTimeout);

               // if the underlying node is a tombstone then mark the workspace node as newly created
               if (!currentNode.isValid()) workspaceNode.markAsCreated();

               if (isTargetFqn && !workspace.isVersioningImplicit())
               {
                  workspaceNode.setVersion(version);
                  workspaceNode.setVersioningImplicit(false);
               }
               else
               {
                  workspaceNode.setVersioningImplicit(true);
               }
               if (trace)
                  log.trace("setting versioning of " + workspaceNode.getFqn() + " to be " + (workspaceNode.isVersioningImplicit() ? "implicit" : "explicit"));
               workspace.addNode(workspaceNode);
            }
            else if (workspaceNode.isRemoved())
            {
               if (trace) log.trace("Found node but it is deleted in this workspace.  Needs resurrecting.");
               undeleteWorkspaceNode(workspaceNode, workspace);
            }
            else
            {
               if (trace) log.trace("Found child node in the workspace: " + currentNode);

            }
         }
      }

      if (!suppressNotification)
      {
         if (nodesCreated.size() > 0)
         {
            Notifier n = cache.getNotifier();
            for (Fqn temp : nodesCreated)
            {
               n.notifyNodeCreated(temp, true, ctx);
               n.notifyNodeCreated(temp, false, ctx);
               if (trace) log.trace("Notifying cache of node created in workspace " + temp);
            }
         }
      }
   }

}
