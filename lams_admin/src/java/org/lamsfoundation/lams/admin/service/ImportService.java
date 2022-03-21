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

package org.lamsfoundation.lams.admin.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * <p>
 * <a href="SpreadsheetService.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 * @author <a href="mailto:jliew@melcoe.mq.edu.au">Jun-Dir Liew</a>
 */
public class ImportService implements IImportService {

    private static Logger log = Logger.getLogger(ImportService.class);
    private IUserManagementService service;
    private MessageService messageService;
    private ILogEventService logEventService;
    private ITimezoneService timezoneService;

    // spreadsheet column indexes for user spreadsheet
    private static final short LOGIN = 0;
    private static final short PASSWORD = 1;
    private static final short TITLE = 2;
    private static final short FIRST_NAME = 3;
    private static final short LAST_NAME = 4;
    private static final short AUTH_METHOD = 5;
    private static final short EMAIL = 6;
    private static final short THEME = 7;
    private static final short LOCALE = 8;
    private static final short ADDRESS1 = 9;
    private static final short ADDRESS2 = 10;
    private static final short ADDRESS3 = 11;
    private static final short CITY = 12;
    private static final short STATE = 13;
    private static final short POSTCODE = 14;
    private static final short COUNTRY = 15;
    private static final short DAY_PHONE = 16;
    private static final short EVE_PHONE = 17;
    private static final short MOB_PHONE = 18;
    private static final short FAX = 19;

    // spreadsheet column indexes for userorgrole spreadsheet
    private static final short ORGANISATION = 1;
    private static final short ROLES = 2;

    // spreadsheet column indexes for groups spreadsheet
    private static final short NAME = 0;
    private static final short CODE = 1;
    private static final short DESCRIPTION = 2;
    private static final short ORGANISATION_STATE = 3;
    private static final short ADMIN_ADD_NEW_USERS = 4;
    private static final short ADMIN_BROWSE_ALL_USERS = 5;
    private static final short ADMIN_CHANGE_STATUS = 6;

    // class-wide variables
    ArrayList<ArrayList> results = new ArrayList<>();
    ArrayList<String> rowResult = new ArrayList<>();
    private boolean emptyRow;
    private boolean hasError;
    private Organisation parentOrg;

