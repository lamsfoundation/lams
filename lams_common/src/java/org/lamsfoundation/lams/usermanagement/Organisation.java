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

package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;

public class Organisation implements Serializable, Comparable {
    private static final long serialVersionUID = -6742443056151585129L;

    /** identifier field */
    private Integer organisationId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String code;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Organisation parentOrganisation;

    /** persistent field */
    private Date createDate;

    /** persistent field */
    private User createdBy;

    /** persistent field */
    private Set<WorkspaceFolder> workspaceFolders;

    /** persistent field */
    private OrganisationType organisationType;

    /** persistent field */
    private Set userOrganisations;

    /** persistent field */
    private Set childOrganisations;

    /** persistent field */
    private Set lessons;

    private OrganisationState organisationState;

    private SupportedLocale locale;

    /** persistent field */
    private Boolean courseAdminCanAddNewUsers;

    /** persistent field */
    private Boolean courseAdminCanBrowseAllUsers;

    /** persistent field */
    private Boolean courseAdminCanChangeStatusOfCourse;

    /** persistent field */
    private Boolean courseAdminCanCreateGuestAccounts;

    /** persistent field */
    private Boolean enableCourseNotifications;

    /** persistent field */
    private Boolean enableGradebookForLearners;

    /** persistent field */
    private Boolean enableSingleActivityLessons;

    /** persistent field */
    private Boolean enableLiveEdit;

    /** persistent field */
    private Boolean enableKumalive;

    /** persistent field */
    private Date archivedDate;

    private String orderedLessonIds;

    /** default constructor */
    public Organisation() {
	this.courseAdminCanAddNewUsers = Boolean.FALSE;
	this.courseAdminCanBrowseAllUsers = Boolean.FALSE;
	this.courseAdminCanChangeStatusOfCourse = Boolean.FALSE;
	this.courseAdminCanCreateGuestAccounts = Boolean.FALSE;
	this.enableCourseNotifications = Boolean.FALSE;
	this.enableGradebookForLearners = Boolean.FALSE;
	this.enableSingleActivityLessons = Boolean.FALSE;
	this.enableLiveEdit = Boolean.FALSE;
    }

