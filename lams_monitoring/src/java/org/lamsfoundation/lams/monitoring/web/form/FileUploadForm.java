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

package org.lamsfoundation.lams.monitoring.web.form;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadForm {

    private static final long serialVersionUID = -2841551690414943725L;

    private Integer organisationID;
    private Long lessonID;
    private Long activityID;
    private MultipartFile groupUploadFile;

    public Integer getOrganisationID() {
	return organisationID;
    }

    public void setOrganisationID(Integer organisationID) {
	this.organisationID = organisationID;
    }

    public Long getLessonID() {
	return lessonID;
    }

    public void setLessonID(Long lessonID) {
	this.lessonID = lessonID;
    }

    public Long getActivityID() {
	return activityID;
    }

    public void setActivityID(Long activityID) {
	this.activityID = activityID;
    }

    public MultipartFile getGroupUploadFile() {
	return groupUploadFile;
    }

    public void setGroupUploadFile(MultipartFile groupUploadFile) {
	this.groupUploadFile = groupUploadFile;
    }
}
