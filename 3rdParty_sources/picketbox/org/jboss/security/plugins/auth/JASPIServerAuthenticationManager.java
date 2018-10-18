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
package org.jboss.security.plugins.auth;

import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.jacc.PolicyContext;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.ServerAuthenticationManager;

/**
 * @author Anil.Saldhana@redhat.com
 */
public class JASPIServerAuthenticationManager 
extends JaasSecurityManagerBase implements ServerAuthenticationManager
{   
   public JASPIServerAuthenticationManager()
   {
      super(); 
   }

   public JASPIServerAuthenticationManager(String securityDomain, CallbackHandler handler)
   {
      super(securityDomain, handler); 
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.ServerAuthenticationManager#isValid(javax.security.auth.message.MessageInfo, javax.security.auth.Subject, java.lang.String, javax.security.auth.callback.CallbackHandler)
    */
   public boolean isValid(MessageInfo requestMessage,Subject clientSubject, String layer,
         CallbackHandler handler)
   {
      return this.isValid(requestMessage, clientSubject, layer, PolicyContext.getContextID(), handler);
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.ServerAuthenticationManager#isValid(javax.security.auth.message.MessageInfo, javax.security.auth.Subject, java.lang.String, java.lang.String, javax.security.auth.callback.CallbackHandler)
    */
   public boolean isValid(MessageInfo messageInfo, Subject clientSubject, String layer, String appContext, 
         CallbackHandler callbackHandler) 
   {

      AuthConfigFactory factory = AuthConfigFactory.getFactory();
      AuthConfigProvider provider = factory.getConfigProvider(layer,appContext,null);
      if(provider == null)
         throw PicketBoxMessages.MESSAGES.invalidNullAuthConfigProviderForLayer(layer, appContext);

      ServerAuthConfig serverConfig = null;
      try
      {
         serverConfig = provider.getServerAuthConfig(layer,appContext,callbackHandler);
      }
      catch (AuthException ae)
      {
         SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
         PicketBoxLogger.LOGGER.errorGettingServerAuthConfig(layer, appContext, ae);
         return false;
      }
      String authContextId = serverConfig.getAuthContextID(messageInfo);
      Properties properties = new Properties();
      properties.setProperty("security-domain", super.getSecurityDomain());

      ServerAuthContext sctx = null;
      try
      {
         sctx = serverConfig.getAuthContext(authContextId, new Subject(), properties);
      }
      catch (AuthException ae)
      {
         SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
         PicketBoxLogger.LOGGER.errorGettingServerAuthContext(authContextId, super.getSecurityDomain(), ae);
         return false;
      }
         
      if(clientSubject == null)
         clientSubject = new Subject();
      Subject serviceSubject = new Subject();

      AuthStatus status = AuthStatus.FAILURE;
      try
      {
           status = sctx.validateRequest(messageInfo, clientSubject, serviceSubject);
         //TODO: Add caching
      }
      catch(AuthException ae)
      {
          SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
          PicketBoxLogger.LOGGER.debugIgnoredException(ae);
      }
      return AuthStatus.SUCCESS == status ;
   }
   
   /*
    * (non-Javadoc)
    * @see org.jboss.security.ServerAuthenticationManager#secureResponse(javax.security.auth.message.MessageInfo, javax.security.auth.Subject, java.lang.String, java.lang.String, javax.security.auth.callback.CallbackHandler)
    */
   public void secureResponse(MessageInfo messageInfo, Subject serviceSubject, String layer, String appContext, 
         CallbackHandler handler)
   {
      AuthConfigFactory factory = AuthConfigFactory.getFactory();
      AuthConfigProvider provider = factory.getConfigProvider(layer, appContext, null);
      if(provider == null)
         throw PicketBoxMessages.MESSAGES.invalidNullAuthConfigProviderForLayer(layer, appContext);

      ServerAuthConfig serverConfig = null;
      try
      {
         serverConfig = provider.getServerAuthConfig(layer, appContext, handler);
      }
      catch (AuthException ae)
      {
         SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
         PicketBoxLogger.LOGGER.errorGettingServerAuthConfig(layer, appContext, ae);
         return;
      }

      String authContextId = serverConfig.getAuthContextID(messageInfo);
      Properties properties = new Properties();
      properties.setProperty("security-domain", super.getSecurityDomain());
      if (serviceSubject == null)
         serviceSubject = new Subject();
      ServerAuthContext sctx = null;
      try
      {
         sctx = serverConfig.getAuthContext(authContextId, serviceSubject, properties);
      }
      catch (AuthException ae)
      {
          SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
          PicketBoxLogger.LOGGER.errorGettingServerAuthContext(authContextId, super.getSecurityDomain(), ae);
          return;
      }

      try
      {
           sctx.secureResponse(messageInfo, serviceSubject);
      }
      catch(AuthException ae)
      {
          SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
          PicketBoxLogger.LOGGER.debugIgnoredException(ae);
      }
   }

   public void cleanSubject(final MessageInfo messageInfo, final Subject subject, final String layer, final String appContext,
                            final CallbackHandler handler)
   {
       AuthConfigFactory factory = AuthConfigFactory.getFactory();
       AuthConfigProvider provider = factory.getConfigProvider(layer, appContext, null);
       if(provider == null)
           throw PicketBoxMessages.MESSAGES.invalidNullAuthConfigProviderForLayer(layer, appContext);

       ServerAuthConfig serverConfig = null;
       try
       {
           serverConfig = provider.getServerAuthConfig(layer, appContext, handler);
       }
       catch (AuthException ae)
       {
           SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
           PicketBoxLogger.LOGGER.errorGettingServerAuthConfig(layer, appContext, ae);
           return;
       }

       String authContextId = serverConfig.getAuthContextID(messageInfo);
       Properties properties = new Properties();
       properties.setProperty("security-domain", super.getSecurityDomain());
       Subject serviceSubject = new Subject();
       ServerAuthContext sctx = null;
       try
       {
           sctx = serverConfig.getAuthContext(authContextId, serviceSubject, properties);
       }
       catch (AuthException ae)
       {
           SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
           PicketBoxLogger.LOGGER.errorGettingServerAuthContext(authContextId, super.getSecurityDomain(), ae);
           return;
       }

       try
       {
           sctx.cleanSubject(messageInfo, subject);
       }
       catch (AuthException ae)
       {
           SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), ae);
           PicketBoxLogger.LOGGER.debugIgnoredException(ae);
       }
   }
   
}