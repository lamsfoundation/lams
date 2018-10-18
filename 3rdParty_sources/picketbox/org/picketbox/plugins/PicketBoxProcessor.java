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
package org.picketbox.plugins;

import java.security.Principal;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.annotation.Authentication;
import org.jboss.security.annotation.Authorization;
import org.jboss.security.annotation.Module;
import org.jboss.security.annotation.ModuleOption;
import org.jboss.security.annotation.ModuleOption.VALUE_TYPE;
import org.jboss.security.annotation.SecurityAudit;
import org.jboss.security.annotation.SecurityConfig;
import org.jboss.security.annotation.SecurityDomain;
import org.jboss.security.annotation.SecurityMapping;
import org.jboss.security.audit.config.AuditProviderEntry;
import org.jboss.security.auth.login.AuthenticationInfo;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.config.AuthorizationModuleEntry;
import org.jboss.security.callbacks.SecurityContextCallbackHandler;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.ApplicationPolicyRegistration;
import org.jboss.security.config.AuditInfo;
import org.jboss.security.config.AuthorizationInfo;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.MappingInfo;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.mapping.config.MappingModuleEntry;
import org.picketbox.config.PicketBoxConfiguration;
import org.picketbox.core.authorization.resources.POJOResource;
import org.picketbox.exceptions.PicketBoxProcessingException;
import org.picketbox.factories.SecurityFactory;

/**
 * <p> Process the security annotations on a POJO.</p>
 * <p>
 * Additionally, there are various useful methods such as {@code #getCallerPrincipal()} to
 * get the authenticated principal, {@code #getCallerSubject()} to get the authenticated
 * subject and {@code #getCallerRoles()} to get the roles for the authenticated subject.
 * </p>
 * @since Feb 16, 2010
 */
public class PicketBoxProcessor
{

   private Principal principal = null;
   private Object credential = null;
   
   public PicketBoxProcessor()
   {   
   } 
   
   /**
    * <p>
    * Set the user name/ Credential
    * </p>
    * 
    * <p>
    * In the case of X509 certificates, they can be passed
    * as the Credential into this method.
    * </p>
    * 
    * @param userName
    * @param credential
    */
   public void setSecurityInfo(String userName, Object credential)
   {
      this.principal = new SimplePrincipal(userName);
      this.credential = credential; 
   }
   
   /**
    * Get the authenticated principal
    * @return 
    * @throws PicketBoxProcessingException 
    */
   public Principal getCallerPrincipal() throws PicketBoxProcessingException
   {
      Principal principal = null;
      
      SecurityContext securityContext = null;
      try
      {
         securityContext = SecurityActions.getSecurityContext();
      }
      catch (PrivilegedActionException pae)
      {
         throw new PicketBoxProcessingException(pae.getCause());
      }
      if(securityContext != null)
         principal = securityContext.getUtil().getUserPrincipal(); 
      return principal;
   }
   
   /**
    * Get the caller roles
    * @return 
    * @throws PicketBoxProcessingException 
    */
   public RoleGroup getCallerRoles() throws PicketBoxProcessingException
   {
      RoleGroup roleGroup = null;
      
      SecurityContext securityContext = null;
      try
      {
         securityContext = SecurityActions.getSecurityContext();
      }
      catch (PrivilegedActionException pae)
      {
         throw new PicketBoxProcessingException(pae.getCause());
      }
      if(securityContext != null)
         roleGroup = securityContext.getUtil().getRoles(); 
      return roleGroup;
   }
   
   /**
    * Get the caller subject
    * @return 
    * @throws PicketBoxProcessingException 
    */
   public Subject getCallerSubject() throws PicketBoxProcessingException
   {
      Subject subject = new Subject();
      SecurityContext securityContext = null;
      try
      {
         securityContext = SecurityActions.getSecurityContext();
      }
      catch (PrivilegedActionException pae)
      {
         throw new PicketBoxProcessingException(pae.getCause());
      }
      if(securityContext != null)
         subject = securityContext.getUtil().getSubject();
      return subject;
   }
   
