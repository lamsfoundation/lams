/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.picketbox.core.authorization.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceType;

/**
 * A resource denoting a POJO
 * @author Anil.Saldhana@redhat.com
 * @since Mar 4, 2010
 */
public class POJOResource implements Resource
{
   private Map<String,Object> map = new HashMap<String, Object>();
   
   @SuppressWarnings("unused")
   private Object pojo = null;
   
   public POJOResource(Object obj)
   {
      this.pojo = obj;
   }

   public ResourceType getLayer()
   { 
      return ResourceType.POJO;
   }

   public void add(Map<String,Object> m)
   {
      this.map.putAll(m);
   }
   
   public Map<String, Object> getMap()
   { 
      return Collections.unmodifiableMap( map );
   }

   public void add(String key, Object value)
   {
      map.put(key, value);
   }
}