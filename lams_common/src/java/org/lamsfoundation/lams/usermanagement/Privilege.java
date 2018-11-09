package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_privilege")
public class Privilege implements Serializable {

    private static final long serialVersionUID = 5237905825598226845L;

    @Id
    @Column(name = "privilege_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long privilegeId;

    @Column
    private String code;

    @Column
    private String description;

    public Privilege() {
    }

    public Long getPrivilegeId() {
	return this.privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
	this.privilegeId = privilegeId;
    }

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("privilegeId", getPrivilegeId()).toString();
    }
}