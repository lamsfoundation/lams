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

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.util.StringPropertyReplacer;

/**
 * A {@code MappingProvider} that reads roles from a properties file in the format
 * 
 * <p>
 * username=role1,role2,...
 * </p>
 * 
 * and adds those roles to the security context for authorization purposes.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class PropertiesRolesMappingProvider extends AbstractRolesMappingProvider
{

   protected String rolesRsrcName = "roles.properties";

   protected Properties roles;
 
   public void init(Map<String, Object> options)
   {
      if (options != null)
      {
         String option = (String) options.get("rolesProperties");
         if (option != null)
            rolesRsrcName = StringPropertyReplacer.replaceProperties(option);

         // read properties file
         try
         {
            roles = loadRoles();
         }
         catch (IOException ioe)
         {
            throw new IllegalStateException(ioe);
         }
      }
   }
 
   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject)
   {
      if (contextMap == null || contextMap.isEmpty())
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");

      //Obtain the principal to roles mapping
      Principal principal = getCallerPrincipal(contextMap);

      if (principal != null)
      {
         String username = principal.getName();
         Util.addRolesToGroup(username, mappedObject, roles);
         result.setMappedObject(mappedObject);
      }
   }

   protected Properties loadRoles() throws IOException
   {
      return Util.loadProperties(rolesRsrcName);
   }

}