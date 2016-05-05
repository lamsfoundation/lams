package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OrganisationState implements Serializable {

    public static final Integer ACTIVE = 1;
    public static final Integer HIDDEN = 2;
    public static final Integer ARCHIVED = 3;
    public static final Integer REMOVED = 4;

    /** identifier field */
    private Integer organisationStateId;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Set Organisations;

    /** full constructor */
    public OrganisationState(String description, Set Organisations) {
	this.description = description;
	this.Organisations = Organisations;
    }

    /** default constructor */
    public OrganisationState() {
    }

    /** minimal constructor */
    public OrganisationState(Set Organisations) {
	this.Organisations = Organisations;
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

    public Set getOrganisations() {
	return this.Organisations;
    }

    public void setOrganisations(Set Organisations) {
	this.Organisations = Organisations;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("organisationStateId", getOrganisationStateId())
		.append("description", getDescription()).toString();
    }

}
