/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.Learner;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

public class ExportServlet  extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;
	private static Logger logger = Logger.getLogger(ExportServlet.class);
	private final String FILENAME = "sbmt_main.html";
	
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
		if (StringUtils.equals(mode,ToolAccessMode.LEARNER.toString())){
			learner(request,response,directoryName,cookies);
		}else if (StringUtils.equals(mode,ToolAccessMode.TEACHER.toString())){
			teacher(request,response,directoryName,cookies);
		}
		
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		writeResponseToFile(basePath+"/export/exportportfolio.jsp",directoryName,FILENAME,cookies);
		
		return FILENAME;
	}
    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
        
        ISubmitFilesService sbmtService = SubmitFilesServiceProxy.getSubmitFilesService(getServletContext());
        
        if (userID == null || toolSessionID == null)
        {
            String error = "Tool session Id or user Id is null. Unable to continue";
            logger.error(error);
            throw new SubmitFilesException(error);
        }
        
        Learner learner = sbmtService.getLearner(toolSessionID,userID);
        
        if (learner == null)
        {
            String error="The user with user id " + userID + " does not exist in this session or session may not exist.";
            logger.error(error);
            throw new SubmitFilesException(error);
        }
        
       SubmitFilesContent content = sbmtService.getSubmitFilesContent(toolSessionID);
        
        if (content == null)
        {
            String error="The content for this activity has not been defined yet.";
            logger.error(error);
            throw new SubmitFilesException(error);
        }
       
    }
    
    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
    	ISubmitFilesService sbmtService = SubmitFilesServiceProxy.getSubmitFilesService(getServletContext());
       
        //check if toolContentId exists in db or not
        if (toolContentID==null)
        {
            String error="Tool Content Id is missing. Unable to continue";
            logger.error(error);
            throw new SubmitFilesException(error);
        }

        SubmitFilesContent content = sbmtService.getSubmitFilesContent(toolContentID);
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new SubmitFilesException(error);
        }
		//return FileDetailsDTO list according to the given sessionID
        Set sessionList = content.getToolSession();
        Iterator iter = sessionList.iterator();
        Map userFilesMap = new HashMap();
        while(iter.hasNext()){
        	SubmitFilesSession session = (SubmitFilesSession) iter.next();
        	userFilesMap.putAll(sbmtService.getFilesUploadedBySession(session.getSessionID()));
        }
		request.getSession().setAttribute("report",userFilesMap);
		

  
    }

}
