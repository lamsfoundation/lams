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
import java.security.acl.Group;
import java.util.Map;
import java.util.Set;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.identity.plugins.SimpleRoleGroup;
import org.jboss.security.mapping.MappingProvider;
import org.jboss.security.mapping.MappingResult;
 

/**
 *  A Role Mapping Module that takes into consideration a principal
 *  to roles mapping that can be done in the assembly descriptor of
 *  jboss.xml, jboss-web.xml and jboss-app.xml
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Nov 1, 2006 
 *  @version $Revision$
 */
public class DeploymentRolesMappingProvider implements MappingProvider<RoleGroup>
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
      Map<String,Set<String>> principalRolesMap = (Map<String,Set<String>>)contextMap.get(SecurityConstants.DEPLOYMENT_PRINCIPAL_ROLES_MAP);
      Set<Principal> subjectPrincipals = (Set<Principal>) contextMap.get(SecurityConstants.PRINCIPALS_SET_IDENTIFIER);
      PicketBoxLogger.LOGGER.debugMappingProviderOptions(principal, principalRolesMap, subjectPrincipals);

      if(principalRolesMap == null || principalRolesMap.isEmpty())
      {
         result.setMappedObject(mappedObject);
         return ; // No Mapping
      }
      
      if(principal != null)
      {
         mappedObject = mapGroup(principal, principalRolesMap, mappedObject);
      }
      
      if(subjectPrincipals != null)
      {
         for(Principal p: subjectPrincipals)
         {
            if(p instanceof Group)
               continue;
            mappedObject = mapGroup(p, principalRolesMap, mappedObject);
         } 
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

   private RoleGroup mapGroup(Principal principal, Map<String, Set<String>> principalRolesMap,
         RoleGroup mappedObject)
   {
      Set<String> roleset = (Set<String>)principalRolesMap.get(principal.getName());
      if(roleset != null)
      {
         RoleGroup newRoles = new SimpleRoleGroup(SecurityConstants.ROLES_IDENTIFIER);
         
         if(roleset != null)
         {
            for(String r:roleset)
            {
               newRoles.addRole(new SimpleRole(r));            
            }
         }
         
         mappedObject.clearRoles();
         mappedObject.addAll(newRoles.getRoles()); 
      } 
      return mappedObject;
   } 
}