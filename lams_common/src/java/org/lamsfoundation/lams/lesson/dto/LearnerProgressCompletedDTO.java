/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.lesson.dto;

/**
 * The data transfer object for remoting data communication.
 *
 * @author Jacky Fang
 * @since 2005-3-11
 * @version 1.1
 *
 */
public class LearnerProgressCompletedDTO {

    private Long lessonId;
    private String lessonName;
    private String userName;
    private String lastName;
    private String firstName;
    private Integer learnerId;
    private CompletedActivityDTO[] completedActivities;
    private Boolean lessonComplete;

    private Long lessonStartTime;
    private Long learnerStartTime;

    /**
     * Full constructor
     */
    public LearnerProgressCompletedDTO(Long lessonId, String lessonName, String userName, String lastName,
	    String firstName, Integer learnerId, CompletedActivityDTO[] completedActivities, Boolean lessonComplete,
	    Long lessonStartTime, Long learnerStartTime) {
	this.lessonId = lessonId;
	this.lessonName = lessonName;
	this.userName = userName;
	this.lastName = lastName;
	this.firstName = firstName;
	this.learnerId = learnerId;
	this.completedActivities = completedActivities;
	this.lessonComplete = lessonComplete;
	this.lessonStartTime = lessonStartTime;
	this.learnerStartTime = learnerStartTime;
    }

    /**
     * @return Returns the learnerId.
     */
    public Integer getLearnerId() {
	return learnerId;
    }

    /**
     * @return Returns the lessonId.
     */
    public Long getLessonId() {
	return lessonId;
    }

    /**
     * @return Returns the lessonName.
     */
    public String getLessonName() {
	return lessonName;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
	return userName;
    }

    /**
     * @return Returns the completedActivities.
     */
    public CompletedActivityDTO[] getCompletedActivities() {
	return completedActivities;
    }

    public String getFirstName() {
	return firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public Boolean getLessonComplete() {
	return lessonComplete;
    }

    public Long getLessonStartTime() {
	return lessonStartTime;
    }

    public Long getLearnerStartTime() {
	return learnerStartTime;
    }

}
