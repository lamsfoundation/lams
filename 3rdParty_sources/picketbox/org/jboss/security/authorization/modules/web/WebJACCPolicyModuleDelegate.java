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
package org.jboss.security.authorization.modules.web;

import java.io.IOException;
import java.security.CodeSource;
import java.security.Permission;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.jacc.WebResourcePermission;
import javax.security.jacc.WebRoleRefPermission;
import javax.security.jacc.WebUserDataPermission;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.authorization.modules.AbstractJACCModuleDelegate;
import org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import org.jboss.security.authorization.resources.WebResource;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;


//$Id: WebJACCPolicyModuleDelegate.java 62923 2007-05-09 03:08:14Z anil.saldhana@jboss.com $

/**
 *  JACC based authorization module helper that deals with the web layer 
 *  authorization decisions
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  July 7, 2006 
 *  @version $Revision: 62923 $
 */
public class WebJACCPolicyModuleDelegate extends AbstractJACCModuleDelegate
{   
   private Policy policy = Policy.getPolicy(); 
   private HttpServletRequest request = null;
   private CodeSource webCS = null;
   
   private String canonicalRequestURI = null; 

   /**
    * @see AuthorizationModuleDelegate#authorize(org.jboss.security.authorization.Resource, javax.security.auth.Subject, org.jboss.security.identity.RoleGroup)
    */
   @SuppressWarnings("unchecked")
   public int authorize(Resource resource, Subject callerSubject, RoleGroup role)
   {
      if(resource instanceof WebResource == false)
         throw PicketBoxMessages.MESSAGES.invalidType(WebResource.class.getName());

      WebResource webResource = (WebResource) resource;
      
      //Get the context map
      Map<String,Object> map = resource.getMap();
      if(map == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("resourceMap");

      //Get the Request Object
      request = (HttpServletRequest) webResource.getServletRequest();
      
      webCS = webResource.getCodeSource();
      this.canonicalRequestURI = webResource.getCanonicalRequestURI();      

      String roleName = (String)map.get(ResourceKeys.ROLENAME);
      Principal principal = (Principal)map.get(ResourceKeys.HASROLE_PRINCIPAL);
      Set<Principal> roles = (Set<Principal>)map.get(ResourceKeys.PRINCIPAL_ROLES); 
      String servletName = webResource.getServletName();
      Boolean resourceCheck = checkBooleanValue((Boolean)map.get(ResourceKeys.RESOURCE_PERM_CHECK));
      Boolean userDataCheck = checkBooleanValue((Boolean)map.get(ResourceKeys.USERDATA_PERM_CHECK));
      Boolean roleRefCheck = checkBooleanValue((Boolean)map.get(ResourceKeys.ROLEREF_PERM_CHECK)); 
      
      validatePermissionChecks(resourceCheck,userDataCheck,roleRefCheck);
      
      boolean decision = false;
      
      try
      {
         if(resourceCheck)
            decision = this.hasResourcePermission(callerSubject, role);
         else
         if(userDataCheck)
           decision = this.hasUserDataPermission();
         else
         if(roleRefCheck)
            decision = this.hasRole(principal, roleName, roles, servletName);
         else
            PicketBoxLogger.LOGGER.debugInvalidWebJaccCheck();
      }
      catch(IOException ioe)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(ioe);
      }
      return decision ? AuthorizationContext.PERMIT : AuthorizationContext.DENY;
   }

   /**
    * @see AuthorizationModuleDelegate#setPolicyRegistrationManager(PolicyRegistration)
    */
   public void setPolicyRegistrationManager(PolicyRegistration authzM)
   { 
     this.policyRegistration = authzM;
   }     

