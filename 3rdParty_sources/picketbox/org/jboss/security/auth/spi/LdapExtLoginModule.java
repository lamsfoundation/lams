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

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.CompositeName;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimpleGroup;
import org.jboss.security.Util;
import org.jboss.security.vault.SecurityVaultUtil;

/**
 The org.jboss.security.auth.spi.LdapExtLoginModule, added in jboss-4.0.3, is an
 alternate ldap login module implementation that uses searches for locating both
 the user to bind as for authentication as well as the associated roles. The
 roles query will recursively follow distinguished names (DNs) to navigate a
 hierarchical role structure.

 The LoginModule options include whatever options your LDAP JNDI provider
 supports. Examples of standard property names are:

 * Context.INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial"
 * Context.SECURITY_PROTOCOL = "java.naming.security.protocol"
 * Context.PROVIDER_URL = "java.naming.provider.url"
 * Context.SECURITY_AUTHENTICATION = "java.naming.security.authentication"

 The authentication happens in 2 steps:
 # An initial bind to the ldap server is done using the __bindDN__ and
 __bindCredential__ options. The __bindDN__ is some user with the ability to
 search both the __baseDN__ and __rolesCtxDN__ trees for the user and roles. The
 user DN to authenticate against is queried using the filter specified by the
 __baseFilter__ attribute (see the __baseFilter__ option description for its
 syntax).
 # The resulting user DN is then authenticated by binding to ldap server using
 the user DN as the InitialLdapContext environment Context.SECURITY_PRINCIPAL.

 The Context.SECURITY_CREDENTIALS property is either set to the String password
 obtained by the callback handler.

 If this is successful, the associated user roles are queried using the
 __rolesCtxDN__, __roleAttributeID__, __roleAttributeIsDN__,
 __roleNameAttributeID__, and __roleFilter__ options.

 The full odule properties include:
 * __baseCtxDN__ : The fixed DN of the context to start the user search from.
 * __bindDN__ : The DN used to bind against the ldap server for the user and
 roles queries. This is some DN with read/search permissions on the baseCtxDN and
 rolesCtxDN values.
 * __bindCredential__ : The password for the bindDN. This can be encrypted if the
 jaasSecurityDomain is specified.
 * __jaasSecurityDomain__ : The JMX ObjectName of the JaasSecurityDomain to use
 to decrypt the java.naming.security.principal. The encrypted form of the
 password is that returned by the JaasSecurityDomain#encrypt64(byte[]) method.
 The org.jboss.security.plugins.PBEUtils can also be used to generate the
 encrypted form.
 * __baseFilter__ : A search filter used to locate the context of the user to
 authenticate. The input username/userDN as obtained from the login module
 callback will be substituted into the filter anywhere a "{0}" expression is
 seen. This substituion behavior comes from the standard
 __DirContext.search(Name, String, Object[], SearchControls cons)__ method. An
 common example search filter is "(uid={0})".
 * __rolesCtxDN__ : The fixed DN of the context to search for user roles.
 Consider that this is not the Distinguished Name of where the actual roles are;
 rather, this is the DN of where the objects containing the user roles are (e.g.
 for active directory, this is the DN where the user account is)
 * __roleFilter__ : A search filter used to locate the roles associated with the
 authenticated user. The input username/userDN as obtained from the login module
 callback will be substituted into the filter anywhere a "{0}" expression is
 seen. The authenticated userDN will be substituted into the filter anywhere a
 "{1}" is seen.  An example search filter that matches on the input username is:
 "(member={0})". An alternative that matches on the authenticated userDN is:
 "(member={1})".
 * __roleAttributeIsDN__ : A flag indicating whether the user's role attribute
 contains the fully distinguished name of a role object, or the users's role
 attribute contains the role name. If false, the role name is taken from the
 value of the user's role attribute. If true, the role attribute represents the
 distinguished name of a role object.  The role name is taken from the value of
 the roleNameAttributeId` attribute of the corresponding object.  In certain
 directory schemas (e.g., Microsoft Active Directory), role (group)attributes in
 the user object are stored as DNs to role objects instead of as simple names, in
 which case, this property should be set to true. The default value of this
 property is false.
 * __roleNameAttributeID__ : The name of the attribute of the role object which
 corresponds to the name of the role.  If the __roleAttributeIsDN__ property is
 set to true, this property is used to find the role object's name attribute. If
 the __roleAttributeIsDN__ property is set to false, this property is ignored.
 * __roleRecursion__ : How deep the role search will go below a given matching
 context. Disable with 0, which is the default.
 * __searchTimeLimit__ : The timeout in milliseconds for the user/role searches.
 Defaults to 10000 (10 seconds).
 * __searchScope__ : Sets the search scope to one of the strings. The default is
 SUBTREE_SCOPE.
 ** OBJECT_SCOPE : only search the named roles context.
 ** ONELEVEL_SCOPE : search directly under the named roles context.
 ** SUBTREE_SCOPE :  If the roles context is not a DirContext, search only the
 object. If the roles context is a DirContext, search the subtree rooted at the
 named object, including the named object itself
 * __allowEmptyPasswords__ : A flag indicating if empty(length==0) passwords
 should be passed to the ldap server. An empty password is treated as an
 anonymous login by some ldap servers and this may not be a desirable feature.
 Set this to false to reject empty passwords, true to have the ldap server
 validate the empty password. The default is true.

 @author Andy Oliver
 @author Scott.Stark@jboss.org
 @version $Revision$ */
