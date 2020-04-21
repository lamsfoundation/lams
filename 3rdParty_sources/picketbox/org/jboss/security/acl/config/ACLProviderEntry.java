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
package org.jboss.security.acl.config;

import java.util.HashMap;
import java.util.Map;

import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.ModuleOption;

/**
 *  Configuration Entry for ACL Providers
 *  @author Anil.Saldhana@redhat.com
 *  @since  Jan 30, 2008 
 *  @version $Revision$
 */
public class ACLProviderEntry
{
   private String aclProviderName;
   private ControlFlag controlFlag; 
   private Map<String,Object> options = new HashMap<String,Object>();
   
   /** 
    * Create a new AuthorizationModuleEntry.
    * 
    * @param name Policy Module Name 
    */
   public ACLProviderEntry(String name)
   {
      this.aclProviderName = name; 
   }
   
   /** 
    * Create a new AuthorizationModuleEntry.
    * 
    * @param name Policy Module Name
    * @param options Options
    */
   public ACLProviderEntry(String name, Map<String,Object> options)
   {
      this.aclProviderName = name;
      this.options = options;
   }
   
   public void add(ModuleOption option)
   { 
      options.put(option.getName(), option.getValue());
   }

   /**
    * Get the Policy Module Name
    * @return
    */
   public String getAclProviderName()
   {
      return aclProviderName;
   }

   /**
    * Get the options
    * @return
    */
   public Map<String,Object> getOptions()
   {
      return options;
   } 
    
   /**
    * Get the Control Flag (Required,Requisite,Sufficient or Optional)
    * @return
    */
   public ControlFlag getControlFlag()
   {
      return controlFlag;
   }
   
   /**
    * Set the Control Flag (Required,Requisite,Sufficient or Optional)
    * @return
    */
   public void setControlFlag(ControlFlag controlFlag)
   {
      this.controlFlag = controlFlag;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString());
      builder.append("{").append(this.aclProviderName).append(":");
      builder.append(this.controlFlag).append(":").append(this.options).append("}");
      return builder.toString();
   } 
}