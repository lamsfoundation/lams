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
package org.jboss.security.auth.callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;

/**
 * <p>
 * A {@code CallbackHandler} using the LDAP to match the passed password.
 * </p>
 * <p>
 * There are two callbacks that can be passed to this handler.
 * <ol>
 * <li>{@code PasswordCallback}:  Passing this callback will get the password for the user.
 *                                The returned password will not be in clear text. It will
 *                                be in the hashed form the ldap server has stored.
 * </li>
 * <li>{@code VerifyPasswordCallback} Passing this callback with a value will make the handler
 *                                to do a ldap bind to verify the user password.
 * </li>
 * </ol>
 * </p>
 * <p>
 * The main method is {@code #setConfiguration(Map)} which takes in a map of String key/value pairs.
 * The possible pairs are:
 * <ol>
 * <li>passwordAttributeID  :  what is the name of the attribute where the password is stored. Default: userPassword</li>
 * <li>bindDN   :  DN used to bind against the ldap server with read/write permissions for baseCtxDN.</li>
 * <li>bindCredential : Password for the bindDN. This can be encrypted if the jaasSecurityDomain is specified.</li>
 * <li>baseCtxDN : The fixed DN of the context to start the user search from.</li>
 * <li>baseFilter: A search filter used to locate the context of the user to authenticate.
 *                 The input username/userDN as provided by the {@code NameCallback}
 *                 will be substituted into the filter anywhere a "{0}" expression is seen.
 *                 This substitution behavior comes from the standard.</li>
 * <li>searchTimeLimit : The timeout in milliseconds for the user/role searches. Defaults to 10000 (10 seconds).</li>
 * <li>jaasSecurityDomain : The JMX ObjectName of the JaasSecurityDomain to use to decrypt the java.naming.security.principal.
 *                          The encrypted form of the password is that returned by the JaasSecurityDomain#encrypt64(byte[]) method.
 *                          The org.jboss.security.plugins.PBEUtils can also be used to generate the encrypted form.</li>
 * <li>distinguishedNameAttribute : Used in ldap servers such as Active Directory where the ldap provider has a property (distinguishedName)
 *                                  to return the relative CN of the user. Default: distinguishedName</li>
 * </ol>
 * </p>
 * <p>
 * Example Usages:
 * <pre>
 *  LdapCallbackHandler cbh = new LdapCallbackHandler();
 *  Map<String,String> map = new HashMap<String,String>();
 *  map.put("bindDN", "cn=Directory Manager");
 *  map.put("bindCredential", "password");
 *  map.put("baseFilter", "(uid={0})");
 *  map.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
 *  map.put("java.naming.provider.url", "ldap://localhost:10389");
 *  map.put("baseCtxDN", "ou=People,dc=jboss,dc=org");
 *  cbh.setConfiguration(map);
 *  NameCallback ncb = new NameCallback("Enter");
 *  ncb.setName("jduke");
 *  VerifyPasswordCallback vpc = new VerifyPasswordCallback();
 *  vpc.setValue("theduke");
 *  cbh.handle(new Callback[] {ncb,vpc} );
 *  assertTrue(vpc.isVerified());
 * </pre>
 * </p>
 * @author Anil Saldhana
 * @since Nov 1, 2011
 */
public class LdapCallbackHandler extends AbstractCallbackHandler implements CallbackHandler
{
	private static final String PASSWORD_ATTRIBUTE_ID = "passwordAttributeID";

	private static final String BIND_DN = "bindDN";

	private static final String BIND_CREDENTIAL = "bindCredential";

	private static final String BASE_CTX_DN = "baseCtxDN";

	private static final String BASE_FILTER_OPT = "baseFilter";

	private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";

	private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";

	private static final String DISTINGUISHED_NAME_ATTRIBUTE_OPT = "distinguishedNameAttribute";

	protected String bindDN;

	protected String bindCredential;

	protected String passwordAttributeID = "userPassword";

	protected int searchTimeLimit = 10000;

	protected String distinguishedNameAttribute;

