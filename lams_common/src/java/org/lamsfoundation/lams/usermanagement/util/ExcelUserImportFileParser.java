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

package org.lamsfoundation.lams.usermanagement.util;

/**
 * <p>
 * <a href="ExcelUserImportFileParser.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ExcelUserImportFileParser implements IUserImportFileParser {

/*
 * start of definition of property keys used in the excel file
 * 
 * private static final short LOGIN = 0;
 * private static final short PASSWORD = 1;
 * private static final short AUTH_METHOD = 2;
 * private static final short ROLE = 3;
 * private static final short STATUS = 4;
 * private static final short TITLE = 5;
 * private static final short FIRST_NAME = 6;
 * private static final short LAST_NAME = 7;
 * private static final short EMAIL = 8;
 * private static final short ADDRESS1 = 9;
 * private static final short ADDRESS2 = 10;
 * private static final short ADDRESS3 = 11;
 * private static final short CITY = 12;
 * private static final short STATE = 13;
 * private static final short COUNTRY = 14;
 * private static final short DAY_PHONE = 15;
 * private static final short EVE_PHONE = 16;
 * private static final short MOB_PHONE = 17;
 * private static final short FAX = 18;
 * 
 * 
 * private static String[] errMsgArray =
 * new String[]{LOGIN_REQUIRED,PASSWORD_REQUIRED,AUTH_METHOD_REQUIRED,ROLE_REQUIRED};
 * //end of definition
 * 
 * private boolean emptyRow;
 * private boolean hasError;
 * private String errMsgForRow;
 * private IUserManagementService service;
 * 
 * public ExcelUserImportFileParser(IUserManagementService service){
 * this.service = service;
 * }
 * 
 * /**
 * 
 * @see org.lamsfoundation.lams.usermanagement.util.IUserImportFileParser#parseUsersInOrganisation(FileItem fileItem,
 * Organisation org, String adminLogin, boolean existingUsersOnly)
 * 
 * public String parseUsersInOrganisation(FileItem fileItem, Organisation org, String adminLogin, boolean
 * existingUsersOnly) throws IOException{
 * String errorMessage = "";
 * POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
 * HSSFWorkbook wb = new HSSFWorkbook(fs);
 * HSSFSheet sheet = wb.getSheetAt(0);
 * int startRow = sheet.getFirstRowNum();
 * int endRow = sheet.getLastRowNum();
 * int count = 0;
 * Double dbl;
 * Long l;
 * HSSFCell cell = null;
 * for (int i = startRow + 1; i < endRow + 1; i++)
 * {
 * emptyRow = true;
 * hasError = false;
 * errMsgForRow = "";
 * HSSFRow row = sheet.getRow(i);
 * User user = new User();
 * Set roles = null;
 * 
 * user.setLogin(parseStringCell(row.getCell(LOGIN),i,LOGIN));
 * if(existingUsersOnly){//if existingUsersOnly, ignore all the other columns
 * user = service.getUserByLogin(user.getLogin());
 * if(user==null)
 * {
 * errMsgForRow = errMsgForRow + ROW + i + SKIP + parseStringCell(row.getCell(LOGIN),i,LOGIN) + USER_NOT_EXIST ;
 * hasError = true;
 * }
 * else{//check if the admin is authorised
 * List userOrgs = service.getUserOrganisationsForUser(user);
 * Iterator iter = userOrgs.iterator();
 * boolean userOrgExisted = false;
 * boolean authorised = false;
 * while(iter.hasNext()){
 * UserOrganisation userOrg = (UserOrganisation)iter.next();
 * if(userOrg.getOrganisation().getOrganisationId().equals(org.getOrganisationId())){
 * userOrgExisted = true;
 * break;
 * }
 * User admin = service.getUserByLogin(adminLogin);
 * Iterator iter2 = service.getUserOrganisationsForUser(admin).iterator();
 * while(iter2.hasNext()){
 * UserOrganisation adminOrg = (UserOrganisation)iter2.next();
 * if(adminOrg.getOrganisation().getOrganisationId().equals(userOrg.getOrganisation().getOrganisationId())){
 * authorised = true;
 * break;
 * }
 * }
 * if(authorised)
 * break;
 * }
 * if(!authorised)
 * {
 * errMsgForRow = errMsgForRow + ROW + i + SKIP + parseStringCell(row.getCell(LOGIN),i,LOGIN) + NO_AUTHORISATION;
 * hasError = true;
 * }
 * if(userOrgExisted){
 * errMsgForRow = errMsgForRow + ROW + i + SKIP + parseStringCell(row.getCell(LOGIN),i,LOGIN) + MEMBERSHIP_EXIST +
 * org.getName();
 * hasError = true;
 * }
 * roles = parseRoleCell(row.getCell(ROLE),i,ROLE);
 * }
 * }else{
 * user.setPassword(parseStringCell(row.getCell(PASSWORD),i,PASSWORD));
 * user.setAuthenticationMethod(parseAuthMethodCell(row.getCell(AUTH_METHOD),i,AUTH_METHOD));
 * roles = parseRoleCell(row.getCell(ROLE),i,ROLE);
 * user.setDisabledFlag(parseStatusCell(row.getCell(STATUS),i));
 * user.setTitle(parseStringCell(row.getCell(TITLE),i,-1));
 * user.setFirstName(parseStringCell(row.getCell(FIRST_NAME),i,-1));
 * user.setLastName(parseStringCell(row.getCell(LAST_NAME),i,-1));
 * user.setEmail(parseStringCell(row.getCell(EMAIL),i,-1));
 * user.setAddressLine1(parseStringCell(row.getCell(ADDRESS1),i,-1));
 * user.setAddressLine2(parseStringCell(row.getCell(ADDRESS2),i,-1));
 * user.setAddressLine3(parseStringCell(row.getCell(ADDRESS3),i,-1));
 * user.setCity(parseStringCell(row.getCell(CITY),i,-1));
 * user.setState(parseStringCell(row.getCell(STATE),i,-1));
 * user.setCountry(parseStringCell(row.getCell(COUNTRY),i,-1));
 * user.setDayPhone(parseStringCell(row.getCell(DAY_PHONE),i,-1));
 * user.setEveningPhone(parseStringCell(row.getCell(EVE_PHONE),i,-1));
 * user.setMobilePhone(parseStringCell(row.getCell(MOB_PHONE),i,-1));
 * user.setFax(parseStringCell(row.getCell(FAX),i,-1));
 * }
 * 
 * if (emptyRow)
 * {
 * break;
 * }
 * if (hasError)
 * {
 * errorMessage = errorMessage + errMsgForRow;
 * continue;
 * }
 * else
 * {
 * try
 * {
 * if(!existingUsersOnly){
 * user.setCreateDate(new Date());
 * service.createUser(user);
 * }
 * UserOrganisation userOrg = new UserOrganisation();
 * userOrg.setUser(user);
 * userOrg.setOrganisation(org);
 * service.saveOrUpdateUserOrganisation(userOrg);
 * Iterator iter = roles.iterator();
 * while(iter.hasNext()){
 * UserOrganisationRole userOrgRole = new UserOrganisationRole();
 * userOrgRole.setUserOrganisation(userOrg);
 * userOrgRole.setRole((Role)iter.next());
 * service.saveOrUpdateUserOrganisationRole(userOrgRole);
 * }
 * count++;
 * }
 * catch (Exception e)
 * {
 * errMsgForRow =
 * errMsgForRow
 * + ROW
 * + i
 * + FAIL_ADD
 * + user.getLogin()
 * + SKIP
 * + e.getMessage();
 * errorMessage = errorMessage + errMsgForRow;
 * }
 * }
 * }
 * return (count + SUCCESS_ADD + errorMessage);
 * }
 * 
 * private String parseStringCell(HSSFCell cell, int row, int msgIndex){
 * if ((msgIndex!=-1)&&(cell == null))
 * {
 * if (hasError)
 * {
 * errMsgForRow = errMsgForRow + errMsgArray[msgIndex];
 * }
 * else
 * {
 * errMsgForRow = errMsgForRow + ROW + row + SKIP + errMsgArray[msgIndex];
 * hasError = true;
 * }
 * return null;
 * }
 * else if(cell!=null)
 * {
 * try{
 * cell.setCellType(HSSFCell.CELL_TYPE_STRING);
 * if(cell.getStringCellValue()!= null){
 * if(cell.getStringCellValue().trim().length()!= 0){
 * emptyRow = false;
 * }
 * }else{
 * return null;
 * }
 * return cell.getStringCellValue().trim();
 * }catch(Exception e)
 * {
 * cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
 * double d = cell.getNumericCellValue();
 * emptyRow = false;
 * return (new Long(new Double(d).longValue()).toString());
 * }
 * }
 * return null;
 * }
 * 
 * private AuthenticationMethod parseAuthMethodCell(HSSFCell cell, int row, int msgIndex){
 * String authMethodName = parseStringCell(cell,row,msgIndex);
 * if(authMethodName==null){
 * return null;
 * }else{
 * return service.getAuthenticationMethodByName(authMethodName);
 * }
 * }
 * 
 * private Set parseRoleCell(HSSFCell cell, int row, int msgIndex)
 * {
 * String roleDescription = "";
 * if ((msgIndex!=-1)&&(cell == null))
 * {
 * if (hasError)
 * {
 * errMsgForRow = errMsgForRow + errMsgArray[msgIndex];
 * }
 * else
 * {
 * errMsgForRow = errMsgForRow + ROW + row + SKIP + errMsgArray[msgIndex];
 * hasError = true;
 * }
 * return null;
 * }
 * else if(cell!=null)
 * {
 * try{
 * cell.setCellType(HSSFCell.CELL_TYPE_STRING);
 * if(cell.getStringCellValue()!= null){
 * if(cell.getStringCellValue().trim().length()!= 0){
 * emptyRow = false;
 * }
 * }else{
 * return null;
 * }
 * roleDescription = cell.getStringCellValue().trim();
 * }catch(Exception e)
 * {
 * return null;
 * }
 * 
 * }
 * Set roles = new HashSet();
 * int fromIndex = 0;
 * int index = roleDescription.indexOf(SEPERATOR, fromIndex);
 * while (index != -1)
 * {
 * Role role = (Role)service.findByProperty(Role.class,"name",roleDescription.substring(fromIndex, index)).get(0);
 * if(role!=null)//ignore wrong spelled role
 * {
 * roles.add(role);
 * }
 * fromIndex = index + 1;
 * index = roleDescription.indexOf(SEPERATOR, fromIndex);
 * }
 * Role role = service.getRoleByName(roleDescription.substring(fromIndex, roleDescription.length()));
 * if(role!=null){
 * roles.add(role);
 * }
 * return roles;
 * }
 * 
 * private Boolean parseStatusCell(HSSFCell cell, int row)
 * {
 * String status = parseStringCell(cell,row,-1);
 * return new Boolean(STATUS_DISABLED.equals(status));
 * }
 */}
