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

/**
 *
 *
 *
 * Get the configuration value for the specified key
 *
 * @author Fiona Malikoff
 */
public class ConfigurationTag extends TagSupport {

    private static final Logger log = Logger.getLogger(ConfigurationTag.class);
    private String key = null;

    /**
     *
     */
    public ConfigurationTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {
	JspWriter writer = pageContext.getOut();
	try {
	    writer.print(Configuration.get(getKey()));
	} catch (IOException e) {
	    log.error("Error in configuration tag", e);
	    throw new JspException(e);
	}
	return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
	return EVAL_PAGE;
    }

    /**
     *
     * Configuration Key
     *
     * @return Returns the property.
     */
    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

}
