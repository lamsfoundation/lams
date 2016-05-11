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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.LinkedList;

/**
 * Maintains a list of Synchronization handlers. Reason is that we have to
 * invoke certain handlers <em>before</em> others. See the description in
 * SyncTxUnitTestCase.testConcurrentPuts(). For example, for synchronous
 * replication, we have to execute the ReplicationInterceptor's
 * afterCompletion() <em>before</em> the TransactionInterceptor's.
 *
 * @author Bela Ban
 *
 */
public class OrderedSynchronizationHandler implements Synchronization
{
   static final Log log = LogFactory.getLog(OrderedSynchronizationHandler.class);

   private Transaction tx = null;
   private final LinkedList<Synchronization> handlers = new LinkedList<Synchronization>();

   public OrderedSynchronizationHandler(Transaction tx) throws SystemException, RollbackException
   {
      this.tx = tx;
      tx.registerSynchronization(this);
   }

   public void registerAtHead(Synchronization handler)
   {
      register(handler, true);
   }

   public void registerAtTail(Synchronization handler)
   {
      register(handler, false);
   }

   void register(Synchronization handler, boolean head)
   {
      if (handler != null && !handlers.contains(handler))
      {
         if (head)
            handlers.addFirst(handler);
         else
            handlers.addLast(handler);
      }
   }

   public void beforeCompletion()
   {
      for (Synchronization sync : handlers)
      {
         sync.beforeCompletion();
      }
   }

   public void afterCompletion(int status)
   {
      RuntimeException exceptionInAfterCompletion = null;
      for (Synchronization sync : handlers)
      {
         try
         {
            sync.afterCompletion(status);
         }
         catch (Throwable t)
         {
            log.error("failed calling afterCompletion() on " + sync, t);
            exceptionInAfterCompletion = (RuntimeException) t;
         }
      }

      // throw the exception so the TM can deal with it.
      if (exceptionInAfterCompletion != null) throw exceptionInAfterCompletion;
   }

   @Override
   public String toString()
   {
      return "tx=" + getTxAsString() + ", handlers=" + handlers;
   }

   private String getTxAsString()
   {
      // JBCACHE-1114 -- don't call toString() on tx or it can lead to stack overflow
      if (tx == null)
         return null;

      return tx.getClass().getName() + "@" + System.identityHashCode(tx);
   }
}
