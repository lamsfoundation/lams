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

/* $Id$ */
package org.lamsfoundation.lams.gradebook.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;

public interface IGradebookDAO extends IBaseDAO {

    GradebookUserLesson getGradebookUserDataForLesson(Long lessonID, Integer userID);

    List<GradebookUserLesson> getGradebookUserDataForLesson(Long lessonID);

    GradebookUserActivity getGradebookUserDataForActivity(Long activityID, Integer userID);

    Double getGradebookUserActivityMarkSum(Long lessonID, Integer userID);

    List<GradebookUserActivity> getAllGradebookUserActivitiesForActivity(Long activityID);

    Double getAverageMarkForLesson(Long lessonID);

    long getAverageDurationLesson(Long lessonID);

    long getAverageDurationForActivity(Long activityID);

    Double getAverageMarkForActivity(Long activityID);

    Double getAverageMarkForGroupedActivity(Long activityID, Long groupID);

    long getAverageDurationForGroupedActivity(Long activityID, Long groupID);

}
