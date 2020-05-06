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
package org.jboss.security.mapping.config;

import java.util.HashMap;
import java.util.Map;

import org.jboss.security.config.ModuleOption;
import org.jboss.security.mapping.MappingType;

// $Id: MappingModuleEntry.java 45985 2006-06-29 20:56:57Z asaldhana $

/**
 * Represents configuration for a single Mapping Module
 * 
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @since August 24, 2006
 * @version $Revision: 45985 $
 */
public class MappingModuleEntry
{
   private final String mappingModuleName;

   private final String mappingModuleType;

   private final Map<String, Object> options;

   /**
    * Create a new MappingModuleEntry.
    * 
    * @param name Policy Module Name
    */
   public MappingModuleEntry(String name)
   {
      this(name, new HashMap<String, Object>());
   }

   /**
    * Create a new MappingModuleEntry.
    * 
    * @param name Policy Module Name
    * @param options Options
    */
   public MappingModuleEntry(String name, Map<String, Object> options)
   {
      this(name, options, MappingType.ROLE.toString());
   }

   /**
    * <p>
    * Creates a new {@code MappingModuleEntry} with the specified module name, module type and module options.
    * </p>
    * 
    * @param name a {@code String} representing the fully-qualified class name of the mapping module.
    * @param options a {@code Map<String,Object>} containing the options configured for the mapping module.
    * @param type a {@code String} representing the type of mapping performed by the mapping module (e.g. role,
    *            identity, principal).
    */
   public MappingModuleEntry(String name, Map<String, Object> options, String type)
   {
      this.mappingModuleName = name;
      this.mappingModuleType = type;
      this.options = options;
   }

   public void add(ModuleOption option)
   {
      options.put(option.getName(), option.getValue());
   }

   /**
    * Get the Policy Module Name
    * 
    * @return
    */
   public String getMappingModuleName()
   {
      return mappingModuleName;
   }

   /**
    * <p>
    * Gets the type of mapping performed by the mapping module.
    * </p>
    * 
    * @return a {@code String} representing the type of mapping performed.
    */
   public String getMappingModuleType()
   {
      return this.mappingModuleType;
   }

   /**
    * Get the options
    * 
    * @return
    */
   public Map<String, Object> getOptions()
   {
      return options;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getName()).append("{");
      builder.append(this.mappingModuleName).append("-").append(this.mappingModuleType);
      builder.append(":").append(this.options);
      builder.append("}");
      return builder.toString();
   }
}