@SuppressWarnings("rawtypes")
public class LdapExtLoginModule extends UsernamePasswordLoginModule
{
   // see AbstractServerLoginModule
   private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";
   private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";
   private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";
   private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
   private static final String PARSE_ROLE_NAME_FROM_DN_OPT = "parseRoleNameFromDN";
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER_OPT = "baseFilter";
   private static final String ROLE_FILTER_OPT = "roleFilter";
   private static final String ROLE_RECURSION = "roleRecursion";
   private static final String DEFAULT_ROLE = "defaultRole";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";
   private static final String DISTINGUISHED_NAME_ATTRIBUTE_OPT = "distinguishedNameAttribute";
   private static final String PARSE_USERNAME = "parseUsername";
   private static final String USERNAME_BEGIN_STRING = "usernameBeginString";
   private static final String USERNAME_END_STRING = "usernameEndString";
   private static final String ALLOW_EMPTY_PASSWORDS = "allowEmptyPasswords";
   private static final String REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK = "referralUserAttributeIDToCheck";
   private static final String[] ALL_VALID_OPTIONS =
   {
      ROLES_CTX_DN_OPT,
      ROLE_ATTRIBUTE_ID_OPT,
      ROLE_ATTRIBUTE_IS_DN_OPT,
      ROLE_NAME_ATTRIBUTE_ID_OPT,
      PARSE_ROLE_NAME_FROM_DN_OPT,
      BIND_DN,
      BIND_CREDENTIAL,
      BASE_CTX_DN,
      BASE_FILTER_OPT,
      ROLE_FILTER_OPT,
      ROLE_RECURSION,
      DEFAULT_ROLE,
      SEARCH_TIME_LIMIT_OPT,
      SEARCH_SCOPE_OPT,
      SECURITY_DOMAIN_OPT,
      DISTINGUISHED_NAME_ATTRIBUTE_OPT,
      PARSE_USERNAME,
      USERNAME_BEGIN_STRING,
      USERNAME_END_STRING,
      ALLOW_EMPTY_PASSWORDS,
      REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK,

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

   protected String bindDN;

   protected String bindCredential;

   protected String baseDN;

   protected String baseFilter;

   protected String rolesCtxDN;

   protected String roleFilter;

   protected String roleAttributeID;

   protected String roleNameAttributeID;

   protected boolean roleAttributeIsDN;

   protected boolean parseRoleNameFromDN;

   protected int recursion = 0;

   protected int searchTimeLimit = 10000;

   protected int searchScope = SearchControls.SUBTREE_SCOPE;

   protected String distinguishedNameAttribute;

   protected boolean parseUsername;

   protected String usernameBeginString;

   protected String usernameEndString;

   // simple flag to indicate is the validatePassword method was called
   protected boolean isPasswordValidated = false;

   protected String referralUserAttributeIDToCheck = null;

   public LdapExtLoginModule()
   {
   }

   private transient SimpleGroup userRoles = new SimpleGroup("Roles");

   @SuppressWarnings("unchecked")
   public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
   }

