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
package org.picketbox.plugins.authorization;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.AuthorizationModule;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.RoleGroup;

/**
 * <p>
 * Simple Authorization Module that authorizes users with
 * the configured roles
 * <b>Note:</b>The roles need to be placed as a comma separated list of values.
 * </p>
 * <p>
 * Example:
 * </p>
 * <p>
 * &lt;policy xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="urn:jboss:security-config:5.0"
         xmlns="urn:jboss:security-config:5.0"
         xmlns:jbxb="urn:jboss:security-config:5.0"&gt;<br/>
     &lt;application-policy name = "test"&gt; <br/>
         &lt;authentication&gt;<br/>
            &lt;login-module code = "org.jboss.security.auth.spi.UsersRolesLoginModule"
               flag = "required"&gt;<br/> 
               &lt;module-option name = "name"&gt;1.1&lt;/module-option&gt;<br/>
               &lt;module-option name = "succeed"&gt;true&lt;/module-option&gt;<br/>
               &lt;module-option name = "throwEx"&gt;false&lt;/module-option&gt; <br/>
            &lt;/login-module&gt; <br/>
         &lt;/authentication&gt; <br/>
         &lt;authorization&gt;<br/>
           &lt;policy-module <br/>
              code="org.picketbox.plugins.authorization.PicketBoxAuthorizationModule"&gt;<br/>
              &lt;module-option name="roles"&gt;validuser&lt;/module-option&gt;<br/>
            &lt;/policy-module&gt;<br/>
         &lt;/authorization&gt;<br/>
      &lt;/application-policy&gt;  <br/>
   &lt;/policy&gt; <br/>
</p>
 * <a href="mailto:anil.saldhana@redhat.com>Anil Saldhana</a>
 */
public class PicketBoxAuthorizationModule implements AuthorizationModule
{
   private Set<String> rolesSet = new HashSet<String>();
   private Subject subject = null; 
   
   public boolean abort() throws AuthorizationException
   {  
      return true;
   }

   public int authorize(Resource resource)
   { 
      Set<Principal> principals = subject.getPrincipals();
      for(Principal p: principals)
      {
         if(p instanceof Group)
         {
            Group group = (Group) p;
            if(group.getName().equalsIgnoreCase("Roles"))
            {
               Enumeration<? extends Principal> roles = group.members();
               while(roles.hasMoreElements())
               {
                  Principal role = roles.nextElement();
                  if(rolesSet.contains(role.getName()))
                     return AuthorizationContext.PERMIT;
               } 
            }
         }
      }
      return AuthorizationContext.DENY;
   }

   public boolean commit() throws AuthorizationException
   {
      return true;
   }

   public boolean destroy()
   {
      return true;
   }


   /**
    * Initialize the module
    * 
    * @param subject the authenticated subject
    * @param handler CallbackHandler
    * @param sharedState state shared with other configured modules 
    * @param options options specified in the Configuration 
    *                for this particular module
    * @param roles Roles of the subject               
    */
   public void initialize(Subject subject, CallbackHandler handler,
         Map<String,Object> sharedState, Map<String,Object> options, RoleGroup roles)
   {
      String configuredRoles = (String) options.get("roles");
      getRoles(configuredRoles);
      this.subject = subject; 
   }
   
   /**
    * Get the role names from the comma separated list of role names
    * @param commaStr
    */
   private void getRoles(String commaStr)
   {
      StringTokenizer st = new StringTokenizer(commaStr, ",");
      while(st.hasMoreTokens())
      {
         this.rolesSet.add(st.nextToken());
      }      
   }
}