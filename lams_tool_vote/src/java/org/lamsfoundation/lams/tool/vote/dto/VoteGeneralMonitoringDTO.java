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

import java.util.List;
//import java.util.Map;

/**
 * DTO that holds monitoring flow properties
 *
 * @author Ozgur Demirtas
 */
public class VoteGeneralMonitoringDTO implements Comparable<Object> {
    protected String requestLearningReport;
    protected String userExceptionNoToolSessions;
    protected String userExceptionContentInUse;
    protected String userExceptionContentDoesNotExist;
    protected String userExceptionNoStudentActivity;

    protected String currentTab;
    protected String activityTitle;
    protected String activityInstructions;
    protected String countAllUsers;
    protected String countSessionComplete;
    protected String responseId;

    protected String toolContentID;
    //    protected List listMonitoredAnswersContainerDto;

    protected List<SessionDTO> sessionDTOs;
    //   protected Map mapStandardNominationsContent;
    //  protected Map mapStandardNominationsHTMLedContent;
    // protected Map mapStandardRatesContent;
    //   protected Map mapStandardUserCount;
    // protected Map mapStandardQuestionUid;
    //  protected Map mapStandardToolSessionUid;

    protected String sessionUserCount;
    protected String completedSessionUserCount;
    //  protected List mapStudentsVoted;

    protected String contentFolderID;

    /**
     * @return Returns the userExceptionContentDoesNotExist.
     */
    public String getUserExceptionContentDoesNotExist() {
	return userExceptionContentDoesNotExist;
    }

    /**
     * @param userExceptionContentDoesNotExist
     *            The userExceptionContentDoesNotExist to set.
     */
    public void setUserExceptionContentDoesNotExist(String userExceptionContentDoesNotExist) {
	this.userExceptionContentDoesNotExist = userExceptionContentDoesNotExist;
    }

    /**
     * @return Returns the userExceptionNoStudentActivity.
     */
    public String getUserExceptionNoStudentActivity() {
	return userExceptionNoStudentActivity;
    }

    /**
     * @param userExceptionNoStudentActivity
     *            The userExceptionNoStudentActivity to set.
     */
    public void setUserExceptionNoStudentActivity(String userExceptionNoStudentActivity) {
	this.userExceptionNoStudentActivity = userExceptionNoStudentActivity;
    }

    /**
     * @return Returns the currentTab.
     */
    public String getCurrentTab() {
	return currentTab;
    }

    /**
     * @param currentTab
     *            The currentTab to set.
     */
    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    /**
     * @return Returns the userExceptionContentInUse.
     */
    public String getUserExceptionContentInUse() {
	return userExceptionContentInUse;
    }

    /**
     * @param userExceptionContentInUse
     *            The userExceptionContentInUse to set.
     */
    public void setUserExceptionContentInUse(String userExceptionContentInUse) {
	this.userExceptionContentInUse = userExceptionContentInUse;
    }

    /**
     * @return Returns the activityInstructions.
     */
    public String getActivityInstructions() {
	return activityInstructions;
    }

    /**
     * @param activityInstructions
     *            The activityInstructions to set.
     */
    public void setActivityInstructions(String activityInstructions) {
	this.activityInstructions = activityInstructions;
    }

    /**
     * @return Returns the activityTitle.
     */
    public String getActivityTitle() {
	return activityTitle;
    }

    /**
     * @param activityTitle
     *            The activityTitle to set.
     */
    public void setActivityTitle(String activityTitle) {
	this.activityTitle = activityTitle;
    }

    /**
     * @return Returns the requestLearningReport.
     */
    public String getRequestLearningReport() {
	return requestLearningReport;
    }

    /**
     * @param requestLearningReport
     *            The requestLearningReport to set.
     */
    public void setRequestLearningReport(String requestLearningReport) {
	this.requestLearningReport = requestLearningReport;
    }

    /**
     * @return Returns the userExceptionNoToolSessions.
     */
    public String getUserExceptionNoToolSessions() {
	return userExceptionNoToolSessions;
    }

