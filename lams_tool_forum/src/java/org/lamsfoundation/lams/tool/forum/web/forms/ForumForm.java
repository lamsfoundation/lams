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

package org.lamsfoundation.lams.tool.forum.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.model.Forum;

/**
 *
 * Message Form.
 *
 *
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:36
 */
public class ForumForm {
    private static final long serialVersionUID = -6054354910960460120L;
    private static Logger logger = Logger.getLogger(ForumForm.class.getName());

    //Forum fields
    private Long toolContentID;
    private String currentTab;
    private String sessionMapID;
    private String contentFolderID;

    private Forum forum;

    /**
     * Empty construction method
     */
    public ForumForm() {

	this.toolContentID = new Long(0);
	this.forum = new Forum();
	this.forum.setTitle("");
	this.currentTab = "";
    }

    /**
     * Initialize this form by given <code>Forum</code> instance.
     *
     * @param forum
     */
    public void setForum(Forum forum) {
	this.forum = forum;
	//set Form special varaible from given forum
	if (forum != null) {
	    this.toolContentID = forum.getContentId();
	} else {
	    logger.error("Initial ForumForm failed by null value of Forum.");
	}

    }

    public void reset(HttpServletRequest request) {
	forum.setAllowEdit(false);
	forum.setAllowAnonym(false);
	forum.setAllowRichEditor(false);
	forum.setLimitedMaxCharacters(false);
	forum.setLockWhenFinished(false);
	forum.setAllowNewTopic(false);
	forum.setAllowUpload(false);
	forum.setAllowRateMessages(false);
    }

    //-------------------------get/set methods----------------
    public Forum getForum() {
	return forum;
    }

    public String getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    public Long getToolContentID() {
	return toolContentID;
    }

    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

}
