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

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dto.ThemeDTO;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethodType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Covers all LAMS authentication.
 */
public class UniversalLoginModule implements LoginModule {
    private static Logger log = Logger.getLogger(UniversalLoginModule.class);

    private Subject subject;
    private CallbackHandler callbackHandler;
    /**
     * Flag indicating if the login 1st phase succeeded.
     */
    private boolean loginOK;
    private Principal identity;
    private char[] credential;

    private static String dsJndiName;
    private static final Map<String, Long> internalAuthenticationTokens = new TreeMap<>();

    private static DatabaseAuthenticator databaseAuthenticator;
    private static IThemeService themeService;
    private static IUserManagementService userManagementService;

    private static final long INTERNAL_AUTHENTICATION_TIMEOUT = 60 * 1000;
    private static final String ROLES_QUERY = "SELECT DISTINCT r.name,'Roles' FROM lams_user u "
	    + "LEFT OUTER JOIN lams_user_organisation uo USING(user_id) "
	    + "LEFT OUTER JOIN lams_user_organisation_role urr USING(user_organisation_id) "
	    + "LEFT OUTER JOIN lams_role r USING (role_id) " + "WHERE u.login=?";

    /**
     * Method to commit the authentication process (phase 2).
     */
    @Override
    public boolean commit() throws LoginException {
	if (loginOK == false) {
	    return false;
	}

	/*
	 * If the login method completed successfully as indicated by
	 * loginOK == true, this method adds the identity value to the subject's principals set. It also adds the
	 * members of
	 * each Group returned by getRoleSets() to the subject's principals Set.
	 */
	Set<Principal> principals = subject.getPrincipals();
	principals.add(identity);
	for (Group group : getRoleSets()) {
	    String name = group.getName();
	    Group subjectGroup = createGroup(name, principals);
	    // Copy the group members to the Subject group
	    Enumeration<? extends Principal> members = group.members();
	    while (members.hasMoreElements()) {
		Principal role = members.nextElement();
		subjectGroup.addMember(role);
	    }
	}

	UniversalLoginModule.log.info("User logged in: " + getUserName());
	return true;
    }

    /**
     * Method to abort the authentication process (phase 2).
     *
     * @return true alaways
     */
    @Override
    public boolean abort() throws LoginException {
	UniversalLoginModule.log.info("Abort log in for user: " + getUserName());
	return true;
    }

    /**
     * Remove the user identity and roles added to the Subject during commit.
     *
     * @return true always.
     */
    @Override
    public boolean logout() throws LoginException {
	UniversalLoginModule.log.info("User logged out: " + getUserName());
	// Remove the user identity
	Set<Principal> principals = subject.getPrincipals();
	principals.remove(identity);
	return true;
    }

    /**
     * Find or create a Group with the given name. Subclasses should use this method to locate the 'Roles' group or
     * create additional types of groups.
     *
     * @return A named Group from the principals set.
     */
    private Group createGroup(String name, Set<Principal> principals) {
	Group roles = null;
	for (Principal principal : principals) {
	    if (principal instanceof Group) {
		Group grp = (Group) principal;
		if (grp.getName().equals(name)) {
		    roles = grp;
		    break;
		}
	    }
	}

	// If we did not find a group create one
	if (roles == null) {
	    roles = new SimpleGroup(name);
	    principals.add(roles);
	}
	return roles;
    }

    /**
     * Perform the authentication of the username and password.
     */
    @Override
    public boolean login() throws LoginException {
	loginOK = false;
	String[] info = getUsernameAndPassword();
	String userName = info[0];
	String password = info[1];

	UniversalLoginModule.log.info("Authenticate user: " + userName);

	if (identity == null) {
	    try {
		identity = new SimplePrincipal(userName);
	    } catch (Exception e) {
		throw new LoginException("Failed to create principal: " + e.getMessage());
	    }

	    if (!validatePassword(password)) {
		if (UniversalLoginModule.log.isDebugEnabled()) {
		    UniversalLoginModule.log.debug("Bad password for user: " + userName);
		}
		throw new FailedLoginException("Incorrect password");
	    }
	}

	loginOK = true;
	if (UniversalLoginModule.log.isDebugEnabled()) {
	    UniversalLoginModule.log.debug("User authenticated: " + identity);
	}
	return true;
    }

    /**
     * Return user name from existing identity.
     */
    private String getUserName() {
	return identity == null ? null : identity.getName();
    }