    public Integer getOrganisationId() {
	return this.organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
	this.organisationId = organisationId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Organisation getParentOrganisation() {
	return this.parentOrganisation;
    }

    public void setParentOrganisation(Organisation parentOrganisation) {
	this.parentOrganisation = parentOrganisation;
    }

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public User getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(User createdBy) {
	this.createdBy = createdBy;
    }

    public Set<WorkspaceFolder> getWorkspaceFolders() {
	return this.workspaceFolders;
    }

    public WorkspaceFolder getNormalFolder() {
	if (workspaceFolders != null) {
	    for (WorkspaceFolder workspaceFolder : workspaceFolders) {
		if (WorkspaceFolder.NORMAL.equals(workspaceFolder.getWorkspaceFolderType())) {
		    return workspaceFolder;
		}
	    }
	}
	return null;
    }

    public WorkspaceFolder getRunSequencesFolder() {
	if (workspaceFolders != null) {
	    for (WorkspaceFolder workspaceFolder : workspaceFolders) {
		if (WorkspaceFolder.RUN_SEQUENCES.equals(workspaceFolder.getWorkspaceFolderType())) {
		    return workspaceFolder;
		}
	    }
	}
	return null;
    }

    public void setWorkspaceFolders(Set<WorkspaceFolder> workspaceFolders) {
	this.workspaceFolders = workspaceFolders;
    }

    public OrganisationType getOrganisationType() {
	return this.organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {
	this.organisationType = organisationType;
    }

    public Set getUserOrganisations() {
	return this.userOrganisations;
    }

    public void setUserOrganisations(Set userOrganisations) {
	this.userOrganisations = userOrganisations;
    }

    public Set getChildOrganisations() {
	return childOrganisations;
    }

    public void setChildOrganisations(Set childOrganisations) {
	this.childOrganisations = childOrganisations;
    }

    public Set getLessons() {
	return this.lessons;
    }

    public void setLessons(Set lessons) {
	this.lessons = lessons;
    }

    public OrganisationState getOrganisationState() {
	return this.organisationState;
    }

    public void setOrganisationState(OrganisationState organisationState) {
	this.organisationState = organisationState;
    }

    public Boolean getCourseAdminCanAddNewUsers() {
	return this.courseAdminCanAddNewUsers;
    }

    public void setCourseAdminCanAddNewUsers(Boolean courseAdminCanAddNewUsers) {
	this.courseAdminCanAddNewUsers = courseAdminCanAddNewUsers;
    }

    public Boolean getCourseAdminCanBrowseAllUsers() {
	return this.courseAdminCanBrowseAllUsers;
    }

    public void setCourseAdminCanBrowseAllUsers(Boolean courseAdminCanBrowseAllUsers) {
	this.courseAdminCanBrowseAllUsers = courseAdminCanBrowseAllUsers;
    }

    public Boolean getCourseAdminCanChangeStatusOfCourse() {
	return this.courseAdminCanChangeStatusOfCourse;
    }

    public void setCourseAdminCanChangeStatusOfCourse(Boolean courseAdminCanChangeStatusOfCourse) {
	this.courseAdminCanChangeStatusOfCourse = courseAdminCanChangeStatusOfCourse;
    }

    public Boolean getCourseAdminCanCreateGuestAccounts() {
	return this.courseAdminCanCreateGuestAccounts;
    }

    public void setCourseAdminCanCreateGuestAccounts(Boolean courseAdminCanCreateGuestAccounts) {
	this.courseAdminCanCreateGuestAccounts = courseAdminCanCreateGuestAccounts;
    }

    public Boolean getEnableCourseNotifications() {
	return this.enableCourseNotifications;
    }

    public void setEnableCourseNotifications(Boolean enableCourseNotifications) {
	this.enableCourseNotifications = enableCourseNotifications;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("organisationId", getOrganisationId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof Organisation)) {
	    return false;
	}
	Organisation castOther = (Organisation) other;
	return new EqualsBuilder().append(this.getOrganisationId(), castOther.getOrganisationId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getOrganisationId()).toHashCode();
    }

    public OrganisationDTO getOrganisationDTO() {
	return new OrganisationDTO(this);
    }

    public SupportedLocale getLocale() {
	return locale;
    }

    public void setLocale(SupportedLocale locale) {
	this.locale = locale;
    }

    @Override
    public int compareTo(Object o) {
	return name.compareToIgnoreCase(((Organisation) o).getName());
    }

    public Date getArchivedDate() {
	return this.archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
	this.archivedDate = archivedDate;
    }

    public String getOrderedLessonIds() {
	return orderedLessonIds;
    }

    public void setOrderedLessonIds(String orderedLessonIds) {
	this.orderedLessonIds = orderedLessonIds;
    }

    public Boolean getEnableGradebookForLearners() {
	return enableGradebookForLearners;
    }

    public void setEnableGradebookForLearners(Boolean enableGradebookForLearners) {
	this.enableGradebookForLearners = enableGradebookForLearners;
    }

    public Boolean getEnableSingleActivityLessons() {
	return enableSingleActivityLessons;
    }

    public void setEnableSingleActivityLessons(Boolean enableSingleActivityLessons) {
	this.enableSingleActivityLessons = enableSingleActivityLessons;
    }

    public Boolean getEnableLiveEdit() {
	return enableLiveEdit;
    }

    public void setEnableLiveEdit(Boolean enableLiveEdit) {
	this.enableLiveEdit = enableLiveEdit;
    }

    public Boolean getEnableKumalive() {
	return enableKumalive;
    }

    public void setEnableKumalive(Boolean enableKumalive) {
	this.enableKumalive = enableKumalive;
    }
}