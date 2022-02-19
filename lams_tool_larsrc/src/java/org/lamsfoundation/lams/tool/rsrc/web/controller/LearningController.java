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

package org.lamsfoundation.lams.tool.rsrc.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
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
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/learning")
public class LearningController {
    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private ILamsToolService toolService;

    @Autowired
    @Qualifier("resourceMessageService")
    private MessageService messageService;

    /**
     * Initial page for add resource item (single file or URL).
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addfile")
    private String addfile(ResourceItemForm resourceItemForm, HttpServletRequest request) {
	resourceItemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	resourceItemForm.setSessionMapID(sessionMapID);
	resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);
	request.setAttribute(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		toolService.getLearnerContentFolder(sessionId, user.getUserID().longValue()));

	return "pages/learning/addfile";
    }

    @RequestMapping("/addurl")
    private String addurl(ResourceItemForm resourceItemForm, HttpServletRequest request) {
	resourceItemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	resourceItemForm.setSessionMapID(sessionMapID);

	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);
	request.setAttribute(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		toolService.getLearnerContentFolder(sessionId, user.getUserID().longValue()));

	return "pages/learning/addurl";
    }

    /**
     *
     */
    @RequestMapping("/start")
    private String start(HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(ResourceConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the resource and item list and display them on page
	ResourceUser resourceUser = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // resourceUser may be null if the user was force completed.
	    resourceUser = getSpecifiedUser(resourceService, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    resourceUser = getCurrentUser(resourceService, sessionId);
	}

	List<ResourceItem> items = null;
	Resource resource;
	items = resourceService.getResourceItemsBySessionId(sessionId);
	resource = resourceService.getResourceBySessionId(sessionId);

	// check whether finish lock is on/off
	boolean lock = resource.getLockWhenFinished() && (resourceUser != null) && resourceUser.isSessionFinished();

	// check whether there is only one resource item and run auto flag is true or not.
	boolean runAuto = false;
	Long runAutoItemUid = null;
	if (resource.isRunAuto() && items != null) {
	    int itemsNumber = 0;
	    for (ResourceItem item : items) {
		// only visible item can be run auto.
		if (!item.isHide()) {
		    itemsNumber++;
		    runAutoItemUid = item.getUid();
		}
	    }
	    // can't autorun if there is more than one!
	    if (itemsNumber == 1) {
		runAuto = true;
	    } else {
		runAutoItemUid = null;
	    }
	}

	// get notebook entry
	String entryText = new String();
	if (resourceUser != null) {
	    NotebookEntry notebookEntry = resourceService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
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
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	resource.setContentInUse(true);
	resource.setDefineLater(false);
	resourceService.saveOrUpdateResource(resource);

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, resourceService.isLastActivity(sessionId));

	// init resource item list
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	resourceItemList.clear();
	Collection<Long> itemsToBeRated = new ArrayList<>();
	if (items != null) {
	    // remove hidden items.
	    for (ResourceItem item : items) {
		// because in webpage will use this login name. Here is just
		// initial it to avoid session close error in proxy object.
		if (item.getCreateBy() != null) {
		    item.getCreateBy().getLoginName();
		}
		if (!item.isHide()) {
		    resourceItemList.add(item);
		}
		if (item.isAllowRating()) {
		    itemsToBeRated.add(item.getUid());
		}
	    }
	}

	List<ItemRatingDTO> ratingDTOs = resourceService.getRatingCriteriaDtos(resource.getContentId(), sessionId,
		itemsToBeRated, resourceUser.getUserId());
	for (ItemRatingDTO ratingDTO : ratingDTOs) {
	    for (ResourceItem item : resourceItemList) {
		if (item.getUid().equals(ratingDTO.getItemId())) {
		    item.setRatingDTO(ratingDTO);
		}
	    }
	}
	sessionMap.put(ResourceConstants.ATTR_RATE_ITEMS, itemsToBeRated.size() > 0);

	// set complete flag for display purpose
	if (resourceUser != null) {
	    resourceService.retrieveComplete(resourceItemList, resourceUser);
	}
	int numItemsCompleted = 0;
	for (ResourceItem item : resourceItemList) {
	    if (item.isComplete()) {
		numItemsCompleted++;
	    }
	}
	sessionMap.put(ResourceConstants.ATTR_COMPLETED_SUFFICIENT_TO_FINISH,
		numItemsCompleted >= resource.getMiniViewResourceNumber());

	sessionMap.put(ResourceConstants.ATTR_RESOURCE, resource);

	if (runAuto) {
	    String redirectURL = "redirect:/reviewItem.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, ResourceConstants.ATTR_SESSION_MAP_ID,
		    sessionMap.getSessionID());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, ResourceConstants.ATTR_TOOL_SESSION_ID,
		    sessionId.toString());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, ResourceConstants.ATTR_RESOURCE_ITEM_UID,
		    runAutoItemUid.toString());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.ATTR_MODE, mode.toString());
	    return redirectURL;

	} else {
	    return "pages/learning/learning";
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
    @RequestMapping("/finish")
    private String finish(HttpServletRequest request) {

	// get back SessionMap
	String sessionMapID = request.getParameter(ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

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
	    return "pages/learning/learning";
	}

	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = resourceService.finishToolSession(sessionId, userID);
	    request.setAttribute(ResourceConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ResourceApplicationException e) {
	    LearningController.log.error("Failed get next activity url:" + e.getMessage());
	}

	return "pages/learning/finish";
    }

    /**
     * Save file or url resource item into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @RequestMapping(value = "/saveOrUpdateItem", method = RequestMethod.POST)
    private String saveOrUpdateItem(ResourceItemForm resourceItemForm, HttpServletRequest request)
	    throws ServletException {
	// get back SessionMap
	String sessionMapID = request.getParameter(ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);

	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	validateResourceItem(resourceItemForm, errorMap);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    switch (resourceItemForm.getItemType()) {
		case 1:
		    return "pages/learning/addurl";
		case 2:
		    resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		    return "pages/learning/addfile";
		default:
		    throw new IllegalArgumentException("Unknown item type" + resourceItemForm.getItemType());
	    }
	}
	short type = resourceItemForm.getItemType();

	// create a new ResourceItem
	ResourceItem item = new ResourceItem();
	ResourceUser resourceUser = getCurrentUser(resourceService, sessionId);
	item.setType(type);
	item.setTitle(resourceItemForm.getTitle());
	item.setInstructions(resourceItemForm.getInstructions());
	item.setCreateDate(new Timestamp(new Date().getTime()));
	item.setCreateByAuthor(false);
	item.setCreateBy(resourceUser);

	// special attribute for URL or FILE
	if (type == ResourceConstants.RESOURCE_TYPE_FILE) {
	    File uploadDir = FileUtil.getTmpFileUploadDir(resourceItemForm.getTmpFileUploadId());
	    if (uploadDir.canRead()) {
		File[] files = uploadDir.listFiles();
		if (files.length > 1) {
		    throw new ServletException("Uploaded more than 1 file");
		}

		if (files.length == 0) {
		    throw new ServletException("No file uploaded");
		}
		try {
		    resourceService.uploadResourceItemFile(item, files[0]);

		    FileUtil.deleteTmpFileUploadDir(resourceItemForm.getTmpFileUploadId());
		} catch (UploadResourceFileException e) {
		    errorMap.add("GLOBAL",
			    messageService.getMessage("error.upload.failed", new Object[] { e.getMessage() }));
		    request.setAttribute("errorMap", errorMap);
		    return "pages/learning/addurl";
		}
	    } else {
		throw new ServletException("No file uploaded");
	    }
	} else if (type == ResourceConstants.RESOURCE_TYPE_URL) {
	    item.setUrl(resourceItemForm.getUrl());
	}
	// save and update session
	ResourceSession resSession = resourceService.getResourceSessionBySessionId(sessionId);
	if (resSession == null) {
	    LearningController.log.error("Failed update ResourceSession by ID[" + sessionId + "]");
	    return "error";
	}
	Set<ResourceItem> items = resSession.getResourceItems();
	if (items == null) {
	    items = new HashSet<>();
	    resSession.setResourceItems(items);
	}
	items.add(item);
	resourceService.saveOrUpdateResourceSession(resSession);

	// update session value
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	resourceItemList.add(item);

	// URL or file upload
	request.setAttribute(ResourceConstants.ATTR_ADD_RESOURCE_TYPE, type);
	request.setAttribute(AttributeNames.ATTR_MODE, mode);

	Resource resource = resSession.getResource();
	if (resource.isNotifyTeachersOnAssigmentSumbit()) {
	    resourceService.notifyTeachersOnAssigmentSumbit(item.getUid());
	}

	return "pages/learning/success";
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

    @RequestMapping("/newReflection")
    private String newReflection(ReflectionForm reflectionForm, HttpServletRequest request) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	if (!validateBeforeFinish(request, sessionMapID)) {
	    return "pages/learning/learning";
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	reflectionForm.setUserID(user.getUserID());
	reflectionForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = resourceService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ResourceConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    reflectionForm.setEntryText(entry.getEntry());
	}

	return "/pages/learning/notebook";
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
    @RequestMapping("/submitReflection")
    private String submitReflection(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request) {
	Integer userId = reflectionForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = resourceService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ResourceConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    resourceService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ResourceConstants.TOOL_SIGNATURE, userId, reflectionForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(reflectionForm.getEntryText());
	    entry.setLastModified(new Date());
	    resourceService.updateEntry(entry);
	}
	request.setAttribute("reflectionForm", reflectionForm);

	return finish(request);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	int miniViewFlag = resourceService.checkMiniView(sessionId, userID);
	// if current user view less than reqired view count number, then just return error message.
	if (miniViewFlag > 0) {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    errorMap.add("GLOBAL", messageService.getMessage("lable.learning.minimum.view.number.less",
		    new Object[] { miniViewFlag }));
	    request.setAttribute("errorMap", errorMap);
	    return false;
	}

	return true;
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
	    list = new TreeSet<>(new ResourceItemComparator());
	    sessionMap.put(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, list);
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
	    LearningController.log.error(
		    "Unable to find specified user for share resources activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return resourceUser;
    }

    /**
     * @param resourceItemForm
     * @return
     */
    private void validateResourceItem(ResourceItemForm resourceItemForm, MultiValueMap<String, String> errorMap) {
	if (StringUtils.isBlank(resourceItemForm.getTitle())) {
	    errorMap.add("GLOBAL", messageService.getMessage(ResourceConstants.ERROR_MSG_TITLE_BLANK));
	}

	if (resourceItemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_URL) {
	    if (StringUtils.isBlank(resourceItemForm.getUrl())) {
		errorMap.add("GLOBAL", messageService.getMessage(ResourceConstants.ERROR_MSG_URL_BLANK));
	    }
	}
	if (resourceItemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_FILE) {
	    File uploadDir = FileUtil.getTmpFileUploadDir(resourceItemForm.getTmpFileUploadId());
	    if (uploadDir.canRead()) {
		File[] files = uploadDir.listFiles();
		if (files.length > 1) {
		    errorMap.add("GLOBAL", "Uploaded more than 1 file");
		}

		if (files.length == 0) {
		    errorMap.add("GLOBAL", "No file uploaded");
		}
	    } else {
		errorMap.add("GLOBAL", "No file uploaded");
	    }
	}
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
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	Long resourceItemUid = new Long(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);
	resourceService.setItemComplete(resourceItemUid, new Long(user.getUserID().intValue()), sessionId);

	// set resource item complete tag
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	for (ResourceItem item : resourceItemList) {
	    if (item.getUid().equals(resourceItemUid)) {
		item.setComplete(true);
		break;
	    }
	}
    }

    @RequestMapping("/hideItem")
    private String hideItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long itemUid = WebUtil.readLongParam(request, ResourceConstants.PARAM_RESOURCE_ITEM_UID);

	// get back sessionMap
	String sessionMapID = request.getParameter(ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);
	Long contentId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_CONTENT_ID);
	ResourceItem resourceItem = resourceService.getResourceItemByUid(itemUid);
	if (!resourceItem.isCreateByAuthor()
		&& user.getUserID().longValue() == resourceItem.getCreateBy().getUserId()) {
	    resourceService.setItemVisible(itemUid, sessionId, contentId, false);
	    //open session Map
	} else {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to hide this item");
	}
	return null;
    }
}