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
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of ILearnerProgressDAO
 * 
 * @author chris
 */
public class LearnerProgressDAO extends HibernateDaoSupport implements ILearnerProgressDAO {

    protected Logger log = Logger.getLogger(LearnerProgressDAO.class);

    private final static String LOAD_PROGRESS_BY_LEARNER = "from LearnerProgress p where p.user.id = :learnerId and p.lesson.id = :lessonId";

    private final static String LOAD_PROGRESS_REFFERING_TO_ACTIVITY = "from LearnerProgress p where p.previousActivity = :activity or p.currentActivity = :activity or p.nextActivity = :activity ";

    private final static String LOAD_COMPLETED_PROGRESS_BY_LESSON = "FROM LearnerProgress p WHERE p.lessonComplete > 0 "
	    + "AND p.lesson.id = :lessonId ORDER BY p.user.firstName <ORDER>, p.user.lastName <ORDER>, p.user.login <ORDER>";

    private final static String LOAD_LEARNERS_LATEST_COMPLETED_BY_LESSON = "SELECT p.user FROM LearnerProgress p WHERE "
	    + "p.lessonComplete > 0 and p.lesson.id = :lessonId ORDER BY p.finishDate DESC";

    private final static String LOAD_LEARNERS_ATTEMPTED_ACTIVITY = "SELECT prog.user FROM LearnerProgress prog, "
	    + " Activity act join prog.attemptedActivities attAct where act.id = :activityId and index(attAct) = act";

    private final static String LOAD_LEARNERS_COMPLETED_ACTIVITY = "SELECT prog.user FROM LearnerProgress prog, "
	    + " Activity act join prog.completedActivities compAct where act.id = :activityId and index(compAct) = act";

    private final static String COUNT_COMPLETED_PROGRESS_BY_LESSON = "select count(*) from LearnerProgress p "
	    + " where p.lessonComplete > 0 and p.lesson.id = :lessonId";

    private final static String COUNT_ATTEMPTED_ACTIVITY = "select count(*) from LearnerProgress prog, "
	    + " Activity act join prog.attemptedActivities attAct " + " where act.id = :activityId and "
	    + " index(attAct) = act";

    private final static String COUNT_COMPLETED_ACTIVITY = "select count(*) from LearnerProgress prog, "
	    + " Activity act join prog.completedActivities compAct " + " where act.id = :activityId and "
	    + " index(compAct) = act";

    private final static String COUNT_CURRENT_ACTIVITY = "select count(*) from LearnerProgress prog WHERE "
	    + " prog.currentActivity = :activity";

    private final static String LOAD_PROGRESS_BY_LESSON = "from LearnerProgress p "
	    + " where p.lesson.id = :lessonId order by p.user.lastName, p.user.firstName, p.user.userId";

    private final static String LOAD_PROGRESS_BY_LESSON_AND_USER_IDS = "from LearnerProgress p "
	    + " where p.lesson.id = :lessonId AND p.user.userId IN (:userIds) order by p.user.lastName, p.user.firstName, p.user.userId";

    private final static String LOAD_PROGRESSES_BY_LESSON_LIST = "FROM LearnerProgress progress WHERE "
	    + " progress.lesson.lessonId IN (:lessonIds)";

    private final static String LOAD_LEARNERS_LATEST_BY_ACTIVITY = "SELECT u.* FROM lams_learner_progress AS prog "
	    + "JOIN lams_progress_attempted AS att USING (learner_progress_id) "
	    + "JOIN lams_user AS u USING (user_id) "
	    + "WHERE prog.current_activity_id = :activityId AND att.activity_id = :activityId "
	    + "ORDER BY att.start_date_time DESC";

    private final static String LOAD_LEARNERS_BY_ACTIVITIES = "SELECT prog.user FROM LearnerProgress prog WHERE "
	    + " prog.currentActivity.id IN (:activityIds) "
	    + "ORDER BY prog.user.firstName <ORDER>, prog.user.lastName <ORDER>, prog.user.login <ORDER>";

    private final static String COUNT_LEARNERS_BY_LESSON = "COUNT(*) FROM LearnerProgress prog WHERE prog.lesson.id = :lessonId";
    private final static String COUNT_LEARNERS_BY_LESSON_ORDER_CLAUSE = " ORDER BY prog.user.firstName ASC, prog.user.lastName ASC, prog.user.login ASC";

