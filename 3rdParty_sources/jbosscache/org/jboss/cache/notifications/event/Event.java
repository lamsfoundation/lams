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
package org.jboss.cache.notifications.event;

import org.jboss.cache.Cache;

/**
 * An interface that defines common characteristics of events
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 */
public interface Event
{
   static enum Type
   {
      CACHE_STARTED, CACHE_STOPPED, CACHE_BLOCKED, CACHE_UNBLOCKED, NODE_ACTIVATED, NODE_PASSIVATED,
      NODE_LOADED, NODE_EVICTED, NODE_CREATED, NODE_REMOVED, NODE_MODIFIED, NODE_MOVED, NODE_VISITED,
      TRANSACTION_COMPLETED, TRANSACTION_REGISTERED, VIEW_CHANGED, BUDDY_GROUP_CHANGED, NODE_INVALIDATED
   }

   /**
    * @return the type of event represented by this instance.
    */
   Type getType();

   /**
    * @return true if the notification is before the event has occured, false if after the event has occured.
    */
   boolean isPre();

   /**
    * @return a handle to the cache instance that generated this notification.
    */
   Cache getCache();
}
