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
package org.lamsfoundation.lams.admin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * TODO Add description here
 * TODO add existing users to an org?
 * TODO write audit log
 *
 * <p>
 * <a href="ExcelUserImportFileParser.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ExcelUserImportFileParser implements IUserImportFileParser{
	
	private static Logger log = Logger.getLogger(ExcelUserImportFileParser.class);
	private static IUserManagementService service;
	private static MessageService messageService;
	private static IAuditService auditService;

	// spreadsheet column indexes
	private static final short LOGIN = 0;
	private static final short PASSWORD = 1;
	private static final short TITLE = 2;
	private static final short FIRST_NAME = 3;
	private static final short LAST_NAME = 4;
	private static final short ORGANISATION = 5;
	private static final short ROLES = 6;
	private static final short AUTH_METHOD = 7;
	private static final short EMAIL = 8;
	private static final short FLASH_THEME = 9;
	private static final short HTML_THEME = 10;
	private static final short LOCALE = 11;
	private static final short ADDRESS1 = 12;
	private static final short ADDRESS2 = 13;
	private static final short ADDRESS3 = 14;
	private static final short CITY = 15;
	private static final short STATE = 16;
	private static final short POSTCODE = 17;
	private static final short COUNTRY = 18;
	private static final short DAY_PHONE = 19;
	private static final short EVE_PHONE = 20;
	private static final short MOB_PHONE = 21;
	private static final short FAX = 22;
	
	ArrayList<ArrayList> results = new ArrayList<ArrayList>();
	ArrayList<String> rowResult = new ArrayList<String>();
	private boolean emptyRow;
	private boolean hasError;
	
	public ExcelUserImportFileParser(IUserManagementService service, MessageService messageService, IAuditService auditService){
		this.service = service;
		this.messageService = messageService;
		this.auditService = auditService;
	}

	/** 
	 * @see org.lamsfoundation.lams.admin.util.IUserImportFileParser#parseUsersInOrganisation(FileItem fileItem, Organisation org, String adminLogin, boolean existingUsersOnly)
	 */
	public List parseSpreadsheet(FormFile fileItem) throws IOException{
		POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		int startRow = sheet.getFirstRowNum();
		int endRow = sheet.getLastRowNum();
		
		log.debug("sheet rows: "+startRow+".."+endRow);
		
		HSSFRow row;
		User user = null;
		Organisation org;
		List<String> roles;
		for (int i = startRow + 1; i < endRow + 1; i++) {
			log.debug("starting row: "+i);
			emptyRow = true;
			hasError = false;
			rowResult = new ArrayList<String>();
			org = null;
			roles = null;
			row = sheet.getRow(i);
			user = parseUser(row, i);
			String orgId = parseStringCell(row.getCell(ORGANISATION));
			if (orgId!=null) org = (Organisation)service.findById(Organisation.class, new Integer(orgId));
			roles = parseRolesCell(row.getCell(ROLES));
			
			if (emptyRow) {
				log.debug("emptyRow: "+emptyRow);
				break;
			}
			if (hasError) {
				log.debug("hasError: "+hasError);
				results.add(rowResult);
				continue;
			} else {
				try {
					user.setCreateDate(new Date());
					if (org!=null || roles!=null) {  // save user+roles if org or roles are set
						log.debug("org: "+org+" roles: "+roles);
						if (org!=null && roles!=null) {
							service.save(user);
							writeAuditLog(user);
							service.setRolesForUserOrganisation(user, org, roles);
							//rowResult.add(org.getOrganisationId().toString());  // for stat summary in save action
							log.debug("saved user: "+user.getUserId()+" with roles: "+roles);
						} else {
							String[] args = new String[1];
							String error;
							if (org!=null) {
								args[0] = "("+parseStringCell(row.getCell(ROLES))+")";
								error = messageService.getMessage("error.roles.invalid", args);
							} else {
								args[0] = "("+orgId+")";
								error = messageService.getMessage("error.org.invalid", args);
							}
							rowResult.add(error);
						}
					} else {
						service.save(user);
						writeAuditLog(user);
						//rowResult.add(String.valueOf(0));  // for stat summary in save action
						log.debug("saved user: "+user.getUserId());
					}
				} catch (Exception e) {
					rowResult.add(messageService.getMessage("error.fail.add"));
				}
				log.debug("rowResult size: "+rowResult.size());
				results.add(rowResult);
			}
		}
		log.debug("found "+results.size()+" users in spreadsheet.");
		return results;
	}
	
	/**
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

		user.setDisabledFlag(false);
		user.setLogin(login);
		
		String password = HashUtil.sha1(parseStringCell(row.getCell(PASSWORD)));
		user.setPassword(password);
		
		user.setTitle(parseStringCell(row.getCell(TITLE)));
		
		String fname = parseStringCell(row.getCell(FIRST_NAME));
		if (fname==null || fname=="") {
			rowResult.add(messageService.getMessage("error.firstname.required"));
			hasError = true;
		}
		user.setFirstName(fname);
		
		String lname = parseStringCell(row.getCell(LAST_NAME));
		if (lname==null || lname=="") {
			rowResult.add(messageService.getMessage("error.lastname.required"));
			hasError = true;
		}
		user.setLastName(lname);
		
		String authMethodName = parseStringCell(row.getCell(AUTH_METHOD));
		AuthenticationMethod authMethod = getAuthMethod(authMethodName);
		if (authMethod==null) {
			args[0] = "("+authMethodName+")";
			rowResult.add(messageService.getMessage("error.authmethod.invalid", args));
			hasError = true;
		}
		user.setAuthenticationMethod(authMethod);
		
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
		}
		user.setEmail(email);
		
		String flashId = parseStringCell(row.getCell(FLASH_THEME));
		CSSThemeVisualElement flashTheme = getFlashTheme(flashId);
		if (flashTheme==null) {
			args[0] = "("+flashId+")";
			rowResult.add(messageService.getMessage("error.flash.theme.invalid", args));
			hasError = true;
		}
		user.setFlashTheme(flashTheme);

		String htmlId = parseStringCell(row.getCell(HTML_THEME));
		CSSThemeVisualElement htmlTheme = getHtmlTheme(htmlId);
		if (htmlTheme==null) {
			args[0] = "("+htmlId+")";
			rowResult.add(messageService.getMessage("error.html.theme.invalid", args));
			hasError = true;
		}
		user.setHtmlTheme(htmlTheme);
		
		String localeId = parseStringCell(row.getCell(LOCALE));
		SupportedLocale locale = getLocale(localeId);
		if (locale==null) {
			args[0] = "("+localeId+")";
			rowResult.add(messageService.getMessage("error.locale.invalid", args));
			hasError = true;
		}
		user.setLocale(locale);
		
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
		return (hasError ? null : user);
	}
	
	/*
	 * the methods below return legible data from individual cells
	 * only specify msgIndex if you want to retrieve an error message, otherwise, use -1
	 */
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
				log.debug("string cell value: "+cell.getStringCellValue().trim());
				return cell.getStringCellValue().trim();
			} catch(Exception e) {
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				double d = cell.getNumericCellValue();
				emptyRow = false;
				log.debug("numeric cell value: "+d);
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
				List list = service.findByProperty(Role.class, "name", roleDescription.substring(fromIndex, index));
				Role role = (list==null || list.isEmpty() ? null : (Role)list.get(0));
				if (role!=null) {
					roles.add(role.getRoleId().toString());
					log.debug("role: "+role.getName());
				} else {
					return null;		// if we can't translate the name to a role, return null
				}
				fromIndex = index + 1;
				index = roleDescription.indexOf(SEPARATOR, fromIndex);
			}
			log.debug("using rolee name: "+roleDescription.substring(fromIndex, roleDescription.length()));
			List list = service.findByProperty(Role.class, "name", roleDescription.substring(fromIndex, roleDescription.length()));
			Role role = (list==null || list.isEmpty() ? null : (Role)list.get(0));
			if (role!=null) {
				roles.add(role.getRoleId().toString());
				log.debug("rolee: "+role.getName());
			} else {
				return null;
			}
			return roles;
		}
		return null;
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
				return (SupportedLocale)service.findById(SupportedLocale.class, new Byte(localeId));
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	private void writeAuditLog(User user) {
		String[] args = new String[2];
		args[0] = user.getLogin()+"("+user.getUserId()+")";
		args[1] = user.getFullName();
		String message = messageService.getMessage("audit.user.create", args);
		auditService.log(AdminConstants.MODULE_NAME, message);
	}
	
}
