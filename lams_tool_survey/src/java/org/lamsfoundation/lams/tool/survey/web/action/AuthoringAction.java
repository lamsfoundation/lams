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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAttachment;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.service.UploadSurveyFileException;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.tool.survey.web.form.QuestionForm;
import org.lamsfoundation.lams.tool.survey.web.form.SurveyForm;
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
	private static final int INIT_INSTRUCTION_COUNT = 2;
	private static final String INSTRUCTION_ITEM_DESC_PREFIX = "instructionItemDesc";
	private static final String INSTRUCTION_ITEM_COUNT = "instructionCount";
	private static final int SHORT_TITLE_LENGTH = 60;
	
	private static Logger log = Logger.getLogger(AuthoringAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String param = mapping.getParameter();
		//-----------------------Survey Author function ---------------------------
		if(param.equals("start")){
			ToolAccessMode mode = getAccessMode(request);
			//teacher mode "check for new" button enter.
			if(mode != null)
				request.setAttribute(AttributeNames.ATTR_MODE,mode.toString());
			else
				request.setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.AUTHOR.toString());
			return start(mapping, form, request, response);
		}
		if (param.equals("definelater")) {
			//update define later flag to true
			Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
			ISurveyService service = getSurveyService();
			Survey survey = service.getSurveyByContentId(contentId);
			
			boolean isEditable = SurveyWebUtils.isSurveyEditable(survey);
			if(!isEditable){
				request.setAttribute(SurveyConstants.PAGE_EDITABLE, new Boolean(isEditable));
				return mapping.findForward("forbidden");
			}
			
			if(!survey.isContentInUse()){
				survey.setDefineLater(true);
				service.saveOrUpdateSurvey(survey);
			}
			
			request.setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.TEACHER.toString());
			return start(mapping, form, request, response);
		}		
	  	if (param.equals("initPage")) {
       		return initPage(mapping, form, request, response);
        }

	  	if (param.equals("updateContent")) {
       		return updateContent(mapping, form, request, response);
        }
        if (param.equals("uploadOnlineFile")) {
       		return uploadOnline(mapping, form, request, response);
        }
        if (param.equals("uploadOfflineFile")) {
       		return uploadOffline(mapping, form, request, response);
        }
        if (param.equals("deleteOnlineFile")) {
        	return deleteOnlineFile(mapping, form, request, response);
        }
        if (param.equals("deleteOfflineFile")) {
        	return deleteOfflineFile(mapping, form, request, response);
        }
        //----------------------- Add survey item function ---------------------------
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
        //-----------------------Survey Item Instruction function ---------------------------
	  	if (param.equals("newInstruction")) {
       		return newInstruction(mapping, form, request, response);
        }
	  	if (param.equals("removeInstruction")) {
	  		return removeInstruction(mapping, form, request, response);
	  	}
        return mapping.findForward(SurveyConstants.ERROR);
	}


	/**
	 * Remove survey item from HttpSession list and update page display. As authoring rule, all persist only happen when 
	 * user submit whole page. So this remove is just impact HttpSession values.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
//		get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		int itemIdx = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ITEM_INDEX),-1);
		if(itemIdx != -1){
			SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
			List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
			SurveyQuestion item = rList.remove(itemIdx);
			surveyList.clear();
			surveyList.addAll(rList);
			//add to delList
			List delList = getDeletedSurveyItemList(sessionMap);
			delList.add(item);
		}	
		
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	
	/**
	 * Display edit page for existed survey item.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		QuestionForm itemForm = (QuestionForm) form;
//		get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		int itemIdx = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ITEM_INDEX),-1);
		SurveyQuestion item = null;
		if(itemIdx != -1){
			SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
			List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
			item = rList.get(itemIdx);
			if(item != null){
				populateItemToForm(itemIdx, item,itemForm,request);
			}
		}		
		if(itemForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)
			return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
		else
			return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
	}
	/**
	 * Display empty page for new survey item.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		QuestionForm questionForm = (QuestionForm)form;
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		questionForm.setSessionMapID(sessionMapID);
		
		List instructionList = new ArrayList(INIT_INSTRUCTION_COUNT);
		for(int idx=0;idx<INIT_INSTRUCTION_COUNT;idx++){
			instructionList.add("");
		}
		request.setAttribute("instructionList",instructionList);
		if(questionForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)
			return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
		else
			return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
	}
	/**
	 * This method will get necessary information from survey item form and save or update into 
	 * <code>HttpSession</code> SurveyItemList. Notice, this save is not persist them into database,  
	 * just save <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring 
	 * page is being persisted.
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws Exception {
		//get instructions:
		List<String> instructionList = getInstructionsFromRequest(request);
		
		QuestionForm itemForm = (QuestionForm)form;
		ActionErrors errors = validateSurveyItem(itemForm,instructionList);
		
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			//add at least 2 instruction list
			for(int idx=instructionList.size();idx < 2;idx++)
				instructionList.add("");
			request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST,instructionList);
			if(itemForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)
				return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
			else
				return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
		}
		
		try {
			extractFormToSurveyItem(request, instructionList, itemForm);
		} catch (Exception e) {
			log.error("Uploading failed. The exception is " + e.toString());
			throw e;
		}
		//set session map ID so that itemlist.jsp can get sessionMAP
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
		//return null to close this window
		return mapping.findForward(SurveyConstants.SUCCESS);
	}


	/**
	 * Read survey data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * @throws ServletException 
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//save toolContentID into HTTPSession
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
//		get back the survey and item list and display them on page
		ISurveyService service = getSurveyService();

		List questions = null;
		Survey survey = null;
		SurveyForm surveyForm = (SurveyForm)form;
		
		// Get contentFolderID and save to form.
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		surveyForm.setContentFolderID(contentFolderID);
				
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		surveyForm.setSessionMapID(sessionMap.getSessionID());
		
		survey = service.getSurveyByContentId(contentId);
		//if survey does not exist, try to use default content instead.
		if(survey == null){
			survey = service.getDefaultContent(contentId);
			if(survey.getQuestions() != null){
				questions = new ArrayList(survey.getQuestions());
			}else
				questions = null;
		}else
			questions = new ArrayList(survey.getQuestions());
		
		surveyForm.setSurvey(survey);

		//initialize instruction attachment list
		List attachmentList = getAttachmentList(sessionMap);
		attachmentList.clear();
		attachmentList.addAll(survey.getAttachments());
		
		//init it to avoid null exception in following handling
		if(questions == null)
			questions = new ArrayList();
		
		//init survey item list
		SortedSet<SurveyQuestion> surveyItemList = getSurveyItemList(sessionMap);
		surveyItemList.clear();
		retriveQuestionListForDisplay(questions);
		surveyItemList.addAll(questions);
		
		sessionMap.put(SurveyConstants.ATTR_SURVEY_FORM, surveyForm);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}


	/**
	 * Display same entire authoring page content from HttpSession variable.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException 
	 */
	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		SurveyForm existForm = (SurveyForm) sessionMap.get(SurveyConstants.ATTR_SURVEY_FORM);
		
		SurveyForm surveyForm = (SurveyForm )form;
		try {
			PropertyUtils.copyProperties(surveyForm, existForm);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		ToolAccessMode mode = getAccessMode(request);
		if(mode.isAuthor())
			return mapping.findForward(SurveyConstants.SUCCESS);
		else
			return mapping.findForward(SurveyConstants.DEFINE_LATER);
	}
	/**
	 * This method will persist all inforamtion in this authoring page, include all survey item, information etc.
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
		SurveyForm surveyForm = (SurveyForm)(form);
		
		//get back sessionMAP
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(surveyForm.getSessionMapID());
		
		ToolAccessMode mode = getAccessMode(request);
    	
		ActionMessages errors = validate(surveyForm, mapping, request);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			if(mode.isAuthor())
	    		return mapping.findForward("author");
	    	else
	    		return mapping.findForward("monitor");			
		}
			
		
		Survey survey = surveyForm.getSurvey();
		ISurveyService service = getSurveyService();
		
		//**********************************Get Survey PO*********************
		Survey surveyPO = service.getSurveyByContentId(surveyForm.getSurvey().getContentId());
		if(surveyPO == null){
			//new Survey, create it.
			surveyPO = survey;
			surveyPO.setCreated(new Timestamp(new Date().getTime()));
			surveyPO.setUpdated(new Timestamp(new Date().getTime()));
		}else{
			if(mode.isAuthor()){
				Long uid = surveyPO.getUid();
				PropertyUtils.copyProperties(surveyPO,survey);
				//get back UID
				surveyPO.setUid(uid);
			}else{ //if it is Teacher, then just update basic tab content (definelater)
				surveyPO.setInstructions(survey.getInstructions());
				surveyPO.setTitle(survey.getTitle());
//				change define later status
				surveyPO.setDefineLater(false);
			}
			surveyPO.setUpdated(new Timestamp(new Date().getTime()));
		}
		
		//*******************************Handle user*******************
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		SurveyUser surveyUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue())
						,surveyForm.getSurvey().getContentId());
		if(surveyUser == null){
			surveyUser = new SurveyUser(user,surveyPO);
		}
		
		surveyPO.setCreatedBy(surveyUser);
		
		//**********************************Handle Authoring Instruction Attachement *********************
    	//merge attachment info
		//so far, attPOSet will be empty if content is existed. because PropertyUtils.copyProperties() is executed
		Set attPOSet = surveyPO.getAttachments();
		if(attPOSet == null)
			attPOSet = new HashSet();
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		
		//current attachemnt in authoring instruction tab.
		Iterator iter = attachmentList.iterator();
		while(iter.hasNext()){
			SurveyAttachment newAtt = (SurveyAttachment) iter.next();
			attPOSet.add(newAtt);
		}
		attachmentList.clear();
		
		//deleted attachment. 2 possible types: one is persist another is non-persist before.
		iter = deleteAttachmentList.iterator();
		while(iter.hasNext()){
			SurveyAttachment delAtt = (SurveyAttachment) iter.next();
			iter.remove();
			//delete from repository
			service.deleteFromRepository(delAtt.getFileUuid(),delAtt.getFileVersionId());
			//it is an existed att, then delete it from current attachmentPO
			if(delAtt.getUid() != null){
				Iterator attIter = attPOSet.iterator();
				while(attIter.hasNext()){
					SurveyAttachment att = (SurveyAttachment) attIter.next();
					if(delAtt.getUid().equals(att.getUid())){
						attIter.remove();
						break;
					}
				}
				service.deleteSurveyAttachment(delAtt.getUid());
			}//end remove from persist value
		}
		
		//copy back
		surveyPO.setAttachments(attPOSet);
		//************************* Handle survey questions *******************
		//Handle survey items
		Set questionList = new LinkedHashSet();
		SortedSet topics = getSurveyItemList(sessionMap);
    	iter = topics.iterator();
    	while(iter.hasNext()){
    		SurveyQuestion item = (SurveyQuestion) iter.next();
    		if(item != null){
				//This flushs user UID info to message if this user is a new user. 
				item.setCreateBy(surveyUser);
				questionList.add(item);
    		}
    	}
    	surveyPO.setQuestions(questionList);
    	//delete instructino file from database.
    	List delSurveyItemList = getDeletedSurveyItemList(sessionMap);
    	iter = delSurveyItemList.iterator();
    	while(iter.hasNext()){
    		SurveyQuestion item = (SurveyQuestion) iter.next();
    		iter.remove();
    		if(item.getUid() != null)
    			service.deleteQuestion(item.getUid());
    	}
    	
		//**********************************************
		//finally persist surveyPO again
		service.saveOrUpdateSurvey(surveyPO);
		
		//initialize attachmentList again
		attachmentList = getAttachmentList(sessionMap);
		attachmentList.addAll(survey.getAttachments());
		surveyForm.setSurvey(surveyPO);
		
		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
    	if(mode.isAuthor())
    		return mapping.findForward("author");
    	else
    		return mapping.findForward("monitor");
	}

	/**
	 * Handle upload online instruction files request. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UploadSurveyFileException 
	 */
	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UploadSurveyFileException {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
	}
	/**
	 * Handle upload offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UploadSurveyFileException 
	 */
	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UploadSurveyFileException {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);
	}
	/**
	 * Common method to upload online or offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 * @throws UploadSurveyFileException 
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) throws UploadSurveyFileException {

		SurveyForm surveyForm = (SurveyForm) form;
		//get back sessionMAP
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(surveyForm.getSessionMapID());

		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) surveyForm.getOfflineFile();
		else
			file = (FormFile) surveyForm.getOnlineFile();
		
		ISurveyService service = getSurveyService();
		//upload to repository
		SurveyAttachment  att = service.uploadInstructionFile(file, type);
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		SurveyAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (SurveyAttachment) iter.next();
			if(StringUtils.equals(existAtt.getFileName(),att.getFileName())){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
				break;
			}
		}
		//add to attachmentList
		attachmentList.add(att);

		return mapping.findForward(SurveyConstants.SUCCESS);

	}
	/**
	 * Delete offline instruction file from current Survey authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping,request, response,form, IToolContentHandler.TYPE_OFFLINE);
	}
	/**
	 * Delete online instruction file from current Survey authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, request, response,form, IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * General method to delete file (online or offline)
	 * @param mapping 
	 * @param request
	 * @param response
	 * @param form 
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form, String type) {
		Long versionID = new Long(WebUtil.readLongParam(request,SurveyConstants.PARAM_FILE_VERSION_ID));
		Long uuID = new Long(WebUtil.readLongParam(request,SurveyConstants.PARAM_FILE_UUID));
		
		//get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		SurveyAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (SurveyAttachment) iter.next();
			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
			}
		}

		request.setAttribute(SurveyConstants.ATTR_FILE_TYPE_FLAG, type);
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(SurveyConstants.SUCCESS);

	}
	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	/**
	 * Return SurveyService bean.
	 */
	private ISurveyService getSurveyService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SurveyConstants.ATT_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SurveyConstants.ATTR_DELETED_ATTACHMENT_LIST);
	}
	/**
	 * List save current survey items.
	 * @param request
	 * @return
	 */
	private SortedSet<SurveyQuestion> getSurveyItemList(SessionMap sessionMap) {
		SortedSet<SurveyQuestion> list = (SortedSet<SurveyQuestion>) sessionMap.get(SurveyConstants.ATTR_QUESTION_LIST);
		if(list == null){
			list = new TreeSet<SurveyQuestion>(new QuestionsComparator());
			sessionMap.put(SurveyConstants.ATTR_QUESTION_LIST,list);
		}
		return list;
	}	
	/**
	 * List save deleted survey items, which could be persisted or non-persisted items. 
	 * @param request
	 * @return
	 */
	private List getDeletedSurveyItemList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SurveyConstants.ATTR_DELETED_QUESTION_LIST);
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
	 * Get survey items instruction from <code>HttpRequest</code>
	 * @param request
	 */
	private List<String> getInstructionsFromRequest(HttpServletRequest request) {
		String list = request.getParameter("instructionList");
		List<String> instructionList = new ArrayList<String>();
		//for open text entry question
		if(list == null)
			return instructionList;
		
		String[] params = list.split("&");
		Map<String,String> paramMap = new HashMap<String,String>();
		String[] pair;
		for (String item: params) {
			pair = item.split("=");
			if(pair == null || pair.length != 2)
				continue;
			try {
				paramMap.put(pair[0],URLDecoder.decode(pair[1],"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("Error occurs when decode instruction string:" + e.toString());
			}
		}
		
		int count = NumberUtils.stringToInt(paramMap.get(INSTRUCTION_ITEM_COUNT));
		for(int idx=0;idx<count;idx++){
			String item = paramMap.get(INSTRUCTION_ITEM_DESC_PREFIX+idx);
			if(item == null)
				continue;
			instructionList.add(item);
		}
		return instructionList;
	}


	/**
	 * This method will populate survey item information to its form for edit use.
	 * @param itemIdx
	 * @param item
	 * @param form
	 * @param request
	 */
	private void populateItemToForm(int itemIdx, SurveyQuestion item, QuestionForm form, HttpServletRequest request) {
		if(itemIdx >=0)
			form.setItemIndex(new Integer(itemIdx).toString());
		
		//set questions
		form.setQuestion(item);
		
		//set options
		Set<SurveyOption> instructionList = item.getOptions();
		List instructions = new ArrayList();
		for(SurveyOption in : instructionList){
			instructions.add(in.getDescription());
		}
		
		request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST,instructions);
		
	}
	/**
	 * Extract web from content to survey item.
	 * @param request
	 * @param instructionList
	 * @param itemForm
	 * @throws SurveyApplicationException 
	 */
	private void extractFormToSurveyItem(HttpServletRequest request, List<String> instructionList, QuestionForm itemForm) 
		throws Exception {
		/* BE CAREFUL: This method will copy nessary info from request form to a old or new SurveyItem instance.
		 * It gets all info EXCEPT SurveyItem.createDate and SurveyItem.createBy, which need be set when persisting 
		 * this survey item.
		 */
		
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(itemForm.getSessionMapID());
		//check whether it is "edit(old item)" or "add(new item)"
		SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
		int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(),-1);
		SurveyQuestion item = itemForm.getQuestion();
		
		if(itemIdx == -1){ //add
			item.setCreateDate(new Timestamp(new Date().getTime()));
			int maxSeq = 1;
			if(surveyList != null && surveyList.size() > 0){
				SurveyQuestion last = surveyList.last();
				maxSeq = last.getSequenceId()+1;
			}
			item.setSequenceId(maxSeq);
			surveyList.add(item);
		}else{ //edit
			List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
			item = rList.get(itemIdx);
			item.setDescription(itemForm.getQuestion().getDescription());
			item.setOptional(itemForm.getQuestion().isOptional());
			item.setAppendText(itemForm.getQuestion().isAppendText());
			item.setAllowMultipleAnswer(itemForm.getQuestion().isAllowMultipleAnswer());
			
		}
		retriveQuestionForDisplay(item);
		short type = getQuestionType(itemForm);
		item.setType(type);

		//set instrcutions
		Set instructions = new LinkedHashSet();
		int idx=0;
		for (String ins : instructionList) {
			SurveyOption rii = new SurveyOption();
			rii.setDescription(ins);
			rii.setSequenceId(idx++);
			instructions.add(rii);
		}
		item.setOptions(instructions);

		
		
	}


	private short getQuestionType(QuestionForm itemForm) {
		//set question type
		short type;
		if(itemForm.getItemType() == SurveyConstants.SURVEY_TYPE_TEXT_ENTRY)
			type = SurveyConstants.SURVEY_TYPE_TEXT_ENTRY;
		else if( itemForm.getQuestion().isAllowMultipleAnswer())
			type = SurveyConstants.SURVEY_TYPE_MULTIPLE_CHOICES;
		else 
			type = SurveyConstants.SURVEY_TYPE_SINGLE_CHOICE;
		return type;
	}
	
	private void retriveQuestionListForDisplay(List<SurveyQuestion> list) {
		for(SurveyQuestion item: list){
			retriveQuestionForDisplay(item);
		}
	}
	private void retriveQuestionForDisplay(SurveyQuestion item) {
		String desc = item.getDescription();
		desc = desc.replaceAll("<(.|\n)*?>", "");
		item.setShortTitle(StringUtils.abbreviate(desc,SHORT_TITLE_LENGTH));
	}

	/**
	 * Vaidate survey item regards to their type (url/file/learning object/website zip file)
	 * @param itemForm
	 * @param instructionList 
	 * @return
	 */
	private ActionErrors validateSurveyItem(QuestionForm itemForm, List<String> instructionList) {
		ActionErrors errors = new ActionErrors();
		if(StringUtils.isBlank(itemForm.getQuestion().getDescription()))
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_DESC_BLANK));
		
		short type = getQuestionType(itemForm);
		if(type != SurveyConstants.QUESTION_TYPE_TEXT_ENTRY){
			if(instructionList == null || instructionList.size() < 2)
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SurveyConstants.ERROR_MSG_LESS_OPTIONS));
		}
		
		return errors;
	}

	/**
	 * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
	 * @param request
	 * @return
	 */
	private ToolAccessMode getAccessMode(HttpServletRequest request) {
		ToolAccessMode mode;
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equalsIgnoreCase(modeStr,ToolAccessMode.TEACHER.toString()))
			mode = ToolAccessMode.TEACHER;
		else
			mode = ToolAccessMode.AUTHOR;
		return mode;
	}
	
	
	private ActionMessages validate(SurveyForm surveyForm, ActionMapping mapping, HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
		if (StringUtils.isBlank(surveyForm.getSurvey().getTitle())) {
			ActionMessage error = new ActionMessage("error.survey.item.title.blank");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
		//define it later mode(TEACHER) skip below validation.
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())){
			return errors;
		}

		//Some other validation outside basic Tab.
		
		return errors;
	}
	/**
	 * Ajax call, will add one more input line for new survey item instruction.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int count = NumberUtils.stringToInt(request.getParameter(INSTRUCTION_ITEM_COUNT),0);
		List instructionList = new ArrayList(++count);
		for(int idx=0;idx<count;idx++){
			String item = request.getParameter(INSTRUCTION_ITEM_DESC_PREFIX+idx);
			if(item == null)
				instructionList.add("");
			else
				instructionList.add(item);
		}
		request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST,instructionList);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Ajax call, remove the given line of instruction of survey item.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward removeInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int count = NumberUtils.stringToInt(request.getParameter(INSTRUCTION_ITEM_COUNT),0);
		int removeIdx = NumberUtils.stringToInt(request.getParameter("removeIdx"),-1);
		List instructionList = new ArrayList(count-1);
		for(int idx=0;idx<count;idx++){
			String item = request.getParameter(INSTRUCTION_ITEM_DESC_PREFIX+idx);
			if(idx == removeIdx)
				continue;
			if(item == null)
				instructionList.add("");
			else
				instructionList.add(item);
		}
		request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST,instructionList);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}

}
