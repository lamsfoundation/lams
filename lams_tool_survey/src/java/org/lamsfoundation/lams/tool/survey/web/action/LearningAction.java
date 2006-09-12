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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.survey.web.action;

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
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.service.UploadSurveyFileException;
import org.lamsfoundation.lams.tool.survey.util.SurveyItemComparator;
import org.lamsfoundation.lams.tool.survey.web.form.ReflectionForm;
import org.lamsfoundation.lams.tool.survey.web.form.SurveyItemForm;
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
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
		//-----------------------Survey Learner function ---------------------------
		if(param.equals("start")){
			return start(mapping, form, request, response);
		}
		if(param.equals("complete")){
			return complete(mapping, form, request, response);
		}

		if(param.equals("finish")){
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
        
		//================ Reflection =======================
		if (param.equals("newReflection")) {
			return newReflection(mapping, form, request, response);
		}
		if (param.equals("submitReflection")) {
			return submitReflection(mapping, form, request, response);
		}
		
		return  mapping.findForward(SurveyConstants.ERROR);
	}
	/**
	 * Initial page for add survey item (single file or URL).
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		SurveyItemForm itemForm = (SurveyItemForm) form;
		itemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
		itemForm.setSessionMapID(WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID));
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Read survey data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		
		//save toolContentID into HTTPSession
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, true);
		
		Long sessionId =  new Long(request.getParameter(SurveyConstants.PARAM_TOOL_SESSION_ID));
		
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		
//		get back the survey and item list and display them on page
		ISurveyService service = getSurveyService();
		SurveyUser surveyUser = getCurrentUser(service,sessionId);

		List<SurveyQuestion> items = null;
		Survey survey;
		items = service.getSurveyItemsBySessionId(sessionId);
		survey = service.getSurveyBySessionId(sessionId);

		//check whehter finish lock is on/off
		boolean lock = survey.getLockWhenFinished() && surveyUser.isSessionFinished();
		
		
		//check whether there is only one survey item and run auto flag is true or not.
		boolean runAuto = false;
		int itemsNumber = 0;
		if(survey.getSurveyItems() != null){
			itemsNumber = survey.getSurveyItems().size();
			if(survey.isRunAuto() && itemsNumber == 1){
				SurveyQuestion item = (SurveyQuestion) survey.getSurveyItems().iterator().next();
				//only visible item can be run auto.
				if(!item.isHide()){
					runAuto = true;
					request.setAttribute(SurveyConstants.ATTR_RESOURCE_ITEM_UID,item.getUid());
				}
			}
		}
		
		//basic information
		sessionMap.put(SurveyConstants.ATTR_TITLE,survey.getTitle());
		sessionMap.put(SurveyConstants.ATTR_RESOURCE_INSTRUCTION,survey.getInstructions());
		sessionMap.put(SurveyConstants.ATTR_FINISH_LOCK,lock);
		
		sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		sessionMap.put(AttributeNames.ATTR_MODE,mode);
		//reflection information
		sessionMap.put(SurveyConstants.ATTR_REFLECTION_ON,survey.isReflectOnActivity());
		sessionMap.put(SurveyConstants.ATTR_REFLECTION_INSTRUCTION,survey.getReflectInstructions());
		sessionMap.put(SurveyConstants.ATTR_RUN_AUTO,new Boolean(runAuto));
		
		//add define later support
		if(survey.isDefineLater()){
			return mapping.findForward("defineLater");
		}
		
		//set contentInUse flag to true!
		survey.setContentInUse(true);
		survey.setDefineLater(false);
		service.saveOrUpdateSurvey(survey);
		
		//add run offline support
		if(survey.getRunOffline()){
			sessionMap.put(SurveyConstants.PARAM_RUN_OFFLINE, true);
			return mapping.findForward("runOffline");
		}else
			sessionMap.put(SurveyConstants.PARAM_RUN_OFFLINE, false);
				
		//init survey item list
		SortedSet<SurveyQuestion> surveyItemList = getSurveyItemList(sessionMap);
		surveyItemList.clear();
		if(items != null){
			//remove hidden items.
			for(SurveyQuestion item : items){
				//becuase in webpage will use this login name. Here is just 
				//initial it to avoid session close error in proxy object. 
				item.getCreateBy().getLoginName();
				if(!item.isHide()){
					surveyItemList.add(item);
				}
			}
		}
		
		//set complete flag for display purpose
		service.retrieveComplete(surveyItemList, surveyUser);
		
		sessionMap.put(SurveyConstants.ATTR_RESOURCE,survey);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Mark survey item as complete status. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String mode = request.getParameter(AttributeNames.ATTR_MODE);
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		
		doComplete(request);
		
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID,sessionMapID);
		return  mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Finish learning session. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//get back SessionMap
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		//get mode and ToolSessionID from sessionMAP
		ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		//auto run mode, when use finish the only one survey item, mark it as complete then finish this activity as well.
		String surveyItemUid = request.getParameter(SurveyConstants.PARAM_RESOURCE_ITEM_UID);
		if(surveyItemUid != null){
			doComplete(request);
			//NOTE:So far this flag is useless(31/08/2006).
			//set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
			request.setAttribute(SurveyConstants.ATTR_RUN_AUTO,true);
		}else
			request.setAttribute(SurveyConstants.ATTR_RUN_AUTO,false);
		
		if(!validateBeforeFinish(request, sessionMapID))
			return mapping.getInputForward();
		
		ISurveyService service = getSurveyService();
		// get sessionId from HttpServletRequest
		String nextActivityUrl = null ;
		try {
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());
			
			nextActivityUrl = service.finishToolSession(sessionId,userID);
			request.setAttribute(SurveyConstants.ATTR_NEXT_ACTIVITY_URL,nextActivityUrl);
		} catch (SurveyApplicationException e) {
			log.error("Failed get next activity url:" + e.getMessage());
		}
		
		return  mapping.findForward(SurveyConstants.SUCCESS);
	}

	
	/**
	 * Save file or url survey item into database.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//get back SessionMap
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		
		Long sessionId = (Long) sessionMap.get(SurveyConstants.ATTR_TOOL_SESSION_ID);
		
		String mode = request.getParameter(AttributeNames.ATTR_MODE);
		SurveyItemForm itemForm = (SurveyItemForm)form;
		ActionErrors errors = validateSurveyItem(itemForm);
		
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			return findForward(itemForm.getItemType(),mapping);
		}
		short type = itemForm.getItemType();
		
		//create a new SurveyItem
		SurveyQuestion item = new SurveyQuestion(); 
		ISurveyService service = getSurveyService();
		SurveyUser surveyUser = getCurrentUser(service,sessionId);
		item.setType(type);
		item.setTitle(itemForm.getTitle());
		item.setDescription(itemForm.getDescription());
		item.setCreateDate(new Timestamp(new Date().getTime()));
		item.setCreateByAuthor(false);
		item.setCreateBy(surveyUser);
		
		//special attribute for URL or FILE
		if(type == SurveyConstants.RESOURCE_TYPE_FILE){
			try {
				service.uploadSurveyItemFile(item, itemForm.getFile());
			} catch (UploadSurveyFileException e) {
				log.error("Failed upload Survey File " + e.toString());
				return  mapping.findForward(SurveyConstants.ERROR);
			}
		}else if(type == SurveyConstants.RESOURCE_TYPE_URL){
			item.setUrl(itemForm.getUrl());
			item.setAppendText(itemForm.isOpenUrlNewWindow());
		}
		//save and update session
		
		SurveySession resSession = service.getSurveySessionBySessionId(sessionId);
		if(resSession == null){
			log.error("Failed update SurveySession by ID[" + sessionId + "]");
			return  mapping.findForward(SurveyConstants.ERROR);
		}
		Set<SurveyQuestion> items = resSession.getSurveyQuestions();
		if(items == null){
			items = new HashSet<SurveyQuestion>();
			resSession.setSurveyQuestions(items);
		}
		items.add(item);
		service.saveOrUpdateSurveySession(resSession);
		
		//update session value
		SortedSet<SurveyQuestion> surveyItemList = getSurveyItemList(sessionMap);
		surveyItemList.add(item);
		
		//URL or file upload
		request.setAttribute(SurveyConstants.ATTR_ADD_RESOURCE_TYPE,new Short(type));
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		return  mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Display empty reflection form.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		//get session value
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		if(!validateBeforeFinish(request, sessionMapID))
			return mapping.getInputForward();

		ReflectionForm refForm = (ReflectionForm) form;
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		refForm.setUserID(user.getUserID());
		refForm.setSessionMapID(sessionMapID);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Submit reflection form input database.
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
		
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		ISurveyService service = getSurveyService();
		service.createNotebookEntry(sessionId, 
				CoreNotebookConstants.NOTEBOOK_TOOL,
				SurveyConstants.TOOL_SIGNATURE, 
				userId,
				refForm.getEntryText());

		return finish(mapping, form, request, response);
	}
	

	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());
		
		ISurveyService service = getSurveyService();
		int miniViewFlag = service.checkMiniView(sessionId,userID);
		//if current user view less than reqired view count number, then just return error message.
		//if it is runOffline content, then need not check minimum view count
		Boolean runOffline = (Boolean) sessionMap.get(SurveyConstants.PARAM_RUN_OFFLINE);
		if(miniViewFlag > 0 && !runOffline){
			ActionErrors errors = new ActionErrors();
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("lable.learning.minimum.view.number.less",miniViewFlag));
			this.addErrors(request,errors);
			return false;
		}
		
		return true;
	}
	private ISurveyService getSurveyService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ISurveyService) wac.getBean(SurveyConstants.RESOURCE_SERVICE);
	}
	/**
	 * List save current survey items.
	 * @param request
	 * @return
	 */
	private SortedSet<SurveyQuestion> getSurveyItemList(SessionMap sessionMap) {
		SortedSet<SurveyQuestion> list = (SortedSet<SurveyQuestion>) sessionMap.get(SurveyConstants.ATTR_RESOURCE_ITEM_LIST);
		if(list == null){
			list = new TreeSet<SurveyQuestion>(new SurveyItemComparator());
			sessionMap.put(SurveyConstants.ATTR_RESOURCE_ITEM_LIST,list);
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
	private List getListFromSession(SessionMap sessionMap,String name) {
		List list = (List) sessionMap.get(name);
		if(list == null){
			list = new ArrayList();
			sessionMap.put(name,list);
		}
		return list;
	}

	/**
	 * Return <code>ActionForward</code> according to survey item type.
	 * @param type
	 * @param mapping
	 * @return
	 */
	private ActionForward findForward(short type, ActionMapping mapping) {
		ActionForward forward;
		switch (type) {
		case SurveyConstants.RESOURCE_TYPE_URL:
			forward = mapping.findForward("url");
			break;
		case SurveyConstants.RESOURCE_TYPE_FILE:
			forward = mapping.findForward("file");
			break;
		case SurveyConstants.RESOURCE_TYPE_WEBSITE:
			forward = mapping.findForward("website");
			break;
		case SurveyConstants.RESOURCE_TYPE_LEARNING_OBJECT:
			forward = mapping.findForward("learningobject");
			break;
		default:
			forward = null;
			break;
		}
		return forward;
	}

	private SurveyUser getCurrentUser(ISurveyService service, Long sessionId) {
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		SurveyUser surveyUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),sessionId);
		
		if(surveyUser == null){
			SurveySession session = service.getSurveySessionBySessionId(sessionId);
			surveyUser = new SurveyUser(user,session);
			service.createUser(surveyUser);
		}
		return surveyUser;
	}
	/**
	 * @param itemForm
	 * @return
	 */
	private ActionErrors validateSurveyItem(SurveyItemForm itemForm) {
		ActionErrors errors = new ActionErrors();
		if(StringUtils.isBlank(itemForm.getTitle()))
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_TITLE_BLANK));
		
		if(itemForm.getItemType() == SurveyConstants.RESOURCE_TYPE_URL){
			if(StringUtils.isBlank(itemForm.getUrl()))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_URL_BLANK));
			//URL validation: Commom URL validate(1.3.0) work not very well: it can not support http://address:port format!!!
