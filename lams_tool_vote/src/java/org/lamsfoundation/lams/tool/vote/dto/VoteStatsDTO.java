/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.vote.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds stats data
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteStatsDTO implements Comparable<Object> {

    private String sessionName;
    private Long sessionUid;
    private Integer countAllUsers;
    private Integer countUsersComplete;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sessionName", sessionName)
		.append("countUsersComplete", countUsersComplete).toString();
    }

    @Override
    public int compareTo(Object o) {
	VoteStatsDTO qaStatsDTO = (VoteStatsDTO) o;
	if (qaStatsDTO == null) {
	    return 1;
	} else {
	    return sessionUid.compareTo(qaStatsDTO.getSessionUid());
	}
    }

    /**
     * @return How many users could vote - based on the session setting in the core, not in the activity
     */
    public Integer getCountAllUsers() {
	return countAllUsers;
    }

    /**
     * @param countAllUsers
     *            The countAllUsers to set.
     */
    public void setCountAllUsers(Integer countAllUsers) {
	this.countAllUsers = countAllUsers;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Long getSessionUid() {
	return sessionUid;
    }

    public void setSessionUid(Long sessionUid) {
	this.sessionUid = sessionUid;
    }

    public Integer getCountUsersComplete() {
	return countUsersComplete;
    }

    public void setCountUsersComplete(Integer countUsersComplete) {
	this.countUsersComplete = countUsersComplete;
    }

}
