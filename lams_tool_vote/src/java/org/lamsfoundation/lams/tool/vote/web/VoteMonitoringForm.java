/***************************************************************************
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
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.vote.web;

import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * <p>
 * ActionForm for the Monitoring environment
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteMonitoringForm extends VoteAuthoringForm implements VoteAppConstants {
    // controls which method is called by the Lookup map */

    protected String method;

    protected String sessionUserCount;

    protected String completedSessionUserCount;

    protected String hideOpenVote;

    protected String showOpenVote;

    protected String currentUid;

    protected String existsOpenVotes;

    protected String toolContentID;

    protected String defineLaterInEditMode;

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
     * @return Returns the hideOpenVote.
     */
    public String getHideOpenVote() {
	return hideOpenVote;
    }

    /**
     * @param hideOpenVote
     *            The hideOpenVote to set.
     */
    public void setHideOpenVote(String hideOpenVote) {
	this.hideOpenVote = hideOpenVote;
    }

    /**
     * @return Returns the method.
     */
    public String getMethod() {
	return method;
    }

    /**
     * @param method
     *            The method to set.
     */
    public void setMethod(String method) {
	this.method = method;
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

    /**
     * @return Returns the showOpenVote.
     */
    public String getShowOpenVote() {
	return showOpenVote;
    }

    /**
     * @param showOpenVote
     *            The showOpenVote to set.
     */
    public void setShowOpenVote(String showOpenVote) {
	this.showOpenVote = showOpenVote;
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
}
