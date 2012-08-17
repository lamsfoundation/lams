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

import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.CacheStarted;
import org.jboss.cache.notifications.annotation.CacheStopped;
import org.jboss.cache.notifications.annotation.NodeActivated;
import org.jboss.cache.notifications.annotation.NodeCreated;
import org.jboss.cache.notifications.annotation.NodeEvicted;
import org.jboss.cache.notifications.annotation.NodeLoaded;
import org.jboss.cache.notifications.annotation.NodeModified;
import org.jboss.cache.notifications.annotation.NodeMoved;
import org.jboss.cache.notifications.annotation.NodePassivated;
import org.jboss.cache.notifications.annotation.NodeRemoved;
import org.jboss.cache.notifications.annotation.NodeVisited;
import org.jboss.cache.notifications.annotation.ViewChanged;
import org.jboss.cache.notifications.event.Event;
import org.jboss.cache.notifications.event.NodeEvent;
import org.jboss.cache.notifications.event.NodeMovedEvent;
import org.jboss.cache.notifications.event.ViewChangedEvent;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;

/**
 * A CacheListener that creates JMX notifications from listener
 * events.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
@CacheListener
public class CacheNotificationListener
{
   // Notification Messages
   private static final String MSG_CACHE_STARTED = "Cache has been started.";
   private static final String MSG_CACHE_STOPPED = "Cache has been stopped.";
   private static final String MSG_NODE_CREATED = "Node has been created.";
   private static final String MSG_NODE_MODIFIED = "Node has been modifed.";
   private static final String MSG_NODE_REMOVED = "Node has been removed.";
   private static final String MSG_NODE_MOVED = "Node has been moved.";
   private static final String MSG_NODE_VISITED = "Node has been visited.";
   private static final String MSG_NODE_EVICTED = "Node has been evicted.";
   private static final String MSG_NODE_LOADED = "Node has been loaded.";
   private static final String MSG_NODE_ACTIVATED = "Node has been activated.";
   private static final String MSG_NODE_PASSIVATED = "Node has been passivated.";
   private static final String MSG_VIEW_CHANGED = "Cache cluster view has changed.";

   // Notification Info
   private static final String NOTIFICATION_NAME = Notification.class.getName();
   private static final String NOTIFICATION_DESCR = "JBossCache event notifications";

   private final CacheNotificationBroadcaster broadcaster;
   private String serviceName;

   // ------------------------------------------------------------ Constructors

   CacheNotificationListener(CacheNotificationBroadcaster support)
   {
      this.broadcaster = support;
   }

   // ----------------------------------------------------------- PublicMethods

   public String getServiceName()
   {
      return serviceName;
   }

   public void setServiceName(String serviceName)
   {
      this.serviceName = serviceName;
   }

   public static MBeanNotificationInfo[] getNotificationInfo()
   {
      String[] types = new String[]
            {
                  CacheNotificationBroadcaster.NOTIF_CACHE_STARTED,
                  CacheNotificationBroadcaster.NOTIF_CACHE_STOPPED,
                  CacheNotificationBroadcaster.NOTIF_NODE_CREATED,
                  CacheNotificationBroadcaster.NOTIF_NODE_EVICTED,
                  CacheNotificationBroadcaster.NOTIF_NODE_LOADED,
                  CacheNotificationBroadcaster.NOTIF_NODE_MODIFIED,
                  CacheNotificationBroadcaster.NOTIF_NODE_ACTIVATED,
                  CacheNotificationBroadcaster.NOTIF_NODE_PASSIVATED,
                  CacheNotificationBroadcaster.NOTIF_NODE_REMOVED,
                  CacheNotificationBroadcaster.NOTIF_NODE_VISITED,
                  CacheNotificationBroadcaster.NOTIF_VIEW_CHANGED,
            };

      MBeanNotificationInfo info = new MBeanNotificationInfo(types, NOTIFICATION_NAME, NOTIFICATION_DESCR);
      return new MBeanNotificationInfo[]{info};
   }

   // ----------------------------------------------------------- CacheListener

   @CacheStarted
   @CacheStopped
   @NodeCreated
   @NodeEvicted
   @NodeLoaded
   @NodeModified
   @NodeRemoved
   @NodeMoved
   @NodeVisited
   @NodeActivated
   @NodePassivated
   @ViewChanged
   public void broadcast(Event e)
   {
      NodeEvent ne;
      Notification n = null;
      switch (e.getType())
      {
         case CACHE_STARTED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_CACHE_STARTED, broadcaster, seq(), MSG_CACHE_STARTED);
            n.setUserData(serviceName);
            break;
         case CACHE_STOPPED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_CACHE_STOPPED, broadcaster, seq(), MSG_CACHE_STOPPED);
            n.setUserData(serviceName);
            break;
         case NODE_CREATED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_CREATED, broadcaster, seq(), MSG_NODE_CREATED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre(), ne.isOriginLocal()});
            break;
         case NODE_EVICTED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_EVICTED, broadcaster, seq(), MSG_NODE_EVICTED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre(), ne.isOriginLocal()});
            break;
         case NODE_LOADED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_LOADED, broadcaster, seq(), MSG_NODE_LOADED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre()});
            break;
         case NODE_MODIFIED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_MODIFIED, broadcaster, seq(), MSG_NODE_MODIFIED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre(), ne.isOriginLocal()});
            break;
         case NODE_REMOVED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_REMOVED, broadcaster, seq(), MSG_NODE_REMOVED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre(), ne.isOriginLocal()});
            break;
         case NODE_MOVED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_MOVED, broadcaster, seq(), MSG_NODE_MOVED);
            NodeMovedEvent nme = (NodeMovedEvent) e;
            n.setUserData(new Object[]{nme.getFqn().toString(), nme.getTargetFqn().toString(), e.isPre()});
            break;
         case NODE_VISITED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_VISITED, broadcaster, seq(), MSG_NODE_VISITED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre()});
            break;
         case NODE_ACTIVATED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_ACTIVATED, broadcaster, seq(), MSG_NODE_ACTIVATED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre()});
            break;
         case NODE_PASSIVATED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_NODE_PASSIVATED, broadcaster, seq(), MSG_NODE_PASSIVATED);
            ne = (NodeEvent) e;
            n.setUserData(new Object[]{ne.getFqn().toString(), e.isPre()});
            break;
         case VIEW_CHANGED:
            n = new Notification(CacheNotificationBroadcaster.NOTIF_VIEW_CHANGED, broadcaster, seq(), MSG_VIEW_CHANGED);
            n.setUserData(((ViewChangedEvent) e).getNewView().toString());
            break;
      }

      broadcaster.sendNotification(n);
   }

   private long seq()
   {
      return broadcaster.getNextNotificationSequenceNumber();
   }
}
