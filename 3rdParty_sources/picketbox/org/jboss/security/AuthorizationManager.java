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
package org.jboss.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.RoleGroup;

// $Id$

/**
 * Generalized Authorization Manager Interface. <br/><br/> <b>Replaces the legacy RealmMapping interface</b>
 * 
 * @see org.jboss.security.RealmMapping
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @since Jan 2, 2006
 * @version $Revision$
 */
public interface AuthorizationManager extends BaseSecurityManager
{
   /**
    * Authorize a resource Note: The implementation will try to derive the authenticated subject by some means
    * 
    * @param resource Resource to be authorized
    * @return AuthorizationContext.PERMIT or AuthorizationContext.DENY
    * @throws AuthorizationException
    */
   public int authorize(final Resource resource) throws AuthorizationException;

   /**
    * Authorize a resource for an authenticated subject
    * 
    * @param resource Resource to be authorized
    * @param subject Authenticated Subject
    * @return AuthorizationContext.PERMIT or AuthorizationContext.DENY
    * @throws AuthorizationException
    */
   public int authorize(final Resource resource, final Subject subject) throws AuthorizationException;

   /**
    * Authorize a resource given a role
    * 
    * @param resource
    * @param subject the authenticated subject
    * @param role a role (which can be a nested role)
    * @return AuthorizationContext.PERMIT or AuthorizationContext.DENY
    * @throws AuthorizationException
    */
   public int authorize(final Resource resource, Subject subject, RoleGroup role) throws AuthorizationException;

   /**
    * Authorize a resource given a Group of Principals representing roles
    * 
    * @param resource
    * @param subject the authenticated subject
    * @param roleGroup
    * @return
    * @throws AuthorizationException
    */
   public int authorize(final Resource resource, Subject subject, Group roleGroup) throws AuthorizationException;

   /**
    * Validates the application domain roles to which the operational environment Principal belongs.
    * 
    * @param principal the caller principal as known in the operation environment.
    * @param roles The Set<Principal> for the application domain roles that the principal is to be validated against.
    * @return true if the principal has at least one of the roles in the roles set, false otherwise.
    */
   public boolean doesUserHaveRole(Principal principal, Set<Principal> roles);

   /**
    * Get the Current Roles for the authenticated Subject The AuthorizationManager will apply role generation and role
    * mapping logic configured for the security domain
    * 
    * @param authenticatedSubject
    * @param cbh a CallbackHandler that can be used by the AuthorizationManager to obtain essentials such as
    *            SecurityContext etc
    * @return
    */
   public RoleGroup getSubjectRoles(Subject authenticatedSubject, CallbackHandler cbh);

   /**
    * Return the set of domain roles the principal has been assigned.
    * 
    * @return The Set<Principal> for the application domain roles that the principal has been assigned.
    */
   public Set<Principal> getUserRoles(Principal principal);

   /**
    * Trust usecases may have a need to determine the roles of the target principal which has been derived via a
    * principal from another domain by the Authentication Manager An implementation of this interface may have to
    * contact a trust provider for additional information about the principal
    * 
    * @param targetPrincipal Principal applicable in current domain
    * @param contextMap Read-Only Contextual Information that may be useful for the implementation in determining the
    *            roles.
    * @return roles from the target domain
    */
   public Group getTargetRoles(Principal targetPrincipal, Map<String, Object> contextMap);
}