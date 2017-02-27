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


package org.lamsfoundation.lams.tool.rsrc.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.service.UploadResourceFileException;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.tool.rsrc.web.form.ReflectionForm;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static IResourceService resourceService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	// -----------------------Resource Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("addfile")) {
	    return addItem(mapping, form, request, response);
	}
	if (param.equals("addurl")) {
	    return addItem(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateItem")) {
	    return saveOrUpdateItem(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(ResourceConstants.ERROR);
    }

    /**
     * Initial page for add resource item (single file or URL).
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ResourceItemForm itemForm = (ResourceItemForm) form;
	itemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
	itemForm.setSessionMapID(WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID));
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(ResourceConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the resource and item list and display them on page
	IResourceService service = getResourceService();
	ResourceUser resourceUser = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // resourceUser may be null if the user was force completed.
	    resourceUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    resourceUser = getCurrentUser(service, sessionId);
	}

	List<ResourceItem> items = null;
	Resource resource;
	items = service.getResourceItemsBySessionId(sessionId);
	resource = service.getResourceBySessionId(sessionId);

	// check whehter finish lock is on/off
	boolean lock = resource.getLockWhenFinished() && (resourceUser != null) && resourceUser.isSessionFinished();

	// check whether there is only one resource item and run auto flag is true or not.
	boolean runAuto = false;
	Long runAutoItemUid = null;
	int itemsNumber = 0;
	if (resource.getResourceItems() != null) {
	    itemsNumber = resource.getResourceItems().size();
	    if (resource.isRunAuto() && (itemsNumber == 1)) {
		ResourceItem item = (ResourceItem) resource.getResourceItems().iterator().next();
		// only visible item can be run auto.
		if (!item.isHide()) {
		    runAuto = true;
		    runAutoItemUid = item.getUid();
		}
	    }
	}

	// get notebook entry
	String entryText = new String();
	if (resourceUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ResourceConstants.TOOL_SIGNATURE, resourceUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// basic information
	sessionMap.put(ResourceConstants.ATTR_TITLE, resource.getTitle());
	sessionMap.put(ResourceConstants.ATTR_RESOURCE_INSTRUCTION, resource.getInstructions());
	sessionMap.put(ResourceConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(ResourceConstants.ATTR_LOCK_ON_FINISH, resource.getLockWhenFinished());
	sessionMap.put(ResourceConstants.ATTR_USER_FINISHED,
		(resourceUser != null) && resourceUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(ResourceConstants.ATTR_REFLECTION_ON, resource.isReflectOnActivity());
	sessionMap.put(ResourceConstants.ATTR_REFLECTION_INSTRUCTION, resource.getReflectInstructions());
	sessionMap.put(ResourceConstants.ATTR_REFLECTION_ENTRY, entryText);
	sessionMap.put(ResourceConstants.ATTR_RUN_AUTO, new Boolean(runAuto));

	// add define later support
	if (resource.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	resource.setContentInUse(true);
	resource.setDefineLater(false);
	service.saveOrUpdateResource(resource);

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// init resource item list
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	resourceItemList.clear();
	if (items != null) {
	    // remove hidden items.
	    for (ResourceItem item : items) {
		// becuase in webpage will use this login name. Here is just
		// initial it to avoid session close error in proxy object.
		if (item.getCreateBy() != null) {
		    item.getCreateBy().getLoginName();
		}
		if (!item.isHide()) {
		    resourceItemList.add(item);
		}
	    }
	}

	// set complete flag for display purpose
	if (resourceUser != null) {
	    service.retrieveComplete(resourceItemList, resourceUser);
	}
	sessionMap.put(ResourceConstants.ATTR_RESOURCE, resource);

	if (runAuto) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("viewItem"));
	    redirect.addParameter(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(ResourceConstants.ATTR_TOOL_SESSION_ID, sessionId);
	    redirect.addParameter(ResourceConstants.ATTR_RESOURCE_ITEM_UID, runAutoItemUid);
	    return redirect;
	    
	} else {
	    return mapping.findForward(ResourceConstants.SUCCESS);
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
	String sessionMapID = request.getParameter(ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// auto run mode, when use finish the only one resource item, mark it as complete then finish this activity as
	// well.
	String resourceItemUid = request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID);
	if (resourceItemUid != null) {
	    doComplete(request);
	    // NOTE:So far this flag is useless(31/08/2006).
	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	    request.setAttribute(ResourceConstants.ATTR_RUN_AUTO, true);
	} else {
	    request.setAttribute(ResourceConstants.ATTR_RUN_AUTO, false);
	}

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	IResourceService service = getResourceService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(ResourceConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ResourceApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     * Save file or url resource item into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back SessionMap
	String sessionMapID = request.getParameter(ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);

	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	ResourceItemForm itemForm = (ResourceItemForm) form;
	ActionErrors errors = validateResourceItem(itemForm);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return findForward(itemForm.getItemType(), mapping);
	}
	short type = itemForm.getItemType();

	// create a new ResourceItem
	ResourceItem item = new ResourceItem();
	IResourceService service = getResourceService();
	ResourceUser resourceUser = getCurrentUser(service, sessionId);
	item.setType(type);
	item.setTitle(itemForm.getTitle());
	item.setDescription(itemForm.getDescription());
	item.setCreateDate(new Timestamp(new Date().getTime()));
	item.setCreateByAuthor(false);
	item.setCreateBy(resourceUser);

	// special attribute for URL or FILE
	if (type == ResourceConstants.RESOURCE_TYPE_FILE) {
	    try {
		service.uploadResourceItemFile(item, itemForm.getFile());
	    } catch (UploadResourceFileException e) {
		LearningAction.log.error("Failed upload Resource File " + e.toString());
		return mapping.findForward(ResourceConstants.ERROR);
	    }
	    item.setOpenUrlNewWindow(itemForm.isOpenUrlNewWindow());

	} else if (type == ResourceConstants.RESOURCE_TYPE_URL) {
	    item.setUrl(itemForm.getUrl());
	    item.setOpenUrlNewWindow(itemForm.isOpenUrlNewWindow());
	}
	// save and update session

	ResourceSession resSession = service.getResourceSessionBySessionId(sessionId);
	if (resSession == null) {
	    LearningAction.log.error("Failed update ResourceSession by ID[" + sessionId + "]");
	    return mapping.findForward(ResourceConstants.ERROR);
	}
	Set<ResourceItem> items = resSession.getResourceItems();
	if (items == null) {
	    items = new HashSet<ResourceItem>();
	    resSession.setResourceItems(items);
	}
	items.add(item);
	service.saveOrUpdateResourceSession(resSession);

	// update session value
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	resourceItemList.add(item);

	// URL or file upload
	request.setAttribute(ResourceConstants.ATTR_ADD_RESOURCE_TYPE, new Short(type));
	request.setAttribute(AttributeNames.ATTR_MODE, mode);

	Resource resource = resSession.getResource();
	if (resource.isNotifyTeachersOnAssigmentSumbit()) {
	    service.notifyTeachersOnAssigmentSumbit(sessionId, resourceUser);
	}

	if (resource.isNotifyTeachersOnFileUpload() && (type == ResourceConstants.RESOURCE_TYPE_FILE)) {
	    service.notifyTeachersOnFileUpload(resource.getContentId(), sessionId, sessionMapID,
		    resourceUser.getFirstName() + " " + resourceUser.getLastName(), item.getUid(),
		    itemForm.getFile().getFileName());
	}

	return mapping.findForward(ResourceConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IResourceService submitFilesService = getResourceService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ResourceConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(ResourceConstants.SUCCESS);
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

	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IResourceService service = getResourceService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ResourceConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ResourceConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
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

	IResourceService service = getResourceService();
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

    private IResourceService getResourceService() {
	if (LearningAction.resourceService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LearningAction.resourceService = (IResourceService) wac.getBean(ResourceConstants.RESOURCE_SERVICE);
	}
	return LearningAction.resourceService;
    }

    /**
     * List save current resource items.
     *
     * @param request
     * @return
     */
    private SortedSet<ResourceItem> getResourceItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<ResourceItem> list = (SortedSet<ResourceItem>) sessionMap
		.get(ResourceConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<ResourceItem>(new ResourceItemComparator());
	    sessionMap.put(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, list);
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
    private List getListFromSession(SessionMap sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * Return <code>ActionForward</code> according to resource item type.
     *
     * @param type
     * @param mapping
     * @return
     */
    private ActionForward findForward(short type, ActionMapping mapping) {
	ActionForward forward;
	switch (type) {
	    case ResourceConstants.RESOURCE_TYPE_URL:
		forward = mapping.findForward("url");
		break;
	    case ResourceConstants.RESOURCE_TYPE_FILE:
		forward = mapping.findForward("file");
		break;
	    case ResourceConstants.RESOURCE_TYPE_WEBSITE:
		forward = mapping.findForward("website");
		break;
	    case ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT:
		forward = mapping.findForward("learningobject");
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

    private ResourceUser getCurrentUser(IResourceService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ResourceUser resourceUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

	if (resourceUser == null) {
	    ResourceSession session = service.getResourceSessionBySessionId(sessionId);
	    resourceUser = new ResourceUser(user, session);
	    service.createUser(resourceUser);
	}
	return resourceUser;
    }

    private ResourceUser getSpecifiedUser(IResourceService service, Long sessionId, Integer userId) {
	ResourceUser resourceUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (resourceUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for share resources activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return resourceUser;
    }

    /**
     * @param itemForm
     * @return
     */
    private ActionErrors validateResourceItem(ResourceItemForm itemForm) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(itemForm.getTitle())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ResourceConstants.ERROR_MSG_TITLE_BLANK));
	}

	if (itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_URL) {
	    if (StringUtils.isBlank(itemForm.getUrl())) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ResourceConstants.ERROR_MSG_URL_BLANK));
		// URL validation: Commom URL validate(1.3.0) work not very well: it can not support http://address:port
		// format!!!
		// UrlValidator validator = new UrlValidator();
		// if(!validator.isValid(itemForm.getUrl()))
		// errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_INVALID_URL));
	    }
	}
	// if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE
	// ||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
	// if(StringUtils.isBlank(itemForm.getDescription()))
	// errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_DESC_BLANK));
	// }
	if ((itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE)
		|| (itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT)
		|| (itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_FILE)) {

	    if ((itemForm.getFile() != null) && FileUtil.isExecutableFile(itemForm.getFile().getFileName())) {
		ActionMessage msg = new ActionMessage("error.attachment.executable");
		errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
	    }

	    // validate item size
	    FileValidatorUtil.validateFileSize(itemForm.getFile(), false, errors);

	    // for edit validate: file already exist
	    if (!itemForm.isHasFile()
		    && ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ResourceConstants.ERROR_MSG_FILE_BLANK));
	    }
	}
	return errors;
    }

    /**
     * Set complete flag for given resource item.
     *
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request) {
	// get back sessionMap
	String sessionMapID = request.getParameter(ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	Long resourceItemUid = new Long(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	IResourceService service = getResourceService();
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);
	service.setItemComplete(resourceItemUid, new Long(user.getUserID().intValue()), sessionId);

	// set resource item complete tag
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	for (ResourceItem item : resourceItemList) {
	    if (item.getUid().equals(resourceItemUid)) {
		item.setComplete(true);
		break;
	    }
	}
    }
}