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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.config.ControlFlag;

//$Id$

/**
 *  Default Server Authentication Context
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  May 17, 2006 
 *  @version $Revision$
 */
@SuppressWarnings("rawtypes")
public class JBossServerAuthContext implements ServerAuthContext
{  
   private List<ServerAuthModule> modules = new ArrayList<ServerAuthModule>();
     
   private Map<String,Map> moduleOptionsByName = new HashMap<String,Map>();
   
   /**
    * Control Flags for the individual modules
    */
   protected List<ControlFlag> controlFlags = new ArrayList<ControlFlag>();
     
   public JBossServerAuthContext(List<ServerAuthModule> modules,
         Map<String,Map> moduleNameToOptions, CallbackHandler cbh) throws AuthException
   {
      this.modules = modules;
      this.moduleOptionsByName = moduleNameToOptions;
      for(ServerAuthModule sam:modules)
      {
         sam.initialize(null, null, cbh, 
               moduleOptionsByName.get(sam.getClass().getName())); 
      }
   }
   
   public void setControlFlags(List<ControlFlag> controlFlags)
   {
      this.controlFlags = controlFlags;
   }
   
   
   /**
    * @see ServerAuthContext#cleanSubject(javax.security.auth.message.MessageInfo, javax.security.auth.Subject)
    */
   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException
   { 
      for(ServerAuthModule sam:modules)
      {
         sam.cleanSubject(messageInfo, subject);
      }
   }
   
   /**
    * @see ServerAuthContext#secureResponse(javax.security.auth.message.MessageInfo, javax.security.auth.Subject)
    */
   public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException
   { 
      AuthStatus status = null; 
      for(ServerAuthModule sam:modules)
      {
         status = sam.secureResponse(messageInfo, serviceSubject);
      }
      return status;
   }
   
   /**
    * @see ServerAuthContext#validateRequest(javax.security.auth.message.MessageInfo, javax.security.auth.Subject, javax.security.auth.Subject)
    */
   public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, 
         Subject serviceSubject) throws AuthException
   { 
      List<ServerAuthModule> supportingModules = new ArrayList<ServerAuthModule>();
      
      Class requestType = messageInfo.getRequestMessage().getClass();
      Class[] requestInterfaces = requestType.getInterfaces(); 
      
      List<Class> intfaee = Arrays.asList(requestInterfaces);
      
      for(ServerAuthModule sam:modules)
      { 
         List<Class> supportedTypes = Arrays.asList(sam.getSupportedMessageTypes());
         
         //Check the interfaces
         for(Class clazz:intfaee)
         {
            if(supportedTypes.contains(clazz) && !supportingModules.contains(sam)) 
               supportingModules.add(sam);
         } 
         
         //Check the class type also
         if((supportedTypes.contains(Object.class) || supportedTypes.contains(requestType))
               && !supportingModules.contains(sam)) 
            supportingModules.add(sam); 
      }
      if(supportingModules.size() == 0)
         throw PicketBoxMessages.MESSAGES.noServerAuthModuleForRequestType(requestType);

      AuthStatus authStatus = invokeModules(messageInfo, clientSubject, serviceSubject);
      return authStatus;
   } 
   
   private AuthStatus invokeModules(MessageInfo messageInfo,
         Subject clientSubject, Subject serviceSubject) 
   throws AuthException
   {
      //Control Flag behavior
      boolean encounteredRequiredError = false; 
      boolean encounteredOptionalError = false; 
      AuthException moduleException = null;
      AuthStatus overallDecision = AuthStatus.FAILURE;
      
      int length = modules.size();
      for(int i = 0; i < length; i++)
      {
         ServerAuthModule module = (ServerAuthModule)modules.get(i);
         ControlFlag flag = (ControlFlag)this.controlFlags.get(i); 
         AuthStatus decision = AuthStatus.FAILURE;
         try
         {
            decision = module.validateRequest(messageInfo, clientSubject, serviceSubject);
         }
         catch(Exception ae)
         { 
            decision = AuthStatus.FAILURE;
            if(moduleException == null)
               moduleException = new AuthException(ae.getMessage());
         }
         
         if(decision == AuthStatus.SUCCESS)
         { 
            overallDecision =  AuthStatus.SUCCESS;
            //SUFFICIENT case
            if(flag == ControlFlag.SUFFICIENT && encounteredRequiredError == false)
               return AuthStatus.SUCCESS;
            continue; //Continue with the other modules
         }
         //Go through the failure cases 
         //REQUISITE case
         if(flag == ControlFlag.REQUISITE)
         {
            if(moduleException == null)
               moduleException = new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
            else
               throw moduleException;
         }
         //REQUIRED Case
         if(flag == ControlFlag.REQUIRED)
         {
            if(encounteredRequiredError == false)
               encounteredRequiredError = true;
         }
         if(flag == ControlFlag.OPTIONAL)
            encounteredOptionalError = true; 
      }
      
      //All the authorization modules have been visited.
      String msg = getAdditionalErrorMessage(moduleException);
      if(encounteredRequiredError)
         throw new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage() + msg);
      if(overallDecision == AuthStatus.FAILURE && encounteredOptionalError)
         throw new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage() + msg);
      if(overallDecision == AuthStatus.FAILURE)
         throw new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
      return AuthStatus.SUCCESS;
   }
   

   private String getAdditionalErrorMessage(Exception e)
   {
      StringBuilder msg = new StringBuilder(" ");
      if(e != null)
         msg.append(e.getLocalizedMessage());
      return msg.toString();
   }
}