package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_group"
 *     
*/
public class Group implements Serializable {

    /** identifier field */
    private Long groupId;

    /** persistent field */
    private int orderId;

    /** persistent field */
    private org.lamsfoundation.lams.learningdesign.Grouping grouping;

    /** persistent field */
    private Set users;

    /** persistent field */
    private Set toolSessions;

    /** full constructor */
    public Group(Long groupId, int orderId, org.lamsfoundation.lams.learningdesign.Grouping grouping, Set userGroups, Set toolSessions) {
        this.groupId = groupId;
        this.orderId = orderId;
        this.grouping = grouping;
        this.users = userGroups;
        this.toolSessions = toolSessions;
    }

    /** default constructor */
    public Group() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="group_id"
     *         
     */
    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /** 
     *            @hibernate.property
     *             column="order_id"
     *             length="6"
     *             not-null="true"
     *         
     */
    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="grouping_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.Grouping getGrouping() {
        return this.grouping;
    }

    public void setGrouping(org.lamsfoundation.lams.learningdesign.Grouping grouping) {
        this.grouping = grouping;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * 				  table = "lams_user_group"
     * @hibernate.collection-key column="group_id"
     * @hibernate.collection-many-to-many
     *            class="org.lamsfoundation.lams.usermanagement.User"
     */
    public Set getUsers() {
        return this.users;
    }

    public void setUsers(Set userGroups) {
        this.users = userGroups;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="group_id"
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
            .append("groupId", getGroupId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Group) ) return false;
        Group castOther = (Group) other;
        return new EqualsBuilder()
            .append(this.getGroupId(), castOther.getGroupId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getGroupId())
            .toHashCode();
    }

}
