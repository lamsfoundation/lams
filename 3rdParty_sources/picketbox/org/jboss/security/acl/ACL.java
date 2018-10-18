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

import java.util.Collection;

import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;

/**
 * <p>
 * This interface represents an Access Control List (ACL), a data structure used to protect access to resources. It is
 * composed of entries, where each entry is represented by the {@code ALCEntry} class and represents the permissions
 * assigned to a given identity.
 * </p>
 * <p>
 * When a client attempts to perform an operation on a resource, the ACL associated to the resource is used to verify if
 * the client has enough permissions to perform that operation. In order to do that, the {@code ACLEntry} corresponding
 * to the client's identity is retrieved and then the permission set contained in the entry is verified to decide if
 * access should be granted or not.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public interface ACL
{

   /**
    * <p>
    * Adds an entry to this ACL. If the ACL already has an {@code ACLEntry} associated to the new entry's identity, then
    * the new entry will not be added.
    * </p>
    * 
    * @param entry the {@code ACLEntry} to be added.
    * @return {@code true} if the entry was added; {@code false} otherwise.
    */
   public boolean addEntry(ACLEntry entry);

   /**
    * <p>
    * Removes an entry from this ACL.
    * </p>
    * 
    * @param entry the {@code ACLEntry} to be removed.
    * @return {@code true} if the entry is removed; {@code false} if the entry can't be found in the ACL.
    */
   public boolean removeEntry(ACLEntry entry);

   /**
    * <p>
    * Obtains the collection of all {@code ACLEntries} in this ACL.
    * </p>
    * 
    * @return a {@code Collection} containing all entries in this ACL.
    */
   public Collection<? extends ACLEntry> getEntries();

   /**
    * <p>
    * Obtains the entry that corresponds to the specified identity. Calling this method is the same as doing
    * {@code getEntry(identity.getName())}.
    * </p>
    * 
    * @param identity a reference to the {@code Identity} object.
    * @return the {@code ACLEntry} that corresponds to the identity, or {@code null} if no entry could be found.
    */
   public ACLEntry getEntry(Identity identity);

   /**
    * <p>
    * Obtains the entry that corresponds to the specified identity or role name.
    * </p>
    * 
    * @param identityOrRole a {@code String} representing an identity or role.
    * @return the {@code ACLEntry} that corresponds to the identity or role or {@code null} if no entry could be found.
    */
   public ACLEntry getEntry(String identityOrRole);

   /**
    * <p>
    * Obtains a reference to the resource being protected by this ACL.
    * </p>
    * 
    * @return a reference to the {@code Resource}.
    */
   public Resource getResource();

   /**
    * <p>
    * Verify if the given permission is assigned to the specified {@code Identity}.
    * </p>
    * 
    * @param permission the {@code ACLPermission} to be checked for.
    * @param identity the {@code Identity} being verified.
    * @return {@code true} if the specified permission is assigned to the identity; {@code false} otherwise.
    */
   public boolean isGranted(ACLPermission permission, Identity identity);
}
