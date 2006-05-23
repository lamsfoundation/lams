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


package org.lamsfoundation.lams.tool.vote.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
/**
 * <p> Enables exporting portfolio for teacher and learner modes. </p> 
 * 
 * @author Ozgur Demirtas
 */

public class ExportServlet  extends AbstractExportPortfolioServlet implements VoteAppConstants{
	static Logger logger = Logger.getLogger(ExportServlet.class.getName());
	private static final long serialVersionUID = -4529093489007108143L;
	private final String FILENAME = "vote_main.html";
	
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
	    logger.debug("dispathcing doExport");
	    request.getSession().setAttribute(IS_PORTFOLIO_EXPORT, new Boolean(true).toString());
	    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

		if (StringUtils.equals(mode,ToolAccessMode.LEARNER.toString())){
		    learner(request,response,directoryName,cookies);
		}else if (StringUtils.equals(mode,ToolAccessMode.TEACHER.toString())){
			teacher(request,response,directoryName,cookies);
		}
		
		logger.debug("basePath: " + basePath);
		writeResponseToFile(basePath+"/export/exportportfolio.jsp",directoryName,FILENAME,cookies);
		
		return FILENAME;
	}
    
	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
	    logger.debug("starting learner mode...");
	    request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "learner");
        
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());
    	logger.debug("voteService:" + voteService);
        
    	logger.debug("userID:" + userID);
    	logger.debug("toolSessionID:" + toolSessionID);
    	
        if (userID == null || toolSessionID == null)
        {
            String error = "Tool session Id or user Id is null. Unable to continue";
            logger.error(error);
            throw new VoteApplicationException(error);
        }
        
        VoteSession voteSession=voteService.retrieveVoteSession(toolSessionID);
        logger.debug("retrieving voteSession: " + voteSession);
        logger.debug("voteSession uid: " + voteSession.getUid());
        
        VoteQueUsr learner = voteService.getVoteUserBySession(userID,voteSession.getUid());
        logger.debug("learner: " + learner);
        
        if (learner == null)
        {
            String error="The user with user id " + userID + " does not exist in this session or session may not exist.";
            logger.error(error);
            throw new VoteApplicationException(error);
        }
        
        VoteContent content=voteSession.getVoteContent();
        logger.debug("content: " + content);
        logger.debug("content id: " + content.getVoteContentId());
        
        if (content == null)
        {
            String error="The content for this activity has not been defined yet.";
            logger.error(error);
            throw new VoteApplicationException(error);
        }

        logger.debug("calling learning mode toolSessionID:" + toolSessionID + " userID: " + userID );
    	VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
    	voteMonitoringAction.refreshSummaryData(request, content, voteService, true, true, toolSessionID.toString(), userID.toString() , true);
    	
    	MonitoringUtil.prepareChartDataForExportLearner(request, voteService, null, content.getVoteContentId(), voteSession.getUid());
    	logger.debug("post prepareChartDataForExport");
    	
    	logger.debug("ending learner mode: ");
    }
    
    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
        logger.debug("starting teacher mode...");
        request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "teacher");
        
        IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());
        logger.debug("voteService:" + voteService);
        logger.debug("toolContentID:" + toolContentID);
       
        if (toolContentID==null)
        {
            String error="Tool Content Id is missing. Unable to continue";
            logger.error(error);
            throw new VoteApplicationException(error);
        }

        VoteContent content=voteService.retrieveVote(toolContentID);
        logger.debug("content: " + content);
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new VoteApplicationException(error);
        }
		
        VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
        logger.debug("starting refreshSummaryData.");
        voteMonitoringAction.refreshSummaryData(request, content, voteService, true, false, null, null, false);
        
        logger.debug("teacher uses content id: " + content.getVoteContentId());
    	MonitoringUtil.prepareChartDataForExportTeacher(request, voteService, null, content.getVoteContentId(), null);
    	logger.debug("post prepareChartDataForExportTeacher");
        
        logger.debug("ending teacher mode: ");
    }
}
