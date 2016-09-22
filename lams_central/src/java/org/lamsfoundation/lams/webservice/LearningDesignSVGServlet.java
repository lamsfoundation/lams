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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.svg.SVGGenerator;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LearningDesignSVGServlet extends HttpServlet {

    private static final long serialVersionUID = -1918180868204870617L;

    private static Logger log = Logger.getLogger(LearningDesignSVGServlet.class);

    private static IntegrationService integrationService = null;

    private ILearningDesignService learningDesignService;

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {
	    // get parameters
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);

	    Long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	    int imageFormat = WebUtil.readIntParam(request, CentralConstants.PARAM_SVG_FORMAT);

	    if (serverId == null || datetime == null || hashValue == null || username == null) {
		String msg = "Parameters missing";
		log.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
	    }

	    // check imageFormat parameter is correct
	    if (!(imageFormat == SVGGenerator.OUTPUT_FORMAT_SVG) && !(imageFormat == SVGGenerator.OUTPUT_FORMAT_PNG)
		    && !(imageFormat == SVGGenerator.OUTPUT_FORMAT_SVG_LAMS_COMMUNITY)) {
		String msg = "Image format parameter is incorrect";
		log.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
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

	    // generate response
	    String contentType;
	    if (imageFormat == SVGGenerator.OUTPUT_FORMAT_SVG) {
		contentType = "image/svg+xml";
	    } else {
		contentType = "image/png";
	    }
	    response.setContentType(contentType);

	    String imagePath = learningDesignService.createLearningDesignSVG(learningDesignId, imageFormat);

	    OutputStream output = response.getOutputStream();
	    FileInputStream input = new FileInputStream(imagePath);
	    IOUtils.copy(input, output);
	    IOUtils.closeQuietly(input);
	    IOUtils.closeQuietly(output);

	} catch (Exception e) {
	    log.error("Problem with LearningDesignRepositoryServlet request", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		    "Problem with LearningDesignRepositoryServlet request");
	}

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException
     *             if an error occure
     */
    @Override
    public void init() throws ServletException {
	integrationService = (IntegrationService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");

	learningDesignService = (ILearningDesignService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("learningDesignService");
    }
}
