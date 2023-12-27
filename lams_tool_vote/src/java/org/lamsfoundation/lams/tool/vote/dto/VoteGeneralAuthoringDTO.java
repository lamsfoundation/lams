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

/**
 * DTO that holds authoring properties for authoring jsps
 *
 * @author Ozgur Demirtas
 */
public class VoteGeneralAuthoringDTO implements Comparable<Object> {
    protected String exceptionMaxNominationInvalid;
    protected String toolContentID;
    protected String activityTitle;
    protected String activityInstructions;

    protected String useSelectLeaderToolOuput;
    protected String allowText;
    protected String showResults;
    protected String lockOnFinish;
    protected String maxNominationCount;
    protected String minNominationCount;

    protected String userExceptionMaxNominationInvalid;
    protected String userExceptionNoToolSessions;
    protected String userExceptionFilenameEmpty;
    protected String validationError;
    protected String userExceptionOptionsDuplicate;
    protected String httpSessionID;

    protected String contentFolderID;
    protected String editableQuestionText;

    protected String editableNominationText;
    protected String responseId;
    protected String currentUid;
    protected List<String> dataFlowObjectNames;

    public List<String> getDataFlowObjectNames() {
	return dataFlowObjectNames;
    }

    public void setDataFlowObjectNames(List<String> dataFlowObjectNames) {
	this.dataFlowObjectNames = dataFlowObjectNames;
    }

    /**
     * @return Returns the httpSessionID.
     */
    public String getHttpSessionID() {
	return httpSessionID;
    }

    /**
     * @param httpSessionID
     *            The httpSessionID to set.
     */
    public void setHttpSessionID(String httpSessionID) {
	this.httpSessionID = httpSessionID;
    }

    /**
     * @return Returns the maxNominationCount.
     */
    public String getMaxNominationCount() {
	return maxNominationCount;
    }

    /**
     * @param maxNominationCount
     *            The maxNominationCount to set.
     */
    public void setMaxNominationCount(String maxNominationCount) {
	this.maxNominationCount = maxNominationCount;
    }

    /**
     * @return Returns the minNominationCount.
     */
    public String getMinNominationCount() {
	return minNominationCount;
    }

    /**
     * @param minNominationCount
     *            The minNominationCount to set.
     */
    public void setMinNominationCount(String minNominationCount) {
	this.minNominationCount = minNominationCount;
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
     * @return Returns the exceptionMaxNominationInvalid.
     */
    public String getExceptionMaxNominationInvalid() {
	return exceptionMaxNominationInvalid;
    }

    /**
     * @param exceptionMaxNominationInvalid
     *            The exceptionMaxNominationInvalid to set.
     */
    public void setExceptionMaxNominationInvalid(String exceptionMaxNominationInvalid) {
	this.exceptionMaxNominationInvalid = exceptionMaxNominationInvalid;
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
     * @return Returns the useSelectLeaderToolOuput.
     */
    public String getUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    /**
     * @param useSelectLeaderToolOuput
     *            The useSelectLeaderToolOuput to set.
     */
    public void setUseSelectLeaderToolOuput(String useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the allowText.
     */
    public String getAllowText() {
	return allowText;
    }

    /**
     * @param allowText
     *            The allowText to set.
     */
    public void setAllowText(String allowText) {
	this.allowText = allowText;
    }

    /**
     * @return Returns the lockOnFinish.
     */
    public String getLockOnFinish() {
	return lockOnFinish;
    }

    /**
     * @param lockOnFinish
     *            The lockOnFinish to set.
     */
    public void setLockOnFinish(String lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    /**
     * @return Returns the userExceptionMaxNominationInvalid.
     */
    public String getUserExceptionMaxNominationInvalid() {
	return userExceptionMaxNominationInvalid;
    }

    /**
     * @param userExceptionMaxNominationInvalid
     *            The userExceptionMaxNominationInvalid to set.
     */
    public void setUserExceptionMaxNominationInvalid(String userExceptionMaxNominationInvalid) {
	this.userExceptionMaxNominationInvalid = userExceptionMaxNominationInvalid;
    }

    /**
     * @return Returns the userExceptionOptionsDuplicate.
     */
    public String getUserExceptionOptionsDuplicate() {
	return userExceptionOptionsDuplicate;
    }

    /**
     * @param userExceptionOptionsDuplicate
     *            The userExceptionOptionsDuplicate to set.
     */
    public void setUserExceptionOptionsDuplicate(String userExceptionOptionsDuplicate) {
	this.userExceptionOptionsDuplicate = userExceptionOptionsDuplicate;
    }

    /**
     * @return Returns the validationError.
     */
    public String getValidationError() {
	return validationError;
    }

    /**
     * @param validationError
     *            The validationError to set.
     */
    public void setValidationError(String validationError) {
	this.validationError = validationError;
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
     * @return Returns the userExceptionFilenameEmpty.
     */
    public String getUserExceptionFilenameEmpty() {
	return userExceptionFilenameEmpty;
    }

    /**
     * @param userExceptionFilenameEmpty
     *            The userExceptionFilenameEmpty to set.
     */
    public void setUserExceptionFilenameEmpty(String userExceptionFilenameEmpty) {
	this.userExceptionFilenameEmpty = userExceptionFilenameEmpty;
    }

    @Override
    public int compareTo(Object o) {
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = (VoteGeneralAuthoringDTO) o;

	if (voteGeneralAuthoringDTO == null) {
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
     * @return Returns the editableQuestionText.
     */
    public String getEditableQuestionText() {
	return editableQuestionText;
    }

    /**
     * @param editableQuestionText
     *            The editableQuestionText to set.
     */
    public void setEditableQuestionText(String editableQuestionText) {
	this.editableQuestionText = editableQuestionText;
    }

    /**
     * @return Returns the editableNominationText.
     */
    public String getEditableNominationText() {
	return editableNominationText;
    }

    /**
     * @param editableNominationText
     *            The editableNominationText to set.
     */
    public void setEditableNominationText(String editableNominationText) {
	this.editableNominationText = editableNominationText;
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

    public String getShowResults() {
	return showResults;
    }

    public void setShowResults(String showResults) {
	this.showResults = showResults;
    }
}
