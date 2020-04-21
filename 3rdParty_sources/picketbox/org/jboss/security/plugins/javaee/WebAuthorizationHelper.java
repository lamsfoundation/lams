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
package org.jboss.security.plugins.javaee;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.security.AuthorizationManager;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.audit.AuditLevel;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.authorization.resources.WebResource;
import org.jboss.security.callbacks.SecurityContextCallbackHandler;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.identity.plugins.SimpleRoleGroup;
import org.jboss.security.javaee.AbstractWebAuthorizationHelper;

/**
 *  Web Authorization Helper Implementation
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 18, 2008 
 *  @version $Revision$
 */
public class WebAuthorizationHelper 
extends AbstractWebAuthorizationHelper
{
   @Override
   public boolean checkResourcePermission(
         Map<String, Object> contextMap, 
         ServletRequest request,
         ServletResponse response, 
         Subject callerSubject, 
         String contextID, 
         String canonicalRequestURI)
   {
      return checkResourcePermission(contextMap, request, response, callerSubject, contextID, canonicalRequestURI, null);
   }
   
   @Override
   public boolean checkResourcePermission(
         Map<String, Object> contextMap, 
         ServletRequest request,
         ServletResponse response, 
         Subject callerSubject, 
         String contextID, 
         String canonicalRequestURI,
         List<String> roles)
   {
      if(contextID == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      if(request == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("request");
      if(response == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("response");
      if(canonicalRequestURI == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("canonicalRequestURI");

      AuthorizationManager authzMgr = securityContext.getAuthorizationManager();
      
      if(authzMgr == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");

      boolean isAuthorized = false; 

      WebResource webResource = new WebResource(Collections.unmodifiableMap(contextMap));
      webResource.setPolicyContextID(contextID);
      webResource.setServletRequest(request);
      webResource.setServletResponse(response);
      webResource.setCallerSubject(callerSubject);
      webResource.setCanonicalRequestURI(canonicalRequestURI);

      SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
      RoleGroup callerRoles = null;
      if (roles == null)
         callerRoles = authzMgr.getSubjectRoles(callerSubject, sch);
      else
      {
         callerRoles = new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
         for (String role : roles)
         {
            callerRoles.addRole(new SimpleRole(role));
         }
      }

      try
      {
         int permit = authzMgr.authorize(webResource, callerSubject, callerRoles);
         isAuthorized = (permit == AuthorizationContext.PERMIT);
         String level = (permit == AuthorizationContext.PERMIT ? AuditLevel.SUCCESS : AuditLevel.FAILURE);
         if(this.enableAudit)
            this.authorizationAudit(level,webResource, null); 
      }
      catch (AuthorizationException e)
      {
         isAuthorized = false;
         if (PicketBoxLogger.LOGGER.isTraceEnabled()) {
             PicketBoxLogger.LOGGER.traceFailureExecutingMethod("hasResourcePermission", e);
         } else {
             PicketBoxLogger.LOGGER.debugFailureExecutingMethod("hasResourcePermission");
         }
         if(this.enableAudit)
            authorizationAudit(AuditLevel.ERROR,webResource,e); 
      }
      return isAuthorized; 
   }

   @Override
   public boolean hasRole(
         String roleName, 
         Principal principal, 
         String servletName, 
         Set<Principal> principalRoles,  
         String contextID,
         Subject callerSubject)
   {
      return hasRole(roleName, principal, servletName, principalRoles, contextID, callerSubject, null);
   }
   
   @Override
   public boolean hasRole(
         String roleName, 
         Principal principal, 
         String servletName, 
         Set<Principal> principalRoles,  
         String contextID,
         Subject callerSubject,
         List<String> roles)
   {
      if(roleName == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleName");
      if(contextID == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      if(callerSubject == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerSubject");

      AuthorizationManager authzMgr = securityContext.getAuthorizationManager();
      if(authzMgr == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");

      boolean hasTheRole = false;
      Map<String,Object> map =  new HashMap<String,Object>();  
      map.put(ResourceKeys.ROLENAME, roleName); 
      map.put(ResourceKeys.ROLEREF_PERM_CHECK, Boolean.TRUE);  
      map.put(ResourceKeys.PRINCIPAL_ROLES, principalRoles);

      map.put(ResourceKeys.POLICY_REGISTRATION, getPolicyRegistration());
      
      WebResource webResource = new WebResource(Collections.unmodifiableMap(map));
      webResource.setPolicyContextID(contextID);
      webResource.setPrincipal(principal);
      webResource.setServletName(servletName);
       
      webResource.setCallerSubject(callerSubject);
      SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext); 
      RoleGroup callerRoles = null;
      if (roles == null)
         callerRoles = authzMgr.getSubjectRoles(callerSubject, sch);
      else
      {
         callerRoles = new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
         for (String role : roles)
         {
            callerRoles.addRole(new SimpleRole(role));
         }
      }
      
      try
      {
         int permit = authzMgr.authorize(webResource, callerSubject, callerRoles);
         hasTheRole = (permit == AuthorizationContext.PERMIT);
         String level = (hasTheRole ? AuditLevel.SUCCESS : AuditLevel.FAILURE);
         if(this.enableAudit)
           this.authorizationAudit(level,webResource, null);
      }
      catch (AuthorizationException e)
      {
         hasTheRole = false;
         if (PicketBoxLogger.LOGGER.isTraceEnabled()) {
             PicketBoxLogger.LOGGER.traceFailureExecutingMethod("hasRole", e);
         } else {
             PicketBoxLogger.LOGGER.debugFailureExecutingMethod("hasRole");
         }
         if(this.enableAudit)
            authorizationAudit(AuditLevel.ERROR,webResource,e); 
      }
      return hasTheRole; 
   }

   @Override
   public boolean hasUserDataPermission(Map<String, Object> contextMap, 
         ServletRequest request,
         ServletResponse response,
         String contextID,
         Subject callerSubject)
   {
      return hasUserDataPermission(contextMap, request, response, contextID, callerSubject, null);
   }
   
   @Override
   public boolean hasUserDataPermission(Map<String, Object> contextMap, 
         ServletRequest request,
         ServletResponse response,
         String contextID,
         Subject callerSubject,
         List<String> roles)
   {
      if(contextID == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      if(callerSubject == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerSubject");
      if(request == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("request");
      if(response == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("response");

      AuthorizationManager authzMgr = securityContext.getAuthorizationManager();
      if(authzMgr == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");

      boolean hasPerm =  false;   
      contextMap.put(ResourceKeys.POLICY_REGISTRATION, getPolicyRegistration());
      
      WebResource webResource = new WebResource(Collections.unmodifiableMap(contextMap)); 
      webResource.setPolicyContextID(contextID);
      webResource.setServletRequest(request);
      webResource.setServletResponse(response);
      
      webResource.setCallerSubject(callerSubject);
      SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext); 
      RoleGroup callerRoles = null;
      if (roles == null)
         callerRoles = authzMgr.getSubjectRoles(callerSubject, sch);
      else
      {
         callerRoles = new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
         for (String role : roles)
         {
            callerRoles.addRole(new SimpleRole(role));
         }
      }
      
      try
      {
         int permit = authzMgr.authorize(webResource, callerSubject, callerRoles);
         hasPerm = (permit == AuthorizationContext.PERMIT);
         String level = (hasPerm ? AuditLevel.SUCCESS : AuditLevel.FAILURE);
         if(this.enableAudit)
            this.authorizationAudit(level,webResource, null);
      }
      catch (AuthorizationException e)
      {
         hasPerm = false;
         if (PicketBoxLogger.LOGGER.isTraceEnabled()) {
             PicketBoxLogger.LOGGER.traceFailureExecutingMethod("hasUserDataPermission", e);
         } else {
             PicketBoxLogger.LOGGER.debugFailureExecutingMethod("hasUserDataPermission");
         }
         if(this.enableAudit)
            authorizationAudit(AuditLevel.ERROR,webResource,e); 
      }
      return hasPerm;
   } 
}