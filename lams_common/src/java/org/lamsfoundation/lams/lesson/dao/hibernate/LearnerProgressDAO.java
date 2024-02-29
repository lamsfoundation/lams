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

package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LearnerProgressArchive;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of ILearnerProgressDAO
 *
 * @author chris
 */
@Repository
public class LearnerProgressDAO extends LAMSBaseDAO implements ILearnerProgressDAO {

    protected Logger log = Logger.getLogger(LearnerProgressDAO.class);

    private final static String LOAD_PROGRESS_BY_LEARNER = "from LearnerProgress p where p.user.id = :learnerId "
	    + "and p.lesson.id = :lessonId";

    private final static String LOAD_PROGRESS_REFFERING_TO_ACTIVITY = "from LearnerProgress p "
	    + "where p.previousActivity = :activity or p.currentActivity = :activity or p.nextActivity = :activity ";

    private final static String LOAD_COMPLETED_PROGRESS_BY_LESSON = "FROM LearnerProgress p WHERE p.lessonComplete > 0 "
	    + "AND p.lesson.id = :lessonId ORDER BY p.user.lastName <ORDER>, p.user.firstName <ORDER>, p.user.login <ORDER>";

    private final static String LOAD_LEARNERS_LATEST_COMPLETED_BY_LESSON = "SELECT p.user FROM LearnerProgress p WHERE "
	    + "p.lessonComplete > 0 and p.lesson.id = :lessonId ORDER BY p.finishDate DESC";

    private final static String LOAD_LEARNERS_ATTEMPTED_ACTIVITY = "SELECT prog.user FROM LearnerProgress prog, "
	    + " Activity act join prog.attemptedActivities attAct where act.id = :activityId and index(attAct) = act";

    private final static String LOAD_LEARNERS_COMPLETED_ACTIVITY = "SELECT prog.user FROM LearnerProgress prog, "
	    + " Activity act join prog.completedActivities compAct where act.id = :activityId and index(compAct) = act";

    private final static String COUNT_COMPLETED_PROGRESS_BY_LESSON = "select count(*) from LearnerProgress p "
	    + " where p.lessonComplete > 0 and p.lesson.id = :lessonId";

    private final static String COUNT_ATTEMPTED_ACTIVITY = "select count(*) from LearnerProgress prog, "
	    + " Activity act join prog.attemptedActivities attAct where act.id = :activityId and "
	    + " index(attAct) = act";

    private final static String COUNT_COMPLETED_ACTIVITY = "select count(*) from LearnerProgress prog, "
	    + " Activity act join prog.completedActivities compAct where act.id = :activityId and "
	    + " index(compAct) = act";

    private final static String COUNT_CURRENT_ACTIVITY = "select prog.currentActivity.activityId, count(prog) "
	    + "from LearnerProgress prog WHERE prog.currentActivity.activityId IN (:activityIds) "
	    + "GROUP BY prog.currentActivity.activityId";

    private final static String COUNT_SINGLE_CURRENT_ACTIVITY = "select count(*) from LearnerProgress prog "
	    + "WHERE prog.currentActivity.activityId = :activityId ";

    private final static String LOAD_PROGRESS_BY_LESSON = "from LearnerProgress p "
	    + " where p.lesson.id = :lessonId order by p.user.lastName, p.user.firstName, p.user.userId";

    private final static String LOAD_PROGRESS_BY_LESSON_AND_USER_IDS = "from LearnerProgress p "
	    + " where p.lesson.id = :lessonId AND p.user.userId IN (:userIds) order by p.user.lastName, p.user.firstName, p.user.userId";

    private final static String LOAD_PROGRESSES_BY_LESSON_LIST = "FROM LearnerProgress progress WHERE "
	    + " progress.lesson.lessonId IN (:lessonIds)";

    private final static String LOAD_LEARNERS_LATEST_BY_ACTIVITY = "SELECT u.* FROM lams_learner_progress AS prog "
	    + "JOIN lams_user AS u USING (user_id) "
	    + "LEFT JOIN lams_progress_attempted AS att ON prog.learner_progress_id = att.learner_progress_id AND att.activity_id = :activityId "
	    + "LEFT JOIN lams_progress_completed AS comp ON prog.learner_progress_id = comp.learner_progress_id AND comp.activity_id = :activityId "
	    + "WHERE prog.current_activity_id = :activityId AND (att.learner_progress_id IS NOT NULL OR comp.learner_progress_id IS NOT NULL) "
	    + "ORDER BY att.start_date_time DESC, comp.start_date_time DESC";

    private final static String LOAD_LEARNERS_BY_ACTIVITIES = "SELECT prog.user FROM LearnerProgress prog WHERE "
	    + " prog.currentActivity.id IN (:activityIds) "
	    + "ORDER BY prog.user.lastName <ORDER>, prog.user.firstName <ORDER>, prog.user.login <ORDER>";

