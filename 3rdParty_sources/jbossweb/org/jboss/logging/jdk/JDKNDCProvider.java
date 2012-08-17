/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.logging.jdk;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import org.jboss.logging.NDCProvider;

/**
 * NDC implementation for JDK logging
 *
 * @author Jason T. Greene
 */
public class JDKNDCProvider implements NDCProvider
{
   private class ArrayStack<E> extends ArrayList<E>
   {
      private static final long serialVersionUID = -8520038422243642840L;

      public E pop()
      {
         int size = size();
         if (size == 0)
            throw new EmptyStackException();

         return remove(size - 1);
      }

      public E peek()
      {
         int size = size();
         if (size == 0)
            throw new EmptyStackException();

         return get(size - 1);
      }

      public void push(E val)
      {
         add(val);
      }

      public void setSize(int newSize)
      {
         int size = size();
         if (newSize >= size || newSize < 0)
            return;

         removeRange(newSize, size);
      }
   }

   private class Entry
   {
      private String merged;
      private String current;

      public Entry(String current)
      {
         this.merged = current;
         this.current = current;
      }

      public Entry(Entry parent, String current)
      {
         this.merged = parent.merged + ' ' + current;
         this.current = current;
      }
   }

   private ThreadLocal<ArrayStack<Entry>> stack = new ThreadLocal<ArrayStack<Entry>>();

   public void clear()
   {
      ArrayStack<Entry> stack = this.stack.get();
      if (stack != null)
         stack.clear();
   }

   public String get()
   {
      ArrayStack<Entry> stack = this.stack.get();

      return stack == null || stack.isEmpty() ? null : stack.peek().merged;
   }

   public int getDepth()
   {
      ArrayStack<Entry> stack = this.stack.get();

      return stack == null ? 0 : stack.size();
   }

   public String peek()
   {
      ArrayStack<Entry> stack = this.stack.get();

      return stack == null || stack.isEmpty() ? "" : stack.peek().current;
   }

   public String pop()
   {
      ArrayStack<Entry> stack = this.stack.get();

      return stack == null || stack.isEmpty() ? "" : stack.pop().current;
   }

   public void push(String message)
   {
      ArrayStack<Entry> stack = this.stack.get();

      if (stack == null)
      {
         stack = new ArrayStack<Entry>();
         this.stack.set(stack);
      }

      stack.push(stack.isEmpty() ? new Entry(message) : new Entry(stack.peek(), message));
   }

   public void setMaxDepth(int maxDepth)
   {
      ArrayStack<Entry> stack = this.stack.get();

      if (stack != null)
         stack.setSize(maxDepth);
   }
}
