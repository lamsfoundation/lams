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

/* $Id$ */
package org.eucm.lams.tool.eadventure.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.service.EadventureServiceProxy;
import org.eucm.lams.tool.eadventure.service.IEadventureService;
import org.eucm.lams.tool.eadventure.util.EadventureToolContentHandler;

public class VarsExchangeServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(VarsExchangeServlet.class);

    private EadventureToolContentHandler handler;
    private IEadventureService service;

    private static final String REPORT = "report";

    @Override
    public void init() throws ServletException {
	service = EadventureServiceProxy.getEadventureService(getServletContext());
	super.init();
    }

    /*
     * public void doGet(HttpServletRequest request, HttpServletResponse response)
     * throws ServletException, IOException {
     * 
     * this.doPost(request, response);
     * }
     */

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
	    // read a String-object from applet
	    // instead of a String-object, you can transmit any object, which
	    // is known to the servlet and to the applet
	    InputStream in = request.getInputStream();
	    ObjectInputStream inputFromApplet = new ObjectInputStream(in);
	    String echo = (String) inputFromApplet.readObject();

	    String pathString = request.getPathInfo();
	    String[] strings = deriveIdFile(pathString);
	    boolean changeButton = service.setAppletInput(strings[2], echo, strings[0], strings[1]);
	    // echo it to the applet
	    if (echo.equals("true") && strings[2].equals(EadventureConstants.VAR_NAME_COMPLETED) && changeButton) {
		// Send the order to applet for it change the visibility of the finish button
		response.setContentType("application/x-java-serialized-object");
		PrintWriter out = response.getWriter();
		out.println("showButton");
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    // Expect format /<userid>/<toolSessionID>/<name>|report/. No parts are optional. Filename may be a path.
    // returns an array of three strings.
    private String[] deriveIdFile(String pathInfo) {
	String[] result = new String[3];

	if (pathInfo != null) {

	    String[] strings = pathInfo.split("/", 4);

	    for (int i = 0, j = 0; i < strings.length && j < 3; i++) {
		// splitting sometimes results in empty strings, so skip them!
		if (strings[i].length() > 0) {
		    result[j++] = strings[i];
		}
	    }

	}

	return result;
    }

}
