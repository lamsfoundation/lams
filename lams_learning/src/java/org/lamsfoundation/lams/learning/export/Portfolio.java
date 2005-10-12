/***************************************************************************
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
* ***********************************************************************/

/*
 * Created on Aug 30, 2005
 *
 */
package org.lamsfoundation.lams.learning.export;

/**
 * @author mtruong
 *
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
	private String mainFileName;
	private String directoryName; //includes the parent path
	private String toolLink;
	
	
	/**
	 * @return Returns the toolLink.
	 */
	public String getToolLink() {
		return toolLink;
	}
	/**
	 * @param toolLink The toolLink to set.
	 */
	public void setToolLink(String toolLink) {
		this.toolLink = toolLink;
	}
	/**
	 * @return Returns the directoryName.
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName The directoryName to set.
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
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
		this.mainFileName = null;
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
	/**
	 * @return Returns the mainFileName.
	 */
	public String getMainFileName() {
		return mainFileName;
	}
	/**
	 * @param mainFileName The mainFileName to set.
	 */
	public void setMainFileName(String mainFileName) {
		this.mainFileName = mainFileName;
	}
	
	
}