	// simple flag to indicate is the validatePassword method was called
	protected boolean isPasswordValidated = false;

	protected Map<String,String> options = new HashMap<String, String>();

	public LdapCallbackHandler()
	{
	}

	public void setConfiguration(Map<String,String> config)
	{
		if(config != null)
		{
			options.putAll(config);
		}
	}

	public void handle(Callback[] callbacks) throws IOException,
	UnsupportedCallbackException
	{
		if(userName == null)
		{
			userName = getUserName(callbacks);
		}
		for (int i = 0; i < callbacks.length; i++)
		{
			Callback callback = callbacks[i];
			try
			{
				this.handleCallBack( callback );
			}
			catch (NamingException e)
			{
				throw new IOException(e);
			}
		}
	}

	/**
	 * Handle a {@code Callback}
	 * @param c callback
	 * @throws UnsupportedCallbackException If the callback is not supported by this handler
	 * @throws NamingException
	 */
	protected void handleCallBack( Callback c ) throws UnsupportedCallbackException, NamingException
	{
		if(c instanceof VerifyPasswordCallback)
		{
			verifyPassword((VerifyPasswordCallback) c);
			return;
		}

		if(c instanceof PasswordCallback == false)
			return;

		PasswordCallback passwdCallback = (PasswordCallback) c;

		String bindDN = getBindDN();

		String bindCredential = getBindCredential();

		String tmp = options.get(PASSWORD_ATTRIBUTE_ID);
		if(tmp != null && tmp.length() > 0)
		{
			passwordAttributeID = tmp;
		}

		InitialLdapContext ctx = null;
		ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
		try
		{
			if (currentTCCL != null)
				SecurityActions.setContextClassLoader(null);
			ctx = this.constructInitialLdapContext(bindDN, bindCredential);
		}
		catch (NamingException e)
		{
			safeClose(ctx);
			if (currentTCCL != null)
				SecurityActions.setContextClassLoader(currentTCCL);

			throw new RuntimeException(e);
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
			}
		}
		if(searchTimeLimit == 0)
			searchTimeLimit = 10000;

		String baseDN = options.get(BASE_CTX_DN);
		String baseFilter = options.get(BASE_FILTER_OPT);

		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

		constraints.setTimeLimit(searchTimeLimit);


		NamingEnumeration<SearchResult> results = null;

		Object[] filterArgs = {userName};
		try
		{
			if(baseDN == null)
				throw PicketBoxMessages.MESSAGES.invalidNullBaseContextDN();
			results = ctx.search(baseDN, baseFilter, filterArgs, constraints);
			if (results.hasMore() == false)
			{
				safeClose(results);
				throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
			}
			SearchResult sr = results.next();
			String name = sr.getName();
			String userDN = null;
			if (sr.isRelative() == true)
				userDN = name + "," + baseDN;
			else
				throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);;

			safeClose(results);

