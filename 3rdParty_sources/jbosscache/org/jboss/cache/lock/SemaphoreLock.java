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

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Implements most of the methods of Lock using the {@link Semaphore} implementation.
 */
public class SemaphoreLock extends Semaphore implements Lock
{

   /**
    * It's unclear why this class would be serialized.
    */
   private static final long serialVersionUID = -15293253129957476L;

   public SemaphoreLock(int permits)
   {
      super(permits);
   }

   public void lock()
   {
      try
      {
         acquire();
      }
      catch (InterruptedException e)
      {
         lock(); // recursive, I know ..
      }
   }

   public void lockInterruptibly() throws InterruptedException
   {
      acquire();
   }

   public Condition newCondition()
   {
      throw new UnsupportedOperationException();
   }

   public boolean tryLock()
   {
      return tryAcquire();
   }

   public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException
   {
      return tryAcquire(arg0, arg1);
   }

   public void unlock()
   {
      release();
   }

}
