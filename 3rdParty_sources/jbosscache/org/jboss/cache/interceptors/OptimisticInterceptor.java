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
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeFactory;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.lock.LockManager;
import static org.jboss.cache.lock.LockType.READ;
import org.jboss.cache.lock.TimeoutException;
import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.WorkspaceNode;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.OptimisticTransactionContext;
import org.jboss.cache.transaction.TransactionTable;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.List;

/**
 * Abstract interceptor for optimistic locking
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public abstract class OptimisticInterceptor extends CommandInterceptor
{
   protected TransactionManager txManager;
   protected TransactionTable txTable;
   protected LockManager lockManager;

   @Inject
   void injectDependencies(TransactionManager txManager, TransactionTable txTable, LockManager lockManager)
   {
      this.txManager = txManager;
      this.txTable = txTable;
      this.lockManager = lockManager;
   }

   protected TransactionWorkspace getTransactionWorkspace(InvocationContext ctx) throws CacheException
   {
      OptimisticTransactionContext transactionContext = (OptimisticTransactionContext) ctx.getTransactionContext();

      if (transactionContext == null)
      {
         throw new CacheException("Unable to map global transaction " + ctx.getGlobalTransaction() + " to transaction entry when trying to retrieve transaction workspace.");
      }

      // try and get the workspace from the transaction
      return transactionContext.getTransactionWorkSpace();
   }

   /**
    * Adds the Fqn of the node as well as all children and childrens children to the list.
    */
   protected void greedyGetFqns(List<Fqn> list, NodeSPI<?, ?> n, Fqn newBase)
   {
      list.add(n.getFqn());
      Fqn newFqn = Fqn.fromRelativeElements(newBase, n.getFqn().getLastElement());
      list.add(newFqn);

      for (NodeSPI child : n.getChildrenDirect())
      {
         greedyGetFqns(list, child, newFqn);
      }
   }

   /**
    * @return the {@link org.jboss.cache.transaction.GlobalTransaction}, extracted from the current {@link org.jboss.cache.InvocationContext}.
    * @throws CacheException if the {@link org.jboss.cache.transaction.GlobalTransaction} or {@link javax.transaction.Transaction} associated with the
    *                        {@link org.jboss.cache.InvocationContext} is null.
    */
   protected GlobalTransaction getGlobalTransaction(InvocationContext ctx) throws CacheException
   {
      Transaction tx = ctx.getTransaction();
      if (tx == null) throw new CacheException("Transaction associated with the current invocation is null!");
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      if (gtx == null) throw new CacheException("GlobalTransaction associated with the current invocation is null!");
      return gtx;
   }

   protected void undeleteWorkspaceNode(WorkspaceNode nodeToUndelete, TransactionWorkspace workspace)
   {
      undeleteWorkspaceNode(nodeToUndelete, workspace.getNode(nodeToUndelete.getFqn().getParent()));
   }

   /**
    * Undeletes a node that already exists in the workspace, by setting appropriate flags and re-adding to parent's child map.
    *
    * @param nodeToUndelete WorkspaceNode to undelete
    * @param parent         parent of node to undelete
    */
   @SuppressWarnings("unchecked")
   protected void undeleteWorkspaceNode(WorkspaceNode nodeToUndelete, WorkspaceNode parent)
   {
      nodeToUndelete.setRemoved(false);
      nodeToUndelete.clearData();
      // add in parent again
      parent.addChild(nodeToUndelete);
      nodeToUndelete.markAsResurrected(true);
   }

   @SuppressWarnings("unchecked")
   protected WorkspaceNode lockAndCreateWorkspaceNode(NodeFactory<?, ?> nodeFactory, NodeSPI node, TransactionWorkspace<?, ?> workspace, GlobalTransaction gtx, long timeout)
   {
      boolean locked;
      try
      {
         locked = lockManager.lock(node, READ, gtx, timeout);
      }
      catch (InterruptedException e)
      {
         // test if we acquired the lock
         locked = lockManager.getReadOwners(node).contains(gtx);
      }

      if (!locked)
         throw new TimeoutException("Unable to lock node " + node.getFqn() + " after timeout " + timeout + " for copying into workspace");

      WorkspaceNode<?, ?> wn = nodeFactory.createWrappedNode(node, workspace);

      lockManager.unlock(node, gtx);
      return wn;
   }


}
