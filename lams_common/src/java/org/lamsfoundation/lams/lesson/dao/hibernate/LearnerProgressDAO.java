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

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
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

    private final static String LOAD_LEARNERS_LATEST_BY_ACTIVITY = "SELECT prog.user_id FROM lams_learner_progress AS prog "
	    + "JOIN lams_progress_attempted AS att USING (learner_progress_id) "
	    + "WHERE prog.current_activity_id = :activityId AND att.activity_id = :activityId "
	    + "ORDER BY att.start_date_time DESC";

    private final static String LOAD_LEARNERS_BY_ACTIVITIES = "SELECT p.user FROM LearnerProgress p WHERE "
	    + " p.currentActivity.id IN (:activityIds)";

    @Override
    public LearnerProgress getLearnerProgress(Long learnerProgressId) {
	return (LearnerProgress) getSession().get(LearnerProgress.class, learnerProgressId);
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
    public LearnerProgress getLearnerProgressByLearner(final Integer learnerId, final Long lessonId) {

	return (LearnerProgress) getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LEARNER)
		.setInteger("learnerId", learnerId).setLong("lessonId", lessonId).uniqueResult();
    }

    @Override
    public void updateLearnerProgress(LearnerProgress learnerProgress) {
	this.getSession().update(learnerProgress);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressReferringToActivity(final Activity activity) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_REFFERING_TO_ACTIVITY)
		.setEntity("activity", activity).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersLatestByActivity(final Long activityId, final Integer limit, final Integer offset) {
	Query query = getSession().createSQLQuery(LearnerProgressDAO.LOAD_LEARNERS_LATEST_BY_ACTIVITY)
		.setLong("activityId", activityId);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	// first query fetches only progress IDs
	List<BigInteger> result = query.list();
	// fetch user objects and return them
	List<User> learners = new LinkedList<User>();
	for (BigInteger userId : result) {
	    learners.add((User) getSession().get(User.class, userId.intValue()));
	}
	return learners;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByActivities(final Long[] activityIds, final Integer limit, final Integer offset) {
	Query query = getSession().createQuery(LearnerProgressDAO.LOAD_LEARNERS_BY_ACTIVITIES)
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
    public List<User> getLearnersLatestCompletedForLesson(final Long lessonId, final Integer limit,
	    final Integer offset) {
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
    public List<LearnerProgress> getCompletedLearnerProgressForLesson(final Long lessonId) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_COMPLETED_PROGRESS_BY_LESSON)
		.setLong("lessonId", lessonId).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLesson(final Long lessonId) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LESSON).setLong("lessonId", lessonId)
		.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLesson(final Long lessonId, final List<Integer> userIds) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESS_BY_LESSON_AND_USER_IDS)
		.setLong("lessonId", lessonId).setParameterList("userIds", userIds).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LearnerProgress> getLearnerProgressForLessons(final List<Long> lessonIds) {
	return getSession().createQuery(LearnerProgressDAO.LOAD_PROGRESSES_BY_LESSON_LIST)
		.setParameterList("lessonIds", lessonIds).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getLearnersHaveAttemptedActivity(final Activity activity) {
	List<User> learners = getSession().getNamedQuery("usersAttemptedActivity")
		.setLong("activityId", activity.getActivityId().longValue()).list();

	return learners;
    }

    @Override
    public Integer getNumUsersAttemptedActivity(final Activity activity) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_ATTEMPTED_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
	Integer attempted = new Integer(((Number) value).intValue());
	return new Integer(attempted.intValue() + getNumUsersCompletedActivity(activity).intValue());
    }

    @Override
    public Integer getNumUsersCompletedActivity(final Activity activity) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_COMPLETED_ACTIVITY)
		.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
	return new Integer(((Number) value).intValue());
    }

    @Override
    public Integer getNumUsersCompletedLesson(final Long lessonId) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_COMPLETED_PROGRESS_BY_LESSON)
		.setLong("lessonId", lessonId).uniqueResult();
	return ((Number) value).intValue();
    }

    @Override
    public Integer getNumUsersCurrentActivity(final Activity activity) {
	Object value = getSession().createQuery(LearnerProgressDAO.COUNT_CURRENT_ACTIVITY)
		.setEntity("activity", activity).uniqueResult();
	return ((Number) value).intValue();
    }
}