			//Finished Authentication.  Lets look for the attributes
			filterArgs = new Object[]{userName, userDN};
			results = ctx.search(userDN, baseFilter, filterArgs, constraints);
			while (results.hasMore())
			{
				sr = results.next();
				Attributes attributes = sr.getAttributes();
				NamingEnumeration<? extends javax.naming.directory.Attribute> ne = attributes.getAll();

				while(ne != null && ne.hasMoreElements())
				{
					javax.naming.directory.Attribute ldapAtt = ne.next();
					if(passwordAttributeID.equalsIgnoreCase(ldapAtt.getID()))
					{
						Object thePass = ldapAtt.get();
						setPasswordCallbackValue(thePass, passwdCallback);
					}
				}
			}
		}
		catch(NamingException ne)
		{
			PicketBoxLogger.LOGGER.error(ne);
		}
		finally
		{
			safeClose(results);
			safeClose(ctx);
			if (currentTCCL != null)
				SecurityActions.setContextClassLoader(currentTCCL);
		}
	}

	protected void verifyPassword( VerifyPasswordCallback vpc) throws NamingException
	{
		String credential = vpc.getValue();

		ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
		if (currentTCCL != null)
			SecurityActions.setContextClassLoader(null);

		String  baseDN = options.get(BASE_CTX_DN);
		String  baseFilter = options.get(BASE_FILTER_OPT);

		InitialLdapContext ctx= this.constructInitialLdapContext(bindDN, bindCredential);
		bindDNAuthentication(ctx, userName, credential, baseDN, baseFilter);
		vpc.setVerified(true);
	}

	protected String getBindDN()
	{
		String bindDN = options.get(BIND_DN);
		if(bindDN == null || bindDN.length() == 0)
		{
			PicketBoxLogger.LOGGER.traceBindDNNotFound();
		}
		return bindDN;
	}

	protected String getBindCredential()
	{
		String bindCredential = options.get(BIND_CREDENTIAL);
		if (org.jboss.security.Util.isPasswordCommand(bindCredential))
		{
			try
			{
				bindCredential = new String(org.jboss.security.Util.loadPassword(bindCredential));
			}
			catch (Exception e1)
			{
				PicketBoxLogger.LOGGER.errorDecryptingBindCredential(e1);
			}
		}
		String securityDomain = options.get(SECURITY_DOMAIN_OPT);
		if (securityDomain != null)
		{
			try
			{
				ObjectName serviceName = new ObjectName(securityDomain);
				char[] tmp = DecodeAction.decode(bindCredential, serviceName);
				bindCredential = new String(tmp);
			}
			catch (Exception e)
			{
				PicketBoxLogger.LOGGER.errorDecryptingBindCredential(e);
			}
		}
		return bindCredential;
	}

	protected void setPasswordCallbackValue(Object thePass, PasswordCallback passwdCallback)
	{
		String tmp;
		if(thePass instanceof String)
		{
		    tmp = (String) thePass;
			passwdCallback.setPassword(tmp.toCharArray());
		}
		else if(thePass instanceof char[])
		{
			passwdCallback.setPassword((char[])thePass);
		}
		else if(thePass instanceof byte[])
		{
			byte[] theBytes = (byte[]) thePass;
			passwdCallback.setPassword((new String(theBytes).toCharArray()));
		}
		else
		{
			throw PicketBoxMessages.MESSAGES.invalidPasswordType(thePass != null ? thePass.getClass() : null);
		}
	}

	private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException
	{
		Properties env = new Properties();
		for (Entry<String, String> entry : options.entrySet())
		{
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
		String providerURL = options.get(Context.PROVIDER_URL);
		if (providerURL == null)
			providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");

		env.setProperty(Context.PROVIDER_URL, providerURL);

		distinguishedNameAttribute = options.get(DISTINGUISHED_NAME_ATTRIBUTE_OPT);
	      if (distinguishedNameAttribute == null)
	          distinguishedNameAttribute = "distinguishedName";


		// JBAS-3555, allow anonymous login with no bindDN and bindCredential
		if (dn != null)
			env.setProperty(Context.SECURITY_PRINCIPAL, dn);
		if (credential != null)
			env.put(Context.SECURITY_CREDENTIALS, credential);
        this.traceLDAPEnv(env);
        return new InitialLdapContext(env, null);
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
   @SuppressWarnings("rawtypes")
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
      results = ctx.search(baseDN, filter, filterArgs, constraints);
      if (results.hasMore() == false)
      {
         results.close();
         throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
      }

      SearchResult sr = (SearchResult) results.next();
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
          if (sr.isRelative() == true)
              userDN = name + ("".equals(baseDN) ? "" : "," + baseDN);
          else
              throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
      }

      safeClose(results);
      results = null;

      InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
      safeClose(userCtx);

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

	@SuppressWarnings("rawtypes")
	protected void safeClose(NamingEnumeration results)
	{
		if(results != null)
		{
			try
			{
				results.close();
			} catch (NamingException e) {}
		}
	}

	protected void safeClose(InitialLdapContext ic)
	{
		if(ic != null)
		{
			try
			{
				ic.close();
			}
			catch (NamingException e)
			{
			}
		}
	}
}