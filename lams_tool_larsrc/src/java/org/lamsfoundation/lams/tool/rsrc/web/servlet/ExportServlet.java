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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */

package org.lamsfoundation.lams.tool.rsrc.web.servlet;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dto.Summary;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceServiceProxy;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

/**
 * Export portfolio servlet to export all shared resource into offline HTML
 * package.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "shared_resources_main.html";

	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {
		try {
			if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
				learner(request, response, directoryName, cookies);
			} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
				teacher(request, response, directoryName, cookies);
			}
		} catch (ResourceApplicationException e) {
			logger.error("Cannot perform export for share resource tool.");
		}

		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp", directoryName, FILENAME, cookies);

		return FILENAME;
	}

	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws ResourceApplicationException {

		IResourceService service = ResourceServiceProxy.getResourceService(getServletContext());

		if (userID == null || toolSessionID == null) {
			String error = "Tool session Id or user Id is null. Unable to continue";
			logger.error(error);
			throw new ResourceApplicationException(error);
		}

		ResourceUser learner = service.getUserByID(userID);

		if (learner == null) {
			String error = "The user with user id " + userID + " does not exist.";
			logger.error(error);
			throw new ResourceApplicationException(error);
		}

		Resource content = service.getResourceByContentId(toolSessionID);

		if (content == null) {
			String error = "The content for this activity has not been defined yet.";
			logger.error(error);
			throw new ResourceApplicationException(error);
		}
		List<Summary> group = service.exportBySessionId(toolSessionID);
		request.getSession().setAttribute(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, group);
	}

	public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws ResourceApplicationException {
		IResourceService service = ResourceServiceProxy.getResourceService(getServletContext());

		// check if toolContentId exists in db or not
		if (toolContentID == null) {
			String error = "Tool Content Id is missing. Unable to continue";
			logger.error(error);
			throw new ResourceApplicationException(error);
		}

		Resource content = service.getResourceByContentId(toolContentID);

		if (content == null) {
			String error = "Data is missing from the database. Unable to Continue";
			logger.error(error);
			throw new ResourceApplicationException(error);
		}
		List<List> groupList = service.exportByContentId(toolContentID);
		
		// put it into HTTPSession
		request.getSession().setAttribute(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, groupList);
	}

}
