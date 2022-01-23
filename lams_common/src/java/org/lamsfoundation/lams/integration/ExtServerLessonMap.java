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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Maps a lesson to multiple integrated server instances
 */
@Entity
@Table(name = "lams_ext_server_lesson_map")
public class ExtServerLessonMap {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "resource_link_id")
    private String resourceLinkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ext_server_org_map_id")
    private ExtServer extServer;

    @Column(name = "grading_type")
    private String gradingType;

    @Column(name = "grading_url")
    private String gradingUrl;

    public ExtServerLessonMap() {
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

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
     */
    public String getResourceLinkId() {
	return resourceLinkId;
    }

    public void setResourceLinkId(String resourceLinkId) {
	this.resourceLinkId = resourceLinkId;
    }

    public ExtServer getExtServer() {
	return extServer;
    }

    public void setExtServer(ExtServer extServer) {
	this.extServer = extServer;
    }

    public String getGradingType() {
	return gradingType;
    }

    public void setGradingType(String gradingType) {
	this.gradingType = gradingType;
    }

    public String getGradingUrl() {
	return gradingUrl;
    }

    public void setGradingUrl(String gradingUrl) {
	this.gradingUrl = gradingUrl;
    }
}