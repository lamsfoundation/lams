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
import java.security.CodeSource;
import java.security.Permission;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.RunAs;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.authorization.modules.AbstractJACCModuleDelegate;
import org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import org.jboss.security.authorization.resources.EJBResource;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
 

//$Id$

/**
 *  Authorization Module delegate that deals with the authorization decisions
 *  for the EJB Layer
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jul 6, 2006 
 *  @version $Revision$
 */
public class EJBJACCPolicyModuleDelegate extends AbstractJACCModuleDelegate
{  
   private String ejbName = null;
   private Method ejbMethod = null; 
   private String methodInterface = null;
   private CodeSource ejbCS = null;
   private String roleName = null;  
   private Boolean roleRefCheck = Boolean.FALSE;  
   private RunAsIdentity callerRunAs;

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
      
      this.ejbCS = ejbResource.getCodeSource();
      this.ejbMethod = ejbResource.getEjbMethod();
      this.ejbName = ejbResource.getEjbName();
      this.methodInterface = ejbResource.getEjbMethodInterface();
      RunAs runAs = ejbResource.getCallerRunAsIdentity();
      if (runAs instanceof RunAsIdentity)
        this.callerRunAs = RunAsIdentity.class.cast(runAs);
      
      //isCallerInRole checks
      this.roleName = (String)map.get(ResourceKeys.ROLENAME); 
      
      this.roleRefCheck = (Boolean)map.get(ResourceKeys.ROLEREF_PERM_CHECK);
      if(this.roleRefCheck == Boolean.TRUE)
         return checkRoleRef(callerSubject, role);
      else
         return process(callerSubject, role);
   } 
   
   /**
    * Process the request
    * @param callerSubject
    * @param role
    * @return
    */
   private int process(Subject callerSubject, Role role)
   {
      EJBMethodPermission methodPerm =
         new EJBMethodPermission(ejbName, methodInterface, ejbMethod);
      boolean policyDecision = checkWithPolicy(methodPerm, callerSubject, role);
      if( policyDecision == false && PicketBoxLogger.LOGGER.isDebugEnabled() )
      {
          PicketBoxLogger.LOGGER.debugJACCDeniedAccess(methodPerm.toString(), callerSubject,
                  role != null ? role.toString() : null);
      }
      return policyDecision ? AuthorizationContext.PERMIT : AuthorizationContext.DENY;
   }
   
   private int checkRoleRef(Subject callerSubject, RoleGroup callerRoles)
   {
      //This has to be the EJBRoleRefPermission
      EJBRoleRefPermission ejbRoleRefPerm = new EJBRoleRefPermission(ejbName,roleName);
      boolean policyDecision = checkWithPolicy(ejbRoleRefPerm, callerSubject, callerRoles);
      if( policyDecision == false && PicketBoxLogger.LOGGER.isDebugEnabled() )
      {
         PicketBoxLogger.LOGGER.debugJACCDeniedAccess(ejbRoleRefPerm.toString(), callerSubject,
                 callerRoles != null ? callerRoles.toString() : null);
      }
      return policyDecision ? AuthorizationContext.PERMIT : AuthorizationContext.DENY; 
   }
   
   private boolean checkWithPolicy(Permission ejbPerm, Subject subject, Role role)
   {
      // caller is using the caller identity
      if (this.callerRunAs == null)
      {
         Principal[] principals = this.getPrincipals(subject, role);
         ProtectionDomain pd = new ProtectionDomain (ejbCS, null, null, principals);
         return Policy.getPolicy().implies(pd, ejbPerm);
      }
      // caller is using a run-as identity
      else {
         Set<Principal> principals = this.callerRunAs.getRunAsRoles();
         ProtectionDomain pd = new ProtectionDomain (ejbCS, null, null, principals.toArray(new Principal[principals.size()]));
         return Policy.getPolicy().implies(pd, ejbPerm);
      }
   }
}