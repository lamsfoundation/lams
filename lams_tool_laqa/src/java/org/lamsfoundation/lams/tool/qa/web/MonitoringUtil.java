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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
	 * boolean isSessionsSync(HttpServletRequest request, long toolContentId)
	 * @param toolContentId
	 * @return boolean
	 */
	public boolean isSessionsSync(QaContent qaContent)
	{
    	/*
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
	
	
	/**
	 * cleanupMonitoringSession(HttpServletRequest request)
	 * @param request
	 */
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
		
		/* remove session attributes used commonly */
		request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
		request.getSession().removeAttribute(REPORT_TITLE_MONITOR);
		request.getSession().removeAttribute(IS_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
		request.getSession().removeAttribute(ATTR_USERDATA);
		request.getSession().removeAttribute(TARGET_MODE);
	}



	
	

	
	/**
	 * updateResponse(HttpServletRequest request, String responseId, String updatedResponse)
	 * @param qaService
	 * @param responseId
	 * @param updatedResponse
	 */
	public void updateResponse(IQaService qaService, String responseId, String updatedResponse)
	{		
		logger.debug("load response with responseId: " + new Long(responseId).longValue());
		QaUsrResp qaUsrResp=qaService.retrieveQaUsrResp(new Long(responseId).longValue());   
		logger.debug("loaded user response:  " + qaUsrResp);
		qaUsrResp.setAnswer(updatedResponse);
		qaService.updateQaUsrResp(qaUsrResp);
		logger.debug("updated user response in the db:  " + qaUsrResp);
	}
	
	
	/**
	 * hideResponse(HttpServletRequest request, String responseId)
	 * @param qaService
	 * @param responseId
	 */
	public void hideResponse(IQaService qaService, String responseId)
	{
		logger.debug("load response with responseId for hiding: " + new Long(responseId).longValue());
		QaUsrResp qaUsrResp=qaService.retrieveQaUsrResp(new Long(responseId).longValue());   
		logger.debug("loaded user response:  " + qaUsrResp);
		qaUsrResp.setHidden(true);
		qaService.updateQaUsrResp(qaUsrResp);
		logger.debug("updated user response in the db:  " + qaUsrResp);
	}
	
	
	/**
	 * unHideResponse(HttpServletRequest request, String responseId)
	 * @param request
	 * @param responseId
	 */
	public void unHideResponse(IQaService qaService, String responseId)
	{
		logger.debug("load response with responseId for un-hiding: " + new Long(responseId).longValue());
		QaUsrResp qaUsrResp=qaService.retrieveQaUsrResp(new Long(responseId).longValue());   
		logger.debug("loaded user response:  " + qaUsrResp);
		qaUsrResp.setHidden(false);
		qaService.updateQaUsrResp(qaUsrResp);
		logger.debug("updated user response in the db:  " + qaUsrResp);
	}
	
	
	/**
	 * populates all the tool sessions in a map 
	 * populateToolSessions(HttpServletRequest request, McContent mcContent)
	 * 
	 * @param request
	 * @param mcContent
	 * @return Map
	 */
	public static Map populateToolSessions(HttpServletRequest request, QaContent qaContent, IQaService qaService)
	{
		List sessionsList=qaService.getSessionNamesFromContent(qaContent);
    	logger.debug("sessionsList size is:..." + sessionsList.size());
    	
    	Map sessionsMap=QaUtils.convertToStringMap(sessionsList, "String");
    	logger.debug("generated sessionsMap:..." + sessionsMap);
    	logger.debug("sessionsMap size:..." + sessionsMap.size());
    	
    	if (sessionsMap.isEmpty())
		{
    		logger.debug("sessionsMap size is 0:");
        	sessionsMap.put(new Long(1).toString() , "None");
		}
    	else
    	{
    		logger.debug("sessionsMap has some entries: " +  sessionsMap.size());
    		sessionsMap.put(new Long(sessionsMap.size()+ 1).toString() , "All");	
    	}
    	
    	logger.debug("final sessionsMap:" + sessionsMap);
    	return sessionsMap;
	}
	
	/**
	 * populates all the tool sessions in a map 
	 * populateToolSessions(HttpServletRequest request, McContent mcContent)
	 * 
	 * @param request
	 * @param mcContent
	 * @return Map
	 */
	public static Map populateToolSessionsId(HttpServletRequest request, QaContent qaContent, IQaService qaService)
	{
		List sessionsList=qaService.getSessionsFromContent(qaContent);
    	logger.debug("sessionsList size is:..." + sessionsList.size());
    	
    	Map sessionsMap=QaUtils.convertToStringMap(sessionsList, "Long");
    	logger.debug("generated sessionsMap:..." + sessionsMap);
    	logger.debug("sessionsMap size:..." + sessionsMap.size());
    	
    	if (sessionsMap.isEmpty())
		{
    		logger.debug("sessionsMap size is 0:");
        	sessionsMap.put(new Long(1).toString() , "None");
		}
    	else
    	{
    		logger.debug("sessionsMap has some entries: " +  sessionsMap.size());
    		sessionsMap.put(new Long(sessionsMap.size()+ 1).toString() , "All");	
    	}
    	
    	logger.debug("final sessionsMap:" + sessionsMap);
    	return sessionsMap;
	}
	
	
	/**
	 * ends up populating the attempt history for all the users of all the tool sessions for a content
	 * buildGroupsQuestionData(HttpServletRequest request, McContent mcContent)
	 * 
	 * @param request
	 * @param mcContent
	 * @return List
	 */
	public static List buildGroupsQuestionData(HttpServletRequest request, QaContent qaContent)
	{
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("qaService: " + qaService);
    	
		logger.debug("will be building groups question data  for content:..." + qaContent);
    	List listQuestions=qaService.getAllQuestionEntries(qaContent.getUid());
    	logger.debug("listQuestions:..." + listQuestions);
    	
    	List listMonitoredAnswersContainerDTO= new LinkedList();
    		
		Iterator itListQuestions = listQuestions.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	QaQueContent qaQueContent =(QaQueContent)itListQuestions.next();
	    	logger.debug("mcQueContent:..." + qaQueContent);
	    	
	    	if (qaQueContent != null)
	    	{
	    		QaMonitoredAnswersDTO qaMonitoredAnswersDTO= new QaMonitoredAnswersDTO();
	    		qaMonitoredAnswersDTO.setQuestionUid(qaQueContent.getUid().toString());
	    		qaMonitoredAnswersDTO.setQuestion(qaQueContent.getQuestion());
	    		
				//Map questionAttemptData= buildGroupsAttemptData(request, qaContent, qaQueContent, qaQueContent.getUid().toString());
				//logger.debug("questionAttemptData:..." + questionAttemptData);
				//qaMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(qaMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}
}

