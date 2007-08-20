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
 *	UniversalLoginModule is LAMS's own implementation of login module
 *  based on JBoss 3.0.*, 3.2.* and possibly higher versions. 
 *	
 *	It's named "universal" as currently it supports WebAuth, LDAP and 
 *	database based authentication mechanisms. 
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
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethodParameter;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethodType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class UniversalLoginModule extends UsernamePasswordLoginModule {
	
	private static Logger log = Logger.getLogger(UniversalLoginModule.class);

	public UniversalLoginModule() {
	}

	protected String dsJndiName;
	protected String rolesQuery;
	protected String principalsQuery;
	//protected String propertyFilePath;

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map sharedState, Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);

		//from options to get path to property file -> authentication.xml
		//propertyFilePath = (String) options.get("authenticationPropertyFile");
		//load authentication property file  
		//AuthenticationMethodConfigurer.setConfigFilePath(propertyFilePath);

		dsJndiName = (String)options.get("dsJndiName");
		principalsQuery = (String)options.get("principalsQuery");
		rolesQuery = (String)options.get("rolesQuery");
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
				UserManagementService service = (UserManagementService) ctx.getBean("userManagementService");
				User user = service.getUserByLogin(username);

				log.debug("===> authenticating user: " + username);
				if (user == null) {
					// provision a new user by checking ldap server
					if (Configuration.getAsBoolean(ConfigurationKeys.LDAP_PROVISIONING_ENABLED)) {
						log.debug("===> LDAP provisioning is enabled, checking username against LDAP server...");
						LDAPAuthenticator ldap = new LDAPAuthenticator();
						isValid = ldap.authenticate(username, inputPassword);
						if (isValid) {  // create a new user
							log.info("===> Creating new user for LDAP username: " + username);
							if (ldap.createLDAPUser(ldap.getAttrs())) {
								user = service.getUserByLogin(username);
								if (!ldap.addLDAPUser(ldap.getAttrs(), user.getUserId())) {
									log.error("===> Couldn't add LDAP user: "+username+" to organisation.");
								}
							} else {
								log.error("===> Couldn't create new user for LDAP username: "+username);
								return false;
							}
						} else {  // didn't authenticate successfully with ldap
							return false;
						}
					} else {
						return false;
					}
				}

				if (user.getDisabledFlag()) {
					log.debug("===> user is disabled.");
					return false;
				}
				
				AuthenticationMethod method = user.getAuthenticationMethod();
				/*try {
					AuthenticationMethodConfigurer.configure(method);

					// use User's AuthMethod's dsJndiName and rolesQuery if set, otherwise use LAMS-Database's
					AuthenticationMethodParameter dsParam = method.getParameterByName("dsJndiName");
					AuthenticationMethodParameter rolesQueryParam = method.getParameterByName("rolesQuery");
					if (dsParam!=null && rolesQueryParam!=null) {
						this.dsJndiName = dsParam.getValue();
						this.rolesQuery = rolesQueryParam.getValue();
					} else {
						AuthenticationMethod defaultAuthMethod = (AuthenticationMethod)service
							.findById(AuthenticationMethod.class, AuthenticationMethod.DB);
						this.dsJndiName = defaultAuthMethod.getParameterByName("dsJndiName").getValue();
						this.rolesQuery = defaultAuthMethod.getParameterByName("rolesQuery").getValue();
					}
				} catch (Exception e) {
					log.error("===> Error retrieving authentication method parameters : " + e, e);
					return false;
				}

				//for debug purpose only
				if (log.isDebugEnabled()) {
					List parameters = method.getAuthenticationMethodParameters();
					for (int i = 0; i < parameters.size(); i++) {
						AuthenticationMethodParameter mp = (AuthenticationMethodParameter) parameters.get(i);
						log.debug("===>" + mp.getName() + " = " + mp.getValue());
					}
				}*/
				
				if (!isValid) {
					String type = method.getAuthenticationMethodType().getDescription();
					log.debug("===> authentication type: " + type);
					if (AuthenticationMethodType.LDAP.equals(type)) {
						LDAPAuthenticator authenticator = new LDAPAuthenticator();
						isValid = authenticator.authenticate(username,inputPassword);
					} else if (AuthenticationMethodType.LAMS.equals(type)) {
						// DatabaseAuthenticator authenticator = new DatabaseAuthenticator(method);
						DatabaseAuthenticator authenticator = new DatabaseAuthenticator(dsJndiName, principalsQuery);
						isValid = authenticator.authenticate(username,inputPassword);
					} else if (AuthenticationMethodType.WEB_AUTH.equals(type)) {
						WebAuthAuthenticator authenticator = new WebAuthAuthenticator();
						isValid = authenticator.authenticate(username,inputPassword);
					} else {
						log.error("===> Unexpected authentication type: "+type);
						return false;
					}
				}
				//if login is valid, register userDTO into session.
				if(isValid){
					HttpSession sharedsession = SessionManager.getSession(); 
					sharedsession.setAttribute(AttributeNames.USER,user.getUserDTO());
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("===> exception: " + e,e);
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

			//log.debug("===> getRoleSets() called: " + dsJndiName + ": " + rolesQuery);
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

			ArrayList<String> groupMembers = new ArrayList<String>();
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
					Principal p;
					// Assign minimal role if user has none
					if (name==null) {
						name = Role.LEARNER;
						log.info("===> Found no roles");
					}
					p = super.createIdentity(name);
					if (!groupMembers.contains(name)) {
						log.info("===> Assign user to role " + p.getName());
						group.addMember(p);
						groupMembers.add(name);
					}
					if (name.equals(Role.SYSADMIN) || name.equals(Role.AUTHOR_ADMIN)) {
						p = super.createIdentity(Role.AUTHOR);
						log.info("===> Found "+name);
						if (!groupMembers.contains(Role.AUTHOR)) {
							log.info("===> Assign user to role "+Role.AUTHOR);
							group.addMember(p);
							groupMembers.add(Role.AUTHOR);
						}
					}
				} catch (Exception e) {
					log.debug("===> Failed to create principal: " + name, e);
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