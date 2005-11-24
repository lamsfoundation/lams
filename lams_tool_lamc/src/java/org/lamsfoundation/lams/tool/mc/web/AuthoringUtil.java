/*
 * Created on 16/05/2005
 */
package org.lamsfoundation.lams.tool.mc.web;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McUtils;
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
     * populates request parameters
     * populateParameters(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     */
    protected static void populateParameters(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
        String selectedQuestion=request.getParameter(SELECTED_QUESTION);
    	logger.debug("read parameter selectedQuestion: " + selectedQuestion);
    	
    	if ((selectedQuestion != null) && (selectedQuestion.length() > 0))
    	{
    		request.getSession().setAttribute(SELECTED_QUESTION,selectedQuestion);
    		logger.debug("updated SELECTED_QUESTION");
    	}
    	
    	
    	mcAuthoringForm.setRemoveQuestion(null);
    	mcAuthoringForm.setEditOptions(null);
    	mcAuthoringForm.setMoveUp(null);
    	mcAuthoringForm.setMoveDown(null);
    	mcAuthoringForm.setRemoveOption(null);
    	mcAuthoringForm.setViewFileItem(null);
    	
    	String editOptions=request.getParameter("editOptions");
    	logger.debug("parameter editOptions" + editOptions);
    	if ((editOptions != null) && editOptions.equals("1"))
    	{
    		logger.debug("parameter editOptions is selected " + editOptions);
    		mcAuthoringForm.setEditOptions("1");
    	}
    	
    	String removeQuestion=request.getParameter("removeQuestion");
    	logger.debug("parameter removeQuestion" + removeQuestion);
    	if ((removeQuestion != null) && removeQuestion.equals("1"))
    	{
    		logger.debug("parameter removeQuestion is selected " + removeQuestion);
    		mcAuthoringForm.setRemoveQuestion("1");
    	}
    	
    	String moveDown=request.getParameter("moveDown");
    	logger.debug("parameter moveDown" + moveDown);
    	if ((moveDown != null) && moveDown.equals("1"))
    	{
    		logger.debug("parameter moveDown is selected " + moveDown);
    		mcAuthoringForm.setMoveDown("1");
    	}

    	String moveUp=request.getParameter("moveUp");
    	logger.debug("parameter moveUp" + moveUp);
    	if ((moveUp != null) && moveUp.equals("1"))
    	{
    		logger.debug("parameter moveUp is selected " + moveUp);
    		mcAuthoringForm.setMoveUp("1");
    	}
    	
    	String removeOption=request.getParameter("removeOption");
    	logger.debug("parameter removeOption" + removeOption);
    	if ((removeOption != null) && removeOption.equals("1"))
    	{
    		logger.debug("parameter removeOption is selected " + removeOption);
    		mcAuthoringForm.setRemoveOption("1");
    	}
    	
    	String viewFileItem=request.getParameter("viewFileItem");
    	logger.debug("parameter viewFileItem" + viewFileItem);
    	if ((viewFileItem != null) && viewFileItem.equals("1"))
    	{
    		logger.debug("parameter viewFileItem is selected " + viewFileItem);
    		mcAuthoringForm.setViewFileItem("1");
    	}
    }

	
	 public static Map repopulateCurrentCheckBoxStatesMap(HttpServletRequest request)
	    {
	    	Map mapTempContent= new TreeMap(new McComparator());
	    	
	    	long mapCounter=0;
	    	for (long i=1; i <= MAX_OPTION_COUNT ; i++)
			{
				String candidateEntry =request.getParameter("checkBoxSelected" + i);
				String optionText =request.getParameter("optionContent" + i);
				
				if (
						(candidateEntry != null) && 
						(candidateEntry.length() > 0)   
						)
					{
						if (candidateEntry.equals("Correct"))
						{
							mapCounter++;
							mapTempContent.put(new Long(mapCounter).toString(), optionText);
						}
					}
			}
	    	logger.debug("return repopulated currentCheckBoxStatesMap: " + mapTempContent);
	    	return mapTempContent;
	    }

		
	    public static boolean verifyDuplicatesOptionsMap(Map mapOptionsContent)
		{
	    	Map originalMapOptionsContent=mapOptionsContent;
	    	Map backupMapOptionsContent=mapOptionsContent;
	    	
	    	int optionCount=0;
	    	for (long i=1; i <= MAX_OPTION_COUNT ; i++)
			{
	    		String currentOption=(String)originalMapOptionsContent.get(new Long(i).toString());
	    		logger.debug("verified currentOption  " + currentOption);
	    		
	    		optionCount=0;
	    		for (long j=1; j <= MAX_OPTION_COUNT ; j++)
	    		{
	        		String backedOption=(String)backupMapOptionsContent.get(new Long(j).toString());
	        		
	        		if ((currentOption != null) && (backedOption !=null))
	        		{
	        			if (currentOption.equals(backedOption))
	    				{
	    					optionCount++;
	    			    	logger.debug("optionCount for  " + currentOption + " is: " + optionCount);
	    				}
	    				
	            		if (optionCount > 1)
	            			return false;	
	        		}
	    		}	
			}
	    	return true;
		}
	    
	    
	    public static boolean validateQuestionsNotEmpty(Map mapQuestionsContent)
	    {
	    	Iterator itMap = mapQuestionsContent.entrySet().iterator();
	    	while (itMap.hasNext()) {
	        	Map.Entry pairs = (Map.Entry)itMap.next();
	            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	            
	            if ((pairs.getValue() != null) && (pairs.getValue().toString().length() == 0))
	            	return false;
	            
			}
	    	return true;
	    }

	    public static Map repopulateCurrentWeightsMap(HttpServletRequest request, String parameterType)
	    {
	    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
	    	logger.debug("parameterType: " + parameterType);
	    	
	    	long mapCounter=0;
	    	for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
			{
				String candidateEntry =request.getParameter(parameterType + i);
				String questionText =request.getParameter("questionContent" + i);
				if ((questionText != null) && (questionText.length() > 0))
				{
					logger.debug("questionText: " + questionText);
					if (candidateEntry != null)
					{
						mapCounter++;
						mapTempQuestionsContent.put(new Long(mapCounter).toString(), candidateEntry);
					}	
				}
				
			}
	    	logger.debug("return repopulated Map: " + mapTempQuestionsContent);
	    	return mapTempQuestionsContent;
	    }


	    /**
	     * shrinks the size of the Map to only used entries
	     * 
	     * @param mapQuestionContent
	     * @param request
	     * @return
	     */
	    public static Map repopulateMap(HttpServletRequest request, String parameterType)
	    {
	    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
	    	logger.debug("parameterType: " + parameterType);
	    	
	    	long mapCounter=0;
	    	for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
			{
				String candidateEntry =request.getParameter(parameterType + i);
				if (
					(candidateEntry != null) && 
					(candidateEntry.length() > 0)   
					)
				{
					mapCounter++;
					mapTempQuestionsContent.put(new Long(mapCounter).toString(), candidateEntry);
				}
			}
	    	logger.debug("return repopulated Map: " + mapTempQuestionsContent);
	    	return mapTempQuestionsContent;
	    }

	    
	    public static Map shiftMap(Map mapQuestionsContent, String questionIndex , String movableQuestionEntry, String direction)
	    {
	    	logger.debug("movableQuestionEntry: " +  movableQuestionEntry);
	    	/* map to be returned */
	    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
	    	
	    	Iterator itMap = mapQuestionsContent.entrySet().iterator();
	    	String shiftableEntry=null;
	    	
	    	int shiftableIndex=0;
	    	if (direction.equals("down"))
	        {
	    		logger.debug("moving map down");
	    		shiftableIndex=new Integer(questionIndex).intValue() + 1;
	        }
	    	else
	    	{
	    		logger.debug("moving map up");
	    		shiftableIndex=new Integer(questionIndex).intValue() - 1;
	        
	    	}
	    		
			logger.debug("shiftableIndex: " +  shiftableIndex);
	    	shiftableEntry=(String)mapQuestionsContent.get(new Integer(shiftableIndex).toString());
	    	logger.debug("shiftable entry: " +  shiftableEntry);
	    	
	    	if (shiftableEntry != null) 
	    	{
	    		Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
	    		long mapCounter=0;
	    		while (itQuestionsMap.hasNext()) {
	            	Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
	                logger.debug("comparing the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	                mapCounter++;
	                logger.debug("mapCounter: " +  mapCounter);
	                
	                if (!pairs.getKey().equals(questionIndex) && !pairs.getKey().equals(new Integer(shiftableIndex).toString()))
	                {
	                	logger.debug("normal copy " +  questionIndex);
	                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), pairs.getValue());
	                }
	                else if (pairs.getKey().equals(questionIndex))
	                {
	                	logger.debug("move type 1 " +  questionIndex);
	                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), shiftableEntry);
	                }
	                else if (pairs.getKey().equals(new Integer(shiftableIndex).toString()))
	                {
	                	logger.debug("move type 2 " +  shiftableIndex);
	                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), movableQuestionEntry);
	                }
	            }
	    	}
	    	else
	    	{
	    		logger.debug("no change to map");
	    		mapTempQuestionsContent=mapQuestionsContent;
	    	}
	    		return mapTempQuestionsContent;
	    }
	    
	    
	    public static boolean validateTotalWeight(HttpServletRequest request)
	    {
	    	Map mapWeights= repopulateCurrentWeightsMap(request, "questionWeight");
	    	logger.debug("mapWeights: " + mapWeights);
	    	
	    	Iterator itMap = mapWeights.entrySet().iterator();
	    	int totalWeight=0;
			while (itMap.hasNext()) {
	        	Map.Entry pairs = (Map.Entry)itMap.next();
	            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	            
	            if ((pairs.getValue() != null) || (pairs.getValue().toString().length() > 0)) 
	            {
	            	totalWeight=totalWeight+ new Integer(pairs.getValue().toString()).intValue();
	            }
			}

	            logger.debug("totalWeight: " +  totalWeight);
	            
	        if (totalWeight != 100)
	        {
	        	return false;
	        }

	        return true;
	    }

	    
	    public static boolean validateSubTotalWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	    {
	    	Map mapWeights= repopulateCurrentWeightsMap(request, "questionWeight");
	    	logger.debug("mapWeights: " + mapWeights);
	    	
	    	int totalWeight=0;
	    	Iterator itMap = mapWeights.entrySet().iterator();
			while (itMap.hasNext()) {
	        	Map.Entry pairs = (Map.Entry)itMap.next();
	            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	            
	            int currentWeight=new Integer(pairs.getValue().toString()).intValue();
	            totalWeight= totalWeight + currentWeight;
	            logger.debug("sub totalWeight: " +  totalWeight);
			}
	        logger.debug("final totalWeight: " +  totalWeight);
	        
	        if (totalWeight > 100)
	        	return false;
	        else
	        	return true;
	    }

	    
	    public static  boolean validateOptions(HttpServletRequest request)
	    {
	    	logger.debug("will validateOptions");
	    	String parameterType="checkBoxSelected";
	    	for (int i=1; i <= MAX_OPTION_COUNT ; i++)
	    	{
	    		String isCorrect=request.getParameter(parameterType + i);
	    		logger.debug("isCorrect: " + isCorrect);
	    		
	    		if (isCorrect != null)
	    		{
	    			if (isCorrect.equals("Correct"))
	        			return true;	
	    		}
	    	}
	    	return false;
	    }

	
    public static Map rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	
    	IMcService mcService =McUtils.getToolService(request);
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

    
    public static Map rebuildQuestionUidMapfromDB(HttpServletRequest request, Long toolContentId)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	
    	IMcService mcService =McUtils.getToolService(request);
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
    		mapQuestionsContent.put(mapIndex.toString(),mcQueContent.getUid());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	
    	logger.debug("refreshed Map:" + mapQuestionsContent);
    	return mapQuestionsContent;
    }
 
    
    
    
    /**
     * returns all the options for all the questions for a content
     * generateGeneralOptionsContentMap(HttpServletRequest request, McContent mcContent)
     * 
     * @param request
     * @param mcContent
     * @return Map
     */
    public static Map generateGeneralOptionsContentMap(HttpServletRequest request, McContent mcContent)
    {
    	Map mapGeneralOptionsContent= new TreeMap(new McComparator());
    	IMcService mcService =McUtils.getToolService(request);
    	
    	Iterator mcQueIterator=mcContent.getMcQueContents().iterator();
		Long mapIndex=new Long(1);
    	while (mcQueIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)mcQueIterator.next();
    		if (mcQueContent != null)
    		{
    			Long uid=mcQueContent.getUid();
    			logger.debug("uid : " + uid);
    			/* get the options for this question */
    			List listMcOptions=mcService.findMcOptionsContentByQueId(uid);
    			logger.debug("listMcOptions : " + listMcOptions);
    			Map mapOptionsContent=McUtils.generateOptionsMap(listMcOptions);
    			logger.debug("mapOptionsContent : " + mapOptionsContent);
    			mapGeneralOptionsContent.put(mapIndex.toString(), mapOptionsContent);
        		mapIndex=new Long(mapIndex.longValue()+1);
    		}
    	}
		logger.debug("current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
		return mapGeneralOptionsContent;
    }
    

    public Map rebuildWeightsMapfromDB(HttpServletRequest request, Long toolContentId)
    {
    	Map mapWeightsContent= new TreeMap(new McComparator());
    	
    	IMcService mcService =McUtils.getToolService(request);
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
    		mapWeightsContent.put(mapIndex.toString(),mcQueContent.getWeight());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	
    	logger.debug("refreshed Map:" + mapWeightsContent);
    	return mapWeightsContent;
    }
    
    
    public static Map buildInCorrectFeedbackMap(HttpServletRequest request, Long toolContentId)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	Map mapIncorrectFeedback= new TreeMap(new McComparator());
    	
    	McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);
		
    	List questionsContent=mcService.refreshQuestionContent(mcContent.getUid());
    	logger.debug("questionsContent:" + questionsContent);
    	
    	Iterator listIterator=questionsContent.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listIterator.next();
    		logger.debug("mcQueContent:" + mcQueContent);
    		mapIncorrectFeedback.put(mapIndex.toString(),mcQueContent.getFeedbackIncorrect());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	
    	return mapIncorrectFeedback;
    }
    
    
    public static Map buildCorrectFeedbackMap(HttpServletRequest request, Long toolContentId)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	Map mapCorrectFeedback= new TreeMap(new McComparator());
    	
    	McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);
		
    	List questionsContent=mcService.refreshQuestionContent(mcContent.getUid());
    	logger.debug("questionsContent:" + questionsContent);
    	
    	Iterator listIterator=questionsContent.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listIterator.next();
    		logger.debug("mcQueContent:" + mcQueContent);
    		mapCorrectFeedback.put(mapIndex.toString(),mcQueContent.getFeedbackCorrect());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	
    	return mapCorrectFeedback;
    }

    
    /**
     * creates the options for a particular question in the db
     * persistOptionsFinal(HttpServletRequest request, Map currentOptions, Map selectedOptions, McQueContent mcQueContent)
     * 
     * @param request
     * @param currentOptions
     * @param selectedOptions
     * @param mcQueContent
     */
    public static void persistOptionsFinal(HttpServletRequest request, Map currentOptions, Map selectedOptions, McQueContent mcQueContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
        logger.debug("passed currentOptions: " + currentOptions);
        logger.debug("passed selectedOptions: " + selectedOptions);
        
        Iterator itCurrentOptions = currentOptions.entrySet().iterator();
        
        
        boolean selected=false;
        while (itCurrentOptions.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itCurrentOptions.next();
            logger.debug("checking the current options  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            selected=false;
            Iterator itSelectedOptions = selectedOptions.entrySet().iterator();    
            while (itSelectedOptions.hasNext())
            {
            	Map.Entry selectedPairs = (Map.Entry)itSelectedOptions.next();
            	logger.debug("checking the selected options  pair: " +  selectedPairs.getKey() + " = " + selectedPairs.getValue());
            	selected=false;
            	if (pairs.getValue().equals(selectedPairs.getValue()))
            	{
            		selected=true;
            		logger.debug("set selected to true for: " + pairs.getValue());
            		break;
            	}
            }
            
            logger.debug("pre-persist mcOptionsContent: " + pairs.getValue() + " " + selected);
            logger.debug("pre-persist mcOptionsContent, using mcQueContent: " + mcQueContent);
            McOptsContent mcOptionsContent= new McOptsContent(selected,pairs.getValue().toString() , mcQueContent, new HashSet());
	        logger.debug("created mcOptionsContent: " + mcOptionsContent);
   	        mcService.saveMcOptionsContent(mcOptionsContent);
   	        logger.debug("persisted the answer: " + pairs.getValue().toString());
        }
    }


    
    /**
     * prepares the data to persist options in the db
     * persistOptions(HttpServletRequest request, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, McQueContent mcQueContent, String questionIndex)
     * 
     * @param request
     * @param mapGeneralOptionsContent
     * @param mapGeneralSelectedOptionsContent
     * @param mcQueContent
     * @param questionIndex
     */
    public static void persistOptions(HttpServletRequest request, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, McQueContent mcQueContent, String questionIndex)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	logger.debug("passed questionIndex: " +  questionIndex);
    	
    	Iterator itOptionsMap = mapGeneralOptionsContent.entrySet().iterator();
    	while (itOptionsMap.hasNext()) {
            Map.Entry pairs = (Map.Entry)itOptionsMap.next();
            logger.debug("checking the general options  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
            {
            	Iterator itSelectedOptionsMap = mapGeneralSelectedOptionsContent.entrySet().iterator();    	
            	while (itSelectedOptionsMap.hasNext()) 
            	{
            		Map.Entry selectedPairs = (Map.Entry)itSelectedOptionsMap.next();
                    logger.debug("checking the general selected options pair: " +  selectedPairs.getKey() + " = " + selectedPairs.getValue());
                    
                    logger.debug("dubuging: " +  pairs.getKey() + "---" + questionIndex);
                    if (pairs.getKey().equals(selectedPairs.getKey())  && questionIndex.equals(pairs.getKey())) 
            		{
                    	logger.debug("using updated equal question: " +  pairs.getKey());
                    	Map currentOptions=(Map) pairs.getValue();
                    	Map selectedOptions=(Map) selectedPairs.getValue();
             
                    	persistOptionsFinal(request, currentOptions,selectedOptions,mcQueContent);
            		}
            	}
            }
        }
    }
    
    
    /**
     * creates the questions content in the db.
     * createContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     * @return
     */
    public static McContent createContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	/* the tool content id is passed from the container to the tool and placed into session in the McStarterAction */
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (toolContentId.longValue() != 0))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + toolContentId);
    		/*delete the existing content in the database before applying new content*/
    		mcService.deleteMcById(toolContentId);  
    		logger.debug("post-deletion existing content");
		}
    	
		String title;
    	String instructions;
    	Long createdBy;
    	String monitoringReportTitle="";
    	String reportTitle="";
    	
    	String offlineInstructions="";
    	String onlineInstructions="";
    	String endLearningMessage="";
    	String creationDate="";
    	int passmark=0;
    	
    	boolean isQuestionsSequenced=false;
    	boolean isSynchInMonitor=false;
    	boolean isUsernameVisible=false;
    	boolean isRunOffline=false;
    	boolean isDefineLater=false;
    	boolean isContentInUse=false;
    	boolean isRetries=false;
    	boolean isShowFeedback=false;
    	boolean isSln=false;
    	
    	logger.debug("isQuestionsSequenced: " +  mcAuthoringForm.getQuestionsSequenced());
    	if (mcAuthoringForm.getQuestionsSequenced().equalsIgnoreCase(ON))
    		isQuestionsSequenced=true;
    	
    	logger.debug("isSynchInMonitor: " +  mcAuthoringForm.getSynchInMonitor());
    	if (mcAuthoringForm.getSynchInMonitor().equalsIgnoreCase(ON))
    		isSynchInMonitor=true;
    	
    	logger.debug("isUsernameVisible: " +  mcAuthoringForm.getUsernameVisible());
    	if (mcAuthoringForm.getUsernameVisible().equalsIgnoreCase(ON))
    		isUsernameVisible=true;
    	
    	logger.debug("isRetries: " +  mcAuthoringForm.getRetries());
    	if (mcAuthoringForm.getRetries().equalsIgnoreCase(ON))
    		isRetries=true;
    	
    	
		logger.debug("isSln" +  mcAuthoringForm.getSln());
		if (mcAuthoringForm.getSln().equalsIgnoreCase(ON))
			isSln=true;
    	
    	logger.debug("passmark: " +  mcAuthoringForm.getPassmark());
    	if ((mcAuthoringForm.getPassmark() != null) && (mcAuthoringForm.getPassmark().length() > 0)) 
    		passmark= new Integer(mcAuthoringForm.getPassmark()).intValue();
    	
    	logger.debug("isShowFeedback: " +  mcAuthoringForm.getShowFeedback());
    	if (mcAuthoringForm.getShowFeedback().equalsIgnoreCase(ON))
    		isShowFeedback=true;
    	    	
    	logger.debug("MONITORING_REPORT_TITLE: " +  mcAuthoringForm.getMonitoringReportTitle());
    	monitoringReportTitle=mcAuthoringForm.getMonitoringReportTitle();
    	if ((monitoringReportTitle == null) || (monitoringReportTitle.length() == 0)) 
    		monitoringReportTitle=(String)request.getSession().getAttribute(MONITORING_REPORT_TITLE);
    	
    	reportTitle=mcAuthoringForm.getReportTitle();
    	logger.debug("REPORT_TITLE: " +  mcAuthoringForm.getReportTitle());
    	if ((reportTitle == null) || (reportTitle.length() == 0)) 
    		reportTitle=(String)request.getSession().getAttribute(REPORT_TITLE);
    	
    			
    	endLearningMessage=mcAuthoringForm.getEndLearningMessage();
    	logger.debug("END_LEARNING_MESSAGE: " +  mcAuthoringForm.getEndLearningMessage());
    	if ((endLearningMessage == null) || (endLearningMessage.length() == 0))
    		endLearningMessage=(String)request.getSession().getAttribute(END_LEARNING_MESSAGE);

    	String richTextTitle="";
    	richTextTitle = (String)request.getSession().getAttribute(RICHTEXT_TITLE);
    	logger.debug("createContent richTextTitle from session: " + richTextTitle);
    	if (richTextTitle == null) richTextTitle="";
    	
    	String richTextInstructions="";
    	richTextInstructions = (String)request.getSession().getAttribute(RICHTEXT_INSTRUCTIONS);
    	logger.debug("createContent richTextInstructions from session: " + richTextInstructions);
    	if (richTextInstructions == null) richTextInstructions="";

    	String richTextOfflineInstructions="";
    	richTextOfflineInstructions = (String)request.getSession().getAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOfflineInstructions from session: " + richTextOfflineInstructions);
    	if (richTextOfflineInstructions == null) richTextOfflineInstructions="";
    	
    	String richTextOnlineInstructions="";
    	richTextOnlineInstructions = (String)request.getSession().getAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOnlineInstructions from session: " + richTextOnlineInstructions);
    	if (richTextOnlineInstructions == null) richTextOnlineInstructions="";
    	
    	
    	String richTextReportTitle=(String)request.getSession().getAttribute(RICHTEXT_REPORT_TITLE);
		logger.debug("richTextReportTitle: " + richTextReportTitle);
		
		String richTextEndLearningMessage=(String)request.getSession().getAttribute(RICHTEXT_END_LEARNING_MSG);
		logger.debug("richTextEndLearningMessage: " + richTextEndLearningMessage);
    	
    	creationDate=(String)request.getSession().getAttribute(CREATION_DATE);
		if (creationDate == null)
			creationDate=new Date(System.currentTimeMillis()).toString();
		
    		
    	/*obtain user object from the session*/
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	long userId=toolUser.getUserID().longValue();
    	logger.debug("userId: " + userId);
    	
    	/* create a new qa content and leave the default content intact*/
    	McContent mc = new McContent();
		mc.setMcContentId(toolContentId);
		mc.setTitle(richTextTitle);
		mc.setInstructions(richTextInstructions);
		mc.setCreationDate(creationDate); /*preserve this from the db*/ 
		mc.setUpdateDate(new Date(System.currentTimeMillis())); /* keep updating this one*/
		mc.setCreatedBy(userId); /* make sure we are setting the userId from the User object above*/
	    mc.setUsernameVisible(isUsernameVisible);
	    mc.setQuestionsSequenced(isQuestionsSequenced); /* the default question listing in learner mode will be all in the same page*/
	    mc.setOnlineInstructions(richTextOnlineInstructions);
	    mc.setOfflineInstructions(richTextOfflineInstructions);
	    mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(isSynchInMonitor);
	    mc.setContentInUse(isContentInUse);
	    mc.setEndLearningMessage(endLearningMessage);
	    mc.setRunOffline(isRunOffline);
	    mc.setReportTitle(richTextReportTitle);
	    mc.setMonitoringReportTitle(monitoringReportTitle);
	    mc.setEndLearningMessage(richTextEndLearningMessage);
	    mc.setRetries(isRetries);
	    mc.setPassMark(new Integer(passmark));
	    mc.setShowReport(isSln);
	    mc.setShowFeedback(isShowFeedback);
	    mc.setMcQueContents(new TreeSet());
	    mc.setMcSessions(new TreeSet());
	    logger.debug("mc content :" +  mc);
    	
    	/*create the content in the db*/
        mcService.createMc(mc);
        logger.debug("mc created with content id: " + toolContentId);
        
        return mc;
    }

    
    public static void cleanupExistingQuestions(HttpServletRequest request, McContent mcContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("remove questions by  mcQueContent uid : " + mcContent.getUid());
    	mcService.cleanAllQuestionsSimple(mcContent.getUid());
    }
    
    
    /**
     * creates the questions from the user in the db and makes a call to persist options.
     * persistQuestions(HttpServletRequest request, Map mapQuestionsContent, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McContent mcContent)
     *  
     * @param request
     * @param mapQuestionsContent
     * @param mapFeedbackIncorrect
     * @param mapFeedbackCorrect
     * @param mcContent
     */
    public static void persistQuestions(HttpServletRequest request, Map mapQuestionsContent, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McContent mcContent)
	{
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("mapQuestionsContent to be persisted :" + mapQuestionsContent);
    	logger.debug("mapFeedbackIncorrect :" + mapFeedbackIncorrect);
    	logger.debug("mapFeedbackCorrect :" + mapFeedbackCorrect);

    	
  	    Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("final MAP_GENERAL_OPTIONS_CONTENT :" + mapGeneralOptionsContent);

		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("final MAP_GENERAL_SELECTED_OPTIONS_CONTENT :" + mapGeneralSelectedOptionsContent);

    	Map mapWeights= AuthoringUtil.repopulateMap(request, "questionWeight");
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
		
    	
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
        while (itQuestionsMap.hasNext()) {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
            {
            	logger.debug("checking existing question text: " +  pairs.getValue().toString() + " and mcContent uid():" + mcContent.getUid());
            	
            	String currentFeedbackIncorrect=(String)mapFeedbackIncorrect.get(pairs.getKey());
        	 	logger.debug("currentFeedbackIncorrect: " +  currentFeedbackIncorrect);
        	 	if (currentFeedbackIncorrect == null) currentFeedbackIncorrect="";
        	 	
        	 	String currentFeedbackCorrect=(String)mapFeedbackCorrect.get(pairs.getKey());
        	 	logger.debug("currentFeedbackCorrect: " +  currentFeedbackCorrect);
        	 	if (currentFeedbackCorrect == null) currentFeedbackCorrect=""; 
            	
            	String currentWeight=(String) mapWeights.get(pairs.getKey().toString());
            	logger.debug("currentWeight: " + currentWeight);

            	McQueContent mcQueContent=  new McQueContent(pairs.getValue().toString(),
              	        	 		new Integer(pairs.getKey().toString()),
              	        	 		new Integer(currentWeight),
									false,
									currentFeedbackIncorrect,
									currentFeedbackCorrect,
									mcContent,
									new HashSet(),
									new HashSet()
             						);
       	        mcService.createMcQue(mcQueContent);
       	        logger.debug("persisted mcQueContent: " + mcQueContent);
           	
        	    logger.debug("remove existing options for  mcQueContent : " + mcQueContent.getUid());
           	    mcService.removeMcOptionsContentByQueId(mcQueContent.getUid());
           	    logger.debug("removed all mcOptionsContents for mcQueContentId :" + mcQueContent.getUid());
     			
       	         if (mcQueContent != null)
       	         {
       	         	logger.debug("pre persistOptions for: " + mcQueContent);
       	         	logger.debug("sending :" + pairs.getKey().toString());
       	         	AuthoringUtil.persistOptions(request, mapGeneralOptionsContent, mapGeneralSelectedOptionsContent, mcQueContent, pairs.getKey().toString());
       	         	logger.debug("post persistOptions"); 	
       	         }
            }
        }
    	
	}
    
    
    /**
     * includes the new question entry in the questions Map
     * addQuestionMemory(HttpServletRequest request, McAuthoringForm mcAuthoringForm, Map mapQuestionsContent, boolean increaseMapSize)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapQuestionsContent
     * @param increaseMapSize
     */
    public static void addQuestionMemory(HttpServletRequest request, McAuthoringForm mcAuthoringForm, Map mapQuestionsContent, boolean increaseMapSize)
    {
    	if (increaseMapSize)
    	{
    		int mapSize=mapQuestionsContent.size();
        	mapQuestionsContent.put(new Long(++mapSize).toString(), "");
        	logger.debug("updated Questions Map size: " + mapQuestionsContent.size());
        	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
        	logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
        	
        	
        	Map mapWeights = (Map) request.getSession().getAttribute(MAP_WEIGHTS);
        	logger.debug("current mapWeights: " + mapWeights);
        	int mapWeightsSize=mapWeights.size();
        	mapWeights.put(new Long(++mapWeightsSize).toString(), "");
        	logger.debug("updated mapWeights size: " + mapWeights.size());
        	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
        	logger.debug("updated mapWeights: " + request.getSession().getAttribute(MAP_WEIGHTS));
    	}
    }

    
    public void simulatePropertyInspector_RunOffline(HttpServletRequest request)
    {
    	IMcService qaService =McUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			qaService.setAsRunOffline(new Long(toolContentId));	
			}
    		catch(ToolException e)
			{
    			logger.debug("we should never come here");
			}
    		
    		logger.debug("post-RunAsOffline");
		}
    	logger.debug("end of simulating RunOffline on content id: " + toolContentId);
    }
	
    /**
     * Normally, a request to set defineLaterproperty of the content comes directly from container through the property inspector.
     * What we do below is simulate that for development purposes.
     * @param request
     */
    public void simulatePropertyInspector_setAsDefineLater(HttpServletRequest request)
    {
    	IMcService qaService =McUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			qaService.setAsDefineLater(new Long(toolContentId));	
    		}
    		catch(ToolException e)
			{
    			logger.debug("we should never come here");
			}
    		
    		logger.debug("post-setAsDefineLater");
		}
    	logger.debug("end of simulating setAsDefineLater on content id: " + toolContentId);
    }
}
