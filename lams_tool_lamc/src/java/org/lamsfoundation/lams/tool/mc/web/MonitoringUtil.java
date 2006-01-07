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
	public static Map populateToolSessions(HttpServletRequest request, McContent mcContent)
	{
		logger.debug("populating tool sessions for content:..." + mcContent);
    	IMcService mcService =McUtils.getToolService(request);
    	
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
				
				Map questionAttemptData= buildGroupsAttemptData(request, mcContent, mcQueContent);
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
	public static Map buildGroupsAttemptData(HttpServletRequest request, McContent mcContent, McQueContent mcQueContent)
	{
		logger.debug("will be building groups attempt data  for mcQueContent:..." + mcQueContent);
    	IMcService mcService =McUtils.getToolService(request);
    	Map mapMonitoredAttemptsContainerDTO= new TreeMap(new McStringComparator());
    	List listMonitoredAttemptsContainerDTO= new LinkedList();
    	
    	Map summaryToolSessions=populateToolSessions(request, mcContent);
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
            		Map sessionUsersAttempts=populateSessionUsersAttempts(request,listMcUsers);
            		
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
	 * ends up populating all the user's attempt data of a  particular tool session  
	 * populateSessionUsersAttempts(HttpServletRequest request,List listMcUsers)
	 * 
	 * @param request
	 * @param listMcUsers
	 * @return List
	 */
	public static Map populateSessionUsersAttempts(HttpServletRequest request,List listMcUsers)
	{
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
    			logger.debug("getting listUserAttempts for user id: " + mcQueUsr.getUid());
    			List listUserAttempts=mcService.getAttemptsForUser(mcQueUsr.getUid());
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
    	    			mcMonitoredUserDTO.setUserName(mcUsrAttempt.getQueUsrId().toString());
    	    			mcMonitoredUserDTO.setQueUsrId(mcQueUsr.getUid().toString());
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
    	
}
