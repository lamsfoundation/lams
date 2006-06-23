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
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredUserDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUserMarkDTO;
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
	public static List buildGroupsQuestionData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		logger.debug("will be building groups question data  for content:..." + mcContent);
		logger.debug("mcService: " + mcService);
		
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
	    		mcMonitoredAnswersDTO.setWeight(mcQueContent.getWeight().toString());
	    		
	    		List listCandidateAnswers=mcService.findMcOptionNamesByQueId(mcQueContent.getUid());
				logger.debug("listCandidateAnswers:..." + listCandidateAnswers);
				mcMonitoredAnswersDTO.setCandidateAnswers(listCandidateAnswers);
				
	    		List listCandidateAnswersDTO=mcService.populateCandidateAnswersDTO(mcQueContent.getUid());
				logger.debug("listCandidateAnswersDTO:..." + listCandidateAnswersDTO);
				mcMonitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);
				
				Map questionAttemptData= buildGroupsAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid().toString(), mcService, null);
				logger.debug("questionAttemptData:..." + questionAttemptData);
				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}

	
	public static List 	buildGroupsQuestionDataForExportLearner(HttpServletRequest request, McContent mcContent, 
	        IMcService mcService, McSession mcSession, McQueUsr mcQueUsr)
	{
		logger.debug("will be building groups question data  for content:..." + mcContent);
		logger.debug("using mcSession:..." + mcSession);
		logger.debug("using mcQueUsr:..." + mcQueUsr);
		logger.debug("mcService: " + mcService);
		
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
	    		mcMonitoredAnswersDTO.setWeight(mcQueContent.getWeight().toString());
	    		
	    		List listCandidateAnswers=mcService.findMcOptionNamesByQueId(mcQueContent.getUid());
				logger.debug("listCandidateAnswers:..." + listCandidateAnswers);
				mcMonitoredAnswersDTO.setCandidateAnswers(listCandidateAnswers);
				
	    		List listCandidateAnswersDTO=mcService.populateCandidateAnswersDTO(mcQueContent.getUid());
				logger.debug("listCandidateAnswersDTO:..." + listCandidateAnswersDTO);
				mcMonitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);
				
				Map questionAttemptData= buildGroupsAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid().toString(), mcService,  mcQueUsr);
				logger.debug("questionAttemptData:..." + questionAttemptData);
				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}
	

	
	public static List buildGroupsMarkDataForExportLearner(HttpServletRequest request, McContent mcContent, IMcService mcService, McSession mcSession, Long learnerUid)
	{
		logger.debug("will be building export learner groups mark data  for content:..." + mcContent);
		
		logger.debug("mcSession: " + mcSession);
		logger.debug("learnerUid: " + learnerUid);
    	List listMonitoredMarksContainerDTO= new LinkedList();

    	
	    McSessionMarkDTO mcSessionMarkDTO= new McSessionMarkDTO();
	    mcSessionMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
	    mcSessionMarkDTO.setSessionName(mcSession.getSession_name().toString());
	    
	    
	    LinkedList sessionUsersData= new LinkedList();
	    Map mapSessionUsersData= new TreeMap(new McStringComparator());
	    
	    McUserMarkDTO mcUserMarkDTO= new McUserMarkDTO();
	    mcUserMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
	    
	    List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
    	logger.debug("listQuestions:..." + listQuestions);
    	
    	Iterator itListQuestions = listQuestions.iterator();
	    LinkedList userMarks= new LinkedList();
	    
    	while (itListQuestions.hasNext())
	    {
	    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
	    	logger.debug("mcQueContent:..." + mcQueContent);
	    	if (mcQueContent != null)
	    	{
	    	    String learnerMark=getLearnerMarkForQuestionInSession(mcQueContent.getUid(), mcSession.getUid(),learnerUid, mcSession, mcService);
	    	    logger.debug("learnerMark for queContent uid, mcSession uid, mcUser uid:..." + mcQueContent.getUid() + "--" + mcSession.getUid() 
	    	            + "--"  +  "is: " + learnerMark);
	    	    
	    	    userMarks.add(learnerMark);
	    	}
	    }

    	logger.debug("final userMarks:..." + userMarks);
    	mcUserMarkDTO.setMarks(userMarks);
    	
    	String totalMark=getTotalUserMarkForQuestions(userMarks);
    	logger.debug("totalMark: " + totalMark);
    	mcUserMarkDTO.setTotalMark(totalMark);
    	
    	logger.debug("final mcUserMarkDTO:..." + mcUserMarkDTO);
    	sessionUsersData.add(mcUserMarkDTO);
    	logger.debug("final sessionUsersData: " + sessionUsersData);
    	mapSessionUsersData=convertToMcUserMarkDTOMap(sessionUsersData);
    	logger.debug("final mapSessionUsersData: " + mapSessionUsersData);
    	mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
    	listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
        	
		logger.debug("final listMonitoredMarksContainerDTO:..." + listMonitoredMarksContainerDTO);
		return listMonitoredMarksContainerDTO;
	}

	
	public static List buildGroupsMarkData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		logger.debug("will be building groups mark data  for content:..." + mcContent);
    	List listMonitoredMarksContainerDTO= new LinkedList();
    	Set sessions=mcContent.getMcSessions();
    	Iterator sessionsIterator=sessions.iterator();

    	while (sessionsIterator.hasNext())
    	{
    	    McSession mcSession=(McSession) sessionsIterator.next(); 
    	    logger.debug("iterating mcSession:..." + mcSession);
    	    
    	    McSessionMarkDTO mcSessionMarkDTO= new McSessionMarkDTO();
    	    mcSessionMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
    	    mcSessionMarkDTO.setSessionName(mcSession.getSession_name().toString());
    	    
    	    Set sessionUsers=mcSession.getMcQueUsers();
    	    Iterator usersIterator=sessionUsers.iterator();
    	    
    	    LinkedList sessionUsersData= new LinkedList();
    	    Map mapSessionUsersData= new TreeMap(new McStringComparator());
        	while (usersIterator.hasNext())
        	{
        	    McQueUsr mcQueUsr=(McQueUsr) usersIterator.next();
        	    logger.debug("iterating mcQueUsr:..." + mcQueUsr);
        	    
        	    McUserMarkDTO mcUserMarkDTO= new McUserMarkDTO();
        	    mcUserMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
        	    mcUserMarkDTO.setUserName(mcQueUsr.getFullname());
        	    mcUserMarkDTO.setQueUsrId(mcQueUsr.getQueUsrId().toString());
        	    
        	    
        	    List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
            	logger.debug("listQuestions:..." + listQuestions);
            	
            	Iterator itListQuestions = listQuestions.iterator();
        	    LinkedList userMarks= new LinkedList();
        	    
            	while (itListQuestions.hasNext())
        	    {
        	    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
        	    	logger.debug("mcQueContent:..." + mcQueContent);
        	    	if (mcQueContent != null)
        	    	{
        	    	    String learnerMark=getLearnerMarkForQuestionInSession(mcQueContent.getUid(), mcSession.getUid(), mcQueUsr.getUid(), mcSession, mcService);
        	    	    logger.debug("learnerMark for queContent uid, mcSession uid, mcUser uid:..." + mcQueContent.getUid() + "--" + mcSession.getUid() 
        	    	            + "--"  + mcQueUsr.getUid() + "is: " + learnerMark);
        	    	    
        	    	    userMarks.add(learnerMark);
        	    	}
        	    }

            	logger.debug("final userMarks:..." + userMarks);
            	mcUserMarkDTO.setMarks(userMarks);
            	
    	    	String totalMark=getTotalUserMarkForQuestions(userMarks);
    	    	logger.debug("totalMark: " + totalMark);
    	    	mcUserMarkDTO.setTotalMark(totalMark);
    	    	
            	logger.debug("final mcUserMarkDTO:..." + mcUserMarkDTO);
            	sessionUsersData.add(mcUserMarkDTO);
        	}
        	logger.debug("final sessionUsersData: " + sessionUsersData);
        	mapSessionUsersData=convertToMcUserMarkDTOMap(sessionUsersData);
        	logger.debug("final mapSessionUsersData: " + mapSessionUsersData);
        	mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
        	listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
    	}
    	
		logger.debug("final listMonitoredMarksContainerDTO:..." + listMonitoredMarksContainerDTO);
		return listMonitoredMarksContainerDTO;
	}

	
	public static String getLearnerMarkForQuestionInSession(Long mcQueContentUid, Long mcSessionUid, Long mcQueUsrUid, McSession mcSession, IMcService mcService)
	{
	    logger.debug("starting getLearnerMarkForQuestionInSession: mcQueContentUid" + mcQueContentUid);
	    logger.debug("using getLearnerMarkForQuestionInSession: mcSessionUid" + mcSessionUid);
	    logger.debug("using getLearnerMarkForQuestionInSession: mcQueUsrUid" + mcQueUsrUid);
	    
	    McUsrAttempt mcUsrAttempt = mcService.getAttemptWithLastAttemptOrderForUserInSession(mcQueUsrUid, mcSessionUid);
    	logger.debug("mcUsrAttempt with highest attempt order: " + mcUsrAttempt);
    	String highestAttemptOrder="";
    	
		List listUserAttempts=null;
    	if (mcUsrAttempt != null)
    	{
        	highestAttemptOrder=mcUsrAttempt.getAttemptOrder().toString();
        	logger.debug("highestAttemptOrder: " + highestAttemptOrder);
    		listUserAttempts=mcService.getAttemptsOnHighestAttemptOrder(mcQueUsrUid,mcQueContentUid, mcSessionUid, new Integer(highestAttemptOrder));
    		logger.debug("listUserAttempts: " + listUserAttempts);
    	}
    	else
    	{
    	    return "0";
    	}
    	
    	if (listUserAttempts.size() == 0)
    	    return "0";
    	    
		Iterator itAttempts=listUserAttempts.iterator();
		
		if (!mcSession.getMcContent().isRetries())
		{
		    logger.debug("retries is OFF.");
		    boolean isAttemptCorrect=false;
		    McUsrAttempt mcUsrAttemptUser=null;
		    
			{
	    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
	    		mcUsrAttemptUser=mcUsrAttempt;

	    		if (mcUsrAttempt != null)
	    		{
	    		    if (mcUsrAttempt.isAttemptCorrect())
	    		    {
	    		        isAttemptCorrect=true;
	    		    }
	    		}
			}
			logger.debug("final isAttemptCorrect: " + isAttemptCorrect);
			logger.debug("mcUsrAttemptUser: " + mcUsrAttemptUser);
			logger.debug("mcUsrAttemptUser weight: " + mcUsrAttemptUser.getMcQueContent().getWeight().toString());
			
			if (isAttemptCorrect)
			{
			    return mcUsrAttemptUser.getMcQueContent().getWeight().toString();
			}
			else
			{
			    return "0";
			}
		}
		else
		{
		    logger.debug("retries is ON. User had to PASS. Print the final attempt's data");
		    McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
		    boolean isAttemptCorrect=false;
		    McUsrAttempt mcUsrAttemptGeneral=null;
			while (itAttempts.hasNext())
			{
	    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
	    		mcUsrAttemptGeneral=mcUsrAttempt;

	    		if (mcUsrAttempt != null)
	    		{
		    		if (mcUsrAttempt.isFinished() && mcUsrAttempt.isPassed()) 
		    		{
		    		    logger.debug("this is a individual question attempt that is finished and passed: " + mcUsrAttempt);
		    		    isAttemptCorrect=mcService.getUserAttemptCorrectForQuestionContentAndSessionUid(mcQueUsrUid,mcQueContentUid, mcSession.getUid(), new Integer(highestAttemptOrder));
		    		    logger.debug("isAttemptCorrect: " + isAttemptCorrect);
		    		    break;	
		    		}
	    		}
			}

			logger.debug("final isAttemptCorrect: " + isAttemptCorrect);
			if (isAttemptCorrect)
			{
			    	return mcUsrAttemptGeneral.getMcQueContent().getWeight().toString();
			}
			else
			{
			    return "0";
			}
		}
	}
	
	
	public static String getTotalUserMarkForQuestions(LinkedList userMarks)
	{
		Iterator itAttempts=userMarks.iterator();
		int totalMark= 0;
		while (itAttempts.hasNext())
		{
			String mark=(String)itAttempts.next();
			int intMark= new Integer(mark).intValue();
			totalMark=totalMark + intMark; 
		}
		return new Integer(totalMark).toString();
	}
	
	
	public static List buildSessionQuestionData(HttpServletRequest request, McContent mcContent, Long sessionId, Long userID)
	{
	    logger.debug("doing buildSessionQuestionData with sessionId: " + sessionId);
	    logger.debug("doing buildSessionQuestionData with userID: " + userID);
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
				
				Map questionAttemptData= buildSessionAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid().toString(), sessionId, userID);
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
	public static Map buildGroupsAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, 
	        String questionUid, IMcService mcService, McQueUsr mcQueUsr)
	{
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent + " questionUid:" + questionUid);
    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
    	
    	Map summaryToolSessions=populateToolSessionsId(request, mcContent, mcService);
    	logger.debug("summaryToolSessions: " + summaryToolSessions);
    	
    	if (mcQueUsr == null)
    	{
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
                		Map sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid, null, mcService);
                		
                		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
                	}
                }
    		}
    	}
    	else
    	{
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
                		Map sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid, mcQueUsr.getQueUsrId(), mcService);
                		
                		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
                	}
                }
    		}
    	    
    	}
    	
    	

    	logger.debug("final listMonitoredAttemptsContainerDTO:..." + listMonitoredAttemptsContainerDTO);
    	mapMonitoredAttemptsContainerDTO=convertToMap(listMonitoredAttemptsContainerDTO);
    	logger.debug("final mapMonitoredAttemptsContainerDTO:..." + mapMonitoredAttemptsContainerDTO);
		return mapMonitoredAttemptsContainerDTO;
	}

	
	public static Map buildGroupsAttemptDataForExportLearner(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, String questionUid, 
	        IMcService mcService, McSession mcSession, McQueUsr mcQueUsr )
	{
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent + " questionUid:" + questionUid);
		logger.debug("using mcSession: " +mcSession);
		logger.debug("using mcQueUsr: " +mcQueUsr);
		
    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
            
    	logger.debug("mcSession: " +  " = " + mcSession);
    	if (mcSession != null)
    	{
    		List listMcUsers=mcService.getMcUserBySessionOnly(mcSession);	
    		logger.debug("listMcUsers for session id:"  + mcSession.getMcSessionId() +  " = " + listMcUsers);
    		Map sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid, mcQueUsr.getQueUsrId(), mcService);
    		
    		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
		}

    	logger.debug("final listMonitoredAttemptsContainerDTO:..." + listMonitoredAttemptsContainerDTO);
    	mapMonitoredAttemptsContainerDTO=convertToMap(listMonitoredAttemptsContainerDTO);
    	logger.debug("final mapMonitoredAttemptsContainerDTO:..." + mapMonitoredAttemptsContainerDTO);
		return mapMonitoredAttemptsContainerDTO;
	}

	
	
	
	public static Map buildSessionAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, String questionUid, Long sessionId, Long userID)
	{
		logger.debug("doing buildSessionAttemptData with sessionId: "  + sessionId);
		logger.debug("doing buildSessionAttemptData with userID: "  + userID);
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent + " questionUid:" + questionUid);
		
    	IMcService mcService =McUtils.getToolService(request);
    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
    	
    	McSession mcSession= mcService.findMcSessionById(sessionId);
    	logger.debug("mcSession: " +  " = " + mcSession);
    	if (mcSession != null)
    	{
    		List listMcUsers=mcService.getMcUserBySessionOnly(mcSession);	
    		logger.debug("listMcUsers for session id:"  + mcSession.getMcSessionId() +  " = " + listMcUsers);
    		Map sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid, userID, mcService);
    		
    		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
	    	logger.debug("final listMonitoredAttemptsContainerDTO:..." + listMonitoredAttemptsContainerDTO);
	    	mapMonitoredAttemptsContainerDTO=convertToMap(listMonitoredAttemptsContainerDTO);
	    	logger.debug("final mapMonitoredAttemptsContainerDTO:..." + mapMonitoredAttemptsContainerDTO);
    	}
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
	public static Map  populateSessionUsersAttempts(HttpServletRequest request,Long sessionId, List listMcUsers, String questionUid, 
	        Long userID, IMcService mcService)
	{
	    logger.debug("starting populateSessionUsersAttempts");
		logger.debug("will be populating users marks for session id: " + sessionId);
		
		McSession mcSession=mcService.retrieveMcSession(sessionId);
	    logger.debug("retrieving mcSession: " + mcSession);

		logger.debug("userID: " + userID);
		Map mapMonitoredUserContainerDTO= new TreeMap(new McStringComparator());
		List listMonitoredUserContainerDTO= new LinkedList();
		
	    logger.debug("generating standard summary page");
		Iterator itUsers=listMcUsers.iterator();
		while (itUsers.hasNext())
		{
    		McQueUsr mcQueUsr=(McQueUsr)itUsers.next();
    		logger.debug("current User is: " + mcQueUsr);
    		
    		if (mcQueUsr != null)
    		{
        		if (userID != null)
        		{
        		    logger.debug("request is export portfolio " + userID);
            		logger.debug("mcQueUsr: " + mcQueUsr);
            		logger.debug("local mcQueUsr userID: " + mcQueUsr.getQueUsrId()); 
            		logger.debug("mcQueUsr.getQueUsrId().toString versus userID.toString(): " + mcQueUsr.getQueUsrId().toString() + " versus "  + userID.toString());

            		if (mcQueUsr.getQueUsrId().toString().equals(userID.toString()))
            		{
            		    logger.debug("returning attempt entries for : " + mcQueUsr);
            		    mapMonitoredUserContainerDTO=getAttemptEntries(request, mcService, mcQueUsr, mcSession, questionUid, listMonitoredUserContainerDTO, mapMonitoredUserContainerDTO);
            		}
        		}
        		else
        		{
        		    logger.debug("request is standard summary page.");
        		    mapMonitoredUserContainerDTO=getAttemptEntries(request, mcService, mcQueUsr, mcSession, questionUid, listMonitoredUserContainerDTO, mapMonitoredUserContainerDTO);
        		}    		    
    		}
		}
		logger.debug("returning: " + mapMonitoredUserContainerDTO);
		return mapMonitoredUserContainerDTO; 
	}
	
	
	
	public static Map getAttemptEntries(HttpServletRequest request, IMcService mcService, McQueUsr mcQueUsr, McSession mcSession, 
	        String questionUid, List listMonitoredUserContainerDTO, Map mapMonitoredUserContainerDTO)
	{
	    logger.debug("starting getAttemptEntries.");
		logger.debug("mcQueUsr: " + mcQueUsr);
		logger.debug("mcSession: " + mcSession);
		
		if (mcQueUsr != null)
		{
			logger.debug("getting listUserAttempts for user id: " + mcQueUsr.getUid() + " and que content id: " + questionUid);
			
		    McUsrAttempt mcUsrAttempt = mcService.getAttemptWithLastAttemptOrderForUserInSession(mcQueUsr.getUid(), mcSession.getUid());
	    	logger.debug("mcUsrAttempt with highest attempt order: " + mcUsrAttempt);
	    	String highestAttemptOrder="";
	    	
	    	List listUserAttempts=null;
	    	boolean attempExists=true; 
	    	if (mcUsrAttempt != null)
	    	{
	        	highestAttemptOrder=mcUsrAttempt.getAttemptOrder().toString();
	        	logger.debug("highestAttemptOrder: " + highestAttemptOrder);

				listUserAttempts=mcService.getAttemptsOnHighestAttemptOrder(mcQueUsr.getUid(), new Long(questionUid), mcSession.getUid(), new Integer(highestAttemptOrder));
				logger.debug("listUserAttempts: " + listUserAttempts);
	    	}
	    	else
	    	{
	    	    attempExists=false;
			    McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
				mcMonitoredUserDTO.setUserName(mcQueUsr.getFullname());
				//mcMonitoredUserDTO.setSessionId(sessionId.toString());
				mcMonitoredUserDTO.setSessionId(mcSession.getMcSessionId().toString());
				mcMonitoredUserDTO.setQuestionUid(questionUid);
			    mcMonitoredUserDTO.setMark("0");
				logger.debug("final constructed mcMonitoredUserDTO: " + mcMonitoredUserDTO);
				listMonitoredUserContainerDTO.add(mcMonitoredUserDTO);
	    	}
	    	logger.debug("attempExists: " + attempExists);
	    	
	    	if (attempExists)
	    	{
				Iterator itAttempts=listUserAttempts.iterator();
				
				if (!mcSession.getMcContent().isRetries())
				{
				    logger.debug("retries is OFF.");
				    boolean isAttemptCorrect=false;
				    McUsrAttempt mcUsrAttemptUser=null;
				    
	    			while (itAttempts.hasNext())
	    			{
	    	    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
	    	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
	    	    		mcUsrAttemptUser=mcUsrAttempt;

	    	    		if (mcUsrAttempt != null)
	    	    		{
	    	    		    if (mcUsrAttempt.isAttemptCorrect())
	    	    		    {
	    	    		        isAttemptCorrect=true;
	    	    		    }
	    	    		}
	    			}
	    			logger.debug("final isAttemptCorrect: " + isAttemptCorrect);
	    			logger.debug("mcUsrAttemptUser: " + mcUsrAttemptUser);
	    			
				    McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
	    			mcMonitoredUserDTO.setUserName(mcQueUsr.getFullname());
	    			//mcMonitoredUserDTO.setSessionId(sessionId.toString());
					mcMonitoredUserDTO.setSessionId(mcSession.getMcSessionId().toString());	    			
	    			mcMonitoredUserDTO.setQuestionUid(questionUid);
	    			
					if (isAttemptCorrect)
					{
	    			    mcMonitoredUserDTO.setMark(mcUsrAttemptUser.getMcQueContent().getWeight().toString());
					}
					else
					{
					    mcMonitoredUserDTO.setMark("0");
					}
	    			
	    			logger.debug("final constructed mcMonitoredUserDTO: " + mcMonitoredUserDTO);
	    			listMonitoredUserContainerDTO.add(mcMonitoredUserDTO);
				}
				else
				{
				    logger.debug("retries is ON. User had to PASS. Print the final attempt's data");
				    McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
				    boolean isAttemptCorrect=false;
				    McUsrAttempt mcUsrAttemptGeneral=null;
					while (itAttempts.hasNext())
					{
			    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
			    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
			    		mcUsrAttemptGeneral=mcUsrAttempt;

			    		if (mcUsrAttempt != null)
			    		{
	    		    		if (mcUsrAttempt.isFinished() && mcUsrAttempt.isPassed()) 
	    		    		{
	    		    		    logger.debug("this is a individual question attempt that is finished and passed: " + mcUsrAttempt);
	    		    		    isAttemptCorrect=mcService.getUserAttemptCorrectForQuestionContentAndSessionUid(mcQueUsr.getUid(), new Long(questionUid), mcSession.getUid(), new Integer(highestAttemptOrder));
	    		    		    logger.debug("isAttemptCorrect: " + isAttemptCorrect);
	    		    		    break;	
	    		    		}
			    		}
					}

					logger.debug("final isAttemptCorrect: " + isAttemptCorrect);
					if (isAttemptCorrect)
					{
	    			    mcMonitoredUserDTO.setMark(mcUsrAttemptGeneral.getMcQueContent().getWeight().toString());
					}
					else
					{
					    mcMonitoredUserDTO.setMark("0");
					}

				    mcMonitoredUserDTO.setUserName(mcQueUsr.getFullname());
				    //mcMonitoredUserDTO.setSessionId(sessionId.toString());
					mcMonitoredUserDTO.setSessionId(mcSession.getMcSessionId().toString());				    
				    mcMonitoredUserDTO.setQuestionUid(questionUid);
				    
				    logger.debug("final constructed mcMonitoredUserDTO: " + mcMonitoredUserDTO);
				    listMonitoredUserContainerDTO.add(mcMonitoredUserDTO);
				}
	    	}
		}
		logger.debug("returning : " + listMonitoredUserContainerDTO);
		//return listMonitoredUserContainerDTO;
		logger.debug("final listMonitoredUserContainerDTO: " + listMonitoredUserContainerDTO);
		mapMonitoredUserContainerDTO=convertToMcMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		logger.debug("final mapMonitoredUserContainerDTO:..." + mapMonitoredUserContainerDTO);
		return mapMonitoredUserContainerDTO;		    
	}


	public static McUsrAttempt getAttemptWithHighestOrder(List listUserAttempts)
	{
		logger.debug("starting getAttemptWithHighestOrder: " + listUserAttempts);
		Iterator itAttempts=listUserAttempts.iterator();
		int highestOrder=0;
		McUsrAttempt mcHighestUsrAttempt=null;
		
		while (itAttempts.hasNext())
		{
    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itAttempts.next();
    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
    		int currentOrder=mcUsrAttempt.getAttemptOrder().intValue();
    		logger.debug("currentOrder: " + currentOrder);
    		
    		if (currentOrder > highestOrder)
    		{
    		    mcHighestUsrAttempt=mcUsrAttempt;
    		    highestOrder=currentOrder;
    		    logger.debug("highestOrder is updated to: " + highestOrder);
    		}
		}
		
		logger.debug("returning mcHighestUsrAttempt: " + mcHighestUsrAttempt);
		logger.debug("highestOrder has become: " + highestOrder);
		return mcHighestUsrAttempt;
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

	
	public static Map convertToMcUserMarkDTOMap(List list)
	{
		logger.debug("using McUserMarkDTOMap: " + list);
		Map map= new TreeMap(new McStringComparator());
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	
    	while (listIterator.hasNext())
    	{
    	    McUserMarkDTO data=(McUserMarkDTO)listIterator.next();
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
