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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
  
package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.bb.integration.Constants;
import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;

import blackboard.base.InitializationException;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;

/**
 * Makes a call to LAMS server to get learning designs and returns it.
 */
public class LamsLearningDesignServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
	process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) {
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverId = LamsPluginUtil.getServerId();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null) {
	    throw new RuntimeException("lams.properties file could not be read. serverAddr:" + serverAddr + ", serverId:" + serverId);
	}

	// get request parameters
	String folderId = request.getParameter(Constants.PARAM_FOLDER_ID);

	//paging parameters of tablesorter - used in the LAMS Template Wizard
	boolean usePaging = false;
	String page = request.getParameter("page");
	String size = request.getParameter("size");
	if ( page != null && page.length()>0) {
	    usePaging = true;
	    if ( size == null || size.length()==0)
		size="10";
	}
	String sortName = request.getParameter("sortName");
	String sortDate = request.getParameter("sortDate");
	String search = request.getParameter("search");
	String type = request.getParameter("type");
	String username = request.getParameter("username"); // backup method to get user, when the Blackboard context does not have the user
	
	ContextManager ctxMgr = null;
	Context ctx = null;
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    ctx = ctxMgr.setContext(request);
	    
	    String courseId = ctx.getCourse().getCourseId();
	    String method = usePaging ? "getPagedHomeLearningDesignsJSON" : "getLearningDesignsJSON";
	    String learningDesigns = LamsSecurityUtil.getLearningDesigns(ctx, username, courseId, folderId, method, type, search, page, size, sortName, sortDate);
	    
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(learningDesigns);
	    
	} catch (InitializationException e) {
	    throw new RuntimeException(e);
	} catch (BbServiceException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    // make sure context is released
	    if (ctxMgr != null) {
		ctxMgr.releaseContext();
	    }
	}
    }
}

