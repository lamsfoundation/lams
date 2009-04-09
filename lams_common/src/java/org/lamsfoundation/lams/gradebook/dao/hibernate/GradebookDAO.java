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
package org.lamsfoundation.lams.gradebook.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;

public class GradebookDAO extends BaseDAO implements IGradebookDAO {

    private static final String GET_GRADEBOOK_USER_ACTIVITY = "from GradebookUserActivity gact where "
	    + "gact.learner.userId=:userID and gact.activity.activityId=:activityID";

    private static final String GET_GRADEBOOK_USER_LESSON = "from GradebookUserLesson gles where "
	    + "gles.learner.userId=:userID and gles.lesson.lessonId=:lessonID";

    private static final String GET_GRADEBOOK_ACTIVITIES_FROM_LESSON_SUM = "select sum(gact.mark) from GradebookUserActivity gact, ToolSession tses where "
	    + "gact.learner.userId=:userID and tses.toolActivity=gact.activity and tses.lesson.lessonId=:lessonID";

    private static final String GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY = "from GradebookUserActivity gact where "
	    + "gact.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_LESSON = "select avg(gles.mark) from GradebookUserLesson gles where "
	    + "gles.lesson.lessonId=:lessonID";

    private static final String GET_AVERAGE_COMPLETION_TIME = "select prog.finishDate, prog.startDate from LearnerProgress prog where "
	    + "prog.lesson.lessonId=:lessonID";

    private static final String GET_AVERAGE_COMPLETION_TIME_ACTIVITY = "select compProg.finishDate, compProg.startDate from CompletedActivityProgress compProg, Activity act where "
	    + "compProg.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_ACTIVTY = "select avg(gact.mark) from GradebookUserActivity gact where "
	    + "gact.activity.activityId=:activityID";

    @SuppressWarnings("unchecked")
    public GradebookUserActivity getGradebookUserDataForActivity(Long activityID, Integer userID) {
	List result = getSession().createQuery(GET_GRADEBOOK_USER_ACTIVITY).setInteger("userID", userID.intValue())
		.setLong("activityID", activityID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (GradebookUserActivity) result.get(0);
	}

	return null;
    }

    @SuppressWarnings("unchecked")
    public GradebookUserLesson getGradebookUserDataForLesson(Long lessonID, Integer userID) {
	List result = getSession().createQuery(GET_GRADEBOOK_USER_LESSON).setInteger("userID", userID.intValue())
		.setLong("lessonID", lessonID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (GradebookUserLesson) result.get(0);
	}

	return null;

    }

    @SuppressWarnings("unchecked")
    public Double getGradebookUserActivityMarkSum(Long lessonID, Integer userID) {
	List result = getSession().createQuery(GET_GRADEBOOK_ACTIVITIES_FROM_LESSON_SUM).setInteger("userID",
		userID.intValue()).setLong("lessonID", lessonID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;

    }

    @SuppressWarnings("unchecked")
    public List<GradebookUserActivity> getAllGradebookUserActivitiesForActivity(Long activityID) {
	List result = getSession().createQuery(GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY).setLong("activityID",
		activityID.longValue()).list();

	return (List<GradebookUserActivity>) result;
    }

    @SuppressWarnings("unchecked")
    public Double getAverageMarkForLesson(Long lessonID) {
	List result = getSession().createQuery(GET_AVERAGE_MARK_FOR_LESSON).setLong("lessonID", lessonID.longValue())
		.list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;
    }

    @SuppressWarnings("unchecked")
    public long getAverageDurationLesson(Long lessonID) {
	List<Object[]> result = (List<Object[]>) getSession().createQuery(GET_AVERAGE_COMPLETION_TIME).setLong(
		"lessonID", lessonID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0) {

		long sum = 0;
		long count = 0;
		for (Object[] dateObjs : result) {
		    if (dateObjs != null && dateObjs.length == 2) {
			Date finishDate = (Date) dateObjs[0];
			Date startDate = (Date) dateObjs[1];

			if (startDate != null && finishDate != null) {

			    sum += finishDate.getTime() - startDate.getTime();
			    count++;
			}
		    }
		}

		if (count > 0) {
		    return sum / count;
		}
	    }

	}
	return 0;
    }

    @SuppressWarnings("unchecked")
    public long getAverageDurationForActivity(Long activityID) {
	List<Object[]> result = (List<Object[]>) getSession().createQuery(GET_AVERAGE_COMPLETION_TIME_ACTIVITY)
		.setLong("activityID", activityID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0) {

		long sum = 0;
		long count = 0;
		for (Object[] dateObjs : result) {
		    if (dateObjs != null && dateObjs.length == 2) {
			Date finishDate = (Date) dateObjs[0];
			Date startDate = (Date) dateObjs[1];

			if (startDate != null && finishDate != null) {

			    sum += finishDate.getTime() - startDate.getTime();
			    count++;
			}
		    }
		}

		if (count > 0) {
		    return sum / count;
		}
	    }

	}
	return 0;
    }

    @SuppressWarnings("unchecked")
    public Double getAverageMarkForActivity(Long activityID) {
	List result = getSession().createQuery(GET_AVERAGE_MARK_FOR_ACTIVTY).setLong("activityID", activityID.longValue())
		.list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;

    }
}
