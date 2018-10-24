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
package org.jboss.security.authorization.modules.ejb;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.AnybodyPrincipal;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.RunAs;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import org.jboss.security.authorization.resources.EJBResource;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.identity.plugins.SimpleRoleGroup;
import org.jboss.security.javaee.SecurityRoleRef;
 

//$Id$

/**
 *  Authorization Module delegate that deals with the authorization decisions
 *  for the EJB Layer (Default Behavior)
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jul 6, 2006 
 *  @version $Revision$
 */
public class EJBPolicyModuleDelegate extends AuthorizationModuleDelegate
{  
   protected String ejbName = null;
   protected Method ejbMethod = null; 
   protected Principal ejbPrincipal = null;  
   private RoleGroup methodRoles = null; 
   private String methodInterface = null; 
   protected RunAs callerRunAs = null;
   protected String roleName = null; 
   private Boolean roleRefCheck = Boolean.FALSE;
   protected Set<SecurityRoleRef> securityRoleReferences = null;
   
   private final Role ANYBODY_ROLE = new SimpleRole(AnybodyPrincipal.ANYBODY);
   private final Role STAR_ROLE = new SimpleRole("**");
   private final Principal UNAUTHENTICATED_PRINCIPAL = new SimplePrincipal("anonymous");
   
   protected boolean ejbRestrictions = false;
   
   /**
    * @see AuthorizationModuleDelegate#authorize(org.jboss.security.authorization.Resource, javax.security.auth.Subject, org.jboss.security.identity.RoleGroup)
    */
   public int authorize(Resource resource, Subject callerSubject, RoleGroup role)
   {
      if(resource instanceof EJBResource == false)
         throw PicketBoxMessages.MESSAGES.invalidType(EJBResource.class.getName());

      EJBResource ejbResource = (EJBResource) resource;
      
      //Get the context map
      Map<String,Object> map = resource.getMap();
      if(map == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("resourceMap");

      this.policyRegistration = (PolicyRegistration) map.get(ResourceKeys.POLICY_REGISTRATION);
      
      this.roleName = (String)map.get(ResourceKeys.ROLENAME);
      this.roleRefCheck = (Boolean)map.get(ResourceKeys.ROLEREF_PERM_CHECK); 
      
      this.callerRunAs = ejbResource.getCallerRunAsIdentity();
      this.ejbMethod = ejbResource.getEjbMethod();
      this.ejbName = ejbResource.getEjbName();
      this.ejbPrincipal = ejbResource.getPrincipal();
      this.methodInterface = ejbResource.getEjbMethodInterface();
      this.methodRoles = ejbResource.getEjbMethodRoles();
      this.securityRoleReferences = ejbResource.getSecurityRoleReferences();
      this.ejbRestrictions = ejbResource.isEnforceEJBRestrictions();
      
      if(this.roleRefCheck == Boolean.TRUE)
         return checkRoleRef(role);
      else
         return process(role);
   }    
   
   //Private Methods
   /**
    * Process the request
    * @param principalRole
    * @return
    */
   private int process(RoleGroup principalRole)
   {             
      boolean allowed = true;

       if(this.ejbMethod == null)
           throw PicketBoxMessages.MESSAGES.invalidNullProperty("ejbMethod");

       //Get the method permissions
      if (methodRoles == null)
      {
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            String method = this.ejbMethod.getName();
            PicketBoxLogger.LOGGER.traceNoMethodPermissions(method, methodInterface);
         }
         return AuthorizationContext.DENY;
      }
      if (PicketBoxLogger.LOGGER.isDebugEnabled())
      {
         PicketBoxLogger.LOGGER.debugEJBPolicyModuleDelegateState(ejbMethod.getName(), this.methodInterface, this.methodRoles.toString());
      }

      // Check if the caller is allowed to access the method
      if(methodRoles.containsAll(ANYBODY_ROLE) == false)
      {
         // The caller is using a the caller identity
         if (callerRunAs == null)
         { 
            //AuthorizationManager am = (AuthorizationManager)policyRegistration; 

             // if the principal is authenticated, add the star role to its set of roles.
             if (ejbPrincipal != null && !ejbPrincipal.equals(UNAUTHENTICATED_PRINCIPAL)) {
                 if (principalRole == null) {
                     principalRole = new SimpleRoleGroup(STAR_ROLE.getRoleName());
                 } else {
                    principalRole.addRole(STAR_ROLE);
                 }
             }

            // Now actually check if the current caller has one of the required method roles
            if(principalRole == null)
               throw PicketBoxMessages.MESSAGES.invalidNullProperty("principalRole");

            if(methodRoles.containsAtleastOneRole(principalRole) == false)
            {
               //Set<Principal> userRoles = am.getUserRoles(ejbPrincipal);
               if (PicketBoxLogger.LOGGER.isDebugEnabled())
               {
                  String method = this.ejbMethod.getName();
                  PicketBoxLogger.LOGGER.debugInsufficientMethodPermissions(this.ejbPrincipal, this.ejbName, method,
                          this.methodInterface, this.methodRoles.toString(), principalRole.toString(), null);
               }
               allowed = false;
            } 
         }

         // The caller is using a run-as identity
         else
         {
            if(callerRunAs instanceof RunAsIdentity)
            {
               RunAsIdentity callerRunAsIdentity = (RunAsIdentity) callerRunAs;
               RoleGroup srg = new SimpleRoleGroup(callerRunAsIdentity.getRunAsRoles());
               srg.addRole(STAR_ROLE);
               
               // Check that the run-as role is in the set of method roles
               if(srg.containsAtleastOneRole(methodRoles) == false)
               {
                  String method = this.ejbMethod.getName();
                  if (PicketBoxLogger.LOGGER.isDebugEnabled())
                  {
                     PicketBoxLogger.LOGGER.debugInsufficientMethodPermissions(this.ejbPrincipal, this.ejbName, method,
                             this.methodInterface, this.methodRoles.toString(), null, callerRunAsIdentity.getRunAsRoles().toString());
                  }
                  allowed = false;
               }           
            }
            
         }
      } 
      return allowed ? AuthorizationContext.PERMIT : AuthorizationContext.DENY;
   } 
   
