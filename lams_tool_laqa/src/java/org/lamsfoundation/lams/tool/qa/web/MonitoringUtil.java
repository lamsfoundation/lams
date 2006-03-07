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
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.qa.QaMonitoredUserDTO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
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
    	
		if (sessionsList.isEmpty())
		{
			/* inform in the Summary tab that the tool has no active sessions */
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

    	
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
	public static List buildGroupsQuestionData(HttpServletRequest request, QaContent qaContent, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("userId: " + userId);
		
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
	    		
	    		logger.debug("using allUsersData to retrieve users data: " + isUserNamesVisible);
				Map questionAttemptData= buildGroupsAttemptData(request, qaContent, qaQueContent, qaQueContent.getUid().toString(), 
						isUserNamesVisible,isLearnerRequest, currentSessionId, userId);
				logger.debug("questionAttemptData:..." + questionAttemptData);
				qaMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(qaMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}
	

	public static Map buildGroupsAttemptData(HttpServletRequest request, QaContent qaContent, QaQueContent qaQueContent, String questionUid, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("currentSessionId: " + currentSessionId);
		logger.debug("userId: " + userId);
		
		logger.debug("doing buildGroupsAttemptData...");
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("qaService: " + qaService);

    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new QaStringComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
    	
    	Map summaryToolSessions=populateToolSessionsId(request, qaContent, qaService);
    	logger.debug("summaryToolSessions: " + summaryToolSessions);
    	
    	Iterator itMap = summaryToolSessions.entrySet().iterator();
    	
    	
    	/*request is for monitoring summary */
    	if (!isLearnerRequest)
    	{
        	while (itMap.hasNext()) 
        	{
            	Map.Entry pairs = (Map.Entry)itMap.next();
                logger.debug("using the  summary tool sessions pair: " +  pairs.getKey() + " = " + pairs.getValue());
                
                if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All")))
                {
                	logger.debug("using the  numerical summary tool sessions pair: " +  " = " + pairs.getValue());
                	QaSession qaSession= qaService.retrieveQaSession(new Long(pairs.getValue().toString()).longValue());
                	logger.debug("qaSession: " +  " = " + qaSession);
                	if (qaSession != null)
                	{
                		List listUsers=qaService.getUserBySessionOnly(qaSession);	
                		logger.debug("listMcUsers for session id:"  + qaSession.getQaSessionId() +  " = " + listUsers);
                		Map sessionUsersAttempts=populateSessionUsersAttempts(request,qaSession.getQaSessionId(), listUsers, questionUid, 
                				isUserNamesVisible, isLearnerRequest, userId);
                		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
                	}
                }
    		}
    	}
    	else
    	{
    		/*request is for learner report, use only the passed tool session in the report*/
    		logger.debug("using currentSessionId for the learner report:"  + currentSessionId);
        	while (itMap.hasNext()) 
        	{
            	Map.Entry pairs = (Map.Entry)itMap.next();
                logger.debug("using the  summary tool sessions pair: " +  pairs.getKey() + " = " + pairs.getValue());
                
                if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All")))
                {
                	logger.debug("using the  numerical summary tool sessions pair: " +  " = " + pairs.getValue());
                	
                	if (currentSessionId.equals(pairs.getValue()))
                	{
                		logger.debug("only using this tool session for the learner report: " +  " = " + pairs.getValue());
                		QaSession qaSession= qaService.retrieveQaSession(new Long(pairs.getValue().toString()).longValue());
                    	logger.debug("qaSession: " +  " = " + qaSession);
                    	if (qaSession != null)
                    	{
                    		List listUsers=qaService.getUserBySessionOnly(qaSession);	
                    		logger.debug("listMcUsers for session id:"  + qaSession.getQaSessionId() +  " = " + listUsers);
                    		Map sessionUsersAttempts=populateSessionUsersAttempts(request,qaSession.getQaSessionId(), listUsers, questionUid, 
                    				isUserNamesVisible, isLearnerRequest, userId);
                    		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
                    	}
                	}
                }
    		}
    	}

    	logger.debug("final listMonitoredAttemptsContainerDTO:..." + listMonitoredAttemptsContainerDTO);
    	mapMonitoredAttemptsContainerDTO=convertToMap(listMonitoredAttemptsContainerDTO);
    	logger.debug("final mapMonitoredAttemptsContainerDTO:..." + mapMonitoredAttemptsContainerDTO);
		return mapMonitoredAttemptsContainerDTO;
	}
	
	
	/**
	 * ends up populating all the user's attempt data of a  particular tool session  
	 * populateSessionUsersAttempts(HttpServletRequest request,List listUsers)
	 * 
	 * @param request
	 * @param listUsers
	 * @return List
	 */
	public static Map populateSessionUsersAttempts(HttpServletRequest request,Long sessionId, List listUsers, String questionUid, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String userId)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("userId: " + userId);
		
		logger.debug("doing populateSessionUsersAttempts...");
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("qaService: " + qaService);
		
		Map mapMonitoredUserContainerDTO= new TreeMap(new QaStringComparator());
		List listMonitoredUserContainerDTO= new LinkedList();
		Iterator itUsers=listUsers.iterator();
		
		
		if (userId == null)
		{
			logger.debug("request is not for learner progress report");
			if ((isUserNamesVisible) && (!isLearnerRequest))
			{
				logger.debug("isUserNamesVisible true, isLearnerRequest false" );
				logger.debug("getting alll the user' data");
				while (itUsers.hasNext())
				{
		    		QaQueUsr qaQueUsr=(QaQueUsr)itUsers.next();
		    		logger.debug("qaQueUsr: " + qaQueUsr);
		    		
		    		if (qaQueUsr != null)
		    		{
		    			logger.debug("getting listUserAttempts for user id: " + qaQueUsr.getUid() + " and que content id: " + questionUid);
		    			List listUserAttempts=qaService.getAttemptsForUserAndQuestionContent(qaQueUsr.getUid(), new Long(questionUid));
		    			logger.debug("listUserAttempts: " + listUserAttempts);
	
		    			Iterator itAttempts=listUserAttempts.iterator();
		    			while (itAttempts.hasNext())
		    			{
		    				QaUsrResp qaUsrResp=(QaUsrResp)itAttempts.next();
		    	    		logger.debug("qaUsrResp: " + qaUsrResp);
		    	    		
		    	    		if (qaUsrResp != null)
		    	    		{
		    	    			QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
		    	    			qaMonitoredUserDTO.setAttemptTime(qaUsrResp.getAttemptTime().toString());
		    	    			qaMonitoredUserDTO.setTimeZone(qaUsrResp.getTimezone());
		    	    			qaMonitoredUserDTO.setUid(qaUsrResp.getResponseId().toString());
		    	    			qaMonitoredUserDTO.setUserName(qaQueUsr.getUsername());
		    	    			qaMonitoredUserDTO.setQueUsrId(qaQueUsr.getUid().toString());
		    	    			qaMonitoredUserDTO.setSessionId(sessionId.toString());
		    	    			qaMonitoredUserDTO.setResponse(qaUsrResp.getAnswer());
		    	    			qaMonitoredUserDTO.setQuestionUid(questionUid);
		    	    			listMonitoredUserContainerDTO.add(qaMonitoredUserDTO);
		    	    		}
		    			}
		    		}
				}
			}
			else if ((isUserNamesVisible) && (isLearnerRequest))
			{
				logger.debug("just populating data normally just like monitoring summary, except that the data is ony for a specific session" );
				logger.debug("isUserNamesVisible true, isLearnerRequest true" );
				String userID= (String)request.getSession().getAttribute(USER_ID);
				logger.debug("userID: " + userID);
				QaQueUsr qaQueUsr=qaService.getQaQueUsrById(new Long(userID).longValue());
				logger.debug("the current user qaQueUsr " + qaQueUsr + " and username: "  + qaQueUsr.getUsername());
							
					while (itUsers.hasNext())
					{
			    		qaQueUsr=(QaQueUsr)itUsers.next();
			    		logger.debug("qaQueUsr: " + qaQueUsr);
			    		
			    		if (qaQueUsr != null)
			    		{
			    			logger.debug("getting listUserAttempts for user id: " + qaQueUsr.getUid() + " and que content id: " + questionUid);
			    			List listUserAttempts=qaService.getAttemptsForUserAndQuestionContent(qaQueUsr.getUid(), new Long(questionUid));
			    			logger.debug("listUserAttempts: " + listUserAttempts);
		
			    			Iterator itAttempts=listUserAttempts.iterator();
			    			while (itAttempts.hasNext())
			    			{
			    				QaUsrResp qaUsrResp=(QaUsrResp)itAttempts.next();
			    	    		logger.debug("qaUsrResp: " + qaUsrResp);
			    	    		
			    	    		if (qaUsrResp != null)
			    	    		{
			    	    			QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
			    	    			qaMonitoredUserDTO.setAttemptTime(qaUsrResp.getAttemptTime().toString());
			    	    			qaMonitoredUserDTO.setTimeZone(qaUsrResp.getTimezone());
			    	    			qaMonitoredUserDTO.setUid(qaUsrResp.getResponseId().toString());
			    	    			qaMonitoredUserDTO.setUserName(qaQueUsr.getUsername());
			    	    			qaMonitoredUserDTO.setQueUsrId(qaQueUsr.getUid().toString());
			    	    			qaMonitoredUserDTO.setSessionId(sessionId.toString());
			    	    			qaMonitoredUserDTO.setResponse(qaUsrResp.getAnswer());
			    	    			qaMonitoredUserDTO.setQuestionUid(questionUid);
			    	    			listMonitoredUserContainerDTO.add(qaMonitoredUserDTO);
			    	    		}
			    			}
			    		}
					}
				}
				else if ((!isUserNamesVisible) && (isLearnerRequest))
				{
					logger.debug("populating data normally exception are for a specific session and other user names are not visible.");				
					logger.debug("isUserNamesVisible false, isLearnerRequest true" );
					logger.debug("getting only current user's data" );
					String userID= (String)request.getSession().getAttribute(USER_ID);
					logger.debug("userID: " + userID);
								
						while (itUsers.hasNext())
						{
							QaQueUsr qaQueUsr=(QaQueUsr)itUsers.next();
				    		logger.debug("qaQueUsr: " + qaQueUsr);
				    		
				    		if (qaQueUsr != null)
				    		{
				    			logger.debug("getting listUserAttempts for user id: " + qaQueUsr.getUid() + " and que content id: " + questionUid);
				    			List listUserAttempts=qaService.getAttemptsForUserAndQuestionContent(qaQueUsr.getUid(), new Long(questionUid));
				    			logger.debug("listUserAttempts: " + listUserAttempts);
		
				    			Iterator itAttempts=listUserAttempts.iterator();
				    			while (itAttempts.hasNext())
				    			{
				    				QaUsrResp qaUsrResp=(QaUsrResp)itAttempts.next();
				    	    		logger.debug("qaUsrResp: " + qaUsrResp);
				    	    		
				    	    		if (qaUsrResp != null)
				    	    		{
				    	    			QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
				    	    			qaMonitoredUserDTO.setAttemptTime(qaUsrResp.getAttemptTime().toString());
				    	    			qaMonitoredUserDTO.setTimeZone(qaUsrResp.getTimezone());
				    	    			qaMonitoredUserDTO.setUid(qaUsrResp.getResponseId().toString());
				    	    			
				    	    			logger.debug("userID versus queUsrId: " + userID + "-" + qaQueUsr.getQueUsrId());
				    	    			if (userID.equals(qaQueUsr.getQueUsrId().toString()))
										{
				    	    				logger.debug("this is current user, put his name normally.");
				    	    				qaMonitoredUserDTO.setUserName(qaQueUsr.getUsername());	
										}
				    	    			else
				    	    			{
				    	    				logger.debug("this is  not current user, put his name as blank.");
				    	    				qaMonitoredUserDTO.setUserName("[        ]");
				    	    			}
				    	    			
				    	    			qaMonitoredUserDTO.setQueUsrId(qaQueUsr.getUid().toString());
				    	    			qaMonitoredUserDTO.setSessionId(sessionId.toString());
				    	    			qaMonitoredUserDTO.setResponse(qaUsrResp.getAnswer());
				    	    			qaMonitoredUserDTO.setQuestionUid(questionUid);
				    	    			listMonitoredUserContainerDTO.add(qaMonitoredUserDTO);
				    	    		}
				    			}
				    		}
						}
				}
		}
		else
		{
				logger.debug("request is for learner progress report: " + userId);
				while (itUsers.hasNext())
				{
					QaQueUsr qaQueUsr=(QaQueUsr)itUsers.next();
		    		logger.debug("qaQueUsr: " + qaQueUsr);
		    		
		    		if (qaQueUsr != null)
		    		{
		    			logger.debug("getting listUserAttempts for user id: " + qaQueUsr.getUid() + " and que content id: " + questionUid);
		    			List listUserAttempts=qaService.getAttemptsForUserAndQuestionContent(qaQueUsr.getUid(), new Long(questionUid));
		    			logger.debug("listUserAttempts: " + listUserAttempts);

		    			Iterator itAttempts=listUserAttempts.iterator();
		    			while (itAttempts.hasNext())
		    			{
		    				QaUsrResp qaUsrResp=(QaUsrResp)itAttempts.next();
		    	    		logger.debug("qaUsrResp: " + qaUsrResp);
		    	    		
		    	    		if (qaUsrResp != null)
		    	    		{
	    	    				logger.debug("userID versus queUsrId: " + userId + "-" + qaQueUsr.getQueUsrId());
		    	    			if (userId.equals(qaQueUsr.getQueUsrId().toString()))
		    	    			{
		    	    				logger.debug("this is the user requested , include his name for learner progress.");
			    	    			QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
			    	    			qaMonitoredUserDTO.setAttemptTime(qaUsrResp.getAttemptTime().toString());
			    	    			qaMonitoredUserDTO.setTimeZone(qaUsrResp.getTimezone());
			    	    			qaMonitoredUserDTO.setUid(qaUsrResp.getResponseId().toString());
		    	    				qaMonitoredUserDTO.setUserName(qaQueUsr.getUsername());	
			    	    			qaMonitoredUserDTO.setQueUsrId(qaQueUsr.getUid().toString());
			    	    			qaMonitoredUserDTO.setSessionId(sessionId.toString());
			    	    			qaMonitoredUserDTO.setResponse(qaUsrResp.getAnswer());
			    	    			qaMonitoredUserDTO.setQuestionUid(questionUid);
			    	    			listMonitoredUserContainerDTO.add(qaMonitoredUserDTO);
		    	    			}
		    	    		}
		    			}
		    		}
				}	
			
		}
		
		
		logger.debug("final listMonitoredUserContainerDTO: " + listMonitoredUserContainerDTO);
		mapMonitoredUserContainerDTO=convertToMcMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		logger.debug("final mapMonitoredUserContainerDTO:..." + mapMonitoredUserContainerDTO);
		return mapMonitoredUserContainerDTO;
	}

	public static Map convertToMcMonitoredUserDTOMap(List list)
	{
		logger.debug("using convertToQaMonitoredUserDTOMap: " + list);
		Map map= new TreeMap(new QaStringComparator());
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	
    	while (listIterator.hasNext())
    	{
    		QaMonitoredUserDTO data=(QaMonitoredUserDTO)listIterator.next();
   			map.put(mapIndex.toString(), data);
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}

	
	public static Map convertToMap(List list)
	{
		logger.debug("using convertToMap: " + list);
		Map map= new TreeMap(new QaStringComparator());
		
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
	
}


