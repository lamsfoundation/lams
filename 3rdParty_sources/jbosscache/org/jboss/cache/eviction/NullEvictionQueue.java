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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A queue that does nothing.
 *
 * @author Brian Stansberry
 */
public class NullEvictionQueue implements EvictionQueue
{
   /**
    * Singleton instance of this class.
    */
   public static final NullEvictionQueue INSTANCE = new NullEvictionQueue();

   /**
    * Constructs a new NullEvictionQueue.
    */
   private NullEvictionQueue()
   {
   }

   /**
    * No-op
    */
   public void addNodeEntry(NodeEntry entry)
   {
      // no-op
   }

   /**
    * No-op
    */
   public void clear()
   {
      // no-op
   }

   /**
    * Returns <code>false</code>
    */
   public boolean containsNodeEntry(NodeEntry entry)
   {
      return false;
   }

   /**
    * Returns <code>null</code>
    */
   public NodeEntry getFirstNodeEntry()
   {
      return null;
   }

   /**
    * Returns <code>null</code>
    */
   public NodeEntry getNodeEntry(Fqn fqn)
   {
      return null;
   }

   /**
    * Returns <code>null</code>
    */
   public NodeEntry getNodeEntry(String fqn)
   {
      return null;
   }

   /**
    * Returns <code>0</code>
    */
   public int getNumberOfElements()
   {
      return 0;
   }

   /**
    * Returns <code>0</code>
    */
   public int getNumberOfNodes()
   {
      return 0;
   }

   /**
    * Returns an <code>Iterator</code> whose
    * <code>hasNext()</code> returns <code>false</code>.
    */
   public Iterator<NodeEntry> iterator()
   {
      return NullQueueIterator.INSTANCE;
   }

   /**
    * No-op
    */
   public void modifyElementCount(int difference)
   {
      // no-op
   }

   /**
    * No-op
    */
   public void removeNodeEntry(NodeEntry entry)
   {
      // no-op
   }

   static class NullQueueIterator implements Iterator<NodeEntry>
   {
      private static final NullQueueIterator INSTANCE = new NullQueueIterator();

      private NullQueueIterator()
      {
      }

      public boolean hasNext()
      {
         return false;
      }

      public NodeEntry next()
      {
         throw new NoSuchElementException("No more elements");
      }

      public void remove()
      {
         throw new IllegalStateException("Must call next() before remove()");
      }
   }

}
