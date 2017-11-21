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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The action servlet that provides the support for the AJAX based Chosen Grouping upload from File
 * 
 * @author Fiona Malikoff
 */
public class GroupingUploadAJAXAction extends DispatchAction {

    private static Logger log = Logger.getLogger(GroupingUploadAJAXAction.class);

    private static IUserManagementService userManagementService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;
    private static MessageService centralMessageService;

    /**
     * Get the spreadsheet file containing list of the current users, ready for uploading with groups.
     * 
     * @throws Exception
     */
    public ActionForward getGroupTemplateFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Lesson lesson = (Lesson) getUserManagementService().findById(Lesson.class, lessonId);
	;
	if (organisationId == null) {
	    // read organisation ID from lesson
	    organisationId = lesson.getOrganisation().getOrganisationId();
	}
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	// check if user is allowed to view and edit groups
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR },
		"view organisation groups", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	// check for any groups already exist in this grouping
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	Activity activity = monitoringService.getActivityById(activityId);
	Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		: ((GroupingActivity) activity).getCreateGrouping();

	String fileName = new StringBuilder(
		getCentralMessageService().getMessage("filename.create.grouping.template").trim()).append(" ")
			.append(lesson.getLessonName()).append(".xls").toString().replaceAll(" ", "-");
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	ServletOutputStream out = response.getOutputStream();

	LinkedHashMap<String, ExcelCell[][]> dataToExport = exportLearnersForGrouping(lesson, grouping.getGroups());

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ExcelUtil.createExcelXLS(out, dataToExport, null, false);
	return null;
    }

    private LinkedHashMap<String, ExcelCell[][]> exportLearnersForGrouping(Lesson lesson, Set<Group> groups) {

	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();
	int numberOfColumns = 4;

	ExcelCell[] title = new ExcelCell[numberOfColumns];
	title[0] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.login"), false);
	title[1] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.firstname"), false);
	title[2] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.lastname"), false);
	title[3] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.groupname"), false);
	rowList.add(title);

	Collection<User> learners = lesson.getLessonClass().getLearners();

	List<Group> groupList = new LinkedList<>(groups);
	Collections.sort(groupList, new GroupComparator());
	for (Group group : groupList) {
	    String groupName = group.getGroupName();
	    for (User groupUser : group.getUsers()) {
		rowList.add(generateUserRow(numberOfColumns, groupName, groupUser));
		learners.remove(groupUser);
	    }
	}

	// all the remaining users are unassigned to any group
	for (User unassignedUser : learners) {
	    rowList.add(generateUserRow(numberOfColumns, null, unassignedUser));
	}

	ExcelCell[][] summaryData = rowList.toArray(new ExcelCell[][] {});
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();
	dataToExport.put(getCentralMessageService().getMessage("label.course.groups.prefix"), summaryData);
	return dataToExport;
    }

    private ExcelCell[] generateUserRow(int numberOfColumns, String groupName, User groupUser) {
	ExcelCell[] userRow = new ExcelCell[numberOfColumns];
	userRow[0] = new ExcelCell(groupUser.getLogin(), false);
	userRow[1] = new ExcelCell(groupUser.getFirstName(), false);
	userRow[2] = new ExcelCell(groupUser.getLastName(), false);
	userRow[3] = new ExcelCell(groupName, false);
	return userRow;
    }
    
    /**
     * Saves a course grouping.
     */
    public ActionForward importLearnersForGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, InvalidParameterException, IOException {

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Lesson lesson = (Lesson) getUserManagementService().findById(Lesson.class, lessonId);

	if (organisationId == null) {
	    // read organisation ID from lesson
	    organisationId = lesson.getOrganisation().getOrganisationId();
	}

	// check if user is allowed to save grouping
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER }, "save organisation grouping from spreadsheet",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	@SuppressWarnings("rawtypes")
	Hashtable fileElements = form.getMultipartRequestHandler().getFileElements();
	if (fileElements.size() == 0) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "No file provided");
	}
	if (GroupingUploadAJAXAction.log.isDebugEnabled()) {
	    GroupingUploadAJAXAction.log.debug("Saving course groups from spreadsheet for user " + userId
		    + " and organisation " + organisationId + " filename " + fileElements);
	}

	// remove users from current group
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());

	Activity activity = monitoringService.getActivityById(activityId);
	Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		: ((GroupingActivity) activity).getCreateGrouping();

	Set<String> existingGroupNames = new HashSet<String>();
	// check for any users starting to use the groups since the page was loaded.
	for (Group group : grouping.getGroups()) {
	    existingGroupNames.add(group.getGroupName());
	    if (!group.mayBeDeleted()) {
		String error = getCentralMessageService().getMessage("error.groups.upload.locked");
		return returnImport(response, error, true);
	    }
	}

	Map<String, Set<String>> groups = new HashMap<String, Set<String>>();
	int totalUserSkipped = parseGroupSpreadsheet((FormFile) fileElements.elements().nextElement(), activityId,
		groups);
	int totalUserAdded = 0;

	// if branching must use the already specified groups or cannot match to a branch!
	if (activity.isChosenBranchingActivity()) {
	    for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
		if (!existingGroupNames.contains(groupEntry.getKey())) {
		    StringBuilder groupNamesStrBlder = new StringBuilder();
		    for (String name : existingGroupNames)
			groupNamesStrBlder.append("'").append(name).append("' ");
		    String error = getCentralMessageService().getMessage(
				    "error.branching.upload.must.use.existing.groups",
				    new String[] { groupNamesStrBlder.toString() });
		    return returnImport(response, error.toString(), false);
		}
	    }
	}

	// remove all the existing users from their groups
	getLessonService().removeAllLearnersFromGrouping(grouping);

	// Now put in the new users groupings
	for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    int added = monitoringService.addUsersToGroupByLogins(activityId, groupEntry.getKey(),
		    groupEntry.getValue());
	    totalUserAdded += added;
	    totalUserSkipped += groupEntry.getValue().size() - added;
	}
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("result", "OK");
	responseJSON.put("added", totalUserAdded);
	responseJSON.put("skipped", totalUserSkipped);

	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private ActionForward returnImport(HttpServletResponse response, String errorMessage, boolean reload)
	    throws JSONException, IOException {
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("result", "FAIL");
	responseJSON.put("reload", reload);
	responseJSON.put("error", errorMessage);
	response.getWriter().write(responseJSON.toString());
	return null;

    }


    /* XLS Version Parse */
    private String parseStringCell(HSSFCell cell) {
 	if (cell != null) {
 	    cell.setCellType(Cell.CELL_TYPE_STRING);
 	    if (cell.getStringCellValue() != null) {
 		return cell.getStringCellValue().trim();
 	    }
 	}
 	return null;
     }

     public int parseGroupSpreadsheet(FormFile fileItem, Long activityID, Map<String, Set<String>> groups)
 	    throws IOException {
 	POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
 	HSSFWorkbook wb = new HSSFWorkbook(fs);
 	HSSFSheet sheet = wb.getSheetAt(0);

 	int startRow = sheet.getFirstRowNum();
 	int endRow = sheet.getLastRowNum();
 	int skipped = 0;

 	for (int i = startRow + 1; i < (endRow + 1); i++) {
 	    HSSFRow row = sheet.getRow(i);
 	    String login = parseStringCell(row.getCell(0));
 	    if (login != null) {
 		login = login.trim();
 		if (login.length() > 0) {
 		    String groupName = row.getLastCellNum() > 3 ? parseStringCell(row.getCell(3)) : null;
 		    groupName = groupName != null ? groupName.trim() : null;
 		    if (groupName == null || groupName.length() == 0) {
 			skipped++;
 			GroupingUploadAJAXAction.log.warn("Unable to add learner " + login
 				+ " for group in related to activity " + activityID + " as group name is missing.");
 		    } else {
 			Set<String> users = groups.get(groupName);
 			if (users == null) {
 			    users = new HashSet<String>();
 			    groups.put(groupName, users);
 			}
 			users.add(login);
 		    }
 		}
 	    }
 	}
 	return skipped;
     }
     
    final Comparator<OrganisationGroup> ORG_GROUP_COMPARATOR = new Comparator<OrganisationGroup>() {
	@Override
	public int compare(OrganisationGroup o1, OrganisationGroup o2) {
	    String grp1Name = o1 != null ? o1.getName() : "";
	    String grp2Name = o2 != null ? o2.getName() : "";

	    AlphanumComparator comparator = new AlphanumComparator();
	    return comparator.compare(grp1Name, grp2Name);
	}
    };

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private IUserManagementService getUserManagementService() {
	if (GroupingUploadAJAXAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GroupingUploadAJAXAction.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return GroupingUploadAJAXAction.userManagementService;
    }

    private ILessonService getLessonService() {
	if (GroupingUploadAJAXAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GroupingUploadAJAXAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return GroupingUploadAJAXAction.lessonService;
    }

    private ISecurityService getSecurityService() {
	if (GroupingUploadAJAXAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GroupingUploadAJAXAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return GroupingUploadAJAXAction.securityService;
    }

    private MessageService getCentralMessageService() {
	if (GroupingUploadAJAXAction.centralMessageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GroupingUploadAJAXAction.centralMessageService = (MessageService) ctx.getBean("centralMessageService");
	}
	return GroupingUploadAJAXAction.centralMessageService;
    }

}
