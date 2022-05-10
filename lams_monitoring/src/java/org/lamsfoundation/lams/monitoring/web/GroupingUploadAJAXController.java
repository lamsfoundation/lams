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
import java.util.Iterator;
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
import org.apache.poi.ss.usermodel.CellType;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.monitoring.web.form.FileUploadForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The action servlet that provides the support for the AJAX based Chosen Grouping upload from File
 *
 * @author Fiona Malikoff
 */
@Controller
@RequestMapping("/groupingUpload")
public class GroupingUploadAJAXController {
    private static Logger log = Logger.getLogger(GroupingUploadAJAXController.class);

    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    /**
     * Get the spreadsheet file containing list of the current users, ready for uploading with groups. If lesson
     * supplied, list lesson users, otherwise list organisation users (course grouping screen has just the
     * organisation).
     *
     * @throws Exception
     */
    @RequestMapping("/getGroupTemplateFile")
    public void getGroupTemplateFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);

	if (lessonId == null && organisationId == null) {
	    log.error("Cannot create group template file as lessonId and organisationId are both null.");
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid parameters");
	}

	Lesson lesson = null;
	String lessonOrOrganisationName = null;

	if (lessonId != null) {
	    lesson = (Lesson) userManagementService.findById(Lesson.class, lessonId);
	    lessonOrOrganisationName = lesson.getLessonName();
	    organisationId = lesson.getOrganisation().getOrganisationId();
	} else {
	    Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		    organisationId);
	    lessonOrOrganisationName = organisation.getName();
	}

	// check if user is allowed to view and edit groups
	if (!securityService.hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR }, "view organisation groups", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return;
	}

	String fileName = new StringBuilder(messageService.getMessage("filename.create.grouping.template").trim())
		.append(" ").append(lessonOrOrganisationName).append(".xls").toString().replaceAll(" ", "-");
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	List<ExcelSheet> sheets;
	if (lesson != null) {
	    Set<User> learners = lesson.getLessonClass().getLearners();
	    // check for any groups already exist in this grouping
	    Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    Activity activity = monitoringService.getActivityById(activityId);
	    Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		    : ((GroupingActivity) activity).getCreateGrouping();
	    sheets = exportLearnersForGrouping(learners, grouping.getGroups(), null);

	} else {
	    Long groupingId = WebUtil.readLongParam(request, "groupingId", true);
	    Set<OrganisationGroup> groups = null;
	    if (groupingId != null) {
		OrganisationGrouping orgGrouping = (OrganisationGrouping) userManagementService
			.findById(OrganisationGrouping.class, groupingId);
		if (orgGrouping != null) {
		    groups = orgGrouping.getGroups();
		}
	    }
	    Vector<User> learners = userManagementService.getUsersFromOrganisationByRole(organisationId, Role.LEARNER,
		    true);
	    sheets = exportLearnersForGrouping(learners, null, groups);
	}

	// set cookie that will tell JS script that export has been finished
	WebUtil.setFileDownloadTokenCookie(request, response);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, null, false, false);
    }

    private List<ExcelSheet> exportLearnersForGrouping(Collection<User> learners, Set<Group> groups,
	    Set<OrganisationGroup> orgGroups) {
	List<ExcelSheet> sheets = new LinkedList<ExcelSheet>();
	ExcelSheet excelSheet = new ExcelSheet(messageService.getMessage("label.course.groups.prefix"));
 	sheets.add(excelSheet);

	ExcelRow titleRow = excelSheet.initRow();
	titleRow.addCell(messageService.getMessage("spreadsheet.column.login"));
	titleRow.addCell(messageService.getMessage("spreadsheet.column.firstname"));
	titleRow.addCell(messageService.getMessage("spreadsheet.column.lastname"));
	titleRow.addCell(messageService.getMessage("spreadsheet.column.groupname"));

	if (groups != null) {
	    List<Group> groupList = new LinkedList<>(groups);
	    Collections.sort(groupList, new GroupComparator());
	    for (Group group : groupList) {
		String groupName = group.getGroupName();
		for (User groupUser : group.getUsers()) {
		    generateUserRow(groupName, groupUser, excelSheet);
		    learners.remove(groupUser);
		}
	    }
	}

	if (orgGroups != null) {
	    List<OrganisationGroup> groupList = new LinkedList<>(orgGroups);
	    for (OrganisationGroup group : groupList) {
		String groupName = group.getName();
		for (User groupUser : group.getUsers()) {
		    generateUserRow(groupName, groupUser, excelSheet);
		    learners.remove(groupUser);
		}
	    }
	}

	// all the remaining users are unassigned to any group
	for (User unassignedUser : learners) {
	    generateUserRow(null, unassignedUser, excelSheet);
	}

	return sheets;
    }

    private void generateUserRow(String groupName, User groupUser, ExcelSheet excelSheet) {
	ExcelRow userRow = excelSheet.initRow();
	userRow.addCell(groupUser.getLogin());
	userRow.addCell(groupUser.getFirstName());
	userRow.addCell(groupUser.getLastName());
	userRow.addCell(groupName);
    }

    /**
     * Saves a course grouping.
     */
    @RequestMapping("/importLearnersForGrouping")
    @ResponseBody
    public String importLearnersForGrouping(@ModelAttribute("uploadForm") FileUploadForm uploadForm,
	    HttpServletRequest request, HttpServletResponse response) throws InvalidParameterException, IOException {

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	boolean isLessonMode = WebUtil.readBooleanParam(request, "lessonMode", true);

	// used for lesson based grouping
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	Lesson lesson = lessonId != null ? (Lesson) userManagementService.findById(Lesson.class, lessonId) : null;

	// used for course grouping
	Long groupingId = WebUtil.readLongParam(request, "groupingId", true);
	String name = WebUtil.readStrParam(request, "name", true);
	if (isLessonMode && activityId == null) {
	    log.error("Lesson grouping to be saved but activityId is missing");
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid parameters");
	} else if (!isLessonMode) {
	    if ((groupingId == null && name == null) || organisationId == null) {
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
	    organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	}

	// check if user is allowed to save grouping
	if (!securityService.hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR },
		"save organisation grouping from spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	MultipartFile file = uploadForm.getGroupUploadFile();
	if (file == null || file.getSize() == 0) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "No file provided");
	    return null;
	}
	if (log.isDebugEnabled()) {
	    log.debug("Saving course groups from spreadsheet for user " + userId + " and organisation " + organisationId
		    + " filename " + file);
	}

	ObjectNode responseJSON = isLessonMode ? saveLessonGrouping(lessonId, activityId, file)
		: saveCourseGrouping(organisation, groupingId, name, file);

	return responseJSON.toString();
    }

    /** Create the new course grouping */
    private ObjectNode saveCourseGrouping(Organisation organisation, Long orgGroupingId, String name,
	    MultipartFile fileElements) throws IOException {

	OrganisationGrouping orgGrouping = null;
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) userManagementService.findById(OrganisationGrouping.class,
		    orgGroupingId);
	}
	if (orgGrouping == null) {
	    orgGrouping = new OrganisationGrouping();
	    orgGrouping.setOrganisationId(organisation.getOrganisationId());
	    orgGrouping.setName(name);
	}

	Map<String, Long> existingGroupNameToId = new HashMap<>();
	if (orgGrouping.getGroups() != null) {
	    for (OrganisationGroup group : orgGrouping.getGroups()) {
		existingGroupNameToId.put(group.getName(), group.getGroupId());
	    }
	}

	Map<String, Set<String>> groups = new HashMap<>();
	int totalUsersSkipped = parseGroupSpreadsheet(fileElements, orgGroupingId, groups);
	int totalUsersAdded = 0;

	List<OrganisationGroup> orgGroups = new LinkedList<>();
	for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    String groupName = groupEntry.getKey();
	    // just overwrite existing groups; they will be updated if already exist
	    Set<User> learners = new HashSet<>();
	    for (String login : groupEntry.getValue()) {
		User learner = userManagementService.getUserByLogin(login);
		if (learner == null) {
		    log.warn("Unable to add learner " + login + " for group in related to grouping " + orgGroupingId
			    + " as learner cannot be found.");
		    totalUsersSkipped++;

		    //Check user is a part of the organisation
		} else if (!securityService.hasOrgRole(organisation.getOrganisationId(), learner.getUserId(),
			new String[] { Role.GROUP_MANAGER, Role.LEARNER, Role.MONITOR, Role.AUTHOR },
			"be added to grouping", false)) {

		    totalUsersSkipped++;

		} else {
		    totalUsersAdded++;
		    learners.add(learner);
		}
	    }
	    OrganisationGroup orgGroup = new OrganisationGroup();
	    Long orgGroupId = existingGroupNameToId.get(groupName);
	    if (orgGroupId != null) {
		orgGroup.setGroupId(orgGroupId);
		orgGroup.setGroupingId(orgGroupingId);
	    }
	    orgGroup.setName(groupName);
	    orgGroup.setUsers(learners);
	    orgGroups.add(orgGroup);
	}

	userManagementService.saveOrganisationGrouping(orgGrouping, orgGroups);
	return createResponseJSON(false, null, true, orgGrouping.getGroupingId(), totalUsersAdded, totalUsersSkipped);

    }

    /** Clean out and reuse any existing groups */
    private ObjectNode saveLessonGrouping(Long lessonId, Long activityId, MultipartFile fileElements)
	    throws IOException {

	int totalUsersSkipped = 0;
	int totalUsersAdded = 0;

	// Lesson grouping case so clean out and reuse any existing groups
	Activity activity = monitoringService.getActivityById(activityId);
	Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		: ((GroupingActivity) activity).getCreateGrouping();

	Set<String> existingGroupNames = new HashSet<>();
	// check for any users starting to use the groups since the page was loaded.
	for (Group group : grouping.getGroups()) {
	    existingGroupNames.add(group.getGroupName());
	    if (!group.mayBeDeleted()) {
		String error = messageService.getMessage("error.groups.upload.locked");
		return createResponseJSON(true, error, true, grouping.getGroupingId(), 0, 0);
	    }
	}

	Map<String, Set<String>> groups = new HashMap<>();
	totalUsersSkipped = parseGroupSpreadsheet(fileElements, grouping.getGroupingId(), groups);

	// if branching must use the already specified groups or cannot match to a branch!
	if (activity.isChosenBranchingActivity()) {
	    for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
		if (!existingGroupNames.contains(groupEntry.getKey())) {
		    StringBuilder groupNamesStrBlder = new StringBuilder();
		    for (String name : existingGroupNames) {
			groupNamesStrBlder.append("'").append(name).append("' ");
		    }
		    String error = messageService.getMessage("error.branching.upload.must.use.existing.groups",
			    new String[] { groupNamesStrBlder.toString() });
		    return createResponseJSON(true, error.toString(), false, grouping.getGroupingId(), 0, 0);
		}
	    }
	}

	//check all users exist and are learners in the specified lesson
	for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    Set<String> logins = groupEntry.getValue();

	    Iterator<String> iter = logins.iterator();
	    while (iter.hasNext()) {
		String login = iter.next();
		User learner = userManagementService.getUserByLogin(login);
		if (learner == null) {
		    log.warn("Unable to add learner " + login + " to lesson grouping as learner cannot be found.");
		    totalUsersSkipped++;
		    iter.remove();

		} else if (!securityService.isLessonLearner(lessonId, learner.getUserId(), "be added to grouping",
			false)) {
		    //log.warn("Unable to add learner " + login + " to lesson grouping as learner doesn't belong to the lesson.");
		    totalUsersSkipped++;
		    iter.remove();
		}
	    }
	}

	// remove all the existing users from their groups
	lessonService.removeAllLearnersFromGrouping(grouping);

	// Now put in the new users groupings
	for (Map.Entry<String, Set<String>> groupEntry : groups.entrySet()) {
	    int added = monitoringService.addUsersToGroupByLogins(activityId, groupEntry.getKey(),
		    groupEntry.getValue());
	    totalUsersAdded += added;
	    totalUsersSkipped += groupEntry.getValue().size() - added;
	}

	return createResponseJSON(false, null, true, grouping.getGroupingId(), totalUsersAdded, totalUsersSkipped);
    }

    private ObjectNode createResponseJSON(boolean isError, String errorMessage, boolean reload, Long groupingId,
	    int totalUsersAdded, int totalUsersSkipped) {
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
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
	    cell.setCellType(CellType.STRING);
	    if (cell.getStringCellValue() != null) {
		return cell.getStringCellValue().trim();
	    }
	}
	return null;
    }

    public int parseGroupSpreadsheet(MultipartFile fileItem, Long groupingID, Map<String, Set<String>> groups)
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
			GroupingUploadAJAXController.log.warn("Unable to add learner " + login
				+ " for group in related to grouping " + groupingID + " as group name is missing.");
		    } else {
			Set<String> users = groups.get(groupName);
			if (users == null) {
			    users = new HashSet<>();
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

}
