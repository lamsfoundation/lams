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



package org.lamsfoundation.lams.tool.kaltura.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 */
public class AuthoringForm extends ActionForm {

    private static final long serialVersionUID = 3950453134542135495L;

    // Properties

    private String title;

    private String instructions;

    private boolean lockOnFinished;

    private boolean allowContributeVideos;

    private boolean allowSeeingOtherUsersRecordings;

    private int learnerContributionLimit;

    private boolean allowComments;

    private boolean allowRatings;

    private String currentTab;

    private String dispatch;

    private String sessionMapID;

    private Long deleteFileUuid;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private SessionMap sessionMap;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));

	return ac;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    public String getDispatch() {
	return dispatch;
    }

    public void setDispatch(String dispatch) {
	this.dispatch = dispatch;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public boolean isAllowContributeVideos() {
	return allowContributeVideos;
    }

    public void setAllowContributeVideos(boolean allowContributeVideos) {
	this.allowContributeVideos = allowContributeVideos;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setSessionMap(SessionMap sessionMap) {
	this.sessionMap = sessionMap;
    }

    public SessionMap getSessionMap() {
	return sessionMap;
    }

    public Long getDeleteFileUuid() {
	return deleteFileUuid;
    }

    public void setDeleteFileUuid(Long deleteFile) {
	this.deleteFileUuid = deleteFile;
    }

    public boolean isAllowSeeingOtherUsersRecordings() {
	return allowSeeingOtherUsersRecordings;
    }

    public void setAllowSeeingOtherUsersRecordings(boolean allowSeeingOtherUsersRecordings) {
	this.allowSeeingOtherUsersRecordings = allowSeeingOtherUsersRecordings;
    }

    public int getLearnerContributionLimit() {
	return learnerContributionLimit;
    }

    public void setLearnerContributionLimit(int learnerContributionLimit) {
	this.learnerContributionLimit = learnerContributionLimit;
    }

    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }

    public boolean isAllowRatings() {
	return allowRatings;
    }

    public void setAllowRatings(boolean allowRatings) {
	this.allowRatings = allowRatings;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }
}
