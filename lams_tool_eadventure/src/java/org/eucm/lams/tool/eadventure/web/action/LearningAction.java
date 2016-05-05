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

/* $Id$ */
package org.eucm.lams.tool.eadventure.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureSession;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.model.EadventureVars;
import org.eucm.lams.tool.eadventure.service.EadventureApplicationException;
import org.eucm.lams.tool.eadventure.service.IEadventureService;
import org.eucm.lams.tool.eadventure.web.form.ReflectionForm;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	// -----------------------Eadventure Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("addfile")) {
	    //return addItem(mapping, form, request, response);
	}
	if (param.equals("addurl")) {
	    //return addItem(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateItem")) {
	    //return saveOrUpdateItem(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(EadventureConstants.ERROR);
    }

    /**
     * Initial page for add eadventure item (single file or URL).
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    //TODO eliminar
    /*
     * private ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) {
     * EadventureItemForm itemForm = (EadventureItemForm) form;
     * itemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
     * itemForm.setSessionMapID(WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID));
     * return mapping.findForward(EadventureConstants.SUCCESS);
     * }
     * 
     * /**
     * Read eadventure data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(EadventureConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the eadventure and item list and display them on page
	IEadventureService service = getEadventureService();
	EadventureUser eadventureUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // eadventureUser may be null if the user was force completed.
	    eadventureUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    eadventureUser = getCurrentUser(service, sessionId);
	    //TODO mio!!!
	    if (eadventureUser == null) {
		log.debug("VAYA PUES, EADUSER ES NULL!!!!!!!!!!!!!!!!!!!!!!!!1");
	    } else {
		sessionMap.put(EadventureConstants.ATTR_USER_ID, eadventureUser.getUserId().intValue());
		sessionMap.put(EadventureConstants.ATTR_USER_FNAME, eadventureUser.getFirstName());
		sessionMap.put(EadventureConstants.ATTR_USER_LNAME, eadventureUser.getLastName());
	    }

	}

	Eadventure eadventure;
	eadventure = service.getEadventureBySessionId(sessionId);
	if (eadventure == null) {
	    log.error("EAD ===== NULL!!!!!!!!!!");
	}

	// check whehter finish lock is on/off
	boolean lock = eadventure.getLockWhenFinished() && eadventureUser != null && eadventureUser.isSessionFinished();

	// check whether there is only one eadventure item and run auto flag is true or not.
	boolean runAuto = false;

	//TODO modify!!
	// only visible item can be run auto.
	runAuto = true;
	request.setAttribute(EadventureConstants.ATTR_RESOURCE_ITEM_UID, eadventure.getUid());

	// get notebook entry
	String entryText = new String();
	boolean completed = false;
	if (eadventureUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    EadventureConstants.TOOL_SIGNATURE, eadventureUser.getUserId().intValue());
	    // check if the item is completed previously
	    EadventureItemVisitLog eadLog = service.getEadventureItemLog(eadventure.getUid(),
		    eadventureUser.getUserId());
	    if (eadLog != null) {
		completed = eadLog.isComplete();
	    }
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// check define complete
	boolean isDefineComplete = false;
	if (eadventure.isDefineComplete()) {
	    isDefineComplete = true;
	}

	// basic information
	sessionMap.put(EadventureConstants.ATTR_TITLE, eadventure.getTitle());
	sessionMap.put(EadventureConstants.ATTR_RESOURCE_INSTRUCTION, eadventure.getInstructions());
	sessionMap.put(EadventureConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(EadventureConstants.ATTR_LOCK_ON_FINISH, eadventure.getLockWhenFinished());
	sessionMap.put(EadventureConstants.ATTR_USER_FINISHED,
		eadventureUser != null && eadventureUser.isSessionFinished());

	//TODO Estamos pasando session ID por atributo y por session!!
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, eadventure.getContentId());
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(EadventureConstants.ATTR_REFLECTION_ON, eadventure.isReflectOnActivity());
	sessionMap.put(EadventureConstants.ATTR_REFLECTION_INSTRUCTION, eadventure.getReflectInstructions());
	sessionMap.put(EadventureConstants.ATTR_REFLECTION_ENTRY, entryText);
	sessionMap.put(EadventureConstants.ATTR_RUN_AUTO, new Boolean(runAuto));
	sessionMap.put(EadventureConstants.PARAM_RESOURCE_ITEM_UID, eadventure.getUid());
	sessionMap.put(EadventureConstants.ATTR_DEFINE_COMPLETED, isDefineComplete);
	sessionMap.put(EadventureConstants.ATTR_COMPLETED, completed);
	// add define later support
	if (eadventure.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	eadventure.setContentInUse(true);
	eadventure.setDefineLater(false);
	service.saveOrUpdateEadventure(eadventure);

	// set complete flag for display purpose
	//TODO getionar el complete
	//if (eadventureUser != null) {
	//  service.retrieveComplete(eadventureItemList, eadventureUser);
	//}
	sessionMap.put(EadventureConstants.ATTR_EADVENTURE, eadventure);

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	return mapping.findForward(EadventureConstants.SUCCESS);
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
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	//String toolContentID = request.getParameter(EadventureConstants.ATTR_TOOL_CONTENT_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolContentID = (Long) sessionMap.get(EadventureConstants.ATTR_TOOL_CONTENT_ID);
	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// auto run mode, when use finish the only one eadventure item, mark it as complete then finish this activity as
	// well.
	String eadventureItemUid = request.getParameter(EadventureConstants.PARAM_RESOURCE_ITEM_UID);
	IEadventureService service = getEadventureService();
	Eadventure ead = service.getEadventureByContentId(toolContentID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	boolean completedFromEad = false;
	if (ead.isDefineComplete()) {
	    EadventureItemVisitLog log = service.getEadventureItemLog(ead.getUid(), userID);
	    EadventureVars var = service.getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_COMPLETED);
	    if (var != null && Boolean.parseBoolean(var.getValue())) {
		completedFromEad = true;
	    }
	}

	//if (eadventureItemUid != null ){

	doComplete(request, ead.getUid());
	ead.setComplete(true);

	// NOTE:So far this flag is useless(31/08/2006).
	// set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	//  request.setAttribute(EadventureConstants.ATTR_RUN_AUTO, true);
	//} else{
	//  request.setAttribute(EadventureConstants.ATTR_RUN_AUTO, false);
	//}

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    // HttpSession ss = SessionManager.getSession();
	    // UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    // Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(EadventureConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (EadventureApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Save file or url eadventure item into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    /*
     * private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) {
     * // get back SessionMap
     * String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
     * SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
     * request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
     * 
     * Long sessionId = (Long) sessionMap.get(EadventureConstants.ATTR_TOOL_SESSION_ID);
     * 
     * String mode = request.getParameter(AttributeNames.ATTR_MODE);
     * EadventureItemForm itemForm = (EadventureItemForm) form;
     * ActionErrors errors = validateEadventureItem(itemForm);
     * 
     * if (!errors.isEmpty()) {
     * this.addErrors(request, errors);
     * return findForward(itemForm.getItemType(), mapping);
     * }
     * short type = itemForm.getItemType();
     * 
     * // create a new EadventureItem
     * EadventureItem item = new EadventureItem();
     * IEadventureService service = getEadventureService();
     * EadventureUser eadventureUser = getCurrentUser(service, sessionId);
     * item.setType(type);
     * item.setTitle(itemForm.getTitle());
     * item.setDescription(itemForm.getDescription());
     * item.setCreateDate(new Timestamp(new Date().getTime()));
     * item.setCreateByAuthor(false);
     * item.setCreateBy(eadventureUser);
     * 
     * // special attribute for URL or FILE
     * if (type == EadventureConstants.RESOURCE_TYPE_FILE) {
     * try {
     * service.uploadEadventureItemFile(item, itemForm.getFile());
     * } catch (UploadEadventureFileException e) {
     * LearningAction.log.error("Failed upload Eadventure File " + e.toString());
     * return mapping.findForward(EadventureConstants.ERROR);
     * }
     * } else if (type == EadventureConstants.RESOURCE_TYPE_URL) {
     * item.setUrl(itemForm.getUrl());
     * item.setOpenUrlNewWindow(itemForm.isOpenUrlNewWindow());
     * }
     * // save and update session
     * 
     * EadventureSession resSession = service.getEadventureSessionBySessionId(sessionId);
     * if (resSession == null) {
     * LearningAction.log.error("Failed update EadventureSession by ID[" + sessionId + "]");
     * return mapping.findForward(EadventureConstants.ERROR);
     * }
     * Set<EadventureItem> items = resSession.getEadventureItems();
     * if (items == null) {
     * items = new HashSet<EadventureItem>();
     * resSession.setEadventureItems(items);
     * }
     * items.add(item);
     * service.saveOrUpdateEadventureSession(resSession);
     * 
     * // update session value
     * SortedSet<EadventureItem> eadventureItemList = getEadventureItemList(sessionMap);
     * eadventureItemList.add(item);
     * 
     * // URL or file upload
     * request.setAttribute(EadventureConstants.ATTR_ADD_RESOURCE_TYPE, new Short(type));
     * request.setAttribute(AttributeNames.ATTR_MODE, mode);
     * 
     * Eadventure eadventure = resSession.getEadventure();
     * if (eadventure.isNotifyTeachersOnAssigmentSumbit()) {
     * List<User> monitoringUsers = service.getMonitorsByToolSessionId(sessionId);
     * if (monitoringUsers != null && !monitoringUsers.isEmpty()) {
     * Long[] monitoringUsersIds = new Long[monitoringUsers.size()];
     * for (int i = 0; i < monitoringUsersIds.length; i++) {
     * monitoringUsersIds[i] = monitoringUsers.get(i).getUserId().longValue();
     * }
     * String fullName = eadventureUser.getLastName() + " " + eadventureUser.getFirstName();
     * service.getEventNotificationService().sendMessage(monitoringUsersIds, DeliveryMethodMail.getInstance(),
     * service.getLocalisedMessage("event.assigment.submit.subject", null),
     * service.getLocalisedMessage("event.assigment.submit.body", new Object[] { fullName }));
     * }
     * }
     * return mapping.findForward(EadventureConstants.SUCCESS);
     * }
     * 
     * /**
     * Display empty reflection form.
     *
     * @param mapping
     * 
     * @param form
     * 
     * @param request
     * 
     * @param response
     * 
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IEadventureService submitFilesService = getEadventureService();

	SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		EadventureConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(EadventureConstants.SUCCESS);
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

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IEadventureService service = getEadventureService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		EadventureConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    EadventureConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
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
    //TODO valorar eliminarlo
    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	return true;
    }

    private IEadventureService getEadventureService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IEadventureService) wac.getBean(EadventureConstants.RESOURCE_SERVICE);
    }

    /**
     * List save current eadventure items.
     *
     * @param request
     * @return
     */
    /*
     * private SortedSet<EadventureItem> getEadventureItemList(SessionMap sessionMap) {
     * SortedSet<EadventureItem> list = (SortedSet<EadventureItem>) sessionMap
     * .get(EadventureConstants.ATTR_RESOURCE_ITEM_LIST);
     * if (list == null) {
     * list = new TreeSet<EadventureItem>(new EadventureItemComparator());
     * sessionMap.put(EadventureConstants.ATTR_RESOURCE_ITEM_LIST, list);
     * }
     * return list;
     * }
     * 
     * /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * 
     * @param name
     * 
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
     * Return <code>ActionForward</code> according to eadventure item type.
     *
     * @param type
     * @param mapping
     * @return
     */
    private ActionForward findForward(short type, ActionMapping mapping) {
	ActionForward forward;
	switch (type) {
	    case EadventureConstants.RESOURCE_TYPE_URL:
		forward = mapping.findForward("url");
		break;
	    case EadventureConstants.RESOURCE_TYPE_FILE:
		forward = mapping.findForward("file");
		break;
	    case EadventureConstants.RESOURCE_TYPE_WEBSITE:
		forward = mapping.findForward("website");
		break;
	    case EadventureConstants.RESOURCE_TYPE_LEARNING_OBJECT:
		forward = mapping.findForward("learningobject");
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

    private EadventureUser getCurrentUser(IEadventureService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	EadventureUser eadventureUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);
	Eadventure ead = service.getEadventureBySessionId(sessionId);
	// EadventureUser eadventureUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()), ead.getContentId());

	if (eadventureUser == null) {
	    EadventureSession session = service.getEadventureSessionBySessionId(sessionId);
	    eadventureUser = new EadventureUser(user, session);
	    // eadventureUser.setEadventure(ead);
	    service.createUser(eadventureUser);
	    log.error("CREAMOS NUEVO USUARIO!!!! en getCurrentUSER");
	} //TODO meter en un save or update de userDAO
	  //else

	return eadventureUser;
    }

    private EadventureUser getSpecifiedUser(IEadventureService service, Long sessionId, Integer userId) {
	EadventureUser eadventureUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (eadventureUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for eadventure activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return eadventureUser;
    }

    /**
     * @param itemForm
     * @return
     */
    /*
     * private ActionErrors validateEadventureItem(EadventureItemForm itemForm) {
     * ActionErrors errors = new ActionErrors();
     * if (StringUtils.isBlank(itemForm.getTitle())) {
     * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_MSG_TITLE_BLANK));
     * }
     * 
     * if (itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_URL) {
     * if (StringUtils.isBlank(itemForm.getUrl())) {
     * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_MSG_URL_BLANK));
     * // URL validation: Commom URL validate(1.3.0) work not very well: it can not support http://address:port
     * // format!!!
     * // UrlValidator validator = new UrlValidator();
     * // if(!validator.isValid(itemForm.getUrl()))
     * // errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(EadventureConstants.ERROR_MSG_INVALID_URL));
     * }
     * }
     * // if(itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_WEBSITE
     * // ||itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_LEARNING_OBJECT){
     * // if(StringUtils.isBlank(itemForm.getDescription()))
     * // errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(EadventureConstants.ERROR_MSG_DESC_BLANK));
     * // }
     * if (itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_WEBSITE
     * || itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_LEARNING_OBJECT
     * || itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_FILE) {
     * 
     * if (itemForm.getFile() != null && FileUtil.isExecutableFile(itemForm.getFile().getFileName())) {
     * ActionMessage msg = new ActionMessage("error.attachment.executable");
     * errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
     * }
     * 
     * // validate item size
     * FileValidatorUtil.validateFileSize(itemForm.getFile(), false, errors);
     * 
     * // for edit validate: file already exist
     * if (!itemForm.isHasFile()
     * && (itemForm.getFile() == null || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
     * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_MSG_FILE_BLANK));
     * }
     * }
     * return errors;
     * }
     */

    /**
     * Set complete flag for given eadventure item.
     *
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request, Long eadUid) {
	// get back sessionMap
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	//Long eadventureItemUid = new Long(request.getParameter(EadventureConstants.PARAM_RESOURCE_ITEM_UID));
	IEadventureService service = getEadventureService();
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(EadventureConstants.ATTR_TOOL_SESSION_ID);
	service.setItemComplete(eadUid, new Long(user.getUserID().intValue()), sessionId);

	// set eadventure item complete tag
	//TODO asignar complete
	/*
	 * SortedSet<EadventureItem> eadventureItemList = getEadventureItemList(sessionMap);
	 * for (EadventureItem item : eadventureItemList) {
	 * if (item.getUid().equals(eadventureItemUid)) {
	 * item.setComplete(true);
	 * break;
	 * }
	 * }
	 */
    }

}
