package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class table="tl_lasbmt11_session"
*/
public class SubmitFilesSession implements Serializable {
    
	/** identifier field */
    private Long sessionID;

    /** persistent field */
    private Integer status;

    /** persistent field */
    private SubmitFilesContent contentID;

    /** full constructor */
    public SubmitFilesSession(Integer status, SubmitFilesContent contentID) {
        this.status = status;
        this.contentID = contentID;
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

    /** 
     * @hibernate.many-to-one not-null="true" 
     * @hibernate.column name="content_id"         
     *         
     */
    public SubmitFilesContent getContentID() {
        return this.contentID;
    }

    public void setContentID(SubmitFilesContent contentID) {
        this.contentID = contentID;
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
            .append(this.getContentID(), castOther.getContentID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSessionID())
            .append(getStatus())
            .append(getContentID())
            .toHashCode();
    }

}
