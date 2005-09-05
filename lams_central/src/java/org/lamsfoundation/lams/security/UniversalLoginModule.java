package org.lamsfoundation.lams.security;

/**
 *	UniversalLoginModule is LAMS's own implementation of login module
 *  based on JBoss 3.0.*, 3.2.* and possibly higher versions. 
 *	
 *	It's named "universal" as currently it supports WebAuth, LDAP and 
 *	database based authentication mechanisms. 
 *  
 */

import java.security.acl.Group;
import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;

import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.naming.InitialContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.HttpSessionManager;
import org.lamsfoundation.lams.usermanagement.*;

public class UniversalLoginModule extends UsernamePasswordLoginModule {
	private static Logger log = Logger.getLogger(UniversalLoginModule.class);

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

	public UniversalLoginModule() {
	}

	private transient SimpleGroup userRoles = new SimpleGroup("Roles");

	protected String dsJndiName;

	protected String rolesQuery;

	protected String propertyFilePath;

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map sharedState, Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);

		//from options to get path to property file -> authentication.xml
		propertyFilePath = (String) options.get("authenticationPropertyFile");
		//load authentication property file  
		AuthenticationMethodConfigurer.setConfigFilePath(propertyFilePath);

	}

	protected boolean validatePassword(String inputPassword,
			String expectedPassword) {
		boolean isValid = false;
		if (inputPassword != null) {
			// empty password not allowed
			if (inputPassword.length() == 0)
				return false;

			log.debug("===> validatePassword() called: " + inputPassword
					+ " : " + expectedPassword);

			try {
				String username = getUsername();
				WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
				UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
				User user = service.getUserByLogin(username);

				log.debug("===> authenticating user: " + username);
				if (user == null)
					return false;

				AuthenticationMethod method = null;
				try {
					method = service.getAuthenticationMethodForUser(username);
					AuthenticationMethodConfigurer.configure(method);

					this.dsJndiName = method.getParameterByName("dsJndiName").getValue();
					this.rolesQuery = method.getParameterByName("rolesQuery").getValue();
				} catch (Exception e) {
					log.debug("===>Exception : " + e);
					return false;
				}

				List parameters = method.getAuthenticationMethodParameters();

				//for debug purpose only
				for (int i = 0; i < parameters.size(); i++) {
					AuthenticationMethodParameter mp = (AuthenticationMethodParameter) parameters.get(i);
					log.debug("===>" + mp.getName() + " = " + mp.getValue());
				}
				
				String type = method.getAuthenticationMethodType().getDescription();
				log.debug("===> authentication type :" + type);
				if ("LDAP".equals(type)) {
					LDAPAuthenticator authenticator = new LDAPAuthenticator(method);
					isValid = authenticator.authenticate(username,inputPassword);
					log.debug("===> LDAP :: user:" + username + ":"
							+ inputPassword + " authenticated! ");
				} else if ("LAMS".equals(type)) {
					DatabaseAuthenticator authenticator = new DatabaseAuthenticator(method);
					isValid = authenticator.authenticate(username,inputPassword);
					log.debug("===> LAMS:: user:" + username + ":"
							+ inputPassword + " authenticated! ");
				} else if ("WEB_AUTH".equals(type)) {
					log.debug("===> WEBAUTH: " + username + " type: " + type);
					WebAuthAuthenticator authenticator = new WebAuthAuthenticator();
					log.debug("===> webauth authenticator is:" + authenticator);
					isValid = authenticator.authenticate(username,inputPassword);
					log.debug("===> WEBAUTH :: user:" + username + ":"
							+ inputPassword + " authenticated! ");

				} else {
					log.debug("Unexpected authentication type!");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("===> exception: " + e);
			}
		}
		return isValid;
	}

	/** 
	 *	According to Lams's security policy, all the authorization 
	 *   must be done locally, in other word, through Lams database
	 * 	or other "local"(logically) data resource.  
	 *  
	 @return Group[] containing the sets of roles 
	 */
	protected Group[] getRoleSets() throws LoginException {
		String username = getUsername();
		Connection conn = null;
		HashMap setsMap = new HashMap();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(this.dsJndiName);

			log.debug("===> getRoleSets() called: " + dsJndiName + ":" + rolesQuery);
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
				if (getUnauthenticatedIdentity() == null)
					throw new FailedLoginException("No matching username found in Roles");
				/* We are running with an unauthenticatedIdentity so create an
				 empty Roles set and return.
				 */
				Group[] roleSets = { new SimpleGroup("Roles") };
				return roleSets;
			}

			do {
				String name = rs.getString(1);
				String groupName = rs.getString(2);
				if (groupName == null || groupName.length() == 0)
					groupName = "Roles";
				Group group = (Group) setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}

				try {
					Principal p = super.createIdentity(name);
					log.info("Assign user to role " + name);
					group.addMember(p);
				} catch (Exception e) {
					log.debug("Failed to create principal: " + name, e);
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
	 * Overriden to return an empty password string as typically one cannot
	 * obtain a user's password. We also override the validatePassword so
	 * this is ok.
	 * @return and empty password String
	 */
	protected String getUsersPassword() throws LoginException {
		return "";
	}

}