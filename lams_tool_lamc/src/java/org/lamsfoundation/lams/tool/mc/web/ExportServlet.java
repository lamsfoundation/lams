/****************************************************************
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
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.mc.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
/**
 * <p> Enables exporting portfolio for teacher and learner modes. </p> 
 * 
 * @author Ozgur Demirtas
 */

public class ExportServlet  extends AbstractExportPortfolioServlet implements McAppConstants{
    static Logger logger = Logger.getLogger(ExportServlet.class.getName());
	private static final long serialVersionUID = -17790L;
	private final String FILENAME = "mcq_main.html";
	
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
	    logger.debug("dispathcing doExport");
	    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	    logger.debug("basePath:" + basePath);

		if (StringUtils.equals(mode,ToolAccessMode.LEARNER.toString())){
		    learner(request,response,directoryName,cookies);
		}else if (StringUtils.equals(mode,ToolAccessMode.TEACHER.toString())){
			teacher(request,response,directoryName,cookies);
		}

		writeResponseToFile(basePath+"/export/exportportfolio.jsp",directoryName,FILENAME,cookies);
		
		return FILENAME;
	}
    
	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
	    logger.debug("starting learner mode...");

	    IMcService mcService = McServiceProxy.getMcService(getServletContext());
    	logger.debug("mcService:" + mcService);
        
    	logger.debug("userID:" + userID);
    	logger.debug("toolSessionID:" + toolSessionID);
    	
        if (userID == null || toolSessionID == null)
        {
            String error = "Tool session Id or user Id is null. Unable to continue";
            logger.error(error);
            throw new McApplicationException(error);
        }
        
        McSession mcSession=mcService.retrieveMcSession(toolSessionID);
        
        McQueUsr learner = mcService.getMcUserBySession(userID,mcSession.getUid());
        logger.debug("learner: " + learner);
        
        if (learner == null)
        {
            String error="The user with user id " + userID + " does not exist in this session or session may not exist.";
            logger.error(error);
            throw new McApplicationException(error);
        }
        
        McContent content=mcSession.getMcContent();
        logger.debug("content: " + content);
        logger.debug("content id: " + content.getMcContentId());
        
        if (content == null)
        {
            String error="The content for this activity has not been defined yet.";
            logger.error(error);
            throw new McApplicationException(error);
        }
        
        
        McMonitoringAction mcMonitoringAction= new McMonitoringAction();
        List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionDataForExportLearner(request, content, mcService, mcSession, learner );
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    

	    String intTotalMark=viewAnswers(request, content, learner, mcSession,  mcService);
	    logger.debug("intTotalMark: " + intTotalMark);
	    
	    request.getSession().setAttribute(LEARNER_MARK,intTotalMark);
	    request.getSession().setAttribute(LEARNER_NAME,learner.getFullname() );
	    request.getSession().setAttribute(PASSMARK,content.getPassMark().toString());
	    
	    request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "learner");
	    mcMonitoringAction.prepareReflectionData(request, content, mcService, userID.toString(), true);
    	logger.debug("ending learner mode: ");
    }

	
	
	 public String viewAnswers(HttpServletRequest request, McContent content, McQueUsr mcQueUsr, McSession mcSession, IMcService mcService)
	 {
			logger.debug("starting viewAnswers...");
			int  intTotalMark=0;
			
		    McUsrAttempt mcUsrAttempt = mcService.getAttemptWithLastAttemptOrderForUserInSession(mcQueUsr.getUid(), mcSession.getUid());
	    	logger.debug("mcUsrAttempt with highest attempt order: " + mcUsrAttempt);
			
			int totalQuestionCount=content.getMcQueContents().size();
			logger.debug("totalQuestionCount: " + totalQuestionCount);
			
			Long toolContentUID=content.getUid();
			logger.debug("toolContentUID: " + toolContentUID);
			logger.debug("mcQueUsr: " + mcQueUsr);
			
			Long queUsrId=mcQueUsr.getUid();
			logger.debug("queUsrId: " + queUsrId);
			
			Map mapResponses= new TreeMap(new McComparator());
			Map mapQueAttempts= new TreeMap(new McComparator());
			Map mapQueCorrectAttempts= new TreeMap(new McComparator());
			Map mapQueIncorrectAttempts= new TreeMap(new McComparator());

			for (int i=1; i<=  new Integer(totalQuestionCount).intValue(); i++)
			{
				logger.debug("doing question with display order: " + i);
				McQueContent mcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(i), toolContentUID);
				logger.debug("mcQueContent uid: " + mcQueContent.getUid());
				
				int intCurrentMark=0;
				

				/**calculating learner's mark*/
		    	if (mcUsrAttempt != null)
		    	{
		        	String highestAttemptOrder=mcUsrAttempt.getAttemptOrder().toString();
		        	logger.debug("highestAttemptOrder: " + highestAttemptOrder);

					List listUserAttempts=mcService.getAttemptsOnHighestAttemptOrder(mcQueUsr.getUid(), mcQueContent.getUid(), mcSession.getUid(), new Integer(highestAttemptOrder));
					logger.debug("listUserAttempts: " + listUserAttempts);
					
					Iterator itAttempts=listUserAttempts.iterator();
					
					if (!mcSession.getMcContent().isRetries())
					{
					    logger.debug("retries is OFF.");
					    boolean isAttemptCorrect=false;
					    McUsrAttempt mcUsrAttemptUser=null;
					    
		    			while (itAttempts.hasNext())
		    			{
		    	    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
		    	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
		    	    		mcUsrAttemptUser=mcUsrAttempt;

		    	    		if (mcUsrAttempt != null)
		    	    		{
		    	    		    if (mcUsrAttempt.isAttemptCorrect())
		    	    		    {
		    	    		        isAttemptCorrect=true;
		    	    		    }
		    	    		}
		    			}
		    			logger.debug("final isAttemptCorrect: " + isAttemptCorrect);
		    			logger.debug("mcUsrAttemptUser: " + mcUsrAttemptUser);
		    			
		    			String currentMark="";
						if (isAttemptCorrect)
						{
						    currentMark= mcUsrAttemptUser.getMcQueContent().getWeight().toString();
						}
						else
						{
						    currentMark= "0";
						}
						mapResponses.put(new Integer(i).toString(), currentMark);
						intCurrentMark=new Integer(currentMark).intValue();
						logger.debug("intCurrentMark: " + intCurrentMark);
		    			
					}
					else
					{
					    logger.debug("retries is ON. User had to PASS. Print the final attempt's data");
					    boolean isAttemptCorrect=false;
					    McUsrAttempt mcUsrAttemptGeneral=null;
						while (itAttempts.hasNext())
						{
				    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
				    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
				    		mcUsrAttemptGeneral=mcUsrAttempt;

				    		if (mcUsrAttempt != null)
				    		{
		    		    		if (mcUsrAttempt.isFinished() && mcUsrAttempt.isPassed()) 
		    		    		{
		    		    		    logger.debug("this is a individual question attempt that is finished and passed: " + mcUsrAttempt);
		    		    		    isAttemptCorrect=mcService.getUserAttemptCorrectForQuestionContentAndSessionUid(mcQueUsr.getUid(), 
		    		    		            mcQueContent.getUid(), mcSession.getUid(), new Integer(highestAttemptOrder));
		    		    		    logger.debug("isAttemptCorrect: " + isAttemptCorrect);
		    		    		    break;	
		    		    		}
				    		}
						}

						logger.debug("final isAttemptCorrect: " + isAttemptCorrect);

						String currentMark="";
						if (isAttemptCorrect)
						{
						    currentMark= mcUsrAttempt.getMcQueContent().getWeight().toString();
						}
						else
						{
						    currentMark= "0";
						}
						mapResponses.put(new Integer(i).toString(), currentMark);
						intCurrentMark=new Integer(currentMark).intValue();
						logger.debug("intCurrentMark: " + intCurrentMark);
					}
		    	}
		    	else
		    	{
		    	    intCurrentMark=0;
		    	    mapResponses.put(new Integer(i).toString(), "0");
		    	}
				
				intTotalMark=intTotalMark + intCurrentMark;
				logger.debug("intTotalMark: " + intTotalMark);

				Map mapAttemptOrderCorrectAttempts= new TreeMap(new McComparator());
				Map mapAttemptOrderIncorrectAttempts= new TreeMap(new McComparator());
				Map mapAttemptOrderAttempts= new TreeMap(new McComparator());
				for (int j=1; j <= MAX_ATTEMPT_HISTORY ; j++ )
	    		{
					logger.debug("getting list for queUsrId: " + queUsrId);
	    			List attemptsByAttemptOrder=mcService.getAttemptByAttemptOrder(queUsrId, mcQueContent.getUid(), new Integer(j));
		    		logger.debug("attemptsByAttemptOrder: " + j + " is: " + attemptsByAttemptOrder);
		    	
		    		Map mapAttempts= new TreeMap(new McComparator());
		    		Map mapAttemptsIncorrect= new TreeMap(new McComparator());
		    		Map mapAttemptsCorrect= new TreeMap(new McComparator());

		    		Iterator attemptIterator=attemptsByAttemptOrder.iterator();
		    		Long mapIndex=new Long(1);
	    	    	while (attemptIterator.hasNext())
	    	    	{
	    	    		McUsrAttempt localMcUsrAttempt=(McUsrAttempt)attemptIterator.next();
	    	    		logger.debug("localMcUsrAttempt: " + localMcUsrAttempt);
	    	    		
        	    		mapAttempts.put(mapIndex.toString(), localMcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
    	    			logger.debug("added attempt with order: " + localMcUsrAttempt.getAttemptOrder() + " , option text is: " + localMcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
    	    			mapIndex=new Long(mapIndex.longValue()+1);
	    	    	}    	    		

	    	    	logger.debug("final mapAttempts is: " + mapAttempts);
	    	    	if (mapAttempts.size() > 0)
	    	    	{
	    	    		mapAttemptOrderAttempts.put(new Integer(j).toString(), mapAttempts);	
	    	    	}
	    	    	if (mapAttemptsCorrect.size() > 0)
	    	    	{
	    	    		mapAttemptOrderCorrectAttempts.put(new Integer(j).toString(), mapAttemptsCorrect);	
	    	    	}
	    	    	if (mapAttemptsIncorrect.size() > 0)
	    	    	{
	    	    		mapAttemptOrderIncorrectAttempts.put(new Integer(j).toString(), mapAttemptsIncorrect);	
	    	    	}
	    	    	
	    		}
				
				logger.debug("final mapAttemptOrderAttempts is: " + mapAttemptOrderAttempts);
				if (mapAttemptOrderAttempts.size() > 0)
		    	{
					mapQueAttempts.put(new Integer(i).toString(), mapAttemptOrderAttempts);	
		    	}
				if (mapAttemptOrderCorrectAttempts.size() > 0)
		    	{
					mapQueCorrectAttempts.put(new Integer(i).toString(), mapAttemptOrderCorrectAttempts);	
		    	}    			
				if (mapAttemptOrderIncorrectAttempts.size() > 0)
		    	{
	    			mapQueIncorrectAttempts.put(new Integer(i).toString(), mapAttemptOrderIncorrectAttempts);	
		    	}    			
				
			}
			request.getSession().setAttribute(MAP_QUE_ATTEMPTS, mapQueAttempts);
			logger.debug("final mapQueAttempts is: " + mapQueAttempts);
			request.getSession().setAttribute(MAP_QUE_CORRECT_ATTEMPTS, mapQueCorrectAttempts);
			request.getSession().setAttribute(MAP_QUE_INCORRECT_ATTEMPTS, mapQueIncorrectAttempts);
			request.getSession().setAttribute(MAP_RESPONSES, mapResponses);
			
			logger.debug("final MAP_RESPONSES is: " + mapResponses);
			logger.debug("final intTotalMark is: " + intTotalMark);
			return new Integer(intTotalMark).toString();
	}
	 

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
        logger.debug("starting teacher mode...");
        
        IMcService mcService = McServiceProxy.getMcService(getServletContext());
       
        if (toolContentID==null)
        {
            String error="Tool Content Id is missing. Unable to continue";
            logger.error(error);
            throw new McApplicationException(error);
        }

        McContent content=mcService.retrieveMc(toolContentID);
        logger.debug("content: " + content);
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new McApplicationException(error);
        }
		
        logger.debug("starting teacher mode: ");
        McMonitoringAction mcMonitoringAction= new McMonitoringAction();

        List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, content, mcService);
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    
	    
	    List listMonitoredMarksContainerDTO=MonitoringUtil.buildGroupsMarkData(request, content, mcService);
	    request.getSession().setAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO, listMonitoredMarksContainerDTO);
	    logger.debug("LIST_MONITORED_MARKS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO));

	    request.getSession().setAttribute(PASSMARK,content.getPassMark().toString());
	    request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "teacher");
	    
	    mcMonitoringAction.prepareReflectionData(request, content, mcService, null, true);
        logger.debug("ending teacher mode: ");
    }

}
