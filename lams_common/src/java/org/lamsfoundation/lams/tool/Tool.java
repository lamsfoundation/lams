package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * 
 * @hibernate.class table="lams_tool"
 */
public class Tool implements Serializable {

    /** identifier field */
    private Long toolId;

    /** persistent field */
    private String learnerUrl;

    /** persistent field */
    private boolean supportsGrouping;

    /** nullable persistent field */
    private String authorUrl;

    /** persistent field */
    private boolean supportsDefineLater;

    /** persistent field */
    private boolean supportsModeration;
    
    /** persistent field */
    private boolean supportsRunOffline;
    
    /** persistent field */
    private boolean supportsContribute;
    
    /** persistent field */
    private boolean valid;
    
    /** nullable persistent field */
    private String defineLaterUrl;

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
    private String exportPortfolioUrl;

    /** persistent field */
    private String monitorUrl;
    
    /** persistent field */
    private String contributeUrl;
    
    /** persistent field */
    private String moderationUrl;
    
    /** persistent field */
    private Set activities;

    /** persistent field */
    private Integer groupingSupportTypeId;
    
    /** full constructor */
    public Tool(Long toolId, 
                String learnerUrl, 
                boolean supportsGrouping, 
                String authorUrl, 
                boolean supportsDefineLater,
                boolean supportsModeration,
                boolean supportsRunOffline,
                boolean supportsContribute,
                String defineLaterUrl, 
                long defaultToolContentId, 
                String toolSignature, 
                String toolDisplayName, 
                String description, 
                String className, 
                String exportPortfolioUrl, 
                Set activities,
                Integer groupingSupportTypeId,
                Date createDateTime,
                String monitorUrl,
                String contributeUrl,
                String moderationUrl) 
    {
        this.toolId = toolId;
        this.learnerUrl = learnerUrl;
        this.supportsGrouping = supportsGrouping;
        this.authorUrl = authorUrl;
        this.supportsDefineLater = supportsDefineLater;
        this.supportsModeration = supportsModeration;
        this.supportsContribute = supportsContribute;
        this.supportsRunOffline = supportsRunOffline;
        this.defineLaterUrl = defineLaterUrl;
        this.defaultToolContentId = defaultToolContentId;
        this.toolSignature = toolSignature;
        this.toolDisplayName = toolDisplayName;
        this.description = description;
        this.serviceName = className;
        this.exportPortfolioUrl = exportPortfolioUrl;
        this.activities = activities;
        this.groupingSupportTypeId = groupingSupportTypeId;
        this.createDateTime = createDateTime;
        this.monitorUrl = monitorUrl;
        this.contributeUrl = contributeUrl;
        this.moderationUrl = moderationUrl;
    }

    /** default constructor */
    public Tool() {
    }

    /** minimal constructor */
    public Tool(Long toolId, 
                String learnerUrl, 
                boolean supportsGrouping, 
                boolean supportsDefineLater,
                boolean supportsModeration,
                boolean supportsContribute,
                boolean supportsRunOffline,
                long defaultToolContentId, 
                String toolSignature, 
                String toolDisplayName, 
                String className, 
                String exportPortfolioUrl, 
                Set activities,
                Integer groupingSupportTypeId,
                Date createDateTime,
                String monitorUrl) 
    {
        this.toolId = toolId;
        this.learnerUrl = learnerUrl;
        this.supportsGrouping = supportsGrouping;
        this.supportsDefineLater = supportsDefineLater;
        this.supportsModeration = supportsModeration;
        this.supportsContribute = supportsContribute;
        this.supportsRunOffline = supportsRunOffline;
        this.defaultToolContentId = defaultToolContentId;
        this.toolSignature = toolSignature;
        this.toolDisplayName = toolDisplayName;
        this.serviceName = className;
        this.exportPortfolioUrl = exportPortfolioUrl;
        this.activities = activities;
        this.groupingSupportTypeId = groupingSupportTypeId;
        this.createDateTime = createDateTime;
        this.monitorUrl = monitorUrl;
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
     * @hibernate.property column="learner_url"length="65535"
     *            		   not-null="true"    
     */
    public String getLearnerUrl() {
        return this.learnerUrl;
    }

    public void setLearnerUrl(String learnerUrl) {
        this.learnerUrl = learnerUrl;
    }

    /** 
     * @hibernate.property column="supports_grouping_flag" length="1"
     *             		   not-null="true"       
     */
    public boolean getSupportsGrouping() {
        return this.supportsGrouping;
    }

    public void setSupportsGrouping(boolean supportsGrouping) {
        this.supportsGrouping = supportsGrouping;
    }

    /**
     * @hibernate.property column="supports_contribute_flag" length="1"
     *             		   not-null="true"    
     * @return Returns the supportsContribute.
     */
    public boolean getSupportsContribute()
    {
        return supportsContribute;
    }
    /**
     * @param supportsContribute The supportsContribute to set.
     */
    public void setSupportsContribute(boolean supportsContribute)
    {
        this.supportsContribute = supportsContribute;
    }
    
    /** 
     * @hibernate.property column="author_url" length="65535"       
     */
    public String getAuthorUrl() {
        return this.authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    /** 
     * @hibernate.property column="supports_define_later_flag" length="1"
     *             		   not-null="true"     
     */
    public boolean getSupportsDefineLater() {
        return this.supportsDefineLater;
    }

    public void setSupportsDefineLater(boolean supportsDefineLater) {
        this.supportsDefineLater = supportsDefineLater;
    }

    /**
     * @hibernate.property column="supports_moderation_flag" length="1"
     *             		   not-null="true" 
     * @return Returns the supportsModeration.
     */
    public boolean getSupportsModeration()
    {
        return supportsModeration;
    }
    /**
     * @param supportsModeration The supportsModeration to set.
     */
    public void setSupportsModeration(boolean supportsModeration)
    {
        this.supportsModeration = supportsModeration;
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
     * @hibernate.property column="define_later_url" length="65535"       
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
     * @hibernate.property column="export_portfolio_url" length="65535"
     *             		   not-null="true"  
     */
    public String getExportPortfolioUrl() {
        return this.exportPortfolioUrl;
    }

    public void setExportPortfolioUrl(String exportPortfolioUrl) {
        this.exportPortfolioUrl = exportPortfolioUrl;
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
     * @hibernate.property column="contribute_url" length="65535"  
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
     * @hibernate.property column="moderation_url" length="65535"   
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
     * @hibernate.property column="monitor_url" length="65535"  
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

}
