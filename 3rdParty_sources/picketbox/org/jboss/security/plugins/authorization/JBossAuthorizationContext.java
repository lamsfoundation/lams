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
package org.jboss.security.plugins.authorization;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.AuthorizationModule;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.authorization.ResourceType;
import org.jboss.security.authorization.config.AuthorizationModuleEntry;
import org.jboss.security.authorization.modules.DelegatingAuthorizationModule;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.AuthorizationInfo;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.SecurityConfiguration;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.plugins.ClassLoaderLocator;
import org.jboss.security.plugins.ClassLoaderLocatorFactory;

//$Id: JBossAuthorizationContext.java 62954 2007-05-10 04:12:18Z anil.saldhana@jboss.com $

/**
 *  JBAS-3374: Authorization Framework for Policy Decision Modules
 *  For information on the behavior of the Authorization Modules,
 *  For Authorization Modules behavior(Required, Requisite, Sufficient and Optional)
 *  please refer to the javadoc for @see javax.security.auth.login.Configuration
 *  
 *  The AuthorizationContext derives the AuthorizationInfo(configuration for the modules)
 *  in the following way:
 *  a) If there has been an injection of ApplicationPolicy, then it will be used.
 *  b) Util.getApplicationPolicy will be used(which relies on SecurityConfiguration static class).
 *  c) Flag an error that there is no available Application Policy
 *  
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 11, 2006 
 *  @version $Revision: 62954 $
 */
public class JBossAuthorizationContext extends AuthorizationContext
{
   private final String EJB = SecurityConstants.DEFAULT_EJB_APPLICATION_POLICY;
   private final String WEB = SecurityConstants.DEFAULT_WEB_APPLICATION_POLICY;

   private Subject authenticatedSubject = null;

   //Application Policy can be injected
   private ApplicationPolicy applicationPolicy = null;
   
   public JBossAuthorizationContext(String name)
   {
      this.securityDomainName = name; 
   }

   public JBossAuthorizationContext(String name, CallbackHandler handler)
   {
      this(name);
      this.callbackHandler = handler;
   }

   public JBossAuthorizationContext(String name, Subject subject, CallbackHandler handler)
   {
      this(name, handler);
      this.authenticatedSubject = subject;
   }

