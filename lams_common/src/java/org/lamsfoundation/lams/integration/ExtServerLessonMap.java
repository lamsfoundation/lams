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


package org.lamsfoundation.lams.integration;

/**
 * Maps a lesson to multiple integrated server instances
 */
public class ExtServerLessonMap {

    private Long uid;
    private Long lessonId;
    private String resourceLinkId;
    private ExtServerOrgMap extServer;

    public ExtServerLessonMap() {
    }

    /**
     *
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */
    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }
    
    /**
     * Used only for LTI tool consumer servers. Stores value of the resource_link_id parameter. Which is an opaque
     * unique identifier that the TC guarantees will be unique within the TC for every placement of the link. If the
     * tool / activity is placed multiple times in the same context, each of those placements will be distinct. This
     * value will also change if the item is exported from one system or context and imported into another system or
     * context. This parameter is required.
     * 
     * @hibernate.property column="resource_link_id"
     *
     */
    public String getResourceLinkId() {
	return resourceLinkId;
    }

    public void setResourceLinkId(String resourceLinkId) {
	this.resourceLinkId = resourceLinkId;
    }

    /**
     *
     *
     *
     */
    public ExtServerOrgMap getExtServer() {
	return extServer;
    }

    public void setExtServer(ExtServerOrgMap extServer) {
	this.extServer = extServer;
    }
}
