/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Output the server url from the value stored in the shared session. This
 * will be the same value as the server url in the lams.xml configuration file.
 *
 *
 *
 *
 * Output the Server URL as defined in the lams.xml configuration file.
 *
 * @author Fiona Malikoff
 */
public class LAMSURLTag extends TagSupport {

    private static final long serialVersionUID = -3773379475085729642L;

    private static final Logger log = Logger.getLogger(LAMSURLTag.class);

    /**
     *
     */
    public LAMSURLTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {
	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	serverURL = (serverURL != null ? serverURL.trim() : null);
	if (serverURL != null && serverURL.length() > 0) {
	    JspWriter writer = pageContext.getOut();
	    try {
		writer.print(serverURL);
	    } catch (IOException e) {
		log.error("ServerURLTag unable to write out server URL due to IOException. ", e);
		throw new JspException(e);
	    }
	} else {
	    log.warn("ServerURLTag unable to write out server URL as it is missing from the configuration file.");
	}

	return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
	return EVAL_PAGE;
    }

}
