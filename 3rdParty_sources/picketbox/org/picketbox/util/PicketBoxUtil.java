/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
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
package org.picketbox.util;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.SecurityConstants;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRoleGroup;

/**
 * Utility Methods
 * @author Anil.Saldhana@redhat.com
 * @since Feb 5, 2010
 */
public class PicketBoxUtil
{
   /**
    * Given a JAAS Subject, will look for {@code Group} principals
    * with name "Roles" and return that in a {@code RoleGroup}
    * @param subject
    * @return a RoleGroup containing the roles
    */
   public static RoleGroup getRolesFromSubject(Subject subject)
   {
      Set<Group> groupPrincipals = subject.getPrincipals(Group.class);
      if(groupPrincipals!= null)
      {
         for(Group groupPrincipal: groupPrincipals)
         {
            if(SecurityConstants.ROLES_IDENTIFIER.equals(groupPrincipal.getName()))
                  return new SimpleRoleGroup(groupPrincipal);  
         }
      }
      return null;
   }
   
   /**
    * Get the first non-group principal
    * @param subject
    * @return
    */
   public static Principal getPrincipalFromSubject(Subject subject)
   {
      Set<Principal> principals = subject.getPrincipals();
      if(principals != null)
      {
         for(Principal p: principals)
         {
            if(p instanceof Group != false)
               return p; 
         }
      }
      return null;
   }

}