   /**
    Overridden to return an empty password string as typically one cannot obtain a
    user's password. We also override the validatePassword so this is ok.
    @return and empty password String
    */
   protected String getUsersPassword() throws LoginException
   {
      return "";
   }

   /**
    Overridden by subclasses to return the Groups that correspond to the to the
    role sets assigned to the user. Subclasses should create at least a Group
    named "Roles" that contains the roles assigned to the user. A second common
    group is "CallerPrincipal" that provides the application identity of the user
    rather than the security domain identity.
    @return Group[] containing the sets of roles
    */
   protected Group[] getRoleSets() throws LoginException
   {
      // SECURITY-225: check if authentication was already done in a previous login module
      // and perform role mapping
      if (!isPasswordValidated && getIdentity() != unauthenticatedIdentity)
      {
         try
         {
            String username = getUsername();
            PicketBoxLogger.LOGGER.traceBindingLDAPUsername(username);
            createLdapInitContext(username, null);
            defaultRole();
         }
         catch (Exception e)
         {
            LoginException le = new LoginException();
            le.initCause(e);
            throw le;
         }
      }

      Group[] roleSets = {userRoles};
      return roleSets;
   }

   /**
    Validate the inputPassword by creating a LDAP InitialContext with the
    SECURITY_CREDENTIALS set to the password.
    @param inputPassword the password to validate.
    @param expectedPassword ignored
    */
   protected boolean validatePassword(String inputPassword, String expectedPassword)
   {
      isPasswordValidated = true;
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
            if (!allowEmptyPasswords)
            {
               PicketBoxLogger.LOGGER.traceRejectingEmptyPassword();
               return false;
            }
         }

