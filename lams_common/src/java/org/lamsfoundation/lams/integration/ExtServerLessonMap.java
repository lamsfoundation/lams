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
package org.lamsfoundation.lams.integration;

/**
 * Maps a lesson to multiple integrated server instances
 *
 * @hibernate.class table="lams_ext_server_lesson_map"
 */
public class ExtServerLessonMap {

    private Long uid;
    private Long lessonId;
    private ExtServerOrgMap extServer;

    public ExtServerLessonMap() {
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="lesson_id" type="java.lang.Long" not-null="true"
     *
     */
    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="ext_server_org_map_id"
     *
     */
    public ExtServerOrgMap getExtServer() {
	return extServer;
    }

    public void setExtServer(ExtServerOrgMap extServer) {
	this.extServer = extServer;
    }
}
