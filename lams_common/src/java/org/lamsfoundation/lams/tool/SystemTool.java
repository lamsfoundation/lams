/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents the URLs for the internal functionality such as grouping and gates. Eventually these will become
 * "pluggable" tools, so that we can have other types of grouping/gates, without making changes to the core.
 */
@Entity
@Table(name = "lams_system_tool")
public class SystemTool implements Serializable {
    private static final long serialVersionUID = 8804420928455093751L;

    /* System Tool IDs */
    public static final Long GROUPING = 1L;
    public static final Long SYNC_GATE = 2L;
    public static final Long SCHEDULE_GATE = 3L;
    public static final Long PERMISSION_GATE = 4L;
    public static final Long SYSTEM_GATE = 5L;
    public static final Long TEACHER_CHOSEN_BRANCHING = 6L;
    public static final Long GROUP_BASED_BRANCHING = 7L;
    public static final Long TOOL_BASED_BRANCHING = 8L;
    public static final Long SEQUENCE = 9L;
    public static final Long CONDITION_GATE = 10L;
    public static final Long FLOATING_ACTIVITIES = 11L;
    public static final Long PASSWORD_GATE = 12L;

    @Id
    @Column(name = "system_tool_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long systemToolId;

    @Column(name = "learning_activity_type_id")
    private Integer activityTypeId;

    @Column(name = "tool_display_name")
    private String toolDisplayName;

    @Column
    private String description;

    @Column(name = "learner_url")
    private String learnerUrl;

    @Column(name = "learner_preview_url")
    private String learnerPreviewUrl;

    @Column(name = "learner_progress_url")
    private String learnerProgressUrl;

    @Column(name = "monitor_url")
    private String monitorUrl;

    @Column(name = "contribute_url")
    private String contributeUrl;

    @Column(name = "help_url")
    private String helpUrl;

    @Column(name = "admin_url")
    private String adminUrl;

    @Column(name = "create_date_time")
    private Date createDateTime;

    /**
     * Entries for an tool in a language property file
     */
    public static final String I18N_DISPLAY_NAME = "tool.display.name";
    public static final String I18N_DESCRIPTION = "tool.description";

    public SystemTool() {
    }

    public Long getSystemToolId() {
	return systemToolId;
    }

    public void setSystemToolId(Long systemToolId) {
	this.systemToolId = systemToolId;
    }

    public Integer getActivityTypeId() {
	return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
	this.activityTypeId = activityTypeId;
    }

    public String getToolDisplayName() {
	return toolDisplayName;
    }

    public void setToolDisplayName(String toolDisplayName) {
	this.toolDisplayName = toolDisplayName;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getLearnerUrl() {
	return learnerUrl;
    }

    public void setLearnerUrl(String learnerUrl) {
	this.learnerUrl = learnerUrl;
    }

    public String getLearnerPreviewUrl() {
	return learnerPreviewUrl;
    }

    public void setLearnerPreviewUrl(String learnerPreviewUrl) {
	this.learnerPreviewUrl = learnerPreviewUrl;
    }

    public String getLearnerProgressUrl() {
	return learnerProgressUrl;
    }

    public void setLearnerProgressUrl(String learnerProgressUrl) {
	this.learnerProgressUrl = learnerProgressUrl;
    }

    public String getAdminUrl() {
	return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
	this.adminUrl = adminUrl;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    /**
     * Does this tool support contribute? Will be true if the contributeURL is not null/empty string.
     */
    public boolean getSupportsContribute() {
	String contributeURL = getContributeUrl();
	return (contributeURL != null) && (contributeURL.trim().length() > 0);
    }

    public String getContributeUrl() {
	return contributeUrl;
    }

    public void setContributeUrl(String contributeUrl) {
	this.contributeUrl = contributeUrl;
    }

    public String getMonitorUrl() {
	return monitorUrl;
    }

    public void setMonitorUrl(String monitorUrl) {
	this.monitorUrl = monitorUrl;
    }

    public String getHelpUrl() {
	return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
	this.helpUrl = helpUrl;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("systemToolId", getSystemToolId())
		.append("activityTypeId", getActivityTypeId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof SystemTool)) {
	    return false;
	}
	SystemTool castOther = (SystemTool) other;
	return new EqualsBuilder().append(this.getSystemToolId(), castOther.getSystemToolId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getSystemToolId()).toHashCode();
    }
}