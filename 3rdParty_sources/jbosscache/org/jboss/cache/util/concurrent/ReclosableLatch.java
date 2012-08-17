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
package org.jboss.cache.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * This latch allows you to set a default state (open or closed), and repeatedly open or close
 * the latch and have threads wait on it.
 * <p/>
 * This is a better impl of the old <tt>org.jboss.cache.util.ThreadGate</tt> (which doesn't exist anymore), that uses an
 * {@link java.util.concurrent.locks.AbstractQueuedSynchronizer} while ThreadGate used to use explicit {@link java.util.concurrent.locks.Lock}
 * objects.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class ReclosableLatch extends AbstractQueuedSynchronizer
{
   private static final long serialVersionUID = 1744280161777661090l;
   // the following states are used in the AQS.
   private static final int OPEN_STATE = 0, CLOSED_STATE = 1;

   public ReclosableLatch()
   {
      setState(CLOSED_STATE);
   }

   public ReclosableLatch(boolean defaultOpen)
   {
      setState(defaultOpen ? OPEN_STATE : CLOSED_STATE);
   }

   @Override
   public final int tryAcquireShared(int ignored)
   {
      // return 1 if we allow the requestor to proceed, -1 if we want the requestor to block.
      return getState() == OPEN_STATE ? 1 : -1;
   }

   @Override
   public final boolean tryReleaseShared(int state)
   {
      // used as a mechanism to set the state of the Sync.
      setState(state);
      return true;
   }

   public final void open()
   {
      // do not use setState() directly since this won't notify parked threads.
      releaseShared(OPEN_STATE);
   }

   public final void close()
   {
      // do not use setState() directly since this won't notify parked threads.
      releaseShared(CLOSED_STATE);
   }

   public final void await() throws InterruptedException
   {
      acquireSharedInterruptibly(1); // the 1 is a dummy value that is not used.
   }

   public final boolean await(long time, TimeUnit unit) throws InterruptedException
   {
      return tryAcquireSharedNanos(1, unit.toNanos(time)); // the 1 is a dummy value that is not used.
   }

   public boolean isOpen()
   {
      return getState() == OPEN_STATE;
   }
}
