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

package org.lamsfoundation.lams.gradebook.dao.hibernate;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;
import org.lamsfoundation.lams.gradebook.model.GradebookUserActivityArchive;
import org.lamsfoundation.lams.gradebook.model.GradebookUserLessonArchive;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

@Repository
public class GradebookDAO extends LAMSBaseDAO implements IGradebookDAO {

    private static final String GET_GRADEBOOK_USER_ACTIVITY = "FROM GradebookUserActivity gact WHERE "
	    + "gact.learner.userId=:userID AND gact.activity.activityId=:activityID";

    private static final String GET_GRADEBOOK_USER_LESSON = "FROM GradebookUserLesson gles WHERE "
	    + "gles.learner.userId=:userID AND gles.lesson.lessonId=:lessonID";

    private static final String GET_GRADEBOOK_USER_LESSONS = "FROM GradebookUserLesson gles WHERE "
	    + "gles.lesson.lessonId=:lessonID";

    private static final String GET_GRADEBOOK_ACTIVITIES_FROM_LESSON = "FROM GradebookUserActivity gact WHERE "
	    + "gact.learner.userId=:userID and gact.activity in (select distinct tses.toolActivity from ToolSession tses WHERE tses.lesson.lessonId=:lessonID)";

//    private static final String GET_GRADEBOOK_ACTIVITIES_FROM_LESSON_SUM = "select sum(gact.mark) from GradebookUserActivity gact where "
//	    + "gact.learner=:userID and gact.activity in (select distinct tses.toolActivity from ToolSession tses where tses.lesson=:lessonID)";

    private static final String GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY = "FROM GradebookUserActivity gact WHERE "
	    + "gact.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_LESSON = "SELECT AVG(gles.mark) FROM GradebookUserLesson gles WHERE "
	    + "gles.lesson.lessonId=:lessonID";

    private static final String GET_AVERAGE_MARK_FOR_ACTIVTY = "SELECT AVG(gact.mark) FROM GradebookUserActivity gact where "
	    + "gact.activity.activityId=:activityID";

    private static final String GET_AVERAGE_MARK_FOR_GROUPED_ACTIVTY = "SELECT AVG(gact.mark) FROM GradebookUserActivity gact, GroupUser gu, Group grp where "
	    + "gact.activity.activityId=:activityID and grp.groupId=:groupID and gu.user=gact.learner and gu.group=grp";

    private static final String GET_ALL_MARKS_FOR_LESSON = "SELECT gles.mark FROM GradebookUserLesson gles WHERE "
	    + "gles.lesson.lessonId=:lessonID";

    @Override
    public GradebookUserActivity getGradebookUserDataForActivity(Long activityID, Integer userID) {
	GradebookUserActivity result = getSession()
		.createQuery(GET_GRADEBOOK_USER_ACTIVITY, GradebookUserActivity.class).setParameter("userID", userID)
		.setParameter("activityID", activityID).uniqueResult();
	return result;
    }

    @Override
    public GradebookUserLesson getGradebookUserDataForLesson(Long lessonID, Integer userID) {
	return getSession().createQuery(GET_GRADEBOOK_USER_LESSON, GradebookUserLesson.class)
		.setParameter("userID", userID).setParameter("lessonID", lessonID).uniqueResult();
    }

    @Override
    public List<GradebookUserLesson> getGradebookUserDataForLesson(Long lessonID) {
	List<GradebookUserLesson> result = getSession()
		.createQuery(GET_GRADEBOOK_USER_LESSONS, GradebookUserLesson.class).setParameter("lessonID", lessonID)
		.list();

	return result;
    }

    @Override
    public List<GradebookUserActivity> getGradebookUserActivitiesForLesson(Long lessonID, Integer userID) {
	List<GradebookUserActivity> result = getSession()
		.createQuery(GET_GRADEBOOK_ACTIVITIES_FROM_LESSON, GradebookUserActivity.class)
		.setParameter("userID", userID.intValue()).setParameter("lessonID", lessonID).list();
	return result;
    }

//    @Override
//    public Double getGradebookUserActivityMarkSum(Long lessonID, Integer userID) {
//	List result = getSession().createQuery(GET_GRADEBOOK_ACTIVITIES_FROM_LESSON_SUM)
//		.setInteger("userID", userID.intValue()).setLong("lessonID", lessonID.longValue()).list();
//
//	if (result != null) {
//	    if (result.size() > 0) {
//		return (Double) result.get(0);
//	    }
//	}
//
//	return 0.0;
//    }

