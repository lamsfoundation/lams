/**
 * Created on 8/03/2005
 * initializes the tool's authoring mode  
 */

package org.lamsfoundation.lams.tool.qa.web;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * TODO: change DEVELOPMENT_FLAG to false once the container creates and passes users to the tool
 * Assumption: Session attribute ATTR_USERDATA will be passed to the tool to hold User object 
 * 
 * CONTENT_LOCKED refers to content being in use or not: Any students answered that content?
 * For future CONTENT_LOCKED ->CONTENT_IN_USE 
 * 
 * DEFAULT_QUE_CONTENT_ID is hardcoded for the moment, it will probably go.
 * 
 * We won't need to create a mock user once the usernames are defined properly in the container and passed to the tool
   take off User mockUser=QaUtils.createMockUser();
 * 
 * QaStarterAction loads the default content and initializes the presentation Map
 * Requests can come either from authoring envuironment or from the monitoring environment for Edit Activity screen
 * 
 * */

public class QaStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaStarterAction.class.getName());
	
	/**
	 * A Map  data structure is used to present the UI.
	 * It is fetched by subsequent Action classes to manipulate its content and gets parsed in the presentation layer for display.
	 */
	protected Map mapQuestionContent= new TreeMap(new QaComparator());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException {

		QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
		qaAuthoringForm.resetRadioBoxes();
		logger.debug(logger + " " + this.getClass().getName() +  "reset radioboxes");

		request.getSession().setAttribute(IS_DEFINE_LATER,"false");
		request.getSession().setAttribute(DISABLE_TOOL,"");

		/**
		 * retrive the service
		 */
		IQaService qaService = QaUtils.getToolService(request);
		logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService from session: " + qaService);
		if (qaService == null)
		{
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService from proxy: " + qaService);
		    request.getSession().setAttribute(TOOL_SERVICE, qaService);		
		}
		
		/**
		 * retrieve the default content id based on tool signature
		 */
		long contentId=0;
		try
		{
			logger.debug(logger + " " + this.getClass().getName() +  "attempt retrieving tool with signatute : " + MY_SIGNATURE);
			contentId=qaService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug(logger + " " + this.getClass().getName() +  "retrieved tool default contentId: " + contentId);
			if (contentId == 0)
			{
				logger.debug(logger + " " + this.getClass().getName() +  "default content id has not been setup");
				persistError(request,"error.defaultContent.notSetup");
		    	request.setAttribute(USER_EXCEPTION_DEAFULTCONTENT_NOTSETUP, new Boolean(true));
				return (mapping.findForward(LOAD_QUESTIONS));	
			}
		}
		catch(Exception e)
		{
			logger.debug(logger + " " + this.getClass().getName() +  "error getting the default content id: " + e.getMessage());
			persistError(request,"error.defaultContent.notSetup");
	    	request.setAttribute(USER_EXCEPTION_DEAFULTCONTENT_NOTSETUP, new Boolean(true));
			return (mapping.findForward(LOAD_QUESTIONS));
		}

		/**
		 * retrieve the default question content id based on default content id determined above
		 */
		try
		{
			logger.debug(logger + " " + this.getClass().getName() +  "retrieve the default question content id based on default content id: " + contentId);
			QaQueContent qaQueContent=qaService.getToolDefaultQuestionContent(contentId);
			logger.debug(logger + " " + this.getClass().getName() +  "using QaQueContent: " + qaQueContent);
			if (qaQueContent == null)
			{
				logger.debug(logger + " " + this.getClass().getName() +  "Exception occured: " + " No default question content");
	    		request.setAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE, new Boolean(true));
				persistError(request,"error.defaultQuestionContent.notAvailable");
				return (mapping.findForward(LOAD_QUESTIONS));
			}
			/**
        	 * display a single sample question
        	 */
    		System.out.println(this.getClass().getName() + " set default qa que content to: " + qaQueContent.getQuestion() );
    		request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, qaQueContent.getQuestion());
		}
		catch(Exception e)
		{
			logger.debug(logger + " " + this.getClass().getName() +  "Exception occured: " + " No default question content");
    		request.setAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE, new Boolean(true));
			persistError(request,"error.defaultQuestionContent.notAvailable");
			return (mapping.findForward(LOAD_QUESTIONS));
		}
		
		/**
	     * mark the http session as an authoring activity 
	     */
	    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_AUTHORING);
	    
	    /**
	     * define tab controllers for jsp
	     */
	    request.getSession().setAttribute(CHOICE_TYPE_BASIC,CHOICE_TYPE_BASIC);
	    request.getSession().setAttribute(CHOICE_TYPE_ADVANCED,CHOICE_TYPE_ADVANCED);
	    request.getSession().setAttribute(CHOICE_TYPE_INSTRUCTIONS,CHOICE_TYPE_INSTRUCTIONS);
	
	    request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(false));
	    request.setAttribute(FORM_INDEX, "0");
		
	    /**
	     * obtain and setup the current user's data 
	     */
	    String userId="";
	    User toolUser=(User)request.getSession().getAttribute(TOOL_USER);
	    if (toolUser != null)
	    	userId=toolUser.getUserId().toString();
		else
		{
			userId=request.getParameter(USER_ID);
		    try
			{
		    	User user=QaUtils.createAuthoringUser(new Integer(userId));
		    	request.getSession().setAttribute(TOOL_USER, user);
			}
		    catch(NumberFormatException e)
			{
		    	persistError(request,"error.userId.notNumeric");
				request.setAttribute(USER_EXCEPTION_USERID_NOTNUMERIC, new Boolean(true));
				return (mapping.findForward(LOAD_QUESTIONS));
			}
		}
	    
	    if ((userId == null) || (userId.length()==0))
		{
	    	logger.debug(logger + " " + this.getClass().getName() +  "error: The tool expects userId");
	    	persistError(request,"error.authoringUser.notAvailable");
	    	request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
			return (mapping.findForward(LOAD_QUESTIONS));
		}
	    
	    
	    /**
	     * find out whether the request is coming from monitoring module for EditActivity tab or from authoring environment url
	     */
	    String strToolContentId="";
	    Boolean isMonitoringEditActivityVisited=(Boolean)request.getSession().getAttribute(MONITORING_EDITACTIVITY_VISITED);
	    logger.debug(logger + " " + this.getClass().getName() +  "isMonitoringEditActivityVisited: " + isMonitoringEditActivityVisited);
	    
	    Long monitoredContentId=(Long)request.getSession().getAttribute(MONITORED_CONTENT_ID);
	    logger.debug(logger + " " + this.getClass().getName() +  "MONITORED_CONTENT_ID: " + monitoredContentId);
	    
	    request.getSession().setAttribute(RENDER_MONITORING_EDITACTIVITY,new Boolean(false));
	    
		Boolean startMonitoringSummaryRequest=(Boolean)request.getAttribute(START_MONITORING_SUMMARY_REQUEST);
		if ((startMonitoringSummaryRequest != null) && (startMonitoringSummaryRequest.booleanValue()))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "will render Monitoring Edit Activity screen");
		    if  ((isMonitoringEditActivityVisited != null) && (isMonitoringEditActivityVisited.booleanValue()))
		    {
		    	if (monitoredContentId != null)
		    	{
		    		/**
		    		 * request is from Edit Activity tab in monitoring
		    		 */
		    		strToolContentId=monitoredContentId.toString();
		    		logger.debug(logger + " " + this.getClass().getName() +  "request is from Edit Activity tab in monitoring: " + monitoredContentId);
		    		logger.debug(logger + " " + this.getClass().getName() +  "using MONITORED_CONTENT_ID: " + monitoredContentId);
		    		request.getSession().setAttribute(RENDER_MONITORING_EDITACTIVITY,new Boolean(true));
		    	}
		    }
		}
	    else
	    {
	    	logger.debug(logger + " " + this.getClass().getName() +  "will render authoring screen");
	    	/**
    		 * request is from authoring environment
    		 */
	    	request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(false));
	    	logger.debug(logger + " " + this.getClass().getName() +  "request is from authoring environment: ");
	    	strToolContentId=request.getParameter(TOOL_CONTENT_ID);
	    }
	    logger.debug(logger + " " + this.getClass().getName() +  "usable strToolContentId: " + strToolContentId);

	    /**
	     * Process incoming tool content id
	     * Either exists or not exists in the db yet, a toolContentId must be passed to the tool from the container 
	     */
	    long toolContentId=0;
    	try
		{
	    	toolContentId=new Long(strToolContentId).longValue();
	    	logger.debug(logger + " " + this.getClass().getName() +  "passed TOOL_CONTENT_ID : " + toolContentId);
	    	request.getSession().setAttribute(TOOL_CONTENT_ID,strToolContentId);
    	}
    	catch(NumberFormatException e)
		{
	    	persistError(request,"error.numberFormatException");
			request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD_QUESTIONS);
			return (mapping.findForward(LOAD_QUESTIONS));
		}


		/**
		 * find out if the passed tool content id exists in the db 
		 * present user either a first timer screen with default content data or fetch the existing content.
		 * 
		 * if the toolcontentid does not exist in the db, create the default Map,
		 * there is no need to check if the content is locked in this case.
		 * It is always unlocked since it is the default content.
		*/
		if (!existsContent(toolContentId, request)) 
		{
		    /**
		     * get default content from db, user never created any content before
		     */
			contentId=qaService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
		    logger.debug(logger + " " + this.getClass().getName() +  " " + "getting default content with id:" + contentId);
		    
		    QaContent defaultQaContent = qaService.retrieveQa(contentId);
			logger.debug(logger + " " + this.getClass().getName() +  " " + defaultQaContent);
			
			/**
			 * this is a new content creation, the content must always be unlocked 
			 */
			request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(false));
		    logger.debug(logger + " " + this.getClass().getName() +  "CONTENT_LOCKED: " + request.getSession().getAttribute(CONTENT_LOCKED));
			
			if (defaultQaContent == null)
			{
				logger.debug(logger + " " + this.getClass().getName() +  "Exception occured: " + " No default content");
				request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOT_AVAILABLE, new Boolean(true));
				persistError(request,"error.defaultContent.notAvailable");
				return (mapping.findForward(LOAD_QUESTIONS));
			}
				
			QaUtils.setDefaultSessionAttributes(request, defaultQaContent, qaAuthoringForm);
			logger.debug(logger + " " + this.getClass().getName() +  "set UsernameVisible to OFF: ");
			qaAuthoringForm.setUsernameVisible(OFF);
			logger.debug(logger + " " + this.getClass().getName() +  "UsernameVisible: " + qaAuthoringForm.getUsernameVisible());
			qaAuthoringForm.setQuestionsSequenced(OFF);
			qaAuthoringForm.setSynchInMonitor(OFF);
			
			mapQuestionContent.clear();
	    	/**
	    	 * place the default question as the first entry in the Map
	    	 */
			mapQuestionContent.put(INITIAL_QUESTION_COUNT,request.getSession().getAttribute(DEFAULT_QUESTION_CONTENT));
			logger.debug(logger + " " + this.getClass().getName() +  "Map initialized with default contentid to: " + mapQuestionContent);
		}
		else
		{
			/**
			 * fetch the existing content from db, user will be presented with her previously created content data
			 * Note that the content might have been LOCKED(content in use) if one or more learner has started activities with this content
			 */
		    logger.debug(logger + " " + this.getClass().getName() +  " " + "getting existing content with id:" + toolContentId);
		    QaContent defaultQaContent = qaService.retrieveQa(toolContentId);
			logger.debug(logger + " " + this.getClass().getName() +  " " + defaultQaContent);
			
			boolean studentActivity=qaService.studentActivityOccurredGlobal(defaultQaContent);
			logger.debug(logger + " " + this.getClass().getName() +  "studentActivity on content: " + studentActivity);
			if (studentActivity)
			{
				logger.debug(logger + " " + this.getClass().getName() +  "forward to warning screen as the content is not allowed to be modified.");
				ActionMessages errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY, new ActionMessage("error.content.inUse"));
				saveErrors(request,errors);
				request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(true));
				logger.debug(logger + " " + this.getClass().getName() +  "forward to:" + LOAD);
				return (mapping.findForward(LOAD));
			}
			
		    request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(isContentLocked(defaultQaContent)));
		    logger.debug(logger + " " + this.getClass().getName() +  "CONTENT_LOCKED: " + request.getSession().getAttribute(CONTENT_LOCKED));
			
			QaUtils.setDefaultSessionAttributes(request, defaultQaContent, qaAuthoringForm);
			
			/**
			 * determine the status of radio boxes
			 */
			logger.debug(logger + " " + this.getClass().getName() +  "IS_USERNAME_VISIBLE: " + defaultQaContent.isUsernameVisible());
		    logger.debug(logger + " " + this.getClass().getName() +  "set UsernameVisible to : " + defaultQaContent.isUsernameVisible());
		    if (defaultQaContent.isUsernameVisible())
		    	qaAuthoringForm.setUsernameVisible(ON);
		    else
		    	qaAuthoringForm.setUsernameVisible(OFF);
		    logger.debug(logger + " " + this.getClass().getName() +  "UsernameVisible: " + qaAuthoringForm.getUsernameVisible());
		    if (defaultQaContent.isSynchInMonitor())
		    	qaAuthoringForm.setSynchInMonitor(ON);
		    else
		    	qaAuthoringForm.setSynchInMonitor(OFF);
		    	
		    if (defaultQaContent.isQuestionsSequenced())
		    	qaAuthoringForm.setQuestionsSequenced(ON);
		    else
		    	qaAuthoringForm.setQuestionsSequenced(OFF);
		    
		    request.getSession().setAttribute(IS_USERNAME_VISIBLE_MONITORING, 	new Boolean(defaultQaContent.isUsernameVisible()));
		    request.getSession().setAttribute(IS_SYNCH_INMONITOR_MONITORING, 	new Boolean(defaultQaContent.isSynchInMonitor()));
		    request.getSession().setAttribute(IS_QUESTIONS_SEQUENCED_MONITORING,new Boolean(defaultQaContent.isQuestionsSequenced()));
		    request.getSession().setAttribute(IS_DEFINE_LATER, 					new Boolean(defaultQaContent.isDefineLater()));
		    request.getSession().setAttribute(REPORT_TITLE, 					defaultQaContent.getReportTitle());
		    request.getSession().setAttribute(MONITORING_REPORT_TITLE, 			defaultQaContent.getMonitoringReportTitle());
		    request.getSession().setAttribute(OFFLINE_INSTRUCTIONS, 			defaultQaContent.getOfflineInstructions());
		    request.getSession().setAttribute(ONLINE_INSTRUCTIONS, 				defaultQaContent.getOnlineInstructions());
		    request.getSession().setAttribute(END_LEARNING_MESSSAGE, 			defaultQaContent.getEndLearningMessage());
		    request.getSession().setAttribute(CREATION_DATE, 					defaultQaContent.getCreationDate());
		    
		    logger.debug(logger + " " + this.getClass().getName() +  "IS_QUESTIONS_SEQUENCED_MONITORING: " + 
		    								request.getSession().getAttribute(IS_QUESTIONS_SEQUENCED_MONITORING));
		    
		    logger.debug(logger + " " + this.getClass().getName() +  "IS_DEFINE_LATER: " + 
					request.getSession().getAttribute(IS_DEFINE_LATER));
		    
			/**
			 * get the existing question content
			 */
			logger.debug(logger + " " + this.getClass().getName() +  " " + "setting existing content data from the db");
			mapQuestionContent.clear();
			Iterator queIterator=defaultQaContent.getQaQueContents().iterator();
			Long mapIndex=new Long(1);
			logger.debug(logger + " " + this.getClass().getName() +  " " + "mapQuestionContent: " + mapQuestionContent);
	    	while (queIterator.hasNext())
	    	{
	    		QaQueContent qaQueContent=(QaQueContent) queIterator.next();
	    		if (qaQueContent != null)
	    		{
	    			logger.debug(logger + " " + this.getClass().getName() +  " " + "question: " + qaQueContent.getQuestion());
		    		mapQuestionContent.put(mapIndex.toString(),qaQueContent.getQuestion());
		    		/**
		    		 * make the first entry the default(first) one for jsp
		    		 */
		    		if (mapIndex.longValue() == 1)
		    			request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, qaQueContent.getQuestion());
		    		mapIndex=new Long(mapIndex.longValue()+1);
	    		}
	    	}
	    	logger.debug(logger + " " + this.getClass().getName() +  "Map initialized with existing contentid to: " + mapQuestionContent);
		}
		
    	request.getSession().setAttribute(MAP_QUESTION_CONTENT, mapQuestionContent);
		logger.debug(logger + " " + this.getClass().getName() +  "starter initialized the Comparable Map: " + request.getSession().getAttribute("mapQuestionContent") );
	/**
	 * load questions page
	 */
	
	logger.debug(logger + " " + this.getClass().getName() +  "START_MONITORING_SUMMARY_REQUEST: " + request.getAttribute(START_MONITORING_SUMMARY_REQUEST));
	logger.debug(logger + " " + this.getClass().getName() +  "RENDER_MONITORING_EDITACTIVITY: " + request.getAttribute(RENDER_MONITORING_EDITACTIVITY));
	qaAuthoringForm.resetUserAction();
	return (mapping.findForward(LOAD_QUESTIONS));
  } 
	

	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	protected boolean existsContent(long toolContentId, HttpServletRequest request)
	{
		/**
		 * retrive the service
		 */
		IQaService qaService =QaUtils.getToolService(request);
		logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService: " + qaService);
	    
    	QaContent qaContent=qaService.loadQa(toolContentId);
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaContent: " + qaContent);
	    if (qaContent == null) 
	    	return false;
	    
		return true;	
	}
	
	/**
	 * find out if the content is locked or not. If it is a locked content, the author can not modify it. 
	 * @param qaContent
	 * @return
	 */
	protected boolean isContentLocked(QaContent qaContent)
	{
		logger.debug(logger + " " + this.getClass().getName() +  "is content locked: " + qaContent.isContentLocked());
		return qaContent.isContentLocked();
	}
	
	
	public ActionForward startMonitoringSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
															throws IOException, ServletException, QaApplicationException 
	{
		request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(true));
		return execute(mapping, form, request, response);
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
