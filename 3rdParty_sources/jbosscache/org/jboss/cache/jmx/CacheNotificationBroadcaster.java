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
package org.jboss.cache.jmx;

import javax.management.Notification;
import javax.management.NotificationEmitter;

public interface CacheNotificationBroadcaster extends NotificationEmitter
{
   // Notification Types
   String
         NOTIF_CACHE_STARTED = "org.jboss.cache.CacheStarted",
         NOTIF_CACHE_STOPPED = "org.jboss.cache.CacheStopped",
         NOTIF_NODE_CREATED = "org.jboss.cache.NodeCreated",
         NOTIF_NODE_MODIFIED = "org.jboss.cache.NodeModified",
         NOTIF_NODE_REMOVED = "org.jboss.cache.NodeRemoved",
         NOTIF_NODE_MOVED = "org.jboss.cache.NodeMoved",
         NOTIF_NODE_VISITED = "org.jboss.cache.NodeVisited",
         NOTIF_NODE_EVICTED = "org.jboss.cache.NodeEvicted",
         NOTIF_NODE_LOADED = "org.jboss.cache.NodeLoaded",
         NOTIF_NODE_ACTIVATED = "org.jboss.cache.NodeActivated",
         NOTIF_NODE_PASSIVATED = "org.jboss.cache.NodePassivated",
         NOTIF_VIEW_CHANGED = "org.jboss.cache.ViewChanged";

   /**
    * Sends a notification to any interested NotificationListener.
    *
    * @param notification the notification to send
    */
   void sendNotification(Notification notification);

   /**
    * Gets the sequence number for the next notification.
    */
   long getNextNotificationSequenceNumber();

}
