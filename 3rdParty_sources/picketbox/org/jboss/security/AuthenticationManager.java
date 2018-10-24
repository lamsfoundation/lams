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
import java.util.Map;

import javax.security.auth.Subject;

/** The AuthenticationManager is responsible for validating credentials
 * associated with principals.
 *      
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@jboss.org
 * @version $Revision$
 */
public interface AuthenticationManager extends BaseSecurityManager
{   
   /** The isValid method is invoked to see if a user identity and associated
    credentials as known in the operational environment are valid proof of the
    user identity. Typically this is implemented as a call to isValid with a
    null Subject.

    @see #isValid(Principal, Object, Subject)

    @param principal - the user identity in the operation environment 
    @param credential - the proof of user identity as known in the
    operation environment 
    @return true if the principal, credential pair is valid, false otherwise.
   */
   public boolean isValid(Principal principal, Object credential);

   /** The isValid method is invoked to see if a user identity and associated
       credentials as known in the operational environment are valid proof of the
       user identity. This extends AuthenticationManager version to provide a
       copy of the resulting authenticated Subject. This allows a caller to
       authenticate a user and obtain a Subject whose state cannot be modified
       by other threads associated with the same principal.
    @param principal - the user identity in the operation environment 
    @param credential - the proof of user identity as known in the
    operation environment
    @param activeSubject - the Subject which should be populated with the
      validated Subject contents. A JAAS based implementation would typically
      populate the activeSubject with the LoginContext.login result.
    @return true if the principal, credential pair is valid, false otherwise.
   */
   boolean isValid(Principal principal, Object credential,
      Subject activeSubject); 
   
   /** Get the currently authenticated subject. Historically implementations of
    AuthenticationManager isValid methods had the side-effect of setting the
    active Subject. This caused problems with multi-threaded usecases where the
    Subject instance was being shared by multiple threads. This is now deprecated
    in favor of the JACC PolicyContextHandler getContext(key, data) method.

    @deprecated Use the JACC PolicyContextHandler using key "javax.security.auth.Subject.container"
    @see javax.security.jacc.PolicyContextHandler#getContext(String, Object)

    @return The previously authenticated Subject if isValid succeeded, null if
        isValid failed or has not been called for the active thread.
    */
   Subject getActiveSubject();
   
   /**
    * Trust related usecases may require translation of a principal from another domain
    * to the current domain
    * An implementation of this interface may need to do a backdoor contact of the external
    * trust provider in deriving the target principal 
    * @param anotherDomainPrincipal Principal that is applicable in the other domain 
    *                              (Can be null - in which case the contextMap is used
    *                               solely to derive the target principal)
    * @param contextMap Any context information (including information on the other domain 
    *                   that may be relevant in deriving the target principal). Any SAML 
    *                   assertions that may be relevant can be passed here.
    * @return principal from a target security domain
    */
   Principal getTargetPrincipal(Principal anotherDomainPrincipal, Map<String,Object> contextMap);

    /**
     * This method must be invoked to perform the logout of the incoming principal. The {@code Subject} associated with
     * the principal is also provided, allowing implementations to perform any special cleanup based on the information
     * contained in the {@code Subject}.
     *
     * @param principal the {@code Principal} being logged out.
     * @param subject the {@code Subject} associated with the principal being logged out.
     */
   public void logout(Principal principal, Subject subject);
}
