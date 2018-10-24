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
package org.jboss.security.auth.spi;

import java.io.IOException;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;

/**
 * Certificate Login Module that uses a properties file to store role information.
 * This works just like the UsersRolesLoginModule, only without the users.properties
 * file. In fact, all the role handling code was borrowed directly from that
 * class.
 * 
 * @author <a href="mailto:jasone@greenrivercomputing.com">Jason Essington</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 * @see org.jboss.security.auth.spi.BaseCertLoginModule
 */
public class CertRolesLoginModule extends BaseCertLoginModule
{
   // see AbstractServerLoginModule
   private static final String ROLES_PROPERTIES = "rolesProperties";
   private static final String DEFAULT_ROLES_PROPERTIES = "defaultRolesProperties";
   private static final String ROLE_GROUP_SEPERATOR = "roleGroupSeperator";

   private static final String[] ALL_VALID_OPTIONS =
   {
	   ROLES_PROPERTIES,DEFAULT_ROLES_PROPERTIES,ROLE_GROUP_SEPERATOR
   };
   
   /** The name of the default properties resource containing user/roles */
   private String defaultRolesRsrcName = "defaultRoles.properties";
   /**
    * The name of the properties resource containing user/roles
    */
   private String rolesRsrcName = "roles.properties";
   /**
    * The roles.properties mappings
    */
   private Properties roles;
   /** The character used to seperate the role group name from the username
    * e.g., '.' in jduke.CallerPrincipal=...
    */
   private char roleGroupSeperator = '.'; 

   /**
    * Initialize this LoginModule.
    * 
    * @param options - the login module option map. Supported options include:
    rolesProperties: The name of the properties resource containing user/roles
      the default is "roles.properties".
    roleGroupSeperator: The character used to seperate the role group name from
      the username e.g., '.' in jduke.CallerPrincipal=... . The default = '.'.
  
    defaultRolesProperties=string: The name of the properties resource containing
      the username to roles mappings that will be used as the defaults
      Properties passed to the usersProperties Properties. This defaults to
      defaultRoles.properties.
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);

      PicketBoxLogger.LOGGER.traceBeginInitialize();

      try
      {
         String option = (String) options.get(ROLES_PROPERTIES);
         if (option != null)
            rolesRsrcName = option;
         option = (String) options.get(DEFAULT_ROLES_PROPERTIES);
         if (option != null)
            defaultRolesRsrcName = option;
         option = (String) options.get(ROLE_GROUP_SEPERATOR);
         if( option != null )
            roleGroupSeperator = option.charAt(0);
         // Load the properties file that contains the list of users and passwords
         loadRoles();
      }
      catch (Exception e)
      {
         // Note that although this exception isn't passed on, users or roles will be null
         // so that any call to login will throw a LoginException.
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(e);
      }

      PicketBoxLogger.LOGGER.traceEndInitialize();
   }

   public boolean login() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginLogin();

      if (roles == null)
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(rolesRsrcName);
      boolean wasSuccessful = super.login();

      PicketBoxLogger.LOGGER.traceEndLogin(wasSuccessful);
      return wasSuccessful;
   }

   /**
    * This method is pretty much straight from the UsersRolesLoginModule.
    * @see org.jboss.security.auth.spi.UsersRolesLoginModule#getRoleSets
    */
   protected Group[] getRoleSets() throws LoginException
   {
      String targetUser = getUsername();
      Group[] roleSets = Util.getRoleSets(targetUser, roles, roleGroupSeperator, this);
      return roleSets;
   }

   private void loadRoles() throws IOException
   {
      roles = Util.loadProperties(defaultRolesRsrcName, rolesRsrcName);
   }

}