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
package org.jboss.cache.util;

import org.jboss.cache.Cache;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.DataContainerImpl;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.invocation.CacheInvocationDelegate;

import java.text.NumberFormat;

/**
 * Helper that prints the contents of a {@link org.jboss.cache.Cache} to a string.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 */
public class CachePrinter
{
   /**
    * Prints the contents of the cache (nodes + data) to a string
    *
    * @param c cache to print
    * @return a String representation of the cache
    */
   public static String printCacheDetails(Cache c)
   {
      // internal cast
      DataContainer ci = ((CacheInvocationDelegate) c).getDataContainer();
      return ci.printDetails();
   }

   /**
    * Prints the status of locks in the cache (nodes + locks) to a string
    *
    * @param c cache to print
    * @return a String representation of the cache
    */
   public static String printCacheLockingInfo(Cache c)
   {
      // internal cast
      DataContainerImpl cd = (DataContainerImpl) ((CacheInvocationDelegate) c).getDataContainer();
      return cd.printLockInfo();
   }

   public static String printCacheInterceptors(CacheSPI<?, ?> cache)
   {
      StringBuilder b = new StringBuilder();
      int index = 0;
      b.append("\n");
      for (CommandInterceptor i : cache.getInterceptorChain())
      {
         b.append("# ");
         b.append(index);
         b.append(" : ");
         b.append(i);
         b.append("\n");
         index++;
      }
      return b.toString();
   }

   public static String printInterceptorChain(CommandInterceptor i)
   {
      StringBuilder sb = new StringBuilder();
      if (i != null)
      {
         if (i.getNext() != null)
         {
            sb.append(printInterceptorChain(i.getNext())).append("\n");
         }
         sb.append("\t>> ");
         sb.append(i.getClass().getName());
      }
      return sb.toString();
   }

   /**
    * Formats a given String for display as an HTML snippet.
    *
    * @param s string to format
    * @return formatted string
    */
   public static String formatHtml(String s)
   {
      s = s.replaceAll("\r\n", "<br/>");
      s = s.replaceAll("\r", "<br/>");
      s = s.replaceAll("\n", "<br/>");
      s = s.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
      s = s.replaceAll(" ", "&nbsp;");
      return s;
   }

   /**
    * Prints a time for display
    * @param millis time in millis
    * @return the time, represented as millis, seconds, minutes or hours as appropriate, with suffix
    */
   public static String prettyPrint(long millis)
   {
      if (millis < 1000) return millis + " milliseconds";
      NumberFormat nf = NumberFormat.getNumberInstance();
      nf.setMaximumFractionDigits(2);
      double toPrint = ((double) millis) / 1000;
      if (toPrint < 300)
      {
         return nf.format(toPrint) + " seconds";
      }

      toPrint = toPrint / 60;

      if (toPrint < 120)
      {
         return nf.format(toPrint) + " minutes";
      }

      toPrint = toPrint / 60;

      return nf.format(toPrint) + " hours";
   }
}
