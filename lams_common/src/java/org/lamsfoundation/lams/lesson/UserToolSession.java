package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_user_tool_session"
 *     
*/
public class UserToolSession implements Serializable {

    /** identifier field */
    private org.lamsfoundation.lams.lesson.UserToolSessionPK comp_id;

    /** nullable persistent field */
    private User user;

    /** nullable persistent field */
    private ToolSession toolSession;

    /** persistent field */
    private org.lamsfoundation.lams.lesson.UserToolSessionState userToolSessionState;

    /** full constructor */
    public UserToolSession(org.lamsfoundation.lams.lesson.UserToolSessionPK comp_id, User user, ToolSession toolSession, org.lamsfoundation.lams.lesson.UserToolSessionState userToolSessionState) {
        this.comp_id = comp_id;
        this.user = user;
        this.toolSession = toolSession;
        this.userToolSessionState = userToolSessionState;
    }

    /** default constructor */
    public UserToolSession() {
    }

    /** minimal constructor */
    public UserToolSession(org.lamsfoundation.lams.lesson.UserToolSessionPK comp_id, org.lamsfoundation.lams.lesson.UserToolSessionState userToolSessionState) {
        this.comp_id = comp_id;
        this.userToolSessionState = userToolSessionState;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *         
     */
    public org.lamsfoundation.lams.lesson.UserToolSessionPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(org.lamsfoundation.lams.lesson.UserToolSessionPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="user_id"
     *         
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="tool_session_id"
     *         
     */
    public ToolSession getToolSession() {
        return this.toolSession;
    }

    public void setToolSession(ToolSession toolSession) {
        this.toolSession = toolSession;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="user_tool_session_state_id"         
     *         
     */
    public org.lamsfoundation.lams.lesson.UserToolSessionState getUserToolSessionState() {
        return this.userToolSessionState;
    }

    public void setUserToolSessionState(org.lamsfoundation.lams.lesson.UserToolSessionState userToolSessionState) {
        this.userToolSessionState = userToolSessionState;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof UserToolSession) ) return false;
        UserToolSession castOther = (UserToolSession) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
