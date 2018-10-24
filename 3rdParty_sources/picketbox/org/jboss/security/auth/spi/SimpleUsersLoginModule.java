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
package org.jboss.security.auth.spi;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.security.SecurityConstants;

/**
 * A {@code LoginModule} that stores username and password as options.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class SimpleUsersLoginModule extends PropertiesUsersLoginModule
{

   protected static Set<String> invalidProperties = new HashSet<String>();
   
   // adding valid properties' keys from super classes
   {
      invalidProperties.add("usersProperties");
      invalidProperties.add("defaultUsersProperties");
      invalidProperties.add("rolesProperties");
      invalidProperties.add("defaultRolesProperties");
      invalidProperties.add("roleGroupSeperator");
      invalidProperties.add("digestCallback");
      invalidProperties.add("storeDigestCallback");
      invalidProperties.add("legacyCreatePasswordHash");
      invalidProperties.add("inputValidator");
      invalidProperties.add("hashAlgorithm");
      invalidProperties.add("hashEncoding");
      invalidProperties.add("hashCharset");
      invalidProperties.add("hashStorePassword");
      invalidProperties.add("hashUserPassword");
      invalidProperties.add("ignorePasswordCase");
      invalidProperties.add("throwValidateError");
      invalidProperties.add(SecurityConstants.SECURITY_DOMAIN_OPTION);
      invalidProperties.add("password-stacking");
      invalidProperties.add("principalClass");
      invalidProperties.add("unauthenticatedIdentity");
   }
   
   @Override
   protected Properties createUsers(Map<String, ?> options) throws IOException
   {
      Properties properties = new Properties();
      for (Entry<String, ?> entry : options.entrySet())
      {
         String key = entry.getKey();
         Object value = entry.getValue();
         if (value != null && isValidEntry(key))
            properties.put(key, value);
      }
      
      return properties;
   }
   
   protected boolean isValidEntry(String key)
   {
      return !invalidProperties.contains(key);
   }
   
   /**
	* This login module cannot participate in the checking of valid options
	* in AbstractServerLoginModule.
	* Hence this override to prevent false alarms
	*/
   @Override
   protected void checkOptions()
   {
	   // do nothing
   }
}
