package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_privilege"
 *     
*/
public class Privilege implements Serializable {

    /** identifier field */
    private Long privilegeId;

    /** persistent field */
    private String code;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Set rolePrivileges;

    /** full constructor */
    public Privilege(String code, String description, Set rolePrivileges) {
        this.code = code;
        this.description = description;
        this.rolePrivileges = rolePrivileges;
    }

    /** default constructor */
    public Privilege() {
    }

    /** minimal constructor */
    public Privilege(String code, Set rolePrivileges) {
        this.code = code;
        this.rolePrivileges = rolePrivileges;
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="privilege_id"
     *         
     */
    public Long getPrivilegeId() {
        return this.privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    /** 
     *            @hibernate.property
     *             column="code"
     *             unique="true"
     *             length="10"
     *             not-null="true"
     *         
     */
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="255"
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
     *             cascade="delete-orphan"
     *            @hibernate.collection-key
     *             column="privilege_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.RolePrivilege"
     *         
     */
    public Set getRolePrivileges() {
        return this.rolePrivileges;
    }

    public void setRolePrivileges(Set rolePrivileges) {
        this.rolePrivileges = rolePrivileges;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("privilegeId", getPrivilegeId())
            .toString();
    }

}
