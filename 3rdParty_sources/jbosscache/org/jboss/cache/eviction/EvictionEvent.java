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
package org.jboss.cache.eviction;

import org.jboss.cache.Fqn;

/**
 * An eviction event records activity on nodes in the cache.  These are recorded on a {@link org.jboss.cache.Region} for processing
 * later by calls to {@link org.jboss.cache.Region#processEvictionQueues()}.
 * <p/>
 *
 * @see org.jboss.cache.Region
 */
public class EvictionEvent
{
   private Fqn fqn;
   private Type type;
   private int elementDifference;

   private long inUseTimeout;
   private long creationTimestamp;

   public EvictionEvent()
   {
   }

   public static enum Type
   {
      ADD_NODE_EVENT,
      REMOVE_NODE_EVENT,
      VISIT_NODE_EVENT,
      ADD_ELEMENT_EVENT,
      REMOVE_ELEMENT_EVENT,
      MARK_IN_USE_EVENT,
      UNMARK_USE_EVENT
   }

   public EvictionEvent(Fqn fqn, Type type, int elementDifference)
   {
      this.fqn = fqn;
      this.type = type;
      this.elementDifference = elementDifference;
      this.creationTimestamp = System.currentTimeMillis();
   }

   public long getCreationTimestamp()
   {
      return creationTimestamp;
   }

   public long getInUseTimeout()
   {
      return inUseTimeout;
   }

   public void setInUseTimeout(long inUseTimeout)
   {
      this.inUseTimeout = inUseTimeout;
   }

   public int getElementDifference()
   {
      return elementDifference;
   }

   public void setElementDifference(int elementDifference)
   {
      this.elementDifference = elementDifference;
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   public void setFqn(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public void setEventType(Type event)
   {
      type = event;
   }

   public Type getEventType()
   {
      return type;
   }

   @Override
   public String toString()
   {
      return "EvictedEventNode[fqn=" + fqn + " event=" + type + " diff=" + elementDifference + "]";
   }

   /**
    * Copies this evicted event node to create a new one with the same values, except with a new Fqn root.
    *
    * @param newRoot new Fqn root to use
    * @return a new EvictedEventNode instance
    * @see org.jboss.cache.Region#copy(org.jboss.cache.Fqn)
    */
   public EvictionEvent copy(Fqn newRoot)
   {
      return new EvictionEvent(Fqn.fromRelativeFqn(newRoot, fqn), type, elementDifference);
   }
}
