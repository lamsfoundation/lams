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

/**
 * NonBlockingWriterLock is a read/write lock (with upgrade) that has
 * non-blocking write lock acquisition on existing read lock(s).
 * <p>Note that the write lock is exclusive among write locks, e.g.,
 * only one write lock can be granted at one time, but the write lock
 * is independent of the read locks. For example,
 * a read lock to be acquired will be blocked if there is existing write lock, but
 * will not be blocked if there are mutiple read locks already granted to other
 * owners. On the other hand, a write lock can be acquired as long as there
 * is no existing write lock, regardless how many read locks have been
 * granted.
 *
 * @author Ben Wang
 *
 */
public class NonBlockingWriterLock extends ReadWriteLockWithUpgrade
{

   // Only need to overwrite this method so WL is not blocked on RL.
   @Override
   protected synchronized boolean startWrite()
   {
      boolean allowWrite = (activeWriter_ == null);
      if (allowWrite) activeWriter_ = Thread.currentThread();
      return allowWrite;
   }
}
