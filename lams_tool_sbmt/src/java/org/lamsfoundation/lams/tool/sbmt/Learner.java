/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
/**
 * 
 * @hibernate.class table="tl_lasbmt11_session_learners"
 * @author Steve.Ni
 * 
 * $version$
 * @serial 4951104689120529660L;
 */
public class Learner implements Serializable,Cloneable{

	private static final long serialVersionUID = 4951104689120529660L;
	private static Logger log = Logger.getLogger(Learner.class);
	
    /** identifier field */
    private Long learnerID;

	/** persistent field */
	private Long userID;
	
	/** persistent field */
	private Long sessionID;
	
	/** persistent field */
	private boolean finished;
	
	private Set submissionDetails;
	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long" column="learner_id"
	 * @return Returns the learnerID.
	 */
	public Long getLearnerID() {
		return learnerID;
	}
	/**
	 * @param learnerID The learnerID to set.
	 */
	public void setLearnerID(Long learnerID) {
		this.learnerID = learnerID;
	}

	/**
	 * @hibernate.property column="user_id" length="20"
	 * @return Returns the userID.
	 */
	public Long getUserID() {
		return userID;
	}
	/**
	 * @param userID
	 *            The userID to set.
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	/**
	 * @hibernate.property column="finished" length="1"
	 * @return Returns the finished.
	 */
	public boolean isFinished() {
		return finished;
	}
	/**
	 * @param finished The finished to set.
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmitFilesReport.class + " failed");
		}
		return obj;
	}
	/**
	 * @hibernate.property column="session_id" length="20"
	 * @return Returns the sessionID.
	 */
	public Long getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID The sessionID to set.
	 */
	public void setSessionID(Long sessionID) {
		this.sessionID = sessionID;
	}
	
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="learner_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.sbmt.SubmissionDetails"
	 *  
	 * @return Returns the submissionDetails.
	 */
	public Set getSubmissionDetails() {
		return submissionDetails;
	}
	/**
	 * @param submissionDetails The submissionDetails to set.
	 */
	public void setSubmissionDetails(Set submissionDetails) {
		this.submissionDetails = submissionDetails;
	}
}
