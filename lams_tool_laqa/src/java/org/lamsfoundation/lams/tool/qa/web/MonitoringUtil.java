/**
 * 
 * Keeps all operations needed for Learning mode. 
 * @author ozgurd
 *
 */
package org.lamsfoundation.lams.tool.qa.web;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;

/**
 * 
 * Keeps all operations needed for Monitoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class MonitoringUtil implements QaAppConstants{
	static Logger logger = Logger.getLogger(MonitoringUtil.class.getName());

	/**
	 * determine whether all the tool sessions for a particular content has been COMPLETED
	 * @param toolContentId
	 * @return
	 */
	
	public boolean isSessionsSync(HttpServletRequest request, long toolContentId)
	{
		logger.debug("start of isSessionsSync with toolContentId: " + toolContentId);
    	IQaService qaService =QaUtils.getToolService(request);
    	
    	QaContent qaContent =qaService.loadQa(toolContentId); 
    	logger.debug("retrieving qaContent: " + qaContent);
    
    	/**
    	 * iterate all the tool sessions, if even one session is INCOMPLETE, the function returns false
    	 */
    	if (qaContent != null)
    	{
    		Iterator sessionIterator=qaContent.getQaSessions().iterator();
            while (sessionIterator.hasNext())
            {
            	QaSession qaSession=(QaSession)sessionIterator.next(); 
            	logger.debug("iterated qaSession : " + qaSession);
            	if (qaSession.getSession_status().equalsIgnoreCase(QaSession.INCOMPLETE))
            		return false;
            }
    	}
	
	return true;
	}
	
	public void cleanupMonitoringSession(HttpServletRequest request)
	{
		request.getSession().removeAttribute(MAP_TOOL_SESSIONS);
		request.getSession().removeAttribute(MAP_MONITORING_QUESTIONS);
		request.getSession().removeAttribute(CURRENT_MONITORED_TOOL_SESSION);
		request.getSession().removeAttribute(MAP_USER_RESPONSES);
		request.getSession().removeAttribute(DATAMAP_EDITABLE);
		request.getSession().removeAttribute(CHOICE_MONITORING);
		request.getSession().removeAttribute(DATAMAP_EDITABLE_RESPONSE_ID);
		request.getSession().removeAttribute(DATAMAP_HIDDEN_RESPONSE_ID);
		
		/** remove session attributes used commonly */
		request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
		request.getSession().removeAttribute(REPORT_TITLE_MONITOR);
		request.getSession().removeAttribute(IS_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(TOOL_CONTENT_ID);
		request.getSession().removeAttribute(ATTR_USERDATA);
		request.getSession().removeAttribute(TOOL_USER);
		request.getSession().removeAttribute(TOOL_SERVICE);
		request.getSession().removeAttribute(TARGET_MODE);
	}

	
	public void startLesson(QaMonitoringForm qaMonitoringForm, HttpServletRequest request) throws ToolException
	{
		IQaService qaService=QaUtils.getToolService(request);
		
		String strFromToolContentId="";
	    String strToToolContentId="";
	    
		qaMonitoringForm.resetUserAction();
		/**
	     * In deployment, we won't be passing FROM_TOOL_CONTENT_ID, TO_TOOL_CONTENT_ID and TOOL_SESSION_ID from url
	     * the Monitoring Service bean calls:
	     * copyToolContent(Long fromContentId, Long toContentId)  
	     */
	    strFromToolContentId=request.getParameter(FROM_TOOL_CONTENT_ID);
		logger.debug("startLesson"  +  "FROM_TOOL_CONTENT_ID: " + strFromToolContentId);
	    if (strFromToolContentId == null)
	    {
	    	throw new QaApplicationException("Exception occured: " +
	    			"Tool expects a legitimate FROM_TOOL_CONTENT_ID from the container. Can't continue!");
	    }
	    
	    strToToolContentId=request.getParameter(TO_TOOL_CONTENT_ID);
	    logger.debug("startLesson" +  "TO_TOOL_CONTENT_ID: " + strToToolContentId);
	    if (strToToolContentId == null)
	    {
	    	throw new QaApplicationException("Exception occured: " +
	    			"Tool expects a legitimate TO_TOOL_CONTENT_ID from the container. Can't continue!");
	    }
	    
	    try
		{
	    	qaService.copyToolContent(new Long(strFromToolContentId), new Long(strToToolContentId));	
	    }
	    catch(ToolException e)
		{
	    	logger.debug("exception copying content.");
	    	throw e;
		}
		
		/** calls to these two methods will be made from Monitoring Service bean optionally depending on
		 *  the the tool is setup for DefineLater and (or) RunOffline 
		 */
		
		/**
		 * TESTED to work
		 * qaService.setAsDefineLater(new Long(strToToolContentId));
		   qaService.setAsRunOffline(new Long(strToToolContentId));
		 * 
		 */
		qaMonitoringForm.resetUserAction();
	}
	
	
	public void deleteLesson(QaMonitoringForm qaMonitoringForm, HttpServletRequest request)
	{
		IQaService qaService=QaUtils.getToolService(request);
		
		String strFromToolContentId="";
	    String strToToolContentId="";
		
	    qaMonitoringForm.resetUserAction();
				
		/**
		 * TESTED to work
		 */
		strToToolContentId=request.getParameter(TO_TOOL_CONTENT_ID);
	    logger.debug("TO_TOOL_CONTENT_ID: " + strToToolContentId);
	    if (strToToolContentId == null)
	    {
	    	throw new QaApplicationException("Exception occured: " +
	    			"Tool expects a legitimate TO_TOOL_CONTENT_ID from the container. Can't continue!");
	    }
		qaService.removeToolContent(new Long(strToToolContentId));
		qaMonitoringForm.resetUserAction();
	}
	
		
	public void forceComplete(QaMonitoringForm qaMonitoringForm, HttpServletRequest request)
	{
		IQaService qaService=QaUtils.getToolService(request);
		/**
		 * Parameter: userId
		 */
		qaMonitoringForm.resetUserAction();
		logger.debug("request for forceComplete");
		String userId=request.getParameter(MONITOR_USER_ID);
		logger.debug("MONITOR_USER_ID: " + userId);
		qaService.setAsForceComplete(new Long(userId));
		logger.debug("end of setAsForceComplete with userId: " + userId);
	}
	
	
	public  boolean isDefaultMonitoringScreen(QaMonitoringForm qaMonitoringForm)
	{
		if ((qaMonitoringForm.getSummary() == null) &&
		   (qaMonitoringForm.getInstructions() == null) &&
		   (qaMonitoringForm.getEditActivity() == null) &&
		   (qaMonitoringForm.getStats() == null))
		{
			qaMonitoringForm.setSummary("summary");
			return true;
		}
		return false;
	}
	

	/**
	 * findSelectedMonitoringTab(ActionForm form,
	 *							            HttpServletRequest request)
	 * return void						            
	 * determines which monitoring tabs is the active one.
	 * 
	 */
	public void findSelectedMonitoringTab(ActionForm form,
								            HttpServletRequest request)
	{
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		String choiceTypeMonitoringSummary=qaMonitoringForm.getSummary();
		String choiceTypeMonitoringInstructions=qaMonitoringForm.getInstructions();
		String choiceTypeMonitoringEditActivity=qaMonitoringForm.getEditActivity();
		String choiceTypeMonitoringStats=qaMonitoringForm.getStats();
		
		/**make the Summary tab the default one */
		request.getSession().setAttribute(CHOICE_MONITORING,CHOICE_TYPE_MONITORING_SUMMARY);
		
		if (choiceTypeMonitoringSummary != null)
		{
			logger.debug("CHOICE_TYPE_MONITORING_SUMMARY");
			request.getSession().setAttribute(CHOICE_MONITORING,CHOICE_TYPE_MONITORING_SUMMARY);
		}
		else if ((choiceTypeMonitoringInstructions != null) || (qaMonitoringForm.getSubmitMonitoringInstructions() != null))
		{
			logger.debug("CHOICE_TYPE_MONITORING_INSTRUCTIONS");
			request.getSession().setAttribute(CHOICE_MONITORING,CHOICE_TYPE_MONITORING_INSTRUCTIONS);
		}
		else if (choiceTypeMonitoringEditActivity != null)
		{
			logger.debug("CHOICE_TYPE_MONITORING_EDITACTIVITY");
			request.getSession().setAttribute(CHOICE_MONITORING,CHOICE_TYPE_MONITORING_EDITACTIVITY);
		}
		else if (choiceTypeMonitoringStats != null)
		{
			logger.debug("CHOICE_TYPE_MONITORING_STATS");
			request.getSession().setAttribute(CHOICE_MONITORING,CHOICE_TYPE_MONITORING_STATS);
		}
		logger.debug("CHOICE_MONITORING is:" + request.getSession().getAttribute(CHOICE_MONITORING));
		
		/** reset tab controllers */
		qaMonitoringForm.setSummary(null);
		qaMonitoringForm.setInstructions(null);
		qaMonitoringForm.setEditActivity(null);
		qaMonitoringForm.setStats(null);
	}
	
	public boolean isNonDefaultScreensVisited(HttpServletRequest request)
	{
		Boolean monitoringInstructionsVisited = (Boolean) request.getSession().getAttribute(MONITORING_INSTRUCTIONS_VISITED);
		Boolean monitoringEditActivityVisited = (Boolean) request.getSession().getAttribute(MONITORING_EDITACTIVITY_VISITED);
		Boolean monitoringStatsVisited 		  = (Boolean) request.getSession().getAttribute(MONITORING_STATS_VISITED);
		
		logger.debug("isNonDefaultScreensVisited:" + monitoringInstructionsVisited  + " " + 
													monitoringEditActivityVisited + " " + monitoringStatsVisited);
		 
		if ((monitoringInstructionsVisited != null) && (monitoringInstructionsVisited.booleanValue()))
			return true;
		
		if ((monitoringEditActivityVisited != null) && (monitoringEditActivityVisited.booleanValue()))
			return true;
		
		if ((monitoringStatsVisited != null) && (monitoringStatsVisited.booleanValue()))
			return true;
		
		return false;
	}
	
	public void updateResponse(HttpServletRequest request, String responseId, String updatedResponse)
	{
		IQaService qaService=QaUtils.getToolService(request);
		
		logger.debug("load response with responseId: " + new Long(responseId).longValue());
		QaUsrResp qaUsrResp=qaService.retrieveQaUsrResp(new Long(responseId).longValue());   
		logger.debug("loaded user response:  " + qaUsrResp);
		qaUsrResp.setAnswer(updatedResponse);
		qaService.updateQaUsrResp(qaUsrResp);
		logger.debug("updated user response in the db:  " + qaUsrResp);
	}
	
	public void hideResponse(HttpServletRequest request, String responseId)
	{
		IQaService qaService=QaUtils.getToolService(request);
		
		logger.debug("load response with responseId for hiding: " + new Long(responseId).longValue());
		QaUsrResp qaUsrResp=qaService.retrieveQaUsrResp(new Long(responseId).longValue());   
		logger.debug("loaded user response:  " + qaUsrResp);
		qaUsrResp.setHidden(true);
		qaService.updateQaUsrResp(qaUsrResp);
		logger.debug("updated user response in the db:  " + qaUsrResp);
	}
	
	
	public void unHideResponse(HttpServletRequest request, String responseId)
	{
		IQaService qaService=QaUtils.getToolService(request);
		
		logger.debug("load response with responseId for un-hiding: " + new Long(responseId).longValue());
		QaUsrResp qaUsrResp=qaService.retrieveQaUsrResp(new Long(responseId).longValue());   
		logger.debug("loaded user response:  " + qaUsrResp);
		qaUsrResp.setHidden(false);
		qaService.updateQaUsrResp(qaUsrResp);
		logger.debug("updated user response in the db:  " + qaUsrResp);
	}
}
