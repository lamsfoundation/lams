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

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.MDCProvider;

/**
 * MDC implementation for JDK logging.
 *
 * @author Jason T. Greene
 */
public class JDKMDCProvider implements MDCProvider
{
   private ThreadLocal<Map<String, Object>> map = new ThreadLocal<Map<String, Object>>();

   public Object get(String key)
   {
      return map.get() == null ? null : map.get().get(key);
   }

   public Map<String, Object> getMap()
   {
      return map.get();
   }

   public void put(String key, Object value)
   {
      Map<String, Object> map = this.map.get();
      if (map == null)
      {
         map = new HashMap<String, Object>();
         this.map.set(map);
      }

      map.put(key, value);
   }

   public void remove(String key)
   {
      Map<String, Object> map = this.map.get();
      if (map == null)
         return;

      map.remove(key);
   }
}