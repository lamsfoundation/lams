package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_tool_session"
 *     
*/
public abstract class ToolSession implements Serializable {
    
    /** Tool session type id for grouped */
    public static final int GROUPED_TYPE = 1;
    /** Tool session type id for non-grouped */
    public static final int NON_GROUPED_TYPE = 2;
    
    /** Tool session state id for started tool session */
    public static final int STARTED_STATE = 1;
    /** Tool session state id for completed tool session */
    public static final int ENDED_STATE = 2;

    public static final String UNIQUE_KEY_PREFIX = "uq_";
    /** identifier field */
    private Long toolSessionId;

    /** persistent field */
    private ToolActivity toolActivity;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private int toolSessionStateId;
    
    private String uniqueKey;
    
    private Lesson lesson;

    /** full constructor */
    public ToolSession(Long toolSessionId, 
                       ToolActivity toolActivity, 
                       Date createDateTime, 
                       int toolSessionStateId) {
        this.toolSessionId = toolSessionId;
        this.toolActivity = toolActivity;
        this.createDateTime = createDateTime;
        this.toolSessionStateId = toolSessionStateId;
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
     * 
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="activity_id"      
     */
    public ToolActivity getToolActivity() {
        return this.toolActivity;
    }

    public void setToolActivity(ToolActivity toolActivity) {
        this.toolActivity = toolActivity;
    }

    /** 
     * @hibernate.property column="create_date_time" length="19"
     *             		   not-null="true"
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }
    /**
     * @hibernate.property column="unique_key" length="128"
     *             not-null="true"
     * @return Returns the uniqueKey.
     */
    public String getUniqueKey()
    {
        return uniqueKey;
    }
    /**
     * @param uniqueKey The uniqueKey to set.
     */
    public void setUniqueKey(String uniqueKey)
    {
        this.uniqueKey = uniqueKey;
    }
    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="tool_session_state_id"         
     *         
     */
    public int getToolSessionStateId() {
        return this.toolSessionStateId;
    }

    public void setToolSessionStateId(int toolSessionStateId) {
        this.toolSessionStateId = toolSessionStateId;
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

	public int getToolSessionTypeId() {
	    if(this instanceof NonGroupedToolSession)
	        return NON_GROUPED_TYPE;
	    else
	        return GROUPED_TYPE;
	}
    public Lesson getLesson() {
        return lesson;
    }
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
