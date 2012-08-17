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

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class NullLock implements Lock, Condition
{

   public void lock()
   {
   }

   public void lockInterruptibly() throws InterruptedException
   {
   }

   public Condition newCondition()
   {
      return this;
   }

   public boolean tryLock()
   {
      return true;
   }

   public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException
   {
      return true;
   }

   public void unlock()
   {
   }

   public void await() throws InterruptedException
   {
   }

   public boolean await(long arg0, TimeUnit arg1) throws InterruptedException
   {
      return true;
   }

   public long awaitNanos(long arg0) throws InterruptedException
   {
      return arg0;
   }

   public void awaitUninterruptibly()
   {
   }

   public boolean awaitUntil(Date arg0) throws InterruptedException
   {
      return true;
   }

   public void signal()
   {
   }

   public void signalAll()
   {
   }
}
