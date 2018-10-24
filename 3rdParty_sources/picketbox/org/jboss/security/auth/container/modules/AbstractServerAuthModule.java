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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ServerAuthModule;
import javax.security.auth.spi.LoginModule;

//$Id$

/**
 *  Superclass of all ServerAuthModules
 *  Can be a container for common functionality and custom methods
 *  <p>
 *  The ServerAuthModule can delegate to a login module passed
 *  via the module option "login-module-delegate"
 *  </p>
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jan 9, 2006 
 *  @version $Revision$
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AbstractServerAuthModule implements ServerAuthModule
{  
   /**
    * Call back handler  
    */
   protected CallbackHandler callbackHandler = null;
   
   protected MessagePolicy requestPolicy = null;
   
   protected MessagePolicy responsePolicy = null; 
   
   protected Map options = null;   
   
   protected ArrayList<Class> supportedTypes = new ArrayList<Class>();
   
   /**
    * @see ServerAuthModule#initialize(javax.security.auth.message.MessagePolicy, javax.security.auth.message.MessagePolicy, javax.security.auth.callback.CallbackHandler, java.util.Map)
    */ 
   public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, 
         CallbackHandler handler, Map options )
         throws AuthException
   {
     this.requestPolicy = requestPolicy;
     this.responsePolicy = responsePolicy;
     this.callbackHandler = handler;
     if(options == null)
        options = new HashMap();
     this.options = options; 
   }  

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException
   {
      //Clear out the principals and credentials
      subject.getPrincipals().clear();
      subject.getPublicCredentials().clear();
      subject.getPrivateCredentials().clear();
   }

   /**
    * This method delegates to a login module if configured in the module options.
    * The sub classes will need to validate the request 
    */
   public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, 
         Subject serviceSubject) 
   throws AuthException
   {
      String loginModuleName = (String) options.get("login-module-delegate");
      if(loginModuleName != null)
      {
         ClassLoader tcl = SecurityActions.getContextClassLoader();
         try
         {
            Class clazz = tcl.loadClass(loginModuleName);
            LoginModule lm = (LoginModule) clazz.newInstance();
            lm.initialize(clientSubject, callbackHandler, new HashMap(), options);
            lm.login();
            lm.commit();
         }
         catch (Exception e)
         {
            throw new AuthException(e.getLocalizedMessage());
         }
      } 
      else
      {
         return validate(clientSubject, messageInfo) ? AuthStatus.SUCCESS : AuthStatus.FAILURE;
      } 
      
      return AuthStatus.SUCCESS;
   }
   
   /**
    * @see ServerAuthModule#getSupportedMessageTypes()
    */
   public Class[] getSupportedMessageTypes()
   { 
      Class[] clsarr = new Class[this.supportedTypes.size()];
      supportedTypes.toArray(clsarr);
      return clsarr;
   } 
   
    
   //Value Added Methods 
   public CallbackHandler getCallbackHandler()
   {
      return callbackHandler;
   }
   
   public void setCallbackHandler(CallbackHandler callbackHandler)
   {
      this.callbackHandler = callbackHandler;
   }
   
   /**
    * Subclasses have to implement this method to actually validate the subject
    * @return
    * @throws AuthException
    */
   protected abstract boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException; 
}