    private final static String COUNT_LEARNERS_BY_LESSON = "COUNT(*) FROM LearnerProgress prog WHERE prog.lesson.id = :lessonId";
    private final static String COUNT_LEARNERS_BY_LESSON_ORDER_CLAUSE = " ORDER BY prog.user.lastName ASC, prog.user.firstName ASC, prog.user.login ASC";

    // find Learners for the given Lesson first, then see if they have Progress, i.e. started the lesson
    private final static String LOAD_LEARNERS_BY_MOST_PROGRESS = "SELECT u.*, COUNT(comp.activity_id) AS comp_count FROM lams_lesson AS lesson "
	    + "JOIN lams_grouping AS grping ON lesson.class_grouping_id = grping.grouping_id "
	    + "JOIN lams_group AS g USING (grouping_id) JOIN lams_user_group AS ug USING (group_id) "
	    + "JOIN lams_user AS u ON ug.user_id = u.user_id "
	    + "LEFT JOIN lams_learner_progress AS prog ON prog.lesson_id = lesson.lesson_id AND prog.user_id = u.user_id "
	    + "LEFT JOIN lams_progress_completed AS comp USING (learner_progress_id) "
	    + "WHERE lesson.lesson_id = :lessonId AND g.group_name NOT LIKE '%Staff%'";
    private final static String LOAD_LEARNERS_BY_MOST_PROGRESS_ORDER_CLAUSE = " GROUP BY u.user_id "
	    + "ORDER BY prog.lesson_completed_flag DESC, comp_count DESC, u.last_name ASC, u.first_name ASC, u.login ASC";

    private final static String FIND_PROGRESS_ARCHIVE_MAX_ATTEMPT = "SELECT MAX(p.attemptId) FROM LearnerProgressArchive p "
	    + "WHERE p.user.id = :learnerId AND p.lesson.id = :lessonId";

    private final static String FIND_PROGRESS_ARCHIVE_BY_DATE = "FROM LearnerProgressArchive a "
	    + "WHERE a.lesson.lessonId = :lessonId AND a.user.userId = :learnerId AND a.archiveDate = :archiveDate";

    @Override
    public LearnerProgress getLearnerProgress(Long learnerProgressId) {
	return getSession().get(LearnerProgress.class, learnerProgressId);
    }

    @Override
    public void saveLearnerProgress(LearnerProgress learnerProgress) {
	getSession().save(learnerProgress);
    }

    @Override
    public void deleteLearnerProgress(LearnerProgress learnerProgress) {
	getSession().delete(learnerProgress);
    }

