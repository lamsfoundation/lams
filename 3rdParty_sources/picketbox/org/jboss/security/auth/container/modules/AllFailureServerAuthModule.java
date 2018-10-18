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
package org.jboss.security.auth.container.modules;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.module.ServerAuthModule;

/**
 * Server Auth Module that sends a AuthStatus.FAILURE
 * @author Anil.Saldhana@redhat.com
 */
@SuppressWarnings({"rawtypes"})
public class AllFailureServerAuthModule extends AbstractServerAuthModule
{ 
   
   @Override
   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException
   { 
      return false;
   }

   public AuthStatus secureResponse(MessageInfo arg0, Subject arg1) throws AuthException
   { 
      return AuthStatus.FAILURE;
   } 
   
   /**
    * @see ServerAuthModule#getSupportedMessageTypes()
    */ 
   @Override
   public Class[] getSupportedMessageTypes()
   {
      this.supportedTypes.add(Object.class); 
      return super.getSupportedMessageTypes();
   } 
}
