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
import java.util.Set;

/** The interface for Principal mapping. It defines the mapping from the
operational environment Principal to the application domain principal via
the {@link #getPrincipal(Principal) getPrincipal} method. It also defines
the method for validating the application domain roles to which the operational
environment Principal belongs via the {@link #getPrincipal(Principal) getPrincipal}
method.

@author Scott.Stark@jboss.org
@version $Revision$
*/
public interface RealmMapping
{
    /** Map from the operational environment Principal to the application
     domain principal. This is used by the EJBContext.getCallerPrincipal implentation
     to map from the authenticated principal to a principal in the application
     domain.
    @param principal the caller principal as known in the operation environment.
    @return the principal 
    */
    public Principal getPrincipal(Principal principal);

    /** Validates the application domain roles to which the operational
    environment Principal belongs.
    @param principal the caller principal as known in the operation environment.
    @param roles The Set<Principal> for the application domain roles that the
     principal is to be validated against.
    @return true if the principal has at least one of the roles in the roles set,
        false otherwise.
     */
    public boolean doesUserHaveRole(Principal principal, Set<Principal> roles);

    /** Return the set of domain roles the principal has been assigned.
    @return The Set<Principal> for the application domain roles that the
     principal has been assigned.
     */
    public Set<Principal> getUserRoles(Principal principal);
}