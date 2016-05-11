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

import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.DataCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.legacy.ReversibleCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.PrePostProcessingCommandInterceptor;
import org.jboss.cache.lock.IsolationLevel;
import org.jboss.cache.lock.LockManager;
import static org.jboss.cache.lock.LockType.READ;
import static org.jboss.cache.lock.LockType.WRITE;
import org.jboss.cache.lock.LockUtil;
import org.jboss.cache.lock.PessimisticNodeBasedLockManager;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.PessimisticTransactionContext;
import org.jboss.cache.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * An interceptor that handles locking. When a TX is associated, we register
 * for TX completion and unlock the locks acquired within the scope of the TX.
 * When no TX is present, we keep track of the locks acquired during the
 * current method and unlock when the method returns.
 *
 * @author Bela Ban
 *
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessimisticLockInterceptor extends PrePostProcessingCommandInterceptor
{
   private DataContainer dataContainer;
   private PessimisticNodeBasedLockManager lockManager;
   private CommandsFactory commandsFactory;

   @Inject
   public void injectDependencies(DataContainer dataContainer, LockManager lockManager, CommandsFactory commandsFactory)
   {
      this.dataContainer = dataContainer;
      this.lockManager = (PessimisticNodeBasedLockManager) lockManager;
      this.commandsFactory = commandsFactory;
   }

   @Override
   protected Object handlePutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handlePutCommand(ctx, command, false);
   }

   @Override
   protected Object handlePutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handlePutCommand(ctx, command, false);
   }

   @Override
   protected Object handlePutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handlePutCommand(ctx, command, true);
   }

   private Object handlePutCommand(InvocationContext ctx, DataCommand command, boolean zeroAcquisitionTimeout)
         throws Throwable
   {
      if ((ctx.isLockingSuppressed()) || configuration.getIsolationLevel() == IsolationLevel.NONE)
      {
         if (trace) log.trace("Suppressing locking, creating nodes if necessary");
         int treeNodeSize = command.getFqn().size();
         NodeSPI n = dataContainer.getRoot();
         for (int i = 0; i < treeNodeSize; i++)
         {
            Object childName = command.getFqn().get(i);
            Fqn childFqn = Fqn.fromElements(childName);
            NodeSPI childNode = n.getChildDirect(childFqn);
            if (childNode == null) childNode = n.addChildDirect(childFqn);
            LockUtil.manageReverseRemove(ctx, childNode, true, null, commandsFactory);
            n = childNode;
         }
      }
      else
      {
         lockManager.lockPessimistically(ctx, command.getFqn(), WRITE, true,
               zeroAcquisitionTimeout, false, true, null, false);
      }
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handlePrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      // 2-phase commit prepares are no-ops here.
      if (!command.isOnePhaseCommit()) return invokeNextInterceptor(ctx, command);

      // commit propagated up from the tx interceptor
      commit(ctx.getTransactionContext(), ctx.getGlobalTransaction());
      Object retVal = invokeNextInterceptor(ctx, command);
      lockManager.unlock(ctx);
      return retVal;
   }

   @Override
   protected Object handleCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      commit(ctx.getTransactionContext(), command.getGlobalTransaction());
      if (trace) log.trace("bypassed locking as method commit() doesn't require locking");
      Object retVal = invokeNextInterceptor(ctx, command);
      lockManager.unlock(ctx);
      return retVal;
   }

   @Override
   protected Object handleRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      TransactionContext transactionContext = ctx.getTransactionContext();
      if (trace)
      {
         log.trace("called to rollback cache with GlobalTransaction=" + command.getGlobalTransaction());
      }
      if (transactionContext == null)
      {
         log.error("transactionContext for transaction " + command.getGlobalTransaction() + " not found (transaction has possibly already been rolled back)");
      }
      else
      {
         for (Fqn fqn : transactionContext.getRemovedNodes())
         {
            dataContainer.removeFromDataStructure(fqn, false);
         }
         // 1. Revert the modifications by running the undo-op list in reverse. This *cannot* throw any exceptions !

         undoOperations((PessimisticTransactionContext) transactionContext);
      }
      if (trace)
      {
         log.trace("bypassed locking as method rollback() doesn't require locking");
      }
      Object retVal = invokeNextInterceptor(ctx, command);
      lockManager.unlock(ctx);
      return retVal;
   }

   private void undoOperations(PessimisticTransactionContext transactionContext)
   {
      List<WriteCommand> modificationList = transactionContext.getAllModifications();

      if (modificationList.isEmpty())
      {
         if (trace) log.trace("Modification list is null, no modifications in this transaction!");
         return;
      }

      if (trace) log.trace("undoOperations " + modificationList);

      ArrayList<WriteCommand> copy;
      copy = new ArrayList<WriteCommand>(modificationList);
      RuntimeException exc = null;
      for (ListIterator<WriteCommand> i = copy.listIterator(copy.size()); i.hasPrevious();)
      {
         WriteCommand undoOp = i.previous();
         // since we are using pessimistic locking, all pessimistic WriteCommands implement ReversibleCommand.
         ReversibleCommand txCommand = (ReversibleCommand) undoOp;
         if (log.isDebugEnabled()) log.debug("Calling rollback() on command " + undoOp);
         try
         {
            txCommand.rollback();
         }
         catch (RuntimeException re)
         {
            exc = re;
         }
      }
      if (exc != null) throw exc;
   }

   @Override
   protected Object handleMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      if (ctx.isLockingSuppressed()) return invokeNextInterceptor(ctx, command);

      // this call will ensure the node gets a WL and its current parent gets RL.
      if (trace) log.trace("Attempting to get WL on node to be moved [" + command.getFqn() + "]");
      if (command.getFqn() != null && !(configuration.getIsolationLevel() == IsolationLevel.NONE))
      {
         lockManager.lockPessimistically(ctx, command.getFqn(), WRITE, false, false, true, false, null, false);
         if (ctx.getGlobalTransaction() != null)
         {
            ctx.getTransactionContext().addRemovedNode(command.getFqn());
         }
         lockManager.lockAllAndRecord(dataContainer.peek(command.getFqn(), true, false), WRITE, ctx);
      }
      if (command.getTo() != null && !(configuration.getIsolationLevel() == IsolationLevel.NONE))
      {
         //now for an RL for the new parent.
         if (trace) log.trace("Attempting to get RL on new parent [" + command.getTo() + "]");
         lockManager.lockPessimistically(ctx, command.getTo(), READ, false, false, false, false, null, false);
         lockManager.lockAllAndRecord(dataContainer.peek(command.getTo(), true, false), READ, ctx);
      }
      Object retValue = invokeNextInterceptor(ctx, command);

      if (ctx.getTransaction() == null) // not transactional
      {
         // do a REAL remove here.
         NodeSPI n = dataContainer.peek(command.getFqn(), true, false);
         if (n != null) lockManager.unlockAll(n, Thread.currentThread());
      }

      return retValue;
   }

   @Override
   protected Object handleRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      if (ctx.isLockingSuppressed()) return invokeNextInterceptor(ctx, command);
      // need to make a note of ALL nodes created here!!
      List<NodeSPI> createdNodes = new LinkedList<NodeSPI>();
      // we need to mark new nodes created as deleted since they are only created to form a path to the node being removed, to
      // create a lock.
      boolean created = lockManager.lockPessimistically(ctx, command.getFqn(), WRITE, true, false, true, true, createdNodes, true);
      TransactionContext transactionContext = null;
      if (ctx.getGlobalTransaction() != null)
      {
         transactionContext = ctx.getTransactionContext();
         transactionContext.addRemovedNode(command.getFqn());
         for (NodeSPI nodeSPI : createdNodes)
         {
            transactionContext.addRemovedNode(nodeSPI.getFqn());
            nodeSPI.markAsDeleted(true);
         }
      }

      lockAllForRemoval(dataContainer.peek(command.getFqn(), false, false), ctx, transactionContext);

      if (!createdNodes.isEmpty())
      {
         if (trace) log.trace("There were new nodes created, skipping notification on delete");
         if (trace)
            log.trace("Changing 'skipNotification' for command 'remove' from " + command.isSkipSendingNodeEvents() + " to true");
         command.setSkipSendingNodeEvents(true);
      }

      Object retVal = invokeNextInterceptor(ctx, command);
      // and make sure we remove all nodes we've created for the sake of later removal.
      if (ctx.getGlobalTransaction() == null)
      {
         for (NodeSPI nodeSPI : createdNodes) dataContainer.removeFromDataStructure(nodeSPI.getFqn(), true);
         dataContainer.removeFromDataStructure(command.getFqn(), true);

         NodeSPI n = dataContainer.peek(command.getFqn(), true, false);
         if (n != null) lockManager.unlockAll(n, Thread.currentThread());
      }
      // if this is a delete op and we had to create the node, return a FALSE as nothing *really* was deleted!
      return created ? false : retVal;
   }

   /**
    * Acquires write locks on the node and all child nodes, adding children to the list of removed nodes in the context.
    *
    * @param node               node to inspect
    * @param ctx                invocation context
    * @param transactionContext transaction entry
    * @throws InterruptedException in the event of interruption
    */
   @SuppressWarnings("unchecked")
   public void lockAllForRemoval(NodeSPI node, InvocationContext ctx, TransactionContext transactionContext) throws InterruptedException
   {
      if (node == null) return;
      // lock node
      lockManager.lockAndRecord(node, WRITE, ctx);

      // add to deleted list
      if (transactionContext != null) transactionContext.addRemovedNode(node.getFqn());

      // now children.
      Map<Object, NodeSPI> children = node.getChildrenMapDirect();
      for (NodeSPI child : children.values())
      {
         // lock child.
         lockAllForRemoval(child, ctx, transactionContext);
      }
   }

   @Override
   protected Object handleRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), WRITE, false, false, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), WRITE, false, false, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), WRITE, false, true, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), READ, false, false, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), READ, false, false, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), READ, false, false, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   protected Object handleGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      lockManager.lockPessimistically(ctx, command.getFqn(), READ, false, false, false, false, null, false);
      return invokeNextInterceptor(ctx, command);
   }

   @Override
   public void doAfterCall(InvocationContext ctx, VisitableCommand command)
   {
      if (!ctx.isValidTransaction()) lockManager.unlock(ctx);
   }

   /**
    * Remove all locks held by <tt>tx</tt>, remove the transaction from the transaction table
    */
   private void commit(TransactionContext transactionContext, GlobalTransaction gtx)
   {
      if (trace) log.trace("committing cache with gtx " + gtx);
      if (transactionContext == null)
      {
         log.error("transactionContext for transaction " + gtx + " not found (maybe already committed)");
         return;
      }
      // first remove nodes that should be deleted.
      for (Fqn fqn : transactionContext.getRemovedNodes())
      {
         dataContainer.removeFromDataStructure(fqn, false);
      }
   }

}
