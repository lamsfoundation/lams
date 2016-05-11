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

package org.lamsfoundation.lams.tool.dto;

import org.lamsfoundation.lams.tool.Tool;

/**
 * @author Manpreet Minhas
 *
 *         This class acts as a data transfer object for transferring information between FLASH and the core module.
 *
 *         This class is required in the authoring enviornment to pass information about the tool assocaited with a
 *         given ToolActivity
 */
public class AuthoringToolDTO {

    private Long toolID;
    private Long toolContentID;
    private String description;
    private String toolDisplayName;
    private Boolean supportsModeration;
    private Boolean supportsContribute;
    private String authoringURL;
    private String helpURL;

    public AuthoringToolDTO(Long toolID, Long toolContentID, String description, String toolDisplayName,
	    Boolean supportsModeration, Boolean supportsContribute, String authoringURL, String helpURL) {
	super();
	this.toolID = toolID;
	this.toolContentID = toolContentID;
	this.description = description;
	this.toolDisplayName = toolDisplayName;
	this.supportsModeration = supportsModeration;
	this.supportsContribute = supportsContribute;
	this.authoringURL = authoringURL;
	this.helpURL = helpURL;
    }

    public AuthoringToolDTO(Tool tool) {
	this.toolID = tool.getToolId();
	this.toolContentID = new Long(tool.getDefaultToolContentId());
	this.description = tool.getDescription();
	this.toolDisplayName = tool.getToolDisplayName();
	this.authoringURL = tool.getAuthorUrl();
	this.helpURL = tool.getHelpUrl();
    }

    /**
     * @return Returns the authoringURL.
     */
    public String getAuthoringURL() {
	return authoringURL;
    }

    /**
     *
     * @return Return the helpURL.
     */
    public String getHelpURL() {
	return helpURL;
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
     * @return Returns the supportsModeration.
     */
    public Boolean getSupportsModeration() {
	return supportsModeration;
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
