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
import java.util.Map;
import java.util.Properties;

/**
 * A simple {@code MappingProvider} that reads roles from the options map.
 * The option key is the username to assign roles to and the option value is
 * the comma separated role names to assign to the user.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class SimpleRolesMappingProvider extends PropertiesRolesMappingProvider
{
   protected Map<String, Object> options;
   
   @Override
   public void init(Map<String, Object> options)
   {
      this.options = options;

      if (options != null)
      {
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
   
   /**
    * Load roles from options map
    */
   @Override
   protected Properties loadRoles() throws IOException
   {
      roles = new Properties();
      for (Map.Entry<String, Object> entry : options.entrySet())
      {
         String key = entry.getKey();
         if (isValidEntry(key))
            roles.put(key, entry.getValue());
      }
      
      return roles;
   }

   /**
    * Removes entries that are valid options for super classes
    * 
    * @param key entry key
    * @return true if entry is valid, false otherwise
    */
   protected boolean isValidEntry(String key)
   {
      if (key.equals("rolesProperties"))
         return false;
      return true;
   }
}
