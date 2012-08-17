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

import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeFactory;
import org.jboss.cache.NodeNotExistsException;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
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
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.notifications.Notifier;
import static org.jboss.cache.notifications.event.NodeModifiedEvent.ModificationType.*;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DefaultDataVersion;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Operations on nodes are done on the copies that exist in the workspace rather than passed down
 * to the {@link org.jboss.cache.interceptors.CallInterceptor}.  These operations happen in this interceptor.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:stevew@jofti.com">Steve Woodcock (stevew@jofti.com)</a>
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticNodeInterceptor extends OptimisticInterceptor
{
   /**
    * Needed for the creation of workspace nodes based on underlying nodes in the cache.
    */
   private NodeFactory nodeFactory;
   private Notifier notifier;
   private DataContainer dataContainer;
   private long lockAcquisitionTimeout;

   @Inject
   protected void injectDependencies(Notifier notifier, NodeFactory nodeFactory, DataContainer dataContainer)
   {
      this.notifier = notifier;
      this.nodeFactory = nodeFactory;
      this.dataContainer = dataContainer;
   }

   @Start
   void init()
   {
      lockAcquisitionTimeout = configuration.getLockAcquisitionTimeout();
   }

   public OptimisticNodeInterceptor()
   {
      log = LogFactory.getLog(getClass());
      trace = log.isTraceEnabled();
   }

   @Override
   public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, false, true);
      if (workspaceNode != null)
      {
         setVersioning(ctx, workspace, workspaceNode);
      }
      Object result = removeNode(workspace, workspaceNode, true, ctx);
      addToModificationList(command, ctx);
      return result;
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return visitPutKeyValueCommand(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, true, true);
      if (workspaceNode != null)
      {
         setVersioning(ctx, workspace, workspaceNode);
      }
      else
      {
         // "fail-more-silently" patch thanks to Owen Taylor - JBCACHE-767
         if ((ctx.getOptionOverrides() == null || !ctx.getOptionOverrides().isFailSilently()))
         {
            throw new CacheException("Unable to set node version for " + command.getFqn() + ", node is null.");
         }
      }
      Object result = putDataKeyValueAndNotify(command.getKey(), command.getValue(), workspace, workspaceNode, ctx);
      addToModificationList(command, ctx);
      return result;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, true, true);
      if (workspaceNode != null)
      {
         setVersioning(ctx, workspace, workspaceNode);
      }
      else
      {
         // "fail-more-silently" patch thanks to Owen Taylor - JBCACHE-767
         if ((ctx.getOptionOverrides() == null || !ctx.getOptionOverrides().isFailSilently()))
         {
            throw new CacheException("Unable to set node version for " + command.getFqn() + ", node is null.");
         }
      }
      putDataMapAndNotify(command.getData(), workspace, workspaceNode, ctx);
      addToModificationList(command, ctx);
      return null;
   }


   @Override
   public Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, true, true);
      if (ctx.isOriginLocal() && ctx.getOptionOverrides() != null && ctx.getOptionOverrides().getDataVersion() != null)
      {
         throw new CacheException("Setting a data version while performing a move() is not supported!!");
      }
      if (workspaceNode != null)
      {
         setVersioning(ctx, workspace, workspaceNode);
      }
      moveNodeAndNotify(command.getTo(), workspaceNode, workspace, ctx);
      addToModificationList(command, ctx);
      return null;
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, true, true);

      if (workspaceNode != null)
      {
         setVersioning(ctx, workspace, workspaceNode);
      }
      Object result = removeKeyAndNotify(command.getKey(), workspace, workspaceNode, ctx);
      addToModificationList(command, ctx);
      return result;
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, true, true);
      if (workspaceNode != null)
      {
         setVersioning(ctx, workspace, workspaceNode);
      }
      removeDataAndNotify(workspace, workspaceNode, ctx);
      addToModificationList(command, ctx);
      return null;
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      Object result;
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, false, false);

      if (workspaceNode == null)
      {
         if (trace) log.debug("Unable to find node " + command.getFqn() + " in workspace.");
         result = null;
      }
      else
      {
         //add this node into the wrokspace
         notifier.notifyNodeVisited(command.getFqn(), true, ctx);
         Object val = workspaceNode.get(command.getKey());
         workspace.addNode(workspaceNode);
         notifier.notifyNodeVisited(command.getFqn(), false, ctx);
         result = val;
      }
      return result;
   }

   @Override
   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      Object result;
      Fqn fqn = command.getFqn();
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, fqn, workspace, false, false);
      if (workspaceNode == null)
      {
         if (trace) log.trace("unable to find node " + fqn + " in workspace.");
         result = null;
      }
      else
      {
         notifier.notifyNodeVisited(fqn, true, ctx);
         Object keySet = workspaceNode.getKeys();
         workspace.addNode(workspaceNode);
         notifier.notifyNodeVisited(fqn, false, ctx);
         result = keySet;
      }
      return result;
   }

   @Override
   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      Object result;
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, false, false);
      if (workspaceNode == null)
      {
         if (trace) log.trace("unable to find node " + command.getFqn() + " in workspace.");
         result = null;
      }
      else
      {
         notifier.notifyNodeVisited(command.getFqn(), true, ctx);
         Object data = workspaceNode.getData();
         workspace.addNode(workspaceNode);
         notifier.notifyNodeVisited(command.getFqn(), false, ctx);
         result = data;
      }
      return result;
   }

   @Override
   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      Object result;
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, false, false);
      if (workspaceNode == null)
      {
         if (trace) log.trace("Unable to find node " + command.getFqn() + " in workspace.");
         result = null;
      }
      else
      {
         notifier.notifyNodeVisited(command.getFqn(), true, ctx);
         Object nameSet = workspaceNode.getChildrenNames();
         workspace.addNode(workspaceNode);
         notifier.notifyNodeVisited(command.getFqn(), false, ctx);
         result = nameSet;
      }
      return result;
   }

   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      TransactionWorkspace workspace = getTransactionWorkspace(ctx);
      Object result;
      WorkspaceNode workspaceNode = fetchWorkspaceNode(ctx, command.getFqn(), workspace, false, false);
      if (workspaceNode == null)
      {
         if (trace) log.trace("Unable to find node " + command.getFqn() + " in workspace.");
         result = null;
      }
      else if (workspaceNode.isRemoved())
      {
         if (trace) log.trace("Attempted to retrieve node " + command.getFqn() + " but it has been deleted!");
         result = null;
      }
      else
      {
         notifier.notifyNodeVisited(command.getFqn(), true, ctx);
         workspace.addNode(workspaceNode);
         notifier.notifyNodeVisited(command.getFqn(), false, ctx);
         result = workspaceNode.getNode();
      }
      return result;
   }

   private void setVersioning(InvocationContext ctx, TransactionWorkspace workspace, WorkspaceNode workspaceNode)
   {
      // use explicit versioning
      if (ctx.getOptionOverrides() != null && ctx.getOptionOverrides().getDataVersion() != null)
      {
         workspace.setVersioningImplicit(false);
         DataVersion version = ctx.getOptionOverrides().getDataVersion();

         workspaceNode.setVersion(version);
         if (trace) log.trace("Setting versioning for node " + workspaceNode.getFqn() + " to explicit");

         workspaceNode.setVersioningImplicit(false);
      }
      else
      {
         if (trace) log.trace("Setting versioning for node " + workspaceNode.getFqn() + " to implicit");
         workspaceNode.setVersioningImplicit(true);
      }
   }

   /**
    * Retrieves a backup fqn in an array of arguments.  This is typically used to parse arguments from a data gravitation cleanup method.
    *
    * @param args array of arguments to parse
    * @return an Fqn
    */
   private Fqn getBackupFqn(Object[] args)
   {
      return (Fqn) args[1];
   }

   /**
    * Adds a method call to the modification list of a given transaction's transaction entry
    */
   private void addToModificationList(WriteCommand command, InvocationContext ctx)
   {
      // Option opt = ctx.getOptionOverrides();
      ctx.getTransactionContext().addModification(command);
      if (log.isDebugEnabled()) log.debug("Adding command " + command + " to modification list");
   }

   // -----------------------------------------------------------------

   // Methods that mimic core functionality in the cache, except that they work on WorkspaceNodes in the TransactionWorkspace.

   // -----------------------------------------------------------------


   /**
    * Performs a move within the workspace.
    *
    * @param parentFqn parent under which the node is to be moved
    * @param node      node to move
    * @param ws        transaction workspace
    * @param ctx
    */
   private void moveNodeAndNotify(Fqn parentFqn, WorkspaceNode node, TransactionWorkspace ws, InvocationContext ctx)
   {
      Fqn nodeFqn = node.getFqn();
      if (nodeFqn.isRoot())
      {
         log.warn("Attempting to move the root node.  Not taking any action, treating this as a no-op.");
         return;
      }

      WorkspaceNode oldParent = fetchWorkspaceNode(ctx, nodeFqn.getParent(), ws, false, true);
      if (oldParent == null) throw new NodeNotExistsException("Node " + nodeFqn.getParent() + " does not exist!");

      if (parentFqn.equals(oldParent.getFqn()))
      {
         log.warn("Attempting to move a node in same place.  Not taking any action, treating this as a no-op.");
         return;
      }
      // retrieve parent
      WorkspaceNode parent = fetchWorkspaceNode(ctx, parentFqn, ws, false, true);
      if (parent == null) throw new NodeNotExistsException("Node " + parentFqn + " does not exist!");

      Object nodeName = nodeFqn.getLastElement();

      // now that we have the parent and target nodes:
      // first correct the pointers at the pruning point
      oldParent.removeChild(nodeName);

      // parent pointer is calculated on the fly using Fqns.
      // now adjust Fqns of node and all children.
      Fqn nodeNewFqn = Fqn.fromRelativeElements(parent.getFqn(), nodeFqn.getLastElement());

      // pre-notify
      notifier.notifyNodeMoved(nodeFqn, nodeNewFqn, true, ctx);
      recursiveMoveNode(ctx, node, parent.getFqn(), ws);

      // remove old nodes. this may mark some nodes which have already been moved as deleted
      removeNode(ws, node, false, ctx);

      // post-notify
      notifier.notifyNodeMoved(nodeFqn, nodeNewFqn, false, ctx);
   }

   /**
    * Moves a node to a new base.
    *
    * @param node    node to move
    * @param newBase new base Fqn under which the given node will now exist
    * @param ws      transaction workspace
    */
   private void recursiveMoveNode(InvocationContext ctx, WorkspaceNode node, Fqn newBase, TransactionWorkspace ws)
   {
      Fqn newFqn = Fqn.fromRelativeElements(newBase, node.getFqn().getLastElement());
      WorkspaceNode movedNode = fetchWorkspaceNode(ctx, newFqn, ws, true, true);
      movedNode.putAll(node.getData());

      // invoke children
      for (Object n : node.getChildrenNames())
      {
         WorkspaceNode child = fetchWorkspaceNode(ctx, Fqn.fromRelativeElements(node.getFqn(), n), ws, false, true);
         if (child != null) recursiveMoveNode(ctx, child, newFqn, ws);
      }
   }

   private void putDataMapAndNotify(Map<Object, Object> data, TransactionWorkspace workspace, WorkspaceNode workspaceNode, InvocationContext ctx)
   {
      if (workspaceNode == null)
         throw new NodeNotExistsException("optimisticCreateIfNotExistsInterceptor should have created this node!");
      // pre-notify
      notifier.notifyNodeModified(workspaceNode.getFqn(), true, PUT_MAP, workspaceNode.getData(), ctx);
      workspaceNode.putAll(data);
      workspace.addNode(workspaceNode);
      // post-notify
      notifier.notifyNodeModified(workspaceNode.getFqn(), false, PUT_MAP, workspaceNode.getData(), ctx);
   }

   private Object putDataKeyValueAndNotify(Object key, Object value, TransactionWorkspace workspace, WorkspaceNode workspaceNode, InvocationContext ctx)
   {
      if (workspaceNode == null)
         throw new NodeNotExistsException("optimisticCreateIfNotExistsInterceptor should have created this node!");

      if (notifier.shouldNotifyOnNodeModified())// pre-notify
      {
         notifier.notifyNodeModified(workspaceNode.getFqn(), true, PUT_DATA, workspaceNode.getData(), ctx);
      }

      Object old = workspaceNode.put(key, value);
      workspace.addNode(workspaceNode);

      if (notifier.shouldNotifyOnNodeModified())// post-notify
      {
         Map addedData = Collections.singletonMap(key, value);
         notifier.notifyNodeModified(workspaceNode.getFqn(), false, PUT_DATA, addedData, ctx);
      }

      return old;
   }

   private boolean removeNode(TransactionWorkspace workspace, WorkspaceNode workspaceNode, boolean notify, InvocationContext ctx) throws CacheException
   {
      // it is already removed - we can ignore it
      if (workspaceNode == null) return false;

      Fqn parentFqn = workspaceNode.getFqn().getParent();
      WorkspaceNode parentNode = fetchWorkspaceNode(ctx, parentFqn, workspace, false, true);
      if (parentNode == null) throw new NodeNotExistsException("Unable to find parent node with fqn " + parentFqn);

      // pre-notify
      if (notify) notifier.notifyNodeRemoved(workspaceNode.getFqn(), true, workspaceNode.getData(), ctx);

      Fqn nodeFqn = workspaceNode.getFqn();
      parentNode.removeChild(nodeFqn.getLastElement());

      SortedMap<Fqn, WorkspaceNode> tailMap = workspace.getNodesAfter(workspaceNode.getFqn());

      for (WorkspaceNode toDelete : tailMap.values())
      {
         if (toDelete.getFqn().isChildOrEquals(nodeFqn))
         {
            if (trace) log.trace("marking node " + toDelete.getFqn() + " as deleted");
            toDelete.setRemoved(true);
         }
         else
         {
            break;// no more children, we came to the end
         }
      }

      // post-notify
      if (notify) notifier.notifyNodeRemoved(workspaceNode.getFqn(), false, null, ctx);
      return workspaceNode.getNode().isValid();
   }

   private Object removeKeyAndNotify(Object removeKey, TransactionWorkspace workspace, WorkspaceNode workspaceNode, InvocationContext ctx)
   {
      if (workspaceNode == null) return null;

      if (notifier.shouldNotifyOnNodeModified())// pre-notify
      {
         notifier.notifyNodeModified(workspaceNode.getFqn(), true, REMOVE_DATA, workspaceNode.getData(), ctx);
      }

      Object old = workspaceNode.remove(removeKey);
      workspace.addNode(workspaceNode);

      if (notifier.shouldNotifyOnNodeModified())
      {
         Map removedData = Collections.singletonMap(removeKey, old);
         // post-notify
         notifier.notifyNodeModified(workspaceNode.getFqn(), false, REMOVE_DATA, removedData, ctx);
      }

      return old;
   }

   private void removeDataAndNotify(TransactionWorkspace workspace, WorkspaceNode workspaceNode, InvocationContext ctx)
   {
      if (workspaceNode == null) return;

      Map data = new HashMap(workspaceNode.getData());

      // pre-notify
      notifier.notifyNodeModified(workspaceNode.getFqn(), true, REMOVE_DATA, data, ctx);

      workspaceNode.clearData();
      workspace.addNode(workspaceNode);

      // post-notify
      notifier.notifyNodeModified(workspaceNode.getFqn(), false, REMOVE_DATA, data, ctx);
   }

   // -----------------------------------------------------------------

   // Methods to help retrieval of nodes from the transaction workspace.

   // -----------------------------------------------------------------

   /**
    * Retrieves a node for a given Fqn from the workspace.  If the node does not exist in the workspace it is retrieved
    * from the cache's data structure.  Note that at no point is a NEW node created in the underlying data structure.
    * That is up to the {@link org.jboss.cache.interceptors.OptimisticCreateIfNotExistsInterceptor}.
    * <p/>
    * If the node requested is in the workspace but marked as deleted, this method will NOT retrieve it, unless undeleteIfNecessary
    * is true, in which case the node's <tt>deleted</tt> property is set to false first before being retrieved.
    *
    * @param fqn                 Fqn of the node to retrieve
    * @param workspace           transaction workspace to look in
    * @param undeleteIfNecessary if the node is in the workspace but marked as deleted, this meth
    * @param includeInvalidNodes
    * @return a node, if found, or null if not.
    */
   private WorkspaceNode fetchWorkspaceNode(InvocationContext ctx, Fqn fqn, TransactionWorkspace workspace, boolean undeleteIfNecessary, boolean includeInvalidNodes)
   {
      WorkspaceNode workspaceNode = workspace.getNode(fqn);
      // if we do not have the node then we need to add it to the workspace
      if (workspaceNode == null)
      {
         NodeSPI node = dataContainer.peek(fqn, true, includeInvalidNodes);
         if (node == null) return null;
         GlobalTransaction gtx = ctx.getGlobalTransaction();
         workspaceNode = lockAndCreateWorkspaceNode(nodeFactory, node, workspace, gtx, lockAcquisitionTimeout);

         // and add the node to the workspace.
         workspace.addNode(workspaceNode);
      }

      // Check that the workspace node has been marked as deleted.
      if (workspaceNode.isRemoved())
      {
         if (trace) log.trace("Node " + fqn + " has been deleted in the workspace.");
         if (undeleteIfNecessary)
         {
            undeleteWorkspaceNode(workspaceNode, fetchWorkspaceNode(ctx, fqn.getParent(), workspace, undeleteIfNecessary, includeInvalidNodes));
         }
         else if (!includeInvalidNodes)
         {
            // don't return deleted nodes if undeleteIfNecessary is false!
            workspaceNode = null;
         }
      }

      // set implicit node versioning flag.
      if (workspaceNode != null && !(workspaceNode.getVersion() instanceof DefaultDataVersion))
      {
         workspaceNode.setVersioningImplicit(false);
      }

      // now make sure all parents are in the wsp as well
      if (workspaceNode != null && !fqn.isRoot())
         fetchWorkspaceNode(ctx, fqn.getParent(), workspace, false, includeInvalidNodes);

      return workspaceNode;
   }
}
