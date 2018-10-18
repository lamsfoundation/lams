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
package org.jboss.security.mapping.providers.role;

import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.Util;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVaultUtil;

/**
 * A mapping provider that assigns roles to an user using a LDAP server to search for the roles. 
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 * @author Andy Oliver
 * @author Scott.Stark@jboss.org
 */
public class LdapRolesMappingProvider extends AbstractRolesMappingProvider
{
   private static final String BIND_DN = "bindDN";

   private static final String BIND_CREDENTIAL = "bindCredential";
   
   private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";

   private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";

   private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";

   private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
   
   private static final String PARSE_ROLE_NAME_FROM_DN_OPT = "parseRoleNameFromDN";
   
   private static final String ROLE_FILTER_OPT = "roleFilter";

   private static final String ROLE_RECURSION = "roleRecursion";
   
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";

   private static final String SEARCH_SCOPE_OPT = "searchScope";
   
   protected String bindDN;

   protected String bindCredential;
   
   protected String rolesCtxDN;

   protected String roleFilter;

   protected String roleAttributeID;

   protected String roleNameAttributeID;

   protected boolean roleAttributeIsDN;
   
   protected boolean parseRoleNameFromDN;

   protected int recursion = 0;

   protected int searchTimeLimit = 10000;

   protected int searchScope = SearchControls.SUBTREE_SCOPE;
   
   protected Map<String, Object> options;
   
