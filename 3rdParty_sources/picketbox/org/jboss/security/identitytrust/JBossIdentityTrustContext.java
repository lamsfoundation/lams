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
package org.jboss.security.identitytrust;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityContext;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.IdentityTrustInfo;
import org.jboss.security.config.SecurityConfiguration;
import org.jboss.security.identitytrust.IdentityTrustManager.TrustDecision;
import org.jboss.security.identitytrust.config.IdentityTrustModuleEntry;
import org.jboss.security.plugins.ClassLoaderLocator;
import org.jboss.security.plugins.ClassLoaderLocatorFactory;
 
/**
 *  Implementation of the Identity Trust Context
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public class JBossIdentityTrustContext extends IdentityTrustContext
{ 
   public JBossIdentityTrustContext(String secDomain, SecurityContext sc)
   {
      this.securityDomain = secDomain;
      this.securityContext = sc;
   }
   
   @Override
   public TrustDecision isTrusted() throws IdentityTrustException
   { 
      TrustDecision decision = NOTAPPLICABLE;
         
      try
      {
         initializeModules();
      }
      catch (Exception e)
      {
         throw new IdentityTrustException(e);
      }
      //Do a PrivilegedAction
      try
      {
         decision = AccessController.doPrivileged(new PrivilegedExceptionAction<TrustDecision>() 
         {
            public TrustDecision run() throws IdentityTrustException 
            {
               TrustDecision result = invokeTrusted();
               if(result == PERMIT)
                  invokeCommit();
               if(result == DENY || result == NOTAPPLICABLE)
               {
                  invokeAbort();  
               } 
               return result;
            }
         });
      }
      catch (PrivilegedActionException e)
      {
         Exception exc = e.getException();
         invokeAbort();
         throw ((IdentityTrustException)exc);
      }
      return decision; 
   } 
   
   private void initializeModules() throws Exception
   {
	  ClassLoader moduleCL = null;
      //Clear the modules
      modules.clear();
      //Get the Configuration
      ApplicationPolicy aPolicy = SecurityConfiguration.getApplicationPolicy( securityDomain);
      if(aPolicy == null)
         throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(securityDomain);

      IdentityTrustInfo iti = aPolicy.getIdentityTrustInfo();
      if(iti == null)
         return;
      List<String> jbossModuleNames = iti.getJBossModuleNames();
      if(!jbossModuleNames.isEmpty())
      {
    	  ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
    	  if(cll != null)
    	  {
    		  moduleCL = cll.get(jbossModuleNames);
    	  }
      }
      IdentityTrustModuleEntry[] itmearr = iti.getIdentityTrustModuleEntry();
      for(IdentityTrustModuleEntry itme: itmearr)
      { 
         ControlFlag cf = itme.getControlFlag();
         if(cf == null)
            cf = ControlFlag.REQUIRED;
         
         this.controlFlags.add(cf); 
         IdentityTrustModule module = instantiateModule(moduleCL, itme.getName(), itme.getOptions()); 
         modules.add(module); 
      }
   }
   
   @SuppressWarnings({"unchecked", "rawtypes"})
   private IdentityTrustModule instantiateModule(ClassLoader cl, String name, Map map) throws Exception
   {
      IdentityTrustModule im = null;
      try
      {
         Class clazz = SecurityActions.loadClass(cl, name);
         im = (IdentityTrustModule)clazz.newInstance();
      }
      catch ( Exception e)
      {
          PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      if(im == null)
         throw new LoginException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(IdentityTrustModule.class));
      im.initialize(this.securityContext, this.callbackHandler, this.sharedState,map);
      return im;
   }
   
   private TrustDecision invokeTrusted() throws IdentityTrustException
   { 
      //Control Flag behavior
      boolean encounteredRequiredDeny = false; 
      boolean encounteredRequiredNotApplicable = false;
      boolean encounteredOptionalError = false; 
      IdentityTrustException moduleException = null;
      TrustDecision overallDecision = TrustDecision.NotApplicable;
      boolean encounteredRequiredPermit = false;
      
      TrustDecision decision = NOTAPPLICABLE;
      int length = modules.size();
     
      if(length == 0)
         return decision;
      
      for(int i = 0; i < length; i++)
      {
         IdentityTrustModule module = modules.get(i);
         ControlFlag flag = this.controlFlags.get(i);
         try
         {
            decision = module.isTrusted();
         }
         catch(Exception ae)
         {
            decision = NOTAPPLICABLE;
            if(moduleException == null)
               moduleException = new IdentityTrustException(ae);
         }
         
         if(decision == PERMIT)
         { 
            overallDecision =  PERMIT;
            if(flag == ControlFlag.REQUIRED)
               encounteredRequiredPermit = true;
            //SUFFICIENT case
            if(flag == ControlFlag.SUFFICIENT && encounteredRequiredDeny == false)
               return PERMIT;
            continue; //Continue with the other modules
         }
         
         if(decision == NOTAPPLICABLE && flag == ControlFlag.REQUIRED)
         {  
            encounteredRequiredNotApplicable = true;
            continue; //Continue with the other modules
         }
         //Go through the failure cases 
         //REQUISITE case
         if(flag == ControlFlag.REQUISITE)
         {
            if (PicketBoxLogger.LOGGER.isDebugEnabled())
            {
               PicketBoxLogger.LOGGER.debugRequisiteModuleFailure(module.getClass().getName());
            }
            if(moduleException == null)
               moduleException = new IdentityTrustException(PicketBoxMessages.MESSAGES.identityTrustValidationFailedMessage());
            else
               throw moduleException;
         }
         //REQUIRED Case
         if(flag == ControlFlag.REQUIRED)
         {
             if (PicketBoxLogger.LOGGER.isDebugEnabled())
             {
                PicketBoxLogger.LOGGER.debugRequiredModuleFailure(module.getClass().getName());
             }
             encounteredRequiredDeny = true;
         }
         if(flag == ControlFlag.OPTIONAL)
            encounteredOptionalError = true; 
      }
      
      //All the identity trust modules have been visited.
      if(encounteredRequiredDeny)
         return DENY;
      if(overallDecision == DENY && encounteredOptionalError)
         return DENY;
      if(overallDecision == DENY)
         return DENY;
      
      if(encounteredRequiredNotApplicable && !encounteredRequiredPermit)
         return NOTAPPLICABLE;
      return PERMIT;
   }
   
   private void invokeCommit() throws IdentityTrustException
   {
      int length = modules.size();
      for(int i = 0; i < length; i++)
      {
         IdentityTrustModule module = modules.get(i);
         boolean bool = module.commit();
         if(!bool)
            throw new IdentityTrustException(PicketBoxMessages.MESSAGES.moduleCommitFailedMessage());
      }
   }
   
   private void invokeAbort() throws IdentityTrustException
   {
      int length = modules.size();
      for(int i = 0; i < length; i++)
      {
         IdentityTrustModule module = modules.get(i);
         boolean bool = module.abort(); 
         if(!bool)
            throw new IdentityTrustException(PicketBoxMessages.MESSAGES.moduleAbortFailedMessage());
      }
   }
}