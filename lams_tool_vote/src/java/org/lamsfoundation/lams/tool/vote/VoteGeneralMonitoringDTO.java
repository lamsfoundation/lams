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
package org.lamsfoundation.lams.tool.vote;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds monitoring flow properties
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteGeneralMonitoringDTO implements Comparable {
    protected String currentMonitoringTab;
    protected String defineLaterInEditMode;
    protected String requestLearningReport;
    protected String userExceptionNoToolSessions;
    protected String userExceptionContentInUse;
    protected String userExceptionContentDoesNotExist;
    protected String userExceptionNoStudentActivity;
    protected String isMonitoredContentInUse;
    protected String monitoredContentInUse;

    protected String activeModule;
    protected String currentTab;
    protected String activityTitle;
    protected String activityInstructions;
    protected String defaultOptionContent;
    protected String countAllUsers;
    protected String countSessionComplete;
    protected String isPortfolioExport;
    protected String existsOpenVotes;
    protected String responseId;
    protected String currentUid;

    protected String toolContentID;
    protected Map mapOptionsContent;
    protected List listMonitoredAnswersContainerDto;
    protected List listUserEntries;

    protected List<SessionDTO> sessionDTOs;
    protected Map mapStandardNominationsContent;
    protected Map mapStandardNominationsHTMLedContent;
    protected Map mapStandardRatesContent;
    protected Map mapStandardUserCount;
    protected Map mapStandardQuestionUid;
    protected Map mapStandardToolSessionUid;

    protected String sessionUserCount;
    protected String completedSessionUserCount;
    protected List mapStudentsVoted;

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
     * @return Returns the activeModule.
     */
    public String getActiveModule() {
	return activeModule;
    }

    /**
     * @param activeModule
     *            The activeModule to set.
     */
    public void setActiveModule(String activeModule) {
	this.activeModule = activeModule;
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
     * @return Returns the defineLaterInEditMode.
     */
    public String getDefineLaterInEditMode() {
	return defineLaterInEditMode;
    }

    /**
     * @param defineLaterInEditMode
     *            The defineLaterInEditMode to set.
     */
    public void setDefineLaterInEditMode(String defineLaterInEditMode) {
	this.defineLaterInEditMode = defineLaterInEditMode;
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
     * @return Returns the isMonitoredContentInUse.
     */
    public String getIsMonitoredContentInUse() {
	return isMonitoredContentInUse;
    }

    /**
     * @param isMonitoredContentInUse
     *            The isMonitoredContentInUse to set.
     */
    public void setIsMonitoredContentInUse(String isMonitoredContentInUse) {
	this.isMonitoredContentInUse = isMonitoredContentInUse;
    }

    /**
     * @return Returns the mapOptionsContent.
     */
    public Map getMapOptionsContent() {
	return mapOptionsContent;
    }

    /**
     * @param mapOptionsContent
     *            The mapOptionsContent to set.
     */
    public void setMapOptionsContent(Map mapOptionsContent) {
	this.mapOptionsContent = mapOptionsContent;
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
     * @return Returns the isPortfolioExport.
     */
    public String getIsPortfolioExport() {
	return isPortfolioExport;
    }

    /**
     * @param isPortfolioExport
     *            The isPortfolioExport to set.
     */
    public void setIsPortfolioExport(String isPortfolioExport) {
	this.isPortfolioExport = isPortfolioExport;
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
     * @return Returns the existsOpenVotes.
     */
    public String getExistsOpenVotes() {
	return existsOpenVotes;
    }

    /**
     * @param existsOpenVotes
     *            The existsOpenVotes to set.
     */
    public void setExistsOpenVotes(String existsOpenVotes) {
	this.existsOpenVotes = existsOpenVotes;
    }

    /**
     * @return Returns the sessionDTOs.
     */
    public List<SessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    /**
     * @param sessionDTOs
     *            The sessionDTOs to set.
     */
    public void setSessionDTOs(List<SessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
    }

    /**
     * @return Returns the mapStandardNominationsContent.
     */
    public Map getMapStandardNominationsContent() {
	return mapStandardNominationsContent;
    }

    /**
     * @param mapStandardNominationsContent
     *            The mapStandardNominationsContent to set.
     */
    public void setMapStandardNominationsContent(Map mapStandardNominationsContent) {
	this.mapStandardNominationsContent = mapStandardNominationsContent;
    }

    /**
     * @return Returns the mapStandardNominationsHTMLedContent.
     */
    public Map getMapStandardNominationsHTMLedContent() {
	return mapStandardNominationsHTMLedContent;
    }

    /**
     * @param mapStandardNominationsHTMLedContent
     *            The mapStandardNominationsHTMLedContent to set.
     */
    public void setMapStandardNominationsHTMLedContent(Map mapStandardNominationsHTMLedContent) {
	this.mapStandardNominationsHTMLedContent = mapStandardNominationsHTMLedContent;
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
     * @return Returns the mapStandardRatesContent.
     */
    public Map getMapStandardRatesContent() {
	return mapStandardRatesContent;
    }

    /**
     * @param mapStandardRatesContent
     *            The mapStandardRatesContent to set.
     */
    public void setMapStandardRatesContent(Map mapStandardRatesContent) {
	this.mapStandardRatesContent = mapStandardRatesContent;
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
     * @return Returns the mapStandardUserCount.
     */
    public Map getMapStandardUserCount() {
	return mapStandardUserCount;
    }

    /**
     * @param mapStandardUserCount
     *            The mapStandardUserCount to set.
     */
    public void setMapStandardUserCount(Map mapStandardUserCount) {
	this.mapStandardUserCount = mapStandardUserCount;
    }

    /**
     * @param mapStudentsVoted
     *            The mapStudentsVoted to set.
     */
    public void setMapStudentsVoted(List mapStudentsVoted) {
	this.mapStudentsVoted = mapStudentsVoted;
    }

    /**
     * @return Returns the mapStudentsVoted.
     */
    public List getMapStudentsVoted() {
	return mapStudentsVoted;
    }

    /**
     * @return Returns the currentUid.
     */
    public String getCurrentUid() {
	return currentUid;
    }

    /**
     * @param currentUid
     *            The currentUid to set.
     */
    public void setCurrentUid(String currentUid) {
	this.currentUid = currentUid;
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

    public String toString() {
	return new ToStringBuilder(this).append("toolContentID: ", toolContentID)
		.append("activeModule: ", activeModule).append("monitoredContentInUse: ", monitoredContentInUse)
		.append("currentMonitoringTab: ", currentMonitoringTab).append("currentTab: ", currentTab)
		.append("requestLearningReport: ", requestLearningReport)
		.append("userExceptionNoToolSessions: ", userExceptionNoToolSessions)
		.append("userExceptionContentDoesNotExist: ", userExceptionContentDoesNotExist)
		.append("userExceptionNoStudentActivity: ", userExceptionNoStudentActivity)
		.append("isMonitoredContentInUse: ", isMonitoredContentInUse).append("activityTitle: ", activityTitle)
		.append("activityInstructions: ", activityInstructions)
		.append("userExceptionContentInUse: ", userExceptionContentInUse)
		.append("defaultOptionContent: ", defaultOptionContent).append("countAllUsers: ", countAllUsers)
		.append("countSessionComplete: ", countSessionComplete)
		.append("isPortfolioExport: ", isPortfolioExport)
		.append("listMonitoredAnswersContainerDto: ", listMonitoredAnswersContainerDto)
		.append("listUserEntries: ", listUserEntries).append("existsOpenVotes: ", existsOpenVotes)
		.append("sessionDTOs: ", sessionDTOs).append("mapStudentsVoted: ", mapStudentsVoted)
		.append("responseId: ", responseId).append("currentUid: ", currentUid)
		.append("sessionUserCount: ", sessionUserCount)
		.append("completedSessionUserCount: ", completedSessionUserCount).toString();
    }

    public int compareTo(Object o) {
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = (VoteGeneralMonitoringDTO) o;

	if (voteGeneralMonitoringDTO == null)
	    return 1;
	else
	    return 0;
    }

    /**
     * @return Returns the monitoredContentInUse.
     */
    public String getMonitoredContentInUse() {
	return monitoredContentInUse;
    }

    /**
     * @param monitoredContentInUse
     *            The monitoredContentInUse to set.
     */
    public void setMonitoredContentInUse(String monitoredContentInUse) {
	this.monitoredContentInUse = monitoredContentInUse;
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