   //****************************************************************************
   //  PRIVATE METHODS
   //****************************************************************************
   /** See if the given JACC permission is implied using the caller as
    * obtained from either the
    * PolicyContext.getContext(javax.security.auth.Subject.container) or
    * the info associated with the requestPrincipal.
    * 
    * @param perm - the JACC permission to check
    * @param requestPrincpal - the http request getPrincipal
    * @param caller the authenticated subject obtained by establishSubjectContext
    * @return true if the permission is allowed, false otherwise
    */ 
   private boolean checkPolicy(Permission perm, Principal requestPrincpal,
         Subject caller, Role role)
   {  
      // Get the caller principals, its null if there is no caller
      Principal[] principals = getPrincipals(caller,role); 
      
      return checkPolicy(perm, principals);
   }
   
   
   /** See if the given permission is implied by the Policy. This calls
    * Policy.implies(pd, perm) with the ProtectionDomain built from the
    * active CodeSource set by the JaccContextValve, and the given
    * principals.
    * 
    * @param perm - the JACC permission to evaluate
    * @param principals - the possibly null set of principals for the caller
    * @return true if the permission is allowed, false otherwise
    */ 
   private boolean checkPolicy(Permission perm, Principal[] principals)
   { 
      ProtectionDomain pd = new ProtectionDomain(webCS, null, null, principals);
      return policy.implies(pd, perm);
   } 
   
   /**
    * Ensure that the bool is a valid value
    * @param bool
    * @return bool or Boolean.FALSE (when bool is null)
    */
   private Boolean checkBooleanValue(Boolean bool)
   {
      if(bool == null)
         return Boolean.FALSE;
      return bool;
   } 

   
   /**
    * Perform hasResourcePermission Check
    * @param caller
    * @param role
    * @return
    * @throws IOException
    */
   private boolean hasResourcePermission(Subject caller, Role  role)
   throws IOException
   { 
      Principal requestPrincipal = request.getUserPrincipal(); 
      WebResourcePermission perm = new WebResourcePermission(this.canonicalRequestURI, 
                                                     request.getMethod());
      boolean allowed = checkPolicy(perm, requestPrincipal, caller, role );
      if (PicketBoxLogger.LOGGER.isTraceEnabled())
      {
         PicketBoxLogger.LOGGER.traceHasResourcePermission(perm.toString(), allowed);
      }
      return allowed;
   }

   /**
    * Perform hasRole check 
    * @param principal
    * @param roleName
    * @param roles
    * @return
    */
   private boolean hasRole(Principal principal, String roleName, 
         Set<Principal> roles, String servletName)
   { 
      if(servletName == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("servletName");

      WebRoleRefPermission perm = new WebRoleRefPermission(servletName, roleName);
      Principal[] principals = {principal}; 
      if( roles != null )
      {
         principals = new Principal[roles.size()];
         roles.toArray(principals);
      }
      boolean allowed = checkPolicy(perm, principals);
      PicketBoxLogger.LOGGER.traceHasRolePermission(perm.toString(), allowed);
      return allowed;
   }

   /**
    * Perform hasUserDataPermission check for the realm.
    * If this module returns false, the base class (Realm) will
    * make the decision as to whether a redirection to the ssl
    * port needs to be done
    * @return
    * @throws IOException
    */
   private boolean hasUserDataPermission() throws IOException
   { 
      WebUserDataPermission perm = new WebUserDataPermission(this.canonicalRequestURI,
                                               request.getMethod());
      boolean ok = false;
      try
      {
         Principal[] principals = null;
         ok = checkPolicy(perm, principals);
      }
      catch(Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      if (PicketBoxLogger.LOGGER.isTraceEnabled())
      {
         PicketBoxLogger.LOGGER.traceHasUserDataPermission(perm.toString(), ok);
      }
      return ok;
   }

   /**
    * Validate that the access check is made only for one of the 
    * following
    * @param resourceCheck
    * @param userDataCheck
    * @param roleRefCheck
    */
   private void validatePermissionChecks(Boolean resourceCheck,
         Boolean userDataCheck, Boolean roleRefCheck)
   {
      if((resourceCheck == Boolean.TRUE && userDataCheck == Boolean.TRUE && roleRefCheck == Boolean.TRUE )
           || (resourceCheck == Boolean.TRUE && userDataCheck == Boolean.TRUE) 
           || (userDataCheck == Boolean.TRUE && roleRefCheck == Boolean.TRUE))
          throw PicketBoxMessages.MESSAGES.invalidPermissionChecks();
   }
}