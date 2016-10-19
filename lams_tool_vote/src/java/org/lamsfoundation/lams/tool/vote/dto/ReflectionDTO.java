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

package org.lamsfoundation.lams.tool.vote.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds reflections from users
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class ReflectionDTO implements Comparable<ReflectionDTO> {
    protected String userName;

    protected String userId;

    protected String sessionId;

    protected String reflectionUid;

    protected String entry;

    @Override
    public int compareTo(ReflectionDTO reflectionDTO) {

	if (reflectionDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("userName: ", userName).append("userId: ", userId)
		.append("sessionId: ", sessionId).append("reflectionUid: ", reflectionUid).append("entry: ", entry)
		.toString();
    }

    /**
     * @return Returns the entry.
     */
    public String getEntry() {
	return entry;
    }

    /**
     * @param entry
     *            The entry to set.
     */
    public void setEntry(String entry) {
	this.entry = entry;
    }

    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
	return sessionId;
    }

    /**
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @return Returns the reflectionUid.
     */
    public String getReflectionUid() {
	return reflectionUid;
    }

    /**
     * @param reflectionUid
     *            The reflectionUid to set.
     */
    public void setReflectionUid(String reflectionUid) {
	this.reflectionUid = reflectionUid;
    }

    /**
     * @return Returns the userId.
     */
    public String getUserId() {
	return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
	return userName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }
}
