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

package org.lamsfoundation.lams.authoring.dto;

import java.util.Vector;

/**
 * DTO created so the valid flag and any other messages
 * can be sent back to Flash.
 *
 * @author mtruong
 *
 */
public class StoreLearningDesignResultsDTO {

    private boolean valid; //indicates whether learning design is valid or not

    private Long learningDesignID;

    /*
     * If the design is valid, then the learningDesignID will be returned.
     * Otherwise will return a list of ValidationErrorDTOs
     */
    private Vector messages;

    private Vector activities;

    public StoreLearningDesignResultsDTO() {
    }

    public StoreLearningDesignResultsDTO(boolean valid, Vector activities, Long learningDesignID) {
	this.valid = valid;
	this.activities = activities;
	this.learningDesignID = learningDesignID;
    }

    public StoreLearningDesignResultsDTO(boolean valid, Vector messages, Vector activities, Long learningDesignID) {
	this.valid = valid;
	this.messages = messages;
	this.activities = activities;
	this.learningDesignID = learningDesignID;
    }

    public StoreLearningDesignResultsDTO(boolean valid, Long learningDesignID) {
	this.valid = valid;
	this.learningDesignID = learningDesignID;
    }

    public Long getLearningDesignID() {
	return learningDesignID;
    }

    public boolean isValid() {
	return valid;
    }

    public Vector getMessages() {
	return messages;
    }

    public Vector getActivities() {
	return activities;
    }
}
