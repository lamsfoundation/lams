/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaToolContentHandler;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 
 *  setAsForceComplete(Long userId) throws QaApplicationException ? 
 */


/**
 * <p>Action class that controls the logic of tool behavior. </p>
 * 
 * <p>Note that Struts action class only has the responsibility to navigate 
 * page flow. All database operation should go to service layer and data 
 * transformation from domain model to struts form bean should go to form 
 * bean class. This ensure clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is
 * identified. No system exception error handling code should appear in the 
 * Struts action class as all of them are handled in 
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 * @author Ozgur Demirtas
 * 
	<action
      path="/authoring"
      type="org.lamsfoundation.lams.tool.qa.web.QaAction"
      name="QaAuthoringForm"
      input="/AuthoringMaincontent.jsp"
      parameter="dispatch"
      scope="request"
      unknown="false"
      validate="false"
    >
    
	  	<forward
	        name="load"
	        path="/AuthoringMaincontent.jsp"
	        redirect="false"
		/>
	
	  	<forward
			name="loadMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>
	
	  	<forward
		    name="refreshMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>
	  
	    <forward
			name="loadViewOnly"
			path="/authoring/AuthoringTabsHolder.jsp"
			redirect="false"
	    />
	  
	  	<forward
	        name="starter"
	        path="/index.jsp"
	        redirect="false"
	  	/>
  </action>

 * 
 */
public class QaAction extends LamsDispatchAction implements QaAppConstants
{
    static Logger logger = Logger.getLogger(QaAction.class.getName());
    
    private QaToolContentHandler toolContentHandler;
    