    private HSSFSheet getSheet(File fileItem) throws IOException {
	POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileItem));
	HSSFWorkbook wb = new HSSFWorkbook(fs);
	return wb.getSheetAt(0);
    }

    @Override
    public boolean isUserSpreadsheet(File fileItem) throws IOException {
	HSSFSheet sheet = getSheet(fileItem);
	HSSFRow row = sheet.getRow(sheet.getFirstRowNum());
	String string = parseStringCell(row.getCell(ImportService.PASSWORD));
	return (StringUtils.equals(string, "* password")) ? true : false;
    }

    @Override
    public boolean isRolesSpreadsheet(File fileItem) throws IOException {
	HSSFSheet sheet = getSheet(fileItem);
	HSSFRow row = sheet.getRow(sheet.getFirstRowNum());
	String string = parseStringCell(row.getCell(ImportService.ORGANISATION));
	return (StringUtils.equals(string, "* organisation")) ? true : false;
    }

    @Override
    public List parseSpreadsheet(File fileItem, String sessionId) throws IOException {
	if (isUserSpreadsheet(fileItem)) {
	    return parseUserSpreadsheet(fileItem, sessionId);
	} else if (isRolesSpreadsheet(fileItem)) {
	    return parseRolesSpreadsheet(fileItem, sessionId);
	}
	return new ArrayList();
    }

    // returns x size list where x is number of orgs.
    // each item in the list lists the id, name, and parent's id of that org; otherwise
    // the items in the list are error messages.
    @Override
    public List parseGroupSpreadsheet(File fileItem, String sessionId) throws IOException {
	results = new ArrayList<>();
	parentOrg = service.getRootOrganisation();
	HSSFSheet sheet = getSheet(fileItem);
	int startRow = sheet.getFirstRowNum();
	int endRow = sheet.getLastRowNum();
	UserDTO userDTO = (UserDTO) SessionManager.getSession(sessionId).getAttribute(AttributeNames.USER);

	ImportService.log.debug("Parsing spreadsheet rows " + startRow + " through " + endRow);

	HSSFRow row;
	Organisation org = null;
	int successful = 0;
	for (int i = startRow + 1; i < (endRow + 1); i++) {
	    emptyRow = true;
	    hasError = false;
	    rowResult = new ArrayList<>();
	    row = sheet.getRow(i);
	    if (row != null) {
		org = parseGroup(row, i);
	    }

	    // an empty row signifies a new group
	    if (emptyRow) {
		ImportService.log.debug("Row " + i + " is empty.");
		parentOrg = service.getRootOrganisation();
		continue;
	    }
	    if (hasError) {
		ImportService.log.debug("Row " + i + " has an error which has been sent to the browser.");
		results.add(rowResult);
		continue;
	    } else {
		org = service.saveOrganisation(org, getCurrentUserId());
		successful++;
		rowResult.add(org.getOrganisationId().toString());
		rowResult.add(org.getName());
		rowResult.add(org.getParentOrganisation().getOrganisationId().toString());
		rowResult.add(org.getOrganisationType().getOrganisationTypeId().toString());
		writeOrgAuditLog(org, userDTO);
		// if we just added a group, then the rows under it become it's subgroups
		if (parentOrg.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.ROOT_TYPE)) {
		    parentOrg = org;
		}
		results.add(rowResult);
	    }
	}
	ImportService.log.debug("Found " + results.size() + " orgs in spreadsheet.");
	writeSuccessAuditLog(successful, userDTO, "audit.successful.organisation.import");
	return results;
    }

    private Integer getCurrentUserId() {
	try {
	    UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	    return user.getUserID();
	} catch (Exception e) {
	    ImportService.log.error(e);
	}
	return null;
    }

    private Organisation parseGroup(HSSFRow row, int rowIndex) {
	Organisation org = new Organisation();
	String[] args = new String[1];

	String name = StringUtils.substring(parseStringCell(row.getCell(ImportService.NAME)), 0, 240);

	//validate organisation name
	if (StringUtils.isBlank(name)) {
	    rowResult.add(messageService.getMessage("error.name.required"));
	    hasError = true;
	    return null;

	} else if (!ValidationUtil.isOrgNameValid(name)) {
	    rowResult.add(messageService.getMessage("error.name.invalid.characters"));
	    hasError = true;
	    return null;
	}

	org.setName(name);
	org.setCode(parseStringCell(row.getCell(ImportService.CODE)));
	org.setDescription(parseStringCell(row.getCell(ImportService.DESCRIPTION)));

	String orgStateText = parseStringCell(row.getCell(ImportService.ORGANISATION_STATE));
	OrganisationState orgState = getOrganisationState(orgStateText);
	org.setOrganisationState(orgState);

	org.setOrganisationType((OrganisationType) service.findById(OrganisationType.class,
		parentOrg.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.ROOT_TYPE)
			? OrganisationType.COURSE_TYPE
			: OrganisationType.CLASS_TYPE));

	org.setParentOrganisation(parentOrg);
	org.setCourseAdminCanAddNewUsers(parseBooleanCell(row.getCell(ImportService.ADMIN_ADD_NEW_USERS)));
	org.setCourseAdminCanBrowseAllUsers(parseBooleanCell(row.getCell(ImportService.ADMIN_BROWSE_ALL_USERS)));
	org.setCourseAdminCanChangeStatusOfCourse(parseBooleanCell(row.getCell(ImportService.ADMIN_CHANGE_STATUS)));

	return (hasError ? null : org);
    }

    private boolean isIntegratedUser(List<String> integPrefixes, String login) {
	int underscore = login.indexOf('_');
	if (underscore >= 0) {
	    if (integPrefixes.contains(login.substring(0, underscore))) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public int getNumRows(File fileItem) throws IOException {
	HSSFSheet sheet = getSheet(fileItem);
	int startRow = sheet.getFirstRowNum();
	int endRow = sheet.getLastRowNum();
	return endRow - startRow;
    }

    @Override
    public List parseUserSpreadsheet(File fileItem, String sessionId) throws IOException {
	results = new ArrayList<>();
	HSSFSheet sheet = getSheet(fileItem);
	int startRow = sheet.getFirstRowNum();
	int endRow = sheet.getLastRowNum();

	setupImportStatus(sessionId, endRow - startRow);
	UserDTO userDTO = (UserDTO) SessionManager.getSession(sessionId).getAttribute(AttributeNames.USER);

	ImportService.log.debug("Parsing spreadsheet rows " + startRow + " through " + endRow);

	HSSFRow row;
	User user = null;
	int successful = 0;
	for (int i = startRow + 1; i < (endRow + 1); i++) {
	    emptyRow = true;
	    hasError = false;
	    rowResult = new ArrayList<>();
	    row = sheet.getRow(i);
	    user = parseUser(row, i);

	    if (emptyRow) {
		ImportService.log.debug("Row " + i + " is empty.");
		break;
	    }
	    if (hasError) {
		ImportService.log.debug("Row " + i + " has an error which has been sent to the browser.");
		results.add(rowResult);
		writeErrorsAuditLog(i + 1, rowResult, userDTO);
		updateImportStatus(sessionId, results.size());
		continue;
	    } else {
		try {
		    service.saveUser(user);
		    successful++;
		    writeAuditLog(user, userDTO);
		    ImportService.log.debug("Row " + i + " saved user: " + user.getLogin());
		} catch (Exception e) {
		    ImportService.log.debug(e);
		    rowResult.add(messageService.getMessage("error.fail.add"));
		}
		if (rowResult.size() > 0) {
		    if (ImportService.log.isDebugEnabled()) {
			ImportService.log.debug("Row " + i + " has " + rowResult.size() + " messages.");
		    }
		    writeErrorsAuditLog(i + 1, rowResult, userDTO);
		}
		results.add(rowResult);
		updateImportStatus(sessionId, results.size());
	    }
	}
	ImportService.log.debug("Found " + results.size() + " users in spreadsheet.");
	writeSuccessAuditLog(successful, userDTO, "audit.successful.user.import");
	return results;
    }

    // use session vars to update browser with import progress so page
    // won't timeout
    private void setupImportStatus(String sessionId, int importTotal) {
	HttpSession ss = SessionManager.getSession(sessionId);
	ss.setAttribute(IImportService.STATUS_IMPORT_TOTAL, importTotal);
	ss.setAttribute(IImportService.STATUS_IMPORTED, 0);
    }

    private void updateImportStatus(String sessionId, int imported) {
	HttpSession ss = SessionManager.getSession(sessionId);
	ss.removeAttribute(IImportService.STATUS_IMPORTED);
	ss.setAttribute(IImportService.STATUS_IMPORTED, imported);
    }

    @Override
    public List parseRolesSpreadsheet(File fileItem, String sessionId) throws IOException {
	results = new ArrayList<>();
	HSSFSheet sheet = getSheet(fileItem);
	int startRow = sheet.getFirstRowNum();
	int endRow = sheet.getLastRowNum();

	ImportService.log.debug("Parsing spreadsheet rows " + startRow + " through " + endRow);

	setupImportStatus(sessionId, endRow - startRow);
	UserDTO userDTO = (UserDTO) SessionManager.getSession(sessionId).getAttribute(AttributeNames.USER);

	HSSFRow row;
	List<String> roles;
	int successful = 0;
	for (int i = startRow + 1; i < (endRow + 1); i++) {
	    emptyRow = true;
	    hasError = false;
	    rowResult = new ArrayList<>();
	    row = sheet.getRow(i);

	    String login = parseStringCell(row.getCell(ImportService.LOGIN));
	    String orgId = parseStringCell(row.getCell(ImportService.ORGANISATION));
	    roles = parseRolesCell(row.getCell(ImportService.ROLES));

	    if (emptyRow) {
		ImportService.log.debug("Row " + i + " is empty.");
		break;
	    }
	    if (hasError) {
		ImportService.log.debug("Row " + i + " has an error which has been sent to the browser.");
		results.add(rowResult);
		writeErrorsAuditLog(i + 1, rowResult, userDTO);
		updateImportStatus(sessionId, results.size());
		continue;
	    } else {
		try {
		    saveUserRoles(isAppadmin(sessionId), login, orgId, roles, row);
		    successful++;
		} catch (Exception e) {
		    ImportService.log.error("Unable to assign roles to user: " + login, e);
		    rowResult.add(messageService.getMessage("error.fail.add"));
		}
		if (rowResult.size() > 0) {
		    if (ImportService.log.isDebugEnabled()) {
			ImportService.log.debug("Row " + i + " has " + rowResult.size() + " messages.");
		    }
		    writeErrorsAuditLog(i + 1, rowResult, userDTO);
		}
		results.add(rowResult);
		updateImportStatus(sessionId, results.size());
	    }
	}
	ImportService.log.debug("Found " + results.size() + " users in spreadsheet.");
	writeSuccessAuditLog(successful, userDTO, "audit.successful.role.import");
	return results;
    }

    private void setError(String message, String arg) {
	if (StringUtils.isBlank(arg)) {
	    rowResult.add(messageService.getMessage(message));
	} else {
	    String[] args = new String[1];
	    args[0] = arg;
	    rowResult.add(messageService.getMessage(message, args));
	}
	hasError = true;
    }

    // used when importing in a separate thread that doesn't have the user's DTO in session
    private boolean isAppadmin(String sessionId) {
	UserDTO userDTO = (UserDTO) SessionManager.getSession(sessionId).getAttribute(AttributeNames.USER);
	return service.isUserInRole(userDTO.getUserID(), service.getRootOrganisation().getOrganisationId(),
		Role.APPADMIN);
    }

    /*
     * user must already exist
     */
    private void saveUserRoles(boolean isAppadmin, String login, String orgId, List<String> roles, HSSFRow row) {
	User user = null;
	if (StringUtils.isNotBlank(login)) {
	    user = service.getUserByLogin(login);
	} else if (StringUtils.isBlank(login)) {
	    setError("error.login.required", "");
	}
	if (user == null) {
	    setError("error.user.does.not.exist", "(" + login + ")");
	}

	Organisation org = null;
	if (StringUtils.isNotBlank(orgId)) {
	    org = (Organisation) service.findById(Organisation.class, new Integer(orgId));
	}
	if (StringUtils.isBlank(orgId) || (org == null)) {
	    setError("error.org.invalid", "(" + orgId + ")");
	} else {
	    if ((roles == null) || !checkValidRoles(roles, isAppadmin, org.getOrganisationType())) {
		setError("error.roles.invalid", "(" + parseStringCell(row.getCell(ImportService.ROLES)) + ")");
	    }
	}

	if (!hasError) {
	    service.setRolesForUserOrganisation(user, org.getOrganisationId(), roles);
	    if (ImportService.log.isDebugEnabled()) {
		ImportService.log.debug("added: " + login + " to: " + org.getName() + " with roles: " + roles);
	    }
	}
    }

    /*
     * gathers error messages for each cell as required, unless it's the login field in which case, flags whole row as
     * empty.
     */
    private User parseUser(HSSFRow row, int rowIndex) {
	User user = new User();
	String[] args = new String[1];

	String login = parseStringCell(row.getCell(ImportService.LOGIN));
	// login validation
	if (StringUtils.isBlank(login)) {
	    rowResult.add(messageService.getMessage("error.login.required"));
	    hasError = true;
	    return null;

	} else if (!ValidationUtil.isUserNameValid(login)) {
	    rowResult.add(messageService.getMessage("error.username.invalid.characters"));
	    hasError = true;
	    return null;

	} else if (service.getUserByLogin(login) != null) {
	    args[0] = "(" + login + ")";
	    rowResult.add(messageService.getMessage("error.login.unique", args));
	    hasError = true;
	    return null;
	}
	user.setLogin(login);

	String password = parseStringCell(row.getCell(ImportService.PASSWORD));
	// password validation
	if (StringUtils.isBlank(password)) {
	    rowResult.add(messageService.getMessage("error.password.required"));
	    hasError = true;
	    return null;
	}
	if (!ValidationUtil.isPasswordValueValid(password, password)) {
	    rowResult.add(messageService.getMessage("label.password.restrictions"));
	    hasError = true;
	    return null;
	}

	user.setTitle(parseStringCell(row.getCell(ImportService.TITLE)));

	String firstName = parseStringCell(row.getCell(ImportService.FIRST_NAME));
	// first name validation
	if (StringUtils.isBlank(firstName)) {
	    rowResult.add(messageService.getMessage("error.firstname.required"));
	    hasError = true;

	} else if (!ValidationUtil.isFirstLastNameValid(firstName)) {
	    rowResult.add(messageService.getMessage("error.firstname.invalid.characters"));
	    hasError = true;

	} else {
	    user.setFirstName(firstName);
	}

	String lastName = parseStringCell(row.getCell(ImportService.LAST_NAME));
	//last name validation
	if (StringUtils.isBlank(lastName)) {
	    rowResult.add(messageService.getMessage("error.lastname.required"));
	    hasError = true;

	} else if (!ValidationUtil.isFirstLastNameValid(lastName)) {
	    rowResult.add(messageService.getMessage("error.lastname.invalid.characters"));
	    hasError = true;

	} else {
	    user.setLastName(lastName);
	}

	String authMethodName = parseStringCell(row.getCell(ImportService.AUTH_METHOD));
	AuthenticationMethod authMethod = getAuthMethod(authMethodName);
	//auth method validation
	if (authMethod == null) {
	    args[0] = "(" + authMethodName + ")";
	    rowResult.add(messageService.getMessage("error.authmethod.invalid", args));
	    hasError = true;

	} else {
	    user.setAuthenticationMethod(authMethod);
	}

	String email = parseStringCell(row.getCell(ImportService.EMAIL));
	//user email validation
	if (StringUtils.isBlank(email)) {
	    rowResult.add(messageService.getMessage("error.email.required"));
	    hasError = true;

	} else if (!ValidationUtil.isEmailValid(email)) {
	    rowResult.add(messageService.getMessage("error.valid.email.required"));
	    hasError = true;

	} else {
	    user.setEmail(email);
	}

	String themeId = parseStringCell(row.getCell(ImportService.THEME));
	Theme theme = getTheme(themeId);
	if (theme == null) {
	    args[0] = "(" + themeId + ")";
	    rowResult.add(messageService.getMessage("error.theme.invalid", args));
	    hasError = true;
	} else {
	    user.setTheme(theme);
	}

	String localeId = parseStringCell(row.getCell(ImportService.LOCALE));
	SupportedLocale locale = getLocale(localeId);
	if (locale == null) {
	    args[0] = "(" + localeId + ")";
	    rowResult.add(messageService.getMessage("error.locale.invalid", args));
	    hasError = true;
	} else {
	    user.setLocale(locale);
	}

	if (!ValidationUtil.isPasswordNotUserDetails(password, user)) {
	    rowResult.add(messageService.getMessage("label.password.restrictions"));
	    hasError = true;
	}

	if (hasError) {
	    return null;
	}

	user.setAddressLine1(parseStringCell(row.getCell(ImportService.ADDRESS1)));
	user.setAddressLine2(parseStringCell(row.getCell(ImportService.ADDRESS2)));
	user.setAddressLine3(parseStringCell(row.getCell(ImportService.ADDRESS3)));
	user.setCity(parseStringCell(row.getCell(ImportService.CITY)));
	user.setState(parseStringCell(row.getCell(ImportService.STATE)));
	user.setPostcode(parseStringCell(row.getCell(ImportService.POSTCODE)));
	String country = parseStringCell(row.getCell(ImportService.COUNTRY));
	user.setCountry(LanguageUtil.getSupportedCountry(country));
	user.setDayPhone(parseStringCell(row.getCell(ImportService.DAY_PHONE)));
	user.setEveningPhone(parseStringCell(row.getCell(ImportService.EVE_PHONE)));
	user.setMobilePhone(parseStringCell(row.getCell(ImportService.MOB_PHONE)));
	user.setFax(parseStringCell(row.getCell(ImportService.FAX)));
	user.setDisabledFlag(false);
	user.setCreateDate(new Date());
	user.setTimeZone(timezoneService.getServerTimezone().getTimezoneId());
	user.setFirstLogin(true);

	service.updatePassword(user, password);

	return user;
    }

    /*
     * the methods below return legible data from individual cells
     */
    private boolean parseBooleanCell(HSSFCell cell) {
	if (cell != null) {
	    String value;
	    try {
		cell.setCellType(CellType.STRING);
		if (cell.getStringCellValue() != null) {
		    if (cell.getStringCellValue().trim().length() != 0) {
			emptyRow = false;
		    }
		} else {
		    return false;
		}
		value = cell.getStringCellValue().trim();
	    } catch (Exception e) {
		cell.setCellType(CellType.NUMERIC);
		double d = cell.getNumericCellValue();
		emptyRow = false;
		value = new Long(new Double(d).longValue()).toString();
	    }
	    if (StringUtils.equals(value, "1") || StringUtils.equalsIgnoreCase(value, "true")) {
		return true;
	    }
	}
	return false;
    }

    private String parseStringCell(HSSFCell cell) {
	if (cell != null) {
	    try {
		cell.setCellType(CellType.STRING);
		if (cell.getStringCellValue() != null) {
		    if (cell.getStringCellValue().trim().length() != 0) {
			emptyRow = false;
		    }
		} else {
		    return null;
		}
		// log.debug("string cell value: '"+cell.getStringCellValue().trim()+"'");
		return cell.getStringCellValue().trim();
	    } catch (Exception e) {
		cell.setCellType(CellType.NUMERIC);
		double d = cell.getNumericCellValue();
		emptyRow = false;
		// log.debug("numeric cell value: '"+d+"'");
		return (new Long(new Double(d).longValue()).toString());
	    }
	}
	return null;
    }

    private AuthenticationMethod getAuthMethod(String authMethodName) {
	List list;
	if ((authMethodName == null) || (authMethodName == "")) {
	    return (AuthenticationMethod) service.findById(AuthenticationMethod.class, AuthenticationMethod.DB);
	} else {
	    try {
		Integer authMethodId = new Integer(authMethodName);
		return (AuthenticationMethod) service.findById(AuthenticationMethod.class, authMethodId);
	    } catch (NumberFormatException e) {
		list = service.findByProperty(AuthenticationMethod.class, "authenticationMethodName", authMethodName);
		return ((list == null) || list.isEmpty() ? null : (AuthenticationMethod) list.get(0));
	    }
	}
    }

    private List<String> parseRolesCell(HSSFCell cell) {
	String roleDescription = "";
	if (cell != null) {
	    try {
		cell.setCellType(CellType.STRING);
		if ((cell.getStringCellValue() != null) || (cell.getStringCellValue().trim().length() != 0)) {
		    emptyRow = false;
		} else {
		    ImportService.log
			    .debug("Couldn't find any roles in spreadsheet column index " + ImportService.ROLES);
		    return null;
		}
		roleDescription = cell.getStringCellValue().trim();
	    } catch (Exception e) {
		ImportService.log.error("Caught exception when reading roles in spreadsheet: " + e.getMessage());
		return null;
	    }
	    List<String> roles = new ArrayList<>();
	    int fromIndex = 0;
	    int index = roleDescription.indexOf(IImportService.SEPARATOR, fromIndex);
	    while (index != -1) {
		String role = addRoleId(roleDescription, fromIndex, index);
		ImportService.log.debug("Found role: " + role);
		if (role == null) {
		    return null;
		} else {
		    roles.add(role);
		}
		fromIndex = index + 1;
		index = roleDescription.indexOf(IImportService.SEPARATOR, fromIndex);
	    }
	    String role = addRoleId(roleDescription, fromIndex, roleDescription.length());
	    ImportService.log.debug("Found last role: " + role);
	    if (role == null) {
		return null;
	    } else {
		roles.add(role);
	    }
	    return roles;
	}
	return null;
    }

    // return id of role name in given role description
    private String addRoleId(String roleDescription, int fromIndex, int index) {
	List list = service.findByProperty(Role.class, "name", roleDescription.substring(fromIndex, index));
	Role role = ((list == null) || list.isEmpty() ? null : (Role) list.get(0));
	if (role != null) {
	    return role.getRoleId().toString();
	} else {
	    return null; // if we can't translate the name to a role, return null
	}
    }

    // return false if a role shouldn't be assigned in given org type
    private boolean checkValidRoles(List<String> idList, boolean isAppadmin, OrganisationType orgType) {
	// convert list of id's into list of Roles
	List<Role> roleList = new ArrayList<>();
	for (String id : idList) {
	    Role role = (Role) service.findById(Role.class, Integer.parseInt(id));
	    if (role != null) {
		roleList.add(role);
	    } else {
		return false;
	    }
	}

	// check they are valid
	List<Role> validRoles = service.filterRoles(roleList, isAppadmin, orgType);
	for (Role r : roleList) {
	    if (!validRoles.contains(r)) {
		return false;
	    }
	}
	return true;
    }

    // set Theme to default html theme if cell is empty
    private Theme getTheme(String themeId) {
	if ((themeId == null) || (themeId == "")) {
	    return service.getDefaultTheme();
	} else {
	    try {
		return (Theme) service.findById(Theme.class, new Long(themeId));
	    } catch (Exception e) {
		return null;
	    }
	}
    }

    // set locale to default system locale if cell is empty
    private SupportedLocale getLocale(String localeId) {
	if ((localeId == null) || (localeId == "")) {
	    return LanguageUtil.getDefaultLocale();
	} else {
	    try {
		return (SupportedLocale) service.findById(SupportedLocale.class, new Integer(localeId));
	    } catch (Exception e) {
		return null;
	    }
	}
    }

    // set organisation state to active if cell is empty
    private OrganisationState getOrganisationState(String orgStateText) {
	if (StringUtils.equals(orgStateText, "hidden")) {
	    return (OrganisationState) service.findById(OrganisationState.class, OrganisationState.HIDDEN);
	} else if (StringUtils.equals(orgStateText, "archived")) {
	    return (OrganisationState) service.findById(OrganisationState.class, OrganisationState.ARCHIVED);
	} else {
	    return (OrganisationState) service.findById(OrganisationState.class, OrganisationState.ACTIVE);
	}
    }

    private void writeAuditLog(User user, UserDTO userDTO) {
	String[] args = new String[2];
	args[0] = user.getLogin() + "(" + user.getUserId() + ")";
	args[1] = user.getFullName();
	String message = messageService.getMessage("audit.user.create", args);
	logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, userDTO != null ? userDTO.getUserID() : null,
		user.getUserId(), null, null, message);

    }

    private void writeOrgAuditLog(Organisation org, UserDTO userDTO) {
	String[] args = new String[2];
	args[0] = org.getName() + "(" + org.getOrganisationId() + ")";
	args[1] = org.getOrganisationType().getName();
	String message = messageService.getMessage("audit.organisation.create", args);
	logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, userDTO != null ? userDTO.getUserID() : null, null, null,
		null, message);
    }

    private void writeErrorsAuditLog(int row, List<String> list, UserDTO userDTO) {
	for (String s : list) {
	    writeErrorAuditLog(row, s, userDTO);
	}
    }

    private void writeErrorAuditLog(int row, String error, UserDTO userDTO) {
	String[] args = { Integer.toString(row), error };
	String message = messageService.getMessage("audit.spreadsheet.error", args);
	logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, userDTO != null ? userDTO.getUserID() : null, null, null,
		null, message);
    }

    private void writeSuccessAuditLog(int successful, UserDTO userDTO, String key) {
	String[] args = { Integer.toString(successful) };
	String message = messageService.getMessage(key, args);
	logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, userDTO != null ? userDTO.getUserID() : null, null, null,
		null, message);
    }

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------

    public void setService(IUserManagementService service) {
	this.service = service;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setTimezoneService(ITimezoneService timezoneService) {
	this.timezoneService = timezoneService;
    }
}
