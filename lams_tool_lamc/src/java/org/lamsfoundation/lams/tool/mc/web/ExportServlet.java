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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
   
	/**
	 * learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	 * 
	 * generates report for learner mode
	 * 
	 * @param request
	 * @param response
	 * @param directoryName
	 * @param cookies
	 */
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
        
        // If the learner hasn't selected any options yet, then they won't exist in the session.
        // Yet we might be asked for their page, as the activity has been commenced. So need to do a "blank" page in that case
        McQueUsr learner = mcService.getMcUserBySession(userID,mcSession.getUid());
        logger.debug("learner: " + learner);
        
        McContent content=mcSession.getMcContent();
        logger.debug("content: " + content);
        logger.debug("content id: " + content.getMcContentId());
        
        if (content == null)
        {
            String error="The content for this activity has not been defined yet.";
            logger.error(error);
            throw new McApplicationException(error);
        }

	    request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "learner");

	    if ( learner != null ) {
	        McMonitoringAction mcMonitoringAction= new McMonitoringAction();
	        List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionDataForExportLearner(request, content, mcService, mcSession, learner );
		    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
		    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
		    
		    request.getSession().setAttribute(LEARNER_MARK,learner.getLastAttemptTotalMark());
		    request.getSession().setAttribute(LEARNER_NAME,learner.getFullname() );

		    request.getSession().setAttribute(PASSMARK,content.getPassMark().toString());
		    mcMonitoringAction.prepareReflectionData(request, content, mcService, userID.toString(), true);
	    }
	    
    	logger.debug("ending learner mode: ");
    }

	
	 /**
	  * teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	  * 
	  * generates report for teacher mode
	  * @param request
	  * @param response
	  * @param directoryName
	  * @param cookies
	  */
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
