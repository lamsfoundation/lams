/*
 * Created on Aug 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Portfolio {
	
	private Long activityId;
	private Long toolSessionId;
	private Long userId;
	private String activityDescription;
	private String activityName;
	private String exportUrl;
	private String exportContent; //the html content of the export
	private Long toolContentId;

	
	public Portfolio()
	{
		this.activityId = null;
		this.toolSessionId = null;
		this.userId = null;
		this.activityDescription = null;
		this.activityName = null;
		this.exportUrl = null;
		this.exportContent = null;
		this.toolContentId = null;
	}
	
	

	/**
	 * @return Returns the activityName.
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName The activityName to set.
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return Returns the content.
	 */
	public String getExportContent() {
		return exportContent;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String exportContent) {
		this.exportContent = exportContent;
	}
	/**
	 * @return Returns the exportUrl.
	 */
	public String getExportUrl() {
		return exportUrl;
	}
	/**
	 * @param exportUrl The exportUrl to set.
	 */
	public void setExportUrl(String exportUrl) {
		this.exportUrl = exportUrl;
	}
	
	
	/**
	 * @return Returns the toolSessionId.
	 */
	public Long getToolSessionId() {
		return toolSessionId;
	}
	/**
	 * @param toolSessionId The toolSessionId to set.
	 */
	public void setToolSessionId(Long toolSessionId) {
		this.toolSessionId = toolSessionId;
	}
	/**
	 * @return Returns the activityId.
	 */
	public Long getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the userId.
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @param exportContent The exportContent to set.
	 */
	public void setExportContent(String exportContent) {
		this.exportContent = exportContent;
	}
	/**
	 * @return Returns the activityDescription.
	 */
	public String getActivityDescription() {
		return activityDescription;
	}
	/**
	 * @param activityDescription The activityDescription to set.
	 */
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
	/**
	 * @return Returns the toolContentId.
	 */
	public Long getToolContentId() {
		return toolContentId;
	}
	/**
	 * @param toolContentId The toolContentId to set.
	 */
	public void setToolContentId(Long toolContentId) {
		this.toolContentId = toolContentId;
	}
}
