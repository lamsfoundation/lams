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

    private final static String LOAD_COMPLETED_PROGRESS_BY_LESSON = "from LearnerProgress p where p.lessonComplete > 0 and p.lesson.id = :lessonId";

    private final static String LOAD_LEARNERS_LATEST_COMPLETED_BY_LESSON = "SELECT p.user FROM LearnerProgress p WHERE "
	    + "p.lessonComplete > 0 and p.lesson.id = :lessonId ORDER BY p.finishDate DESC";

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

    private final static String LOAD_LEARNERS_BY_ACTIVITIES = "SELECT p.user FROM LearnerProgress p WHERE "
	    + " p.currentActivity.id IN (:activityIds)";

    private final static String LOAD_LEARNERS_BY_LESSON = "FROM LearnerProgress prog WHERE prog.lesson.id = :lessonId";

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
    public List<User> getLearnersByActivities(final Long[] activityIds, final Integer limit, final Integer offset) {
	final HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(LearnerProgressDAO.LOAD_LEARNERS_BY_ACTIVITIES)
			.setParameterList("activityIds", activityIds);
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
    public List<User> getLearnersByLesson(final Long lessonId, String searchPhrase, boolean orderByCompletion,
	    final Integer limit, final Integer offset) {
	final String queryText = LearnerProgressDAO.buildLearnersByLessonQuery(false, searchPhrase, orderByCompletion);

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<User>) hibernateTemplate.execute(new HibernateCallback() {
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

    private static String buildLearnersByLessonQuery(boolean count, String searchPhrase, Boolean orderByCompletion) {
	StringBuilder queryText = new StringBuilder("SELECT ").append(count ? "COUNT(*) " : "prog.user ")
		.append(LearnerProgressDAO.LOAD_LEARNERS_BY_LESSON);
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryText.append(" AND (prog.user.firstName LIKE '%").append(token)
			.append("%' OR prog.user.lastName LIKE '%").append(token)
			.append("%' OR prog.user.login LIKE '%").append(token).append("%')");
	    }
	}
	if (!count && orderByCompletion != null) {
	    queryText.append(" ORDER BY");
	    if (orderByCompletion) {
		queryText.append(" prog.lessonComplete DESC, prog.completedActivities.size DESC,");
	    }
	    queryText.append(" prog.user.firstName ASC, prog.user.lastName ASC");
	}
	return queryText.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getCompletedLearnerProgressForLesson(final Long lessonId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (List<LearnerProgress>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LearnerProgressDAO.LOAD_COMPLETED_PROGRESS_BY_LESSON)
			.setLong("lessonId", lessonId).list();
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
    public List<User> getLearnersHaveAttemptedActivity(final Activity activity) {
	List<User> learners = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	learners = (List<User>) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.getNamedQuery("usersAttemptedActivity")
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
	final String queryText = buildLearnersByLessonQuery(true, searchPhrase, null);
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Object value = session.createQuery(queryText).setLong("lessonId", lessonId.longValue()).uniqueResult();
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