    // find Learners for the given Lesson first, then see if they have Progress, i.e. started the lesson
    private final static String LOAD_LEARNERS_BY_MOST_PROGRESS = "SELECT u.*, COUNT(comp.activity_id) AS comp_count FROM lams_lesson AS lesson "
	    + "JOIN lams_grouping AS grouping ON lesson.class_grouping_id = grouping.grouping_id "
	    + "JOIN lams_group AS g USING (grouping_id) JOIN lams_user_group AS ug USING (group_id) "
	    + "JOIN lams_user AS u ON ug.user_id = u.user_id "
	    + "LEFT JOIN lams_learner_progress AS prog ON prog.lesson_id = lesson.lesson_id AND prog.user_id = u.user_id "
	    + "LEFT JOIN lams_progress_completed AS comp USING (learner_progress_id) "
	    + "WHERE lesson.lesson_id = :lessonId AND g.group_name NOT LIKE '%Staff%'";
    private final static String LOAD_LEARNERS_BY_MOST_PROGRESS_ORDER_CLAUSE = " GROUP BY u.user_id "
	    + "ORDER BY prog.lesson_completed_flag DESC, comp_count DESC, u.first_name ASC, u.last_name ASC, u.login ASC";

    @Override
    public LearnerProgress getLearnerProgress(Long learnerProgressId) {
	return (LearnerProgress) getHibernateTemplate().get(LearnerProgress.class, learnerProgressId);
    }

    @Override
    public void saveLearnerProgress(LearnerProgress learnerProgress) {
	getHibernateTemplate().save(learnerProgress);
    }

    @Override
    public void deleteLearnerProgress(LearnerProgress learnerProgress) {
	getHibernateTemplate().delete(learnerProgress);
    }

