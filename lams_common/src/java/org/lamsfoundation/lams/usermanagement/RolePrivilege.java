package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

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

    public Long getRpId() {
	return this.rpId;
    }

    public void setRpId(Long rpId) {
	this.rpId = rpId;
    }

    public Privilege getPrivilege() {
	return this.privilege;
    }

    public void setPrivilege(Privilege privilege) {
	this.privilege = privilege;
    }

    public Role getRole() {
	return this.role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("rpId", getRpId()).toString();
    }

}
