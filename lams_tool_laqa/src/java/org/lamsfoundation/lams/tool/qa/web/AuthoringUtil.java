/*
 * Created on 16/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.web;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * 
 * Keeps all operations needed for Authoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class AuthoringUtil implements QaAppConstants {
	static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());
	
    /**
     * reconstructQuestionContentMapForAdd(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * adds a new entry to the Map
     */
    protected void reconstructQuestionContentMapForAdd(Map mapQuestionContent, HttpServletRequest request)
    {
    	logger.debug("pre-add Map content: " + mapQuestionContent);
    	logger.debug("pre-add Map size: " + mapQuestionContent.size());
    	
    	repopulateMap(mapQuestionContent, request);
    	
    	mapQuestionContent.put(new Long(mapQuestionContent.size()+1).toString(), "");
    	request.getSession().setAttribute("mapQuestionContent", mapQuestionContent);
	     
    	logger.debug("post-add Map is: " + mapQuestionContent);    	
	   	logger.debug("post-add count " + mapQuestionContent.size());
    }


    /**
     * reconstructQuestionContentMapForRemove(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * deletes the requested entry from the Map
     */
    protected void reconstructQuestionContentMapForRemove(Map mapQuestionContent, HttpServletRequest request, QaAuthoringForm qaAuthoringForm)
    {
    	 	String questionIndex =qaAuthoringForm.getQuestionIndex();
    	 	logger.debug("pre-delete map content:  " + mapQuestionContent);
    	 	logger.debug("questionIndex: " + questionIndex);
    	 	
    	 	long longQuestionIndex= new Long(questionIndex).longValue();
    	 	logger.debug("pre-delete count: " + mapQuestionContent.size());
    	 	
        	repopulateMap(mapQuestionContent, request);
        	logger.debug("post-repopulateMap questionIndex: " + questionIndex);
        	
	 		mapQuestionContent.remove(new Long(longQuestionIndex).toString());	
	 		logger.debug("removed the question content with index: " + longQuestionIndex);
	 		request.getSession().setAttribute("mapQuestionContent", mapQuestionContent);
	    	
	    	logger.debug("post-delete count " + mapQuestionContent.size());
	    	logger.debug("post-delete map content:  " + mapQuestionContent);
    }

    
    /**
     * reconstructQuestionContentMapForSubmit(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * re-organizes the Map by removing empty entries from the user and creates the final Map ready for service layer
    */
    protected  void reconstructQuestionContentMapForSubmit(Map mapQuestionContent, HttpServletRequest request)
    {
    	logger.debug("pre-submit Map:" + mapQuestionContent);
    	logger.debug("pre-submit Map size :" + mapQuestionContent.size());
    	
    	repopulateMap(mapQuestionContent, request);
    	Map mapFinalQuestionContent = new TreeMap(new QaComparator());
    	
    	Iterator itMap = mapQuestionContent.entrySet().iterator();
	    while (itMap.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
    		{
	        	mapFinalQuestionContent.put(pairs.getKey(), pairs.getValue());
	        	logger.debug("adding the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
    		}
	    }
	    
	    mapQuestionContent=(TreeMap)mapFinalQuestionContent;
	    request.getSession().setAttribute("mapQuestionContent", mapQuestionContent);
	    logger.debug("post-submit Map:" + mapQuestionContent);
    }
    
    
    /**
     * repopulateMap(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void 
     * repopulates the user entries into the Map
     */
    protected void repopulateMap(Map mapQuestionContent, HttpServletRequest request)
    {
    	logger.debug("queIndex: " + request.getSession().getAttribute("queIndex"));
    	long queIndex= new Long(request.getSession().getAttribute("queIndex").toString()).longValue();
    	logger.debug("queIndex: " + queIndex);

    	/** if there is data in the Map remaining from previous session remove those */
		mapQuestionContent.clear();
		logger.debug("Map got initialized: " + mapQuestionContent);
		
		for (long i=0; i < queIndex ; i++)
		{
			String candidateQuestionEntry =request.getParameter("questionContent" + i);
			if (i==0)
    		{
    			request.getSession().setAttribute("defaultQuestionContent", candidateQuestionEntry);
    			logger.debug("defaultQuestionContent set to: " + candidateQuestionEntry);
    		}
			if ((candidateQuestionEntry != null) && (candidateQuestionEntry.length() > 0))
			{
				logger.debug("using key: " + i);
				mapQuestionContent.put(new Long(i+1).toString(), candidateQuestionEntry);
				logger.debug("added new entry.");	
			}
		}
    }


    /**
     * findSelectedTab(ActionMapping mapping,
					            ActionForm form,
					            HttpServletRequest request,
					            HttpServletResponse response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * 
     * determines which tab in the UI is the active one
     */
    protected void findSelectedTab(ActionMapping mapping,
					            ActionForm form,
					            HttpServletRequest request,
					            HttpServletResponse response) 
    {
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
    	String choiceBasic=qaAuthoringForm.getChoiceBasic();
    	String choiceAdvanced=qaAuthoringForm.getChoiceAdvanced();
    	String choiceInstructions=qaAuthoringForm.getChoiceInstructions();
    	
    	/** make the Basic tab the default one */
    	request.getSession().setAttribute(CHOICE,CHOICE_TYPE_BASIC);
    	
		if (choiceBasic != null)
		{
    		logger.debug("CHOICE_TYPE_BASIC");
    		request.getSession().setAttribute(CHOICE,CHOICE_TYPE_BASIC);
		}
    	else if (choiceAdvanced != null)
    	{
    		logger.debug("CHOICE_TYPE_ADVANCED");
    		request.getSession().setAttribute(CHOICE,CHOICE_TYPE_ADVANCED);
    	}
    	else if (choiceInstructions != null)
    	{
    		logger.debug("CHOICE_TYPE_INSTRUCTIONS");
    		request.getSession().setAttribute(CHOICE,CHOICE_TYPE_INSTRUCTIONS);
    	}

    	logger.debug("CHOICE is:" + request.getSession().getAttribute(CHOICE));
    	/** reset tab controllers */
		qaAuthoringForm.choiceBasic=null;
		qaAuthoringForm.choiceAdvanced=null;
		qaAuthoringForm.choiceInstructions=null;
		
		
		/**
		 * if the presentation is for monitoring EditActivity screen, keep preserving request scope START_MONITORING_SUMMARY_REQUEST
		 */
		Boolean renderMonitoringEditActivity=(Boolean)request.getSession().getAttribute(RENDER_MONITORING_EDITACTIVITY);
		if ((renderMonitoringEditActivity != null) && (renderMonitoringEditActivity.booleanValue()))
		{
			request.setAttribute(FORM_INDEX, "1");
			request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(true));
		}
		else
		{
			request.setAttribute(FORM_INDEX, "0");
			request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(false));
		}
			
		logger.debug("START_MONITORING_SUMMARY_REQUEST: " + request.getAttribute(START_MONITORING_SUMMARY_REQUEST));
		logger.debug("formIndex:" + request.getAttribute(FORM_INDEX));

		request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(false));
		if (qaAuthoringForm.getEdit() != null)
		{
			logger.debug("request for editActivity - editmode");
	    	IQaService qaService =QaUtils.getToolService(request);
	    	Long monitoredContentId=(Long)request.getSession().getAttribute(MONITORED_CONTENT_ID);
		    logger.debug("MONITORED_CONTENT_ID: " + monitoredContentId);
		    
		    try
			{
		    	qaService.setAsDefineLater(monitoredContentId);
			}
		    catch (ToolException e)
			{
		    	logger.debug("We should never come here.");
		    	logger.debug("Warning! ToolException occurred");
			}
			
		    
		    logger.debug("MONITORED_CONTENT_ID has been marked as defineLater: ");
			request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));
		}
		else
		{
			request.getSession().setAttribute(TITLE,qaAuthoringForm.getTitle());
			request.getSession().setAttribute(INSTRUCTIONS,qaAuthoringForm.getInstructions());
		}
    }
    
    
    /**
     * createContent(TreeMap mapQuestionContent)
     * return QaContent
     * At this stage, the Map is available from the presentaion layer and can be passed to services layer for persistance.
     * We are making the content and question contents persistent.
     * Id generation is "CURRENTLY" based on java.util.Random
     */
    protected QaContent createContent(Map mapQuestionContent, HttpServletRequest request, QaAuthoringForm qaAuthoringForm)
    {
    	IQaService qaService =QaUtils.getToolService(request);
    	
    	/** the tool content id is passed from the container to the tool and placed into session in the QaStarterAction */
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		/**delete the existing content in the database before applying new content*/
    		qaService.deleteQaById(new Long(toolContentId));
    		logger.debug("post-deletion existing content");
		}
    	logger.debug("using TOOL_CONTENT_ID: " +  toolContentId);
    	request.getSession().setAttribute(TOOL_CONTENT_ID,toolContentId);
    	
    	boolean isQuestionsSequenced=false;
    	boolean isSynchInMonitor=false;
    	boolean isUsernameVisible=false;
    	String reportTitle="";
    	String monitoringReportTitle="";
    	String offlineInstructions="";
    	String onlineInstructions="";
    	String endLearningMessage="";
    	String creationDate="";
    	
    	Boolean isQuestionsSequencedBoolean=new Boolean(false);
	    Boolean isSynchInMonitorBoolean=new Boolean(false);
	    Boolean isUsernameVisibleBoolean=new Boolean(false);
    	
	    Boolean renderMonitoringEditActivity=(Boolean)request.getSession().getAttribute(RENDER_MONITORING_EDITACTIVITY);
		if ((renderMonitoringEditActivity != null) && (renderMonitoringEditActivity.booleanValue()))
		{
			logger.debug("getting properties based on editActivity: All properties available from the http session.");
			isQuestionsSequencedBoolean=(Boolean)request.getSession().getAttribute(IS_QUESTIONS_SEQUENCED_MONITORING);
		    isSynchInMonitorBoolean=(Boolean)request.getSession().getAttribute(IS_SYNCH_INMONITOR_MONITORING);
		    isUsernameVisibleBoolean=(Boolean)request.getSession().getAttribute(IS_USERNAME_VISIBLE_MONITORING);
		    reportTitle=(String)request.getSession().getAttribute(REPORT_TITLE);
		    monitoringReportTitle=(String)request.getSession().getAttribute(MONITORING_REPORT_TITLE);
		    offlineInstructions=(String)request.getSession().getAttribute(OFFLINE_INSTRUCTIONS);
		    onlineInstructions=(String)request.getSession().getAttribute(ONLINE_INSTRUCTIONS);
		    endLearningMessage=(String)request.getSession().getAttribute(END_LEARNING_MESSSAGE);
		    endLearningMessage=(String)request.getSession().getAttribute(END_LEARNING_MESSSAGE);
		    
		    if (isQuestionsSequencedBoolean != null)
		    	isQuestionsSequenced=isQuestionsSequencedBoolean.booleanValue();
		    
		    if (isSynchInMonitorBoolean != null)
		    	isSynchInMonitor=isSynchInMonitorBoolean.booleanValue();
		    
		    if (isUsernameVisibleBoolean != null)
		    	isUsernameVisible=isUsernameVisibleBoolean.booleanValue();
		}
		else
		{
			logger.debug("getting properties based on normal flow: Properties available from form and request parameters.");
			logger.debug("isQuestionsSequenced: " +  qaAuthoringForm.getQuestionsSequenced());
	    	if (qaAuthoringForm.getQuestionsSequenced().equalsIgnoreCase(ON))
	    		isQuestionsSequenced=true;
	    	
	    	logger.debug("isSynchInMonitor: " +  qaAuthoringForm.getSynchInMonitor());
	    	if (qaAuthoringForm.getSynchInMonitor().equalsIgnoreCase(ON))
	    		isSynchInMonitor=true;
	    	
	    	logger.debug("isUsernameVisible: " +  qaAuthoringForm.getUsernameVisible());
	    	if (qaAuthoringForm.getUsernameVisible().equalsIgnoreCase(ON))
	    		isUsernameVisible=true;
	    	
	    	reportTitle=qaAuthoringForm.getReportTitle();
		    monitoringReportTitle=qaAuthoringForm.getMonitoringReportTitle();
		    offlineInstructions=qaAuthoringForm.getOnlineInstructions();
		    onlineInstructions=qaAuthoringForm.getOfflineInstructions();
		    endLearningMessage=qaAuthoringForm.getEndLearningMessage();
		}
		creationDate=(String)request.getSession().getAttribute(CREATION_DATE);
		if (creationDate == null)
			creationDate=new Date(System.currentTimeMillis()).toString();
		
		
		/** read rich text values */
		String richTextOfflineInstructions="";
    	richTextOfflineInstructions = (String)request.getSession().getAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
    	logger.debug("createContent:  richTextOfflineInstructions from session: " + richTextOfflineInstructions);
    	if (richTextOfflineInstructions == null) richTextOfflineInstructions="";
    	
    	String richTextOnlineInstructions="";
    	richTextOnlineInstructions = (String)request.getSession().getAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOnlineInstructions from session: " + richTextOnlineInstructions);
    	if (richTextOnlineInstructions == null) richTextOnlineInstructions="";
    	
    	String richTextTitle="";
    	richTextTitle = (String)request.getSession().getAttribute(RICHTEXT_TITLE);
    	logger.debug("createContent richTextTitle from session: " + richTextTitle);
    	if (richTextTitle == null) richTextTitle="";
    	
    	String richTextInstructions="";
    	richTextInstructions = (String)request.getSession().getAttribute(RICHTEXT_INSTRUCTIONS);
    	logger.debug("createContent richTextInstructions from session: " + richTextInstructions);
    	if (richTextInstructions == null) richTextInstructions="";
    	
    		
    	/**obtain user object from the session*/
    	User toolUser=(User)request.getSession().getAttribute(TOOL_USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserId());
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	long userId=toolUser.getUserId().longValue();
    	
    	/** create a new qa content and leave the default content intact*/
    	QaContent qa = new QaContent();
		qa.setQaContentId(new Long(toolContentId));
		qa.setTitle(richTextTitle);
		qa.setInstructions(richTextInstructions);
		qa.setCreationDate(creationDate); /**preserve this from the db*/ 
		qa.setUpdateDate(new Date(System.currentTimeMillis())); /**keep updating this one*/
		qa.setCreatedBy(userId); /**make sure we are setting the userId from the User object above*/
	    qa.setUsernameVisible(isUsernameVisible);
	    qa.setQuestionsSequenced(isQuestionsSequenced); /**the default question listing in learner mode will be all in the same page*/
	    qa.setSynchInMonitor(isSynchInMonitor);
	    qa.setOnlineInstructions(richTextOnlineInstructions);
	    qa.setOfflineInstructions(richTextOfflineInstructions);
	    qa.setEndLearningMessage(endLearningMessage);
	    qa.setReportTitle(reportTitle);
	    qa.setMonitoringReportTitle(monitoringReportTitle);
	    qa.setQaQueContents(new TreeSet());
	    qa.setQaSessions(new TreeSet());
	    qa.setQaUploadedFiles(new TreeSet());
	    logger.debug("qa content :" +  qa);
    	
    	/**create the content in the db*/
        qaService.createQa(qa);
        logger.debug("qa created with content id: " + toolContentId);
        
        LinkedList listUploadedOfflineFiles = (LinkedList) request.getSession().getAttribute(LIST_UPLOADED_OFFLINE_FILES);
    	logger.debug("final listUploadedOfflineFiles: " + listUploadedOfflineFiles);
    	LinkedList listUploadedOnlineFiles = (LinkedList) request.getSession().getAttribute(LIST_UPLOADED_ONLINE_FILES);
    	logger.debug("final listUploadedOnlineFiles: " + listUploadedOnlineFiles);
    	
    	try{
    		logger.debug("start persisting offline file information to db...");
        	Iterator offlineFilesIterator=listUploadedOfflineFiles.iterator();
        	while (offlineFilesIterator.hasNext())
        	{
        		String uuidAndFileName=(String) offlineFilesIterator.next();
        		logger.debug("iterated uuidAndFileName: " + uuidAndFileName);
        		if ((uuidAndFileName != null) && (uuidAndFileName.indexOf('~') > 0))
        		{
        			int separator=uuidAndFileName.indexOf('~');
        			logger.debug("separator: " + separator);
        			String uuid=uuidAndFileName.substring(0,separator);
        			String fileName=uuidAndFileName.substring(separator+1);
        			logger.debug("using uuid: " + uuid);
        			logger.debug("using fileName: " + fileName);
        			qaService.persistFile(uuid,false, fileName,qa);
        		}
        	}
        	logger.debug("all offline files data has been persisted");	
    	}
    	catch(Exception e)
		{
    		logger.debug("error persisting offline files data: " + listUploadedOfflineFiles);
		}
    	
    	try{
	    	logger.debug("start persisting online file information to db...");
	    	Iterator onlineFilesIterator=listUploadedOnlineFiles.iterator();
	    	while (onlineFilesIterator.hasNext())
	    	{
	    		String uuidAndFileName=(String) onlineFilesIterator.next();
	    		logger.debug("iterated uuidAndFileName: " + uuidAndFileName);
	    		if ((uuidAndFileName != null) && (uuidAndFileName.indexOf('~') > 0))
	    		{
	    			int separator=uuidAndFileName.indexOf('~');
	    			logger.debug("separator: " + separator);
	    			String uuid=uuidAndFileName.substring(0,separator);
	    			String fileName=uuidAndFileName.substring(separator+1);
	    			logger.debug("using uuid: " + uuid);
	    			logger.debug("using fileName: " + fileName);
	    			qaService.persistFile(uuid,true, fileName,qa);
	    		}
	    	}
	    	logger.debug("all online files data has been persisted");
    	}
    	catch(Exception e)
		{
    		logger.debug("error persisting offline files data: " + listUploadedOnlineFiles);
		}
    	
        return qa;
    }

    
    /**
     * createQuestionContent(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * 
     * persist the questions in the Map the user has submitted 
     */
    protected void createQuestionContent(Map mapQuestionContent, HttpServletRequest request, QaContent qaContent)
    {
    	IQaService qaService =QaUtils.getToolService(request);
        
        Iterator itMap = mapQuestionContent.entrySet().iterator();
        int diplayOrder=0;
        while (itMap.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        
	        /**make sure question entered is NOT blank*/
	        if (pairs.getValue().toString().length() != 0)
	        {	        	
		        QaQueContent queContent=  new QaQueContent(pairs.getValue().toString(), 
		        											++diplayOrder, 
															qaContent,
															null,
															null);
	        
   			logger.debug("queContent: " + queContent);
   			qaService.createQaQue(queContent);
   			logger.debug("a qaQueContent created:");
	        }
	    }
	    logger.debug("all questions in the Map persisted:");
    }
    
    
    /** remove existing content data from the db
     * 
     * qaService.removeToolContent(Long toolContentId) gets automatically called only in monitoring mode when
     * the author chooses to delete a lesson. 
     * 
     * In here we act as another client of this contact method since we want to clear q/a tool's content tables based on
     * author's UI interactions. We are removing content + question's content + relavant tool sessions from the db. 
     * 
     * removeAllDBContent(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * removes content for a specific content Id
     */
    protected void removeAllDBContent(HttpServletRequest request)
    {
    	IQaService qaService =QaUtils.getToolService(request);
    	logger.debug("retrieve qaService: " + qaService);
    	String toolContentId=null;
    	
    	if (request.getSession().getAttribute(TOOL_CONTENT_ID) != null)
    		toolContentId=(String)(request.getSession().getAttribute(TOOL_CONTENT_ID));	
    	
    	if ((toolContentId != null) && (!toolContentId.equals(""))) 
		{
    		logger.debug("simulate container behaviour by calling: removeToolContent with: " + new Long(toolContentId));
    		/**
    		 * we are calling removeToolContent to clear content tables although this method normally 
    		 * gets called only in the monitoring mode automatically by the core.
    		 * 
    		 * Having this method here also makes sure that this contract has implemented and tested properly. 
    		 */
    		qaService.removeToolContent(new Long(toolContentId));
    		logger.debug("simulated container behaviour by calling: removeToolContent with: " + toolContentId);
    		logger.debug("removed content from database for content id:" + toolContentId);
		}
    }
    
    /**
     * Normally, a request to set runOffline property of the content comes directly from container through the property inspector.
     * What we do below is simulate that for development purposes.
     * @param request
     */
    
    public void simulatePropertyInspector_RunOffline(HttpServletRequest request)
    {
    	IQaService qaService =QaUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			qaService.setAsRunOffline(new Long(toolContentId));	
			}
    		catch(ToolException e)
			{
    			logger.debug("we should never come here");
			}
    		
    		logger.debug("post-RunAsOffline");
		}
    	logger.debug("end of simulating RunOffline on content id: " + toolContentId);
    }
	
    /**
     * Normally, a request to set defineLaterproperty of the content comes directly from container through the property inspector.
     * What we do below is simulate that for development purposes.
     * @param request
     */
    public void simulatePropertyInspector_setAsDefineLater(HttpServletRequest request)
    {
    	IQaService qaService =QaUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			qaService.setAsDefineLater(new Long(toolContentId));	
    		}
    		catch(ToolException e)
			{
    			logger.debug("we should never come here");
			}
    		
    		logger.debug("post-setAsDefineLater");
		}
    	logger.debug("end of simulating setAsDefineLater on content id: " + toolContentId);
    }
	
}
