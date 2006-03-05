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
import javax.servlet.http.HttpSession;

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
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaToolContentHandler;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * the tool's web.xml will be modified to have classpath to learning service.
 * This is how the tool gets the definition of "learnerService"
 */

/**
 * 
 * when to reset define later and synchin monitor etc..
 *  
 */

/** make sure the tool gets called on:
 *  setAsForceComplete(Long userId) throws QaApplicationException 
 */


/**
 * 
 * User Issue:
 * Right now:
 * 1- the tool gets the request object from the container.
 * 2- Principal principal = req.getUserPrincipal();
 * 3- String username = principal.getName();
 * 4- User userCompleteData = qaService.getCurrentUserData(userName);
 * 5- write back userCompleteData.getUserId()
 */


/**
 * 
 * JBoss Issue: 
 * Currently getUserPrincipal() returns null and ServletRequest.isUserInRole() always returns false on unsecured pages, 
 * even after the user has been authenticated.
 * http://jira.jboss.com/jira/browse/JBWEB-19 
 */


/**
 * eliminate calls:
 * authoringUtil.simulatePropertyInspector_RunOffline(request);
 * authoringUtil.simulatePropertyInspector_setAsDefineLater(request);
 */


/**
 * 
 * TOOL PARAMETERS: ?? (toolAccessMode) ??
 * Authoring environment: toolContentId
 * Learning environment: toolSessionId + toolContentId  
 * Monitoring environment: toolContentId / Contribute tab:toolSessionId(s)
 *   
 * 
 */

/**
 * Note: the tool must support deletion of an existing content from within the authoring environment.
 * The current support for this is by implementing the tool contract : removeToolContent(Long toolContentId)
 */


/**
 * 
 * We have had to simulate container bahaviour in development stage by calling 
 * createToolSession and leaveToolSession from the web layer. These will go once the tool is 
 * in deployment environment.
 * 
 * 
 * CHECK: leaveToolSession and relavent LearnerService may need to be defined in the spring config file.
 * 
 */


/**
 * 
 * GROUPING SUPPORT: Find out what to do.
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
 */
public class QAction extends LamsDispatchAction implements QaAppConstants
{
    static Logger logger = Logger.getLogger(QAction.class.getName());
    
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
        QaContent qaContent = authoringUtil.saveOrUpdateQaContent(mapQuestionContent, qaService, qaAuthoringForm);
        logger.debug("after saveOrUpdateQaContent.");

        saveAttachments(qaContent, attachmentList, deletedAttachmentList, mapping, request);
  
