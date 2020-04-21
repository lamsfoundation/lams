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
package org.jboss.security.identity;

import java.util.Collection;

//$Id$

/**
 *  Represents a group of roles
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 16, 2007 
 *  @version $Revision$
 */
public interface RoleGroup extends Role
{  
   /**
    * <p>
    * Get the roles contained in the {@code RoleGroup}. The returned {@code List} should be unmodifiable as the
    * {@code RoleGroup} interface provides methods to add and remove roles.
    * </p>
    *
    * @return a unmodifiable {@code Collection} containing the {@code RoleGroup}'s roles.
    */
   public Collection<Role> getRoles();

   /**
    * Add a role
    * @param aRole
    */
   public void addRole(Role aRole);
   
   /**
    * <p>
    * Adds all specified roles to the role group.
    * </p>
    * 
    * @param roles the list of roles to be added.
    */
   public void addAll(Collection<Role> roles);

   /**
    * Clear all the roles
    */
   public void clearRoles();
   
   /**
    * Remove a role
    * @param aRole
    */
   public void removeRole(Role aRole); 
   
   /**
    * Validates whether a simple role is available
    * @param aRole simple role
    * @return
    * @throws IllegalArgumentException role is not simple
    */
   public boolean containsRole(Role aRole);
   
   /**
    * Validates whether there is at least one matching
    * role in "anotherRoleGroup"
    * @param anotherRoleGroup another role group
    * @return
    */
   public boolean containsAtleastOneRole(RoleGroup anotherRoleGroup);
}