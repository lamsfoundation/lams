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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.authoring.dto;

public class PedagogicalPlannerActivityDTO {
    private String pedagogicalPlannerUrl;
    private Long toolContentID;
    private String toolIconUrl;
    private String title;
    private String type;

    public String getPedagogicalPlannerUrl() {
	return pedagogicalPlannerUrl;
    }

    public void setPedagogicalPlannerUrl(String toolSignature) {
	pedagogicalPlannerUrl = toolSignature;
    }

    public Long getToolContentID() {
	return toolContentID;
    }

    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
    }

    public PedagogicalPlannerActivityDTO(Long toolContentID, String type, String title, String pedagogicalPlannerUrl,
	    String toolIconUrl) {
	this.pedagogicalPlannerUrl = pedagogicalPlannerUrl;
	this.toolContentID = toolContentID;
	this.toolIconUrl = toolIconUrl;
	this.title = title;
	this.type = type;
    }

    public String getToolIconUrl() {
	return toolIconUrl;
    }

    public void setToolIconUrl(String toolIconUrl) {
	this.toolIconUrl = toolIconUrl;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }
}