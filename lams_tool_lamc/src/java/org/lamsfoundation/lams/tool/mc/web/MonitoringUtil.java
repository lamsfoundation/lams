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

import java.util.Collection;
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
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredUserDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUserMarkDTO;
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
    		
	    		List listCandidateAnswersDTO=mcService.populateCandidateAnswersDTO(mcQueContent.getUid());
				logger.debug("listCandidateAnswersDTO:..." + listCandidateAnswersDTO);
				mcMonitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);
				
				Map questionAttemptData= buildGroupsAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid(), mcService, null);
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
	    		
	    		List listCandidateAnswersDTO=mcService.populateCandidateAnswersDTO(mcQueContent.getUid());
				logger.debug("listCandidateAnswersDTO:..." + listCandidateAnswersDTO);
				mcMonitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);
				
				// Get the attempts for this user. The maps must match the maps in buildGroupsAttemptData or the jsp won't work.
				List listMonitoredUserContainerDTO=getAttemptEntries(request, mcService, mcQueUsr, mcSession, mcQueContent.getUid(), new LinkedList(), false);
				Map questionAttemptData = new TreeMap(new McStringComparator());
				questionAttemptData.put(mcSession.getSession_name(), listMonitoredUserContainerDTO);

				if ( logger.isDebugEnabled() ) {
					logger.debug("questionAttemptData:..." + questionAttemptData);
				}
				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
	    	}
		}
		logger.debug("final listMonitoredAnswersContainerDTO:..." + listMonitoredAnswersContainerDTO);
		return listMonitoredAnswersContainerDTO;
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
    	int numQuestions = mcContent.getMcQueContents().size();
	    
	    while (sessionsIterator.hasNext())
    	{
    	    McSession mcSession=(McSession) sessionsIterator.next(); 
    	    logger.debug("iterating mcSession:..." + mcSession);
    	    
    	    McSessionMarkDTO mcSessionMarkDTO= new McSessionMarkDTO();
    	    mcSessionMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
    	    mcSessionMarkDTO.setSessionName(mcSession.getSession_name().toString());
    	    
    	    Set sessionUsers=mcSession.getMcQueUsers();
    	    Iterator usersIterator=sessionUsers.iterator();
    	    
    		Map mapSessionUsersData= new TreeMap(new McStringComparator());
        	Long mapIndex=new Long(1);

        	while (usersIterator.hasNext())
        	{
        	    McQueUsr mcQueUsr=(McQueUsr) usersIterator.next();
        	    logger.debug("iterating mcQueUsr:..." + mcQueUsr);
        	    
        	    McUserMarkDTO mcUserMarkDTO= new McUserMarkDTO();
        	    mcUserMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
        	    mcUserMarkDTO.setSessionName(mcSession.getSession_name().toString());
        	    mcUserMarkDTO.setUserName(mcQueUsr.getFullname());
        	    mcUserMarkDTO.setQueUsrId(mcQueUsr.getUid().toString());
        	    
            	// The marks for the user must be listed in the display order of the question.
            	// Other parts of the code assume that the questions will be in consecutive display 
            	// order starting 1 (e.g. 1, 2, 3, not 1, 3, 4) so we set up an array and use
            	// the ( display order - 1) as the index (arrays start at 0, rather than 1 hence -1)
            	// The user must answer all questions, so we can assume that they will have marks 
            	// for all questions or no questions.
            	// At present there can only be one answer for each question but there may be more 
            	// than one in the future and if so, we don't want to count the mark twice hence
            	// we need to check if we've already processed this question in the total.
            	Integer[] userMarks = new Integer[numQuestions];
            	Iterator attemptIterator = mcService.getLatestAttemptsForAUser(mcQueUsr.getUid()).iterator();
            	long totalMark = 0;
            	while (attemptIterator.hasNext())
        	    {
        	    	McUsrAttempt attempt =(McUsrAttempt)attemptIterator.next();
        	    	Integer displayOrder = attempt.getMcQueContent().getDisplayOrder();
        	    	int arrayIndex = displayOrder != null && displayOrder.intValue() > 0 ? displayOrder.intValue() - 1 : 1;
        	    	if ( userMarks[arrayIndex] == null ) {

                    	// We get the mark for the attempt if the answer is correct and we don't allow
                    	// retries, or if the answer is correct and the learner has met the passmark if 
                    	// we do allow retries.
        	    		Integer mark = attempt.getMarkForShow(mcSession.getMcContent().isRetries());
        	    		userMarks[arrayIndex] = mark;
        	    		totalMark += mark.intValue();
        	    	}
        	    }

            	mcUserMarkDTO.setMarks(userMarks);
    	    	mcUserMarkDTO.setTotalMark(new Long(totalMark));
    	    	
            	mapSessionUsersData.put(mapIndex.toString(), mcUserMarkDTO);
        		mapIndex=new Long(mapIndex.longValue()+1);
        	}
        	
        	mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
        	listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
    	}
    	
    	if ( logger.isDebugEnabled() )
    		logger.debug("final listMonitoredMarksContainerDTO:..." + listMonitoredMarksContainerDTO);
    	
		return listMonitoredMarksContainerDTO;
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
	        Long questionUid, IMcService mcService, McQueUsr mcQueUsr)
	{
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent + " questionUid:" + questionUid);
    	Map<String,List>  mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	
    	Iterator sessionIterator = mcContent.getMcSessions().iterator();
    	while (sessionIterator.hasNext()) 
    	{
    		McSession mcSession = (McSession) sessionIterator.next();
    		Set listMcUsers=mcSession.getMcQueUsers();	
    		List sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid, mcService);
    		mapMonitoredAttemptsContainerDTO.put(mcSession.getSession_name(), sessionUsersAttempts);
		}

    	if ( logger.isDebugEnabled() )
    		logger.debug("final mapMonitoredAttemptsContainerDTO:..." + mapMonitoredAttemptsContainerDTO);

    	return mapMonitoredAttemptsContainerDTO;
	}

	/**
	 *   
	 * populateSessionUsersAttempts(HttpServletRequest request,List listMcUsers)
	 * 
	 * ends up populating all the user's attempt data of a  particular tool session
	 * 
	 * if userID is not null, it only gets the attempts for that user.
	 * 
	 * @param request
	 * @param listMcUsers
	 * @return List
	 */
	public static List  populateSessionUsersAttempts(HttpServletRequest request,Long sessionId, Set listMcUsers, Long questionUid, IMcService mcService)
	{
		if ( logger.isDebugEnabled() ) {
			logger.debug("starting populateSessionUsersAttempts");
			logger.debug("will be populating users marks for session id: " + sessionId);
		}
		
		McSession mcSession=mcService.retrieveMcSession(sessionId);

		List listMonitoredUserContainerDTO= new LinkedList();
		Iterator itUsers=listMcUsers.iterator();
		while (itUsers.hasNext())
		{
    		McQueUsr mcQueUsr=(McQueUsr)itUsers.next();
		    listMonitoredUserContainerDTO=getAttemptEntries(request, mcService, mcQueUsr, mcSession, questionUid, listMonitoredUserContainerDTO, true);
		}
			
		return listMonitoredUserContainerDTO; 
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
	public static List getAttemptEntries(HttpServletRequest request, IMcService mcService, McQueUsr mcQueUsr, McSession mcSession, 
			Long questionUid, List listMonitoredUserContainerDTO, boolean latestOnly)
	{
	    logger.debug("starting getAttemptEntries.");
		logger.debug("mcQueUsr: " + mcQueUsr);
		logger.debug("mcSession: " + mcSession);
		
		if (mcQueUsr != null)
		{
			logger.debug("getting listUserAttempts for user id: " + mcQueUsr.getUid() + " and que content id: " + questionUid);
			
		    McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
			mcMonitoredUserDTO.setUserName(mcQueUsr.getFullname());
			mcMonitoredUserDTO.setSessionId(mcSession.getMcSessionId().toString());	    			
			mcMonitoredUserDTO.setQuestionUid(questionUid.toString());
			mcMonitoredUserDTO.setQueUsrId(mcQueUsr.getUid().toString());

			List<McUsrAttempt> listUserAttempts = null;
			if ( latestOnly ) 
				listUserAttempts = mcService.getLatestAttemptsForAUserForOneQuestionContent(mcQueUsr.getUid(), questionUid);
			else 
				listUserAttempts = mcService.getAllAttemptsForAUserForOneQuestionContentOrderByAttempt(mcQueUsr.getUid(), questionUid);
				
	    	if (listUserAttempts.size() == 0 )	{

	    		mcMonitoredUserDTO.setMark(new Integer(0));

	    	} else {

				// At present, we expect there to be only one answer to a question but there
				// could be more in the future - if that happens then we need to change 
	    		// String to a list of Strings.

	    		// We get the mark for the attempt if the answer is correct and we don't allow
	        	// retries, or if the answer is correct and the learner has met the passmark if 
	        	// we do allow retries.
				
	    		Map<Integer,String> attemptMap = new TreeMap<Integer,String>();
	    		for ( McUsrAttempt attempt : listUserAttempts) {
	    			attemptMap.put(attempt.getAttemptOrder(), attempt.getMcOptionsContent().getMcQueOptionText());
					mcMonitoredUserDTO.setMark( attempt.getMarkForShow(mcSession.getMcContent().isRetries()) );
					mcMonitoredUserDTO.setIsCorrect( new Boolean(attempt.isAttemptCorrect()).toString() );
				}
				mcMonitoredUserDTO.setUsersAttempts(attemptMap);
	    	}
			
		    if ( logger.isDebugEnabled() ) 
		    	logger.debug("final constructed mcMonitoredUserDTO: " + mcMonitoredUserDTO);

		    listMonitoredUserContainerDTO.add(mcMonitoredUserDTO);

		}
		
		return listMonitoredUserContainerDTO;
	}

	/**
	 * @param request
	 * @param mcService
	 * @param mcContent
	 */
	public static void setSessionUserCount(McContent mcContent, McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	{
		int countSessionComplete=0;
		int countAllUsers=0;
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
			        countSessionComplete++;
			    }
			    countAllUsers += mcSession.getMcQueUsers().size();
		    }
		}
		
		mcGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers));
		mcGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete));
		
		if ( countSessionComplete > 0 )
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(Boolean.FALSE.toString());
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
