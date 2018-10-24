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

import java.util.Arrays;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.module.ServerAuthModule;

/**
 *  A simple implementation of an username/password based 
 *  server auth module. The principal name and password are
 *  passed as options to the module.
 *  @author <mailto:Anil.Saldhana@jboss.org>Anil Saldhana
 *  @since  Dec 6, 2005
 */
public class SimpleServerAuthModule extends AbstractServerAuthModule
{    
   public SimpleServerAuthModule()
   {   
      supportedTypes.add(Object.class);
      supportedTypes.add(Object.class);
   }
   
   public SimpleServerAuthModule(Class<?>[] supTypes)
   { 
      super();
      this.supportedTypes.addAll(Arrays.asList(supTypes));
   } 
 

   /**
    * @see ServerAuthModule#secureResponse(javax.security.auth.message.MessageInfo, javax.security.auth.Subject)
    */
   public AuthStatus secureResponse(MessageInfo param, Subject source) throws AuthException
   {  
      return AuthStatus.SUCCESS;
   }
 
   
   @Override
   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException
   {
    //Construct Callbacks
      NameCallback nc = new NameCallback("Dummy");
      PasswordCallback pc = new PasswordCallback("B" , true);
      try
      {
         this.callbackHandler.handle(new Callback[]{nc,pc});
         String userName = nc.getName();
         String pwd = new String(pc.getPassword());
         
         //Check the options
         if(!(userName.equals(options.get("principal"))
               && (pwd.equals(options.get("pass")))))
         {
            return false;
         }
               
      }
      catch (Exception e)
      {
         throw new AuthException(e.getLocalizedMessage());
      } 
      return true;
   } 
}
