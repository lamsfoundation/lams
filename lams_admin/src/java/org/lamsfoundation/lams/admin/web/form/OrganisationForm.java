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
package org.lamsfoundation.lams.admin.web.form;

public class OrganisationForm {

    private Integer orgId;

    private Integer parentId;
    
    private Integer typeId;

    private String parentName;

    private String name;

    private String code;

    private String description;

    private Integer stateId;

    private boolean courseAdminCanAddNewUsers = false;

    private boolean courseAdminCanBrowseAllUsers = false;

    private boolean courseAdminCanChangeStatusOfCourse = false;

    private boolean enableCourseNotifications = false;

    private boolean enableGradebookForLearners = false;

    private boolean enableSingleActivityLessons = false;

    private boolean enableLiveEdit = true;

    private boolean enableKumalive = false;

    public Integer getOrgId() {
	return orgId;
    }

    public void setOrgId(Integer orgId) {
	this.orgId = orgId;
    }

    public Integer getParentId() {
	return parentId;
    }

    public void setParentId(Integer parentId) {
	this.parentId = parentId;
    }
   
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Integer getStateId() {
	return stateId;
    }

    public void setStateId(Integer stateId) {
	this.stateId = stateId;
    }

    public boolean isCourseAdminCanAddNewUsers() {
	return courseAdminCanAddNewUsers;
    }

    public void setCourseAdminCanAddNewUsers(boolean courseAdminCanAddNewUsers) {
	this.courseAdminCanAddNewUsers = courseAdminCanAddNewUsers;
    }

    public boolean isCourseAdminCanBrowseAllUsers() {
	return courseAdminCanBrowseAllUsers;
    }

    public void setCourseAdminCanBrowseAllUsers(boolean courseAdminCanBrowseAllUsers) {
	this.courseAdminCanBrowseAllUsers = courseAdminCanBrowseAllUsers;
    }

    public boolean isCourseAdminCanChangeStatusOfCourse() {
	return courseAdminCanChangeStatusOfCourse;
    }

    public void setCourseAdminCanChangeStatusOfCourse(boolean courseAdminCanChangeStatusOfCourse) {
	this.courseAdminCanChangeStatusOfCourse = courseAdminCanChangeStatusOfCourse;
    }

    public boolean isEnableCourseNotifications() {
	return enableCourseNotifications;
    }

    public void setEnableCourseNotifications(boolean enableCourseNotifications) {
	this.enableCourseNotifications = enableCourseNotifications;
    }

    public boolean isEnableGradebookForLearners() {
	return enableGradebookForLearners;
    }

    public void setEnableGradebookForLearners(boolean enableGradebookForLearners) {
	this.enableGradebookForLearners = enableGradebookForLearners;
    }

    public boolean isEnableSingleActivityLessons() {
	return enableSingleActivityLessons;
    }

    public void setEnableSingleActivityLessons(boolean enableSingleActivityLessons) {
	this.enableSingleActivityLessons = enableSingleActivityLessons;
    }

    public boolean isEnableLiveEdit() {
	return enableLiveEdit;
    }

    public void setEnableLiveEdit(boolean enableLiveEdit) {
	this.enableLiveEdit = enableLiveEdit;
    }

    public boolean isEnableKumalive() {
	return enableKumalive;
    }

    public void setEnableKumalive(boolean enableKumalive) {
	this.enableKumalive = enableKumalive;
    }

}