    /**
     * @param userExceptionNoToolSessions
     *            The userExceptionNoToolSessions to set.
     */
    public void setUserExceptionNoToolSessions(String userExceptionNoToolSessions) {
	this.userExceptionNoToolSessions = userExceptionNoToolSessions;
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

    /**
     * @return Returns the sessionDtos.
     */
    public List<SessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    /**
     * @param sessionDtos
     *            The sessionDtos to set.
     */
    public void setSessionDTOs(List<SessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
    }

//    /**
//     * @return Returns the mapStandardNominationsContent.
//     */
//    public Map getMapStandardNominationsContent() {
//	return mapStandardNominationsContent;
//    }
//
//    /**
//     * @param mapStandardNominationsContent
//     *            The mapStandardNominationsContent to set.
//     */
//    public void setMapStandardNominationsContent(Map mapStandardNominationsContent) {
//	this.mapStandardNominationsContent = mapStandardNominationsContent;
//    }

//    /**
//     * @return Returns the mapStandardNominationsHTMLedContent.
//     */
//    public Map getMapStandardNominationsHTMLedContent() {
//	return mapStandardNominationsHTMLedContent;
//    }
//
//    /**
//     * @param mapStandardNominationsHTMLedContent
//     *            The mapStandardNominationsHTMLedContent to set.
//     */
//    public void setMapStandardNominationsHTMLedContent(Map mapStandardNominationsHTMLedContent) {
//	this.mapStandardNominationsHTMLedContent = mapStandardNominationsHTMLedContent;
//    }

//    /**
//     * @return Returns the mapStandardQuestionUid.
//     */
//    public Map getMapStandardQuestionUid() {
//	return mapStandardQuestionUid;
//    }
//
//    /**
//     * @param mapStandardQuestionUid
//     *            The mapStandardQuestionUid to set.
//     */
//    public void setMapStandardQuestionUid(Map mapStandardQuestionUid) {
//	this.mapStandardQuestionUid = mapStandardQuestionUid;
//    }

//    /**
//     * @return Returns the mapStandardRatesContent.
//     */
//    public Map getMapStandardRatesContent() {
//	return mapStandardRatesContent;
//    }
//
//    /**
//     * @param mapStandardRatesContent
//     *            The mapStandardRatesContent to set.
//     */
//    public void setMapStandardRatesContent(Map mapStandardRatesContent) {
//	this.mapStandardRatesContent = mapStandardRatesContent;
//    }

//    /**
//     * @return Returns the mapStandardToolSessionUid.
//     */
//    public Map getMapStandardToolSessionUid() {
//	return mapStandardToolSessionUid;
//    }
//
//    /**
//     * @param mapStandardToolSessionUid
//     *            The mapStandardToolSessionUid to set.
//     */
//    public void setMapStandardToolSessionUid(Map mapStandardToolSessionUid) {
//	this.mapStandardToolSessionUid = mapStandardToolSessionUid;
//    }

//    /**
//     * @return Returns the mapStandardUserCount.
//     */
//    public Map getMapStandardUserCount() {
//	return mapStandardUserCount;
//    }
//
//    /**
//     * @param mapStandardUserCount
//     *            The mapStandardUserCount to set.
//     */
//    public void setMapStandardUserCount(Map mapStandardUserCount) {
//	this.mapStandardUserCount = mapStandardUserCount;
//    }

//    /**
//     * @param mapStudentsVoted
//     *            The mapStudentsVoted to set.
//     */
//    public void setMapStudentsVoted(List mapStudentsVoted) {
//	this.mapStudentsVoted = mapStudentsVoted;
//    }
//
//    /**
//     * @return Returns the mapStudentsVoted.
//     */
//    public List getMapStudentsVoted() {
//	return mapStudentsVoted;
//    }

    /**
     * @return Returns the responseId.
     */
    public String getResponseId() {
	return responseId;
    }

    /**
     * @param responseId
     *            The responseId to set.
     */
    public void setResponseId(String responseId) {
	this.responseId = responseId;
    }

    /**
     * @return Returns the toolContentID.
     */
    public String getToolContentID() {
	return toolContentID;
    }

    /**
     * @param toolContentID
     *            The toolContentID to set.
     */
    public void setToolContentID(String toolContentID) {
	this.toolContentID = toolContentID;
    }

    /**
     * @return Returns the completedSessionUserCount.
     */
    public String getCompletedSessionUserCount() {
	return completedSessionUserCount;
    }

    /**
     * @param completedSessionUserCount
     *            The completedSessionUserCount to set.
     */
    public void setCompletedSessionUserCount(String completedSessionUserCount) {
	this.completedSessionUserCount = completedSessionUserCount;
    }

    /**
     * @return Returns the sessionUserCount.
     */
    public String getSessionUserCount() {
	return sessionUserCount;
    }

    /**
     * @param sessionUserCount
     *            The sessionUserCount to set.
     */
    public void setSessionUserCount(String sessionUserCount) {
	this.sessionUserCount = sessionUserCount;
    }

    @Override
    public int compareTo(Object o) {
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = (VoteGeneralMonitoringDTO) o;

	if (voteGeneralMonitoringDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    /**
     * @return Returns the contentFolderID.
     */
    public String getContentFolderID() {
	return contentFolderID;
    }

    /**
     * @param contentFolderID
     *            The contentFolderID to set.
     */
    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }
}
