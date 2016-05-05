/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.notebook.web.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.service.NotebookServiceProxy;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 * @version
 *
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch" scope="request" validate="false"
 *
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public INotebookService notebookService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     *
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, "mode", true);

	// set up notebookService
	if (notebookService == null) {
	    notebookService = NotebookServiceProxy.getNotebookService(this.getServlet().getServletContext());
	}

	// retrieving Notebook with given toolContentID
	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	if (notebook == null) {
	    notebook = notebookService.copyDefaultContent(toolContentID);
	    notebook.setCreateDate(new Date());
	    notebookService.saveOrUpdateNotebook(notebook);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content while we are editing. This flag is released
	    // when updateContent is called.
	    notebook.setDefineLater(true);
	    notebookService.saveOrUpdateNotebook(notebook);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	updateAuthForm(authForm, notebook);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(notebook, getAccessMode(request), contentFolderID,
		toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get notebook content.
	Notebook notebook = notebookService.getNotebookByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update notebook content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringAction.KEY_MODE);
	updateNotebook(notebook, authForm, mode);

	notebookService.releaseConditionsFromCache(notebook);

	Set<NotebookCondition> conditions = notebook.getConditions();
	if (conditions == null) {
	    conditions = new TreeSet<NotebookCondition>(new TextSearchConditionComparator());
	}
	SortedSet<NotebookCondition> conditionSet = (SortedSet<NotebookCondition>) map
		.get(NotebookConstants.ATTR_CONDITION_SET);
	conditions.addAll(conditionSet);

	List<NotebookCondition> deletedConditionList = (List<NotebookCondition>) map
		.get(NotebookConstants.ATTR_DELETED_CONDITION_LIST);
	if (deletedConditionList != null) {
	    for (NotebookCondition condition : deletedConditionList) {
		// remove from db, leave in repository
		conditions.remove(condition);
		notebookService.deleteCondition(condition);
	    }
	}

	// set conditions in case it didn't exist
	notebook.setConditions(conditionSet);

	// set the update date
	notebook.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	notebook.setDefineLater(false);

	notebookService.saveOrUpdateNotebook(notebook);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Notebook content using AuthoringForm inputs.
     *
     * @param authForm
     * @param mode
     * @return
     */
    private void updateNotebook(Notebook notebook, AuthoringForm authForm, ToolAccessMode mode) {
	notebook.setTitle(authForm.getTitle());
	notebook.setInstructions(authForm.getInstructions());
	if (mode.isAuthor()) { // Teacher cannot modify following
	    notebook.setLockOnFinished(authForm.isLockOnFinished());
	    notebook.setAllowRichEditor(authForm.isAllowRichEditor());

	}
    }

    /**
     * Updates AuthoringForm using Notebook content.
     *
     * @param notebook
     * @param authForm
     * @return
     */
    private void updateAuthForm(AuthoringForm authForm, Notebook notebook) {
	authForm.setTitle(notebook.getTitle());
	authForm.setInstructions(notebook.getInstructions());
	authForm.setLockOnFinished(notebook.isLockOnFinished());
	authForm.setAllowRichEditor(notebook.isAllowRichEditor());

    }

    /**
     * Updates SessionMap using Notebook content.
     *
     * @param notebook
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Notebook notebook, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(AuthoringAction.KEY_MODE, mode);
	map.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);
	map.put(NotebookConstants.ATTR_DELETED_CONDITION_LIST, new ArrayList<NotebookCondition>());

	SortedSet<NotebookCondition> set = new TreeSet<NotebookCondition>(new TextSearchConditionComparator());

	if (notebook.getConditions() != null) {
	    set.addAll(notebook.getConditions());
	}
	map.put(NotebookConstants.ATTR_CONDITION_SET, set);
	return map;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     *
     * @param request
     * @param authForm
     * @return
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }

    /**
     * Lists deleted Notebook conditions, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedNotebookConditionList(SessionMap sessionMap) {
	List list = (List) sessionMap.get(NotebookConstants.ATTR_DELETED_CONDITION_LIST);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(NotebookConstants.ATTR_DELETED_CONDITION_LIST, list);
	}
	return list;

    }
}