    @Override
    public List<GradebookUserActivity> getAllGradebookUserActivitiesForActivity(Long activityID) {
	return getSession().createQuery(GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY, GradebookUserActivity.class)
		.setParameter("activityID", activityID).list();
    }

    @Override
    public List<GradebookUserActivity> getGradebookUserActivitiesForActivity(Long activityID, List<Integer> userIds) {
	final String GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY = "FROM GradebookUserActivity gact where "
		+ "gact.activity.activityId=:activityID AND gact.learner.userId IN (:userIds)";

	List<GradebookUserActivity> result = getSession()
		.createQuery(GET_GRADEBOOK_USER_ACTIVITIES_FOR_ACTIVITY, GradebookUserActivity.class)
		.setParameter("activityID", activityID).setParameterList("userIds", userIds).list();

	return result;
    }

    @Override
    public Double getAverageMarkForLesson(Long lessonID) {
	Double result = getSession().createQuery(GET_AVERAGE_MARK_FOR_LESSON, Double.class)
		.setParameter("lessonID", lessonID).uniqueResult();

	return result == null ? 0.0 : result;
    }

    @Override
    public List<Number> getAllMarksForLesson(Long lessonID) {
	return getSession().createQuery(GET_ALL_MARKS_FOR_LESSON, Number.class).setParameter("lessonID", lessonID)
		.list();
    }

