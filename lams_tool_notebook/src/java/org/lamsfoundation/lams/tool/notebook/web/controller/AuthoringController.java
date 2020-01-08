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

package org.lamsfoundation.lams.tool.notebook.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger logger = Logger.getLogger(AuthoringController.class);

    @Autowired
    private INotebookService notebookService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     *
     */
    @RequestMapping("")
    protected String unspecified(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	// Extract toolContentID from parameters.
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Notebook with given toolContentID
	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	if (notebook == null) {
	    notebook = notebookService.copyDefaultContent(toolContentID);
	    notebook.setCreateDate(new Date());
	    notebookService.saveOrUpdateNotebook(notebook);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	return readDatabaseData(authoringForm, notebook, request, mode);
    }
    
    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	notebook.setDefineLater(true);
	notebookService.saveOrUpdateNotebook(notebook);

	//audit log the teacher has started editing activity in monitor
	notebookService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, notebook, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Notebook notebook, HttpServletRequest request,
	    ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	// Set up the authForm.
	updateAuthForm(authoringForm, notebook);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(notebook, mode, contentFolderID, toolContentID);
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("authoringForm", authoringForm);

	return "pages/authoring/authoring";
    }

    @RequestMapping(value = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute("authoringForm") AuthoringForm authoringForm,
	    HttpServletRequest request) {
	// TODO need error checking.

	// get authForm and session map.
	SessionMap<String, Object> map = getSessionMap(request, authoringForm);

	// get notebook content.
	Notebook notebook = notebookService
		.getNotebookByContentId((Long) map.get(AuthoringController.KEY_TOOL_CONTENT_ID));

	// update notebook content using form inputs
	updateNotebook(notebook, authoringForm);

	notebookService.releaseConditionsFromCache(notebook);

	Set<NotebookCondition> conditions = notebook.getConditions();
	if (conditions == null) {
	    conditions = new TreeSet<>(new TextSearchConditionComparator());
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

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("authoringForm", authoringForm);

	return "pages/authoring/authoring";
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Notebook content using AuthoringForm inputs.
     *
     * @param authForm
     * @param mode
     * @return
     */
    private void updateNotebook(Notebook notebook, AuthoringForm authForm) {
	notebook.setTitle(authForm.getTitle());
	notebook.setInstructions(authForm.getInstructions());
	notebook.setForceResponse(authForm.isForceResponse());
	notebook.setLockOnFinished(authForm.isLockOnFinished());
	notebook.setAllowRichEditor(authForm.isAllowRichEditor());
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
	authForm.setForceResponse(notebook.isForceResponse());
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

	SessionMap<String, Object> map = new SessionMap<>();

	map.put(AuthoringController.KEY_MODE, mode);
	map.put(AuthoringController.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringController.KEY_TOOL_CONTENT_ID, toolContentID);
	map.put(NotebookConstants.ATTR_DELETED_CONDITION_LIST, new ArrayList<NotebookCondition>());

	SortedSet<NotebookCondition> set = new TreeSet<>(new TextSearchConditionComparator());

	if (notebook.getConditions() != null) {
	    set.addAll(notebook.getConditions());
	}
	map.put(NotebookConstants.ATTR_CONDITION_SET, set);
	return map;
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