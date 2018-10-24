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

package org.lamsfoundation.lams.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.learningdesign.service.LearningDesignService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LearningDesignSVGServlet extends HttpServlet {
    private static final long serialVersionUID = -1918180868204870617L;
    private static Logger log = Logger.getLogger(LearningDesignSVGServlet.class);

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private ILessonService lessonService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);

	    // in case lsId parameter is provided - get learningDesignId from the responsible lesson, otherwise try to get
	    // it from a request as "ldId" parameter
	    Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, true);
	    Long learningDesignId = (lessonId == null) ? WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID) : 
		lessonService.getLessonDetails(lessonId).getLearningDesignID();

	    if (serverId == null || datetime == null || hashValue == null || username == null) {
		String msg = "Parameters missing";
		log.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
	    }

	    // LDEV-2196 preserve character encoding if necessary
	    if (request.getCharacterEncoding() == null) {
		log.debug(
			"request.getCharacterEncoding is empty, parsing username and courseName as 8859_1 to UTF-8...");
		username = new String(username.getBytes("8859_1"), "UTF-8");
	    }

	    // get Server map
	    ExtServer extServer = integrationService.getExtServer(serverId);

	    // authenticate
	    Authenticator.authenticate(extServer, datetime, username, hashValue);

	    String imagePath = LearningDesignService.getLearningDesignSVGPath(learningDesignId);
	    File imageFile = new File(imagePath);
	    if (!imageFile.canRead()) {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return;
	    }

	    boolean download = WebUtil.readBooleanParam(request, "download", false);
	    // should the image be downloaded or a part of page?
	    if (download) {
		String name = learningDesignService
			.getLearningDesignDTO(learningDesignId, Configuration.get(ConfigurationKeys.SERVER_LANGUAGE))
			.getTitle();
		name += "." + "svg";
		name = FileUtil.encodeFilenameForDownload(request, name);
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=" + name);
	    } else {
		response.setContentType("image/svg+xml");
	    }

	    FileInputStream input = new FileInputStream(imagePath);
	    OutputStream output = response.getOutputStream();
	    IOUtils.copy(input, output);
	    IOUtils.closeQuietly(input);
	    IOUtils.closeQuietly(output);
	} catch (Exception e) {
	    log.error("Problem with LearningDesignSVGServlet request", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Problem with LearningDesignSVGServlet request");
	}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }
}