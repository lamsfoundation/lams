package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_user_tool_session_state"
 *     
*/
public class UserToolSessionState implements Serializable {

    /** identifier field */
    private Integer userToolSessionStateId;

    /** persistent field */
    private String description;

    /** persistent field */
    private Set userToolSessions;

    /** full constructor */
    public UserToolSessionState(Integer userToolSessionStateId, String description, Set userToolSessions) {
        this.userToolSessionStateId = userToolSessionStateId;
        this.description = description;
        this.userToolSessions = userToolSessions;
    }

    /** default constructor */
    public UserToolSessionState() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Integer"
     *             column="user_tool_session_state_id"
     *         
     */
    public Integer getUserToolSessionStateId() {
        return this.userToolSessionStateId;
    }

    public void setUserToolSessionStateId(Integer userToolSessionStateId) {
        this.userToolSessionStateId = userToolSessionStateId;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="user_tool_session_state_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.lesson.UserToolSession"
     *         
     */
    public Set getUserToolSessions() {
        return this.userToolSessions;
    }

    public void setUserToolSessions(Set userToolSessions) {
        this.userToolSessions = userToolSessions;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userToolSessionStateId", getUserToolSessionStateId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof UserToolSessionState) ) return false;
        UserToolSessionState castOther = (UserToolSessionState) other;
        return new EqualsBuilder()
            .append(this.getUserToolSessionStateId(), castOther.getUserToolSessionStateId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserToolSessionStateId())
            .toHashCode();
    }

}
