package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_role_privilege")
public class RolePrivilege implements Serializable {
    private static final long serialVersionUID = -8812038010815801094L;

    @Id
    @Column(name = "rp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rpId;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

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