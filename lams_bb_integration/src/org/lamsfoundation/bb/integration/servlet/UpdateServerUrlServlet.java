/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */

/* $$ */
package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.bb.integration.Constants;
import org.lamsfoundation.bb.integration.util.BlackboardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbPersister;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

/**
 * Updates server urls for all LAMS lessons in the specified course.
 */
public class UpdateServerUrlServlet extends HttpServlet {

    private static final long serialVersionUID = 274843716397522792L;
    private static Logger logger = LoggerFactory.getLogger(UpdateServerUrlServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	// get Parameter values
	String courseIdParam = request.getParameter(Constants.PARAM_COURSE_ID);
	String oldUrlHost = request.getParameter("oldUrlHost");
	String newUrlHost = request.getParameter("newUrlHost");

	// check parameters
	if (courseIdParam == null || oldUrlHost == null || newUrlHost == null) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing expected parameters");
	    return;
	}

	ContextManager ctxMgr = null;
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	try {
	    // check permission
	    if (!PlugInUtil.authorizeForSystemAdmin(request, response)) {
		return;
	    }
	    
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	    Container bbContainer = bbPm.getContainer();
	    ContentDbPersister persister = ContentDbPersister.Default.getInstance();

	    PkId courseId = new PkId(bbContainer, Course.DATA_TYPE, courseIdParam);
	    
	    //find all lessons that should be updated
	    List<Content> lamsContents = BlackboardUtil.getLamsLessonsByCourse(courseId, true);
	    for (Content content : lamsContents) {
		String oldUrl = content.getUrl();
		String newUrl = oldUrl.replaceFirst(oldUrlHost, newUrlHost);
		content.setUrl(newUrl);
		persister.persist(content);

		out.write("Old Url" + oldUrl + ". New url:" + newUrl + "\n\r");
	    }

	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PlugInException e) {
	    throw new ServletException(e);
	} finally {
	    // make sure context is released
	    if (ctxMgr != null) {
		ctxMgr.releaseContext();
	    }
	    out.flush();
	    out.close();
	}

	out.write("OK");
    }

}
