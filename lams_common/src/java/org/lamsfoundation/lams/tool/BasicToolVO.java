/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * DTO which is passed to tools that want to know about their entry in the lams_tool database.
 * This allows us to change the main Tool object (Tool) without changing the object
 * used by the tools. 
 */
public class BasicToolVO implements Serializable, IToolVO {

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
    private Integer groupingSupportTypeId;
    
    /** persistent field */
    private String toolIdentifier;

    /** persistent field */
    private String toolVersion;

	/** Name of the file (including the package) that contains the text strings for
	 * this activity. e.g. org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties. */
	private String languageFile;

    /** full constructor */
    public BasicToolVO(Long toolId, 
                boolean supportsGrouping, 
                String learnerUrl, 
                String learnerPreviewUrl, 
                String learnerProgressUrl, 
                String authorUrl, 
                String defineLaterUrl, 
                String exportPortfolioLearnerUrl, 
                String exportPortfolioClassUrl, 
                String monitorUrl,
                String contributeUrl,
                String moderationUrl,
                boolean supportsRunOffline,
                long defaultToolContentId, 
                String toolSignature, 
                String toolDisplayName, 
                String description, 
                String serviceName, 
                Date createDateTime,
                Integer groupingSupportTypeId,
                String toolIdentifier, 
                String toolVersion, 
                String languageFile) 
    {
		this.supportsGrouping=supportsGrouping;
		this.learnerUrl=learnerUrl;
		this.learnerPreviewUrl=learnerPreviewUrl;
		this.learnerProgressUrl=learnerProgressUrl;
		this.authorUrl=authorUrl;
		this.defineLaterUrl=defineLaterUrl;
		this.exportPortfolioLearnerUrl=exportPortfolioLearnerUrl;
		this.exportPortfolioClassUrl=exportPortfolioClassUrl;
		this.monitorUrl=monitorUrl;
		this.contributeUrl=contributeUrl;
		this.moderationUrl=moderationUrl;
		this.supportsRunOffline=supportsRunOffline;
		this.defaultToolContentId=defaultToolContentId;
		this.toolSignature=toolSignature;
		this.toolDisplayName=toolDisplayName;
		this.description=description;
		this.serviceName=serviceName;
		this.createDateTime=createDateTime;
		this.groupingSupportTypeId=groupingSupportTypeId;
		this.toolIdentifier=toolIdentifier;
		this.toolVersion=toolVersion;
		this.languageFile=languageFile;
    }

    /** default constructor */
    public BasicToolVO() {
    }

