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
import org.jboss.cache.Fqn;
import org.jboss.cache.buddyreplication.BuddyGroup;
import org.jgroups.View;

import javax.transaction.Transaction;
import java.util.Map;

/**
 * Basic implementation of an event that covers all event types.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 */
public class EventImpl implements CacheBlockedEvent, CacheUnblockedEvent, CacheStartedEvent, CacheStoppedEvent,
      NodeActivatedEvent, NodeCreatedEvent, NodeEvictedEvent, NodeLoadedEvent, NodeModifiedEvent, NodeMovedEvent,
      NodePassivatedEvent, NodeRemovedEvent, NodeVisitedEvent, TransactionCompletedEvent, TransactionRegisteredEvent,
      ViewChangedEvent, BuddyGroupChangedEvent, NodeInvalidatedEvent
{
   private boolean pre = false; // by default events are after the fact
   private Cache cache;
   private ModificationType modificationType;
   private Map data;
   private Fqn fqn;
   private Transaction transaction;
   private boolean originLocal = true; // by default events all originate locally
   private Fqn targetFqn;
   private boolean successful;
   private View newView;
   private Type type;
   private BuddyGroup buddyGroup;


   public EventImpl(boolean pre, Cache cache, ModificationType modificationType, Map data, Fqn fqn, Transaction transaction, boolean originLocal, Fqn targetFqn, boolean successful, View newView, Type type)
   {
      this.pre = pre;
      this.cache = cache;
      this.modificationType = modificationType;
      this.data = data;
      this.fqn = fqn;
      this.transaction = transaction;
      this.originLocal = originLocal;
      this.targetFqn = targetFqn;
      this.successful = successful;
      this.newView = newView;
      this.type = type;
   }

   public EventImpl()
   {
   }

   public Type getType()
   {
      return type;
   }

   public boolean isPre()
   {
      return pre;
   }

   public Cache getCache()
   {
      return cache;
   }

   public ModificationType getModificationType()
   {
      return modificationType;
   }

   public Map getData()
   {
      return data;
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   public Transaction getTransaction()
   {
      return transaction;
   }

   public boolean isOriginLocal()
   {
      return originLocal;
   }

   public Fqn getTargetFqn()
   {
      return targetFqn;
   }

   public boolean isSuccessful()
   {
      return successful;
   }

   public View getNewView()
   {
      return newView;
   }

   // ------------------------------ setters -----------------------------

   public void setPre(boolean pre)
   {
      this.pre = pre;
   }

   public void setCache(Cache cache)
   {
      this.cache = cache;
   }

   public void setModificationType(ModificationType modificationType)
   {
      this.modificationType = modificationType;
   }

   public void setData(Map data)
   {
      this.data = data;
   }

   public void setFqn(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public void setTransaction(Transaction transaction)
   {
      this.transaction = transaction;
   }

   public void setOriginLocal(boolean originLocal)
   {
      this.originLocal = originLocal;
   }

   public void setTargetFqn(Fqn targetFqn)
   {
      this.targetFqn = targetFqn;
   }

   public void setSuccessful(boolean successful)
   {
      this.successful = successful;
   }

   public void setNewView(View newView)
   {
      this.newView = newView;
   }

   public void setType(Type type)
   {
      this.type = type;
   }

   public void setBuddyGroup(BuddyGroup buddyGroup)
   {
      this.buddyGroup = buddyGroup;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      EventImpl event = (EventImpl) o;

      if (originLocal != event.originLocal) return false;
      if (pre != event.pre) return false;
      if (successful != event.successful) return false;
      if (cache != null ? !cache.equals(event.cache) : event.cache != null) return false;
      if (data != null ? !data.equals(event.data) : event.data != null) return false;
      if (fqn != null ? !fqn.equals(event.fqn) : event.fqn != null) return false;
      if (modificationType != event.modificationType) return false;
      if (targetFqn != null ? !targetFqn.equals(event.targetFqn) : event.targetFqn != null) return false;
      if (transaction != null ? !transaction.equals(event.transaction) : event.transaction != null) return false;
      if (newView != null ? !newView.equals(event.newView) : event.newView != null) return false;
      if (buddyGroup != null ? !buddyGroup.equals(event.buddyGroup) : event.buddyGroup != null) return false;
      if (type != null ? !type.equals(event.type) : event.type != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (pre ? 1 : 0);
      result = 31 * result + (cache != null ? cache.hashCode() : 0);
      result = 31 * result + (modificationType != null ? modificationType.hashCode() : 0);
      result = 31 * result + (data != null ? data.hashCode() : 0);
      result = 31 * result + (fqn != null ? fqn.hashCode() : 0);
      result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
      result = 31 * result + (originLocal ? 1 : 0);
      result = 31 * result + (targetFqn != null ? targetFqn.hashCode() : 0);
      result = 31 * result + (successful ? 1 : 0);
      result = 31 * result + (newView != null ? newView.hashCode() : 0);
      result = 31 * result + (buddyGroup != null ? buddyGroup.hashCode() : 0);
      result = 31 * result + (type != null ? type.hashCode() : 0);
      return result;
   }


   @Override
   public String toString()
   {
      return "EventImpl{" +
            "type=" + type +
            ",pre=" + pre +
            ", cache=" + cache +
            ", modificationType=" + modificationType +
            ", data=" + data +
            ", fqn=" + fqn +
            ", transaction=" + transaction +
            ", originLocal=" + originLocal +
            ", targetFqn=" + targetFqn +
            ", successful=" + successful +
            ", newView=" + newView +
            ", buddyGroup=" + buddyGroup +
            '}';
   }

   public BuddyGroup getBuddyGroup()
   {
      return buddyGroup;
   }
}
