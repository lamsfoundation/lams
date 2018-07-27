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


package org.lamsfoundation.lams.tool.qa.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds stats data
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class QaStatsDTO implements Comparable {
    private String countAllUsers;

    private String countSessionComplete;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("question", countAllUsers)
		.append("countSessionComplete", countSessionComplete).toString();
    }

    @Override
    public int compareTo(Object o) {
	QaStatsDTO qaStatsDTO = (QaStatsDTO) o;

	if (qaStatsDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    /**
     * @return Returns the countAllUsers.
     */
    public String getCountAllUsers() {
	return countAllUsers;
    }

    /**
     * @param countAllUsers
     *            The countAllUsers to set.
     */
    public void setCountAllUsers(String countAllUsers) {
	this.countAllUsers = countAllUsers;
    }

    /**
     * @return Returns the countSessionComplete.
     */
    public String getCountSessionComplete() {
	return countSessionComplete;
    }

    /**
     * @param countSessionComplete
     *            The countSessionComplete to set.
     */
    public void setCountSessionComplete(String countSessionComplete) {
	this.countSessionComplete = countSessionComplete;
    }
}
