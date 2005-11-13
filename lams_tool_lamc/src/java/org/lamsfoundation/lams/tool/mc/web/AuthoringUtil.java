/*
 * Created on 16/05/2005
 */
package org.lamsfoundation.lams.tool.mc.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;

/**
 * 
 * Keeps all operations needed for Authoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class AuthoringUtil implements McAppConstants {
	static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());
	
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

	
    public Map rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId)
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
