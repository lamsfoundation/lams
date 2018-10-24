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
package org.jboss.security.auth.spi;

import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.Util;

/**
 * A login module to authenticate users using a LDAP server.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 * @author Andy Oliver
 * @author Scott.Stark@jboss.org
 */
public class LdapUsersLoginModule extends UsernamePasswordLoginModule
{
   // see AbstractServerLoginModule
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER_OPT = "baseFilter";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   private static final String DISTINGUISHED_NAME_ATTRIBUTE_OPT = "distinguishedNameAttribute";
   private static final String PARSE_USERNAME = "parseUsername";
   private static final String USERNAME_BEGIN_STRING = "usernameBeginString";
   private static final String USERNAME_END_STRING = "usernameEndString";
   private static final String ALLOW_EMPTY_PASSWORDS = "allowEmptyPasswords";
   private static final String[] ALL_VALID_OPTIONS =
   {
	   BIND_DN,BIND_CREDENTIAL,BASE_CTX_DN,BASE_FILTER_OPT,
	   SEARCH_TIME_LIMIT_OPT,SEARCH_SCOPE_OPT,
	   DISTINGUISHED_NAME_ATTRIBUTE_OPT,
	   PARSE_USERNAME,USERNAME_BEGIN_STRING,USERNAME_END_STRING,ALLOW_EMPTY_PASSWORDS,
	   
	   Context.INITIAL_CONTEXT_FACTORY,Context.SECURITY_AUTHENTICATION,Context.SECURITY_PROTOCOL,
	   Context.PROVIDER_URL,Context.SECURITY_PRINCIPAL,Context.SECURITY_CREDENTIALS
   };
   
   protected String bindDN;

   protected String bindCredential;

   protected String baseDN;

   protected String baseFilter;

   protected int searchTimeLimit = 10000;

   protected int searchScope = SearchControls.SUBTREE_SCOPE; 
   
   protected String distinguishedNameAttribute;
   
   protected boolean parseUsername;
   
   protected String usernameBeginString;
   
   protected String usernameEndString;
   
   protected boolean allowEmptyPasswords;

   @Override
   protected String getUsersPassword() throws LoginException
   {
      return "";
   }

   @Override
   protected Group[] getRoleSets() throws LoginException
   {
      return new Group[0];
   }
   
   @Override
   protected String getUsername()
   {
      String username = super.getUsername();
      if (parseUsername)
      {
         int beginIndex = 0;
         if (usernameBeginString != null && !usernameBeginString.equals(""))
            beginIndex = username.indexOf(usernameBeginString) + usernameBeginString.length();
         if (beginIndex == -1) // not allowed. reset
            beginIndex = 0;
         int endIndex = username.length();
         if (usernameEndString != null && !usernameEndString.equals(""))
            endIndex = username.substring(beginIndex).indexOf(usernameEndString);
         if (endIndex == -1) // not allowed. reset
            endIndex = username.length();
         else
            endIndex += beginIndex;
         username = username.substring(beginIndex, endIndex);
      }
      return username;
   }

   @Override
   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
         Map<String, ?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
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
      baseDN = (String) options.get(BASE_CTX_DN);
      baseFilter = (String) options.get(BASE_FILTER_OPT);
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

      distinguishedNameAttribute = (String) options.get(DISTINGUISHED_NAME_ATTRIBUTE_OPT);
      if (distinguishedNameAttribute == null)
          distinguishedNameAttribute = "distinguishedName";
      allowEmptyPasswords = Boolean.valueOf((String) options.get(ALLOW_EMPTY_PASSWORDS));
      
      parseUsername = Boolean.valueOf((String) options.get(PARSE_USERNAME));
      if (parseUsername)
      {
         usernameBeginString = (String) options.get(USERNAME_BEGIN_STRING);
         usernameEndString = (String) options.get(USERNAME_END_STRING);
      }
   }

   @Override
   protected boolean validatePassword(String inputPassword, String expectedPassword)
   {
      boolean isValid = false;
      if (inputPassword != null)
      {
         // See if this is an empty password that should be disallowed
         if (inputPassword.length() == 0)
         {
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
            isValid = createLdapInitContext(username, inputPassword);
         }
         catch (Throwable e)
         {
            super.setValidateError(e);
         }
      }
      return isValid;
   }
   
   /**
    * Bind to the LDAP server for authentication
    */
   private boolean createLdapInitContext(String username, Object credential) throws Exception
   {
      // Get the admin context for searching
      InitialLdapContext ctx = null;
      ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
      try
      {
         if (currentTCCL != null)
            SecurityActions.setContextClassLoader(null);
         ctx = constructInitialLdapContext(bindDN, bindCredential);
         // Validate the user by binding against the userDN
         bindDNAuthentication(ctx, username, credential, baseDN, baseFilter);
      }
      catch(Exception e)
      {
    	  throw e;
      }
	  finally
      {
         if (ctx != null)
            ctx.close();
         if (currentTCCL != null)
            SecurityActions.setContextClassLoader(currentTCCL);
      }
      return true;
   }
   
   @SuppressWarnings("rawtypes")
   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException
   {
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
   
   protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN,
         String filter) throws NamingException
   {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(searchScope);
      constraints.setTimeLimit(searchTimeLimit);
      String attrList[] = {distinguishedNameAttribute};
      constraints.setReturningAttributes(attrList);

      NamingEnumeration<SearchResult> results = null;

      Object[] filterArgs = {user};
      results = ctx.search(baseDN, filter, filterArgs, constraints);
      if (!results.hasMore())
      {
         results.close();
         throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
      }

      SearchResult sr = results.next();
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
      if (userDN == null)
      {
         if (sr.isRelative())
            userDN = name + ("".equals(baseDN) ? "" : "," + baseDN);
         else
            throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
      }

      results.close();
      results = null;
      // Bind as the user dn to authenticate the user
      InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
      userCtx.close();

      return userDN;
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
