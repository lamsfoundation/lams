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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.vote.web;


import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * <p> ActionForm for the Monitoring environment </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteMonitoringForm extends VoteAuthoringForm implements VoteAppConstants {
	// controls which method is called by the Lookup map */
    
	protected String method;
	
	protected String selectedToolSessionId;
	
	protected String isToolSessionChanged;
	
	protected String sessionUserCount;
	
	protected String completedSessionUserCount;
	
	protected String completedSessionUserPercent;
	
	protected String viewOpenVotes;
	
	protected String showOpenVotesSection;
	
	protected String closeOpenVotes;
	
	protected String hideOpenVote;
	
	protected String showOpenVote;
	
	protected String currentUid;
	
	
	
    /**
     * @return Returns the currentUid.
     */
    public String getCurrentUid() {
        return currentUid;
    }
    /**
     * @param currentUid The currentUid to set.
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
     * @param hideOpenVote The hideOpenVote to set.
     */
    public void setHideOpenVote(String hideOpenVote) {
        this.hideOpenVote = hideOpenVote;
    }
    /**
     * @return Returns the closeOpenVotes.
     */
    public String getCloseOpenVotes() {
        return closeOpenVotes;
    }
    /**
     * @param closeOpenVotes The closeOpenVotes to set.
     */
    public void setCloseOpenVotes(String closeOpenVotes) {
        this.closeOpenVotes = closeOpenVotes;
    }
    /**
     * @return Returns the viewOpenVotes.
     */
    public String getViewOpenVotes() {
        return viewOpenVotes;
    }
    /**
     * @param viewOpenVotes The viewOpenVotes to set.
     */
    public void setViewOpenVotes(String viewOpenVotes) {
        this.viewOpenVotes = viewOpenVotes;
    }
	/**
	 * @return Returns the isToolSessionChanged.
	 */
	public String getIsToolSessionChanged() {
		return isToolSessionChanged;
	}
	/**
	 * @param isToolSessionChanged The isToolSessionChanged to set.
	 */
	public void setIsToolSessionChanged(String isToolSessionChanged) {
		this.isToolSessionChanged = isToolSessionChanged;
	}
	/**
	 * @return Returns the selectedToolSessionId.
	 */
	public String getSelectedToolSessionId() {
		return selectedToolSessionId;
	}
	/**
	 * @param selectedToolSessionId The selectedToolSessionId to set.
	 */
	public void setSelectedToolSessionId(String selectedToolSessionId) {
		this.selectedToolSessionId = selectedToolSessionId;
	}
	
	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method The method to set.
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
     * @param completedSessionUserCount The completedSessionUserCount to set.
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
     * @param sessionUserCount The sessionUserCount to set.
     */
    public void setSessionUserCount(String sessionUserCount) {
        this.sessionUserCount = sessionUserCount;
    }
    /**
     * @return Returns the showOpenVotesSection.
     */
    public String getShowOpenVotesSection() {
        return showOpenVotesSection;
    }
    /**
     * @param showOpenVotesSection The showOpenVotesSection to set.
     */
    public void setShowOpenVotesSection(String showOpenVotesSection) {
        this.showOpenVotesSection = showOpenVotesSection;
    }
    /**
     * @return Returns the showOpenVote.
     */
    public String getShowOpenVote() {
        return showOpenVote;
    }
    /**
     * @param showOpenVote The showOpenVote to set.
     */
    public void setShowOpenVote(String showOpenVote) {
        this.showOpenVote = showOpenVote;
    }
    /**
     * @return Returns the completedSessionUserPercent.
     */
    public String getCompletedSessionUserPercent() {
        return completedSessionUserPercent;
    }
    /**
     * @param completedSessionUserPercent The completedSessionUserPercent to set.
     */
    public void setCompletedSessionUserPercent(
            String completedSessionUserPercent) {
        this.completedSessionUserPercent = completedSessionUserPercent;
    }
}
