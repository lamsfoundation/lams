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

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;
import org.springframework.stereotype.Repository;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

@Repository
public class GradebookDAO extends LAMSBaseDAO implements IGradebookDAO {

    private static final String GET_GRADEBOOK_USER_ACTIVITY = "from GradebookUserActivity gact where "
	    + "gact.learner.userId=:userID and gact.activity.activityId=:activityID";

    private static final String GET_GRADEBOOK_USER_LESSON = "from GradebookUserLesson gles where "
	    + "gles.learner.userId=:userID and gles.lesson.lessonId=:lessonID";
    
    private static final String GET_GRADEBOOK_USER_LESSONS = "from GradebookUserLesson gles where "
	    + "gles.lesson.lessonId=:lessonID";

    private static final String GET_GRADEBOOK_ACTIVITIES_FROM_LESSON_SUM = "select sum(gact.mark) from GradebookUserActivity gact where " +
    		"gact.learner=:userID and gact.activity in (select distinct tses.toolActivity from ToolSession tses where tses.lesson=:lessonID)";

    private static final String GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY = "FROM GradebookUserActivity gact where "
	    + "gact.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_LESSON = "SELECT AVG(gles.mark) FROM GradebookUserLesson gles WHERE "
	    + "gles.lesson.lessonId=:lessonID";

    private static final String GET_AVERAGE_COMPLETION_TIME = "select prog.finishDate, prog.startDate FROM LearnerProgress prog where "
	    + "prog.lesson.lessonId=:lessonID";
    
	final String GET_AVERAGE_COMPLETION_TIME2 = "SELECT AVG(TIMEDIFF(progress.finishDate,progress.startDate)) " +
		"FROM LearnerProgress progress " +
		"WHERE progress.lesson.lessonId = :lessonId ";

    private static final String GET_AVERAGE_COMPLETION_TIME_ACTIVITY = "select compProg.finishDate, compProg.startDate from CompletedActivityProgress compProg where "
	    + "compProg.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_ACTIVTY = "select avg(gact.mark) from GradebookUserActivity gact where "
	    + "gact.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_GROUPED_ACTIVTY = "select avg(gact.mark) from GradebookUserActivity gact, GroupUser gu, Group grp where "
	    + "gact.activity.activityId=:activityID and grp.groupId=:groupID and gu.user=gact.learner and gu.group=grp";

    private static final String GET_AVERAGE_COMPLETION_TIME_GROUPED_ACTIVITY = "select compProg.finishDate, compProg.startDate from CompletedActivityProgress compProg, Group grp, GroupUser gu where "
	    + "compProg.activity.activityId=:activityID and grp.groupId=:groupID and gu.user=compProg.learnerProgress.user and gu.group=grp";

    @SuppressWarnings("unchecked")
    public GradebookUserActivity getGradebookUserDataForActivity(Long activityID, Integer userID) {
	List result = getSessionFactory().getCurrentSession().createQuery(GET_GRADEBOOK_USER_ACTIVITY).setInteger("userID", userID.intValue())
		.setLong("activityID", activityID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (GradebookUserActivity) result.get(0);
	}

	return null;
    }

    @SuppressWarnings("unchecked")
    public GradebookUserLesson getGradebookUserDataForLesson(Long lessonID, Integer userID) {
	List result = getSessionFactory().getCurrentSession().createQuery(GET_GRADEBOOK_USER_LESSON).setInteger("userID", userID.intValue())
		.setLong("lessonID", lessonID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (GradebookUserLesson) result.get(0);
	}

	return null;
    }
    
    @SuppressWarnings("unchecked")
    public List<GradebookUserLesson> getGradebookUserDataForLesson(Long lessonID) {
	List<GradebookUserLesson> result = getSession().createQuery(GET_GRADEBOOK_USER_LESSONS).setLong("lessonID", lessonID.longValue())
		.list();
	
	 return result;
    }

