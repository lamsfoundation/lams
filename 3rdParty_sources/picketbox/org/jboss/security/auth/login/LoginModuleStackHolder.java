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
package org.jboss.security.auth.login; 

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.AuthPermission;
import javax.security.auth.login.AppConfigurationEntry;

import org.jboss.security.PicketBoxMessages;

//$Id$

/**
 *  Holder for the login module stack element in login-config
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Dec 21, 2005 
 */
@SuppressWarnings("rawtypes")
public class LoginModuleStackHolder  
{
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");
   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");
   
   private String name = "";
    
   private ArrayList appEntries;
   
   @SuppressWarnings("unchecked")
   public LoginModuleStackHolder(String name, List entries)
   {
      this.name = name;
      if(entries != null)
      { 
         this.appEntries = new ArrayList();
         this.appEntries.addAll(entries);
      }
   } 
   
   public String getName()
   {
      return this.name;
   } 
   
   @SuppressWarnings("unchecked")
   public void addAppConfigurationEntry(AppConfigurationEntry entry)
   {
      if(appEntries == null)
         this.appEntries = new ArrayList();
      this.appEntries.add(entry);
   }
   
   @SuppressWarnings("unchecked")
   public AppConfigurationEntry[] getAppConfigurationEntry()
   {
      SecurityManager sm = System.getSecurityManager();
      if( sm != null )
         sm.checkPermission(GET_CONFIG_ENTRY_PERM); 
      AppConfigurationEntry[] entries = new AppConfigurationEntry[appEntries.size()];
      appEntries.toArray(entries);
      return entries;
   }
   
   @SuppressWarnings("unchecked")
   public void setAppConfigurationEntry(List entries)
   {
      if(entries  == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("entries");
      if(appEntries == null)
         this.appEntries = new ArrayList();
      this.appEntries.addAll(entries);
   }
}