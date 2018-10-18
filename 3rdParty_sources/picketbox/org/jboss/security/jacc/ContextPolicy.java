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
package org.jboss.security.jacc;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.security.jacc.PolicyContextException;

import org.jboss.security.PicketBoxLogger;

/** The permissions for a JACC context id. This implementation is based on
 * the 3.2.x model of associating the declarative roles with the Subject of
 * the authenticated caller. This allows the 3.2.x login modules to be used
 * as the source of the authentication and authorization information.
 * 
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@jboss.org
 * @version $Revison:$
 */
public class ContextPolicy
{
   private String contextID;
   private Permissions excludedPermissions = new Permissions();
   private Permissions uncheckedPermissions = new Permissions();
   /** HashMap<String, Permissions> role name to permissions mapping */
   private HashMap<String, Permissions> rolePermissions = new HashMap<String, Permissions>();

   private static final String ANY_AUTHENTICATED_USER_ROLE = "**";

   ContextPolicy(String contextID)
   {
      this.contextID = contextID;
   }

   Permissions getPermissions()
   {
      Permissions perms = new Permissions();
      Enumeration<Permission> eter = uncheckedPermissions.elements();
      while( eter.hasMoreElements() )
      {
         Permission p = (Permission) eter.nextElement();
         perms.add(p);
      }
      Iterator<Permissions> iter = rolePermissions.values().iterator();
      while( iter.hasNext() )
      {
         Permissions rp = iter.next();
         eter = rp.elements();
         while( eter.hasMoreElements() )
         {
            Permission p = eter.nextElement();
            perms.add(p);
         }
      }
      return perms;
   }

   boolean implies(ProtectionDomain domain, Permission permission)
   {
      boolean implied = false;
      // First check the excluded permissions
      if( excludedPermissions.implies(permission) )
      {
         PicketBoxLogger.LOGGER.traceImpliesMatchesExcludedSet(permission);
         return false;
      }

      // Next see if this matches an unchecked permission
      if( uncheckedPermissions.implies(permission) )
      {
         PicketBoxLogger.LOGGER.traceImpliesMatchesUncheckedSet(permission);
         return true;
      }

      // Check principal to role permissions
      Principal[] principals = domain.getPrincipals();
      int length = principals != null ? principals.length : 0;
      ArrayList<String> principalNames = new ArrayList<String>();
      for(int n = 0; n < length; n ++)
      {
         Principal p = principals[n];
         if( p instanceof Group )
         {
            Group g = (Group) p;
            Enumeration<? extends Principal> iter = g.members();
            while( iter.hasMoreElements() )
            {
               p = iter.nextElement();
               String name = p.getName();
               principalNames.add(name);
            }
         }
         else
         {
            String name = p.getName();
            principalNames.add(name);
         }
      }
      if (principalNames.size() == 0)
         PicketBoxLogger.LOGGER.traceNoPrincipalsInProtectionDomain(domain);

      principalNames.add(ANY_AUTHENTICATED_USER_ROLE);
      PicketBoxLogger.LOGGER.traceProtectionDomainPrincipals(principalNames);
      for(int n = 0; implied == false && n < principalNames.size(); n ++)
      {
         String name = principalNames.get(n);
         Permissions perms = rolePermissions.get(name);
         PicketBoxLogger.LOGGER.debugImpliesParameters(name, perms);
         if( perms == null )
            continue;
         implied = perms.implies(permission);
         PicketBoxLogger.LOGGER.debugImpliesResult(implied);
      }

      return implied;
   }

   void clear()
   {
      excludedPermissions = new Permissions();
      uncheckedPermissions = new Permissions();
      rolePermissions.clear();
   }

   void addToExcludedPolicy(Permission permission)
      throws PolicyContextException
   {
      excludedPermissions.add(permission);
   }
   
   void addToExcludedPolicy(PermissionCollection permissions)
      throws PolicyContextException
   {
      Enumeration<Permission> iter = permissions.elements();
      while( iter.hasMoreElements() )
      {
         Permission p = iter.nextElement();
         excludedPermissions.add(p);
      }
   }

