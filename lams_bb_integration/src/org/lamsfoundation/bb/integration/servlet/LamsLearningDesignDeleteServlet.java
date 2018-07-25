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

import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;

import blackboard.base.InitializationException;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;

/**
 * Makes a call to LAMS server to delete a learning design.
 */
public class LamsLearningDesignDeleteServlet extends HttpServlet {

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
	String courseId = request.getParameter("course_id");

	String strLearningDesignId = request.getParameter("sequence_id");
	if ( strLearningDesignId != null ) {
	    strLearningDesignId.trim();
	}

	// validate method parameter and associated parameters
	if ( strLearningDesignId == null || strLearningDesignId.length() == 0 ) {
	    throw new RuntimeException("Required parameters missing. Add sequence_id for the id of the learning design to be deleted");
	}

	long learningDesignId = 0;
	try {
	    learningDesignId = Long.parseLong(strLearningDesignId);
	} catch ( Exception e ) {
	    throw new RuntimeException("Required parameters missing. Add sequence_id for the id of the learning design to be deleted",e);
	}
	
	ContextManager ctxMgr = null;
	Context ctx = null;
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    ctx = ctxMgr.setContext(request);
	    
	    String serverResponse = LamsSecurityUtil.deleteLearningDesigns(ctx, courseId, learningDesignId);
	    
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(serverResponse);
	    
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