    @Override
    public LearnerProgress getLearnerProgressByLearner(final Integer learnerId, final Long lessonId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (LearnerProgress) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LEARNER)
			.setInteger("learnerId", learnerId).setLong("lessonId", lessonId).uniqueResult();
	    }
	});
    }

    @Override
    public void updateLearnerProgress(LearnerProgress learnerProgress) {
	this.getHibernateTemplate().update(learnerProgress);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressReferringToActivity(final Activity activity) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<LearnerProgress>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_PROGRESS_REFFERING_TO_ACTIVITY)
			.setEntity("activity", activity).list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersLatestByActivity(final Long activityId, final Integer limit, final Integer offset) {
	final HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createSQLQuery(LearnerProgressDAO.LOAD_LEARNERS_LATEST_BY_ACTIVITY)
			.addEntity(User.class).setLong("activityId", activityId);
		if (limit != null) {
		    query.setMaxResults(limit);
		}
		if (offset != null) {
		    query.setFirstResult(offset);
		}
		return query.list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByActivities(final Long[] activityIds, final Integer limit, final Integer offset,
	    boolean orderAscending) {
	final String queryText = LearnerProgressDAO.LOAD_LEARNERS_BY_ACTIVITIES.replaceAll("<ORDER>",
		orderAscending ? "ASC" : "DESC");
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(queryText).setParameterList("activityIds", activityIds);
		if (limit != null) {
		    query.setMaxResults(limit);
		}
		if (offset != null) {
		    query.setFirstResult(offset);
		}
		return query.list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersLatestCompletedForLesson(final Long lessonId, final Integer limit,
	    final Integer offset) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(LearnerProgressDAO.LOAD_LEARNERS_LATEST_COMPLETED_BY_LESSON)
			.setLong("lessonId", lessonId);
		if (limit != null) {
		    query.setMaxResults(limit);
		}
		if (offset != null) {
		    query.setFirstResult(offset);
		}
		return query.list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByMostProgress(final Long lessonId, String searchPhrase, final Integer limit,
	    final Integer offset) {
	final StringBuilder queryText = new StringBuilder(LearnerProgressDAO.LOAD_LEARNERS_BY_MOST_PROGRESS);
	// find the search phrase parts in any of name parts of the user
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryText.append(" AND (u.firstName LIKE '%").append(token).append("%' OR u.lastName LIKE '%")
			.append(token).append("%' OR u.login LIKE '%").append(token).append("%')");
	    }
	}
	queryText.append(LearnerProgressDAO.LOAD_LEARNERS_BY_MOST_PROGRESS_ORDER_CLAUSE);

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createSQLQuery(queryText.toString()).addEntity(User.class).setLong("lessonId",
			lessonId);
		if (limit != null) {
		    query.setMaxResults(limit);
		}
		if (offset != null) {
		    query.setFirstResult(offset);
		}
		return query.list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getCompletedLearnerProgressForLesson(final Long lessonId, final Integer limit,
	    final Integer offset, boolean orderAscending) {
	final String queryText = LearnerProgressDAO.LOAD_COMPLETED_PROGRESS_BY_LESSON.replaceAll("<ORDER>",
		orderAscending ? "ASC" : "DESC");
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<LearnerProgress>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(queryText).setLong("lessonId", lessonId);
		if (limit != null) {
		    query.setMaxResults(limit);
		}
		if (offset != null) {
		    query.setFirstResult(offset);
		}
		return query.list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLesson(final Long lessonId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<LearnerProgress>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LESSON).setLong("lessonId", lessonId)
			.list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLesson(final Long lessonId, final List<Integer> userIds) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<LearnerProgress>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LESSON_AND_USER_IDS)
			.setLong("lessonId", lessonId).setParameterList("userIds", userIds).list();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLessons(final List<Long> lessonIds) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<LearnerProgress>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_PROGRESSES_BY_LESSON_LIST)
			.setParameterList("lessonIds", lessonIds).list();
	    }
	});
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getLearnersAttemptedOrCompletedActivity(final Activity activity) {
	List<User> learners = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	learners = (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		List<User> users = session.createQuery(LearnerProgressDAO.LOAD_LEARNERS_ATTEMPTED_ACTIVITY)
			.setLong("activityId", activity.getActivityId().longValue()).list();
		return users.addAll(session.createQuery(LearnerProgressDAO.LOAD_LEARNERS_COMPLETED_ACTIVITY)
			.setLong("activityId", activity.getActivityId().longValue()).list());
	    }
	});

	return learners;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getLearnersAttemptedActivity(final Activity activity) {
	List<User> learners = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	learners = (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_LEARNERS_ATTEMPTED_ACTIVITY)
			.setLong("activityId", activity.getActivityId().longValue()).list();
	    }
	});

	return learners;
    }

    @Override
    public Integer getNumUsersAttemptedActivity(final Activity activity) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	Integer attempted = (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Object value = session.createQuery(LearnerProgressDAO.COUNT_ATTEMPTED_ACTIVITY)
			.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
		return new Integer(((Number) value).intValue());
	    }
	});
	return new Integer(attempted.intValue() + getNumUsersCompletedActivity(activity).intValue());
    }

    @Override
    public Integer getNumUsersCompletedActivity(final Activity activity) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Object value = session.createQuery(LearnerProgressDAO.COUNT_COMPLETED_ACTIVITY)
			.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
		return new Integer(((Number) value).intValue());
	    }
	});
    }

    @Override
    public Integer getNumUsersByLesson(final Long lessonId, String searchPhrase) {
	final StringBuilder queryText = new StringBuilder(LearnerProgressDAO.COUNT_LEARNERS_BY_LESSON);
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

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Object value = session.createQuery(queryText.toString()).setLong("lessonId", lessonId.longValue())
			.uniqueResult();
		return ((Number) value).intValue();
	    }
	});
    }

    @Override
    public Integer getNumUsersCompletedLesson(final Long lessonId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Object value = session.createQuery(LearnerProgressDAO.COUNT_COMPLETED_PROGRESS_BY_LESSON)
			.setLong("lessonId", lessonId).uniqueResult();
		return ((Number) value).intValue();
	    }
	});
    }

    @Override
    public Integer getNumUsersCurrentActivity(final Activity activity) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Object value = session.createQuery(LearnerProgressDAO.COUNT_CURRENT_ACTIVITY)
			.setEntity("activity", activity).uniqueResult();
		return ((Number) value).intValue();
	    }
	});
    }
}