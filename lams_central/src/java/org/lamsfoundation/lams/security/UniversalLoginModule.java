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

/**
 * UniversalLoginModule is LAMS's own implementation of login module based on
 * JBoss 3.0.*, 3.2.* and possibly higher versions.
 * 
 * It's named "universal" as currently it supports WebAuth, LDAP and database
 * based authentication mechanisms.
 * 
 */

import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethodType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class UniversalLoginModule extends UsernamePasswordLoginModule {

    private static Logger log = Logger.getLogger(UniversalLoginModule.class);

    public UniversalLoginModule() {
    }

    protected String dsJndiName;

    protected String rolesQuery;

    protected String principalsQuery;

    private IThemeService themeService;
    private UserManagementService service;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
	super.initialize(subject, callbackHandler, sharedState, options);
	dsJndiName = (String) options.get("dsJndiName");
	principalsQuery = (String) options.get("principalsQuery");
	rolesQuery = (String) options.get("rolesQuery");
    }

    @Override
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
	boolean isValid = false;
	if (inputPassword != null) {
	    // empty password not allowed
	    if (inputPassword.length() == 0) {
		return false;
	    }

	    try {
		String username = getUsername();
		UniversalLoginModule.log.debug("===> authenticating user: " + username);

		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SessionManager
			.getSession().getServletContext());

		if (service == null) {
		    service = (UserManagementService) ctx.getBean("userManagementService");
		}
		User user = service.getUserByLogin(username);

		if (themeService == null) {
		    themeService = (IThemeService) ctx.getBean("themeService");
		}

		// LDAP user provisioning
		if (user == null) {
		    // provision a new user by checking ldap server
		    if (Configuration.getAsBoolean(ConfigurationKeys.LDAP_PROVISIONING_ENABLED)) {
			LdapService ldapService;
			try {
			    ldapService = (LdapService) ctx.getBean("ldapService");
			} catch (NoSuchBeanDefinitionException e) {
			    // LDEV-1937
			    UniversalLoginModule.log
				    .error("NoSuchBeanDefinitionException while getting ldapService bean, will try another method...",
					    e);
			    ApplicationContext context = new ClassPathXmlApplicationContext(
				    "org/lamsfoundation/lams/usermanagement/ldapContext.xml");
			    ldapService = (LdapService) context.getBean("ldapService");
			}
			UniversalLoginModule.log
				.debug("===> LDAP provisioning is enabled, checking username against LDAP server...");
			LDAPAuthenticator ldap = new LDAPAuthenticator();
			isValid = ldap.authenticate(username, inputPassword);
			if (isValid) { // create a new user
			    UniversalLoginModule.log.info("===> Creating new user for LDAP username: " + username);
			    if (ldapService.createLDAPUser(ldap.getAttrs())) {
				user = service.getUserByLogin(username);
				if (!ldapService.addLDAPUser(ldap.getAttrs(), user.getUserId())) {
				    UniversalLoginModule.log.error("===> Couldn't add LDAP user: " + username
					    + " to organisation.");
				}
			    } else {
				UniversalLoginModule.log.error("===> Couldn't create new user for LDAP username: "
					+ username);
				return false;
			    }
			} else { // didn't authenticate successfully with
				 // ldap
			    return false;
			}
		    } else {
			return false;
		    }
		}

		// allow sysadmin to login as another user; in this case, the
		// LAMS shared session
		// will be present, allowing the following check to work
		if (service.isUserSysAdmin()) {
		    isValid = true;
		}

		// perform password checking according to user's authentication
		// method
		if (!isValid) {
		    String type = user.getAuthenticationMethod().getAuthenticationMethodType().getDescription();
		    UniversalLoginModule.log.debug("===> authentication type: " + type);
		    if (AuthenticationMethodType.LDAP.equals(type)) {
			LDAPAuthenticator authenticator = new LDAPAuthenticator();
			isValid = authenticator.authenticate(username, inputPassword);
			// if ldap user profile has updated, udpate user object
			// for dto below
			user = service.getUserByLogin(username);
		    } else if (AuthenticationMethodType.LAMS.equals(type)) {
			DatabaseAuthenticator authenticator = new DatabaseAuthenticator(dsJndiName, principalsQuery);
			// if the password is not encrypted when sent from the
			// jsp (e.g. when it is passed
			// unencrypted to say, ldap) then encrypt it here when
			// authenticating against local db
			if (!Configuration.getAsBoolean(ConfigurationKeys.LDAP_ENCRYPT_PASSWORD_FROM_BROWSER)) {
			    // try the passed in password first,
			    // LoginRequestServlet always passes in encrypted
			    // passwords
			    isValid = authenticator.authenticate(username, inputPassword);
			    if (!isValid) {
				inputPassword = HashUtil.sha1(inputPassword);
			    }
			    isValid = authenticator.authenticate(username, inputPassword);
			} else {
			    isValid = authenticator.authenticate(username, inputPassword);
			}
		    } else if (AuthenticationMethodType.WEB_AUTH.equals(type)) {
			WebAuthAuthenticator authenticator = new WebAuthAuthenticator();
			isValid = authenticator.authenticate(username, inputPassword);
		    } else {
			UniversalLoginModule.log.error("===> Unexpected authentication type: " + type);
			return false;
		    }
		}

		// disabled users can't login;
		// check after authentication to give non-db authentication
		// methods
		// a chance to update disabled flag
		if (user.getDisabledFlag()) {
		    UniversalLoginModule.log.debug("===> user is disabled.");
		    return false;
		}

		// if login is valid, register userDTO into session.
		if (isValid) {
		    UserDTO userDTO = user.getUserDTO();

		    // If the user's css theme has been deleted, use the default
		    // as fallback
		    CSSThemeBriefDTO userCSSTheme = userDTO.getHtmlTheme();
		    if (userCSSTheme != null) {
			boolean themeExists = false;
			for (Theme theme : themeService.getAllCSSThemes()) {
			    if (userCSSTheme.getId().equals(theme.getThemeId())) {
				themeExists = true;
				break;
			    }
			}

			if (!themeExists) {
			    userDTO.setHtmlTheme(new CSSThemeBriefDTO(themeService.getDefaultCSSTheme()));
			}
		    }

		    // If the user's flash theme has been deleted, use the
		    // default as fallback
		    CSSThemeBriefDTO userFlashTheme = userDTO.getFlashTheme();
		    if (userFlashTheme != null) {
			boolean themeExists = false;
			for (Theme theme : themeService.getAllFlashThemes()) {
			    if (userFlashTheme.getId().equals(theme.getThemeId())) {
				themeExists = true;
				break;
			    }
			}

			if (!themeExists) {
			    userDTO.setFlashTheme(new CSSThemeBriefDTO(themeService.getDefaultFlashTheme()));
			}
		    }
		}
	    } catch (Exception e) {
		UniversalLoginModule.log.error("Error while validating password", e);
	    }
	}
	return isValid;
    }

    /**
     * According to Lams's security policy, all the authorization must be done locally, in other word, through Lams
     * database or other "local"(logically) data resource.
     * 
     * @return Group[] containing the sets of roles
     */
    @Override
    protected Group[] getRoleSets() throws LoginException {
	String username = getUsername();
	Connection conn = null;
	HashMap setsMap = new HashMap();
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    InitialContext ctx = new InitialContext();
	    DataSource ds = (DataSource) ctx.lookup(this.dsJndiName);

	    // log.debug("===> getRoleSets() called: " + dsJndiName + ": " +
	    // rolesQuery);
	    conn = ds.getConnection();
	    // Get the user role names
	    ps = conn.prepareStatement(this.rolesQuery);
	    try {
		ps.setString(1, username);
	    } catch (ArrayIndexOutOfBoundsException ignore) {
		// The query may not have any parameters so just try it
	    }
	    rs = ps.executeQuery();
	    if (rs.next() == false) {
		if (getUnauthenticatedIdentity() == null) {
		    throw new FailedLoginException("No matching username found in Roles");
		}
		/*
		 * We are running with an unauthenticatedIdentity so create an
		 * empty Roles set and return.
		 */
		Group[] roleSets = { new SimpleGroup("Roles") };
		return roleSets;
	    }

	    ArrayList<String> groupMembers = new ArrayList<String>();
	    do {
		String name = rs.getString(1);
		String groupName = rs.getString(2);
		if ((groupName == null) || (groupName.length() == 0)) {
		    groupName = "Roles";
		}
		Group group = (Group) setsMap.get(groupName);
		if (group == null) {
		    group = new SimpleGroup(groupName);
		    setsMap.put(groupName, group);
		}

		try {
		    Principal p;
		    // Assign minimal role if user has none
		    if (name == null) {
			name = Role.LEARNER;
			UniversalLoginModule.log.info("===> Found no roles");
		    }
		    p = super.createIdentity(name);
		    if (!groupMembers.contains(name)) {
			UniversalLoginModule.log.info("===> Assign user to role " + p.getName());
			group.addMember(p);
			groupMembers.add(name);
		    }
		    if (name.equals(Role.SYSADMIN)) {
			p = super.createIdentity(Role.AUTHOR);
			UniversalLoginModule.log.info("===> Found " + name);
			if (!groupMembers.contains(Role.AUTHOR)) {
			    UniversalLoginModule.log.info("===> Assign user to role " + Role.AUTHOR);
			    group.addMember(p);
			    groupMembers.add(Role.AUTHOR);
			}
		    }
		} catch (Exception e) {
		    UniversalLoginModule.log.debug("===> Failed to create principal: " + name, e);
		}
	    } while (rs.next());
	} catch (NamingException ex) {
	    throw new LoginException(ex.toString(true));
	} catch (SQLException ex) {
	    super.log.error("SQL failure", ex);
	    throw new LoginException(ex.toString());
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		}
	    }
	    if (ps != null) {
		try {
		    ps.close();
		} catch (SQLException e) {
		}
	    }
	    if (conn != null) {
		try {
		    conn.close();
		} catch (Exception ex) {
		}
	    }
	}

	Group[] roleSets = new Group[setsMap.size()];
	setsMap.values().toArray(roleSets);
	return roleSets;
    }

    /**
     * Overriden to return an empty password string as typically one cannot obtain a user's password. We also override
     * the validatePassword so this is ok.
     * 
     * @return and empty password String
     */
    @Override
    protected String getUsersPassword() throws LoginException {
	return "";
    }

}