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
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerLessonMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.lesson.Lesson;
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
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.ValidationUtil;

/**
 * <p>
 * <a href="IntegrationService.java.html"><i>View Source</i><a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class IntegrationService implements IIntegrationService {

    private static Logger log = Logger.getLogger(IntegrationService.class);

    private IUserManagementService service;

    public IUserManagementService getService() {
	return service;
    }

    public void setService(IUserManagementService service) {
	this.service = service;
    }

    @Override
    public ExtServerOrgMap getExtServerOrgMap(String serverId) {
	List list = service.findByProperty(ExtServerOrgMap.class, "serverid", serverId);
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (ExtServerOrgMap) list.get(0);
	}
    }

    @Override
    public ExtCourseClassMap getExtCourseClassMap(Integer extServerOrgMapId, String extCourseId) {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("courseid", extCourseId);
	properties.put("extServerOrgMap.sid", extServerOrgMapId);
	List<ExtCourseClassMap> list = service.findByProperties(ExtCourseClassMap.class, properties);

	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    // wrapper method for compatibility with original integration modules
    @Override
    public ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap,
	    String extCourseId, String countryIsoCode, String langIsoCode, String prettyCourseName, String method,
	    Boolean prefix) throws UserInfoValidationException {

	// Set the pretty course name if available, otherwise maintain the extCourseId
	String courseName = "";
	if (prettyCourseName != null) {
	    courseName = prettyCourseName;
	} else {
	    courseName = extCourseId;
	}
	if (StringUtils.equals(method, LoginRequestDispatcher.METHOD_AUTHOR)
		|| StringUtils.equals(method, LoginRequestDispatcher.METHOD_MONITOR)) {
	    return getExtCourseClassMap(serverMap, userMap, extCourseId, courseName, countryIsoCode, langIsoCode,
		    service.getRootOrganisation().getOrganisationId().toString(), true, prefix);
	} else {
	    return getExtCourseClassMap(serverMap, userMap, extCourseId, courseName, countryIsoCode, langIsoCode,
		    service.getRootOrganisation().getOrganisationId().toString(), false, prefix);
	}
    }

    // wrapper method for compatibility with original integration modules
    @Override
    public ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap,
	    String extCourseId, String countryIsoCode, String langIsoCode, String prettyCourseName, String method)
		    throws UserInfoValidationException {
	return getExtCourseClassMap(serverMap, userMap, extCourseId, countryIsoCode, langIsoCode, prettyCourseName,
		method, true);
    }

    // newer method which accepts course name, a parent org id, a flag for whether user should get
    // 'teacher' roles, and a flag for whether to use a prefix in the org's name
    @Override
    public ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap,
	    String extCourseId, String extCourseName, String countryIsoCode, String langIsoCode, String parentOrgId,
	    Boolean isTeacher, Boolean prefix) throws UserInfoValidationException {
	Organisation org;
	User user = userMap.getUser();

	ExtCourseClassMap extCourseClassMap = getExtCourseClassMap(serverMap.getSid(), extCourseId);
	if (extCourseClassMap == null) {
	    //create new ExtCourseClassMap

	    org = createOrganisation(serverMap, user, extCourseId, extCourseName, countryIsoCode, langIsoCode,
		    parentOrgId, prefix);
	    extCourseClassMap = new ExtCourseClassMap();
	    extCourseClassMap.setCourseid(extCourseId);
	    extCourseClassMap.setExtServerOrgMap(serverMap);
	    extCourseClassMap.setOrganisation(org);
	    service.save(extCourseClassMap);

	} else {
	    org = extCourseClassMap.getOrganisation();

	    // update external course name if if has changed
	    String requestedCourseName = prefix ? buildName(serverMap.getPrefix(), extCourseName) : extCourseName;
	    if ((extCourseName != null) && !org.getName().equals(requestedCourseName)) {

		// validate org name
		if (!ValidationUtil.isOrgNameValid(requestedCourseName)) {
		    throw new UserInfoValidationException("Can't create organisation due to validation error: "
			    + "organisation name cannot contain any of these characters < > ^ * @ % $. External server:"
			    + serverMap.getServerid() + ", orgId:" + extCourseId + ", orgName:" + requestedCourseName);
		}

		org.setName(requestedCourseName);
		service.updateOrganisationandWorkspaceNames(org);
	    }
	}

	updateUserRoles(user, org, isTeacher);

	return extCourseClassMap;
    }

    private void updateUserRoles(User user, Organisation org, Boolean isTeacher) {

	//create UserOrganisation if it doesn't exist
	UserOrganisation uo = service.getUserOrganisation(user.getUserId(), org.getOrganisationId());
	if (uo == null) {
	    uo = new UserOrganisation(user, org);
	    service.save(uo);
	    user.addUserOrganisation(uo);
	    service.save(user);
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
    public List<ExtUserUseridMap> getExtUserUseridMapByServerMap(ExtServerOrgMap serverMap) {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("extServerOrgMap.sid", serverMap.getSid());
	List list = service.findByProperties(ExtUserUseridMap.class, properties);
	return list;
    }

    @Override
    public ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, boolean prefix)
	    throws UserInfoFetchException, UserInfoValidationException {
	ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(serverMap, extUsername);

	if (extUserUseridMap == null) {
	    String[] userData = getUserDataFromExtServer(serverMap, extUsername);
	    String salt = HashUtil.salt();
	    String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);
	    return createExtUserUseridMap(serverMap, extUsername, password, salt, userData, prefix);
	} else {
	    return extUserUseridMap;
	}
    }

    @Override
    public ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername)
	    throws UserInfoFetchException, UserInfoValidationException {
	return getExtUserUseridMap(serverMap, extUsername, true);
    }

    @Override
    public ExtUserUseridMap getExistingExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername) {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("extServerOrgMap.sid", serverMap.getSid());
	properties.put("extUsername", extUsername);
	List<ExtUserUseridMap> list = service.findByProperties(ExtUserUseridMap.class, properties);

	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public ExtUserUseridMap getImplicitExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, String password,
	    String salt, String firstName, String lastName, String email) throws UserInfoValidationException {
	ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(serverMap, extUsername);

	if (extUserUseridMap == null) {
	    String[] defaultLangCountry = LanguageUtil.getDefaultLangCountry();
	    String[] userData = { "", firstName, lastName, "", "", "", "", "", "", "", "", email, defaultLangCountry[1],
		    defaultLangCountry[0] };
	    return createExtUserUseridMap(serverMap, extUsername, password, salt, userData, false);
	} else {
	    return extUserUseridMap;
	}
    }

    @Override
    public ExtUserUseridMap getImplicitExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, String firstName,
	    String lastName, String language, String country, String email, boolean prefix, boolean isUpdateUserDetails)
		    throws UserInfoValidationException {

	ExtUserUseridMap extUserUseridMap = getExistingExtUserUseridMap(serverMap, extUsername);

	//create new one if it doesn't exist yet 
	if (extUserUseridMap == null) {
	    String[] userData = { "", firstName, lastName, "", "", "", "", "", "", "", "", email, country, language };
	    String salt = HashUtil.salt();
	    String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);
	    return createExtUserUseridMap(serverMap, extUsername, password, salt, userData, prefix);

	    //update user details if it's required
	} else if (isUpdateUserDetails) {

	    User user = extUserUseridMap.getUser();

	    // first name validation
	    if (StringUtils.isNotBlank(firstName) && !ValidationUtil.isFirstLastNameValid(firstName)) {
		throw new UserInfoValidationException("Can't update user details due to validation error: "
			+ "First name contains invalid characters. External server:" + serverMap.getServerid()
			+ ", Username:" + user.getLogin() + ", firstName:" + firstName + ", lastName:" + lastName);
	    }
	    // last name validation
	    if (StringUtils.isNotBlank(lastName) && !ValidationUtil.isFirstLastNameValid(lastName)) {
		throw new UserInfoValidationException("Can't update user details due to validation error: "
			+ "Last name contains invalid characters. External server:" + serverMap.getServerid()
			+ ", Username:" + user.getLogin() + ", firstName:" + firstName + ", lastName:" + lastName);
	    }
	    // user email validation
	    if (StringUtils.isNotBlank(email) && !ValidationUtil.isEmailValid(email)) {
		throw new UserInfoValidationException("Can't update user details due to validation error: "
			+ "Email format is invalid. External server:" + serverMap.getServerid() + ", Username:"
			+ user.getLogin() + ", firstName:" + firstName + ", lastName:" + lastName);
	    }

	    user.setFirstName(firstName);
	    user.setLastName(lastName);
	    user.setEmail(email);
	    user.setModifiedDate(new Date());
	    user.setLocale(LanguageUtil.getSupportedLocale(language, country));
	    service.save(user);

	    return extUserUseridMap;

	    //simply return existing one
	} else {
	    return extUserUseridMap;
	}
    }

    private Organisation createOrganisation(ExtServerOrgMap serverMap, User user, String extCourseId,
	    String extCourseName, String countryIsoCode, String langIsoCode, String parentOrgId, Boolean prefix)
		    throws UserInfoValidationException {

	Organisation org = new Organisation();

	// org name validation
	String orgName = prefix ? buildName(serverMap.getPrefix(), extCourseName) : extCourseName;
	if (StringUtils.isNotBlank(orgName) && !ValidationUtil.isOrgNameValid(orgName)) {
	    throw new UserInfoValidationException("Can't create organisation due to validation error: "
		    + "organisation name cannot contain any of these characters < > ^ * @ % $. External server:"
		    + serverMap.getServerid() + ", orgId:" + extCourseId + ", orgName:" + orgName);
	}
	org.setName(orgName);

	org.setDescription(extCourseId);
	org.setOrganisationState(
		(OrganisationState) service.findById(OrganisationState.class, OrganisationState.ACTIVE));
	org.setLocale(LanguageUtil.getSupportedLocale(langIsoCode, countryIsoCode));

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
    private ExtUserUseridMap createExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, String password,
	    String salt, String[] userData, boolean prefix) throws UserInfoValidationException {

	String login = prefix ? buildName(serverMap.getPrefix(), extUsername) : extUsername;
	String firstName = userData[1];
	String lastName = userData[2];
	String email = userData[11];

	// login validation
	if (StringUtils.isBlank(login)) {
	    throw new UserInfoValidationException(
		    "Can't create user due to validation error: " + "Username cannot be blank. External server:"
			    + serverMap.getServerid() + ", firstName:" + firstName + ", lastName:" + lastName);
	} else if (!ValidationUtil.isUserNameValid(login)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "Username can only contain alphanumeric characters and no spaces. External server:"
		    + serverMap.getServerid() + ", Username:" + login);
	}

	// first name validation
	if (StringUtils.isNotBlank(firstName) && !ValidationUtil.isFirstLastNameValid(firstName)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "First name contains invalid characters. External server:" + serverMap.getServerid()
		    + ", Username:" + login + ", firstName:" + firstName + ", lastName:" + lastName);
	}

	// last name validation
	if (StringUtils.isNotBlank(lastName) && !ValidationUtil.isFirstLastNameValid(lastName)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "Last name contains invalid characters. External server:" + serverMap.getServerid()
		    + ", Username:" + login + ", firstName:" + firstName + ", lastName:" + lastName);
	}

	// user email validation
	if (StringUtils.isNotBlank(email) && !ValidationUtil.isEmailValid(email)) {
	    throw new UserInfoValidationException("Can't create user due to validation error: "
		    + "Email format is invalid. External server:" + serverMap.getServerid() + ", Username:" + login
		    + ", firstName:" + firstName + ", lastName:" + lastName);
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
	user.setCountry(userData[7]);
	user.setDayPhone(userData[8]);
	user.setMobilePhone(userData[9]);
	user.setFax(userData[10]);
	user.setEmail(userData[11]);
	user.setAuthenticationMethod(
		(AuthenticationMethod) service.findById(AuthenticationMethod.class, AuthenticationMethod.DB));
	user.setCreateDate(new Date());
	user.setDisabledFlag(false);
	user.setLocale(LanguageUtil.getSupportedLocale(userData[13], userData[12]));
	user.setFlashTheme(service.getDefaultFlashTheme());
	user.setHtmlTheme(service.getDefaultHtmlTheme());
	service.save(user);
	ExtUserUseridMap map = new ExtUserUseridMap();
	map.setExtServerOrgMap(serverMap);
	map.setExtUsername(extUsername);
	map.setUser(user);
	service.save(map);
	return map;
    }

    private String[] getUserDataFromExtServer(ExtServerOrgMap serverMap, String extUsername)
	    throws UserInfoFetchException {
	// the callback url must contain %username%, %timestamp% and %hash%
	// eg:
	// "http://test100.ics.mq.edu.au/webapps/lams-plglamscontent-bb_bb60/UserData?uid=%username%&ts=%timestamp%&hash=%hash%";
	// where %username%, %timestamp% and %hash% will be replaced with their real values
	try {
	    String userDataCallbackUrl = serverMap.getUserinfoUrl();
	    String timestamp = Long.toString(new Date().getTime());
	    String hash = hash(serverMap, extUsername, timestamp);

	    String encodedExtUsername = URLEncoder.encode(extUsername, "UTF8");

	    // set the values for the parameters
	    userDataCallbackUrl = userDataCallbackUrl.replaceAll("%username%", encodedExtUsername)
		    .replaceAll("%timestamp%", timestamp).replaceAll("%hash%", hash);
	    IntegrationService.log.debug(userDataCallbackUrl);
	    URL url = new URL(userDataCallbackUrl);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		throw new UserInfoFetchException("Fail to fetch user data from external server:"
			+ serverMap.getServerid() + "- Invalid connection type");
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;
	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new UserInfoFetchException("Fail to fetch user data from external server:"
			+ serverMap.getServerid() + " - Unexpected return HTTP Status:" + httpConn.getResponseCode());
	    }

	    InputStream is = url.openConnection().getInputStream();
	    BufferedReader isReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	    String str = isReader.readLine();
	    if (str == null) {
		throw new UserInfoFetchException("Fail to fetch user data from external server:"
			+ serverMap.getServerid() + " - No data returned from external server");
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
    public String hash(ExtServerOrgMap serverMap, String extUsername, String timestamp) {
	String serverId = serverMap.getServerid();
	String serverKey = serverMap.getServerkey();
	String plaintext = timestamp.trim().toLowerCase() + extUsername.trim().toLowerCase()
		+ serverId.trim().toLowerCase() + serverKey.trim().toLowerCase();
	return HashUtil.sha1(plaintext);
    }

    private String buildName(String prefix, String name) {
	return prefix + '_' + name;
    }

    @Override
    public List getAllExtServerOrgMaps() {
	return service.findAll(ExtServerOrgMap.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ExtServerToolAdapterMap> getMappedServers(String toolSig) {

	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("tool.toolSignature", toolSig);
	return service.findByProperties(ExtServerToolAdapterMap.class, properties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExtServerToolAdapterMap getMappedServer(String serverId, String toolSig) {
	Map<String, Object> properties = new HashMap<String, Object>();
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
    public void saveExtServerOrgMap(ExtServerOrgMap map) {
	service.save(map);
    }

    @Override
    public ExtServerOrgMap getExtServerOrgMap(Integer sid) {
	return (ExtServerOrgMap) service.findById(ExtServerOrgMap.class, sid);
    }

    public void createExtServerLessonMap(Long lessonId, ExtServerOrgMap extServer) {
	ExtServerLessonMap map = new ExtServerLessonMap();
	map.setLessonId(lessonId);
	map.setExtServer(extServer);
	service.save(map);
    }

    @Override
    public String getLessonFinishCallbackUrl(User user, Lesson lesson) throws UnsupportedEncodingException {
	// the callback url must contain %username%, %lessonid%, %timestamp% and %hash% eg:
	// "http://test100.ics.mq.edu.au/webapps/lams-plglamscontent-bb_bb60/UserData?uid=%username%&lessonid=%lessonid%&ts=%timestamp%&hash=%hash%";
	// where %username%, %lessonid%, %timestamp% and %hash% will be replaced with their real values
	String lessonFinishCallbackUrl = null;

	if (lesson != null) {
	    Long lessonId = lesson.getLessonId();
	    ExtServerLessonMap extServerLesson = getExtServerLessonMap(lessonId);
	    // checks whether the lesson was created from extServer and whether it has lessonFinishCallbackUrl setting
	    if ((extServerLesson != null)
		    && StringUtils.isNotBlank(extServerLesson.getExtServer().getLessonFinishUrl())) {
		ExtServerOrgMap serverMap = extServerLesson.getExtServer();

		ExtUserUseridMap extUserUseridMap = getExtUserUseridMapByUserId(serverMap, user.getUserId());
		if (extUserUseridMap != null) {
		    String extUsername = extUserUseridMap.getExtUsername();

		    // construct real lessonFinishCallbackUrl
		    lessonFinishCallbackUrl = serverMap.getLessonFinishUrl();
		    String timestamp = Long.toString(new Date().getTime());
		    String hash = hash(serverMap, extUsername, timestamp);
		    String encodedExtUsername = URLEncoder.encode(extUsername, "UTF8");

		    // set the values for the parameters
		    lessonFinishCallbackUrl = lessonFinishCallbackUrl.replaceAll("%username%", encodedExtUsername)
			    .replaceAll("%lessonid%", lessonId.toString()).replaceAll("%timestamp%", timestamp)
			    .replaceAll("%hash%", hash);
		    IntegrationService.log.debug(lessonFinishCallbackUrl);
		}
	    }
	}

	return lessonFinishCallbackUrl;
    }

    private ExtServerLessonMap getExtServerLessonMap(Long lessonId) {
	List list = service.findByProperty(ExtServerLessonMap.class, "lessonId", lessonId);
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (ExtServerLessonMap) list.get(0);
	}
    }

    private ExtUserUseridMap getExtUserUseridMapByUserId(ExtServerOrgMap serverMap, Integer userId) {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("extServerOrgMap.sid", serverMap.getSid());
	properties.put("user.userId", userId);
	List list = service.findByProperties(ExtUserUseridMap.class, properties);
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (ExtUserUseridMap) list.get(0);
	}
    }
}