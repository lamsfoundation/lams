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
package org.lamsfoundation.lams.admin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.admin.web.dto.V1OrgRightDTO;
import org.lamsfoundation.lams.admin.web.dto.V1OrganisationDTO;
import org.lamsfoundation.lams.admin.web.dto.V1UserDTO;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
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
	public IUserManagementService service;
	public MessageService messageService;
	public IAuditService auditService;
	
	public IUserManagementService getService() {
		return service;
	}

	public void setService(IUserManagementService service) {
		this.service = service;
	}
	
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	public IAuditService getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}
	
	// spreadsheet column indexes for user spreadsheet
	private static final short LOGIN = 0;
	private static final short PASSWORD = 1;
	private static final short TITLE = 2;
	private static final short FIRST_NAME = 3;
	private static final short LAST_NAME = 4;
	private static final short AUTH_METHOD = 5;
	private static final short EMAIL = 6;
	private static final short FLASH_THEME = 7;
	private static final short HTML_THEME = 8;
	private static final short LOCALE = 9;
	private static final short ADDRESS1 = 10;
	private static final short ADDRESS2 = 11;
	private static final short ADDRESS3 = 12;
	private static final short CITY = 13;
	private static final short STATE = 14;
	private static final short POSTCODE = 15;
	private static final short COUNTRY = 16;
	private static final short DAY_PHONE = 17;
	private static final short EVE_PHONE = 18;
	private static final short MOB_PHONE = 19;
	private static final short FAX = 20;
	
	// spreadsheet column indexes for userorgrole spreadsheet
	private static final short ORGANISATION = 1;
	private static final short ROLES = 2;
	
	// spreadsheet column indexes for groups spreadsheet
	private static final short NAME = 0;
	private static final short CODE = 1;
	private static final short DESCRIPTION = 2;
	private static final short LOCALE_ID = 3;
	private static final short ORGANISATION_STATE = 4;
	private static final short ADMIN_ADD_NEW_USERS = 5;
	private static final short ADMIN_BROWSE_ALL_USERS = 6;
	private static final short ADMIN_CHANGE_STATUS = 7;
	
	// class-wide variables
	ArrayList<ArrayList> results = new ArrayList<ArrayList>();
	ArrayList<String> rowResult = new ArrayList<String>();
	private boolean emptyRow;
	private boolean hasError;
	private Organisation parentOrg;

	private HSSFSheet getSheet(FormFile fileItem) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		return wb.getSheetAt(0);
	}
	
	public boolean isUserSpreadsheet(FormFile fileItem) throws IOException {
		HSSFSheet sheet = getSheet(fileItem);
		HSSFRow row = sheet.getRow(sheet.getFirstRowNum());
		String string = parseStringCell(row.getCell(PASSWORD));
		return (StringUtils.equals(string, "* password")) ? true : false;
	}
	
	public boolean isRolesSpreadsheet(FormFile fileItem) throws IOException {
		HSSFSheet sheet = getSheet(fileItem);
		HSSFRow row = sheet.getRow(sheet.getFirstRowNum());
		String string = parseStringCell(row.getCell(ORGANISATION));
		return (StringUtils.equals(string, "* organisation")) ? true : false;
	}
	
	public List parseSpreadsheet(FormFile fileItem, String sessionId) throws IOException {
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
	public List parseGroupSpreadsheet(FormFile fileItem) throws IOException {
		results = new ArrayList<ArrayList>();
		parentOrg = service.getRootOrganisation();
		HSSFSheet sheet = getSheet(fileItem);
		int startRow = sheet.getFirstRowNum();
		int endRow = sheet.getLastRowNum();
		
		log.debug("sheet rows: "+startRow+".."+endRow);
		
		HSSFRow row;
		Organisation org = null;
		for (int i = startRow + 1; i < endRow + 1; i++) {
			log.debug("starting row: "+i);
			emptyRow = true;
			hasError = false;
			rowResult = new ArrayList<String>();
			row = sheet.getRow(i);
			if (row != null) {
				org = parseGroup(row, i);
			}
			
			// an empty row signifies a new group
			if (emptyRow) {
				log.debug("emptyRow: "+emptyRow);
				parentOrg = service.getRootOrganisation();
				continue;
			}
			if (hasError) {
				log.debug("hasError: "+hasError);
				results.add(rowResult);
				continue;
			} else {
				org = service.saveOrganisation(org, getCurrentUserId());
				rowResult.add(org.getOrganisationId().toString());
				rowResult.add(org.getName());
				rowResult.add(org.getParentOrganisation().getOrganisationId().toString());
				rowResult.add(org.getOrganisationType().getOrganisationTypeId().toString());
				writeOrgAuditLog(org);
				// if we just added a group, then the rows under it become it's subgroups
				if (parentOrg.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.ROOT_TYPE)) {
					parentOrg = org;
				}
				results.add(rowResult);
			}
		}
		log.debug("found "+results.size()+" orgs in spreadsheet.");
		return results;
	}
	
	private Integer getCurrentUserId() {
		try {
			UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
			return user.getUserID();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	private Organisation parseGroup(HSSFRow row, int rowIndex) {
		Organisation org = new Organisation();
		String[] args = new String[1];
		
		String name = parseStringCell(row.getCell(NAME));
		if (name==null || name=="") {
			rowResult.add(messageService.getMessage("error.name.required"));
			hasError = true;
			return null;
		}
		org.setName(name);
		org.setCode(parseStringCell(row.getCell(CODE)));
		org.setDescription(parseStringCell(row.getCell(DESCRIPTION)));
		
		String localeId = parseStringCell(row.getCell(LOCALE_ID));
		SupportedLocale locale = getLocale(localeId);
		if (locale==null) {
			args[0] = "("+localeId+")";
			rowResult.add(messageService.getMessage("error.locale.invalid", args));
			hasError = true;
		} else {
			org.setLocale(locale);
		}
		
		String orgStateText = parseStringCell(row.getCell(ORGANISATION_STATE));
		OrganisationState orgState = getOrganisationState(orgStateText);
		org.setOrganisationState(orgState);

		org.setOrganisationType((OrganisationType)service.findById(OrganisationType.class,
				parentOrg.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.ROOT_TYPE)
				? OrganisationType.COURSE_TYPE : OrganisationType.CLASS_TYPE));

		org.setParentOrganisation(parentOrg);
		org.setCourseAdminCanAddNewUsers(parseBooleanCell(row.getCell(ADMIN_ADD_NEW_USERS)));
		org.setCourseAdminCanBrowseAllUsers(parseBooleanCell(row.getCell(ADMIN_BROWSE_ALL_USERS)));
		org.setCourseAdminCanChangeStatusOfCourse(parseBooleanCell(row.getCell(ADMIN_CHANGE_STATUS)));
		
		return (hasError ? null : org);
	}
	
	
	public List<List> parseV1UsersFile(FormFile fileItem, boolean includeIntegrated) throws IOException {
		ArrayList<V1UserDTO> users = new ArrayList<V1UserDTO>();
		ArrayList<V1OrganisationDTO> orgs = new ArrayList<V1OrganisationDTO>();
		ArrayList<List> results = new ArrayList<List>();
		ArrayList<String> integPrefixes = new ArrayList<String>();
		ArrayList<String> integOrgid = new ArrayList<String>();		
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream()));
		
		// get username prefixes, for integrations on the lams 1 server
		String line = reader.readLine();
		while (!line.startsWith("login\tpassword")) {
			if (!line.startsWith("prefix")) {
				String[] lineArray = line.split("\t");
				if (lineArray.length > 0) {
					integPrefixes.add(lineArray[0]);
				}
				if (lineArray.length > 1) {
					integOrgid.add(lineArray[1]);
				}
			}
			line = reader.readLine();
		}
		
		// get user details
		// login, password, fname, lname, email
		line = reader.readLine();  // skip line containing column headings
		while (!line.startsWith("sid\tname")) {
			String[] userDetails = line.split("\t");
			line = reader.readLine();
			if (!includeIntegrated && isIntegratedUser(integPrefixes, userDetails[0])) {
				continue;
			}
			V1UserDTO userDTO = new V1UserDTO(
					userDetails[0], userDetails[1], userDetails[2], userDetails[3]);
			if (userDetails.length > 4 && !StringUtils.equals(userDetails[4], "NULL")) {
				userDTO.setEmail(userDetails[4]);
			}
			users.add(userDTO);
		}
		
		// get organisations
		// sid, name, description, account_organisation
		line = reader.readLine();
		while (!line.startsWith("login\tg")) {
			String[] orgDetails = line.split("\t");
			line = reader.readLine();
			if (orgDetails.length != 4) {
				log.debug("LAMS 1 text file has troublesome organisation: ");
				for (int i=0; i<orgDetails.length; i++) {
					log.debug("column: "+orgDetails[i]);
				}
				continue;
			}
			if (!includeIntegrated) {
				if (integOrgid.contains(orgDetails[0])) {
					continue;
				}
			}
			V1OrganisationDTO org = new V1OrganisationDTO(
					orgDetails[0], orgDetails[1], orgDetails[2], orgDetails[3]);
			orgs.add(org);
		}
		
		// gather user roles which are 1 role per user per line, into a dto of roles for the user
		// login, role id
		line = reader.readLine();
		ArrayList<String> currentRoles = new ArrayList<String>();
		String currentLogin = "";
		while (!line.startsWith("login\tsid")) {
			String[] userRole = line.split("\t");
			line = reader.readLine();
			if (!includeIntegrated && isIntegratedUser(integPrefixes, userRole[0])) {
				continue;
			}
			if (!StringUtils.equals(userRole[0], currentLogin)) {
				if (!currentRoles.isEmpty()) {
					int index = users.indexOf(new V1UserDTO(currentLogin));
					V1UserDTO userDTO = users.get(index);
					userDTO.setRoleIds(new ArrayList<String>(currentRoles));
					users.set(index, userDTO);
				}
				currentLogin = userRole[0];
				currentRoles.clear();
			}
			currentRoles.add(userRole[1]);
		}
		int index = users.indexOf(new V1UserDTO(currentLogin));
		V1UserDTO userDTO = users.get(index);
		userDTO.setRoleIds(new ArrayList<String>(currentRoles));
		users.set(index, userDTO);
		
		// get user rights
		// login, org id, right id
		line = reader.readLine();
		while (line!=null) {
			String[] userRight = line.split("\t");
			line = reader.readLine();
			if (!includeIntegrated && isIntegratedUser(integPrefixes, userRight[0])) {
				continue;
			}
			V1OrgRightDTO orgRightDTO = new V1OrgRightDTO(userRight[1], userRight[2]);
			index = users.indexOf(new V1UserDTO(userRight[0]));
			userDTO = users.get(index);
			List<V1OrgRightDTO> orgRights = userDTO.getOrgRights();
			if (orgRights==null) {
				orgRights = new ArrayList<V1OrgRightDTO>();
			}
			orgRights.add(orgRightDTO);
			userDTO.setOrgRights(orgRights);
			users.set(index, userDTO);
		}
		
		results.add(users);
		results.add(orgs);
		
		return results;
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
	
	public int getNumRows(FormFile fileItem) throws IOException {
		HSSFSheet sheet = getSheet(fileItem);
		int startRow = sheet.getFirstRowNum();
		int endRow = sheet.getLastRowNum();
		return endRow - startRow;
	}
	
	public List parseUserSpreadsheet(FormFile fileItem, String sessionId) throws IOException {
		results = new ArrayList<ArrayList>();
		HSSFSheet sheet = getSheet(fileItem);
		int startRow = sheet.getFirstRowNum();
		int endRow = sheet.getLastRowNum();
		
		setupImportStatus(sessionId, endRow-startRow);
		UserDTO userDTO = (UserDTO)SessionManager.getSession(sessionId).getAttribute(AttributeNames.USER);
		
		log.debug("sheet rows: "+startRow+".."+endRow);
		
		HSSFRow row;
		User user = null;
		for (int i = startRow + 1; i < endRow + 1; i++) {
			log.debug("starting row: "+i);
			emptyRow = true;
			hasError = false;
			rowResult = new ArrayList<String>();
			row = sheet.getRow(i);
			user = parseUser(row, i);
			
			if (emptyRow) {
				log.debug("emptyRow: "+emptyRow);
				break;
			}
			if (hasError) {
				log.debug("hasError: "+hasError);
				results.add(rowResult);
				updateImportStatus(sessionId, results.size());
				continue;
			} else {
				try {
					service.save(user);
					writeAuditLog(user, userDTO);
					log.debug("saved user: "+user.getUserId());
				} catch (Exception e) {
					log.debug(e);
					rowResult.add(messageService.getMessage("error.fail.add"));
				}
				log.debug("rowResult size: "+rowResult.size());
				results.add(rowResult);
				updateImportStatus(sessionId, results.size());
			}
		}
		log.debug("found "+results.size()+" users in spreadsheet.");
		return results;
	}
	
	// use session vars to update browser with import progress so page
	// won't timeout
	private void setupImportStatus(String sessionId, int importTotal) {
		HttpSession ss = SessionManager.getSession(sessionId);
		ss.setAttribute(STATUS_IMPORT_TOTAL, importTotal);
		ss.setAttribute(STATUS_IMPORTED, 0);
	}
	
	private void updateImportStatus(String sessionId, int imported) {
		HttpSession ss = SessionManager.getSession(sessionId);
		ss.removeAttribute(STATUS_IMPORTED);
		ss.setAttribute(STATUS_IMPORTED, imported);
	}
	
	public List parseRolesSpreadsheet(FormFile fileItem, String sessionId) throws IOException {
		results = new ArrayList<ArrayList>();
		HSSFSheet sheet = getSheet(fileItem);
		int startRow = sheet.getFirstRowNum();
		int endRow = sheet.getLastRowNum();
		
		log.debug("sheet rows: "+startRow+".."+endRow);
		
		setupImportStatus(sessionId, endRow-startRow);
		
		HSSFRow row;
		List<String> roles;
		for (int i = startRow + 1; i < endRow + 1; i++) {
			log.debug("starting row: "+i);
			emptyRow = true;
			hasError = false;
			rowResult = new ArrayList<String>();
			row = sheet.getRow(i);
			
			String login = parseStringCell(row.getCell(LOGIN));
			String orgId = parseStringCell(row.getCell(ORGANISATION));
			roles = parseRolesCell(row.getCell(ROLES));
			
			if (emptyRow) {
				log.debug("emptyRow: "+emptyRow);
				break;
			}
			if (hasError) {
				log.debug("hasError: "+hasError);
				results.add(rowResult);
				updateImportStatus(sessionId, results.size());
				continue;
			} else {
				try {
					saveUserRoles(isSysadmin(sessionId), login, orgId, roles, row);
				} catch (Exception e) {
					log.error("Unable to assign roles to user: "+login, e);
					rowResult.add(messageService.getMessage("error.fail.add"));
				}
				log.debug("rowResult size: "+rowResult.size());
				results.add(rowResult);
				updateImportStatus(sessionId, results.size());
			}
		}
		log.debug("found "+results.size()+" users in spreadsheet.");
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
	private boolean isSysadmin(String sessionId) {
		UserDTO userDTO = (UserDTO)SessionManager.getSession(sessionId).getAttribute(AttributeNames.USER);
		return service.isUserInRole(userDTO.getUserID(), service.getRootOrganisation().getOrganisationId(), Role.SYSADMIN);
	}
	
	/*
	 * user must already exist
	 */
	private void saveUserRoles(boolean isSysadmin, String login, String orgId, List<String> roles, HSSFRow row) {
		User user = null;
		if (StringUtils.isNotBlank(login)) {
			user = service.getUserByLogin(login);
		} else if (StringUtils.isBlank(login)) {
			setError("error.login.required", "");
		}
		if (user==null) {
			setError("error.user.does.not.exist", "("+login+")");
		}
		
		Organisation org = null;
		if (StringUtils.isNotBlank(orgId)) {
			org = (Organisation)service.findById(Organisation.class, new Integer(orgId));
		}
		if (StringUtils.isBlank(orgId) || org==null){
			setError("error.org.invalid", "("+orgId+")");
		} else {
			if (roles==null || !checkValidRoles(roles, isSysadmin, org.getOrganisationType())) {
				setError("error.roles.invalid", "("+parseStringCell(row.getCell(ROLES))+")");
			}
		}
		
		if (!hasError) {
			service.setRolesForUserOrganisation(user, org, roles);
			log.debug("added: "+login+" to: "+org.getName()+" with roles: "+roles);
		}
	}
	
	/*
	 * gathers error messages for each cell as required, unless it's the login field in which case,
	 * flags whole row as empty.
	 */
	private User parseUser(HSSFRow row, int rowIndex) {
		User user = new User();
		String[] args = new String[1];
		
		String login = parseStringCell(row.getCell(LOGIN));
		if (login==null || login=="") {
			rowResult.add(messageService.getMessage("error.login.required"));
			hasError = true;
			return null;
		} else if (service.getUserByLogin(login)!=null) {
			args[0] = "("+login+")";
			rowResult.add(messageService.getMessage("error.login.unique", args));
			hasError = true;
			return null;
		}

		user.setLogin(login);
		
		String password = HashUtil.sha1(parseStringCell(row.getCell(PASSWORD)));
		user.setPassword(password);
		
		user.setTitle(parseStringCell(row.getCell(TITLE)));
		
		String fname = parseStringCell(row.getCell(FIRST_NAME));
		if (fname==null || fname=="") {
			rowResult.add(messageService.getMessage("error.firstname.required"));
			hasError = true;
		} else {
			user.setFirstName(fname);
		}
		
		String lname = parseStringCell(row.getCell(LAST_NAME));
		if (lname==null || lname=="") {
			rowResult.add(messageService.getMessage("error.lastname.required"));
			hasError = true;
		} else {
			user.setLastName(lname);
		}
		
		String authMethodName = parseStringCell(row.getCell(AUTH_METHOD));
		AuthenticationMethod authMethod = getAuthMethod(authMethodName);
		if (authMethod==null) {
			args[0] = "("+authMethodName+")";
			rowResult.add(messageService.getMessage("error.authmethod.invalid", args));
			hasError = true;
		} else {
			user.setAuthenticationMethod(authMethod);
		}
		
		String email = parseStringCell(row.getCell(EMAIL));
		if (email==null || email=="") {
			rowResult.add(messageService.getMessage("error.email.required"));
			hasError = true;
		} else {
			Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher m = p.matcher(email);
			if (!m.matches()) {
				rowResult.add(messageService.getMessage("error.valid.email.required"));
				hasError = true;
			}
			user.setEmail(email);
		}
		
		String flashId = parseStringCell(row.getCell(FLASH_THEME));
		CSSThemeVisualElement flashTheme = getFlashTheme(flashId);
		if (flashTheme==null) {
			args[0] = "("+flashId+")";
			rowResult.add(messageService.getMessage("error.flash.theme.invalid", args));
			hasError = true;
		} else {
			user.setFlashTheme(flashTheme);
		}

		String htmlId = parseStringCell(row.getCell(HTML_THEME));
		CSSThemeVisualElement htmlTheme = getHtmlTheme(htmlId);
		if (htmlTheme==null) {
			args[0] = "("+htmlId+")";
			rowResult.add(messageService.getMessage("error.html.theme.invalid", args));
			hasError = true;
		} else {
			user.setHtmlTheme(htmlTheme);
		}
		
		String localeId = parseStringCell(row.getCell(LOCALE));
		SupportedLocale locale = getLocale(localeId);
		if (locale==null) {
			args[0] = "("+localeId+")";
			rowResult.add(messageService.getMessage("error.locale.invalid", args));
			hasError = true;
		} else {
			user.setLocale(locale);
		}
		
		user.setAddressLine1(parseStringCell(row.getCell(ADDRESS1)));
		user.setAddressLine2(parseStringCell(row.getCell(ADDRESS2)));
		user.setAddressLine3(parseStringCell(row.getCell(ADDRESS3)));
		user.setCity(parseStringCell(row.getCell(CITY)));
		user.setState(parseStringCell(row.getCell(STATE)));
		user.setPostcode(parseStringCell(row.getCell(POSTCODE)));
		user.setCountry(parseStringCell(row.getCell(COUNTRY)));
		user.setDayPhone(parseStringCell(row.getCell(DAY_PHONE)));
		user.setEveningPhone(parseStringCell(row.getCell(EVE_PHONE)));
		user.setMobilePhone(parseStringCell(row.getCell(MOB_PHONE)));
		user.setFax(parseStringCell(row.getCell(FAX)));
		user.setDisabledFlag(false);
		user.setCreateDate(new Date());
		
		return (hasError ? null : user);
	}
	
	/*
	 * the methods below return legible data from individual cells
	 */
	private boolean parseBooleanCell(HSSFCell cell){
		if (cell!=null) {
			String value;
			try {
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if (cell.getStringCellValue()!= null) {
					if (cell.getStringCellValue().trim().length()!= 0) {
						emptyRow = false;
					}
				} else {
					return false;
				}
				value = cell.getStringCellValue().trim();
			} catch(Exception e) {
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
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
	
	private String parseStringCell(HSSFCell cell){
		if (cell!=null) {
			try {
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if (cell.getStringCellValue()!= null) {
					if (cell.getStringCellValue().trim().length()!= 0) {
						emptyRow = false;
					}
				} else {
					return null;
				}
				log.debug("string cell value: '"+cell.getStringCellValue().trim()+"'");
				return cell.getStringCellValue().trim();
			} catch(Exception e) {
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				double d = cell.getNumericCellValue();
				emptyRow = false;
				log.debug("numeric cell value: '"+d+"'");
				return (new Long(new Double(d).longValue()).toString());
			}
		}
		return null;
	}
	
	private AuthenticationMethod getAuthMethod(String authMethodName){
		List list;
		if (authMethodName==null || authMethodName=="") {
			return (AuthenticationMethod)service.findById(AuthenticationMethod.class, AuthenticationMethod.DB);
		} else {
			list = service.findByProperty(AuthenticationMethod.class, "authenticationMethodName", authMethodName);
			return (list==null || list.isEmpty() ? null : (AuthenticationMethod)list.get(0));
		}
	}
	
	private List<String> parseRolesCell(HSSFCell cell)
	{
		String roleDescription = "";
		if (cell!=null) {
			try {
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if (cell.getStringCellValue()!= null || cell.getStringCellValue().trim().length()!= 0) {
					emptyRow = false;
				} else {
					log.debug("role cell is null");
					return null;
				}
				roleDescription = cell.getStringCellValue().trim();
			} catch(Exception e) {
				log.debug("role cell exception");
				return null;
			}
			List<String> roles = new ArrayList<String>();
			int fromIndex = 0;
			int index = roleDescription.indexOf(SEPARATOR, fromIndex);
			while (index != -1) {
				log.debug("using role name: "+roleDescription.substring(fromIndex, index));
				String role = addRoleId(roleDescription, fromIndex, index);
				if (role==null) {
					return null;
				} else {
					roles.add(role);
				}
				fromIndex = index + 1;
				index = roleDescription.indexOf(SEPARATOR, fromIndex);
			}
			log.debug("using rolee name: "+roleDescription.substring(fromIndex, roleDescription.length()));
			String role = addRoleId(roleDescription, fromIndex, roleDescription.length());
			if (role==null) {
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
		Role role = (list==null || list.isEmpty() ? null : (Role)list.get(0));
		if (role!=null) {
			log.debug("role: "+role.getName());
			return role.getRoleId().toString();
		} else {
			return null;		// if we can't translate the name to a role, return null
		}
	}
	
	// return false if a role shouldn't be assigned in given org type
	private boolean checkValidRoles(List<String> idList, boolean isSysadmin, OrganisationType orgType) {
		// convert list of id's into list of Roles
		List<Role> roleList = new ArrayList<Role>();
		for (String id : idList) {
			Role role = (Role)service.findById(Role.class, Integer.parseInt(id));
			if (role!=null) {
				roleList.add(role);
			} else {
				return false;
			}
		}
		
		// check they are valid
		List<Role> validRoles = service.filterRoles(roleList, isSysadmin, orgType);
		for (Role r : roleList) {
			if (!validRoles.contains(r)) {
				return false;
			}
		}
		return true;
	}

	// set CSSThemeVisualElement to default flash theme if cell is empty
	private CSSThemeVisualElement getFlashTheme(String flashId){
		if (flashId==null || flashId=="") {
			String flashName = Configuration.get(ConfigurationKeys.DEFAULT_FLASH_THEME);
			List list = service.findByProperty(CSSThemeVisualElement.class, "name", flashName);
			return (list != null && list.size() > 0) ? (CSSThemeVisualElement) list.get(0) : null;
		} else {
			try {
				return (CSSThemeVisualElement)service.findById(CSSThemeVisualElement.class, new Long(flashId));
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	// set CSSThemeVisualElement to default html theme if cell is empty
	private CSSThemeVisualElement getHtmlTheme(String htmlId){
		if (htmlId==null || htmlId=="") {
			String htmlName = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
			List list = service.findByProperty(CSSThemeVisualElement.class, "name", htmlName);
			return (list != null && list.size() > 0) ? (CSSThemeVisualElement) list.get(0) : null;
		} else {
			try {
				return (CSSThemeVisualElement)service.findById(CSSThemeVisualElement.class, new Long(htmlId));
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	// set locale to default system locale if cell is empty
	private SupportedLocale getLocale(String localeId){
		if (localeId==null || localeId=="") {
			String defaultLocale = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
			return service.getSupportedLocale(defaultLocale.substring(0, 2), defaultLocale.substring(3));
		} else {
			try {
				return (SupportedLocale)service.findById(SupportedLocale.class, new Integer(localeId));
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	// set organisation state to active if cell is empty
	private OrganisationState getOrganisationState(String orgStateText) {
		if (StringUtils.equals(orgStateText, "hidden")) {
			return (OrganisationState)service.findById(OrganisationState.class, OrganisationState.HIDDEN);
		} else if (StringUtils.equals(orgStateText, "archived")) {
			return (OrganisationState)service.findById(OrganisationState.class, OrganisationState.ARCHIVED);
		} else {
			return (OrganisationState)service.findById(OrganisationState.class, OrganisationState.ACTIVE);
		}
	}
	
	private void writeAuditLog(User user, UserDTO userDTO) {
		String[] args = new String[2];
		args[0] = user.getLogin()+"("+user.getUserId()+")";
		args[1] = user.getFullName();
		String message = messageService.getMessage("audit.user.create", args);
		auditService.log(userDTO, AdminConstants.MODULE_NAME, message);
	}
	
	private void writeOrgAuditLog(Organisation org) {
		String[] args = new String[2];
		args[0] = org.getName()+"("+org.getOrganisationId()+")";
		args[1] = org.getOrganisationType().getName();
		String message = messageService.getMessage("audit.organisation.create", args);
		auditService.log(AdminConstants.MODULE_NAME, message);
	}
}
