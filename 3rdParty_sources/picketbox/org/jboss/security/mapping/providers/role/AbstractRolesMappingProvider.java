/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.mapping.providers.role;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;
import org.jboss.security.SecurityConstants;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.mapping.MappingProvider;
import org.jboss.security.mapping.MappingResult;

/**
 * Abstract class for Role mapping providers
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public abstract class AbstractRolesMappingProvider implements MappingProvider<RoleGroup>
{
   protected MappingResult<RoleGroup> result;
    
   public boolean supports(Class<?> p)
   {
      if (RoleGroup.class.isAssignableFrom(p))
         return true;

      return false;
   }
    
   public void setMappingResult(MappingResult<RoleGroup> result)
   {
      this.result = result;
   }
   
   protected Principal getCallerPrincipal(Map<String, Object> map)
   {
      Principal principal = (Principal) map.get(SecurityConstants.PRINCIPAL_IDENTIFIER);
      Principal callerPrincipal = null;
      if (principal == null)
      {
         @SuppressWarnings("unchecked")
         Set<Principal> principals = (Set<Principal>) map.get(SecurityConstants.PRINCIPALS_SET_IDENTIFIER);
         if (principals != null && !principals.isEmpty())
         {
            for (Principal p : principals) {
               if (!(p instanceof Group) && principal == null) {
                  principal = p;
               }
               if (p instanceof Group) {
                  Group g = Group.class.cast(p);
                  if (g.getName().equals(SecurityConstants.CALLER_PRINCIPAL_GROUP) && callerPrincipal == null) {
                     Enumeration<? extends Principal> e = g.members();
                     if (e.hasMoreElements())
                        callerPrincipal = e.nextElement();
                  }
               }
            }
         }
      }
      return callerPrincipal == null ? principal : callerPrincipal;
   }
}