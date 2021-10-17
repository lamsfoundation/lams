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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

@Entity
@Table(name = "lams_tool")
public class Tool implements Serializable {
    private static final long serialVersionUID = 626016857794096029L;

    @Id
    @Column(name = "tool_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long toolId;

    @Column(name = "learner_url")
    private String learnerUrl;

    @Column(name = "learner_preview_url")
    private String learnerPreviewUrl;

    @Column(name = "learner_progress_url")
    private String learnerProgressUrl;

    @Column(name = "author_url")
    private String authorUrl;

    @Column(name = "monitor_url")
    private String monitorUrl;

    @Column(name = "help_url")
    private String helpUrl;

    @Column(name = "admin_url")
    private String adminUrl;

    @Column(name = "valid_flag")
    private boolean valid;

    @Column(name = "default_tool_content_id")
    private long defaultToolContentId;

    @Column(name = "tool_signature")
    private String toolSignature;

    @Column(name = "tool_display_name")
    private String toolDisplayName;

    @Column
    private String description;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "create_date_time")
    private Date createDateTime;

    @OneToMany(mappedBy = "tool")
    private Set<ToolActivity> activities = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "tool_id")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ExtServerToolAdapterMap> mappedServers = new HashSet<>();

    @Column(name = "grouping_support_type_id")
    private Integer groupingSupportTypeId;

    @Column(name = "tool_identifier")
    private String toolIdentifier;

    @Column(name = "tool_version")
    private String toolVersion;

    @Column(name = "learning_library_id")
    private Long learningLibraryId;

    /**
     * Name of the file (including the package) that contains the text strings for this activity. e.g.
     * org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
     */
    @Column(name = "language_file")
    private String languageFile;

    /** Does this tool produce output definitions / conditions */
    @Column(name = "supports_outputs")
    private Boolean supportsOutputs;

    /**
     * Null if not a tool adapter tool, otherwise this string maps to the external server which this tool adapter will
     * be used for
     */
    @Column(name = "ext_lms_id")
    private String extLmsId;

    /**
     * Entries for an tool in a language property file
     */
    public static final String I18N_DISPLAY_NAME = "tool.display.name";
    public static final String I18N_DESCRIPTION = "tool.description";

    public Tool() {
    }

    public Long getToolId() {
	return toolId;
    }

    public void setToolId(Long toolId) {
	this.toolId = toolId;
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

    public String getAuthorUrl() {
	return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
	this.authorUrl = authorUrl;
    }

    public long getDefaultToolContentId() {
	return defaultToolContentId;
    }

    public void setDefaultToolContentId(long defaultToolContentId) {
	this.defaultToolContentId = defaultToolContentId;
    }

    public String getToolSignature() {
	return toolSignature;
    }

    public void setToolSignature(String toolSignature) {
	this.toolSignature = toolSignature;
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

    public String getServiceName() {
	return serviceName;
    }

    public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
    }

    public Set<ToolActivity> getActivities() {
	return activities;
    }

    public void setActivities(Set<ToolActivity> activities) {
	this.activities = activities;
    }

    public boolean isValid() {
	return valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

    public Integer getGroupingSupportTypeId() {
	return groupingSupportTypeId;
    }

    public void setGroupingSupportTypeId(Integer groupingSupportTypeId) {
	this.groupingSupportTypeId = groupingSupportTypeId;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
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

    public String getAdminUrl() {
	return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
	this.adminUrl = adminUrl;
    }

    public String getToolIdentifier() {
	return toolIdentifier;
    }

    public void setToolIdentifier(String toolIdentifier) {
	this.toolIdentifier = toolIdentifier;
    }

    public String getToolVersion() {
	return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
	this.toolVersion = toolVersion;
    }

    public Long getLearningLibraryId() {
	return learningLibraryId;
    }

    public void setLearningLibraryId(Long learningLibraryId) {
	this.learningLibraryId = learningLibraryId;
    }

    public String getLanguageFile() {
	return languageFile;
    }

    public void setLanguageFile(String languageFile) {
	this.languageFile = languageFile;
    }

    public boolean getSupportsOutputs() {
	return supportsOutputs;
    }

    public void setSupportsOutputs(boolean supportsOutputs) {
	this.supportsOutputs = supportsOutputs;
    }

    public String getExtLmsId() {
	return extLmsId;
    }

    public void setExtLmsId(String extLmsId) {
	this.extLmsId = extLmsId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("toolId", getToolId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof Tool)) {
	    return false;
	}
	Tool castOther = (Tool) other;
	return new EqualsBuilder().append(this.getToolId(), castOther.getToolId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getToolId()).toHashCode();
    }

    public Set<ExtServerToolAdapterMap> getMappedServers() {
	return mappedServers;
    }

    public void setMappedServers(Set<ExtServerToolAdapterMap> mappedServers) {
	this.mappedServers = mappedServers;
    }
}