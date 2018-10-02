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

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author Andrey Balan
 */
public class GetServerTimeServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(GetServerTimeServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
	try {
	    long currentTime = (new Date()).getTime();

	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();

	    out.println(currentTime);

	    out.flush();
	    out.close();

	} catch (Exception e) {
	    logger.error(e.getMessage());
	}
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	doPost(request, response);
    }

}
