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

// $Id$ 

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;

/**
 * A simple properties file based login module that consults a Java Properties
 * formatted text files for username to password("users.properties") mapping.
 * The name of the properties file may be overriden by the usersProperties option.
 * The properties file are loaded during initialization using the thread context
 * class loader. This means that these files can be placed into the J2EE
 * deployment jar or the JBoss config directory.
 *
 * The users.properties file uses a format:
 * username1=password1
 * username2=password2
 * ...
 *
 * to define all valid usernames and their corresponding passwords.
 *
 * @author Thomas.Diesler@jboss.org
 * @version $Revision$
 */
public class UsersLoginModule extends UsernamePasswordLoginModule
{
    // see AbstractServerLoginModule
   private static final String USER_PROPERTIES = "usersProperties";
	   
   private static final String[] ALL_VALID_OPTIONS =
   {
	   USER_PROPERTIES
   };
   
	/** The name of the properties resource containing user/passwords */
   private String usersRsrcName = "users.properties";
   /** The users.properties values */
   private Properties users;

   /**
    * Initialize this LoginModule.
    * @param options the login module option map. Supported options include:
    * usersProperties: The name of the properties resource containing
    * user/passwords. The default is "users.properties"
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler, 
         Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      try
      {
         // Check for usersProperties & rolesProperties
         String option = (String) options.get(USER_PROPERTIES);
         if (option != null)
            usersRsrcName = option;

         // Load the properties file that contains the list of users and passwords
         loadUsers();
      }
      catch (Exception e)
      {
         // Note that although this exception isn't passed on, users or roles will be null
         // so that any call to login will throw a LoginException.
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(e);
      }
   }

   /**
    * Method to authenticate a Subject (phase 1). This validates that the
    * users properties file were loaded and then calls
    * super.login to perform the validation of the password.
    *
    * @exception javax.security.auth.login.LoginException thrown if the users or roles properties files
    * were not found or the super.login method fails.
    */
   public boolean login() throws LoginException
   {
      if (users == null)
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(usersRsrcName);

      return super.login();
   }

   /**
    * Return a group Roles with no members
    *
    * @return Group[] containing the sets of roles
    */
   protected Group[] getRoleSets() throws LoginException
   {
      return new Group[0];
   }

   protected String getUsersPassword()
   {
      String username = getUsername();
      String password = null;
      if (username != null)
         password = users.getProperty(username, null);
      return password;
   }

   private void loadUsers() throws IOException
   {
      users = loadProperties(usersRsrcName);
   }

   /**
    * Loads the given properties file and returns a Properties object containing the
    * key,value pairs in that file.
    * The properties files should be in the class path.
    */
   private Properties loadProperties(String propertiesName) throws IOException
   {
      Properties bundle = null;
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL url = loader.getResource(propertiesName);
      if (url == null)
         throw PicketBoxMessages.MESSAGES.unableToFindPropertiesFile(propertiesName);

      InputStream is = null;
      try
      {
         is = url.openStream();
         if (is != null)
         {
            bundle = new Properties();
            bundle.load(is);
         }
         else
         {
            throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
         }
         return bundle;
      }
      finally
      {
         safeClose(is);
      }
   }
}