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
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McRandomizedListsDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;

/**
 * 
 * Keeps all operations needed for Authoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class LearningUtil implements McAppConstants {
	static Logger logger = Logger.getLogger(LearningUtil.class.getName());
	
	/**
	 * void saveFormRequestData(HttpServletRequest request, McLearningForm mcLearningForm, boolean prepareViewAnswersDataMode)
	 * 
	 * @param request
	 * @param mcLearningForm
	 * @param prepareViewAnswersDataMode
	 */
	public static void saveFormRequestData(HttpServletRequest request, McLearningForm mcLearningForm, boolean prepareViewAnswersDataMode)
	{
	    logger.debug("starting saveFormRequestData:");
	    
	 	String httpSessionID=request.getParameter("httpSessionID");
	 	logger.debug("httpSessionID: " + httpSessionID);
	 	mcLearningForm.setHttpSessionID(httpSessionID);
	 	
	 	String userID=request.getParameter("userID");
	 	logger.debug("userID: " + userID);
	 	mcLearningForm.setUserID(userID);
	 	
	 	String passMarkApplicable=request.getParameter("passMarkApplicable");
	 	logger.debug("passMarkApplicable: " + passMarkApplicable);
	 	mcLearningForm.setPassMarkApplicable(passMarkApplicable);
	 	
	 	String userOverPassMark=request.getParameter("userOverPassMark");
	 	logger.debug("userOverPassMark: " + userOverPassMark);
	 	mcLearningForm.setUserOverPassMark(userOverPassMark);
	 	
	 	if (prepareViewAnswersDataMode == false)
	 	{
		 	String learnerProgress=request.getParameter("learnerProgress");
		 	logger.debug("learnerProgress: " + learnerProgress);
		 	mcLearningForm.setLearnerProgress(learnerProgress);
		 	logger.debug("form is populated with learnerProgress");
		 	String learnerProgressUserId=request.getParameter("learnerProgressUserId");
		 	logger.debug("learnerProgressUserId: " + learnerProgressUserId);
		 	mcLearningForm.setLearnerProgressUserId(learnerProgressUserId);

	 	}
	 	
	 	String questionListingMode=request.getParameter("questionListingMode");
	 	logger.debug("questionListingMode: " + questionListingMode);
	 	mcLearningForm.setQuestionListingMode(questionListingMode);


	 	logger.debug("ending saveFormRequestData:");
	}
	
    
    
    /**
     * calculateWeights(Map mapLeanerAssessmentResults, Map mapQuestionWeights)
     * 
     * @param mapLeanerAssessmentResults
     * @param mapQuestionWeights
     * @return int
     */
    public static int calculateWeights(Map mapLeanerAssessmentResults, Map mapQuestionWeights)
	{
    	logger.debug("starting calculate weights...");
    	logger.debug("mapLeanerAssessmentResults : " + mapLeanerAssessmentResults);
    	logger.debug("mapQuestionWeights : " + mapQuestionWeights);
    	
    	int totalUserWeight=0;
    	Iterator itLearnerAssessmentMap = mapLeanerAssessmentResults.entrySet().iterator();
		while (itLearnerAssessmentMap.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)itLearnerAssessmentMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            Iterator itWeightsMap = mapQuestionWeights.entrySet().iterator();
            while (itWeightsMap.hasNext())
            {
            	Map.Entry pairsWeight = (Map.Entry)itWeightsMap.next();
            	logger.debug("using the  weight pair: " +  pairsWeight.getKey() + " = " + pairsWeight.getValue());
            	if (pairs.getKey().toString().equals(pairsWeight.getKey().toString()))
            	{
            		logger.debug("equal question found " +  pairsWeight.getKey() + " = " + pairsWeight.getValue() + " and " +  pairs.getValue());
            		
            		if (pairs.getValue().toString().equalsIgnoreCase("true"))
            		{
                		logger.debug("equal question found " +  pairsWeight.getKey() + " is a correct answer.");
                		int weight= new Integer(pairsWeight.getValue().toString()).intValue();
                		logger.debug("weight: " + weight);
                		
                		totalUserWeight=totalUserWeight + weight;  
            		}
            	}
            }
		}
		logger.debug("totalUserWeight: " + totalUserWeight);
		return totalUserWeight;
	}
     
    /**
     * conversts correct options list to correct options map
     * buildMapCorrectOptions(List correctOptions)
     * 
     * @param correctOptions
     * @return Map
     */
    public static Map buildMapCorrectOptions(List correctOptions)
	{
    	Map mapCorrectOptions= new TreeMap(new McComparator());
    	Iterator correctOptionsIterator=correctOptions.iterator();
    	Long mapIndex=new Long(1);
    	while (correctOptionsIterator.hasNext())
    	{
    		McOptsContent mcOptsContent=(McOptsContent)correctOptionsIterator.next();
    		if (mcOptsContent != null)
    		{
    			mapCorrectOptions.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
        		mapIndex=new Long(mapIndex.longValue()+1);
    		}
    	}
    	logger.debug("mapCorrectOptions : " + mapCorrectOptions);
    	return mapCorrectOptions;
	}
    

    /**
     * Map buildMapCorrectOptionUids(List correctOptions)
     * 
     * @param correctOptions
     * @return
     */
    public static Map buildMapCorrectOptionUids(List correctOptions)
	{
    	Map mapCorrectOptionUids= new TreeMap(new McComparator());
    	Iterator correctOptionsIterator=correctOptions.iterator();
    	Long mapIndex=new Long(1);
    	while (correctOptionsIterator.hasNext())
    	{
    		McOptsContent mcOptsContent=(McOptsContent)correctOptionsIterator.next();
    		if (mcOptsContent != null)
    		{
    		    mapCorrectOptionUids.put(mapIndex.toString(),mcOptsContent.getUid().toString());
        		mapIndex=new Long(mapIndex.longValue()+1);
    		}
    	}
    	logger.debug("mapCorrectOptionUids : " + mapCorrectOptionUids);
    	return mapCorrectOptionUids;
	}

    
    /**
     * Map compare(Map mapGeneralCorrectOptions,Map mapGeneralCheckedOptionsContent)
     * 
     * @param mapGeneralCorrectOptions
     * @param mapGeneralCheckedOptionsContent
     * @return
     */
    public static Map compare(Map mapGeneralCorrectOptions,Map mapGeneralCheckedOptionsContent)
    {
    	logger.debug("incoming mapGeneralCorrectOptions : " + mapGeneralCorrectOptions);
    	logger.debug("incoming mapGeneralCheckedOptionsContent : " + mapGeneralCheckedOptionsContent);
    	
    	Map mapLeanerAssessmentResults= new TreeMap(new McComparator());
    	
    	if (mapGeneralCheckedOptionsContent == null)
    		return mapLeanerAssessmentResults;
		
    	Iterator itMap = mapGeneralCorrectOptions.entrySet().iterator();
    	boolean compareResult= false;
		while (itMap.hasNext()) {
			compareResult= false;
        	Map.Entry pairs = (Map.Entry)itMap.next();
            
            Iterator itCheckedMap = mapGeneralCheckedOptionsContent.entrySet().iterator();
            while (itCheckedMap.hasNext()) 
            {
            	compareResult= false;
            	Map.Entry checkedPairs = (Map.Entry)itCheckedMap.next();
                if (pairs.getKey().toString().equals(checkedPairs.getKey().toString()))
    			{
                    Map mapCorrectOptions=(Map) pairs.getValue();
                    Map mapCheckedOptions=(Map) checkedPairs.getValue();
                    
                    boolean isEqual=compareMapItems(mapCorrectOptions, mapCheckedOptions);
                    boolean isEqualCross=compareMapsItemsCross(mapCorrectOptions, mapCheckedOptions);
                    
                    compareResult= isEqual && isEqualCross; 
                    mapLeanerAssessmentResults.put(pairs.getKey(), new Boolean(compareResult).toString());
        		}
            }
		}
		logger.debug("constructed mapLeanerAssessmentResults: " + mapLeanerAssessmentResults);
		return mapLeanerAssessmentResults;
    }
    
    /**
     * boolean compareMapItems(Map mapCorrectOptions, Map mapCheckedOptions)
     * 
     * @param mapCorrectOptions
     * @param mapCheckedOptions
     * @return
     */
    public static boolean compareMapItems(Map mapCorrectOptions, Map mapCheckedOptions)
	{
       logger.debug("performing compareMaps");
	   logger.debug("mapCorrectOptions: " +  mapCorrectOptions);
       logger.debug("mapCheckedOptions: " +  mapCheckedOptions);
       
       Iterator itMap = mapCorrectOptions.entrySet().iterator();
       boolean response=false;
       while (itMap.hasNext()) 
       {
       		Map.Entry pairs = (Map.Entry)itMap.next();
       		logger.debug("pairs.getValue(): " +  pairs.getValue());
			boolean optionExists=optionExists(pairs.getValue().toString(), mapCheckedOptions);
			logger.debug("optionExists: " + optionExists);
			
			if (optionExists == false)
			{
				return false;
			}
		}
       return true;
	}
    
    
    /**
     * compareMapsCross(Map mapCorrectOptions, Map mapCheckedOptions)
     * 
     * @param mapCorrectOptions
     * @param mapCheckedOptions
     * @return boolean
     */
    public static boolean compareMapsItemsCross(Map mapCorrectOptions, Map mapCheckedOptions)
	{
       logger.debug("performing compareMapsCross");
	   logger.debug("mapCorrectOptions: " +  mapCorrectOptions);
       logger.debug("mapCheckedOptions: " +  mapCheckedOptions);
       
       Iterator itMap = mapCheckedOptions.entrySet().iterator();
       boolean response=false;
       while (itMap.hasNext()) 
       {
       		Map.Entry pairs = (Map.Entry)itMap.next();
       		logger.debug("pairs.getValue(): " + pairs.getValue());
       		boolean optionExists=optionExists(pairs.getValue().toString(), mapCorrectOptions);
       		logger.debug("optionExists: " + optionExists);
			
			if (optionExists == false)
			{
				return false;
			}
		}
       return true;
	}
    
    
    /**
     * optionExists(String optionValue, Map generalMap)
     * 
     * @param optionValue
     * @param generalMap
     * @return boolean
     */
    public static boolean optionExists(String optionValue, Map generalMap)
    {
        logger.debug("performing optionExists: " + optionValue);
        logger.debug("generalMap: " + generalMap);
        
    	Iterator itMap = generalMap.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
       		Map.Entry pairsChecked = (Map.Entry)itMap.next();
       		logger.debug("pairsChecked: " + pairsChecked);
       		
       		if (pairsChecked.getValue().toString().equals(optionValue.toString()))
				return true;
    	}	
    	return false;
    }
	
    
    /**
     * McQueUsr getUser(HttpServletRequest request, IMcService mcService, String toolSessionId)
     * 
     * @param request
     * @param mcService
     * @param toolSessionId
     * @return
     */
    public static McQueUsr getUser(HttpServletRequest request, IMcService mcService, String toolSessionId)
	{
    	logger.debug("getUser:: " + toolSessionId);
	    Long queUsrId=McUtils.getUserId();
    	
    	McSession mcSession=mcService.retrieveMcSession(new Long(toolSessionId));
        logger.debug("retrieving mcSession: " + mcSession);
        McQueUsr mcQueUsr=mcService.getMcUserBySession(queUsrId, mcSession.getUid());
        logger.debug("retrieving mcQueUsr: " + mcQueUsr);
		return mcQueUsr;
	}
    
    
    /**
     * creates the user in the db
     * createUser(HttpServletRequest request)
     * 
     * @param request
     */
    public static McQueUsr createUser(HttpServletRequest request, IMcService mcService, Long toolSessionId)
	{
        logger.debug("createUser: using toolSessionId: " + toolSessionId);
		Long queUsrId=McUtils.getUserId();
		String username=McUtils.getUserName();
		String fullname=McUtils.getUserFullName();
		
		McSession mcSession=mcService.retrieveMcSession(toolSessionId);
		McQueUsr mcQueUsr= new McQueUsr(queUsrId, 
										username, 
										fullname,  
										mcSession, 
										new TreeSet());		
		mcService.createMcQueUsr(mcQueUsr);
		logger.debug("created mcQueUsr in the db: " + mcQueUsr);
		return mcQueUsr;
	}

    
    /**
     * createLearnerAttempt(HttpServletRequest request, McQueUsr mcQueUsr, List selectedQuestionAndCandidateAnswersDTO, 
            Integer totalMark, boolean passed, int highestAttemptOrder, Map mapLeanerAssessmentResults, IMcService mcService)
            
     * @param request
     * @param mcQueUsr
     * @param selectedQuestionAndCandidateAnswersDTO
     * @param mark
     * @param passed
     * @param highestAttemptOrder
     * @param mapLeanerAssessmentResults
     * @param mcService
     */
    public static void createLearnerAttempt(HttpServletRequest request, McQueUsr mcQueUsr, List selectedQuestionAndCandidateAnswersDTO, 
            boolean passed, Integer highestAttemptOrder, Map mapLeanerAssessmentResults, IMcService mcService)
	{
        logger.debug("starting createLearnerAttempt: ");
		Date attemptTime=McUtils.getGMTDateTime();
		
		Iterator itSelectedMap = selectedQuestionAndCandidateAnswersDTO.iterator();
		while (itSelectedMap.hasNext())
		{
		    McLearnerAnswersDTO mcLearnerAnswersDTO=(McLearnerAnswersDTO)itSelectedMap.next();
		    
		    if ( logger.isDebugEnabled() ) {
		    	logger.debug("mcLearnerAnswersDTO: " + mcLearnerAnswersDTO);
		    }
		    
			McQueContent mcQueContent=mcService.findMcQuestionContentByUid(mcLearnerAnswersDTO.getQuestionUid());
			
			createIndividualOptions(request, 
					mcLearnerAnswersDTO.getCandidateAnswers(), 
					mcQueContent, mcQueUsr, 
					attemptTime, 
					mcLearnerAnswersDTO.getMark(), 
					passed, 
					highestAttemptOrder, 
			        mcLearnerAnswersDTO.getAttemptCorrect(),
			        mcService);
		}
	
	 }

    
    /**
     * 
     * createIndividualOptions(HttpServletRequest request, Map candidateAnswers, McQueContent mcQueContent, 
            McQueUsr mcQueUsr, Date attempTime, String timeZone, int mark,  boolean passed, Integer highestAttemptOrder, String isAttemptCorrect, 
            IMcService mcService)
     * 
     * @param request
     * @param candidateAnswers
     * @param mcQueContent
     * @param mcQueUsr
     * @param attemptTime
     * @param mark
     * @param passed
     * @param highestAttemptOrder
     * @param isAttemptCorrect
     * @param mcService
     */
    public static void createIndividualOptions(HttpServletRequest request, Map candidateAnswers, McQueContent mcQueContent, 
            McQueUsr mcQueUsr, Date attemptTime, int mark,  boolean passed, Integer highestAttemptOrder, String isAttemptCorrect, 
            IMcService mcService)
    {
        logger.debug("starting createIndividualOptions");
    	Integer IntegerMark= new Integer(mark);
		
    	logger.debug("createIndividualOptions-> isAttemptCorrect: " + isAttemptCorrect);
    	logger.debug("mcQueContent: " + mcQueContent);
    	logger.debug("candidateAnswers: " + candidateAnswers);
    	logger.debug("highestAttemptOrder used : " + highestAttemptOrder);
    	
    	
    	if (mcQueContent != null)
    	{
    	    if (candidateAnswers != null)
        	{
        	    Iterator itCheckedMap = candidateAnswers.entrySet().iterator();
                while (itCheckedMap.hasNext()) 
                {
                	Map.Entry checkedPairs = (Map.Entry)itCheckedMap.next();
                	McOptsContent mcOptsContent= mcService.getOptionContentByOptionText(checkedPairs.getValue().toString(), mcQueContent.getUid());
                	logger.debug("mcOptsContent: " + mcOptsContent);
                	if (mcOptsContent != null)
                	{
                	    McUsrAttempt mcUsrAttempt=new McUsrAttempt(attemptTime, mcQueContent, mcQueUsr, mcOptsContent, IntegerMark, passed, highestAttemptOrder, new Boolean(isAttemptCorrect).booleanValue());
	    			    mcService.createMcUsrAttempt(mcUsrAttempt);
                    	logger.debug("created mcUsrAttempt in the db :" + mcUsrAttempt);
                	}
                }    
        	}    
    	}
    }
    
    /**
     * Map buildMarksMap(HttpServletRequest request, Long toolContentId, IMcService mcService)
     * 
     * @param request
     * @param toolContentId
     * @param mcService
     * @return
     */
    public static Map buildMarksMap(HttpServletRequest request, Long toolContentId, IMcService mcService)
    {
        logger.debug("starting buildMarksMap : " + toolContentId);
    	Map mapMarks= new TreeMap(new McComparator());
    	McContent mcContent=mcService.retrieveMc(toolContentId);
    	logger.debug("mcContent : " + mcContent);
		
    	List questionsContent=mcService.refreshQuestionContent(mcContent.getUid());
    	logger.debug("questionsContent : " + questionsContent);
    	
    	Iterator listIterator=questionsContent.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listIterator.next();
    		logger.debug("mcQueContent : " + mcQueContent);
    		mapMarks.put(mapIndex.toString(),mcQueContent.getMark().toString());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	logger.debug("mapMarks : " + mapMarks);
    	return mapMarks;
    }

    

    /**
     * McGeneralLearnerFlowDTO buildMcGeneralLearnerFlowDTO(McContent mcContent)
     * 
     * @param mcContent
     * @return
     */
    public static McGeneralLearnerFlowDTO buildMcGeneralLearnerFlowDTO(McContent mcContent)
    {
        logger.debug("starting buildMcGeneralLearnerFlowDTO: " + mcContent);
        McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO =new McGeneralLearnerFlowDTO();
        mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());
        mcGeneralLearnerFlowDTO.setActivityTitle(mcContent.getTitle());
        mcGeneralLearnerFlowDTO.setActivityInstructions(mcContent.getInstructions());
        mcGeneralLearnerFlowDTO.setPassMark(mcContent.getPassMark());
        mcGeneralLearnerFlowDTO.setReportTitleLearner("Report");
        mcGeneralLearnerFlowDTO.setLearnerProgress(new Boolean(false).toString());
        
        if (mcContent.isQuestionsSequenced()) 
            mcGeneralLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_SEQUENTIAL);
        else
            mcGeneralLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_COMBINED);
            
        
        logger.debug("continue buildMcGeneralLearnerFlowDTO: " + mcContent);
        mcGeneralLearnerFlowDTO.setTotalQuestionCount(new Integer(mcContent.getMcQueContents().size()));
        logger.debug("final mcGeneralLearnerFlowDTO: " + mcGeneralLearnerFlowDTO);
        return mcGeneralLearnerFlowDTO;
    }
    
    
    public static McRandomizedListsDTO randomizeList(List listCandidateAnswers, List listCandidateAnswerUids)
    {
        logger.debug("starting randomizeList: " + listCandidateAnswers);
        logger.debug("using listCandidateAnswerUids: " + listCandidateAnswerUids);
        
        McRandomizedListsDTO mcRandomizedListsDTO= new McRandomizedListsDTO();
        
        int caCount=listCandidateAnswers.size();
        logger.debug("caCount: " + caCount);

        Random generator = new Random();
                
        boolean listNotComplete=true;
        int randomInt=0;
                
        List randomList= new LinkedList();
        List randomUidList= new LinkedList();
        while (listNotComplete)
        {
            randomInt = generator.nextInt(caCount);
            logger.debug("randomInt: " + randomInt);
            
            String ca=(String)listCandidateAnswers.get(randomInt);
            logger.debug("ca: " + ca);
            
            String caUid=(String)listCandidateAnswerUids.get(randomInt);
            
        
            if (!isEntryStored(ca, randomList))
            {
                logger.debug("adding ca, since it is a new candidate, ca: " + ca);
                randomList.add(ca);
                randomUidList.add(caUid);
                
                logger.debug("randomList size: " + randomList.size());
                if (randomList.size() == listCandidateAnswers.size())
                {
                    logger.debug("the list is populated completely, randomList: " + randomList);
                    listNotComplete =false;        
                }
            }
        }
        
        listCandidateAnswerUids=randomUidList;
        logger.debug("modified listCandidateAnswerUids as: " + listCandidateAnswerUids);
        
        mcRandomizedListsDTO.setListCandidateAnswers(randomList);
        mcRandomizedListsDTO.setListCandidateAnswerUids(listCandidateAnswerUids);
        
        logger.debug("returning mcRandomizedListsDTO: " + mcRandomizedListsDTO);
        return mcRandomizedListsDTO;
    }
    
    
    public static boolean  isEntryStored(String ca, List randomList)
    {
        logger.debug("isEntryStored, randomList: " + randomList);
        logger.debug("isEntryStored, ca: " + ca);
        
        Iterator randomListIterator=randomList.iterator();
        
        while (randomListIterator.hasNext())
        {
            String caStored=(String)randomListIterator.next();
            logger.debug("caStored: " + caStored);
            
            if (caStored.equals(ca))
            {
                logger.debug("this ca already is stored: " + ca);
                return true;
            }
        }
        
       return false;
    }
    
    
    /**
     * List buildQuestionAndCandidateAnswersDTO(HttpServletRequest request, McContent mcContent, IMcService mcService)
     * 
     * @param request
     * @param mcContent
     * @param mcService
     * @return
     */
    public static List<McLearnerAnswersDTO> buildQuestionAndCandidateAnswersDTO(HttpServletRequest request, McContent mcContent, boolean randomize, 
            IMcService mcService)
    {
    	List<McLearnerAnswersDTO> questionAndCandidateAnswersList= new LinkedList<McLearnerAnswersDTO>();
    	List<McQueContent> listQuestionEntries=mcService.getAllQuestionEntries(mcContent.getUid());
    	
        Iterator listQuestionEntriesIterator=listQuestionEntries.iterator();
    	while (listQuestionEntriesIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listQuestionEntriesIterator.next();
    		McLearnerAnswersDTO mcLearnerAnswersDTO= new McLearnerAnswersDTO();
    		List listCandidateAnswers=mcService.findMcOptionNamesByQueId(mcQueContent.getUid());
    		List listCandidateAnswerUids=mcService.findMcOptionUidsByQueId(mcQueContent.getUid());
    		if (randomize)
    		{
    		    //listCandidateAnswers=randomizeList(listCandidateAnswers, listCandidateAnswerUids);
    		    McRandomizedListsDTO mcRandomizedListsDTO=randomizeList(listCandidateAnswers, listCandidateAnswerUids);
    		    listCandidateAnswers=mcRandomizedListsDTO.getListCandidateAnswers();
    		    listCandidateAnswerUids=mcRandomizedListsDTO.getListCandidateAnswerUids();
    		}
    		Map mapCandidateAnswers=convertToStringMap(listCandidateAnswers);
    		Map mapCandidateAnswerUids=convertToStringMap(listCandidateAnswerUids);
    		
    		String question=mcQueContent.getQuestion();
    		
    		mcLearnerAnswersDTO.setQuestion(question);
    		mcLearnerAnswersDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
    		mcLearnerAnswersDTO.setQuestionUid(mcQueContent.getUid());
    		
    		mcLearnerAnswersDTO.setMark(mcQueContent.getMark());
    		mcLearnerAnswersDTO.setCandidateAnswerUids(mapCandidateAnswerUids);
    		mcLearnerAnswersDTO.setCandidateAnswers(mapCandidateAnswers);
    		
    		questionAndCandidateAnswersList.add(mcLearnerAnswersDTO);
    	}
    	
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("buildQuestionAndCandidateAnswersDTO: mcContent uid "+mcContent.getUid()+" randomize "+randomize
    				+ "final questionAndCandidateAnswersList: " + questionAndCandidateAnswersList);
    	}
    	
    	return questionAndCandidateAnswersList;
    }
    
    
    /**
     * Map convertToStringMap(List list)
     * 
     * @param list
     * @return
     */
	public static Map convertToStringMap(List list)
	{
		logger.debug("using convertToStringMap: " + list);
		Map map= new TreeMap(new McComparator());
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	
    	while (listIterator.hasNext())
    	{
    		String data=(String)listIterator.next();
   			map.put(mapIndex.toString(), data);
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}

    
    /**
     * int getHighestMark(Map mapQuestionMarks, boolean mapExcluded)
     * 
     * @param mapQuestionMarks
     * @param mapExcluded
     * @return
     */
    public static int getHighestMark(Map mapQuestionMarks, boolean mapExcluded)
	{
	    logger.debug("mapExcluded: " + mapExcluded);
	   
		if ((mapQuestionMarks.size() == 1) && (!mapExcluded))
		{
			logger.debug("using map with 1 question only");
			/*the only alternative is 100*/
			return 100;
		}
		
	   logger.debug("continue excluding map");
	   Iterator itMap = mapQuestionMarks.entrySet().iterator();
 	   int highestMark=0; 	   
       while (itMap.hasNext()) 
       {
       		Map.Entry pair = (Map.Entry)itMap.next();
       		String mark=pair.getValue().toString();
       		int intMark=new Integer(mark).intValue();
       		
			if (intMark > highestMark)
			    highestMark= intMark;
       }
       return highestMark;
	}

    
 
	/**
	 * Map rebuildMarksMapExcludeHighestMark(Map mapQuestionMarks, int highestMark)
	 * 
	 * @param mapQuestionMarks
	 * @param highestMark
	 * @return
	 */
	public static Map rebuildMarksMapExcludeHighestMark(Map mapQuestionMarks, int highestMark)
	{
		logger.debug("doing rebuildMarksMapExcludeHighestMark: " + mapQuestionMarks);
		logger.debug("using highestMark: " + highestMark);
		
	   Map mapMarksExcludeHighestMark= new TreeMap(new McComparator());
	   
	   Iterator itMap = mapQuestionMarks.entrySet().iterator();
	   Long mapIndex=new Long(1);
       while (itMap.hasNext()) 
       {
       		Map.Entry pair = (Map.Entry)itMap.next();
       		String mark=pair.getValue().toString();
       		int intMark=new Integer(mark).intValue();
       		
       		logger.debug("intMark: " + intMark);
       		logger.debug("intMark versus highestMark:" + intMark + " versus" + highestMark);
       		if (intMark != highestMark)
       		{
       		    mapMarksExcludeHighestMark.put(mapIndex.toString(),mark);
    	   		mapIndex=new Long(mapIndex.longValue()+1);
       		}
       		else
       		{
       			logger.debug("excluding highest weight from the reconstructed map: " + intMark);
       		}
       }
       logger.debug("returning mapWeightsExcludeHighestWeight: " + mapMarksExcludeHighestMark);
       return mapMarksExcludeHighestMark; 
	}
	
    public static boolean isTextMarkup(String text)
    {
        logger.debug("starting isTextMarkup: " + text);
        
        int markupSignPos=text.indexOf("<");
        logger.debug("markupSignPos: " + markupSignPos);
        
        int markupSignPos2=text.indexOf(">");
        logger.debug("markupSignPos2: " + markupSignPos2);
        
        if ((markupSignPos != -1) && (markupSignPos2) != -1)
        {
            logger.debug("text has markup in it: " + text);
            return true;
        }
        return false;
    }

    /*
    public static String getWrappedText(String text, boolean authoring)
    {
        
        logger.debug("starting getWrappedText: " + text);
        logger.debug("authoring: " + authoring);
    	
        String newText="";
        int breakPos=50;
        
        if (authoring)
        	breakPos=30;
        
        if (text.length() > breakPos)
    	{
    		int counter=0;
    		while (counter < 100)
    		{
    		    counter++;
    		    logger.debug("using text: " + text);
    		    
    		    if (text.length() > breakPos)
    		    {
    		        newText += text.substring(0, breakPos+1) + "<br>" ;
    		        text=text.substring(breakPos+1);
        		    
    		    }
    		    else
    		    {
    		        newText +=text;
    		        break;
    		    }
    		}

    	}
    	else
    	{
    	    newText=text;
    	}
        
	    logger.debug("returning newText: " + newText);
    	return newText;
    }
    */

    /** Gets the various maps used by jsps to display a learner's attempts.
     * @return Map[mapFinalAnswersIsContent, mapFinalAnswersContent, mapQueAttempts, mapQueCorrectAttempts,  mapQueIncorrectAttempts, mapMarks] */
    public static Map[] getAttemptMapsForUser(int intTotalQuestionCount, Long toolContentUID, boolean allowRetries, IMcService mcService, McQueUsr mcQueUsr) {
    	
		Map mapFinalAnswersIsContent= new TreeMap(new McComparator());
		Map mapFinalAnswersContent= new TreeMap(new McComparator());
		
		// mapQueAttempts: key is the question display order, the value is the mapAttempOrderAttempts map.
		// mapAttemptOrderAttempts: key is the attempt order, the value is the mapAttempt map. 
		// mapAttemptMap: key is an artificial ordering, the value is the actual value for the question
		// at the moment, there will only be one attempt for each question of each learner in a tool session
		// so mapAttemptMap will only have one value.
		// The mapQueCorrectAttempts and mapQueIncorrectAttempts work in a similar way
		Map mapQueAttempts= new TreeMap(new McComparator());
		Map mapQueCorrectAttempts= new TreeMap(new McComparator());
		Map mapQueIncorrectAttempts= new TreeMap(new McComparator());
		
		for (int i=1; i<= intTotalQuestionCount; i++) 
		{
			logger.debug("doing question with display order: " + i);
			McQueContent mcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(i), toolContentUID);
			logger.debug("mcQueContent uid: " + mcQueContent.getUid());
			
			McUsrAttempt mcUsrAttemptFinal = null;
			
			List userAttempts = mcService.getAllAttemptsForAUserForOneQuestionContentOrderByAttempt(mcQueUsr.getUid(), mcQueContent.getUid());
			Iterator userAttemptsIter = userAttempts.iterator();

			Map mapAttemptOrderAttempts= new TreeMap(new McComparator());
			Map mapAttemptOrderCorrectAttempts= new TreeMap(new McComparator());
			Map mapAttemptOrderIncorrectAttempts= new TreeMap(new McComparator());

 		    while ( userAttemptsIter.hasNext() ) {
				McUsrAttempt mcUsrAttempt = (McUsrAttempt) userAttemptsIter.next();
				
				if ( mcUsrAttemptFinal == null || mcUsrAttempt.getAttemptOrder().compareTo(mcUsrAttemptFinal.getAttemptOrder()) > 0)
					mcUsrAttemptFinal = mcUsrAttempt;

				addToAttemptMaps(mapAttemptOrderAttempts, mapAttemptOrderCorrectAttempts, mapAttemptOrderIncorrectAttempts, mcUsrAttempt);
			}

			String questionDisplayOrderString = new Integer(i).toString();

    		Integer mark = null;
			if ( mcUsrAttemptFinal != null ) {
			    mapFinalAnswersIsContent.put(questionDisplayOrderString, new Boolean(mcUsrAttemptFinal.isAttemptCorrect()).toString());
			    mapFinalAnswersContent.put(questionDisplayOrderString, mcUsrAttemptFinal.getMcOptionsContent().getMcQueOptionText().toString());
			}
			if (mapAttemptOrderAttempts.size() > 0) {
				mapQueAttempts.put(questionDisplayOrderString, mapAttemptOrderAttempts);	
	    	}
			if (mapAttemptOrderCorrectAttempts.size() > 0) {
				mapQueCorrectAttempts.put(questionDisplayOrderString, mapAttemptOrderCorrectAttempts);	
	    	}    			
			if (mapAttemptOrderIncorrectAttempts.size() > 0) {
    			mapQueIncorrectAttempts.put(questionDisplayOrderString, mapAttemptOrderIncorrectAttempts);	
	    	}    			
		}
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("final mapFinalAnswersContent is: " + mapFinalAnswersContent);
			logger.debug("final mapFinalAnswersIsContent is: " + mapFinalAnswersIsContent);
			logger.debug("final mapQueAttempts is: " + mapQueAttempts);
			logger.debug("final mapQueCorrectAttempts is: " + mapQueCorrectAttempts);
			logger.debug("final mapQueIncorrectAttempts is: " + mapQueIncorrectAttempts);
		}
		
		return new Map[] {mapFinalAnswersIsContent, mapFinalAnswersContent, mapQueAttempts, mapQueCorrectAttempts,  mapQueIncorrectAttempts};
    }
    
    private static void addToAttemptMaps(Map mapAttemptOrderAttempts, Map mapAttemptOrderCorrectAttempts, Map mapAttemptOrderIncorrectAttempts, McUsrAttempt mcUsrAttempt) {
    	String attemptOrderString = mcUsrAttempt.getAttemptOrder().toString();
    	
    	Map attemptMap = (Map) mapAttemptOrderAttempts.get(attemptOrderString);
		Map correctAttemptMap = (Map) mapAttemptOrderCorrectAttempts.get(attemptOrderString);
		Map incorrectAttemptMap = (Map) mapAttemptOrderIncorrectAttempts.get(attemptOrderString);
		
		if ( attemptMap == null ) {
			attemptMap = new TreeMap(new McComparator());
			mapAttemptOrderAttempts.put(attemptOrderString, attemptMap);
			
			correctAttemptMap = new TreeMap(new McComparator());
			mapAttemptOrderAttempts.put(attemptOrderString, correctAttemptMap);

			incorrectAttemptMap = new TreeMap(new McComparator());
			mapAttemptOrderAttempts.put(attemptOrderString, incorrectAttemptMap);
		}

		int mapSize = attemptMap.size();
		String mapIndex = (new Integer(mapSize+1)).toString();
		attemptMap.put(mapIndex, mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
		if ( mcUsrAttempt.isAttemptCorrect() ) 
			correctAttemptMap.put(mapIndex, mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
		else
			incorrectAttemptMap.put(mapIndex, mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());

    }

    /** Should we show the marks for each question - we show the marks if any of the questions
     * have a mark > 1.
     */
    public static Boolean isShowMarksOnQuestion(List<McLearnerAnswersDTO> listQuestionAndCandidateAnswersDTO) {
	    Iterator iter = listQuestionAndCandidateAnswersDTO.iterator();
	    while (iter.hasNext()) {
			McLearnerAnswersDTO elem = (McLearnerAnswersDTO) iter.next();
			if ( elem.getMark().intValue() > 1 ) {
				return Boolean.TRUE;
			}
		}
	    return Boolean.FALSE;
    }
 }