   /**
    * Inject an ApplicationPolicy that contains AuthorizationInfo
    * @param appPolicy
    * @throws IllegalArgumentException if ApplicationPolicy is null or
    *    does not contain AuthorizationInfo or domain name does not match
    */
   public void setApplicationPolicy(ApplicationPolicy appPolicy)
   {
      if (appPolicy == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("appPolicy");
      AuthorizationInfo authzInfo = appPolicy.getAuthorizationInfo();
      if (authzInfo == null)
         throw PicketBoxMessages.MESSAGES.failedToObtainInfoFromAppPolicy("AuthorizationInfo");
      if (!authzInfo.getName().equals(securityDomainName))
         throw PicketBoxMessages.MESSAGES.unexpectedSecurityDomainInInfo("AuthorizationInfo", this.securityDomainName);
      this.applicationPolicy = appPolicy;
   }

   /**
    * Authorize the Resource
    * @param resource
    * @return AuthorizationContext.PERMIT or AuthorizationContext.DENY
    * @throws AuthorizationException
    */
   public int authorize(final Resource resource) throws AuthorizationException
   {
      return this.authorize(resource, this.authenticatedSubject, (RoleGroup) resource.getMap().get(
            ResourceKeys.SECURITY_CONTEXT_ROLES));
   }

   /**
    * @see AuthorizationContext#authorize(org.jboss.security.authorization.Resource, javax.security.auth.Subject, org.jboss.security.identity.RoleGroup)
    */
   public int authorize(final Resource resource, final Subject subject, final RoleGroup callerRoles)
         throws AuthorizationException
   {  
      final List<AuthorizationModule> modules = new ArrayList<AuthorizationModule>();
      final List<ControlFlag> controlFlags = new ArrayList<ControlFlag>();
      
      try
      {
         this.authenticatedSubject = subject;

         initializeModules(resource, callerRoles, modules, controlFlags); 

         AccessController.doPrivileged(new PrivilegedExceptionAction<Object>()
         {
            public Object run() throws AuthorizationException
            {
               int result = invokeAuthorize(resource, modules, controlFlags);
               if (result == PERMIT)
                  invokeCommit( modules, controlFlags );
               if (result == DENY)
               {
                  invokeAbort( modules, controlFlags );
                  throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
               }
               return null;
            }
         });
      }
      catch (PrivilegedActionException e)
      {
         Exception exc = e.getException();
         invokeAbort( modules, controlFlags );
         throw ((AuthorizationException) exc);
      }
      finally
      { 
         if(modules != null)
            modules.clear();
         if(controlFlags != null )
            controlFlags.clear();  
      }
      return PERMIT;
   }

   //Private Methods  
   private void initializeModules(Resource resource, RoleGroup role, List<AuthorizationModule> modules,
         List<ControlFlag> controlFlags) throws PrivilegedActionException
   {
      AuthorizationInfo authzInfo = getAuthorizationInfo(securityDomainName, resource);
      if (authzInfo == null)
         throw PicketBoxMessages.MESSAGES.failedToObtainAuthorizationInfo(securityDomainName);

      ClassLoader moduleCL = null;
      List<String> jbossModuleNames = authzInfo.getJBossModuleNames();
      if(!jbossModuleNames.isEmpty())
      {
    	  ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
    	  if( cll != null)
    	  {
    		  moduleCL = cll.get(jbossModuleNames);
    	  }
      }
      AuthorizationModuleEntry[] entries = authzInfo.getAuthorizationModuleEntry();
      int len = entries != null ? entries.length : 0;
      for (int i = 0; i < len; i++)
      {
    	  AuthorizationModuleEntry entry = entries[i];
    	  ControlFlag flag = entry.getControlFlag();
    	  if (flag == null)
    	  {
    		  flag = ControlFlag.REQUIRED;
    	  }

    	  controlFlags.add(flag);
    	  AuthorizationModule module = instantiateModule(moduleCL, entry.getPolicyModuleName(), entry.getOptions(), role); 
    	  modules.add(module);
      }
   }

   private int invokeAuthorize(Resource resource, List<AuthorizationModule> modules,
         List<ControlFlag> controlFlags) throws AuthorizationException
   {
      //Control Flag behavior
      boolean encounteredRequiredError = false;
      boolean encounteredOptionalError = false;
      AuthorizationException moduleException = null;
      int overallDecision = DENY;

      int length = modules.size();
      for (int i = 0; i < length; i++)
      {
         AuthorizationModule module = modules.get(i);
         ControlFlag flag = controlFlags.get(i);
         int decision = DENY;
         try
         {
            decision = module.authorize(resource);
         }
         catch (Exception ae)
         {
            decision = DENY;
            if (moduleException == null)
               moduleException = new AuthorizationException(ae.getMessage());
         }

         if (decision == PERMIT)
         {
            overallDecision = PERMIT;
            //SUFFICIENT case
            if (flag == ControlFlag.SUFFICIENT && encounteredRequiredError == false)
               return PERMIT;
            continue; //Continue with the other modules
         }
         //Go through the failure cases 
         //REQUISITE case
         if (flag == ControlFlag.REQUISITE)
         {
            if (PicketBoxLogger.LOGGER.isDebugEnabled())
            {
               PicketBoxLogger.LOGGER.debugRequisiteModuleFailure(module.getClass().getName());
            }
            if (moduleException == null)
               moduleException = new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
            else
               throw moduleException;
         }
         //REQUIRED Case
         if (flag == ControlFlag.REQUIRED)
         {
            if (PicketBoxLogger.LOGGER.isDebugEnabled())
            {
               PicketBoxLogger.LOGGER.debugRequiredModuleFailure(module.getClass().getName());
            }
            if (encounteredRequiredError == false)
               encounteredRequiredError = true;
         }
         if (flag == ControlFlag.OPTIONAL)
            encounteredOptionalError = true;
      }

      //All the authorization modules have been visited.
      String msg = getAdditionalErrorMessage(moduleException);
      if (encounteredRequiredError)
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage() + msg);
      if (overallDecision == DENY && encounteredOptionalError)
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage() + msg);
      if (overallDecision == DENY)
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
      return PERMIT;
   }

   private void invokeCommit( List<AuthorizationModule> modules,
         List<ControlFlag> controlFlags ) throws AuthorizationException
   {
      int length = modules.size();
      for (int i = 0; i < length; i++)
      {
         AuthorizationModule module = modules.get(i);
         boolean bool = module.commit();
         if (!bool)
            throw new AuthorizationException(PicketBoxMessages.MESSAGES.moduleCommitFailedMessage());
      }
   }

   private void invokeAbort( List<AuthorizationModule> modules,
         List<ControlFlag> controlFlags ) throws AuthorizationException
   {
      int length = modules.size();
      for (int i = 0; i < length; i++)
      {
         AuthorizationModule module = modules.get(i);
         boolean bool = module.abort();
         if (!bool)
            throw new AuthorizationException(PicketBoxMessages.MESSAGES.moduleAbortFailedMessage());
      }
   }

   private AuthorizationModule instantiateModule(ClassLoader cl, String name, Map<String, Object> map, RoleGroup subjectRoles)
         throws PrivilegedActionException
   {
      AuthorizationModule am = null;
      try
      {
         Class<?> clazz;
         try
         {
            if(cl == null)
            {
               cl = getClass().getClassLoader();
            }
            clazz = cl.loadClass(name);
         }
         catch (Exception ignore)
         {
            ClassLoader tcl = SecurityActions.getContextClassLoader();
            clazz = tcl.loadClass(name);
         }

         am = (AuthorizationModule) clazz.newInstance();
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugFailureToInstantiateClass(name, e);
      }
      if (am == null)
         throw new IllegalStateException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(AuthorizationModule.class));
      am.initialize(this.authenticatedSubject, this.callbackHandler, this.sharedState, map, subjectRoles);
      return am;
   }

   private AuthorizationInfo getAuthorizationInfo(String domainName, Resource resource)
   {
      ResourceType layer = resource.getLayer();

      //Check if an instance of ApplicationPolicy is available 
      if (this.applicationPolicy != null)
         return applicationPolicy.getAuthorizationInfo();

      ApplicationPolicy aPolicy = SecurityConfiguration.getApplicationPolicy(domainName);

      if (aPolicy == null)
      {
         if (layer == ResourceType.EJB)
            aPolicy = SecurityConfiguration.getApplicationPolicy(EJB);
         else if (layer == ResourceType.WEB)
            aPolicy = SecurityConfiguration.getApplicationPolicy(WEB);
      }
      if (aPolicy == null)
         throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(domainName);

      AuthorizationInfo ai = aPolicy.getAuthorizationInfo();
      if (ai == null)
         return getAuthorizationInfo(layer);
      else
         return aPolicy.getAuthorizationInfo();
   }

   private AuthorizationInfo getAuthorizationInfo(ResourceType layer)
   {
      AuthorizationInfo ai = null;

      if (layer == ResourceType.EJB)
         ai = SecurityConfiguration.getApplicationPolicy(EJB).getAuthorizationInfo();
      else if (layer == ResourceType.WEB)
         ai = SecurityConfiguration.getApplicationPolicy(WEB).getAuthorizationInfo();
      else
      {
         ai = new AuthorizationInfo(SecurityConstants.DEFAULT_APPLICATION_POLICY);
         ai.add(new AuthorizationModuleEntry(DelegatingAuthorizationModule.class.getName()));
      }
      return ai;
   }

   private String getAdditionalErrorMessage(Exception e)
   {
      StringBuilder msg = new StringBuilder(" ");
      if (e != null)
         msg.append(e.getLocalizedMessage());
      return msg.toString();
   } 
}