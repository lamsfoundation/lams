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
package org.jboss.security.acl;

import org.jboss.security.identity.Identity;

/**
 * <p>
 * This interface represents an entry in the Access Control List.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public interface ACLEntry
{

   /**
    * <p>
    * Obtains the identity or role for which a permission has been assigned in this entry.
    * </p>
    * 
    * @return a {@code String} representing the identity or role name.
    */
   public String getIdentityOrRole();

   /**
    * <p>
    * Obtains the {@code Identity} for which a permission has been assigned in this entry.
    * </p>
    * 
    * @return a reference to the {@code Identity} contained in this entry.
    */
   public Identity getIdentity();

   /**
    * <p>
    * Obtains the {@code Permission} object held by this entry.
    * </p>
    * 
    * @return a reference to the {@code Permission} contained in this entry.
    */
   public ACLPermission getPermission();

   /**
    * <p>
    * Checks if the specified permission is part of the this entry's permission.
    * </p>
    * 
    * @param permission the {@code ACLPermission} to be checked for.
    * @return {@code true} if the permission is part of this entry's permission; {@code false} otherwise.
    */
   public boolean checkPermission(ACLPermission permission);
}
