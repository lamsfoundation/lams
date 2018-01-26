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


package org.lamsfoundation.lams.tool.commonCartridge.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeSession;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.tool.commonCartridge.service.CommonCartridgeApplicationException;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeItemComparator;
import org.lamsfoundation.lams.tool.commonCartridge.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	// -----------------------CommonCartridge Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(CommonCartridgeConstants.ERROR);
    }

    /**
     * Read commonCartridge data from database and put them into HttpSession. It will redirect to init.do directly after
     * this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(CommonCartridgeConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the commonCartridge and item list and display them on page
	ICommonCartridgeService service = getCommonCartridgeService();
	CommonCartridgeUser commonCartridgeUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // commonCartridgeUser may be null if the user was force completed.
	    commonCartridgeUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    commonCartridgeUser = getCurrentUser(service, sessionId);
	}

	CommonCartridge commonCartridge = service.getCommonCartridgeBySessionId(sessionId);
	List<CommonCartridgeItem> items = new ArrayList<CommonCartridgeItem>();
	items.addAll(commonCartridge.getCommonCartridgeItems());

	// check whehter finish lock is on/off
	boolean lock = commonCartridge.getLockWhenFinished() && commonCartridgeUser != null
		&& commonCartridgeUser.isSessionFinished();

	// check whether there is only one commonCartridge item and run auto flag is true or not.
	boolean runAuto = false;
	Long runAutoItemUid = null;
	int itemsNumber = 0;
	if (commonCartridge.getCommonCartridgeItems() != null) {
	    itemsNumber = commonCartridge.getCommonCartridgeItems().size();
	    if (commonCartridge.isRunAuto() && itemsNumber == 1) {
		CommonCartridgeItem item = (CommonCartridgeItem) commonCartridge.getCommonCartridgeItems().iterator()
			.next();
		// only visible item can be run auto.
		if (!item.isHide()) {
		    runAuto = true;
		    runAutoItemUid = item.getUid();
		}
	    }
	}

	// get notebook entry
	String entryText = new String();
	if (commonCartridgeUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    CommonCartridgeConstants.TOOL_SIGNATURE, commonCartridgeUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// basic information
	sessionMap.put(CommonCartridgeConstants.ATTR_TITLE, commonCartridge.getTitle());
	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_INSTRUCTION, commonCartridge.getInstructions());
	sessionMap.put(CommonCartridgeConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(CommonCartridgeConstants.ATTR_LOCK_ON_FINISH, commonCartridge.getLockWhenFinished());
	sessionMap.put(CommonCartridgeConstants.ATTR_USER_FINISHED,
		commonCartridgeUser != null && commonCartridgeUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(CommonCartridgeConstants.ATTR_REFLECTION_ON, commonCartridge.isReflectOnActivity());
	sessionMap.put(CommonCartridgeConstants.ATTR_REFLECTION_INSTRUCTION, commonCartridge.getReflectInstructions());
	sessionMap.put(CommonCartridgeConstants.ATTR_REFLECTION_ENTRY, entryText);
	sessionMap.put(CommonCartridgeConstants.ATTR_RUN_AUTO, new Boolean(runAuto));

	// add define later support
	if (commonCartridge.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	commonCartridge.setContentInUse(true);
	commonCartridge.setDefineLater(false);
	service.saveOrUpdateCommonCartridge(commonCartridge);

	ActivityPositionDTO activityPosition = WebUtil.putActivityPositionInRequestByToolSessionId(sessionId, request,
		getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// init commonCartridge item list
	SortedSet<CommonCartridgeItem> commonCartridgeItemList = getCommonCartridgeItemList(sessionMap);
	commonCartridgeItemList.clear();
	if (items != null) {
	    // remove hidden items.
	    for (CommonCartridgeItem item : items) {
		// becuase in webpage will use this login name. Here is just
		// initial it to avoid session close error in proxy object.
		if (item.getCreateBy() != null) {
		    item.getCreateBy().getLoginName();
		}
		if (!item.isHide()) {
		    commonCartridgeItemList.add(item);
		}
	    }
	}

	// set complete flag for display purpose
	if (commonCartridgeUser != null) {
	    service.retrieveComplete(commonCartridgeItemList, commonCartridgeUser);
	}
	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE, commonCartridge);

	if (runAuto) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("viewItem"));
	    redirect.addParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(CommonCartridgeConstants.ATTR_TOOL_SESSION_ID, sessionId);
	    redirect.addParameter(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_UID, runAutoItemUid);
	    redirect.addParameter(CentralConstants.PARAM_MODE, mode.toString());
	    return redirect;
	    
	} else {
	    return mapping.findForward(CommonCartridgeConstants.SUCCESS);
	}
    }

    /**
     * Finish learning session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// auto run mode, when use finish the only one commonCartridge item, mark it as complete then finish this activity as
	// well.
	String commonCartridgeItemUid = request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);
	if (commonCartridgeItemUid != null) {
	    doComplete(request);
	    // NOTE:So far this flag is useless(31/08/2006).
	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	    request.setAttribute(CommonCartridgeConstants.ATTR_RUN_AUTO, true);
	} else {
	    request.setAttribute(CommonCartridgeConstants.ATTR_RUN_AUTO, false);
	}

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ICommonCartridgeService service = getCommonCartridgeService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(CommonCartridgeConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (CommonCartridgeApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(CommonCartridgeConstants.SUCCESS);
    }

    /**
     * Display empty reflection form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	ICommonCartridgeService submitFilesService = getCommonCartridgeService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		CommonCartridgeConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(CommonCartridgeConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ICommonCartridgeService service = getCommonCartridgeService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		CommonCartridgeConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    CommonCartridgeConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	ICommonCartridgeService service = getCommonCartridgeService();
	int miniViewFlag = service.checkMiniView(sessionId, userID);
	// if current user view less than reqired view count number, then just return error message.
	if (miniViewFlag > 0) {
	    ActionErrors errors = new ActionErrors();
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("lable.learning.minimum.view.number.less", miniViewFlag));
	    this.addErrors(request, errors);
	    return false;
	}

	return true;
    }

    private ICommonCartridgeService getCommonCartridgeService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ICommonCartridgeService) wac.getBean(CommonCartridgeConstants.RESOURCE_SERVICE);
    }

    /**
     * List save current commonCartridge items.
     *
     * @param request
     * @return
     */
    private SortedSet<CommonCartridgeItem> getCommonCartridgeItemList(SessionMap sessionMap) {
	SortedSet<CommonCartridgeItem> list = (SortedSet<CommonCartridgeItem>) sessionMap
		.get(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<CommonCartridgeItem>(new CommonCartridgeItemComparator());
	    sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    private CommonCartridgeUser getCurrentUser(ICommonCartridgeService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	CommonCartridgeUser commonCartridgeUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),
		sessionId);

	if (commonCartridgeUser == null) {
	    CommonCartridgeSession session = service.getCommonCartridgeSessionBySessionId(sessionId);
	    commonCartridgeUser = new CommonCartridgeUser(user, session);
	    service.createUser(commonCartridgeUser);
	}
	return commonCartridgeUser;
    }

    private CommonCartridgeUser getSpecifiedUser(ICommonCartridgeService service, Long sessionId, Integer userId) {
	CommonCartridgeUser commonCartridgeUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (commonCartridgeUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for commonCartridge activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return commonCartridgeUser;
    }

    /**
     * Set complete flag for given commonCartridge item.
     *
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request) {
	// get back sessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	Long commonCartridgeItemUid = new Long(request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID));
	ICommonCartridgeService service = getCommonCartridgeService();
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(CommonCartridgeConstants.ATTR_TOOL_SESSION_ID);
	service.setItemComplete(commonCartridgeItemUid, new Long(user.getUserID().intValue()), sessionId);

	// set commonCartridge item complete tag
	SortedSet<CommonCartridgeItem> commonCartridgeItemList = getCommonCartridgeItemList(sessionMap);
	for (CommonCartridgeItem item : commonCartridgeItemList) {
	    if (item.getUid().equals(commonCartridgeItemUid)) {
		item.setComplete(true);
		break;
	    }
	}
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (MessageService) wac.getBean("basicLTIMessageService");
    }

}
