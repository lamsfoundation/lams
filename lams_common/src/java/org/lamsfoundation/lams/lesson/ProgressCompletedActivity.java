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

package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mseaton
 */
public class ProgressCompletedActivity implements Serializable {

    /** Identifier field */
    private Long learnerProgressId;
    private Integer completedActivityId;
    private Date completedDateTime;

    /** default constructor */
    public ProgressCompletedActivity() {
    }

    /**
     * Chain constructor to create new learner progress with minimum data.
     * 
     * @param user
     *            the learner.
     */
    public ProgressCompletedActivity(Long learnerProgressId, Integer completedActivityId, Date completedDateTime) {
	this.learnerProgressId = learnerProgressId;
	this.completedActivityId = completedActivityId;
	this.completedDateTime = completedDateTime;
    }

    public Long getLearnerProgressId() {
	return this.learnerProgressId;
    }

    public void setLearnerProgressId(Long learnerProgressId) {
	this.learnerProgressId = learnerProgressId;
    }

    public Integer getCompletedActivityId() {
	return this.completedActivityId;
    }

    public void setCompletedActivityId(Integer completedActivityId) {
	this.completedActivityId = completedActivityId;
    }

    public Date getCompletedDateTime() {
	return completedDateTime;
    }

    public void setCompletedDateTime(Date completedDateTime) {
	this.completedDateTime = completedDateTime;
    }
}
