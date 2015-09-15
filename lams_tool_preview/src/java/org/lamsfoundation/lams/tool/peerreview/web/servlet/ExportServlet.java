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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */

package org.lamsfoundation.lams.tool.peerreview.web.servlet;

import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.peerreview.service.PeerreviewApplicationException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

/**
 * Export Portfolio not supported.
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
    private static final long serialVersionUID = -4529093489007108143L;

    private final String FILENAME = "peer_review_main.html";

    @Override
    public void init() throws ServletException {
	super.init();
    }

    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	String basePath =WebUtil.getBaseServerURL()
		+ request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws PeerreviewApplicationException {

    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws PeerreviewApplicationException {

    }

}
