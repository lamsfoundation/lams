package org.lamsfoundation.lams.usermanagement.dto;

import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;

/**
 * Class for displaying data on groupings page.
 */
public class OrganisationGroupingDTO implements Comparable<OrganisationGroupingDTO> {
    private Long groupingId;
    private String name;
    private Integer groupCount;

    public OrganisationGroupingDTO(OrganisationGrouping grouping) {
	this.groupingId = grouping.getGroupingId();
	this.name = grouping.getName();
	this.groupCount = grouping.getGroups().size();
    }

    public Long getGroupingId() {
	return groupingId;
    }

    public void setGroupingId(Long groupingId) {
	this.groupingId = groupingId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getGroupCount() {
	return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
	this.groupCount = groupCount;
    }

    @Override
    public int compareTo(OrganisationGroupingDTO o) {
	if (o == null) {
	    return 1;
	}
	if (this.name == null) {
	    return o.name == null ? 0 : 1;
	}
	return this.name.compareTo(o.name);
    }
}
