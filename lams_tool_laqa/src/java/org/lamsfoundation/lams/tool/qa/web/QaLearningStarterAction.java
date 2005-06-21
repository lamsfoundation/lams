/**
 * Created on 8/03/2005
 * initializes the tool's learning mode
 */

package org.lamsfoundation.lams.tool.qa.web;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.lamsfoundation.lams.usermanagement.User;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;

/**
 * 
 * @author ozgurd
 *
 * Learner mode takes in the parameters TOOL_SESSION_ID and TOOL_CONTENT_ID
 *
 * Make a note to change QaAppConstants.DEVELOPMENT_FLAG to false in deployment.
 *
 * This class is used to load the default content and initialize the presentation Map for Learner mode 
 * 
 * createToolSession will not be called once the tool is deployed.
 * 
 * It is important that ALL the session attributes created in this action gets removed by: QaUtils.cleanupSession(request) 
 * 
 */

/**
 * 
 * verifies that the content id passed to the tool is numeric and does refer to an existing content.
 * 
 */

public class QaLearningStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaLearningStarterAction.class.getName());

	/**
	 * holds the question contents for a given tool session and relevant content
	 */
	protected Map mapQuestions= new TreeMap(new QaComparator());
	/**
	 * holds the answers
	 */  
	protected Map mapAnswers= new TreeMap(new QaComparator());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException {

		QaLearningForm qaQaLearningForm = (QaLearningForm) form;
		
		/**
		 * reset the question index to 1
		 */
		request.getSession().setAttribute(CURRENT_QUESTION_INDEX, "1");
		logger.debug(logger + " " + this.getClass().getName() +  "CURRENT_QUESTION_INDEX: " + request.getSession().getAttribute(CURRENT_QUESTION_INDEX));

		/**
		 * reset the current answer
		 */
		request.getSession().setAttribute(CURRENT_ANSWER, "");

		/**
		 * initialize available question display modes in the session
		 */
		request.getSession().setAttribute(QUESTION_LISTING_MODE_SEQUENTIAL,QUESTION_LISTING_MODE_SEQUENTIAL);
	    request.getSession().setAttribute(QUESTION_LISTING_MODE_COMBINED, QUESTION_LISTING_MODE_COMBINED);
	    
	    /**
		 * retrive the service
		 */
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService: " + qaService);
	    request.getSession().setAttribute(TOOL_SERVICE, qaService);
	    
	    /**
	     * mark the http session as a learning activity 
	     */
	    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_LEARNING);
	    
	    /**
	     * obtain and setup the current user's data 
	     */
	    String userId=request.getParameter(USER_ID);
	    if ((userId == null) || (userId.length()==0))
		{
	    	logger.debug(logger + " " + this.getClass().getName() +  "error: The tool expects userId");
	    	persistError(request,"error.authoringUser.notAvailable");
	    	request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
			return (mapping.findForward(LOAD));
		}
	    
	    try
		{
	    	User user=QaUtils.createUser(new Integer(userId));
	    	request.getSession().setAttribute(TOOL_USER, user);
		}
	    catch(NumberFormatException e)
		{
	    	persistError(request,"error.userId.notNumeric");
			request.setAttribute(USER_EXCEPTION_USERID_NOTNUMERIC, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
			return (mapping.findForward(LOAD));
		}
	    logger.debug(logger + " " + this.getClass().getName() +  "TOOL_USER is:" + request.getSession().getAttribute(TOOL_USER));
	    
	    /**
	     * verify that userID does not already exist in the db
	     */
	    QaQueUsr qaQueUsr=qaService.loadQaQueUsr(new Long(userId));
	    logger.debug(logger + " " + this.getClass().getName() +  "QaQueUsr:" + qaQueUsr);
	    if (qaQueUsr != null)
	    {
	    	persistError(request,"error.userId.existing");
			request.setAttribute(USER_EXCEPTION_USERID_EXISTING, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
			return (mapping.findForward(LOAD));
	    }
	    
		/**
	     * process incoming tool content id
	     */
	    String strToolContentId=request.getParameter(TOOL_CONTENT_ID);
	    long toolContentId=0;
	    if ((strToolContentId == null) || (strToolContentId.length() == 0))
	    {
	    	persistError(request, "error.contentId.required");
	    	request.setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
			return (mapping.findForward(LOAD));
	    }
	    else
	    {
	    	try
			{
	    		toolContentId=new Long(strToolContentId).longValue();
		    	logger.debug(logger + " " + this.getClass().getName() +  "passed TOOL_CONTENT_ID : " + new Long(toolContentId));
		    	request.getSession().setAttribute(TOOL_CONTENT_ID,new Long(toolContentId));	
			}
	       	catch(NumberFormatException e)
			{
	       		persistError(request, "error.contentId.numberFormatException");
				request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
				return (mapping.findForward(LOAD));
			}
	    }
	    
	    if (!QaUtils.existsContent(toolContentId, request))
    	{
       		persistError(request, "error.content.doesNotExist");
    		request.setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
			return (mapping.findForward(LOAD));
    	}
	    
	    QaContent qaContent=qaService.loadQa(toolContentId);
	    /**
	     * process incoming tool session id
	     * A toolSessionId must be passed to the tool from the container.
	     */
	    String strToolSessionId=request.getParameter(TOOL_SESSION_ID);
	    long toolSessionId=0;
	    if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) 
	    {
	    	persistError(request, "error.toolSessionId.required");
	    	request.setAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
			return (mapping.findForward(LOAD));
	    }
	    else
	    {
	    	try
			{
	    		toolSessionId=new Long(strToolSessionId).longValue();
		    	logger.debug(logger + " " + this.getClass().getName() +  "passed TOOL_SESSION_ID : " + new Long(toolSessionId));
		    	request.getSession().setAttribute(TOOL_SESSION_ID,new Long(toolSessionId));	
			}
	    	catch(NumberFormatException e)
			{
	    		persistError(request, "error.sessionId.numberFormatException");
	    		logger.debug(logger + " " + this.getClass().getName() +  "add error.sessionId.numberFormatException to ActionMessages: ");
				request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD);
				return (mapping.findForward(LOAD));
			}
	    }

	    /**
	     * By now, the passed tool session id MUST exist in the db through the calling of:
	     * public void createToolSession(Long toolSessionId, Long toolContentId) by the container.
	     *  
	     * make sure this session exists in tool's session table by now.
	     */
		if (!QaUtils.existsSession(toolSessionId, request)) 
		{
				Long currentToolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
				logger.debug(logger + " " + this.getClass().getName() +  "Simulating container behaviour: calling createToolSession with toolSessionId : " + 
												new Long(toolSessionId) + " and toolContentId: " + currentToolContentId);
				qaService.createToolSession(new Long(toolSessionId), currentToolContentId);
				logger.debug(logger + " " + this.getClass().getName() +  "Simulated container behaviour:"); 
		}
		
		/**
		 * by now, we made sure that the passed tool session id exists in the db as a new record
		 * Make sure we can retrieve it and relavent content
		 */
		
		QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionId);
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaSession: " + qaSession);
	    /**
	     * find out what content this tool session is referring to
	     * get the content for this tool session (many to one mapping)
	     */
	    
	    /**
	     * Each passed tool session id points to a particular content. Many to one mapping.
	     */
		qaContent=qaSession.getQaContent();
	    logger.debug(logger + " " + this.getClass().getName() +  "using qaContent: " + qaContent);
	    
	    /**
	     * The content we retrieved above must have been created before in Authoring time. 
	     * And the passed tool session id already refers to it.
	     */
	    	    
		logger.debug(logger + " " + this.getClass().getName() +  "REPORT_TITLE_LEARNER: " + qaContent.getReportTitle());
	    request.getSession().setAttribute(REPORT_TITLE_LEARNER,qaContent.getReportTitle());
	    
	    request.getSession().setAttribute(END_LEARNING_MESSAGE,qaContent.getEndLearningMessage());
	    logger.debug(logger + " " + this.getClass().getName() +  "END_LEARNING_MESSAGE: " + qaContent.getEndLearningMessage());
	    
	    logger.debug(logger + " " + this.getClass().getName() +  "IS_TOOL_ACTIVITY_OFFLINE: " + qaContent.isRunOffline());
	    request.getSession().setAttribute(IS_TOOL_ACTIVITY_OFFLINE, new Boolean(qaContent.isRunOffline()).toString());
	    
	    logger.debug(logger + " " + this.getClass().getName() +  "IS_USERNAME_VISIBLE: " + qaContent.isUsernameVisible());
	    request.getSession().setAttribute(IS_USERNAME_VISIBLE, new Boolean(qaContent.isUsernameVisible()));
	
	    logger.debug(logger + " " + this.getClass().getName() +  "IS_DEFINE_LATER: " + qaContent.isDefineLater());
	    request.getSession().setAttribute(IS_DEFINE_LATER, new Boolean(qaContent.isDefineLater()));
	    
	    /**
	     * convince jsp: Learning mode requires this setting for jsp to generate the user's report 
	     */
	    request.getSession().setAttribute(CHECK_ALL_SESSIONS_COMPLETED, new Boolean(false));
	    	    
	    logger.debug(logger + " " + this.getClass().getName() +  "IS_QUESTIONS_SEQUENCED: " + qaContent.isQuestionsSequenced());
	    String feedBackType="";
    	if (qaContent.isQuestionsSequenced())
    	{
    		request.getSession().setAttribute(QUESTION_LISTING_MODE, QUESTION_LISTING_MODE_SEQUENTIAL);
    		feedBackType=FEEDBACK_TYPE_SEQUENTIAL;
    	}
	    else
	    {
	    	request.getSession().setAttribute(QUESTION_LISTING_MODE, QUESTION_LISTING_MODE_COMBINED);
    		feedBackType=FEEDBACK_TYPE_COMBINED;
	    }
	    logger.debug(logger + " " + this.getClass().getName() +  "QUESTION_LISTING_MODE: " + request.getSession().getAttribute(QUESTION_LISTING_MODE));
	    
    	/**
    	 * fetch question content from content
    	 */
    	Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		if (qaQueContent != null)
    		{
    			int displayOrder=qaQueContent.getDisplayOrder();
        		if (displayOrder != 0)
        		{
        			/**
    	    		 *  add the question to the questions Map in the displayOrder
    	    		 */
            		mapQuestions.put(new Integer(displayOrder).toString(),qaQueContent.getQuestion());
        		}
    		}
    	}
		
    	request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
    	request.getSession().setAttribute(MAP_QUESTION_CONTENT_LEARNER, mapQuestions);
    	logger.debug(logger + " " + this.getClass().getName() +  "qaContent has : " + mapQuestions.size() + " entries.");
    	
    	request.getSession().setAttribute(TOTAL_QUESTION_COUNT, new Long(mapQuestions.size()).toString());
    	String userFeedback= feedBackType + request.getSession().getAttribute(TOTAL_QUESTION_COUNT) + QUESTIONS;
    	request.getSession().setAttribute(USER_FEEDBACK, userFeedback);
    	
		logger.debug(logger + " " + this.getClass().getName() +  "forwarding to learning screen");
		return (mapping.findForward(LOAD));
  } 
	
	/**
     * persists error messages to request scope
     * @param request
     * @param message
     */
	public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug(logger + " " + this.getClass().getName() +  "add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
