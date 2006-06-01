package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_role_privilege"
 *     
*/
public class RolePrivilege implements Serializable {

    /** identifier field */
    private Long rpId;

    /** persistent field */
    private Privilege privilege;

    /** persistent field */
    private Role role;

    /** full constructor */
    public RolePrivilege(Privilege privilege, Role role) {
        this.privilege = privilege;
        this.role = role;
    }

    /** default constructor */
    public RolePrivilege() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="rp_id"
     *         
     */
    public Long getRpId() {
        return this.rpId;
    }

    public void setRpId(Long rpId) {
        this.rpId = rpId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="privilege_id"         
     *         
     */
    public Privilege getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="role_id"         
     *         
     */
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("rpId", getRpId())
            .toString();
    }

}
