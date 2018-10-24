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
package org.jboss.security.auth.container.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.login.LoginModuleStackHolder;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.ModuleOption;

//$Id$

/**
 *  Represents a configuration for a single auth module
 *  along the lines of AppConfigurationEntry for a JAAS LoginModule 
 *  @author <a href="mailto:anil.saldhana@jboss.org>anil.saldhana@jboss.org</a>
 *  @since  Dec 20, 2005 
 */
public class AuthModuleEntry
{
   private ControlFlag controlFlag = ControlFlag.REQUIRED;
   private Map<String,Object> options = new HashMap<String,Object>();
   private String name = null;
   private LoginModuleStackHolder loginModuleStackHolder = null;
   private String loginModuleStackHolderName = null;
   
   /**
    * Create a new AuthModuleEntry.
    * 
    * @param authModuleName Name of the AuthModule
    * @param options the options configured for this AuthModule.
    * @param loginModuleStackHolderName Name of the LoginModuleStack (Can be Null
    */
   public AuthModuleEntry(String authModuleName, Map<String,Object> options, String loginModuleStackHolderName)
   {
      this.name = authModuleName;
      if(options != null)
        this.options = options;
      this.loginModuleStackHolderName = loginModuleStackHolderName;
   } 
   
   /**
    * Get the name of the configured AuthModule
    * @return the class name of the configured AuthModule as a String.
    */
   public String getAuthModuleName()
   {
      return name;
   }
   
   public void addOption(ModuleOption option)
   { 
      if(option == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("option");
      options.put(option.getName(), option.getValue());
   }
   
   /**
    * Get the options configured for this AuthModule.
    * @return the options configured for this AuthModule as an unmodifiable Map
    */
   public Map<String,Object> getOptions()
   { 
      return Collections.unmodifiableMap(options);
   }
   
   public void setOptions(Map<String,Object> options)
   {
      if (options == null)
          throw PicketBoxMessages.MESSAGES.invalidNullArgument("options");
        this.options = options;
   }
   
   /**
    * A ServerAuthModule may delegate its decision making to a stack
    * of LoginModules
    * 
    * @return a stack of LoginModules
    */
   public LoginModuleStackHolder getLoginModuleStackHolder()
   {
      return loginModuleStackHolder;
   }
   
   /**
    * A ServerAuthModule may delegate its decision making to a stack
    * of LoginModules
    *
    * @param loginModuleStackHolder a stack of LoginModules
    */
   public void setLoginModuleStackHolder(LoginModuleStackHolder loginModuleStackHolder)
   {
      if(loginModuleStackHolder == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("loginModuleStackHolder");
      this.loginModuleStackHolder = loginModuleStackHolder;
      this.loginModuleStackHolderName = this.loginModuleStackHolder.getName();
   }
   
   public String getLoginModuleStackHolderName()
   {
      return loginModuleStackHolderName;
   }
   
   public void setLoginModuleStackHolderName(String loginModuleStackHolderName)
   {
      if(loginModuleStackHolderName == null)
          throw PicketBoxMessages.MESSAGES.invalidNullArgument("loginModuleStackHolderName");
       this.loginModuleStackHolderName = loginModuleStackHolderName;
   }

   public ControlFlag getControlFlag()
   {
      return controlFlag;
   }

   public void setControlFlag(ControlFlag flag)
   {
      this.controlFlag = flag;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getName()).append("{");
      builder.append(this.name).append(":").append(this.options);
      builder.append("}"); 
      return builder.toString();
   } 
}