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
package org.jboss.security;

import java.util.Map;
import java.util.Set;


/**
 * The SecurityRolesAssociation uses a ThreadLocal to associate the SecurityRoleMetaData
 * from the deployment with the current thread.
 *
 * @author Thomas.Diesler@jboss.org
 * @author Anil.Saldhana@jboss.org
 * @version $Revision$
 */
public final class SecurityRolesAssociation
{
   /** Thread local that holds the deployment security roles - it holds a map
    * of principal-name versus a set of role strings
    */
   private static ThreadLocal<Map<String,Set<String>>> threadSecurityRoleMapping 
               = new ThreadLocal<Map<String,Set<String>>>();
   
   /**
    * Get the current map of SecurityRoleMetaData.
    * The map is principal name  keyed against a set of rolenames
    * @return A Map that stores SecurityRoleMetaData by roleName
    */
   public static Map<String,Set<String>> getSecurityRoles()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityRolesAssociation.class.getName() + ".getSecurityRoles"));
      }
      return (Map<String,Set<String>>) threadSecurityRoleMapping.get();
   }

   /**
    * Set the current map of SecurityRoleMetaData.
    */
   public static void setSecurityRoles(Map<String,Set<String>> securityRoles)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityRolesAssociation.class.getName() + ".setSecurityRoles"));
      }
      PicketBoxLogger.LOGGER.traceSecRolesAssociationSetSecurityRoles(securityRoles);
      if(securityRoles == null)
         threadSecurityRoleMapping.remove();
      else
         threadSecurityRoleMapping.set(securityRoles);
   }
}