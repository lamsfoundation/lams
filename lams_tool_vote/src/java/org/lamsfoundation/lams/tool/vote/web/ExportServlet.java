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


package org.lamsfoundation.lams.tool.vote.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
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
	private static final long serialVersionUID = -1529093489007108983L;
	private final String FILENAME = "vote_main.html";
	
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
	    logger.debug("dispathcing doExport");
	    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

		if (StringUtils.equals(mode,ToolAccessMode.LEARNER.toString())){
		    learner(request,response,directoryName,cookies);
		}else if (StringUtils.equals(mode,ToolAccessMode.TEACHER.toString())){
			teacher(request,response,directoryName,cookies);
		}

		logger.debug("writing out chart to directoryName: " + directoryName);
		writeOutChart(request, "pie",  directoryName);
		writeOutChart(request, "bar",  directoryName);
		logger.debug("basePath: " + basePath);
		writeResponseToFile(basePath+"/export/exportportfolio.jsp",directoryName,FILENAME,cookies);
		
		return FILENAME;
	}
    
	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
	    ExportPortfolioDTO exportPortfolioDTO= new ExportPortfolioDTO();
	    
	    logger.debug("starting learner mode...");
	    exportPortfolioDTO.setPortfolioExportMode("learner");
        
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

        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
        logger.debug("calling learning mode toolSessionID:" + toolSessionID + " userID: " + userID );
        
    	VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
    	voteMonitoringAction.refreshSummaryData(request, content, voteService, true, true, 
    	        toolSessionID.toString(), userID.toString() , true, null, voteGeneralMonitoringDTO, exportPortfolioDTO);
    	logger.debug("post refreshSummaryData, exportPortfolioDTO now: " + exportPortfolioDTO);
    	
    	
    	MonitoringUtil.prepareChartDataForExportTeacher(request, voteService, null, content.getVoteContentId(), 
    	        voteSession.getUid(), exportPortfolioDTO);
    	    	
    	logger.debug("final exportPortfolioDTO: " + exportPortfolioDTO);
    	logger.debug("final exportPortfolioDTO userExceptionNoToolSessions: " + exportPortfolioDTO.getUserExceptionNoToolSessions() );
    	request.getSession().setAttribute(EXPORT_PORTFOLIO_DTO, exportPortfolioDTO);
    	
    	voteMonitoringAction.prepareReflectionData(request, content, voteService, userID.toString(),true);

    	logger.debug("ending learner mode: ");
    }
    
    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
        logger.debug("starting teacher mode...");
	    ExportPortfolioDTO exportPortfolioDTO= new ExportPortfolioDTO();
        exportPortfolioDTO.setPortfolioExportMode("teacher");
        
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
		
        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
        VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
        
        logger.debug("starting refreshSummaryData.");
        voteMonitoringAction.refreshSummaryData(request, content, voteService, true, false, null, null, false, null, voteGeneralMonitoringDTO, exportPortfolioDTO);
        logger.debug("post refreshSummaryData, exportPortfolioDTO now: " + exportPortfolioDTO);
        
        logger.debug("teacher uses content id: " + content.getVoteContentId());
    	MonitoringUtil.prepareChartDataForExportTeacher(request, voteService, null, content.getVoteContentId(), null, exportPortfolioDTO);
    	logger.debug("post prepareChartDataForExportTeacher");

        logger.debug("final exportPortfolioDTO: " + exportPortfolioDTO);
        logger.debug("final exportPortfolioDTO userExceptionNoToolSessions: " + exportPortfolioDTO.getUserExceptionNoToolSessions() );
        request.getSession().setAttribute(EXPORT_PORTFOLIO_DTO, exportPortfolioDTO);
        
        voteMonitoringAction.prepareReflectionData(request, content, voteService, null,true);
        logger.debug("ending teacher mode: ");
    }
    
    /**
     * creates JFreeCharts for offline visibility 
     * @param request
     * @param chartType
     * @param directoryName
     */
    public void writeOutChart(HttpServletRequest request, String chartType, String directoryName) {
        logger.debug("File.separator: " + File.separator) ;
        String fileName=chartType + ".png";
        logger.debug("output image fileName: " + fileName) ;
        logger.debug("full folder name:" + directoryName + File.separator + fileName);
        
        try{
            OutputStream out = new FileOutputStream(directoryName +  File.separator + fileName);
            VoteChartGenerator  voteChartGenerator= new VoteChartGenerator();
            JFreeChart chart=null;
            if (chartType.equals("pie"))
            {
                chart = voteChartGenerator.createChart(request, "pie");    
            }
            else
            {
                chart = voteChartGenerator.createChart(request, "bar");
            }
            logger.debug("chart:" + chart);
            
            ChartUtilities.writeChartAsPNG(out, chart, 400, 300);
        }
        catch(FileNotFoundException e)
        {
            logger.debug("exception creating chart: " + e) ;
        }
        catch(IOException e)
        {
            logger.debug("exception creating chart: " + e) ;
        }
      }
}
