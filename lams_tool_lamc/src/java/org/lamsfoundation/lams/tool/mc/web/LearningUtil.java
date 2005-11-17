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
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McUtils;
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
     * updates the Map based on learner activity
     * selectOptionsCheckBox(HttpServletRequest request,McLearningForm mcLearningForm, String questionIndex)
     * 
     * @param request
     * @param form
     */
    public static  void selectOptionsCheckBox(HttpServletRequest request,McLearningForm mcLearningForm, String questionIndex)
    {
    	logger.debug("requested optionCheckBoxSelected...");
    	logger.debug("questionIndex: " + mcLearningForm.getQuestionIndex());
    	logger.debug("optionIndex: " + mcLearningForm.getOptionIndex());
    	logger.debug("optionValue: " + mcLearningForm.getOptionValue());
    	logger.debug("checked: " + mcLearningForm.getChecked());
    	
    	Map mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	
    	if (mapGeneralCheckedOptionsContent.size() == 0)
    	{
    		logger.debug("mapGeneralCheckedOptionsContent size is 0");
    		Map mapLeanerCheckedOptionsContent= new TreeMap(new McComparator());
    		
    		if (mcLearningForm.getChecked().equals("true"))
    			mapLeanerCheckedOptionsContent.put(mcLearningForm.getOptionIndex(), mcLearningForm.getOptionValue());
    		else
    			mapLeanerCheckedOptionsContent.remove(mcLearningForm.getOptionIndex());
    		
    		mapGeneralCheckedOptionsContent.put(mcLearningForm.getQuestionIndex(),mapLeanerCheckedOptionsContent);
    		request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
    	}
    	else
    	{
    		Map mapCurrentOptions=(Map) mapGeneralCheckedOptionsContent.get(questionIndex);
    		
    		logger.debug("mapCurrentOptions: " + mapCurrentOptions);
    		if (mapCurrentOptions != null)
    		{
    			if (mcLearningForm.getChecked().equals("true"))
    				mapCurrentOptions.put(mcLearningForm.getOptionIndex(), mcLearningForm.getOptionValue());
    			else
    				mapCurrentOptions.remove(mcLearningForm.getOptionIndex());
    			
    			logger.debug("updated mapCurrentOptions: " + mapCurrentOptions);
    			
    			mapGeneralCheckedOptionsContent.put(mcLearningForm.getQuestionIndex(),mapCurrentOptions);
    			request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);	
    		}
    		else
    		{
    			logger.debug("no options for this questions has been selected yet");
    			Map mapLeanerCheckedOptionsContent= new TreeMap(new McComparator());
    			        			
    			if (mcLearningForm.getChecked().equals("true"))
    				mapLeanerCheckedOptionsContent.put(mcLearningForm.getOptionIndex(), mcLearningForm.getOptionValue());
    			else
    				mapLeanerCheckedOptionsContent.remove(mcLearningForm.getOptionIndex());        			
    			
    			mapGeneralCheckedOptionsContent.put(mcLearningForm.getQuestionIndex(),mapLeanerCheckedOptionsContent);
    			request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
    		}
    	}
    	
    	mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    }
    
    
    /**
     * continueOptions(HttpServletRequest request)
     * 
     * @param request
     * @return boolean
     */
    public static boolean continueOptions(HttpServletRequest request)
    {
	 	IMcService mcService =McUtils.getToolService(request);
	 	
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
     * readParameters(HttpServletRequest request, McLearningForm mcLearningForm)
     * 
     * @param request
     * @param mcLearningForm
     */
    public static void readParameters(HttpServletRequest request, McLearningForm mcLearningForm)
    {
    	String optionCheckBoxSelected=request.getParameter("optionCheckBoxSelected");
    	logger.debug("parameter optionCheckBoxSelected: " + optionCheckBoxSelected);
    	if ((optionCheckBoxSelected != null) && optionCheckBoxSelected.equals("1"))
    	{
    		logger.debug("parameter optionCheckBoxSelected is selected " + optionCheckBoxSelected);
    		mcLearningForm.setOptionCheckBoxSelected("1");
    	}
    	
    	String questionIndex=request.getParameter("questionIndex");
    	logger.debug("parameter questionIndex: " + questionIndex);
    	if ((questionIndex != null))
    	{
    		logger.debug("parameter questionIndex is selected " + questionIndex);
    		mcLearningForm.setQuestionIndex(questionIndex);
    	}
    	
    	String optionIndex=request.getParameter("optionIndex");
    	logger.debug("parameter optionIndex: " + optionIndex);
    	if (optionIndex != null)
    	{
    		logger.debug("parameter optionIndex is selected " + optionIndex);
    		mcLearningForm.setOptionIndex(optionIndex);
    	}
    	
    	String optionValue=request.getParameter("optionValue");
    	logger.debug("parameter optionValue: " + optionValue);
    	if (optionValue != null)
    	{
    		mcLearningForm.setOptionValue(optionValue);
    	}
    	
    	
    	String checked=request.getParameter("checked");
    	logger.debug("parameter checked: " + checked);
    	if (checked != null)
    	{
    		logger.debug("parameter checked is selected " + checked);
    		mcLearningForm.setChecked(checked);
    	}
    }
    
    
    /**
     * assess(HttpServletRequest request, Map mapGeneralCheckedOptionsContent)
     * 
     * @param request
     * @param mapGeneralCheckedOptionsContent
     */
    public static void assess(HttpServletRequest request, Map mapGeneralCheckedOptionsContent, Long toolContentId)
    {
		Map mapGeneralCorrectOptions= new TreeMap(new McComparator());
		
		IMcService mcService =McUtils.getToolService(request);
		
		Map mapQuestionsUidContent=AuthoringUtil.rebuildQuestionUidMapfromDB(request,toolContentId);
		logger.debug("mapQuestionsUidContent : " + mapQuestionsUidContent);
		
		Iterator itMap = mapQuestionsUidContent.entrySet().iterator();
		Long mapIndex=new Long(1);
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            logger.debug("using mcQueContentId: " +  pairs.getValue());
            List correctOptions=(List) mcService.getPersistedSelectedOptions(new Long(pairs.getValue().toString()));
            Map mapCorrectOptions=buildMapCorrectOptions(correctOptions);
            logger.debug("mapCorrectOptions: " +  mapCorrectOptions);
        	mapGeneralCorrectOptions.put(mapIndex.toString(),mapCorrectOptions);
            mapIndex=new Long(mapIndex.longValue()+1);
		}
		logger.debug("mapGeneralCorrectOptions : " + mapGeneralCorrectOptions);
		
		Map mapLeanerAssessmentResults=compare(mapGeneralCorrectOptions,mapGeneralCheckedOptionsContent);
		logger.debug("mapLeanerAssessmentResults : " + mapLeanerAssessmentResults);
		request.getSession().setAttribute(MAP_LEARNER_ASSESSMENT_RESULTS, mapLeanerAssessmentResults);
    }
    
    
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
    
    
    public static Map compare(Map mapGeneralCorrectOptions,Map mapGeneralCheckedOptionsContent)
    {
    	logger.debug("incoming mapGeneralCorrectOptions : " + mapGeneralCorrectOptions);
    	logger.debug("incoming mapGeneralCheckedOptionsContent : " + mapGeneralCheckedOptionsContent);
    	
    	Map mapLeanerAssessmentResults= new TreeMap(new McComparator());		
		
    	Iterator itMap = mapGeneralCorrectOptions.entrySet().iterator();
    	boolean compareResult= false;
		while (itMap.hasNext()) {
			compareResult= false;
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
        	
            Iterator itCheckedMap = mapGeneralCheckedOptionsContent.entrySet().iterator();
            while (itCheckedMap.hasNext()) 
            {
            	compareResult= false;
            	Map.Entry checkedPairs = (Map.Entry)itCheckedMap.next();
                logger.debug("using the  pair: " +  checkedPairs.getKey() + " = " + checkedPairs.getValue());
                if (pairs.getKey().toString().equals(checkedPairs.getKey().toString()))
    			{
                    logger.debug("same key found: " +  pairs.getKey());
                    Map mapCorrectOptions=(Map) pairs.getValue();
                    Map mapCheckedOptions=(Map) checkedPairs.getValue();
                    
                    boolean isEqual=compareMaps(mapCorrectOptions, mapCheckedOptions);
                    boolean isEqualCross=compareMapsCross(mapCorrectOptions, mapCheckedOptions);
                    
                    compareResult= isEqual && isEqualCross; 
                    logger.debug("compareResult: " +compareResult);
                    logger.debug("question index: " + pairs.getKey().toString() + " has isEqual as:" +  isEqual + " and " + isEqualCross);
                    
                    mapLeanerAssessmentResults.put(pairs.getKey(), new Boolean(compareResult).toString());
        		}
            }
		}
		logger.debug("constructed mapLeanerAssessmentResults: " + mapLeanerAssessmentResults);
		return mapLeanerAssessmentResults;
    }
    
    
    public static boolean compareMaps(Map mapCorrectOptions, Map mapCheckedOptions)
	{
	   logger.debug("mapCorrectOptions: " +  mapCorrectOptions);
       logger.debug("mapCheckedOptions: " +  mapCheckedOptions);
       
       Iterator itMap = mapCorrectOptions.entrySet().iterator();
       boolean response=false;
       while (itMap.hasNext()) 
       {
       		Map.Entry pairs = (Map.Entry)itMap.next();
       		logger.debug("using the  pair value: " +   pairs.getValue());
			boolean optionExists=optionExists(pairs.getValue().toString(), mapCheckedOptions);
			
			if (optionExists == false)
			{
				return false;
			}
		}
       return true;
	}
    
    
    public static boolean compareMapsCross(Map mapCorrectOptions, Map mapCheckedOptions)
	{
	   logger.debug("mapCorrectOptions: " +  mapCorrectOptions);
       logger.debug("mapCheckedOptions: " +  mapCheckedOptions);
       
       Iterator itMap = mapCheckedOptions.entrySet().iterator();
       boolean response=false;
       while (itMap.hasNext()) 
       {
       		Map.Entry pairs = (Map.Entry)itMap.next();
       		logger.debug("using the  pair value: " +   pairs.getValue());
			boolean optionExists=optionExists(pairs.getValue().toString(), mapCorrectOptions);
			
			if (optionExists == false)
			{
				return false;
			}
		}
       return true;
	}
    
    
    
    
    public static boolean optionExists(String optionValue, Map generalMap)
    {
    	Iterator itMap = generalMap.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
       		Map.Entry pairsChecked = (Map.Entry)itMap.next();
       		logger.debug("using the  pairsChecked value: " +   pairsChecked.getValue());
       		if (pairsChecked.getValue().equals(optionValue))
				return true;
    	}	
    	return false;
    }
	
 }
