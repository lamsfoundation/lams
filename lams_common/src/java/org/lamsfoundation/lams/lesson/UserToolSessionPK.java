package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class UserToolSessionPK implements Serializable {

    /** identifier field */
    private Long toolSessionId;

    /** identifier field */
    private Long userId;

    /** full constructor */
    public UserToolSessionPK(Long toolSessionId, Long userId) {
        this.toolSessionId = toolSessionId;
        this.userId = userId;
    }

    /** default constructor */
    public UserToolSessionPK() {
    }

    /** 
     *                @hibernate.property
     *                 column="tool_session_id"
     *                 length="20"
     *             
     */
    public Long getToolSessionId() {
        return this.toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
        this.toolSessionId = toolSessionId;
    }

    /** 
     *                @hibernate.property
     *                 column="user_id"
     *                 length="20"
     *             
     */
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("toolSessionId", getToolSessionId())
            .append("userId", getUserId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof UserToolSessionPK) ) return false;
        UserToolSessionPK castOther = (UserToolSessionPK) other;
        return new EqualsBuilder()
            .append(this.getToolSessionId(), castOther.getToolSessionId())
            .append(this.getUserId(), castOther.getUserId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getToolSessionId())
            .append(getUserId())
            .toHashCode();
    }

}
