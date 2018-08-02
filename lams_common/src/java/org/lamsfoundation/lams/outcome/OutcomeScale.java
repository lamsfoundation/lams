package org.lamsfoundation.lams.outcome;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public class OutcomeScale {
    private Long scaleId;
    private Organisation organisation;
    private String name;
    private String code;
    private String description;
    private User createBy;
    private Date createDateTime;

    private Set<OutcomeScaleItem> items = new HashSet<OutcomeScaleItem>();

    public Long getScaleId() {
	return scaleId;
    }

    public void setScaleId(Long scaleId) {
	this.scaleId = scaleId;
    }

    public Organisation getOrganisation() {
	return organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public User getCreateBy() {
	return createBy;
    }

    public void setCreateBy(User createBy) {
	this.createBy = createBy;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    public Set<OutcomeScaleItem> getItems() {
	return items;
    }

    public void setItems(Set<OutcomeScaleItem> items) {
	this.items = items;
    }
}