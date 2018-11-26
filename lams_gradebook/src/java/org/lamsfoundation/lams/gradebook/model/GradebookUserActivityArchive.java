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

package org.lamsfoundation.lams.gradebook.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_gradebook_user_activity_archive")
public class GradebookUserActivityArchive {

    @Id
    @Column
    private long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ToolActivity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User learner;

    @Column
    private Double mark;

    @Column
    private String feedback;

    @Column(name = "marked_in_gradebook")
    private Boolean markedInGradebook;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "archive_date")
    private Date archiveDate;

    public GradebookUserActivityArchive() {
    }

    public GradebookUserActivityArchive(GradebookUserActivity source, Date archiveDate) {
	this.uid = source.getUid();
	this.activity = source.getActivity();
	this.learner = source.getLearner();
	this.mark = source.getMark();
	this.feedback = source.getFeedback();
	this.markedInGradebook = source.getMarkedInGradebook();
	this.updateDate = source.getUpdateDate();
	this.archiveDate = archiveDate;
    }

    public long getUid() {
	return uid;
    }

    public void setUid(long uid) {
	this.uid = uid;
    }

    public ToolActivity getActivity() {
	return activity;
    }

    public void setActivity(ToolActivity activity) {
	this.activity = activity;
    }

    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    public Double getMark() {
	return mark;
    }

    public void setMark(Double mark) {
	this.mark = mark;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public Boolean getMarkedInGradebook() {
	return markedInGradebook;
    }

    public void setMarkedInGradebook(Boolean markedInGradebook) {
	this.markedInGradebook = markedInGradebook;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public Date getArchiveDate() {
	return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
	this.archiveDate = archiveDate;
    }
}