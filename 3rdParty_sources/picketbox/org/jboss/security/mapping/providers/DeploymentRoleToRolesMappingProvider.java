/*
  * JBoss, Home of Professional Open Source.
  * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.mapping.providers;
 
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.identity.plugins.SimpleRoleGroup;
import org.jboss.security.mapping.MappingProvider;
import org.jboss.security.mapping.MappingResult;
 

/**
 *  A Role to Roles Mapping Module that takes into consideration a principal
 *  to roles mapping that can be done in the assembly descriptor of
 *  jboss.xml, jboss-web.xml and jboss-app.xml. 
 *  In this case principal denotes role to map other roles to.
 *  
 *  @author pskopek at redhat dot com
 *  @since  Jan 24, 2013 
 *  @version $Revision$
 */
public class DeploymentRoleToRolesMappingProvider implements MappingProvider<RoleGroup>
{

   private MappingResult<RoleGroup> result;

   public void init(Map<String,Object> options)
   { 
   } 

   public void setMappingResult(MappingResult<RoleGroup> res)
   { 
      result = res;
   }

   /**
    * Obtains the deployment roles via the context map and applies it
    * on the mappedObject
    * @see MappingProvider#performMapping(Map, Object)
    */ 
   @SuppressWarnings("unchecked")
   public void performMapping(Map<String,Object> contextMap, RoleGroup mappedObject)
   {  
      if(contextMap == null || contextMap.isEmpty())
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");

      //Obtain the principal to roles mapping
      Principal principal = (Principal) contextMap.get(SecurityConstants.PRINCIPAL_IDENTIFIER);
      Map<String,Set<String>> roleToRolesMap = (Map<String,Set<String>>)contextMap.get(SecurityConstants.DEPLOYMENT_PRINCIPAL_ROLES_MAP);
      Set<Principal> subjectPrincipals = (Set<Principal>) contextMap.get(SecurityConstants.PRINCIPALS_SET_IDENTIFIER);      
      PicketBoxLogger.LOGGER.debugMappingProviderOptions(principal, roleToRolesMap, subjectPrincipals);
      
      
      if(roleToRolesMap == null || roleToRolesMap.isEmpty())
      {
         result.setMappedObject(mappedObject);
         return ; // No Mapping
      }

      RoleGroup newRoles = new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
      
      RoleGroup assignedRoles = (SimpleRoleGroup)contextMap.get(SecurityConstants.ROLES_IDENTIFIER);

      if(assignedRoles != null){      
         for (Role r: assignedRoles.getRoles()) {

            boolean mappedRoleIncluded = false;
            for (String mappedRole: roleToRolesMap.keySet()) {
               if (roleToRolesMap.get(mappedRole).contains(r.getRoleName())) {
                  newRoles.addRole(new SimpleRole(mappedRole));
                  mappedRoleIncluded = true;
               }
            }
         
            if (!mappedRoleIncluded) {
               newRoles.addRole(r);
            }
         
         }
      }

      if(assignedRoles != null){
         mappedObject.clearRoles();
         mappedObject.addAll(newRoles.getRoles());
      } 
      result.setMappedObject(mappedObject);
      
   } 
    
   /**
    * @see MappingProvider#supports(Class)
    */
   public boolean supports(Class<?> p)
   {
      if(RoleGroup.class.isAssignableFrom(p))
         return true;

      return false;
   }

}
