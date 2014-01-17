/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dto;


import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p> DTO that holds learner flow decision properties and some other view-only properties
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class GeneralMonitoringDTO implements Comparable
{
    protected String userExceptionNoToolSessions;
    protected String userExceptionNoStudentActivity;
    protected String userExceptionContentInUse;
    protected String monitoredContentInUse;
    
    protected String defineLaterInEditMode;
    
    protected String countAllUsers;
    protected String countSessionComplete;
    
    protected String activityTitle;
    protected String activityInstructions;
    
    protected String editResponse;
    
    protected String defaultQuestionContent;
    
    protected String contentFolderID;
    
	
	public String toString() {
        return new ToStringBuilder(this)
            .append("userExceptionNoToolSessions: ", userExceptionNoToolSessions)
            .append("userExceptionNoStudentActivity: ", userExceptionNoStudentActivity)
            .append("userExceptionContentInUse: ", userExceptionContentInUse)
            .append("defineLaterInEditMode: ", defineLaterInEditMode)
            .append("monitoredContentInUse: ", monitoredContentInUse)
            .append("activityTitle: ", activityTitle)
            .append("activityInstructions: ", activityInstructions)
            .append("editResponse: ", editResponse)
            .append("defaultQuestionContent: ", defaultQuestionContent)
            .append("countAllUsers: ", countAllUsers)
            .append("countSessionComplete: ", countSessionComplete)      
            .toString();
    }

	public int compareTo(Object o)
    {
	    GeneralMonitoringDTO generalMonitoringDTO = (GeneralMonitoringDTO) o;
     
        if (generalMonitoringDTO == null)
        	return 1;
		else
			return 0;
    }

	
    /**
     * @return Returns the defineLaterInEditMode.
     */
    public String getDefineLaterInEditMode() {
        return defineLaterInEditMode;
    }
    /**
     * @param defineLaterInEditMode The defineLaterInEditMode to set.
     */
    public void setDefineLaterInEditMode(String defineLaterInEditMode) {
        this.defineLaterInEditMode = defineLaterInEditMode;
    }
    /**
     * @return Returns the monitoredContentInUse.
     */
    public String getMonitoredContentInUse() {
        return monitoredContentInUse;
    }
    /**
     * @param monitoredContentInUse The monitoredContentInUse to set.
     */
    public void setMonitoredContentInUse(String monitoredContentInUse) {
        this.monitoredContentInUse = monitoredContentInUse;
    }
    /**
     * @return Returns the userExceptionContentInUse.
     */
    public String getUserExceptionContentInUse() {
        return userExceptionContentInUse;
    }
    /**
     * @param userExceptionContentInUse The userExceptionContentInUse to set.
     */
    public void setUserExceptionContentInUse(String userExceptionContentInUse) {
        this.userExceptionContentInUse = userExceptionContentInUse;
    }
    /**
     * @return Returns the editResponse.
     */
    public String getEditResponse() {
        return editResponse;
    }
    /**
     * @param editResponse The editResponse to set.
     */
    public void setEditResponse(String editResponse) {
        this.editResponse = editResponse;
    }
    /**
     * @return Returns the defaultQuestionContent.
     */
    public String getDefaultQuestionContent() {
        return defaultQuestionContent;
    }
    /**
     * @param defaultQuestionContent The defaultQuestionContent to set.
     */
    public void setDefaultQuestionContent(String defaultQuestionContent) {
        this.defaultQuestionContent = defaultQuestionContent;
    }
    /**
     * @return Returns the userExceptionNoStudentActivity.
     */
    public String getUserExceptionNoStudentActivity() {
        return userExceptionNoStudentActivity;
    }
    /**
     * @param userExceptionNoStudentActivity The userExceptionNoStudentActivity to set.
     */
    public void setUserExceptionNoStudentActivity(
            String userExceptionNoStudentActivity) {
        this.userExceptionNoStudentActivity = userExceptionNoStudentActivity;
    }
    /**
     * @return Returns the countAllUsers.
     */
    public String getCountAllUsers() {
        return countAllUsers;
    }
    /**
     * @param countAllUsers The countAllUsers to set.
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
     * @param countSessionComplete The countSessionComplete to set.
     */
    public void setCountSessionComplete(String countSessionComplete) {
        this.countSessionComplete = countSessionComplete;
    }
    /**
     * @return Returns the userExceptionNoToolSessions.
     */
    public String getUserExceptionNoToolSessions() {
        return userExceptionNoToolSessions;
    }
    /**
     * @param userExceptionNoToolSessions The userExceptionNoToolSessions to set.
     */
    public void setUserExceptionNoToolSessions(
            String userExceptionNoToolSessions) {
        this.userExceptionNoToolSessions = userExceptionNoToolSessions;
    }
    /**
     * @return Returns the activityInstructions.
     */
    public String getActivityInstructions() {
        return activityInstructions;
    }
    /**
     * @param activityInstructions The activityInstructions to set.
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
     * @param activityTitle The activityTitle to set.
     */
    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }
    /**
     * @return Returns the contentFolderID.
     */
    public String getContentFolderID() {
        return contentFolderID;
    }
    /**
     * @param contentFolderID The contentFolderID to set.
     */
    public void setContentFolderID(String contentFolderID) {
        this.contentFolderID = contentFolderID;
    }
}
