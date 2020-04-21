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
package org.jboss.security.auth.container.modules;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.callback.JBossCallbackHandler;

//$Id$

/**
 *  Server Auth Module capable of handling Http Servlet Authentication
 *  @author Anil.Saldhana@redhat.com
 *  @since  Jul 30, 2007 
 *  @version $Revision$
 */
public class HttpServletServerAuthModule extends DelegatingServerAuthModule
{
   public HttpServletServerAuthModule()
   {
      this("");
   }
   
   public HttpServletServerAuthModule(String lmshName)
   {
      super(lmshName);
      this.supportedTypes.add(HttpServletRequest.class);
      this.supportedTypes.add(HttpServletResponse.class);
   }

   @Override
   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException
   {  
      callbackHandler = new JBossCallbackHandler(getUserName(messageInfo),
            getCredential(messageInfo)); 
      return super.validate(clientSubject, messageInfo);
   }

   public AuthStatus secureResponse(MessageInfo arg0, Subject arg1) throws AuthException
   { 
      throw new UnsupportedOperationException();
   }
   
   private Principal getUserName(MessageInfo messageInfo)
   {
      Object requestInfo =  messageInfo.getRequestMessage();
      String userNameParam = (String) options.get("userNameParam");
      if(requestInfo instanceof HttpServletRequest == false)
         throw PicketBoxMessages.MESSAGES.invalidType(HttpServletRequest.class.getName());
      HttpServletRequest hsr = (HttpServletRequest)requestInfo;
      return new SimplePrincipal(hsr.getParameter(userNameParam));
   }
   
   private Object getCredential(MessageInfo messageInfo)
   {
      Object requestInfo = messageInfo.getRequestMessage();
      String passwordParam = (String) options.get("passwordParam");
      if(requestInfo instanceof HttpServletRequest == false)
         throw PicketBoxMessages.MESSAGES.invalidType(HttpServletRequest.class.getName());
      HttpServletRequest hsr = (HttpServletRequest)requestInfo;
      return  hsr.getParameter(passwordParam);
   }
}