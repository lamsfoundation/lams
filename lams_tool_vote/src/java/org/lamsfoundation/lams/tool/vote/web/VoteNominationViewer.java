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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;

/**
 * <p> presents students voted for a particular nomination </p>
 * 
 * @author Ozgur Demirtas
 *  
 */
public class VoteNominationViewer extends HttpServlet implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteChartGenerator.class.getName());
    
    public VoteNominationViewer(){
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        logger.debug("dispatching doGet for VoteChartGenerator...");

        PrintWriter out= new PrintWriter(response.getWriter());
        
        try{
            String questionUid=request.getParameter("questionUid");
            String sessionUid=request.getParameter("sessionUid");
            
            logger.debug("questionUid: " + questionUid);
            logger.debug("sessionUid: " + sessionUid);
            
        	IVoteService voteService=null;
    	    voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
    		logger.debug("voteService: " + voteService);

    		List userNames=voteService.getStandardAttemptUsersForQuestionContentAndSessionUid(new Long(questionUid), new Long(sessionUid));
    		logger.debug("userNames: " + userNames);
    		
            response.setContentType("text/html");
            out.println("<html:html locale='true'>");
            out.println("<HEAD>");
	            out.println("<TITLE>"); out.println("Learners Voted");out.println("</TITLE>");
	            out.println("<lams:css/>");

	       	out.println("<link href='http://localhost:8080/lams/css/aqua.css' rel='stylesheet' type='text/css'> " +
	       			"<link href='http://localhost:8080/lams/css/default.css' rel='stylesheet' type='text/css'> " +
	       			" <script type='text/javascript' src='http://localhost:8080/lams/includes/javascript/common.js'></script>");	            
	            
            out.println("</HEAD>");
            out.println("<BODY>");
	            out.println("<table width='80%' cellspacing='8' align='CENTER' class='forms'>");
	            out.println("<tr> <th NOWRAP>  Students Voted </th> </tr>");            
				
	            Iterator userIterator=userNames.iterator();
	        	while (userIterator.hasNext())
	        	{
	        		String userName=(String)userIterator.next();
					out.println("<tr>");  
		            out.println("<td NOWRAP align=center valign=top>"); 
		            out.println(userName);						
		            out.println("</td>");
					out.println("</tr>");
	        	}
	            out.println("</table>");
            
            out.println("</BODY>");
            out.println("</html:html>");
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            logger.error("error occurred: " + e.toString());
        }
        finally
        {
            out.close();
        }
    }
        
}
