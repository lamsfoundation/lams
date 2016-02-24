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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * DTO that holds monitoring flow properties
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McGeneralMonitoringDTO implements Comparable {

    protected Map mapQuestionContent;
    protected String defaultQuestionContent;

    protected String currentMonitoringTab;
    protected String requestLearningReport;
    protected String userExceptionNoToolSessions;
    protected String userExceptionContentInUse;
    protected String userExceptionContentDoesNotExist;
    protected String userExceptionNoStudentActivity;

    protected String currentTab;
    protected String activityTitle;
    protected String activityInstructions;
    protected String defaultOptionContent;
    protected Integer countAllUsers;
    protected Integer countSessionComplete;
    protected String groupName;
    protected String existsOpenMcs;
    protected String responseId;
    protected String displayAnswers;

    protected String toolContentID;
    protected Map summaryToolSessions;
    protected List listMonitoredAnswersContainerDto;
    protected List listUserEntries;

    protected String showOpenMcsSection;

    protected List listMcAllSessionsDTO;
    protected Map mapStandardQuestionUid;
    protected Map mapStandardToolSessionUid;

    protected String sessionUserCount;
    protected String completedSessionUserCount;
    protected String completedSessionUserPercent;
    protected List mapStudentsMcd;

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
     * @return Returns the defaultOptionContent.
     */
    public String getDefaultOptionContent() {
	return defaultOptionContent;
    }

    /**
     * @param defaultOptionContent
     *            The defaultOptionContent to set.
     */
    public void setDefaultOptionContent(String defaultOptionContent) {
	this.defaultOptionContent = defaultOptionContent;
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
     * @return Returns the currentMonitoringTab.
     */
    public String getCurrentMonitoringTab() {
	return currentMonitoringTab;
    }

    /**
     * @param currentMonitoringTab
     *            The currentMonitoringTab to set.
     */
    public void setCurrentMonitoringTab(String currentMonitoringTab) {
	this.currentMonitoringTab = currentMonitoringTab;
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

    /**
     * @return Returns the countSessionComplete.
     */
    public Integer getCountSessionComplete() {
	return countSessionComplete;
    }

    /**
     * @param countSessionComplete
     *            The countSessionComplete to set.
     */
    public void setCountSessionComplete(Integer countSessionComplete) {
	this.countSessionComplete = countSessionComplete;
    }

    /**
     * @return Returns the summaryToolSessions.
     */
    public Map getSummaryToolSessions() {
	return summaryToolSessions;
    }

    /**
     * @param summaryToolSessions
     *            The summaryToolSessions to set.
     */
    public void setSummaryToolSessions(Map summaryToolSessions) {
	this.summaryToolSessions = summaryToolSessions;
    }

    /**
     * @return Returns the groupName.
     */
    public String getGroupName() {
	return groupName;
    }

    /**
     * @param groupName
     *            The groupName to set.
     */
    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    /**
     * @return Returns the listMonitoredAnswersContainerDto.
     */
    public List getListMonitoredAnswersContainerDto() {
	return listMonitoredAnswersContainerDto;
    }

    /**
     * @param listMonitoredAnswersContainerDto
     *            The listMonitoredAnswersContainerDto to set.
     */
    public void setListMonitoredAnswersContainerDto(List listMonitoredAnswersContainerDto) {
	this.listMonitoredAnswersContainerDto = listMonitoredAnswersContainerDto;
    }

    /**
     * @return Returns the listUserEntries.
     */
    public List getListUserEntries() {
	return listUserEntries;
    }

    /**
     * @param listUserEntries
     *            The listUserEntries to set.
     */
    public void setListUserEntries(List listUserEntries) {
	this.listUserEntries = listUserEntries;
    }

    /**
     * @return Returns the existsOpenMcs.
     */
    public String getExistsOpenMcs() {
	return existsOpenMcs;
    }

    /**
     * @param existsOpenMcs
     *            The existsOpenMcs to set.
     */
    public void setExistsOpenMcs(String existsOpenMcs) {
	this.existsOpenMcs = existsOpenMcs;
    }

    /**
     * @return Returns the listMcAllSessionsDTO.
     */
    public List getListMcAllSessionsDTO() {
	return listMcAllSessionsDTO;
    }

    /**
     * @param listMcAllSessionsDTO
     *            The listMcAllSessionsDTO to set.
     */
    public void setListMcAllSessionsDTO(List listMcAllSessionsDTO) {
	this.listMcAllSessionsDTO = listMcAllSessionsDTO;
    }

    /**
     * @return Returns the mapStandardQuestionUid.
     */
    public Map getMapStandardQuestionUid() {
	return mapStandardQuestionUid;
    }

    /**
     * @param mapStandardQuestionUid
     *            The mapStandardQuestionUid to set.
     */
    public void setMapStandardQuestionUid(Map mapStandardQuestionUid) {
	this.mapStandardQuestionUid = mapStandardQuestionUid;
    }

    /**
     * @return Returns the mapStandardToolSessionUid.
     */
    public Map getMapStandardToolSessionUid() {
	return mapStandardToolSessionUid;
    }

    /**
     * @param mapStandardToolSessionUid
     *            The mapStandardToolSessionUid to set.
     */
    public void setMapStandardToolSessionUid(Map mapStandardToolSessionUid) {
	this.mapStandardToolSessionUid = mapStandardToolSessionUid;
    }

    /**
     * @return Returns the showOpenMcsSection.
     */
    public String getShowOpenMcsSection() {
	return showOpenMcsSection;
    }

    /**
     * @param showOpenMcsSection
     *            The showOpenMcsSection to set.
     */
    public void setShowOpenMcsSection(String showOpenMcsSection) {
	this.showOpenMcsSection = showOpenMcsSection;
    }

    /**
     * @param mapStudentsMcd
     *            The mapStudentsMcd to set.
     */
    public void setMapStudentsMcd(List mapStudentsMcd) {
	this.mapStudentsMcd = mapStudentsMcd;
    }

    /**
     * @return Returns the mapStudentsMcd.
     */
    public List getMapStudentsMcd() {
	return mapStudentsMcd;
    }

    /**
     * @return Returns the displayAnswers.
     */
    public String getDisplayAnswers() {
	return displayAnswers;
    }

    /**
     * @param displayAnswers
     *            The displayAnswers to set.
     */
    public void setDisplayAnswers(String displayAnswers) {
	this.displayAnswers = displayAnswers;
    }

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
     * @return Returns the completedSessionUserPercent.
     */
    public String getCompletedSessionUserPercent() {
	return completedSessionUserPercent;
    }

    /**
     * @param completedSessionUserPercent
     *            The completedSessionUserPercent to set.
     */
    public void setCompletedSessionUserPercent(String completedSessionUserPercent) {
	this.completedSessionUserPercent = completedSessionUserPercent;
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
	McGeneralMonitoringDTO mcGeneralMonitoringDTO = (McGeneralMonitoringDTO) o;

	if (mcGeneralMonitoringDTO == null) {
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

    /**
     * @return Returns the defaultQuestionContent.
     */
    public String getDefaultQuestionContent() {
	return defaultQuestionContent;
    }

    /**
     * @param defaultQuestionContent
     *            The defaultQuestionContent to set.
     */
    public void setDefaultQuestionContent(String defaultQuestionContent) {
	this.defaultQuestionContent = defaultQuestionContent;
    }

    /**
     * @return Returns the mapQuestionContent.
     */
    public Map getMapQuestionContent() {
	return mapQuestionContent;
    }

    /**
     * @param mapQuestionContent
     *            The mapQuestionContent to set.
     */
    public void setMapQuestionContent(Map mapQuestionContent) {
	this.mapQuestionContent = mapQuestionContent;
    }
}
