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

package org.lamsfoundation.lams.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.web.util.TokenProcessor;

/**
 * @author daveg edited by lfoxton
 *
 */
public abstract class LamsDispatchAction extends DispatchAction {

    protected static String className = "Action";

    public static final String ENCODING_UTF8 = "UTF8";
    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    public static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    public static final long HEADER_EXPIRES_VALUE = 1000L * 60L * 60L * 24L * 365L;

    protected static TokenProcessor token = TokenProcessor.getInstance();
    protected static Logger log = Logger.getLogger(LamsDispatchAction.class);

    @Override
    protected void saveToken(javax.servlet.http.HttpServletRequest request) {
	token.saveToken(request);
    }

    @Override
    protected boolean isTokenValid(javax.servlet.http.HttpServletRequest request) {
	return token.isTokenValid(request, false);
    }

    @Override
    protected boolean isTokenValid(javax.servlet.http.HttpServletRequest request, boolean reset) {
	return token.isTokenValid(request, reset);
    }

    @Override
    protected void resetToken(HttpServletRequest request) {
	token.resetToken(request);
    }

    protected void writeAJAXResponse(HttpServletResponse response, String output) throws IOException {
	// set it to unicode (LDEV-1275)
	response.setContentType("text/html;charset=utf-8");
	PrintWriter writer = response.getWriter();

	if (output.length() > 0) {
	    writer.println(output);
	}
    }

    protected void writeAJAXOKResponse(HttpServletResponse response) throws IOException {
	writeAJAXResponse(response, "OK");
    }

    protected void writeResponse(HttpServletResponse response, String contentType, String characterEncoding,
	    String output) throws IOException {
	response.setContentType(contentType);
	response.setCharacterEncoding(characterEncoding);
	PrintWriter writer = response.getWriter();
	if (output.length() > 0) {
	    writer.println(output);
	}
    }

}
