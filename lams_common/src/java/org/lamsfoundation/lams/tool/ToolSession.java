package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.learningdesign.Group;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_tool_session"
 *     
*/
public class ToolSession implements Serializable {

    /** identifier field */
    private Long toolSessionId;

    /** persistent field */
    private long activityId;

    /** persistent field */
    private long toolSessionKey;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private Group group;

    /** persistent field */
    private org.lamsfoundation.lams.tool.ToolSessionState toolSessionState;

    /** persistent field */
    private Set userToolSessions;

    /** full constructor */
    public ToolSession(Long toolSessionId, long activityId, long toolSessionKey, Date createDateTime, Group group, org.lamsfoundation.lams.tool.ToolSessionState toolSessionState, Set userToolSessions) {
        this.toolSessionId = toolSessionId;
        this.activityId = activityId;
        this.toolSessionKey = toolSessionKey;
        this.createDateTime = createDateTime;
        this.group = group;
        this.toolSessionState = toolSessionState;
        this.userToolSessions = userToolSessions;
    }

    /** default constructor */
    public ToolSession() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="tool_session_id"
     *         
     */
    public Long getToolSessionId() {
        return this.toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
        this.toolSessionId = toolSessionId;
    }

    /** 
     *            @hibernate.property
     *             column="activity_id"
     *             length="20"
     *             not-null="true"
     *         
     */
    public long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    /** 
     *            @hibernate.property
     *             column="tool_session_key"
     *             length="20"
     *             not-null="true"
     *         
     */
    public long getToolSessionKey() {
        return this.toolSessionKey;
    }

    public void setToolSessionKey(long toolSessionKey) {
        this.toolSessionKey = toolSessionKey;
    }

    /** 
     *            @hibernate.property
     *             column="create_date_time"
     *             length="19"
     *             not-null="true"
     *         
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="group_id"         
     *         
     */
    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="tool_session_state_id"         
     *         
     */
    public org.lamsfoundation.lams.tool.ToolSessionState getToolSessionState() {
        return this.toolSessionState;
    }

    public void setToolSessionState(org.lamsfoundation.lams.tool.ToolSessionState toolSessionState) {
        this.toolSessionState = toolSessionState;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="tool_session_id"
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
            .append("toolSessionId", getToolSessionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ToolSession) ) return false;
        ToolSession castOther = (ToolSession) other;
        return new EqualsBuilder()
            .append(this.getToolSessionId(), castOther.getToolSessionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getToolSessionId())
            .toHashCode();
    }

}
