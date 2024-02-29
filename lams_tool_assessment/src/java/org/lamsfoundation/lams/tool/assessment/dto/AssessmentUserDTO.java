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

import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

public class AssessmentUserDTO implements IUserDetails {
    private Long userId;
    private String firstName;
    private String lastName;
    private String login;
    private Long sessionId;
    private float grade;
    private boolean resultSubmitted;

    private Long questionResultUid;
    private String portraitId;

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String userId) {
	this.firstName = userId;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public float getGrade() {
	return grade;
    }

    public void setGrade(float grade) {
	this.grade = grade;
    }

    public boolean isResultSubmitted() {
	return resultSubmitted;
    }

    public void setResultSubmitted(boolean resultSubmitted) {
	this.resultSubmitted = resultSubmitted;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getQuestionResultUid() {
	return questionResultUid;
    }

    public void setQuestionResultUid(Long questionResultUid) {
	this.questionResultUid = questionResultUid;
    }

    public String getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(String portraitId) {
	this.portraitId = portraitId;
    }

}