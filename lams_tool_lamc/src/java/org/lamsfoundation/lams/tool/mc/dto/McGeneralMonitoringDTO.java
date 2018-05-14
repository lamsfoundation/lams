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

package org.lamsfoundation.lams.tool.mc.dto;

/**
 * <p>
 * DTO that holds monitoring flow properties
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McGeneralMonitoringDTO {

    protected String activityTitle;
    protected String activityInstructions;
    protected Integer countAllUsers;
    protected Integer countSessionComplete;
    protected String displayAnswers;
    private String displayFeedbackOnly;
    protected String toolContentID;

    protected String contentFolderID;

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

    public String getDisplayFeedbackOnly() {
	return displayFeedbackOnly;
    }

    public void setDisplayFeedbackOnly(String displayFeedbackOnly) {
	this.displayFeedbackOnly = displayFeedbackOnly;
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
