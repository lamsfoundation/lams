package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/** 
 * @hibernate.class table="tl_lasbmt11_session"
*/
public class SubmitFilesSession implements Serializable,Cloneable{
	private static Logger log = Logger.getLogger(SubmitFilesSession.class);
	
    public final static int INCOMPLETE = 0;
    public final static int COMPLETED = 1;
    
	/** identifier field */
    private Long sessionID;

    /** persistent field */
    private Integer status;
	
	/** persistent field */
	private Set submissionDetails;

    /** full constructor */
    public SubmitFilesSession(Long sessionID,int status) {
    	this.sessionID = sessionID;
        this.status = new Integer(status);
    }

    /** default constructor */
    public SubmitFilesSession() {
    }

    /** 
     * @hibernate.id generator-class="identity" type="java.lang.Long" column="session_id"
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
			//clone SubmissionDetails object
			if(submissionDetails != null){
				Iterator iter = submissionDetails.iterator();
				Set set = new TreeSet();
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
	 * @hibernate.collection-one-to-many 
	 * class="org.lamsfoundation.lams.tool.sbmt.SubmissionDetails"
	 * @hibernate.collection-key column="session_id"
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
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
