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

import java.io.Serializable;

//$Id$

/**
 *  Represents a Role
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 16, 2007 
 *  @version $Revision$
 */
public interface Role extends Serializable
{
   /**
    * Get Name of the Role
    * @return
    */
   public String getRoleName();

   /**
    * Get type of role
    * @return simple,group
    */
   public RoleType getType();

   /**
    * Indicate whether the argument role is equal or contained
    * depending on the role-type
    * @param anotherRole
    * @return true or false
    */
   public boolean containsAll(Role anotherRole);

   /**
    * Gets a reference to the parent role, if one is available.
    * @return   a reference to the parent role if one exists; {@code null} otherwise.
    */
   public Role getParent();
}