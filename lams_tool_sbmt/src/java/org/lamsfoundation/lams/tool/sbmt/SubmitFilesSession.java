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
 * @hibernate.class table="tl_lasbmt11_session"
 * @serial 3399851325547422573L
*/
public class SubmitFilesSession implements Serializable,Cloneable{

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

	/** persistent field */
	private Set learners;

	/** persistent field, but not cloned to avoid to clone block*/
	private SubmitFilesContent content;
	
    /** full constructor */
    public SubmitFilesSession(Long sessionID,int status) {
    	this.sessionID = sessionID;
        this.status = new Integer(status);
    }

    /** default constructor */
    public SubmitFilesSession() {
    }

    /** 
     * @hibernate.id generator-class="assigned" type="java.lang.Long" column="session_id"
     */
    public Long getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(Long sessionID) {
        this.sessionID = sessionID;
    }

    /** 
     * @hibernate.property column="status" length="11" not-null="true" 
     */
    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String toString() {
        return new ToStringBuilder(this)
            .append("sessionID", getSessionID())
            .append("status", getStatus())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof SubmitFilesSession) ) return false;
        SubmitFilesSession castOther = (SubmitFilesSession) other;
        return new EqualsBuilder()
            .append(this.getSessionID(), castOther.getSessionID())
            .append(this.getStatus(), castOther.getStatus())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSessionID())
            .append(getStatus())
            .toHashCode();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()  {
		Object obj = null;
		try {
			obj = super.clone();
			//never clone key!
			((SubmitFilesSession)obj).setSessionID(null);
			//clone SubmissionDetails object
			if(submissionDetails != null){
				Iterator iter = submissionDetails.iterator();
				Set set = new HashSet();
				while(iter.hasNext())
					set.add(((SubmissionDetails)iter.next()).clone());
				((SubmitFilesSession)obj).submissionDetails = set;
			}
			
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmissionDetails.class + " failed");
		}
		return obj;
	}
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
	 * @hibernate.collection-one-to-many 
	 * class="org.lamsfoundation.lams.tool.sbmt.SubmissionDetails"
	 * @hibernate.collection-key column="session_id"
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
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
	 * @hibernate.collection-one-to-many 
	 * class="org.lamsfoundation.lams.tool.sbmt.Learner"
	 * @hibernate.collection-key column="session_id"
	 * 
	 * @return Returns the learners.
	 */
	public Set getLearners() {
		return learners;
	}
	/**
	 * @param submissionDetails The submissionDetails to set.
	 */
	public void setLearners(Set learners) {
		this.learners = learners;
	}
	/**
	 * @hibernate.many-to-one column="content_id" cascade="none"
	 * 
	 * @return Returns the content.
	 */
	public SubmitFilesContent getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(SubmitFilesContent content) {
		this.content = content;
	}
	
	/**
	 * @hibernate.property column="session_name" length="250"
	 * 
	 * @return Returns the session name.
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * 
	 * @param sessionName The session name to set.
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
