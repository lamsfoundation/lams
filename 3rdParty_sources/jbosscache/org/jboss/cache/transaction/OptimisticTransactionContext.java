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
package org.jboss.cache.transaction;

import org.jboss.cache.optimistic.TransactionWorkspace;
import org.jboss.cache.optimistic.TransactionWorkspaceImpl;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * Subclasses the {@link PessimisticTransactionContext} class to add a {@link TransactionWorkspace}.  Used with optimistic locking
 * where each call is assigned a trasnaction and a transaction workspace.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:stevew@jofti.com">Steve Woodcock (stevew@jofti.com)</a>
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class OptimisticTransactionContext extends PessimisticTransactionContext
{
   private TransactionWorkspace transactionWorkSpace = new TransactionWorkspaceImpl();

   public OptimisticTransactionContext(Transaction tx) throws SystemException, RollbackException
   {
      super(tx);
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder(super.toString());
      sb.append("\nworkspace: ").append(transactionWorkSpace);
      return sb.toString();
   }

   /**
    * @return Returns the transactionWorkSpace.
    */
   public TransactionWorkspace getTransactionWorkSpace()
   {
      return transactionWorkSpace;
   }

   /**
    * @param transactionWorkSpace The transactionWorkSpace to set.
    */
   public void setTransactionWorkSpace(TransactionWorkspace transactionWorkSpace)
   {
      this.transactionWorkSpace = transactionWorkSpace;
   }

}
