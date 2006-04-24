/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.vote.web;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.VoteStringComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;

/**
 * 
 * Keeps all operations needed for Monitoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class MonitoringUtil implements VoteAppConstants{
	static Logger logger = Logger.getLogger(MonitoringUtil.class.getName());
	
	public static Map populateToolSessions(HttpServletRequest request, VoteContent voteContent, IVoteService voteService)
	{
		List sessionsList=voteService.getSessionNamesFromContent(voteContent);
    	logger.debug("sessionsList size is:..." + sessionsList.size());
    	
    	Map sessionsMap=VoteUtils.convertToStringMap(sessionsList, "String");
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

	public static List buildGroupsQuestionData(HttpServletRequest request, VoteContent voteContent, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("userId: " + userId);
		
		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
    	
		logger.debug("will be building groups question data  for content:..." + voteContent);
    	List listQuestions=voteService.getAllQuestionEntries(voteContent.getUid());
    	logger.debug("listQuestions:..." + listQuestions);
    	
    	List listMonitoredAnswersContainerDTO= new LinkedList();
    		
		Iterator itListQuestions = listQuestions.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	VoteQueContent voteQueContent =(VoteQueContent)itListQuestions.next();
	    	logger.debug("voteQueContent:..." + voteQueContent);
	    	
	    	if (voteQueContent != null)
	    	{
	    		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO= new VoteMonitoredAnswersDTO();
	    		voteMonitoredAnswersDTO.setQuestionUid(voteQueContent.getUid().toString());
	    		voteMonitoredAnswersDTO.setQuestion(voteQueContent.getQuestion());
	    		
	    		logger.debug("using allUsersData to retrieve users data: " + isUserNamesVisible);
				Map questionAttemptData= buildGroupsAttemptData(request, voteContent, voteQueContent, voteQueContent.getUid().toString(), 
						isUserNamesVisible,isLearnerRequest, currentSessionId, userId);
				logger.debug("questionAttemptData:..." + questionAttemptData);
				voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}
	

	public static Map buildGroupsAttemptData(HttpServletRequest request, VoteContent voteContent, VoteQueContent voteQueContent, String questionUid, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("currentSessionId: " + currentSessionId);
		logger.debug("userId: " + userId);
		
		logger.debug("doing buildGroupsAttemptData...");
		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("voteService: " + voteService);

    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new VoteComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
    	
    	Map summaryToolSessions=populateToolSessionsId(request, voteContent, voteService);
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
                	VoteSession voteSession= voteService.retrieveVoteSession(new Long(pairs.getValue().toString()));
                	logger.debug("voteSession: " +  " = " + voteSession);
                	if (voteSession != null)
                	{
                		List listUsers=voteService.getUserBySessionOnly(voteSession);	
                		logger.debug("listMcUsers for session id:"  + voteSession.getVoteSessionId() +  " = " + listUsers);
                		Map sessionUsersAttempts=populateSessionUsersAttempts(request,voteSession.getVoteSessionId(), listUsers, questionUid, 
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
                		VoteSession voteSession= voteService.retrieveVoteSession(new Long(pairs.getValue().toString()));
                    	logger.debug("voteSession: " +  " = " + voteSession);
                    	if (voteSession != null)
                    	{
                    		List listUsers=voteService.getUserBySessionOnly(voteSession);	
                    		logger.debug("listMcUsers for session id:"  + voteSession.getVoteSessionId() +  " = " + listUsers);
                    		Map sessionUsersAttempts=populateSessionUsersAttempts(request,voteSession.getVoteSessionId(), listUsers, questionUid, 
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
	
	
	public static Map populateSessionUsersAttempts(HttpServletRequest request,Long sessionId, List listUsers, String questionUid, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String userId)
	{
	    logger.debug("doing populateSessionUsersAttempts for: " +questionUid);
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("userId: " + userId);
		
		logger.debug("doing populateSessionUsersAttempts...");
		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("voteService: " + voteService);
		
		Map mapMonitoredUserContainerDTO= new TreeMap(new VoteStringComparator());
		List listMonitoredUserContainerDTO= new LinkedList();
		Iterator itUsers=listUsers.iterator();
		
		
		if (userId == null)
		{
			logger.debug("request is not for learner progress report");
			if ((isUserNamesVisible) && (!isLearnerRequest))
			{
			    logger.debug("summary reporting case 1" );
				logger.debug("isUserNamesVisible true, isLearnerRequest false" );
				logger.debug("getting alll the user' data");
				while (itUsers.hasNext())
				{
		    		VoteQueUsr voteQueUsr=(VoteQueUsr)itUsers.next();
		    		logger.debug("voteQueUsr: " + voteQueUsr);
		    		
		    		if (voteQueUsr != null)
		    		{
		    			logger.debug("getting listUserAttempts for user id: " + voteQueUsr.getUid() + " and que content id: " + questionUid);
		    			List listUserAttempts=voteService.getAttemptsListForUserAndQuestionContent(voteQueUsr.getUid(), new Long(questionUid));
		    			logger.debug("listUserAttempts: " + listUserAttempts);
	
		    			Iterator itAttempts=listUserAttempts.iterator();
		    			while (itAttempts.hasNext())
		    			{
		    			    VoteUsrAttempt voteUsrResp=(VoteUsrAttempt)itAttempts.next();
		    	    		logger.debug("voteUsrResp: " + voteUsrResp);
		    	    		
		    	    		if (voteUsrResp != null)
		    	    		{
		    	    			VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
		    	    			voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
		    	    			voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
		    	    			//voteMonitoredUserDTO.setUid(voteUsrResp.getResponseId().toString());
		    	    			voteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
		    	    			voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
		    	    			voteMonitoredUserDTO.setSessionId(sessionId.toString());
		    	    			voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
		    	    			
		    	    			logger.debug("attempt: " + voteUsrResp);
		    	            	VoteQueContent localVoteQueContent=voteUsrResp.getVoteQueContent();
		    	            	logger.debug("localVoteQueContent: " + localVoteQueContent);        	
		    	            	logger.debug("localVoteQueContent question : " + localVoteQueContent.getQuestion());
		    	            	voteMonitoredUserDTO.setResponse(localVoteQueContent.getQuestion());
		    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
		    	            	
		    	            	
		    	            	boolean isSingleUserEntry=voteUsrResp.isSingleUserEntry();
		    	            	logger.debug("isSingleUserEntry: " + isSingleUserEntry);
		    	            	logger.debug("userEntry: " + voteUsrResp.getUserEntry());
		    	            	if ((isSingleUserEntry == false) && (voteUsrResp.getUserEntry().length() > 0))
		    	            	{
		    	            	    logger.debug("userEntry available and must be added " + voteUsrResp.getUserEntry());
		    	            	    VoteMonitoredUserDTO userEntryVoteMonitoredUserDTO = new VoteMonitoredUserDTO();

		    	            	    userEntryVoteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
		    	            	    userEntryVoteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
		    	            	    userEntryVoteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
		    	            	    userEntryVoteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
		    	            	    userEntryVoteMonitoredUserDTO.setSessionId(sessionId.toString());
		    	            	    voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());

		    	            	    userEntryVoteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
			    	    			listMonitoredUserContainerDTO.add(userEntryVoteMonitoredUserDTO);
		    	            	}
		    	    			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
		    	    		}
		    			}
		    		}
				}
			}
			else if ((isUserNamesVisible) && (isLearnerRequest))
			{
			    logger.debug("summary reporting case 2" );
				logger.debug("just populating data normally just like monitoring summary, except that the data is ony for a specific session" );
				logger.debug("isUserNamesVisible true, isLearnerRequest true" );
				String userID= (String)request.getSession().getAttribute(USER_ID);
				logger.debug("userID: " + userID);
				VoteQueUsr voteQueUsr=voteService.getVoteQueUsrById(new Long(userID).longValue());
				logger.debug("the current user voteQueUsr " + voteQueUsr + " and username: "  + voteQueUsr.getUsername());
							
					while (itUsers.hasNext())
					{
			    		voteQueUsr=(VoteQueUsr)itUsers.next();
			    		logger.debug("voteQueUsr: " + voteQueUsr);
			    		
			    		if (voteQueUsr != null)
			    		{
			    			logger.debug("getting listUserAttempts for user id: " + voteQueUsr.getUid() + " and que content id: " + questionUid);
			    			List listUserAttempts=voteService.getAttemptsListForUserAndQuestionContent(voteQueUsr.getUid(), new Long(questionUid));
			    			logger.debug("listUserAttempts: " + listUserAttempts);
		
			    			Iterator itAttempts=listUserAttempts.iterator();
			    			while (itAttempts.hasNext())
			    			{
			    			    VoteUsrAttempt voteUsrResp=(VoteUsrAttempt)itAttempts.next();
			    	    		logger.debug("voteUsrResp: " + voteUsrResp);
			    	    		
			    	    		if (voteUsrResp != null)
			    	    		{
			    	    			VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
			    	    			voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
			    	    			voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
			    	    			voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
			    	    			voteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
			    	    			voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
			    	    			voteMonitoredUserDTO.setSessionId(sessionId.toString());
			    	    			voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
			    	    			//voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
			    	    			
			    	    			logger.debug("attempt: " + voteUsrResp);
			    	            	VoteQueContent localVoteQueContent=voteUsrResp.getVoteQueContent();
			    	            	logger.debug("localVoteQueContent: " + localVoteQueContent);        	
			    	            	logger.debug("localVoteQueContent question : " + localVoteQueContent.getQuestion());
			    	            	voteMonitoredUserDTO.setResponse(localVoteQueContent.getQuestion());
			    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
			    	            	
			    	            	boolean isSingleUserEntry=voteUsrResp.isSingleUserEntry();
			    	            	logger.debug("isSingleUserEntry: " + isSingleUserEntry);
			    	            	logger.debug("userEntry: " + voteUsrResp.getUserEntry());
			    	            	if ((isSingleUserEntry == false) && (voteUsrResp.getUserEntry().length() > 0))
			    	            	{
			    	            	    logger.debug("userEntry available and must be added " + voteUsrResp.getUserEntry());
			    	            	    VoteMonitoredUserDTO userEntryVoteMonitoredUserDTO = new VoteMonitoredUserDTO();

			    	            	    userEntryVoteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
			    	            	    userEntryVoteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
			    	            	    userEntryVoteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
			    	            	    userEntryVoteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
			    	            	    userEntryVoteMonitoredUserDTO.setSessionId(sessionId.toString());
			    	            	    voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());

			    	            	    userEntryVoteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
				    	    			listMonitoredUserContainerDTO.add(userEntryVoteMonitoredUserDTO);
			    	            	}
			    	    			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    	    		}
			    			}
			    		}
					}
				}
				else if ((!isUserNamesVisible) && (isLearnerRequest))
				{
				    logger.debug("summary reporting case 3" );
					logger.debug("populating data normally exception are for a specific session and other user names are not visible.");				
					logger.debug("isUserNamesVisible false, isLearnerRequest true" );
					logger.debug("getting only current user's data" );
					String userID= (String)request.getSession().getAttribute(USER_ID);
					logger.debug("userID: " + userID);
								
						while (itUsers.hasNext())
						{
							VoteQueUsr voteQueUsr=(VoteQueUsr)itUsers.next();
				    		logger.debug("voteQueUsr: " + voteQueUsr);
				    		
				    		if (voteQueUsr != null)
				    		{
				    			logger.debug("getting listUserAttempts for user id: " + voteQueUsr.getUid() + " and que content id: " + questionUid);
				    			List listUserAttempts=voteService.getAttemptsListForUserAndQuestionContent(voteQueUsr.getUid(), new Long(questionUid));
				    			logger.debug("listUserAttempts: " + listUserAttempts);
		
				    			Iterator itAttempts=listUserAttempts.iterator();
				    			while (itAttempts.hasNext())
				    			{
				    			    VoteUsrAttempt voteUsrResp=(VoteUsrAttempt)itAttempts.next();
				    	    		logger.debug("voteUsrResp: " + voteUsrResp);
				    	    		
				    	    		if (voteUsrResp != null)
				    	    		{
				    	    			VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
				    	    			voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
				    	    			voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
				    	    			voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
				    	    			
				    	    			logger.debug("userID versus queUsrId: " + userID + "-" + voteQueUsr.getQueUsrId());
				    	    			if (userID.equals(voteQueUsr.getQueUsrId().toString()))
										{
				    	    				logger.debug("this is current user, put his name normally.");
				    	    				voteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());	
										}
				    	    			else
				    	    			{
				    	    				logger.debug("this is  not current user, put his name as blank.");
				    	    				voteMonitoredUserDTO.setUserName("[        ]");
				    	    			}
				    	    			
				    	    			voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				    	    			voteMonitoredUserDTO.setSessionId(sessionId.toString());
				    	    			//voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

				    	    			logger.debug("attempt: " + voteUsrResp);
				    	            	VoteQueContent localVoteQueContent=voteUsrResp.getVoteQueContent();
				    	            	logger.debug("localVoteQueContent: " + localVoteQueContent);        	
				    	            	logger.debug("localVoteQueContent question : " + localVoteQueContent.getQuestion());
				    	            	voteMonitoredUserDTO.setResponse(localVoteQueContent.getQuestion());
				    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
				    	            	voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
				    	            	
				    	            	boolean isSingleUserEntry=voteUsrResp.isSingleUserEntry();
				    	            	logger.debug("isSingleUserEntry: " + isSingleUserEntry);
				    	            	logger.debug("userEntry: " + voteUsrResp.getUserEntry());
				    	            	if ((isSingleUserEntry == false) && (voteUsrResp.getUserEntry().length() > 0))
				    	            	{
				    	            	    logger.debug("userEntry available and must be added " + voteUsrResp.getUserEntry());
				    	            	    VoteMonitoredUserDTO userEntryVoteMonitoredUserDTO = new VoteMonitoredUserDTO();

				    	            	    userEntryVoteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
				    	            	    userEntryVoteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
				    	            	    userEntryVoteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
				    	            	    userEntryVoteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				    	            	    userEntryVoteMonitoredUserDTO.setSessionId(sessionId.toString());
				    	            	    voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());

				    	            	    userEntryVoteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
					    	    			listMonitoredUserContainerDTO.add(userEntryVoteMonitoredUserDTO);
				    	            	}
				    	    			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);

				    	    		}
				    			}
				    		}
						}
				}
		}
		else
		{
		    	logger.debug("summary reporting case 4" );
				logger.debug("request is for learner progress report: " + userId);
				while (itUsers.hasNext())
				{
					VoteQueUsr voteQueUsr=(VoteQueUsr)itUsers.next();
		    		logger.debug("voteQueUsr: " + voteQueUsr);
		    		
		    		if (voteQueUsr != null)
		    		{
		    			logger.debug("getting listUserAttempts for user id: " + voteQueUsr.getUid() + " and que content id: " + questionUid);
		    			List listUserAttempts=voteService.getAttemptsListForUserAndQuestionContent(voteQueUsr.getUid(), new Long(questionUid));
		    			logger.debug("listUserAttempts: " + listUserAttempts);

		    			Iterator itAttempts=listUserAttempts.iterator();
		    			while (itAttempts.hasNext())
		    			{
		    			    VoteUsrAttempt voteUsrResp=(VoteUsrAttempt)itAttempts.next();
		    	    		logger.debug("voteUsrResp: " + voteUsrResp);
		    	    		
		    	    		if (voteUsrResp != null)
		    	    		{
	    	    				logger.debug("userID versus queUsrId: " + userId + "-" + voteQueUsr.getQueUsrId());
		    	    			if (userId.equals(voteQueUsr.getQueUsrId().toString()))
		    	    			{
		    	    				logger.debug("this is the user requested , include his name for learner progress.");
			    	    			VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
			    	    			voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
			    	    			voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
			    	    			voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
		    	    				voteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());	
			    	    			voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
			    	    			voteMonitoredUserDTO.setSessionId(sessionId.toString());
			    	    			voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
			    	    			//voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

			    	    			logger.debug("attempt: " + voteUsrResp);
			    	            	VoteQueContent localVoteQueContent=voteUsrResp.getVoteQueContent();
			    	            	logger.debug("localVoteQueContent: " + localVoteQueContent);        	
			    	            	logger.debug("localVoteQueContent question : " + localVoteQueContent.getQuestion());
			    	            	voteMonitoredUserDTO.setResponse(localVoteQueContent.getQuestion());
			    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
			    	            	
			    	            	boolean isSingleUserEntry=voteUsrResp.isSingleUserEntry();
			    	            	logger.debug("isSingleUserEntry: " + isSingleUserEntry);
			    	            	logger.debug("userEntry: " + voteUsrResp.getUserEntry());
			    	            	if ((isSingleUserEntry == false) && (voteUsrResp.getUserEntry().length() > 0))
			    	            	{
			    	            	    logger.debug("userEntry available and must be added " + voteUsrResp.getUserEntry());
			    	            	    VoteMonitoredUserDTO userEntryVoteMonitoredUserDTO = new VoteMonitoredUserDTO();

			    	            	    userEntryVoteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime().toString());
			    	            	    userEntryVoteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
			    	            	    userEntryVoteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
			    	            	    userEntryVoteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
			    	            	    userEntryVoteMonitoredUserDTO.setSessionId(sessionId.toString());
			    	            	    voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());

			    	            	    userEntryVoteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
				    	    			listMonitoredUserContainerDTO.add(userEntryVoteMonitoredUserDTO);
			    	            	}
			    	    			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
		    	    			}
		    	    		}
		    			}
		    		}
				}	
			
		}
		
		
		logger.debug("final listMonitoredUserContainerDTO: " + listMonitoredUserContainerDTO);
		mapMonitoredUserContainerDTO=convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		logger.debug("final mapMonitoredUserContainerDTO:..." + mapMonitoredUserContainerDTO);
		return mapMonitoredUserContainerDTO;
	}
	

	
	public static Map populateToolSessionsId(HttpServletRequest request, VoteContent voteContent, IVoteService voteService)
	{
	    logger.debug("attempt populateToolSessionsId for: " + voteContent);
		List sessionsList=voteService.getSessionsFromContent(voteContent);
    	logger.debug("sessionsList size is:..." + sessionsList.size());
    	
    	Map sessionsMap=VoteUtils.convertToStringMap(sessionsList, "Long");
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

	public static Map convertToVoteMonitoredUserDTOMap(List list)
	{
		logger.debug("using convertToVoteMonitoredUserDTOMap: " + list);
		Map map= new TreeMap(new VoteComparator());
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	
    	while (listIterator.hasNext())
    	{
    		VoteMonitoredUserDTO data=(VoteMonitoredUserDTO)listIterator.next();
    		logger.debug("using data: " + data);
    		logger.debug("using data: " + data.getResponse());
    		logger.debug("using session id: " + data.getSessionId());
    		logger.debug("using question uid: " + data.getQuestionUid()) ;
    		
    		
   			map.put(mapIndex.toString(), data);
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}

	
	public static Map convertToMap(List list)
	{
		logger.debug("using convertToMap: " + list);
		Map map= new TreeMap(new VoteComparator());
		
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
