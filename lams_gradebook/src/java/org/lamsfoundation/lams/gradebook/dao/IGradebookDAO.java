/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.gradebook.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.model.GradebookUserActivityArchive;
import org.lamsfoundation.lams.gradebook.model.GradebookUserLessonArchive;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

public interface IGradebookDAO extends IBaseDAO {

    GradebookUserLesson getGradebookUserDataForLesson(Long lessonID, Integer userID);

    List<GradebookUserLesson> getGradebookUserDataForLesson(Long lessonID);

    GradebookUserActivity getGradebookUserDataForActivity(Long activityID, Integer userID);

    List<GradebookUserActivity> getGradebookUserActivitiesForLesson(Long lessonID, Integer userID);

//    Double getGradebookUserActivityMarkSum(Long lessonID, Integer userID);

    List<GradebookUserActivity> getAllGradebookUserActivitiesForActivity(Long activityID);

    /**
     * The same as getAllGradebookUserActivitiesForActivity(Long activityID) except it returns not all
     * GradebookUserActivity but for specified users.
     *
     * @param activityID
     * @param userIds
     * @return
     */
    List<GradebookUserActivity> getGradebookUserActivitiesForActivity(Long activityID, List<Integer> userIds);

    Double getAverageMarkForLesson(Long lessonID);

    long getMedianTimeTakenLesson(Long lessonID);

    long getMedianTimeTakenForActivity(Long activityID);

    long getMinTimeTakenForActivity(Long activityID);

    long getMaxTimeTakenForActivity(Long activityID);

    Double getAverageMarkForActivity(Long activityID);

    Double getAverageMarkForGroupedActivity(Long activityID, Long groupID);

    long getMedianTimeTakenForGroupedActivity(Long activityID, Long groupID);

    long getMinTimeTakenForGroupedActivity(Long activityID, Long groupID);

    long getMaxTimeTakenForGroupedActivity(Long activityID, Long groupID);

    List<Lesson> getLessonsByGroupAndUser(final Integer userId, boolean staffOnly, final Integer orgId, int page,
	    int size, String sortBy, String sortOrder, String searchString);

    List<User> getLearnersByLesson(Long lessonId, int page, int size, String sortBy, String sortOrder,
	    String searchString);

    List<User> getUsersByGroup(Long lessonId, Long activityId, Long groupId, int page, int size, String sortBy,
	    String sortOrder, String searchString);

    List<User> getLearnersByActivity(Long lessonId, Long activityId, int page, int size, String sortBy, String sortOrder,
	    String searchString);

    int getCountUsersByLesson(Long lessonId, String searchString);

    List<User> getLearnersFromOrganisation(Integer orgId, int page, int size, String sortOrder, String searchString);

    int getCountUsersByOrganisation(Integer orgId, String searchString);

    /**
     * Get all GradebookUserLessons by lessonId.
     *
     * @param lesson
     * @return
     */
    List<GradebookUserLesson> getGradebookUserLessons(Lesson lesson);

    /**
     * Get GradebookUserLessons from lesson and restricted userIds list.
     *
     * @param lesson
     * @param userIds
     * @return
     */
    List<GradebookUserLesson> getGradebookUserLessons(Lesson lesson, List<Integer> userIds);

    List<GradebookUserLesson> getGradebookUserLessons(List<Long> lessonIds);

    List<Number> getAllMarksForLesson(Long lessonID);

    boolean hasArchivedMarks(Long lessonId, Integer userId);

    List<GradebookUserLessonArchive> getArchivedLessonMarks(Long lessonId, Integer userId);

    List<GradebookUserActivityArchive> getArchivedActivityMarks(Long activityId, Integer userId);
}
