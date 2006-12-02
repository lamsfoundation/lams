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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.mc.McAllGroupsDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredUserDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStatsDTO;
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
	 *  
	 * populateToolSessions(HttpServletRequest request, McContent mcContent)
	 * 
	 * populates all the tool sessions in a map
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
	 * 
	 * buildGroupsQuestionData(HttpServletRequest request, McContent mcContent)
	 * 
	 * ends up populating the attempt history for all the users of all the tool sessions for a content
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
	    		mcMonitoredAnswersDTO.setMark(mcQueContent.getMark().toString());
    		
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

	
	/**
	 * buildGroupsQuestionDataForExportLearner(HttpServletRequest request, McContent mcContent, 
	        IMcService mcService, McSession mcSession, McQueUsr mcQueUsr)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @param mcSession
	 * @param mcQueUsr
	 * @return
	 */
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
	    		mcMonitoredAnswersDTO.setMark(mcQueContent.getMark().toString());
	    		
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
	

	/**
	 * 
	 * List buildGroupsMarkDataForExportLearner(HttpServletRequest request, 
	 * McContent mcContent, IMcService mcService, McSession mcSession, Long learnerUid)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @param mcSession
	 * @param learnerUid
	 * @return
	 */
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
	
	
	/**
	 * List buildGroupsMarkData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @return
	 */
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
        	    mcUserMarkDTO.setSessionName(mcSession.getSession_name().toString());
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

	
	/**
	 * List buildGroupingBasedResponsesData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @return
	 */
	public static List buildGroupingBasedResponsesData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		logger.debug("will be building grouping data  for content:..." + mcContent);
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

	
	/**
	 *
	 * String getLearnerMarkForQuestionInSession(Long mcQueContentUid, Long mcSessionUid, Long mcQueUsrUid, 
	 * McSession mcSession, IMcService mcService)
	 *  
	 * @param mcQueContentUid
	 * @param mcSessionUid
	 * @param mcQueUsrUid
	 * @param mcSession
	 * @param mcService
	 * @return
	 */
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
			
			if (isAttemptCorrect)
			{
			    //return mcUsrAttemptUser.getMcQueContent().getWeight().toString();
			    return mcUsrAttemptUser.getMcQueContent().getMark().toString();
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
			    //return mcUsrAttemptGeneral.getMcQueContent().getWeight().toString();
			    return mcUsrAttemptGeneral.getMcQueContent().getMark().toString();
			}
			else
			{
			    return "0";
			}
		}
	}
	
	
	/**
	 * String getTotalUserMarkForQuestions(LinkedList userMarks)
	 * 
	 * @param userMarks
	 * @return
	 */
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
	
	
	/**
	 * List buildSessionQuestionData(HttpServletRequest request, McContent mcContent, Long sessionId, Long userID)
	 * 
	 * @param request
	 * @param mcContent
	 * @param sessionId
	 * @param userID
	 * @return
	 */
	public static List buildSessionQuestionData(HttpServletRequest request, McContent mcContent, Long sessionId, Long userID, IMcService mcService)
	{
	    logger.debug("doing buildSessionQuestionData with sessionId: " + sessionId);
	    logger.debug("doing buildSessionQuestionData with userID: " + userID);
		logger.debug("will be building groups question data  for content:..." + mcContent);

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
				
				Map questionAttemptData= buildSessionAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid().toString(), sessionId, 
				        userID, mcService);
				logger.debug("questionAttemptData:..." + questionAttemptData);
				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
				
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
	}

	
	
	/**
	 *  
	 * buildGroupsAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent)
	 * 
	 * helps populating user's attempt history
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

	
	/**
	 * Map buildGroupsAttemptDataForExportLearner(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, String questionUid, 
	        IMcService mcService, McSession mcSession, McQueUsr mcQueUsr )
	        
	 * @param request
	 * @param mcContent
	 * @param mcQueContent
	 * @param questionUid
	 * @param mcService
	 * @param mcSession
	 * @param mcQueUsr
	 * @return
	 */
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

	
	/**
	 * Map buildSessionAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, String questionUid, 
	 * Long sessionId, Long userID)
	 *  
	 * @param request
	 * @param mcContent
	 * @param mcQueContent
	 * @param questionUid
	 * @param sessionId
	 * @param userID
	 * @return
	 */
	public static Map buildSessionAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent, String questionUid, Long sessionId, Long userID, IMcService mcService)
	{
		logger.debug("doing buildSessionAttemptData with sessionId: "  + sessionId);
		logger.debug("doing buildSessionAttemptData with userID: "  + userID);
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent + " questionUid:" + questionUid);
		
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
    public static int getTotalAttemptCount(HttpServletRequest request, IMcService mcService)
    {
    	List listMarks=mcService.getMarks();
    	logger.debug("total attempt count: " + listMarks.size());
    	return listMarks.size();
    }
	
	
	/**
	 *   
	 * populateSessionUsersAttempts(HttpServletRequest request,List listMcUsers)
	 * 
	 * ends up populating all the user's attempt data of a  particular tool session
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
	
	
	/**
	 * Map getAttemptEntries(HttpServletRequest request, IMcService mcService, McQueUsr mcQueUsr, McSession mcSession, 
	        String questionUid, List listMonitoredUserContainerDTO, Map mapMonitoredUserContainerDTO)
	 * 
	 * @param request
	 * @param mcService
	 * @param mcQueUsr
	 * @param mcSession
	 * @param questionUid
	 * @param listMonitoredUserContainerDTO
	 * @param mapMonitoredUserContainerDTO
	 * @return
	 */
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
	    			    //mcMonitoredUserDTO.setMark(mcUsrAttemptUser.getMcQueContent().getWeight().toString());
					    mcMonitoredUserDTO.setMark(mcUsrAttemptUser.getMcQueContent().getMark().toString());
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
	    			    //mcMonitoredUserDTO.setMark(mcUsrAttemptGeneral.getMcQueContent().getWeight().toString());
	    			    mcMonitoredUserDTO.setMark(mcUsrAttemptGeneral.getMcQueContent().getMark().toString());
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


	/**
	 * McUsrAttempt getAttemptWithHighestOrder(List listUserAttempts)
	 * 
	 * @param listUserAttempts
	 * @return
	 */
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

	
	/**
	 * Map populateToolSessionsId(HttpServletRequest request, McContent mcContent, IMcService mcService)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @return
	 */
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

	
	/**
	 * Map convertToMap(List list)
	 * 
	 * @param list
	 * @return
	 */
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

	
	/**
	 * Map convertToMcUserMarkDTOMap(List list)
	 * 
	 * @param list
	 * @return
	 */
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
	
	
	/**
	 * Map convertToMcMonitoredUserDTOMap(List list)
	 * 
	 * @param list
	 * @return
	 */
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
	 * void buildMcStatsDTO(HttpServletRequest request, IMcService mcService, McContent mcContent)
	 * 
	 * @param request
	 * @param mcService
	 * @param mcContent
	 */
	public static void buildMcStatsDTO(HttpServletRequest request, IMcService mcService, McContent mcContent)
	{
		logger.debug("building mcStatsDTO: " + mcContent);
		McStatsDTO mcStatsDTO= new McStatsDTO();
		
		int countSessionComplete=0;
		int countAllUsers=0;
		logger.debug("finding out about content level notebook entries: " + mcContent);
		Iterator iteratorSession= mcContent.getMcSessions().iterator();
		while (iteratorSession.hasNext())
		{
		    McSession mcSession=(McSession) iteratorSession.next();
		    logger.debug("mcSession: " + mcSession);
		    
		    if (mcSession != null)
		    {
			    logger.debug("mcSession id: " + mcSession.getMcSessionId());
			    
			    if (mcSession.getSessionStatus().equals(COMPLETED))
			    {
			        ++countSessionComplete;
			    }
			        
			    Iterator iteratorUser=mcSession.getMcQueUsers().iterator();
			    while (iteratorUser.hasNext())
				{
			        McQueUsr mcQueUsr=(McQueUsr) iteratorUser.next();
			        logger.debug("mcQueUsr: " + mcQueUsr);
			        
			        if (mcQueUsr != null)
			        {
				        logger.debug("mcQueUsr foundid");
				        ++countAllUsers;
			        }
				}		        
		    }
		}
		logger.debug("countAllUsers: " + countAllUsers);
		logger.debug("countSessionComplete: " + countSessionComplete);
		
		
		mcStatsDTO.setCountAllUsers(new Integer(countAllUsers).toString());
		mcStatsDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		
		logger.debug("mcStatsDTO: " + mcStatsDTO);

		request.setAttribute(MC_STATS_DTO, mcStatsDTO);
	}

	
	/**
	 *  boolean notebookEntriesExist(IMcService mcService, McContent mcContent)
	 * 
	 * @param mcService
	 * @param mcContent
	 * @return
	 */
	public static boolean notebookEntriesExist(IMcService mcService, McContent mcContent)
	{
		logger.debug("finding out about content level notebook entries: " + mcContent);
		Iterator iteratorSession= mcContent.getMcSessions().iterator();
		while (iteratorSession.hasNext())
		{
		    McSession mcSession=(McSession) iteratorSession.next();
		    logger.debug("mcSession: " + mcSession);
		    
		    if (mcSession != null)
		    {
			    logger.debug("mcSession id: " + mcSession.getMcSessionId());
			    
			    Iterator iteratorUser=mcSession.getMcQueUsers().iterator();
			    while (iteratorUser.hasNext())
				{
			        McQueUsr mcQueUsr=(McQueUsr) iteratorUser.next();
			        logger.debug("mcQueUsr: " + mcQueUsr);
			        
			        if (mcQueUsr != null)
			        {
				        logger.debug("mcQueUsr id: " + mcQueUsr.getQueUsrId());
				        
						logger.debug("attempt getting notebookEntry: ");
						NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
								CoreNotebookConstants.NOTEBOOK_TOOL,
								MY_SIGNATURE, new Integer(mcQueUsr.getQueUsrId().intValue()));
						
				        logger.debug("notebookEntry: " + notebookEntry);
				        
						if (notebookEntry != null)
						{
						    logger.debug("found at least one notebookEntry: " + notebookEntry.getEntry());
						    return true;
						}
						    
			        }
				}		        
		    }
		}
		return false;
	}
	

	/**
	 * generateGroupsSessionData(HttpServletRequest request, IMcService mcService, McContent mcContent)
	 * 
	 * @param request
	 * @param mcService
	 * @param mcContent
	 */
	public static void generateGroupsSessionData(HttpServletRequest request, IMcService mcService, McContent mcContent)
	{
	    logger.debug("generateGroupsSessionData: " + mcContent);
	    
	    List listAllGroupsDTO=buildGroupBasedSessionData(request, mcContent, mcService);
		logger.debug("listAllGroupsDTO: " + listAllGroupsDTO);
		
	    request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
	}


	/**
	 * List buildGroupBasedSessionData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @return
	 */
	public static List buildGroupBasedSessionData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		logger.debug("buildGroupBasedSessionData" + mcContent);
		logger.debug("will be building groups question data  for content:..." + mcContent);
    	List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
    	logger.debug("listQuestions:..." + listQuestions);
    	
    	List listAllGroupsContainerDTO= new LinkedList();
    	
    	
		Iterator iteratorSession= mcContent.getMcSessions().iterator();
		while (iteratorSession.hasNext())
		{
		    McSession mcSession=(McSession) iteratorSession.next();
		    logger.debug("iteration for group based session data: " + mcSession);
		    String currentSessionId=mcSession.getMcSessionId().toString();
		    logger.debug("currentSessionId: " + currentSessionId);
		    
		    String currentSessionName=mcSession.getSession_name();
		    logger.debug("currentSessionName: " + currentSessionName);
		    
		    McAllGroupsDTO mcAllGroupsDTO= new McAllGroupsDTO();
		    List listMonitoredAnswersContainerDTO= new LinkedList();
		    
		    if (mcSession != null)
		    {
				Iterator itListQuestions = listQuestions.iterator();
			    while (itListQuestions.hasNext())
			    {
			    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
			    	logger.debug("mcQueContent:..." + mcQueContent);
			    	
			    	if (mcQueContent != null)
			    	{
					    logger.debug("populating McMonitoredAnswersDTO for : " + mcQueContent);
			    		McMonitoredAnswersDTO mcMonitoredAnswersDTO= new McMonitoredAnswersDTO();
			    		mcMonitoredAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
			    		mcMonitoredAnswersDTO.setQuestion(mcQueContent.getQuestion());
			    		mcMonitoredAnswersDTO.setSessionId(currentSessionId);
			    		mcMonitoredAnswersDTO.setSessionName(currentSessionName);
			    		
			    		Map questionAttemptData = new TreeMap();
			    		
			    		logger.debug("generated  questionAttemptData: " + questionAttemptData);
						mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
						
						logger.debug("adding mcMonitoredAnswersDTO to the listMonitoredAnswersContainerDTO: " + mcMonitoredAnswersDTO);
						listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
			    	}
			    }
		    }
		    logger.debug("listMonitoredAnswersContainerDTO:" + listMonitoredAnswersContainerDTO);
		    
		    logger.debug("adding listMonitoredAnswersContainerDTO to the mcAllGroupsDTO:" + listMonitoredAnswersContainerDTO);
		    mcAllGroupsDTO.setGroupData(listMonitoredAnswersContainerDTO);
		    mcAllGroupsDTO.setSessionName(currentSessionName);
		    mcAllGroupsDTO.setSessionId(currentSessionId);
		    
		    logger.debug("built mcAllGroupsDTO:" + mcAllGroupsDTO);
		    listAllGroupsContainerDTO.add(mcAllGroupsDTO);
		    
		}
    	
    	
		logger.debug("final listAllGroupsContainerDTO:..." + listAllGroupsContainerDTO);
		return listAllGroupsContainerDTO;
	}	

	
	/**
	 * void setupAllSessionsData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 */
	protected static void setupAllSessionsData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
	    logger.debug("starting setupAllSessionsData, mcContent: " + mcContent);
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getAttribute(CURRENT_MONITORED_TOOL_SESSION));

	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, mcContent, mcService);
	    request.setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    
	    List listMonitoredMarksContainerDTO=MonitoringUtil.buildGroupsMarkData(request, mcContent, mcService);
	    request.setAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO, listMonitoredMarksContainerDTO);
	    logger.debug("LIST_MONITORED_MARKS_CONTAINER_DTO: " + request.getAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO));
	    logger.debug("ending setupAllSessionsData, mcContent: ");
	    
	    request.setAttribute(HR_COLUMN_COUNT, new Integer(mcContent.getMcQueContents().size()+2).toString());
	    
	    String strPassMark="";
	    Integer passMark=mcContent.getPassMark();
	    logger.debug("passMark: " + passMark);
	    
    	if (passMark == null)
    	    strPassMark=" ";
    	else if ((passMark != null) && (passMark.equals("0")))
    	    strPassMark=" ";
    	else
    	    strPassMark=passMark.toString();
    	
    	logger.debug("strPassMark: " + strPassMark);
    	 
    	if (strPassMark.trim().equals("0"))
    		strPassMark=" ";
    	
    	logger.debug("strPassMark: " + strPassMark);
    	request.setAttribute(PASSMARK, strPassMark);
	}
}