    @Override
    public long getMedianTimeTakenLesson(Long lessonID) {
	final String GET_MEDIAN_TIME_TAKEN_FOR_LESSON = "SELECT AVG(t1.timeTaken) AS medianVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(progress.finish_date_time, progress.start_date_time)) AS timeTaken"
		+ "  FROM lams_learner_progress progress,  (SELECT @rownum\\:=0) r"
		+ "  WHERE progress.lesson_id=:lessonID AND TIMEDIFF(progress.finish_date_time, progress.start_date_time) IS NOT NULL"
		+ "  ORDER BY TIMEDIFF(progress.finish_date_time, progress.start_date_time)" + " ) AS t1, " + " ("
		+ "  SELECT count(*) AS totalRows" + "  FROM lams_learner_progress progress"
		+ "  WHERE progress.lesson_id=:lessonID AND TIMEDIFF(progress.finish_date_time, progress.start_date_time) IS NOT NULL"
		+ " ) AS t2" + " WHERE t1.rowNumber in ( floor((totalRows+1)/2), floor((totalRows+2)/2) )";

	Object result = getSession().createSQLQuery(GET_MEDIAN_TIME_TAKEN_FOR_LESSON).setParameter("lessonID", lessonID)
		.uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public long getMedianTimeTakenForActivity(Long activityID) {
	final String GET_MEDIAN_TIME_TAKEN_FOR_ACTIVITY = "SELECT AVG(t1.timeTaken) AS medianVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(progress.completed_date_time, progress.start_date_time)) AS timeTaken"
		+ "  FROM lams_progress_completed progress,  (SELECT @rownum\\:=0) r"
		+ "  WHERE progress.activity_id=:activityID AND TIMEDIFF(progress.completed_date_time, progress.start_date_time) IS NOT NULL"
		+ "  ORDER BY TIMEDIFF(progress.completed_date_time, progress.start_date_time)" + " ) AS t1, " + " ("
		+ "  SELECT count(*) AS totalRows" + "  FROM lams_progress_completed progress"
		+ "  WHERE progress.activity_id=:activityID AND TIMEDIFF(progress.completed_date_time, progress.start_date_time) IS NOT NULL"
		+ " ) AS t2" + " WHERE t1.rowNumber in ( floor((totalRows+1)/2), floor((totalRows+2)/2) )";

	Object result = getSession().createSQLQuery(GET_MEDIAN_TIME_TAKEN_FOR_ACTIVITY)
		.setParameter("activityID", activityID).uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public long getMinTimeTakenForActivity(Long activityID) {
	final String GET_MIN_TIME_TAKEN_FOR_ACTIVITY = "SELECT MIN(t1.timeTaken) AS minVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(progress.completed_date_time, progress.start_date_time)) AS timeTaken"
		+ "  FROM lams_progress_completed progress,  (SELECT @rownum\\:=0) r"
		+ "  WHERE progress.activity_id=:activityID AND TIMEDIFF(progress.completed_date_time, progress.start_date_time) IS NOT NULL"
		+ "  ORDER BY TIMEDIFF(progress.completed_date_time, progress.start_date_time)" + " ) AS t1 ";

	Object result = getSession().createSQLQuery(GET_MIN_TIME_TAKEN_FOR_ACTIVITY)
		.setParameter("activityID", activityID).uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public long getMaxTimeTakenForActivity(Long activityID) {
	final String GET_MAX_TIME_TAKEN_FOR_ACTIVITY = "SELECT MAX(t1.timeTaken) AS maxVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(progress.completed_date_time, progress.start_date_time)) AS timeTaken"
		+ "  FROM lams_progress_completed progress,  (SELECT @rownum\\:=0) r"
		+ "  WHERE progress.activity_id=:activityID AND TIMEDIFF(progress.completed_date_time, progress.start_date_time) IS NOT NULL"
		+ "  ORDER BY TIMEDIFF(progress.completed_date_time, progress.start_date_time)" + " ) AS t1 ";

	Object result = getSession().createSQLQuery(GET_MAX_TIME_TAKEN_FOR_ACTIVITY)
		.setParameter("activityID", activityID).uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public Double getAverageMarkForActivity(Long activityID) {
	Double result = getSession().createQuery(GET_AVERAGE_MARK_FOR_ACTIVTY, Double.class)
		.setParameter("activityID", activityID).getSingleResult();

	return result;
    }

    @Override
    public Double getAverageMarkForGroupedActivity(Long activityID, Long groupID) {
	Double result = getSession().createQuery(GET_AVERAGE_MARK_FOR_GROUPED_ACTIVTY, Double.class)
		.setParameter("activityID", activityID).setParameter("groupID", groupID).uniqueResult();

	return result == null ? 0.0 : result;
    }

    @Override
    public long getMedianTimeTakenForGroupedActivity(Long activityID, Long groupID) {
	final String GET_MEDIAN_TIME_TAKEN_FOR_GROUPED_ACTIVITY = "SELECT AVG(t1.timeTaken) AS medianVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time)) AS timeTaken"
		+ "  FROM lams_progress_completed compProgress,  (SELECT @rownum\\:=0) r, lams_learner_progress progr, lams_user_group ug "
		+ "  WHERE compProgress.activity_id=:activityID AND TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time) IS NOT NULL"
		+ "  AND ug.group_id=:groupID AND compProgress.learner_progress_id = progr.learner_progress_id AND progr.user_id=ug.user_id "
		+ "  ORDER BY TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time)" + " ) AS t1, "
		+ " (" + "  SELECT count(*) AS totalRows"
		+ "  FROM lams_progress_completed compProgress, lams_learner_progress progr, lams_user_group ug"
		+ "  WHERE compProgress.activity_id=:activityID AND TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time) IS NOT NULL"
		+ "  AND ug.group_id=:groupID AND compProgress.learner_progress_id = progr.learner_progress_id AND progr.user_id=ug.user_id"
		+ " ) AS t2" + " WHERE t1.rowNumber in ( floor((totalRows+1)/2), floor((totalRows+2)/2) )";

	Object result = getSession().createSQLQuery(GET_MEDIAN_TIME_TAKEN_FOR_GROUPED_ACTIVITY)
		.setParameter("activityID", activityID).setParameter("groupID", groupID).uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public long getMinTimeTakenForGroupedActivity(Long activityID, Long groupID) {
	final String GET_MIN_TIME_TAKEN_FOR_GROUPED_ACTIVITY = "SELECT MIN(t1.timeTaken) AS minVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time)) AS timeTaken"
		+ "  FROM lams_progress_completed compProgress,  (SELECT @rownum\\:=0) r, lams_learner_progress progr, lams_user_group ug "
		+ "  WHERE compProgress.activity_id=:activityID AND TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time) IS NOT NULL"
		+ "  AND ug.group_id=:groupID AND compProgress.learner_progress_id = progr.learner_progress_id AND progr.user_id=ug.user_id "
		+ "  ORDER BY TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time)" + " ) AS t1";

	Object result = getSession().createSQLQuery(GET_MIN_TIME_TAKEN_FOR_GROUPED_ACTIVITY)
		.setParameter("activityID", activityID).setParameter("groupID", groupID).uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public long getMaxTimeTakenForGroupedActivity(Long activityID, Long groupID) {
	final String GET_MAX_TIME_TAKEN_FOR_GROUPED_ACTIVITY = "SELECT MAX(t1.timeTaken) AS maxVal FROM ("
		+ " SELECT @rownum\\:=@rownum+1 AS `rowNumber`, TIME_TO_SEC(TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time)) AS timeTaken"
		+ "  FROM lams_progress_completed compProgress,  (SELECT @rownum\\:=0) r, lams_learner_progress progr, lams_user_group ug "
		+ "  WHERE compProgress.activity_id=:activityID AND TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time) IS NOT NULL"
		+ "  AND ug.group_id=:groupID AND compProgress.learner_progress_id = progr.learner_progress_id AND progr.user_id=ug.user_id "
		+ "  ORDER BY TIMEDIFF(compProgress.completed_date_time, compProgress.start_date_time)" + " ) AS t1";

	Object result = getSession().createSQLQuery(GET_MAX_TIME_TAKEN_FOR_GROUPED_ACTIVITY)
		.setParameter("activityID", activityID).setParameter("groupID", groupID).uniqueResult();

	//converting into milliseconds
	return result == null ? 0 : ((Number) result).intValue() * 1000;
    }

    @Override
    public List<Lesson> getLessonsByGroupAndUser(final Integer userId, boolean staffOnly, final Integer orgId, int page,
	    int size, String sortBy, String sortOrder, String searchString) {
	final String LOAD_LESSONS_ORDERED_BY_FIELDS = "SELECT DISTINCT lesson "
		+ "FROM Lesson lesson, LearningDesign ld, {0} Organisation lo "
		+ "WHERE lesson.learningDesign.learningDesignId = ld.learningDesignId AND ld.copyTypeID != 3 "
		+ "AND lesson.organisation.organisationId = lo.organisationId "
		+ "AND (lo.organisationId = :orgId OR lo.parentOrganisation.organisationId = :orgId) "
		+ "AND lesson.lessonStateId != 7 {1} {2} "
		+ "ORDER BY CASE WHEN :sortBy='rowName' THEN lesson.lessonName "
		+ "WHEN :sortBy='startDate' THEN lesson.startDateTime END " + sortOrder;

	//when :sortBy='avgTimeTaken'
	final String LOAD_LESSONS_ORDERED_BY_AVERAGE_TIME_TAKEN = "SELECT DISTINCT lesson "
		+ "FROM LearnerProgress progress right outer join progress.lesson lesson, LearningDesign ld, {0} Organisation lo "
		+ "WHERE lesson.learningDesign.learningDesignId = ld.learningDesignId AND ld.copyTypeID != 3 "
		+ "AND lesson.organisation.organisationId = lo.organisationId "
		+ "AND (lo.organisationId = :orgId OR lo.parentOrganisation.organisationId = :orgId) "
		+ "AND lesson.lessonStateId != 7 {1} {2} "
		+ "GROUP BY lesson ORDER BY AVG(TIMEDIFF(progress.finishDate,progress.startDate)) " + sortOrder;

	//when :sortBy='avgMark'
	final String LOAD_LESSONS_ORDERED_BY_AVERAGE_MARK = "SELECT DISTINCT lesson "
		+ "FROM GradebookUserLesson gles right outer join gles.lesson lesson, LearningDesign ld, {0} Organisation lo "
		+ "WHERE lesson.learningDesign.learningDesignId = ld.learningDesignId AND ld.copyTypeID != 3 "
		+ "AND lesson.organisation.organisationId = lo.organisationId "
		+ "AND (lo.organisationId = :orgId OR lo.parentOrganisation.organisationId = :orgId) "
		+ "AND lesson.lessonStateId != 7 {1} {2} " + "GROUP BY lesson ORDER BY AVG(IFNULL(gles.mark, -1)) "
		+ sortOrder;

	final String CONDITION_IF_ANY_USER_PROVIDED = " AND lesson.lessonClass.groupingId = g.grouping.groupingId "
		+ "AND ug.groupId = g.groupId AND ug.userId = :userId ";

	final String CONDITION_IF_STAFF_PROVIDED = " AND ug.groupId = lesson.lessonClass.staffGroup.groupId AND ug.userId = :userId ";

	String queryString;
	if (sortBy.equals("avgTimeTaken")) {
	    queryString = LOAD_LESSONS_ORDERED_BY_AVERAGE_TIME_TAKEN;
	} else if (sortBy.equals("avgMark")) {
	    queryString = LOAD_LESSONS_ORDERED_BY_AVERAGE_MARK;
	} else {
	    queryString = LOAD_LESSONS_ORDERED_BY_FIELDS;
	}

	queryString = queryString.replace("{0}", userId == null ? "" : " Group g, GroupUser ug, ");
	queryString = queryString.replace("{1}",
		searchString == null ? "" : " AND lesson.lessonName LIKE CONCAT('%', :searchString, '%') ");
	queryString = queryString.replace("{2}",
		userId == null ? "" : staffOnly ? CONDITION_IF_STAFF_PROVIDED : CONDITION_IF_ANY_USER_PROVIDED);

	Query<Lesson> query = getSession().createQuery(queryString, Lesson.class);
	if (userId != null) {
	    query.setParameter("userId", userId);
	}
	query.setParameter("orgId", orgId);
	if (!sortBy.equals("avgTimeTaken") && !sortBy.equals("avgMark")) {
	    query.setParameter("sortBy", sortBy);
	}
	if (searchString != null) {
	    // support for custom search from a toolbar
	    query.setParameter("searchString", searchString);
	}
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }

    @Override
    public List<User> getUsersByLesson(Long lessonId, int page, int size, String sortBy, String sortOrder,
	    String searchString) {

	final String LOAD_LEARNERS_ORDERED_BY_NAME = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id " + " WHERE lesson.lesson_id = :lessonId "
		+ " AND lesson.class_grouping_id=g.grouping_id " + " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ "ORDER BY CONCAT(user.last_name, ' ', user.first_name) " + sortOrder;

	//when :sortBy='timeTaken'
	final String LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_learner_progress progress "
		+ " ON progress.user_id=user.user_id AND progress.lesson_id=:lessonId "
		+ " WHERE lesson.lesson_id = :lessonId " + " AND lesson.class_grouping_id=g.grouping_id "
		+ " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ "ORDER BY TIMEDIFF(progress.finish_date_time, progress.start_date_time) " + sortOrder;

	//when :sortBy='mark'
	final String LOAD_LEARNERS_ORDERED_BY_MARK = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_gradebook_user_lesson gradebookUserLesson "
		+ " ON user.user_id=gradebookUserLesson.user_id AND gradebookUserLesson.lesson_id =:lessonId "
		+ " WHERE lesson.lesson_id = :lessonId " + " AND lesson.class_grouping_id=g.grouping_id "
		+ " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY gradebookUserLesson.mark " + sortOrder;

	//when :sortBy='feedback'
	final String LOAD_LEARNERS_ORDERED_BY_FEEDBACK = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_gradebook_user_lesson gradebookUserLesson "
		+ " ON user.user_id=gradebookUserLesson.user_id AND gradebookUserLesson.lesson_id =:lessonId "
		+ " WHERE lesson.lesson_id = :lessonId " + " AND lesson.class_grouping_id=g.grouping_id "
		+ " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY gradebookUserLesson.feedback " + sortOrder;

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

	@SuppressWarnings("unchecked")
	NativeQuery<User> query = getSession().createSQLQuery(queryString);
	query.addEntity(User.class);
	query.setParameter("lessonId", lessonId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }

    @Override
    public List<User> getUsersByActivity(Long lessonId, Long activityId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	final String LOAD_LEARNERS_ORDERED_BY_NAME = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id " + " WHERE lesson.lesson_id = :lessonId "
		+ " AND lesson.class_grouping_id=g.grouping_id " + " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY CONCAT(user.last_name, ' ', user.first_name) " + sortOrder;

	//when :sortBy='timeTaken'
	final String LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_ACTIVITY = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_learner_progress progress "
		+ " INNER JOIN lams_progress_completed completedActivityProgress "
		+ " ON completedActivityProgress.learner_progress_id=progress.learner_progress_id "
		+ " AND completedActivityProgress.activity_id=:activityId " + " ON progress.user_id=user.user_id "
		+ " WHERE lesson.lesson_id = :lessonId " + " AND lesson.class_grouping_id=g.grouping_id "
		+ " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ "ORDER BY TIMEDIFF(completedActivityProgress.completed_date_time, completedActivityProgress.start_date_time) "
		+ sortOrder;

	//when :sortBy='mark'
	final String LOAD_LEARNERS_ORDERED_BY_MARK_ACTIVITY = "SELECT DISTINCT user.* "
		+ " FROM lams_lesson lesson, lams_group g, lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_gradebook_user_activity gradebookUserActivity "
		+ " ON user.user_id=gradebookUserActivity.user_id AND gradebookUserActivity.activity_id =:activityId "
		+ " WHERE lesson.lesson_id = :lessonId " + " AND lesson.class_grouping_id=g.grouping_id "
		+ " AND ug.group_id=g.group_id "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ "ORDER BY gradebookUserActivity.mark " + sortOrder;

	String queryString;
	if (sortBy.equals("timeTaken")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_ACTIVITY;
	} else if (sortBy.equals("mark")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_MARK_ACTIVITY;
	} else {
	    queryString = LOAD_LEARNERS_ORDERED_BY_NAME;
	}

	@SuppressWarnings("unchecked")
	NativeQuery<User> query = getSession().createSQLQuery(queryString);
	query.addEntity(User.class);
	query.setParameter("lessonId", lessonId);
	if (sortBy.equals("timeTaken") || sortBy.equals("mark")) {
	    query.setParameter("activityId", activityId);
	}
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }

    @Override
    public List<User> getUsersByGroup(Long lessonId, Long activityId, Long groupId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	final String LOAD_LEARNERS_ORDERED_BY_NAME = "SELECT DISTINCT user.* " + " FROM lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id " + " WHERE ug.group_id=:groupId "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY CONCAT(user.last_name, ' ', user.first_name) " + sortOrder;

	//when :sortBy='timeTaken'
	final String LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_GROUP = "SELECT DISTINCT user.* " + " FROM lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_learner_progress progress "
		+ " INNER JOIN lams_progress_completed completedActivityProgress "
		+ " ON completedActivityProgress.learner_progress_id=progress.learner_progress_id "
		+ " AND completedActivityProgress.activity_id=:activityId " + " ON progress.user_id=user.user_id "
		+ " WHERE ug.group_id=:groupId "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY TIMEDIFF(completedActivityProgress.completed_date_time, completedActivityProgress.start_date_time) "
		+ sortOrder;

	//when :sortBy='mark'
	final String LOAD_LEARNERS_ORDERED_BY_MARK_GROUP = "SELECT DISTINCT user.* " + " FROM lams_user_group ug "
		+ " INNER JOIN lams_user user ON ug.user_id=user.user_id "
		+ " LEFT OUTER JOIN lams_gradebook_user_activity gradebookUserActivity "
		+ " ON user.user_id=gradebookUserActivity.user_id AND gradebookUserActivity.activity_id =:activityId "
		+ " WHERE ug.group_id=:groupId "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ "ORDER BY gradebookUserActivity.mark " + sortOrder;

	String queryString;
	if (sortBy.equals("timeTaken")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_TIME_TAKEN_GROUP;
	} else if (sortBy.equals("mark")) {
	    queryString = LOAD_LEARNERS_ORDERED_BY_MARK_GROUP;
	} else {
	    queryString = LOAD_LEARNERS_ORDERED_BY_NAME;
	}

	@SuppressWarnings("unchecked")
	NativeQuery<User> query = getSession().createSQLQuery(queryString);
	query.addEntity(User.class);
	if (sortBy.equals("timeTaken") || sortBy.equals("mark")) {
	    query.setParameter("activityId", activityId);
	}
	query.setParameter("groupId", groupId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
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

	return getSession().createQuery(COUNT_COMMENTS_BY_ITEM_AND_USER, Number.class)
		.setParameter("lessonId", lessonId).getSingleResult().intValue();
    }

    @Override
    /**
     * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisation(int)
     */
    public List<User> getUsersFromOrganisation(Integer orgId, int page, int size, String sortOrder,
	    String searchString) {
	final String LOAD_LEARNERS_BY_ORG = "SELECT uo.user FROM UserOrganisation uo"
		+ " WHERE uo.organisation.organisationId=:orgId"
		+ " AND CONCAT(uo.user.lastName, ' ', uo.user.firstName) LIKE CONCAT('%', :searchString, '%') "
		+ " ORDER BY uo.user.lastName " + sortOrder + " , uo.user.firstName " + sortOrder;

	Query<User> query = getSession().createQuery(LOAD_LEARNERS_BY_ORG, User.class);
	query.setParameter("orgId", orgId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
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
	return getSession().createQuery(COUNT_LEARNERS_BY_ORG, Number.class).setParameter("orgId", orgId)
		.setParameter("searchString", searchString).getSingleResult().intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GradebookUserLesson> getGradebookUserLessons(Lesson lesson) {
	String GET_GRADEBOOK_USER_LESSONS_BY_LESSON_ID = "select ul from GradebookUserLesson ul where ul.lesson.lessonId=?";
	return find(GET_GRADEBOOK_USER_LESSONS_BY_LESSON_ID, new Object[] { lesson.getLessonId() });
    }

    @Override
    public List<GradebookUserLesson> getGradebookUserLessons(Lesson lesson, List<Integer> userIds) {
	String GET_GRADEBOOK_USER_LESSONS_BY_LESSON_AND_USERS = "select ul from GradebookUserLesson ul "
		+ " where ul.lesson.lessonId=:lessonId AND ul.learner.userId IN (:userIds)";

	List<GradebookUserLesson> results = getSession()
		.createQuery(GET_GRADEBOOK_USER_LESSONS_BY_LESSON_AND_USERS, GradebookUserLesson.class)
		.setParameter("lessonId", lesson.getLessonId()).setParameterList("userIds", userIds).list();
	return results;
    }

    @Override
    public List<GradebookUserLesson> getGradebookUserLessons(List<Long> lessonIds) {
	final String GET_GRADEBOOK_LEARNER_LESSONS_BY_LESSON_LIST = "FROM GradebookUserLesson ul WHERE "
		+ " ul.lesson.lessonId IN (:lessonIds)";

	List<GradebookUserLesson> gradebookUserLessons = getSession()
		.createQuery(GET_GRADEBOOK_LEARNER_LESSONS_BY_LESSON_LIST, GradebookUserLesson.class)
		.setParameterList("lessonIds", lessonIds).list();

	return gradebookUserLessons;
    }

    @Override
    public boolean hasArchivedMarks(Long lessonId, Integer userId) {
	final String HAS_ARCHIVED_MARKS = "SELECT COUNT(*) FROM GradebookUserLessonArchive a WHERE "
		+ " a.lesson.lessonId = :lessonId AND a.learner.userId = :userId";
	long count = (Long) getSession().createQuery(HAS_ARCHIVED_MARKS).setParameter("lessonId", lessonId)
		.setParameter("userId", userId).uniqueResult();
	return count > 0;
    }

    @Override
    public List<GradebookUserLessonArchive> getArchivedLessonMarks(Long lessonId, Integer userId) {
	final String GET_ARCHIVED_LESSON_MARKS = "FROM GradebookUserLessonArchive a WHERE "
		+ " a.lesson.lessonId = :lessonId AND a.learner.userId = :userId ORDER BY a.archiveDate DESC";
	return getSession().createQuery(GET_ARCHIVED_LESSON_MARKS, GradebookUserLessonArchive.class)
		.setParameter("lessonId", lessonId).setParameter("userId", userId).list();
    }

    @Override
    public List<GradebookUserActivityArchive> getArchivedActivityMarks(Long activityId, Integer userId) {
	final String GET_ARCHIVED_ACTIVITY_MARKS = "FROM GradebookUserActivityArchive a WHERE "
		+ " a.activity.activityId = :activityId AND a.learner.userId = :userId ORDER BY a.archiveDate DESC";
	return getSession().createQuery(GET_ARCHIVED_ACTIVITY_MARKS, GradebookUserActivityArchive.class)
		.setParameter("activityId", activityId).setParameter("userId", userId).list();
    }
}
