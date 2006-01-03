/*
 * Created on 16/05/2005
 */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McAttachmentDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McUploadedFile;
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
	 * populates data
	 * readData(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	 * 
	 * @param request
	 * @param mcAuthoringForm
	 */
    public static void readData(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	/** define the next tab as Basic tab by default*/
     	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
     	
     	McUtils.persistRichText(request);
     	AuthoringUtil.populateParameters(request, mcAuthoringForm);
    }
 	
	
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
  
        if ((selectedQuestion != null) && (selectedQuestion.length() > 0))
    	{
    		request.getSession().setAttribute(SELECTED_QUESTION,selectedQuestion);
    		logger.debug("updated SELECTED_QUESTION");
    	}
    }

    
	/**
	 * builds a candidate answers map for the selected question
	 * repopulateCurrentCheckBoxStatesMap(HttpServletRequest request)
	 * 
	 * @param request
	 * @return Map
	 */
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

		
    /**
     * verifies whether there are any duplicate candidate answer entries in the mapOptionsContent  
     * verifyDuplicatesOptionsMap(Map mapOptionsContent)
     * 
     * @param mapOptionsContent
     * @return boolean
     */
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
     * validates if submitted questions map is empty or not
     * validateQuestionsNotEmpty(Map mapQuestionsContent)
     * 
     * @param mapQuestionsContent
     * @return boolean
     */
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

    
    /**
     * builds a weights map for all the questions
     * repopulateCurrentWeightsMap(HttpServletRequest request, String parameterType)
     * 
     * @param request
     * @param parameterType
     * @return Map
     */
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

    
    public static Map sequenceMap(Map globalMap)
	{
    	logger.debug("globalMap:"+ globalMap);
    	Map mapTemp= new TreeMap(new McComparator());
    	
    	long mapCounter=0;
    	Iterator itMap = globalMap.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            Map optionsMap=(Map)pairs.getValue();
            logger.debug("optionsMap:"+ optionsMap);
            mapCounter++;
            mapTemp.put(new Long(mapCounter).toString(), optionsMap);
		}
    	
    	logger.debug("final mapTemp:"+ mapTemp);
    	return mapTemp;
	}
	
    

    /**
     * shrinks the size of the Map to only used entries
     * repopulateMap(HttpServletRequest request, String parameterType)
     * 
     * @param mapQuestionContent
     * @param request
     * @return Map
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

    
    public static String getRequiredWeightEntry(Map mapWeights, String questionIndex)
    {
    	logger.debug("mapWeights: " +  mapWeights);
    	
    	Iterator itMap = mapWeights.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if (questionIndex.equals(pairs.getKey().toString()))
    		{
            	String weight=pairs.getValue().toString();
            	logger.debug("required weight:" + weight);
            	return weight;
    		}
		}
    	return null;
    }
    
    /**
     * returns the options Map within mapGeneralOptionsContent indexed with the questionIndex parameter.
     * getRequiredOptionsEntry(String questionIndex)
     * 
     * @param questionIndex
     * @return Map
     */
    public static Map getRequiredOptionsEntry(Map mapGeneralOptionsContent, String questionIndex)
	{
    	logger.debug("mapGeneralOptionsContent: " +  mapGeneralOptionsContent);
    	
    	Iterator itMap = mapGeneralOptionsContent.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if (questionIndex.equals(pairs.getKey().toString()))
    		{
            	Map tempOptionsMap=(Map)pairs.getValue();
            	logger.debug("tempOptionsMap:" + tempOptionsMap);
            	return tempOptionsMap;
    		}
		}
    	return null;
	}
    
    
    public static Map shiftWeightsMap(Map mapWeights, String questionIndex , String direction)
    {
    	/* map to be returned */
    	Map mapTempWeights= new TreeMap(new McComparator());
    	mapTempWeights= mapWeights;
    	
    	String movableWeightEntry = getRequiredWeightEntry(mapWeights, questionIndex);
    	logger.debug("movableWeightEntry: " +  movableWeightEntry);
    	
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
    		
    	String shiftableWeightEntry = getRequiredWeightEntry(mapWeights, new Integer(shiftableIndex).toString());
    	logger.debug("shiftableWeightEntry: " +  shiftableWeightEntry);
    	
    	if (movableWeightEntry != null)
    	{
    		mapTempWeights.put(new Integer(shiftableIndex).toString(), movableWeightEntry);
    		logger.debug("mapTempWeights has been updated with movableWeightEntry");
    	}
    	else
    	{
    		mapTempWeights.put(new Integer(shiftableIndex).toString(), "");
    		logger.debug("mapTempWeights has been updated with a empty string");
    	}
    	
    	
    	if (shiftableWeightEntry != null)
    	{
    		mapTempWeights.put(questionIndex,shiftableWeightEntry);
    		logger.debug("mapTempWeights has been updated with shiftableWeightEntry");
    	}
    	else
    	{
    		mapTempWeights.put(questionIndex, "");
    		logger.debug("mapTempWeights has been updated with empty string");
    	}
		 
    	logger.debug("final shifted mapTempWeights: " +  mapTempWeights);
    	return mapTempWeights;
    }
    
    /**
     * shifts the options contents within mapGenaralOptionsContent and mapGeneralSelectedlOptionsContent
	 * shiftOptionsMap(Map mapGlobalOptionsContent, String questionIndex , String direction)
	 * 
	 * @param mapGlobalOptionsContent
	 * @param questionIndex
	 * @param direction
	 * @return Map
	 */
    public static Map shiftOptionsMap(Map mapGlobalOptionsContent, String questionIndex , String direction)
    {
    	/* mapGlobalOptionsContent refers to mapGeneralOptionsContent and mapGeneralSelectedlOptionsContent */
    	
    	/* map to be returned */
    	Map mapTempGeneralOptionsContent= new TreeMap(new McComparator());
    	mapTempGeneralOptionsContent= mapGlobalOptionsContent;
    	
    	Map  mapMovableOptionsEntry = getRequiredOptionsEntry(mapGlobalOptionsContent, questionIndex);
    	logger.debug("mapMovableOptionsEntry: " +  mapMovableOptionsEntry);
    	
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
    		
    	Map  mapShiftableOptionsEntry = getRequiredOptionsEntry(mapGlobalOptionsContent, new Integer(shiftableIndex).toString());
    	logger.debug("mapShiftableOptionsEntry: " +  mapShiftableOptionsEntry);
    	
    	if (mapMovableOptionsEntry != null)
    	{
    		mapTempGeneralOptionsContent.put(new Integer(shiftableIndex).toString(), mapMovableOptionsEntry);
    		logger.debug("mapTempGeneralOptionsContent has been updated with mapMovableOptionsEntry");
    	}
    	else
    	{
    		mapTempGeneralOptionsContent.put(new Integer(shiftableIndex).toString(), new TreeMap(new McComparator()));
    		logger.debug("mapTempGeneralOptionsContent has been updated with a new Map");
    	}
    	
    	
    	if (mapShiftableOptionsEntry != null)
    	{
    		mapTempGeneralOptionsContent.put(questionIndex,mapShiftableOptionsEntry);
    		logger.debug("mapTempGeneralOptionsContent has been updated with mapShiftableOptionsEntry");
    	}
    	else
    	{
    		mapTempGeneralOptionsContent.put(questionIndex,new TreeMap(new McComparator()));
    		logger.debug("mapTempGeneralOptionsContent has been updated with a new Map");
    	}
		 
    	logger.debug("final shifted mapTempGeneralOptionsContent: " +  mapTempGeneralOptionsContent);
    	return mapTempGeneralOptionsContent;
    }
    
    
    /**
     * removes options from mapGlobalOptionsContent
	 * removeFromOptionsMap(Map mapGlobalOptionsContent, String questionIndex )
	 * 
	 * @param mapGlobalOptionsContent
	 * @param questionIndex
	 * @param direction
	 * @return Map
	 */
    public static Map removeFromMap(Map mapContent, String index)
    {
    	/* mapGlobalOptionsContent refers to mapGenaralOptionsContent and mapGeneralSelectedlOptionsContent */
    	/* map to be returned */
    	Map mapTempContent= new TreeMap(new McComparator());
    	mapTempContent= mapContent;
    	
    	mapTempContent.remove(index);
		logger.debug("entry at index removed from mapTempContent...");
    	
		logger.debug("final mapTempContent: " +  mapTempContent);
    	return mapTempContent;
    }
    
	
    /**
	 * shifs questions Map when moving up and down
	 * shiftMap(Map mapQuestionsContent, String questionIndex , String movableQuestionEntry, String direction)
	 * 
	 * @param mapQuestionsContent
	 * @param questionIndex
	 * @param movableQuestionEntry
	 * @param direction
	 * @return Map
	 */    
    public static Map shiftMap(Map mapQuestionsContent, String questionIndex , String movableQuestionEntry, String direction)
    {
    	logger.debug("movableQuestionEntry: " +  movableQuestionEntry);
    	/* map to be returned */
    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
    	
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
                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), movableQuestionEntry);
                }
            }
    	}
    	else
    	{
    		mapTempQuestionsContent=mapQuestionsContent;
    	}
    		return mapTempQuestionsContent;
    }
	    

    /**
     * validates if the total weight for the submitted questiona is 100 or not. 
     * validateTotalWeight(Map mapWeights)
     * 
     * @param mapWeights
     * @return boolean
     */
    public static boolean validateTotalWeight(Map mapWeights)
    {
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

	   
    /**
     * validates total question weight when adding new questions
     * validateSubTotalWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     * @return boolean
     */
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

	   
    /**
     * checks candidate answers for a questions to see if there is at least one selected correct candidate answer 
     * validateOptions(HttpServletRequest request)
     * @param request
     * @return boolean 
     */
    public static boolean validateOptions(HttpServletRequest request)
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


    /**
     * builds a map to hold all the candidate answers for all the questions by accessing the db 
     * rebuildStartupGeneralOptionsQueIdfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
     * 
     * @param request
     * @param mapQuestionsUidContent
     * @return Map
     */
	public static Map rebuildStartupGeneralOptionsQueIdfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
	{
		IMcService mcService =McUtils.getToolService(request);
		Map map= new TreeMap(new McComparator());
    	
    	Iterator itMap = mapQuestionsUidContent.entrySet().iterator();
    	Long mapIndex=new Long(1);
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            String currentQuestionUid=pairs.getValue().toString();
            logger.debug("currentQuestionUid: " +  currentQuestionUid);
            List listQuestionOptions=mcService.findMcOptionsContentByQueId(new Long(currentQuestionUid));
            logger.debug("listQuestionOptions: " +  listQuestionOptions);
            Map mapQuestionOptions=generateOptionsQueIdMap(listQuestionOptions);
            map.put(mapIndex.toString(),mapQuestionOptions);
            mapIndex=new Long(mapIndex.longValue()+1);
		}
		return map;
	}


	/**
	 * builds a map to hold all the selected candidate answers for all the questions by accessing the db 
	 * rebuildStartupGeneralSelectedOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
	 * 
	 * @param request
	 * @param mapQuestionsUidContent
	 * @return Map
	 */
	public static Map rebuildStartupGeneralSelectedOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
	{
		IMcService mcService =McUtils.getToolService(request);
		Map mapStartupGeneralOptionsContent= new TreeMap(new McComparator());
    	
    	Iterator itMap = mapQuestionsUidContent.entrySet().iterator();
    	Long mapIndex=new Long(1);
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            String currentQuestionUid=pairs.getValue().toString();
            logger.debug("currentQuestionUid: " +  currentQuestionUid);
            List listQuestionOptions=mcService.getPersistedSelectedOptions(new Long(currentQuestionUid));
            logger.debug("listQuestionOptions: " +  listQuestionOptions);
            Map mapQuestionOptions=generateOptionsMap(listQuestionOptions);
            mapStartupGeneralOptionsContent.put(mapIndex.toString(),mapQuestionOptions);
            mapIndex=new Long(mapIndex.longValue()+1);
		}
		return mapStartupGeneralOptionsContent;
	}

	
	/**
	 * builds a map to hold all the candidate answers for all the questions by accessing the db
	 * rebuildStartupGeneralOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
	 * 
	 * @param request
	 * @param mapQuestionsUidContent
	 * @return
	 */
    public static Map rebuildStartupGeneralOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
	{
		IMcService mcService =McUtils.getToolService(request);
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
	
	
    /**
	 * builds a questions map from questions list 
	 * generateOptionsMap(List listQuestionOptions)
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
	 * generates a map from the questions list 
	 * generateOptionsQueIdMap(List listQuestionOptions)
	 * 
	 * @param listQuestionOptions
	 * @return Map
	 */
	public static Map generateOptionsQueIdMap(List listQuestionOptions)
	{
		Map mapOptsContent= new TreeMap(new McComparator());
		
		Iterator listIterator=listQuestionOptions.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
    		logger.debug("mcOptsContent: " +  mcOptsContent);
    		mapOptsContent.put(mapIndex.toString(),mcOptsContent.getMcQueContentId().toString());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return mapOptsContent;
	}
	
	
	/**
	 * builds a map to hold question texts 
	 * rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId)
	 * 
	 * @param request
	 * @param toolContentId
	 * @return Map
	 */
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

    
    /**
     * builds a map to hold persisted uid values for questions
     * rebuildQuestionUidMapfromDB(HttpServletRequest request, Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
    public static Map rebuildQuestionUidMapfromDB(HttpServletRequest request, Long toolContentId)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	
    	IMcService mcService =McUtils.getToolService(request);
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
    

    /**
     * builds a map to hold the weights for all the questions
     * rebuildWeightsMapfromDB(HttpServletRequest request, Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
    public static Map rebuildWeightsMapfromDB(HttpServletRequest request, Long toolContentId)
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
    		mapWeightsContent.put(mapIndex.toString(),mcQueContent.getWeight().toString());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	
    	logger.debug("refreshed Map:" + mapWeightsContent);
    	return mapWeightsContent;
    }
    
    
    /**
     * builds a map to hold all the correct feedback entries
     * buildInCorrectFeedbackMap(HttpServletRequest request, Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
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
    

    /**
     * builds a map to hold all the incorrect feedback entries
     * buildCorrectFeedbackMap(HttpServletRequest request, Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
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
    	logger.debug("doing persistOptionsFinal...");
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
    	logger.debug("doing persistOptions...");
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
    	logger.debug("doing createContent...");
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

    
    public static Map mergeMaps(Map map1, Map map2)
    {
    	Map mapMergedMap= new TreeMap(new McComparator());
    	logger.debug("merging maps now...");
    	
    	Iterator itMap1 = map1.entrySet().iterator();
		while (itMap1.hasNext()) 
        {
        	Map.Entry pairs = (Map.Entry)itMap1.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            mapMergedMap.put(pairs.getKey(), pairs.getValue());
        }
		
		logger.debug("adding the other map...");
		Iterator itMap2 = map2.entrySet().iterator();
		while (itMap2.hasNext()) 
        {
        	Map.Entry pairs = (Map.Entry)itMap2.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            mapMergedMap.put(pairs.getKey(), pairs.getValue());
        }
		
		logger.debug("final merged map: " + mapMergedMap);
		return mapMergedMap;
    }
    
    
    /**
     * builds a question weights map from only those questions with candidate answers
     * updateCurrentWeightsMapForQuestionWithNoOptions(HttpServletRequest request, Map mapWeights, Map mapQuestionsContent)
     * 
     * @param request
     * @param mapWeights
     * @param mapQuestionsContent
     * @return Map
     */
    public static Map updateCurrentWeightsMapForQuestionWithNoOptions(HttpServletRequest request, Map mapWeights, Map mapQuestionsContent)
	{
    	logger.debug("mapWeights: " + mapWeights);
    	logger.debug("mapQuestionsContent: " + mapQuestionsContent);
    	Map mapUpdatedWeightsMap= new TreeMap(new McComparator());
    	
    	boolean questionWithOptionsFound=false;
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
		while (itQuestionsMap.hasNext()) 
        {
        	Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            questionWithOptionsFound=false;
            Iterator itWeightsMap = mapWeights.entrySet().iterator();
            while (itWeightsMap.hasNext()) 
            {
            	Map.Entry pairsWeight = (Map.Entry)itWeightsMap.next();
                logger.debug("using the pairsWeight: " +  pairsWeight.getKey() + " = " + pairsWeight.getValue());
                
                if (pairs.getKey().toString().equals(pairsWeight.getKey().toString()))
                {
                	logger.debug("this pair is  eligible to be added to the updated weights map since this question is with candidate answers");
                	mapUpdatedWeightsMap.put(pairsWeight.getKey().toString(), pairsWeight.getValue().toString());
                	break;
                }
            }
        }
		logger.debug("final mapUpdatedWeightsMap: " +  mapUpdatedWeightsMap);
		return mapUpdatedWeightsMap;
	}
    
    
    /**
     * builds a questions map from only those questions with candidate answers
     * updateQuestionsMapForNoOptions(HttpServletRequest request, Map mapQuestionsContent, Map mapGeneralOptionsContent)
     * 
     * @param request
     * @param mapQuestionsContent
     * @param mapGeneralOptionsContent
     * @return Map
     */
    public static Map updateQuestionsMapForNoOptions(HttpServletRequest request, Map mapQuestionsContent, Map mapGeneralOptionsContent)
    {
    	logger.debug("doing updateQuestionsMapForNoOptions...");
    	IMcService mcService =McUtils.getToolService(request);
    	Map mapUpdatedQuestionsContent= new TreeMap(new McComparator());
    	
    	logger.debug("mapQuestionsContent: " + mapQuestionsContent);
    	logger.debug("mapGeneralOptionsContent: " + mapGeneralOptionsContent);
    	
	    request.getSession().setAttribute(QUESTIONS_WITHNO_OPTIONS, new Boolean(false));
	    
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
		boolean questionFound=false;
        while (itQuestionsMap.hasNext()) 
        {
        	Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());

            questionFound=false;
            Iterator itMapGOC = mapGeneralOptionsContent.entrySet().iterator();
    		while (itMapGOC.hasNext()) 
            {
    		    Map.Entry pairsGOC = (Map.Entry)itMapGOC.next();
    		    logger.debug("using the  pair: " +  pairsGOC.getKey() + " = " + pairsGOC.getValue());
    		    logger.debug("comparing the pairs : " +  pairs.getKey() + " and "  + pairsGOC.getKey());
    		    
    		    if (pairs.getKey().toString().equals(pairsGOC.getKey().toString()))
    		    {
    		        logger.debug("question has options, keep this one: " +  pairs.getKey());
    		        questionFound=true;
    		    }
            }
    		logger.debug("questionFound: " +  questionFound);
    		
    		if (questionFound == true)
    		{
    		    logger.debug("will add this question to the the questionsMap:" + pairs.getKey().toString());
    		    mapUpdatedQuestionsContent.put(pairs.getKey().toString(), pairs.getValue().toString());
    		}
    		else
    		{
    		    request.getSession().setAttribute(QUESTIONS_WITHNO_OPTIONS, new Boolean(true));
    		    logger.debug("setting QUESTIONS_WITHNO_OPTIONS to true.");
    		}
    		logger.debug("final mapUpdatedQuestionsContent:" + mapUpdatedQuestionsContent);
        }
        return mapUpdatedQuestionsContent;
    }
    
    
    /**
     * 
     * removes only unused question entries from the db. It keeps the valid entries since they get updated. 
     * cleanupRedundantQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent, McContent mcContent)
     * 
     * @param request
     * @param existingQuestions
     * @param mapQuestionsContent
     * @param mcContent
     */
    public static void cleanupRedundantQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent, McContent mcContent)
    {
    	logger.debug("doing cleanupRedundantQuestions...");
    	IMcService mcService =McUtils.getToolService(request);
    	
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
    				mcService.removeMcQueContent(mcQueContent);
        			logger.debug("removed mcQueContent from the db: " + mcQueContent);	
    			}
    		}
        }    		
    }
    
    
    /**
     * helps persist questions
     * selectAndPersistQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent,Map mapFeedbackIncorrect, Map mapFeedbackCorrect, Map mapGeneralOptionsContent, McContent mcContent)
     * 
     * @param request
     * @param existingQuestions
     * @param mapQuestionsContent
     * @param mapFeedbackIncorrect
     * @param mapFeedbackCorrect
     * @param mapGeneralOptionsContent
     * @param mcContent
     */
    public static void selectAndPersistQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent,Map mapFeedbackIncorrect, Map mapFeedbackCorrect, Map mapGeneralOptionsContent, Map mapWeights, McContent mcContent)
	{
    	logger.debug("doing selectAndPersistQuestions...");
    	logger.debug("mapWeights: " + mapWeights);
    	IMcService mcService =McUtils.getToolService(request);
    	
    	
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
		boolean questionContentFound=false;
        while (itQuestionsMap.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            questionContentFound = false;
            String displayOrder="1";
            String weight="";
            McQueContent mcQueContent=null;
            Iterator itExistingQuestions = existingQuestions.iterator();
            while (itExistingQuestions.hasNext()) 
            {
            	mcQueContent=(McQueContent)itExistingQuestions.next();
        		logger.debug("mcQueContent:" + mcQueContent);
        		
        		if (mcQueContent.getQuestion().equals(pairs.getValue()))
				{
                	logger.debug("existing mcQueContent found :" + mcQueContent);
                	questionContentFound=true;
                	displayOrder=pairs.getKey().toString(); 
                	weight=(String)mapWeights.get(displayOrder);
                	logger.debug("weight :" + weight);
                	
                	logger.debug("new display order for " + mcQueContent.getQuestion()+ " will be: "  + displayOrder);
                	logger.debug("new weight  for " + mcQueContent.getQuestion()+ " will be: "  + weight);
                	break;
				}
            }

            if (questionContentFound == true)
            {
            	logger.debug("questionContentFound is true: " + mcQueContent);
            	updateExistingQuestionContent(request,mapFeedbackIncorrect, mapFeedbackCorrect, mcQueContent, displayOrder, weight);
            }
            else
            {
            	logger.debug("questionContentFound is false: " + mcQueContent);
            	createQuestionContent(request,mapQuestionsContent, mapFeedbackIncorrect, mapFeedbackCorrect,  pairs.getValue().toString(), mapGeneralOptionsContent, mcContent);
            }
        }
    }
    
    
    /**
     * updates existing questions while persisting the questions
     * updateExistingQuestionContent(HttpServletRequest request, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McQueContent mcQueContent, String displayOrder)
     *  
     * @param request
     * @param mapFeedbackIncorrect
     * @param mapFeedbackCorrect
     * @param mcQueContent
     * @param displayOrder
     */
    public static void updateExistingQuestionContent(HttpServletRequest request, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McQueContent mcQueContent, String displayOrder, String weight)
    {
    	logger.debug("doing updateExistingQuestionContent...");
    	logger.debug("using displayOrder: " + displayOrder);
    	logger.debug("using weight: " + weight);
    	
    	String incorrectFeedback=(String)mapFeedbackIncorrect.get(displayOrder);
    	logger.debug("new  incorrectFeedback will be :" + incorrectFeedback);
    	String correctFeedback=(String)mapFeedbackCorrect.get(displayOrder);
    	logger.debug("new correctFeedback will be :" + correctFeedback);
    	
    	IMcService mcService =McUtils.getToolService(request);
    	mcQueContent.setDisplayOrder(new Integer(displayOrder));
    	mcQueContent.setDisabled(false);
    	mcQueContent.setWeight(new Integer(weight));
    	
    	if ((incorrectFeedback != null) && !(incorrectFeedback.equals("")))  
    		mcQueContent.setFeedbackIncorrect(incorrectFeedback);
    	
    	if ((correctFeedback != null) && !(correctFeedback.equals("")))  
    		mcQueContent.setFeedbackCorrect(correctFeedback);
    	
    	mcService.saveOrUpdateMcQueContent(mcQueContent);
		logger.debug("updated mcQueContent in the db: " + mcQueContent);
    }


	public static void removeRedundantOptionEntry(HttpServletRequest request, Map mapSGO, Map mapGO, Map options)
	{
		logger.debug("doing removeRedundantOptionEntry...");
		IMcService mcService =McUtils.getToolService(request);
		Iterator itSGOMap = mapSGO.entrySet().iterator();
    	boolean optionFound=false;
        while (itSGOMap.hasNext()) 
        {
            Map.Entry pairsSGO = (Map.Entry)itSGOMap.next();
            optionFound=false;
            Iterator itGOMap = mapGO.entrySet().iterator();
	    	while (itGOMap.hasNext()) 
	        {
	        	Map.Entry pairsGO = (Map.Entry)itGOMap.next();
	        	if (pairsSGO.getValue().equals(pairsGO.getValue()))
	        	{
	        		logger.debug("equal option found: " + pairsGO.getValue());
	        		optionFound=true;
	        		break;
	        	}
	        }
	    	
	    	if (optionFound == false)
        	{
        		logger.debug("options optionFound is false: remove this option");
        		int optionSize=options.size();
        		logger.debug("optionSize:" + optionSize);
        		String mcQueContentUid=(String)options.get(new Integer(optionSize).toString()); 
        		
        		logger.debug("mcQueContentUid: " + mcQueContentUid);
        		
        		String deletableOptionText=pairsSGO.getValue().toString();
        		logger.debug("deletableOptionText: " + deletableOptionText);
	        	
        		if (deletableOptionText != null && (!deletableOptionText.equals("")))
        		{
        			McOptsContent mcOptsContent = mcService.getOptionContentByOptionText(deletableOptionText, new Long(mcQueContentUid));
		        	logger.debug("mcOptsContent: " + mcOptsContent);
            		mcService.removeMcOptionsContent(mcOptsContent);
            		logger.debug("removed mcOptsContent from db: " + mcOptsContent);	
        		}
        		else
        		{
        			logger.debug("This should not happen: deletableOptionText is null..");
        		}
        	}
        }
	}

	
	/**
	 * removes the unused persisted candidate answers 
	 * cleanupRedundantOptions(HttpServletRequest request, Map mapStartupGeneralOptionsContent, Map mapStartupGeneralSelectedOptionsContent, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, Map mapStartupGeneralOptionsQueId)
	 *  
	 * @param request
	 * @param mapStartupGeneralOptionsContent
	 * @param mapStartupGeneralSelectedOptionsContent
	 * @param mapGeneralOptionsContent
	 * @param mapGeneralSelectedOptionsContent
	 * @param mapStartupGeneralOptionsQueId
	 */
	public static void cleanupRedundantOptions(HttpServletRequest request, Map mapStartupGeneralOptionsContent, Map mapStartupGeneralSelectedOptionsContent, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, Map mapStartupGeneralOptionsQueId)
	{
		logger.debug("doing cleanupRedundantOptions...");
		logger.debug("starting mapStartupGeneralOptionsContent...:" + mapStartupGeneralOptionsContent);
		logger.debug("using mapGeneralOptionsContent..: "+ mapGeneralOptionsContent);
    	IMcService mcService =McUtils.getToolService(request);
    	
    	Iterator itSGOMap = mapStartupGeneralOptionsContent.entrySet().iterator();
    	int displayOrder=0;
    	boolean optionFound=false;
        while (itSGOMap.hasNext()) 
        {
            Map.Entry pairsSGO = (Map.Entry)itSGOMap.next();

			optionFound=false;
            Iterator itGOMap = mapGeneralOptionsContent.entrySet().iterator();
	    	while (itGOMap.hasNext()) 
	        {
	            Map.Entry pairsGO = (Map.Entry)itGOMap.next();
	            if (pairsSGO.getKey().equals(pairsGO.getKey()))
	            {
	            	logger.debug("equal keys found: " + pairsSGO.getKey());
	            	Map mapSGO=(Map)pairsSGO.getValue();
	            	Map mapGO=(Map)pairsGO.getValue();
	            	logger.debug("mapSGO: " + mapSGO);
	            	logger.debug("mapGO: " + mapGO);
	            	
	            	Map options=(Map)mapStartupGeneralOptionsQueId.get(pairsSGO.getKey());
	            	logger.debug("options: " + options);
	            	removeRedundantOptionEntry(request, mapSGO, mapGO,options); 
	            }
	        }
        }
    }
    
	
	/**
	 * help persist candidate answers
	 * selectAndPersistOptions(HttpServletRequest request, Map mapStartupGeneralOptionsContent, Map mapStartupGeneralSelectedOptionsContent,
	 *  
	 * @param request
	 * @param mapStartupGeneralOptionsContent
	 * @param mapStartupGeneralSelectedOptionsContent
	 * @param mapGeneralOptionsContent
	 * @param mapGeneralSelectedOptionsContent
	 * @param mapStartupGeneralOptionsQueId
	 */
	public static void selectAndPersistOptions(HttpServletRequest request, Map mapStartupGeneralOptionsContent, Map mapStartupGeneralSelectedOptionsContent, 
			Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, Map mapStartupGeneralOptionsQueId)
	{
		logger.debug("doing selectAndPersistOptions...");
		logger.debug("starting selectAndPersistOptions..: "+ mapStartupGeneralOptionsContent);
		logger.debug("using mapGeneralOptionsContent..: "+ mapGeneralOptionsContent);
		IMcService mcService =McUtils.getToolService(request);
    	
    	Iterator itSGOMap = mapStartupGeneralOptionsContent.entrySet().iterator();
    	int displayOrder=0;
    	boolean optionFound=false;
        while (itSGOMap.hasNext()) 
        {
            Map.Entry pairsSGO = (Map.Entry)itSGOMap.next();

			optionFound=false;
            Iterator itGOMap = mapGeneralOptionsContent.entrySet().iterator();
	    	while (itGOMap.hasNext()) 
	        {
	            Map.Entry pairsGO = (Map.Entry)itGOMap.next();
	            if (pairsSGO.getKey().equals(pairsGO.getKey()))
	            {
	            	logger.debug("equal keys found: " + pairsSGO.getKey());
	            	String questionIndex=pairsSGO.getKey().toString();
	            	Map mapSGO=(Map)pairsSGO.getValue();
	            	Map mapGO=(Map)pairsGO.getValue();
	            	logger.debug("mapSGO: " + mapSGO);
	            	logger.debug("mapGO: " + mapGO);
	            	
	            	Map options=(Map)mapStartupGeneralOptionsQueId.get(pairsSGO.getKey());
	            	logger.debug("options: " + options);
	            	updateOrCreateOptions(request, mapSGO, mapGO,options, questionIndex, mapGeneralSelectedOptionsContent); 
	            }
	        }
        }
        
    	logger.debug("will do writePendingOptions for the new questions..");
    	writePendingOptions(request, mapGeneralOptionsContent, mapGeneralSelectedOptionsContent);    	
    }
    
    
	/**
	 * used to persist or update candidate answers
	 * updateOrCreateOptions(HttpServletRequest request, Map mapSGO, Map mapGO, Map options, String questionIndex, Map mapGeneralSelectedOptionsContent)
	 *  
	 * @param request
	 * @param mapSGO
	 * @param mapGO
	 * @param options
	 * @param questionIndex
	 * @param mapGeneralSelectedOptionsContent
	 */
    public static void updateOrCreateOptions(HttpServletRequest request, Map mapSGO, Map mapGO, Map options, String questionIndex, Map mapGeneralSelectedOptionsContent)
    {
    	logger.debug("doing updateOrCreateOptions...");
    	Iterator itOptionsGoMap = mapGO.entrySet().iterator();
		boolean optionContentFound=false;
        while (itOptionsGoMap.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itOptionsGoMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            optionContentFound = false;
            Iterator itOptionsSGO = mapSGO.entrySet().iterator();
            while (itOptionsSGO.hasNext()) 
            {
            	Map.Entry pairsSGO = (Map.Entry)itOptionsSGO.next();
            	if (pairsSGO.getValue().equals(pairs.getValue()))
				{
				    optionContentFound = true;
                	break;
				}
            }

            if (optionContentFound == false)
            {
            	String optionText=pairs.getValue().toString();
            	logger.debug("optionContentFound is false and option is: " + optionText);
            	createOptionContent(request, options, optionText,  questionIndex, mapGeneralSelectedOptionsContent);
            }
        }
    }


    /**
     * used to update existing candidate answers
     * updateExistingOptionContent(HttpServletRequest request, Map options, String optionText)
     * 
     * @param request
     * @param options
     * @param optionText
     */
	public static void updateExistingOptionContent(HttpServletRequest request, Map options, String optionText)
    {
		logger.debug("doing updateExistingOptionContent...");
    	IMcService mcService =McUtils.getToolService(request);
    	int optionSize=options.size();
		logger.debug("optionSize:" + optionSize);
		String mcQueContentUid=(String)options.get(new Integer(optionSize).toString()); 
		logger.debug("mcQueContentUid: " + mcQueContentUid);
		
		McOptsContent mcOptsContent = mcService.getOptionContentByOptionText(optionText, new Long(mcQueContentUid));
		logger.debug("mcOptsContent: " + mcOptsContent);
		mcService.removeMcOptionsContent(mcOptsContent);
		logger.debug("removed mcOptsContent from db: " + mcOptsContent);
    }

	
	/**
	 * persists candidate answers
	 * writePendingOptions(HttpServletRequest request, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent)
	 * 
	 * @param request
	 * @param mapGeneralOptionsContent
	 * @param mapGeneralSelectedOptionsContent
	 */
	public static void writePendingOptions(HttpServletRequest request, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent)
	{
		logger.debug("doing writePendingQuestions...");
		Iterator itGO = mapGeneralOptionsContent.entrySet().iterator();
        while (itGO.hasNext()) 
        {
        	Map.Entry pairsGO = (Map.Entry)itGO.next();
           	Map pendingOptions=(Map) pairsGO.getValue();
           	logger.debug("pendingOptions: " + pendingOptions);
           	writePendingOption(request, pairsGO.getKey().toString(), pendingOptions, mapGeneralSelectedOptionsContent);
        }
	}

	
	/**
	 * persists a candidate answer 
	 * writePendingOption(HttpServletRequest request, String questionIndex, Map pendingOptions, Map mapGeneralSelectedOptionsContent)
	 * 
	 * @param request
	 * @param questionIndex
	 * @param pendingOptions
	 * @param mapGeneralSelectedOptionsContent
	 */
	public static void writePendingOption(HttpServletRequest request, String questionIndex, Map pendingOptions, Map mapGeneralSelectedOptionsContent)
	{
    	logger.debug("doing writePendingOption...");
		logger.debug("questionIndex: " + questionIndex);
		logger.debug("pendingOptions: " + pendingOptions);
		IMcService mcService =McUtils.getToolService(request);
		
		Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
		logger.debug("getting existing content with id:" + toolContentId);
		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("existing mcContent:" + mcContent);
		McQueContent mcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(questionIndex), mcContent.getUid());
		logger.debug("exracted mcQueContent:" + mcQueContent);
		
		if (mcQueContent != null)
		{
			Long mcQueContentUid=mcQueContent.getUid();
			logger.debug("mcQueContentUid:" + mcQueContentUid);
			
			Iterator itPQ = pendingOptions.entrySet().iterator();
			boolean isOptionSelected=false;
	        while (itPQ.hasNext())
	        {
	        	Map.Entry pairsPQ = (Map.Entry)itPQ.next();
	        	String optionText=pairsPQ.getValue().toString() ;
	        	logger.debug("optionText: " + optionText);
	        	isOptionSelected=false;
	        	isOptionSelected=isOptionSelected(mapGeneralSelectedOptionsContent, optionText, questionIndex);
	    		logger.debug("isOptionSelected: " + isOptionSelected);
	    		
	    		McOptsContent mcOptsContent = mcService.getOptionContentByOptionText(optionText, mcQueContentUid);
	    		logger.debug("mcOptsContent: " + mcOptsContent);
	    		
	    		if (mcOptsContent == null)
	    		{
	    			mcOptsContent = new McOptsContent(isOptionSelected, pairsPQ.getValue().toString(),mcQueContent , new TreeSet());
	    			logger.debug("created new mcOptsContent:" + mcOptsContent);
	    		}
	        	else
	        	{
	        		logger.debug("this option is already persisted mcQueContent, just look after isOptionSelected:" + mcQueContent);
	        		mcOptsContent.setCorrectOption(isOptionSelected);
	        	}
	    		mcService.saveMcOptionsContent(mcOptsContent);
				logger.debug("persisted mcQueContent: " + mcQueContent);
	        }
		}
	}
	
	
	/**
	 * persists a candidate answer
	 * createOptionContent(HttpServletRequest request, Map options, String optionText, 
			String questionIndex, Map mapGeneralSelectedOptionsContent)
	 * 
	 * @param request
	 * @param options
	 * @param optionText
	 * @param questionIndex
	 * @param mapGeneralSelectedOptionsContent
	 */
	public static void createOptionContent(HttpServletRequest request, Map options, String optionText, 
			String questionIndex, Map mapGeneralSelectedOptionsContent)
    {
		logger.debug("doing createOptionContent...");
       	IMcService mcService =McUtils.getToolService(request);
    	int optionSize=options.size();
		logger.debug("optionSize:" + optionSize);
		logger.debug("optionText:" + optionText);
		
		String mcQueContentUid=(String)options.get(new Integer(optionSize).toString()); 
		logger.debug("mcQueContentUid: " + mcQueContentUid);

		boolean isOptionSelected=isOptionSelected(mapGeneralSelectedOptionsContent, optionText, questionIndex);
		logger.debug("isOptionSelected: " + isOptionSelected);
		
		if (mcQueContentUid != null)
		{
			logger.debug("mcQueContentUid not null: " + mcQueContentUid);
			McQueContent mcQueContent= mcService.getMcQueContentByUID(new Long(mcQueContentUid));
			logger.debug("mcQueContent: " + mcQueContent);
			
			McOptsContent mcOptsContent = mcService.getOptionContentByOptionText(optionText, new Long(mcQueContentUid));
			if (mcOptsContent == null)
			{
				logger.debug("mcOptsContent null: " + mcOptsContent);
				mcOptsContent = new McOptsContent(isOptionSelected, optionText,mcQueContent , new TreeSet());
				mcService.saveMcOptionsContent(mcOptsContent);
				logger.debug("persisted mcQueContent: " + mcQueContent);	
			}
			else
			{
				logger.debug("mcOptsContent not null: " + mcOptsContent);
				logger.debug("mcOptsContent already exists: " + mcQueContent);
			}	
		}
		else
		{
			logger.debug("mcQueContentUid null: " + mcQueContentUid);
			Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
			logger.debug("getting existing content with id:" + toolContentId);
			McContent mcContent=mcService.retrieveMc(toolContentId);
			logger.debug("existing mcContent:" + mcContent);
			logger.debug("mcOptsContent is null, get the mcQueContent in an alternative way, use questionIndex: " + questionIndex);
			McQueContent mcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(questionIndex), mcContent.getUid());
			logger.debug("exracted mcQueContent:" + mcQueContent);

			if (mcQueContent != null)
			{
				McOptsContent mcOptsContent = new McOptsContent(isOptionSelected, optionText,mcQueContent , new TreeSet());
				mcService.saveMcOptionsContent(mcOptsContent);
				logger.debug("persisted mcQueContent: " + mcQueContent);	
			}
		}
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
	 * persistw a question content 
	 * createQuestionContent(HttpServletRequest request, Map mapQuestionsContent, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, String question, Map mapGeneralOptionsContent, McContent mcContent)
	 *  
	 * @param request
	 * @param mapQuestionsContent
	 * @param mapFeedbackIncorrect
	 * @param mapFeedbackCorrect
	 * @param question
	 * @param mapGeneralOptionsContent
	 * @param mcContent
	 */
    public static void createQuestionContent(HttpServletRequest request, Map mapQuestionsContent, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, String question, Map mapGeneralOptionsContent, McContent mcContent)
    {
    	logger.debug("using createQuestionContent with question: " + question);
    	IMcService mcService =McUtils.getToolService(request);
    	
    	int displayOrder= getNewDisplayOrder(mapQuestionsContent, question);
    	logger.debug("displayOrder: " + displayOrder);
    	
    	boolean isQuestionWithOptions=isQuestionWithOptions(mapGeneralOptionsContent, displayOrder);
    	logger.debug("isQuestionWithOptions: " + isQuestionWithOptions);
    	
    	if (isQuestionWithOptions == true)
    	{
    	    Map mapWeights= (Map)request.getSession().getAttribute(MAP_WEIGHTS);
        	logger.debug("MAP_WEIGHTS: " + MAP_WEIGHTS);
        	
        	String weight=(String)mapWeights.get(new Integer(displayOrder).toString());
        	logger.debug("new weight will be:" + weight);
        	
        	String incorrectFeedback=(String)mapFeedbackIncorrect.get(new Integer(displayOrder).toString());
        	logger.debug("new  incorrectFeedback will be :" + incorrectFeedback);

        	String correctFeedback=(String)mapFeedbackCorrect.get(new Integer(displayOrder).toString());
        	logger.debug("new correctFeedback will be :" + correctFeedback);
        	
        	McQueContent mcQueContent=  new McQueContent(question,
          	 		new Integer(displayOrder),
          	 		new Integer(weight),
    				false,
    				incorrectFeedback,
    				correctFeedback,
    				mcContent,
    				new HashSet(),
    				new HashSet()
    				);
        	
        	mcService.saveOrUpdateMcQueContent(mcQueContent);
    		logger.debug("created a new mcQueContent in the db: " + mcQueContent);    
    	}
    	else
    	{
    	    logger.debug("we don't create this question's content since it doesn't have options associated with it.");
    	    //mapCopyQuestionsContent.remove(new Long(displayOrder).toString());
    	    //request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapCopyQuestionsContent);
    	}
    }
    

    public static int getNewDisplayOrder(Map mapQuestionsContent, String question)
    {
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
    	int displayOrder=0;
        while (itQuestionsMap.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if (pairs.getValue().equals(question))
            {
            	logger.debug("question found:" + question);
            	displayOrder=new Integer(pairs.getKey().toString()).intValue();
            	logger.debug("displayOrder:" + displayOrder);
            }
        }    
        return displayOrder;
    }
    
    
    /**
     * finds out whether a question has options (candidate answers) or not.
     * 
     * isQuestionWithOptions(Map mapGeneralOptionsContent, int displayOrder)
     * 
     * @param mapGeneralOptionsContent
     * @param displayOrder
     * @return
     */
    public static boolean isQuestionWithOptions(Map mapGeneralOptionsContent, int displayOrder)
    {
    	logger.debug("mapGeneralOptionsContent:" + mapGeneralOptionsContent);
    	
    	Iterator itQuestionsMap = mapGeneralOptionsContent.entrySet().iterator();
        while (itQuestionsMap.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            logger.debug("current questionIndex: " + new Integer(pairs.getKey().toString()).intValue());
            int questionIndex=new Integer(pairs.getKey().toString()).intValue();
            if (questionIndex == displayOrder)
            {
            	logger.debug("questionIndex found:" + questionIndex);
            	return true;
            }
        }
        return false;
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

    
    public static int getNextAvailableDisplayOrder(HttpServletRequest request, Long mcContentId)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	List listDisplayOrder=mcService.getNextAvailableDisplayOrder(mcContentId.longValue());
    	
    	Iterator itListDisplayOrder = listDisplayOrder.iterator();
    	int maxDisplayOrder=1;
        while (itListDisplayOrder.hasNext())
        {
        	McQueContent mcQueContent=(McQueContent)itListDisplayOrder.next();
        	logger.debug("mcQueContent:" + mcQueContent);
        	int currentDisplayOrder=mcQueContent.getDisplayOrder().intValue();
        	if (currentDisplayOrder > maxDisplayOrder)
        		 maxDisplayOrder = currentDisplayOrder;
        }
        logger.debug("current biggest displayOrder is: " + maxDisplayOrder);
        return maxDisplayOrder+1;
    	
    }
    
    
    public static void  refreshMaps(HttpServletRequest request, long toolContentId)
    {
    	Map mapQuestionsUidContent=AuthoringUtil.rebuildQuestionUidMapfromDB(request, new Long(toolContentId));
    	logger.debug("mapQuestionsUidContent:" + mapQuestionsUidContent);
    	
    	if (mapQuestionsUidContent.size() > 0)
    	{
    		Map mapStartupGeneralOptionsContent=AuthoringUtil.rebuildStartupGeneralOptionsContentMapfromDB(request, mapQuestionsUidContent);
        	logger.debug("mapStartupGeneralOptionsContent:" + mapStartupGeneralOptionsContent);
        	request.getSession().setAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT, mapStartupGeneralOptionsContent);
        	
        	Map mapStartupGeneralSelectedOptionsContent=AuthoringUtil.rebuildStartupGeneralSelectedOptionsContentMapfromDB(request, mapQuestionsUidContent);
        	logger.debug("mapStartupGeneralSelectedOptionsContent:" + mapStartupGeneralSelectedOptionsContent);
        	request.getSession().setAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT, mapStartupGeneralSelectedOptionsContent);
        	
        	Map mapStartupGeneralOptionsQueId=AuthoringUtil.rebuildStartupGeneralOptionsQueIdfromDB(request, mapQuestionsUidContent);
        	logger.debug("mapStartupGeneralOptionsQueId:" + mapStartupGeneralOptionsQueId);
        	request.getSession().setAttribute(MAP_STARTUP_GENERAL_OPTIONS_QUEID, mapStartupGeneralOptionsQueId);	
    	}
    }
    
    
    public static McAttachmentDTO uploadFile(HttpServletRequest request, McAuthoringForm mcAuthoringForm, boolean isOfflineFile) throws RepositoryCheckedException 
	{
    	logger.debug("doing uploadFile...");
    	logger.debug("isOfflineFile:" + isOfflineFile);
    	
    	InputStream stream=null; 
		String fileName=null; 
		String mimeType=null;
		String fileProperty=null;
    	
    	if (isOfflineFile)
    	{
    		FormFile theOfflineFile = mcAuthoringForm.getTheOfflineFile();
    		logger.debug("retrieved theOfflineFile: " + theOfflineFile);
    		
    		try
    		{
    			stream = theOfflineFile.getInputStream();
    			fileName=theOfflineFile.getFileName();
    			if (fileName.length() == 0)      
        		{
        			return null;
        		}
    			logger.debug("retrieved fileName: " + fileName);
    	    	fileProperty="OFFLINE";
    	    	
    	    }
    		catch(FileNotFoundException e)
    		{
    			logger.debug("filenotfound exception occured in accessing the repository server for the offline file : " + e.getMessage());
    		}
    		catch(IOException e)
    		{
    			logger.debug("io exception occured in accessing the repository server for the offline file : " + e.getMessage());
    		}
    		
    		if (fileName.length() > 0)     
    		{
    			List listUploadedOfflineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
        		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
        		int index=findFileNameIndex(listUploadedOfflineFileNames, fileName);
        		logger.debug("index:" + index);
        		if (index == 0)
        		{
        			listUploadedOfflineFileNames.add(fileName);
            		logger.debug("listUploadedOfflineFileNames after add :" + listUploadedOfflineFileNames);
            		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);	
        		}
    		}
    		
    	}
    	else
    	{
    		FormFile theOnlineFile = mcAuthoringForm.getTheOnlineFile();
    		logger.debug("retrieved theOnlineFile: " + theOnlineFile);
    		
    		try
    		{
    			stream = theOnlineFile.getInputStream();
    			fileName=theOnlineFile.getFileName();
    			
    			if (fileName.length() == 0)      
        		{
        			return null;
        		}
    			
    			logger.debug("retrieved fileName: " + fileName);
    			fileProperty="ONLINE";
    	    	
    	    }
    		catch(FileNotFoundException e)
    		{
    			logger.debug("filenotfound exception occured in accessing the repository server for the online file : " + e.getMessage());
    		}
    		catch(IOException e)
    		{
    			logger.debug("io exception occured in accessing the repository server for the online file : " + e.getMessage());
    		}

    		if (fileName.length() > 0)     
    		{
    			List listUploadedOnlineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
        		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
        		int index=findFileNameIndex(listUploadedOnlineFileNames, fileName);
        		logger.debug("index:" + index);
        		if (index == 0)
        		{
        			listUploadedOnlineFileNames.add(fileName);
            		logger.debug("listUploadedOnlineFileNames after add :" + listUploadedOnlineFileNames);
            		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);	
        		}
    		}
    	}
    	
    	IMcService mcService =McUtils.getToolService(request);
		logger.debug("calling uploadFile with:");
		logger.debug("istream:" + stream);
		logger.debug("filename:" + fileName);
		logger.debug("mimeType:" + mimeType);
		logger.debug("fileProperty:" + fileProperty);
		
		NodeKey nodeKey=mcService.uploadFile(stream, fileName, mimeType, fileProperty);
		logger.debug("nodeKey:" + nodeKey);
		logger.debug("nodeKey uuid:" + nodeKey.getUuid());
		
		McAttachmentDTO mcAttachmentDTO= new McAttachmentDTO();
		mcAttachmentDTO.setUid(null);
 		mcAttachmentDTO.setUuid(nodeKey.getUuid().toString());
 		mcAttachmentDTO.setFilename(fileName);
 		mcAttachmentDTO.setOfflineFile(isOfflineFile);
 		
		return mcAttachmentDTO;
	}
    

    
    public static List populateMetaDataAsAttachments(List listOfflineFilesMetaData)
    {
    	List listAttachments=new LinkedList();
    	
    	Iterator itList = listOfflineFilesMetaData.iterator();
    	while (itList.hasNext())
        {
        	McUploadedFile mcUploadedFile=(McUploadedFile)itList.next();
        	logger.debug("mcUploadedFile:" + mcUploadedFile);
        	logger.debug("mcUploadedFile details, uid" + mcUploadedFile.getUid().toString());
        	logger.debug("mcUploadedFile details, uuid" + mcUploadedFile.getUuid());
        	logger.debug("mcUploadedFile details, filename" + mcUploadedFile.getFilename());
        	logger.debug("mcUploadedFile details, isOfflineFile" + !mcUploadedFile.isFileOnline());
        	
        	McAttachmentDTO mcAttachmentDTO= new McAttachmentDTO();
        	mcAttachmentDTO.setUid(mcUploadedFile.getUid().toString());
     		mcAttachmentDTO.setUuid(mcUploadedFile.getUuid());
     		mcAttachmentDTO.setFilename(mcUploadedFile.getFilename());
     		mcAttachmentDTO.setOfflineFile(!mcUploadedFile.isFileOnline());
     		
     		listAttachments.add(mcAttachmentDTO);
     		logger.debug("listAttachments after add" + listAttachments);
        }
 		logger.debug("final listAttachments after populating all: " + listAttachments);
    	return listAttachments;
    }
    
    
    public static List populateMetaDataAsFilenames(List listFilesMetaData)
    {
    	List listFilenames=new LinkedList();
    	
    	Iterator itList = listFilesMetaData.iterator();
    	while (itList.hasNext())
        {
    		McAttachmentDTO mcAttachmentDTO=(McAttachmentDTO)itList.next();
    		logger.debug("current filename" + mcAttachmentDTO.getFilename());
     		listFilenames.add(mcAttachmentDTO.getFilename());
     		logger.debug("listFilenames after add" + listFilenames);
        }
 		logger.debug("final listFilenames after populating all: " + listFilenames);
    	return listFilenames;
    }
    
    
    public static void removeFileItem(HttpServletRequest request, String filename, String offlineFile)
	{
    	logger.debug("offlineFile:" + offlineFile);
    	if (offlineFile.equals("1"))
    	{
    		logger.debug("will remove an offline file");
    		List listUploadedOfflineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
    		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
    		
    		listUploadedOfflineFileNames.remove(filename);
    		logger.debug("removed offline filename:" + filename);
    		
    		logger.debug("listUploadedOfflineFileNames after remove :" + listUploadedOfflineFileNames);
    		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);
    	}
    	else
    	{
    		logger.debug("will remove an online file");
    		List listUploadedOnlineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
    		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
    		
    		listUploadedOnlineFileNames.remove(filename);
    		logger.debug("removed online filename:" + filename);
    		
    		logger.debug("listUploadedOnlineFileNames after remove :" + listUploadedOnlineFileNames);
    		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);
    	}
	}
    
    
    public static int findFileNameIndex(List listUploadedFileNames, String filename)
    {
    	Iterator itListUploadedFileNames = listUploadedFileNames.iterator();
    	int mainIndex=0;
    	while (itListUploadedFileNames.hasNext())
        {
    		mainIndex++;
        	String currentFilename=(String) itListUploadedFileNames.next();
        	logger.debug("currentFilename :" + currentFilename);
        	if (currentFilename.equals(filename))
			{
        		logger.debug("currentFilename found in the list at mainIndex :" + mainIndex);
        		return mainIndex;
			}
        }
    	return 0;
    }
    
    
    public static List removeFileItem(List listFilesMetaData, String uuid)
    {
    	McAttachmentDTO deletableAttachmentDTO=null;
    	
    	Iterator itList = listFilesMetaData.iterator();
    	int mainIndex=0;
    	while (itList.hasNext())
        {
    		mainIndex++;
    		McAttachmentDTO currentAttachmentDTO=(McAttachmentDTO) itList.next();
    		logger.debug("currentAttachmentDTO :" + currentAttachmentDTO);
    		logger.debug("currentAttachmentDTO uuid :" + currentAttachmentDTO.getUuid());
    		
    		if (currentAttachmentDTO.getUuid().equals(uuid))
			{
    			logger.debug("equal uuid found uuid :" + uuid);
    			deletableAttachmentDTO=currentAttachmentDTO;
    			break;
			}
        }
    	
    	logger.debug("equal uuid found at index :" + mainIndex);
    	logger.debug("deletable attachment is:" + deletableAttachmentDTO);
    	
    	listFilesMetaData.remove(deletableAttachmentDTO);
    	logger.debug("listOfflineFilesMetaData after remove:" + listFilesMetaData);
    	
    	return listFilesMetaData;
    }
    
    public static void persistFilesMetaData(HttpServletRequest request, boolean isOfflineFile, McContent mcContent)
    {
    	IMcService mcService =McUtils.getToolService(request);

    	List listFilesMetaData=null;
    	logger.debug("doing persistFilesMetaData...");
    	logger.debug("isOfflineFile:" + isOfflineFile);
    	
    	if (isOfflineFile)
    	{
    		listFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
    	}
    	else
    	{
    		listFilesMetaData =(List)request.getSession().getAttribute(LIST_ONLINEFILES_METADATA);
    	}
    	logger.debug("listFilesMetaData:" + listFilesMetaData);
    	
    	Iterator itListFilesMetaData = listFilesMetaData.iterator();
        while (itListFilesMetaData.hasNext())
        {
        	McAttachmentDTO mcAttachmentDTO=(McAttachmentDTO)itListFilesMetaData.next();
        	logger.debug("mcAttachmentDTO:" + mcAttachmentDTO);
        	String uid=mcAttachmentDTO.getUid();
        	logger.debug("uid:" + uid);
        	
        	String uuid=mcAttachmentDTO.getUuid();
        	boolean isOnlineFile=!mcAttachmentDTO.isOfflineFile();
        	String fileName=mcAttachmentDTO.getFilename();
        	
        	if (uid == null)
        	{
        		logger.debug("persisting files metadata...");
        		if (!mcService.isUuidPersisted(uuid))
        		{
        			mcService.persistFile(uuid, isOnlineFile, fileName, mcContent);
        		}
        	}
        }
    }
    
    
    public static List extractFileNames(List listFilesMetaData)
    {
    	Iterator itList = listFilesMetaData.iterator();
    	LinkedList listFilenames= new LinkedList();
    	
        while (itList.hasNext())
        {
        	McAttachmentDTO mcAttachmentDTO=(McAttachmentDTO)itList.next();
        	String filename=mcAttachmentDTO.getFilename();
        	logger.debug("extracted filename: " + filename);
        	listFilenames.add(filename);
        }
    	logger.debug("final extracted listFilenames: " + listFilenames);
    	return listFilenames;
    }
    
    
    public static void removeRedundantOfflineFileItems(HttpServletRequest request, McContent mcContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	List allOfflineFilenames=mcService.retrieveMcUploadedOfflineFilesName(mcContent.getUid());
    	logger.debug("allOfflineFilenames:" + allOfflineFilenames);
    	
    	List listOfflineFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		
 		List listUploadedOfflineFileNames=extractFileNames(listOfflineFilesMetaData);
		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
    	
		boolean matchFound=false;
    	Iterator itAllOfflineFiles = allOfflineFilenames.iterator();
        while (itAllOfflineFiles.hasNext())
        {
        	String filename =(String)itAllOfflineFiles.next();
        	logger.debug("filename: " + filename);
        
        	matchFound=false;
        	Iterator itFiles = listUploadedOfflineFileNames.iterator();
            while (itFiles.hasNext())
            {
            	matchFound=false;
            	String currentFilename =(String)itFiles.next();
            	logger.debug("currentFilename: " + currentFilename);
            	
            	if (filename.equals(currentFilename))
            	{
            		logger.debug("filename match found : " + currentFilename);
            		matchFound=true;
            		break;
            	}
    		}
            
            logger.debug("matchFound : " + matchFound);
            if (matchFound == false)
            {
            	logger.debug("matchFound is false for filename: " + filename);
            	mcService.removeOffLineFile(filename, mcContent.getUid()); 
            	logger.debug("filename removed: " + filename);
            }
		}
    }
    
    
    public static void removeRedundantOnlineFileItems(HttpServletRequest request, McContent mcContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	List allOnlineFilenames=mcService.retrieveMcUploadedOnlineFilesName(mcContent.getUid());
    	logger.debug("allOnlineFilenames:" + allOnlineFilenames);
    	
    	List listOnlineFilesMetaData =(List)request.getSession().getAttribute(LIST_ONLINEFILES_METADATA);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		
 		List listUploadedOnlineFileNames=extractFileNames(listOnlineFilesMetaData);
		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
    	
    	
		boolean matchFound=false;
    	Iterator itAllOnlineFiles = allOnlineFilenames.iterator();
        while (itAllOnlineFiles.hasNext())
        {
        	String filename =(String)itAllOnlineFiles.next();
        	logger.debug("filename: " + filename);
        
        	matchFound=false;
        	Iterator itFiles = listUploadedOnlineFileNames.iterator();
            while (itFiles.hasNext())
            {
            	matchFound=false;
            	String currentFilename =(String)itFiles.next();
            	logger.debug("currentFilename: " + currentFilename);
            	
            	if (filename.equals(currentFilename))
            	{
            		logger.debug("filename match found : " + currentFilename);
            		matchFound=true;
            		break;
            	}
    		}
            
            logger.debug("matchFound : " + matchFound);
            if (matchFound == false)
            {
            	logger.debug("matchFound is false for filename: " + filename);
            	mcService.removeOnLineFile(filename, mcContent.getUid()); 
            	logger.debug("filename removed: " + filename);
            }
		}
    }
    
    
    public void simulatePropertyInspector_RunOffline(HttpServletRequest request)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			mcService.setAsRunOffline(new Long(toolContentId));	
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
    	IMcService mcService =McUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			mcService.setAsDefineLater(new Long(toolContentId));	
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
