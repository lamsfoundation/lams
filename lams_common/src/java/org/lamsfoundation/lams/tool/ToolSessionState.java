package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class table="lams_tool_session_state"
 *     
 */
public class ToolSessionState implements Serializable {

    /** identifier field */
    private Integer toolSessionStateId;

    /** persistent field */
    private String description;

    /** persistent field */
    private Set toolSessions;

    //---------------------------------------------------------------------
    // Class level constants
    //---------------------------------------------------------------------
    public static final String NOT_ATTEMPTED="NOT ATTEMPTED";
    
    public static final String INCOMPLETE="INCOMPLETE";
    
    public static final String COMPLETED="COMPLETED";
    
    /** full constructor */
    public ToolSessionState(Integer toolSessionStateId, String description, Set toolSessions) {
        this.toolSessionStateId = toolSessionStateId;
        this.description = description;
        this.toolSessions = toolSessions;
    }

    /** default constructor */
    public ToolSessionState() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Integer"
     *             column="tool_session_state_id"
     *         
     */
    public Integer getToolSessionStateId() {
        return this.toolSessionStateId;
    }

    public void setToolSessionStateId(Integer toolSessionStateId) {
        this.toolSessionStateId = toolSessionStateId;
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
     *             column="tool_session_state_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.ToolSession"
     *         
     */
    public Set getToolSessions() {
        return this.toolSessions;
    }

    public void setToolSessions(Set toolSessions) {
        this.toolSessions = toolSessions;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("toolSessionStateId", getToolSessionStateId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ToolSessionState) ) return false;
        ToolSessionState castOther = (ToolSessionState) other;
        return new EqualsBuilder()
            .append(this.getToolSessionStateId(), castOther.getToolSessionStateId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getToolSessionStateId())
            .toHashCode();
    }

}
