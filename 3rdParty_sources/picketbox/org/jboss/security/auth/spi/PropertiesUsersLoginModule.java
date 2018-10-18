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
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.login.LoginException;

/**
 * A {@code LoginModule} that uses a properties file to store username and password for authentication.
 * No roles are mapped.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class PropertiesUsersLoginModule extends UsersRolesLoginModule
{

   @Override
   protected Group[] getRoleSets() throws LoginException
   {
      return new Group[0];
   }

   @Override
   protected Properties createRoles(Map<String, ?> options) throws IOException
   {
      return new Properties();
   }

}
