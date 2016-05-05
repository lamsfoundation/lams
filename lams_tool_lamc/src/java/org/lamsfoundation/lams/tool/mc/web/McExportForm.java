/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;

/**
 * @author Ozgur Demirtas
 *
 */
public class McExportForm extends ActionForm {

    private String title;
    private String content;

    private String toolContentID;
    private String toolSessionID;
    private String userID;
    private String mode;

    /**
     * @return Returns the content.
     */
    public String getContent() {
	return content;
    }

    /**
     * @param content
     *            The content to set.
     */
    public void setContent(String content) {
	this.content = content;
    }

    /**
     * @return Returns the mode.
     */
    public String getMode() {
	return mode;
    }

    /**
     * @param mode
     *            The mode to set.
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the toolContentId.
     */
    public String getToolContentID() {
	return toolContentID;
    }

    /**
     * @param toolContentId
     *            The toolContentId to set.
     */
    public void setToolContentID(String toolContentId) {
	this.toolContentID = toolContentId;
    }

    /**
     * @return Returns the toolSessionId.
     */
    public String getToolSessionID() {
	return toolSessionID;
    }

    /**
     * @param toolSessionId
     *            The toolSessionId to set.
     */
    public void setToolSessionID(String toolSessionId) {
	this.toolSessionID = toolSessionId;
    }

    /**
     * @return Returns the userId.
     */
    public String getUserID() {
	return userID;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserID(String userId) {
	this.userID = userId;
    }

    public void reset() {
	this.mode = null;
	this.title = null;
	this.content = null;
	this.toolContentID = null;
	this.toolSessionID = null;
	this.userID = null;
    }

    public void populateForm(McContent content) {
	String strTitle = null;
	if (content.getTitle() == null) {
	    strTitle = "";
	} else {
	    strTitle = content.getTitle();
	}
	setTitle(strTitle);

	String strContent = null;
	if (content.getContent() == null) {
	    strContent = "";
	} else {
	    strContent = content.getContent();
	}
	setContent(strContent);
    }
}
