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

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;

import org.jboss.security.PicketBoxMessages;

//$Id$

/**
 *  Default Auth Config Provider 
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  May 15, 2006 
 *  @version $Revision$
 */
public class JBossAuthConfigProvider implements AuthConfigProvider
{
   private Map<String,Object> contextProperties = null;
   private String cbhProperty = "authconfigprovider.client.callbackhandler";

   /**
    * Create a new JBossAuthConfigProvider.
    * 
    * @param props Context Properties
    */
   public JBossAuthConfigProvider(Map<String,Object> props, AuthConfigFactory factory)
   {
      this.contextProperties = props;
      
      // if a factory has been supplied this provider needs to register itself.
      if (factory != null)
         factory.registerConfigProvider(this, null, null, "JBossAuthConfigProvider Self Registration");
   } 
   /**
    * @see AuthConfigProvider#getClientAuthConfig(String, String, CallbackHandler)
    */
   public ClientAuthConfig getClientAuthConfig(String layer, String appContext, 
         CallbackHandler handler) throws AuthException
   { 
      //TODO: Throw SecurityException if user has no perms
      if(handler == null)
      {
         try
         {
             handler = this.instantiateCallbackHandler();  
         } 
         catch(Exception e)
         {
            throw new AuthException(e.getLocalizedMessage());
         }
      }
      
         
      return new JBossClientAuthConfig(layer,appContext, handler, contextProperties);
   }
   
   /**
    * @see AuthConfigProvider#getServerAuthConfig(String, String, CallbackHandler)
    */
   public ServerAuthConfig getServerAuthConfig(String layer, String appContext, 
         CallbackHandler handler) throws AuthException
   { 
      //TODO: Throw SecurityException if user has no perms
      if(handler == null)
      {
         try
         {
             handler = this.instantiateCallbackHandler();  
         } 
         catch(Exception e)
         {
            throw new AuthException(e.getLocalizedMessage());
         }
      }
      return new JBossServerAuthConfig(layer,appContext, handler, contextProperties);
   }

   /**
    * @see AuthConfigProvider#refresh()
    */
   public void refresh()
   { 
   } 
   
   //Private Methods
   private CallbackHandler instantiateCallbackHandler() throws Exception
   {
      String cbhClass = System.getProperty(cbhProperty);
      if(cbhClass == null)
         throw PicketBoxMessages.MESSAGES.callbackHandlerSysPropertyNotSet(cbhProperty);
      ClassLoader cl = SecurityActions.getContextClassLoader();
      Class<?> cls = cl.loadClass(cbhClass);
      
      return (CallbackHandler)cls.newInstance();
   }
}
