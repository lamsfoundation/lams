package org.lamsfoundation.lams.tool;

import java.util.Date;

public interface IToolVO {

	public abstract String getAuthorUrl();

	public abstract void setAuthorUrl(String authorUrl);

	public abstract String getContributeUrl();

	public abstract void setContributeUrl(String contributeUrl);

	public abstract Date getCreateDateTime();

	public abstract void setCreateDateTime(Date createDateTime);

	public abstract long getDefaultToolContentId();

	public abstract void setDefaultToolContentId(long defaultToolContentId);

	public abstract String getDefineLaterUrl();

	public abstract void setDefineLaterUrl(String defineLaterUrl);

	public abstract String getDescription();

	public abstract String getExportPortfolioClassUrl();

	public abstract String getExportPortfolioLearnerUrl();

	public abstract Integer getGroupingSupportTypeId();

	public abstract String getLanguageFile();

	public abstract String getLearnerPreviewUrl();

	public abstract String getLearnerProgressUrl();

	public abstract String getLearnerUrl();

	public abstract String getModerationUrl();

	public abstract String getMonitorUrl();

	public abstract String getServiceName();

	public abstract boolean isSupportsGrouping();

	public abstract boolean isSupportsRunOffline();

	public abstract String getToolDisplayName();

	public abstract Long getToolId();

	public abstract String getToolIdentifier();

	public abstract String getToolSignature();

	public abstract String getToolVersion();

	public abstract boolean isValid();

}