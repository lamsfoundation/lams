package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class UserGroupPK implements Serializable {

    /** identifier field */
    private Long userId;

    /** identifier field */
    private Long groupId;

    /** full constructor */
    public UserGroupPK(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    /** default constructor */
    public UserGroupPK() {
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

    /** 
     *                @hibernate.property
     *                 column="group_id"
     *                 length="20"
     *             
     */
    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userId", getUserId())
            .append("groupId", getGroupId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof UserGroupPK) ) return false;
        UserGroupPK castOther = (UserGroupPK) other;
        return new EqualsBuilder()
            .append(this.getUserId(), castOther.getUserId())
            .append(this.getGroupId(), castOther.getGroupId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserId())
            .append(getGroupId())
            .toHashCode();
    }

}