//			UrlValidator validator = new UrlValidator();
//			if(!validator.isValid(itemForm.getUrl()))
//				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_INVALID_URL));
		}
//		if(itemForm.getItemType() == SurveyConstants.RESOURCE_TYPE_WEBSITE 
//				||itemForm.getItemType() == SurveyConstants.RESOURCE_TYPE_LEARNING_OBJECT){
			if(StringUtils.isBlank(itemForm.getDescription()))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_DESC_BLANK));
//		}
		if(itemForm.getItemType() == SurveyConstants.RESOURCE_TYPE_WEBSITE 
				||itemForm.getItemType() == SurveyConstants.RESOURCE_TYPE_LEARNING_OBJECT
				||itemForm.getItemType() == SurveyConstants.RESOURCE_TYPE_FILE){
			//for edit validate: file already exist
			if(!itemForm.isHasFile() &&
				(itemForm.getFile() == null || StringUtils.isEmpty(itemForm.getFile().getFileName())))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_FILE_BLANK));
		}
		return errors;
	}
	/**
	 * Set complete flag for given survey item.
	 * @param request
	 * @param sessionId 
	 */
	private void doComplete(HttpServletRequest request) {
		//get back sessionMap
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		Long surveyItemUid = new Long(request.getParameter(SurveyConstants.PARAM_RESOURCE_ITEM_UID));
		ISurveyService service = getSurveyService();
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		Long sessionId =  (Long) sessionMap.get(SurveyConstants.ATTR_TOOL_SESSION_ID);
		service.setItemComplete(surveyItemUid,new Long(user.getUserID().intValue()),sessionId);
		
		//set survey item complete tag
		SortedSet<SurveyQuestion> surveyItemList = getSurveyItemList(sessionMap);
		for(SurveyQuestion item:surveyItemList){
			if(item.getUid().equals(surveyItemUid)){
				item.setComplete(true);
				break;
			}
		}
	}

}
