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

import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class EvictionQueueList
{
   EvictionListEntry head;
   EvictionListEntry tail;
   int modCount;
   private int size;

   EvictionQueueList()
   {
      head = null;
      tail = null;
      size = 0;
      modCount = 0;
   }

   void addToTop(EvictionListEntry entry)
   {
      EvictionListEntry formerHead = head;
      head = entry;
      // if there was no previous head then this list was empty.
      if (formerHead != null)
      {
         formerHead.previous = head;
         head.next = formerHead;
         head.previous = null;
      }
      else
      {
         tail = entry;
      }
      size++;
      modCount++;
   }

   void addToBottom(EvictionListEntry entry)
   {
      EvictionListEntry formerTail = tail;
      tail = entry;
      // if there was no previous head then this list was empty.
      if (formerTail != null)
      {
         tail.previous = formerTail;
         formerTail.next = tail;
         tail.next = null;
      }
      else
      {
         head = entry;
      }
      size++;
      modCount++;
   }

   void remove(EvictionListEntry entry)
   {
      if (this.isEmpty())
      {
         return;
      }

      if (isSingleNode(entry))
      {
         head = null;
         tail = null;
      }
      else if (isTail(entry))
      {
         tail = entry.previous;
         // unlink the last node.
         entry.previous.next = null;
      }
      else if (isHead(entry))
      {
         head = entry.next;
         head.previous = null;
      }
      else
      {
         // node is in between two other nodes.
         entry.next.previous = entry.previous;
         entry.previous.next = entry.next;
      }
      size--;
      modCount++;
   }

   int size()
   {
      return this.size;
   }

   void clear()
   {
      head = null;
      tail = null;
      size = 0;
      modCount++;
   }

   EvictionListEntry getFirst()
   {
      if (head == null)
      {
         throw new NoSuchElementException("List is empty");
      }
      return head;
   }

   EvictionListEntry getLast()
   {
      if (tail == null)
      {
         throw new NoSuchElementException("List is empty");
      }
      return tail;
   }

   Iterator<NodeEntry> iterator()
   {
      return new EvictionListIterator();
   }

   NodeEntry[] toNodeEntryArray()
   {
      if (isEmpty())
      {
         return null;
      }
      NodeEntry[] ret = new NodeEntry[size];
      int i = 0;
      EvictionListEntry temp = head;

      do
      {
         ret[i] = temp.node;
         temp = temp.next;
         i++;
      }
      while (temp != null);

      return ret;
   }

   EvictionListEntry[] toArray()
   {
      if (isEmpty())
      {
         return null;
      }
      EvictionListEntry[] ret = new EvictionListEntry[size];
      int i = 0;
      EvictionListEntry temp = head;

      do
      {
         ret[i] = temp;
         temp = temp.next;
         i++;
      }
      while (temp != null);

      return ret;
   }

   void fromArray(EvictionListEntry[] evictionListEntries)
   {

      for (EvictionListEntry evictionListEntry : evictionListEntries)
      {
         this.addToBottom(evictionListEntry);
      }
   }

   private boolean isEmpty()
   {
      return head == null && tail == null;
   }

   private boolean isSingleNode(EvictionListEntry entry)
   {
      return isTail(entry) && isHead(entry);
   }

   private boolean isTail(EvictionListEntry entry)
   {
      return entry.next == null;
   }

   private boolean isHead(EvictionListEntry entry)
   {
      return entry.previous == null;
   }

   @Override
   public String toString()
   {
      return Arrays.asList(toArray()).toString();
   }

   static class EvictionListComparator implements Comparator<EvictionListEntry>
   {
      Comparator<NodeEntry> nodeEntryComparator;

      EvictionListComparator(Comparator<NodeEntry> nodeEntryComparator)
      {
         this.nodeEntryComparator = nodeEntryComparator;
      }

      public int compare(EvictionListEntry e1, EvictionListEntry e2)
      {
         return nodeEntryComparator.compare(e1.node, e2.node);
      }
   }

   class EvictionListIterator implements ListIterator<NodeEntry>
   {
      EvictionListEntry next = head;
      EvictionListEntry previous;
      EvictionListEntry cursor;

      int initialModCount = EvictionQueueList.this.modCount;

      public boolean hasNext()
      {
         this.doConcurrentModCheck();
         return next != null;
      }

      public NodeEntry next()
      {
         this.doConcurrentModCheck();
         this.forwardCursor();
         return cursor.node;
      }

      public boolean hasPrevious()
      {
         this.doConcurrentModCheck();
         return previous != null;
      }

      public NodeEntry previous()
      {
         this.doConcurrentModCheck();
         this.rewindCursor();
         return cursor.node;
      }

      public int nextIndex()
      {
         throw new UnsupportedOperationException();
      }

      public int previousIndex()
      {
         throw new UnsupportedOperationException();
      }

      public void remove()
      {
         this.doConcurrentModCheck();
         if (cursor == null)
         {
            throw new IllegalStateException("Cannot remove from iterator when there is nothing at the current iteration point");
         }
         EvictionQueueList.this.remove(cursor);
         cursor = null;
         initialModCount++;
      }

      public void set(NodeEntry o)
      {
         this.doConcurrentModCheck();
         cursor.node = (NodeEntry) o;
      }

      public void add(NodeEntry o)
      {
         this.doConcurrentModCheck();
         initialModCount++;
      }

      private void doConcurrentModCheck()
      {
         if (EvictionQueueList.this.modCount != initialModCount)
         {
            throw new ConcurrentModificationException();
         }
      }

      private void forwardCursor()
      {
         if (next == null)
         {
            throw new NoSuchElementException("No more objects to iterate.");
         }
         previous = cursor;
         cursor = next;
         next = cursor.next;
      }

      private void rewindCursor()
      {
         if (previous == null)
         {
            throw new NoSuchElementException();
         }
         next = cursor;
         cursor = previous;
         previous = cursor.previous;
      }
   }

}

class EvictionListEntry
{
   EvictionListEntry next;
   EvictionListEntry previous;

   NodeEntry node;

   EvictionListEntry()
   {
   }

   EvictionListEntry(NodeEntry node)
   {
      this.node = node;
   }

   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof EvictionListEntry))
         return false;
      EvictionListEntry entry = (EvictionListEntry) o;
      return this.node.getFqn().equals(entry.node.getFqn());
   }

   @Override
   public int hashCode()
   {
      return this.node.getFqn().hashCode();
   }

   @Override
   public String toString()
   {
      return "EntryLE=" + node;
   }

}
