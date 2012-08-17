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
package org.jboss.cache.notifications;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyGroup;
import org.jboss.cache.notifications.event.NodeModifiedEvent;
import org.jgroups.View;

import javax.transaction.Transaction;
import java.util.Map;
import java.util.Set;

/**
 * Public interface with all allowed notifications.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public interface Notifier
{
   /**
    * Notifies all registered listeners of a nodeCreated event.
    */
   void notifyNodeCreated(Fqn fqn, boolean pre, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeModified event.
    */
   void notifyNodeModified(Fqn fqn, boolean pre, NodeModifiedEvent.ModificationType modificationType, Map data, InvocationContext ctx);

   /**
    * When notifying about node modifications, in many scenarios there is a need of building a new Map object. If no
    * listeners are registered for notification then it is pointless building this object  - so guard the notification
    * with this call.
    */
   public boolean shouldNotifyOnNodeModified();

   /**
    * Notifies all registered listeners of a nodeRemoved event.
    */
   void notifyNodeRemoved(Fqn fqn, boolean pre, Map data, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeVisited event.
    */
   void notifyNodeVisited(Fqn fqn, boolean pre, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeMoved event.
    */
   void notifyNodeMoved(Fqn originalFqn, Fqn newFqn, boolean pre, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeEvicted event.
    */
   void notifyNodeEvicted(Fqn fqn, boolean pre, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeInvalidated event.
    */
   void notifyNodeInvalidated(Fqn fqn, boolean pre, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeLoaded event.
    */
   void notifyNodeLoaded(Fqn fqn, boolean pre, Map data, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodeActivated event.
    */
   void notifyNodeActivated(Fqn fqn, boolean pre, Map data, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a nodePassivated event.
    */
   void notifyNodePassivated(Fqn fqn, boolean pre, Map data, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a viewChange event.  Note that viewChange notifications are ALWAYS sent
    * immediately.
    */
   void notifyViewChange(View view, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a buddy group change event.  Note that buddy group change notifications are ALWAYS sent
    * immediately.
    *
    * @param buddyGroup buddy group to set
    * @param pre        if true, this has occured before the buddy group message is broadcast to the cluster
    */
   void notifyBuddyGroupChange(BuddyGroup buddyGroup, boolean pre);

   /**
    * Notifies all registered listeners of a transaction completion event.
    *
    * @param transaction the transaction that has just completed
    * @param successful  if true, the transaction committed.  If false, this is a rollback event
    */
   void notifyTransactionCompleted(Transaction transaction, boolean successful, InvocationContext ctx);

   /**
    * Notifies all registered listeners of a transaction registration event.
    *
    * @param transaction the transaction that has just completed
    */
   void notifyTransactionRegistered(Transaction transaction, InvocationContext ctx);

   void notifyCacheBlocked(boolean pre);

   void notifyCacheUnblocked(boolean pre);

   /**
    * Adds a cache listener to the list of cache listeners registered.
    *
    * @param listener
    */
   void addCacheListener(Object listener);

   /**
    * Removes a cache listener from the list of cache listeners registered.
    *
    * @param listener
    */
   void removeCacheListener(Object listener);

   /**
    * @return Retrieves an (unmodifiable) set of cache listeners registered.
    */
   Set<Object> getCacheListeners();
}
