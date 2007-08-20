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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LDAPAuthenticator {
	
	private static Logger log = Logger.getLogger(LDAPAuthenticator.class);
	private static UserManagementService service;
	private static final String INITIAL_CONTEXT_FACTORY_VALUE = "com.sun.jndi.ldap.LdapCtxFactory";
	private Attributes attrs = null;
	
	public LDAPAuthenticator() {
	}
	
	private UserManagementService getService() {
		if (service==null) {
			WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
			service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
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
		String principalDNSuffix = Configuration.get(ConfigurationKeys.LDAP_PRINCIPAL_DN_SUFFIX);
		String userDN = principalDNPrefix + username + principalDNSuffix;
		env.setProperty(Context.SECURITY_PRINCIPAL, userDN);

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
		try {
			ctx = new InitialLdapContext(env, null);
			log.debug("===> LDAP context created: "+ctx);
			Attributes attrs = ctx.getAttributes(userDN);
			setAttrs(attrs);
			
			UserManagementService service = getService();
			if (service.getUserByLogin(username)!=null) {
				// update user's attributes and org membership
			}
			
			// debug attrs
			NamingEnumeration enumAttrs = attrs.getAll();
			while (enumAttrs.hasMoreElements()) {
				System.out.println(enumAttrs.next());
			}
			
			return true;
		} catch (AuthenticationNotSupportedException e) {
			log.error("===> Authentication mechanism not supported.  Check your "
					+ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION+" parameter: "
					+Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
		} catch (AuthenticationException e) {
			log.info("===> Incorrect username ("+userDN+") or password ("+credential+"): "+e.getMessage());
		} catch (Exception e) {
			log.error("===> LDAP exception: " + e);
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
		
		return false;
	}
	
	protected boolean createLDAPUser(Attributes attrs) {
		UserManagementService service = getService();
		User user = new User();
		try {
			String login = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR)));
			String fname = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_FIRST_NAME_ATTR)));
			String lname = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LAST_NAME_ATTR)));
			String email = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_EMAIL_ATTR)));
			String phone = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_PHONE_ATTR)));
			String fax = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_FAX_ATTR)));
			String mobile = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_MOBILE_ATTR)));
			if (login!=null && login.trim().length()>0) {
				log.debug("===> using LDAP attributes: "+login+","+fname+","+lname+","+email+","+phone+","+fax+","+mobile);
				user.setLogin(login);
				user.setPassword("dummy");  // password column is not-null
				user.setFirstName(fname);
				user.setLastName(lname);
				user.setEmail(email);
				user.setDayPhone(phone);
				user.setFax(fax);
				user.setMobilePhone(mobile);
				user.setAuthenticationMethod((AuthenticationMethod)service
						.findById(AuthenticationMethod.class, AuthenticationMethod.LDAP));
				user.setFlashTheme(service.getDefaultFlashTheme());
				user.setHtmlTheme(service.getDefaultHtmlTheme());
				user.setDisabledFlag(false);
				user.setCreateDate(new Date());
				user.setLocale(service.getDefaultLocale());
				service.save(user);
				// TODO write audit log
				return true;
			} else {
				log.error("===> Login name from LDAP is empty - user not created.");
			}
		} catch (Exception e) {
			log.error("===> Exception occurred while creating LDAP user: ", e);
		}
		return false;
	}
	
	protected boolean addLDAPUser(Attributes attrs, Integer userId) {
		UserManagementService service = getService();
		String orgCode = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ORG_CODE_ATTR)));
		List<String> ldapRoles = getAttributeStrings(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ROLES_ATTR)));
		log.debug("orgCode: "+orgCode);
		log.debug("ldapRoles: "+ldapRoles);
		List orgList = (List)service.findByProperty(Organisation.class, "code", orgCode);
		if (orgList!=null && !orgList.isEmpty()) {
			if (orgList.size()==1) {
				Organisation org = (Organisation)orgList.get(0);
				List<String> roleIds = getRoleIds(ldapRoles);
				if (roleIds!=null && !roleIds.isEmpty()) {
					User user = (User)service.findById(User.class, userId);
					log.debug("userId: "+userId);
					log.debug("orgId: "+org.getOrganisationId());
					log.debug("roleIds: "+roleIds);
					service.setRolesForUserOrganisation(user, org.getOrganisationId(), roleIds);
					return true;
				}
			} else {
				log.warn("More than one LAMS organisation found with the code: "+orgCode);
			}
		} else {
			log.warn("LDAP organisation code: "+orgCode+" doesn't correspond to any LAMS organisation code.");
		}
		return false;
	}
	
	// get list of LAMS role ids from list of ldap roles
	private List<String> getRoleIds(List<String> ldapRoles) {
		if (ldapRoles!=null) {
			ArrayList<String> roleIds = new ArrayList<String>();
			for (String role : ldapRoles) {
				if (Configuration.get(ConfigurationKeys.LDAP_LEARNER_MAP).indexOf(role) > 0 
						&& !roleIds.contains(Role.ROLE_LEARNER.toString())) {
					roleIds.add(Role.ROLE_LEARNER.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_MONITOR_MAP).indexOf(role) > 0
						&& !roleIds.contains(Role.ROLE_MONITOR.toString())) {
					roleIds.add(Role.ROLE_MONITOR.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_AUTHOR_MAP).indexOf(role) > 0
						&& !roleIds.contains(Role.ROLE_AUTHOR.toString())) {
					roleIds.add(Role.ROLE_AUTHOR.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_GROUP_ADMIN_MAP).indexOf(role) > 0
						&& !roleIds.contains(Role.ROLE_GROUP_ADMIN.toString())) {
					roleIds.add(Role.ROLE_GROUP_ADMIN.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_GROUP_MANAGER_MAP).indexOf(role) > 0
						&& !roleIds.contains(Role.ROLE_GROUP_MANAGER.toString())) {
					roleIds.add(Role.ROLE_GROUP_MANAGER.toString());
				}
			}
			return roleIds;
		}
		return null;
	}
	
	// get the multiple values of an ldap attribute
	private List<String> getAttributeStrings(Attribute attr) {
		try {
			ArrayList<String> attrValues = new ArrayList<String>();
			if (attr!=null) {
				NamingEnumeration attrEnum = attr.getAll();
				while (attrEnum.hasMore()) {
					Object attrValue = attrEnum.next();
					if (attrValue!=null) {
						attrValues.add(attrValue.toString());
					}
				}
				return attrValues;
			}
		} catch (NamingException e) {
			log.error("===> Naming exception occurred: "+e.getMessage());
		}
		return null;
	}
	
	// get the single (string) value of an ldap attribute
	private String getSingleAttributeString(Attribute attr) {
		try {
			if (attr!=null) {
				Object attrValue = attr.get();
				if (attrValue!=null) {
					return attrValue.toString();
				}
			}
		} catch (NamingException e) {
			log.error("===> Naming exception occurred: "+e.getMessage());
		}
		return null;
	}

}