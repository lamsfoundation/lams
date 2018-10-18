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
import org.jboss.security.util.StringPropertyReplacer;

/** A simple Properties map based login module that consults two Java Properties
 formatted text files for username to password("users.properties") and
 username to roles("roles.properties") mapping. The names of the properties
 files may be overriden by the usersProperties and rolesProperties options.
 The properties files are loaded during initialization using the thread context
 class loader. This means that these files can be placed into the J2EE
 deployment jar or the JBoss config directory.

 The users.properties file uses a format:
 username1=password1
 username2=password2
 ...

 to define all valid usernames and their corresponding passwords.

 The roles.properties file uses a format:
 username1=role1,role2,...
 username1.RoleGroup1=role3,role4,...
 username2=role1,role3,...

 to define the sets of roles for valid usernames. The "username.XXX" form of
 property name is used to assign the username roles to a particular named
 group of roles where the XXX portion of the property name is the group name.
 The "username=..." form is an abbreviation for "username.Roles=...".
 The following are therefore equivalent:
 jduke=TheDuke,AnimatedCharacter
 jduke.Roles=TheDuke,AnimatedCharacter

 @author <a href="edward.kenworthy@crispgroup.co.uk">Edward Kenworthy</a>
 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public class UsersRolesLoginModule extends UsernamePasswordLoginModule
{
   // see AbstractServerLoginModule
   private static final String USER_PROPERTIES = "usersProperties";
   private static final String DEFAULT_USER_PROPERTIES = "defaultUsersProperties";
   private static final String ROLES_PROPERTIES = "rolesProperties";
   private static final String DEFAULT_ROLES_PROPERTIES = "defaultRolesProperties";
   private static final String ROLE_GROUP_SEPERATOR = "roleGroupSeperator";
	   
   private static final String[] ALL_VALID_OPTIONS =
   {
	   USER_PROPERTIES,DEFAULT_USER_PROPERTIES,
	   ROLES_PROPERTIES, DEFAULT_ROLES_PROPERTIES,
	   ROLE_GROUP_SEPERATOR
   };
   
	/** The name of the default properties resource containing user/passwords */
   private String defaultUsersRsrcName = "defaultUsers.properties";
   /** The name of the default properties resource containing user/roles */
   private String defaultRolesRsrcName = "defaultRoles.properties";
   /** The name of the properties resource containing user/passwords */
   private String usersRsrcName = "users.properties";
   /** The name of the properties resource containing user/roles */
   private String rolesRsrcName = "roles.properties";
   /** The users.properties mappings */
   private Properties users;
   /** The roles.properties mappings */
   private Properties roles;
   /** The character used to seperate the role group name from the username
    * e.g., '.' in jduke.CallerPrincipal=...
    */
   private char roleGroupSeperator = '.';

   /** Initialize this LoginModule.
    *@param options - the login module option map. Supported options include:
    usersProperties: The name of the properties resource containing
    user/passwords. The default is "users.properties"

    rolesProperties: The name of the properties resource containing user/roles
    The default is "roles.properties".

    roleGroupSeperator: The character used to seperate the role group name from
      the username e.g., '.' in jduke.CallerPrincipal=... . The default = '.'.
    defaultUsersProperties=string: The name of the properties resource containing
      the username to password mappings that will be used as the defaults
      Properties passed to the usersProperties Properties. This defaults to
      defaultUsers.properties. 
  
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
      try
      {
         // Check for usersProperties & rolesProperties
         String option = (String) options.get(USER_PROPERTIES);
         if (option != null)
            usersRsrcName = StringPropertyReplacer.replaceProperties(option);
         option = (String) options.get(DEFAULT_USER_PROPERTIES);
         if (option != null)
            defaultUsersRsrcName = StringPropertyReplacer.replaceProperties(option);
         option = (String) options.get(ROLES_PROPERTIES);
         if (option != null)
            rolesRsrcName = StringPropertyReplacer.replaceProperties(option);
         option = (String) options.get(DEFAULT_ROLES_PROPERTIES);
         if (option != null)
            defaultRolesRsrcName = StringPropertyReplacer.replaceProperties(option);
         option = (String) options.get(ROLE_GROUP_SEPERATOR);
         if( option != null )
            roleGroupSeperator = option.charAt(0);
         // Load the properties file that contains the list of users and passwords
         users = createUsers(options);
         roles = createRoles(options);
      }
      catch (Exception e)
      {
         /* Note that although this exception isn't passed on, users or roles
            will be null so that any call to login will throw a LoginException.
         */
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(e);
      }
   }

   /** Method to authenticate a Subject (phase 1). This validates that the
    *users and roles properties files were loaded and then calls
    *super.login to perform the validation of the password.
    *@exception LoginException thrown if the users or roles properties files
    *were not found or the super.login method fails.
    */
   public boolean login() throws LoginException
   {
      if (users == null)
         throw PicketBoxMessages.MESSAGES.missingPropertiesFile(usersRsrcName);
      if (roles == null)
          throw PicketBoxMessages.MESSAGES.missingPropertiesFile(rolesRsrcName);

       return super.login();
   }

   /** Create the set of roles the user belongs to by parsing the roles.properties
    data for username=role1,role2,... and username.XXX=role1,role2,...
    patterns.
    @return Group[] containing the sets of roles 
    */
   protected Group[] getRoleSets() throws LoginException
   {
      String targetUser = getUsername();
      Group[] roleSets = Util.getRoleSets(targetUser, roles, roleGroupSeperator, this);
      return roleSets;
   }

   protected String getUsersPassword()
   {
      String username = getUsername();
      String password = null;
      if (username != null)
         password = users.getProperty(username, null);
      return password;
   }

