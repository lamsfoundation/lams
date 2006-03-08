/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.dto.AuthoringToolDTO;


/** 
 * 
 * @hibernate.class table="lams_tool"
 */
public class Tool implements Serializable {

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

    /** nullable persistent field */
    private String defineLaterUrl;

    /** persistent field */
    private String exportPortfolioLearnerUrl;

    /** persistent field */
    private String exportPortfolioClassUrl;

    /** persistent field */
    private String monitorUrl;
    
    /** persistent field */
    private String contributeUrl;
    
    /** persistent field */
    private String moderationUrl;
    
    /** persistent field */
    private boolean supportsRunOffline;
    
    /** persistent field */
    private boolean valid;
    
    /** nullable persistent field */
    private long defaultToolContentId;

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
    private Set activities;

    /** persistent field */
    private Integer groupingSupportTypeId;
    
    /** persistent field */
    private String toolIdentifier;

    /** persistent field */
    private String toolVersion;

	/** Name of the file (including the package) that contains the text strings for
	 * this activity. e.g. org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties. */
	private String languageFile;


    /** full constructor */
    public Tool(Long toolId, 
                String learnerUrl, 
                String learnerPreviewUrl,
                String learnerProgressUrl,
                String authorUrl, 
                String defineLaterUrl, 
                String monitorUrl,
                String contributeUrl,
                String moderationUrl,
                String exportPortfolioLearnerUrl,
                String exportPortfolioClassUrl,
                boolean supportsGrouping, 
                boolean supportsRunOffline,
                long defaultToolContentId, 
                String toolSignature, 
                String toolDisplayName, 
                String description, 
                String className, 
                Set activities,
                Integer groupingSupportTypeId,
                Date createDateTime,
                String toolIdentifier,
                String toolVersion,
                String languageFile) 
    {
        this.toolId = toolId;
        this.learnerUrl = learnerUrl;
        this.learnerPreviewUrl = learnerPreviewUrl;
        this.learnerProgressUrl = learnerProgressUrl;
        this.authorUrl = authorUrl;
        this.defineLaterUrl = defineLaterUrl;
        this.monitorUrl = monitorUrl;
        this.contributeUrl = contributeUrl;
        this.moderationUrl = moderationUrl;
        this.exportPortfolioLearnerUrl = exportPortfolioLearnerUrl;
        this.exportPortfolioClassUrl = exportPortfolioClassUrl;

        this.supportsGrouping = supportsGrouping;
        this.supportsRunOffline = supportsRunOffline;
        this.defaultToolContentId = defaultToolContentId;
        this.toolSignature = toolSignature;
        this.toolDisplayName = toolDisplayName;
        this.description = description;
        this.serviceName = className;
        this.activities = activities;
        this.groupingSupportTypeId = groupingSupportTypeId;
        this.createDateTime = createDateTime;
        this.toolIdentifier = toolIdentifier;
        this.toolVersion = toolVersion;
        this.languageFile = languageFile;
    }

    /** default constructor */
    public Tool() {
    }

    /** minimal constructor */
    public Tool(Long toolId, 
                String learnerUrl, 
                String authorUrl, 
                boolean supportsGrouping, 
                boolean supportsRunOffline,
                long defaultToolContentId, 
                String toolSignature, 
                String toolDisplayName, 
                String className, 
                Set activities,
                Integer groupingSupportTypeId,
                Date createDateTime,
                String toolIdentifier,
                String toolVersion) 
    {
        this.toolId = toolId;
        this.learnerUrl = learnerUrl;
        this.authorUrl = authorUrl;
        this.supportsGrouping = supportsGrouping;
        this.supportsRunOffline = supportsRunOffline;
        this.defaultToolContentId = defaultToolContentId;
        this.toolSignature = toolSignature;
        this.toolDisplayName = toolDisplayName;
        this.serviceName = className;
        this.activities = activities;
        this.groupingSupportTypeId = groupingSupportTypeId;
        this.createDateTime = createDateTime;
        this.toolIdentifier = toolIdentifier;
        this.toolVersion = toolVersion;
    }

