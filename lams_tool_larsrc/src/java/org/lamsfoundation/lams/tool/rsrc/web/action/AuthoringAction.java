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

package org.lamsfoundation.lams.tool.rsrc.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.service.UploadResourceFileException;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceForm;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceItemForm;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourcePedagogicalPlannerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
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
public class AuthoringAction extends Action {
    private static final int INIT_INSTRUCTION_COUNT = 2;
    private static final String INSTRUCTION_ITEM_DESC_PREFIX = "instructionItemDesc";
    private static final String INSTRUCTION_ITEM_COUNT = "instructionCount";
    private static final String ITEM_TYPE = "itemType";

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Resource Author function
	// ---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = getAccessMode(request);
	    // teacher mode "check for new" button enter.
	    if (mode != null) {
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    } else {
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR.toString());
	    }
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IResourceService service = getResourceService();
	    Resource resource = service.getResourceByContentId(contentId);

	    resource.setDefineLater(true);
	    service.saveOrUpdateResource(resource);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}

	// ----------------------- Add resource item function
	// ---------------------------
	if (param.equals("newItemInit")) {
	    return newItemlInit(mapping, form, request, response);
	}
	if (param.equals("editItemInit")) {
	    return editItemInit(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateItem")) {
	    return saveOrUpdateItem(mapping, form, request, response);
	}
	if (param.equals("removeItem")) {
	    return removeItem(mapping, form, request, response);
	}
	// -----------------------Resource Item Instruction function
	// ---------------------------
	if (param.equals("newInstruction")) {
	    return newInstruction(mapping, form, request, response);
	}
	if (param.equals("removeInstruction")) {
	    return removeInstruction(mapping, form, request, response);
	}
	if (param.equals("removeItemAttachment")) {
	    return removeItemAttachment(mapping, form, request, response);
	}
	if (param.equals("initPedagogicalPlannerForm")) {
	    return initPedagogicalPlannerForm(mapping, form, request, response);
	}
	if (param.equals("createPedagogicalPlannerItem")) {
	    return createPedagogicalPlannerItem(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdatePedagogicalPlannerForm")) {
	    return saveOrUpdatePedagogicalPlannerForm(mapping, form, request, response);
	}
	if (param.equals("switchResourceItemPosition")) {
	    return switchResourceItemPosition(mapping, form, request, response);
	}

	return mapping.findForward(ResourceConstants.ERROR);
    }

    /**
     * Remove resource item attachment, such as single file, learning object
     * ect. It is a ajax call and just temporarily remove from page, all
     * permenant change will happen only when user sumbit this resource item
     * again.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeItemAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("itemAttachment", null);
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     * Remove resource item from HttpSession list and update page display. As
     * authoring rule, all persist only happen when user submit whole page. So
     * this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);
	    List<ResourceItem> rList = new ArrayList<ResourceItem>(resourceList);
	    ResourceItem item = rList.remove(itemIdx);
	    resourceList.clear();
	    resourceList.addAll(rList);
	    // add to delList
	    List delList = getDeletedResourceItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     * Display edit page for existed resource item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX), -1);
	ResourceItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);
	    List<ResourceItem> rList = new ArrayList<ResourceItem>(resourceList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, (ResourceItemForm) form, request);
	    }
	}
	return findForward(item == null ? -1 : item.getType(), mapping);
    }

    /**
     * Display empty page for new resource item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	((ResourceItemForm) form).setSessionMapID(sessionMapID);

	short type = (short) WebUtil.readIntParam(request, AuthoringAction.ITEM_TYPE);
	List<String> instructionList = new ArrayList<String>(AuthoringAction.INIT_INSTRUCTION_COUNT);
	for (int idx = 0; idx < AuthoringAction.INIT_INSTRUCTION_COUNT; idx++) {
	    instructionList.add("");
	}
	request.setAttribute("instructionList", instructionList);
	return findForward(type, mapping);
    }

    /**
     * This method will get necessary information from resource item form and
     * save or update into <code>HttpSession</code> ResourceItemList. Notice,
     * this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the
     * entire authoring page is being persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get instructions:
	List<String> instructionList = getInstructionsFromRequest(request);

	ResourceItemForm itemForm = (ResourceItemForm) form;
	ActionErrors errors = validateResourceItem(itemForm);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST, instructionList);
	    return findForward(itemForm.getItemType(), mapping);
	}

	try {
	    extractFormToResourceItem(request, instructionList, itemForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather
	    // then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(ResourceConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	    if (!errors.isEmpty()) {
		this.addErrors(request, errors);
		request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST, instructionList);
		return findForward(itemForm.getItemType(), mapping);
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     * Ajax call, will add one more input line for new resource item
     * instruction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	int numberOfInstructions = WebUtil.readIntParam(request, INSTRUCTION_ITEM_COUNT);
	List<String> instructionList = new ArrayList<String>(++numberOfInstructions);
	for (int idx = 0; idx < numberOfInstructions; idx++) {
	    String item = request.getParameter(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (item == null) {
		instructionList.add("");
	    } else {
		instructionList.add(item);
	    }
	}
	request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST, instructionList);
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     * Ajax call, remove the given line of instruction of resource item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	int numberOfInstructions = WebUtil.readIntParam(request, INSTRUCTION_ITEM_COUNT);
	int removeIdx = WebUtil.readIntParam(request, "removeIdx");
	List<String> instructionList = new ArrayList<String>(numberOfInstructions - 1);
	for (int idx = 0; idx < numberOfInstructions; idx++) {
	    String item = request.getParameter(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (idx == removeIdx) {
		continue;
	    }
	    if (item == null) {
		instructionList.add("");
	    } else {
		instructionList.add(item);
	    }
	}
	request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST, instructionList);
	return mapping.findForward(ResourceConstants.SUCCESS);
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
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, ResourceConstants.PARAM_TOOL_CONTENT_ID));

	// get back the resource and item list and display them on page
	IResourceService service = getResourceService();

	List<ResourceItem> items = null;
	Resource resource = null;
	ResourceForm resourceForm = (ResourceForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	resourceForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	resourceForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    resource = service.getResourceByContentId(contentId);
	    // if resource does not exist, try to use default content instead.
	    if (resource == null) {
		resource = service.getDefaultContent(contentId);
		if (resource.getResourceItems() != null) {
		    items = new ArrayList<ResourceItem>(resource.getResourceItems());
		} else {
		    items = null;
		}
	    } else {
		items = service.getAuthoredItems(resource.getUid());
	    }

	    resourceForm.setResource(resource);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<ResourceItem>();
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

	sessionMap.put(ResourceConstants.ATTR_RESOURCE_FORM, resourceForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return mapping.findForward(ResourceConstants.SUCCESS);
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
    private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	ResourceForm existForm = (ResourceForm) sessionMap.get(ResourceConstants.ATTR_RESOURCE_FORM);

	ResourceForm resourceForm = (ResourceForm) form;
	try {
	    PropertyUtils.copyProperties(resourceForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}
	
	ToolAccessMode mode = getAccessMode(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include
     * all resource item, information etc.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ResourceForm resourceForm = (ResourceForm) form;

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(resourceForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	ActionMessages errors = validate(resourceForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    return mapping.findForward(ResourceConstants.SUCCESS);
	}

	Resource resource = resourceForm.getResource();
	IResourceService service = getResourceService();

	// **********************************Get Resource PO*********************
	Resource resourcePO = service.getResourceByContentId(resource.getContentId());
	if (resourcePO == null) {
	    // new Resource, create it
	    resourcePO = resource;
	    resourcePO.setCreated(new Timestamp(new Date().getTime()));
	    resourcePO.setUpdated(new Timestamp(new Date().getTime()));
	    
	} else {
	    Long uid = resourcePO.getUid();
	    PropertyUtils.copyProperties(resourcePO, resource);

	    // copyProperties() above may result in "collection assigned to two objects in a session" exception
	    service.evict(resource);
	    resourceForm.setResource(null);
	    resource = null;
	    // set back UID
	    resourcePO.setUid(uid);

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
	ResourceUser resourceUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		resourcePO.getContentId());
	if (resourceUser == null) {
	    resourceUser = new ResourceUser(user, resourcePO);
	}

	resourcePO.setCreatedBy(resourceUser);

	// ************************* Handle resource items *******************
	// Handle resource items
	Set itemList = new LinkedHashSet();
	SortedSet topics = getResourceItemList(sessionMap);
	Iterator iter = topics.iterator();
	while (iter.hasNext()) {
	    ResourceItem item = (ResourceItem) iter.next();
	    if (item != null) {
		// This flushs user UID info to message if this user is a new
		// user.
		item.setCreateBy(resourceUser);
		itemList.add(item);
	    }
	}
	resourcePO.setResourceItems(itemList);
	// delete instructino file from database.
	List delResourceItemList = getDeletedResourceItemList(sessionMap);
	iter = delResourceItemList.iterator();
	while (iter.hasNext()) {
	    ResourceItem item = (ResourceItem) iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		service.deleteResourceItem(item.getUid());
	    }
	}
	// handle resource item attachment file:
	List delItemAttList = getDeletedItemAttachmentList(sessionMap);
	iter = delItemAttList.iterator();
	while (iter.hasNext()) {
	    ResourceItem delAtt = (ResourceItem) iter.next();
	    iter.remove();
	}

	// if miniview number is bigger than available items, then set it topics
	// size
	if (resourcePO.getMiniViewResourceNumber() > topics.size()) {
	    resourcePO.setMiniViewResourceNumber(topics.size());
	}
	// **********************************************
	// finally persist resourcePO again
	service.saveOrUpdateResource(resourcePO);

	resourceForm.setResource(resourcePO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return ResourceService bean.
     */
    private IResourceService getResourceService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IResourceService) wac.getBean(ResourceConstants.RESOURCE_SERVICE);
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
     * List save deleted resource items, which could be persisted or
     * non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedResourceItemList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ResourceConstants.ATTR_DELETED_RESOURCE_ITEM_LIST);
    }

    /**
     * If a resource item has attahment file, and the user edit this item and
     * change the attachment to new file, then the old file need be deleted when
     * submitting the whole authoring page. Save the file uuid and version id
     * into ResourceItem object for temporarily use.
     *
     * @param request
     * @return
     */
    private List getDeletedItemAttachmentList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ResourceConstants.ATTR_DELETED_RESOURCE_ITEM_ATTACHMENT_LIST);
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
     * Get resource items instruction from <code>HttpRequest</code>
     *
     * @param request
     */
    private List<String> getInstructionsFromRequest(HttpServletRequest request) {
	String list = request.getParameter("instructionList");
	String[] params = list.split("&");
	Map<String, String> paramMap = new HashMap<String, String>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if (pair == null || pair.length != 2) {
		continue;
	    }
	    try {
		paramMap.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		AuthoringAction.log.error("Error occurs when decode instruction string:" + e.toString());
	    }
	}

	int count = paramMap.keySet().size();
	List<String> instructionList = new ArrayList<String>();

	for (int idx = 0; idx < count; idx++) {
	    String item = paramMap.get(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (item == null) {
		continue;
	    }

	    instructionList.add(item);
	}

	return instructionList;

    }

    /**
     * Get back relative <code>ActionForward</code> from request.
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
	form.setDescription(item.getDescription());
	form.setTitle(item.getTitle());
	form.setUrl(item.getUrl());
	form.setOpenUrlNewWindow(item.isOpenUrlNewWindow());
	if (itemIdx >= 0) {
	    form.setItemIndex(new Integer(itemIdx).toString());
	}

	Set<ResourceItemInstruction> instructionList = item.getItemInstructions();
	List instructions = new ArrayList();
	for (ResourceItemInstruction in : instructionList) {
	    instructions.add(in.getDescription());
	}
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

	request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST, instructions);
    }

    /**
     * Extract web from content to resource item.
     *
     * @param request
     * @param instructionList
     * @param itemForm
     * @throws ResourceApplicationException
     */
    private void extractFormToResourceItem(HttpServletRequest request, List<String> instructionList,
	    ResourceItemForm itemForm) throws Exception {
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to a
	 * old or new ResourceItem instance. It gets all info EXCEPT
	 * ResourceItem.createDate and ResourceItem.createBy, which need be set
	 * when persisting this resource item.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ResourceItem> resourceList = getResourceItemList(sessionMap);
	int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(), -1);
	ResourceItem item = null;

	if (itemIdx == -1) { // add
	    item = new ResourceItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    item.setOrderId(resourceList.size() + 1);
	    resourceList.add(item);
	} else { // edit
	    List<ResourceItem> rList = new ArrayList<ResourceItem>(resourceList);
	    item = rList.get(itemIdx);
	}
	short type = itemForm.getItemType();
	item.setType(itemForm.getItemType());
	/*
	 * Set following fields regards to the type: item.setFileUuid();
	 * item.setFileVersionId(); item.setFileType(); item.setFileName();
	 *
	 * item.getInitialItem() item.setImsSchema() item.setOrganizationXml()
	 */
	// if the item is edit (not new add) then the getFile may return null
	// it may throw exception, so put it as first, to avoid other invlidate
	// update:
	if (itemForm.getFile() != null) {
	    if (type == ResourceConstants.RESOURCE_TYPE_WEBSITE
		    || type == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT
		    || type == ResourceConstants.RESOURCE_TYPE_FILE) {
		// if it has old file, and upload a new, then save old to
		// deleteList
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
		IResourceService service = getResourceService();
		try {
		    service.uploadResourceItemFile(item, itemForm.getFile());
		} catch (UploadResourceFileException e) {
		    // if it is new add , then remove it!
		    if (itemIdx == -1) {
			resourceList.remove(item);
		    }
		    throw e;
		}
		// put it after "upload" to ensure deleted file added into list
		// only no exception happens during upload
		if (hasOld) {
		    List delAtt = getDeletedItemAttachmentList(sessionMap);
		    delAtt.add(delAttItem);
		}
	    }
	}
	item.setTitle(itemForm.getTitle());
	item.setCreateByAuthor(true);
	item.setHide(false);
	// set instrcutions
	Set instructions = new LinkedHashSet();
	int idx = 0;
	for (String ins : instructionList) {
	    ResourceItemInstruction rii = new ResourceItemInstruction();
	    rii.setDescription(ins);
	    rii.setSequenceId(idx++);
	    instructions.add(rii);
	}
	item.setItemInstructions(instructions);

	if (type == ResourceConstants.RESOURCE_TYPE_URL) {
	    item.setUrl(itemForm.getUrl());
	}
	if (type == ResourceConstants.RESOURCE_TYPE_URL || type == ResourceConstants.RESOURCE_TYPE_FILE) {
	    item.setOpenUrlNewWindow(itemForm.isOpenUrlNewWindow());
	}
	// if(type == ResourceConstants.RESOURCE_TYPE_WEBSITE
	// ||itemForm.getItemType() ==
	// ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
	item.setDescription(itemForm.getDescription());
	// }

    }

    /**
     * Vaidate resource item regards to their type (url/file/learning
     * object/website zip file)
     *
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
		// URL validation: Commom URL validate(1.3.0) work not very
		// well: it can not support http://
		// address:port format!!!
		// UrlValidator validator = new UrlValidator();
		// if(!validator.isValid(itemForm.getUrl()))
		// errors.add(ActionMessages.GLOBAL_MESSAGE,new
		// ActionMessage(ResourceConstants.ERROR_MSG_INVALID_URL));
	    }
	}
	// if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE
	// ||itemForm.getItemType() ==
	// ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
	// if(StringUtils.isBlank(itemForm.getDescription()))
	// errors.add(ActionMessages.GLOBAL_MESSAGE,new
	// ActionMessage(ResourceConstants.ERROR_MSG_DESC_BLANK));
	// }
	if (itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE
		|| itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT
		|| itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_FILE) {
	    // validate item size
	    FileValidatorUtil.validateFileSize(itemForm.getFile(), true, errors);
	    // for edit validate: file already exist
	    if (!itemForm.isHasFile()
		    && (itemForm.getFile() == null || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ResourceConstants.ERROR_MSG_FILE_BLANK));
	    }
	}
	return errors;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR
     * mode.
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

    private ActionMessages validate(ResourceForm resourceForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	if (StringUtils.isBlank(resourceForm.getResource().getTitle())) {
	    ActionMessage error = new ActionMessage("error.resource.item.title.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	return errors;
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ResourcePedagogicalPlannerForm plannerForm = (ResourcePedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Resource taskList = getResourceService().getResourceByContentId(toolContentID);
	plannerForm.fillForm(taskList);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	ResourcePedagogicalPlannerForm plannerForm = (ResourcePedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    Resource taskList = getResourceService().getResourceByContentId(plannerForm.getToolContentID());
	    taskList.setInstructions(plannerForm.getInstructions());

	    int itemIndex = 0;
	    String title = null;
	    ResourceItem resourceItem = null;
	    List<ResourceItem> newItems = new LinkedList<ResourceItem>();
	    // we need a copy for later Hibernate-bound processing
	    LinkedList<ResourceItem> resourceItems = new LinkedList<ResourceItem>(taskList.getResourceItems());
	    Iterator<ResourceItem> taskListItemIterator = resourceItems.iterator();
	    /*
	     * Not the case anymore (why?):
	     * We need to reverse the order, since the items are delivered newest-first
	     * LinkedList<ResourceItem> reversedResourceItems = new LinkedList<ResourceItem>();
	     * while (taskListItemIterator.hasNext()) {
	     * reversedResourceItems.addFirst(taskListItemIterator.next());
	     * }
	     * taskListItemIterator = reversedResourceItems.iterator();
	     */
	    do {
		title = plannerForm.getTitle(itemIndex);
		if (StringUtils.isEmpty(title)) {
		    plannerForm.removeItem(itemIndex);
		} else {
		    if (taskListItemIterator.hasNext()) {
			resourceItem = taskListItemIterator.next();
		    } else {
			resourceItem = new ResourceItem();
			resourceItem.setCreateByAuthor(true);
			Date currentDate = new Date();
			resourceItem.setCreateDate(currentDate);

			HttpSession session = SessionManager.getSession();
			UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);
			ResourceUser taskListUser = getResourceService().getUserByIDAndContent(
				new Long(user.getUserID().intValue()), plannerForm.getToolContentID());
			resourceItem.setCreateBy(taskListUser);

			newItems.add(resourceItem);
		    }
		    resourceItem.setTitle(title);
		    Short type = plannerForm.getType(itemIndex);
		    resourceItem.setType(type);
		    boolean hasFile = resourceItem.getFileUuid() != null;
		    if (type.equals(ResourceConstants.RESOURCE_TYPE_URL)) {
			resourceItem.setUrl(plannerForm.getUrl(itemIndex));
			if (hasFile) {
			    resourceItem.setFileName(null);
			    resourceItem.setFileUuid(null);
			    resourceItem.setFileVersionId(null);
			    resourceItem.setFileType(null);
			}
		    } else if (type.equals(ResourceConstants.RESOURCE_TYPE_FILE)) {
			FormFile file = plannerForm.getFile(itemIndex);
			resourceItem.setUrl(null);
			IResourceService service = getResourceService();
			if (file != null && !StringUtils.isEmpty(file.getFileName())) {
			    try {
				if (hasFile) {
				    // delete the old file
				    service.deleteFromRepository(resourceItem.getFileUuid(),
					    resourceItem.getFileVersionId());
				}
				service.uploadResourceItemFile(resourceItem, file);
			    } catch (Exception e) {
				AuthoringAction.log.error(e);
				ActionMessage error = new ActionMessage("error.msg.io.exception");
				errors.add(ActionMessages.GLOBAL_MESSAGE, error);
				saveErrors(request, errors);
				plannerForm.setValid(false);
				return mapping.findForward(ResourceConstants.SUCCESS);
			    }
			    plannerForm.setFileName(itemIndex, resourceItem.getFileName());
			    plannerForm.setFileUuid(itemIndex, resourceItem.getFileUuid());
			    plannerForm.setFileVersion(itemIndex, resourceItem.getFileVersionId());
			    plannerForm.setFile(itemIndex, null);
			}
		    }
		    itemIndex++;
		}

	    } while (title != null);
	    // we need to clear it now, otherwise we get Hibernate error (item
	    // re-saved by cascade)
	    taskList.getResourceItems().clear();
	    while (taskListItemIterator.hasNext()) {
		resourceItem = taskListItemIterator.next();
		taskListItemIterator.remove();
		getResourceService().deleteResourceItem(resourceItem.getUid());
	    }
	    resourceItems.addAll(newItems);

	    taskList.getResourceItems().addAll(resourceItems);
	    getResourceService().saveOrUpdateResource(taskList);
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    public ActionForward createPedagogicalPlannerItem(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	ResourcePedagogicalPlannerForm plannerForm = (ResourcePedagogicalPlannerForm) form;
	int insertIndex = plannerForm.getItemCount();
	plannerForm.setTitle(insertIndex, "");
	plannerForm.setType(insertIndex, new Short(request.getParameter(ResourceConstants.ATTR_ADD_RESOURCE_TYPE)));
	plannerForm.setUrl(insertIndex, null);
	plannerForm.setFileName(insertIndex, null);
	plannerForm.setFile(insertIndex, null);
	plannerForm.setFileUuid(insertIndex, null);
	plannerForm.setFileVersion(insertIndex, null);
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

    private ActionForward switchResourceItemPosition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, "sessionMapID");
	int resourceItemOrderID1 = WebUtil.readIntParam(request, "resourceItemOrderID1");
	int resourceItemOrderID2 = WebUtil.readIntParam(request, "resourceItemOrderID2");

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
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

	SortedSet<ResourceItem> newItems = new TreeSet<ResourceItem>(new ResourceItemComparator());
	newItems.addAll(resourceList);
	sessionMap.put(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, newItems);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// return null to close this window
	return mapping.findForward(ResourceConstants.SUCCESS);
    }

}