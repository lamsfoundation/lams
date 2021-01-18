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

package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;

/**
 *
 *
 * @author Andrey Balan
 */
public class SessionDTO {

    private Long sessionId;
    private String sessionName;
    private int numberLearners;
    private String minMark;
    private String maxMark;
    private String avgMark;

    private boolean leaderFinished;

    //used for export purposes only
    private List<AssessmentResult> assessmentResults;

    public SessionDTO() {
    }

    /**
     * Contruction method for monitoring summary function.
     *
     * <B>Don't not set isInitGroup and viewNumber fields</B>
     *
     * @param sessionName
     * @param question
     * @param isInitGroup
     */
    public SessionDTO(Long sessionId, String sessionName) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public List<AssessmentResult> getAssessmentResults() {
	return assessmentResults;
    }

    public void setAssessmentResults(List<AssessmentResult> assessmentResults) {
	this.assessmentResults = assessmentResults;
    }

    public int getNumberLearners() {
	return numberLearners;
    }

    public void setNumberLearners(int numberLearners) {
	this.numberLearners = numberLearners;
    }

    public String getAvgMark() {
	return avgMark;
    }

    public void setAvgMark(String avgMark) {
	this.avgMark = avgMark;
    }

    public String getMinMark() {
	return minMark;
    }

    public void setMinMark(String minMark) {
	this.minMark = minMark;
    }

    public String getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(String maxMark) {
	this.maxMark = maxMark;
    }

    public boolean isLeaderFinished() {
	return leaderFinished;
    }

    public void setLeaderFinished(boolean leaderFinished) {
	this.leaderFinished = leaderFinished;
    }
}