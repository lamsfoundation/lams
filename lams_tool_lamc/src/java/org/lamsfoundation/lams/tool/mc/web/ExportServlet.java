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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
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
	private static final long serialVersionUID = -4529093489007108143L;
	private final String FILENAME = "mc_main.html";
	
	
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

		writeResponseToFile(basePath+"/export/exportportfolio.jsp",directoryName,FILENAME,cookies);
		
		return FILENAME;
	}
    
	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
	    logger.debug("starting learner mode...");
	    request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "learner");
        
    	IMcService mcService = McServiceProxy.getMcService(getServletContext());
        
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

        logger.debug("calling learning mode toolSessionID:" + toolSessionID + " userID: " + userID );
    	McMonitoringAction mcMonitoringAction= new McMonitoringAction();
    	//mcMonitoringAction.refreshSummaryData(request, content, mcService, true, true, toolSessionID.toString(), userID.toString() , true);
    	
    	logger.debug("ending learner mode: ");
    }
    
    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
        logger.debug("starting teacher mode...");
        request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "teacher");
        
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
		
        McMonitoringAction mcMonitoringAction= new McMonitoringAction();
        logger.debug("starting refreshSummaryData.");
        //mcMonitoringAction.refreshSummaryData(request, content, mcService, true, false, null, null, false);
        
        logger.debug("ending teacher mode: ");
    }

}
