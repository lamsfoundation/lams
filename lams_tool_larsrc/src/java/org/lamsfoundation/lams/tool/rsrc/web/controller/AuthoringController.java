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

package org.lamsfoundation.lams.tool.rsrc.web.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceForm;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
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
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger log = Logger.getLogger(AuthoringController.class);

    private static final String ITEM_TYPE = "itemType";

    @Autowired
    private IResourceService resourceService;
    @Autowired
    @Qualifier("resourceMessageService")
    private MessageService messageService;

    /**
     * Remove resource item attachment, such as single file, learning object
     * ect. It is a ajax call and just temporarily remove from page, all
     * permenant change will happen only when user sumbit this resource item
     * again.
     *
     * @param request
     * @return
     */
    @RequestMapping("/removeItemAttachment")
    private String removeItemAttachment(HttpServletRequest request) {
	ResourceItemForm resourceItemForm = new ResourceItemForm();
	resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
	request.setAttribute("resourceItemForm", resourceItemForm);
	return "pages/authoring/parts/itemattachment";
    }

    /**
     * Remove resource item from HttpSession list and update page display. As
     * authoring rule, all persist only happen when user submit whole page. So
     * this remove is just impact HttpSession values.
     */
    @RequestMapping(path = "/removeItem", method = RequestMethod.POST)
    private String removeItem(@ModelAttribute ResourceItemForm resourceItemForm, HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	@SuppressWarnings("deprecation")
	int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);
	    List<ResourceItem> rList = new ArrayList<>(resourceList);
	    ResourceItem item = rList.remove(itemIdx);
	    resourceList.clear();
	    resourceList.addAll(rList);
	    // add to delList
	    List<ResourceItem> delList = getDeletedResourceItemList(sessionMap);
	    delList.add(item);
	}

	return "pages/authoring/parts/itemlist";
    }

    /**
     * Display edit page for existed resource item.
     *
     * @param resourceItemForm
     * @param request
     * @return
     */
    @RequestMapping("/editItemInit")
    private String editItemInit(@ModelAttribute ResourceItemForm resourceItemForm, HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX), -1);
	ResourceItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);
	    List<ResourceItem> rList = new ArrayList<>(resourceList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, resourceItemForm, request);
	    }
	}
	switch (item.getType()) {
	    case 1:
		return "pages/authoring/parts/addurl";
	    case 2:
		if (!resourceItemForm.isHasFile()) {
		    resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		}
		return "pages/authoring/parts/addfile";
	    case 3:
		resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		return "pages/authoring/parts/addwebsite";
	    default:
		throw new IllegalArgumentException("Unknown item type" + item.getType());
	}
    }

    /**
     * Display empty page for new resource item.
     */
    @RequestMapping("/newItemInit")
    private String newItemlInit(@ModelAttribute ResourceItemForm resourceItemForm, HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	resourceItemForm.setSessionMapID(sessionMapID);

	short type = (short) WebUtil.readIntParam(request, AuthoringController.ITEM_TYPE);
	request.setAttribute("resourceItemForm", resourceItemForm);
	switch (type) {
	    case 1:
		return "pages/authoring/parts/addurl";
	    case 2:
		resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		return "pages/authoring/parts/addfile";
	    case 3:
		resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		return "pages/authoring/parts/addwebsite";
	    default:
		throw new IllegalArgumentException("Unknown item type" + type);
	}
    }

    /**
     * This method will get necessary information from resource item form and
     * save or update into <code>HttpSession</code> ResourceItemList. Notice,
     * this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the
     * entire authoring page is being persisted.
     */
    @RequestMapping(path = "/saveOrUpdateItem", method = RequestMethod.POST)
    private String saveOrUpdateItem(@ModelAttribute ResourceItemForm resourceItemForm, HttpServletRequest request) {
	// get instructions:
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	validateResourceItem(resourceItemForm, errorMap);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute("resourceItemForm", resourceItemForm);
	    switch (resourceItemForm.getItemType()) {
		case 1:
		    return "pages/authoring/parts/addurl";
		case 2:
		    resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		    return "pages/authoring/parts/addfile";
		case 3:
		    resourceItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
		    return "pages/authoring/parts/addwebsite";
		default:
		    throw new IllegalArgumentException("Unknown item type" + resourceItemForm.getItemType());
	    }

	}

	try {
	    extractFormToResourceItem(request, resourceItemForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather
	    // then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage(ResourceConstants.ERROR_MSG_UPLOAD_FAILED,
		    new Object[] { e.getMessage() }));
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		switch (resourceItemForm.getItemType()) {
		    case 1:
			return "pages/authoring/parts/addurl";
		    case 2:
			return "pages/authoring/parts/addfile";
		    case 3:
			return "pages/authoring/parts/addwebsite";
		    default:
			throw new IllegalArgumentException("Unknown item type" + resourceItemForm.getItemType());
		}
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, resourceItemForm.getSessionMapID());
	// return null to close this window
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Read resource data from database and put them into HttpSession. It will
     * redirect to init.do directly after this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item
     * lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    @RequestMapping(value = "/start")
    private String start(@ModelAttribute("startForm") ResourceForm startForm, HttpServletRequest request)
	    throws ServletException {

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return readDatabaseData(startForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    private String defineLater(@ModelAttribute("startForm") ResourceForm startForm, HttpServletRequest request)
	    throws ServletException {
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Resource resource = resourceService.getResourceByContentId(contentId);

	resource.setDefineLater(true);
	resourceService.saveOrUpdateResource(resource);

	//audit log the teacher has started editing activity in monitor
	resourceService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());

	return readDatabaseData(startForm, request);
    }

    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(ResourceForm startForm, HttpServletRequest request) throws ServletException {
	// save toolContentID into HTTPSession
	Long contentId = WebUtil.readLongParam(request, ResourceConstants.PARAM_TOOL_CONTENT_ID);

	// get back the resource and item list and display them on page

	List<ResourceItem> items = null;
	Resource resource = null;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	startForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	startForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    resource = resourceService.getResourceByContentId(contentId);
	    // if resource does not exist, try to use default content instead.
	    if (resource == null) {
		resource = resourceService.getDefaultContent(contentId);
		if (resource.getResourceItems() != null) {
		    items = new ArrayList<>(resource.getResourceItems());
		} else {
		    items = null;
		}
	    } else {
		items = resourceService.getAuthoredItems(resource.getUid());
	    }

	    startForm.setResource(resource);
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<>();
	} else {
	    ResourceUser resourceUser = null;
	    // handle system default question: createBy is null, now set it to
	    // current user
	    for (ResourceItem item : items) {
		if (item.getCreateBy() == null) {
		    if (resourceUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			resourceUser = new ResourceUser(user, resource);
		    }
		    item.setCreateBy(resourceUser);
		}
	    }
	}
	// init resource item list
	SortedSet<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	resourceItemList.clear();
	resourceItemList.addAll(items);

	// If there is no order id, set it up
	int i = 1;
	for (ResourceItem resourceItem : resourceItemList) {
	    if (resourceItem.getOrderId() == null || resourceItem.getOrderId() != i) {
		resourceItem.setOrderId(i);
	    }
	    i++;
	}

	sessionMap.put(ResourceConstants.ATTR_RESOURCE_FORM, startForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping("/init")
    private String initPage(@ModelAttribute("startForm") ResourceForm startForm, HttpServletRequest request)
	    throws ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	ResourceForm existForm = (ResourceForm) sessionMap.get(ResourceConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(startForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	startForm.setMode(mode.toString());
	request.setAttribute("authoringForm", startForm);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include
     * all resource item, information etc.
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    private String updateContent(@ModelAttribute("authoringForm") ResourceForm authoringForm,
	    HttpServletRequest request) throws Exception {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	Resource resource = authoringForm.getResource();

	// **********************************Get Resource PO*********************
	Resource resourcePO = resourceService.getResourceByContentId(resource.getContentId());
	if (resourcePO == null) {
	    // new Resource, create it
	    resourcePO = resource;
	    resourcePO.setCreated(new Timestamp(new Date().getTime()));
	    resourcePO.setUpdated(new Timestamp(new Date().getTime()));

	} else {
	    Set<LearnerItemRatingCriteria> criterias = resourcePO.getRatingCriterias();
	    Long uid = resourcePO.getUid();
	    PropertyUtils.copyProperties(resourcePO, resource);

	    // copyProperties() above may result in "collection assigned to two objects in a session" exception
	    resourceService.evict(resource);
	    authoringForm.setResource(null);
	    resource = null;
	    // set back UID && rating criteria
	    resourcePO.setUid(uid);
	    resourcePO.setRatingCriterias(criterias);

	    // if it's a Teacher (from monitor) - change define later status
	    if (mode.isTeacher()) {
		resourcePO.setDefineLater(false);
	    }

	    resourcePO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ResourceUser resourceUser = resourceService.getUserByIDAndContent(user.getUserID().longValue(),
		resourcePO.getContentId());
	if (resourceUser == null) {
	    resourceUser = new ResourceUser(user, resourcePO);
	}

	resourcePO.setCreatedBy(resourceUser);

	// ************************* Handle resource items *******************
	// Handle resource items
	boolean useRatings = false;
	Set<ResourceItem> itemList = new LinkedHashSet<>();
	SortedSet<ResourceItem> topics = getResourceItemList(sessionMap);
	Iterator<ResourceItem> iter = topics.iterator();
	while (iter.hasNext()) {
	    ResourceItem item = iter.next();
	    if (item != null) {
		// This flushs user UID info to message if this user is a new
		// user.
		item.setCreateBy(resourceUser);
		itemList.add(item);
		useRatings = useRatings || item.isAllowRating();
	    }
	}
	resourcePO.setResourceItems(itemList);
	// delete instruction file from database.
	List<ResourceItem> delResourceItemList = getDeletedResourceItemList(sessionMap);
	iter = delResourceItemList.iterator();
	while (iter.hasNext()) {
	    ResourceItem item = iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		resourceService.deleteResourceItem(item.getUid());
	    }
	}
	// handle resource item attachment file:
	List<ResourceItem> delItemAttList = getDeletedItemAttachmentList(sessionMap);
	iter = delItemAttList.iterator();
	while (iter.hasNext()) {
	    ResourceItem delAtt = iter.next();
	    iter.remove();
	}

	// if miniview number is bigger than available items, then set it topics
	// size
	if (resourcePO.getMiniViewResourceNumber() > topics.size()) {
	    resourcePO.setMiniViewResourceNumber(topics.size());
	}
	// **********************************************
	// finally persist resourcePO again

	resourceService.saveOrUpdateResource(resourcePO);

	// Set up rating criteria. Do not delete existing criteria as this will destroy ratings already done
	// if the monitor edits the activity and turns of the criteria temporarily.
	if (useRatings) {
	    if (resourcePO.getRatingCriterias() == null || resourcePO.getRatingCriterias().size() == 0) {
		LearnerItemRatingCriteria newCriteria = resourceService.createRatingCriteria(resourcePO.getContentId());
		if (resourcePO.getRatingCriterias() == null) {
		    resourcePO.setRatingCriterias(new HashSet<LearnerItemRatingCriteria>());
		}
		resourcePO.getRatingCriterias().add(newCriteria);
	    }
	}
	authoringForm.setResource(resourcePO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	return "pages/authoring/authoring";
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    /**
     * List save current resource items.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
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
     * List save deleted resource items, which could be persisted or
     * non-persisted items.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<ResourceItem> getDeletedResourceItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ResourceConstants.ATTR_DELETED_RESOURCE_ITEM_LIST);
    }

    /**
     * If a resource item has attahment file, and the user edit this item and
     * change the attachment to new file, then the old file need be deleted when
     * submitting the whole authoring page. Save the file uuid and version id
     * into ResourceItem object for temporarily use.
     */
    @SuppressWarnings("unchecked")
    private List<ResourceItem> getDeletedItemAttachmentList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ResourceConstants.ATTR_DELETED_RESOURCE_ITEM_ATTACHMENT_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     */
    @SuppressWarnings("rawtypes")
    private List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * This method will populate resource item information to its form for edit
     * use.
     *
     * @param itemIdx
     * @param item
     * @param form
     * @param request
     */
    private void populateItemToForm(int itemIdx, ResourceItem item, ResourceItemForm form, HttpServletRequest request) {
	form.setInstructions(item.getInstructions());
	form.setTitle(item.getTitle());
	form.setUrl(item.getUrl());
	form.setAllowRating(item.isAllowRating());
	form.setAllowComments(item.isAllowComments());
	if (itemIdx >= 0) {
	    form.setItemIndex(String.valueOf(itemIdx));
	}
	if (item.getFileUuid() != null) {
	    form.setFileUuid(item.getFileUuid());
	    form.setFileVersionId(item.getFileVersionId());
	    form.setFileName(item.getFileName());
	    form.setHasFile(true);
	} else {
	    form.setHasFile(false);
	}
    }

    /**
     * Extract web from content to resource item.
     *
     * BE CAREFUL: This method will copy nessary info from request form to a
     * old or new ResourceItem instance. It gets all info EXCEPT
     * ResourceItem.createDate and ResourceItem.createBy, which need be set
     * when persisting this resource item.
     */
    @SuppressWarnings("unchecked")
    private void extractFormToResourceItem(HttpServletRequest request, ResourceItemForm itemForm) throws Exception {

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);
	int itemIdx = NumberUtils.toInt(itemForm.getItemIndex(), -1);
	ResourceItem item = null;

	if (itemIdx == -1) { // add
	    item = new ResourceItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    item.setOrderId(resourceList.size() + 1);

	} else { // edit
	    List<ResourceItem> rList = new ArrayList<>(resourceList);
	    item = rList.get(itemIdx);
	}
	short type = itemForm.getItemType();
	item.setType(itemForm.getItemType());

	if ((type == ResourceConstants.RESOURCE_TYPE_FILE || type == ResourceConstants.RESOURCE_TYPE_WEBSITE)
		&& !itemForm.isHasFile()) {
	    File uploadDir = FileUtil.getTmpFileUploadDir(itemForm.getTmpFileUploadId());
	    if (uploadDir.canRead()) {
		File[] files = uploadDir.listFiles();
		if (files.length > 1) {
		    throw new ServletException("Uploaded more than 1 file");
		}

		if (files.length == 1) {
		    // if it has old file, and upload a new, then save old to deleteList
		    ResourceItem delAttItem = new ResourceItem();
		    boolean hasOld = false;
		    if (item.getFileUuid() != null) {
			hasOld = true;
			// be careful, This new ResourceItem object never be save
			// into database
			// just temporarily use for saving fileUuid and versionID
			// use:
			delAttItem.setFileUuid(item.getFileUuid());
			delAttItem.setFileVersionId(item.getFileVersionId());
		    }

		    File file = files[0];

		    resourceService.uploadResourceItemFile(item, file);

		    FileUtil.deleteTmpFileUploadDir(itemForm.getTmpFileUploadId());

		    // put it after "upload" to ensure deleted file added into list
		    // only no exception happens during upload
		    if (hasOld) {
			List<ResourceItem> delAtt = getDeletedItemAttachmentList(sessionMap);
			delAtt.add(delAttItem);
		    }
		}
	    }
	}

	item.setTitle(itemForm.getTitle());
	item.setCreateByAuthor(true);
	item.setHide(false);
	item.setAllowRating(itemForm.isAllowRating());
	item.setAllowComments(itemForm.isAllowComments());
	if (type == ResourceConstants.RESOURCE_TYPE_URL) {
	    item.setUrl(itemForm.getUrl());
	}
	item.setInstructions(itemForm.getInstructions());

	// if it's a new item, add it to resourceList
	if (itemIdx == -1) {
	    resourceList.add(item);
	}
    }

    /**
     * Vaidate resource item regards to their type (url/file/learning
     * object/website zip file)
     *
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
    }

    @RequestMapping("/switchResourceItemPosition")
    private String switchResourceItemPosition(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	int resourceItemOrderID1 = WebUtil.readIntParam(request, "resourceItemOrderID1");
	int resourceItemOrderID2 = WebUtil.readIntParam(request, "resourceItemOrderID2");

	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);

	for (ResourceItem item : resourceList) {
	    if (item.getOrderId() == resourceItemOrderID1) {
		item.setOrderId(resourceItemOrderID2);
		continue;
	    }
	    if (item.getOrderId() == resourceItemOrderID2) {
		item.setOrderId(resourceItemOrderID1);
		continue;
	    }
	}

	SortedSet<ResourceItem> newItems = new TreeSet<>(new ResourceItemComparator());
	newItems.addAll(resourceList);
	sessionMap.put(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, newItems);

	// return null to close this window
	return "pages/authoring/parts/itemlist";
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }
}