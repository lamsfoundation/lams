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

package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DTO which is passed by a tool with its completion status for a particular learner and tool session. 
 * Value of the status field is the same as learner progress status
 */
public class ToolCompletionStatus{

    /** Indicates activity has been completed */
    public static final byte ACTIVITY_COMPLETED = 1;
    /** Indicates activity has been attempted but not completed */
    public static final byte ACTIVITY_ATTEMPTED = 2;
    /** Indicates activity has not been attempted yet */
    public static final byte ACTIVITY_NOT_ATTEMPTED = 3;

    private byte status;

    private Date startDate;

    private Date finishDate;

    public ToolCompletionStatus(byte status, Date startDate, Date finishDate) {
	this.status = status;
	this.startDate = startDate;
	this.finishDate = finishDate;
    }
    
    public int getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("status", status).append("startDate", startDate).append("finishDate", finishDate)
		.toString();
    }

}
