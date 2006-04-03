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
package org.lamsfoundation.lams.tool.dto;

import org.lamsfoundation.lams.tool.Tool;

/**
 * @author Manpreet Minhas
 * 
 * This class acts as a data transfer object for
 * transferring information between FLASH and the core module. 
 * 
 * This class is required in the authoring enviornment to pass
 * information about the tool assocaited with a given ToolActivity 
 */
public class AuthoringToolDTO {
	
	private Long toolID;
	private Long toolContentID;
	private String description;
	private String toolDisplayName;	
	private Boolean supportsDefineLater;
	private Boolean supportsRunOffline;
	private Boolean supportsModeration;
	private Boolean supportsContribute;	
	private String authoringURL;
	
	public AuthoringToolDTO(Long toolID, Long toolContentID,
			String description, String toolDisplayName,
			Boolean supportsDefineLater, Boolean supportsRunOffline,
			Boolean supportsModeration, Boolean supportsContribute,
			String authoringURL) {
		super();
		this.toolID = toolID;
		this.toolContentID = toolContentID;
		this.description = description;
		this.toolDisplayName = toolDisplayName;
		this.supportsDefineLater = supportsDefineLater;
		this.supportsRunOffline = supportsRunOffline;
		this.supportsModeration = supportsModeration;
		this.supportsContribute = supportsContribute;
		this.authoringURL = authoringURL;
	}
	public AuthoringToolDTO(Tool tool){
		this.toolID = tool.getToolId();
		this.toolContentID = new Long(tool.getDefaultToolContentId());
		this.description = tool.getDescription();
		this.toolDisplayName = tool.getToolDisplayName();
		this.supportsDefineLater = new Boolean(tool.getSupportsDefineLater());
		this.supportsRunOffline = new Boolean(tool.getSupportsRunOffline());
		this.supportsModeration = new Boolean(tool.getSupportsModeration());
		this.supportsContribute = new Boolean(tool.getSupportsContribute());
		this.authoringURL = tool.getAuthorUrl();
	}
	/**
	 * @return Returns the authoringURL.
	 */
	public String getAuthoringURL() {
		return authoringURL;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return Returns the supportsContribute.
	 */
	public Boolean getSupportsContribute() {
		return supportsContribute;
	}
	/**
	 * @return Returns the supportsDefineLater.
	 */
	public Boolean getSupportsDefineLater() {
		return supportsDefineLater;
	}
	/**
	 * @return Returns the supportsModeration.
	 */
	public Boolean getSupportsModeration() {
		return supportsModeration;
	}
	/**
	 * @return Returns the supportsRunOffline.
	 */
	public Boolean getSupportsRunOffline() {
		return supportsRunOffline;
	}
	/**
	 * @return Returns the toolContentID.
	 */
	public Long getToolContentID() {
		return toolContentID;
	}
	/**
	 * @return Returns the toolDisplayName.
	 */
	public String getToolDisplayName() {
		return toolDisplayName;
	}
	/**
	 * @return Returns the toolID.
	 */
	public Long getToolID() {
		return toolID;
	}
}
