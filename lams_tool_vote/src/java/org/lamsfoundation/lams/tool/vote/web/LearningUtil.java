/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.vote.web;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;

/**
 * 
 * Keeps all operations needed for Learning mode. 
 * @author Ozgur Demirtas
 *
 */
public class LearningUtil implements VoteAppConstants {
	static Logger logger = Logger.getLogger(LearningUtil.class.getName());
	
    public static Map buildQuestionContentMap(HttpServletRequest request, VoteContent voteContent)
    {
    	IVoteService voteService =VoteUtils.getToolService(request);
    	Map mapQuestionsContent= new TreeMap(new VoteComparator());
    	
        Iterator contentIterator=voteContent.getVoteQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		VoteQueContent voteQueContent=(VoteQueContent)contentIterator.next();
    		if (voteQueContent != null)
    		{
    			//int displayOrder=voteQueContent.getDisplayOrder().intValue();
    		    int displayOrder=voteQueContent.getDisplayOrder();
        		if (displayOrder != 0)
        		{
        			/* add the question to the questions Map in the displayOrder*/
        			mapQuestionsContent.put(new Integer(displayOrder).toString(),voteQueContent.getQuestion());
        		}
        		
        		/* prepare the first question's candidate answers for presentation*/ 
        		if (displayOrder == 1)
        		{
        		    /*
        			logger.debug("first question... ");
        			Long uid=voteQueContent.getUid();
        			logger.debug("uid : " + uid);
        			List listVoteOptions=voteService.findVoteOptionsContentByQueId(uid);
        			logger.debug("listVoteOptions : " + listVoteOptions);
        			Map mapOptionsContent=VoteUtils.generateOptionsMap(listVoteOptions);
        			request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
        			logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
        			*/
        		}
    		}
    	}
    	return mapQuestionsContent;
    }

    
    public static boolean doesUserExists(HttpServletRequest request)
	{
		IVoteService voteService =VoteUtils.getToolService(request);
	    Long queUsrId=VoteUtils.getUserId();
		VoteQueUsr voteQueUsr=voteService.retrieveVoteQueUsr(queUsrId);
		
		if (voteQueUsr != null)
			return true;
		
		return false;
	}


    public static void createUser(HttpServletRequest request)
	{
		IVoteService voteService =VoteUtils.getToolService(request);
	    Long queUsrId=VoteUtils.getUserId();
		String username=VoteUtils.getUserName();
		String fullname=VoteUtils.getUserFullName();
		Long toolSessionId=(Long) request.getSession().getAttribute(TOOL_SESSION_ID);
		
		VoteSession voteSession=voteService.retrieveVoteSession(toolSessionId);
		VoteQueUsr voteQueUsr= new VoteQueUsr(queUsrId, 
										username, 
										fullname,  
										voteSession, 
										new TreeSet());		
		voteService.createVoteQueUsr(voteQueUsr);
		logger.debug("created voteQueUsr in the db: " + voteQueUsr);
	}

    
    public static VoteQueUsr getUser(HttpServletRequest request)
	{
		IVoteService voteService =VoteUtils.getToolService(request);
	    Long queUsrId=VoteUtils.getUserId();
		VoteQueUsr voteQueUsr=voteService.retrieveVoteQueUsr(queUsrId);
		return voteQueUsr;
	}
    
    
    public static void createAttempt(HttpServletRequest request, VoteQueUsr voteQueUsr, Map mapGeneralCheckedOptionsContent, String userEntry, int nominationCount, boolean singleUserEntry)
	{
        logger.debug("doing createAttempt: " + mapGeneralCheckedOptionsContent);
        logger.debug("nominationCount: " + nominationCount);
        logger.debug("userEntry: " + userEntry);
        logger.debug("singleUserEntry: " + singleUserEntry);
        
		IVoteService voteService =VoteUtils.getToolService(request);
		Date attempTime=VoteUtils.getGMTDateTime();
		String timeZone= VoteUtils.getCurrentTimeZone();
		logger.debug("timeZone: " + timeZone);
		
		Long toolContentUID= (Long) request.getSession().getAttribute(TOOL_CONTENT_UID);
		logger.debug("toolContentUID: " + toolContentUID);
		 	
		if (toolContentUID != null)
		{
			Iterator itCheckedMap = mapGeneralCheckedOptionsContent.entrySet().iterator();
	        while (itCheckedMap.hasNext()) 
	        {
	        	Map.Entry checkedPairs = (Map.Entry)itCheckedMap.next();
	            Long questionDisplayOrder=new Long(checkedPairs.getKey().toString());
	            
	            logger.debug("questionDisplayOrder: " + questionDisplayOrder);
	            
	            VoteQueContent voteQueContent=voteService.getQuestionContentByDisplayOrder(questionDisplayOrder, toolContentUID);
	            logger.debug("voteQueContent: " + voteQueContent);
	            if (voteQueContent != null)
	            {
	                createIndividualOptions(request, voteQueContent, voteQueUsr, attempTime, timeZone, userEntry, nominationCount, false);    
	            }
	            else if ((voteQueContent == null) && (questionDisplayOrder.toString().equals("101")))
	            {
	                logger.debug("creating user entry record");
	                VoteQueContent localVoteQueContent=voteService.getToolDefaultQuestionContent(1);
	                logger.debug("localVoteQueContent: " + localVoteQueContent);
	                createIndividualOptions(request, localVoteQueContent, voteQueUsr, attempTime, timeZone, userEntry, nominationCount, true);    
	            }
	            
	        }			
		}
	 }

    public static void createIndividualOptions(HttpServletRequest request, VoteQueContent voteQueContent, VoteQueUsr voteQueUsr, Date attempTime, String timeZone, String userEntry, int nominationCount, boolean singleUserEntry)
    {
        logger.debug("doing createIndividualOptions");
        logger.debug("nominationCount: " + nominationCount);
        logger.debug("userEntry: " + userEntry);
        logger.debug("singleUserEntry: " + singleUserEntry);

    	IVoteService voteService =VoteUtils.getToolService(request);
    	
    	logger.debug("voteQueContent: " + voteQueContent);
    	logger.debug("user " + voteQueUsr.getQueUsrId());
    	logger.debug("voteQueContent.getVoteContentId() " +voteQueContent.getVoteContentId());
    	
    	if (voteQueContent != null)
    	{
    	    VoteUsrAttempt existingVoteUsrAttempt=voteService.getAttemptsForUserAndQuestionContent(voteQueUsr.getQueUsrId(),voteQueContent.getVoteContentId());
    	    logger.debug("existingVoteUsrAttempt: " + existingVoteUsrAttempt);
    	    
    	    if (existingVoteUsrAttempt != null)
    	    {
    	        logger.debug("update existingVoteUsrAttempt: " + existingVoteUsrAttempt);
    	        existingVoteUsrAttempt.setNominationCount(nominationCount);
    	        existingVoteUsrAttempt.setUserEntry(userEntry);
    	        existingVoteUsrAttempt.setAttemptTime(attempTime);
    	        existingVoteUsrAttempt.setTimeZone(timeZone);
    	        voteService.updateVoteUsrAttempt(existingVoteUsrAttempt);
    	        logger.debug("done updating existingVoteUsrAttempt: " + existingVoteUsrAttempt);
    	    }
    	    else
    	    {
    	        logger.debug("create new attempt");
        	    VoteUsrAttempt voteUsrAttempt=new VoteUsrAttempt(attempTime, timeZone, voteQueContent, voteQueUsr, userEntry, nominationCount , singleUserEntry);
        	    logger.debug("voteUsrAttempt: " + voteUsrAttempt);
            	voteService.createVoteUsrAttempt(voteUsrAttempt);
            	logger.debug("created voteUsrAttempt in the db :" + voteUsrAttempt);    
    	    }
    	}
    }

    
    public static void readParameters(HttpServletRequest request, VoteLearningForm voteLearningForm)
    {
    	String optionCheckBoxSelected=request.getParameter("optionCheckBoxSelected");
    	logger.debug("parameter optionCheckBoxSelected: " + optionCheckBoxSelected);
    	if ((optionCheckBoxSelected != null) && optionCheckBoxSelected.equals("1"))
    	{
    		logger.debug("parameter optionCheckBoxSelected is selected " + optionCheckBoxSelected);
    		voteLearningForm.setOptionCheckBoxSelected("1");
    	}
    	
    	String questionIndex=request.getParameter("questionIndex");
    	logger.debug("parameter questionIndex: " + questionIndex);
    	if ((questionIndex != null))
    	{
    		logger.debug("parameter questionIndex is selected " + questionIndex);
    		voteLearningForm.setQuestionIndex(questionIndex);
    	}
    	
    	String optionIndex=request.getParameter("optionIndex");
    	logger.debug("parameter optionIndex: " + optionIndex);
    	if (optionIndex != null)
    	{
    		logger.debug("parameter optionIndex is selected " + optionIndex);
    		voteLearningForm.setOptionIndex(optionIndex);
    	}
    	
    	String optionValue=request.getParameter("optionValue");
    	logger.debug("parameter optionValue: " + optionValue);
    	if (optionValue != null)
    	{
    		voteLearningForm.setOptionValue(optionValue);
    	}
    	
    	String checked=request.getParameter("checked");
    	logger.debug("parameter checked: " + checked);
    	if (checked != null)
    	{
    		logger.debug("parameter checked is selected " + checked);
    		voteLearningForm.setChecked(checked);
    	}
    }

    
    public static  void selectOptionsCheckBox(HttpServletRequest request,VoteLearningForm voteLearningForm, String questionIndex)
    {
    	logger.debug("requested optionCheckBoxSelected...");
    	logger.debug("questionIndex: " + voteLearningForm.getQuestionIndex());
    	logger.debug("optionIndex: " + voteLearningForm.getOptionIndex());
    	logger.debug("optionValue: " + voteLearningForm.getOptionValue());
    	logger.debug("checked: " + voteLearningForm.getChecked());
    	
    	Map mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	
    	if (mapGeneralCheckedOptionsContent.size() == 0)
    	{
    		logger.debug("mapGeneralCheckedOptionsContent size is 0");
    		Map mapLeanerCheckedOptionsContent= new TreeMap(new VoteComparator());
    		
    		if (voteLearningForm.getChecked().equals("true"))
    			mapLeanerCheckedOptionsContent.put(voteLearningForm.getQuestionIndex(), voteLearningForm.getOptionValue());
    		else
    			mapLeanerCheckedOptionsContent.remove(voteLearningForm.getQuestionIndex());
    		
			request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapLeanerCheckedOptionsContent);
    	}
    	else
    	{
    		Map mapCurrentOptions= mapGeneralCheckedOptionsContent;
    		
    		logger.debug("mapCurrentOptions: " + mapCurrentOptions);
    		if (mapCurrentOptions != null)
    		{
    			if (voteLearningForm.getChecked().equals("true"))
    				mapCurrentOptions.put(voteLearningForm.getQuestionIndex(), voteLearningForm.getOptionValue());
    			else
    				mapCurrentOptions.remove(voteLearningForm.getQuestionIndex());
    			
    			logger.debug("updated mapCurrentOptions: " + mapCurrentOptions);
    			
    			request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapCurrentOptions);	
    		}
    		else
    		{
    			logger.debug("no options for this questions has been selected yet");
    			Map mapLeanerCheckedOptionsContent= new TreeMap(new VoteComparator());
    			        			
    			if (voteLearningForm.getChecked().equals("true"))
    				mapLeanerCheckedOptionsContent.put(voteLearningForm.getQuestionIndex(), voteLearningForm.getOptionValue());
    			else
    				mapLeanerCheckedOptionsContent.remove(voteLearningForm.getOptionIndex());        			
    			
    			request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapLeanerCheckedOptionsContent);
    		}
    	}
    	
    	mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    }

    
}