    /** 
     * <p>Struts dispatch method.</p> 
     * 
     * <p>It is assuming that progress engine should pass in the tool access
     * mode and the tool session id as http parameters.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     * @throws QaApplicationException the known runtime exception 
     * 
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    /**
     * submits content into the tool database
     * ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	
    	logger.debug("dispathcing submitAllContent :" +form);
    	
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=qaAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);

		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
        Map mapQuestionContent=(Map)sessionMap.get(MAP_QUESTION_CONTENT_KEY);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
        
        ActionMessages errors= new ActionMessages();
        /* full form validation should be performed only in standard authoring mode, but not in monitoring EditActivity */
        /*
        errors=validateSubmit(request, errors, qaAuthoringForm);

        if (errors.size() > 0)  
        {
            logger.debug("returning back to from to fix errors:");
            //qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
            return (mapping.findForward(LOAD_QUESTIONS));
        }
        */
        
        
		List attachmentListBackup= new  ArrayList();
        List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);
	    attachmentListBackup=attachmentList;
	    
	    List deletedAttachmentListBackup= new  ArrayList();
	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
	    deletedAttachmentListBackup=deletedAttachmentList;
	    
	    AuthoringUtil authoringUtil= new AuthoringUtil();
        authoringUtil.reconstructQuestionContentMapForSubmit(mapQuestionContent, request);
        logger.debug("before saveOrUpdateQaContent." + mapQuestionContent);
        
	 	
	 	QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
	 	String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);
	 	
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
        qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
        
        sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
        
        
        String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
        logger.debug("onlineInstructions: " + onlineInstructions);
	    qaGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

        String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
        logger.debug("offlineInstructions: " + offlineInstructions);
	    qaGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
        
	    qaGeneralAuthoringDTO.setAttachmentList(attachmentList);
		qaGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
		
		String defaultQuestionContent=request.getParameter("questionContent0");
		logger.debug("defaultQuestionContent: " + defaultQuestionContent);
		qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);

		String reportTitle=request.getParameter(REPORT_TITLE);
		logger.debug("reportTitle: " + reportTitle);
		qaAuthoringForm.setReportTitle(reportTitle);
		qaGeneralAuthoringDTO.setReportTitle(reportTitle);
		
		String monitoringReportTitle=request.getParameter(MONITORING_REPORT_TITLE);
		logger.debug("monitoringReportTitle: " + monitoringReportTitle);
		qaAuthoringForm.setMonitoringReportTitle(monitoringReportTitle);
		qaGeneralAuthoringDTO.setMonitoringReportTitle(monitoringReportTitle);
		
		String endLearningMessage=request.getParameter(END_LEARNING_MESSSAGE);
		logger.debug("endLearningMessage: " + endLearningMessage);
		qaAuthoringForm.setEndLearningMessage(endLearningMessage);
		qaGeneralAuthoringDTO.setEndLearningMessage(endLearningMessage);
		
		
		logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

		
        boolean verifyDuplicatesOptionsMap=AuthoringUtil.verifyDuplicatesOptionsMap(mapQuestionContent);
	 	logger.debug("verifyDuplicatesOptionsMap: " + verifyDuplicatesOptionsMap);

	 	if (verifyDuplicatesOptionsMap == false)
 		{
 			errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("error.questions.duplicate"));
			saveErrors(request,errors);
			qaGeneralAuthoringDTO.setUserExceptionQuestionsDuplicate(new Boolean(true).toString());
			
			logger.debug("error: qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
			request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
			
			qaAuthoringForm.resetUserAction();
			logger.debug("going back to form to fix error: " + LOAD_QUESTIONS);
            return (mapping.findForward(LOAD_QUESTIONS));
 		}
        
        
	 	logger.debug("there are no issues with input, continue and submit data");
        /*to remove deleted entries in the questions table based on mapQuestionContent */
        authoringUtil.removeRedundantQuestions(mapQuestionContent, qaService, qaAuthoringForm, request, strToolContentID);
        logger.debug("end of removing unused entries... ");

        QaContent qaContentTest=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContentTest: " + qaContentTest);

		QaContent qaContent=authoringUtil.saveOrUpdateQaContent(mapQuestionContent, qaService, qaAuthoringForm, 
                request, qaContentTest, strToolContentID);
        logger.debug("qaContent: " + qaContent);
        
		qaGeneralAuthoringDTO= QaUtils.buildGeneralAuthoringDTO(request, qaService, qaContent, qaAuthoringForm);
		logger.debug("updated qaGeneralAuthoringDTO to: " + qaGeneralAuthoringDTO);
		
        authoringUtil.reOrganizeDisplayOrder(mapQuestionContent, qaService, qaAuthoringForm, qaContent);

        logger.debug("activeModule: " + activeModule);
        if (activeModule.equals(AUTHORING))
        {
            List attacments=saveAttachments(qaContent, attachmentList, deletedAttachmentList, mapping, request);
            logger.debug("attacments: " + attacments);
        }

        errors.clear();
        errors.add(Globals.ERROR_KEY, new ActionMessage("submit.successful"));
        qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());
        
        logger.debug("setting SUBMIT_SUCCESS to 1.");
        
	    logger.debug("strToolContentID: " + strToolContentID);
        QaUtils.setDefineLater(request, false, strToolContentID, qaService);
        
        saveErrors(request,errors);
        
        QaUtils.setDefineLater(request, false, qaService, strToolContentID);
        logger.debug("define later set to false");
        
        qaAuthoringForm.resetUserAction();
        
        qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
        
		QaUtils.setFormProperties(request, qaService, qaContent, 
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);


		qaGeneralAuthoringDTO.setAttachmentList(attachmentListBackup);
		qaGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentListBackup);

		defaultQuestionContent=request.getParameter("questionContent0");
		logger.debug("defaultQuestionContent: " + defaultQuestionContent);
		qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);

		
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

        request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
        return mapping.findForward(LOAD_QUESTIONS);
    }

    
    /**
     * adds a new question to the questions map
     * ActionForward addNewQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addNewQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	
    	logger.debug("dispathcing addNewQuestion");
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=qaAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);

		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= QaUtils.buildGeneralAuthoringDTO(request, qaService, qaContent, qaAuthoringForm);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		
		qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
        AuthoringUtil authoringUtil= new AuthoringUtil();
        
        Map mapQuestionContent=(Map)sessionMap.get(MAP_QUESTION_CONTENT_KEY);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
        
		String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);

        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
  		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
  		
        sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

		String defaultQuestionContent=request.getParameter("questionContent0");
		logger.debug("defaultQuestionContent: " + defaultQuestionContent);
        qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);
        
        
        String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
        logger.debug("onlineInstructions: " + onlineInstructions);
	    qaGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

        String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
        logger.debug("offlineInstructions: " + offlineInstructions);
	    qaGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

        qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
        
        authoringUtil.reconstructQuestionContentMapForAdd(mapQuestionContent, qaGeneralAuthoringDTO, request);
        
        sessionMap.put(MAP_QUESTION_CONTENT_KEY, mapQuestionContent);
        request.getSession().setAttribute(httpSessionID, sessionMap);
        
        qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
		
		QaUtils.setFormProperties(request, qaService, qaContent, 
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("1");
		

        List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);

	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    qaGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    qaGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
	    
		logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    
    /**
     * removes a question from the questions map
     * ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	logger.debug("dispatching removeQuestion");
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
    	
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

		String httpSessionID=qaAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
        
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
		
        sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);


		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);

		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= QaUtils.buildGeneralAuthoringDTO(request, qaService, qaContent, qaAuthoringForm);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		
		String defaultQuestionContent=request.getParameter("questionContent0");
		logger.debug("defaultQuestionContent: " + defaultQuestionContent);
        qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);

        qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
  		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

        
        String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
        logger.debug("onlineInstructions: " + onlineInstructions);
	    qaGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

        String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
        logger.debug("offlineInstructions: " + offlineInstructions);
	    qaGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
        
		AuthoringUtil authoringUtil= new AuthoringUtil();
        
        Map mapQuestionContent=(Map)sessionMap.get(MAP_QUESTION_CONTENT_KEY);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
    	
        qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
        mapQuestionContent=authoringUtil.reconstructQuestionContentMapForRemove(mapQuestionContent, request, qaAuthoringForm, activeModule);
		logger.debug("final mapQuestionContent: " + mapQuestionContent);
        
        sessionMap.put(MAP_QUESTION_CONTENT_KEY, mapQuestionContent);
        request.getSession().setAttribute(httpSessionID, sessionMap);
        qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);

		QaUtils.setFormProperties(request, qaService, qaContent, 
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("1");

        List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    qaGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    qaGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
		
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
        
        return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    /**
     * adds a new file to content repository
     * ActionForward addNewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
        
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addNewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
        logger.debug("dispathching addNewFile");
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
        
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

		String httpSessionID=qaAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
		
		String onlineInstructions=request.getParameter(ONLINE_INSTRUCTIONS);
		logger.debug("onlineInstructions: " + onlineInstructions);
		
		String offlineInstructions=request.getParameter(OFFLINE_INSTRUCTIONS);
		logger.debug("offlineInstructions: " + offlineInstructions);
		
		sessionMap.put(ONLINE_INSTRUCTIONS_KEY, onlineInstructions);
		sessionMap.put(OFFLINE_INSTRUCTIONS, offlineInstructions);


		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
		
		
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= QaUtils.buildGeneralAuthoringDTO(request, qaService, qaContent, qaAuthoringForm);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		
	    qaGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	    qaGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);


        Map mapQuestionContent=(Map)sessionMap.get(MAP_QUESTION_CONTENT_KEY);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
        qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);

		String defaultQuestionContent=request.getParameter("questionContent0");
		logger.debug("defaultQuestionContent: " + defaultQuestionContent);
        qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);

		qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
        String richTextTitle = (String)sessionMap.get(ACTIVITY_TITLE_KEY);
	 	String richTextInstructions = (String)sessionMap.get(ACTIVITY_INSTRUCTIONS_KEY);
        
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
  		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);


	    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
	    
        addFileToContentRepository(request, qaAuthoringForm, attachmentList, deletedAttachmentList, sessionMap, qaGeneralAuthoringDTO);
        logger.debug("post addFileToContentRepository, attachmentList: " + attachmentList);
        logger.debug("post addFileToContentRepository, deletedAttachmentList: " + deletedAttachmentList);

        sessionMap.put(ATTACHMENT_LIST_KEY,attachmentList);
        sessionMap.put(DELETED_ATTACHMENT_LIST_KEY,deletedAttachmentList);
        
	    qaGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);

		QaUtils.setFormProperties(request, qaService, qaContent, 
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, 
	             activeModule, sessionMap, httpSessionID);

		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
		

		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

        qaAuthoringForm.resetUserAction();
		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    
    /**
     * deletes a file from the content repository
     * ActionForward deleteFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
        logger.debug("dispatching deleteFile");
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
        
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

		String httpSessionID=qaAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
		
		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
		

		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= QaUtils.buildGeneralAuthoringDTO(request, qaService, qaContent, qaAuthoringForm);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);

		
		qaGeneralAuthoringDTO.setSbmtSuccess( new Integer(0).toString());
        Map mapQuestionContent=(Map)sessionMap.get(MAP_QUESTION_CONTENT_KEY);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
        qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
        
        if ((mapQuestionContent != null) && (mapQuestionContent.size() > 0))
        {
            String defaultQuestionContent = (String)mapQuestionContent.get("1");
            logger.debug("defaultQuestionContent: " + defaultQuestionContent);
            qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);
        }
        
		QaUtils.setFormProperties(request, qaService, qaContent, 
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

        
		String onlineInstructions=(String) sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
		logger.debug("onlineInstructions: " + onlineInstructions);

		String offlineInstructions=(String) sessionMap.get(OFFLINE_INSTRUCTIONS);
		logger.debug("offlineInstructions: " + offlineInstructions);

	    qaGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	    qaGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

		
        String richTextTitle = (String)sessionMap.get(ACTIVITY_TITLE_KEY);
	 	String richTextInstructions = (String)sessionMap.get(ACTIVITY_INSTRUCTIONS_KEY);
        
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
  		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
		
        long uuid = WebUtil.readLongParam(request, UUID);

	    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);
	    
	    if(attachmentList == null)
	        attachmentList = new ArrayList();


	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    if(deletedAttachmentList == null)
            deletedAttachmentList = new ArrayList();
        
        /* move the file's details from the attachment collection to the deleted attachments collection
         the attachment will be delete on saving. */
	    
        deletedAttachmentList = QaUtils.moveToDelete(Long.toString(uuid), attachmentList, deletedAttachmentList );

        
        sessionMap.put(ATTACHMENT_LIST_KEY,attachmentList);
        sessionMap.put(DELETED_ATTACHMENT_LIST_KEY,deletedAttachmentList);
        
	    qaGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);


		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
		

		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

        qaAuthoringForm.resetUserAction();
		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
        
        return (mapping.findForward(LOAD_QUESTIONS));
    }
   

    /**
     * performs error validation on form submit
     * 
     * ActionMessages validateSubmit(HttpServletRequest request, ActionMessages errors, QaAuthoringForm qaAuthoringForm)
     * @param request
     * @param errors
     * @param qaAuthoringForm
     * @return ActionMessages
     */
    protected ActionMessages validateSubmit(HttpServletRequest request, ActionMessages errors, QaAuthoringForm qaAuthoringForm)
            
    {
    	//qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
    	
		String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        if ((richTextTitle == null) || (richTextTitle.trim().length() == 0) || richTextTitle.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            errors.add(Globals.ERROR_KEY,new ActionMessage("error.title"));
            logger.debug("add error.title to ActionMessages");
        }

        if ((richTextInstructions == null) || (richTextInstructions.trim().length() == 0) || richTextInstructions.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.instructions"));
            logger.debug("add error.instructions to ActionMessages: ");
        }

        /*
         * enforce that the first (default) question entry is not empty
         */
        String defaultQuestionEntry =request.getParameter("questionContent0");
        if ((defaultQuestionEntry == null) || (defaultQuestionEntry.length() == 0))
        {
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.defaultquestion.empty"));
            logger.debug("add error.defaultquestion.empty to ActionMessages: ");
        }
        
        
        // check this again in Monitoring
        Boolean renderMonitoringEditActivity=(Boolean)request.getSession().getAttribute(RENDER_MONITORING_EDITACTIVITY);
        if ((renderMonitoringEditActivity != null) && (!renderMonitoringEditActivity.booleanValue()))
        {

            if ((qaAuthoringForm.getReportTitle() == null) || (qaAuthoringForm.getReportTitle().length() == 0))
            {
                errors.add(Globals.ERROR_KEY, new ActionMessage("error.reportTitle"));
                logger.debug("add reportTitle to ActionMessages: ");
            }
            
            if ((qaAuthoringForm.getMonitoringReportTitle() == null) || (qaAuthoringForm.getMonitoringReportTitle().length() == 0))
            {
                errors.add(Globals.ERROR_KEY, new ActionMessage("error.monitorReportTitle"));
                logger.debug("add monitorReportTitle to ActionMessages: ");
            }
        }
        
        saveErrors(request,errors);
        return errors;
    }
    
    /**
     * persists error messages to request scope
     * 
     * persistError(HttpServletRequest request, String message)
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message)
    {
        ActionMessages errors= new ActionMessages();
        errors.add(Globals.ERROR_KEY, new ActionMessage(message));
        logger.debug("add " + message +"  to ActionMessages:");
        saveErrors(request,errors);             
    } 
    
    
    /**
     * addFileToContentRepository(HttpServletRequest request, QaAuthoringForm qaAuthoringForm)
     * @param request
     * @param qaAuthoringForm
     */
    public void addFileToContentRepository(HttpServletRequest request, QaAuthoringForm qaAuthoringForm, 
            List attachmentList, List deletedAttachmentList, SessionMap sessionMap, 
            QaGeneralAuthoringDTO qaGeneralAuthoringDTO)
    {
        logger.debug("attempt addFileToContentRepository");
        logger.debug("attachmentList: " + attachmentList);
        logger.debug("deletedAttachmentList: " + deletedAttachmentList);
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        logger.debug("qaService: " + qaService);
        
        if(attachmentList == null)
            attachmentList = new ArrayList();
        
        if(deletedAttachmentList == null)
            deletedAttachmentList = new ArrayList();
        
        FormFile uploadedFile = null;
        boolean isOnlineFile = false;
        String fileType = null;
        if(qaAuthoringForm.getTheOfflineFile() != null && qaAuthoringForm.getTheOfflineFile().getFileSize() > 0 ){
            logger.debug("theOfflineFile is available: ");
            uploadedFile = qaAuthoringForm.getTheOfflineFile();
            logger.debug("uploadedFile: " + uploadedFile);
            fileType = IToolContentHandler.TYPE_OFFLINE;
        }
        else if(qaAuthoringForm.getTheOnlineFile() != null && qaAuthoringForm.getTheOnlineFile().getFileSize() > 0 ){
            logger.debug("theOnlineFile is available: ");
            uploadedFile = qaAuthoringForm.getTheOnlineFile();
            logger.debug("uploadedFile: " + uploadedFile);
            isOnlineFile = true;
            fileType = IToolContentHandler.TYPE_ONLINE;
        }
        else
            /*no file uploaded*/
            return;
        
        logger.debug("uploadedFile.getFileName(): " + uploadedFile.getFileName());
        
        /* if a file with the same name already exists then move the old one to deleted */
        deletedAttachmentList = QaUtils.moveToDelete(uploadedFile.getFileName(), isOnlineFile, attachmentList, deletedAttachmentList );
        logger.debug("deletedAttachmentList: " + deletedAttachmentList);

        try
        {
            /* This is a new file and so is saved to the content repository. Add it to the 
             attachments collection, but don't add it to the tool's tables yet.
             */
            NodeKey node = getToolContentHandler().uploadFile(uploadedFile.getInputStream(), uploadedFile.getFileName(), 
                    uploadedFile.getContentType(), fileType); 
            QaUploadedFile file = new QaUploadedFile();
            String fileName=uploadedFile.getFileName();
            logger.debug("fileName: " + fileName);
            logger.debug("fileName length: " + fileName.length());
            
            if ((fileName != null) && (fileName.length() > 30))
            {
                fileName=fileName.substring(0, 31);
                logger.debug("shortened fileName: " + fileName);
            }
            	
            file.setFileName(fileName);
            file.setFileOnline(isOnlineFile);
            file.setUuid(node.getUuid().toString());
            /* file.setVersionId(node.getVersion()); */ 
            
            /* add the files to the attachment collection - if one existed, it should have already been removed. */
            attachmentList.add(file);
            
            /* reset the fields so that more files can be uploaded */
            qaAuthoringForm.setTheOfflineFile(null);
            qaAuthoringForm.setTheOnlineFile(null);
        }
        catch (FileNotFoundException e) {
            logger.error("Unable to uploadfile",e);
            throw new RuntimeException("Unable to upload file, exception was "+e.getMessage());
        } catch (IOException e) {
            logger.error("Unable to uploadfile",e);
            throw new RuntimeException("Unable to upload file, exception was "+e.getMessage());
        } catch (RepositoryCheckedException e) {
            logger.error("Unable to uploadfile",e);
            throw new RuntimeException("Unable to upload file, exception was "+e.getMessage());
        }
    }
        
    
    /**
     * QaToolContentHandler getToolContentHandler()
     * @return
     */
    private QaToolContentHandler getToolContentHandler()
    {
        if ( toolContentHandler == null ) {
              WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
              toolContentHandler = (QaToolContentHandler) wac.getBean("qaToolContentHandler");
            }
            return toolContentHandler;
    }

    /**
     * 
     * Go through the attachments collections. Remove any content repository or tool objects
     * matching entries in the the deletedAttachments collection, add any new attachments in the
     * attachments collection. Clear the deletedAttachments collection, ready for new editing.
     * 
     * @param qaContent
     * @param attachmentList
     * @param deletedAttachmentList
     * @param mapping
     * @param request
     * @return
     */
    private List saveAttachments (QaContent qaContent, 
            List attachmentList, List deletedAttachmentList,
            ActionMapping mapping, HttpServletRequest request) {

    	logger.debug("start saveAttachments, attachmentList " + attachmentList);
    	logger.debug("start deletedAttachmentList, deletedAttachmentList " + deletedAttachmentList);
    	
        if(attachmentList==null || deletedAttachmentList==null)
            return null;
        
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        logger.debug("qaService: " + qaService);
        
        if ( deletedAttachmentList != null ) {
        	logger.debug("deletedAttachmentList is iterated...");
            Iterator iter = deletedAttachmentList.iterator();
            while (iter.hasNext()) {
                QaUploadedFile attachment = (QaUploadedFile) iter.next();
                logger.debug("attachment: " + attachment);
                
                /*remove entry from content repository. deleting a non-existent entry 
                 shouldn't cause any errors.*/
                try 
				{
                	
                    if(attachment.getUuid()!= null)
                    {
                    	getToolContentHandler().deleteFile(Long.getLong(attachment.getUuid()));
                        logger.error("deleted file with uuid: " + attachment.getUuid());
                    }
                }
                catch (RepositoryCheckedException e) {
                    logger.error("Unable to delete file",e);
                    ActionMessages am = new ActionMessages(); 
                    am.add( ActionMessages.GLOBAL_MESSAGE,  
                           new ActionMessage( "error.contentrepository" , 
                                              attachment.getFileName())); 
                    saveErrors( request, am ); 
                }

                if ( attachment.getSubmissionId() != null ) {
                    qaService.removeFile(attachment.getSubmissionId());
                }
            }
            deletedAttachmentList.clear();
            logger.error("cleared attachment list.");
        }
        
        if ( attachmentList != null ) 
        {
        	logger.debug("attachmentList is iterated...");
            Iterator iter = attachmentList.iterator();
            while (iter.hasNext()) {
                QaUploadedFile attachment = (QaUploadedFile) iter.next();
            	logger.debug("attachment: " + attachment);
            	logger.debug("attachment submission id: " + attachment.getSubmissionId());

                if ( attachment.getSubmissionId() == null ) {
                    /* add entry to tool table - file already in content repository */
                	logger.debug("calling persistFile with  attachment: " + attachment);
                    qaService.persistFile(qaContent, attachment);
                }
            }
        }
            
        return deletedAttachmentList;
    }
    
    
    public ActionForward editActivity(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching proxy editActivity...");
    	return null;
	}

    
    /**
     * calls monitoring action summary screen generation
     * ActionForward getSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching proxy getSummary...start with monitoringStarter" + request);
    	QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
    	return qaMonitoringAction.getSummary(mapping, form, request, response);
	}

    
    /**
     * calls monitoring action instructions screen generation
     * ActionForward getInstructions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getInstructions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching proxy getInstructions..." + request);
    	QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
    	return qaMonitoringAction.getInstructions(mapping, form, request, response);
	}

    /**
     * calls monitoring action stats screen generation
     * 
     * ActionForward getStats(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getStats(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching proxy getStats..." + request);
    	QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
    	return qaMonitoringAction.getStats(mapping, form, request, response);
	}

    
    /**
     * generates Edit Activity screen
     * ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");
    	
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
		logger.debug("qaAuthoringForm: " + qaAuthoringForm);
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);

		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= QaUtils.buildGeneralAuthoringDTO(request, qaService, qaContent, qaAuthoringForm);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		Map mapQuestionContent=qaAuthoringForm.getMapQuestionContent();
		logger.debug("mapQuestionContent: " + mapQuestionContent);
		
        if ((mapQuestionContent != null) && (mapQuestionContent.size() > 0))
        {
            String defaultQuestionContent = (String)mapQuestionContent.get("1");
            logger.debug("defaultQuestionContent: " + defaultQuestionContent);
            qaGeneralAuthoringDTO.setDefaultQuestionContent(defaultQuestionContent);
        }
		
		
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);

		qaAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
     	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
     	qaGeneralAuthoringDTO.setShowAuthoringTabs(new Boolean(false).toString());
     	
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		qaGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistError(request,"error.content.inUse");
	    	qaGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
     	
		QaUtils.setDefineLater(request, true, strToolContentID, qaService);

		//QaUtils.setFormProperties(request, qaService, qaContent, 
	    //         qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
		

		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		//qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		//qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("1");

		logger.debug("before fwding to jsp, qaAuthoringForm: " + qaAuthoringForm);
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

		logger.debug("forwarding to : " + LOAD_QUESTIONS);
		return mapping.findForward(LOAD_QUESTIONS);
    }

    
	protected boolean existsContent(long toolContentID, IQaService qaService)
	{
		QaContent qaContent=qaService.loadQa(toolContentID);
	    if (qaContent == null) 
	    	return false;
	    
		return true;	
	}
    
}
