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

import java.security.acl.Group;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;

/** A login module that obtains its security information directly from its
 login module options. The name of the login module comes from the use of
 the login-config.xml descriptor which allows the user/roles content to be
 embedded directly in the login module configuration. The following
 login-config.xml fragment illustrates an example:

 <?xml version="1.0" encoding="UTF-8"?>
 <policy xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://www.jboss.org/j2ee/schema/jaas"
    targetNamespace="http://www.jboss.org/j2ee/schema/jaas"
    >
...
    <application-policy name="test-xml-config">
       <authentication>
          <login-module code="org.jboss.security.auth.spi.XMLLoginModule" flag="required">
             <module-option name="userInfo">
                <lm:users xmlns:lm="http://jboss.org/schemas/XMLLoginModule">
                   <lm:user name="jduke" password="theduke">
                      <lm:role name="TheDuke"/>
                      <lm:role name="AnimatedCharacter"/>
                   </lm:user>
                   <lm:user name="javaduke" password="anotherduke">
                      <lm:role name="TheDuke2"/>
                      <lm:role name="AnimatedCharacter2"/>
                      <lm:role name="Java Duke" group="CallerPrincipal" />
                   </lm:user>
                </lm:users>
             </module-option>
             <module-option name="unauthenticatedIdentity">guest</module-option>
          </login-module>
       </authentication>
    </application-policy>
 </policy>

 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public class XMLLoginModule extends UsernamePasswordLoginModule
{
   // see AbstractServerLoginModule
   private static final String USER_INFO = "userInfo";
	   
   private static final String[] ALL_VALID_OPTIONS =
   {
	   USER_INFO
   };
   
   /** The name of the properties resource containing user/passwords */
   private Users users;

   /** Initialize this LoginModule.
    *@param options - the login module option map. Supported options include:
    *userInfo: The name of the properties resource containing
    user/passwords. The default is "users.properties"
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      try
      {
         users = (Users) options.get(USER_INFO);
      }
      catch (Exception e)
      {
         // Note that although this exception isn't passed on, users or roles will be null
         // so that any call to login will throw a LoginException.
         PicketBoxLogger.LOGGER.errorLoadingUserRolesPropertiesFiles(e);
      }
   }

   /** Method to authenticate a Subject (phase 1). This validates that the
    *users and roles properties files were loaded and then calls
    *super.login to perform the validation of the password.
    *@exception javax.security.auth.login.LoginException thrown if the users or roles properties files
    *were not found or the super.login method fails.
    */
   public boolean login() throws LoginException
   {
      if (users == null)
         throw PicketBoxMessages.MESSAGES.missingXMLUserRolesMapping();

      return super.login();
   }

   /** Obtain the various groups of roles for the user
    @return Group[] containing the sets of roles 
    */
   protected Group[] getRoleSets() throws LoginException
   {
      String targetUser = getUsername();
      Users.User user = users.getUser(targetUser);
      Group[] roleSets = {};
      if( user != null )
         roleSets = user.getRoleSets();
      
      return roleSets;
   }

   protected String getUsersPassword()
   {
      String username = getUsername();
      Users.User user = users.getUser(username);
      String password = null;
      if (user != null)
      {
         password = user.getPassword();
      }

      return password;
   }

}
