/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.integration.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONObject;
import org.imsglobal.pox.IMSPOXRequest;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtServerLessonMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.dto.ExtGroupDTO;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.util.GroupInfoFetchException;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CSVUtil;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;

import oauth.signpost.exception.OAuthException;

/**
 * <p>
 * <a href="IntegrationService.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class IntegrationService implements IIntegrationService {

    private static Logger log = Logger.getLogger(IntegrationService.class);

    private IGradebookService gradebookService;
    private IUserManagementService service;
    private ILessonService lessonService;
    private ILamsCoreToolService toolService;
    private ITimezoneService timezoneService;

    /**
     * Returns integration server or LTI tool consumer by its human-entered server key/server id.
     *
     * @param serverId
     *            server key/server id
     * @param isIntegrationServer
     *            true in case this is an integration server, false - LTI tool consumer
     * @return
     */
    @Override
    public ExtServer getExtServer(String serverId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("serverid", serverId);
	List<ExtServer> list = service.findByProperties(ExtServer.class, properties);
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public ExtCourseClassMap getExtCourseClassMap(Integer sid, String extCourseId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("courseid", extCourseId);
	properties.put("extServer.sid", sid);
	List<ExtCourseClassMap> list = service.findByProperties(ExtCourseClassMap.class, properties);

	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    // wrapper method for compatibility with original integration modules
    @Override
    public ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap, String extCourseId,
	    String countryIsoCode, String langIsoCode, String prettyCourseName, String method, Boolean prefix)
	    throws UserInfoValidationException {

	// Set the pretty course name if available, otherwise maintain the extCourseId
	String courseName = "";
	if (prettyCourseName != null) {
	    courseName = prettyCourseName;
	} else {
	    courseName = extCourseId;
	}

	Boolean isTeacher = (StringUtils.equals(method, LoginRequestDispatcher.METHOD_AUTHOR)
		|| StringUtils.equals(method, LoginRequestDispatcher.METHOD_MONITOR));
	return getExtCourseClassMap(extServer, userMap, extCourseId, courseName, countryIsoCode, langIsoCode,
		service.getRootOrganisation().getOrganisationId().toString(), isTeacher, prefix);
    }

    // wrapper method for compatibility with original integration modules
    @Override
    public ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap, String extCourseId,
	    String countryIsoCode, String langIsoCode, String prettyCourseName, String method)
	    throws UserInfoValidationException {
	return getExtCourseClassMap(extServer, userMap, extCourseId, countryIsoCode, langIsoCode, prettyCourseName,
		method, true);
    }

    // newer method which accepts course name, a parent org id, a flag for whether user should get
    // 'teacher' roles, and a flag for whether to use a prefix in the org's name
    @Override
    public ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap, String extCourseId,
	    String extCourseName, String countryIsoCode, String langIsoCode, String parentOrgId, Boolean isTeacher,
	    Boolean prefix) throws UserInfoValidationException {
	Organisation org;
	User user = userMap.getUser();

	ExtCourseClassMap extCourseClassMap = getExtCourseClassMap(extServer.getSid(), extCourseId);
	if (extCourseClassMap == null) {
	    //create new ExtCourseClassMap
	    extCourseClassMap = createExtCourseClassMap(extServer, user.getUserId(), extCourseId, extCourseName,
		    countryIsoCode, langIsoCode, parentOrgId, prefix);
	    org = extCourseClassMap.getOrganisation();
	} else {
	    org = extCourseClassMap.getOrganisation();

	    // update external course name if if has changed
	    String requestedCourseName = prefix ? buildName(extServer.getPrefix(), extCourseName) : extCourseName;
	    if ((extCourseName != null) && !org.getName().equals(requestedCourseName)) {

		// validate org name
		if (!ValidationUtil.isOrgNameValid(requestedCourseName)) {
		    throw new UserInfoValidationException("Can't create organisation due to validation error: "
			    + "organisation name cannot contain any of these characters < > ^ * @ % $. External server:"
			    + extServer.getServerid() + ", orgId:" + extCourseId + ", orgName:" + requestedCourseName);
		}

		org.setName(requestedCourseName);
		service.updateOrganisationAndWorkspaceFolderNames(org);
	    }
	}

	updateUserRoles(user, org, isTeacher);

	return extCourseClassMap;
    }

    @Override
    public ExtCourseClassMap createExtCourseClassMap(ExtServer extServer, Integer userId, String extCourseId,
	    String extCourseName, String countryIsoCode, String langIsoCode, String parentOrgId, Boolean prefix)
	    throws UserInfoValidationException {
	User user = (User) service.findById(User.class, userId);
	Organisation org = createOrganisation(extServer, user, extCourseId, extCourseName, countryIsoCode, langIsoCode,
		parentOrgId, prefix);
	ExtCourseClassMap extCourseClassMap = new ExtCourseClassMap();
	extCourseClassMap.setCourseid(extCourseId);
	extCourseClassMap.setExtServer(extServer);
	extCourseClassMap.setOrganisation(org);
	service.save(extCourseClassMap);
	return extCourseClassMap;
    }

    private void updateUserRoles(User user, Organisation org, Boolean isTeacher) {

	//create UserOrganisation if it doesn't exist
	UserOrganisation uo = service.getUserOrganisation(user.getUserId(), org.getOrganisationId());
	if (uo == null) {
	    uo = new UserOrganisation(user, org);
	    service.save(uo);
	    user.addUserOrganisation(uo);
	    service.saveUser(user);
	}

	Integer[] roles;
	if (isTeacher) {
	    roles = new Integer[] { Role.ROLE_AUTHOR, Role.ROLE_MONITOR, Role.ROLE_LEARNER };
	} else {
	    roles = new Integer[] { Role.ROLE_LEARNER };
	}
	for (Integer roleId : roles) {
	    if (!service.hasRoleInOrganisation(user, roleId, org)) {
		UserOrganisationRole uor = new UserOrganisationRole(uo, (Role) service.findById(Role.class, roleId));
		service.save(uor);
		uo.addUserOrganisationRole(uor);
		service.save(uo);
	    }
	}
    }

    @Override
    public List<ExtUserUseridMap> getExtUserUseridMapByExtServer(ExtServer extServer) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("extServer.sid", extServer.getSid());
	List list = service.findByProperties(ExtUserUseridMap.class, properties);
	return list;
    }

    @Override
    public ExtUserUseridMap getExtUserUseridMap(ExtServer extServer, String extUsername, boolean prefix)
	    throws UserInfoFetchException, UserInfoValidationException {
	ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(extServer, extUsername);

	if (extUserUseridMap == null) {
	    String[] userData = getUserDataFromExtServer(extServer, extUsername);
	    String salt = HashUtil.salt();
	    String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);
	    return createExtUserUseridMap(extServer, extUsername, password, salt, userData, prefix);
	} else {
	    return extUserUseridMap;
	}
    }

    @Override
    public ExtUserUseridMap getExtUserUseridMap(ExtServer extServer, String extUsername)
	    throws UserInfoFetchException, UserInfoValidationException {
	return getExtUserUseridMap(extServer, extUsername, true);
    }

    @Override
    public ExtUserUseridMap getExistingExtUserUseridMap(ExtServer extServer, String extUsername) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("extServer.sid", extServer.getSid());
	properties.put("extUsername", extUsername);
	List<ExtUserUseridMap> list = service.findByProperties(ExtUserUseridMap.class, properties);

	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public ExtUserUseridMap getImplicitExtUserUseridMap(ExtServer extServer, String extUsername, String password,
	    String salt, String firstName, String lastName, String email) throws UserInfoValidationException {
	ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(extServer, extUsername);

	if (extUserUseridMap == null) {
	    String[] defaultLangCountry = LanguageUtil.getDefaultLangCountry();
	    String country = LanguageUtil.getDefaultCountry();
	    String[] userData = { "", firstName, lastName, "", "", "", "", country, "", "", "", email,
		    defaultLangCountry[1], defaultLangCountry[0] };
	    return createExtUserUseridMap(extServer, extUsername, password, salt, userData, false);
	} else {
	    return extUserUseridMap;
	}
    }

    @Override
    public ExtUserUseridMap getImplicitExtUserUseridMap(ExtServer extServer, String extUsername, String firstName,
	    String lastName, String langIsoCode, String countryIsoCode, String country, String email, boolean prefix,
	    boolean isUpdateUserDetails) throws UserInfoValidationException {

	ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(extServer, extUsername);

	//create new one if it doesn't exist yet
	if (extUserUseridMap == null) {
	    String[] userData = { "", firstName, lastName, "", "", "", "", country, "", "", "", email, countryIsoCode,
		    langIsoCode };
	    String salt = HashUtil.salt();
	    String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);
	    return createExtUserUseridMap(extServer, extUsername, password, salt, userData, prefix);

	    //update user details if it's required
	} else if (isUpdateUserDetails) {

	    User user = extUserUseridMap.getUser();

	    // first name validation
	    if (StringUtils.isNotBlank(firstName) && !ValidationUtil.isFirstLastNameValid(firstName)) {
		throw new UserInfoValidationException("Can't update user details due to validation error: "
			+ "First name contains invalid characters. External server:" + extServer.getServerid()
			+ ", Username:" + user.getLogin() + ", firstName:" + firstName + ", lastName:" + lastName);
	    }
	    // last name validation
	    if (StringUtils.isNotBlank(lastName) && !ValidationUtil.isFirstLastNameValid(lastName)) {
		throw new UserInfoValidationException("Can't update user details due to validation error: "
			+ "Last name contains invalid characters. External server:" + extServer.getServerid()
			+ ", Username:" + user.getLogin() + ", firstName:" + firstName + ", lastName:" + lastName);
	    }
	    // user email validation
	    if (StringUtils.isNotBlank(email) && !ValidationUtil.isEmailValid(email)) {
		throw new UserInfoValidationException("Can't update user details due to validation error: "
			+ "Email format is invalid. External server:" + extServer.getServerid() + ", Username:"
			+ user.getLogin() + ", firstName:" + firstName + ", lastName:" + lastName);
	    }

	    user.setFirstName(firstName);
	    user.setLastName(lastName);
	    user.setEmail(email);
	    user.setModifiedDate(new Date());
	    user.setLocale(LanguageUtil.getSupportedLocale(langIsoCode, countryIsoCode));
	    if (StringUtils.isBlank(country)) {
		country = LanguageUtil.getDefaultCountry();
	    }
	    user.setCountry(country);
	    service.saveUser(user);

	    return extUserUseridMap;

	    //simply return existing one
	} else {
	    return extUserUseridMap;
	}
    }

    private Organisation createOrganisation(ExtServer extServer, User user, String extCourseId, String extCourseName,
	    String countryIsoCode, String langIsoCode, String parentOrgId, Boolean prefix)
	    throws UserInfoValidationException {

	Organisation org = new Organisation();

	// org name validation
	String orgName = prefix ? buildName(extServer.getPrefix(), extCourseName) : extCourseName;
	if (StringUtils.isNotBlank(orgName) && !ValidationUtil.isOrgNameValid(orgName)) {
	    throw new UserInfoValidationException("Can't create organisation due to validation error: "
		    + "organisation name cannot contain any of these characters < > ^ * @ % $. External server:"
		    + extServer.getServerid() + ", orgId:" + extCourseId + ", orgName:" + orgName);
	}
	org.setName(orgName);

	org.setDescription(extCourseId);
	org.setOrganisationState(
		(OrganisationState) service.findById(OrganisationState.class, OrganisationState.ACTIVE));

	org.setEnableCourseNotifications(true);

	// determine whether org will be a group or subgroup
	Organisation parent = (Organisation) service.findById(Organisation.class, Integer.valueOf(parentOrgId));
	if (parent != null) {
	    org.setParentOrganisation(parent);
	    if (!parent.getOrganisationId().equals(service.getRootOrganisation().getOrganisationId())) {
		org.setOrganisationType(
			(OrganisationType) service.findById(OrganisationType.class, OrganisationType.CLASS_TYPE));
	    } else {
		org.setOrganisationType(
			(OrganisationType) service.findById(OrganisationType.class, OrganisationType.COURSE_TYPE));
	    }
	} else {
	    // default
	    org.setParentOrganisation(service.getRootOrganisation());
	    org.setOrganisationType(
		    (OrganisationType) service.findById(OrganisationType.class, OrganisationType.COURSE_TYPE));
	}
	return service.saveOrganisation(org, user.getUserId());
    }

    // flexible method to specify username and password
    private ExtUserUseridMap createExtUserUseridMap(ExtServer extServer, String extUsername, String password,
	    String salt, String[] userData, boolean prefix) throws UserInfoValidationException {

	String login = prefix ? buildName(extServer.getPrefix(), extUsername) : extUsername;
	String firstName = userData[1];
	String lastName = userData[2];
	String email = userData[11];

	// login validation
	if (StringUtils.isBlank(login)) {
	    throw new UserInfoValidationException(
		    "Can't create user due to validation error: " + "Username cannot be blank. External server:"
			    + extServer.getServerid() + ", firstName:" + firstName + ", lastName:" + lastName);
	} else if (!ValidationUtil.isUserNameValid(login)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "Username can only contain alphanumeric characters and no spaces. External server:"
		    + extServer.getServerid() + ", Username:" + login);
	}

	// first name validation
	if (StringUtils.isNotBlank(firstName) && !ValidationUtil.isFirstLastNameValid(firstName)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "First name contains invalid characters. External server:" + extServer.getServerid()
		    + ", Username:" + login + ", firstName:" + firstName + ", lastName:" + lastName);
	}

	// last name validation
	if (StringUtils.isNotBlank(lastName) && !ValidationUtil.isFirstLastNameValid(lastName)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "Last name contains invalid characters. External server:" + extServer.getServerid()
		    + ", Username:" + login + ", firstName:" + firstName + ", lastName:" + lastName);
	}

	// user email validation
	if (StringUtils.isNotBlank(email) && !ValidationUtil.isEmailValid(email)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "Email format is invalid. External server:" + extServer.getServerid() + ", Username:" + login
		    + ", firstName:" + firstName + ", lastName:" + lastName);
	}

	//set user's country to default value if it wasn't provided or has a wrong value
	String country = userData[7];
	if (StringUtils.isBlank(country) || !Arrays.asList(CommonConstants.COUNTRY_CODES).contains(country)) {
	    country = LanguageUtil.getDefaultCountry();
	}

	User user = new User();
	user.setLogin(login);
	user.setPassword(password);
	user.setSalt(salt);
	user.setTitle(userData[0]);
	user.setFirstName(userData[1]);
	user.setLastName(userData[2]);
	user.setAddressLine1(userData[3]);
	user.setCity(userData[4]);
	user.setState(userData[5]);
	user.setPostcode(userData[6]);
	user.setCountry(country);
	user.setDayPhone(userData[8]);
	user.setMobilePhone(userData[9]);
	user.setFax(userData[10]);
	user.setEmail(userData[11]);
	user.setAuthenticationMethod(
		(AuthenticationMethod) service.findById(AuthenticationMethod.class, AuthenticationMethod.DB));
	user.setCreateDate(new Date());
	user.setDisabledFlag(false);
	user.setLocale(LanguageUtil.getSupportedLocale(userData[13], userData[12]));
	user.setTimeZone(timezoneService.getServerTimezone().getTimezoneId());
	user.setTheme(service.getDefaultTheme());
	service.saveUser(user);
	ExtUserUseridMap extUserUseridMap = new ExtUserUseridMap();
	extUserUseridMap.setExtServer(extServer);
	extUserUseridMap.setExtUsername(extUsername);
	extUserUseridMap.setUser(user);
	service.save(extUserUseridMap);
	return extUserUseridMap;
    }

    private String[] getUserDataFromExtServer(ExtServer extServer, String extUsername) throws UserInfoFetchException {
	// the callback url must contain %username%, %timestamp% and %hash%
	// eg:
	// "http://test100.ics.mq.edu.au/webapps/lams-plglamscontent-bb_bb60/UserData?uid=%username%&ts=%timestamp%&hash=%hash%";
	// where %username%, %timestamp% and %hash% will be replaced with their real values
	try {
	    String userDataCallbackUrl = extServer.getUserinfoUrl();
	    String timestamp = Long.toString(new Date().getTime());
	    String hash = hash(extServer, extUsername, timestamp);

	    String encodedExtUsername = URLEncoder.encode(extUsername, "UTF8");

	    // set the values for the parameters
	    userDataCallbackUrl = userDataCallbackUrl.replaceAll("%username%", encodedExtUsername)
		    .replaceAll("%timestamp%", timestamp).replaceAll("%hash%", hash);
	    IntegrationService.log.debug(userDataCallbackUrl);
	    URL url = new URL(userDataCallbackUrl);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		throw new UserInfoFetchException("Fail to fetch user data from external server:"
			+ extServer.getServerid() + "- Invalid connection type");
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;
	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new UserInfoFetchException("Fail to fetch user data from external server:"
			+ extServer.getServerid() + " - Unexpected return HTTP Status:" + httpConn.getResponseCode());
	    }

	    InputStream is = url.openConnection().getInputStream();
	    BufferedReader isReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	    String str = isReader.readLine();
	    if (str == null) {
		throw new UserInfoFetchException("Fail to fetch user data from external server:"
			+ extServer.getServerid() + " - No data returned from external server");
	    }

	    return CSVUtil.parse(str);

	} catch (MalformedURLException e) {
	    IntegrationService.log.error(e);
	    throw new UserInfoFetchException(e);
	} catch (IOException e) {
	    IntegrationService.log.error(e);
	    throw new UserInfoFetchException(e);
	} catch (ParseException e) {
	    IntegrationService.log.error(e);
	    throw new UserInfoFetchException(e);
	}
    }

    @Override
    public boolean isIntegrationUser(Integer userId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("user.userId", userId);
	List list = service.findByProperties(ExtUserUseridMap.class, properties);
	return (list != null) && !list.isEmpty();
    }

    @Override
    public String hash(ExtServer extServer, String extUsername, String timestamp) {
	String serverId = extServer.getServerid();
	String serverKey = extServer.getServerkey();
	String plaintext = timestamp.trim().toLowerCase() + extUsername.trim().toLowerCase()
		+ serverId.trim().toLowerCase() + serverKey.trim().toLowerCase();
	return HashUtil.sha1(plaintext);
    }

    private String buildName(String prefix, String name) {
	return prefix + '_' + name;
    }

    @Override
    public List<ExtServer> getAllExtServers() {
	Map<String, Object> properties = new HashMap<>();
	properties.put("serverTypeId", ExtServer.INTEGRATION_SERVER_TYPE);
	return service.findByProperties(ExtServer.class, properties);
    }

    @Override
    public List<ExtServer> getAllToolConsumers() {
	Map<String, Object> properties = new HashMap<>();
	properties.put("serverTypeId", ExtServer.LTI_CONSUMER_SERVER_TYPE);
	return service.findByProperties(ExtServer.class, properties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ExtServerToolAdapterMap> getMappedServers(String toolSig) {

	Map<String, Object> properties = new HashMap<>();
	properties.put("tool.toolSignature", toolSig);
	return service.findByProperties(ExtServerToolAdapterMap.class, properties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExtServerToolAdapterMap getMappedServer(String serverId, String toolSig) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("tool.toolSignature", toolSig);
	properties.put("extServer.serverid", serverId);
	List ret = service.findByProperties(ExtServerToolAdapterMap.class, properties);
	if ((ret != null) && (ret.size() > 0)) {
	    return (ExtServerToolAdapterMap) ret.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void saveExtServerToolAdapterMap(ExtServerToolAdapterMap map) {
	service.save(map);
    }

    @Override
    public void deleteExtServerToolAdapterMap(ExtServerToolAdapterMap map) {
	service.delete(map);
    }

    @Override
    public void saveExtServer(ExtServer map) {
	service.save(map);
    }

    @Override
    public ExtServer getExtServer(Integer sid) {
	return (ExtServer) service.findById(ExtServer.class, sid);
    }

    @Override
    public void createExtServerLessonMap(Long lessonId, ExtServer extServer) {
	createExtServerLessonMap(lessonId, null, extServer);
	;
    }

    @Override
    public void createExtServerLessonMap(Long lessonId, String resourceLinkId, ExtServer extServer) {
	ExtServerLessonMap map = new ExtServerLessonMap();
	map.setLessonId(lessonId);
	map.setResourceLinkId(resourceLinkId);
	map.setExtServer(extServer);
	service.save(map);
    }

    @Override
    public String getLessonFinishCallbackUrl(User user, Lesson lesson) throws UnsupportedEncodingException {
	// the callback url must contain %username%, %lessonid%, %timestamp% and %hash% eg:
	// "http://server.com/lams--bb/UserData?uid=%username%&lessonid=%lessonid%&ts=%timestamp%&hash=%hash%";
	// where %username%, %lessonid%, %timestamp% and %hash% will be replaced with their real values
	String lessonFinishCallbackUrl = null;

	if (lesson != null) {
	    Long lessonId = lesson.getLessonId();
	    ExtServerLessonMap extServerLesson = getExtServerLessonMap(lessonId);
	    // checks whether the lesson was created from extServer and whether it has lessonFinishCallbackUrl setting
	    if (extServerLesson != null
		    && StringUtils.isNotBlank(extServerLesson.getExtServer().getLessonFinishUrl())) {
		ExtServer server = extServerLesson.getExtServer();

		ExtUserUseridMap extUser = getExtUserUseridMapByUserId(server, user.getUserId());
		if (extUser != null) {
		    String extUsername = extUser.getExtUsername();

		    //return URL in case of integration server
		    if (server.getServerTypeId().equals(ExtServer.INTEGRATION_SERVER_TYPE)) {
			// construct real lessonFinishCallbackUrl
			lessonFinishCallbackUrl = server.getLessonFinishUrl();
			String timestamp = Long.toString(new Date().getTime());
			String hash = hash(server, extUsername, timestamp);
			String encodedExtUsername = URLEncoder.encode(extUsername, "UTF8");

			// set the values for the parameters
			lessonFinishCallbackUrl = lessonFinishCallbackUrl.replaceAll("%username%", encodedExtUsername)
				.replaceAll("%lessonid%", lessonId.toString()).replaceAll("%timestamp%", timestamp)
				.replaceAll("%hash%", hash);
			log.debug(lessonFinishCallbackUrl);

			// in case of LTI Tool Consumer - create a new thread to report score back to LMS (in order to do this task in parallel not to slow down later work)
		    } else {

			// calculate lesson's MaxPossibleMark
			Long lessonMaxPossibleMark = toolService.getLessonMaxPossibleMark(lesson);
			GradebookUserLesson gradebookUserLesson = gradebookService.getGradebookUserLesson(lessonId,
				user.getUserId());
			Double userTotalMark = (gradebookUserLesson == null) || (gradebookUserLesson.getMark() == null)
				? null
				: gradebookUserLesson.getMark();

			final String lessonFinishUrl = server.getLessonFinishUrl();
			if (userTotalMark != null && StringUtils.isNotBlank(lessonFinishUrl)) {

			    Double score = lessonMaxPossibleMark.equals(0L) ? 0 : userTotalMark / lessonMaxPossibleMark;
			    final String scoreStr = (userTotalMark == null) || lessonMaxPossibleMark.equals(0L) ? ""
				    : score.toString();

			    final String serverKey = server.getServerid();
			    final String serverSecret = server.getServerkey();
			    final String tcGradebookId = extUser.getTcGradebookId();
			    final ExtUserUseridMap extUserFinal = extUser;

			    Thread preaddLearnersMonitorsThread = new Thread(new Runnable() {
				@Override
				public void run() {
				    //send Request directly
				    try {
					IMSPOXRequest.sendReplaceResult(lessonFinishUrl, serverKey, serverSecret,
						tcGradebookId, scoreStr);
				    } catch (IOException e) {
					throw new RuntimeException(e);
				    } catch (OAuthException e) {
					throw new RuntimeException(e);
				    } catch (GeneralSecurityException e) {
					throw new RuntimeException(e);
				    }
				    log.debug("Score (" + scoreStr + ") posted to Tool Consumer (serverKey:" + serverKey
					    + "). extUsername:" + extUserFinal.getExtUsername());
				}
			    }, "LAMS_sendScoresLTI_thread");
			    preaddLearnersMonitorsThread.start();

			}
		    }

		}
	    }
	}

	return lessonFinishCallbackUrl;
    }

    @Override
    public boolean isIntegratedServerGroupFetchingAvailable(Long lessonId) {

	boolean isIntegratedServerGroupFetchingAvailable = false;
	if (lessonId != null) {
	    ExtServerLessonMap extServerLesson = getExtServerLessonMap(lessonId);
	    isIntegratedServerGroupFetchingAvailable = (extServerLesson != null)
		    && StringUtils.isNotBlank(extServerLesson.getExtServer().getExtGroupsUrl());
	}

	return isIntegratedServerGroupFetchingAvailable;
    }

    @Override
    public List<ExtGroupDTO> getExtGroups(Long lessonId, String[] extGroupIds) throws Exception {
	// the callback url must contain %username%, %lessonid%, %timestamp% and %hash% eg:
	// "http://server.org/lams-bb/UserData?uid=%username%&lessonid=%lessonid%&ts=%timestamp%&hash=%hash%";
	// where %username%, %lessonid%, %timestamp% and %hash% will be replaced with their real values

	if (lessonId == null) {
	    throw new GroupInfoFetchException(
		    "Fail to fetch group data from external server:" + " specified lessonId is null");
	}

	Lesson lesson = (Lesson) service.findById(Lesson.class, lessonId);
	if (lesson == null) {
	    throw new GroupInfoFetchException(
		    "Fail to fetch group data from external server:" + " specified lesson is null");
	}
	Organisation organisation = lesson.getOrganisation();
	Integer organisationId = lesson.getOrganisation().getOrganisationId();

	ExtServerLessonMap extServerLesson = getExtServerLessonMap(lessonId);
	ExtCourseClassMap extCourse = getExtCourseClassMap(organisationId);

	// checks whether the lesson was created from extServer and whether it has lessonFinishCallbackUrl setting
	if (extServerLesson == null) {
	    throw new GroupInfoFetchException("Fail to fetch group data from external server:"
		    + " there is no corresponding ExtServerLessonMap for lessonId " + lessonId);
	}

	if (StringUtils.isBlank(extServerLesson.getExtServer().getExtGroupsUrl())) {
	    throw new GroupInfoFetchException("Fail to fetch group data from external server:"
		    + " corresponding ExtCourseClassMap doesn't have extGroupsUrl specified.");
	}

	if (extCourse == null) {
	    throw new GroupInfoFetchException("Fail to fetch group data from external server:"
		    + " there is no corresponding ExtCourseClassMap for lessonId " + lessonId);
	}

	ExtServer extServer = extServerLesson.getExtServer();

	// construct real lessonFinishCallbackUrl
	String lmsGroupsUrl = extServer.getExtGroupsUrl();

	//in case group info is also requested - provide selected groups' ids
	if (extGroupIds != null && extGroupIds.length > 0) {

	    if (!lmsGroupsUrl.contains("?")) {
		lmsGroupsUrl += "?";
	    }
	    String extGroupIdsParam = "";
	    for (String extGroupId : extGroupIds) {
		extGroupIdsParam += "&extGroupIds=" + extGroupId;
	    }
	    lmsGroupsUrl += extGroupIdsParam;
	}

	String timestamp = Long.toString(new Date().getTime());
	String hashUsername = "username";
	String hash = hash(extServer, hashUsername, timestamp);

	// set values for the parameters
	HashMap<String, String> params = new HashMap<>();
	params.put("uid", hashUsername);
	params.put("ts", timestamp);
	params.put("hash", hash);
	params.put("course_id", extCourse.getCourseid());

	// send the request to the external server
	InputStream is = WebUtil.getResponseInputStreamFromExternalServer(lmsGroupsUrl, params);
	BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	String str = isReader.readLine();

	JSONArray jsonGroups = new JSONArray(str);
	List<ExtGroupDTO> extGroups = new ArrayList<>();
	for (int i = 0; i < jsonGroups.length(); i++) {
	    JSONObject jsonGroup = jsonGroups.getJSONObject(i);
	    ExtGroupDTO group = new ExtGroupDTO();
	    group.setGroupName(jsonGroup.getString("groupName"));
	    group.setGroupId(jsonGroup.getString("groupId"));
	    extGroups.add(group);

	    // in case group info is also requested - provide selected groups' ids
	    if (extGroupIds != null && extGroupIds.length > 0) {
		ArrayList<User> users = new ArrayList<>();

		JSONArray jsonUsers = jsonGroup.getJSONArray("users");
		for (int j = 0; j < jsonUsers.length(); j++) {
		    JSONObject jsonUser = jsonUsers.getJSONObject(j);

		    String extUsername = jsonUser.getString("userName");

		    ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(extServer, extUsername);

		    //create extUserUseridMap if it's not available
		    if (extUserUseridMap == null) {
			// User properties list format: <Title>,<First name>,<Last name>,<Address>,<City>,<State>,
			// <Postcode>,<Country>,<Day time number>,<Mobile number>,<Fax number>,<Email>,<Locale
			// language>,<Locale country>
			String[] userData = new String[14];
			for (int k = 1; k <= 14; k++) {
			    String userProperty = jsonUser.getString("" + k);
			    userData[k - 1] = userProperty;
			}
			String salt = HashUtil.salt();
			String password = HashUtil.sha1(RandomPasswordGenerator.nextPassword(10));
			extUserUseridMap = createExtUserUseridMap(extServer, extUsername, password, salt, userData,
				true);
		    }
		    User user = extUserUseridMap.getUser();
		    Integer userId = extUserUseridMap.getUser().getUserId();

		    //add user to organisation if it's not there
		    updateUserRoles(user, organisation, false);

		    //check if user belong to the lesson. and if not - add it
		    if (!lesson.getLessonClass().getLearnersGroup().hasLearner(user)) {
			lessonService.addLearner(lessonId, userId);
		    }

		    users.add(user);
		}

		group.setUsers(users);
	    } else {
		group.setNumberUsers(jsonGroup.getInt("groupSize"));
	    }

	}

	return extGroups;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExtCourseClassMap getExtCourseClassMap(Integer sid, Long lessonId) {
	Lesson lesson = lessonService.getLesson(lessonId);
	Map<String, Object> properties = new HashMap<>();
	properties.put("extServer.sid", sid);
	properties.put("organisation.organisationId", lesson.getOrganisation().getOrganisationId());
	List<ExtCourseClassMap> list = service.findByProperties(ExtCourseClassMap.class, properties);
	return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public ExtServerLessonMap getLtiConsumerLesson(String serverId, String resourceLinkId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("extServer.serverid", serverId);
	properties.put("resourceLinkId", resourceLinkId);
	List<ExtServerLessonMap> list = service.findByProperties(ExtServerLessonMap.class, properties);

	return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    @Override
    public ExtServerLessonMap getExtServerLessonMap(Long lessonId) {
	List list = service.findByProperty(ExtServerLessonMap.class, "lessonId", lessonId);
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (ExtServerLessonMap) list.get(0);
	}
    }

    private ExtUserUseridMap getExtUserUseridMapByUserId(ExtServer extServer, Integer userId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("extServer.sid", extServer.getSid());
	properties.put("user.userId", userId);
	List list = service.findByProperties(ExtUserUseridMap.class, properties);
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (ExtUserUseridMap) list.get(0);
	}
    }

    private ExtCourseClassMap getExtCourseClassMap(Integer organisationId) {
	List list = service.findByProperty(ExtCourseClassMap.class, "organisation.organisationId", organisationId);
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (ExtCourseClassMap) list.get(0);
	}
    }
    
    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------

    public void setService(IUserManagementService service) {
	this.service = service;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public void setToolService(ILamsCoreToolService toolService) {
	this.toolService = toolService;
    }
    
    public void setTimezoneService(ITimezoneService timezoneService) {
	this.timezoneService = timezoneService;
    }

}