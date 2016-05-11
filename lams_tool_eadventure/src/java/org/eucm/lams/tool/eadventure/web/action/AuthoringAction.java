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

package org.eucm.lams.tool.eadventure.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureCondition;
import org.eucm.lams.tool.eadventure.model.EadventureExpression;
import org.eucm.lams.tool.eadventure.model.EadventureParam;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.service.IEadventureService;
import org.eucm.lams.tool.eadventure.util.EadventureConditionComparator;
import org.eucm.lams.tool.eadventure.web.form.EadventureForm;
import org.eucm.lams.tool.eadventure.web.form.EadventureGameForm;
import org.eucm.lams.tool.eadventure.web.form.EadventurePedagogicalPlannerForm;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {
    private static final String INSTRUCTION_ITEM_DESC_PREFIX = "instructionItemDesc";

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Eadventure Author function
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
	    IEadventureService service = getEadventureService();
	    Eadventure eadventure = service.getEadventureByContentId(contentId);

	    eadventure.setDefineLater(true);
	    service.saveOrUpdateEadventure(eadventure);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}

	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// ----------------------- Add eadventure item function
	// ---------------------------
/*
 * if (param.equals("newItemInit")) {
 * return newItemlInit(mapping, form, request, response);
 * }
 */
	if (param.equals("editGameInit")) {
	    return editGameInit(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateGame")) {
	    return saveOrUpdateGame(mapping, form, request, response);
	}
	if (param.equals("removeItem")) {
	    return removeItem(mapping, form, request, response);
	}
	// -----------------------Eadventure Item Instruction function
	// ---------------------------
	if (param.equals("newInstruction")) {
	    return newInstruction(mapping, form, request, response);
	}
	if (param.equals("removeInstruction")) {
	    return removeInstruction(mapping, form, request, response);
	}
	if (param.equals("removeGameAttachment")) {
	    return removeGameAttachment(mapping, form, request, response);
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
	if (param.equals("switchEadventureItemPosition")) {
	    // return switchEadventureItemPosition(mapping, form, request, response);
	}

	return mapping.findForward(EadventureConstants.ERROR);
    }

    /**
     * Read eadventure data from database and put them into HttpSession. It will
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
	Long contentId = new Long(WebUtil.readLongParam(request, EadventureConstants.PARAM_TOOL_CONTENT_ID));

	// get back the eadventure and item list and display them on page
	IEadventureService service = getEadventureService();

	Eadventure eadventure = null;
	EadventureForm eadventureForm = (EadventureForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	eadventureForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	eadventureForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    eadventure = service.getEadventureByContentId(contentId);
	    // if eadventure does not exist, try to use default content instead.
	    //TODO fijar el default content!!!!
	    if (eadventure == null) {
		eadventure = service.getDefaultContent(contentId);
	    }

	    eadventureForm.setEadventure(eadventure);
	    eadventureForm.setHasFile(eadventure.getFileName() != null);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling

	EadventureUser eadventureUser = null;
	// handle system default question: createBy is null, now set it to
	// current user
	if (eadventure.getCreatedBy() == null) {
	    if (eadventureUser == null) {
		// get back login user DTO
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		eadventureUser = new EadventureUser(user, eadventure);
	    }
	    eadventure.setCreatedBy(eadventureUser);
	}

	// initialize conditions list (getEadventureConditionList puts a new conditions list in sessionMap
	// if there are not list inside)
	SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	conditionList.clear();
	conditionList.addAll(eadventure.getConditions());
	// initialize params list
	List<EadventureParam> paramsList = new ArrayList(eadventure.getParams());
	sessionMap.put(EadventureConstants.ATTR_PARAMS_LIST, paramsList);
	//initialize deletedParamList
	List<EadventureParam> delParamsList = new ArrayList();
	sessionMap.put(EadventureConstants.ATTR_DELETED_PARAMS_LIST, delParamsList);

	//TODO esta info puede ser redundante, ya que pasamos tb el form en la session
	if (eadventureForm.isHasFile()) {
	    sessionMap.put(EadventureConstants.ATTR_HAS_FILE, "true");
	    sessionMap.put(EadventureConstants.ATTR_OPEN_SAVED_GAME, "true");
	} else {
	    sessionMap.put(EadventureConstants.ATTR_OPEN_SAVED_GAME, "false");
	    sessionMap.put(EadventureConstants.ATTR_HAS_FILE, "false");
	}

	sessionMap.put(EadventureConstants.ATTR_CHANGE_FILE, "false");
	sessionMap.put(EadventureConstants.ATTR_RESOURCE_FORM, eadventureForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return mapping.findForward(EadventureConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	EadventureForm existForm = (EadventureForm) sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM);

	EadventureForm eadventureForm = (EadventureForm) form;
	try {
	    PropertyUtils.copyProperties(eadventureForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}
	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(EadventureConstants.SUCCESS);
	} else {
	    return mapping.findForward(EadventureConstants.DEFINE_LATER);
	}
    }

    /**
     * Display edit page for eadventure game file.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editGameInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	//TODO MODIFICAR PARA que sean una llamada javascript o algo asi
	//((EadventureGameForm) form).setSessionMapID(sessionMapID);
	//((EadventureGameForm) form).has
	//EadventureForm eadForm = ((EadventureForm)sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM));
	EadventureGameForm eadGameForm = ((EadventureGameForm) form);
	eadGameForm.setSessionMapID(sessionMapID);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * This method will get necessary information from eadventure game
     * save or update into <code>HttpSession</code> via. Notice,
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
    private ActionForward saveOrUpdateGame(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	EadventureGameForm gameForm = (EadventureGameForm) form;
	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(gameForm.getSessionMapID());

	Eadventure eadventure = ((EadventureForm) sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM))
		.getEadventure();

	ActionErrors errors = new ActionErrors();
	FormFile file = ((EadventureGameForm) form).getFile();
	if (file.getFileName() == null || file.getFileName().equals("")) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_NOT_EAD_ADD));
	}

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    //request.setAttribute(EadventureConstants.ATTR_INSTRUCTION_LIST, instructionList);
	    request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, gameForm.getSessionMapID());
	    return mapping.findForward("addgame");
	}

	// if the conditions list is not empty, add conditions to deleteConditionsList to delete form the data base
	// when the eadventure object will be persistent 

	//TODO comprobar, lo hacemos en update game tb.....
	/*
	 * Set<EadventureCondition> conditions = eadventure.getConditions();
	 * if (conditions.size()!=0){
	 * List delEadventureConditionList = getDeletedEadventureConditionList(sessionMap);
	 * delEadventureConditionList.addAll(conditions);
	 * sessionMap.put(EadventureConstants.ATTR_DELETED_CONDITION_LIST, delEadventureConditionList);
	 * // add the related expressions to their delete list
	 * List delEadventureExpressionList = this.getDeletedEadventureExpressionList(sessionMap);
	 * for (EadventureCondition condition: conditions){
	 * Set expressions = condition.getEadListExpression();
	 * delEadventureExpressionList.addAll(expressions);
	 * }
	 * //reset conditions
	 * eadventure.setConditions(new HashSet());
	 * }
	 */
	try {

	    IEadventureService service = getEadventureService();
	    service.uploadEadventureFile(eadventure, gameForm.getFile());

	} catch (Exception e) {
	    // any upload exception will display as normal error message rather
	    // then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(EadventureConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	    if (!errors.isEmpty()) {
		this.addErrors(request, errors);
		return mapping.findForward("addgame");
	    }
	}
	sessionMap.put(EadventureConstants.ATTR_HAS_FILE, "true");
	List<EadventureParam> paramList = this.getEdventureParamList(sessionMap);
	paramList.clear();
	paramList.addAll(eadventure.getParams());
	sessionMap.put(EadventureConstants.ATTR_PARAMS_LIST, paramList);
	sessionMap.put(EadventureConstants.ATTR_CHANGE_FILE, "true");
	((EadventureForm) sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM)).setHasFile(true);
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, gameForm.getSessionMapID());
	request.setAttribute(EadventureConstants.ATTR_GAME_DELETE, "false");
	// return null to close this window
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Remove eadventure game. It is a ajax call and just temporarily remove from page, all
     * permenant change will happen only when user sumbit this eadventure
     * again.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeGameAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	EadventureForm existForm = (EadventureForm) sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM);
	// Add to delete list all the conditions and game params
	List delCondList = getDeletedEadventureConditionList(sessionMap);
	delCondList.addAll(existForm.getEadventure().getConditions());

	//TODO comprobar que etsa bien: solo hay que meter en deleteExpresion cuando borramos la expression, no la cond (xk como
	// estan definidas las tablas)
	//Set<EadventureCondition> conditions = existForm.getEadventure().getConditions();
	//List<EadventureCondition> condList = new ArrayList<EadventureCondition>(conditions);
	//List delExprlList = getDeletedEadventureExpressionList(sessionMap);
	/*
	 * for (EadventureCondition eadc: condList ){
	 * delCondList.add(eadc);
	 * delExprlList.addAll(eadc.getEadListExpression());
	 * }
	 */

	// the same with the params
	List delParamsList = getDeletedEadventureParamsList(sessionMap);
	delParamsList.addAll(existForm.getEadventure().getParams());

	sessionMap.put(EadventureConstants.ATTR_CONDITION_LIST, null);
	sessionMap.put(EadventureConstants.ATTR_EXPRESSION_LIST, null);
	sessionMap.put(EadventureConstants.ATTR_PARAMS_LIST, null);

	existForm.resetFileInfo();
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	sessionMap.put(EadventureConstants.ATTR_HAS_FILE, "false");
	// TODO esta no funciona, se ha arreglado de otra manera en la view, comprobar
	request.setAttribute(EadventureConstants.ATTR_GAME_DELETE, 1);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Remove eadventure item from HttpSession list and update page display. As
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
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	//int itemIdx = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_ITEM_INDEX), -1);
	/*
	 * if (itemIdx != -1) {
	 * SortedSet<EadventureItem> eadventureList = getEadventureItemList(sessionMap);
	 * List<EadventureItem> rList = new ArrayList<EadventureItem>(eadventureList);
	 * EadventureItem item = rList.remove(itemIdx);
	 * eadventureList.clear();
	 * eadventureList.addAll(rList);
	 * // add to delList
	 * List delList = getDeletedEadventureItemList(sessionMap);
	 * delList.add(item);
	 * }
	 */

	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Ajax call, will add one more input line for new eadventure item
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
	int numberOfInstructions = getNumberOfInstructionsInRequest(request);
	List instructionList = new ArrayList(++numberOfInstructions);
	for (int idx = 0; idx < numberOfInstructions; idx++) {
	    String item = request.getParameter(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (item == null) {
		instructionList.add("");
	    } else {
		instructionList.add(item);
	    }
	}
	request.setAttribute(EadventureConstants.ATTR_INSTRUCTION_LIST, instructionList);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Ajax call, remove the given line of instruction of eadventure item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	int numberOfInstructions = getNumberOfInstructionsInRequest(request);
	int removeIdx = NumberUtils.stringToInt(request.getParameter("removeIdx"), -1);
	List instructionList = new ArrayList(numberOfInstructions - 1);
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
	request.setAttribute(EadventureConstants.ATTR_INSTRUCTION_LIST, instructionList);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * This method will persist all inforamtion in this authoring page.
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
	EadventureForm eadventureForm = (EadventureForm) form;

	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(eadventureForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	//TODO mirar si merece la pena llevar el form por sessionMap?¿?¿?
	//Eadventure eadventure = eadventureForm.getEadventure();
	Eadventure eadventure = ((EadventureForm) sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM))
		.getEadventure();
	//TODO fix this...
	eadventure.setInstructions(eadventureForm.getEadventure().getInstructions());
	eadventure.setDefineComplete(eadventureForm.getEadventure().isDefineComplete());
	eadventure.setDefineLater(eadventureForm.getEadventure().isDefineLater());
	eadventure.setLockWhenFinished(eadventureForm.getEadventure().getLockWhenFinished());
	eadventure.setReflectOnActivity(eadventureForm.getEadventure().isReflectOnActivity());
	eadventure.setReflectInstructions(eadventureForm.getEadventure().getReflectInstructions());
	eadventure.setTitle(eadventureForm.getEadventure().getTitle());

	ActionMessages errors = validate(eadventureForm, mapping, request);
	String file = eadventure.getFileName();
	if (file == null || file.equals("")) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_NOT_EAD_ADD));
	}
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    if (mode.isAuthor()) {
		return mapping.findForward("author");
	    } else {
		return mapping.findForward("monitor");
	    }
	}

	IEadventureService service = getEadventureService();

	// **********************************Get Eadventure
	// PO*********************
	Eadventure eadventurePO = service.getEadventureByContentId(eadventureForm.getEadventure().getContentId());
	if (eadventurePO == null) {
	    // new Eadventure, create it.
	    eadventurePO = eadventure;
	    eadventurePO.setCreated(new Timestamp(new Date().getTime()));
	    eadventurePO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    //TODO revisar el deine later!!
	    if (mode.isAuthor()) {
		Long uid = eadventurePO.getUid();
		//Set params = eadventurePO.getParams();
		//Set conditions = eadventurePO.getConditions();
		Date created = eadventurePO.getCreated();
		PropertyUtils.copyProperties(eadventurePO, eadventure);
		// get back UID
		eadventurePO.setUid(uid);
		eadventurePO.setCreated(created);
	    } else { // if it is Teacher, then just update basic tab content
		// (definelater)
		eadventurePO.setInstructions(eadventure.getInstructions());
		eadventurePO.setTitle(eadventure.getTitle());
		// change define later status
		eadventurePO.setDefineLater(false);
	    }
	    eadventurePO.setUpdated(new Timestamp(new Date().getTime()));
	}
	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	EadventureUser eadventureUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		eadventureForm.getEadventure().getContentId());
	if (eadventureUser == null) {
	    eadventureUser = new EadventureUser(user, eadventurePO);
	}
	eadventurePO.setCreatedBy(eadventureUser);
	// **********************************Handle Authoring Instruction
	// Attachement *********************
	// merge attachment info
	// so far, attPOSet will be empty if content is existed. because
	// PropertyUtils.copyProperties() is executed

	service.saveOrUpdateEadventureParams(eadventurePO.getParams());

	// Handle taskList conditions. Also delete conditions that don't contain any taskLIstItems.
	SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	List delEadventureConditionList = getDeletedEadventureConditionList(sessionMap);
	//iter = conditionList.iterator();
	if (conditionList.size() > 0) {
	    service.saveOrUpdateEadventureConditions(conditionList);
	}
	/*
	 * while (iter.hasNext()) {
	 * EadventureCondition condition = (EadventureCondition) iter.next();
	 * Set<EadventureExpression> eadExprList = condition.getEadListExpression();
	 * if (condition.getUid()==null){
	 * condition.setEadListExpression(null);
	 * service.saveOrUpdateEadventureCondition(condition);
	 * Iterator<EadventureExpression> it2 = eadExprList.iterator();
	 * while (it2.hasNext()){
	 * EadventureExpression eadExp = (EadventureExpression)it2.next();
	 * // eadExp.setCondition_uid(condition.getUid());
	 * service.saveOrUpdateEadventureExpression(eadExp);
	 * }
	 * }else {
	 * Iterator<EadventureExpression> it2 = eadExprList.iterator();
	 * while (it2.hasNext()){
	 * EadventureExpression eadExp = (EadventureExpression)it2.next();
	 * //eadExp.setCondition_uid(condition.getUid());
	 * service.saveOrUpdateEadventureExpression(eadExp);
	 * }
	 * service.saveOrUpdateEadventureCondition(condition);
	 * }
	 * 
	 * }
	 */
	eadventurePO.setConditions(conditionList);
	// this method also saves the eAdventure expressions associated to each condition in the list
	//TODO ver si merece la pena revisar si ha habido algun cambio en cada una de las condiciones, y en caso afirmativo guardar

	// delete TaEadventureConditionfrom database.
	Iterator iter = delEadventureConditionList.iterator();
	while (iter.hasNext()) {
	    EadventureCondition condition = (EadventureCondition) iter.next();
	    iter.remove();

	    if (condition.getUid() != null) {
		service.deleteEadventureCondition(condition.getUid());
	    }
	}

	//SortedSet<EadventureExpression> expressionList = getEadventureConditionList(sessionMap);
	//SortedSet<EadventureExpression> expressionListWithoutEmptyElements = new TreeSet<EadventureCondition>(conditionList);
	List delEadventureExpressionList = getDeletedEadventureExpressionList(sessionMap);

	// delete TaEadventureConditionfrom database.
	iter = delEadventureExpressionList.iterator();
	while (iter.hasNext()) {
	    EadventureExpression expression = (EadventureExpression) iter.next();
	    if (expression.getUid() != null) {
		service.deleteEadventureExpression(expression.getUid());
	    }
	    iter.remove();
	}

	// finally persist eadventurePO again
	//eadventurePO.setParams(eadventurePO.getParams());
	//if (eadventureForm.getFile()==null)
	String changeFile = (String) sessionMap.get(EadventureConstants.ATTR_CHANGE_FILE);
	String openSavedGame = (String) sessionMap.get(EadventureConstants.ATTR_OPEN_SAVED_GAME);
	if (changeFile.equals("true") && openSavedGame.equals("true")) {
	    //remove the old params

	    //service.removeParams(getDeletedEadventureConditionList(sessionMap));
	    List delParamsList = getDeletedEadventureParamsList(sessionMap);
	    Iterator it = delParamsList.iterator();
	    while (it.hasNext()) {
		EadventureParam eadp = (EadventureParam) it.next();
		service.removeParam(eadp);
	    }
	    //service.saveOrUpdateEadventureParams(eadventurePO.getParams());
	    //service.uploadEadventureFile(eadventurePO, eadventureForm.getFile());
	    //TODO borrar condiciones y expressiones

	}
	//TODO ver si hay que borrar params
	//if (openSavedGame.equals("false"))
	//  service.saveOrUpdateEadventureParams(eadventurePO.getParams());

	service.saveOrUpdateEadventure(eadventurePO);

	eadventureForm.setEadventure(eadventurePO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	} else {
	    return mapping.findForward("monitor");
	}
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return EadventureService bean.
     */
    private IEadventureService getEadventureService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IEadventureService) wac.getBean(EadventureConstants.RESOURCE_SERVICE);
    }

    /**
     * List save deleted eadventure conditions, which could be persisted or non-persisted conditions.
     *
     * @param request
     * @return
     */
    private List getDeletedEadventureConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_DELETED_CONDITION_LIST);
    }

    /**
     * List of deleted eadventure expressions, which could be persisted or non-persisted expressions.
     *
     * @param request
     * @return
     */
    private List getDeletedEadventureExpressionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_DELETED_EXPRESSION_LIST);
    }

    /**
     * List save deleted eadventure params, which could be persisted or non-persisted expressions.
     * 
     * @param request
     * @return
     */
    private List getDeletedEadventureParamsList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_DELETED_PARAMS_LIST);
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
     * Get eadventure items instruction from <code>HttpRequest</code>
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
     * Get number of instruction items in the <code>HttpRequest</code>
     *
     * @param request
     *            the HttpServletRequest
     * @return numberOfInstruction the number of instructions in the request
     */
    private int getNumberOfInstructionsInRequest(HttpServletRequest request) {
	int numberOfInstructions = 0;
	Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
	    if (e.nextElement().toString().indexOf(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX) != -1) {
		numberOfInstructions++;
	    }
	}
	return numberOfInstructions;
    }

    /**
     * List save current eadventure conditions.
     *
     * @param request
     * @return
     */
    private SortedSet<EadventureCondition> getEadventureConditionList(SessionMap sessionMap) {
	SortedSet<EadventureCondition> list = (SortedSet<EadventureCondition>) sessionMap
		.get(EadventureConstants.ATTR_CONDITION_LIST);
	if (list == null) {
	    list = new TreeSet<EadventureCondition>(new EadventureConditionComparator());
	    sessionMap.put(EadventureConstants.ATTR_CONDITION_LIST, list);
	}
	return list;
    }

    /**
     * List save current eadventure params.
     *
     * @param request
     * @return
     */

    private List getEdventureParamList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_PARAMS_LIST);
    }

    /**
     * Vaidate eadventure item regards to their type (url/file/learning
     * object/website zip file)
     *
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
     * // URL validation: Commom URL validate(1.3.0) work not very
     * // well: it can not support http://
     * // address:port format!!!
     * // UrlValidator validator = new UrlValidator();
     * // if(!validator.isValid(itemForm.getUrl()))
     * // errors.add(ActionMessages.GLOBAL_MESSAGE,new
     * // ActionMessage(EadventureConstants.ERROR_MSG_INVALID_URL));
     * }
     * }
     * // if(itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_WEBSITE
     * // ||itemForm.getItemType() ==
     * // EadventureConstants.RESOURCE_TYPE_LEARNING_OBJECT){
     * // if(StringUtils.isBlank(itemForm.getDescription()))
     * // errors.add(ActionMessages.GLOBAL_MESSAGE,new
     * // ActionMessage(EadventureConstants.ERROR_MSG_DESC_BLANK));
     * // }
     * if (itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_WEBSITE || itemForm.getItemType() ==
     * EadventureConstants.RESOURCE_TYPE_LEARNING_OBJECT
     * || itemForm.getItemType() == EadventureConstants.RESOURCE_TYPE_FILE) {
     * // validate item size
     * FileValidatorUtil.validateFileSize(itemForm.getFile(), true, errors);
     * // for edit validate: file already exist
     * if (!itemForm.isHasFile() && (itemForm.getFile() == null ||
     * StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
     * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_MSG_FILE_BLANK));
     * }
     * }
     * return errors;
     * }
     * /*
     * /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR
     * mode.
     *
     * @param request
     * 
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

    private ActionMessages validate(EadventureForm eadventureForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	if (StringUtils.isBlank(eadventureForm.getEadventure().getTitle())) {
	    ActionMessage error = new ActionMessage("error.resource.item.title.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	// define it later mode(TEACHER) skip below validation.
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errors;
	}

	// Some other validation outside basic Tab.

	return errors;
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	EadventurePedagogicalPlannerForm plannerForm = (EadventurePedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Eadventure taskList = getEadventureService().getEadventureByContentId(toolContentID);
	String command = WebUtil.readStrParam(request, AttributeNames.PARAM_COMMAND, true);
	if (command == null) {
	    plannerForm.fillForm(taskList);
	    String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	    plannerForm.setContentFolderID(contentFolderId);
	    return mapping.findForward(EadventureConstants.SUCCESS);
	} else {
	    try {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();

		if (AttributeNames.COMMAND_CHECK_EDITING_ADVICE.equals(command)) {
		    Integer activityIndex = WebUtil.readIntParam(request, AttributeNames.PARAM_ACTIVITY_INDEX);
		    String responseText = (StringUtils.isEmpty("") ? "NO" : "OK") + '&' + activityIndex;
		    writer.print(responseText);

		} else if (AttributeNames.COMMAND_GET_EDITING_ADVICE.equals(command)) {
		    writer.print("onlineInstructions");
		}
	    } catch (IOException e) {
		AuthoringAction.log.error(e);
	    }
	    return null;
	}

    }

    //TODO completar
    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	EadventurePedagogicalPlannerForm plannerForm = (EadventurePedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
/*
 * if (errors.isEmpty()) {
 * Eadventure taskList = getEadventureService().getEadventureByContentId(plannerForm.getToolContentID());
 * taskList.setInstructions(plannerForm.getInstructions());
 * 
 * int itemIndex = 0;
 * String title = null;
 * EadventureItem eadventureItem = null;
 * List<EadventureItem> newItems = new LinkedList<EadventureItem>();
 * Set<EadventureItem> eadventureItems = taskList.getEadventureItems();
 * Iterator<EadventureItem> taskListItemIterator = eadventureItems.iterator();
 * // We need to reverse the order, since the items are delivered
 * // newest-first
 * LinkedList<EadventureItem> reversedEadventureItems = new LinkedList<EadventureItem>();
 * while (taskListItemIterator.hasNext()) {
 * reversedEadventureItems.addFirst(taskListItemIterator.next());
 * }
 * taskListItemIterator = reversedEadventureItems.iterator();
 * do {
 * title = plannerForm.getTitle(itemIndex);
 * if (StringUtils.isEmpty(title)) {
 * plannerForm.removeItem(itemIndex);
 * } else {
 * if (taskListItemIterator.hasNext()) {
 * eadventureItem = taskListItemIterator.next();
 * } else {
 * eadventureItem = new EadventureItem();
 * eadventureItem.setCreateByAuthor(true);
 * Date currentDate = new Date();
 * eadventureItem.setCreateDate(currentDate);
 * 
 * HttpSession session = SessionManager.getSession();
 * UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);
 * EadventureUser taskListUser = getEadventureService().getUserByIDAndContent(new Long(user.getUserID().intValue()),
 * plannerForm.getToolContentID());
 * eadventureItem.setCreateBy(taskListUser);
 * 
 * newItems.add(eadventureItem);
 * }
 * eadventureItem.setTitle(title);
 * Short type = plannerForm.getType(itemIndex);
 * eadventureItem.setType(type);
 * boolean hasFile = eadventureItem.getFileUuid() != null;
 * if (type.equals(EadventureConstants.RESOURCE_TYPE_URL)) {
 * eadventureItem.setUrl(plannerForm.getUrl(itemIndex));
 * if (hasFile) {
 * eadventureItem.setFileName(null);
 * eadventureItem.setFileUuid(null);
 * eadventureItem.setFileVersionId(null);
 * eadventureItem.setFileType(null);
 * }
 * } else if (type.equals(EadventureConstants.RESOURCE_TYPE_FILE)) {
 * FormFile file = plannerForm.getFile(itemIndex);
 * eadventureItem.setUrl(null);
 * IEadventureService service = getEadventureService();
 * if (file != null) {
 * try {
 * if (hasFile) {
 * // delete the old file
 * service.deleteFromRepository(eadventureItem.getFileUuid(), eadventureItem.getFileVersionId());
 * }
 * service.uploadEadventureItemFile(eadventureItem, file);
 * } catch (Exception e) {
 * AuthoringAction.log.error(e);
 * ActionMessage error = new ActionMessage("error.msg.io.exception");
 * errors.add(ActionMessages.GLOBAL_MESSAGE, error);
 * saveErrors(request, errors);
 * plannerForm.setValid(false);
 * return mapping.findForward(EadventureConstants.SUCCESS);
 * }
 * }
 * plannerForm.setFileName(itemIndex, eadventureItem.getFileName());
 * plannerForm.setFileUuid(itemIndex, eadventureItem.getFileUuid());
 * plannerForm.setFileVersion(itemIndex, eadventureItem.getFileVersionId());
 * plannerForm.setFile(itemIndex, null);
 * }
 * itemIndex++;
 * }
 * 
 * } while (title != null);
 * // we need to clear it now, otherwise we get Hibernate error (item
 * // re-saved by cascade)
 * taskList.getEadventureItems().clear();
 * while (taskListItemIterator.hasNext()) {
 * eadventureItem = taskListItemIterator.next();
 * taskListItemIterator.remove();
 * getEadventureService().deleteEadventureItem(eadventureItem.getUid());
 * }
 * reversedEadventureItems.addAll(newItems);
 * 
 * taskList.getEadventureItems().addAll(reversedEadventureItems);
 * getEadventureService().saveOrUpdateEadventure(taskList);
 * } else {
 * saveErrors(request, errors);
 * }
 */
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    public ActionForward createPedagogicalPlannerItem(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	EadventurePedagogicalPlannerForm plannerForm = (EadventurePedagogicalPlannerForm) form;
	int insertIndex = plannerForm.getItemCount();
	plannerForm.setTitle(insertIndex, "");
	plannerForm.setType(insertIndex, new Short(request.getParameter(EadventureConstants.ATTR_ADD_RESOURCE_TYPE)));
	plannerForm.setUrl(insertIndex, null);
	plannerForm.setFileName(insertIndex, null);
	plannerForm.setFile(insertIndex, null);
	plannerForm.setFileUuid(insertIndex, null);
	plannerForm.setFileVersion(insertIndex, null);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

}
