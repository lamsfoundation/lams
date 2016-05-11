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
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	    service = (UserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }

    private LdapService getLdapService() {
	if (ldapService == null) {
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

	// setup initial connection to search for user's dn
	env.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY_VALUE);
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
	    Object[] filterArgs = { username };
	    NamingEnumeration<SearchResult> results = ctx.search(baseDN, filter, filterArgs, ctrl);
	    while (results.hasMore()) {
		SearchResult result = results.next();
		if (log.isDebugEnabled()) {
		    log.debug("===> found matching object...");
		    log.debug("name: " + result.getName());
		    log.debug("namespace name: " + result.getNameInNamespace());
		}
		Attributes attrs = result.getAttributes();
		Attribute attr = attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR));
		login = getLdapService().getSingleAttributeString(attr);
		if (attr != null) {
		    Object attrValue = attr.get();
		    if (attrValue != null) {
			login = attrValue.toString();
		    }
		}
		if (StringUtils.equals(login, username)) {
		    // now we can try to authenticate
		    dn = result.getNameInNamespace();
		    setAttrs(attrs);
		    ctx.close();
		    break;
		}
	    }
	    if (StringUtils.isBlank(login)) {
		log.error("===> No LDAP user found with username: " + username
			+ ". This could mean that the the login attribute is incorrect,"
			+ " the user doesn't exist, or that an initial bind user is required.");
		return false;
	    }

	    // authenticate
	    env.setProperty(Context.SECURITY_PRINCIPAL, dn);
	    env.setProperty(Context.SECURITY_CREDENTIALS, credential.toString());
	    ctx = new InitialLdapContext(env, null);

	    // if no exception, success
	    log.debug("===> LDAP context created using DN: " + dn);
	    isValid = true;

	    // start checking whether we need to update user depending on its
	    // attributes
	    if (log.isDebugEnabled()) {
		NamingEnumeration enumAttrs = this.attrs.getAll();
		while (enumAttrs.hasMoreElements()) {
		    log.debug(enumAttrs.next());
		}
	    }

	    // check user is disabled in ldap
	    if (getLdapService().getDisabledBoolean(this.attrs)) {
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
		    getLdapService().updateLDAPUser(user, this.attrs);
		    getLdapService().addLDAPUser(this.attrs, user.getUserId());
		}
	    }

	    return true;
	} catch (AuthenticationNotSupportedException e) {
	    log.error("===> Authentication mechanism not supported.  Check your "
		    + ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION + " parameter: "
		    + Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
	} catch (AuthenticationException e) {
	    log.info("===> Incorrect username (" + dn + ") or password. " + e.getMessage());
	} catch (Exception e) {
	    log.error("===> LDAP exception: " + e, e);
	} finally {
	    try {
		if (ctx != null) {
		    ctx.close();
		}
	    } catch (Exception e) {
		log.error("===> gettting problem when closing context. Exception: " + e);
	    }
	}

	return isValid;
    }

}