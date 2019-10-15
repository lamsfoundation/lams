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

package org.lamsfoundation.lams.learning.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.form.NotebookForm;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author M Seaton
 */
@Controller
@RequestMapping("/notebook")
public class NotebookController {
    @Autowired
    private ICoreNotebookService coreNotebookService;
    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private IUserManagementService userManagementService;

    /**
     * View all notebook entries
     */
    @RequestMapping("/viewAll")
    public String viewAll(@ModelAttribute NotebookForm notebookForm, HttpServletRequest request)
	    throws IOException, ServletException {
	// getting requested object according to coming parameters
	Integer learnerID = LearningWebUtil.getUserId();

	// lessonID
	Long lessonID = notebookForm.getCurrentLessonID();
	if (lessonID == null || lessonID == 0) {
	    lessonID = notebookForm.getLessonID();
	}

	// get all notebook entries for the learner
	TreeMap<Long, List<NotebookEntry>> entries = coreNotebookService.getEntriesGroupedByLesson(learnerID);
	request.getSession().setAttribute("entries", entries.values());
	request.setAttribute("lessonID", lessonID);

	return "notebook/viewall";
    }

    /**
     * View all journals entries from a lesson call
     */
    @RequestMapping("/viewAllJournals")
    public String viewAllJournals(@ModelAttribute NotebookForm notebookForm, HttpServletRequest request)
	    throws IOException, ServletException {

	// getting requested object according to coming parameters
	Integer userID = LearningWebUtil.getUserId();
	User user = (User) userManagementService.findById(User.class, userID);

	// lesson service
	Long lessonID = notebookForm.getLessonID();
	Lesson lesson = learnerService.getLesson(lessonID);

	if (!hasStaffAccessToJournals(user, lesson)) {
	    throw new UserAccessDeniedException(
		    "User " + userID + " may not retrieve journal entries for lesson " + lessonID);
	}

	// List of Journal entries
	List<NotebookEntry> journals = coreNotebookService.getEntry(lesson.getLessonId(), CoreNotebookConstants.SCRATCH_PAD,
		CoreNotebookConstants.JOURNAL_SIG);
	request.getSession().setAttribute("journals", journals);
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonID);

	return "notebook/viewalljournals";
    }

    // check user has permission to access all the journals for a lesson
    private boolean hasStaffAccessToJournals(User user, Lesson lesson) {
	if (lesson == null) {
	    return false;
	}

	// lesson owner okay
	if ((lesson.getUser() != null) && lesson.getUser().getUserId().equals(user.getUserId())) {
	    return true;
	}

	// staff member okay
	if ((lesson.getLessonClass() != null) && lesson.getLessonClass().isStaffMember(user)) {
	    return true;
	}

	return false;
    }

    /**
     * View single notebook entry
     */
    @RequestMapping("/viewEntry")
    public String viewEntry(@ModelAttribute NotebookForm notebookForm, HttpServletRequest request)
	    throws IOException, ServletException {

	Long uid = notebookForm.getUid();
	Long currentLessonID = notebookForm.getCurrentLessonID();
	String mode = WebUtil.readStrParam(request, "mode", true);

	NotebookEntry entry = coreNotebookService.getEntry(uid);

	// getting requested object according to coming parameters
	Integer userID = LearningWebUtil.getUserId();
	User user = (User) userManagementService.findById(User.class, userID);

	if (entry.getUser() != null && !entry.getUser().getUserId().equals(user.getUserId())) {
	    // wants to look at someone else's entry - check they are a teacher
	    Lesson lesson = learnerService.getLesson(currentLessonID);
	    if (!hasStaffAccessToJournals(user, lesson)) {
		throw new UserAccessDeniedException(
			"User " + userID + " may not retrieve journal entries for lesson " + currentLessonID);
	    }
	}

	if (mode != null) {
	    request.setAttribute("mode", mode);
	}

	if (entry != null) {
	    request.setAttribute("entry", entry);
	}

	if (currentLessonID != null) {
	    request.setAttribute("currentLessonID", currentLessonID);
	}

	return "notebook/view";
    }

    @RequestMapping("/add")
    public String addNewEntry(@ModelAttribute("notebookForm") NotebookForm notebookForm) {
	return "notebook/addnew";
    }

    @RequestMapping("/processNewEntry")
    public String processNewEntry(@ModelAttribute("notebookForm") NotebookForm notebookForm, HttpServletRequest request)
	    throws IOException, ServletException {

	Long id = notebookForm.getLessonID();
	String title = notebookForm.getTitle();
	String entry = notebookForm.getEntry();
	String signature = notebookForm.getSignature();
	Integer userID = LearningWebUtil.getUserId();

	coreNotebookService.createNotebookEntry(id, CoreNotebookConstants.SCRATCH_PAD, signature, userID, title, entry);

	boolean skipViewAll = WebUtil.readBooleanParam(request, "skipViewAll", false);
	return skipViewAll ? null : viewAll(notebookForm, request);
    }

    @RequestMapping(path = "/updateEntry")
    public String updateEntry(@ModelAttribute("notebookForm") NotebookForm notebookForm, HttpServletRequest request)
	    throws IOException, ServletException {

	Long uid = notebookForm.getUid();
	String title = notebookForm.getTitle();
	String entry = notebookForm.getEntry();
	String signature = notebookForm.getSignature();

	// get existing entry to edit
	NotebookEntry entryObj = coreNotebookService.getEntry(uid);

	// check entry is being edited by it's owner
	Integer userID = LearningWebUtil.getUserId();
	if (userID != entryObj.getUser().getUserId()) {
	    // throw exception
	}

	// update entry
	entryObj.setTitle(title);
	entryObj.setEntry(entry);
	entryObj.setExternalSignature(signature);

	coreNotebookService.updateEntry(entryObj);

	return viewAll(notebookForm, request);
    }

}