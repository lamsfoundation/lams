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
package org.lamsfoundation.ld.integration.blackboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.Constants;

import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.data.navigation.CourseToc;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInUtil;

/**
 * Updates server urls for all LAMS lessons in the specified course.
 */
public class UpdateServerUrlServlet extends HttpServlet {

    private static final long serialVersionUID = 274843716397522792L;
    private static Logger logger = Logger.getLogger(UpdateServerUrlServlet.class);

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

	    ContentDbLoader contentDbLoader = ContentDbLoader.Default.getInstance();
	    CourseTocDbLoader courseTocDbLoader = CourseTocDbLoader.Default.getInstance();
	    ContentDbPersister persister = (ContentDbPersister) bbPm.getPersister(ContentDbPersister.TYPE);

	    Id courseId = new PkId(bbContainer, Course.DATA_TYPE, courseIdParam);
	    
	    List<CourseToc> listCourseToc = courseTocDbLoader.loadByCourseId(courseId);
	    for (CourseToc cToc : listCourseToc) {

		// determine if the TOC item is of type "CONTENT" rather than application, or something else
		if ((cToc.getTargetType() == CourseToc.Target.CONTENT) && (cToc.getContentId() != Id.UNSET_ID)) {
		    
		    // load the content object and iterate through it
		    List<Content> listContent = contentDbLoader.loadListById(cToc.getContentId());
		    for (Content content : listContent) {

			if (content.getUrlHost().contains(oldUrlHost)
				&& (content.getContentHandler().equals("resource/x-lams-lamscontent")
					|| content.getContentHandler().equals("resource/x-ntu-hdllams"))) {
			    String oldUrl = content.getUrl();
			    String newUrl = oldUrl.replaceFirst(oldUrlHost, newUrlHost);
			    content.setUrl(newUrl);
			    persister.persist(content);

			    out.write("Old Url" + oldUrl + ". New url:" + newUrl + "\n\r");
			}
		
		    }
		}
	    }
	    

	} catch (Exception e) {
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

    private static String extractParameterValue(String url, String param) {
	if (url != null && param != null) {
	    int quotationMarkIndex = url.indexOf("?");
	    String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	    String[] paramEntries = queryPart.split("&");
	    for (String paramEntry : paramEntries) {
		String[] paramEntrySplitted = paramEntry.split("=");
		if ((paramEntrySplitted.length > 1) && param.equalsIgnoreCase(paramEntrySplitted[0])) {
		    return paramEntrySplitted[1];
		}
	    }
	}
	return null;
    }

}
