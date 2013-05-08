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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.mc.McAllGroupsDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
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
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Monitoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class MonitoringUtil implements McAppConstants{
	
	/**
	 * 
	 * ends up populating the attempt history for all the users of all the tool sessions for a content
	 * 
	 * @param request
	 * @param mcContent
	 * @return List
	 */
	public static List buildGroupsQuestionData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
		//will be building groups question data  for content
		
    	List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
    	
    	List listMonitoredAnswersContainerDTO= new LinkedList();
    	
		Iterator itListQuestions = listQuestions.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
	    	
	    	if (mcQueContent != null)
	    	{
	    		McMonitoredAnswersDTO mcMonitoredAnswersDTO= new McMonitoredAnswersDTO();
	    		mcMonitoredAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
	    		mcMonitoredAnswersDTO.setQuestion(mcQueContent.getQuestion());
	    		mcMonitoredAnswersDTO.setMark(mcQueContent.getMark().toString());
    		
	    		List listCandidateAnswersDTO=mcService.populateCandidateAnswersDTO(mcQueContent.getUid());
				mcMonitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);
				
				Map questionAttemptData= buildGroupsAttemptData(request, mcContent, mcQueContent, mcQueContent.getUid(), mcService, null);
				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
				
	    	}
		}
		return listMonitoredAnswersContainerDTO;
	}

	
	/**
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
		
    	List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
    	
    	List listMonitoredAnswersContainerDTO= new LinkedList();
    	
		Iterator itListQuestions = listQuestions.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
	    	
	    	if (mcQueContent != null)
	    	{
	    		McMonitoredAnswersDTO mcMonitoredAnswersDTO= new McMonitoredAnswersDTO();
	    		mcMonitoredAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
	    		mcMonitoredAnswersDTO.setQuestion(mcQueContent.getQuestion());
	    		mcMonitoredAnswersDTO.setMark(mcQueContent.getMark().toString());
	    		
	    		List listCandidateAnswersDTO=mcService.populateCandidateAnswersDTO(mcQueContent.getUid());
				mcMonitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);
				
				// Get the attempts for this user. The maps must match the maps in buildGroupsAttemptData or the jsp won't work.
				List listMonitoredUserContainerDTO=getAttemptEntries(request, mcService, mcQueUsr, mcSession, mcQueContent.getUid(), new LinkedList(), false);
				Map questionAttemptData = new TreeMap(new McStringComparator());
				questionAttemptData.put(mcSession.getSession_name(), listMonitoredUserContainerDTO);

				mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
				listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
	    	}
		}
		return listMonitoredAnswersContainerDTO;
	}
	

	/**
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @return
	 */
	public static List buildGroupsMarkData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
    	List listMonitoredMarksContainerDTO= new LinkedList();
    	Set sessions=mcContent.getMcSessions();
    	Iterator sessionsIterator=sessions.iterator();
    	int numQuestions = mcContent.getMcQueContents().size();
	    
	    while (sessionsIterator.hasNext())
    	{
    	    McSession mcSession=(McSession) sessionsIterator.next();
    	    
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
        	    
        	    McUserMarkDTO mcUserMarkDTO= new McUserMarkDTO();
        	    mcUserMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
        	    mcUserMarkDTO.setSessionName(mcSession.getSession_name().toString());
        	    mcUserMarkDTO.setFullName(mcQueUsr.getFullname());
        	    mcUserMarkDTO.setUserName(mcQueUsr.getUsername());
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
            	Date attemptTime = null;
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
                	// get the attempt time, (NB all questions will have the same attempt time)
        	    	// Not efficient, since we assign this value for each attempt
        	    	attemptTime = attempt.getAttemptTime();
        	    }
            	
            	mcUserMarkDTO.setMarks(userMarks);
            	mcUserMarkDTO.setAttemptTime(attemptTime);
    	    	mcUserMarkDTO.setTotalMark(new Long(totalMark));
    	    	
            	mapSessionUsersData.put(mapIndex.toString(), mcUserMarkDTO);
        		mapIndex=new Long(mapIndex.longValue()+1);
        	}
        	
        	mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
        	listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
    	}
    	
    	
		return listMonitoredMarksContainerDTO;
	}

	/**
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
    	Map<String,List>  mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	
    	Iterator sessionIterator = mcContent.getMcSessions().iterator();
    	while (sessionIterator.hasNext()) 
    	{
    		McSession mcSession = (McSession) sessionIterator.next();
    		Set listMcUsers=mcSession.getMcQueUsers();	
    		List sessionUsersAttempts=populateSessionUsersAttempts(request,mcSession.getMcSessionId(), listMcUsers, questionUid, mcService);
    		mapMonitoredAttemptsContainerDTO.put(mcSession.getSession_name(), sessionUsersAttempts);
		}

    	return mapMonitoredAttemptsContainerDTO;
	}

	/**
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
		
		if (mcQueUsr != null)
		{
			
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
		    
		    if (mcSession != null)
		    {
			    
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
	 * 
	 * @param mcService
	 * @param mcContent
	 * @return
	 */
	public static boolean notebookEntriesExist(IMcService mcService, McContent mcContent)
	{
		Iterator iteratorSession= mcContent.getMcSessions().iterator();
		while (iteratorSession.hasNext())
		{
		    McSession mcSession=(McSession) iteratorSession.next();
		    
		    if (mcSession != null)
		    {
			    
			    Iterator iteratorUser=mcSession.getMcQueUsers().iterator();
			    while (iteratorUser.hasNext())
				{
			        McQueUsr mcQueUsr=(McQueUsr) iteratorUser.next();
			        
			        if (mcQueUsr != null)
			        {
						NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
								CoreNotebookConstants.NOTEBOOK_TOOL,
								MY_SIGNATURE, new Integer(mcQueUsr.getQueUsrId().intValue()));
						if (notebookEntry != null)
						{
						    return true;
						}
						    
			        }
				}		        
		    }
		}
		return false;
	}
	

	/**
	 * @param request
	 * @param mcService
	 * @param mcContent
	 */
	public static void generateGroupsSessionData(HttpServletRequest request, IMcService mcService, McContent mcContent)
	{
	    List listAllGroupsDTO=buildGroupBasedSessionData(request, mcContent, mcService);
	    request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
	}


	/**
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @return
	 */
	public static List buildGroupBasedSessionData(HttpServletRequest request, McContent mcContent, IMcService mcService)
	{
    	List listQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
    	
    	List listAllGroupsContainerDTO= new LinkedList();
    	
    	
		Iterator iteratorSession= mcContent.getMcSessions().iterator();
		while (iteratorSession.hasNext())
		{
		    McSession mcSession=(McSession) iteratorSession.next();
		    String currentSessionId=mcSession.getMcSessionId().toString();
		    
		    String currentSessionName=mcSession.getSession_name();
		    
		    McAllGroupsDTO mcAllGroupsDTO= new McAllGroupsDTO();
		    List listMonitoredAnswersContainerDTO= new LinkedList();
		    
		    if (mcSession != null)
		    {
				Iterator itListQuestions = listQuestions.iterator();
			    while (itListQuestions.hasNext())
			    {
			    	McQueContent mcQueContent =(McQueContent)itListQuestions.next();
			    	
			    	if (mcQueContent != null)
			    	{
			    		McMonitoredAnswersDTO mcMonitoredAnswersDTO= new McMonitoredAnswersDTO();
			    		mcMonitoredAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
			    		mcMonitoredAnswersDTO.setQuestion(mcQueContent.getQuestion());
			    		mcMonitoredAnswersDTO.setSessionId(currentSessionId);
			    		mcMonitoredAnswersDTO.setSessionName(currentSessionName);
			    		
			    		Map questionAttemptData = new TreeMap();
			    		
						mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
						
						listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
			    	}
			    }
		    }
		    mcAllGroupsDTO.setGroupData(listMonitoredAnswersContainerDTO);
		    mcAllGroupsDTO.setSessionName(currentSessionName);
		    mcAllGroupsDTO.setSessionId(currentSessionId);
		    
		    listAllGroupsContainerDTO.add(mcAllGroupsDTO);
		    
		}
		return listAllGroupsContainerDTO;
	}	

	
    /**
     * Sets up auxiliary parameters. Used by all monitoring action methods. 
     * 
     * @param request
     * @param mcContent
     * @param mcService
     */
    protected static void setupAllSessionsData(HttpServletRequest request, McContent mcContent, IMcService mcService) {
	List listMonitoredAnswersContainerDTO = MonitoringUtil.buildGroupsQuestionData(request, mcContent, mcService);
	request.setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);

	List listMonitoredMarksContainerDTO = MonitoringUtil.buildGroupsMarkData(request, mcContent, mcService);
	request.setAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO, listMonitoredMarksContainerDTO);

	request.setAttribute(HR_COLUMN_COUNT, new Integer(mcContent.getMcQueContents().size() + 2).toString());

	String strPassMark = "";
	Integer passMark = mcContent.getPassMark();
	if (passMark == null)
	    strPassMark = " ";
	else if ((passMark != null) && (passMark.equals("0")))
	    strPassMark = " ";
	else
	    strPassMark = passMark.toString();
	if (strPassMark.trim().equals("0"))
	    strPassMark = " ";
	request.setAttribute(PASSMARK, strPassMark);

	// setting up the advanced summary for LDEV-1662
	request.setAttribute("questionsSequenced", mcContent.isQuestionsSequenced());
	request.setAttribute("showMarks", mcContent.isShowMarks());
	request.setAttribute("randomize", mcContent.isRandomize());
	request.setAttribute("displayAnswers", mcContent.isDisplayAnswers());
	request.setAttribute("retries", mcContent.isRetries());
	request.setAttribute("reflect", mcContent.isReflect());
	request.setAttribute("reflectionSubject", mcContent.getReflectionSubject());
	request.setAttribute("passMark", mcContent.getPassMark());
	request.setAttribute("toolContentID", mcContent.getMcContentId());
	
	// setting up Date and time restriction in activities
	HttpSession ss = SessionManager.getSession();
	Date submissionDeadline = mcContent.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute("submissionDeadline", tzSubmissionDeadline.getTime());
	}

	boolean isGroupedActivity = mcService.isGroupedActivity(new Long(mcContent.getMcContentId()));
	request.setAttribute("isGroupedActivity", isGroupedActivity);
    }
}
