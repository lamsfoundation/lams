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

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.Resource;

/**
 * <p>
 * This class represents a standard entry in the collection returned by the {@code ACLProvider.getEntitlements} method.
 * It contains the permissions that a particular identity or role has over an specific resource.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class EntitlementEntry
{
   private final Resource resource;

   private final ACLPermission permission;

   private final String identityOrRole;
   
   /**
    * <p>
    * Creates an instance of {@code EntitlementEntry} with the specified resource and permissions.
    * </p>
    * 
    * @param resource a reference to the {@code Resource} object.
    * @param permission the permissions a particular identity has over the specified resource.
    * @param identityOrRole a {@code String} containing the name of the identity or the role.
    */
   public EntitlementEntry(Resource resource, ACLPermission permission, String identityOrRole)
   {
      if(resource == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource");
      if (permission == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("permission");
      this.resource = resource;
      this.permission = permission;
      this.identityOrRole = identityOrRole;
   }

   public Resource getResource()
   {
      return this.resource;
   }

   public ACLPermission getPermission()
   {
      return this.permission;
   }
   
   public String getIdentityOrRole()
   {
      return this.identityOrRole;
   }

}
