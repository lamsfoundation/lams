/**
 * @author Ozgur Demirtas
 * 
 * McStarterAction loads the default content and initializes the presentation Map.
 * Initializes the tool's authoring mode
 * Requests can come either from authoring envuironment or from the monitoring environment for Edit Activity screen
 * 
 * Tool path The URL path for the tool should be <lamsroot>/tool/$TOOL_SIG.  
 * 
<action path="/authoringStarter" type="org.lamsfoundation.lams.tool.mc.web.McStarterAction" 
		name="McAuthoringForm" input=".starter"> 
	<forward
    name="load"
    path=".questions"
    redirect="true"
	/>
	
	<forward
    name="error"
    path=".error"
    redirect="true"
	/>

	<forward
    name="errorList"
    path=".errorList"
    redirect="true"
	/>
</action>  
*/

package org.lamsfoundation.lams.tool.mc.web;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;

public class McStarterAction extends Action implements McAppConstants {
	/*
	 * CONTENT_LOCKED refers to content being in use or not: Any students answered that content?
	 * For future CONTENT_LOCKED ->CONTENT_IN_USE
	 *  
	 * may use org.lamsfoundation.lams.web.util.AttributeNames  
	 * check back McUtils.configureContentRepository(request);
	 */
	static Logger logger = Logger.getLogger(McStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException {
		IMcService mcService = McUtils.getToolService(request);
		logger.debug("retrieving mcService from session: " + mcService);
		if (mcService == null)
		{
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from proxy: " + mcService);
		    request.getSession().setAttribute(TOOL_SERVICE, mcService);		
		}

		setupPaths(request);
		initialiseAttributes(request);
		
		McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
		mcAuthoringForm.resetRadioBoxes();
		
		ActionForward validateParameters=populateParameters(request,mapping);
		logger.debug("validateParameters:  " + validateParameters);
		if (validateParameters != null)
		{
			logger.debug("validateParameters not null : " + validateParameters);
			return validateParameters;
		}
		else
		{
			/* mark the http session as an authoring activity */
		    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_AUTHORING);
		    
		    /* define tab controllers for jsp */
		    request.getSession().setAttribute(CHOICE_TYPE_BASIC,CHOICE_TYPE_BASIC);
		    request.getSession().setAttribute(CHOICE_TYPE_ADVANCED,CHOICE_TYPE_ADVANCED);
		    request.getSession().setAttribute(CHOICE_TYPE_INSTRUCTIONS,CHOICE_TYPE_INSTRUCTIONS);
		
		    logger.debug("will render authoring screen");
		    String strToolContentId="";
		    strToolContentId=request.getParameter(TOOL_CONTENT_ID);
		    
		    /* Process incoming tool content id. 
		     * Either exists or not exists in the db yet, a toolContentId must be passed to the tool from the container */
		    long toolContentId=0;
	    	try
			{
		    	toolContentId=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + toolContentId);
		    	request.getSession().setAttribute(TOOL_CONTENT_ID, new Long(strToolContentId));
	    	}
	    	catch(NumberFormatException e)
			{
		    	persistError(request,"error.numberFormatException");
				request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				logger.debug("forwarding to: " + LOAD_QUESTIONS);
				return (mapping.findForward(LOAD_QUESTIONS));
			}
		
	    	/*
			 * find out if the passed tool content id exists in the db 
			 * present user either a first timer screen with default content data or fetch the existing content.
			 * 
			 * if the toolcontentid does not exist in the db, create the default Map,
			 * there is no need to check if the content is in use in this case.
			 * It is always unlocked -> not in use since it is the default content.
			*/
			if (!existsContent(toolContentId, request))
			{
				logger.debug("retrieving default content");
				retrieveDefaultContent(request, mcAuthoringForm);
			}
			else
			{
				logger.debug("retrieving existing content for: " + toolContentId);
				retrieveExistingContent(request, mcAuthoringForm, toolContentId);
			}
		}
		mcAuthoringForm.resetUserAction();
		logger.debug("return to: " + LOAD_QUESTIONS);
		return (mapping.findForward(LOAD_QUESTIONS));
	} 
	
	public ActionForward populateParameters(HttpServletRequest request, ActionMapping mapping)
	{
		IMcService mcService =McUtils.getToolService(request);
		/* retrieve the default content id based on tool signature */
		long contentId=0;
		try
		{
			logger.debug("attempt retrieving tool with signature : " + MY_SIGNATURE);
			contentId=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + contentId);
			if (contentId == 0) 
			{
				logger.debug("default content id has not been setup");
				request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true));
				persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));	
			}
		}
		catch(Exception e)
		{
			logger.debug("error getting the default content id: " + e.getMessage());
			request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true));
			persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}

		/* retrieve uid of the content based on default content id determined above */
		long contentUID=0;
		try
		{
			logger.debug("retrieve uid of the content based on default content id determined above: " + contentId);
			McContent mcContent=mcService.retrieveMc(new Long(contentId));
			if (mcContent == null)
			{
				logger.debug("Exception occured: No default content");
	    		request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true));
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using mcContent: " + mcContent);
			logger.debug("using mcContent uid: " + mcContent.getUid());
			contentUID=mcContent.getUid().longValue();
		}
		catch(Exception e)
		{
			logger.debug("Exception occured: No default question content");
			request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true));
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}
				
		
		/* retrieve uid of the default question content  */
		long queContentUID=0;
		try
		{
			logger.debug("retrieve the default question content based on default content UID: " + queContentUID);
			McQueContent mcQueContent=mcService.getToolDefaultQuestionContent(contentUID);
			logger.debug("using mcQueContent: " + mcQueContent);
			if (mcQueContent == null)
			{
				logger.debug("Exception occured: No default question content");
	    		request.setAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE, new Boolean(true));
				persistError(request,"error.defaultQuestionContent.notAvailable");
				return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using mcQueContent uid: " + mcQueContent.getUid());
			queContentUID=mcQueContent.getUid().longValue();
			request.getSession().setAttribute(DEFAULT_QUESTION_UID, new Long(queContentUID));
			logger.debug("DEFAULT_QUESTION_UID: " + queContentUID);
		}
		catch(Exception e)
		{
			logger.debug("Exception occured: No default question content");
    		request.setAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE, new Boolean(true));
			persistError(request,"error.defaultQuestionContent.notAvailable");
			return (mapping.findForward(ERROR_LIST));
		}
		
		
		/* retrieve default options content */
		try
		{
			logger.debug("retrieve the default options content based on default question content UID: " + queContentUID);
			List list=mcService.findMcOptionsContentByQueId(new Long(queContentUID));
			logger.debug("using options list: " + list);
			if (list == null)
			{
				logger.debug("Exception occured: No default options content");
	    		request.setAttribute(USER_EXCEPTION_DEFAULTOPTIONSCONTENT_NOT_AVAILABLE, new Boolean(true));
				persistError(request,"error.defaultOptionsContent.notAvailable");
				return (mapping.findForward(ERROR_LIST));
			}
			
		}
		catch(Exception e)
		{
			logger.debug("Exception occured: No default options content");
    		request.setAttribute(USER_EXCEPTION_DEFAULTOPTIONSCONTENT_NOT_AVAILABLE, new Boolean(true));
			persistError(request,"error.defaultOptionsContent.notAvailable");
			return (mapping.findForward(ERROR_LIST));
		}		
		
		return null;
	}
	

	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	protected boolean existsContent(long toolContentId, HttpServletRequest request)
	{
		IMcService mcService =McUtils.getToolService(request);
		McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
	    if (mcContent == null) 
	    	return false;
	    
		return true;	
	}
	
	/**
	 * find out if the content is locked or not. If it is a locked content, the author can not modify it.
	 * The idea of content being locked is, once any one learner starts using a particular content
	 * that content should become unmodifiable. 
	 * @param mcContent
	 * @return
	 */
	protected boolean isContentInUse(McContent mcContent)
	{
		logger.debug("is content inuse: " + mcContent.isContentInUse());
		return  mcContent.isContentInUse();
	}
	
	
	/**
	 * sets up ROOT_PATH and PATH_TO_LAMS attributes for presentation purposes
	 * setupPaths(HttpServletRequest request)
	 * @param request
	 */
	protected void setupPaths(HttpServletRequest request)
	{
		String protocol = request.getProtocol();
		if(protocol.startsWith("HTTPS")){
			protocol = "https://";
		}else{
			protocol = "http://";
		}
		String root = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		String pathToLams = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";
		request.getSession().setAttribute(ROOT, root);
		request.getSession().setAttribute(ROOT_PATH, root);
		request.getSession().setAttribute(PATH_TO_LAMS, pathToLams);
		
		logger.debug("setting root to: " + request.getSession().getAttribute(ROOT));
	}
	
	
	/**
	 * retrieves the contents of an existing content from the db and prepares it for presentation
	 * retrieveExistingContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm, long toolContentId)
	 * 
	 * @param request
	 * @param mcAuthoringForm
	 * @param toolContentId
	 */
	protected void retrieveExistingContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm, long toolContentId)
	{
		IMcService mcService =McUtils.getToolService(request);
		
		request.getSession().setAttribute(IS_REVISITING_USER, new Boolean(true));
		
		logger.debug("getting existing content with id:" + toolContentId);
		McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
		logger.debug("existing mcContent:" + mcContent);
		
		request.getSession().setAttribute(RICHTEXT_TITLE,mcContent.getTitle());
		request.getSession().setAttribute(RICHTEXT_INSTRUCTIONS,mcContent.getInstructions());
		request.getSession().setAttribute(QUESTIONS_SEQUENCED,new Boolean(mcContent.isQuestionsSequenced()));
		request.getSession().setAttribute(USERNAME_VISIBLE,new Boolean(mcContent.isUsernameVisible()));
		request.getSession().setAttribute(CREATED_BY, new Long(mcContent.getCreatedBy()));
		request.getSession().setAttribute(MONITORING_REPORT_TITLE,mcContent.getMonitoringReportTitle());
		request.getSession().setAttribute(REPORT_TITLE,mcContent.getReportTitle());
		request.getSession().setAttribute(RUN_OFFLINE, new Boolean(mcContent.isRunOffline()));
		request.getSession().setAttribute(DEFINE_LATER, new Boolean(mcContent.isDefineLater()));
		request.getSession().setAttribute(SYNCH_IN_MONITOR, new Boolean(mcContent.isSynchInMonitor()));
		request.getSession().setAttribute(OFFLINE_INSTRUCTIONS,mcContent.getOfflineInstructions());
		request.getSession().setAttribute(ONLINE_INSTRUCTIONS,mcContent.getOnlineInstructions());
		request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,mcContent.getOfflineInstructions());
		request.getSession().setAttribute(RICHTEXT_REPORT_TITLE,mcContent.getReportTitle());
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,mcContent.getOnlineInstructions());
	    request.getSession().setAttribute(RICHTEXT_END_LEARNING_MSG,mcContent.getEndLearningMessage());
		request.getSession().setAttribute(END_LEARNING_MESSAGE,mcContent.getEndLearningMessage());
		request.getSession().setAttribute(CONTENT_IN_USE, new Boolean(mcContent.isContentInUse()));
		request.getSession().setAttribute(RETRIES, new Boolean(mcContent.isRetries()));
		request.getSession().setAttribute(PASSMARK, mcContent.getPassMark()); //Integer
		request.getSession().setAttribute(SHOW_FEEDBACK, new Boolean(mcContent.isShowFeedback())); 
		
		McUtils.setDefaultSessionAttributes(request, mcContent, mcAuthoringForm);
		logger.debug("RICHTEXT_TITLE:" + request.getSession().getAttribute(RICHTEXT_TITLE));
		
		mcAuthoringForm.setPassmark((mcContent.getPassMark()).toString());
		logger.debug("PASSMARK:" + mcAuthoringForm.getPassmark());
		
		logger.debug("getting name lists based on uid:" + mcContent.getUid());
		
		List listUploadedOffFiles= mcService.retrieveMcUploadedOfflineFilesName(mcContent.getUid());
		logger.debug("existing listUploadedOfflineFileNames:" + listUploadedOffFiles);
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOffFiles);
		
		List listUploadedOnFiles= mcService.retrieveMcUploadedOnlineFilesName(mcContent.getUid());
		logger.debug("existing listUploadedOnlineFileNames:" + listUploadedOnFiles);
		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnFiles);
		
		/*
		List listUploadedOffFilesUuidPlusFilename= mcService.retrieveMcUploadedOfflineFilesUuidPlusFilename(mcContent.getUid());
		logger.debug("existing listUploadedOffFilesUuidPlusFilename:" + listUploadedOffFilesUuidPlusFilename);
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILES,listUploadedOffFilesUuidPlusFilename);
		*/
		
		if (mcContent.isUsernameVisible())
		{
			mcAuthoringForm.setUsernameVisible(ON);
			logger.debug("setting userNameVisible to true");
		}
		else
		{
			mcAuthoringForm.setUsernameVisible(OFF);	
			logger.debug("setting userNameVisible to false");				
		}
	    
		
		if (mcContent.isQuestionsSequenced())
		{
			mcAuthoringForm.setQuestionsSequenced(ON);
			logger.debug("setting questionsSequenced to true");
		}
		else
		{
			mcAuthoringForm.setQuestionsSequenced(OFF);	
			logger.debug("setting questionsSequenced to false");				
		}

		if (mcContent.isSynchInMonitor())
		{
			mcAuthoringForm.setSynchInMonitor(ON);	
			logger.debug("setting SynchInMonitor to true");
		}
		else
		{
			mcAuthoringForm.setSynchInMonitor(OFF);	
			logger.debug("setting SynchInMonitor to false");				
		}

		if (mcContent.isRetries())
		{
			mcAuthoringForm.setRetries(ON);	
			logger.debug("setting retries to true");
		}
		else
		{
			mcAuthoringForm.setRetries(OFF);	
			logger.debug("setting retries to false");				
		}

		if (mcContent.isShowFeedback())
		{
			mcAuthoringForm.setShowFeedback(ON);	
			logger.debug("setting showFeedback to true");
		}
		else
		{
			mcAuthoringForm.setShowFeedback(OFF);	
			logger.debug("setting showFeedback to false");				
		}
		
		if (mcContent.isShowReport())
		{
			mcAuthoringForm.setSln(ON);	
			logger.debug("setting sln to true");
		}
		else
		{
			mcAuthoringForm.setSln(OFF);	
			logger.debug("setting sln to false");				
		}
		
		McUtils.populateUploadedFilesData(request, mcContent);
	    logger.debug("populated UploadedFilesData");
	    Map mapWeights= AuthoringUtil.rebuildWeightsMapfromDB(request, new Long(toolContentId));
    	logger.debug("mapWeights: " + mapWeights);
    	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
    	
    	Map mapQuestionsContent=AuthoringUtil.rebuildQuestionMapfromDB(request, new Long(toolContentId));
    	logger.debug("mapQuestionsContent:" + mapQuestionsContent);
    	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
    	logger.debug("starter initialized the existing Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
    	
	    AuthoringUtil.refreshMaps(request, toolContentId);
	    /*process offline files metadata*/
	    List listOfflineFilesMetaData=mcService.getOfflineFilesMetaData(mcContent.getUid());
	    logger.debug("existing listOfflineFilesMetaData, to be structured as McAttachmentDTO: " + listOfflineFilesMetaData);
	    listOfflineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOfflineFilesMetaData);
	    logger.debug("populated listOfflineFilesMetaData: " + listOfflineFilesMetaData);
	    request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
	    
	    List listUploadedOfflineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOfflineFilesMetaData);
	    logger.debug("returned from db listUploadedOfflineFileNames: " + listUploadedOfflineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES, listUploadedOfflineFileNames);
	    
	    /*process online files metadata*/
	    List listOnlineFilesMetaData=mcService.getOnlineFilesMetaData(mcContent.getUid());
	    logger.debug("existing listOnlineFilesMetaData, to be structured as McAttachmentDTO: " + listOnlineFilesMetaData);
	    listOnlineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOnlineFilesMetaData);
	    logger.debug("populated listOnlineFilesMetaData: " + listOnlineFilesMetaData);
	    request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
	    
	    List listUploadedOnlineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOnlineFilesMetaData);
	    logger.debug("returned from db listUploadedOnlineFileNames: " + listUploadedOnlineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES, listUploadedOnlineFileNames);
	}
	
	
	/**
	 * retrieves the contents of the default content from the db and prepares it for presentation
	 * retrieveDefaultContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	 * 
	 * @param request
	 * @param mcAuthoringForm
	 */
	protected void retrieveDefaultContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	{
		IMcService mcService =McUtils.getToolService(request);
		request.getSession().setAttribute(IS_REVISITING_USER, new Boolean(false));
		
		long contentId=0;
		logger.debug("getting default content");
		contentId=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
		McContent mcContent=mcService.retrieveMc(new Long(contentId));
		logger.debug("mcContent:" + mcContent);
		
		/* reset all radioboxes to false*/
		mcAuthoringForm.resetRadioBoxes();
		logger.debug("all radioboxes arec reset");
		
		request.getSession().setAttribute(RICHTEXT_TITLE,mcContent.getTitle());
		request.getSession().setAttribute(RICHTEXT_INSTRUCTIONS,mcContent.getInstructions());
		request.getSession().setAttribute(QUESTIONS_SEQUENCED,new Boolean(mcContent.isQuestionsSequenced()));
		request.getSession().setAttribute(USERNAME_VISIBLE,new Boolean(mcContent.isUsernameVisible()));
		request.getSession().setAttribute(CREATED_BY, new Long(mcContent.getCreatedBy()));
		request.getSession().setAttribute(MONITORING_REPORT_TITLE,mcContent.getMonitoringReportTitle());
		request.getSession().setAttribute(REPORT_TITLE,mcContent.getReportTitle());
		request.getSession().setAttribute(RUN_OFFLINE, new Boolean(mcContent.isRunOffline()));
		request.getSession().setAttribute(DEFINE_LATER, new Boolean(mcContent.isDefineLater()));
		request.getSession().setAttribute(SYNCH_IN_MONITOR, new Boolean(mcContent.isSynchInMonitor()));
		request.getSession().setAttribute(OFFLINE_INSTRUCTIONS,mcContent.getOfflineInstructions());
		request.getSession().setAttribute(ONLINE_INSTRUCTIONS,mcContent.getOnlineInstructions());
		request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,mcContent.getOfflineInstructions());
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,mcContent.getOnlineInstructions());
		request.getSession().setAttribute(END_LEARNING_MESSAGE,mcContent.getEndLearningMessage());
		request.getSession().setAttribute(CONTENT_IN_USE, new Boolean(mcContent.isContentInUse()));
		request.getSession().setAttribute(RETRIES, new Boolean(mcContent.isRetries()));
		request.getSession().setAttribute(PASSMARK, mcContent.getPassMark()); //Integer
		request.getSession().setAttribute(SHOW_FEEDBACK, new Boolean(mcContent.isShowFeedback())); 
		request.getSession().setAttribute(RICHTEXT_REPORT_TITLE,mcContent.getReportTitle());
		request.getSession().setAttribute(RICHTEXT_END_LEARNING_MSG,mcContent.getEndLearningMessage());
		
		McUtils.setDefaultSessionAttributes(request, mcContent, mcAuthoringForm);
		
		logger.debug("PASSMARK:" + request.getSession().getAttribute(PASSMARK));
		
		logger.debug("RICHTEXT_TITLE:" + request.getSession().getAttribute(RICHTEXT_TITLE));
		logger.debug("getting default content");
		/*
		 * this is a new content creation, the content must not be in use.
		 * relevant attribute: CONTENT_IN_USE  
		 */
		request.getSession().setAttribute(CONTENT_IN_USE, new Boolean(false));
	    logger.debug("CONTENT_IN_USE: " + request.getSession().getAttribute(CONTENT_IN_USE));
	    
	    /* this is already done in  mcAuthoringForm.resetRadioBoxes()*/ 
	    mcAuthoringForm.setUsernameVisible(OFF);
	    mcAuthoringForm.setQuestionsSequenced(OFF);
		mcAuthoringForm.setSynchInMonitor(OFF);
		mcAuthoringForm.setRetries(OFF);
		mcAuthoringForm.setShowFeedback(OFF);
		mcAuthoringForm.setSln(OFF);
				
		/* collect options for the default question content into a Map*/
		McQueContent mcQueContent=mcService.getToolDefaultQuestionContent(mcContent.getUid().longValue());
		logger.debug("mcQueContent:" + mcQueContent);
		
		/* mcQueContent can not be null since here it was verified before*/ 
		/* display a single sample question */
		request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, mcQueContent.getQuestion());
		
		Map mapQuestionsContent= new TreeMap(new McComparator());
		mapQuestionsContent.put(new Long(1).toString(), mcQueContent.getQuestion());
		request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		logger.debug("starter initialized the Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
		
		/* hold all he options for this question*/
		List list=mcService.findMcOptionsContentByQueId(mcQueContent.getUid());
		logger.debug("options list:" + list);

		Map mapOptionsContent= new TreeMap(new McComparator());
		Iterator listIterator=list.iterator();
		Long mapIndex=new Long(1);
		while (listIterator.hasNext())
		{
			McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
			logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
			mapOptionsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
			mapIndex=new Long(mapIndex.longValue()+1);
		}
		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
		Map mapDefaultOptionsContent=mapOptionsContent;
		request.getSession().setAttribute(MAP_DEFAULTOPTIONS_CONTENT, mapDefaultOptionsContent);
		logger.debug("starter initialized the Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
		logger.debug("starter initialized the Default Options Map: " + request.getSession().getAttribute(MAP_DEFAULTOPTIONS_CONTENT));
		
		Map mapWeights= new TreeMap(new McComparator());
		/* reset all the weights to 0*/
		long mapCounter=0;
		for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
		{
			mapCounter++;
			mapWeights.put(new Long(mapCounter).toString(), new Integer(0));
		}	
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		logger.debug("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));	
	}
	
	
	/**
	 * initialisation
	 * initialiseAttributes(HttpServletRequest request)
	 * 
	 * @param request
	 */
	protected void initialiseAttributes(HttpServletRequest request)
	{
		logger.debug("starting initialiseAttributes...");
		/*  CURRENT_TAB == 1 defines Basic Tab
		 *  CURRENT_TAB == 2 defines Avanced Tab
		 *  CURRENT_TAB == 3 defines Instructions Tab
		 */ 
		request.getSession().setAttribute(CURRENT_TAB, new Long(1));
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		
		/* needs to run only once per tool*/ 
		/* McUtils.configureContentRepository(request, mcService); */
		
		/* these two are for repository access */
		/* holds the final offline files  list */
		//LinkedList listUploadedOfflineFiles= new LinkedList();
		//LinkedList listUploadedOnlineFiles= new LinkedList();
		
		/* these two are for jsp */
		LinkedList listUploadedOfflineFileNames= new LinkedList();
		LinkedList listUploadedOnlineFileNames= new LinkedList();
		
		//request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILES,listUploadedOfflineFiles);
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);
		
		//request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILES,listUploadedOnlineFiles);
		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);
		
		LinkedList listOfflineFilesMetaData= new LinkedList();
		LinkedList listOnlineFilesMetaData= new LinkedList();
		request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
		request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
		
		Map mapQuestionsContent= new TreeMap(new McComparator());
		Map mapOptionsContent= new TreeMap(new McComparator());
		Map mapDefaultOptionsContent= new TreeMap(new McComparator());
		
		Map mapGeneralOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		Map mapStartupGeneralOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT, mapStartupGeneralOptionsContent);
		
		Map mapStartupGeneralSelectedOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT, mapStartupGeneralSelectedOptionsContent);
		
		Map mapDisabledQuestions= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_DISABLED_QUESTIONS, mapDisabledQuestions);
		
		Map mapWeights= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		
		Map mapCheckBoxStates= new TreeMap(new McComparator());
		mapCheckBoxStates.put("1" ,"INCORRECT");
		mapCheckBoxStates.put("2" ,"CORRECT");
		request.getSession().setAttribute(MAP_CHECKBOX_STATES, mapCheckBoxStates);

		Map mapSelectedOptions= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
		
		Map mapFeedbackIncorrect= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_FEEDBACK_INCORRECT, mapFeedbackIncorrect);
		
		Map mapFeedbackCorrect= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_FEEDBACK_CORRECT, mapFeedbackCorrect);
		
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
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
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