   protected int checkRoleRef(RoleGroup principalRole)
   {
      //AuthorizationManager am = (AuthorizationManager)policyRegistration;
      //Check the caller of this beans run-as identity 
      if (ejbPrincipal == null && callerRunAs == null)
      {
         return AuthorizationContext.DENY;
      } 

      // Map the role name used by Bean Provider to the security role
      // link in the deployment descriptor. The EJB 1.1 spec requires
      // the security role refs in the descriptor but for backward
      // compability we're not enforcing this requirement.
      // To enforce, you need to use the jboss.xml setting
      // <enforce-ejb-restrictions>
      //
       
      boolean matchFound = false;
      Iterator<SecurityRoleRef> it = this.securityRoleReferences.iterator();
      while ( it.hasNext())
      {
         SecurityRoleRef meta = it.next();
         if (meta.getName().equals(roleName))
         {
            roleName = meta.getLink();
            matchFound = true;
            break;
         }
      }
      
      if(!matchFound)
      {
         // A conditional check using jboss.xml <enforce-ejb-restrictions> element
         // which will throw an exception in case no matching
         // security ref is found.
         if(this.ejbRestrictions)
            throw PicketBoxMessages.MESSAGES.noMatchingRoleFoundInDescriptor(this.roleName);
      }
 
      Role deploymentrole = new SimpleRole(roleName);

      boolean allowed = false;
      if (callerRunAs == null)
         allowed = principalRole.containsRole(deploymentrole);
      else
      {
         if(callerRunAs instanceof RunAsIdentity)
         {
            RunAsIdentity callerRunAsIdentity = (RunAsIdentity) callerRunAs;
            SimpleRoleGroup srg = new SimpleRoleGroup(callerRunAsIdentity.getRunAsRoles());
            allowed = srg.containsRole(deploymentrole);
         }
      }
      return allowed ? AuthorizationContext.PERMIT : AuthorizationContext.DENY;
   }
}