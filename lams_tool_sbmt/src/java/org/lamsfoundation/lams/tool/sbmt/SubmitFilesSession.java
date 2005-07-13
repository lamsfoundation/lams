package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/** 
 * @hibernate.class table="tl_lasbmt11_session"
*/
public class SubmitFilesSession implements Serializable{
	private static Logger log = Logger.getLogger(SubmitFilesSession.class);
	
    public final static int INCOMPLETE = 0;
    public final static int COMPLETED = 1;
    
	/** identifier field */
    private Long sessionID;

    /** persistent field */
    private Integer status;

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
	protected Object clone() throws CloneNotSupportedException {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmissionDetails.class + " failed");
		}
		return obj;
	}
}
