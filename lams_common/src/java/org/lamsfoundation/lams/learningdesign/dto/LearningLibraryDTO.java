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

package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningLibrary;

/**
 * @author Manpreet Minhas
 *
 *         This class acts as a data transfer object for transfering
 *         information between Authoring and the core module. It passes
 *         information about all available Learning Libraries.
 *
 */
public class LearningLibraryDTO extends BaseDTO {

    private Long learningLibraryID;
    private String description;
    private String title;
    private Boolean validFlag;
    private Date createDateTime;
    private Vector templateActivities;

    public LearningLibraryDTO() {

    }

    public LearningLibraryDTO(LearningLibrary learningLibrary) {
	this.learningLibraryID = learningLibrary.getLearningLibraryId();
	this.description = learningLibrary.getDescription();
	this.title = learningLibrary.getTitle();
	this.validFlag = learningLibrary.getValidLibrary();
	this.createDateTime = learningLibrary.getCreateDateTime();
	// this.templateActivities = populateActivities(templateActivity.iterator(), languageCode);
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the learningLibraryID.
     */
    public Long getLearningLibraryID() {
	return learningLibraryID;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @return Returns the validFlag.
     */
    public Boolean getValidFlag() {
	return validFlag;
    }

    /**
     * @return Returns the activities.
     */
    public Vector getTemplateActivities() {
	return templateActivities;
    }

    public void setTemplateActivities(Vector templateActivities) {
	this.templateActivities = templateActivities;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }
}
