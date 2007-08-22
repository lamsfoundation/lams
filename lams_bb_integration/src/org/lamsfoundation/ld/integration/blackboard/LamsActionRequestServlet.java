/****************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.ld.integration.blackboard;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.lamsfoundation.ld.integration.Constants;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.Context;
import blackboard.platform.session.BbSession;
import blackboard.platform.session.BbSessionManagerService;


/**
 *  Handles requests to the LAMS server
 *  
 *  @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsActionRequestServlet extends HttpServlet {
          

	private static final long serialVersionUID = 1L;

	/**
     * The doGet method of the servlet. <br>
     *
     * This method is called by a when a GET request is performed
     * There are three types of requests for LAMS AUTHOR, MONITOR and LEARNER
     * Basically it creates the redirect URL strings and sends the redirect
     * 
     * @param request the request sent by the client to the server
     * @param response the response sent by the server to the client
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
                
        //get request parameters
        String p_method = request.getParameter(Constants.PARAM_METHOD);
        
        //String p_learningSessionId = request.getParameter(Constants.PARAM_LEARNING_SESSION_ID);
        //String p_learningDesignId = request.getParameter(Constants.PARAM_LEARNING_DESIGN_ID);
        String p_courseId = request.getParameter(Constants.PARAM_COURSE_ID);
        
        //validate method parameter and associated parameters
        if(p_method == null)
        {
            throw new ServletException("requred parameters missing");
        }
     

        ContextManager ctxMgr = null;
        Context ctx = null;
        
        try{        
            // get Blackboard context                
            ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
            ctx = ctxMgr.setContext(request);
            
            
            // get the session object to obtain the current user and course object
	        BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();
	        BbSession bbSession = sessionService.getSession( request );

	        String redirect = LamsSecurityUtil.generateRequestURL(ctx, p_method);

	        if(p_courseId!=null)
	        {
	            redirect += '&' + Constants.PARAM_COURSE_ID + '=' + p_courseId;
	            //redirect1 += '&' + Constants.PARAM_LEARNING_SESSION_ID + '=' + p_learningSessionId;
	        }
	        
	        response.sendRedirect(response.encodeRedirectURL(redirect));
	        
	            
        } catch (Exception e){
            
            throw new ServletException(e.getMessage(), e);
        } 
        finally{
            //make sure context is released
	        if (ctxMgr != null)
	            ctxMgr.releaseContext();
        }
    }
}
