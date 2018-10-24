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

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.MessageInfo;

import org.jboss.security.AnybodyPrincipal;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.RealmMapping;
import org.jboss.security.SubjectSecurityManager;


/** An implementation of SubjectSecurityManager, RealmMapping that authenticates
everyone and for which Principals have any role requested. It can be used
as a pass-through security manager when you want noop security.

@see #isValid(Principal, Object, Subject)
@see #getPrincipal(Principal)
@see #doesUserHaveRole(Principal, Set)

@author Scott.Stark@jboss.org
@version $Revision$
*/
public class NullSecurityManager
    implements SubjectSecurityManager, RealmMapping, Serializable
{
    static final long serialVersionUID = -5942994627247826747L;
    private String securityDomain;

    /** Creates a default JaasSecurityManager for with the
        given securityDomain name.
    */
    public NullSecurityManager(String securityDomain)
    {
        this.securityDomain = securityDomain;
    }

    /** Get the name of the security domain associated with this security mgr.
        @return Name of the security manager security domain.
     */
    public String getSecurityDomain()
    {
        return securityDomain;
    }
    /** Get the currently authenticated Subject.
        @return Always returns null.
     */
    public Subject getActiveSubject()
    {
        return null;
    }

    /** Validate that the given credential is correct for principal.
    @return always returns true.
     */
    public boolean isValid(Principal principal, Object credential)
    {
        return true;
    }
    /** Validate that the given credential is correct for principal. This does
     not populate the activeSubject with any state since no authentication
     is performed.
    @return always returns true.
     */
    public boolean isValid(Principal principal, Object credential,
      Subject activeSubject)
    {
       return true;
    }
    
    public boolean isValid(MessageInfo requestMessage, Subject clientSubject, String layer)
    { 
       return true;
    }
    
    public boolean isValid(MessageInfo requestMessage, Subject clientSubject, String layer,
          CallbackHandler handler)
    { 
       return true;
    }
    
    /**
     * @see AuthenticationManager#getTargetPrincipal(Principal,Map)
     */
    public Principal getTargetPrincipal(Principal anotherDomainPrincipal, Map<String,Object> contextMap)
    {
       return anotherDomainPrincipal;
    }

    /** Always returns the argument principal.
    @return The argument principal
     */
    public Principal getPrincipal(Principal principal)
    {
        Principal result = principal;
        return result;
    }

    /** Does the current Subject have a role(a Principal) that equates to one
        of the role names. This method always returns true.
    @param principal - ignored.
    @param roleNames - ignored.
    @return Always returns true.
    */
    public boolean doesUserHaveRole(Principal principal, Set<Principal> roleNames)
    {
        boolean hasRole = true;
        return hasRole;
    }

    /** Return the set of domain roles the principal has been assigned.
    @return The Set<Principal> with the AnybodyPrincipal as the sole role.
     */
    public Set<Principal> getUserRoles(Principal principal)
    {
        HashSet<Principal> roles = new HashSet<Principal>();
        roles.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
        return roles;
    }

    /**
     * No-op implementation.
     *
     * @param principal the {@code Principal} being logged out.
     * @param subject the {@code Subject} associated with the principal being logged out.
     */
    public void logout(Principal principal, Subject subject) {
        // nothing to do.
    }
}
