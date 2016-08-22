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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DTO which is passed to tools that want to know about their entry in the lams_tool database. This allows us to change
 * the main Tool object (Tool) without changing the object used by the tools.
 */
public class BasicToolVO implements Serializable, IToolVO {

    private static final long serialVersionUID = 3835534200399940767L;

    /** identifier field */
    private Long toolId;

    /** persistent field */
    private boolean supportsGrouping;

    /** persistent field */
    private String learnerUrl;

    /** persistent field */
    private String learnerPreviewUrl;

    /** persistent field */
    private String learnerProgressUrl;

    /** nullable persistent field */
    private String authorUrl;

    /** persistent field */
    private String monitorUrl;

    /** persistent field */
    private String helpUrl;

    /** persistent field */
    private boolean valid;

    /** nullable persistent field */
    private long defaultToolContentId;

    private Long learningLibraryId;

    /** persistent field */
    private String toolSignature;

    /** persistent field */
    private String toolDisplayName;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private String serviceName;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private Integer groupingSupportTypeId;

    /** persistent field */
    private String toolIdentifier;

    /** persistent field */
    private String toolVersion;

    /**
     * Name of the file (including the package) that contains the text strings for this activity. e.g.
     * org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
     */
    private String languageFile;

    /** Is this tool a tool adapter for an external LMS tool */
    private String extLmsId;

    /** full constructor */
    public BasicToolVO(Long toolId, boolean supportsGrouping, String learnerUrl, String learnerPreviewUrl,
	    String learnerProgressUrl, String authorUrl, String monitorUrl, String helpUrl, long defaultToolContentId,
	    Long learningLibraryId, String toolSignature, String toolDisplayName, String description,
	    String serviceName, Date createDateTime, Integer groupingSupportTypeId, String toolIdentifier,
	    String toolVersion, String languageFile, String extLmsId) {
	this.supportsGrouping = supportsGrouping;
	this.learnerUrl = learnerUrl;
	this.learnerPreviewUrl = learnerPreviewUrl;
	this.learnerProgressUrl = learnerProgressUrl;
	this.authorUrl = authorUrl;
	this.monitorUrl = monitorUrl;
	this.helpUrl = helpUrl;
	this.defaultToolContentId = defaultToolContentId;
	this.learningLibraryId = learningLibraryId;
	this.toolSignature = toolSignature;
	this.toolDisplayName = toolDisplayName;
	this.description = description;
	this.serviceName = serviceName;
	this.createDateTime = createDateTime;
	this.groupingSupportTypeId = groupingSupportTypeId;
	this.toolIdentifier = toolIdentifier;
	this.toolVersion = toolVersion;
	this.languageFile = languageFile;
	this.extLmsId = extLmsId;
    }

    /** default constructor */
    public BasicToolVO() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getAuthorUrl()
     */
    @Override
    public String getAuthorUrl() {
	return authorUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#setAuthorUrl(java.lang.String)
     */
    @Override
    public void setAuthorUrl(String authorUrl) {
	this.authorUrl = authorUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getHelpUrl()
     */
    @Override
    public String getHelpUrl() {
	return helpUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#setHelpUrl(java.lang.String)
     */
    @Override
    public void setHelpUrl(String helpUrl) {
	this.helpUrl = helpUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getCreateDateTime()
     */
    @Override
    public Date getCreateDateTime() {
	return createDateTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#setCreateDateTime(java.util.Date)
     */
    @Override
    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getDefaultToolContentId()
     */
    @Override
    public long getDefaultToolContentId() {
	return defaultToolContentId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#setDefaultToolContentId(long)
     */
    @Override
    public void setDefaultToolContentId(long defaultToolContentId) {
	this.defaultToolContentId = defaultToolContentId;
    }

    @Override
    public Long getLearningLibraryId() {
	return learningLibraryId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getDescription()
     */
    @Override
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getGroupingSupportTypeId()
     */
    @Override
    public Integer getGroupingSupportTypeId() {
	return groupingSupportTypeId;
    }

    public void setGroupingSupportTypeId(Integer groupingSupportTypeId) {
	this.groupingSupportTypeId = groupingSupportTypeId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getLanguageFile()
     */
    @Override
    public String getLanguageFile() {
	return languageFile;
    }

    public void setLanguageFile(String languageFile) {
	this.languageFile = languageFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getLearnerPreviewUrl()
     */
    @Override
    public String getLearnerPreviewUrl() {
	return learnerPreviewUrl;
    }

    public void setLearnerPreviewUrl(String learnerPreviewUrl) {
	this.learnerPreviewUrl = learnerPreviewUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getLearnerProgressUrl()
     */
    @Override
    public String getLearnerProgressUrl() {
	return learnerProgressUrl;
    }

    public void setLearnerProgressUrl(String learnerProgressUrl) {
	this.learnerProgressUrl = learnerProgressUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getLearnerUrl()
     */
    @Override
    public String getLearnerUrl() {
	return learnerUrl;
    }

    public void setLearnerUrl(String learnerUrl) {
	this.learnerUrl = learnerUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getMonitorUrl()
     */
    @Override
    public String getMonitorUrl() {
	return monitorUrl;
    }

    public void setMonitorUrl(String monitorUrl) {
	this.monitorUrl = monitorUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getServiceName()
     */
    @Override
    public String getServiceName() {
	return serviceName;
    }

    public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#isSupportsGrouping()
     */
    @Override
    public boolean isSupportsGrouping() {
	return supportsGrouping;
    }

    public void setSupportsGrouping(boolean supportsGrouping) {
	this.supportsGrouping = supportsGrouping;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getToolDisplayName()
     */
    @Override
    public String getToolDisplayName() {
	return toolDisplayName;
    }

    public void setToolDisplayName(String toolDisplayName) {
	this.toolDisplayName = toolDisplayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getToolId()
     */
    @Override
    public Long getToolId() {
	return toolId;
    }

    public void setToolId(Long toolId) {
	this.toolId = toolId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getToolIdentifier()
     */
    @Override
    public String getToolIdentifier() {
	return toolIdentifier;
    }

    public void setToolIdentifier(String toolIdentifier) {
	this.toolIdentifier = toolIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getToolSignature()
     */
    @Override
    public String getToolSignature() {
	return toolSignature;
    }

    public void setToolSignature(String toolSignature) {
	this.toolSignature = toolSignature;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getToolVersion()
     */
    @Override
    public String getToolVersion() {
	return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
	this.toolVersion = toolVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#isValid()
     */
    @Override
    public boolean isValid() {
	return valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#isSupportsOutputs()
     */
    @Override
    public boolean isSupportsOutputs() {
	return supportsGrouping;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.IToolVO#getExtLmsId()
     */
    @Override
    public String getExtLmsId() {
	return this.extLmsId;
    }

    public void setExtLmsId(String extLmsId) {
	this.extLmsId = extLmsId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("toolId", getToolId()).append("toolSignature", getToolSignature())
		.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
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

}
