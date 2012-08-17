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


/**
 * A "Nested Diagnostic Context" abstraction.
 *
 * @author Jason T. Greene
 */
public class NDC
{
   private final static NDCProvider ndc;

   static
   {
      NDCProvider n = null;
      if (NDCSupport.class.isAssignableFrom(Logger.pluginClass))
      {

         try
         {
            n = ((NDCSupport) Logger.pluginClass.newInstance()).getNDCProvider();
         }
         catch (Throwable t)
         {
            // Eat
         }
      }

      if (n == null)
         n = new NullNDCProvider();

      ndc = n;
   }

   public static void clear()
   {
      ndc.clear();
   }

   public static String get()
   {
      return ndc.get();
   }

   public static int getDepth()
   {
      return ndc.getDepth();
   }

   public static String pop()
   {
      return ndc.pop();
   }

   public static String peek()
   {
      return ndc.peek();
   }

   public static void push(String message)
   {
      ndc.push(message);
   }

   public static void setMaxDepth(int maxDepth)
   {
      ndc.setMaxDepth(maxDepth);
   }
}
