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

import org.jboss.cache.CacheException;

import java.util.Map;


/**
 * Used for all locking-related exceptions, e.g. when  a lock could not be
 * acquired within the timeout, or when a deadlock was detected or an upgrade failed.
 *
 * @author <a href="mailto:bela@jboss.org">Bela Ban</a>.
 */

public class LockingException extends CacheException
{

   private static final long serialVersionUID = 5551396474793902133L;

   /**
    * A list of all nodes that failed to acquire a lock
    */
   Map failed_lockers = null;

   public LockingException()
   {
      super();
   }

   public LockingException(Map failed_lockers)
   {
      super();
      this.failed_lockers = failed_lockers;
   }

   public LockingException(String msg)
   {
      super(msg);
   }

   public LockingException(String msg, Map failed_lockers)
   {
      super(msg);
      this.failed_lockers = failed_lockers;
   }

   public LockingException(String msg, Throwable cause)
   {
      super(msg, cause);
   }

   public LockingException(String msg, Throwable cause, Map failed_lockers)
   {
      super(msg, cause);
      this.failed_lockers = failed_lockers;
   }

   @Override
   public String toString()
   {
      String retval = super.toString();
      if (failed_lockers != null && failed_lockers.size() > 0)
         retval = retval + ", failed lockers: " + failed_lockers;
      return retval;
   }

}
