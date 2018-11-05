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
@Table(name = "lams_organisation_state")
public class OrganisationState implements Serializable {
    private static final long serialVersionUID = -294038064014685720L;

    public static final Integer ACTIVE = 1;
    public static final Integer HIDDEN = 2;
    public static final Integer ARCHIVED = 3;
    public static final Integer REMOVED = 4;

    @Id
    @Column(name = "organisation_state_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organisationStateId;

    @Column
    private String description;

    public OrganisationState() {
    }

    public Integer getOrganisationStateId() {
	return this.organisationStateId;
    }

    public void setOrganisationStateId(Integer organisationStateId) {
	this.organisationStateId = organisationStateId;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("organisationStateId", getOrganisationStateId())
		.append("description", getDescription()).toString();
    }
}