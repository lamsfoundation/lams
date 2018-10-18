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

import javax.security.auth.login.LoginException;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

/**
 * A simple server login module useful to quick setup of security for testing
 * purposes. It implements the following simple algorithm:
 * <ul>
 * <li> if password is null, authenticate the user and assign an identity of "guest"
 *        and a role of "guest".
 * <li> else if password is equal to the user name, assign an identity equal to
 *        the username and both "user" and "guest" roles
 * <li> else authentication fails.
 * </ul>
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class SimpleServerLoginModule extends UsernamePasswordLoginModule
{
   private SimplePrincipal user;
   private boolean guestOnly;

   protected Principal getIdentity()
   {
      Principal principal = user;
      if( principal == null )
         principal = super.getIdentity();
      return principal;
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword)
   {
      boolean isValid = false;
      if( inputPassword == null )
      {
         guestOnly = true;
         isValid = true;
         user = new SimplePrincipal("guest");
      }
      else
      {
         isValid = inputPassword.equals(expectedPassword);
      }
      return isValid;
   }

   protected Group[] getRoleSets() throws LoginException
   {
      Group[] roleSets = {new SimpleGroup("Roles")};
      if( guestOnly == false )
         roleSets[0].addMember(new SimplePrincipal("user"));
      roleSets[0].addMember(new SimplePrincipal("guest"));
      return roleSets;
   }

   protected String getUsersPassword() throws LoginException
   {
      return getUsername();
   }
   
   @Override
   public boolean logout() throws LoginException
   {
      Group[] groups = this.getRoleSets();
      subject.getPrincipals().remove(groups[0]); 
      return super.logout();
   }  
}