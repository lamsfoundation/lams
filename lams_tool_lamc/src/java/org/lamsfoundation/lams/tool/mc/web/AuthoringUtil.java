/*
 * Created on 16/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
