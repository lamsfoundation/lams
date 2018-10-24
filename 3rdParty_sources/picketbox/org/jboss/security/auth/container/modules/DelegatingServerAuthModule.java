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

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
 
/**
 *  Server Auth Module that delegates work to a login context 
 *  @author Anil.Saldhana@redhat.com
 *  @since  Jul 25, 2007 
 *  @version $Revision$
 */
@SuppressWarnings({"rawtypes"})
public class DelegatingServerAuthModule extends AbstractServerAuthModule
{  
   private LoginContext loginContext = null;
   private String loginContextName = null;

   public DelegatingServerAuthModule()
   {  
      this.supportedTypes.add(Object.class);
   }
   
   public DelegatingServerAuthModule(String loginModuleStackHolderName)
   {
      this();
      this.loginContextName = loginModuleStackHolderName;
   }
   
   public Class[] getSupportedMessageTypes()
   { 
      Class[] clarr = new Class[this.supportedTypes.size()];
      this.supportedTypes.toArray(clarr);
      return clarr;
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException
   {
      if(loginContext != null)
         try
         {
            loginContext.logout();
         }
         catch (LoginException e)
         {
            throw new AuthException(e.getLocalizedMessage());
         } 
   }

   public AuthStatus secureResponse(MessageInfo messageInfo, Subject arg1) throws AuthException
   { 
      throw new UnsupportedOperationException();
   } 
   
   @Override
   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException
   {
      try
      {
         loginContext = SecurityActions.createLoginContext(getSecurityDomainName(), clientSubject, this.callbackHandler);
         loginContext.login();
         return true;
      }
      catch (Exception e)
      {
          throw new AuthException(e.getLocalizedMessage());
      }   
   }

   private String getSecurityDomainName()
   {
      if(loginContextName != null)
         return loginContextName;
      
      //Check if it is passed in the options
      String domainName = (String) options.get("javax.security.auth.login.LoginContext");
      if(domainName == null)
      {
         domainName = getClass().getName();  
      }
      return domainName;
   }
}