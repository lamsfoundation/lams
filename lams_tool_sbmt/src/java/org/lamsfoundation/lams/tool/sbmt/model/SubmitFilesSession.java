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

package org.lamsfoundation.lams.tool.sbmt.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

@Entity
@Table(name = "tl_lasbmt11_session")
public class SubmitFilesSession implements Serializable, Cloneable {
    private static final long serialVersionUID = 3399851325547422573L;
    private static Logger log = Logger.getLogger(SubmitFilesSession.class);

    public final static int INCOMPLETE = 0;
    public final static int COMPLETED = 1;

    @Id
    @Column(name = "session_id")
    private Long sessionID;

    @Column(name = "session_name")
    private String sessionName;

    @Column
    private Integer status;

    @Column(name = "marks_released")
    private boolean marksReleased;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "submitFileSession")
    private Set<SubmissionDetails> submissionDetails = new HashSet<>();

    /** persistent field, but not cloned to avoid to clone block */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private SubmitFilesContent content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_leader_uid")
    private SubmitUser groupLeader;

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

    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    //never clone key!
	    ((SubmitFilesSession) obj).setSessionID(null);
	    //clone SubmissionDetails object
	    if (submissionDetails != null) {
		Iterator<SubmissionDetails> iter = submissionDetails.iterator();
		Set<SubmissionDetails> set = new HashSet<>();
		while (iter.hasNext()) {
		    set.add((SubmissionDetails) (iter.next()).clone());
		}
		((SubmitFilesSession) obj).submissionDetails = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SubmissionDetails.class + " failed");
	}
	return obj;
    }

    // ***********************************************************
    // Get / Set methods
    // ***********************************************************

    public Long getSessionID() {
	return this.sessionID;
    }

    public void setSessionID(Long sessionID) {
	this.sessionID = sessionID;
    }

    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
     * @return Returns the submissionDetails.
     */
    public Set<SubmissionDetails> getSubmissionDetails() {
	return submissionDetails;
    }

    /**
     * @param submissionDetails
     *            The submissionDetails to set.
     */
    public void setSubmissionDetails(Set<SubmissionDetails> submissionDetails) {
	this.submissionDetails = submissionDetails;
    }

    /**
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
     * @return Returns the session name.
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
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
