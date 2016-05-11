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

package org.lamsfoundation.lams.security;

import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.ILdapService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Validates user password against an entry in a LDAP database.
 */
public class LDAPAuthenticator {

    private static Logger log = Logger.getLogger(LDAPAuthenticator.class);

    private static IUserManagementService userManagementService;
    private static ILdapService ldapService;

    private static final String INITIAL_CONTEXT_FACTORY_VALUE = "com.sun.jndi.ldap.LdapCtxFactory";

    private Attributes attrs = null;

    public LDAPAuthenticator(IUserManagementService userManagementService) {
	if (LDAPAuthenticator.userManagementService == null) {
	    LDAPAuthenticator.userManagementService = userManagementService;
	}
	if (LDAPAuthenticator.ldapService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    LDAPAuthenticator.ldapService = (ILdapService) ctx.getBean("ldapService");
	}
    }

    public boolean authenticate(String userName, String credential) {
	Properties env = new Properties();

	// setup initial connection to search for user's dn
	env.setProperty(Context.INITIAL_CONTEXT_FACTORY, LDAPAuthenticator.INITIAL_CONTEXT_FACTORY_VALUE);
	env.setProperty(Context.SECURITY_AUTHENTICATION,
		Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
	env.setProperty(Context.PROVIDER_URL, Configuration.get(ConfigurationKeys.LDAP_PROVIDER_URL));

	String securityProtocol = Configuration.get(ConfigurationKeys.LDAP_SECURITY_PROTOCOL);
	if (StringUtils.equals("ssl", securityProtocol)) {
	    env.setProperty(Context.SECURITY_PROTOCOL, securityProtocol);
	}

	// setup initial bind user credentials if configured
	if (StringUtils.isNotBlank(Configuration.get(ConfigurationKeys.LDAP_BIND_USER_DN))) {
	    env.setProperty(Context.SECURITY_PRINCIPAL, Configuration.get(ConfigurationKeys.LDAP_BIND_USER_DN));
	    env.setProperty(Context.SECURITY_CREDENTIALS, Configuration.get(ConfigurationKeys.LDAP_BIND_USER_PASSWORD));
	}

	String login = "";
	String dn = "";
	boolean isValid = false;
	InitialLdapContext ctx = null;

	try {
	    ctx = new InitialLdapContext(env, null);

	    // set search to subtree of base dn
	    SearchControls ctrl = new SearchControls();
	    ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

	    // search for the user's cn
	    String filter = Configuration.get(ConfigurationKeys.LDAP_SEARCH_FILTER);
	    String baseDN = Configuration.get(ConfigurationKeys.LDAP_BASE_DN);
	    Object[] filterArgs = { userName };
	    NamingEnumeration<SearchResult> results = ctx.search(baseDN, filter, filterArgs, ctrl);
	    while (results.hasMore()) {
		SearchResult result = results.next();
		if (LDAPAuthenticator.log.isDebugEnabled()) {
		    LDAPAuthenticator.log.debug("Found matching object. Name: " + result.getName() + ". Namespace: "
			    + result.getNameInNamespace());
		}
		Attributes attrs = result.getAttributes();
		Attribute attr = attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR));
		login = LDAPAuthenticator.ldapService.getSingleAttributeString(attr);
		if (attr != null) {
		    Object attrValue = attr.get();
		    if (attrValue != null) {
			login = attrValue.toString();
		    }
		}
		if (StringUtils.equals(login, userName)) {
		    // now we can try to authenticate
		    dn = result.getNameInNamespace();
		    this.attrs = attrs;
		    ctx.close();
		    break;
		}
	    }
	    if (StringUtils.isBlank(login)) {
		LDAPAuthenticator.log.error("No LDAP user found with name: " + userName
			+ ". This could mean that the the login attribute is incorrect,"
			+ " the user does not exist, or that an initial bind user is required.");
		return false;
	    }

	    // authenticate
	    env.setProperty(Context.SECURITY_PRINCIPAL, dn);
	    env.setProperty(Context.SECURITY_CREDENTIALS, credential.toString());
	    ctx = new InitialLdapContext(env, null);

	    // if no exception, success
	    LDAPAuthenticator.log.debug("LDAP context created using DN: " + dn);
	    isValid = true;

	    // start checking whether we need to update user depending on its
	    // attributes
	    if (LDAPAuthenticator.log.isDebugEnabled()) {
		NamingEnumeration<? extends Attribute> enumAttrs = this.attrs.getAll();
		while (enumAttrs.hasMoreElements()) {
		    LDAPAuthenticator.log.debug(enumAttrs.next());
		}
	    }

	    // check user is disabled in ldap
	    if (LDAPAuthenticator.ldapService.getDisabledBoolean(this.attrs)) {
		LDAPAuthenticator.log.info("User " + userName + "is disabled in LDAP.");
		User user = LDAPAuthenticator.userManagementService.getUserByLogin(userName);
		if (user != null) {
		    LDAPAuthenticator.userManagementService.disableUser(user.getUserId());
		}
		return false;
	    }

	    if (Configuration.getAsBoolean(ConfigurationKeys.LDAP_UPDATE_ON_LOGIN)) {
		User user = LDAPAuthenticator.userManagementService.getUserByLogin(userName);
		if (user != null) {
		    // update user's attributes and org membership
		    LDAPAuthenticator.ldapService.updateLDAPUser(user, this.attrs);
		    LDAPAuthenticator.ldapService.addLDAPUser(this.attrs, user.getUserId());
		}
	    }

	    return true;
	} catch (AuthenticationNotSupportedException e) {
	    LDAPAuthenticator.log.error("Authentication mechanism not supported. Check your "
		    + ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION + " parameter: "
		    + Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
	} catch (AuthenticationException e) {
	    LDAPAuthenticator.log.info("Incorrect username (" + dn + ") or password. " + e.getMessage());
	} catch (Exception e) {
	    LDAPAuthenticator.log.error("LDAP exception", e);
	} finally {
	    try {
		if (ctx != null) {
		    ctx.close();
		}
	    } catch (Exception e) {
		LDAPAuthenticator.log.error("Exception when closing context.", e);
	    }
	}

	return isValid;
    }

    public Attributes getAttrs() {
	return attrs;
    }
}