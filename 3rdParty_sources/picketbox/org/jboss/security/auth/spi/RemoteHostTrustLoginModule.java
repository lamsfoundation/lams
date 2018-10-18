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
package org.jboss.security.auth.spi;

import java.security.acl.Group;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.plugins.HostThreadLocal;


/**
 * Trust specific hosts so that when usernames are passed in and the host that 
 * passes them in is "trusted" it returns true regardless of any password or 
 * credentials.  Must be used in conjunction with the RemoteHostValve.
 *
 * trustedHosts - comma del list of hosts (ips) that are trusted to have
 * already authenticated the user
 * roles - list of roles (comma del) that the user is given as a result of 
 * this login module
 * 
 * @author Andrew C. Oliver acoliver@gmail.com
 * @version $Revision: 0 $
 */
@SuppressWarnings("rawtypes")
public class RemoteHostTrustLoginModule extends UsernamePasswordLoginModule
{
    // see AbstractServerLoginModule
   private final static String OPTION_TRUSTED_HOSTS = "trustedHosts";
   private final static String OPTION_ROLES = "roles";
   private static final String[] ALL_VALID_OPTIONS =
   {
	   OPTION_TRUSTED_HOSTS,OPTION_ROLES
   };
   
   List<String> trustedHosts;
   private String roleNames;
   
   /**
    * Initialize this LoginModule.
    * 
    * @param options -
    * trustedHosts: a comma delimited list of trusted hosts allowed to pass principals without credentials and be "trusted"
    * roles: automatically granted to any users authenticated
    */
   @SuppressWarnings("unchecked")
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map sharedState, Map options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      String tmp = (String)options.get(OPTION_TRUSTED_HOSTS);
      trustedHosts = Arrays.asList(parseHosts(tmp));
      roleNames = (String) options.get(OPTION_ROLES);
   }

   private String[] parseHosts(String commaDel) {
       return commaDel.split("\\,");
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword)
   {
      String host = getRealHost();
      PicketBoxLogger.LOGGER.debugRealHostForTrust(host);

      return trustedHosts.contains(host);
   }


   /** 
    * bogus password
    * @return the valid password String
    */
   protected String getUsersPassword() throws LoginException
   {
      return "trustme";
   }

   /**
    * @return the hostname of the client
    */
   protected String getRealHost() {
      return HostThreadLocal.get();
   }

   protected Group[] getRoleSets() throws LoginException
   {
      SimpleGroup roles = new SimpleGroup("Roles");
      Group[] roleSets = {roles};
      if( roleNames != null )
      {
         String[] tokens = roleNames.split(",");
         for ( String token:tokens )
         {
            String roleName = token != null ? token.trim() : token;
            roles.addMember(new SimplePrincipal(roleName));
         }
      }
      return roleSets;
   }

}
