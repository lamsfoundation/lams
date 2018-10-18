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
package org.jboss.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.MessageInfo;

/**
 * AuthenticationManager with JSR-196 Semantics
 * @author Anil.Saldhana@redhat.com
 * @since May 30, 2008
 */
public interface ServerAuthenticationManager extends AuthenticationManager
{  
   /**
    * <p>Authenticate a Subject given the request response JSR-196(JASPI) messages.</p>
    *
    * <p>If any {@link javax.security.auth.message.AuthException} is thrown during the processing it will be available
    * through {@link SecurityContext} context data with a key <code>javax.security.auth.message.AuthException</code>.</p>
    *
    * @param requestMessage 
    * @param clientSubject Pre-created or null subject
    * @param layer Message Layer for the JASPI (Optional):  Default: HTTP
    * @param callbackHandler CallbackHandler
    * @return true if client subject is valid, false otherwise
    */
   boolean isValid(MessageInfo requestMessage, Subject clientSubject, String layer,
         CallbackHandler callbackHandler);
   
   /**
    * <p>Authenticate a Subject given the request response JSR-196(JASPI) messages.</p>
    *
    * <p>If any {@link javax.security.auth.message.AuthException} is thrown during the processing it will be available
    * through {@link SecurityContext} context data with a key <code>javax.security.auth.message.AuthException</code>.</p>
    * 
    * @param messageInfo the object that contains the request and response messages.
    * @param clientSubject the client subject.
    * @param layer the message layer for JASPI.
    * @param appContext the JASPI application context.
    * @param callbackHandler the callback handler instance.
    * @return {@code true} if the client subject is valid; {@code false} otherwise.
    */
   boolean isValid(MessageInfo messageInfo, Subject clientSubject, String layer, String appContext, 
         CallbackHandler callbackHandler);
   
   /**
    * <p>
    * Secures the response encapsulated in the specified {@code MessageInfo} object.
    * </p>
    *
    * <p>If any {@link javax.security.auth.message.AuthException} is thrown during the processing it will be available
    * through {@link SecurityContext} context data with a key <code>javax.security.auth.message.AuthException</code>.</p>
    *
    * @param messageInfo the object that contains the request and response messages.
    * @param serviceSubject an optional server {@code Subject} instance.
    * @param layer  the JASPI message layer. 
    * @param appContext the JASPI application context.
    * @param callbackHandler the {@code CallbackHandler} instance that can be used to obtain further information
    * (such as keys) to secure the response message.
    */
   void secureResponse(MessageInfo messageInfo, Subject serviceSubject, String layer, String appContext, 
         CallbackHandler callbackHandler);

    /**
     * <p>
     * Remove method specific principals and credentials from the subject.
     * </p>
     *
     * @param messageInfo the object that contains the request and response messages.
     * @param subject the subject to be cleaned.
     * @param layer the message layer for JASPI.
     * @param appContext the JASPI application context.
     * @param handler the callback handler instance.
     */
   void cleanSubject(final MessageInfo messageInfo, final Subject subject, final String layer, final String appContext,
                             final CallbackHandler handler);

}