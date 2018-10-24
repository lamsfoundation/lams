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
package org.jboss.security.plugins;
 
import static org.jboss.security.SecurityConstants.ROLES_IDENTIFIER;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.AnybodyPrincipal;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.NobodyPrincipal;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.RunAs;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityRolesAssociation;
import org.jboss.security.SecurityUtil;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.Resource;
import org.jboss.security.callbacks.SecurityContextCallback;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.identity.plugins.SimpleRoleGroup;
import org.jboss.security.mapping.MappingContext;
import org.jboss.security.mapping.MappingManager;
import org.jboss.security.mapping.MappingType;
import org.jboss.security.plugins.authorization.JBossAuthorizationContext;

//$Id$

/**
 *  Authorization Manager implementation
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jan 3, 2006 
 *  @version $Revision$
 */
public class JBossAuthorizationManager 
implements AuthorizationManager 
{  
   private final String securityDomain;  
   
   private AuthorizationContext authorizationContext = null;
   
   //Lock deals with synchronization of authorizationContext usage
   private final Lock lock = new ReentrantLock();
   
   public JBossAuthorizationManager(String securityDomainName)
   {
      this.securityDomain = SecurityUtil.unprefixSecurityDomain( securityDomainName );
   } 
   
   /**
    * @see AuthorizationManager#authorize(Resource)
    */
   public int authorize(Resource resource) throws AuthorizationException
   {
      validateResource(resource);
      Subject subject = SubjectActions.getActiveSubject();
      return internalAuthorization(resource,subject, null);
   }
   
   /**
    * @see AuthorizationManager#authorize(Resource, Subject)
    */
   public int authorize(Resource resource, Subject subject)
   throws AuthorizationException
   {
      return internalAuthorization(resource, subject, null);
   }
   
   /**
    * @see AuthorizationManager#authorize(Resource, Subject, RoleGroup) 
    */
   public int authorize(Resource resource, Subject subject,
         RoleGroup role) throws AuthorizationException
   {
      this.validateResource(resource);
      return internalAuthorization(resource, subject, role);
   }

   /**
    * @see AuthorizationManager#authorize(Resource, Subject, Group)
    */
   public int authorize(Resource resource, Subject subject, 
         Group roleGroup) throws AuthorizationException
   { 
      this.validateResource(resource);
      return internalAuthorization(resource, subject, getRoleGroup(roleGroup));
   }

   /** Does the current Subject have a role(a Principal) that equates to one
    of the role names. This method obtains the Group named 'Roles' from
    the principal set of the currently authenticated Subject as determined
    by the SecurityAssociation.getSubject() method and then creates a
    SimplePrincipal for each name in roleNames. If the role is a member of the
    Roles group, then the user has the role. This requires that the caller
    establish the correct SecurityAssociation subject prior to calling this
    method. In the past this was done as a side-effect of an isValid() call,
    but this is no longer the case.
    
    @param principal - ignored. The current authenticated Subject determines
    the active user and assigned user roles.
    @param rolePrincipals - a Set of Principals for the roles to check.
    
    @see java.security.acl.Group;
    @see Subject#getPrincipals()
    */
   public boolean doesUserHaveRole(Principal principal, Set<Principal> rolePrincipals)
   {
      boolean hasRole = false;
      RoleGroup roles = this.getCurrentRoles(principal);
      if (PicketBoxLogger.LOGGER.isTraceEnabled())
      {
         PicketBoxLogger.LOGGER.traceBeginDoesUserHaveRole(principal, roles != null ? roles.toString() : "");
      }
      if(roles != null)
      {
         Iterator<Principal> iter = rolePrincipals.iterator();
         while( hasRole == false && iter.hasNext() )
         {
            Principal role = iter.next();
            hasRole = doesRoleGroupHaveRole(role, roles);
         }
         PicketBoxLogger.LOGGER.traceEndDoesUserHaveRole(hasRole);
      }
      return hasRole;
   }
   
   /** Does the current Subject have a role(a Principal) that equates to one
    of the role names.
    
    @see #doesUserHaveRole(Principal, Set)
    
    @param principal - ignored. The current authenticated Subject determines
    the active user and assigned user roles.
    @param role - the application domain role that the principal is to be
    validated against.
    @return true if the active principal has the role, false otherwise.
    */
   public boolean doesUserHaveRole(Principal principal, Principal role)
   {
      boolean hasRole = false;
      RoleGroup roles = this.getCurrentRoles(principal);
      hasRole = doesRoleGroupHaveRole(role, roles); 
      return hasRole;
   } 
   
   /** Return the set of domain roles the current active Subject 'Roles' group
    found in the subject Principals set.
    
    @param principal - ignored. The current authenticated Subject determines
    the active user and assigned user roles.
    @return The Set<Principal> for the application domain roles that the
    principal has been assigned.
    */
   public Set<Principal> getUserRoles(Principal principal)
   { 
      RoleGroup userRoles = getCurrentRoles(principal);
      return this.getRolesAsSet(userRoles); 
   }  
     
   
   /** Check that the indicated application domain role is a member of the
    user's assigned roles. This handles the special AnybodyPrincipal and
    NobodyPrincipal independent of the Group implementation.
    
    @param role , the application domain role required for access
    @param userRoles , the set of roles assigned to the user
    @return true if role is in userRoles or an AnybodyPrincipal instance, false
    if role is a NobodyPrincipal or no a member of userRoles
    */
   protected boolean doesRoleGroupHaveRole(Principal role, RoleGroup userRoles)
   {
      // First check that role is not a NobodyPrincipal
      if (role instanceof NobodyPrincipal)
         return false;
      
      // Check for inclusion in the user's role set
      boolean isMember = userRoles.containsRole(new SimpleRole(role.getName())); 
      if (isMember == false)
      {   // Check the AnybodyPrincipal special cases
         isMember = (role instanceof AnybodyPrincipal);
      }
      
      return isMember;
   } 
   
   @Override
   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      buf.append("[AuthorizationManager:class=").append(getClass().getName());
      buf.append(":").append(this.securityDomain).append(":");
      buf.append("]");
      return buf.toString();
   } 
   
   //Value added methods
   /**
    * Set the AuthorizationContext
    */
   public void setAuthorizationContext(AuthorizationContext authorizationContext)
   {
      if(authorizationContext == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("authorizationContext");

      String sc = authorizationContext.getSecurityDomain();
      if(this.securityDomain.equals(sc) == false)
         throw PicketBoxMessages.MESSAGES.unexpectedSecurityDomainInContext(this.securityDomain);

      lock.lock();
      try
      { 
         this.authorizationContext = authorizationContext;
      }
      finally
      {
         lock.unlock();
      }
   }
   
   public String getSecurityDomain()
   {
      return this.securityDomain;
   }
   

   /**
    * @see AuthorizationManager#getTargetRoles(Principal, Map)
    */
   public Group getTargetRoles(Principal targetPrincipal, Map<String,Object> contextMap)
   {
      throw new UnsupportedOperationException();
   }

   //Private Methods
   private HashSet<Principal> getRolesAsSet(RoleGroup roles)
   {
      HashSet<Principal> userRoles = null;
      if( roles != null )
      {
         userRoles = new HashSet<Principal>();
         Collection<Role> rolesList = roles.getRoles();
         for(Role r: rolesList)
         {
            userRoles.add(new SimplePrincipal(r.getRoleName()));
         } 
      }
      return userRoles;
   } 

   /**
    * @see AuthorizationManager#getSubjectRoles(Subject, CallbackHandler)
    */
   public RoleGroup getSubjectRoles(Subject authenticatedSubject, CallbackHandler cbh)
   {
      if(authenticatedSubject == null)
         return null;
      
      //Ask the CBH for the SecurityContext
      SecurityContextCallback scb = new SecurityContextCallback();
      try
      {
         cbh.handle(new Callback[]{scb});
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      } 
      SecurityContext sc = scb.getSecurityContext();
      
      //Handle the case of Incoming RunAs
      Principal callerPrincipal = null;
      RunAs callerRunAs = sc.getIncomingRunAs();
      if(callerRunAs != null)
      {
         callerPrincipal = new SimplePrincipal(callerRunAs.getName()); 
      }
      
      RoleGroup roles = this.getCurrentRoles(callerPrincipal, authenticatedSubject, sc);
      if(roles == null)
         roles = new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
      return roles; 
   }  
   
   /*
    * Get the current role group from the security context or
    * the Subject
    * @param principal The Principal in question
    */
   private RoleGroup getCurrentRoles(Principal principal)
   { 
      //Check that the caller is authenticated to the current thread
      Subject subject = SubjectActions.getActiveSubject();  
      
      //Deal with the security context
      SecurityContext sc = SubjectActions.getSecurityContext(); 
      if(sc == null)
      {
         sc = new JBossSecurityContext(securityDomain); 
         SubjectActions.setSecurityContext(sc);   
      } 
      
      return getCurrentRoles(principal,subject,sc); 
   } 
   
   private RoleGroup getCurrentRoles(Principal principal, Subject subject, SecurityContext securityContext)
   {
      if(subject == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("subject");
      if(securityContext == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityContext");

      Group subjectRoles = getGroupFromSubject(subject);
      
      boolean emptyContextRoles = false;
      
      RoleGroup userRoles = securityContext.getUtil().getRoles();
      //Group userRoles = (Group)sc.getData().get(ROLES_IDENTIFIER);
      if(userRoles == null || "true".equalsIgnoreCase(SubjectActions.getRefreshSecurityContextRoles()))
         emptyContextRoles = true;
      userRoles = copyGroups(userRoles, subjectRoles); 
      
      /**
       * Update the roles in the SecurityContext and
       * allow mapping rules be applied only if the SC roles
       * and the subject roles are not the same
       */
      if(subjectRoles != userRoles || emptyContextRoles)
      { 
         MappingManager mm = securityContext.getMappingManager();
         MappingContext<RoleGroup> mc = mm.getMappingContext(MappingType.ROLE.name());
        
         RoleGroup mappedUserRoles = userRoles;
         if(mc != null && mc.hasModules())
         {
            Map<String,Object> contextMap = new HashMap<String,Object>();
            contextMap.put(SecurityConstants.ROLES_IDENTIFIER, userRoles);
            if(principal != null)
              contextMap.put(SecurityConstants.PRINCIPAL_IDENTIFIER, principal);
            //Append any deployment role->principals configuration done by the user
            contextMap.put(SecurityConstants.DEPLOYMENT_PRINCIPAL_ROLES_MAP,
                  SecurityRolesAssociation.getSecurityRoles());
            
            //Append the principals also
            contextMap.put(SecurityConstants.PRINCIPALS_SET_IDENTIFIER, subject.getPrincipals());
            if (PicketBoxLogger.LOGGER.isTraceEnabled())
            {
               PicketBoxLogger.LOGGER.traceRolesBeforeMapping(userRoles != null ? userRoles.toString() : "");
            }

            if(userRoles == null)
               userRoles = this.getEmptyRoleGroup();
            
            mc.performMapping(contextMap, userRoles);
            mappedUserRoles = mc.getMappingResult().getMappedObject();
            if (PicketBoxLogger.LOGGER.isTraceEnabled())
            {
               PicketBoxLogger.LOGGER.traceRolesAfterMapping(userRoles.toString());
            }
         }
         securityContext.getData().put(ROLES_IDENTIFIER, mappedUserRoles);
      } 
      
      //Ensure that the security context has the roles
      if(securityContext.getUtil().getRoles() == null)
         securityContext.getUtil().setRoles(userRoles);

      //Send the final processed (mapping applied) roles
      return userRoles;   
   }
   
   /**
    * Copy the principals from the second group into the first.
    * If the first group is null and the second group is not, the
    * first group will be made equal to the second group
    * @param source
    * @param toCopy
    */
   private RoleGroup copyGroups(RoleGroup source, Group toCopy)
   {
      if(toCopy == null)
         return source;
      if(source == null && toCopy != null) 
         source = this.getEmptyRoleGroup();
      Enumeration<? extends Principal> en = toCopy.members();
      while(en.hasMoreElements())
      {
         source.addRole(new SimpleRole(en.nextElement().getName())); 
      }
       
      return source;
   }
   
   private int internalAuthorization(final Resource resource, Subject subject,
         RoleGroup role)
   throws AuthorizationException
   {
      if(this.authorizationContext == null)
         this.setAuthorizationContext( new JBossAuthorizationContext(this.securityDomain) );
       return this.authorizationContext.authorize(resource, subject, role); 
   }
   
   /**
    * Get the Subject roles by looking for a Group called 'Roles'
    * @param theSubject - the Subject to search for roles
    * @return the Group contain the subject roles if found, null otherwise
    */
   private Group getGroupFromSubject(Subject theSubject)
   {
      if(theSubject == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("theSubject");
      Set<Group> subjectGroups = theSubject.getPrincipals(Group.class);
      Iterator<Group> iter = subjectGroups.iterator();
      Group roles = null;
      while( iter.hasNext() )
      {
         Group grp = iter.next();
         String name = grp.getName();
         if( name.equals(ROLES_IDENTIFIER) )
            roles = grp;
      }
      return roles;
   } 
   
   private RoleGroup getRoleGroup(Group roleGroup)
   {
      if(roleGroup == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleGroup");
      SimpleRoleGroup srg = new SimpleRoleGroup(roleGroup.getName());
      Enumeration<? extends Principal> principals = roleGroup.members();
      while(principals.hasMoreElements())
      {
         srg.addRole(new SimpleRole(principals.nextElement().getName()));
      }
      return srg;  
   }
   

   private void validateResource(Resource resource)
   {
      if(resource == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource");
      if(resource.getMap() == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource.contextMap");
   }
   
   private RoleGroup getEmptyRoleGroup()
   {
      return new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
   }
}