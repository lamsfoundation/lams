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
package org.jboss.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Various states that an object that has a four stage lifecycle
 * (i.e. <code>create()</code>, <code>start()</code>, <code>stop()</code>
 * and <code>destroy()</code>) might be in.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public enum CacheStatus
{
   /**
    * Object has been instantiated, but create() has not been called.
    */
   INSTANTIATED,
   /**
    * The <code>create()</code> method has been called but not yet completed.
    */
   CREATING,
   /**
    * The <code>create()</code> method has been completed but
    * <code>start()</code> has not been called.
    */
   CREATED,
   /**
    * The <code>start()</code> method has been called but has not yet completed.
    */
   STARTING,
   /**
    * The <code>start()</code> method has completed.
    */
   STARTED,
   /**
    * The <code>stop()</code> method has been called but has not yet completed.
    */
   STOPPING,
   /**
    * The <code>stop()</code> method has completed but <code>destroy()</code>
    * has not yet been called. Conceptually equivalent to {@link #CREATED}.
    */
   STOPPED,
   /**
    * The <code>destroy()</code> method has been called but has not yet completed.
    */
   DESTROYING,
   /**
    * The <code>destroy()</code> method has completed.
    * Conceptually equivalent to {@link #INSTANTIATED}.
    */
   DESTROYED,
   /**
    * A failure occurred during the execution of <code>create()</code>,
    * <code>start()</code>, <code>stop()</code> or <code>destroy()</code>.
    * The next logical transition is to call <code>destroy()</code>.
    */
   FAILED;

   private static final Log log = LogFactory.getLog(CacheStatus.class);

   public boolean createAllowed()
   {
      switch (this)
      {
         case CREATING:
         case CREATED:
         case STARTING:
         case STARTED:
         case STOPPED:
         case FAILED:
         case STOPPING:
         case DESTROYING:
            return false;
         default:
            return true;
      }
   }

   public boolean needToDestroyFailedCache()
   {
      if (this == CacheStatus.FAILED)
      {
         log.debug("need to call destroy() since current state is " +
               this);
         return true;
      }

      return false;
   }

   public boolean startAllowed()
   {
      switch (this)
      {
         case INSTANTIATED:
         case DESTROYED:
         case STARTING:
         case STARTED:
         case FAILED:
         case STOPPING:
         case DESTROYING:
            return false;
         default:
            return true;
      }
   }

   public boolean needCreateBeforeStart()
   {
      switch (this)
      {
         case INSTANTIATED:
         case DESTROYED:
            log.debug("start() called while current state is " +
                  this + " -- call create() first");
            return true;
         default:
            return false;
      }
   }

   public boolean stopAllowed()
   {
      switch (this)
      {
         case INSTANTIATED:
         case CREATED:
         case STOPPING:
         case STOPPED:
         case DESTROYED:
            log.debug("Ignoring call to stop() as current state is " + this);
            return false;
         case CREATING:
         case STARTING:
         case DESTROYING:
            log.warn("Ignoring call to stop() as current state is " + this);
            return false;
         case FAILED:
         case STARTED:
         default:
            return true;
      }

   }

   public boolean destroyAllowed()
   {
      switch (this)
      {
         case INSTANTIATED:
         case DESTROYING:
         case DESTROYED:
            log.debug("Ignoring call to destroy() as current state is " + this);
            return false;
         case CREATING:
         case STARTING:
         case STOPPING:
            log.warn("Ignoring call to destroy() as current state is " + this);
            return false;
         case STARTED:
            // stop first
            return false;
         case CREATED:
         case STOPPED:
         case FAILED:
         default:
            return true;
      }
   }

   public boolean needStopBeforeDestroy()
   {
      if (this == CacheStatus.STARTED)
      {
         log.warn("destroy() called while current state is " +
               this + " -- call stop() first");
         return true;
      }

      return false;
   }

   public boolean allowInvocations()
   {
      return (this == CacheStatus.STARTED);
   }
}