    /** 
     * @hibernate.id generator-class="identity" type="java.lang.Long"
     *             	 column="tool_id"      
     */
    public Long getToolId() {
        return this.toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    /** 
     * @hibernate.property column="learner_url" length="65535"
     *            		   not-null="true"    
     */
    public String getLearnerUrl() {
        return this.learnerUrl;
    }

    public void setLearnerUrl(String learnerUrl) {
        this.learnerUrl = learnerUrl;
    }

    /** 
     * @hibernate.property column="learner_preview_url" length="65535"
     *            		   not-null="false"    
     */
    public String getLearnerPreviewUrl() {
        return this.learnerPreviewUrl;
    }

    public void setLearnerPreviewUrl(String learnerPreviewUrl) {
        this.learnerPreviewUrl = learnerPreviewUrl;
    }

    /** 
     * @hibernate.property column="learner_progress_url" length="65535"
     *            		   not-null="false"    
     */
    public String getLearnerProgressUrl() {
        return this.learnerProgressUrl;
    }

    public void setLearnerProgressUrl(String learnerProgressUrl) {
        this.learnerProgressUrl = learnerProgressUrl;
    }

    /**
     * Does this tool support contribute? Will be true if the contributeURL is not null/empty string.
     */
    public boolean getSupportsContribute()
    {
    	String contributeURL = getContributeUrl();
    	return ( contributeURL != null && contributeURL.trim().length() > 0);   
    }
    
    /** 
     * @hibernate.property column="author_url" length="65535" not-null="true"         
     */
    public String getAuthorUrl() {
        return this.authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    /** 
     * Does this tool support define later? Will be true if the defineLaterURL is not null/empty string.
     */
    public boolean getSupportsDefineLater() {
    	String url = getDefineLaterUrl();
    	return ( url != null && url.trim().length() > 0);   
    }

    /**
     * Does this tool support moderation? Will be true if the moderateURL is not null/empty string.
     */
    public boolean getSupportsModeration()
    {
    	String url = getModerationUrl();
    	return ( url != null && url.trim().length() > 0);   
    }
    
    /**
     * @hibernate.property column="supports_run_offline_flag" length="1"
     *             		   not-null="true" 
     * @return Returns the supportsRunOffline.
     */
    public boolean getSupportsRunOffline()
    {
        return supportsRunOffline;
    }
    
    /**
     * @param supportsRunOffline The supportsRunOffline to set.
     */
    public void setSupportsRunOffline(boolean supportsRunOffline)
    {
        this.supportsRunOffline = supportsRunOffline;
    }
    
    /** 
     * @hibernate.property column="define_later_url" length="65535"  not-null="false"        
     */
    public String getDefineLaterUrl() {
        return this.defineLaterUrl;
    }

    public void setDefineLaterUrl(String defineLaterUrl) {
        this.defineLaterUrl = defineLaterUrl;
    }

    /** 
     * @hibernate.property column="default_tool_content_id" length="20"
     *             		   not-null="false"       
     */
    public long getDefaultToolContentId() {
        return this.defaultToolContentId;
    }

    public void setDefaultToolContentId(long defaultToolContentId) {
        this.defaultToolContentId = defaultToolContentId;
    }

    /** 
     * @hibernate.property column="tool_signature" length="64"
     *                     not-null="true"      
     */
    public String getToolSignature() {
        return this.toolSignature;
    }

    public void setToolSignature(String toolSignature) {
        this.toolSignature = toolSignature;
    }

    /** 
     * @hibernate.property column="tool_display_name" length="255"
     *             		   not-null="true"     
     */
    public String getToolDisplayName() {
        return this.toolDisplayName;
    }

    public void setToolDisplayName(String toolDisplayName) {
        this.toolDisplayName = toolDisplayName;
    }

    /** 
     * @hibernate.property column="description" length="65535"     
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     * @hibernate.property column="service_name" length="65535"
     *             		   not-null="true"      
     */
    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /** 
     * @hibernate.property column="export_pfolio_learner_url" length="65535"
     *             		   not-null="false"  
     */
    public String getExportPortfolioLearnerUrl() {
        return this.exportPortfolioLearnerUrl;
    }

    public void setExportPortfolioLearnerUrl(String exportPortfolioLearnerUrl) {
        this.exportPortfolioLearnerUrl = exportPortfolioLearnerUrl;
    }

    /** 
     * @hibernate.property column="export_pfolio_class_url" length="65535"
     *             		   not-null="false"  
     */
    public String getExportPortfolioClassUrl() {
        return this.exportPortfolioClassUrl;
    }

    public void setExportPortfolioClassUrl(String exportPortfolioClassUrl) {
        this.exportPortfolioClassUrl = exportPortfolioClassUrl;
    }

    /** 
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key  column="tool_id"
     * @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.learningdesign.Activity"
     *         
     */
    public Set getActivities() {
        return this.activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
    }

    /**
     * @hibernate.property column="valid_flag" length="1"
     *             		   not-null="true"    
     * @return Returns the valid.
     */
    public boolean isValid()
    {
        return valid;
    }
    /**
     * @param valid The valid to set.
     */
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }
    /**
     * @return Returns the groupingSupportTypeId.
     */
    public Integer getGroupingSupportTypeId()
    {
        return groupingSupportTypeId;
    }
    /**
     * @param groupingSupportTypeId The groupingSupportTypeId to set.
     */
    public void setGroupingSupportTypeId(Integer groupingSupportTypeId)
    {
        this.groupingSupportTypeId = groupingSupportTypeId;
    }
    
    /**
     * @hibernate.property column="create_date_time"
				           length="19" not-null="true"
     * @return Returns the createDateTime.
     */
    public Date getCreateDateTime()
    {
        return createDateTime;
    }
    /**
     * @param createDateTime The createDateTime to set.
     */
    public void setCreateDateTime(Date createDateTime)
    {
        this.createDateTime = createDateTime;
    }
    

    /**
     * @hibernate.property column="contribute_url" length="65535" not-null="false"  
     * @return Returns the contributeUrl.
     */
    public String getContributeUrl()
    {
        return contributeUrl;
    }
    /**
     * @param contributeUrl The contributUrl to set.
     */
    public void setContributeUrl(String contributeUrl)
    {
        this.contributeUrl = contributeUrl;
    }
    /**
     * @hibernate.property column="moderation_url" length="65535" not-null="false"   
     * @return Returns the moderationUrl.
     */
    public String getModerationUrl()
    {
        return moderationUrl;
    }
    /**
     * @param moderationUrl The moderationUrl to set.
     */
    public void setModerationUrl(String moderationUrl)
    {
        this.moderationUrl = moderationUrl;
    }
    /**
     * @hibernate.property column="monitor_url" length="65535" not-null="false"  
     * @return Returns the monitorUrl.
     */
    public String getMonitorUrl()
    {
        return monitorUrl;
    }
    /**
     * @param monitorUrl The monitorUrl to set.
     */
    public void setMonitorUrl(String monitorUrl)
    {
        this.monitorUrl = monitorUrl;
    }
 
    
    /** 
     * @hibernate.property column="tool_identifier" length="64"       
     */
    public String getToolIdentifier() {
        return this.toolIdentifier;
    }

    public void setToolIdentifier(String toolIdentifier) {
        this.toolIdentifier = toolIdentifier;
    }

    /** 
     * @hibernate.property column="tool_version" length="10"       
     */
    public String getToolVersion() {
        return this.toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    
    /** 
     * @hibernate.property column="language_file" length="255" not-null="false"   
     */
	public String getLanguageFile() {
		return languageFile;
	}

	public void setLanguageFile(String languageFile) {
		this.languageFile = languageFile;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("toolId", getToolId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Tool) ) return false;
        Tool castOther = (Tool) other;
        return new EqualsBuilder()
            .append(this.getToolId(), castOther.getToolId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getToolId())
            .toHashCode();
    }
    public AuthoringToolDTO getAuthoringToolDTO(){
    	return new AuthoringToolDTO(this);
    }

    public BasicToolVO createBasicToolVO() {
    	BasicToolVO vo = new BasicToolVO();
    	vo.setAuthorUrl(this.getAuthorUrl());
    	return vo;
    }

}