   public void init(Map<String, Object> options)
   {
      if (options != null)
      {
         this.options = options;
         bindDN = (String) options.get(BIND_DN);
         bindCredential = (String) options.get(BIND_CREDENTIAL);
         if ((bindCredential != null) && Util.isPasswordCommand(bindCredential))
         {
            try
            {
               bindCredential = new String(Util.loadPassword(bindCredential));
            }
            catch (Exception e)
            {
               throw PicketBoxMessages.MESSAGES.failedToDecodeBindCredential(e);
            }
         }
         
         //Check if the credential is vaultified
         if(bindCredential != null && SecurityVaultUtil.isVaultFormat(bindCredential))
         {
            try {
               bindCredential = SecurityVaultUtil.getValueAsString(bindCredential);
            }
            catch (SecurityVaultException ve) 
            {
               throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.unableToGetPasswordFromVault());
            }
         }
         
         roleFilter = (String) options.get(ROLE_FILTER_OPT);
         roleAttributeID = (String) options.get(ROLE_ATTRIBUTE_ID_OPT);
         if (roleAttributeID == null)
            roleAttributeID = "role";
         // Is user's role attribute a DN or the role name
         String roleAttributeIsDNOption = (String) options.get(ROLE_ATTRIBUTE_IS_DN_OPT);
         roleAttributeIsDN = Boolean.valueOf(roleAttributeIsDNOption).booleanValue();
         roleNameAttributeID = (String) options.get(ROLE_NAME_ATTRIBUTE_ID_OPT);
         if (roleNameAttributeID == null)
            roleNameAttributeID = "name";
      
         //JBAS-4619:Parse Role Name from DN
         String parseRoleNameFromDNOption = (String) options.get(PARSE_ROLE_NAME_FROM_DN_OPT);
         parseRoleNameFromDN = Boolean.valueOf(parseRoleNameFromDNOption).booleanValue();
      
         rolesCtxDN = (String) options.get(ROLES_CTX_DN_OPT);
         String strRecursion = (String) options.get(ROLE_RECURSION);
         try
         {
            recursion = Integer.parseInt(strRecursion);
         }
         catch (Exception e)
         {
            PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty(ROLE_RECURSION, 0);
            // its okay for this to be 0 as this just disables recursion
            recursion = 0;
         }
         String timeLimit = (String) options.get(SEARCH_TIME_LIMIT_OPT);
         if (timeLimit != null)
         {
            try
            {
               searchTimeLimit = Integer.parseInt(timeLimit);
            }
            catch (NumberFormatException e)
            {
               PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty(SEARCH_TIME_LIMIT_OPT, searchTimeLimit);
            }
         }
         String scope = (String) options.get(SEARCH_SCOPE_OPT);
         if ("OBJECT_SCOPE".equalsIgnoreCase(scope))
            searchScope = SearchControls.OBJECT_SCOPE;
         else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope))
            searchScope = SearchControls.ONELEVEL_SCOPE;
         if ("SUBTREE_SCOPE".equalsIgnoreCase(scope))
            searchScope = SearchControls.SUBTREE_SCOPE;
      }
   }
 
   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject)
   {
      if (contextMap == null || contextMap.isEmpty())
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");

      //Obtain the principal to roles mapping
      Principal principal = getCallerPrincipal(contextMap);

      if (principal != null)
      {
         // Get the admin context for searching
         InitialLdapContext ctx = null;
         ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
         try
         {
            if (currentTCCL != null)
               SecurityActions.setContextClassLoader(null);
            ctx = constructInitialLdapContext(bindDN, bindCredential);
            
            // Query for roles matching the role filter
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(searchScope);
            constraints.setReturningAttributes(new String[0]);
            constraints.setTimeLimit(searchTimeLimit);
            rolesSearch(ctx, constraints, principal.getName(), recursion, 0, mappedObject);
         }
         catch (NamingException ne)
         {
            PicketBoxLogger.LOGGER.debugIgnoredException(ne);
         }
         finally
         {
            if (ctx != null)
            {
               try
               {
                  ctx.close();
               }
               catch (NamingException ne)
               {
                   PicketBoxLogger.LOGGER.debugIgnoredException(ne);
               }
            }
            if (currentTCCL != null)
               SecurityActions.setContextClassLoader(currentTCCL);
         }
      }
   }
   
   protected InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException
   {
      Properties env = new Properties();
      Iterator<Entry<String, Object>> iter = options.entrySet().iterator();
      while (iter.hasNext())
      {
         Entry<String, Object> entry = iter.next();
         env.put(entry.getKey(), entry.getValue());
      }

      // Set defaults for key values if they are missing
      String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
      if (factoryName == null)
      {
         factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
         env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
      }
      String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
      if (authType == null)
         env.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
      String protocol = env.getProperty(Context.SECURITY_PROTOCOL);
      String providerURL = (String) options.get(Context.PROVIDER_URL);
      if (providerURL == null)
         providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");

      env.setProperty(Context.PROVIDER_URL, providerURL);
      // JBAS-3555, allow anonymous login with no bindDN and bindCredential
      if (dn != null)
         env.setProperty(Context.SECURITY_PRINCIPAL, dn);
      if (credential != null)
         env.put(Context.SECURITY_CREDENTIALS, credential);
      this.traceLDAPEnv(env);
      return new InitialLdapContext(env, null);
   }
   
   protected void rolesSearch(InitialLdapContext ctx, SearchControls constraints, String user, int recursionMax,
         int nesting, RoleGroup roleGroup) throws NamingException
   {
      rolesSearch(ctx, constraints, user, null, recursionMax, nesting, roleGroup);
   }

   protected void rolesSearch(InitialLdapContext ctx, SearchControls constraints, String user, String previousRoleDn,
                              int recursionMax, int nesting, RoleGroup roleGroup) throws NamingException
   {
      Object[] filterArgs = {user};
      String searchFilter = previousRoleDn == null ? roleFilter : "member=" + previousRoleDn;
      NamingEnumeration<SearchResult> results = ctx.search(rolesCtxDN, searchFilter, filterArgs, constraints);
      try
      {
         while (results.hasMore())
         {
            SearchResult sr = results.next();
            String dn = canonicalize(sr.getName());

            // Query the context for the roleDN values
            String[] attrNames = {roleAttributeID};
            Attributes result = ctx.getAttributes(dn, attrNames);
            if (result != null && result.size() > 0)
            {
               Attribute roles = result.get(roleAttributeID);
               for (int n = 0; n < roles.size(); n++)
               {
                  String roleName = (String) roles.get(n);
                  if (roleAttributeIsDN && parseRoleNameFromDN)
                  {
                     parseRole(roleName, roleGroup);
                  }
                  else if (roleAttributeIsDN)
                  {
                     // Query the roleDN location for the value of roleNameAttributeID
                     String roleDN = roleName;
                     String[] returnAttribute = {roleNameAttributeID};
                     PicketBoxLogger.LOGGER.traceFollowRoleDN(roleDN);
                     try
                     {
                        Attributes result2 = ctx.getAttributes(roleDN, returnAttribute);
                        Attribute roles2 = result2.get(roleNameAttributeID);
                        if (roles2 != null)
                        {
                           for (int m = 0; m < roles2.size(); m++)
                           {
                              roleName = (String) roles2.get(m);
                              addRole(roleName, roleGroup);
                           }
                        }
                     }
                     catch (NamingException e)
                     {
                        PicketBoxLogger.LOGGER.debugFailureToQueryLDAPAttribute(roleNameAttributeID, roleDN, e);
                     }
                  }
                  else
                  {
                     // The role attribute value is the role name
                     addRole(roleName, roleGroup);
                  }
               }
            }

            if (nesting < recursionMax)
            {
               rolesSearch(ctx, constraints, user, dn, recursionMax, nesting + 1, roleGroup);
            }
         }
      }
      finally
      {
         if (results != null)
            results.close();
      }
   }
   
   //JBAS-3438 : Handle "/" correctly
   private String canonicalize(String searchResult)
   {
      String result = searchResult;
      int len = searchResult.length();

      String appendRolesCtxDN = "" + ("".equals(rolesCtxDN) ? "" : "," + rolesCtxDN);
      if (searchResult.endsWith("\""))
      {
         result = searchResult.substring(0, len - 1) + appendRolesCtxDN + "\"";
      }
      else
      {
         result = searchResult + appendRolesCtxDN;
      }
      return result;
   }

   private void addRole(String roleName, RoleGroup roleGroup)
   {
      if (roleName != null)
      {
         try
         {
            SimpleRole role = new SimpleRole(roleName);
            PicketBoxLogger.LOGGER.traceAssignUserToRole(roleName);
            roleGroup.addRole(role);
         }
         catch (Exception e)
         {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(roleName, e);
         }
      }
   }

   private void parseRole(String dn, RoleGroup roleGroup)
   {
      StringTokenizer st = new StringTokenizer(dn, ",");
      while (st != null && st.hasMoreTokens())
      {
         String keyVal = st.nextToken();
         if (keyVal.indexOf(roleNameAttributeID) > -1)
         {
            StringTokenizer kst = new StringTokenizer(keyVal, "=");
            kst.nextToken();
            addRole(kst.nextToken(), roleGroup);
         }
      }
   }

   /**
    * <p>
    * Logs the specified LDAP env, masking security-sensitive information (passwords).
    * </p>
    *
    * @param env the LDAP env to be logged.
    */
   private void traceLDAPEnv(Properties env)
   {
      Properties tmp = new Properties();
      tmp.putAll(env);
      if (tmp.containsKey(Context.SECURITY_CREDENTIALS))
         tmp.setProperty(Context.SECURITY_CREDENTIALS, "******");
      if (tmp.containsKey(BIND_CREDENTIAL))
         tmp.setProperty(BIND_CREDENTIAL, "******");
      PicketBoxLogger.LOGGER.traceLDAPConnectionEnv(tmp);
   }

}
