/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.authorization.modules;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.SimplePrincipal;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;

//$Id$

/**
 *  Common methods for the JACC layer
 *  @author Anil.Saldhana@redhat.com
 *  @since  Jan 4, 2008 
 *  @version $Revision$
 */
public abstract class AbstractJACCModuleDelegate extends AuthorizationModuleDelegate
{   
   public abstract int authorize(Resource resource, Subject subject, RoleGroup role); 
   
   protected Principal[] getPrincipals(Subject subject, Role role)
   {
      Set<Principal> principalsSet = null;
      
      if(role != null)
      {
         principalsSet = getPrincipalSetFromRole(role); 
      }
      
      Principal[] arr = null;
      if(principalsSet != null)
      {
         arr = new Principal[principalsSet.size()];
         principalsSet.toArray(arr);
      }
      return arr;
   } 
   
   private Set<Principal> getPrincipalSetFromRole(Role role)
   {
      Set<Principal> principalsSet = new HashSet<Principal>();
      if(role instanceof RoleGroup)
      {
         RoleGroup rg = (RoleGroup) role;
         Collection<Role> rolesList = rg.getRoles();
         for(Role r: rolesList)
         {
           principalsSet.add(new SimplePrincipal(r.getRoleName()));      
         }
      }
      else
         principalsSet.add(new SimplePrincipal(role.getRoleName()));
      return principalsSet;
   }
}