    @SuppressWarnings("unchecked")
    public Double getGradebookUserActivityMarkSum(Long lessonID, Integer userID) {
	List result = getSessionFactory().getCurrentSession().createQuery(GET_GRADEBOOK_ACTIVITIES_FROM_LESSON_SUM)
		.setInteger("userID", userID.intValue()).setLong("lessonID", lessonID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GradebookUserActivity> getAllGradebookUserActivitiesForActivity(Long activityID) {
	List result = getSessionFactory().getCurrentSession().createQuery(GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY)
		.setLong("activityID", activityID.longValue()).list();

	return (List<GradebookUserActivity>) result;
    }
    
    @Override
    public List<GradebookUserActivity> getGradebookUserActivitiesForActivity(Long activityID, List<Integer> userIds) {
	final String GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY = "FROM GradebookUserActivity gact where "
		+ "gact.activity.activityId=:activityID AND gact.learner.userId IN (:userIds)";
	
	List result = getSession().createQuery(GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY)
		.setLong("activityID", activityID.longValue()).setParameterList("userIds", userIds).list();

	return (List<GradebookUserActivity>) result;
    }

    @SuppressWarnings("unchecked")
    public Double getAverageMarkForLesson(Long lessonID) {
	List result = getSessionFactory().getCurrentSession().createQuery(GET_AVERAGE_MARK_FOR_LESSON).setLong("lessonID", lessonID.longValue())
		.list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;
    }

    @SuppressWarnings("unchecked")
    public long getAverageDurationLesson(Long lessonID) {
	
	final String GET_AVERAGE_COMPLETION_TIME2 = "SELECT AVG(TIMEDIFF(progress.finishDate,progress.startDate)) " +
		"FROM LearnerProgress progress " +
		"WHERE progress.lesson.lessonId = :lessonId ";
	
	List result = getSession().createQuery(GET_AVERAGE_COMPLETION_TIME2).setLong("lessonId", lessonID)
		.list();

	if (result == null || result.size() == 0 || result.get(0) == null) {
	    return 0;
	} else {
	    return ((Number) result.get(0)).intValue();
	}
    }

    @SuppressWarnings("unchecked")
    public long getAverageDurationForActivity(Long activityID) {
	List<Object[]> result = (List<Object[]>) getSessionFactory().getCurrentSession().createQuery(GET_AVERAGE_COMPLETION_TIME_ACTIVITY)
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
	List result = getSessionFactory().getCurrentSession().createQuery(GET_AVERAGE_MARK_FOR_ACTIVTY)
		.setLong("activityID", activityID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;

    }

    @SuppressWarnings("unchecked")
    public Double getAverageMarkForGroupedActivity(Long activityID, Long groupID) {
	List result = getSessionFactory().getCurrentSession().createQuery(GET_AVERAGE_MARK_FOR_GROUPED_ACTIVTY)
		.setLong("activityID", activityID.longValue()).setLong("groupID", groupID.longValue()).list();

	if (result != null) {
	    if (result.size() > 0)
		return (Double) result.get(0);
	}

	return 0.0;
    }

    @SuppressWarnings("unchecked")
    public long getAverageDurationForGroupedActivity(Long activityID, Long groupID) {
	List<Object[]> result = (List<Object[]>) getSessionFactory().getCurrentSession().createQuery(GET_AVERAGE_COMPLETION_TIME_GROUPED_ACTIVITY)
		.setLong("activityID", activityID.longValue()).setLong("groupID", groupID.longValue()).list();

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
    
    @Override
    public List<Lesson> getLessonsByGroupAndUser(final Integer userId, final Integer orgId, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	
	final String LOAD_LESSONS_ORDERED_BY_FIELDS = "SELECT DISTINCT lesson " +
 		"FROM Lesson lesson, LearningDesign ld, Group g, GroupUser ug, Organisation lo " +
 		"WHERE lesson.learningDesign.learningDesignId = ld.learningDesignId " +
			"AND ld.copyTypeID != 3 " +
			"AND lesson.organisation.organisationId = lo.organisationId " +
			"AND (lo.organisationId = :orgId OR lo.parentOrganisation.organisationId = :orgId) " +
			"AND lesson.lessonClass.groupingId = g.grouping.groupingId " +
			"AND lesson.lessonStateId != 7 " +
			"AND ug.group.groupId = g.groupId " +
			"AND ug.user.userId = :userId " +
			"AND lesson.lessonName LIKE CONCAT('%', :searchString, '%') " +
		"ORDER BY " +
		"CASE " +
			"WHEN :sortBy='rowName' THEN lesson.lessonName " +
			"WHEN :sortBy='startDate' THEN lesson.startDateTime " +
		"END " + sortOrder;
	
	//when :sortBy='avgTimeTaken'
	final String LOAD_LESSONS_ORDERED_BY_AVERAGE_TIME_TAKEN = "SELECT DISTINCT lesson " +
		"FROM LearnerProgress progress right outer join progress.lesson lesson, LearningDesign ld, Group g, GroupUser ug, Organisation lo " +
		"WHERE lesson.learningDesign.learningDesignId = ld.learningDesignId " +
		"AND ld.copyTypeID != 3 " +
		"AND lesson.organisation.organisationId = lo.organisationId " +
		"AND (lo.organisationId = :orgId OR lo.parentOrganisation.organisationId = :orgId) " +
		"AND lesson.lessonClass.groupingId = g.grouping.groupingId " +
		"AND lesson.lessonStateId != 7 " +
		"AND ug.group.groupId = g.groupId " +
		"AND ug.user.userId = :userId " +
		"AND lesson.lessonName LIKE CONCAT('%', :searchString, '%') " +
		"GROUP BY lesson " +
		"ORDER BY AVG(TIMEDIFF(progress.finishDate,progress.startDate)) " + sortOrder;
	
	//when :sortBy='avgMark'
	final String LOAD_LESSONS_ORDERED_BY_AVERAGE_MARK = "SELECT DISTINCT lesson " +
		"FROM GradebookUserLesson gles right outer join gles.lesson lesson, LearningDesign ld, Group g, GroupUser ug, Organisation lo " +
		"WHERE lesson.learningDesign.learningDesignId = ld.learningDesignId " +
		"AND ld.copyTypeID != 3 " +
		"AND lesson.organisation.organisationId = lo.organisationId " +
		"AND (lo.organisationId = :orgId OR lo.parentOrganisation.organisationId = :orgId) " +
		"AND lesson.lessonClass.groupingId = g.grouping.groupingId " +
		"AND lesson.lessonStateId != 7 " +
		"AND ug.group.groupId = g.groupId " +
		"AND ug.user.userId = :userId " +
		"AND lesson.lessonName LIKE CONCAT('%', :searchString, '%') " +
		"GROUP BY lesson " +
		"ORDER BY AVG(IFNULL(gles.mark, -1)) " + sortOrder;
	
	String queryString;
	if (sortBy.equals("avgTimeTaken")) {
	    queryString = LOAD_LESSONS_ORDERED_BY_AVERAGE_TIME_TAKEN;
	} else if (sortBy.equals("avgMark")) {
	    queryString = LOAD_LESSONS_ORDERED_BY_AVERAGE_MARK;
	} else {
	    queryString = LOAD_LESSONS_ORDERED_BY_FIELDS;
	}
	
	Query query = getSession().createQuery(queryString);
	query.setInteger("userId", userId.intValue());
	query.setInteger("orgId", orgId.intValue());
	if (!sortBy.equals("avgTimeTaken") && !sortBy.equals("avgMark")) {
	    query.setString("sortBy", sortBy);
	}
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }
    
    @Override
    public List<User> getUsersByLesson(Long lessonId, int page, int size, String sortBy, String sortOrder, String searchString) {
	
	final String LOAD_LEARNERS_ORDERED_BY_NAME = "SELECT DISTINCT user.* " +
 		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
 		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" WHERE lesson.lesson_id = :lessonId " +
        		" AND lesson.class_grouping_id=g.grouping_id " +
        		" AND ug.group_id=g.group_id " +
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		"ORDER BY CONCAT(user.last_name, ' ', user.first_name) " + sortOrder;
	
	//when :sortBy='timeTaken'
	final String LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN = "SELECT DISTINCT user.* " +
 		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
 		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_learner_progress progress " +
		" ON progress.user_id=user.user_id AND progress.lesson_id=:lessonId " +
 		" WHERE lesson.lesson_id = :lessonId " +
        		" AND lesson.class_grouping_id=g.grouping_id " +
        		" AND ug.group_id=g.group_id " +
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		"ORDER BY TIMEDIFF(progress.finish_date_time, progress.start_date_time) " + sortOrder;
	
	//when :sortBy='mark'
	final String LOAD_LEARNERS_ORDERED_BY_MARK = "SELECT DISTINCT user.* " +
 		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
 		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_gradebook_user_lesson gradebookUserLesson " +
 		" ON user.user_id=gradebookUserLesson.user_id AND gradebookUserLesson.lesson_id =:lessonId " +
 		" WHERE lesson.lesson_id = :lessonId " +
        		" AND lesson.class_grouping_id=g.grouping_id " +
        		" AND ug.group_id=g.group_id " +
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +			
		" ORDER BY gradebookUserLesson.mark " + sortOrder;
	
	//when :sortBy='feedback'
	final String LOAD_LEARNERS_ORDERED_BY_FEEDBACK = "SELECT DISTINCT user.* " +
 		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
 		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_gradebook_user_lesson gradebookUserLesson " +
 		" ON user.user_id=gradebookUserLesson.user_id AND gradebookUserLesson.lesson_id =:lessonId " +
 		" WHERE lesson.lesson_id = :lessonId " +
        		" AND lesson.class_grouping_id=g.grouping_id " +
        		" AND ug.group_id=g.group_id " +	
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +			
		" ORDER BY gradebookUserLesson.feedback " + sortOrder;
	
	String queryString;
	if (sortBy.equals("timeTaken")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN;
	} else if (sortBy.equals("mark")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_MARK;
	} else if (sortBy.equals("feedback")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_FEEDBACK;
	} else {
	    queryString = LOAD_LEARNERS_ORDERED_BY_NAME;
	}
	
	SQLQuery query = getSession().createSQLQuery(queryString);
	query.addEntity(User.class);
	query.setLong("lessonId", lessonId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }
    
    @Override
    public List<User> getUsersByActivity(Long lessonId, Long activityId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	
	final String LOAD_LEARNERS_ORDERED_BY_NAME = "SELECT DISTINCT user.* " +
		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
		" WHERE lesson.lesson_id = :lessonId " +
			" AND lesson.class_grouping_id=g.grouping_id " +
			" AND ug.group_id=g.group_id " +		
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		" ORDER BY CONCAT(user.last_name, ' ', user.first_name) " + sortOrder;
	
	//when :sortBy='timeTaken'
	final String LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_ACTIVITY = "SELECT DISTINCT user.* " +
 		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_learner_progress progress " + 
			" INNER JOIN lams_progress_completed completedActivityProgress " + 
			" ON completedActivityProgress.learner_progress_id=progress.learner_progress_id " +
			" AND completedActivityProgress.activity_id=:activityId "+
		" ON progress.user_id=user.user_id " +
 		" WHERE lesson.lesson_id = :lessonId " +
			" AND lesson.class_grouping_id=g.grouping_id " +
			" AND ug.group_id=g.group_id " +
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		"ORDER BY TIMEDIFF(completedActivityProgress.completed_date_time, completedActivityProgress.start_date_time) " + sortOrder;
	
	//when :sortBy='mark'
	final String LOAD_LEARNERS_ORDERED_BY_MARK_ACTIVITY = "SELECT DISTINCT user.* " +
		" FROM lams_lesson lesson, lams_group g, lams_user_group ug " +
 		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_gradebook_user_activity gradebookUserActivity " +
 		" ON user.user_id=gradebookUserActivity.user_id AND gradebookUserActivity.activity_id =:activityId " +
 		" WHERE lesson.lesson_id = :lessonId " +
			" AND lesson.class_grouping_id=g.grouping_id " +
			" AND ug.group_id=g.group_id " +
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		"ORDER BY gradebookUserActivity.mark " + sortOrder;
	
	String queryString;
	if (sortBy.equals("timeTaken")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_ACTIVITY;
	} else if (sortBy.equals("mark")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_MARK_ACTIVITY;
	} else {
	    queryString = LOAD_LEARNERS_ORDERED_BY_NAME;
	}
	
	SQLQuery query = getSession().createSQLQuery(queryString);
	query.addEntity(User.class);
	query.setLong("lessonId", lessonId);
	if (sortBy.equals("timeTaken") || sortBy.equals("mark")) {
	    query.setLong("activityId", activityId);
	}
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }
    
    @Override
    public List<User> getUsersByGroup(Long lessonId, Long activityId, Long groupId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	
	final String LOAD_LEARNERS_ORDERED_BY_NAME = "SELECT DISTINCT user.* " +
		" FROM lams_user_group ug " +
		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
		" WHERE ug.group_id=:groupId " +
		" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		" ORDER BY CONCAT(user.last_name, ' ', user.first_name) " +
		sortOrder;
	
	//when :sortBy='timeTaken'
	final String LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_GROUP = "SELECT DISTINCT user.* " +
 		" FROM lams_user_group ug " +
		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_learner_progress progress " + 
 			" INNER JOIN lams_progress_completed completedActivityProgress " + 
 			" ON completedActivityProgress.learner_progress_id=progress.learner_progress_id " +
 			" AND completedActivityProgress.activity_id=:activityId "+
 		" ON progress.user_id=user.user_id " +
		" WHERE ug.group_id=:groupId " +  
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		" ORDER BY TIMEDIFF(completedActivityProgress.completed_date_time, completedActivityProgress.start_date_time) " + sortOrder;
	
	//when :sortBy='mark'
	final String LOAD_LEARNERS_ORDERED_BY_MARK_GROUP = "SELECT DISTINCT user.* " +
 		" FROM lams_user_group ug " +
 		" INNER JOIN lams_user user ON ug.user_id=user.user_id " +
 		" LEFT OUTER JOIN lams_gradebook_user_activity gradebookUserActivity " +
 		" ON user.user_id=gradebookUserActivity.user_id AND gradebookUserActivity.activity_id =:activityId " +
 		" WHERE ug.group_id=:groupId " +
			" AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		"ORDER BY gradebookUserActivity.mark " + sortOrder;
	
	String queryString;
	if (sortBy.equals("timeTaken")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_GROUP;
	} else if (sortBy.equals("mark")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_MARK_GROUP;
	} else {
	    queryString = LOAD_LEARNERS_ORDERED_BY_NAME;
	}
	
	SQLQuery query = getSession().createSQLQuery(queryString);
	query.addEntity(User.class);
	if (sortBy.equals("timeTaken") || sortBy.equals("mark")) {
	    query.setLong("activityId", activityId);
	}
	query.setLong("groupId", groupId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }
    
    @Override
    public int getCountUsersByLesson(Long lessonId, String searchString) {
	
	final String COUNT_COMMENTS_BY_ITEM_AND_USER = "SELECT COUNT(ug.user) "
		+ "FROM Lesson lesson, Group g, GroupUser ug "
		+ "WHERE lesson.lessonId = :lessonId AND lesson.lessonClass.groupingId = g.grouping.groupingId "
		+ "AND ug.group.groupId = g.groupId ";

	List list = getSession().createQuery(COUNT_COMMENTS_BY_ITEM_AND_USER).setLong("lessonId", lessonId).list();
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}

    }
    
    @Override
    /**
     * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisation(int)
     */
    public List<User> getUsersFromOrganisation(Integer orgId, int page, int size, String sortOrder, String searchString) {
	final String LOAD_LEARNERS_BY_ORG = "SELECT uo.user FROM UserOrganisation uo"
		+ " WHERE uo.organisation.organisationId=:orgId"
		+ " AND CONCAT(uo.user.lastName, ' ', uo.user.firstName) LIKE CONCAT('%', :searchString, '%') "
		+ " ORDER BY uo.user.lastName " + sortOrder +" , uo.user.firstName " + sortOrder;

	Query query = getSession().createQuery(LOAD_LEARNERS_BY_ORG);
	query.setLong("orgId", orgId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }
    
    @Override
    public int getCountUsersByOrganisation(Integer orgId, String searchString) {
	
	final String COUNT_LEARNERS_BY_ORG = "SELECT COUNT(uo.user) FROM UserOrganisation uo"
		+ " WHERE uo.organisation.organisationId=:orgId"
		+ " AND CONCAT(uo.user.lastName, ' ', uo.user.firstName) LIKE CONCAT('%', :searchString, '%') ";

	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	List list = getSession().createQuery(COUNT_LEARNERS_BY_ORG).setLong("orgId", orgId)
		.setString("searchString", searchString).list();
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}

    }
    
    @Override
    public List<GradebookUserLesson> getGradebookUserLessons(Lesson lesson) {
	String GET_GRADEBOOK_USER_LESSONS_BY_LESSON_ID = "select ul from GradebookUserLesson ul where ul.lesson.lessonId=?";
	return find(GET_GRADEBOOK_USER_LESSONS_BY_LESSON_ID, new Object[] { lesson.getLessonId() });
    }
    
    @Override
    public List<GradebookUserLesson> getGradebookUserLessons(Lesson lesson, List<Integer> userIds) {
	String GET_GRADEBOOK_USER_LESSONS_BY_LESSON_AND_USERS = "select ul from GradebookUserLesson ul "
		+ " where ul.lesson.lessonId=:lessonId AND ul.learner.userId IN (:userIds)";

	List<GradebookUserLesson> results = getSession().createQuery(GET_GRADEBOOK_USER_LESSONS_BY_LESSON_AND_USERS)
		.setLong("lessonId", lesson.getLessonId()).setParameterList("userIds", userIds).list();
	return results;
    }

    @Override
    public List<GradebookUserLesson> getGradebookUserLessons(List<Long> lessonIds) {
	final String GET_GRADEBOOK_LEARNER_LESSONS_BY_LESSON_LIST = "FROM GradebookUserLesson ul WHERE "
		+ " ul.lesson.lessonId IN (:lessonIds)";

	List<GradebookUserLesson> gradebookUserLessons = getSession()
		.createQuery(GET_GRADEBOOK_LEARNER_LESSONS_BY_LESSON_LIST).setParameterList("lessonIds", lessonIds)
		.list();

	return gradebookUserLessons;
    }
}
