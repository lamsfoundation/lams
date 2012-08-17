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
package org.jboss.cache.batch;

import org.jboss.cache.CacheException;
import org.jboss.cache.factories.annotations.Inject;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * A container for holding thread locals for batching, to be used with the {@link org.jboss.cache.Cache#startBatch()} and
 * {@link org.jboss.cache.Cache#endBatch(boolean)} calls.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class BatchContainer
{
   TransactionManager transactionManager;
   private ThreadLocal<Transaction> batchTransactionContainer = new ThreadLocal<Transaction>();

   @Inject
   void inject(TransactionManager transactionManager)
   {
      this.transactionManager = transactionManager;
   }

   public void startBatch() throws CacheException
   {
      try
      {
         if (transactionManager.getTransaction() != null) return;
         if (batchTransactionContainer.get() == null)
         {
            transactionManager.begin();
            batchTransactionContainer.set(transactionManager.suspend());
         }
      }
      catch (Exception e)
      {
         throw new CacheException("Unable to start batch", e);
      }
   }

   public void endBatch(boolean success)
   {
      Transaction tx = batchTransactionContainer.get();
      if (tx == null) return;
      Transaction existingTx = null;
      try
      {
         existingTx = transactionManager.getTransaction();
         transactionManager.resume(tx);
         if (success)
            tx.commit();
         else
            tx.rollback();
      }
      catch (Exception e)
      {
         throw new CacheException("Unable to end batch", e);
      }
      finally
      {
         batchTransactionContainer.remove();
         try
         {
            if (existingTx != null) transactionManager.resume(existingTx);
         }
         catch (Exception e)
         {
            throw new CacheException("Failed resuming existing transaction " + existingTx, e);
         }
      }
   }

   public Transaction getBatchTransaction()
   {
      return batchTransactionContainer.get();
   }
}
