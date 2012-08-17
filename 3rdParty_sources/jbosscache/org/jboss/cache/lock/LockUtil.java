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
package org.jboss.cache.lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.transaction.TransactionTable;

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LockUtil
{
   private final static Log log = LogFactory.getLog(LockUtil.class);
   private static final boolean trace = log.isTraceEnabled();

   private static interface TransactionLockStatus extends Status
   {
      int STATUS_BROKEN = Integer.MIN_VALUE;
   }

   public static boolean breakTransactionLock(Fqn fqn,
                                              LockManager lockManager,
                                              GlobalTransaction gtx,
                                              boolean localTx,
                                              TransactionTable tx_table, TransactionManager tm)
   {
      boolean broken = false;
      int tryCount = 0;
      int lastStatus = TransactionLockStatus.STATUS_BROKEN;

      while (!broken && lockManager.ownsLock(fqn, gtx))
      {
         int status = breakTransactionLock(gtx, fqn, lockManager, tx_table, tm, localTx, lastStatus, tryCount);
         if (status == TransactionLockStatus.STATUS_BROKEN)
         {
            broken = true;
         }
         else if (status != lastStatus)
         {
            tryCount = 0;
         }
         lastStatus = status;

         tryCount++;
      }

      return broken;
   }

   /**
    * Attempts to release the lock held by <code>gtx</code> by altering the
    * underlying transaction.  Different strategies will be employed
    * depending on the status of the transaction and param
    * <code>tryCount</code>.  Transaction may be rolled back or marked
    * rollback-only, or the lock may just be broken, ignoring the tx.  Makes an
    * effort to not affect the tx or break the lock if tx appears to be in
    * the process of completion; param <code>tryCount</code> is used to help
    * make decisions about this.
    * <p/>
    * This method doesn't guarantee to have broken the lock unless it returns
    * {@link TransactionLockStatus#STATUS_BROKEN}.
    *
    * @param gtx        the gtx holding the lock
    * @param lastStatus the return value from a previous invocation of this
    *                   method for the same lock, or Status.STATUS_UNKNOW
    *                   for the first invocation.
    * @param tryCount   number of times this method has been called with
    *                   the same gtx, lock and lastStatus arguments. Should
    *                   be reset to 0 anytime lastStatus changes.
    * @return the current status of the Transaction associated with
    *         <code>gtx</code>, or {@link TransactionLockStatus#STATUS_BROKEN}
    *         if the lock held by gtx was forcibly broken.
    */
   private static int breakTransactionLock(GlobalTransaction gtx,
                                           Fqn fqn, LockManager lockManager,
                                           TransactionTable transactionTable,
                                           TransactionManager tm,
                                           boolean localTx,
                                           int lastStatus,
                                           int tryCount)
   {
      int status = Status.STATUS_UNKNOWN;
      Transaction tx = transactionTable.getLocalTransaction(gtx);
      if (tx != null)
      {
         try
         {
            status = tx.getStatus();

            if (status != lastStatus)
            {
               tryCount = 0;
            }

            switch (status)
            {
               case Status.STATUS_ACTIVE:
               case Status.STATUS_MARKED_ROLLBACK:
               case Status.STATUS_PREPARING:
               case Status.STATUS_UNKNOWN:
                  if (tryCount == 0)
                  {
                     if (trace)
                     {
                        log.trace("Attempting to break transaction lock held " +
                              " by " + gtx + " by rolling back local tx");
                     }
                     // This thread has to join the tx
                     tm.resume(tx);
                     try
                     {
                        tx.rollback();
                     }
                     finally
                     {
                        tm.suspend();
                     }

                  }
                  else if (tryCount > 100)
                  {
                     // Something is wrong; our initial rollback call
                     // didn't generate a valid state change; just force it
                     lockManager.unlock(fqn, gtx);
                     status = TransactionLockStatus.STATUS_BROKEN;
                  }
                  break;

               case Status.STATUS_COMMITTING:
               case Status.STATUS_ROLLING_BACK:
                  // We'll try up to 10 times before just releasing
                  if (tryCount < 10)
                  {
                     break;// let it finish
                  }
                  // fall through and release

               case Status.STATUS_COMMITTED:
               case Status.STATUS_ROLLEDBACK:
               case Status.STATUS_NO_TRANSACTION:
                  lockManager.unlock(fqn, gtx);
                  status = TransactionLockStatus.STATUS_BROKEN;
                  break;

               case Status.STATUS_PREPARED:
                  // If the tx was started here, we can still abort the commit,
                  // otherwise we are in the middle of a remote commit() call
                  // and the status is just about to change
                  if (tryCount == 0 && localTx)
                  {
                     // We can still abort the commit
                     if (trace)
                     {
                        log.trace("Attempting to break transaction lock held " +
                              "by " + gtx + " by marking local tx as " +
                              "rollback-only");
                     }
                     tx.setRollbackOnly();
                     break;
                  }
                  else if (tryCount < 10)
                  {
                     // EITHER tx was started elsewhere (in which case we'll
                     // wait a bit to allow the commit() call to finish;
                     // same as STATUS_COMMITTING above)
                     // OR we marked the tx rollbackOnly above and are just
                     // waiting a bit for the status to change
                     break;
                  }

                  // fall through and release
               default:
                  lockManager.unlock(fqn, gtx);
                  status = TransactionLockStatus.STATUS_BROKEN;
            }
         }
         catch (Exception e)
         {
            log.error("Exception breaking locks held by " + gtx, e);
            lockManager.unlock(fqn, gtx);
            status = TransactionLockStatus.STATUS_BROKEN;
         }
      }
      else
      {
         // Race condition; globalTransaction was cleared from txTable.
         // Just double check if globalTransaction still holds a lock
         if (lockManager.ownsLock(fqn, gtx))
         {
            // perhaps we should throw an exception?
            lockManager.unlock(fqn, gtx);
            status = TransactionLockStatus.STATUS_BROKEN;
         }
      }

      return status;
   }

   /**
    * Test if this node needs to be 'undeleted'
    * reverse the "remove" if the node has been previously removed in the same tx, if this operation is a put()
    */
   public static void manageReverseRemove(InvocationContext ctx, NodeSPI childNode, boolean reverseRemoveCheck, List createdNodes, CommandsFactory commandsFactory)
   {
      if (ctx.getGlobalTransaction() != null) //if no tx then reverse remove does not make sense
      {
         Fqn fqn = childNode.getFqn();
         TransactionContext transactionContext = ctx.getTransactionContext();
         boolean needToReverseRemove = reverseRemoveCheck && childNode.isDeleted() && transactionContext != null && transactionContext.getRemovedNodes().contains(fqn);
         if (!needToReverseRemove) return;
         childNode.markAsDeleted(false);
         //if we'll rollback the tx data should be added to the node again
         Map oldData = new HashMap(childNode.getDataDirect());
         PutDataMapCommand command = commandsFactory.buildPutDataMapCommand(ctx.getGlobalTransaction(), fqn, oldData);
         // txTable.get(gtx).addUndoOperation(command); --- now need to make sure this is added to the normal mods list instead
         transactionContext.addModification(command);
         //we're prepared for rollback, now reset the node
         childNode.clearDataDirect();
         if (createdNodes != null)
         {
            createdNodes.add(childNode);
         }
      }
   }
}
