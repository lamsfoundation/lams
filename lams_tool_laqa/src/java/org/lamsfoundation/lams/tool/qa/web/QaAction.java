/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.tool.qa.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaToolContentHandler;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
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
 * 	<action
      path="/authoring"
      type="org.lamsfoundation.lams.tool.qa.web.QaAction"
      name="QaAuthoringForm"
      scope="session"
      input="/AuthoringMaincontent.jsp"
      parameter="dispatch"
      unknown="false"
      validate="true"
    >
    
		<exception
			key="error.exception.QaApplication"
			type="org.lamsfoundation.lams.tool.qa.QaApplicationException"
			handler="org.lamsfoundation.lams.tool.qa.web.CustomStrutsExceptionHandler"
			path="/SystemErrorContent.jsp"
			scope="request"
		/>
		    
		<exception
		    key="error.exception.QaApplication"
		    type="java.lang.NullPointerException"
		    handler="org.lamsfoundation.lams.tool.qa.web.CustomStrutsExceptionHandler"
		    path="/SystemErrorContent.jsp"
		    scope="request"
		/>	         			
	    
	  	<forward
	        name="load"
	        path="/AuthoringMaincontent.jsp"
	        redirect="true"
		/>
	
	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="true"
	  	/>
	  
	    <forward
			name="loadViewOnly"
			path="/authoring/AuthoringTabsHolder.jsp"
			redirect="false"
	    />
	  
	  	<forward
	        name="starter"
	        path="/index.jsp"
	        redirect="true"
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
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    /**
     * submits content
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
    	
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        AuthoringUtil authoringUtil= new AuthoringUtil();
        Map mapQuestionContent=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT);
        logger.debug("mapQuestionContent :" +mapQuestionContent);
        
        if (mapQuestionContent == null)
        	mapQuestionContent= new TreeMap(new QaComparator());
        logger.debug("mapQuestionContent :" +mapQuestionContent);
        
        ActionMessages errors= new ActionMessages();
        /* full form validation should be performed only in standard authoring mode, but not in monitoring EditActivity */
        errors=validateSubmit(request, errors, qaAuthoringForm);

        if (errors.size() > 0)  
        {
            logger.debug("returning back to from to fix errors:");
            request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));
            return (mapping.findForward(LOAD_QUESTIONS));
        }
        
        List attachmentList = (List) request.getSession().getAttribute(ATTACHMENT_LIST);
        List deletedAttachmentList = (List) request.getSession().getAttribute(DELETED_ATTACHMENT_LIST);

        authoringUtil.reconstructQuestionContentMapForSubmit(mapQuestionContent, request);
        logger.debug("before saveOrUpdateQaContent.");
        
        boolean verifyDuplicatesOptionsMap=AuthoringUtil.verifyDuplicatesOptionsMap(mapQuestionContent);
	 	logger.debug("verifyDuplicatesOptionsMap: " + verifyDuplicatesOptionsMap);
	 	request.getSession().removeAttribute(USER_EXCEPTION_QUESTIONS_DUPLICATE);
	 	if (verifyDuplicatesOptionsMap == false)
 		{
 			errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("error.questions.duplicate"));
			request.getSession().setAttribute(USER_EXCEPTION_QUESTIONS_DUPLICATE, new Boolean(true).toString());
			logger.debug("add error.questions.duplicate to ActionMessages");
			saveErrors(request,errors);
			qaAuthoringForm.resetUserAction();
            return (mapping.findForward(LOAD_QUESTIONS));
 		}
        
        
        /*to remove deleted entries in the questions table based on mapQuestionContent */
        authoringUtil.removeRedundantQuestions(mapQuestionContent, qaService, qaAuthoringForm, request);
        logger.debug("end of removing unused entries... ");
        
        QaContent qaContent=authoringUtil.saveOrUpdateQaContent(mapQuestionContent, qaService, qaAuthoringForm, request);
        logger.debug("qaContent: " + qaContent);
        
        authoringUtil.reOrganizeDisplayOrder(mapQuestionContent, qaService, qaAuthoringForm, qaContent);

        List attacments=saveAttachments(qaContent, attachmentList, deletedAttachmentList, mapping, request);
        logger.debug("attacments: " + attacments);

  
        errors.clear();
        errors.add(Globals.ERROR_KEY, new ActionMessage("submit.successful"));
        request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(1));
        logger.debug("setting SUBMIT_SUCCESS to 1.");
        
        Long strToolContentId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
	    logger.debug("strToolContentId: " + strToolContentId);
        QaUtils.setDefineLater(request, false, strToolContentId.toString());
        
        saveErrors(request,errors);
        
        qaAuthoringForm.resetUserAction();
        return mapping.findForward(LOAD_QUESTIONS);
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
    	
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);

     	request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true));
     	request.getSession().setAttribute(SHOW_AUTHORING_TABS,new Boolean(false).toString());
     	     	
     	String toolContentId=qaAuthoringForm.getToolContentId();
     	logger.debug("toolContentId: " + toolContentId);
     	if ((toolContentId== null) || toolContentId.equals(""))
     	{
     		logger.debug("getting toolContentId from session.");
     		Long longToolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
     		toolContentId=longToolContentId.toString();
     		logger.debug("toolContentId: " + toolContentId);
     	}
    	
     	
		QaUtils.setDefineLater(request, true, toolContentId);
		
		logger.debug("forwarding to : " + LOAD_QUESTIONS);
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
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        AuthoringUtil authoringUtil= new AuthoringUtil();
        Map mapQuestionContent=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT);
        
        request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));  //FIXME: ??
        authoringUtil.reconstructQuestionContentMapForAdd(mapQuestionContent, request);
        
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
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        AuthoringUtil authoringUtil= new AuthoringUtil();
        QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
        Map mapQuestionContent=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT);
        
        request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));  //FIXME: ??
        authoringUtil.reconstructQuestionContentMapForRemove(mapQuestionContent, request, qaAuthoringForm);
        
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
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
        
        addFileToContentRepository(request, qaAuthoringForm);
        qaAuthoringForm.resetUserAction();
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
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        long uuid = WebUtil.readLongParam(request, UUID);
        
        /* move the file's details from the attachment collection to the deleted attachments collection
         the attachment will be delete on saving. */
        List attachmentList = (List) request.getSession().getAttribute(ATTACHMENT_LIST);
        List deletedAttachmentList = (List) request.getSession().getAttribute(DELETED_ATTACHMENT_LIST);
        if(deletedAttachmentList == null)
            deletedAttachmentList = new ArrayList();
        
        deletedAttachmentList = QaUtils.moveToDelete(Long.toString(uuid), attachmentList, deletedAttachmentList );

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
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        String title = qaAuthoringForm.getTitle();
        logger.debug("title: " + title);

        String instructions = qaAuthoringForm.getInstructions();
        logger.debug("instructions: " + instructions);
        
        if ((title == null) || (title.trim().length() == 0) || title.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            errors.add(Globals.ERROR_KEY,new ActionMessage("error.title"));
            logger.debug("add title to ActionMessages");
        }

        if ((instructions == null) || (instructions.trim().length() == 0) || instructions.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.instructions"));
            logger.debug("add instructions to ActionMessages: ");
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
    public void addFileToContentRepository(HttpServletRequest request, QaAuthoringForm qaAuthoringForm)
    {
        logger.debug("attempt addFileToContentRepository");
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        logger.debug("qaService: " + qaService);
        
        List attachmentList = (List) request.getSession().getAttribute(ATTACHMENT_LIST);
        List deletedAttachmentList = (List) request.getSession().getAttribute(DELETED_ATTACHMENT_LIST);
        
        if(attachmentList == null)
            attachmentList = new ArrayList();
        
        if(deletedAttachmentList == null)
            deletedAttachmentList = new ArrayList();
        
        FormFile uploadedFile = null;
        boolean isOnlineFile = false;
        String fileType = null;
        if(qaAuthoringForm.getTheOfflineFile() != null && qaAuthoringForm.getTheOfflineFile().getFileSize() > 0 ){
            uploadedFile = qaAuthoringForm.getTheOfflineFile();
            fileType = IToolContentHandler.TYPE_OFFLINE;
        }
        else if(qaAuthoringForm.getTheOnlineFile() != null && qaAuthoringForm.getTheOnlineFile().getFileSize() > 0 ){
            uploadedFile = qaAuthoringForm.getTheOnlineFile();
            isOnlineFile = true;
            fileType = IToolContentHandler.TYPE_ONLINE;
        }
        else
            /*no file uploaded*/
            return;
        
        logger.debug("uploadedFile.getFileName(): " + uploadedFile.getFileName());
        
        /* if a file with the same name already exists then move the old one to deleted */
        deletedAttachmentList = QaUtils.moveToDelete(uploadedFile.getFileName(), isOnlineFile, attachmentList, deletedAttachmentList );

        try
        {
            /* This is a new file and so is saved to the content repository. Add it to the 
             attachments collection, but don't add it to the tool's tables yet.
             */
            NodeKey node = getToolContentHandler().uploadFile(uploadedFile.getInputStream(), uploadedFile.getFileName(), 
                    uploadedFile.getContentType(), fileType); 
            QaUploadedFile file = new QaUploadedFile();
            file.setFileName(uploadedFile.getFileName());
            file.setFileOnline(isOnlineFile);
            file.setUuid(node.getUuid().toString());
            /* file.setVersionId(node.getVersion()); */ 
            
            /* add the files to the attachment collection - if one existed, it should have already been removed. */
            attachmentList.add(file);
            
            QaUtils.addUploadsToSession(request, attachmentList, deletedAttachmentList);
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
}
