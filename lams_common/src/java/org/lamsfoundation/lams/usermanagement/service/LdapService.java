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

package org.lamsfoundation.lams.usermanagement.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.timezone.TimeZoneUtil;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.BulkUpdateResultDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author jliew
 *
 */
public class LdapService implements ILdapService {

    private Logger log = Logger.getLogger(LdapService.class);

    private IUserManagementService service;

    private static final int BULK_UPDATE_CREATED = 0;

    private static final int BULK_UPDATE_UPDATED = 1;

    private static final int BULK_UPDATE_DISABLED = 2;

    @Override
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
	user.setDisabledFlag(getDisabledBoolean(attrs));
	service.saveUser(user);
    }

    // tries to match ldap attribute to a locale, otherwise returns server
    // default
    private SupportedLocale getLocale(String attribute) {
	if ((attribute != null) && (attribute.trim().length() > 0)) {
	    int index = attribute.indexOf("_");
	    if (index > 0) {
		String language = attribute.substring(0, index);
		String country = attribute.substring(index);
		return LanguageUtil.getSupportedLocale(language, country);
	    } else {
		return LanguageUtil.getSupportedLocale(attribute);
	    }
	}
	return LanguageUtil.getDefaultLocale();
    }

    @Override
    public boolean createLDAPUser(Attributes attrs) {
	User user = new User();
	try {
	    HashMap<String, String> map = getLDAPUserAttributes(attrs);
	    if ((map.get("login") != null) && (map.get("login").trim().length() > 0)) {
		if (log.isDebugEnabled()) {
		    log.debug("===> using LDAP attributes: " + map.get("login") + "," + map.get("fname") + ","
			    + map.get("lname") + "," + map.get("email") + "," + map.get("address1") + ","
			    + map.get("address2") + "," + map.get("address3") + "," + map.get("city") + ","
			    + map.get("state") + "," + map.get("postcode") + "," + map.get("country") + ","
			    + map.get("dayphone") + "," + map.get("eveningphone") + "," + map.get("fax") + ","
			    + map.get("mobile") + "," + map.get("locale"));
		}
		user.setLogin(map.get("login"));
		String salt = HashUtil.salt();
		user.setSalt(salt);
		user.setPassword(HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt));
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
		user.setAuthenticationMethod(
			(AuthenticationMethod) service.findById(AuthenticationMethod.class, AuthenticationMethod.LDAP));
		user.setTheme(service.getDefaultTheme());
		user.setDisabledFlag(getDisabledBoolean(attrs));
		user.setCreateDate(new Date());
		user.setLocale(getLocale(map.get("locale")));
		user.setTimeZone(TimeZoneUtil.getServerTimezone());
		user.setFirstLogin(true);
		service.saveUser(user);
		service.logUserCreated(user, (User) null);
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
	    map.put("fname",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_FIRST_NAME_ATTR))));
	    map.put("lname",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LAST_NAME_ATTR))));
	    map.put("email", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_EMAIL_ATTR))));
	    map.put("address1",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ADDR1_ATTR))));
	    map.put("address2",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ADDR2_ATTR))));
	    map.put("address3",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ADDR3_ATTR))));
	    map.put("city", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_CITY_ATTR))));
	    map.put("state", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_STATE_ATTR))));
	    map.put("postcode",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_POSTCODE_ATTR))));
	    map.put("country",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_COUNTRY_ATTR))));
	    map.put("dayphone",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_DAY_PHONE_ATTR))));
	    map.put("eveningphone",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_EVENING_PHONE_ATTR))));
	    map.put("fax", getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_FAX_ATTR))));
	    map.put("mobile",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_MOBILE_ATTR))));
	    map.put("locale",
		    getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOCALE_ATTR))));
	    map.put("disabled", getSingleAttributeString(
		    attrs.get(getLdapAttr(Configuration.get(ConfigurationKeys.LDAP_DISABLED_ATTR)))));
	} catch (Exception e) {
	    log.error("===> Exception occurred while getting LDAP user attributes: ", e);
	}

	// field validation; trim values before they get to database
	if ((map.get("login") != null) && (map.get("login").trim().length() > 255)) {
	    map.put("login", map.get("login").substring(0, 255));
	}
	if ((map.get("fname") != null) && (map.get("fname").trim().length() > 128)) {
	    map.put("fname", map.get("fname").substring(0, 128));
	}
	if ((map.get("lname") != null) && (map.get("lname").trim().length() > 128)) {
	    map.put("lname", map.get("lname").substring(0, 128));
	}
	if ((map.get("email") != null) && (map.get("email").trim().length() > 128)) {
	    map.put("email", map.get("email").substring(0, 128));
	}
	if ((map.get("address1") != null) && (map.get("address1").trim().length() > 64)) {
	    map.put("address1", map.get("address1").substring(0, 64));
	}
	if ((map.get("address2") != null) && (map.get("address2").trim().length() > 64)) {
	    map.put("address2", map.get("address2").substring(0, 64));
	}
	if ((map.get("address3") != null) && (map.get("address3").trim().length() > 64)) {
	    map.put("address3", map.get("address3").substring(0, 64));
	}
	if ((map.get("city") != null) && (map.get("city").trim().length() > 64)) {
	    map.put("city", map.get("city").substring(0, 64));
	}
	if ((map.get("state") != null) && (map.get("state").trim().length() > 64)) {
	    map.put("state", map.get("state").substring(0, 64));
	}
	if ((map.get("postcode") != null) && (map.get("postcode").trim().length() > 10)) {
	    map.put("postcode", map.get("postcode").substring(0, 10));
	}
	if ((map.get("country") != null) && (map.get("country").trim().length() > 64)) {
	    map.put("country", map.get("country").substring(0, 64));
	}
	if ((map.get("dayphone") != null) && (map.get("dayphone").trim().length() > 64)) {
	    map.put("dayphone", map.get("dayphone").substring(0, 64));
	}
	if ((map.get("eveningphone") != null) && (map.get("eveningphone").trim().length() > 64)) {
	    map.put("eveningphone", map.get("eveningphone").substring(0, 64));
	}
	if ((map.get("fax") != null) && (map.get("fax").trim().length() > 64)) {
	    map.put("fax", map.get("fax").substring(0, 64));
	}
	if ((map.get("mobile") != null) && (map.get("mobile").trim().length() > 64)) {
	    map.put("mobile", map.get("mobile").substring(0, 64));
	}

	return map;
    }

    @Override
    public String getLdapAttr(String ldapAttr) {
	if (ldapAttr != null) {
	    return (ldapAttr.startsWith("!") ? ldapAttr.substring(1) : ldapAttr);
	} else {
	    return ldapAttr;
	}
    }

    private Boolean getAsBoolean(Attribute attr) {
	String attrString = getSingleAttributeString(attr);
	if (attrString != null) {
	    if (attrString.equals("1") || attrString.equals("true")) {
		return true;
	    } else if (attrString.equals("0") || attrString.equals("false")) {
		return false;
	    }
	}
	return null;
    }

    @Override
    public boolean getDisabledBoolean(Attributes attrs) {
	String ldapDisabledAttrStr = Configuration.get(ConfigurationKeys.LDAP_DISABLED_ATTR);
	if (ldapDisabledAttrStr.startsWith("!")) {
	    ldapDisabledAttrStr = ldapDisabledAttrStr.substring(1);
	    Attribute ldapDisabledAttr = attrs.get(ldapDisabledAttrStr);
	    Boolean booleanValue = getAsBoolean(ldapDisabledAttr);
	    if (booleanValue != null) {
		return !booleanValue;
	    } else {
		// if there is no value, assume not disabled
		return false;
	    }
	} else {
	    return getAsBoolean(attrs.get(ldapDisabledAttrStr));
	}

    }

    @Override
    public boolean addLDAPUser(Attributes attrs, Integer userId) {
	User user = (User) service.findById(User.class, userId);
	// get ldap attributes for lams org and roles
	//String ldapOrgAttr = getSingleAttributeString(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ORG_ATTR)));
	List<String> ldapOrgs = getAttributeStrings(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ORG_ATTR)));
	List<String> ldapRoles = getAttributeStrings(attrs.get(Configuration.get(ConfigurationKeys.LDAP_ROLES_ATTR)));
	// get column name of lams_organisation to match ldapOrgAttr to
	String orgField = Configuration.get(ConfigurationKeys.LDAP_ORG_FIELD);

	boolean isAddingUserSuccessful = true;
	if ((ldapOrgs != null) && (ldapRoles != null) && (orgField != null)) {
	    // get list of possible matching organisations
	    for (String ldapOrg : ldapOrgs) {
		log.debug("Looking for organisation to add ldap user to...");
		List orgList = service.findByProperty(Organisation.class, orgField, ldapOrg);
		if ((orgList != null) && !orgList.isEmpty()) {
		    Organisation org = null;
		    if (orgList.size() == 1) {
			org = (Organisation) orgList.get(0);
		    } else if (orgList.size() > 1) {
			// if there are multiple orgs, select the one that is
			// active, if there is one
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(orgField, ldapOrg);
			properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);
			orgList = service.findByProperties(Organisation.class, properties);
			if (orgList.size() == 1) {
			    org = (Organisation) orgList.get(0);
			} else {
			    log.warn("More than one LAMS organisation found with the " + orgField + ": " + ldapOrg);
			    isAddingUserSuccessful = false;
			    break;
			}
		    }

		    // now convert the roles to lams roles and add the user to the org
		    List<String> roleIds = getRoleIds(ldapRoles);
		    if ((roleIds != null) && !roleIds.isEmpty()) {
			service.setRolesForUserOrganisation(user, org.getOrganisationId(), roleIds);
		    } else {
			log.warn("Couldn't map any roles from attribute: "
				+ Configuration.get(ConfigurationKeys.LDAP_ROLES_ATTR));
			isAddingUserSuccessful = false;
		    }

		    // if the user is a member of any other groups, remove them
		    if (Configuration.getAsBoolean(ConfigurationKeys.LDAP_ONLY_ONE_ORG)) {
			service.removeUserFromOtherGroups(userId, org.getOrganisationId());
			break;
		    }
		} else {
		    log.warn("No LAMS organisations found with the " + orgField + ": " + ldapOrg);
		    isAddingUserSuccessful = false;
		}
	    }
	}
	return isAddingUserSuccessful;
    }

    // get list of LAMS role ids from list of ldap roles
    private List<String> getRoleIds(List<String> ldapRoles) {
	if (ldapRoles != null) {
	    ArrayList<String> roleIds = new ArrayList<String>();
	    for (String role : ldapRoles) {
		if (isRoleInList(Configuration.get(ConfigurationKeys.LDAP_LEARNER_MAP), role)
			&& !roleIds.contains(Role.ROLE_LEARNER.toString())) {
		    roleIds.add(Role.ROLE_LEARNER.toString());
		}
		if (isRoleInList(Configuration.get(ConfigurationKeys.LDAP_MONITOR_MAP), role)
			&& !roleIds.contains(Role.ROLE_MONITOR.toString())) {
		    roleIds.add(Role.ROLE_MONITOR.toString());
		}
		if (isRoleInList(Configuration.get(ConfigurationKeys.LDAP_AUTHOR_MAP), role)
			&& !roleIds.contains(Role.ROLE_AUTHOR.toString())) {
		    roleIds.add(Role.ROLE_AUTHOR.toString());
		}
		if (isRoleInList(Configuration.get(ConfigurationKeys.LDAP_GROUP_MANAGER_MAP), role)
			&& !roleIds.contains(Role.ROLE_GROUP_MANAGER.toString())) {
		    roleIds.add(Role.ROLE_GROUP_MANAGER.toString());
		}
	    }
	    return roleIds;
	}
	return null;
    }

    private boolean isRoleInList(String list, String role) {
	if ((list != null) && (role != null)) {
	    String[] array = list.split(";");
	    for (String s : array) {
		if (role.contains(s)) {
		    return true;
		}
	    }
	}
	return false;
    }

    // get the multiple values of an ldap attribute
    private List<String> getAttributeStrings(Attribute attr) {
	try {
	    ArrayList<String> attrValues = new ArrayList<String>();
	    if (attr != null) {
		NamingEnumeration attrEnum = attr.getAll();
		while (attrEnum.hasMore()) {
		    Object attrValue = attrEnum.next();
		    if (attrValue != null) {
			attrValues.add(attrValue.toString());
		    }
		}
		return attrValues;
	    }
	} catch (NamingException e) {
	    log.error("===> Naming exception occurred: " + e.getMessage());
	}
	return null;
    }

    // get the single (string) value of an ldap attribute
    @Override
    public String getSingleAttributeString(Attribute attr) {
	try {
	    if (attr != null) {
		Object attrValue = attr.get();
		if (attrValue != null) {
		    return attrValue.toString();
		}
	    }
	} catch (NamingException e) {
	    log.error("===> Naming exception occurred: " + e.getMessage());
	}
	return null;
    }

    @Override
    public BulkUpdateResultDTO bulkUpdate() {
	// setup ldap context
	Properties env = new Properties();
	env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	env.setProperty(Context.SECURITY_AUTHENTICATION,
		Configuration.get(ConfigurationKeys.LDAP_SECURITY_AUTHENTICATION));
	// make java ldap provider return 10 results at a time instead of
	// default 1
	env.setProperty(Context.BATCHSIZE, "10");
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

	// get base DN to search on
	String baseDN = Configuration.get(ConfigurationKeys.LDAP_BASE_DN);

	// get search filter
	String filter = Configuration.get(ConfigurationKeys.LDAP_SEARCH_FILTER);

	// we can assume the filter will only have one variable since we only
	// have one input: the username
	filter = filter.replaceAll("\\{0\\}", "*");

	// get page size
	int pageSize = 100;
	try {
	    pageSize = new Integer(Configuration.get(ConfigurationKeys.LDAP_SEARCH_RESULTS_PAGE_SIZE)).intValue();
	} catch (Exception e) {
	    log.error("Couldn't read " + ConfigurationKeys.LDAP_SEARCH_RESULTS_PAGE_SIZE
		    + ", using default page size of 100.");
	}

	int totalResults = 0;
	int createdUsers = 0;
	int updatedUsers = 0;
	int disabledUsers = 0;
	List<String> messages = new ArrayList<String>();

	int contextResults = 0;
	try {
	    // open LDAP connection
	    LdapContext ctx = null;
	    try {
		ctx = new InitialLdapContext(env, null);
		// ask ldap server to return results in pages of PAGE_SIZE,
		// if supported
		ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
	    } catch (Exception e) {
		messages.add("Error creating control: " + e.getMessage());
		log.error(e, e);
	    }

	    // perform ldap search, in batches
	    log.info("Searching " + baseDN + " using filter " + filter);
	    byte[] cookie = null;
	    do {
		// set search to subtree of base dn
		SearchControls ctrl = new SearchControls();
		ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

		// do the search for all ldap users
		NamingEnumeration<SearchResult> results = ctx.search(baseDN, filter, ctrl);
		while (results.hasMore()) {
		    try {
			SearchResult result = results.next();
			Attributes attrs = result.getAttributes();

			// add or update this user to LAMS
			boolean disabled = getDisabledBoolean(attrs);
			String login = getSingleAttributeString(
				attrs.get(Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR)));
			if ((login != null) && (login.trim().length() > 0)) {
			    int code = bulkUpdateLDAPUser(login, attrs, disabled);
			    switch (code) {
				case BULK_UPDATE_CREATED:
				    createdUsers++;
				    break;
				case BULK_UPDATE_UPDATED:
				    updatedUsers++;
				    break;
				case BULK_UPDATE_DISABLED:
				    disabledUsers++;
				    break;
			    }
			} else {
			    log.error("Couldn't find login attribute for user using attribute name: "
				    + Configuration.get(ConfigurationKeys.LDAP_LOGIN_ATTR)
				    + ".  Dumping attributes...");
			    NamingEnumeration enumAttrs = attrs.getAll();
			    while (enumAttrs.hasMoreElements()) {
				log.error(enumAttrs.next());
			    }
			}
		    } catch (Exception e) {
			// continue processing
			messages.add(
				"Error processing context result number " + contextResults + ": " + e.getMessage());
		    }

		    contextResults++;
		}

		cookie = getPagedResponseCookie(ctx.getResponseControls());

		// set response cookie to continue paged result
		ctx.setRequestControls(
			new Control[] { new PagedResultsControl(pageSize, cookie, Control.NONCRITICAL) });
	    } while (cookie != null);
	    log.info("Ldap context " + baseDN + " returned " + contextResults + " users.");
	    ctx.close();
	} catch (NamingException e) {
	    messages.add("Error while processing " + baseDN + ": " + e.getMessage());
	    log.error(e, e);
	} catch (IOException e) {
	    messages.add("Error setting response cookie to continue paged ldap search results: " + e.getMessage());
	    log.error(e, e);
	} catch (Exception e) {
	    messages.add("Unknown error: " + e.getMessage());
	    log.error(e, e);
	}
	totalResults += contextResults;

	BulkUpdateResultDTO dto = new BulkUpdateResultDTO(totalResults, createdUsers, updatedUsers, disabledUsers,
		messages);

	log.info("Ldap returned " + totalResults + " users.");
	log.info(createdUsers + " were created, " + updatedUsers + " were updated/existed, and " + disabledUsers
		+ " were disabled.");

	return dto;
    }

    // create, update, or disable this user
    private int bulkUpdateLDAPUser(String login, Attributes attrs, boolean disabled) {
	int returnCode = -1;
	User user = service.getUserByLogin(login);
	if (!disabled) {
	    if (user == null) {
		log.info("Creating new user for LDAP username: " + login);
		if (createLDAPUser(attrs)) {
		    user = service.getUserByLogin(login);
		    returnCode = LdapService.BULK_UPDATE_CREATED;
		} else {
		    log.error("Couldn't create new user for LDAP username: " + login);
		}
	    } else {
		updateLDAPUser(user, attrs);
		returnCode = LdapService.BULK_UPDATE_UPDATED;
	    }
	    if (!addLDAPUser(attrs, user.getUserId())) {
		log.error("Couldn't add LDAP user: " + login + " to organisation.");
	    }
	} else {
	    // remove user from groups and set disabled flag
	    if (user != null) {
		service.disableUser(user.getUserId());
		returnCode = LdapService.BULK_UPDATE_DISABLED;
	    }
	}
	return returnCode;
    }

    // get paged result response cookie
    private byte[] getPagedResponseCookie(Control[] controls) {
	if (controls != null) {
	    for (Control control : controls) {
		if (control instanceof PagedResultsResponseControl) {
		    PagedResultsResponseControl prrc = (PagedResultsResponseControl) control;
		    return prrc.getCookie();
		}
	    }
	}
	return null;
    }

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------

    public void setService(IUserManagementService service) {
	this.service = service;
    }
}