   void addToRole(String roleName, Permission permission)
      throws PolicyContextException
   {
      Permissions perms = rolePermissions.get(roleName);
      if( perms == null )
      {
         perms = new Permissions();
         rolePermissions.put(roleName, perms);
      }
      perms.add(permission);
   }

   void addToRole(String roleName, PermissionCollection permissions)
      throws PolicyContextException
   {
      Permissions perms = rolePermissions.get(roleName);
      if( perms == null )
      {
         perms = new Permissions();
         rolePermissions.put(roleName, perms);
      }
      Enumeration<Permission> iter = permissions.elements();
      while( iter.hasMoreElements() )
      {
         Permission p = iter.nextElement();
         perms.add(p);
      }
   }

   void addToUncheckedPolicy(Permission permission)
      throws PolicyContextException
   {
      uncheckedPermissions.add(permission);
   }

   void addToUncheckedPolicy(PermissionCollection permissions)
      throws PolicyContextException
   {
      Enumeration<Permission> iter = permissions.elements();
      while( iter.hasMoreElements() )
      {
         Permission p = iter.nextElement();
         uncheckedPermissions.add(p);
      }
   }

   void commit()
      throws PolicyContextException
   {
   }

   void delete()
      throws PolicyContextException
   {
      clear();
   }

   String getContextID()
      throws PolicyContextException
   {
      return contextID;
   }

   void linkConfiguration(ContextPolicy link)
      throws PolicyContextException
   {
   }

   void removeExcludedPolicy()
      throws PolicyContextException
   {
      excludedPermissions = new Permissions();
   }

   void removeRole(String roleName)
      throws PolicyContextException
   {
      // JACC 1.4 spec: if "*" is used as the role name and no role by this name exists in the config, remove all roles.
      if ("*".equals(roleName) && !this.rolePermissions.containsKey("*"))
         this.rolePermissions.clear();
      else
         this.rolePermissions.remove(roleName);
   }

   void removeUncheckedPolicy()
      throws PolicyContextException
   {
      uncheckedPermissions = new Permissions();
   }
   
   Permissions getPermissionsForRole(String role)
   { 
      return this.rolePermissions.get(role);
   }

   public String toString()
   {
      StringBuffer tmp = new StringBuffer("<ContextPolicy contextID='");
      tmp.append(contextID);
      tmp.append("'>\n");
      tmp.append("\t<ExcludedPermissions>\n");
      Enumeration<Permission> iter = excludedPermissions.elements();
      while( iter.hasMoreElements() )
      {
         Permission p = iter.nextElement();
         tmp.append("<Permission type='");
         tmp.append(p.getClass());
         tmp.append("' name='");
         tmp.append(p.getName());
         tmp.append("' actions='");
         tmp.append(p.getActions());
         tmp.append("' />\n");
      }
      tmp.append("\t</ExcludedPermissions>\n");

      tmp.append("\t<UncheckedPermissions>\n");
      iter = uncheckedPermissions.elements();
      while( iter.hasMoreElements() )
      {
         Permission p = iter.nextElement();
         tmp.append("<Permission type='");
         tmp.append(p.getClass());
         tmp.append(" name='");
         tmp.append(p.getName());
         tmp.append("' actions='");
         tmp.append(p.getActions());
         tmp.append("' />\n");
      }
      tmp.append("\t</UncheckedPermissions>\n");

      tmp.append("\t<RolePermssions>\n");
      Iterator<String> roles = rolePermissions.keySet().iterator();
      while( roles.hasNext() )
      {
         String role = roles.next();
         Permissions perms = rolePermissions.get(role);
         iter = perms.elements();
         tmp.append("\t\t<Role name='"+role+"'>\n");
         while( iter.hasMoreElements() )
         {
            Permission p = iter.nextElement();
            tmp.append("<Permission type='");
            tmp.append(p.getClass());
            tmp.append(" name='");
            tmp.append(p.getName());
            tmp.append("' actions='");
            tmp.append(p.getActions());
            tmp.append("' />\n");
         }
         tmp.append("\t\t</Role>\n");
      }
      tmp.append("\t</RolePermssions>");
      tmp.append("</ContextPolicy>\n");
      return tmp.toString();
   }
}
