package org.lamsfoundation.lams.outcome;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public class OutcomeScale {
    private Long scaleId;
    private Organisation organisation;
    private String name;
    private String code;
    private String description;
    private String contentFolderId;
    private User createBy;
    private Date createDateTime;

    private Set<OutcomeScaleItem> items = new LinkedHashSet<OutcomeScaleItem>();

    /**
     * Split comma separated values into a list
     */
    public static List<String> parseItems(String itemString) {
	return StringUtils.isBlank(itemString) ? null : Arrays.asList(itemString.split(","));
    }

    /**
     * Build a string of comma separated values
     */
    public String getItemString() {
	StringBuilder itemString = new StringBuilder();
	for (OutcomeScaleItem item : items) {
	    itemString.append(item.getName()).append(",");
	}
	return itemString.length() == 0 ? null : itemString.substring(0, itemString.length() - 1);
    }

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

    public String getContentFolderId() {
	return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
	this.contentFolderId = contentFolderId;
    }
}