// utility methods

   /**
    * Loads the users Properties from the defaultUsersRsrcName and usersRsrcName
    * resource settings.
    * 
    * @throws IOException - thrown on failure to load the properties file.
    */ 
   protected void loadUsers() throws IOException
   {
      users = Util.loadProperties(defaultUsersRsrcName, usersRsrcName);
   }
   /**
    * A hook to allow subclasses to create the users Properties map. This
    * implementation simply calls loadUsers() and returns the users ivar.
    * Subclasses can override to obtain the users Properties map in a different
    * way.
    * 
    * @param options - the login module options passed to initialize
    * @return Properties map used for the username/password mapping.
    * @throws IOException - thrown on failure to load the properties
    */ 
   protected Properties createUsers(Map<String,?> options) throws IOException
   {
      loadUsers();
      return this.users;
   }

   /**
    * Loads the roles Properties from the defaultRolesRsrcName and rolesRsrcName
    * resource settings.
    * 
    * @throws IOException - thrown on failure to load the properties file.
    */ 
   protected void loadRoles() throws IOException
   {
      roles = Util.loadProperties(defaultRolesRsrcName, rolesRsrcName);
   }
   /**
    * A hook to allow subclasses to create the roles Properties map. This
    * implementation simply calls loadRoles() and returns the roles ivar.
    * Subclasses can override to obtain the roles Properties map in a different
    * way.
    * 
    * @param options - the login module options passed to initialize
    * @return Properties map used for the username/roles mapping.
    * @throws IOException - thrown on failure to load the properties
    */ 
   protected Properties createRoles(Map<String,?> options) throws IOException
   {
      loadRoles();
      return this.roles;
   }

   /** Parse the comma delimited roles names given by value and add them to
    * group. The type of Principal created for each name is determined by
    * the createIdentity method.
    *
    * @see #createIdentity(String)
    * 
    * @param group - the Group to add the roles to.
    * @param roles - the comma delimited role names.
    */ 
   protected void parseGroupMembers(Group group, String roles)
   {
      Util.parseGroupMembers(group, roles, this);
   }

}