    /**
     * Called by login() to acquire the username and password strings for authentication. This method does no validation
     * of either.
     *
     * @return String[], [0] = username, [1] = password
     */
    private String[] getUsernameAndPassword() throws LoginException {
	if (callbackHandler == null) {
	    throw new LoginException("No CallbackHandler available to collect authentication information");
	}
	NameCallback nc = new NameCallback("User name: ", "guest");
	PasswordCallback pc = new PasswordCallback("Password: ", false);
	Callback[] callbacks = { nc, pc };
	String username = null;
	String password = null;
	try {
	    callbackHandler.handle(callbacks);
	    username = nc.getName();
	    char[] tmpPassword = pc.getPassword();
	    if (tmpPassword != null) {
		credential = new char[tmpPassword.length];
		System.arraycopy(tmpPassword, 0, credential, 0, tmpPassword.length);
		pc.clearPassword();
		password = new String(credential);
	    }
	} catch (IOException ioe) {
	    throw new LoginException(ioe.toString());
	} catch (UnsupportedCallbackException uce) {
	    throw new LoginException("CallbackHandler does not support: " + uce.getCallback());
	}
	return new String[] { username, password };
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
	    Map<String, ?> options) {
	this.subject = subject;
	this.callbackHandler = callbackHandler;

	if (UniversalLoginModule.dsJndiName == null) {
	    UniversalLoginModule.dsJndiName = (String) options.get("dsJndiName");
	}
    }

    private boolean validatePassword(String inputPassword) {
	WebApplicationContext ctx = WebApplicationContextUtils
		.getWebApplicationContext(SessionManager.getServletContext());
	if (UniversalLoginModule.userManagementService == null) {
	    UniversalLoginModule.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	    UniversalLoginModule.themeService = (IThemeService) ctx.getBean("themeService");
	}

	// there is no session if the request did not go through SsoHandler
	// it happens on session failover
	if (SessionManager.getSession() != null) {
	    // allow sysadmin to login as another user; in this case, the LAMS shared session will be present,
	    // allowing the following check to work
	    if (UniversalLoginModule.userManagementService.isUserSysAdmin()) {
		if (UniversalLoginModule.log.isDebugEnabled()) {
		    UniversalLoginModule.log.debug("Authenticated sysadmin");
		}
		return true;
	    }
	}

	String userName = getUserName();

	// empty password not allowed
	if (StringUtils.isBlank(inputPassword)) {
	    if (UniversalLoginModule.log.isDebugEnabled()) {
		UniversalLoginModule.log.debug("Entered password is blank for user: " + userName);
	    }
	    return false;
	}

	boolean isValid = false;

	// check for internal authentication made by LoginRequestServlet or LoginAsAction
	if (inputPassword.startsWith("#LAMS")) {
	    if (UniversalLoginModule.log.isDebugEnabled()) {
		UniversalLoginModule.log.debug("Authenticating internally user: " + userName);
	    }

	    Long internalAuthenticationTime = UniversalLoginModule.internalAuthenticationTokens.get(inputPassword);

	    // internal authentication is valid for 10 seconds
	    isValid = (internalAuthenticationTime != null) && ((System.currentTimeMillis()
		    - internalAuthenticationTime) < UniversalLoginModule.INTERNAL_AUTHENTICATION_TIMEOUT);
	    if (!isValid) {
		UniversalLoginModule.internalAuthenticationTokens.remove(inputPassword);
	    }
	    return isValid;
	}

	try {
	    User user = UniversalLoginModule.userManagementService.getUserByLogin(userName);
	    // LDAP user provisioning
	    if (user == null) {
		if (!Configuration.getAsBoolean(ConfigurationKeys.LDAP_PROVISIONING_ENABLED)) {
		    return false;
		}

		// provision a new user by checking LDAP server
		LdapService ldapService = null;
		try {
		    ldapService = (LdapService) ctx.getBean("ldapService");
		} catch (NoSuchBeanDefinitionException e) {
		    // LDEV-1937
		    UniversalLoginModule.log.warn("No ldapService bean found, trying another method to fetch it.", e);
		    @SuppressWarnings("resource")
		    ApplicationContext context = new ClassPathXmlApplicationContext(
			    "org/lamsfoundation/lams/usermanagement/ldapContext.xml");
		    ldapService = (LdapService) context.getBean("ldapService");
		}

		if (UniversalLoginModule.log.isDebugEnabled()) {
		    UniversalLoginModule.log
			    .debug("LDAP provisioning is enabled, checking user " + userName + " against LDAP server.");
		}
		LDAPAuthenticator ldap = new LDAPAuthenticator(UniversalLoginModule.userManagementService);
		isValid = ldap.authenticate(userName, inputPassword);
		if (!isValid) {
		    return false;
		}

		// create a new user
		UniversalLoginModule.log.info("Creating new user using LDAP: " + userName);
		if (ldapService.createLDAPUser(ldap.getAttrs())) {
		    user = UniversalLoginModule.userManagementService.getUserByLogin(userName);
		    if (!ldapService.addLDAPUser(ldap.getAttrs(), user.getUserId())) {
			UniversalLoginModule.log.error("Could not add LDAP user " + userName + " to organisation.");
		    }
		} else {
		    UniversalLoginModule.log.error("Could not create new user for LDAP user: " + userName);
		    return false;
		}
	    }

	    // perform password checking according to user's authentication method
	    if (!isValid) {
		String type = user.getAuthenticationMethod().getAuthenticationMethodType().getDescription();
		if (AuthenticationMethodType.LDAP.equals(type)) {
		    LDAPAuthenticator authenticator = new LDAPAuthenticator(UniversalLoginModule.userManagementService);
		    isValid = authenticator.authenticate(userName, inputPassword);
		    // if LDAP user profile has updated, udpate user object for dto below
		    user = UniversalLoginModule.userManagementService.getUserByLogin(userName);
		} else if (AuthenticationMethodType.LAMS.equals(type)) {
		    // check password in LAMS DB
		    if (UniversalLoginModule.databaseAuthenticator == null) {
			UniversalLoginModule.databaseAuthenticator = new DatabaseAuthenticator(
				UniversalLoginModule.dsJndiName);
		    }
		    isValid = UniversalLoginModule.databaseAuthenticator.authenticate(userName, inputPassword);
		} else {
		    UniversalLoginModule.log.error("Unexpected authentication type: " + type);
		    return false;
		}
	    }

	    // disabled users can't login;
	    // check after authentication to give non-db authentication methods
	    // a chance to update disabled flag
	    if (user.getDisabledFlag()) {
		UniversalLoginModule.log.debug("User is disabled: " + user.getLogin());
		return false;
	    }

	    // if login is valid, register userDTO into session.
	    if (isValid) {
		UserDTO userDTO = user.getUserDTO();

		// If the user's css theme has been deleted, use the default
		// as fallback
		ThemeDTO userTheme = userDTO.getTheme();
		if (userTheme != null) {
		    boolean themeExists = false;
		    for (Theme theme : UniversalLoginModule.themeService.getAllThemes()) {
			if (userTheme.getId().equals(theme.getThemeId())) {
			    themeExists = true;
			    break;
			}
		    }

		    if (!themeExists) {
			userDTO.setTheme(new ThemeDTO(UniversalLoginModule.themeService.getDefaultTheme()));
		    }
		}
	    }
	} catch (Exception e) {
	    UniversalLoginModule.log.error("Error while validating password", e);
	    return false;
	}
	return isValid;
    }

