/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * A class representing a finished activity for a user
 *
 * @author lfoxton
 *
 */
@Embeddable
public class CompletedActivityProgress implements Serializable {

    private static final long serialVersionUID = -6210497575761751861L;

    @Column(name = "start_date_time")
    private Date startDate;

    @Column(name = "completed_date_time")
    private Date finishDate;

    public CompletedActivityProgress() {
    }

    public CompletedActivityProgress(Date startDate, Date finishDate) {
	this.startDate = startDate;
	this.finishDate = finishDate;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }
}