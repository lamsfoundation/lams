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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * General utility methods used throughout the JBC code base.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public final class Util
{
   /**
    * Loads the specified class using this class's classloader, or, if it is <code>null</code>
    * (i.e. this class was loaded by the bootstrap classloader), the system classloader.
    * <p/>
    * If loadtime instrumentation via GenerateInstrumentedClassLoader is used, this
    * class may be loaded by the bootstrap classloader.
    * </p>
    *
    * @param classname name of the class to load
    * @return the class
    * @throws ClassNotFoundException
    */
   public static Class loadClass(String classname) throws ClassNotFoundException
   {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      if (cl == null)
         cl = ClassLoader.getSystemClassLoader();
      return cl.loadClass(classname);
   }

   @SuppressWarnings("unchecked")
   public static <T> T getInstance(Class<T> clazz) throws Exception
   {
      // first look for a getInstance() constructor
      T instance;
      try
      {
         Method factoryMethod = clazz.getMethod("getInstance", new Class[]{});
         instance = (T) factoryMethod.invoke(null);
      }
      catch (Exception e)
      {
         // no factory method or factory method failed.  Try a constructor.
         instance = clazz.newInstance();
      }
      return instance;
   }

   @SuppressWarnings("unchecked")
   public static Object getInstance(String classname) throws Exception
   {
      if (classname == null) throw new IllegalArgumentException("Cannot load null class!");
      Class clazz = loadClass(classname);
      return getInstance(clazz);
   }

   /**
    * Prevent instantiation
    */
   private Util()
   {
   }

   /**
    * Calculates the diffs between data maps passed in to {@link org.jboss.cache.notifications.event.NodeModifiedEvent#getData()}
    * before and after modification.  This only makes sense if the modification type is {@link org.jboss.cache.notifications.event.NodeModifiedEvent.ModificationType#PUT_MAP}.
    * Refer to {@link org.jboss.cache.notifications.event.NodeModifiedEvent} and {@link org.jboss.cache.notifications.annotation.NodeModified}.
    *
    * @param pre  map of data before the node was modified
    * @param post Map of data after the node was modified
    * @return MapModifications containing the differences.
    */
   public static MapModifications diffNodeData(Map<Object, Object> pre, Map<Object, Object> post)
   {
      MapModifications mods = new MapModifications();

      // let's start with what's been added and modified.
      for (Map.Entry me : post.entrySet())
      {
         Object key = me.getKey();
         Object value = me.getValue();
         if (pre.containsKey(key))
         {
            if (!value.equals(pre.get(key)))
            {
               mods.modifiedEntries.put(key, value);
            }
         }
         else
         {
            mods.addedEntries.put(key, value);
         }
      }

      // now the removed entries.
      for (Map.Entry me : pre.entrySet())
      {
         Object key = me.getKey();
         if (!post.containsKey(key))
         {
            mods.removedEntries.put(key, me.getValue());
         }
      }

      return mods;
   }

   /**
    * Null-safe equality test.
    *
    * @param a first object to compare
    * @param b second object to compare
    * @return true if the objects are equals or both null, false otherwise.
    */
   public static boolean safeEquals(Object a, Object b)
   {
      return (a == b) || (a != null && a.equals(b));
   }

   /**
    * Static inner class that holds 3 maps - for data added, removed and modified.
    */
   public static class MapModifications
   {
      public final Map<Object, Object> addedEntries = new HashMap<Object, Object>();
      public final Map<Object, Object> removedEntries = new HashMap<Object, Object>();
      public final Map<Object, Object> modifiedEntries = new HashMap<Object, Object>();


      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         MapModifications that = (MapModifications) o;

         if (addedEntries != null ? !addedEntries.equals(that.addedEntries) : that.addedEntries != null) return false;
         if (modifiedEntries != null ? !modifiedEntries.equals(that.modifiedEntries) : that.modifiedEntries != null)
            return false;
         if (removedEntries != null ? !removedEntries.equals(that.removedEntries) : that.removedEntries != null)
            return false;

         return true;
      }

      @Override
      public int hashCode()
      {
         int result;
         result = (addedEntries != null ? addedEntries.hashCode() : 0);
         result = 31 * result + (removedEntries != null ? removedEntries.hashCode() : 0);
         result = 31 * result + (modifiedEntries != null ? modifiedEntries.hashCode() : 0);
         return result;
      }

      @Override
      public String toString()
      {
         return "Added Entries " + addedEntries + " Removeed Entries " + removedEntries + " Modified Entries " + modifiedEntries;
      }
   }
}
