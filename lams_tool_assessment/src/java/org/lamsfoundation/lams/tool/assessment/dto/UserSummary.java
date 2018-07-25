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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;

/**
 * List contains following element: <br>
 *
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>AssessmentQuestion.uid</li>
 * <li>AssessmentQuestion.question_type</li>
 * <li>
 * AssessmentQuestion.create_by_author</li>
 * <li>AssessmentQuestion.is_hide</li>
 * <li>AssessmentQuestion.title</li>
 * <li>
 * User.login_name</li>
 * <li>count(assessment_question_uid)</li>
 *
 * @author Andrey Balan
 */
public class UserSummary {

    private AssessmentUser user;
    private int numberOfAttempts;
    private Date timeOfLastAttempt;
    private float lastAttemptGrade;

    private List<UserSummaryItem> userSummaryItems;

    public AssessmentUser getUser() {
	return user;
    }

    public void setUser(AssessmentUser user) {
	this.user = user;
    }

    public int getNumberOfAttempts() {
	return numberOfAttempts;
    }

    public void setNumberOfAttempts(int numberOfAttempts) {
	this.numberOfAttempts = numberOfAttempts;
    }

    public Date getTimeOfLastAttempt() {
	return timeOfLastAttempt;
    }

    public void setTimeOfLastAttempt(Date timeOfLastAttempt) {
	this.timeOfLastAttempt = timeOfLastAttempt;
    }

    public float getLastAttemptGrade() {
	return lastAttemptGrade;
    }

    public void setLastAttemptGrade(float lastAttemptGrade) {
	this.lastAttemptGrade = lastAttemptGrade;
    }

    public List<UserSummaryItem> getUserSummaryItems() {
	return userSummaryItems;
    }

    public void setUserSummaryItems(List<UserSummaryItem> userSummaryItems) {
	this.userSummaryItems = userSummaryItems;
    }

}
