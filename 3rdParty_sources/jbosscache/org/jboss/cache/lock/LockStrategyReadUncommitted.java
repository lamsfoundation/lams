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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Transaction isolation level of READ-UNCOMMITTED. Reads always succeed (NullLock), whereas writes are exclusive.
 * It prevents none of the dirty read, non-repeatable read, or phantom read.
 *
 * @author Ben Wang
 * @version $Revision$
 */
public class LockStrategyReadUncommitted implements LockStrategy
{
   private SemaphoreLock wLock_;
   private Lock rLock_; // Null lock will always return true

   public LockStrategyReadUncommitted()
   {
      wLock_ = new SemaphoreLock(1);
      rLock_ = new NullLock();
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#readLock()
    */
   public Lock readLock()
   {
      return rLock_;
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#upgradeLockAttempt(long)
    */
   public Lock upgradeLockAttempt(long msecs) throws UpgradeException
   {
      // Since write is exclusive, we need to obtain the write lock first
      // before we can return the upgrade
      try
      {
         wLock_.tryLock(msecs, TimeUnit.MILLISECONDS);
      }
      catch (InterruptedException e)
      {
         throw new UpgradeException("Upgrade failed in " + msecs + " msecs", e);
      }
      return wLock_;
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#writeLock()
    */
   public Lock writeLock()
   {
      return wLock_;
   }
}