    /** minimal constructor */
    public BasicToolVO(Long toolId, 
                boolean supportsGrouping, 
                String learnerUrl, 
                String authorUrl, 
                boolean supportsRunOffline,
                String toolSignature, 
                String toolDisplayName, 
                String serviceName, 
                Date createDateTime,
                Integer groupingSupportTypeId,
                String toolIdentifier, 
                String toolVersion) 
    {
		this.supportsGrouping=supportsGrouping;
		this.learnerUrl=learnerUrl;
		this.authorUrl=authorUrl;
		this.supportsRunOffline=supportsRunOffline;
		this.toolSignature=toolSignature;
		this.toolDisplayName=toolDisplayName;
		this.serviceName=serviceName;
		this.createDateTime=createDateTime;
		this.groupingSupportTypeId=groupingSupportTypeId;
		this.toolIdentifier=toolIdentifier;
		this.toolVersion=toolVersion;
   }



	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getAuthorUrl()
	 */
	public String getAuthorUrl() {
		return authorUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#setAuthorUrl(java.lang.String)
	 */
	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getContributeUrl()
	 */
	public String getContributeUrl() {
		return contributeUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#setContributeUrl(java.lang.String)
	 */
	public void setContributeUrl(String contributeUrl) {
		this.contributeUrl = contributeUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getCreateDateTime()
	 */
	public Date getCreateDateTime() {
		return createDateTime;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#setCreateDateTime(java.util.Date)
	 */
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getDefaultToolContentId()
	 */
	public long getDefaultToolContentId() {
		return defaultToolContentId;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#setDefaultToolContentId(long)
	 */
	public void setDefaultToolContentId(long defaultToolContentId) {
		this.defaultToolContentId = defaultToolContentId;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getDefineLaterUrl()
	 */
	public String getDefineLaterUrl() {
		return defineLaterUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#setDefineLaterUrl(java.lang.String)
	 */
	public void setDefineLaterUrl(String defineLaterUrl) {
		this.defineLaterUrl = defineLaterUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getExportPortfolioClassUrl()
	 */
	public String getExportPortfolioClassUrl() {
		return exportPortfolioClassUrl;
	}

	public void setExportPortfolioClassUrl(String exportPortfolioClassUrl) {
		this.exportPortfolioClassUrl = exportPortfolioClassUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getExportPortfolioLearnerUrl()
	 */
	public String getExportPortfolioLearnerUrl() {
		return exportPortfolioLearnerUrl;
	}

	public void setExportPortfolioLearnerUrl(String exportPortfolioLearnerUrl) {
		this.exportPortfolioLearnerUrl = exportPortfolioLearnerUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getGroupingSupportTypeId()
	 */
	public Integer getGroupingSupportTypeId() {
		return groupingSupportTypeId;
	}

	public void setGroupingSupportTypeId(Integer groupingSupportTypeId) {
		this.groupingSupportTypeId = groupingSupportTypeId;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getLanguageFile()
	 */
	public String getLanguageFile() {
		return languageFile;
	}

	public void setLanguageFile(String languageFile) {
		this.languageFile = languageFile;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getLearnerPreviewUrl()
	 */
	public String getLearnerPreviewUrl() {
		return learnerPreviewUrl;
	}

	public void setLearnerPreviewUrl(String learnerPreviewUrl) {
		this.learnerPreviewUrl = learnerPreviewUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getLearnerProgressUrl()
	 */
	public String getLearnerProgressUrl() {
		return learnerProgressUrl;
	}

	public void setLearnerProgressUrl(String learnerProgressUrl) {
		this.learnerProgressUrl = learnerProgressUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getLearnerUrl()
	 */
	public String getLearnerUrl() {
		return learnerUrl;
	}

	public void setLearnerUrl(String learnerUrl) {
		this.learnerUrl = learnerUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getModerationUrl()
	 */
	public String getModerationUrl() {
		return moderationUrl;
	}

	public void setModerationUrl(String moderationUrl) {
		this.moderationUrl = moderationUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getMonitorUrl()
	 */
	public String getMonitorUrl() {
		return monitorUrl;
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getServiceName()
	 */
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#isSupportsGrouping()
	 */
	public boolean isSupportsGrouping() {
		return supportsGrouping;
	}

	public void setSupportsGrouping(boolean supportsGrouping) {
		this.supportsGrouping = supportsGrouping;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#isSupportsRunOffline()
	 */
	public boolean isSupportsRunOffline() {
		return supportsRunOffline;
	}

	public void setSupportsRunOffline(boolean supportsRunOffline) {
		this.supportsRunOffline = supportsRunOffline;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getToolDisplayName()
	 */
	public String getToolDisplayName() {
		return toolDisplayName;
	}

	public void setToolDisplayName(String toolDisplayName) {
		this.toolDisplayName = toolDisplayName;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getToolId()
	 */
	public Long getToolId() {
		return toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getToolIdentifier()
	 */
	public String getToolIdentifier() {
		return toolIdentifier;
	}

	public void setToolIdentifier(String toolIdentifier) {
		this.toolIdentifier = toolIdentifier;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getToolSignature()
	 */
	public String getToolSignature() {
		return toolSignature;
	}

	public void setToolSignature(String toolSignature) {
		this.toolSignature = toolSignature;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#getToolVersion()
	 */
	public String getToolVersion() {
		return toolVersion;
	}

	public void setToolVersion(String toolVersion) {
		this.toolVersion = toolVersion;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.IToolVO#isValid()
	 */
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("toolId", getToolId())
            .append("toolSignature", getToolSignature())
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
