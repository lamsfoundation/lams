package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_tool_content"
 *     
*/
public class ToolContent implements Serializable {

    /** identifier field */
    private Long toolContentId;

    /** persistent field */
    private long toolContentKey;

    /** persistent field */
    private Set activities;

    /** full constructor */
    public ToolContent(Long toolContentId, long toolContentKey, Set activities) {
        this.toolContentId = toolContentId;
        this.toolContentKey = toolContentKey;
        this.activities = activities;
    }

    /** default constructor */
    public ToolContent() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="tool_content_id"
     *         
     */
    public Long getToolContentId() {
        return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
        this.toolContentId = toolContentId;
    }

    /** 
     *            @hibernate.property
     *             column="tool_content_key"
     *             length="20"
     *             not-null="true"
     *         
     */
    public long getToolContentKey() {
        return this.toolContentKey;
    }

    public void setToolContentKey(long toolContentKey) {
        this.toolContentKey = toolContentKey;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="tool_content_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.learningdesign.Activity"
     *         
     */
    public Set getActivities() {
        return this.activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("toolContentId", getToolContentId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ToolContent) ) return false;
        ToolContent castOther = (ToolContent) other;
        return new EqualsBuilder()
            .append(this.getToolContentId(), castOther.getToolContentId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getToolContentId())
            .toHashCode();
    }

}
