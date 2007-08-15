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
package org.lamsfoundation.lams.tool.mc.pojos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Persistent  object/bean that defines the user for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_que_usr
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McQueUsr implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long queUsrId;

    /** nullable persistent field */
    private String username;

    /** nullable persistent field */
    private String fullname;
    
    private boolean responseFinalised;
    
    private boolean viewSummaryRequested;

    private Long mcSessionId;
    
    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McSession mcSession;

    /** persistent field */
    private Set mcUsrAttempts;

    private Integer lastAttemptOrder;
    
    private Integer lastAttemptTotalMark;
    
    /** full constructor */
    public McQueUsr(Long queUsrId, String username, String fullname,  org.lamsfoundation.lams.tool.mc.pojos.McSession mcSession, Set mcUsrAttempts) {
        this.queUsrId = queUsrId;
        this.username = username;
        this.fullname = fullname;
        this.mcSession = mcSession;
        this.mcUsrAttempts = mcUsrAttempts;
    }

    /**
     * @return Returns the responseFinalised.
     */
    public boolean isResponseFinalised() {
        return responseFinalised;
    }
    /**
     * @param responseFinalised The responseFinalised to set.
     */
    public void setResponseFinalised(boolean responseFinalised) {
        this.responseFinalised = responseFinalised;
    }
    /** default constructor */
    public McQueUsr() {
    }

    /** minimal constructor */
    public McQueUsr(Long queUsrId, Set mcUsrAttempts) {
        this.queUsrId = queUsrId;
        this.mcUsrAttempts = mcUsrAttempts;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getQueUsrId() {
        return this.queUsrId;
    }

    public void setQueUsrId(Long queUsrId) {
        this.queUsrId = queUsrId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    
    public org.lamsfoundation.lams.tool.mc.pojos.McSession getMcSession() {
        return this.mcSession;
    }

    public void setMcSession(org.lamsfoundation.lams.tool.mc.pojos.McSession mcSession) {
        this.mcSession = mcSession;
    }

    public Set getMcUsrAttempts() {
    	if (this.mcUsrAttempts == null)
        	setMcUsrAttempts(new HashSet());
        return this.mcUsrAttempts;
    }
    
    
    public void setMcUsrAttempts(Set mcUsrAttempts) {
        this.mcUsrAttempts = mcUsrAttempts;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .append("queUsrId", getQueUsrId())
            .append("username", getUsername())
            .append("fullname", getFullname())
            .append("responseFinalised", isResponseFinalised())
            .append("viewSummaryRequested", isViewSummaryRequested())
            .append("mcSessionId", getMcSessionId())
            .append("lastAttemptOrder", getLastAttemptOrder())
            .append("lastAttemptTotalMark", getLastAttemptTotalMark())
            .toString();
    }

	/**
	 * @return Returns the mcSessionId.
	 */
	public Long getMcSessionId() {
		return mcSessionId;
	}
	/**
	 * @param mcSessionId The mcSessionId to set.
	 */
	public void setMcSessionId(Long mcSessionId) {
		this.mcSessionId = mcSessionId;
	}
    /**
     * @return Returns the viewSummaryRequested.
     */
    public boolean isViewSummaryRequested() {
        return viewSummaryRequested;
    }
    /**
     * @param viewSummaryRequested The viewSummaryRequested to set.
     */
    public void setViewSummaryRequested(boolean viewSummaryRequested) {
        this.viewSummaryRequested = viewSummaryRequested;
    }

	public Integer getLastAttemptOrder() {
		return lastAttemptOrder;
	}

	public void setLastAttemptOrder(Integer lastAttemptOrder) {
		this.lastAttemptOrder = lastAttemptOrder;
	}

	public Integer getLastAttemptTotalMark() {
		return lastAttemptTotalMark;
	}

	public void setLastAttemptTotalMark(Integer lastAttemptTotalMark) {
		this.lastAttemptTotalMark = lastAttemptTotalMark;
	}
	
	/** Is the latest attempt a pass? True if and only if passmark is applicable for
	 * the related content and the user's lastAttemptTotalMark >= passmark. 
	 */
	public boolean isLastAttemptMarkPassed() {
		return isMarkPassed(lastAttemptTotalMark);
	}
	
	/** Does this mark count as a pass? True if and only if passmark is applicable for
	 * the related content and the given mark >= passmark. Used to calculate
	 * if the user has passed before setting up the learner's attempts. 
	 */
	public boolean isMarkPassed(Integer mark) {
		McContent content = mcSession.getMcContent();
		if ( mark != null && content.isPassMarkApplicable() ) {
			Integer passMark = content.getPassMark();
			return passMark!=null && (mark.compareTo(passMark) >= 0); 
		}
		return false;
	}
}
