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


package org.lamsfoundation.lams.tool.noticeboard.web;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * @author mtruong
 *
 *         ----------------XDoclet Tags--------------------
 *
 * @struts:form name="NbExportForm" type="org.lamsfoundation.lams.tool.noticeboard.web.NbExportForm"
 *
 *              ----------------XDoclet Tags--------------------
 */

public class NbExportForm extends ActionForm {

    static Logger logger = Logger.getLogger(NbExportForm.class.getName());

    private String title;
    private String basicContent;

    private String toolContentID;
    private String toolSessionID;
    private String userID;
    private String mode;

    /**
     * @return Returns the logger.
     */
    public static Logger getLogger() {
	return logger;
    }

    /**
     * @param logger
     *            The logger to set.
     */
    public static void setLogger(Logger logger) {
	NbExportForm.logger = logger;
    }

    /**
     * @return Returns the content.
     */
    public String getBasicContent() {
	return basicContent;
    }

    /**
     * @param basicContent
     *            The content to set.
     */
    public void setBasicContent(String basicContent) {
	this.basicContent = basicContent;
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
	this.basicContent = null;
	this.toolContentID = null;
	this.toolSessionID = null;
	this.userID = null;
    }

    public void populateForm(NoticeboardContent content) {
	setTitle(content.getTitle());
	setBasicContent(content.getContent());
    }
}
