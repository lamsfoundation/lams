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

package org.lamsfoundation.lams.tool.commonCartridge.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.tool.commonCartridge.service.CommonCartridgeApplicationException;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.service.UploadCommonCartridgeFileException;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeItemComparator;
import org.lamsfoundation.lams.tool.commonCartridge.web.form.CommonCartridgeForm;
import org.lamsfoundation.lams.tool.commonCartridge.web.form.CommonCartridgeItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.FileValidatorUtil;
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
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger log = Logger.getLogger(AuthoringController.class);
    private static final String ITEM_TYPE = "itemType";

    @Autowired
    private ICommonCartridgeService commonCartridgeService;

    @Autowired
    @Qualifier("commonCartridgeMessageService")
    private MessageService messageService;

    /**
     * Remove commonCartridge item from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/removeItem")
    private String removeItem(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(CommonCartridgeConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<CommonCartridgeItem> commonCartridgeList = getCommonCartridgeItemList(sessionMap);
	    List<CommonCartridgeItem> rList = new ArrayList<>(commonCartridgeList);
	    CommonCartridgeItem item = rList.remove(itemIdx);
	    commonCartridgeList.clear();
	    commonCartridgeList.addAll(rList);
	    // add to delList
	    List delList = getDeletedCommonCartridgeItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Display edit page for existed commonCartridge item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("/editItemInit")
    private String editItemInit(
	    @ModelAttribute("commonCartridgeItemForm") CommonCartridgeItemForm commonCartridgeItemForm,
	    HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(CommonCartridgeConstants.PARAM_ITEM_INDEX), -1);
	CommonCartridgeItem item = null;
	if (itemIdx != -1) {
	    SortedSet<CommonCartridgeItem> commonCartridgeList = getCommonCartridgeItemList(sessionMap);
	    List<CommonCartridgeItem> rList = new ArrayList<>(commonCartridgeList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, commonCartridgeItemForm, request);
	    }
	}
	return findForward(item == null ? -1 : item.getType());
    }

    /**
     * Display empty page for new commonCartridge item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/newItemInit")
    private String newItemInit(
	    @ModelAttribute("commonCartridgeItemForm") CommonCartridgeItemForm commonCartridgeItemForm,
	    HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	commonCartridgeItemForm.setSessionMapID(sessionMapID);

	short type = (short) NumberUtils.stringToInt(request.getParameter(AuthoringController.ITEM_TYPE));
	return findForward(type);
    }

    /**
     * This method will get necessary information from commonCartridge item form and save or update into
     * <code>HttpSession</code> CommonCartridgeItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @RequestMapping(path = "/saveOrUpdateItem", method = RequestMethod.POST)
    private String saveOrUpdateItem(
	    @ModelAttribute("commonCartridgeItemForm") CommonCartridgeItemForm commonCartridgeItemForm,
	    HttpServletRequest request) {
	MultiValueMap<String, String> errorMap = validateCommonCartridgeItem(commonCartridgeItemForm);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return findForward(commonCartridgeItemForm.getItemType());
	}

	short type = commonCartridgeItemForm.getItemType();
	try {
	    if (type == CommonCartridgeConstants.RESOURCE_TYPE_COMMON_CARTRIDGE) {
		uploadCommonCartridge(request, commonCartridgeItemForm);
	    } else {
		extractFormToCommonCartridgeItem(request, commonCartridgeItemForm);
	    }
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage(CommonCartridgeConstants.ERROR_MSG_UPLOAD_FAILED,
		    new Object[] { e.getMessage() }));
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		return findForward(commonCartridgeItemForm.getItemType());
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, commonCartridgeItemForm.getSessionMapID());
	// return null to close this window

	if (type == CommonCartridgeConstants.RESOURCE_TYPE_COMMON_CARTRIDGE) {
	    return "pages/authoring/parts/selectResources";
	} else {
	    return "pages/authoring/parts/itemlist";
	}
    }

    /**
     *
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @RequestMapping("/selectResources")
    private String selectResources(HttpServletRequest request) {
	//count uploaded resources
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	List<CommonCartridgeItem> uploadedCartridgeResources = getUploadedCartridgeResources(sessionMap);
	int countUploadedResources = uploadedCartridgeResources.size();

	SortedSet<CommonCartridgeItem> items = getCommonCartridgeItemList(sessionMap);

	for (int i = 0; i < countUploadedResources; i++) {
	    String itemStr = request.getParameter(CommonCartridgeConstants.ATTR_ITEM + i);
	    if (StringUtils.isBlank(itemStr)) {
		continue;
	    }

	    CommonCartridgeItem resource = uploadedCartridgeResources.get(i);

	    String launchUrl = request.getParameter(CommonCartridgeConstants.ATTR_LAUNCH_URL + i);
	    resource.setLaunchUrl(launchUrl);
	    String secureLaunchUrl = request.getParameter(CommonCartridgeConstants.ATTR_SECURE_LAUNCH_URL + i);
	    resource.setSecureLaunchUrl(secureLaunchUrl);
	    String toolKey = request.getParameter(CommonCartridgeConstants.ATTR_REMOTE_TOOL_KEY + i);
	    resource.setKey(toolKey);
	    String toolSecret = request.getParameter(CommonCartridgeConstants.ATTR_REMOTE_TOOL_SECRET + i);
	    resource.setSecret(toolSecret);
	    String buttonText = request.getParameter(CommonCartridgeConstants.ATTR_BUTTON_TEXT + i);
	    resource.setButtonText(buttonText);
	    String isOpenUrlNewWindow = request.getParameter(CommonCartridgeConstants.ATTR_OPEN_URL_NEW_WINDOW + i);
	    resource.setOpenUrlNewWindow(isOpenUrlNewWindow != null);
	    int frameHeight = WebUtil.readIntParam(request, CommonCartridgeConstants.ATTR_FRAME_HEIGHT + i, true);
	    resource.setFrameHeight(frameHeight);

	    //add selected resource to item list
	    items.add(resource);
	}

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	// return null to close this window

	return "pages/authoring/parts/itemlist";

    }

    /**
     * Read commonCartridge data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    @RequestMapping("/start")
    private String start(@ModelAttribute("authoringForm") CommonCartridgeForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return starting(authoringForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    private String definelater(@ModelAttribute("authoringForm") CommonCartridgeForm authoringForm,
	    HttpServletRequest request) throws ServletException {
	// update define later flag to true
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	CommonCartridge commonCartridge = commonCartridgeService.getCommonCartridgeByContentId(contentId);

	commonCartridge.setDefineLater(true);
	commonCartridgeService.saveOrUpdateCommonCartridge(commonCartridge);

	//audit log the teacher has started editing activity in monitor
	commonCartridgeService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return starting(authoringForm, request);
    }

    private String starting(@ModelAttribute("authoringForm") CommonCartridgeForm authoringForm,
	    HttpServletRequest request) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_TOOL_CONTENT_ID));

	// get back the commonCartridge and item list and display them on page

	List<CommonCartridgeItem> items = null;
	CommonCartridge commonCartridge = null;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	authoringForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	authoringForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    commonCartridge = commonCartridgeService.getCommonCartridgeByContentId(contentId);
	    // if commonCartridge does not exist, try to use default content instead.
	    if (commonCartridge == null) {
		commonCartridge = commonCartridgeService.getDefaultContent(contentId);
		if (commonCartridge.getCommonCartridgeItems() != null) {
		    items = new ArrayList<>(commonCartridge.getCommonCartridgeItems());
		} else {
		    items = null;
		}
	    } else {
		items = commonCartridgeService.getAuthoredItems(commonCartridge.getUid());
	    }

	    authoringForm.setCommonCartridge(commonCartridge);
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<>();
	} else {
	    CommonCartridgeUser commonCartridgeUser = null;
	    // handle system default question: createBy is null, now set it to current user
	    for (CommonCartridgeItem item : items) {
		if (item.getCreateBy() == null) {
		    if (commonCartridgeUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			commonCartridgeUser = new CommonCartridgeUser(user, commonCartridge);
		    }
		    item.setCreateBy(commonCartridgeUser);
		}
	    }
	}
	// init commonCartridge item list
	SortedSet<CommonCartridgeItem> commonCartridgeItemList = getCommonCartridgeItemList(sessionMap);
	commonCartridgeItemList.clear();
	commonCartridgeItemList.addAll(items);

	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_FORM, authoringForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @RequestMapping("/init")
    private String initPage(@ModelAttribute("authoringForm") CommonCartridgeForm authoringForm,
	    HttpServletRequest request) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	CommonCartridgeForm existForm = (CommonCartridgeForm) sessionMap
		.get(CommonCartridgeConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(authoringForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	authoringForm.setMode(mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all commonCartridge item, information
     * etc.
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    private String updateContent(@ModelAttribute("authoringForm") CommonCartridgeForm authoringForm,
	    HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	CommonCartridge commonCartridge = authoringForm.getCommonCartridge();

	// **********************************Get CommonCartridge PO*********************
	CommonCartridge commonCartridgePO = commonCartridgeService
		.getCommonCartridgeByContentId(authoringForm.getCommonCartridge().getContentId());
	if (commonCartridgePO == null) {
	    // new CommonCartridge, create it.
	    commonCartridgePO = commonCartridge;
	    commonCartridgePO.setCreated(new Timestamp(new Date().getTime()));
	    commonCartridgePO.setUpdated(new Timestamp(new Date().getTime()));

	} else {
	    Long uid = commonCartridgePO.getUid();
	    PropertyUtils.copyProperties(commonCartridgePO, commonCartridge);
	    // get back UID
	    commonCartridgePO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		commonCartridgePO.setDefineLater(false);
	    }

	    commonCartridgePO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	CommonCartridgeUser commonCartridgeUser = commonCartridgeService.getUserByIDAndContent(
		new Long(user.getUserID().intValue()), authoringForm.getCommonCartridge().getContentId());
	if (commonCartridgeUser == null) {
	    commonCartridgeUser = new CommonCartridgeUser(user, commonCartridgePO);
	}

	commonCartridgePO.setCreatedBy(commonCartridgeUser);

	// ************************* Handle commonCartridge items *******************

	Set itemList = new LinkedHashSet();
	SortedSet topics = getCommonCartridgeItemList(sessionMap);
	Iterator iter = topics.iterator();
	while (iter.hasNext()) {
	    CommonCartridgeItem item = (CommonCartridgeItem) iter.next();
	    if (item != null) {
		// This flushs user UID info to message if this user is a new user.
		item.setCreateBy(commonCartridgeUser);
		itemList.add(item);
	    }
	}
	commonCartridgePO.setCommonCartridgeItems(itemList);
	// delete instructino file from database.
	List delCommonCartridgeItemList = getDeletedCommonCartridgeItemList(sessionMap);
	iter = delCommonCartridgeItemList.iterator();
	while (iter.hasNext()) {
	    CommonCartridgeItem item = (CommonCartridgeItem) iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		commonCartridgeService.deleteCommonCartridgeItem(item.getUid());
	    }
	}

	// if miniview number is bigger than available items, then set it topics size
	if (commonCartridgePO.getMiniViewCommonCartridgeNumber() > topics.size()) {
	    commonCartridgePO.setMiniViewCommonCartridgeNumber(topics.size());
	}
	// **********************************************
	// finally persist commonCartridgePO again
	commonCartridgeService.saveOrUpdateCommonCartridge(commonCartridgePO);

	authoringForm.setCommonCartridge(commonCartridgePO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return "pages/authoring/authoring";
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * List items from fresh uploaded cartridge.
     *
     * @param request
     * @return
     */
    private List<CommonCartridgeItem> getUploadedCartridgeResources(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, "uploadedCartridgeResources");
    }

    /**
     * List save current commonCartridge items.
     *
     * @param request
     * @return
     */
    private SortedSet<CommonCartridgeItem> getCommonCartridgeItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<CommonCartridgeItem> list = (SortedSet<CommonCartridgeItem>) sessionMap
		.get(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new CommonCartridgeItemComparator());
	    sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted commonCartridge items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedCommonCartridgeItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, CommonCartridgeConstants.ATTR_DELETED_RESOURCE_ITEM_LIST);
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

    /**
     * Get back relative <code>ActionForward</code> from request.
     *
     * @param type
     * @param mapping
     * @return
     */
    private String findForward(short type) {
	String forward;
	switch (type) {
	    case CommonCartridgeConstants.RESOURCE_TYPE_BASIC_LTI:
		forward = "pages/authoring/parts/addbasiclti";
		break;
	    case CommonCartridgeConstants.RESOURCE_TYPE_COMMON_CARTRIDGE:
		forward = "pages/authoring/parts/addcommoncartridge";
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

    /**
     * This method will populate commonCartridge item information to its form for edit use.
     *
     * @param itemIdx
     * @param item
     * @param form
     * @param request
     */
    private void populateItemToForm(int itemIdx, CommonCartridgeItem item, CommonCartridgeItemForm form,
	    HttpServletRequest request) {
	form.setDescription(item.getDescription());
	form.setTitle(item.getTitle());
	if (itemIdx >= 0) {
	    form.setItemIndex(new Integer(itemIdx).toString());
	}

	if (StringUtils.isBlank(item.getLaunchUrl()) && StringUtils.isNotBlank(item.getSecureLaunchUrl())) {
	    form.setUrl(item.getSecureLaunchUrl());
	} else {
	    form.setUrl(item.getLaunchUrl());
	}
	form.setKey(item.getKey());
	form.setSecret(item.getSecret());
	form.setCustomStr(item.getCustomStr());
	form.setButtonText(item.getButtonText());
	form.setOpenUrlNewWindow(item.isOpenUrlNewWindow());
	form.setFrameHeight(item.getFrameHeight());

	// FOR requirment from LDEV-754
	// add extra blank line for instructions
	// for(int idx=0;idx<INIT_INSTRUCTION_COUNT;idx++){
	// instructions.add("");
	// }
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
     * Uploads common cartridge and parses it to commonCartridge items.
     *
     * @param request
     * @param itemForm
     * @throws UploadCommonCartridgeFileException
     */
    private void uploadCommonCartridge(HttpServletRequest request, CommonCartridgeItemForm itemForm)
	    throws UploadCommonCartridgeFileException {

	// if the item is edit (not new add) then the getFile may return null
	// it may throw exception, so put it as first, to avoid other invlidate update:
	List<CommonCartridgeItem> items = null;
	if (itemForm.getFile() != null) {
	    try {
		CommonCartridgeItem itemTemp = new CommonCartridgeItem();
		items = commonCartridgeService.uploadCommonCartridgeFile(itemTemp, itemForm.getFile());
	    } catch (UploadCommonCartridgeFileException e) {
		throw e;
	    }
	}

	for (CommonCartridgeItem item : items) {
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    item.setType(CommonCartridgeConstants.RESOURCE_TYPE_BASIC_LTI);
	    item.setCreateByAuthor(true);
	    item.setHide(false);
	    //item.setDescription(itemForm.getDescription());
	}

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(itemForm.getSessionMapID());
	List<CommonCartridgeItem> uploadedCartridgeResources = getUploadedCartridgeResources(sessionMap);
	uploadedCartridgeResources.clear();
	uploadedCartridgeResources.addAll(items);
    }

    /**
     * Extract web from content to commonCartridge item.
     *
     * @param request
     * @param itemForm
     * @throws CommonCartridgeApplicationException
     */
    private void extractFormToCommonCartridgeItem(HttpServletRequest request, CommonCartridgeItemForm itemForm)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to a old or new CommonCartridgeItem
	 * instance. It gets all info EXCEPT CommonCartridgeItem.createDate and CommonCartridgeItem.createBy, which need
	 * be set when persisting this commonCartridge item.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<CommonCartridgeItem> commonCartridgeList = getCommonCartridgeItemList(sessionMap);
	int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(), -1);
	CommonCartridgeItem item = null;

	if (itemIdx == -1) { // add
	    item = new CommonCartridgeItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    commonCartridgeList.add(item);
	} else { // edit
	    List<CommonCartridgeItem> rList = new ArrayList<>(commonCartridgeList);
	    item = rList.get(itemIdx);
	}
	short type = itemForm.getItemType();
	item.setType(itemForm.getItemType());

	item.setTitle(itemForm.getTitle());
	item.setCreateByAuthor(true);
	item.setHide(false);

	if (type == CommonCartridgeConstants.RESOURCE_TYPE_BASIC_LTI) {

	    if (StringUtils.isBlank(item.getLaunchUrl()) && StringUtils.isNotBlank(item.getSecureLaunchUrl())) {
		item.setSecureLaunchUrl(itemForm.getUrl());
	    } else {
		item.setLaunchUrl(itemForm.getUrl());
	    }

	    item.setKey(itemForm.getKey());
	    item.setSecret(itemForm.getSecret());
	    item.setButtonText(itemForm.getButtonText());
	    item.setOpenUrlNewWindow(itemForm.isOpenUrlNewWindow());
	    item.setFrameHeight(itemForm.getFrameHeight());
	    item.setCustomStr(itemForm.getCustomStr());
	}
	// if(type == CommonCartridgeConstants.RESOURCE_TYPE_WEBSITE
	// ||itemForm.getItemType() == CommonCartridgeConstants.RESOURCE_TYPE_LEARNING_OBJECT){
	item.setDescription(itemForm.getDescription());
	// }

    }

    /**
     * Vaidate commonCartridge item regards to their type (url/file/learning object/website zip file)
     *
     * @param itemForm
     * @return
     */
    private MultiValueMap<String, String> validateCommonCartridgeItem(CommonCartridgeItemForm itemForm) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if (itemForm.getItemType() == CommonCartridgeConstants.RESOURCE_TYPE_BASIC_LTI) {
	    if (StringUtils.isBlank(itemForm.getTitle())) {
		errorMap.add("GLOBAL", messageService.getMessage(CommonCartridgeConstants.ERROR_MSG_TITLE_BLANK));
	    }

	    if (StringUtils.isBlank(itemForm.getUrl())) {
		errorMap.add("GLOBAL", messageService.getMessage(CommonCartridgeConstants.ERROR_MSG_URL_BLANK));
		// URL validation: Commom URL validate(1.3.0) work not very well: it can not support http://
		// address:port format!!!
		// UrlValidator validator = new UrlValidator();
		// if(!validator.isValid(itemForm.getUrl()))
		// errors.add(ActionMessages.GLOBAL_MESSAGE,new
		// ActionMessage(CommonCartridgeConstants.ERROR_MSG_INVALID_URL));
	    }
	}
	// if(itemForm.getItemType() == CommonCartridgeConstants.RESOURCE_TYPE_WEBSITE
	// ||itemForm.getItemType() == CommonCartridgeConstants.RESOURCE_TYPE_LEARNING_OBJECT){
	// if(StringUtils.isBlank(itemForm.getDescription()))
	// errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(CommonCartridgeConstants.ERROR_MSG_DESC_BLANK));
	// }
	if (itemForm.getItemType() == CommonCartridgeConstants.RESOURCE_TYPE_COMMON_CARTRIDGE) {
	    // validate item size
	    FileValidatorUtil.validateFileSize(itemForm.getFile(), true);
	    // for edit validate: file already exist
	    if (!itemForm.isHasFile()
		    && (itemForm.getFile() == null || StringUtils.isEmpty(itemForm.getFile().getName()))) {
		errorMap.add("GLOBAL", messageService.getMessage(CommonCartridgeConstants.ERROR_MSG_FILE_BLANK));
	    }
	}
	return errorMap;
    }
}