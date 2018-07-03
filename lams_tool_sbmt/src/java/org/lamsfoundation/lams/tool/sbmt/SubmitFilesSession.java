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


package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @serial 3399851325547422573L
 */
public class SubmitFilesSession implements Serializable, Cloneable {

    private static final long serialVersionUID = 3399851325547422573L;

    private static Logger log = Logger.getLogger(SubmitFilesSession.class);

    public final static int INCOMPLETE = 0;
    public final static int COMPLETED = 1;

    /** identifier field */
    private Long sessionID;

    /** persistent field */
    private String sessionName;

    /** persistent field */
    private Integer status;

    /** persistent field */
    private Set submissionDetails;

    /** persistent field, but not cloned to avoid to clone block */
    private SubmitFilesContent content;

    private SubmitUser groupLeader;
    
    private boolean marksReleased;
    
    /** full constructor */
    public SubmitFilesSession(Long sessionID, int status) {
	this.sessionID = sessionID;
	this.status = new Integer(status);
    }

    /** default constructor */
    public SubmitFilesSession() {
    }

    /**
     *
     */
    public Long getSessionID() {
	return this.sessionID;
    }

    public void setSessionID(Long sessionID) {
	this.sessionID = sessionID;
    }

    /**
     *
     */
    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sessionID", getSessionID()).append("status", getStatus()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof SubmitFilesSession)) {
	    return false;
	}
	SubmitFilesSession castOther = (SubmitFilesSession) other;
	return new EqualsBuilder().append(this.getSessionID(), castOther.getSessionID())
		.append(this.getStatus(), castOther.getStatus()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getSessionID()).append(getStatus()).toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    //never clone key!
	    ((SubmitFilesSession) obj).setSessionID(null);
	    //clone SubmissionDetails object
	    if (submissionDetails != null) {
		Iterator iter = submissionDetails.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    set.add(((SubmissionDetails) iter.next()).clone());
		}
		((SubmitFilesSession) obj).submissionDetails = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SubmissionDetails.class + " failed");
	}
	return obj;
    }

    /**
     *
     *
     *
     *
     * @return Returns the submissionDetails.
     */
    public Set getSubmissionDetails() {
	return submissionDetails;
    }

    /**
     * @param submissionDetails
     *            The submissionDetails to set.
     */
    public void setSubmissionDetails(Set submissionDetails) {
	this.submissionDetails = submissionDetails;
    }

    /**
     *
     * 
     * @return Returns the content.
     */
    public SubmitFilesContent getContent() {
	return content;
    }

    /**
     * @param content
     *            The content to set.
     */
    public void setContent(SubmitFilesContent content) {
	this.content = content;
    }

    /**
     *
     * 
     * @return Returns the session name.
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * 
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }
    
    public SubmitUser getGroupLeader() {
   	return groupLeader;
    }

    public void setGroupLeader(SubmitUser groupLeader) {
   	this.groupLeader = groupLeader;
    }
    
    public boolean isMarksReleased() {
	return marksReleased;
    }

    public void setMarksReleased(boolean marksReleased) {
	this.marksReleased = marksReleased;
    }
}
