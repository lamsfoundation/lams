/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.audit.config;

import java.util.HashMap;
import java.util.Map;

import org.jboss.security.config.ModuleOption;
 

/**
 *  An entry representing an Audit Provider in the configuration 
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 10, 2007 
 *  @version $Revision$
 */
public class AuditProviderEntry
{
   private String name; 
   
   private Map<String,Object> options = new HashMap<String,Object>();

   public AuditProviderEntry(String name)
   { 
      this.name = name;
   } 
   
   public AuditProviderEntry(String name, Map<String,Object> options)
   { 
      this.name = name;
      this.options = options;
   }

   public String getName()
   {
      return name;
   }
   
   public void add(ModuleOption option)
   {
      options.put(option.getName(), option.getValue());
   }
   
   public Map<String,Object> getOptions()
   {
      return this.options;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString()).append("{").append(this.name).append(":");
      builder.append(this.options).append("}");
      return builder.toString();
   }
}