        errors.clear();
        errors.add(Globals.ERROR_KEY, new ActionMessage("submit.successful"));
        request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(1));
        logger.debug("setting SUBMIT_SUCCESS to 1.");
        
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
    	//QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
	    //return qaMonitoringAction.editActivity(mapping, form, request, response);
    	return null;
	}

    
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
		QaUtils.setDefineLater(request, true, toolContentId);
		
		logger.debug("forwarding to : " + LOAD_QUESTIONS);
		return mapping.findForward(LOAD_QUESTIONS);
    }

    
    public ActionForward addNewQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        AuthoringUtil authoringUtil= new AuthoringUtil();
        Map mapQuestionContent=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT);
        
        request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));  //FIXME: ??
        authoringUtil.reconstructQuestionContentMapForAdd(mapQuestionContent, request);
        
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
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

    public ActionForward addNewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
        
        addFileToContentRepository(request, qaAuthoringForm);
        qaAuthoringForm.resetUserAction();
        // request.getSession().setAttribute(CHOICE,CHOICE_TYPE_INSTRUCTIONS); 
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
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
     * perform error validation on form submit
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
     * This method manages the presentation Map for the learner mode.
     * The dispatch method to decide which view should be shown to the user.
     * The deicision is based on tool access mode and tool session status.
     * The tool access mode is lams concept and should comes from progress
     * engine, whereas tool session status is tool's own session state
     * concept and should be loaded from database and setup by
     * <code>loadQuestionnaire</code>.
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
     * @throws ToolException 
     */
    public ActionForward displayQ(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException,
                                                                        ServletException, ToolException
    {
        logger.debug("dispatching displayQ...");
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        /*
         * if the content is not ready yet, don't even proceed.
         * check the define later status
         */
        Boolean defineLater=(Boolean)request.getSession().getAttribute(IS_DEFINE_LATER);
        logger.debug("learning-defineLater: " + defineLater);
        if (defineLater.booleanValue() == true)
        {
            persistError(request,"error.defineLater");
            return (mapping.findForward(ERROR_LIST_LEARNER));
        }
        
        LearningUtil learningUtil= new LearningUtil();
        QaLearningForm qaLearningForm = (QaLearningForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        
        /*retrieve the default question content map*/ 
        Map mapQuestions=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER);
        logger.debug("MAP_QUESTION_CONTENT_LEARNER:" + request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER));
        
        Map mapAnswers=(Map)request.getSession().getAttribute(MAP_ANSWERS);
        logger.debug("MAP_ANSWERS:" + mapAnswers);

        /*obtain author's question listing preference*/
        String questionListingMode=(String) request.getSession().getAttribute(QUESTION_LISTING_MODE);
        /* maintain Map either based on sequential listing or based on combined listing*/
        if (questionListingMode.equalsIgnoreCase(QUESTION_LISTING_MODE_SEQUENTIAL))
        {
            logger.debug("QUESTION_LISTING_MODE_SEQUENTIAL");
            
            int currentQuestionIndex=new Long(qaLearningForm.getCurrentQuestionIndex()).intValue();
            logger.debug("currentQuestionIndex is: " + currentQuestionIndex);
            logger.debug("getting answer for question: " + currentQuestionIndex + "as: " +  qaLearningForm.getAnswer());
            logger.debug("mapAnswers size:" + mapAnswers.size());
            
            if  (mapAnswers.size() >= currentQuestionIndex)
            {
                logger.debug("mapAnswers size:" + mapAnswers.size() + " and currentQuestionIndex: " + currentQuestionIndex);
                mapAnswers.remove(new Long(currentQuestionIndex).toString());
            }
            logger.debug("before adding to mapAnswers: " + mapAnswers);
            mapAnswers.put(new Long(currentQuestionIndex).toString(), qaLearningForm.getAnswer());
            logger.debug("adding new answer:" + qaLearningForm.getAnswer() + " to mapAnswers.");
            
            if (qaLearningForm.getGetNextQuestion() != null)
                currentQuestionIndex++; 
            else if (qaLearningForm.getGetPreviousQuestion() != null)
                currentQuestionIndex--;
            
            request.getSession().setAttribute(CURRENT_ANSWER, mapAnswers.get(new Long(currentQuestionIndex).toString()));
            logger.debug("currentQuestionIndex will be: " + currentQuestionIndex);
            request.getSession().setAttribute(CURRENT_QUESTION_INDEX, new Long(currentQuestionIndex));
            learningUtil.feedBackAnswersProgress(request,currentQuestionIndex);
            qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */ 
        }
        else
        {
            logger.debug(logger + " " + this.getClass().getName() +  "QUESTION_LISTING_MODE_COMBINED");
            for (int questionIndex=INITIAL_QUESTION_COUNT.intValue(); questionIndex<= mapQuestions.size(); questionIndex++ )
            {
                String answer=request.getParameter("answer" + questionIndex);
                logger.debug("answer for question " + questionIndex + " is:" + answer);
                mapAnswers.put(new Long(questionIndex).toString(), answer);
            }
        }
        logger.debug("continue processing answers...");
        /*
         *  At this point the Map holding learner responses is ready. So place that into the session.
         */
        request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
        
        /*
         * Learner submits the responses to the questions. 
         */
        if (qaLearningForm.getSubmitAnswersContent() != null)
        {
            logger.debug(logger + " " + this.getClass().getName() +  "submit the responses: " + mapAnswers);
            /*recreate the users and responses*/
            learningUtil.createUsersAndResponses(mapAnswers, request, qaService);
            qaLearningForm.resetUserActions();
            qaLearningForm.setSubmitAnswersContent(null);
            Long toolContentId=(Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
            learningUtil.lockContent(toolContentId.longValue(), qaService);
            logger.debug("content has been locked");
            return (mapping.findForward(LEARNER_REPORT));
        }
        /*
         * Simulate learner leaving the current tool session. This will normally gets called by the container by 
         * leaveToolSession(toolSessionId, user) 
         */
        else if (qaLearningForm.getEndLearning() != null) 
        {
                /*
                 * The learner is done with the tool session. The tool needs to clean-up.
                 */
        		logger.debug("end learning...");
                Long toolSessionId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
                HttpSession ss = SessionManager.getSession();
                /*get back login user DTO*/
                UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
                logger.debug("simulating container behaviour by calling  " +
                             "leaveToolSession() with toolSessionId: " +  toolSessionId + " and user: " + user);
                
                String nextActivityUrl = qaService.leaveToolSession(toolSessionId, new Long(user.getUserID().longValue()));
                response.sendRedirect(nextActivityUrl);
                return null;
            }
            
        logger.debug("forwarding to: " + LOAD_LEARNER);
        qaLearningForm.resetUserActions();
        return (mapping.findForward(LOAD_LEARNER));
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
            //no file uploaded
            return;
        
        logger.debug("uploadedFile.getFileName(): " + uploadedFile.getFileName());
        
        // if a file with the same name already exists then move the old one to deleted
        deletedAttachmentList = QaUtils.moveToDelete(uploadedFile.getFileName(), isOnlineFile, attachmentList, deletedAttachmentList );

        
        try
        {
            // This is a new file and so is saved to the content repository. Add it to the 
            // attachments collection, but don't add it to the tool's tables yet.
            NodeKey node = getToolContentHandler().uploadFile(uploadedFile.getInputStream(), uploadedFile.getFileName(), 
                    uploadedFile.getContentType(), fileType); 
            QaUploadedFile file = new QaUploadedFile();
            file.setFileName(uploadedFile.getFileName());
            file.setFileOnline(isOnlineFile);
            //file.setQaContent(qaContent);
            file.setUuid(node.getUuid().toString());
            //file.setVersionId(node.getVersion()); 
            
            // add the files to the attachment collection - if one existed, it should have already been removed.
            attachmentList.add(file);
            
            QaUtils.addUploadsToSession(request, attachmentList, deletedAttachmentList);
            //reset the fields so that more files can be uploaded
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
        
    
    private QaToolContentHandler getToolContentHandler()
    {
        if ( toolContentHandler == null ) {
              WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
//            toolContentHandler = (QaToolContentHandler) wac.getBean(QaToolContentHandler.SPRING_BEAN_NAME);
              toolContentHandler = (QaToolContentHandler) wac.getBean("qaToolContentHandler");
            }
            return toolContentHandler;
    }

    /** 
    * Go through the attachments collections. Remove any content repository or tool objects
    * matching entries in the the deletedAttachments collection, add any new attachments in the
    * attachments collection. Clear the deletedAttachments collection, ready for new editing.
    */ 
    private List saveAttachments (QaContent qaContent, 
            List attachmentList, List deletedAttachmentList,
            ActionMapping mapping, HttpServletRequest request) {

        if(attachmentList==null || deletedAttachmentList==null)
            return null;
        
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        
        if ( deletedAttachmentList != null ) {
            Iterator iter = deletedAttachmentList.iterator();
            while (iter.hasNext()) {
                QaUploadedFile attachment = (QaUploadedFile) iter.next();
                
                // remove entry from content repository. deleting a non-existent entry 
                // shouldn't cause any errors.
                try {
                    if(attachment.getUuid()!= null)
                        getToolContentHandler().deleteFile(Long.getLong(attachment.getUuid()));
                }
                catch (RepositoryCheckedException e) {
                    logger.error("Unable to delete file",e);
                    ActionMessages am = new ActionMessages(); 
                    am.add( ActionMessages.GLOBAL_MESSAGE,  
                           new ActionMessage( "error.contentrepository" , 
                                              attachment.getFileName())); 
                    saveErrors( request, am ); 
                }

                // remove tool entry from db
                if ( attachment.getSubmissionId() != null ) {
                    qaService.removeFile(attachment.getSubmissionId());
                }
            }
            deletedAttachmentList.clear();
        }
        
        if ( attachmentList != null ) {
            Iterator iter = attachmentList.iterator();
            while (iter.hasNext()) {
                QaUploadedFile attachment = (QaUploadedFile) iter.next();

                if ( attachment.getSubmissionId() == null ) {
                    // add entry to tool table - file already in content repository
                    //FIXME: qaService.saveAttachment(nbContent, attachment);
                    qaService.persistFile(qaContent, attachment);
                }
            }
        }
            
        return deletedAttachmentList;
    }
}
