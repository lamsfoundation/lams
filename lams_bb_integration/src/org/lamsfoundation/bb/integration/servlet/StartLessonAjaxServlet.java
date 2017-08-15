package org.lamsfoundation.bb.integration.servlet;
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

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.bb.integration.util.BlackboardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.data.user.User;
import blackboard.persist.PersistenceException;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.ContextManager;

/**
 * Starts a lesson, returning the BB Content Id in JSON. Based on start_lesson_proc but uses the username
 * parameter as a basis for identifying the user. 
 * Return a server error rather than throw an exception as this will be consumed by AJAX call or the like.
 */
public class StartLessonAjaxServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = LoggerFactory.getLogger(StartLessonAjaxServlet.class);
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {

	ContextManager ctxMgr = null;
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);

	    String courseIdStr = request.getParameter("course_id");
	    String contentIdStr = request.getParameter("content_id");
	    String strTitle = BlackboardUtil.getTrimmedString(request, "title");
	    String strSequenceID = BlackboardUtil.getTrimmedString(request, "sequence_id");	    
	    
	    if ( courseIdStr == null || contentIdStr == null || strSequenceID.length()==0 || strTitle.length() == 0) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
			"Unable to create error - parameter missing. course_id, content_id, sequence_id and title required");
	    } else {

		String username = request.getParameter("username");
		User user = BlackboardUtil.loadUserFromDB(username);
		
		String bbContentId = BlackboardUtil.storeBlackboardContent(request, response, user);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print("{\"content_id\":" + bbContentId + "}");
	    }
	    
	} catch (PersistenceException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} catch (ParseException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} catch (ValidationException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} catch (ParserConfigurationException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} catch (SAXException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} catch (InitializationException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} catch (BbServiceException e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} finally {
	    // make sure context is released
	    if (ctxMgr != null) {
		ctxMgr.releaseContext();
	    }
	}

    }

}

