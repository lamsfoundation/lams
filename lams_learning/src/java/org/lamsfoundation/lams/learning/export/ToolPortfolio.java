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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.export;

/**
 * @author mtruong
 *
 */
public class ToolPortfolio {
	
	private Long activityId;
	private String activityDescription;
	private String activityName;
	private String exportUrl;
	/* The link to the tool page from the main export page */
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

	public ToolPortfolio()
	{
		this.activityId = null;	
		this.activityDescription = null;
		this.activityName = null;
		this.exportUrl = null;
		this.toolLink = null;
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
	
	
}
