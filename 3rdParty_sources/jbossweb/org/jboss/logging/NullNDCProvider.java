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
package org.jboss.logging;

import java.util.Stack;

/**
 * An NDC provider which does nothing.
 *
 * @author Jason T. Greene
 */
public class NullNDCProvider implements NDCProvider
{
   public void clear()
   {
   }

   public Stack cloneStack()
   {
      return null;
   }

   public String get()
   {
      return null;
   }

   public int getDepth()
   {
      return 0;
   }

   public void inherit(Stack stack)
   {
   }

   public String peek()
   {
      return null;
   }

   public String pop()
   {
      return null;
   }

   public void push(String message)
   {
   }

   public void remove()
   {
   }

   public void setMaxDepth(int maxDepth)
   {
   }
}
