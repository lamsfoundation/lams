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

import java.util.concurrent.locks.Lock;

/**
 * Transaction isolation level of Repeatable_Read. It prevents dirty read and non-repeatable read.
 * <p> Dirty read allows (t1) write and then (t2) read within two separate threads, all without
 * transaction commit. </p>
 * <p> Non-repeatable allows read allows (t1) read, (t2) write, and then (t1) read, all without
 * transaction commit. </p>
 *
 * @author Ben Wang
 * @version $Revision$
 */
public class LockStrategyRepeatableRead implements LockStrategy
{
   private ReadWriteLockWithUpgrade lock_; // Delegate is ReadWriteLockWithUpgrade

   public LockStrategyRepeatableRead()
   {
      lock_ = new ReadWriteLockWithUpgrade();
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#readLock()
    */
   public Lock readLock()
   {
      return lock_.readLock();
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#upgradeLockAttempt(long)
    */
   public Lock upgradeLockAttempt(long msecs) throws UpgradeException
   {
      return lock_.upgradeLockAttempt(msecs);
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#writeLock()
    */
   public Lock writeLock()
   {
      return lock_.writeLock();
   }


   @Override
   public String toString()
   {
      return lock_ != null ? lock_.toString() : "(null)";
   }
}
