/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.acl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;

/**
 * <p>
 * Implementation of {@code ACLProvider} that uses the identity roles when checking if access to a protected resource
 * should be granted or not. If no roles are associated with the specified identity, then the default implementation,
 * which is based on the identity name, is used. Otherwise, {@code #isAccessGranted()} iterates over the roles and if
 * one of the roles has sufficient permissions, then access is granted.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class RoleBasedACLProviderImpl extends ACLProviderImpl
{

   @Override
   @SuppressWarnings("unchecked")
   public <T> Set<T> getEntitlements(Class<T> clazz, Resource resource, Identity identity)
         throws AuthorizationException
   {
      if (identity.getRole() == null)
         return super.getEntitlements(clazz, resource, identity);

      // currently we only provide sets of EntitlementEntry objects.
      if (!EntitlementEntry.class.equals(clazz))
         return null;

      Set<EntitlementEntry> entitlements = new HashSet<EntitlementEntry>();
      // fill the entitlements for each role.
      List<Role> roles = new ArrayList<Role>();
      this.getAllRoles(identity.getRole(), roles);

      for (Role role : roles)
      {
         // get the initial permissions - those that apply to the specified resource.
         ACLPermission permission = super.getInitialPermissions(resource, role.getRoleName());
         if (permission != null)
            super.fillEntitlements(entitlements, resource, role.getRoleName(), permission);
      }
      return (Set<T>) entitlements;
   }

   /**
    * <p>
    * This method overrides the default implementation to use roles instead of the identity name when checking for
    * permissions. If the specified identity has one or more roles associated with it, this implementation will use
    * these roles to check if the identity should be granted access to the resource or not.
    * </p>
    */
   @Override
   public boolean isAccessGranted(Resource resource, Identity identity, ACLPermission permission)
         throws AuthorizationException
   {
      // if there are no roles associated with the identity, use the default implementation
      if (identity.getRole() == null)
         return super.isAccessGranted(resource, identity, permission);

      if (super.strategy != null)
      {
         ACL acl = strategy.getACL(resource);
         if (acl != null)
         {
            // check if any of the identity's roles has access to the resource.
            List<Role> roles = new ArrayList<Role>();
            this.getAllRoles(identity.getRole(), roles);

            for (Role role : roles)
            {
               ACLEntry entry = acl.getEntry(role.getRoleName());
               if (entry != null && entry.checkPermission(permission))
                  return true;
            }
            return false;
         }
         else
            throw new AuthorizationException(PicketBoxMessages.MESSAGES.unableToLocateACLForResourceMessage(
                    resource != null ? resource.toString() : null));
      }
      throw new AuthorizationException(PicketBoxMessages.MESSAGES.unableToLocateACLWithNoStrategyMessage());
   }

   /**
    * <p>
    * This method traverses the role tree that has the specified root role and puts all simple (i.e. not an instance of
    * RoleGroup) roles into the specified roles list.
    * </p>
    * 
    * @param role the root of the role tree.
    * @param roles the {@code List<Role>} that contains the simple roles of the tree.
    */
   protected void getAllRoles(Role role, List<Role> roles)
   {
      if (role instanceof RoleGroup)
      {
         RoleGroup group = (RoleGroup) role;
         for (Role nestedRole : group.getRoles())
            getAllRoles(nestedRole, roles);
      }
      else
         roles.add(role);
   }
}