         try
         {
            // Validate the password by trying to create an initial context
            String username = getUsername();
            isValid = createLdapInitContext(username, inputPassword);
            defaultRole();
            isValid = true;
         }
         catch (Throwable e)
         {
            super.setValidateError(e);
         }
      }
      return isValid;
   }

   /**
    @todo move to a generic role mapping function at the base login module
    */
   private void defaultRole()
   {
       String defaultRole = (String) options.get(DEFAULT_ROLE);
       try
      {
         if (defaultRole == null || defaultRole.equals(""))
         {
            return;
         }
         Principal p = super.createIdentity(defaultRole);
         PicketBoxLogger.LOGGER.traceAssignUserToRole(defaultRole);
         userRoles.addMember(p);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(defaultRole, e);
      }
   }

   /**
    Bind to the LDAP server for authentication.

    @param username
    @param credential
    @return true if the bind for authentication succeeded
    @throws NamingException
    */
   private boolean createLdapInitContext(String username, Object credential) throws Exception
   {
      bindDN = (String) options.get(BIND_DN);
      bindCredential = (String) options.get(BIND_CREDENTIAL);
      if ((bindCredential != null) && Util.isPasswordCommand(bindCredential))
         bindCredential = new String(Util.loadPassword(bindCredential));
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

      baseDN = (String) options.get(BASE_CTX_DN);
      baseFilter = (String) options.get(BASE_FILTER_OPT);
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

      referralUserAttributeIDToCheck = (String) options.get(REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK);

      //JBAS-4619:Parse Role Name from DN
      String parseRoleNameFromDNOption = (String) options.get(PARSE_ROLE_NAME_FROM_DN_OPT);
      parseRoleNameFromDN = Boolean.valueOf(parseRoleNameFromDNOption).booleanValue();

      rolesCtxDN = (String) options.get(ROLES_CTX_DN_OPT);
      String strRecursion = (String) options.get(ROLE_RECURSION);
      try
      {
         recursion = Integer.parseInt(strRecursion);
      }
      catch (NumberFormatException e)
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
            PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty(SEARCH_TIME_LIMIT_OPT, this.searchTimeLimit);
         }
      }
      String scope = (String) options.get(SEARCH_SCOPE_OPT);
      if ("OBJECT_SCOPE".equalsIgnoreCase(scope))
         searchScope = SearchControls.OBJECT_SCOPE;
      else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope))
         searchScope = SearchControls.ONELEVEL_SCOPE;
      if ("SUBTREE_SCOPE".equalsIgnoreCase(scope))
         searchScope = SearchControls.SUBTREE_SCOPE;

      distinguishedNameAttribute = (String) options.get(DISTINGUISHED_NAME_ATTRIBUTE_OPT);
      if (distinguishedNameAttribute == null)
          distinguishedNameAttribute = "distinguishedName";

      // Get the admin context for searching
      InitialLdapContext ctx = null;
      try
      {
         ctx = constructInitialLdapContext(bindDN, bindCredential);
         // Validate the user by binding against the userDN
         String userDN = bindDNAuthentication(ctx, username, credential, baseDN, baseFilter);

         // Query for roles matching the role filter
         SearchControls constraints = new SearchControls();
         constraints.setSearchScope(searchScope);
         constraints.setTimeLimit(searchTimeLimit);
         String[] attrList;
         if (referralUserAttributeIDToCheck != null)
         {
             attrList = new String[] {roleAttributeID, referralUserAttributeIDToCheck};
         } else {
             attrList = new String[] {roleAttributeID};
         }
         constraints.setReturningAttributes(attrList);
         rolesSearch(ctx, constraints, username, userDN, recursion, 0);
      }
      catch(Exception e)
      {
    	  throw e;
      }
	  finally
      {
         if (ctx != null)
            ctx.close();
      }
      return true;
   }

   /**
    @param ctx - the context to search from
    @param user - the input username
    @param credential - the bind credential
    @param baseDN - base DN to search the ctx from
    @param filter - the search filter string
    @return the userDN string for the successful authentication
    @throws NamingException
    */
   protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN,
         String filter) throws NamingException
   {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
      constraints.setTimeLimit(searchTimeLimit);
      String attrList[] = {distinguishedNameAttribute};
      constraints.setReturningAttributes(attrList);

      NamingEnumeration results = null;

      Object[] filterArgs = {user};

      LdapContext ldapCtx = ctx;

      boolean referralsLeft = true;
      SearchResult sr = null;
      while (referralsLeft) {
         try {
            results = ldapCtx.search(baseDN, filter, filterArgs, constraints);
            while (results.hasMore()) {
               sr = (SearchResult) results.next();
               break;
            }
            referralsLeft = false;
         }
         catch (ReferralException e) {
            ldapCtx = (LdapContext) e.getReferralContext();
            if (results != null) {
               results.close();
            }
         }
      }

      if (sr == null)
      {
         results.close();
         throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
      }

      String name = sr.getName();
      String userDN = null;
      Attributes attrs = sr.getAttributes();
      if (attrs != null)
      {
          Attribute dn = attrs.get(distinguishedNameAttribute);
          if (dn != null)
          {
              userDN = (String) dn.get();
          }
      }

      results.close();
      results = null;

      if (userDN == null)
      {
          if (sr.isRelative() == true) {
              userDN = new CompositeName(name).get(0) + ("".equals(baseDN) ? "" : "," + baseDN);
              // SECURITY-225: don't need to authenticate again
              if (isPasswordValidated)
              {
                 // Bind as the user dn to authenticate the user
                 InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
                 userCtx.close();
              }
          }
          else {
             userDN = bindDNReferralAuthentication(sr.getName(), credential);
             if (userDN == null) {
                 throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
             }
          }
      }
      else {
          if (isPasswordValidated)
          {
             // Bind as the user dn to authenticate the user
             InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
             userCtx.close();
          }
      }

      return userDN;
   }

   /**
    * This method validates absoluteName and credential against referral LDAP and returns used user DN.
    *
    * <ol>
    * <li> Parses given absoluteName to URL and DN
    * <li> creates initial LDAP context of referral LDAP to validate credential
    * <li> closes the initial context
    * </ol>
    *
    * It uses all options from login module setup except of ProviderURL.
    *
    * @param absoluteName - absolute user DN
    * @param credential
    * @return used user DN for validation
    * @throws NamingException
    */
   private String bindDNReferralAuthentication(String absoluteName, Object credential)
           throws NamingException
   {
       URI uri;
       try {
           uri = new URI(absoluteName);
       }
       catch (URISyntaxException e)
       {
           throw PicketBoxMessages.MESSAGES.unableToParseReferralAbsoluteName(e, absoluteName);
       }
       String name = uri.getPath().substring(1);
       String namingProviderURL = uri.getScheme() + "://" + uri.getAuthority();

       Properties refEnv = constructLdapContextEnvironment(namingProviderURL, name, credential);

       InitialLdapContext refCtx = new InitialLdapContext(refEnv, null);
       refCtx.close();
	   return name;
   }

   /**
    @param ctx
    @param constraints
    @param user
    @param userDN
    @param recursionMax
    @param nesting
    @throws NamingException
    */
   protected void rolesSearch(LdapContext ctx, SearchControls constraints, String user, String userDN,
         int recursionMax, int nesting) throws NamingException
   {
      if (rolesCtxDN == null || roleFilter == null) {
          // no role search - initial DN nor role filter specified, so assigning no roles
          return;
      }

      LdapContext ldapCtx = ctx;

      Object[] filterArgs = {user, sanitizeDN(userDN)};
      boolean referralsExist = true;
      while (referralsExist) {
         NamingEnumeration results = ldapCtx.search(rolesCtxDN, roleFilter, filterArgs, constraints);
         try
         {
            while (results.hasMore())
            {
               SearchResult sr = (SearchResult) results.next();

               String dn;
               if (sr.isRelative()) {
                  dn = canonicalize(sr.getName());
               }
               else {
                  dn = sr.getNameInNamespace();
               }
               if (nesting == 0 && roleAttributeIsDN && roleNameAttributeID != null)
               {
                  if(parseRoleNameFromDN)
                  {
                     parseRole(dn);
                  }
                  else
                  {
                     // Check the top context for role names
                     String[] attrNames = {roleNameAttributeID};
                     Attributes result2 = null;
                     if (sr.isRelative()) {
                        result2 = ldapCtx.getAttributes(quoteDN(dn), attrNames);
                     }
                     else {
                        result2 = getAttributesFromReferralEntity(sr, user, userDN);
                     }
                     Attribute roles2 = (result2 != null ? result2.get(roleNameAttributeID) : null);
                     if( roles2 != null )
                     {
                        for(int m = 0; m < roles2.size(); m ++)
                        {
                           String roleName = (String) roles2.get(m);
                           addRole(roleName);
                        }
                     }
                  }
               }

               // Query the context for the roleDN values
               String[] attrNames = {roleAttributeID};
               Attributes result = null;
               if (sr.isRelative()) {
                  // SECURITY-891
                  result = sr.getAttributes();
                  if (result.size() == 0) {
                     result = ldapCtx.getAttributes(quoteDN(dn), attrNames);
                  }
               }
               else {
                  result = getAttributesFromReferralEntity(sr, user, userDN);
               }
               if (result != null && result.size() > 0)
               {
                  Attribute roles = result.get(roleAttributeID);
                  for (int n = 0; n < roles.size(); n++)
                  {
                     String roleName = (String) roles.get(n);
                     if(roleAttributeIsDN && parseRoleNameFromDN)
                     {
                         parseRole(roleName);
                     }
                     else if (roleAttributeIsDN)
                     {
                        // Query the roleDN location for the value of roleNameAttributeID
                        String roleDN = quoteDN(roleName);
                        String[] returnAttribute = {roleNameAttributeID};
                        try
                        {
                           Attributes result2 = null;
                           if (sr.isRelative()) {
                              result2 = ldapCtx.getAttributes(roleDN, returnAttribute);
                           }
                           else {
                              result2 = getAttributesFromReferralEntity(sr, user, userDN);
                           }

                           Attribute roles2 = (result2 != null ? result2.get(roleNameAttributeID) : null);
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
                        addRole(roleName);
                     }
                  }
               }

               if (nesting < recursionMax)
               {
                  rolesSearch(ldapCtx, constraints, user, dn, recursionMax, nesting + 1);
               }
            }
            referralsExist = false;
         }
         catch (ReferralException e) {
            ldapCtx = (LdapContext) e.getReferralContext();
         }
         finally
         {
            if (results != null)
               results.close();
         }
      } // while (referralsExist)
   }

   /**
    * Remove enclosing quotes, if any, from dn.
    * This has to be done, because some LDAPs choke on quotes in ldap search parameter.
    *
    * @param dn
    * @return
    */
   private String sanitizeDN(final String dn) {
      if (dn != null && dn.startsWith("\"") && dn.endsWith("\"")) {
         return dn.substring(1, dn.length() - 1);
      } else {
         return dn;
      }
   }

   /**
    * In case dn contains slash character, it should be enclosed in quotes.
    * If it is already quoted, nothing is done.
    *
    * @param dn
    * @return
    */
   private String quoteDN(final String dn) {
      if (dn != null && !dn.startsWith("\"") && !dn.endsWith("\"") && dn.indexOf("/") > -1) {
         return "\"" + dn + "\"";
      } else {
         return dn;
      }
   }

   /**
    * Returns Attributes from referral entity and check them if they belong to user or userDN currently in evaluation.
    * Returns null in case of user is not validated.
    *
    * @param sr SearchResult
    * @param users to check
    * @return
    * @throws NamingException
    */
   private Attributes getAttributesFromReferralEntity(SearchResult sr, String... users) throws NamingException {

      Attributes result = sr.getAttributes();
      boolean chkSuccessful = false;
      if (referralUserAttributeIDToCheck != null) {
         Attribute usersToCheck = result.get(referralUserAttributeIDToCheck);
         check:
         for (int i = 0; usersToCheck != null && i < usersToCheck.size(); i++) {
            String userDNToCheck = (String) usersToCheck.get(i);
            for (String u: users) {
               if (u.equals(userDNToCheck)) {
                  chkSuccessful = true;
                  break check;
               }
            }
         }
      }
      return (chkSuccessful ? result : null);
   }

   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException
   {
       String protocol = (String)options.get(Context.SECURITY_PROTOCOL);
       String providerURL = (String) options.get(Context.PROVIDER_URL);
       if (providerURL == null)
          providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");

       Properties env = constructLdapContextEnvironment(providerURL, dn, credential);
       return new InitialLdapContext(env, null);
   }

   private Properties constructLdapContextEnvironment(String namingProviderURL, String principalDN, Object credential) {
       Properties env = new Properties();
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

       if (namingProviderURL != null) {
           env.setProperty(Context.PROVIDER_URL, namingProviderURL);
       }

       // JBAS-3555, allow anonymous login with no bindDN and bindCredential
       if (principalDN != null)
          env.setProperty(Context.SECURITY_PRINCIPAL, principalDN);
       if (credential != null)
          env.put(Context.SECURITY_CREDENTIALS, credential);
       this.traceLDAPEnv(env);
       return env;
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

   private void parseRole(String dn)
   {
      parseRole(dn, roleNameAttributeID);
   }

   private void parseRole(String dn, String roleNameAttributeIdentifier)
   {
      StringTokenizer st = new StringTokenizer(dn, ",");
      while(st != null && st.hasMoreTokens())
      {
         String keyVal = st.nextToken();
         if(keyVal.indexOf(roleNameAttributeIdentifier) > -1)
         {
            StringTokenizer kst = new StringTokenizer(keyVal,"=");
            kst.nextToken();
            addRole(kst.nextToken());
         }
      }
   }

   protected String getUsername()
   {
      String username = super.getUsername();
      parseUsername = Boolean.valueOf((String) options.get(PARSE_USERNAME));
      if (parseUsername)
      {
         usernameBeginString = (String) options.get(USERNAME_BEGIN_STRING);
         usernameEndString = (String) options.get(USERNAME_END_STRING);
         int beginIndex = -1;
         if (usernameBeginString != null && !usernameBeginString.equals(""))
            beginIndex = username.indexOf(usernameBeginString);
         if (beginIndex == -1)
         { // not allowed. reset
            beginIndex = 0;
         }
         else
         {
             beginIndex += usernameBeginString.length();
         }

         if(usernameEndString == null || usernameEndString.equals(""))
         {
             return username.substring(beginIndex,username.length());
         }

         int endIndex = username.indexOf(usernameEndString, beginIndex);
         if (endIndex == -1)
         { // not allowed. reset
            endIndex = username.length();
         }
         username = username.substring(beginIndex, endIndex);
      }
      return username;
   }
}