    /**
     * According to Lams's security policy, all the authorization must be done locally, in other word, through Lams
     * database or other "local"(logically) data resource.
     *
     * @return Group[] containing the sets of roles
     */
    private Group[] getRoleSets() throws LoginException {
	String userName = getUserName();
	Map<String, Group> setsMap = new HashMap<>();
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    InitialContext ctx = new InitialContext();
	    DataSource ds = (DataSource) ctx.lookup(UniversalLoginModule.dsJndiName);

	    conn = ds.getConnection();
	    // Get the user role names
	    ps = conn.prepareStatement(UniversalLoginModule.ROLES_QUERY);
	    ps.setString(1, userName);
	    rs = ps.executeQuery();
	    if (rs.next() == false) {
		throw new FailedLoginException("No matching user name found in roles: " + userName);
	    }

	    ArrayList<String> groupMembers = new ArrayList<>();
	    do {
		String name = rs.getString(1);
		String groupName = rs.getString(2);
		if ((groupName == null) || (groupName.length() == 0)) {
		    groupName = "Roles";
		}
		Group group = setsMap.get(groupName);
		if (group == null) {
		    group = new SimpleGroup(groupName);
		    setsMap.put(groupName, group);
		}

		try {
		    Principal p = null;
		    // Assign minimal role if user has none
		    if (name == null) {
			name = Role.LEARNER;
			UniversalLoginModule.log.info("Found no roles for user: " + userName + ", assigning: " + name);
		    }
		    p = new SimplePrincipal(name);
		    if (!groupMembers.contains(name)) {
			UniversalLoginModule.log.info("Assign user: " + userName + " to role " + p.getName());
			group.addMember(p);
			groupMembers.add(name);
		    }
		    if (name.equals(Role.SYSADMIN)) {
			p = new SimplePrincipal(Role.AUTHOR);
			UniversalLoginModule.log.info("Found role " + name);
			if (!groupMembers.contains(Role.AUTHOR)) {
			    UniversalLoginModule.log.info("Assign user: " + userName + " to role " + Role.AUTHOR);
			    group.addMember(p);
			    groupMembers.add(Role.AUTHOR);
			}
		    }
		} catch (Exception e) {
		    UniversalLoginModule.log.info("Failed to create principal: " + name + " for user: " + userName, e);
		}
	    } while (rs.next());
	} catch (NamingException e) {
	    throw new LoginException(e.toString(true));
	} catch (SQLException e) {
	    throw new LoginException(e.toString());
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    UniversalLoginModule.log.error(e);
		}
	    }
	    if (ps != null) {
		try {
		    ps.close();
		} catch (SQLException e) {
		    UniversalLoginModule.log.error(e);
		}
	    }
	    if (conn != null) {
		try {
		    conn.close();
		} catch (Exception e) {
		    UniversalLoginModule.log.error(e);
		}
	    }
	}

	Group[] roleSets = new Group[setsMap.size()];
	setsMap.values().toArray(roleSets);
	return roleSets;
    }

    /**
     * Allows other LAMS modules to confirm user authentication before WildFly proper authentication commences.
     */
    public static void setAuthenticationToken(String token) {
	UniversalLoginModule.internalAuthenticationTokens.put(token, System.currentTimeMillis());
    }
}