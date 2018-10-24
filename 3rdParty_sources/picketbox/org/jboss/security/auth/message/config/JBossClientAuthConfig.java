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
package org.jboss.security.auth.message.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.AuthConfig;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;

//$Id$

/**
 *  Default Client Authentication Configuration
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  May 15, 2006 
 *  @version $Revision$
 */
public class JBossClientAuthConfig implements ClientAuthConfig
{
   private String layer = null;
   private String contextId = null;
   @SuppressWarnings("unused")
   private CallbackHandler callbackHandler = null;
   @SuppressWarnings({ "rawtypes"})
   private List modules = new ArrayList();
   @SuppressWarnings({  "unused", "rawtypes"})
   private Map contextProperties;
   
   /**
    * Create a new JBossClientAuthConfig.
    * 
    * @param layer Message Layer
    * @param appContext Application Context
    * @param handler Callback Handler to be passed to auth modules
    * @param properties Contextual properties
    */ 
   @SuppressWarnings("rawtypes")
   public JBossClientAuthConfig(String layer, String appContext,
         CallbackHandler handler, Map properties)
   {
      this.layer = layer;
      this.contextId = appContext;
      this.callbackHandler = handler;
      this.contextProperties = properties;
   }

   /**
    * @see ClientAuthConfig#getAuthContext(String, javax.security.auth.Subject, java.util.Map)
    */ 
   @SuppressWarnings("rawtypes")
   public ClientAuthContext getAuthContext(String authContextID,
         Subject clientSubject, Map properties)
   throws AuthException
   { 
      return new JBossClientAuthContext(this);
   }  

   /**
    * @see AuthConfig#getMessageLayer()
    */
   public String getMessageLayer()
   { 
      return this.layer;
   }

   /**
    * @see AuthConfig#refresh()
    */
   public void refresh()
   {  
   }

   //Custom Methods
   @SuppressWarnings({"rawtypes"})
   public List getClientAuthModules()
   {
      return modules ;
   }
 

   public String getAppContext()
   { 
      return this.contextId;
   }

   public String getAuthContextID(MessageInfo messageInfo)
   {
      throw new UnsupportedOperationException();
   }

   public boolean isProtected()
   { 
      throw new UnsupportedOperationException();
   } 
}