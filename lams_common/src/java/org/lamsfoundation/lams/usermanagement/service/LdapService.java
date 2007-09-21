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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.usermanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.LangUtil;

/**
 * @author jliew
 *
 */
public class LdapService implements ILdapService {

	private Logger log = Logger.getLogger(LdapService.class);
	private IUserManagementService service;
	
	public IUserManagementService getService() {
		return service;
	}

	public void setService(IUserManagementService service) {
		this.service = service;
	}
	
	public void updateLDAPUser(User user, Attributes attrs) {
		HashMap<String, String> map = getLDAPUserAttributes(attrs);
		user.setLogin(map.get("login"));
		user.setFirstName(map.get("fname"));
		user.setLastName(map.get("lname"));
		user.setEmail(map.get("email"));
		user.setAddressLine1(map.get("address1"));
		user.setAddressLine2(map.get("address2"));
		user.setAddressLine3(map.get("address3"));
		user.setCity(map.get("city"));
		user.setState(map.get("state"));
		user.setPostcode(map.get("postcode"));
		user.setCountry(map.get("country"));
		user.setDayPhone(map.get("dayphone"));
		user.setEveningPhone(map.get("eveningphone"));
		user.setFax(map.get("fax"));
		user.setMobilePhone(map.get("mobile"));
		user.setLocale(getLocale(map.get("locale")));
		getService().save(user);
	}
	
	// tries to match ldap attribute to a locale, otherwise returns server default
	private SupportedLocale getLocale(String attribute) {
		if (attribute!=null && attribute.trim().length()>0) {
			int index = attribute.indexOf("_");
			if (index>0) {
				String language = attribute.substring(0, index);
				String country = attribute.substring(index);
				return LangUtil.getSupportedLocale(language, country);
			} else {
				return LangUtil.getSupportedLocale(attribute);
			}
		}
		return LangUtil.getDefaultLocale();
	}
	
	public boolean createLDAPUser(Attributes attrs) {
		User user = new User();
		try {
			HashMap<String, String> map = getLDAPUserAttributes(attrs);
			if (map.get("login")!=null && map.get("login").trim().length()>0) {
				if (log.isDebugEnabled()) {
					log.debug("===> using LDAP attributes: "
							+map.get("login")+","+map.get("fname")+","+map.get("lname")+","
							+map.get("email")+","+map.get("phone")+","+map.get("fax")+","
							+map.get("mobile"));
				}
				user.setLogin(map.get("login"));
				user.setPassword("dummy");  // password column is not-null
				user.setFirstName(map.get("fname"));
				user.setLastName(map.get("lname"));
				user.setEmail(map.get("email"));
				user.setAddressLine1(map.get("address1"));
				user.setAddressLine2(map.get("address2"));
				user.setAddressLine3(map.get("address3"));
				user.setCity(map.get("city"));
				user.setState(map.get("state"));
				user.setPostcode(map.get("postcode"));
				user.setCountry(map.get("country"));
				user.setDayPhone(map.get("dayphone"));
				user.setEveningPhone(map.get("eveningphone"));
				user.setFax(map.get("fax"));
				user.setMobilePhone(map.get("mobile"));
				user.setAuthenticationMethod((AuthenticationMethod)service
						.findById(AuthenticationMethod.class, AuthenticationMethod.LDAP));
				user.setFlashTheme(service.getDefaultFlashTheme());
				user.setHtmlTheme(service.getDefaultHtmlTheme());
				user.setDisabledFlag(false);
				user.setCreateDate(new Date());
				user.setLocale(getLocale(map.get("locale")));
				service.save(user);
				service.auditUserCreated(user, "common");
				return true;
			} else {
				log.error("===> Login name from LDAP is empty - user not created.");
			}
		} catch (Exception e) {
			log.error("===> Exception occurred while creating LDAP user: ", e);
		}
		return false;
	}
	
