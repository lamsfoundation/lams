/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.security;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;

import org.apache.log4j.Logger;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;

public class LDAPAuthenticator {
	private static Logger log = Logger.getLogger(LDAPAuthenticator.class);

	private static final String USE_OBJECT_CREDENTIAL_OPT = "useObjectCredential";

	private static final String PRINCIPAL_DN_PREFIX_OPT = "principalDNPrefix";

	private static final String PRINCIPAL_DN_SUFFIX_OPT = "principalDNSuffix";

	private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";

	private static final String USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT = "userRolesCtxDNAttributeName";

	private static final String UID_ATTRIBUTE_ID_OPT = "uidAttributeID";

	private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";

	private static final String MATCH_ON_USER_DN_OPT = "matchOnUserDN";

	private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";

	private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";

	private AuthenticationMethod method;

	public LDAPAuthenticator(AuthenticationMethod method) {
		this.method = method;
	}

	public boolean authenticate(String username, String inputPassword) {
		return authentication(username, inputPassword);
	}

	private boolean authentication(String username, Object credential) {
		Properties env = new Properties();

		// Load all authentication method parameters into env
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, method.getParameterByName(Context.INITIAL_CONTEXT_FACTORY).getValue());
		env.setProperty(Context.SECURITY_AUTHENTICATION, method.getParameterByName(Context.SECURITY_AUTHENTICATION).getValue());
		env.setProperty(Context.SECURITY_PROTOCOL, method.getParameterByName(Context.SECURITY_PROTOCOL).getValue());

		String principalDNPrefix = method.getParameterByName(PRINCIPAL_DN_PREFIX_OPT).getValue();
		String principalDNSuffix = method.getParameterByName(PRINCIPAL_DN_SUFFIX_OPT).getValue();
		String userDN = principalDNPrefix + username + principalDNSuffix;
		env.setProperty(Context.SECURITY_PRINCIPAL, userDN);

		env.setProperty(Context.PROVIDER_URL, method.getParameterByName(Context.PROVIDER_URL).getValue());
		env.put(Context.SECURITY_CREDENTIALS, credential);

		Object originalTrustStore = System.getProperty("javax.net.ssl.trustStore");
		Object originalTrustPass = System.getProperty("javax.net.ssl.trustStorePassword");
		//FIXME: synchronization issue: dynamically load certificate into
		// system instead of overwritting it.
		System.setProperty("javax.net.ssl.trustStore", method.getParameterByName("truststore.path").getValue());
		System.setProperty("javax.net.ssl.trustStorePassword", method.getParameterByName("truststore.password").getValue());

		log.debug("===> LDAP authenticator: " + env);

		InitialLdapContext ctx = null;
		try {
			ctx = new InitialLdapContext(env, null);
			log.debug("===> ldap context created: "+ctx);
			return true;
		} catch (Exception e) {
			log.error("===> Ldap exception: " + e);
			return false;
		} finally {

			try {
				//FIXME: synchronization issue -- dynamically load certificate
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

}