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

import java.security.Principal;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.vault.SecurityVaultUtil;

/**
 * An implementation of LoginModule that authenticates against an LDAP server
 * using JNDI, based on the configuration properties.
 * <p>
 * The LoginModule options include whatever options your LDAP JNDI provider
 * supports. Examples of standard property names are:
 * <ul>
 * <li><code>Context.INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial"</code>
 * <li><code>Context.SECURITY_PROTOCOL = "java.naming.security.protocol"</code>
 * <li><code>Context.PROVIDER_URL = "java.naming.provider.url"</code>
 * <li><code>Context.SECURITY_AUTHENTICATION = "java.naming.security.authentication"</code>
 * </ul>
 * <p>
 * The Context.SECURITY_PRINCIPAL is set to the distinguished name of the user
 * as obtained by the callback handler and the Context.SECURITY_CREDENTIALS
 * property is either set to the String password or Object credential depending
 * on the useObjectCredential option.
 * <p>
 * Additional module properties include:
 * <ul>
 * <li>principalDNPrefix, principalDNSuffix : A prefix and suffix to add to the
 * username when forming the user distinguished name. This is useful if you
 * prompt a user for a username and you don't want them to have to enter the
 * fully distinguished name. Using this property and principalDNSuffix the
 * userDN will be formed as:
 * <pre>
 *    String userDN = principalDNPrefix + username + principalDNSuffix;
 * </pre>
 * <li>useObjectCredential : indicates that the credential should be obtained as
 * an opaque Object using the <code>org.jboss.security.plugins.ObjectCallback</code> type
 * of Callback rather than as a char[] password using a JAAS PasswordCallback.
 * <li>rolesCtxDN : The fixed distinguished name to the context to search for user roles.
 * <li>userRolesCtxDNAttributeName : The name of an attribute in the user
 * object that contains the distinguished name to the context to search for
 * user roles. This differs from rolesCtxDN in that the context to search for a
 * user's roles can be unique for each user.
 * <li>uidAttributeID : The name of the attribute that in the object containing
 * the user roles that corresponds to the userid. This is used to locate the
 * user roles.
 * <li>matchOnUserDN : A flag indicating if the search for user roles should match
 * on the user's fully distinguished name. If false just the username is used
 * as the match value. If true, the userDN is used as the match value.
 * <li>allowEmptyPasswords : A flag indicating if empty(length==0) passwords
 * should be passed to the LDAP server. An empty password is treated as an
 * anonymous login by some LDAP servers and this may not be a desirable
 * feature. Set this to false to reject empty passwords, true to have the ldap
 * server validate the empty password. The default is true.
 *
 * <li>roleAttributeIsDN : A flag indicating whether the user's role attribute
 * contains the fully distinguished name of a role object, or the users's role
 * attribute contains the role name. If false, the role name is taken from the
 * value of the user's role attribute. If true, the role attribute represents
 * the distinguished name of a role object.  The role name is taken from the
 * value of the `roleNameAttributeId` attribute of the corresponding object.  In
 * certain directory schemas (e.g., Microsoft Active Directory), role (group)
 * attributes in the user object are stored as DNs to role objects instead of
 * as simple names, in which case, this property should be set to true.
 * The default value of this property is false.
 * <li>roleNameAttributeID : The name of the attribute of the role object which
 * corresponds to the name of the role.  If the `roleAttributeIsDN` property is
 * set to true, this property is used to find the role object's name attribute.
 * If the `roleAttributeIsDN` property is set to false, this property is ignored.
 * <li>java.naming.security.principal (4.0.3+): This standard JNDI property if
 * specified in the login configuration, it is used to rebind to the ldap server
 * after user authentication for the role searches. This may be necessary if the
 * user does not have permission to perform these queres. If specified, the
 * java.naming.security.credentials provides the rebind credentials.
 * </li>
 * <li>java.naming.security.credentials (4.0.3+): This standard JNDI property
 * if specified in the login configuration, it is used to rebind to the LDAP
 * server after user authentication for the role searches along with the
 * java.naming.security.principal value. This can be encrypted using the
 * jaasSecurityDomain.
 * <li>jaasSecurityDomain (4.0.3+): The JMX ObjectName of the JaasSecurityDomain
 * to use to decrypt the java.naming.security.principal. The encrypted form
 * of the password is that returned by the JaasSecurityDomain#encrypt64(byte[])
 * method. The org.jboss.security.plugins.PBEUtils can also be used to generate
 * the encrypted form.
 * </ul>
 * A sample login config:
 * <p>
 <pre>
 testLdap {
 org.jboss.security.auth.spi.LdapLoginModule required
 java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory
 java.naming.provider.url="ldap://ldaphost.jboss.org:1389/"
 java.naming.security.authentication=simple
 principalDNPrefix=uid=
 uidAttributeID=userid
 roleAttributeID=roleName
 principalDNSuffix=,ou=People,o=jboss.org
 rolesCtxDN=cn=JBossSX Tests,ou=Roles,o=jboss.org
 };

 testLdap2 {
 org.jboss.security.auth.spi.LdapLoginModule required
 java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory
 java.naming.provider.url="ldap://ldaphost.jboss.org:1389/"
 java.naming.security.authentication=simple
 principalDNPrefix=uid=
 uidAttributeID=userid
 roleAttributeID=roleName
 principalDNSuffix=,ou=People,o=jboss.org
 userRolesCtxDNAttributeName=ou=Roles,dc=user1,dc=com
 };

 testLdapToActiveDirectory {
 org.jboss.security.auth.spi.LdapLoginModule required
 java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory
 java.naming.provider.url="ldap://ldaphost.jboss.org:1389/"
 java.naming.security.authentication=simple
 rolesCtxDN=cn=Users,dc=ldaphost,dc=jboss,dc=org
 uidAttributeID=userPrincipalName
 roleAttributeID=memberOf
 roleAttributeIsDN=true
 roleNameAttributeID=name
 };
 </pre>
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("rawtypes")
public class LdapLoginModule extends UsernamePasswordLoginModule
{
   // see AbstractServerLoginModule
   private static final String PRINCIPAL_DN_PREFIX_OPT = "principalDNPrefix";
   private static final String PRINCIPAL_DN_SUFFIX_OPT = "principalDNSuffix";
   private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";
   private static final String USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT = "userRolesCtxDNAttributeName";
   private static final String UID_ATTRIBUTE_ID_OPT = "uidAttributeID";
   private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";
   private static final String MATCH_ON_USER_DN_OPT = "matchOnUserDN";
   private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";
   private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";
   private static final String ALLOW_EMPTY_PASSWORDS = "allowEmptyPasswords";

   private static final String[] ALL_VALID_OPTIONS =
   {
      PRINCIPAL_DN_PREFIX_OPT,
      PRINCIPAL_DN_SUFFIX_OPT,
      ROLES_CTX_DN_OPT,
      USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT,
      UID_ATTRIBUTE_ID_OPT,
      ROLE_ATTRIBUTE_ID_OPT,
      MATCH_ON_USER_DN_OPT,
      ROLE_ATTRIBUTE_IS_DN_OPT,
      ROLE_NAME_ATTRIBUTE_ID_OPT,
      SEARCH_TIME_LIMIT_OPT,
      SEARCH_SCOPE_OPT,
      SECURITY_DOMAIN_OPT,
      ALLOW_EMPTY_PASSWORDS,

      Context.INITIAL_CONTEXT_FACTORY,
      Context.OBJECT_FACTORIES,
      Context.STATE_FACTORIES,
      Context.URL_PKG_PREFIXES,
      Context.PROVIDER_URL,
      Context.DNS_URL,
      Context.AUTHORITATIVE,
      Context.BATCHSIZE,
      Context.REFERRAL,
      Context.SECURITY_PROTOCOL,
      Context.SECURITY_AUTHENTICATION,
      Context.SECURITY_PRINCIPAL,
      Context.SECURITY_CREDENTIALS,
      Context.LANGUAGE,
      Context.APPLET
   };

   public LdapLoginModule()
   {
   }

   @Override
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
   }

   private transient SimpleGroup userRoles = new SimpleGroup("Roles");

   /** Overridden to return an empty password string as typically one cannot
    obtain a user's password. We also override the validatePassword so
    this is ok.
    @return and empty password String
    */
   protected String getUsersPassword() throws LoginException
   {
      return "";
   }

   /** Overridden by subclasses to return the Groups that correspond to the
    to the role sets assigned to the user. Subclasses should create at
    least a Group named "Roles" that contains the roles assigned to the user.
    A second common group is "CallerPrincipal" that provides the application
    identity of the user rather than the security domain identity.
    @return Group[] containing the sets of roles
    */
   protected Group[] getRoleSets() throws LoginException
   {
      Group[] roleSets = {userRoles};
      return roleSets;
   }

   /** Validate the inputPassword by creating a ldap InitialContext with the
    SECURITY_CREDENTIALS set to the password.

    @param inputPassword the password to validate.
    @param expectedPassword ignored
    */
   protected boolean validatePassword(String inputPassword, String expectedPassword)
   {
      boolean isValid = false;
      if (inputPassword != null)
      {
         // See if this is an empty password that should be disallowed
         if (inputPassword.length() == 0)
         {
            // Check for an allowEmptyPasswords option
            boolean allowEmptyPasswords = false;
            String flag = (String) options.get(ALLOW_EMPTY_PASSWORDS);
            if (flag != null)
               allowEmptyPasswords = Boolean.valueOf(flag).booleanValue();
            if (allowEmptyPasswords == false)
            {
               PicketBoxLogger.LOGGER.traceRejectingEmptyPassword();
               return false;
            }
         }

         try
         {
            // Validate the password by trying to create an initial context
            String username = getUsername();
            createLdapInitContext(username, inputPassword);
            isValid = true;
         }
         catch (Throwable e)
         {
            super.setValidateError(e);
         }
      }
      return isValid;
   }

   private void createLdapInitContext(String username, Object credential) throws Exception
   {
      Properties env = new Properties();
      // Map all option into the JNDI InitialLdapContext env
      Iterator iter = options.entrySet().iterator();
      while (iter.hasNext())
      {
         Entry entry = (Entry) iter.next();
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

      String bindDN = (String) options.get(Context.SECURITY_PRINCIPAL);
      String bindCredential = (String) options.get(Context.SECURITY_CREDENTIALS);
      String securityDomain = (String) options.get(SECURITY_DOMAIN_OPT);
      if (securityDomain != null)
      {
         ObjectName serviceName = new ObjectName(securityDomain);
         char[] tmp = DecodeAction.decode(bindCredential, serviceName);
         bindCredential = new String(tmp);
      }
      //Check if the credential is vaultified
      if(bindCredential != null && SecurityVaultUtil.isVaultFormat(bindCredential))
      {
    	  bindCredential = SecurityVaultUtil.getValueAsString(bindCredential);
      }

      String principalDNPrefix = (String) options.get(PRINCIPAL_DN_PREFIX_OPT);
      if (principalDNPrefix == null)
         principalDNPrefix = "";
      String principalDNSuffix = (String) options.get(PRINCIPAL_DN_SUFFIX_OPT);
      if (principalDNSuffix == null)
         principalDNSuffix = "";
      String matchType = (String) options.get(MATCH_ON_USER_DN_OPT);
      boolean matchOnUserDN = Boolean.valueOf(matchType).booleanValue();
      String userDN = principalDNPrefix + username + principalDNSuffix;
      env.setProperty(Context.PROVIDER_URL, providerURL);
      env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
      env.put(Context.SECURITY_CREDENTIALS, credential);

      this.traceLDAPEnv(env);

      InitialLdapContext ctx = null;
      ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
      try
      {
         if (currentTCCL != null)
            SecurityActions.setContextClassLoader(null);
         ctx = new InitialLdapContext(env, null);
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            PicketBoxLogger.LOGGER.traceSuccessfulLogInToLDAP(ctx.toString());
         }

         if (bindDN != null)
         {
            try {
               ctx.close();
            }
            catch (NamingException e) {
               PicketBoxLogger.LOGGER.warnProblemClosingOriginalLdapContextDuringRebind(e);
            }

            // Rebind the ctx to the bind dn/credentials for the roles searches
            PicketBoxLogger.LOGGER.traceRebindWithConfiguredPrincipal(bindDN);
            env.setProperty(Context.SECURITY_PRINCIPAL, bindDN);
            env.put(Context.SECURITY_CREDENTIALS, bindCredential);
            ctx = new InitialLdapContext(env, null);
         }

         /* If a userRolesCtxDNAttributeName was speocified, see if there is a
          user specific roles DN. If there is not, the default rolesCtxDN will
          be used.
          */
         String rolesCtxDN = (String) options.get(ROLES_CTX_DN_OPT);
         String userRolesCtxDNAttributeName = (String) options.get(USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT);
         if (userRolesCtxDNAttributeName != null)
         {
            // Query the indicated attribute for the roles ctx DN to use
            String[] returnAttribute = {userRolesCtxDNAttributeName};
            try
            {
               Attributes result = ctx.getAttributes(userDN, returnAttribute);
               if (result.get(userRolesCtxDNAttributeName) != null)
               {
                  rolesCtxDN = result.get(userRolesCtxDNAttributeName).get().toString();
                  PicketBoxLogger.LOGGER.traceFoundUserRolesContextDN(rolesCtxDN);
               }
            }
            catch (NamingException e)
            {
                PicketBoxLogger.LOGGER.debugFailureToQueryLDAPAttribute(userRolesCtxDNAttributeName, userDN, e);
            }
         }

         // Search for any roles associated with the user
         if (rolesCtxDN != null)
         {
            String uidAttrName = (String) options.get(UID_ATTRIBUTE_ID_OPT);
            if (uidAttrName == null)
               uidAttrName = "uid";
            String roleAttrName = (String) options.get(ROLE_ATTRIBUTE_ID_OPT);
            if (roleAttrName == null)
               roleAttrName = "roles";
            StringBuffer roleFilter = new StringBuffer("(");
            roleFilter.append(uidAttrName);
            roleFilter.append("={0})");
            String userToMatch = username;
            if (matchOnUserDN == true)
               userToMatch = userDN;

            String[] roleAttr = {roleAttrName};
            // Is user's role attribute a DN or the role name
            String roleAttributeIsDNOption = (String) options.get(ROLE_ATTRIBUTE_IS_DN_OPT);
            boolean roleAttributeIsDN = Boolean.valueOf(roleAttributeIsDNOption).booleanValue();

            // If user's role attribute is a DN, what is the role's name attribute
            // Default to 'name' (Group name attribute in Active Directory)
            String roleNameAttributeID = (String) options.get(ROLE_NAME_ATTRIBUTE_ID_OPT);
            if (roleNameAttributeID == null)
               roleNameAttributeID = "name";

            int searchScope = SearchControls.SUBTREE_SCOPE;
            int searchTimeLimit = 10000;
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

            NamingEnumeration answer = null;
            try
            {
               SearchControls controls = new SearchControls();
               controls.setSearchScope(searchScope);
               controls.setReturningAttributes(roleAttr);
               controls.setTimeLimit(searchTimeLimit);
               Object[] filterArgs = {userToMatch};
               if (PicketBoxLogger.LOGGER.isTraceEnabled())
               {
                  PicketBoxLogger.LOGGER.traceRolesDNSearch(rolesCtxDN, roleFilter.toString(), userToMatch,
                          Arrays.toString(roleAttr), searchScope, searchTimeLimit);
               }
               answer = ctx.search(rolesCtxDN, roleFilter.toString(), filterArgs, controls);
               while (answer.hasMore())
               {
                  SearchResult sr = (SearchResult) answer.next();
                  PicketBoxLogger.LOGGER.traceCheckSearchResult(sr.getName());

                  Attributes attrs = sr.getAttributes();
                  Attribute roles = attrs.get(roleAttrName);
                  if (roles != null)
                  {
                     for (int r = 0; r < roles.size(); r++)
                     {
                        Object value = roles.get(r);
                        String roleName = null;
                        if (roleAttributeIsDN == true)
                        {
                           // Query the roleDN location for the value of roleNameAttributeID
                           String roleDN = value.toString();
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
                                    addRole(roleName);
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
                           roleName = value.toString();
                           addRole(roleName);
                        }
                     }
                  }
                  else
                  {
                     PicketBoxLogger.LOGGER.debugFailureToFindAttrInSearchResult(roleAttrName, sr.getName());
                  }
               }
            }
            catch (NamingException e)
            {
               PicketBoxLogger.LOGGER.debugFailureToExecuteRolesDNSearch(e);
            }
            finally
            {
               if (answer != null)
                  answer.close();
            }
         }
      }
      finally
      {
         // Close the context to release the connection
         if (ctx != null)
            ctx.close();
         if (currentTCCL != null)
            SecurityActions.setContextClassLoader(currentTCCL);
      }
   }

   private void addRole(String roleName)
   {
      if (roleName != null)
      {
         try
         {
            Principal p = super.createIdentity(roleName);
            PicketBoxLogger.LOGGER.traceAssignUserToRole(roleName);
            userRoles.addMember(p);
         }
         catch (Exception e)
         {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(roleName, e);
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
      PicketBoxLogger.LOGGER.traceLDAPConnectionEnv(tmp);
   }

}
