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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.VoteAllSessionsDTO;
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
 * <p> Keeps all operations needed for Monitoring mode. </p>
 *  
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

	/**
	 * 
	 * used in presenting user votes data 
	 * 
	 * @param request
	 * @param voteContent
	 * @param isUserNamesVisible
	 * @param isLearnerRequest
	 * @param currentSessionId
	 * @param userId
	 * @param voteService
	 * @return
	 */
	public static List buildGroupsQuestionData(HttpServletRequest request, VoteContent voteContent, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, IVoteService voteService)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("userId: " + userId);
		
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
						isUserNamesVisible,isLearnerRequest, currentSessionId, userId, voteService);
				logger.debug("questionAttemptData:..." + questionAttemptData);
				voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}
	

	/**
	 * groups user's votes
	 * 
	 * @param request
	 * @param voteContent
	 * @param voteQueContent
	 * @param questionUid
	 * @param isUserNamesVisible
	 * @param isLearnerRequest
	 * @param currentSessionId
	 * @param userId
	 * @param voteService
	 * @return
	 */
	public static Map buildGroupsAttemptData(HttpServletRequest request, VoteContent voteContent, VoteQueContent voteQueContent, String questionUid, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, IVoteService voteService)
	{
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("currentSessionId: " + currentSessionId);
		logger.debug("userId: " + userId);
		
		logger.debug("doing buildGroupsAttemptData...");
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
                		Map sessionUsersAttempts=populateSessionUsersAttempts(request,voteContent, voteSession.getVoteSessionId(), listUsers, questionUid, 
                				isUserNamesVisible, isLearnerRequest, userId, voteService);
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
                    		Map sessionUsersAttempts=populateSessionUsersAttempts(request,voteContent, voteSession.getVoteSessionId(), listUsers, questionUid, 
                    				isUserNamesVisible, isLearnerRequest, userId, voteService);
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
	
	
	public static Map populateSessionUsersAttempts(HttpServletRequest request, VoteContent voteContent, Long sessionId, List listUsers, String questionUid, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String userId, IVoteService voteService)
	{
	    logger.debug("doing populateSessionUsersAttempts for: " +questionUid);
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		logger.debug("voteContent: " + voteContent);
		logger.debug("userId: " + userId);
		
		logger.debug("doing populateSessionUsersAttempts...");
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
		    	    			voteMonitoredUserDTO.setUserName(voteQueUsr.getUsername());
		    	    			voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
		    	    			voteMonitoredUserDTO.setSessionId(sessionId.toString());
		    	    			voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
		    	    			
		    	    			logger.debug("attempt: " + voteUsrResp);
		    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
		    	            	
		    	            	VoteQueContent voteQueContent=voteUsrResp.getVoteQueContent();
		    		        	logger.debug("voteQueContent: " + voteQueContent);        
		    	            	String entry=voteQueContent.getQuestion(); 
		    	    		    logger.debug("entry: " + entry);

		    	    		    String voteQueContentId=voteUsrResp.getVoteQueContentId().toString();	    
		    	    		    logger.debug("voteQueContentId: " + voteQueContentId);
		    	    		    
			    	    	    VoteSession localUserSession=voteUsrResp.getVoteQueUsr().getVoteSession();
			    	    	    logger.debug("localUserSession: " + localUserSession);
			    	    	    logger.debug("localUserSession's content id: " + localUserSession.getVoteContentId()); 
			    	    	    logger.debug("incoming content id versus localUserSession's content id: " + voteContent.getVoteContentId() + " versus " +  localUserSession.getVoteContentId());
			    	    	    logger.debug("summary reporting case 1" );
			    	    	    if (voteContent.getVoteContentId().toString().equals(localUserSession.getVoteContentId().toString()))
			    	    	    {
			    	    		    if (entry != null)
			    	    		    {
			    	    		        if (entry.equals("sample nomination")  &&  (voteQueContentId.equals("1")))
			    		    		    {
			    		    		        logger.debug("this nomination entry points to a user entered nomination: " + voteUsrResp.getUserEntry());
			    		    		        voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
			    		    		    }
			    		    		    else
			    		    		    {
			    		    		        logger.debug("this nomination entry points to a standard nomination: " + voteQueContent.getQuestion());
			    		    		        voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());    
			    		    		    }
			    	    		    }
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
			    	    			logger.debug("attempt: " + voteUsrResp);
			    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
			    	            	
			    	            	VoteQueContent voteQueContent=voteUsrResp.getVoteQueContent();
			    		        	logger.debug("voteQueContent: " + voteQueContent);        
			    	            	String entry=voteQueContent.getQuestion(); 
			    	    		    logger.debug("entry: " + entry);
			    	    		    String voteQueContentId=voteUsrResp.getVoteQueContentId().toString();	    
			    	    		    logger.debug("voteQueContentId: " + voteQueContentId);

				    	    	    VoteSession localUserSession=voteUsrResp.getVoteQueUsr().getVoteSession();
				    	    	    logger.debug("localUserSession: " + localUserSession);
				    	    	    logger.debug("localUserSession's content id: " + localUserSession.getVoteContentId()); 
				    	    	    logger.debug("incoming content id versus localUserSession's content id: " + voteContent.getVoteContentId() + " versus " +  localUserSession.getVoteContentId());
				    			    logger.debug("summary reporting case 2" );
				    	    	    if (voteContent.getVoteContentId().toString().equals(localUserSession.getVoteContentId().toString()))
				    	    	    {
				    	    		    if (entry != null)
				    	    		    {
				    	    		        if (entry.equals("sample nomination")  &&  (voteQueContentId.equals("1")))
				    		    		    {
				    		    		        logger.debug("this nomination entry points to a user entered nomination: " + voteUsrResp.getUserEntry());
				    		    		        voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
				    		    		    }
				    		    		    else
				    		    		    {
				    		    		        logger.debug("this nomination entry points to a standard nomination: " + voteQueContent.getQuestion());
				    		    		        voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());    
				    		    		    }
				    	    		    }
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

				    	    			logger.debug("attempt: " + voteUsrResp);
				    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
				    	            	voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
				    	            	
				    	            	VoteQueContent voteQueContent=voteUsrResp.getVoteQueContent();
				    		        	logger.debug("voteQueContent: " + voteQueContent);        
				    	            	String entry=voteQueContent.getQuestion(); 
				    	    		    logger.debug("entry: " + entry);

				    	    		    String voteQueContentId=voteUsrResp.getVoteQueContentId().toString();	    
				    	    		    logger.debug("voteQueContentId: " + voteQueContentId);
				    	    		    
					    	    	    VoteSession localUserSession=voteUsrResp.getVoteQueUsr().getVoteSession();
					    	    	    logger.debug("localUserSession: " + localUserSession);
					    	    	    logger.debug("localUserSession's content id: " + localUserSession.getVoteContentId()); 
					    	    	    logger.debug("incoming content id versus localUserSession's content id: " + voteContent.getVoteContentId() + " versus " +  localUserSession.getVoteContentId());
									    logger.debug("summary reporting case 3" );					    	    	    
					    	    	    if (voteContent.getVoteContentId().toString().equals(localUserSession.getVoteContentId().toString()))
					    	    	    {
					    	    		    if (entry != null)
					    	    		    {
					    	    		        if (entry.equals("sample nomination")  &&  (voteQueContentId.equals("1")))
					    		    		    {
					    		    		        logger.debug("this nomination entry points to a user entered nomination: " + voteUsrResp.getUserEntry());
					    		    		        voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
					    		    		    }
					    		    		    else
					    		    		    {
					    		    		        logger.debug("this nomination entry points to a standard nomination: " + voteQueContent.getQuestion());
					    		    		        voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());    
					    		    		    }
					    	    		    }
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
			    	    			logger.debug("attempt: " + voteUsrResp);
			    	            	voteMonitoredUserDTO.setQuestionUid(questionUid);
			    	            	
			    	            	
			    	            	VoteQueContent voteQueContent=voteUsrResp.getVoteQueContent();
			    		        	logger.debug("voteQueContent: " + voteQueContent);        
			    	            	String entry=voteQueContent.getQuestion(); 
			    	    		    logger.debug("entry: " + entry);

			    	    		    String voteQueContentId=voteUsrResp.getVoteQueContentId().toString();	    
			    	    		    logger.debug("voteQueContentId: " + voteQueContentId);
			    	    		    
				    	    	    VoteSession localUserSession=voteUsrResp.getVoteQueUsr().getVoteSession();
				    	    	    logger.debug("localUserSession: " + localUserSession);
				    	    	    logger.debug("localUserSession's content id: " + localUserSession.getVoteContentId()); 
				    	    	    logger.debug("incoming content id versus localUserSession's content id: " + voteContent.getVoteContentId() + " versus " +  localUserSession.getVoteContentId());
				    		    	logger.debug("summary reporting case 4" );
				    	    	    if (voteContent.getVoteContentId().toString().equals(localUserSession.getVoteContentId().toString()))
				    	    	    {
				    	    		    if (entry != null)
				    	    		    {
				    	    		        if (entry.equals("sample nomination")  &&  (voteQueContentId.equals("1")))
				    		    		    {
				    		    		        logger.debug("this nomination entry points to a user entered nomination: " + voteUsrResp.getUserEntry());
				    		    		        voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
				    		    		    }
				    		    		    else
				    		    		    {
				    		    		        logger.debug("this nomination entry points to a standard nomination: " + voteQueContent.getQuestion());
				    		    		        voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());    
				    		    		    }
				    	    		    }
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

	
	public static double calculateTotal(Map mapVoteRatesContent)
	{
	    logger.debug("calculating total for: " +  mapVoteRatesContent);
	    double total=0d;
		Iterator itMap = mapVoteRatesContent.entrySet().iterator();
		while (itMap.hasNext()) {
	    	Map.Entry pairs = (Map.Entry)itMap.next();
	        logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        
	        if (pairs.getValue() != null) 
	        {
	            total=total+ new Double(pairs.getValue().toString()).doubleValue();
	        }
	        logger.debug("total: " + total);
		}
		return total;
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

	/**
	 * Generates JFreeChart data for all sessions in the Monitoring Summary. 
	 * @param request
	 * @param voteService
	 * @param voteMonitoringForm
	 * @param toolContentId
	 * @return 
	 */
	public static List prepareChartDTO(HttpServletRequest request, IVoteService voteService, VoteMonitoringForm voteMonitoringForm, Long toolContentId)
	{
	    logger.debug("start preparing ChartDTO with voteMonitoringForm: " + voteMonitoringForm);
	    logger.debug("start preparing ChartDTO with toolContentId: " + toolContentId);
	    
	    List listVoteAllSessionsDTO= new LinkedList();

	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);
	    logger.debug("will be building groups question data  for content:..." + voteContent);
    	
		Iterator itListSessions = voteContent.getVoteSessions().iterator();
	    while (itListSessions.hasNext())
	    {
	    	VoteSession voteSession =(VoteSession)itListSessions.next();
	    	logger.debug("voteSession:..." + voteSession);
	    	logger.debug("current voteSession id :..." + voteSession.getVoteSessionId());
	    	logger.debug("current toolSessionUid :..." + voteSession.getUid());

			Map mapOptionsContent= new TreeMap(new VoteComparator());
			logger.debug("mapOptionsContent: " + mapOptionsContent);

			Map mapVoteRatesContent= new TreeMap(new VoteComparator());
			logger.debug("mapVoteRatesContent: " + mapVoteRatesContent);

	    	VoteAllSessionsDTO voteAllSessionsDTO= new VoteAllSessionsDTO();
	    	voteAllSessionsDTO.setSessionId(voteSession.getVoteSessionId().toString());
	    	
		    int entriesCount=voteService.getSessionEntriesCount(voteSession.getUid());
		    logger.debug("entriesCount: " + entriesCount);
		    Set userEntries=voteService.getSessionUserEntriesSet(voteSession.getUid());
		    logger.debug("sessionUserCount: " + userEntries.size());

	        int potentialUserCount=voteService.getVoteSessionPotentialLearnersCount(voteSession.getUid());
	        logger.debug("potentialUserCount: " + potentialUserCount);
	        voteAllSessionsDTO.setSessionUserCount(Integer.toString(potentialUserCount));
	        
		    int completedSessionUserCount=voteService.getCompletedVoteUserBySessionUid(voteSession.getUid());
		    logger.debug("completedSessionUserCount: " + completedSessionUserCount);
	        voteAllSessionsDTO.setCompletedSessionUserCount(new Integer(completedSessionUserCount).toString());
	        
	        if (potentialUserCount != 0)
	        {
	            double completedPercent=(completedSessionUserCount*100) / potentialUserCount;
	            logger.debug("completed percent: " + completedPercent);
	            voteAllSessionsDTO.setCompletedSessionUserPercent(new Double(completedPercent).toString());
	        }
	        else
	        {
	            voteAllSessionsDTO.setCompletedSessionUserPercent("Not Available");
	        }
	        
	        
			logger.debug("entriesCount: " + entriesCount);
			logger.debug("userEntries: " + userEntries);
			
			Map mapStandardUserCount= new TreeMap(new VoteComparator());

			logger.debug("setting existing content data from the db");
			mapOptionsContent.clear();
			Iterator queIterator=voteContent.getVoteQueContents().iterator();
			Long mapIndex=new Long(1);
			logger.debug("mapOptionsContent: " + mapOptionsContent);
			int totalStandardVotesCount=0;
			
			logger.debug("using entriesCount: " + entriesCount);
			Map mapStandardNominationsHTMLedContent= new TreeMap(new VoteComparator());
			while (queIterator.hasNext())
			{
				VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
				if (voteQueContent != null)
				{
					logger.debug("question: " + voteQueContent.getQuestion());
					mapStandardNominationsHTMLedContent.put(mapIndex.toString(),voteQueContent.getQuestion());
					String noHTMLNomination = VoteUtils.stripHTML(voteQueContent.getQuestion());
				    logger.debug("noHTMLNomination: " + noHTMLNomination);
					mapOptionsContent.put(mapIndex.toString(),noHTMLNomination);
					
					int votesCount=0;
				    logger.debug("getting votesCount based on session: " + voteSession.getUid());
					votesCount=voteService.getStandardAttemptsForQuestionContentAndSessionUid(voteQueContent.getUid(), voteSession.getUid());
					logger.debug("votesCount for questionContent uid: " + votesCount + " for" + voteQueContent.getUid());
					mapStandardUserCount.put(mapIndex.toString(),new Integer(votesCount).toString());
					totalStandardVotesCount=totalStandardVotesCount + votesCount;

					
					double voteRate=0d;
					if (entriesCount != 0)
					{
					    voteRate=((votesCount * 100)/ entriesCount);
					}

					logger.debug("voteRate" + voteRate);
					
					mapVoteRatesContent.put(mapIndex.toString(), new Double(voteRate).toString());
		    		mapIndex=new Long(mapIndex.longValue()+1);
				}
			}
			logger.debug("test1: Map initialized with existing contentid to: " + mapOptionsContent);
			Map mapStandardNominationsContent= new TreeMap(new VoteComparator());
			mapStandardNominationsContent=mapOptionsContent;
			logger.debug("mapStandardNominationsContent: " + mapStandardNominationsContent);
			logger.debug("mapStandardNominationsHTMLedContent: " + mapStandardNominationsHTMLedContent);
			
			Map mapStandardRatesContent= new TreeMap(new VoteComparator());
			mapStandardRatesContent=mapVoteRatesContent;
			logger.debug("test1: mapStandardRatesContent: " + mapStandardRatesContent);
			logger.debug("test1: mapStandardUserCount: " + mapStandardUserCount);
			
			Iterator itListQuestions = userEntries.iterator();
		    int mapVoteRatesSize=mapVoteRatesContent.size();
		    logger.debug("mapVoteRatesSize: " + mapVoteRatesSize);
		    mapIndex=new Long(mapVoteRatesSize+1);
		    logger.debug("updated mapIndex: " + mapIndex);
		    
		    double total=MonitoringUtil.calculateTotal(mapVoteRatesContent);
		    logger.debug("updated mapIndex: " + mapIndex);
		    double share=100d-total ; 
		    logger.debug("share: " + share);
		    
		    logger.debug("totalStandardVotesCount: " + totalStandardVotesCount);
		    int userEnteredVotesCount=entriesCount - totalStandardVotesCount;
	        logger.debug("userEnteredVotesCount for this session: " + userEnteredVotesCount);
		    
		    mapStandardNominationsContent.put(mapIndex.toString(), "Open Vote");
		    mapStandardNominationsHTMLedContent.put(mapIndex.toString(), "Open Vote");
		    mapStandardRatesContent.put(mapIndex.toString(), new Double(share).toString());
		    mapStandardUserCount.put(mapIndex.toString(), new Integer(userEnteredVotesCount).toString());

			logger.debug("processed for prepareChartDTO: mapStandardNominationsContent: " + mapStandardNominationsContent);
			logger.debug("processed for prepareChartDTO: mapStandardNominationsHTMLedContent: " + mapStandardNominationsHTMLedContent);
			logger.debug("processed for prepareChartDTO: mapStandardUserCount: " + mapStandardUserCount);	        
			logger.debug("processed for prepareChartDTO: mapStandardRatesContent: " + mapStandardRatesContent);
	        
			voteAllSessionsDTO.setMapStandardNominationsContent(mapStandardNominationsContent);
			voteAllSessionsDTO.setMapStandardNominationsHTMLedContent(mapStandardNominationsHTMLedContent);
			voteAllSessionsDTO.setMapStandardUserCount(mapStandardUserCount);
			voteAllSessionsDTO.setMapStandardRatesContent(mapStandardRatesContent);
			
			VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
			List listUserEntries=voteMonitoringAction.processUserEnteredNominations(voteService, voteContent, voteSession.getVoteSessionId().toString(), true, null, false);
		    logger.debug("listUserEntries: " + listUserEntries);
		    voteAllSessionsDTO.setListUserEntries(listUserEntries);
	        
	        listVoteAllSessionsDTO.add(voteAllSessionsDTO);
		}
        logger.debug("listVoteAllSessionsDTO: " + listVoteAllSessionsDTO);
	    
        return listVoteAllSessionsDTO;
	}
	
	/**
	 * Generates JFreeChart data for the learner module and 
	 * monitoring module Summary tab (Individual Sessions mode) 
	 * 
	 * @param request
	 * @param voteService
	 * @param voteMonitoringForm
	 * @param toolContentId
	 * @param toolSessionUid
	 */
	public static void prepareChartData(HttpServletRequest request, IVoteService voteService, VoteMonitoringForm voteMonitoringForm, Long toolContentId, Long toolSessionUid)
	{
	    logger.debug("starting prepareChartData, toolContentId: " + toolContentId);
	    logger.debug("starting prepareChartData, toolSessionUid: " + toolSessionUid);
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
	    logger.debug("starting prepareChartData, voteContent uid: " + voteContent.getUid());
	    
	    logger.debug("starting prepareChartData, voteMonitoringForm: " + voteMonitoringForm);
	    
		logger.debug("existing voteContent:" + voteContent);
		Map mapOptionsContent= new TreeMap(new VoteComparator());
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		
		Map mapVoteRatesContent= new TreeMap(new VoteComparator());
		logger.debug("mapVoteRatesContent: " + mapVoteRatesContent);
		
		
		List distinctSessionUsers=new ArrayList();
		boolean sessionLevelCharting=true;
		int entriesCount=0;
		Set userEntries=null;
		if (toolSessionUid != null)
		{
		    logger.debug("process for session: " + toolSessionUid);
		    entriesCount=voteService.getSessionEntriesCount(toolSessionUid);
		    logger.debug("entriesCount: " + entriesCount);
		    userEntries=voteService.getSessionUserEntriesSet(toolSessionUid);
		    logger.debug("sessionUserCount: " + userEntries.size());
		    
		    int completedSessionUserCount=voteService.getCompletedVoteUserBySessionUid(toolSessionUid);
		    logger.debug("completedSessionUserCount: " + completedSessionUserCount);
		    
		    
		    int completedEntriesCount=voteService.getCompletedSessionEntriesCount(toolSessionUid);
		    logger.debug("completedEntriesCount: " + completedEntriesCount);

		    if (voteMonitoringForm != null)
		    {
		        int potentialUserCount=voteService.getVoteSessionPotentialLearnersCount(toolSessionUid);
		        logger.debug("potentialUserCount: " + potentialUserCount);
		        voteMonitoringForm.setSessionUserCount(Integer.toString(potentialUserCount));
		        voteMonitoringForm.setCompletedSessionUserCount(new Integer(completedSessionUserCount).toString());
		        
		        if (potentialUserCount != 0)
		        {
		            double completedPercent=(completedSessionUserCount*100) / potentialUserCount;
		            logger.debug("completed percent: " + completedPercent);
		            voteMonitoringForm.setCompletedSessionUserPercent(new Double(completedPercent).toString());    
		        }
		        else
		        {
		            voteMonitoringForm.setCompletedSessionUserPercent("Not Available");
		        }
		    }
		}
		
		logger.debug("entriesCount: " + entriesCount);
		logger.debug("userEntries: " + userEntries);
		logger.debug("sessionLevelCharting: " + sessionLevelCharting);
		
		
		Map mapStandardUserCount= new TreeMap(new VoteComparator());

		logger.debug("setting existing content data from the db");
		mapOptionsContent.clear();
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		int totalStandardVotesCount=0;
		
		logger.debug("using entriesCount: " + entriesCount);
		
		Map mapStandardNominationsHTMLedContent= new TreeMap(new VoteComparator());
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapStandardNominationsHTMLedContent.put(mapIndex.toString(), voteQueContent.getQuestion());
				String noHTMLNomination = VoteUtils.stripHTML(voteQueContent.getQuestion());
			    logger.debug("noHTMLNomination: " + noHTMLNomination);
				mapOptionsContent.put(mapIndex.toString(),noHTMLNomination);
				
				int votesCount=0;
				if (sessionLevelCharting == true)
				{
				    logger.debug("getting votesCount based on session: " + toolSessionUid);
					votesCount=voteService.getStandardAttemptsForQuestionContentAndSessionUid(voteQueContent.getUid(), toolSessionUid);
					logger.debug("votesCount for questionContent uid: " + votesCount + " for" + voteQueContent.getUid());
					mapStandardUserCount.put(mapIndex.toString(),new Integer(votesCount).toString());
					totalStandardVotesCount=totalStandardVotesCount + votesCount;
				}
				else
				{
				    logger.debug("getting votesCount based on content: " + voteQueContent.getUid());
				    votesCount=voteService.getAttemptsForQuestionContent(voteQueContent.getUid());
					logger.debug("votesCount for questionContent uid: " + votesCount + " for" + voteQueContent.getUid());
				}
				
				double voteRate=0d;
				if (entriesCount != 0)
				{
				    voteRate=((votesCount * 100)/ entriesCount);
				}

				logger.debug("voteRate" + voteRate);
				
				mapVoteRatesContent.put(mapIndex.toString(), new Double(voteRate).toString());
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		
		logger.debug("test1: Map initialized with existing contentid to: " + mapOptionsContent);
		Map mapStandardNominationsContent= new TreeMap(new VoteComparator());
		mapStandardNominationsContent=mapOptionsContent;
		logger.debug("mapStandardNominationsContent: " + mapStandardNominationsContent);
		logger.debug("mapStandardNominationsHTMLedContent: " + mapStandardNominationsHTMLedContent);
		
		Map mapStandardRatesContent= new TreeMap(new VoteComparator());
		mapStandardRatesContent=mapVoteRatesContent;
		logger.debug("test1: mapStandardRatesContent: " + mapStandardRatesContent);
		logger.debug("test1: mapStandardUserCount: " + mapStandardUserCount);
		
		Iterator itListQuestions = userEntries.iterator();
	    int mapVoteRatesSize=mapVoteRatesContent.size();
	    logger.debug("mapVoteRatesSize: " + mapVoteRatesSize);
	    mapIndex=new Long(mapVoteRatesSize+1);
	    logger.debug("updated mapIndex: " + mapIndex);
	    
	    double total=MonitoringUtil.calculateTotal(mapVoteRatesContent);
	    logger.debug("updated mapIndex: " + mapIndex);
	    double share=100d-total ; 
	    logger.debug("share: " + share);
	    
	    logger.debug("totalStandardVotesCount: " + totalStandardVotesCount);
	    int userEnteredVotesCount=entriesCount - totalStandardVotesCount;
        logger.debug("userEnteredVotesCount for this session: " + userEnteredVotesCount);
	    
	    mapStandardNominationsContent.put(mapIndex.toString(), "Open Vote");
	    mapStandardNominationsHTMLedContent.put(mapIndex.toString(), "Open Vote");
	    mapStandardRatesContent.put(mapIndex.toString(), new Double(share).toString());
	    mapStandardUserCount.put(mapIndex.toString(), new Integer(userEnteredVotesCount).toString());
        
		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_CONTENT, mapStandardNominationsContent);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));
		
		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT, mapStandardNominationsHTMLedContent);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT));

		request.getSession().setAttribute(MAP_STANDARD_RATES_CONTENT, mapStandardRatesContent);
		logger.debug("test2: MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));
		
		request.getSession().setAttribute(MAP_STANDARD_USER_COUNT, mapStandardUserCount);
		logger.debug("test2: MAP_STANDARD_USER_COUNT: " + request.getSession().getAttribute(MAP_STANDARD_USER_COUNT));
	}
	
	/**
	 * generates JFreeChart for the learner export portfolio
	 *  
	 * @param request
	 * @param voteService
	 * @param voteMonitoringForm
	 * @param toolContentId
	 * @param toolSessionUid
	 */
	public static void prepareChartDataForExportLearner(HttpServletRequest request, IVoteService voteService, VoteMonitoringForm voteMonitoringForm, Long toolContentId, Long toolSessionUid)
	{
	    logger.debug("starting prepareChartDataForExportLearner, toolContentId: " + toolContentId);
	    logger.debug("starting prepareChartDataForExportLearner, toolSessionUid: " + toolSessionUid);
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
	    logger.debug("starting prepareChartData, voteContent uid: " + voteContent.getUid());
	    
	    logger.debug("starting prepareChartDataForExport, voteMonitoringForm: " + voteMonitoringForm);
	    
		logger.debug("existing voteContent:" + voteContent);
		Map mapOptionsContent= new TreeMap(new VoteComparator());
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		
		Map mapVoteRatesContent= new TreeMap(new VoteComparator());
		logger.debug("mapVoteRatesContent: " + mapVoteRatesContent);
		
		List distinctSessionUsers=new ArrayList();
		boolean sessionLevelCharting=true;
		int entriesCount=0;
		Set userEntries=null;
		
		if (toolSessionUid != null)
		{
		    logger.debug("process for session: " + toolSessionUid);
		    entriesCount=voteService.getSessionEntriesCount(toolSessionUid);
		    logger.debug("entriesCount: " + entriesCount);
		    userEntries=voteService.getSessionUserEntriesSet(toolSessionUid);
		    logger.debug("sessionUserCount: " + userEntries.size());
		    
		    int completedSessionUserCount=voteService.getCompletedVoteUserBySessionUid(toolSessionUid);
		    logger.debug("completedSessionUserCount: " + completedSessionUserCount);
		    
		    int completedEntriesCount=voteService.getCompletedSessionEntriesCount(toolSessionUid);
		    logger.debug("completedEntriesCount: " + completedEntriesCount);

	        int potentialUserCount=voteService.getVoteSessionPotentialLearnersCount(toolSessionUid);
	        logger.debug("potentialUserCount: " + potentialUserCount);
	        
	        if (potentialUserCount != 0)
	        {
	            double completedPercent=(completedSessionUserCount * 100) / potentialUserCount;
	            logger.debug("completed percent: " + completedPercent);

	        }
		}
		
		logger.debug("entriesCount: " + entriesCount);
		logger.debug("userEntries: " + userEntries);
		logger.debug("sessionLevelCharting: " + sessionLevelCharting);
		
		
		Map mapStandardUserCount= new TreeMap(new VoteComparator());
		Map mapStandardNominationsVisibility= new TreeMap(new VoteComparator());

		logger.debug("setting existing content data from the db");
		mapOptionsContent.clear();
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		int totalStandardVotesCount=0;
		
		logger.debug("using entriesCount: " + entriesCount);
		Map mapStandardNominationsHTMLedContent= new TreeMap(new VoteComparator());
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapStandardNominationsHTMLedContent.put(mapIndex.toString(),voteQueContent.getQuestion());
				String noHTMLNomination = VoteUtils.stripHTML(voteQueContent.getQuestion());
			    logger.debug("noHTMLNomination: " + noHTMLNomination);
				mapOptionsContent.put(mapIndex.toString(),noHTMLNomination);
				
				
				int votesCount=0;
				if (sessionLevelCharting == true)
				{
				    logger.debug("getting votesCount based on session: " + toolSessionUid);
					votesCount=voteService.getStandardAttemptsForQuestionContentAndSessionUid(voteQueContent.getUid(), toolSessionUid);
					
					logger.debug("votesCount for questionContent uid: " + votesCount + " for" + voteQueContent.getUid());
					mapStandardUserCount.put(mapIndex.toString(),new Integer(votesCount).toString());
					totalStandardVotesCount=totalStandardVotesCount + votesCount;
				}
				else
				{
				    logger.debug("getting votesCount based on content: " + voteQueContent.getUid());
				    votesCount=voteService.getAttemptsForQuestionContent(voteQueContent.getUid());
					logger.debug("votesCount for questionContent uid: " + votesCount + " for" + voteQueContent.getUid());
				}
				
				double voteRate=0d;
				if (entriesCount != 0)
				{
				    voteRate=((votesCount * 100)/ entriesCount);
				}

				logger.debug("voteRate" + voteRate);
				
				mapVoteRatesContent.put(mapIndex.toString(), new Double(voteRate).toString());
				mapStandardNominationsVisibility.put(mapIndex.toString(), new Boolean(true).toString());
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("test1: Map initialized with existing contentid to: " + mapOptionsContent);
		Map mapStandardNominationsContent= new TreeMap(new VoteComparator());
		mapStandardNominationsContent=mapOptionsContent;
		logger.debug("mapStandardNominationsContent: " + mapStandardNominationsContent);
		logger.debug("mapStandardNominationsHTMLedContent: " + mapStandardNominationsHTMLedContent);
		
		Map mapStandardRatesContent= new TreeMap(new VoteComparator());
		mapStandardRatesContent=mapVoteRatesContent;
		logger.debug("test1: mapStandardRatesContent: " + mapStandardRatesContent);
		logger.debug("test1: mapStandardUserCount: " + mapStandardUserCount);
		logger.debug("test1: mapStandardNominationsVisibility: " + mapStandardNominationsVisibility);
		
		Iterator itListQuestions = userEntries.iterator();
	    int mapVoteRatesSize=mapVoteRatesContent.size();
	    logger.debug("mapVoteRatesSize: " + mapVoteRatesSize);
	    mapIndex=new Long(mapVoteRatesSize+1);
	    logger.debug("updated mapIndex: " + mapIndex);
	    
	    double total=MonitoringUtil.calculateTotal(mapVoteRatesContent);
	    logger.debug("updated mapIndex: " + mapIndex);
	    double share=100d-total ; 
	    logger.debug("share: " + share);
	    
	    logger.debug("totalStandardVotesCount: " + totalStandardVotesCount);
	    int userEnteredVotesCount=entriesCount - totalStandardVotesCount;
        logger.debug("userEnteredVotesCount for this session: " + userEnteredVotesCount);
	    
	    logger.debug("start processing userEntries: " + userEntries);
	    itListQuestions = userEntries.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	String  userEntry =(String)itListQuestions.next();
	    	logger.debug("userEntry:..." + userEntry);
	    	logger.debug("mapIndex: " + mapIndex);
	    	
	    	if ((userEntry != null) && (userEntry.length() > 0))
	    	{
	    	    int userEntryRate=0;
				if (sessionLevelCharting == true)
				{
				    logger.debug("getting userEntryRate based on session: " + toolSessionUid);
				    List voteEntries=voteService.getUserEnteredVotesForSession(userEntry, toolSessionUid);
				    boolean isVoteVisible=voteService.isVoteVisibleForSession(userEntry, toolSessionUid);
				    logger.debug("isVoteVisible: " + isVoteVisible);
				    
				    logger.debug("voteEntries: " + voteEntries);
				    int individualUserEnteredVoteCount=voteEntries.size();
				    logger.debug("individualUserEnteredVoteCount: " + individualUserEnteredVoteCount);
				    
				    double votesShare=(individualUserEnteredVoteCount * share) / userEnteredVotesCount;
					logger.debug("votesShare: " + votesShare);
		    	    
					if (isVoteVisible == true)
					{
					    mapStandardNominationsContent.put(mapIndex.toString(), userEntry);
						mapStandardNominationsHTMLedContent.put(mapIndex.toString(), userEntry);
					}
					else
					{
					    String nominationName="Nomination"  + mapIndex + " Hidden";
					    logger.debug("nominationName: " + nominationName);
					    mapStandardNominationsContent.put(mapIndex.toString(), nominationName);
					    mapStandardNominationsHTMLedContent.put(mapIndex.toString(), nominationName);
					}
					
					mapStandardRatesContent.put(mapIndex.toString(), new Double(votesShare).toString());
					mapStandardUserCount.put(mapIndex.toString(), new Integer(individualUserEnteredVoteCount).toString());
					mapStandardNominationsVisibility.put(mapIndex.toString(), new Boolean(isVoteVisible).toString());
		    	    mapIndex=new Long(mapIndex.longValue()+1);
			  }
	    	}
	    }
	    
		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_CONTENT, mapStandardNominationsContent);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));
		
		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT, mapStandardNominationsHTMLedContent);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT));
		
		request.getSession().setAttribute(MAP_STANDARD_RATES_CONTENT, mapStandardRatesContent);
		logger.debug("test2: MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));
		
		request.getSession().setAttribute(MAP_STANDARD_USER_COUNT, mapStandardUserCount);
		logger.debug("test2: MAP_STANDARD_USER_COUNT: " + request.getSession().getAttribute(MAP_STANDARD_USER_COUNT));
		
		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_VISIBILITY, mapStandardNominationsVisibility);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_VISIBILITY: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_VISIBILITY));
	}	
	

	/**
	 * generates JFreeChart for the teacher export portfolio
	 * 
	 * @param request
	 * @param voteService
	 * @param voteMonitoringForm
	 * @param toolContentId
	 * @param toolSessionUid
	 */
	public static void prepareChartDataForExportTeacher(HttpServletRequest request, IVoteService voteService, VoteMonitoringForm voteMonitoringForm, Long toolContentId, Long toolSessionUid)
	{
	    logger.debug("starting prepareChartDataForExportTeacher, toolContentId: " + toolContentId);
	    logger.debug("starting prepareChartDataForExportTeacher, toolSessionUid: " + toolSessionUid);
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
	    logger.debug("starting prepareChartData, voteContent uid: " + voteContent.getUid());
	    logger.debug("starting prepareChartDataForExport, voteMonitoringForm: " + voteMonitoringForm);
		logger.debug("existing voteContent:" + voteContent);
		
		Map mapOptionsContent= new TreeMap(new VoteComparator());
		Map mapVoteRatesContent= new TreeMap(new VoteComparator());
		Map mapStandardUserCount= new TreeMap(new VoteComparator());
		
	    Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		int totalStandardVotesCount=0;
		int totalStandardContentVotesCount=0;
		int entriesCount=0;
		
		logger.debug("get all entries for content: " + voteContent);
		List setEntriesCount=voteService.getContentEntries(voteContent.getUid());
		logger.debug("setEntriesCount: " +setEntriesCount);
		entriesCount=setEntriesCount.size();
		logger.debug("entriesCount: " + entriesCount);
		
		Map mapStandardNominationsHTMLedContent= new TreeMap(new VoteComparator());
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapStandardNominationsHTMLedContent.put(mapIndex.toString(),voteQueContent.getQuestion());
				String noHTMLNomination = VoteUtils.stripHTML(voteQueContent.getQuestion());
			    logger.debug("noHTMLNomination: " + noHTMLNomination);
				mapOptionsContent.put(mapIndex.toString(),noHTMLNomination);

				int votesCount=voteService.getStandardAttemptsForQuestionContentAndContentUid(voteQueContent.getUid(), voteContent.getUid());
				logger.debug("standardContentAttemptCount: " + votesCount);
				
				mapStandardUserCount.put(mapIndex.toString(),new Integer(votesCount).toString());
				totalStandardVotesCount=totalStandardVotesCount + votesCount;

				
				double voteRate=0d;
				if (entriesCount != 0)
				{
				    voteRate=((votesCount * 100)/ entriesCount);
				}

				logger.debug("voteRate" + voteRate);
				
				mapVoteRatesContent.put(mapIndex.toString(), new Double(voteRate).toString());

	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("test1: Map initialized with existing contentid to: " + mapOptionsContent);
		Map mapStandardNominationsContent= new TreeMap(new VoteComparator());
		mapStandardNominationsContent=mapOptionsContent;
		logger.debug("mapStandardNominationsContent: " + mapStandardNominationsContent);
		logger.debug("mapStandardNominationsHTMLedContent: " + mapStandardNominationsHTMLedContent);
		
		Map mapStandardRatesContent= new TreeMap(new VoteComparator());
		mapStandardRatesContent=mapVoteRatesContent;
		logger.debug("test1: mapStandardRatesContent: " + mapStandardRatesContent);
		logger.debug("test1: mapStandardUserCount: " + mapStandardUserCount);
		
	    int mapVoteRatesSize=mapVoteRatesContent.size();
	    logger.debug("mapVoteRatesSize: " + mapVoteRatesSize);
	    mapIndex=new Long(mapVoteRatesSize+1);
	    logger.debug("updated mapIndex: " + mapIndex);
	    
	    double total=MonitoringUtil.calculateTotal(mapVoteRatesContent);
	    logger.debug("updated mapIndex: " + mapIndex);
	    double share=100d-total ; 
	    logger.debug("share: " + share);
	    
	    logger.debug("totalStandardVotesCount: " + totalStandardVotesCount);
	    int userEnteredVotesCount=entriesCount - totalStandardVotesCount;
        logger.debug("userEnteredVotesCount for this session: " + userEnteredVotesCount);
	    
	    mapStandardNominationsContent.put(mapIndex.toString(), "Open Vote");
	    mapStandardNominationsHTMLedContent.put(mapIndex.toString(), "Open Vote");
	    mapStandardRatesContent.put(mapIndex.toString(), new Double(share).toString());
	    mapStandardUserCount.put(mapIndex.toString(), new Integer(userEnteredVotesCount).toString());
	    
		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_CONTENT, mapStandardNominationsContent);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));

		request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT, mapStandardNominationsHTMLedContent);
		logger.debug("test2: MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT));

		request.getSession().setAttribute(MAP_STANDARD_RATES_CONTENT, mapStandardRatesContent);
		logger.debug("test2: MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));
		
		request.getSession().setAttribute(MAP_STANDARD_USER_COUNT, mapStandardUserCount);
		logger.debug("test2: MAP_STANDARD_USER_COUNT: " + request.getSession().getAttribute(MAP_STANDARD_USER_COUNT));
	}	
	
}
