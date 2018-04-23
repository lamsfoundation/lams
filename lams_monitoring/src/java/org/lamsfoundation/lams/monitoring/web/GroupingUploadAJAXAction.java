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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
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
     * Get the spreadsheet file containing list of the current users, ready for uploading with groups. If lesson supplied,
     * list lesson users, otherwise list organisation users (course grouping screen has just the organisation).
     * 
     * @throws Exception
     */
    public ActionForward getGroupTemplateFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	
	if ( lessonId == null && organisationId == null ) {
		log.error("Cannot create group template file as lessonId and organisationId are both null.");
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid parameters");
	}
    
    	Lesson lesson = null;
 	String lessonOrOrganisationName = null; 	
	
    	if ( lessonId != null ) {
    	    lesson = (Lesson) getUserManagementService().findById(Lesson.class, lessonId);
    	    lessonOrOrganisationName = lesson.getLessonName();
    	    organisationId = lesson.getOrganisation().getOrganisationId();
    	} else {
    	    Organisation organisation = (Organisation) getUserManagementService().findById(Organisation.class, organisationId);
    	    lessonOrOrganisationName = organisation.getName();
	}

	// check if user is allowed to view and edit groups
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR },
		"view organisation groups", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	String fileName = new StringBuilder(
		getCentralMessageService().getMessage("filename.create.grouping.template").trim()).append(" ")
			.append(lessonOrOrganisationName).append(".xls").toString().replaceAll(" ", "-");
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	LinkedHashMap<String, ExcelCell[][]> dataToExport = null;

	if (lesson != null) {
	    Set<User> learners = lesson.getLessonClass().getLearners();
	    // check for any groups already exist in this grouping
	    Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    IMonitoringService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    Activity activity = monitoringService.getActivityById(activityId);
	    Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		    : ((GroupingActivity) activity).getCreateGrouping();
	    dataToExport = exportLearnersForGrouping(learners, grouping.getGroups(), null);

	} else {
	    @SuppressWarnings("unchecked")
	    Long groupingId = WebUtil.readLongParam(request,  "groupingId", true);
	    Set<OrganisationGroup> groups = null;
	    if ( groupingId != null ) {
		OrganisationGrouping orgGrouping = (OrganisationGrouping) getUserManagementService().findById(OrganisationGrouping.class, groupingId);
		if ( orgGrouping != null )
		    groups = orgGrouping.getGroups();
	    }
	    Vector<User> learners = (Vector<User>) getUserManagementService().getUsersFromOrganisationByRole(organisationId, Role.LEARNER, true);
	    dataToExport = exportLearnersForGrouping(learners, null, groups);
	}

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcelXLS(out, dataToExport, null, false);
	return null;
    }

    private LinkedHashMap<String, ExcelCell[][]> exportLearnersForGrouping(Collection<User> learners, Set<Group> groups, Set<OrganisationGroup> orgGroups) {

	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();
	int numberOfColumns = 4;

	ExcelCell[] title = new ExcelCell[numberOfColumns];
	title[0] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.login"), false);
	title[1] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.firstname"), false);
	title[2] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.lastname"), false);
	title[3] = new ExcelCell(getCentralMessageService().getMessage("spreadsheet.column.groupname"), false);
	rowList.add(title);

	if ( groups != null ) {
	    List<Group> groupList = new LinkedList<>(groups);
	    Collections.sort(groupList, new GroupComparator());
	    for (Group group : groupList) {
		String groupName = group.getGroupName();
		for (User groupUser : group.getUsers()) {
		    rowList.add(generateUserRow(numberOfColumns, groupName, groupUser));
		    learners.remove(groupUser);
		}
	    }
	}
	
	if ( orgGroups != null ) {
	    List<OrganisationGroup> groupList = new LinkedList<>(orgGroups);
	    for (OrganisationGroup group : groupList) {
		String groupName = group.getName();
		for (User groupUser : group.getUsers()) {
		    rowList.add(generateUserRow(numberOfColumns, groupName, groupUser));
		    learners.remove(groupUser);
		}
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
	boolean isLessonMode = WebUtil.readBooleanParam(request, "lessonMode", true);
	
	// used for lesson based grouping
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	Lesson lesson = lessonId != null ? (Lesson) getUserManagementService().findById(Lesson.class, lessonId) : null;

	// used for course grouping
	Long groupingId = WebUtil.readLongParam(request,  "groupingId", true);
	String name = WebUtil.readStrParam(request, "name", true);
	if ( isLessonMode && activityId == null ) {
	    log.error("Lesson grouping to be saved but activityId is missing");
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid parameters");
	} else if ( !isLessonMode ) {
	    if ( ( groupingId == null && name == null )  || organisationId == null ) {
		log.error("Course grouping to be saved but groupingId, grouping name or organisationId is missing");
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid parameters");
	    }
	}
	
	Organisation organisation;
	if (organisationId == null) {
	    // read organisation ID from lesson
	    organisation = lesson.getOrganisation();
	    organisationId = organisation.getOrganisationId();
	} else {
	    organisation = (Organisation) getUserManagementService().findById(Organisation.class, organisationId);
	}

	// check if user is allowed to save grouping
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR }, 
		"save organisation grouping from spreadsheet",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	@SuppressWarnings("rawtypes")
	Hashtable fileElements = form.getMultipartRequestHandler().getFileElements();
	if (fileElements.size() == 0) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "No file provided");
	}
	if (log.isDebugEnabled()) {
	    log.debug("Saving course groups from spreadsheet for user " + userId
		    + " and organisation " + organisationId + " filename " + fileElements);
	}

	JSONObject responseJSON = isLessonMode ? saveLessonGrouping(lessonId, activityId, fileElements)
		: saveCourseGrouping(organisation, groupingId, name, fileElements);

	response.getWriter().write(responseJSON.toString());
	return null;
    }


    /** Create the new course grouping */
    private JSONObject saveCourseGrouping(Organisation organisation,  Long orgGroupingId, String name, Hashtable fileElements)
	    throws JSONException, IOException {
	
	OrganisationGrouping orgGrouping = null;
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) getUserManagementService().findById(OrganisationGrouping.class,
		    orgGroupingId);
	} 
	if (orgGrouping == null) {
	    orgGrouping = new OrganisationGrouping();
	    orgGrouping.setOrganisationId(organisation.getOrganisationId());
	    orgGrouping.setName(name);
	}

	Map<String, Long> existingGroupNameToId = new HashMap<String, Long>();
	if (orgGrouping.getGroups() != null) {
	    for (OrganisationGroup group : orgGrouping.getGroups()) {
		existingGroupNameToId.put(group.getName(), group.getGroupId());
	    }
	}

	Map<String, Set<String>> groups = new HashMap<String, Set<String>>();
	int totalUsersSkipped = parseGroupSpreadsheet((FormFile) fileElements.elements().nextElement(), orgGroupingId, groups);
	
	int totalUsersAdded = 0;
	List<OrganisationGroup> orgGroups = new LinkedList<>();
	for ( Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    String groupName = groupEntry.getKey();
	    // just overwrite existing groups; they will be updated if already exist
	    Set<User> learners = new HashSet<>();
	    for ( String login : groupEntry.getValue() ) {
		User learner = (User) getUserManagementService().getUserByLogin(login);
		if (learner == null) {
		    log.warn("Unable to add learner " + login + " for group in related to grouping "
			    + orgGroupingId + " as learner cannot be found.");
		    totalUsersSkipped++;
		} else {
		    totalUsersAdded++;
		    learners.add(learner);
		}
	    }
	    OrganisationGroup orgGroup = new OrganisationGroup();
	    Long orgGroupId = existingGroupNameToId.get(groupName);
	    if ( orgGroupId != null ) {
		orgGroup.setGroupId(orgGroupId);
		orgGroup.setGroupingId(orgGroupingId);
	    }
	    orgGroup.setName(groupName);
	    orgGroup.setUsers(learners);
	    orgGroups.add(orgGroup);
	}

	getUserManagementService().saveOrganisationGrouping(orgGrouping, orgGroups);
	return createResponseJSON(false, null, true, orgGrouping.getGroupingId(), totalUsersAdded, totalUsersSkipped);

    }

    /** 	 Clean out and reuse any existing groups */
    private JSONObject saveLessonGrouping(Long lessonId, Long activityId, Hashtable fileElements)
	    throws JSONException, IOException {

	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());

	int totalUsersSkipped = 0;
	int totalUsersAdded = 0;

	// Lesson grouping case so clean out and reuse any existing groups
	Activity activity = monitoringService.getActivityById(activityId);
	Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		: ((GroupingActivity) activity).getCreateGrouping();

	Set<String> existingGroupNames = new HashSet<String>();
	// check for any users starting to use the groups since the page was loaded.
	for (Group group : grouping.getGroups()) {
	    existingGroupNames.add(group.getGroupName());
	    if (!group.mayBeDeleted()) {
		String error = getCentralMessageService().getMessage("error.groups.upload.locked");
		return createResponseJSON(true, error, true, grouping.getGroupingId(),0, 0);
	    }
	}

	Map<String, Set<String>> groups = new HashMap<String, Set<String>>();
	totalUsersSkipped = parseGroupSpreadsheet((FormFile) fileElements.elements().nextElement(), grouping.getGroupingId(), groups);

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
		    return createResponseJSON(true, error.toString(), false, grouping.getGroupingId(), 0, 0);
		}
	    }
	}
	
	//check all users exist and are learners in the specified lesson
	for ( Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    Set<String> logins = groupEntry.getValue();

	    Iterator<String> iter = logins.iterator();
	    while (iter.hasNext()) {
	        String login = iter.next();
		User learner = (User) getUserManagementService().getUserByLogin(login);
		if (learner == null) {
		    log.warn("Unable to add learner " + login + " to lesson grouping as learner cannot be found.");
		    totalUsersSkipped++;
		    iter.remove();
		    
		} else if (!getSecurityService().isLessonLearner(lessonId, learner.getUserId(), "be added to grouping", false)) {
		    //log.warn("Unable to add learner " + login + " to lesson grouping as learner doesn't belong to the lesson.");
		    totalUsersSkipped++;
		    iter.remove();
		}
	    }
	}

	// remove all the existing users from their groups
	getLessonService().removeAllLearnersFromGrouping(grouping);

	// Now put in the new users groupings
	for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    int added = monitoringService.addUsersToGroupByLogins(activityId, groupEntry.getKey(),
		    groupEntry.getValue());
	    totalUsersAdded += added;
	    totalUsersSkipped += groupEntry.getValue().size() - added;
	}

	return createResponseJSON(false, null, true, grouping.getGroupingId(), totalUsersAdded, totalUsersSkipped);
    }

    private JSONObject createResponseJSON(boolean isError, String errorMessage, boolean reload, Long groupingId,
	    int totalUsersAdded, int totalUsersSkipped) throws JSONException {
	JSONObject responseJSON = new JSONObject();
	if (isError) {
	    responseJSON.put("result", "FAIL");
	    responseJSON.put("reload", reload);
	    responseJSON.put("error", errorMessage);
	} else {
	    responseJSON.put("result", "OK");
	    responseJSON.put("added", totalUsersAdded);
	    responseJSON.put("skipped", totalUsersSkipped);
	}
	responseJSON.put("groupingId", groupingId);
	return responseJSON;
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

     public int parseGroupSpreadsheet(FormFile fileItem, Long groupingID, Map<String, Set<String>> groups)
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
			log.warn("Unable to add learner " + login + " for group in related to grouping " + groupingID
				+ " as group name is missing.");
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
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }

    private ISecurityService getSecurityService() {
	if (securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return securityService;
    }

    private MessageService getCentralMessageService() {
	if (centralMessageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    centralMessageService = (MessageService) ctx.getBean("centralMessageService");
	}
	return centralMessageService;
    }

}
