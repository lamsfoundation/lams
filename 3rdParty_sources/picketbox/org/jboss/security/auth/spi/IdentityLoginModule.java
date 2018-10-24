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
package org.jboss.security.auth.spi;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

/**
 * A simple login module that simply associates the principal specified
 * in the module options with any subject authenticated against the module.
 * The type of Principal class used is
 * <code>org.jboss.security.SimplePrincipal.</code>
 * <p>
 * If no principal option is specified a principal with the name of 'guest'
 * is used.
 *
 * @see org.jboss.security.SimpleGroup
 * @see org.jboss.security.SimplePrincipal
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class IdentityLoginModule extends AbstractServerLoginModule
{
   // see AbstractServerLoginModule
   private static final String PRINCIPAL = "principal";
   private static final String ROLES = "roles";

   private static final String[] ALL_VALID_OPTIONS =
   {
	   PRINCIPAL,ROLES
   };
   
   private String principalName;
   private String roleNames;

   public IdentityLoginModule()
   {
   }

   public void initialize(Subject subject, CallbackHandler handler, 
         Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, handler, sharedState, options);
      principalName = (String) options.get(PRINCIPAL);
      if( principalName == null )
         principalName = "guest";
      roleNames = (String) options.get(ROLES);
   }

   @SuppressWarnings("unchecked")
   public boolean login() throws LoginException
   {
      if( super.login() == true )
         return true;

      Principal principal = new SimplePrincipal(principalName);
      subject.getPrincipals().add(principal);
      // Put the principal name into the sharedState map
      sharedState.put("javax.security.auth.login.name", principalName);
      super.loginOk = true;
      return true;
   }

   protected Principal getIdentity()
   {
      Principal principal = new SimplePrincipal(principalName);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException
   {
      SimpleGroup roles = new SimpleGroup("Roles");
      Group[] roleSets = {roles};
      if( roleNames != null )
      {
         StringTokenizer tokenizer = new StringTokenizer(roleNames, ",");
         while( tokenizer.hasMoreTokens() )
         {
            String roleName = tokenizer.nextToken();
            roles.addMember(new SimplePrincipal(roleName));
         }
      }
      return roleSets;
   }
}