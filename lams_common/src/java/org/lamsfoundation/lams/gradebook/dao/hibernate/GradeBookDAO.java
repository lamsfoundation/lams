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

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.gradebook.GradeBookUserActivity;
import org.lamsfoundation.lams.gradebook.GradeBookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradeBookDAO;

public class GradeBookDAO extends BaseDAO implements IGradeBookDAO {

    private static final String GET_GRADEBOOK_USER_ACTIVITY = "from GradeBookUserActivity gact where "
	    + "gact.learner.userId=:userID and gact.activity.activityId=:activityID";

    private static final String GET_GRADEBOOK_USER_LESSON = "from GradeBookUserLesson gles where "
	    + "gles.learner.userId=:userID and gles.lesson.lessonId=:lessonID";

//    private static final String GET_GRADEBOOK_ACTIVITIES_FROM_LESSON = "select sum(gact.mark) from GradeBookUserActivity gact, Lesson lesson where "
//	    + "gact.learner.userId=:userID and lesson.lessonId=:lessonID and gact.activity in lesson.learningDesign.activities";


    private static final String GET_GRADEBOOK_ACTIVITIES_FROM_LESSON = "select sum(gact.mark) from GradeBookUserActivity gact, ToolSession tses where "
	    + "gact.learner.userId=:userID and tses.toolActivity=gact.activity and tses.lesson.lessonId=:lessonID";

    
    @SuppressWarnings("unchecked")
    public GradeBookUserActivity getGradeBookUserDataForActivity(Long activityID, Integer userID) {
	List result = getSession().createQuery(GET_GRADEBOOK_USER_ACTIVITY).setInteger("userID", userID.intValue())
		.setLong("activityID", activityID.longValue()).list();

	if (result != null && result.size() > 0) {
	    return (GradeBookUserActivity) result.get(0);
	} else {
	    return null;
	}
    }

    @SuppressWarnings("unchecked")
    public GradeBookUserLesson getGradeBookUserDataForLesson(Long lessonID, Integer userID) {
	List result = getSession().createQuery(GET_GRADEBOOK_USER_LESSON).setInteger("userID", userID.intValue())
		.setLong("lessonID", lessonID.longValue()).list();

	if (result != null && result.size() > 0) {
	    return (GradeBookUserLesson) result.get(0);
	} else {
	    return null;
	}
    }

    @SuppressWarnings("unchecked")
    public Double getGradeBookUserActivityMarkSum(Long lessonID, Integer userID) {
	List result = getSession().createQuery(GET_GRADEBOOK_ACTIVITIES_FROM_LESSON).setInteger("userID",
		userID.intValue()).setLong("lessonID", lessonID.longValue()).list();
	
	if (result != null && result.size() > 0) {
	    return (Double) result.get(0);
	} else {
	    return 0.0;
	}

    }
}