   /**
    * Process the POJO for security annotations
    * @param pojo
    * @throws PicketBoxProcessingException 
    * @throws LoginException
    */
   public void process(Object pojo) throws LoginException, PicketBoxProcessingException
   {
      String securityDomain = SecurityConstants.DEFAULT_APPLICATION_POLICY;
      
      Class<?> objectClass = pojo.getClass();
      
      SecurityDomain securityDomainAnnotation = objectClass.getAnnotation(SecurityDomain.class);
      if(securityDomainAnnotation != null)
         securityDomain = securityDomainAnnotation.value();

      SecurityFactory.prepare();
      try
      {
         boolean needAuthorization = false;
         
         SecurityConfig securityConfig = objectClass.getAnnotation(SecurityConfig.class);
         Authentication authenticationAnnotation = objectClass.getAnnotation(Authentication.class);
         
         if(securityConfig == null && authenticationAnnotation == null)
            throw PicketBoxMessages.MESSAGES.invalidSecurityAnnotationConfig();

         if(securityConfig != null)
         { 
            PicketBoxConfiguration idtrustConfig = new PicketBoxConfiguration();
            idtrustConfig.load(securityConfig.fileName());
         } 
         else
         {
            ApplicationPolicyRegistration apr = (ApplicationPolicyRegistration) Configuration.getConfiguration();
            
            ApplicationPolicy aPolicy = new ApplicationPolicy(securityDomain);
            AuthenticationInfo authenticationInfo = getAuthenticationInfo(authenticationAnnotation, securityDomain);
            aPolicy.setAuthenticationInfo(authenticationInfo );
            
            Authorization authorizationAnnotation = objectClass.getAnnotation(Authorization.class);
            SecurityAudit auditAnnotation = objectClass.getAnnotation(SecurityAudit.class);
            SecurityMapping mappingAnnotation = objectClass.getAnnotation(SecurityMapping.class);
            
            if(authorizationAnnotation != null)
            {
               AuthorizationInfo authorizationInfo = getAuthorizationInfo(authorizationAnnotation, securityDomain);
               aPolicy.setAuthorizationInfo(authorizationInfo);
               
               needAuthorization = true;
            }
            
            if(auditAnnotation != null)
            {
               AuditInfo auditInfo = getAuditInfo(auditAnnotation, securityDomain);
               aPolicy.setAuditInfo(auditInfo);
            }
            
            if(mappingAnnotation != null)
            {
               MappingInfo mappingInfo = getMappingInfo(mappingAnnotation, securityDomain);
               
               List<MappingModuleEntry> entries = mappingInfo.getModuleEntries();
               for(MappingModuleEntry entry: entries)
               {
                  aPolicy.setMappingInfo(entry.getMappingModuleType(), mappingInfo);
               } 
            }
            
            apr.addApplicationPolicy(securityDomain, aPolicy); 
         }
         

         
         SecurityContext securityContext = SecurityActions.createSecurityContext(securityDomain);
         SecurityActions.setSecurityContext(securityContext);
         
         AuthenticationManager authMgr = SecurityFactory.getAuthenticationManager(securityDomain);
         
         Subject subject = new Subject();
         boolean valid = authMgr.isValid(principal, credential, subject);
         if(!valid)
            throw new LoginException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());

         SecurityActions.register(securityContext, principal, credential, subject); 
         AuthorizationManager authzMgr = SecurityFactory.getAuthorizationManager(securityDomain);
         SecurityContextCallbackHandler cbh = new SecurityContextCallbackHandler(securityContext);
         
         //We try to get the roles of the current authenticated subject. This internally will also
         //apply the role mapping logic if it is configured at the security domain level
         RoleGroup roles = authzMgr.getSubjectRoles(subject, cbh); 
         if(roles == null)
            throw new PicketBoxProcessingException(PicketBoxMessages.MESSAGES.nullRolesInSubjectMessage());
         
         if(needAuthorization)
         {
            int permit =  authzMgr.authorize(new POJOResource(pojo), subject, roles);
            if(permit != AuthorizationContext.PERMIT)
               throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
         }
      }
      catch(PrivilegedActionException pae)
      {
         throw new PicketBoxProcessingException(pae.getCause());
      }
      catch (AuthorizationException e)
      {
         throw new PicketBoxProcessingException(e);
      } 
      catch (Exception e)
      {
         throw new PicketBoxProcessingException(e);
      }
      finally
      {
         SecurityFactory.release();
      } 
   }
   
   private MappingInfo getMappingInfo(SecurityMapping mappingAnnotation, String securityDomain)
   {
      MappingInfo mappingInfo = new MappingInfo(securityDomain);
      
      Module[] modules = mappingAnnotation.modules();
      if(modules != null)
      {
         for(Module module: modules)
         {
            String code = module.code().getCanonicalName(); 
            String type = module.type();
             
            Map<String,Object> map = new HashMap<String,Object>();
            
            ModuleOption[] options = module.options();
            if(options != null)
            {
               for(ModuleOption option : options)
               {
                  String key = option.key();
                  String value = option.value(); 
                  VALUE_TYPE valueType = option.valueType();
                  
                  if(key != null && key.length() > 0 && valueType == ModuleOption.VALUE_TYPE.JAVA_PROPERTIES)
                  {
                     StringTokenizer st = new StringTokenizer(value,"=");
                     
                     String prop1 = st.nextToken();
                     String prop2 = st.nextToken();
                     
                     Properties properties = new Properties();
                     properties.put(prop1, prop2); 
                     
                     map.put(key, properties);
                  }
                  else 
                    if(key != null && key.length() > 0)
                       map.put(key, value);
               }
            } 
            
            MappingModuleEntry entry = new MappingModuleEntry(code, map, type);  
            mappingInfo.add(entry); 
         }
      }
      return mappingInfo;
   }

   private AuditInfo getAuditInfo(SecurityAudit auditAnnotation, String securityDomain)
   {
      AuditInfo auditInfo = new AuditInfo(securityDomain);
      
      Module[] modules = auditAnnotation.modules();
      if(modules != null)
      {
         for(Module module: modules)
         {
            String code = module.code().getCanonicalName(); 
             
            Map<String,Object> map = new HashMap<String,Object>();
            
            ModuleOption[] options = module.options();
            if(options != null)
            {
               for(ModuleOption option : options)
               {
                  String key = option.key();
                  String value = option.value(); 
                  if(key != null && key.length() > 0)
                     map.put(key, value);
               }
            } 
            
            AuditProviderEntry entry = new AuditProviderEntry(code, map); 
            
            auditInfo.add(entry); 
         }
      }
      
      return auditInfo;
   }

   private AuthorizationInfo getAuthorizationInfo(Authorization authorizationAnnotation, String securityDomain)
   {
      AuthorizationInfo authorizationInfo = new AuthorizationInfo(securityDomain);
      
      Module[] modules = authorizationAnnotation.modules();
      if(modules != null)
      {
         for(Module module: modules)
         {
            String code = module.code().getCanonicalName();
            String flag = module.flag();
             
            Map<String,Object> map = new HashMap<String,Object>();
            
            ModuleOption[] options = module.options();
            if(options != null)
            {
               for(ModuleOption option : options)
               {
                  String key = option.key();
                  String value = option.value(); 
                  if(key != null && key.length() > 0)
                     map.put(key, value);
               }
            } 
            
            AuthorizationModuleEntry entry = new AuthorizationModuleEntry(code, map);
            entry.setControlFlag(ControlFlag.valueOf(flag));
            
            authorizationInfo.add(entry); 
         }
      }
      
      return authorizationInfo;
   }

   private AuthenticationInfo getAuthenticationInfo(Authentication auth, String securityDomainName)
   {
      AuthenticationInfo authInfo = new AuthenticationInfo(securityDomainName); 
      
      Module[] modules = auth.modules();
      if(modules != null)
      {
         for(Module module: modules)
         {
            String code = module.code().getCanonicalName();
            String flag = module.flag();
             
            Map<String,Object> map = new HashMap<String,Object>();
            
            ModuleOption[] options = module.options();
            if(options != null)
            {
               for(ModuleOption option : options)
               {
                  String key = option.key();
                  String value = option.value(); 
                  if(key != null && key.length() > 0)
                     map.put(key, value);
               }
            } 

            AppConfigurationEntry entry = new AppConfigurationEntry(code, getFlag(flag), map);
            authInfo.addAppConfigurationEntry(entry);
         }
      }
      
      return authInfo;
   }
    
   
   private AppConfigurationEntry.LoginModuleControlFlag getFlag(String flag)
   {
      if("REQUIRED".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.REQUIRED;
      if("REQUISITE".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.REQUISITE;
      if("SUFFICIENT".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.SUFFICIENT;
      return LoginModuleControlFlag.OPTIONAL;
   }
}