    @Override
    public LearnerProgress getLearnerProgressByLearner(Integer learnerId, Long lessonId) {

	return (LearnerProgress) getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LEARNER)
		.setInteger("learnerId", learnerId).setLong("lessonId", lessonId).uniqueResult();
    }

    @Override
    public void updateLearnerProgress(LearnerProgress learnerProgress) {
	this.getSession().update(learnerProgress);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressReferringToActivity(Activity activity) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_REFFERING_TO_ACTIVITY)
		.setEntity("activity", activity).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersLatestByActivity(Long activityId, Integer limit, Integer offset) {
	Query query = getSession().createSQLQuery(LearnerProgressDAO.LOAD_LEARNERS_LATEST_BY_ACTIVITY)
		.addEntity(User.class).setLong("activityId", activityId);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByActivities(Long[] activityIds, Integer limit, Integer offset,
	    boolean orderAscending) {
	Query query = getSession().createQuery(
		LearnerProgressDAO.LOAD_LEARNERS_BY_ACTIVITIES.replaceAll("<ORDER>", orderAscending ? "ASC" : "DESC"))
		.setParameterList("activityIds", activityIds);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersLatestCompletedForLesson(Long lessonId, Integer limit, Integer offset) {
	Query query = getSession().createQuery(LearnerProgressDAO.LOAD_LEARNERS_LATEST_COMPLETED_BY_LESSON)
		.setLong("lessonId", lessonId);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByMostProgress(Long lessonId, String searchPhrase, Integer limit, Integer offset) {
	StringBuilder queryText = new StringBuilder(LearnerProgressDAO.LOAD_LEARNERS_BY_MOST_PROGRESS);
	// find the search phrase parts in any of name parts of the user
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryText.append(" AND (u.first_name LIKE '%").append(token).append("%' OR u.last_name LIKE '%")
			.append(token).append("%' OR u.login LIKE '%").append(token).append("%')");
	    }
	}
	queryText.append(LearnerProgressDAO.LOAD_LEARNERS_BY_MOST_PROGRESS_ORDER_CLAUSE);

	Query query = getSession().createSQLQuery(queryText.toString()).addEntity(User.class).setLong("lessonId",
		lessonId);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getCompletedLearnerProgressForLesson(Long lessonId, Integer limit, Integer offset,
	    boolean orderAscending) {
	Query query = getSession().createQuery(LearnerProgressDAO.LOAD_COMPLETED_PROGRESS_BY_LESSON
		.replaceAll("<ORDER>", orderAscending ? "ASC" : "DESC")).setLong("lessonId", lessonId);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLesson(Long lessonId) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LESSON).setLong("lessonId", lessonId)
		.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLesson(Long lessonId, List<Integer> userIds) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LESSON_AND_USER_IDS)
		.setLong("lessonId", lessonId).setParameterList("userIds", userIds).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLessons(List<Long> lessonIds) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESSES_BY_LESSON_LIST)
		.setParameterList("lessonIds", lessonIds).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getLearnersAttemptedOrCompletedActivity(Activity activity) {
	List<User> users = getSession().createQuery(LearnerProgressDAO.LOAD_LEARNERS_ATTEMPTED_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).list();
	users.addAll(getSession().createQuery(LearnerProgressDAO.LOAD_LEARNERS_COMPLETED_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).list());

	return users;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getLearnersAttemptedActivity(Activity activity) {
	List<User> users = getSession().createQuery(LearnerProgressDAO.LOAD_LEARNERS_ATTEMPTED_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).list();

	return users;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getLearnersCompletedActivity(final Activity activity) {
	List<User> users = getSession().createQuery(LearnerProgressDAO.LOAD_LEARNERS_COMPLETED_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).list();

	return users;
    }

    @Override
    public Integer getNumUsersAttemptedActivity(Activity activity) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_ATTEMPTED_ACTIVITY)
		.setParameter("activityId", activity.getActivityId().longValue()).uniqueResult();
	return ((Number) value).intValue();
    }

    @Override
    public Integer getNumUsersAttemptedOrCompletedActivity(Activity activity) {
	return getNumUsersAttemptedActivity(activity) + getNumUsersCompletedActivity(activity).intValue();
    }

    @Override
    public Integer getNumUsersCompletedActivity(Activity activity) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_COMPLETED_ACTIVITY)
		.setParameter("activityId", activity.getActivityId().longValue()).uniqueResult();
	return ((Number) value).intValue();
    }

    @Override
    public Integer getNumUsersByLesson(Long lessonId, String searchPhrase) {
	StringBuilder queryText = new StringBuilder(LearnerProgressDAO.COUNT_LEARNERS_BY_LESSON);
	// find the search phrase parts in any of name parts of the user
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryText.append(" AND (prog.user.firstName LIKE '%").append(token)
			.append("%' OR prog.user.lastName LIKE '%").append(token)
			.append("%' OR prog.user.login LIKE '%").append(token).append("%')");
	    }
	}
	queryText.append(LearnerProgressDAO.COUNT_LEARNERS_BY_LESSON_ORDER_CLAUSE);

	Object value = getSession().createQuery(queryText.toString()).setLong("lessonId", lessonId.longValue())
		.uniqueResult();
	return ((Number) value).intValue();
    }

    @Override
    public Integer getNumUsersCompletedLesson(Long lessonId) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_COMPLETED_PROGRESS_BY_LESSON)
		.setLong("lessonId", lessonId).uniqueResult();
	return ((Number) value).intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Long, Integer> getNumUsersCurrentActivities(Long[] activityIds) {
	List<Object[]> resultQuery = getSession().createQuery(LearnerProgressDAO.COUNT_CURRENT_ACTIVITY)
		.setParameterList("activityIds", activityIds).list();
	Map<Long, Integer> result = new TreeMap<>();
	// put all requested activity IDs into the result
	for (Long activityId : activityIds) {
	    result.put(activityId, 0);
	}
	// update only the existing ones
	for (Object[] entry : resultQuery) {
	    // for some reason entry can be null
	    if (entry != null) {
		result.put((Long) entry[0], ((Long) entry[1]).intValue());
	    }
	}
	return result;
    }

    @Override
    public Integer getNumUsersCurrentActivity(Activity activity) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_SINGLE_CURRENT_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
	return new Integer(((Number) value).intValue());
    }

    @Override
    public Integer getLearnerProgressArchiveMaxAttemptID(Integer userId, Long lessonId) {
	Object value = getSession().createQuery(LearnerProgressDAO.FIND_PROGRESS_ARCHIVE_MAX_ATTEMPT)
		.setInteger("learnerId", userId).setLong("lessonId", lessonId).setCacheable(true).uniqueResult();
	return value == null ? null : ((Number) value).intValue();
    }

    @Override
    public LearnerProgressArchive getLearnerProgressArchive(Long lessonId, Integer userId, Date archiveDate) {
	return (LearnerProgressArchive) getSession().createQuery(LearnerProgressDAO.FIND_PROGRESS_ARCHIVE_BY_DATE)
		.setInteger("learnerId", userId).setLong("lessonId", lessonId).setTimestamp("archiveDate", archiveDate)
		.setCacheable(true).uniqueResult();
    }
}