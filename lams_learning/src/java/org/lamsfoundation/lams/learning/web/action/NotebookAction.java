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
package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author M Seaton
 * 
 *         ----------------XDoclet Tags--------------------
 * 
 * @struts:action name="NotebookForm" path="/notebook" parameter="method" validate="false"
 * 
 * @struts.action-forward name = "viewAll" path = ".notebookViewAll"
 * @struts.action-forward name= "viewSingle" path = ".notebookViewSingle"
 * @struts.action-forward name= "viewJournals" path = ".notebookViewJournals"
 * @struts.action-forward name = "addNew" path = ".notebookAddNew"
 * @struts.action-forward name = "saveSuccess" path = ".notebookSaveSuccess" ----------------XDoclet
 *                        Tags--------------------
 * 
 */
public class NotebookAction extends LamsDispatchAction {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(NotebookAction.class);

    private static IAuditService auditService;

    private static final String VIEW_ALL = "viewAll";
    private static final String VIEW_SINGLE = "viewSingle";
    private static final String VIEW_JOURNALS = "viewJournals";
    private static final String ADD_NEW = "addNew";
    private static final String SAVE_SUCCESS = "saveSuccess";

    public ICoreNotebookService getNotebookService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(this.getServlet().getServletContext());
	return (ICoreNotebookService) webContext.getBean(CoreNotebookConstants.NOTEBOOK_SERVICE_BEAN_NAME);
    }

    /**
     * View all notebook entries
     */
    public ActionForward viewAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// initialize service object
	ICoreNotebookService notebookService = getNotebookService();

	DynaActionForm notebookForm = (DynaActionForm) actionForm;

	// getting requested object according to coming parameters
	Integer learnerID = LearningWebUtil.getUserId();

	// lessonID
	Long lessonID = (Long) notebookForm.get(AttributeNames.PARAM_LESSON_ID);

	// get all notebook entries for the learner

	TreeMap<Long, List<NotebookEntry>> entries = notebookService.getEntryByLesson(learnerID,
		CoreNotebookConstants.SCRATCH_PAD);

	request.getSession().setAttribute("entries", entries.values());
	request.setAttribute("lessonID", lessonID);

	return mapping.findForward(NotebookAction.VIEW_ALL);

    }

    /**
     * View all journals entries from a lesson call
     */
    public ActionForward viewAllJournals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// List of Journal entries
	List<NotebookEntry> journals = null;

	DynaActionForm notebookForm = (DynaActionForm) actionForm;

	// lesson service
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	// getting requested object according to coming parameters
	Integer userID = LearningWebUtil.getUserId();

	// lessonID
	Long lessonID = (Long) notebookForm.get(AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = learnerService.getLesson(lessonID);

	// check user has permission
	User user = (User) LearnerServiceProxy.getUserManagementService(getServlet().getServletContext())
		.findById(User.class, userID);

	if ((lesson.getUser() != null) && lesson.getUser().getUserId().equals(userID)) {
	    journals = getJournals(lesson.getLessonId());
	}

	if ((lesson == null) || (lesson.getLessonClass() == null) || !lesson.getLessonClass().isStaffMember(user)) {
	    throw new UserAccessDeniedException(
		    "User " + userID + " may not retrieve journal entries for lesson " + lesson.getLessonId());
	} else if (journals == null) {
	    journals = getJournals(lesson.getLessonId());
	}

	request.getSession().setAttribute("journals", journals);
	request.setAttribute("lessonID", lessonID);

	return mapping.findForward(NotebookAction.VIEW_JOURNALS);
    }

    /**
     * 
     * @param lessonID
     *            Lesson to get the journals from.
     * @return List of Journal entries
     */
    private List<NotebookEntry> getJournals(Long lessonID) {
	// initialize service object
	ICoreNotebookService notebookService = getNotebookService();

	if (lessonID == null) {
	    return null;
	}

	return notebookService.getEntry(lessonID, CoreNotebookConstants.SCRATCH_PAD, CoreNotebookConstants.JOURNAL_SIG);

    }

    /**
     * View single notebook entry
     */
    public ActionForward viewEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// initialize service object
	ICoreNotebookService notebookService = getNotebookService();

	DynaActionForm notebookForm = (DynaActionForm) actionForm;
	Long uid = (Long) notebookForm.get("uid");
	String mode = WebUtil.readStrParam(request, "mode", true);

	NotebookEntry entry = notebookService.getEntry(uid);

	if (mode != null) {
	    request.setAttribute("mode", mode);
	}

	if (entry != null) {
	    request.setAttribute("entry", entry);
	}

	return mapping.findForward(NotebookAction.VIEW_SINGLE);
    }

    /**
     * 
     */
    public ActionForward processNewEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// initialize service object
	ICoreNotebookService notebookService = getNotebookService();

	DynaActionForm notebookForm = (DynaActionForm) actionForm;
	Long id = (Long) notebookForm.get(AttributeNames.PARAM_LESSON_ID);
	String title = (String) notebookForm.get("title");
	String entry = (String) notebookForm.get("entry");
	String signature = (String) notebookForm.get("signature");
	Integer userID = LearningWebUtil.getUserId();

	notebookService.createNotebookEntry(id, CoreNotebookConstants.SCRATCH_PAD, signature, userID, title, entry);

	boolean skipViewAll = WebUtil.readBooleanParam(request, "skipViewAll", false);
	return skipViewAll ? null : viewAll(mapping, actionForm, request, response);
    }

    /**
     * 
     */
    public ActionForward updateEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// initialize service object
	ICoreNotebookService notebookService = getNotebookService();

	// get form data
	DynaActionForm notebookForm = (DynaActionForm) actionForm;
	Long uid = (Long) notebookForm.get("uid");
	Long id = (Long) notebookForm.get(AttributeNames.PARAM_LESSON_ID);
	String title = (String) notebookForm.get("title");
	String entry = (String) notebookForm.get("entry");
	String signature = (String) notebookForm.get("signature");

	// get existing entry to edit
	NotebookEntry entryObj = notebookService.getEntry(uid);

	// check entry is being edited by it's owner
	Integer userID = LearningWebUtil.getUserId();
	if (userID != entryObj.getUser().getUserId()) {
	    // throw exception
	}

	// update entry
	entryObj.setTitle(title);
	entryObj.setEntry(entry);
	entryObj.setExternalSignature(signature);

	notebookService.updateEntry(entryObj);

	return viewAll(mapping, actionForm, request, response);

    }

    /**
     * Get AuditService bean.
     * 
     * @return
     */
    private IAuditService getAuditService() {
	if (NotebookAction.auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    NotebookAction.auditService = (IAuditService) ctx.getBean("auditService");
	}
	return NotebookAction.auditService;
    }

}