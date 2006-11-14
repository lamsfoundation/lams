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
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Authoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class AuthoringUtil implements McAppConstants {
	static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());
 	
	/**
	 * setRadioboxes(McContent mcContent, McAuthoringForm mcAuthoringForm)
	 * 
	 * set values of radioboxes for an existing content
	 * 
	 * @param mcContent
	 * @param mcAuthoringForm
	 */
	protected static void setRadioboxes(McContent mcContent, McAuthoringForm mcAuthoringForm)
	{
		mcAuthoringForm.setQuestionsSequenced(mcContent.isQuestionsSequenced()?"1":"0");
		mcAuthoringForm.setShowMarks(mcContent.isShowMarks()?"1":"0");
		mcAuthoringForm.setRetries(mcContent.isRetries()?"1":"0");
		mcAuthoringForm.setSln(mcContent.isShowReport()?"1":"0");
		mcAuthoringForm.setReflect(mcContent.isReflect()?"1":"0");
	}

    
    /**
     * verifyMapNoEmptyString(Map map)
     * 
     * makes sure there is data in the map
     * 
     * @param map
     * @return boolean
     */
    public static boolean verifyMapNoEmptyString(Map map)
	{
    	Iterator itMap = map.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getValue() != null) && (pairs.getValue().toString().length() == 0))
            	return false;
            
		}
    	return true;
	}
    
	
    
    /**
     * removes only unused question entries from the db. It keeps the valid entries since they get updated.
     *  
     * cleanupRedundantQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent, McContent mcContent)
     * 
     * @param request
     * @param existingQuestions
     * @param mapQuestionsContent
     * @param mcContent
     */
    public static void cleanupRedundantQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent, McContent mcContent, IMcService mcService)
    {
    	logger.debug("doing cleanupRedundantQuestions...");
    	logger.debug("using  existingQuestions: " + existingQuestions);
    	logger.debug("using  mapQuestionsContent: " + mapQuestionsContent);
    	logger.debug("using  mcContent: " + mcContent);
    	
        /*remove ununsed question entries from the db */
        boolean questionFound=false;
        Iterator itExistingQuestions = existingQuestions.iterator();
        while (itExistingQuestions.hasNext()) 
        {
        	McQueContent mcQueContent=(McQueContent)itExistingQuestions.next();
    		logger.debug("mcQueContent:" + mcQueContent);
    		
    		Iterator itNewQuestionsMap = mapQuestionsContent.entrySet().iterator();
    		questionFound=false;
            while (itNewQuestionsMap.hasNext()) 
            {
            	Map.Entry pairs = (Map.Entry)itNewQuestionsMap.next();
                logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
                logger.debug("mcQueContent.getQuestion(): " + mcQueContent.getQuestion() + "--" + pairs.getValue());
	    		
	    		if (mcQueContent.getQuestion().equals(pairs.getValue().toString()))
				{
	    			logger.debug("equals mcQueContent.getQuestion(): " + mcQueContent.getQuestion() + "--" + pairs.getValue());
	    			questionFound=true;
	            	break;
				}
            }
            
            if (questionFound == false)
    		{
    			String deletableQuestion=mcQueContent.getQuestion();
    			logger.debug("found is false, delete this question " + deletableQuestion);
    			mcQueContent=mcService.getQuestionContentByQuestionText(deletableQuestion, mcContent.getUid());
    			
    			logger.debug("found is false, delete this question " + mcQueContent);
    			if (mcQueContent != null)
    			{
    			    logger.debug("first removing from collection " + mcQueContent);
    			    mcContent.getMcQueContents().remove(mcQueContent);
    			    logger.debug("removed from collection ");
    			    
    				mcService.removeMcQueContent(mcQueContent);
        			logger.debug("removed mcQueContent from the db: " + mcQueContent);	
    			}
    		}
        }    		
    }
    

    
	/**
	 * 
	 * rebuildStartupGeneralOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
	 * 
	 * builds a map to hold all the candidate answers for all the questions by accessing the db
	 *
	 * @param request
	 * @param mapQuestionsUidContent
	 * @return
	 */
    public static Map rebuildStartupGeneralOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent, IMcService mcService)
	{
		Map mapStartupGeneralOptionsContent= new TreeMap(new McComparator());

    	Iterator itMap = mapQuestionsUidContent.entrySet().iterator();
    	Long mapIndex=new Long(1);
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            String currentQuestionUid=pairs.getValue().toString();
            logger.debug("currentQuestionUid: " +  currentQuestionUid);
            List listQuestionOptions=mcService.findMcOptionsContentByQueId(new Long(currentQuestionUid));
            logger.debug("listQuestionOptions: " +  listQuestionOptions);
            Map mapQuestionOptions=generateOptionsMap(listQuestionOptions);
            mapStartupGeneralOptionsContent.put(mapIndex.toString(),mapQuestionOptions);
            mapIndex=new Long(mapIndex.longValue()+1);
		}
		return mapStartupGeneralOptionsContent;
	}
    
    
    protected static List removeBlankEntries(List list)
    {
        List newList= new LinkedList();
        
		Iterator listIterator=list.iterator();
    	while (listIterator.hasNext())
    	{
    	    McCandidateAnswersDTO mcCandidateAnswersDTO=(McCandidateAnswersDTO)listIterator.next();
    		logger.debug("mcCandidateAnswersDTO: " +  mcCandidateAnswersDTO);
    		
    		if (mcCandidateAnswersDTO != null)
    		{
    		    String ca=mcCandidateAnswersDTO.getCandidateAnswer();
    		    logger.debug("ca: " + ca);
    		    
    		    if ((ca != null) && (ca.length() > 0))
    		        newList.add(mcCandidateAnswersDTO);
    		}
    	}

    	return newList;
    }
    

    /**
     * 
     * generateOptionsMap(List listQuestionOptions)
	 * 
	 * builds a questions map from questions list
	 *
	 * @param listQuestionOptions
	 * @return Map
	 */
	public static Map generateOptionsMap(List listQuestionOptions)
	{
		Map mapOptsContent= new TreeMap(new McComparator());

		Iterator listIterator=listQuestionOptions.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
    		logger.debug("mcOptsContent: " +  mcOptsContent);
    		mapOptsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return mapOptsContent;
	}
    
    
    /**
     * updates existing questions while persisting the questions
     * 
     * updateExistingQuestionContent(HttpServletRequest request, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McQueContent mcQueContent, String displayOrder)
     *  
     * @param request
     * @param mapFeedbackIncorrect
     * @param mapFeedbackCorrect
     * @param mcQueContent
     * @param displayOrder
     */
    public static void updateExistingQuestionContent(HttpServletRequest request, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, 
            McQueContent mcQueContent, String displayOrder, IMcService mcService)
    {
    	logger.debug("doing updateExistingQuestionContent...");
    	logger.debug("using displayOrder: " + displayOrder);
    	
    	String incorrectFeedback=(String)mapFeedbackIncorrect.get(displayOrder);
    	logger.debug("new  incorrectFeedback will be :" + incorrectFeedback);
    	String correctFeedback=(String)mapFeedbackCorrect.get(displayOrder);
    	logger.debug("new correctFeedback will be :" + correctFeedback);
    	
    	mcQueContent.setDisplayOrder(new Integer(displayOrder));
    	
    	mcService.saveOrUpdateMcQueContent(mcQueContent);
		logger.debug("updated mcQueContent in the db: " + mcQueContent);
    }

    
	/**
	 * builds a map to hold question texts
	 * rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId)
	 *
	 * @param request
	 * @param toolContentId
	 * @return Map
	 */
    public static Map rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId, IMcService mcService)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	logger.debug("toolContentId:" + toolContentId);

		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);

    	List list=mcService.refreshQuestionContent(mcContent.getUid());
		logger.debug("refreshed list:" + list);

		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listIterator.next();
    		logger.debug("mcQueContent:" + mcQueContent);
    		mapQuestionsContent.put(mapIndex.toString(),mcQueContent.getQuestion());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}

    	logger.debug("refreshed Map:" + mapQuestionsContent);
    	return mapQuestionsContent;
    }
    

    public static Map rebuildFeedbackMapfromDB(HttpServletRequest request, Long toolContentId, IMcService mcService)
    {
    	Map map= new TreeMap(new McComparator());
    	logger.debug("toolContentId:" + toolContentId);

		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);

    	List list=mcService.refreshQuestionContent(mcContent.getUid());
		logger.debug("refreshed list:" + list);

		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listIterator.next();
    		logger.debug("mcQueContent:" + mcQueContent);
    
    		String feedback=mcQueContent.getFeedback();
    		logger.debug("feedback:" + feedback);
    		
    		map.put(mapIndex.toString(),feedback);
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}

    	logger.debug("refreshed Map:" + map);
    	return map;
    }

    
    
    /**
     * builds a map to hold persisted uid values for questions
     * rebuildQuestionUidMapfromDB(HttpServletRequest request, Long toolContentId)
     *
     * @param request
     * @param toolContentId
     * @return Map
     */
    public static Map rebuildQuestionUidMapfromDB(HttpServletRequest request, Long toolContentId, IMcService mcService)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	logger.debug("toolContentId:" + toolContentId);

		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);

		if (mcContent != null)
		{
			List list=mcService.refreshQuestionContent(mcContent.getUid());
			logger.debug("refreshed list:" + list);

			Iterator listIterator=list.iterator();
	    	Long mapIndex=new Long(1);
	    	while (listIterator.hasNext())
	    	{
	    		McQueContent mcQueContent=(McQueContent)listIterator.next();
	    		logger.debug("mcQueContent:" + mcQueContent);
	    		mapQuestionsContent.put(mapIndex.toString(),mcQueContent.getUid());
	    		mapIndex=new Long(mapIndex.longValue()+1);
	    	}
		}

		logger.debug("refreshed Map:" + mapQuestionsContent);
    	return mapQuestionsContent;
    }



	/**
	 * finds whether a candidate answer is selected or not
	 * isOptionSelected(Map mapGeneralSelectedOptionsContent, String optionText, String questionIndex)
	 * 
	 * @param mapGeneralSelectedOptionsContent
	 * @param optionText
	 * @param questionIndex
	 * @return boolean
	 */
	public static boolean isOptionSelected(Map mapGeneralSelectedOptionsContent, String optionText, String questionIndex)
	{
	   Iterator itGSOMap = mapGeneralSelectedOptionsContent.entrySet().iterator();
	   logger.debug("questionIndex: " + questionIndex);
	   logger.debug("optionText: " + optionText);
	   while (itGSOMap.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itGSOMap.next();
            if (pairs.getKey().toString().equals(questionIndex))
            {
            	Map currentOptionsMap= (Map)pairs.getValue();
            	logger.debug("currentOptionsMap: " + currentOptionsMap);
            	boolean isOptionSelectedInMap=isOptionSelectedInMap(optionText, currentOptionsMap);
            	logger.debug("isOptionSelectedInMap: " + isOptionSelectedInMap);
            	return isOptionSelectedInMap;
            }
        }
	   return false;
	}
	
	
	/**
	 * checks the existence of a candidate answer in the options map
	 * isOptionSelectedInMap(String optionText, Map currentOptionsMap)
	 * 
	 * @param optionText
	 * @param currentOptionsMap
	 * @return
	 */
	public static boolean isOptionSelectedInMap(String optionText, Map currentOptionsMap)
	{
		logger.debug("optionText: " + optionText);
		Iterator itCOMap = currentOptionsMap.entrySet().iterator();
		while (itCOMap.hasNext()) 
	    {
			Map.Entry pairs = (Map.Entry)itCOMap.next();
			if (pairs.getValue().toString().equals(optionText))
			{
				logger.debug("option text found in the map: " + optionText);
				return true;
			}
	    }
		return false;
	}


    
    
   /**
    * List swapNodes(List listQuestionContentDTO, String questionIndex, String direction)
    * 
    * swappes McQuestionContentDTO nodes in the list
    * 
    * @param listQuestionContentDTO
    * @param questionIndex
    * @param direction
    * @return
    */ 
    protected static List swapNodes(List listQuestionContentDTO, String questionIndex, String direction)
    {
        logger.debug("swapNodes:");
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        logger.debug("questionIndex:" + questionIndex);
        logger.debug("direction:" + direction);

        int intQuestionIndex=new Integer(questionIndex).intValue();
        int intOriginalQuestionIndex=intQuestionIndex;
        logger.debug("intQuestionIndex:" + intQuestionIndex);
        
        int replacedNodeIndex=0;
        if (direction.equals("down"))
        {
            logger.debug("direction down:");
            replacedNodeIndex=++intQuestionIndex;
        }
        else
        {
            logger.debug("direction up:");
            replacedNodeIndex=--intQuestionIndex;
            
        }
        logger.debug("replacedNodeIndex:" + replacedNodeIndex);
        logger.debug("replacing nodes:" + intOriginalQuestionIndex + " and " + replacedNodeIndex);
        
        McQuestionContentDTO mainNode=extractNodeAtDisplayOrder(listQuestionContentDTO, intOriginalQuestionIndex);
        logger.debug("mainNode:" + mainNode);
        

        McQuestionContentDTO replacedNode=extractNodeAtDisplayOrder(listQuestionContentDTO, replacedNodeIndex);
        logger.debug("replacedNode:" + replacedNode);

        if ((mainNode == null) || (replacedNode == null))
        {
            logger.debug("mainNode/replacedNode is null");
            return listQuestionContentDTO;
        }

        
        
        List listFinalQuestionContentDTO=new LinkedList();
        
        listFinalQuestionContentDTO=reorderSwappedListQuestionContentDTO(listQuestionContentDTO, intOriginalQuestionIndex,
                replacedNodeIndex, mainNode, replacedNode);
        
        
	    logger.debug("listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }

    
    
    /**
     * List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO, int intOriginalQuestionIndex, 
            int replacedNodeIndex, McQuestionContentDTO mainNode, McQuestionContentDTO replacedNode)
            
     * @param listQuestionContentDTO
     * @param intOriginalQuestionIndex
     * @param replacedNodeIndex
     * @param mainNode
     * @param replacedNode
     * @return
     */
    protected  static List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO, int intOriginalQuestionIndex, 
            int replacedNodeIndex, McQuestionContentDTO mainNode, McQuestionContentDTO replacedNode)
    {
        logger.debug("reorderSwappedListQuestionContentDTO: intOriginalQuestionIndex:" + intOriginalQuestionIndex);
        logger.debug("reorderSwappedListQuestionContentDTO: replacedNodeIndex:" + replacedNodeIndex);
        logger.debug("mainNode: " + mainNode);
        logger.debug("replacedNode: " + replacedNode);
        
        
        List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        queIndex++;
	        McQuestionContentDTO tempNode=new McQuestionContentDTO();
	        
            if ((!mcQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) && 
                 !mcQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString()))
            {
            	logger.debug("normal copy ");
            	tempNode.setQuestion(mcQuestionContentDTO.getQuestion());
            	tempNode.setDisplayOrder(mcQuestionContentDTO.getDisplayOrder());
            	tempNode.setFeedback(mcQuestionContentDTO.getFeedback());
            	tempNode.setListCandidateAnswersDTO(mcQuestionContentDTO.getListCandidateAnswersDTO());
            	tempNode.setCaCount(mcQuestionContentDTO.getCaCount());
            	tempNode.setMark(mcQuestionContentDTO.getMark());
            	
            }
            else if (mcQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString()))
            {
            	logger.debug("move type 1 ");
            	tempNode.setQuestion(replacedNode.getQuestion());
            	tempNode.setDisplayOrder(replacedNode.getDisplayOrder());
            	tempNode.setFeedback(replacedNode.getFeedback());
            	tempNode.setListCandidateAnswersDTO(replacedNode.getListCandidateAnswersDTO());
            	tempNode.setCaCount(replacedNode.getCaCount());
            	tempNode.setMark(replacedNode.getMark());
            }
            else if (mcQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString()))
            {
            	logger.debug("move type 2 ");
            	tempNode.setQuestion(mainNode.getQuestion());
            	tempNode.setDisplayOrder(mainNode.getDisplayOrder());
            	tempNode.setFeedback(mainNode.getFeedback());
            	tempNode.setListCandidateAnswersDTO(mainNode.getListCandidateAnswersDTO());
            	tempNode.setCaCount(mainNode.getCaCount());
            	tempNode.setMark(mainNode.getMark());
            }
	        
	        listFinalQuestionContentDTO.add(tempNode);
	    }

	    
        logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }


    /**
     * List swapCandidateNodes(List listCandidates, String candidateIndex, String direction)
     * 
     * swaps McCandidateAnswersDTO in the list
     * 
     * 
     * @param listCandidates
     * @param candidateIndex
     * @param direction
     * @return
     */
    protected static List swapCandidateNodes(List listCandidates, String candidateIndex, String direction)
    {
        logger.debug("swapCandidateNodes:");
        logger.debug("listCandidates:" + listCandidates);
        logger.debug("candidateIndex:" + candidateIndex);
        logger.debug("direction:" + direction);

        int intCandidateIndex=new Integer(candidateIndex).intValue();
        int intOriginalCandidateIndex=intCandidateIndex;
        logger.debug("intCandidateIndex:" + intCandidateIndex);
        
        int replacedNodeIndex=0;
        if (direction.equals("down"))
        {
            logger.debug("direction down:");
            replacedNodeIndex=++intCandidateIndex;
        }
        else
        {
            logger.debug("direction up:");
            replacedNodeIndex=--intCandidateIndex;
            
        }
        logger.debug("replacedNodeIndex:" + replacedNodeIndex);
        logger.debug("replacing nodes:" + intOriginalCandidateIndex + " and " + replacedNodeIndex);
        
        McCandidateAnswersDTO mainNode=extractCandidateAtOrder(listCandidates, intOriginalCandidateIndex);
        logger.debug("mainNode:" + mainNode);
        

        McCandidateAnswersDTO replacedNode=extractCandidateAtOrder(listCandidates, replacedNodeIndex);
        logger.debug("replacedNode:" + replacedNode);
        
        if ((mainNode == null) || (replacedNode == null))
        {
            logger.debug("mainNode/replacedNode is null");
            return listCandidates;
        }
        

        List listFinalCandidateDTO=new LinkedList();
        
        listFinalCandidateDTO=reorderSwappedListCandidateDTO(listCandidates, intOriginalCandidateIndex,
                replacedNodeIndex, mainNode, replacedNode);
        
        
	    logger.debug("listFinalCandidateDTO:" + listFinalCandidateDTO);
        return listFinalCandidateDTO;
    }

    
    /**
     * reorderSwappedListCandidateDTO(List listCandidates, int intOriginalCandidateIndex, 
            int replacedNodeIndex, McCandidateAnswersDTO mainNode, McCandidateAnswersDTO replacedNode)
            
            
     * @param listCandidates
     * @param intOriginalCandidateIndex
     * @param replacedNodeIndex
     * @param mainNode
     * @param replacedNode
     * @return
     */
    protected  static List reorderSwappedListCandidateDTO(List listCandidates, int intOriginalCandidateIndex, 
            int replacedNodeIndex, McCandidateAnswersDTO mainNode, McCandidateAnswersDTO replacedNode)
    {
        logger.debug("reorderSwappedListCandidateDTO: intOriginalQuestionIndex:" + intOriginalCandidateIndex);
        logger.debug("reorderSwappedListCandidateDTO: replacedNodeIndex:" + replacedNodeIndex);
        logger.debug("mainNode: " + mainNode);
        logger.debug("replacedNode: " + replacedNode);
        
        
        List listFinalCandidatesDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listCandidates.iterator();
	    while (listIterator.hasNext())
	    {
	        McCandidateAnswersDTO mcCandidateAnswersDTO= (McCandidateAnswersDTO)listIterator.next();
	        logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
	        logger.debug("mcCandidateAnswersDTO candidateAnswer:" + mcCandidateAnswersDTO.getCandidateAnswer());
	        queIndex++;

	        McCandidateAnswersDTO tempNode=new McCandidateAnswersDTO();
	        
	        
            if ((!new Integer(queIndex).toString().equals(new Integer(intOriginalCandidateIndex).toString())) && 
                 !new Integer(queIndex).toString().equals(new Integer(replacedNodeIndex).toString()))
            {
            	logger.debug("normal copy ");
            	tempNode.setCandidateAnswer(mcCandidateAnswersDTO.getCandidateAnswer());
            	tempNode.setCorrect(mcCandidateAnswersDTO.getCorrect());
            }
            else if (new Integer(queIndex).toString().equals(new Integer(intOriginalCandidateIndex).toString()))
            {
            	logger.debug("move type 1 ");
            	tempNode.setCandidateAnswer(replacedNode.getCandidateAnswer());
            	tempNode.setCorrect(replacedNode.getCorrect());
            }
            else if (new Integer(queIndex).toString().equals(new Integer(replacedNodeIndex).toString()))
            {
            	logger.debug("move type 2 ");
            	tempNode.setCandidateAnswer(mainNode.getCandidateAnswer());
            	tempNode.setCorrect(mainNode.getCorrect());
            }
	        
            listFinalCandidatesDTO.add(tempNode);
	    }

        logger.debug("final listFinalCandidatesDTO:" + listFinalCandidatesDTO);
        return listFinalCandidatesDTO;
    }
    
    
    /**
     * McQuestionContentDTO extractNodeAtDisplayOrder(List listQuestionContentDTO, int intOriginalQuestionIndex)
     * 
     * returns McQuestionContentDTO  in the specified order of the list 
     * 
     * @param listQuestionContentDTO
     * @param intOriginalQuestionIndex
     * @return
     */
    protected static McQuestionContentDTO extractNodeAtDisplayOrder(List listQuestionContentDTO, int intOriginalQuestionIndex)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        logger.debug("intOriginalQuestionIndex:" + intOriginalQuestionIndex);
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        while (listIterator.hasNext())
        {
            McQuestionContentDTO mcQuestionContentDTO=(McQuestionContentDTO)listIterator.next();
            logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
            logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
            
            logger.debug("intOriginalQuestionIndex versus displayOrder:" + new Integer(intOriginalQuestionIndex).toString() + " versus " + 
                    mcQuestionContentDTO.getDisplayOrder());
            if (new Integer(intOriginalQuestionIndex).toString().equals(mcQuestionContentDTO.getDisplayOrder()))
            {
                logger.debug("node found:" + mcQuestionContentDTO);
                return mcQuestionContentDTO;
            }
        }
        return null;
    }


    
    /**
     * McCandidateAnswersDTO extractCandidateAtOrder(List listCandidates, int intOriginalCandidateIndex)
     * 
     * @param listCandidates
     * @param intOriginalCandidateIndex
     * @return
     */
    protected static McCandidateAnswersDTO extractCandidateAtOrder(List listCandidates, int intOriginalCandidateIndex)
    {
        logger.debug("listCandidates:" + listCandidates);
        logger.debug("intOriginalCandidateIndex:" + intOriginalCandidateIndex);
        
        int counter=0;
        Iterator listIterator=listCandidates.iterator();
        while (listIterator.hasNext())
        {
            ++counter;
            McCandidateAnswersDTO mcCandidateAnswerDTO=(McCandidateAnswersDTO)listIterator.next();
            logger.debug("mcCandidateAnswerDTO:" + mcCandidateAnswerDTO);
            
            logger.debug("counter:" + counter);
            
            if (new Integer(intOriginalCandidateIndex).toString().equals(new Integer(counter).toString()))
            {
                logger.debug("node found:" + mcCandidateAnswerDTO);
                return mcCandidateAnswerDTO;
            }
        }
        return null;
    }
    
    protected static String getTotalMark(List listQuestionContentDTO)
    {
        logger.debug("starting getTotalMark:" + listQuestionContentDTO);
        
        Map mapMarks = extractMapMarks(listQuestionContentDTO);
        
        int intTotalMark=0;
    	Iterator itMap = mapMarks.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            String mark= (String)pairs.getValue();
            logger.debug("mark: " +  mark);
            
            if (mark != null)
            {
                int intMark= new Integer(mark).intValue();
                intTotalMark+=intMark;
                logger.debug("current intTotalMark: " +  intTotalMark);
            }
		}
    	logger.debug("final  intTotalMark: " +  intTotalMark);
    	
    	String strFinalTotalMark= new Integer(intTotalMark).toString();
    	logger.debug("final strFinalTotalMark: " +  strFinalTotalMark);
    	return strFinalTotalMark;
    }
    
    
    /**
     * extractMapQuestionContent(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapQuestionContent(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapQuestionContent= new TreeMap(new McComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            McQuestionContentDTO mcQuestionContentDTO=(McQuestionContentDTO)listIterator.next();
            logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
            logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapQuestionContent.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getQuestion());
        }
        logger.debug("mapQuestionContent:" + mapQuestionContent);
        return mapQuestionContent;
    }

    
    /**
     * List reorderListCandidatesDTO(List candidates)
     * 
     * @param candidates
     * @return
     */
    protected  static List reorderListCandidatesDTO(List candidates)
    {
        logger.debug("reorderListCandidatesDTO");
    	logger.debug("reorderListCandidatesDTO:" + candidates);
    	
    	List listFinalCandidatesDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=candidates.iterator();
	    while (listIterator.hasNext())
	    {
	        McCandidateAnswersDTO mcCandidateAnswersDTO= (McCandidateAnswersDTO)listIterator.next();
	        logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
	        logger.debug("mcCandidateAnswersDTO answer:" + mcCandidateAnswersDTO.getCandidateAnswer());
	        
	        String answer=mcCandidateAnswersDTO.getCandidateAnswer();
	        logger.debug("answer:" + answer);
	        
	        String correct=mcCandidateAnswersDTO.getCorrect(); 
	        logger.debug("correct:" + correct);

	        
	        if ((answer != null) && (!answer.equals("")))
    		{
	            	mcCandidateAnswersDTO.setCandidateAnswer(answer);
	            	mcCandidateAnswersDTO.setCorrect(correct);

	            	listFinalCandidatesDTO.add(mcCandidateAnswersDTO);

    		}
	    }
	    
	    
        logger.debug("final listFinalCandidatesDTO:" + listFinalCandidatesDTO);
        return listFinalCandidatesDTO;
    }

    
    /**
     * reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex)
     * 
     * @param listQuestionContentDTO
     * @param excludeQuestionIndex
     * @return
     */
    protected  static List reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex)
    {
        logger.debug("reorderListQuestionContentDTO");
    	logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
    	logger.debug("excludeQuestionIndex:" + excludeQuestionIndex);
    	
    	List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        
	        String question=mcQuestionContentDTO.getQuestion();
	        logger.debug("question:" + question);
	        
	        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        String feedback=mcQuestionContentDTO.getFeedback();
	        logger.debug("feedback:" + feedback);
	        
	        String caCount= mcQuestionContentDTO.getCaCount();
	        logger.debug("caCount:" + caCount);
	        
	        List caList=mcQuestionContentDTO.getListCandidateAnswersDTO(); 
	        logger.debug("caList:" + caList);
	        
	        
	        logger.debug("displayOrder versus excludeQuestionIndex :" + displayOrder + " versus " + excludeQuestionIndex);
	        
	        if ((question != null) && (!question.equals("")))
    		{
	            if (!displayOrder.equals(excludeQuestionIndex))
	            {
		            ++queIndex;
		            logger.debug("using queIndex:" + queIndex);
		            
		            mcQuestionContentDTO.setQuestion(question);
		            mcQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		            mcQuestionContentDTO.setFeedback(feedback);
		            mcQuestionContentDTO.setListCandidateAnswersDTO(caList);

		            logger.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
				    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO().size()).toString());
		            
		            listFinalQuestionContentDTO.add(mcQuestionContentDTO);
	            }
    		}
	    }
	    
	    
        logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }

    
    /**
     * List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected  static List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO)
    {
        logger.debug("reorderListQuestionContentDTO");
    	logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
    	List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        
	        String question=mcQuestionContentDTO.getQuestion();
	        logger.debug("question:" + question);
	        
	        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        String feedback=mcQuestionContentDTO.getFeedback();
	        logger.debug("feedback:" + feedback);
	        
	        List caList=mcQuestionContentDTO.getListCandidateAnswersDTO(); 
	        logger.debug("caList:" + caList);
	        
	        String caCount=mcQuestionContentDTO.getCaCount(); 
	        logger.debug("caCount:" + caCount);
	        
	        String mark=mcQuestionContentDTO.getMark();
	        logger.debug("mark:" + mark);
	        
	        
	        if ((question != null) && (!question.equals("")))
    		{
		            ++queIndex;
		            logger.debug("using queIndex:" + queIndex);
		            
		            mcQuestionContentDTO.setQuestion(question);
		            mcQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		            mcQuestionContentDTO.setFeedback(feedback);
		            mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		            mcQuestionContentDTO.setCaCount(caCount);
		            mcQuestionContentDTO.setMark(mark);
		            listFinalQuestionContentDTO.add(mcQuestionContentDTO);
    		}
	    }
	    
	    
        logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }

    
    /**
     * reorderUpdateListQuestionContentDTO(List listQuestionContentDTO, 
            										McQuestionContentDTO mcQuestionContentDTONew, 
            										String editableQuestionIndex)
            										
     * @param listQuestionContentDTO
     * @param mcQuestionContentDTONew
     * @param editableQuestionIndex
     * @return
     */
    protected  static List reorderUpdateListQuestionContentDTO(List listQuestionContentDTO, 
            										McQuestionContentDTO mcQuestionContentDTONew, 
            										String editableQuestionIndex)
    {
        logger.debug("reorderUpdateListQuestionContentDTO");
    	logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
    	logger.debug("mcQuestionContentDTONew:" + mcQuestionContentDTONew);
    	logger.debug("editableQuestionIndex:" + editableQuestionIndex);
    	
    	
    	List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        
	        ++queIndex;
	        logger.debug("using queIndex:" + queIndex);
	        String question=mcQuestionContentDTO.getQuestion();
	        logger.debug("question:" + question);
	        
	        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        String feedback=mcQuestionContentDTO.getFeedback();
	        logger.debug("feedback:" + feedback);
	        
	        String mark=mcQuestionContentDTO.getMark(); 
	        logger.debug("mark:" + mark);

	        List caList=mcQuestionContentDTO.getListCandidateAnswersDTO();
	        logger.debug("caList:" + caList);

	        String caCount=mcQuestionContentDTO.getCaCount();
	        logger.debug("caCount:" + caCount);
	        
	        
	        if (displayOrder.equals(editableQuestionIndex))
            {
	            logger.debug("displayOrder equals editableQuestionIndex:" + editableQuestionIndex);
		        mcQuestionContentDTO.setQuestion(mcQuestionContentDTONew.getQuestion());
	            mcQuestionContentDTO.setDisplayOrder(mcQuestionContentDTONew.getDisplayOrder());
	            mcQuestionContentDTO.setFeedback(mcQuestionContentDTONew.getFeedback());
	            mcQuestionContentDTO.setMark(mcQuestionContentDTONew.getMark());
	            mcQuestionContentDTO.setCaCount(mcQuestionContentDTONew.getCaCount());
	            mcQuestionContentDTO.setListCandidateAnswersDTO(mcQuestionContentDTONew.getListCandidateAnswersDTO());
	            
	            listFinalQuestionContentDTO.add(mcQuestionContentDTO);
	        }
	        else
	        {
	            logger.debug("displayOrder does not equal editableQuestionIndex:" + editableQuestionIndex);
	            mcQuestionContentDTO.setQuestion(question);
	            mcQuestionContentDTO.setDisplayOrder(displayOrder);
	            mcQuestionContentDTO.setFeedback(feedback);
	            mcQuestionContentDTO.setMark(mark);
	            mcQuestionContentDTO.setCaCount(caCount);
	            mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
	            listFinalQuestionContentDTO.add(mcQuestionContentDTO);
	            
	        }
	         
    	}
	    
	    logger.debug("listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }
    
    
  
    /**
     * sorts the questions by the display order
     * 
     * reOrganizeDisplayOrder(Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm, McContent mcContent)
     * 
     * @param mapQuestionContent
     * @param mcService
     * @param mcAuthoringForm
     * @param mcContent
     */
    public void reOrganizeDisplayOrder(Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm, McContent mcContent)
    {    
        logger.debug("mcContent: " + mcContent);
        if (mcContent != null)
        {
        	logger.debug("content uid: " + mcContent.getUid());
        	List sortedQuestions=mcService.getAllQuestionEntriesSorted(mcContent.getUid().longValue());
        	logger.debug("sortedQuestions: " + sortedQuestions);
        	
    		Iterator listIterator=sortedQuestions.iterator();
    		int displayOrder=1;
    		while (listIterator.hasNext())
    		{
    			McQueContent queContent=(McQueContent)listIterator.next();
    			logger.debug("queContent data: " + queContent);
    			logger.debug("queContent: " + queContent.getQuestion() + " " + queContent.getDisplayOrder());
    			
    			McQueContent existingMcQueContent=mcService.getQuestionContentByQuestionText(queContent.getQuestion(), mcContent.getUid());
    	    	logger.debug("existingMcQueContent: " + existingMcQueContent);
    	    	existingMcQueContent.setDisplayOrder(new Integer(displayOrder));
    	    	logger.debug("updating the existing question content for displayOrder: " + existingMcQueContent);
           		mcService.updateMcQueContent(existingMcQueContent);
    			displayOrder++;
    		}
        }
		logger.debug("done with reOrganizeDisplayOrder...");
    }
    
    

    /**
     * boolean checkDuplicateQuestions(List listQuestionContentDTO, String newQuestion)
     * 
     * verifies that there are no duplicate questions
     * 
     * @param listQuestionContentDTO
     * @param newQuestion
     * @return
     */
    public static boolean checkDuplicateQuestions(List listQuestionContentDTO, String newQuestion)
    {
        logger.debug("checkDuplicateQuestions: " + listQuestionContentDTO);
        logger.debug("newQuestion: " + newQuestion);
        
        Map mapQuestionContent=extractMapQuestionContent(listQuestionContentDTO);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        
    	Iterator itMap = mapQuestionContent.entrySet().iterator();
	    while (itMap.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
    		{
	        	logger.debug("checking the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        	
	        	if (pairs.getValue().equals(newQuestion))
	        	{
	        	    logger.debug("entry found: " +  newQuestion);
	        	    return true;
	        	}
    		}
	    }
	    return false;
    }
    

    /**
     * removes unused question entries from db
     * removeRedundantQuestions (Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm)
     * 
     * @param mapQuestionContent
     * @param mcService
     * @param mcAuthoringForm
     */
    public void removeRedundantQuestions (Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm, 
            HttpServletRequest request, String toolContentID)
	{
    	logger.debug("removing unused entries... ");
    	logger.debug("mapQuestionContent:  " + mapQuestionContent);
    	logger.debug("toolContentID:  " + toolContentID);
    	
    	McContent mcContent=mcService.retrieveMc( new Long(toolContentID));
    	logger.debug("mcContent:  " + mcContent);
    	
    	if (mcContent != null)
    	{
        	logger.debug("mcContent uid: " + mcContent.getUid());
        	List allQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
        	logger.debug("allQuestions: " + allQuestions);
        	
        	Iterator listIterator=allQuestions.iterator();
    		int mapIndex=0;
    		boolean entryUsed=false;
    		while (listIterator.hasNext())
    		{
	            ++mapIndex;
	            logger.debug("current mapIndex: " +  mapIndex);

    			McQueContent queContent=(McQueContent)listIterator.next();
    			logger.debug("queContent data: " + queContent);
    			logger.debug("queContent: " + queContent.getQuestion() + " " + queContent.getDisplayOrder());
    			
    			entryUsed=false;
    	        Iterator itMap = mapQuestionContent.entrySet().iterator();
    	        int displayOrder=0;
    	        while (itMap.hasNext()) 
    		    {
    	            ++displayOrder;
    	            logger.debug("current displayOrder: " +  displayOrder);
    	        	entryUsed=false;
    		        Map.Entry pairs = (Map.Entry)itMap.next();
    		        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
    		        
    		        if (pairs.getValue().toString().length() != 0)
    		        {
    		        	logger.debug("text from map:" + pairs.getValue().toString());
    		        	logger.debug("text from db:" + queContent.getQuestion());

		        	    logger.debug("mapIndex versus displayOrder:" + mapIndex + " versus " + displayOrder);
    		        	if (mapIndex == displayOrder)
    		        	{
    		        		//logger.debug("used entry in db:" + queContent.getQuestion());
    		        	    logger.debug("used displayOrder position:" + displayOrder);
    		        		entryUsed=true;
    		        		break;
    		        	}
    		        	
    		        }
    		    }
    	        
    	        if (entryUsed == false)
    	        {
    	        	logger.debug("removing unused entry in db:" + queContent.getQuestion());
    	        	
    	        	McQueContent removeableMcQueContent=mcService.getQuestionContentByQuestionText(queContent.getQuestion(), mcContent.getUid());
        			logger.debug("removeableMcQueContent"  + removeableMcQueContent);
        			if (removeableMcQueContent != null)
        			{
        			    //mcContent.getMcQueContents().remove(removeableMcQueContent);
        				mcService.removeMcQueContent(removeableMcQueContent);
            			logger.debug("removed removeableMcQueContent from the db: " + removeableMcQueContent);	
        			}
    	        	
    	        }
    		}    		
    	}
	
	}
    
    
    /**
     * Map extractMapFeedback(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapFeedback(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapFeedbackContent= new TreeMap(new McComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            McQuestionContentDTO mcQuestionContentDTO=(McQuestionContentDTO)listIterator.next();
            logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
            logger.debug("mcQuestionContentDTO feedback:" + mcQuestionContentDTO.getFeedback()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapFeedbackContent.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getFeedback());
        }
        logger.debug("mapFeedbackContent:" + mapFeedbackContent);
        return mapFeedbackContent;
    }

    
    /**
     * Map extractMapWeights(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapWeights(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapWeights= new TreeMap(new McComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            McQuestionContentDTO mcQuestionContentDTO=(McQuestionContentDTO)listIterator.next();
            logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
            logger.debug("mcQuestionContentDTO weight:" + mcQuestionContentDTO.getWeight()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapWeights.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getWeight());
        }
        logger.debug("mapWeights:" + mapWeights);
        return mapWeights;
    }


    /**
     * Map extractMapMarks(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapMarks(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapMarks= new TreeMap(new McComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            McQuestionContentDTO mcQuestionContentDTO=(McQuestionContentDTO)listIterator.next();
            logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
            logger.debug("mcQuestionContentDTO weight:" + mcQuestionContentDTO.getMark()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapMarks.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getMark());
        }
        logger.debug("mapMarks:" + mapMarks);
        return mapMarks;
    }


    /**
     * Map extractMapCandidatesList(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapCandidatesList(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapCandidatesList= new TreeMap(new McComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            McQuestionContentDTO mcQuestionContentDTO=(McQuestionContentDTO)listIterator.next();
            logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
            logger.debug("mcQuestionContentDTO candidates list:" + mcQuestionContentDTO.getListCandidateAnswersDTO()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapCandidatesList.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getListCandidateAnswersDTO());
        }
        logger.debug("mapCandidatesList:" + mapCandidatesList);
        return mapCandidatesList;
    }
    
    

    /**
     * saveOrUpdateMcContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, Map mapMarks, Map mapCandidatesList, 
            IMcService mcService, McAuthoringForm mcAuthoringForm, 
            HttpServletRequest request, McContent mcContent, String strToolContentID)
            
        enables persisting content
        
     * @param mapQuestionContent
     * @param mapFeedback
     * @param mapWeights
     * @param mapMarks
     * @param mapCandidatesList
     * @param mcService
     * @param mcAuthoringForm
     * @param request
     * @param mcContent
     * @param strToolContentID
     * @return
     */
    public McContent  saveOrUpdateMcContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, Map mapMarks, Map mapCandidatesList, 
            IMcService mcService, McAuthoringForm mcAuthoringForm, 
            HttpServletRequest request, McContent mcContent, String strToolContentID)
    {
        logger.debug("doing saveOrUpdateMcContent, mapCandidatesList: " + mapCandidatesList);
        UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
        
        String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);

        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);

    	String sln=request.getParameter("sln");
    	logger.debug("sln: " + sln);
		
		String questionsSequenced=request.getParameter("questionsSequenced");
		logger.debug("questionsSequenced: " + questionsSequenced);
		
		String showMarks=request.getParameter("showMarks");
		logger.debug("showMarks: " + showMarks);
		

		String retries=request.getParameter("retries");
		logger.debug("retries: " + retries);
		
		String reflect=request.getParameter(REFLECT);
		logger.debug("reflect: " + reflect);

		
		String richTextOfflineInstructions=request.getParameter(OFFLINE_INSTRUCTIONS);
		String richTextOnlineInstructions=request.getParameter(ONLINE_INSTRUCTIONS);
		
		String reflectionSubject=request.getParameter(REFLECTION_SUBJECT);
		logger.debug("reflectionSubject: " + reflectionSubject);
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
        
        
        boolean setCommonContent=true; 
        if ((sln == null) || (questionsSequenced == null) || 
             (retries == null) || (reflect == null) || (showMarks == null))
        {
        	setCommonContent=false;
        }
        logger.debug("setCommonContent: " + setCommonContent);
		
        boolean questionsSequencedBoolean=false;
        boolean showMarksBoolean=false;
        boolean slnBoolean=false;
        boolean retriesBoolean=false;
        boolean reflectBoolean=false;

    	if ((questionsSequenced != null) && (questionsSequenced.equalsIgnoreCase("1")))
            questionsSequencedBoolean=true;            

    	if ((showMarks != null) && (showMarks.equalsIgnoreCase("1")))
    	    showMarksBoolean=true;            
    	
        if ((sln != null) && (sln.equalsIgnoreCase("1")))
            slnBoolean=true;            
        
        if ((retries != null) && (retries.equalsIgnoreCase("1")))
            retriesBoolean=true;            

        if ((reflect != null) && (reflect.equalsIgnoreCase("1")))
            reflectBoolean=true;
            
            
        logger.debug("questionsSequencedBoolean: " + questionsSequencedBoolean);
        logger.debug("slnBoolean: " + slnBoolean);
        logger.debug("retriesBoolean: " + retriesBoolean);
        logger.debug("reflectBoolean: " + reflectBoolean);
        
        long userId=0;
        if (toolUser != null)
        {
        	userId = toolUser.getUserID().longValue();	
        }
        else
        {
    		HttpSession ss = SessionManager.getSession();
    		logger.debug("ss: " + ss);
    		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
    		logger.debug("user" + user);
    		if (user != null)
    		{
    			userId = user.getUserID().longValue();	
    		}
    		else
    		{
    			logger.debug("should not reach here");
    			userId=0;
    		}
        }
        logger.debug("userId: " + userId);
     	logger.debug("mcContent: " + mcContent);
     	
     	boolean newContent=false;
        if(mcContent == null)
        {
        	mcContent = new McContent();
        	newContent=true;
        }


    	logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
    	mcContent.setMcContentId(new Long(strToolContentID));
     	mcContent.setTitle(richTextTitle);
     	mcContent.setInstructions(richTextInstructions);
     	mcContent.setUpdateDate(new Date(System.currentTimeMillis())); /**keep updating this one*/
     	logger.debug("userId: " + userId);
     	mcContent.setCreatedBy(userId); /**make sure we are setting the userId from the User object above*/
     	logger.debug("end of setting common content values...");

     	
     	String passmark= request.getParameter("passmark");
     	logger.debug("passmark: " + passmark);

     	if (passmark == null) 
     	   passmark="0";
     	
     	if  ((passmark != null) && (passmark.equals(" ")))
     	   passmark="0";
     	    
     	else if ((passmark != null) && (passmark.length() == 0))  
      	   passmark="0";
      	
     	
     	     	logger.debug("activeModule:" + activeModule);
     	if (activeModule.equals(AUTHORING))
		{
        	logger.debug("setting other content values...");
         	mcContent.setOnlineInstructions(richTextOnlineInstructions);
         	mcContent.setOfflineInstructions(richTextOfflineInstructions);
         	mcContent.setQuestionsSequenced(questionsSequencedBoolean);
         	mcContent.setShowMarks(showMarksBoolean);
         	mcContent.setRetries(retriesBoolean);
         	mcContent.setShowReport(slnBoolean);	
         	mcContent.setReflect(reflectBoolean);
         	mcContent.setReflectionSubject(reflectionSubject);
         	
         	logger.debug("setting passmark...");
         	mcContent.setPassMark(new Integer(passmark));
		}
        	
 
        if (newContent)
        {
        	logger.debug("will create: " + mcContent);
         	mcService.createMc(mcContent);
        }
        else
        {
        	logger.debug("will update: " + mcContent);
            mcService.updateMc(mcContent);
        }
        
        mcContent=mcService.retrieveMc(new Long(strToolContentID));
     	logger.debug("mcContent: " + mcContent);
        
        mcContent=createQuestionContent(mapQuestionContent, mapFeedback, mapWeights, mapMarks, mapCandidatesList, mcService, mcContent);
        
        return mcContent;
    }
    
        
    
    /**
     * 
     * McContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, 
            Map mapMarks, Map mapCandidatesList, IMcService mcService, McContent mcContent)
     *
     * persists the questions in the Map the user has submitted
     *  
     * @param mapQuestionContent
     * @param mapFeedback
     * @param mapWeights
     * @param mapMarks
     * @param mapCandidatesList
     * @param mcService
     * @param mcContent
     * @return
     */
    protected McContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, 
            Map mapMarks, Map mapCandidatesList, IMcService mcService, McContent mcContent)
    {    
        logger.debug("createQuestionContent, mapCandidatesList: " + mapCandidatesList);
        logger.debug("content uid is: " + mcContent.getUid());
        List questions=mcService.retrieveMcQueContentsByToolContentId(mcContent.getUid().longValue());
        logger.debug("questions: " + questions);
        
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapFeedback: " + mapFeedback);
        logger.debug("mapMarks: " + mapMarks);

        
        Iterator itMap = mapQuestionContent.entrySet().iterator();
        int displayOrder=0;
        while (itMap.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        
	        if (pairs.getValue().toString().length() != 0)
	        {
	        	logger.debug("starting createQuestionContent: pairs.getValue().toString():" + pairs.getValue().toString());
	        	logger.debug("starting createQuestionContent: mcContent: " + mcContent);
	        	
	        	++displayOrder;
	        	logger.debug("starting createQuestionContent: displayOrder: " + displayOrder);
	        	String currentFeedback=(String)mapFeedback.get(new Integer(displayOrder).toString());
	        	logger.debug("currentFeedback: " + currentFeedback);
	        	
	        	String currentMark=(String)mapMarks.get(new Integer(displayOrder).toString());
	        	logger.debug("currentMark: " + currentMark);
	        	/*set the default mark in case it is not provided*/
	        	if (currentMark == null)
	        	    currentMark= "1";
	        	
	        	List caList=(List)mapCandidatesList.get(new Integer(displayOrder).toString());
	        	logger.debug("caList: " + caList);
	        	
	        	
	        	
	        	McQueContent queContent=  new McQueContent(pairs.getValue().toString(), 
		        											new Integer(displayOrder),
		        											new Integer(currentMark),
		        											currentFeedback,
															mcContent,
															null,
															null);
		        
		        
	      	       logger.debug("queContent: " + queContent);
	        	
			       /* checks if the question is already recorded*/
			       logger.debug("question text is: " + pairs.getValue().toString());
			       logger.debug("content uid is: " + mcContent.getUid());
			       logger.debug("question display order is: " + displayOrder);
			       McQueContent existingMcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(displayOrder), mcContent.getUid());
			       logger.debug("existingMcQueContent: " + existingMcQueContent);
			       
			       if (existingMcQueContent == null)
			       {
			       	/*make sure a question with the same question text is not already saved*/
			    	McQueContent duplicateMcQueContent=mcService.getQuestionContentByQuestionText(pairs.getValue().toString(), mcContent.getUid());
			    	logger.debug("duplicateMcQueContent: " + duplicateMcQueContent);
				       		logger.debug("adding a new question to content: " + queContent);
				       		mcContent.getMcQueContents().add(queContent);
				       		queContent.setMcContent(mcContent);
		
				       		mcService.createMcQue(queContent);
				       		
				       		logger.debug("start persisting candidate answers: " + queContent);
				       		persistCandidates(caList, queContent, mcService);
			       }
			       else
			       {

				       String existingQuestion=existingMcQueContent.getQuestion(); 
				       logger.debug("existingQuestion: " + existingQuestion);
				       
				       logger.debug("map question versus existingQuestion: " + pairs.getValue().toString() + 
				               " versus db question value: " + existingQuestion);

			       		existingMcQueContent.setQuestion(pairs.getValue().toString());
			       		existingMcQueContent.setFeedback(currentFeedback);
			       		existingMcQueContent.setDisplayOrder(new Integer(displayOrder));
			       		existingMcQueContent.setMark(new Integer(currentMark));
			       		
			       		
			       		logger.debug("updating the existing question content: " + existingMcQueContent);
			       		mcService.updateMcQueContent(existingMcQueContent);

			       		logger.debug("questionUid " + existingMcQueContent.getUid());
			       		 
			       		logger.debug("will be removing redundant candidates, questionUid: " + existingMcQueContent.getUid());
			       		mcService.removeMcOptionsContentByQueId(existingMcQueContent.getUid());
			       		logger.debug("start persisting candidates " + existingMcQueContent.getUid());
			       		
			       		persistCandidates(caList, existingMcQueContent, mcService);
			       }
	        }      
	    }
        return mcContent;
    }

    
    
    protected Map buildDynamicPassMarkMap(List listQuestionContentDTO, boolean initialScreen)
    {
        logger.debug("starting buildDynamicPassMarkMap: " + listQuestionContentDTO);
        logger.debug("initialScreen: " + initialScreen);
        
    	Map map= new TreeMap(new McComparator());
        
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        
        int intTotalMark=0;
        if ((totalMark != null) && (totalMark.length() > 0))
            intTotalMark= new Integer(totalMark).intValue();
        
        logger.debug("intTotalMark: " + intTotalMark);
            
        
        Map passMarksMap=buildPassMarkMap(intTotalMark, false);
        logger.debug("passMarksMap: " + passMarksMap);
        return  passMarksMap;
    }
    
    
    protected Map buildPassMarkMap(int intTotalMark, boolean initialScreen)
    {
        logger.debug("building buildMarksMap: " + intTotalMark);
        logger.debug("initialScreen: " + initialScreen);
        
    	Map map= new TreeMap(new McComparator());
    	
        if (initialScreen)
            return map;
        
    	for (int i=1; i <= intTotalMark ; i++)
		{
    	    map.put(new Integer(i).toString(), new Integer(i).toString());
		}
    	logger.debug("return passmarks Map: " + map);
    	return map;
    }


    
    /**
     * Map buildMarksMap()
     * 
     * @return
     */
    protected Map buildMarksMap()
    {
    	Map map= new TreeMap(new McComparator());
    	
    	for (int i=1; i <= 10 ; i++)
		{
    	    map.put(new Integer(i).toString(), new Integer(i).toString());
		}
    	logger.debug("return marks Map: " + map);
    	return map;
    }


    /**
     * Map buildCorrectMap()
     * 
     * @return
     */
    protected Map buildCorrectMap()
    {
    	Map map= new TreeMap(new McComparator());
	    map.put(new Integer(2).toString(), "Correct");
    	logger.debug("return marks Map: " + map);
    	return map;
    }

    
    /**
     * List repopulateCandidateAnswersBox(HttpServletRequest request, boolean addBlankCa)
     * 
     * @param request
     * @param addBlankCa
     * @return
     */
    protected List repopulateCandidateAnswersBox(HttpServletRequest request, boolean addBlankCa)
    {
        logger.debug("doing repopulateCandidateAnswersBox, addBlankCa: " + addBlankCa);
        
        String correct= request.getParameter("correct");
        logger.debug("correct: " + correct);
        
        
        /* check this logic again*/
        int intCorrect=0;
        if (correct == null)
        {
            logger.debug("correct is null: ");
        }
        else
        {
            intCorrect= new Integer(correct).intValue();            
        }
        

        logger.debug("intCorrect: " + intCorrect);
        
        List listFinalCandidatesDTO=new LinkedList();
        
        for (int i=0; i< MAX_OPTION_COUNT; i++)
        {
            String candidate= request.getParameter("ca" + i);
            logger.debug("candidate: " + candidate);
            
            String isCorrect="Incorrect";
            logger.debug("i versus intCorrect: " + i + " versus " + intCorrect);
           
            if (i == intCorrect)
            {
                isCorrect="Correct";
            }
            logger.debug("isCorrect: " + isCorrect);
            
            if (candidate != null)            
            {
                McCandidateAnswersDTO mcCandidateAnswersDTO= new McCandidateAnswersDTO();
                mcCandidateAnswersDTO.setCandidateAnswer(candidate);
                mcCandidateAnswersDTO.setCorrect(isCorrect);
                listFinalCandidatesDTO.add(mcCandidateAnswersDTO);
            }
        }
        
        if (addBlankCa == true)
        {
            McCandidateAnswersDTO mcCandidateAnswersDTO= new McCandidateAnswersDTO();
            mcCandidateAnswersDTO.setCandidateAnswer("");
            mcCandidateAnswersDTO.setCorrect("Incorrect");
            listFinalCandidatesDTO.add(mcCandidateAnswersDTO);
        }

        logger.debug("returning listFinalCandidatesDTO: " + listFinalCandidatesDTO);
        return listFinalCandidatesDTO;
    }


    
    protected boolean validateCandidateAnswersNotBlank(HttpServletRequest request)
    {
        
        for (int i=0; i< MAX_OPTION_COUNT; i++)
        {
            String candidate= request.getParameter("ca" + i);
            logger.debug("candidate: " + candidate);
            
            if ((candidate != null) && (candidate.length() == 0))
            {
                logger.debug("there is at least 1 blank candidate");
                return false;
            }
        }
        return true;
    }


    
    
    
    protected List repopulateCandidateAnswersRadioBox(HttpServletRequest request, boolean addBlankCa)
    {
        logger.debug("doing repopulateCandidateAnswersRadioBox, addBlankCa: " + addBlankCa);
        
        String correct= request.getParameter("correct");
        logger.debug("correct: " + correct);
        
        List listFinalCandidatesDTO=new LinkedList();

        logger.debug("returning listFinalCandidatesDTO: " + listFinalCandidatesDTO);
        return listFinalCandidatesDTO;
    }

    
    /**
     * boolean validateSingleCorrectCandidate(List caList)
     * 
     * verifies that there is at least one Correct entry selected
     * 
     * @param caList
     * @return
     */
    protected boolean validateSingleCorrectCandidate(List caList)
    {
        Iterator itCaList=  caList.iterator();
        while (itCaList.hasNext())
        {
            McCandidateAnswersDTO mcCandidateAnswersDTO= (McCandidateAnswersDTO)itCaList.next();
            logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);

 	       	String candidateAnswer= mcCandidateAnswersDTO.getCandidateAnswer();
 	    	String correct= mcCandidateAnswersDTO.getCorrect();
 	    	logger.debug("correct:" + correct);
 	    	
 	    	if (correct.equals("Correct"))
 	    	{
 	    	   logger.debug("there is at least one Correct candidate, it is good.");
 	    	   return true;
 	    	}
    	} 	    
 	
 	    return false;
 	}
 	    	    

    
    protected boolean validateOnlyOneCorrectCandidate(List caList)
    {
        int correctCandidatesCount= 0;
        
        Iterator itCaList=  caList.iterator();
        while (itCaList.hasNext())
        {
            McCandidateAnswersDTO mcCandidateAnswersDTO= (McCandidateAnswersDTO)itCaList.next();
            logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);

 	       	String candidateAnswer= mcCandidateAnswersDTO.getCandidateAnswer();
 	    	String correct= mcCandidateAnswersDTO.getCorrect();
 	    	logger.debug("correct:" + correct);
 	    	
 	    	if (correct.equals("Correct"))
 	    	{
 	    	   logger.debug("there is at leat one Correct candidate, it is good.");
 	    	   ++correctCandidatesCount;
 	    	}
    	} 	    
 	   logger.debug("correctCandidatesCount: " + correctCandidatesCount);
 	   
 	   if (correctCandidatesCount > 1)
 	       return false; //not good
 	
 	   return true;
 	}
    
    
    
    
    /**
     * buildDefaultQuestionContent(McContent mcContent, IMcService mcService)
     * 
     * generates a list for holding default questions and their candidate answers
     * 
     * @param mcContent
     * @param mcService
     * @return
     */
    protected List buildDefaultQuestionContent(McContent mcContent, IMcService mcService)
    {
        logger.debug("starting buildDefaultQuestionContent, mcContent: " + mcContent);
        List listQuestionContentDTO= new  LinkedList();
    	
        /*
    	 * get the existing question content
    	 */
    	logger.debug("setting content data from the db");
    	Iterator queIterator=mcContent.getMcQueContents().iterator();
    	Long mapIndex=new Long(1);
    	
    	while (queIterator.hasNext())
    	{
    	    McQuestionContentDTO mcQuestionContentDTO=new McQuestionContentDTO();
    	    
    		McQueContent mcQueContent=(McQueContent) queIterator.next();
    		if (mcQueContent != null)
    		{
    			logger.debug("question: " + mcQueContent.getQuestion());
    			logger.debug("displayorder: " + mcQueContent.getDisplayOrder().toString());
    			logger.debug("mark: " + mcQueContent.getMark().toString());
    			logger.debug("feedback: " + mcQueContent.getFeedback());
    			
    			String feedback="";
    			if (mcQueContent.getFeedback() != null)
    			    feedback=mcQueContent.getFeedback();
    			
    			logger.debug("feedback now: " + mcQueContent.getFeedback());
    			
    			
        		String question=mcQueContent.getQuestion();
        		logger.debug("question: " + question);
    			
    			
    			mcQuestionContentDTO.setQuestion(question);
        		mcQuestionContentDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
        		mcQuestionContentDTO.setFeedback(feedback);
        		mcQuestionContentDTO.setMark(mcQueContent.getMark().toString());
        		
        		
        		/* get candidates, from here.. */
        		List list=mcService.findMcOptionsContentByQueId(mcQueContent.getUid());
        		logger.debug("candidiate answers list for mapQuestionContent:" + list);
        		
        		int caCount=list.size();
        		logger.debug("caCount:" + caCount);
        		mcQuestionContentDTO.setCaCount(new Integer(caCount).toString());

        		List listCandidates= new  LinkedList();
        		Iterator listIterator=list.iterator();
        		while (listIterator.hasNext())
        		{
        			McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
        			McCandidateAnswersDTO mcCandidateAnswersDTO = new McCandidateAnswersDTO();
        			
    	    		logger.debug("mcOptsContent:" + mcOptsContent);
        			logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
        			logger.debug("option text:" + mcOptsContent.isCorrectOption());
        			
        			mcCandidateAnswersDTO.setCandidateAnswer(mcOptsContent.getMcQueOptionText());
        			
        			if (mcOptsContent.isCorrectOption())
        			{
        			    logger.debug("mcOptsContent.getMcQueOptionText() is set to true");
        			    mcCandidateAnswersDTO.setCorrect("Correct");
        			}
        			else
        			{
        			    logger.debug("mcOptsContent.getMcQueOptionText() is set to true");
        			    mcCandidateAnswersDTO.setCorrect("Incorrect");
        			}
        			logger.debug("current mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
        			listCandidates.add(mcCandidateAnswersDTO);
        			logger.debug("current listCandidates:" + listCandidates);
        		}
        		logger.debug("final listCandidates for mcQueContent:" + listCandidates);
        		/* get candidates, till here.. */
        		
        		mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
        		logger.debug("current mcQuestionContentDTO:" + mcQuestionContentDTO);
        		
        		
        		listQuestionContentDTO.add(mcQuestionContentDTO);
        		logger.debug("current listQuestionContentDTO:" + listQuestionContentDTO);

        		mapIndex=new Long(mapIndex.longValue()+1);
    		}
    	}
    	
    	logger.debug("final listQuestionContentDTO:" + listQuestionContentDTO);
    	return listQuestionContentDTO;
    }
    
    /**
     * persistCandidates(List caList, McQueContent mcQueContent, IMcService mcService)
     * 
     * @param caList
     * @param mcQueContent
     * @param mcService
     */
    protected void persistCandidates(List caList, McQueContent mcQueContent, IMcService mcService)
    {
       logger.debug("doing persistCandidates:" + caList);
       logger.debug("mcQueContent:" + mcQueContent);

       Iterator itCaList=  caList.iterator();
       while (itCaList.hasNext())
       {
           McCandidateAnswersDTO mcCandidateAnswersDTO= (McCandidateAnswersDTO)itCaList.next();
           logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);

	       	String candidateAnswer= mcCandidateAnswersDTO.getCandidateAnswer();
	    	String correct= mcCandidateAnswersDTO.getCorrect();
	    	
	    	boolean correctOption=false;
	    	if (correct.equals("Correct"))
	    	    correctOption= true;
	    	else
	    	    correctOption= false;
	    	    
	    	McOptsContent mcOptsContent = new McOptsContent(correctOption, candidateAnswer, mcQueContent, new TreeSet());
			logger.debug("mcOptsContent: " + mcOptsContent);
			
			mcService.saveMcOptionsContent(mcOptsContent);
			logger.debug("persisted mcOptsContent: " + mcOptsContent);	
       }
    }
}
