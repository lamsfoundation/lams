/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.security;

import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LDAPAuthenticator {
	
	private static Logger log = Logger.getLogger(LDAPAuthenticator.class);
	private static UserManagementService service;
	private static LdapService ldapService;
	private static final String INITIAL_CONTEXT_FACTORY_VALUE = "com.sun.jndi.ldap.LdapCtxFactory";
	private Attributes attrs = null;
	
	public LDAPAuthenticator() {
	}
	
	private UserManagementService getService() {
		if (service==null) {
			WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
			service = (UserManagementService) ctx.getBean("userManagementService");
		}
		return service;
	}
	
	private LdapService getLdapService() {
		if (ldapService==null) {
			WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
			ldapService = (LdapService) ctx.getBean("ldapService");
		}
		return ldapService;
	}

	public Attributes getAttrs() {
		return attrs;
	}
	
	public void setAttrs(Attributes attrs) {
		this.attrs = attrs;
	}
	
	public boolean authenticate(String username, String inputPassword) {
		return authentication(username, inputPassword);
	}

	private boolean authentication(String username, Object credential) {
		Properties env = new Properties();

		// Load all authentication method parameters into env
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY_VALUE);
		env.setProperty(Context.SECURITY_AUTHENTICATION, Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));

		String principalDNPrefix = Configuration.get(ConfigurationKeys.LDAP_PRINCIPAL_DN_PREFIX);
		String[] principalDNSuffixes = Configuration.get(ConfigurationKeys.LDAP_PRINCIPAL_DN_SUFFIX).split(";");

		env.setProperty(Context.PROVIDER_URL, Configuration.get(ConfigurationKeys.LDAP_PROVIDER_URL));
		env.put(Context.SECURITY_CREDENTIALS, credential);
		
		Object originalTrustStore = System.getProperty("javax.net.ssl.trustStore");
		Object originalTrustPass = System.getProperty("javax.net.ssl.trustStorePassword");
		
		String securityProtocol = Configuration.get(ConfigurationKeys.LDAP_SECURITY_PROTOCOL);
		if (StringUtils.equals("ssl", securityProtocol)) {
			env.setProperty(Context.SECURITY_PROTOCOL, securityProtocol);
			// FIXME: synchronization issue: dynamically load certificate into
			// system instead of overwritting it.
			System.setProperty("javax.net.ssl.trustStore", Configuration.get(ConfigurationKeys.LDAP_TRUSTSTORE_PATH));
			System.setProperty("javax.net.ssl.trustStorePassword", Configuration.get(ConfigurationKeys.LDAP_TRUSTSTORE_PASSWORD));
		}

		log.debug("===> LDAP authenticator: " + env);

		InitialLdapContext ctx = null;
		
		for (String principalDNSuffix : principalDNSuffixes) {
			if (!principalDNSuffix.startsWith(",")) {
				principalDNSuffix = "," + principalDNSuffix;
			}
			String userDN = principalDNPrefix + username + principalDNSuffix;
			env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
			try {
				ctx = new InitialLdapContext(env, null);
				log.debug("===> LDAP context created using DN: "+userDN);
				Attributes attrs = ctx.getAttributes(userDN);
				setAttrs(attrs);
				
				if (log.isDebugEnabled()) {
					NamingEnumeration enumAttrs = attrs.getAll();
					while (enumAttrs.hasMoreElements()) {
						log.debug(enumAttrs.next());
					}
				}
			
				// check user is disabled in ldap
				if (getLdapService().getDisabledBoolean(attrs)) {
					log.debug("===> User is disabled in LDAP.");
					User user = getService().getUserByLogin(username);
					if (user != null) {
						getService().disableUser(user.getUserId());
					}
					return false;
				}
			
				if (Configuration.getAsBoolean(ConfigurationKeys.LDAP_UPDATE_ON_LOGIN)) {
					User user = getService().getUserByLogin(username);
					if (user != null) {
						// update user's attributes and org membership
						getLdapService().updateLDAPUser(user, attrs);
						getLdapService().addLDAPUser(attrs, user.getUserId());
					}
				}
			
				return true;
			} catch (AuthenticationNotSupportedException e) {
				log.error("===> Authentication mechanism not supported.  Check your "
						+ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION+" parameter: "
						+Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
			} catch (AuthenticationException e) {
				log.info("===> Incorrect username ("+userDN+") or password ("+credential+"): "+e.getMessage());
			} catch (Exception e) {
				log.error("===> LDAP exception: " + e, e);
			} finally {

				try {
					// FIXME: synchronization issue -- dynamically load certificate
					// instead of overwritting system properties
					//System.setProperty("javax.net.ssl.trustStore",(String)originalTrustStore
					// );
					//System.setProperty("javax.net.ssl.trustStorePassword",(String)originalTrustPass
					// );

					if (ctx != null)
						ctx.close();
				} catch (Exception e) {
					log.error("===> gettting problem when closing context. Excetion: "+e);
				}
			}
		}
		
		return false;
	}

}