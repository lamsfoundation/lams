package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.usermanagement.User;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_user_group"
 *     
*/
public class UserGroup implements Serializable {

    /** identifier field */
    private org.lamsfoundation.lams.lesson.UserGroupPK comp_id;

    /** nullable persistent field */
    private User user;

    /** nullable persistent field */
    private Group group;

    /** full constructor */
    public UserGroup(org.lamsfoundation.lams.lesson.UserGroupPK comp_id, User user, Group group) {
        this.comp_id = comp_id;
        this.user = user;
        this.group = group;
    }

    /** default constructor */
    public UserGroup() {
    }

    /** minimal constructor */
    public UserGroup(org.lamsfoundation.lams.lesson.UserGroupPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *         
     */
    public org.lamsfoundation.lams.lesson.UserGroupPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(org.lamsfoundation.lams.lesson.UserGroupPK comp_id) {
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
     *             name="group_id"
     *         
     */
    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof UserGroup) ) return false;
        UserGroup castOther = (UserGroup) other;
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
