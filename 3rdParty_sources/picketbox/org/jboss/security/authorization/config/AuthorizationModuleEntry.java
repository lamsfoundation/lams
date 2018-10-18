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
package org.jboss.security.authorization.config;

import java.util.HashMap;
import java.util.Map;

import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.ModuleOption;

//$Id$

/**
 *  Represents configuration for a single Policy Decision Module
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 9, 2006 
 *  @version $Revision$
 */
public class AuthorizationModuleEntry
{
   private String policyModuleName;
   private ControlFlag controlFlag; 
   private Map<String,Object> options = new HashMap<String,Object>();
   
   /** 
    * Create a new AuthorizationModuleEntry.
    * 
    * @param name Policy Module Name 
    */
   public AuthorizationModuleEntry(String name)
   {
      this.policyModuleName = name; 
   }
   
   /** 
    * Create a new AuthorizationModuleEntry.
    * 
    * @param name Policy Module Name
    * @param options Options
    */
   public AuthorizationModuleEntry(String name, Map<String,Object> options)
   {
      this.policyModuleName = name;
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
   public String getPolicyModuleName()
   {
      return policyModuleName;
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
      builder.append(getClass().getName()).append("{");
      builder.append(this.policyModuleName).append(":").append(this.options);
      builder.append(this.controlFlag).append("}"); 
      return builder.toString();
   } 
}