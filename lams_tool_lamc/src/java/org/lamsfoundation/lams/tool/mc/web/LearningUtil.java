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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
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
	
	
	public static void saveFormRequestData(HttpServletRequest request, McLearningForm mcLearningForm, boolean prepareViewAnswersDataMode)
	{
	    logger.debug("prepareViewAnswersDataMode: " + prepareViewAnswersDataMode);
	    
	 	String httpSessionID=request.getParameter("httpSessionID");
	 	logger.debug("httpSessionID: " + httpSessionID);
	 	mcLearningForm.setHttpSessionID(httpSessionID);
	    
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
	 	}
	 	
	 	String learnerProgressUserId=request.getParameter("learnerProgressUserId");
	 	logger.debug("learnerProgressUserId: " + learnerProgressUserId);
	 	mcLearningForm.setLearnerProgressUserId(learnerProgressUserId);

	 	String questionListingMode=request.getParameter("questionListingMode");
	 	logger.debug("questionListingMode: " + questionListingMode);
	 	mcLearningForm.setQuestionListingMode(questionListingMode);


	 	logger.debug("done saving form request data.");
	}
	
    /**
     * updates the Map based on learner activity
     * selectOptionsCheckBox(HttpServletRequest request,McLearningForm mcLearningForm, String questionIndex)
     * 
     * @param request
     * @param form
     * @param questionIndex
     */
    
    /**
     * continueOptions(HttpServletRequest request)
     * 
     * @param request
     * @return boolean
     */
    public static boolean continueOptions(HttpServletRequest request, IMcService mcService)
    {
    	logger.debug("continue options requested.");
    	String currentQuestionIndex=(String)request.getSession().getAttribute(CURRENT_QUESTION_INDEX);
    	logger.debug("currentQuestionIndex:" + currentQuestionIndex);
    	
    	int newQuestionIndex=new Integer(currentQuestionIndex).intValue() + 1;
    	request.getSession().setAttribute(CURRENT_QUESTION_INDEX, new Integer(newQuestionIndex).toString());
    	logger.debug("updated questionIndex:" + request.getSession().getAttribute(CURRENT_QUESTION_INDEX));
    	
    	Long toolContentID= (Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
        logger.debug("TOOL_CONTENT_ID: " + toolContentID);
        
        McContent mcContent=mcService.retrieveMc(toolContentID);
        logger.debug("mcContent: " + mcContent);
        
        /*
    	 * fetch question content from content
    	 */
        logger.debug("newQuestionIndex: " + newQuestionIndex);
    	Iterator contentIterator=mcContent.getMcQueContents().iterator();
    	boolean questionFound=false;
    	while (contentIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)contentIterator.next();
    		if (mcQueContent != null)
    		{
    			int displayOrder=mcQueContent.getDisplayOrder().intValue();
    			logger.debug("displayOrder: " + displayOrder);
    			
        		/* prepare the next question's candidate answers for presentation*/ 
        		if (newQuestionIndex == displayOrder)
        		{
        			logger.debug("get the next question... ");
        			Long uid=mcQueContent.getUid();
        			logger.debug("uid : " + uid);
        			/* get the options for this question */
        			List listMcOptions=mcService.findMcOptionsContentByQueId(uid);
        			logger.debug("listMcOptions : " + listMcOptions);
        			Map mapOptionsContent=McUtils.generateOptionsMap(listMcOptions);
        			request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
        			logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
        			questionFound=true;
        		}
    		}
    	}
    	logger.debug("questionFound: " + questionFound);
		return questionFound;
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
     * calculates the mark of a learner
     * getMark(Map mapLeanerAssessmentResults)
     * 
     * @param mapLeanerAssessmentResults
     * @return int
     */
    public static int getMark(Map mapLeanerAssessmentResults)
    {
    	int totalUserWeight=0;
    	Iterator itLearnerAssessmentMap = mapLeanerAssessmentResults.entrySet().iterator();
    	int correctAnswerCount=0;
		while (itLearnerAssessmentMap.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)itLearnerAssessmentMap.next();
		    if (pairs.getValue().toString().equalsIgnoreCase("true"))
		    {
		    	++correctAnswerCount;
		    }
		}
		return correctAnswerCount;
    }
  
    /**
     * calculates the max attempt count of a learner
     * getHighestAttemptOrder(HttpServletRequest request, Long queUsrId)
     * 
     * @param request
     * @param queUsrId
     * @return
     */
    public static int getHighestAttemptOrder(HttpServletRequest request, Long queUsrId, IMcService mcService)
    {
    	logger.debug("mcService: " + mcService);
    	List listMarks=mcService.getHighestAttemptOrder(queUsrId);
    	
    	Iterator itMarks=listMarks.iterator();
    	int highestAO=0;
    	while (itMarks.hasNext())
		{
    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itMarks.next();
    		int currentAO=mcUsrAttempt.getAttemptOrder().intValue();
    		if (currentAO > highestAO)
    			highestAO= currentAO;
		}
    	return highestAO;
    }
    
    
    /**
     * returns the highest mark a learner has achieved
     * getHighestMark(HttpServletRequest request, Long queUsrId)
     * 
     * @param request
     * @param queUsrId
     * @return
     */
    public static int getHighestMark(HttpServletRequest request, Long queUsrId, IMcService mcService)
    {
    	List listMarks=mcService.getHighestMark(queUsrId);
    	
    	Iterator itMarks=listMarks.iterator();
    	int highestMark=0;
    	while (itMarks.hasNext())
		{
    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itMarks.next();
    		int currentMark=mcUsrAttempt.getMark().intValue();
    		if (currentMark > highestMark)
    			highestMark= currentMark;
		}
    	return highestMark;
    }
    
    /**
     * return the top mark for all learners
     * getTopMark(HttpServletRequest request)
     * 
     * @param request
     * @return
     */
    public static int getTopMark(HttpServletRequest request, IMcService mcService)
    {
    	List listMarks=mcService.getMarks();
    	
    	Iterator itMarks=listMarks.iterator();
    	int highestMark=0;
    	while (itMarks.hasNext())
		{
    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itMarks.next();
    		int currentMark=mcUsrAttempt.getMark().intValue();
    		if (currentMark > highestMark)
    			highestMark= currentMark;
		}
    	return highestMark;
    }

    
    /**
     * return the lowest mark for all learners
     * getTopMark(HttpServletRequest request)
     * 
     * @param request
     * @return
     */
    public static int getLowestMark(HttpServletRequest request, IMcService mcService)
    {
    	List listMarks=mcService.getMarks();
    	
    	Iterator itMarks=listMarks.iterator();
    	int lowestMark=100;
    	while (itMarks.hasNext())
		{
    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itMarks.next();
    		int currentMark=mcUsrAttempt.getMark().intValue();
    		if (currentMark < lowestMark)
    			lowestMark= currentMark;
		}
    	
    	//in case there was no attempts
    	if (lowestMark == 100)
    		lowestMark=0;
    	
    	return lowestMark;
    }
    
    /**
     * return the average mark for all learners
     * getTopMark(HttpServletRequest request)
     * 
     * @param request
     * @return
     */
    public static int getAverageMark(HttpServletRequest request, IMcService mcService)
    {
    	List listMarks=mcService.getMarks();
    	
    	Iterator itMarks=listMarks.iterator();
    	int marksTotal=0;
    	int count=0;
    	while (itMarks.hasNext())
		{
    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itMarks.next();
    		int currentMark=mcUsrAttempt.getMark().intValue();
    		marksTotal=marksTotal + currentMark;
			count++;
		}
    	logger.debug("marksTotal: " + marksTotal);
    	logger.debug("count: " + count);
    	
    	int averageMark=0;
    	if (count > 0)
    	{
    		averageMark= (marksTotal / count);
    	}
    	
    	logger.debug("averageMark: " + averageMark);
    	return averageMark;
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

    public static void createLearnerAttempt(HttpServletRequest request, McQueUsr mcQueUsr, List selectedQuestionAndCandidateAnswersDTO, 
            int mark,  boolean passed, int highestAttemptOrder, Map mapLeanerAssessmentResults, IMcService mcService)
	{
        logger.debug("starting createLearnerAttempt: ");
		Date attempTime=McUtils.getGMTDateTime();
		String timeZone= McUtils.getCurrentTimeZone();
		logger.debug("timeZone: " + timeZone);
		
		
		Iterator itSelectedMap = selectedQuestionAndCandidateAnswersDTO.iterator();
		while (itSelectedMap.hasNext())
		{
		    McLearnerAnswersDTO mcLearnerAnswersDTO=(McLearnerAnswersDTO)itSelectedMap.next();
			logger.debug("mcLearnerAnswersDTO: " + mcLearnerAnswersDTO);
			String questionUid=mcLearnerAnswersDTO.getQuestionUid();
			logger.debug("questionUid: " + questionUid);
			
			McQueContent mcQueContent=mcService.findMcQuestionContentByUid(new Long(questionUid));
			logger.debug("mcQueContent: " + mcQueContent);
			
			Map candidateAnswers=mcLearnerAnswersDTO.getCandidateAnswers();
			logger.debug("candidateAnswers: " + candidateAnswers);
			
			String isAttemptCorrect=mcLearnerAnswersDTO.getAttemptCorrect();
			logger.debug("isAttemptCorrect: " + isAttemptCorrect);
			
			logger.debug("requesting  createIndividualOptions");
			createIndividualOptions(request, candidateAnswers, mcQueContent, mcQueUsr, attempTime, timeZone, mark, passed, 
			        new Integer(highestAttemptOrder), isAttemptCorrect,mcService);
		}
		
	 }

    
    public static void createIndividualOptions(HttpServletRequest request, Map candidateAnswers, McQueContent mcQueContent, 
            McQueUsr mcQueUsr, Date attempTime, String timeZone, int mark,  boolean passed, Integer highestAttemptOrder, String isAttemptCorrect, 
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
                	    McUsrAttempt mcUsrAttempt=new McUsrAttempt(attempTime, timeZone, mcQueContent, mcQueUsr, mcOptsContent, IntegerMark, passed, highestAttemptOrder, new Boolean(isAttemptCorrect).booleanValue());
	    			    mcService.createMcUsrAttempt(mcUsrAttempt);
                    	logger.debug("created mcUsrAttempt in the db :" + mcUsrAttempt);
                	}
                }    
        	}    
    	}
    }
    
    
    /**
     * buildWeightsMap(HttpServletRequest request, Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
    public static Map buildWeightsMap(HttpServletRequest request, Long toolContentId, IMcService mcService)
    {
        logger.debug("starting buildWeightsMap : " + toolContentId);
    	Map mapWeights= new TreeMap(new McComparator());
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
    		mapWeights.put(mapIndex.toString(),mcQueContent.getWeight().toString());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	logger.debug("mapWeights : " + mapWeights);
    	return mapWeights;
    }
    
    
    public static Map buildQuestionContentMap(HttpServletRequest request, McContent mcContent, IMcService mcService)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	
        Iterator contentIterator=mcContent.getMcQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)contentIterator.next();
    		if (mcQueContent != null)
    		{
    			int displayOrder=mcQueContent.getDisplayOrder().intValue();
        		if (displayOrder != 0)
        		{
        			/* add the question to the questions Map in the displayOrder*/
        			mapQuestionsContent.put(new Integer(displayOrder).toString(),mcQueContent.getQuestion() + "      of weight: " + mcQueContent.getWeight().toString() + "%");
        		}
        		
        		/* prepare the first question's candidate answers for presentation*/ 
        		if (displayOrder == 1)
        		{
        			logger.debug("first question... ");
        			Long uid=mcQueContent.getUid();
        			logger.debug("uid : " + uid);
        			List listMcOptions=mcService.findMcOptionsContentByQueId(uid);
        			logger.debug("listMcOptions : " + listMcOptions);
        			Map mapOptionsContent=McUtils.generateOptionsMap(listMcOptions);
        			request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
        			logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
        		}
    		}
    	}
    	return mapQuestionsContent;
    }
    
    
    public static McGeneralLearnerFlowDTO buildMcGeneralLearnerFlowDTO(McContent mcContent)
    {
        logger.debug("starting buildMcGeneralLearnerFlowDTO: " + mcContent);
        McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO =new McGeneralLearnerFlowDTO();
        mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());
        mcGeneralLearnerFlowDTO.setActivityTitle(mcContent.getTitle());
        mcGeneralLearnerFlowDTO.setActivityInstructions(mcContent.getInstructions());
        mcGeneralLearnerFlowDTO.setPassMark(mcContent.getPassMark().toString());
        mcGeneralLearnerFlowDTO.setReportTitleLearner(mcContent.getReportTitle());
        mcGeneralLearnerFlowDTO.setLearnerProgress(new Boolean(false).toString());
        
        if (mcContent.isQuestionsSequenced()) 
            mcGeneralLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_SEQUENTIAL);
        else
            mcGeneralLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_COMBINED);
            
        
        logger.debug("continue buildMcGeneralLearnerFlowDTO: " + mcContent);
        mcGeneralLearnerFlowDTO.setTotalQuestionCount(new Integer(mcContent.getMcQueContents().size()).toString());
        logger.debug("final mcGeneralLearnerFlowDTO: " + mcGeneralLearnerFlowDTO);
        return mcGeneralLearnerFlowDTO;
    }
    
    
    public static List buildQuestionAndCandidateAnswersDTO(HttpServletRequest request, McContent mcContent, IMcService mcService)
    {
        logger.debug("starting buildQuestionAndCandidateAnswersDTO");
    	List questionAndCandidateAnswersList= new LinkedList();
    	logger.debug("mcContent uid : " + mcContent.getUid());
    	
    	List listQuestionEntries=mcService.getAllQuestionEntries(mcContent.getUid());
    	logger.debug("listQuestionEntries : " + listQuestionEntries);
    	
        Iterator listQuestionEntriesIterator=listQuestionEntries.iterator();
    	while (listQuestionEntriesIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listQuestionEntriesIterator.next();
    		logger.debug("mcQueContent : " + mcQueContent);
    		
    		McLearnerAnswersDTO mcLearnerAnswersDTO= new McLearnerAnswersDTO();
    		logger.debug("mcQueContent uid: " + mcQueContent.getUid());
    		
    		List listCandidateAnswers=mcService.findMcOptionNamesByQueId(mcQueContent.getUid());
    		logger.debug("listCandidateAnswers: " + listCandidateAnswers);
    		Map mapCandidateAnswers=convertToStringMap(listCandidateAnswers);
    		logger.debug("mapCandidateAnswers: " + mapCandidateAnswers);

    		
    		List listCandidateAnswerUids=mcService.findMcOptionUidsByQueId(mcQueContent.getUid());
    		logger.debug("listCandidateAnswerUids: " + listCandidateAnswerUids);
    		Map mapCandidateAnswerUids=convertToStringMap(listCandidateAnswerUids);
    		logger.debug("mapCandidateAnswerUids: " + mapCandidateAnswerUids);
    		
    		mcLearnerAnswersDTO.setQuestion(mcQueContent.getQuestion());
    		mcLearnerAnswersDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
    		mcLearnerAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
    		
    		mcLearnerAnswersDTO.setWeight(mcQueContent.getWeight().toString());
    		mcLearnerAnswersDTO.setCandidateAnswerUids(mapCandidateAnswerUids);
    		
    		mcLearnerAnswersDTO.setCandidateAnswers(mapCandidateAnswers);
    		logger.debug("current mcLearnerAnswersDTO: " + mcLearnerAnswersDTO);
    		
    		logger.debug("current mcLearnerAnswersDTO: " + mcLearnerAnswersDTO);

    		questionAndCandidateAnswersList.add(mcLearnerAnswersDTO);
    	}
    	
    	logger.debug("final questionAndCandidateAnswersList: " + questionAndCandidateAnswersList);
    	return questionAndCandidateAnswersList;
    }
    
    
	public static Map convertToStringMap(List list)
	{
		logger.debug("using convertToStringMap: " + list);
		Map map= new TreeMap(new McStringComparator());
		
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

    

    public static int getLearnerMarkAtLeast(Integer passMark, Map mapQuestionWeights)
    {
    	logger.debug("doing getLearnerMarkAtLeast");
    	logger.debug("passMark:" + passMark);
        logger.debug("mapQuestionWeights:" + mapQuestionWeights);
        
    	if ((passMark == null) || (passMark.intValue() == 0))
    	{
    		logger.debug("no passMark..");
    		return 0;
    	}
    	else if ((passMark != null) && (passMark.intValue() != 0))
    	{
    		int minimumQuestionCountToPass=calculateMinimumQuestionCountToPass(passMark, mapQuestionWeights);
    		logger.debug("minimumQuestionCountToPass: " + minimumQuestionCountToPass);
    		return minimumQuestionCountToPass;
    	}
    	return 0;
    }


    public static int calculateMinimumQuestionCountToPass(Integer passMark, Map mapQuestionWeights)
    {
    	logger.debug("calculating minimumQuestionCountToPass: mapQuestionWeights: " + mapQuestionWeights + " passmark: " + passMark);
    	logger.debug("passMark: " + passMark);
    	logger.debug("original mapQuestionWeights: " + mapQuestionWeights);
    	
    	boolean mapExcluded=false;
    	int minimumQuestionCount=0;
    	int totalHighestWeights=0;
    	while (totalHighestWeights <= passMark.intValue())
    	{
    		logger.debug("totalHighestWeights versus passMark: " + totalHighestWeights + " versus" + passMark);
        	int highestWeight=getHighestWeight(mapQuestionWeights, mapExcluded);
        	logger.debug("highestWeight: " + highestWeight);
        	totalHighestWeights=totalHighestWeights + highestWeight;
        	logger.debug("totalHighestWeights: " + totalHighestWeights);
        	mapQuestionWeights=rebuildWeightsMapExcludeHighestWeight(mapQuestionWeights, highestWeight);
        	mapExcluded=true;
    		logger.debug("mapQuestionWeights: " + mapQuestionWeights);
    		++minimumQuestionCount;
    		if (mapQuestionWeights.size() == 0)
    		{
    			logger.debug("no more weights: ");
    			break;
    		}
    	}
    	logger.debug("returning minimumQuestionCount: " + minimumQuestionCount);
    	return minimumQuestionCount;
    }
    
    
	public static int getHighestWeight(Map mapQuestionWeights, boolean mapExcluded)
	{
	    logger.debug("mapExcluded: " + mapExcluded);
	   
		if ((mapQuestionWeights.size() == 1) && (!mapExcluded))
		{
			logger.debug("using map with 1 question only");
			/*the only alternative is 100*/
			return 100;
		}
		
	   logger.debug("continue excluding map");
	   Iterator itMap = mapQuestionWeights.entrySet().iterator();
 	   int highestWeight=0; 	   
       while (itMap.hasNext()) 
       {
       		Map.Entry pair = (Map.Entry)itMap.next();
       		String weight=pair.getValue().toString();
       		int intWeight=new Integer(weight).intValue();
       		
			if (intWeight > highestWeight)
				highestWeight= intWeight;
       }
       return highestWeight;
	}

	
	public static Map rebuildWeightsMapExcludeHighestWeight(Map mapQuestionWeights, int highestWeight)
	{
		logger.debug("doing rebuildWeightsMapExcludeHighestWeight: " + mapQuestionWeights);
		logger.debug("doing highestWeight: " + highestWeight);
		
	   Map mapWeightsExcludeHighestWeight= new TreeMap(new McComparator());
	   
	   Iterator itMap = mapQuestionWeights.entrySet().iterator();
	   Long mapIndex=new Long(1);
       while (itMap.hasNext()) 
       {
       		Map.Entry pair = (Map.Entry)itMap.next();
       		String weight=pair.getValue().toString();
       		int intWeight=new Integer(weight).intValue();
       		logger.debug("intWeight: " + intWeight);
       		logger.debug("intWeight versus highestWeight:" + intWeight + " versus" + highestWeight);
       		if (intWeight != highestWeight)
       		{
           		mapWeightsExcludeHighestWeight.put(mapIndex.toString(),weight);
    	   		mapIndex=new Long(mapIndex.longValue()+1);
       		}
       		else
       		{
       			logger.debug("excluding highest weight from the reconstructed map: " + intWeight);
       		}
       }
       logger.debug("returning mapWeightsExcludeHighestWeight: " + mapWeightsExcludeHighestWeight);
       return mapWeightsExcludeHighestWeight; 
	}

    
    /**
     * removes Learning session attributes
     * cleanUpLearningSession(HttpServletRequest request)
     * 
     * @param request
     */
    public static void cleanUpLearningSession(HttpServletRequest request)
    {
    	//request.getSession().removeAttribute(USER_ID);
    	request.getSession().removeAttribute(TOOL_CONTENT_ID);
    	request.getSession().removeAttribute(TOOL_SESSION_ID);
    	request.getSession().removeAttribute(QUESTION_LISTING_MODE);
    	request.getSession().removeAttribute(QUESTION_LISTING_MODE_SEQUENTIAL);
    	request.getSession().removeAttribute(QUESTION_LISTING_MODE_COMBINED);
    	request.getSession().removeAttribute(MAP_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_QUESTION_CONTENT_LEARNER);
    	request.getSession().removeAttribute(TOTAL_QUESTION_COUNT);
    	request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
    	request.getSession().removeAttribute(LEARNER_LAST_ATTEMPT_ORDER);
    	request.getSession().removeAttribute(LEARNER_BEST_MARK);
    	request.getSession().removeAttribute(PASSMARK);
    	request.getSession().removeAttribute(REPORT_TITLE_LEARNER);
    	request.getSession().removeAttribute(IS_CONTENT_IN_USE);
    	request.getSession().removeAttribute(IS_TOOL_ACTIVITY_OFFLINE);
    	request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
    	request.getSession().removeAttribute(IS_SHOW_FEEDBACK);
    	request.getSession().removeAttribute(TOTAL_COUNT_REACHED);
    	request.getSession().removeAttribute(COUNT_SESSION_COMPLETE);
    	request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
    	request.getSession().removeAttribute(TOP_MARK);
    	request.getSession().removeAttribute(LOWEST_MARK);
    	request.getSession().removeAttribute(AVERAGE_MARK);
    	request.getSession().removeAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_LEARNER_ASSESSMENT_RESULTS);
    	request.getSession().removeAttribute(MAP_LEARNER_FEEDBACK_INCORRECT);
    	request.getSession().removeAttribute(MAP_LEARNER_FEEDBACK_CORRECT);
    	request.getSession().removeAttribute(MAP_QUE_ATTEMPTS);
    	request.getSession().removeAttribute(MAP_QUE_CORRECT_ATTEMPTS);
    	request.getSession().removeAttribute(MAP_QUE_INCORRECT_ATTEMPTS);
    	request.getSession().removeAttribute(MAP_QUESTION_WEIGHTS);
    	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);
    	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
    	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
    	request.getSession().removeAttribute(LEARNING_MODE);
    	request.getSession().removeAttribute(PREVIEW_ONLY);
    	request.getSession().removeAttribute(LEARNER_PROGRESS);
    	request.getSession().removeAttribute(LEARNER_PROGRESS_USERID);
    }
 }
