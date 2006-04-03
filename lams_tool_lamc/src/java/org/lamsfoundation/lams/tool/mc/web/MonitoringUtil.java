/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredUserDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;

/**
 * 
 * Keeps all operations needed for Monitoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class MonitoringUtil implements McAppConstants{
	static Logger logger = Logger.getLogger(MonitoringUtil.class.getName());
	
	/**
	 * populates all the tool sessions in a map 
	 * populateToolSessions(HttpServletRequest request, McContent mcContent)
	 * 
	 * @param request
	 * @param mcContent
	 * @return Map
	 */
	public static Map populateToolSessions(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		List sessionsList=mcService.getSessionNamesFromContent(mcContent);
    	logger.debug("sessionsList size is:..." + sessionsList.size());
    	
    	Map sessionsMap=McUtils.convertToStringMap(sessionsList, "String");
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
	public static List buildGroupsQuestionData(HttpServletRequest request, McContent mcContent)
	{
		logger.debug("will be building groups question data  for content:..." + mcContent);
    	IMcService mcService =McUtils.getToolService(request);
    	List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
    	logger.debug("listQuestions:..." + listQuestions);
    	
    	List listMonitoredAnswersContainerDTO= new LinkedList();
    		
		Iterator itListQuestions = listQuestions.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
	    	logger.debug("mcQueContent:..." + mcQueContent);
	    	
	    	if (mcQueContent != null)
	    	{
	    		McMonitoredAnswersDTO mcMonitoredAnswersDTO= new McMonitoredAnswersDTO();
	    		mcMonitoredAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
	    		mcMonitoredAnswersDTO.setQuestion(mcQueContent.getQuestion());
	    		
	    		List listCandidateAnswers=mcService.findMcOptionNamesByQueId(mcQueContent.getUid());
				logger.debug("listCandidateAnswers:..." + listCandidateAnswers);
				mcMonitoredAnswersDTO.setCandidateAnswers(listCandidateAnswers);
				
				Map questionAttemptData= buildGroupsAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid().toString());
				logger.debug("questionAttemptData:..." + questionAttemptData);
				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}
	
	/**
	 * helps populating user's attempt history 
	 * buildGroupsAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcQueContent
	 * @return Map
	 */
	public static Map buildGroupsAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, String questionUid)
	{
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent + " questionUid:" + questionUid);
    	IMcService mcService =McUtils.getToolService(request);
    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
    	
    	Map summaryToolSessions=populateToolSessionsId(request, mcContent, mcService);
    	logger.debug("summaryToolSessions: " + summaryToolSessions);
    	
    	Iterator itMap = summaryToolSessions.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  summary tool sessions pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All")))
            {
            	logger.debug("using the  numerical summary tool sessions pair: " +  " = " + pairs.getValue());
            	McSession mcSession= mcService.findMcSessionById(new Long(pairs.getValue().toString()));
            	logger.debug("mcSession: " +  " = " + mcSession);
            	if (mcSession != null)
            	{
            		List listMcUsers=mcService.getMcUserBySessionOnly(mcSession);	
            		logger.debug("listMcUsers for session id:"  + mcSession.getMcSessionId() +  " = " + listMcUsers);
            		Map sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid);
            		
            		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
            	}
            }
		}

    	logger.debug("final listMonitoredAttemptsContainerDTO:..." + listMonitoredAttemptsContainerDTO);
    	mapMonitoredAttemptsContainerDTO=convertToMap(listMonitoredAttemptsContainerDTO);
    	logger.debug("final mapMonitoredAttemptsContainerDTO:..." + mapMonitoredAttemptsContainerDTO);
		return mapMonitoredAttemptsContainerDTO;
	}


	/**
	 * return total attempt count from all the users
	 * getTotalAttemptCount(HttpServletRequest request)
	 * 
	 * @param request
	 * @return int
	 */
    public static int getTotalAttemptCount(HttpServletRequest request)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	List listMarks=mcService.getMarks();
    	logger.debug("total attempt count: " + listMarks.size());
    	return listMarks.size();
    }
	
	
	/**
	 * ends up populating all the user's attempt data of a  particular tool session  
	 * populateSessionUsersAttempts(HttpServletRequest request,List listMcUsers)
	 * 
	 * @param request
	 * @param listMcUsers
	 * @return List
	 */
	public static Map populateSessionUsersAttempts(HttpServletRequest request,Long sessionId, List listMcUsers, String questionUid)
	{
		logger.debug("will be populating users attempt history for session id: " + sessionId);
		
		IMcService mcService =McUtils.getToolService(request);
		
		Map mapMonitoredUserContainerDTO= new TreeMap(new McStringComparator());
		List listMonitoredUserContainerDTO= new LinkedList();
		
		Iterator itUsers=listMcUsers.iterator();
		while (itUsers.hasNext())
		{
    		McQueUsr mcQueUsr=(McQueUsr)itUsers.next();
    		logger.debug("mcQueUsr: " + mcQueUsr);
    		
    		if (mcQueUsr != null)
    		{
    			logger.debug("getting listUserAttempts for user id: " + mcQueUsr.getUid() + " and que content id: " + questionUid);
    			//List listUserAttempts=mcService.getAttemptsForUser(mcQueUsr.getUid());
    			List listUserAttempts=mcService.getAttemptsForUserAndQuestionContent(mcQueUsr.getUid(), new Long(questionUid));
    			logger.debug("listUserAttempts: " + listUserAttempts);

    			Iterator itAttempts=listUserAttempts.iterator();
    			while (itAttempts.hasNext())
    			{
    	    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itAttempts.next();
    	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
    	    		
    	    		if (mcUsrAttempt != null)
    	    		{
    	    			McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
    	    			mcMonitoredUserDTO.setAttemptTime(mcUsrAttempt.getAttemptTime().toString());
    	    			mcMonitoredUserDTO.setIsCorrect(new Boolean(mcUsrAttempt.isAttemptCorrect()).toString());
    	    			mcMonitoredUserDTO.setResponse(mcUsrAttempt.getMcOptionsContent().getMcQueOptionText().toString());
    	    			mcMonitoredUserDTO.setTimeZone(mcUsrAttempt.getTimeZone());
    	    			mcMonitoredUserDTO.setUid(mcUsrAttempt.getUid().toString());
    	    			mcMonitoredUserDTO.setUserName(mcQueUsr.getUsername());
    	    			mcMonitoredUserDTO.setQueUsrId(mcQueUsr.getUid().toString());
    	    			mcMonitoredUserDTO.setSessionId(sessionId.toString());
    	    			mcMonitoredUserDTO.setQuestionUid(questionUid);
    	    			listMonitoredUserContainerDTO.add(mcMonitoredUserDTO);
    	    		}
    			}
    		}
		}
		logger.debug("final listMonitoredUserContainerDTO: " + listMonitoredUserContainerDTO);
		mapMonitoredUserContainerDTO=convertToMcMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		logger.debug("final mapMonitoredUserContainerDTO:..." + mapMonitoredUserContainerDTO);
		return mapMonitoredUserContainerDTO;
	}
	
	public static Map populateToolSessionsId(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		List sessionsList=mcService.getSessionsFromContent(mcContent);
    	logger.debug("sessionsList size is:..." + sessionsList.size());
    	
    	Map sessionsMap=McUtils.convertToStringMap(sessionsList, "Long");
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

	

	public static Map convertToMap(List list)
	{
		logger.debug("using convertToMap: " + list);
		Map map= new TreeMap(new McStringComparator());
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	
    	while (listIterator.hasNext())
    	{
   			Map data=(Map)listIterator.next();
   			map.put(mapIndex.toString(), data);
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}

	
	public static Map convertToMcMonitoredUserDTOMap(List list)
	{
		logger.debug("using convertToMcMonitoredUserDTOMap: " + list);
		Map map= new TreeMap(new McStringComparator());
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	
    	while (listIterator.hasNext())
    	{
    		McMonitoredUserDTO data=(McMonitoredUserDTO)listIterator.next();
   			map.put(mapIndex.toString(), data);
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}
	
	/**
	 * cleans up the monitoring session
	 * cleanupMonitoringSession(HttpServletRequest request)
	 * 
	 * @param request
	 */
	public static void cleanupMonitoringSession(HttpServletRequest request)
	{
		request.getSession().removeAttribute(USER_ID);
		request.getSession().removeAttribute(TOOL_CONTENT_ID);
		request.getSession().removeAttribute(ACTIVE_MODULE);
		request.getSession().removeAttribute(CURRENT_MONITORING_TAB);
		request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST);
		request.getSession().removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);
		request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS);
		request.getSession().removeAttribute(SELECTION_CASE);
		request.getSession().removeAttribute(CURRENT_MONITORED_TOOL_SESSION);
		request.getSession().removeAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO);
		request.getSession().removeAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
		request.getSession().removeAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
		request.getSession().removeAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY);
		request.getSession().removeAttribute(COUNT_SESSION_COMPLETE);
		request.getSession().removeAttribute(COUNT_MAX_ATTEMPT);
		request.getSession().removeAttribute(TOP_MARK);
		request.getSession().removeAttribute(LOWEST_MARK);
		request.getSession().removeAttribute(AVERAGE_MARK);
		request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);
		request.getSession().removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
		request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
		request.getSession().removeAttribute(DEFINE_LATER_IN_EDIT_MODE);
		request.getSession().removeAttribute(EDIT_OPTIONS_MODE);
	}
    	
}
