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
package org.jboss.security.auth.container.modules;

import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.ClientAuth;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ClientAuthModule;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimplePrincipal;

/**
 *  A simple implementation of an username/password based 
 *  client auth module
 *  @author <mailto:Anil.Saldhana@jboss.org>Anil Saldhana
 *  @since  Dec 5, 2005
 */
@SuppressWarnings({"rawtypes"})
public class SimpleClientAuthModule implements ClientAuthModule
{
   private Class[] supportedTypes = null;
   private SimplePrincipal principal = null;
   private Object credential = null;  
   
   @SuppressWarnings("unused")
   private MessagePolicy requestPolicy = null;
   @SuppressWarnings("unused")
   private MessagePolicy responsePolicy = null;
   @SuppressWarnings("unused")
   private CallbackHandler handler = null;
   @SuppressWarnings("unused")
   private Map options = null; 

   public SimpleClientAuthModule(Class[] supportedTypes)
   { 
      this.supportedTypes = supportedTypes;
   } 

   /**
    * @see ClientAuthModule#initialize(javax.security.auth.message.MessagePolicy, javax.security.auth.message.MessagePolicy, javax.security.auth.callback.CallbackHandler, java.util.Map)
    */
   public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, 
         CallbackHandler handler, Map options)
   throws AuthException
   { 
      this.requestPolicy = requestPolicy;
      this.responsePolicy = responsePolicy;
      this.handler = handler;
      this.options = options; 
   }

   /**
    * @see ClientAuthModule#secureRequest(javax.security.auth.message.MessageInfo, javax.security.auth.Subject)
    */
   public AuthStatus secureRequest(MessageInfo param, Subject source) 
   throws AuthException
   { 
      source.getPrincipals().add(this.principal);
      source.getPublicCredentials().add(this.credential);
      return AuthStatus.SUCCESS;
   }

   /**
    * @see ClientAuthModule#validateResponse(javax.security.auth.message.MessageInfo, javax.security.auth.Subject, javax.security.auth.Subject)
    */
   public AuthStatus validateResponse(MessageInfo messageInfo, Subject source, Subject recipient) throws AuthException
   {  
      //Custom check: Check that the source of the response and the recipient
      // of the response have identical credentials
      Set sourceSet = source.getPrincipals(SimplePrincipal.class);
      Set recipientSet = recipient.getPrincipals(SimplePrincipal.class);
      if(sourceSet == null && recipientSet == null)
         throw new AuthException();
      if(sourceSet.size() != recipientSet.size())
         throw new AuthException(PicketBoxMessages.MESSAGES.sizeMismatchMessage("source", "recipient"));
      return AuthStatus.SUCCESS;
   } 
   
   /**
    * @see ClientAuthModule#getSupportedMessageTypes()
    */
   public Class[] getSupportedMessageTypes()
   { 
      return this.supportedTypes;
   }

   /**
    * @see ClientAuth#cleanSubject(javax.security.auth.message.MessageInfo, javax.security.auth.Subject)
    */
   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException
   { 
      subject.getPrincipals().remove(principal);
      subject.getPublicCredentials().remove(credential); 
   } 
}