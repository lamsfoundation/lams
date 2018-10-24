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


package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;

/**
 * Forum
 *
 * @author ruslan
 *
 *
 *
 *
 */
public class Timestamp implements Cloneable {
    private Long uid; // key
    private ForumUser forumUser;
    private Message message;
    private Date timestamp;

    /**
     * Default contruction method.
     *
     */
    public Timestamp() {
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * Returns the user's timestamp
     *
     * @return timestamp
     *
     */
    public Date getTimestamp() {
	return timestamp;
    }

    /**
     * Sets the user's timestamp
     *
     * @param timestamp
     */
    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    /**
     * @return Returns the user of the user who saved the timestamp
     *
     *
     *
     */
    public ForumUser getForumUser() {
	return forumUser;
    }

    /**
     * @param userid
     *            The userid of the user who saved the timestamp
     */
    public void setForumUser(ForumUser forumUser) {
	this.forumUser = forumUser;
    }

    /**
     *
     *
     */
    public Message getMessage() {
	return message;
    }

    public void setMessage(Message message) {
	this.message = message;
    }

}
