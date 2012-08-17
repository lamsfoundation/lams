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

//import org.jboss.logging.Logger;

/**
 * Lock strategy of Serializable that prevents dirty read, non-repeatable read, and
 * phantom read.
 * <p> Dirty read allows (t1) write and then (t2) read within two separate threads, all without
 * transaction commit. </p>
 * <p> Non-repeatable read allows (t1) read, (t2) write, and then (t1) read, all without
 * transaction commit. </p>
 * <p> Phantom read allows (t1) read n rows, (t2) insert k rows, and (t1) read n+k rows.</p>
 *
 * @author <a href="mailto:bwang00@sourceforge.net">Ben Wang</a> July 15, 2003
 * @version $Revision$
 */
public class LockStrategySerializable implements LockStrategy
{
//    Log log=LogFactory.getLog(getClass());

   private SemaphoreLock sem_;

   public LockStrategySerializable()
   {
      sem_ = new SemaphoreLock(1);
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#readLock()
    */
   public Lock readLock()
   {
      return sem_;
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#upgradeLockAttempt(long)
    */
   public Lock upgradeLockAttempt(long msecs) throws UpgradeException
   {
      // If we come to this far, that means the thread owns a rl already
      // so we just return the same lock
      return sem_;
   }

   /**
    * @see org.jboss.cache.lock.LockStrategy#writeLock()
    */
   public Lock writeLock()
   {
      return sem_;
   }
}
