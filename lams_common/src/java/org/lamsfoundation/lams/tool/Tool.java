package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_tool"
 *     
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

    /** nullable persistent field */
    private String defineLaterUrl;

    /** persistent field */
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
    private String exportPortfolioUrl;

    /** persistent field */
    private Set activities;

    /** full constructor */
    public Tool(Long toolId, String learnerUrl, boolean supportsGrouping, String authorUrl, boolean supportsDefineLater, String defineLaterUrl, long defaultToolContentId, String toolSignature, String toolDisplayName, String description, String className, String exportPortfolioUrl, Set activities) {
        this.toolId = toolId;
        this.learnerUrl = learnerUrl;
        this.supportsGrouping = supportsGrouping;
        this.authorUrl = authorUrl;
        this.supportsDefineLater = supportsDefineLater;
        this.defineLaterUrl = defineLaterUrl;
        this.defaultToolContentId = defaultToolContentId;
        this.toolSignature = toolSignature;
        this.toolDisplayName = toolDisplayName;
        this.description = description;
        this.serviceName = className;
        this.exportPortfolioUrl = exportPortfolioUrl;
        this.activities = activities;
    }

    /** default constructor */
    public Tool() {
    }

    /** minimal constructor */
    public Tool(Long toolId, String learnerUrl, boolean supportsGrouping, boolean supportsDefineLater, long defaultToolContentId, String toolSignature, String toolDisplayName, String className, String exportPortfolioUrl, Set activities) {
        this.toolId = toolId;
        this.learnerUrl = learnerUrl;
        this.supportsGrouping = supportsGrouping;
        this.supportsDefineLater = supportsDefineLater;
        this.defaultToolContentId = defaultToolContentId;
        this.toolSignature = toolSignature;
        this.toolDisplayName = toolDisplayName;
        this.serviceName = className;
        this.exportPortfolioUrl = exportPortfolioUrl;
        this.activities = activities;
    }

    /** 
     * @hibernate.id generator-class="identity" type="java.lang.Long"
     *             	 column="tool_id"
     *         
     */
    public Long getToolId() {
        return this.toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    /** 
     *            @hibernate.property
     *             column="learner_url"
     *             length="65535"
     *             not-null="true"
     *         
     */
    public String getLearnerUrl() {
        return this.learnerUrl;
    }

    public void setLearnerUrl(String learnerUrl) {
        this.learnerUrl = learnerUrl;
    }

    /** 
     *            @hibernate.property
     *             column="supports_grouping_flag"
     *             length="1"
     *             not-null="true"
     *         
     */
    public boolean getSupportsGrouping() {
        return this.supportsGrouping;
    }

    public void setSupportsGrouping(boolean supportsGrouping) {
        this.supportsGrouping = supportsGrouping;
    }

    /** 
     *            @hibernate.property
     *             column="author_url"
     *             length="65535"
     *         
     */
    public String getAuthorUrl() {
        return this.authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    /** 
     *            @hibernate.property
     *             column="supports_define_later_flag"
     *             length="1"
     *             not-null="true"
     *         
     */
    public boolean getSupportsDefineLater() {
        return this.supportsDefineLater;
    }

    public void setSupportsDefineLater(boolean supportsDefineLater) {
        this.supportsDefineLater = supportsDefineLater;
    }

    /** 
     *            @hibernate.property
     *             column="define_later_url"
     *             length="65535"
     *         
     */
    public String getDefineLaterUrl() {
        return this.defineLaterUrl;
    }

    public void setDefineLaterUrl(String defineLaterUrl) {
        this.defineLaterUrl = defineLaterUrl;
    }

    /** 
     *            @hibernate.property
     *             column="default_tool_content_id"
     *             length="20"
     *             not-null="true"
     *         
     */
    public long getDefaultToolContentId() {
        return this.defaultToolContentId;
    }

    public void setDefaultToolContentId(long defaultToolContentId) {
        this.defaultToolContentId = defaultToolContentId;
    }

    /** 
     *            @hibernate.property
     *             column="tool_signature"
     *             length="64"
     *             not-null="true"
     *         
     */
    public String getToolSignature() {
        return this.toolSignature;
    }

    public void setToolSignature(String toolSignature) {
        this.toolSignature = toolSignature;
    }

    /** 
     *            @hibernate.property
     *             column="tool_display_name"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getToolDisplayName() {
        return this.toolDisplayName;
    }

    public void setToolDisplayName(String toolDisplayName) {
        this.toolDisplayName = toolDisplayName;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="65535"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.property
     *             column="service_name"
     *             length="65535"
     *             not-null="true"
     *         
     */
    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /** 
     *            @hibernate.property
     *             column="export_portfolio_url"
     *             length="65535"
     *             not-null="true"
     *         
     */
    public String getExportPortfolioUrl() {
        return this.exportPortfolioUrl;
    }

    public void setExportPortfolioUrl(String exportPortfolioUrl) {
        this.exportPortfolioUrl = exportPortfolioUrl;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="tool_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.learningdesign.Activity"
     *         
     */
    public Set getActivities() {
        return this.activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
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
