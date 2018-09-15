package org.lamsfoundation.lams.outcome;

import java.io.Serializable;
import java.util.Date;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public class Outcome implements Serializable {
    private static final long serialVersionUID = -7175245687448269571L;

    private Long outcomeId;
    private Organisation organisation;
    private OutcomeScale scale;
    private String name;
    private String code;
    private String description;
    private String contentFolderId;
    private User createBy;
    private Date createDateTime;

    public Long getOutcomeId() {
	return outcomeId;
    }

    public void setOutcomeId(Long outcomeId) {
	this.outcomeId = outcomeId;
    }

    public Organisation getOrganisation() {
	return organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public OutcomeScale getScale() {
	return scale;
    }

    public void setScale(OutcomeScale scale) {
	this.scale = scale;
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

    public String getContentFolderId() {
	return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
	this.contentFolderId = contentFolderId;
    }
}