	private HashMap<String, String> getLDAPUserAttributes(Attributes attrs) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			map.put("login", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR))));
			map.put("fname", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_FIRST_NAME_ATTR))));
			map.put("lname", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LAST_NAME_ATTR))));
			map.put("email", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_EMAIL_ATTR))));
			map.put("address1", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ADDR1_ATTR))));
			map.put("address2", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ADDR2_ATTR))));
			map.put("address3", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ADDR3_ATTR))));
			map.put("city", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_CITY_ATTR))));
			map.put("state", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_STATE_ATTR))));
			map.put("postcode", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_POSTCODE_ATTR))));
			map.put("country", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_COUNTRY_ATTR))));
			map.put("dayphone", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_DAY_PHONE_ATTR))));
			map.put("eveningphone", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_EVENING_PHONE_ATTR))));
			map.put("fax", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_FAX_ATTR))));
			map.put("mobile", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_MOBILE_ATTR))));
			map.put("locale", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOCALE_ATTR))));
		} catch (Exception e) {
			log.error("===> Exception occurred while getting LDAP user attributes: ", e);
		}
		return map;
	}
	
	public boolean addLDAPUser(Attributes attrs, Integer userId) {
		User user = (User)service.findById(User.class, userId);
		// get ldap attributes for lams org and roles
		String ldapOrgAttr = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ORG_ATTR)));
		List<String> ldapRoles = getAttributeStrings(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ROLES_ATTR)));
		// get column name of lams_organisation to match ldapOrgAttr to
		String orgField = Configuration.get(ConfigurationKeys.LDAP_ORG_FIELD);
		
		if (ldapOrgAttr != null && ldapRoles != null && orgField != null) {
			// get list of possible matching organisations
			log.debug("Looking for organisation to add ldap user to...");
			List orgList = (List)service.findByProperty(Organisation.class, orgField, ldapOrgAttr);
			if (orgList!=null && !orgList.isEmpty()) {
				Organisation org = null;
				if (orgList.size()==1) {
					org = (Organisation)orgList.get(0);
				} else if (orgList.size() > 1) {
					// if there are multiple orgs, select the one that is active, if there is one
					HashMap<String, Object> properties = new HashMap<String, Object>();
					properties.put(orgField, ldapOrgAttr);
					properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);
					orgList = (List)service.findByProperties(Organisation.class, properties);
					if (orgList.size()==1) {
						org = (Organisation)orgList.get(0);
					} else {
						log.warn("More than one LAMS organisation found with the "+orgField+": "+ldapOrgAttr);
						return false;
					}
				}
				// if the user is a member of any other groups, remove them
				if (Configuration.getAsBoolean(ConfigurationKeys.LDAP_ONLY_ONE_ORG)) {
					Set uos = user.getUserOrganisations();
					Iterator i = uos.iterator();
					while (i.hasNext()) {
						UserOrganisation uo = (UserOrganisation)i.next();
						Organisation currentOrg = uo.getOrganisation();
						if (currentOrg.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)) {
							if (!currentOrg.equals(org)) {
								i.remove();
								// remove userOrg from the org's collection
								Set currentOrgUos = currentOrg.getUserOrganisations();
								currentOrgUos.remove(uo);
								currentOrg.setUserOrganisations(currentOrgUos);
								// remove subgroups
								service.deleteChildUserOrganisations(uo.getUser(), uo.getOrganisation());
							}
						}
					}
					user.setUserOrganisations(uos);
					service.save(user);
				}
				// now convert the roles to lams roles and add the user to the org
				List<String> roleIds = getRoleIds(ldapRoles);
				if (roleIds!=null && !roleIds.isEmpty()) {
					service.setRolesForUserOrganisation(user, org.getOrganisationId(), roleIds);
					return true;
				}
			} else {
				log.warn("No LAMS organisations found with the "+orgField+": "+ldapOrgAttr);
			}
		}
		return false;
	}
	
	// get list of LAMS role ids from list of ldap roles
	private List<String> getRoleIds(List<String> ldapRoles) {
		if (ldapRoles!=null) {
			ArrayList<String> roleIds = new ArrayList<String>();
			for (String role : ldapRoles) {
				if (Configuration.get(ConfigurationKeys.LDAP_LEARNER_MAP).indexOf(role) >= 0 
						&& !roleIds.contains(Role.ROLE_LEARNER.toString())) {
					roleIds.add(Role.ROLE_LEARNER.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_MONITOR_MAP).indexOf(role) >= 0
						&& !roleIds.contains(Role.ROLE_MONITOR.toString())) {
					roleIds.add(Role.ROLE_MONITOR.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_AUTHOR_MAP).indexOf(role) >= 0
						&& !roleIds.contains(Role.ROLE_AUTHOR.toString())) {
					roleIds.add(Role.ROLE_AUTHOR.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_GROUP_ADMIN_MAP).indexOf(role) >= 0
						&& !roleIds.contains(Role.ROLE_GROUP_ADMIN.toString())) {
					roleIds.add(Role.ROLE_GROUP_ADMIN.toString());
				}
				if (Configuration.get(ConfigurationKeys.LDAP_GROUP_MANAGER_MAP).indexOf(role) >= 0
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
	
	public int updateLAMSFromLdap() {
		// setup ldap context
		Properties env = new Properties();
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.setProperty(Context.SECURITY_AUTHENTICATION, Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
		env.setProperty(Context.PROVIDER_URL, Configuration.get(ConfigurationKeys.LDAP_PROVIDER_URL));
		String securityProtocol = Configuration.get(ConfigurationKeys.LDAP_SECURITY_PROTOCOL);
		if (StringUtils.equals("ssl", securityProtocol)) {
			env.setProperty(Context.SECURITY_PROTOCOL, securityProtocol);
			// FIXME: synchronization issue: dynamically load certificate into
			// system instead of overwritting it.
			System.setProperty("javax.net.ssl.trustStore", Configuration.get(ConfigurationKeys.LDAP_TRUSTSTORE_PATH));
			System.setProperty("javax.net.ssl.trustStorePassword", Configuration.get(ConfigurationKeys.LDAP_TRUSTSTORE_PASSWORD));
		}
		
		// get base dn
		String baseDN = Configuration.get(ConfigurationKeys.LDAP_PRINCIPAL_DN_SUFFIX);
		if (baseDN.startsWith(",")) {
			baseDN = baseDN.substring(1);
		}
		
		// get search filter
		String filter = Configuration.get(ConfigurationKeys.LDAP_PRINCIPAL_DN_PREFIX);
		filter = "(" + filter + (filter.endsWith("=") ? "" : "=") + "*)";
		
		int numResults = 0;
		try {
			DirContext ctx = new InitialDirContext(env);
			
			// set search to subtree of base dn
			SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            
            // do the search for all ldap users
            NamingEnumeration<SearchResult> results = ctx.search(baseDN, filter, ctrl);
            while (results.hasMore()) {
            	SearchResult result = results.next();
            	Attributes attrs = result.getAttributes();
            	
            	// add or update this user to LAMS
            	String login = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR)));
            	if (login != null && login.trim().length() > 0) {
            		User user = getService().getUserByLogin(login);
            		if (user == null) {
            			log.info("Creating new user for LDAP username: " + login);
            			if (createLDAPUser(attrs)) {
            				user = getService().getUserByLogin(login);
            			} else {
    						log.error("Couldn't create new user for LDAP username: "+login);
    					}
            		} else {
            			updateLDAPUser(user, attrs);
            		}
            		if (!addLDAPUser(attrs, user.getUserId())) {
            			log.error("Couldn't add LDAP user: "+login+" to organisation.");
            		}
            	} else {
            		log.error("Couldn't find login attribute for user using attribute name: " 
            				+ Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR) + ".  Dumping attributes...");
            		NamingEnumeration enumAttrs = attrs.getAll();
    				while (enumAttrs.hasMoreElements()) {
    					log.error(enumAttrs.next());
    				}
            	}
            	
            	numResults++;
            }
            log.info("Ldap returned " + numResults + " users.");
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return numResults;
	}
}
