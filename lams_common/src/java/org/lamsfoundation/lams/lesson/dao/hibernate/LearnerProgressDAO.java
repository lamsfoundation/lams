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

import org.apache.log4j.Logger;
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
    private final static String LOAD_PROGRESS_BY_ACTIVITY = "from LearnerProgress p where p.previousActivity = :activity or p.currentActivity = :activity or p.nextActivity = :activity ";
    // +
    // "or activity in elements(p.previousActivity) or activity in elements(p.completedActivities)";
    private final static String LOAD_COMPLETED_PROGRESS_BY_LESSON = "from LearnerProgress p where p.lessonComplete > 0 and p.lesson.id = :lessonId";

    private final static String COUNT_ATTEMPTED_ACTIVITY = "select count(*) from LearnerProgress prog, "
	    + " Activity act join prog.attemptedActivities attAct " + " where act.id = :activityId and "
	    + " index(attAct) = act";
    private final static String COUNT_COMPLETED_ACTIVITY = "select count(*) from LearnerProgress prog, "
	    + " Activity act join prog.completedActivities compAct " + " where act.id = :activityId and "
	    + " index(compAct) = act";

    private final static String COUNT_PROGRESS_BY_LESSON = "select count(*) from LearnerProgress p where p.lesson.id = :lessonId";
    
    private final static String LOAD_PROGRESS_BY_LESSON = "from LearnerProgress p "
	    + " where p.lesson.id = :lessonId order by p.user.lastName, p.user.firstName, p.user.userId";
    
    private final static String LOAD_PROGRESS_BY_LESSON_AND_USER_IDS = "from LearnerProgress p "
	    + " where p.lesson.id = :lessonId AND p.user.userId IN (:userIds) order by p.user.lastName, p.user.firstName, p.user.userId";

    private final String LOAD_PROGRESSES_BY_LESSON_LIST = "FROM LearnerProgress progress WHERE "
	    + " progress.lesson.lessonId IN (:lessonIds)";
    
    private final static String LOAD_NEXT_BATCH_PROGRESS_BY_LESSON = "from LearnerProgress p where p.lesson.id = :lessonId "
	    + " and (( p.user.lastName > :lastUserLastName)"
	    + " or ( p.user.lastName = :lastUserLastName and p.user.firstName > :lastUserFirstName) "
	    + " or ( p.user.lastName = :lastUserLastName and p.user.firstName = :lastUserFirstName and p.user.userId > :lastUserId))"
	    + " order by p.user.lastName, p.user.firstName, p.user.userId";

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

		return (LearnerProgress) getSession().createQuery(LOAD_PROGRESS_BY_LEARNER).setInteger("learnerId", learnerId)
				.setLong("lessonId", lessonId).uniqueResult();
	}

    @Override
    public void updateLearnerProgress(LearnerProgress learnerProgress) {
	this.getSession().update(learnerProgress);
    }

    @Override
	public List getLearnerProgressReferringToActivity(final Activity activity) {
		return (List) getSession().createQuery(LOAD_PROGRESS_BY_ACTIVITY).setEntity("activity", activity).list();
	}

	@Override
	public List getCompletedLearnerProgressForLesson(final Long lessonId) {

		return (List) getSession().createQuery(LOAD_COMPLETED_PROGRESS_BY_LESSON).setLong("lessonId", lessonId).list();
	}
    
	@Override
	public List<LearnerProgress> getLearnerProgressForLesson(final Long lessonId) {
		return (List<LearnerProgress>) getSession().createQuery(LOAD_PROGRESS_BY_LESSON).setLong("lessonId", lessonId).list();
	}
	    
    @Override
    public List getLearnerProgressForLesson(final Long lessonId, final List<Integer> userIds) {
	return getSession().createQuery(LOAD_PROGRESS_BY_LESSON_AND_USER_IDS).setLong("lessonId", lessonId)
		.setParameterList("userIds", userIds).list();
    }

    @Override
    public List<LearnerProgress> getLearnerProgressForLessons(final List<Long> lessonIds) {
	return getSession().createQuery(LOAD_PROGRESSES_BY_LESSON_LIST).setParameterList("lessonIds", lessonIds).list();
    }

    @Override
    @SuppressWarnings("unchecked")
	public List<User> getLearnersHaveAttemptedActivity(final Activity activity) {
		List<User> learners = (List<User>) getSession().getNamedQuery("usersAttemptedActivity")
				.setLong("activityId", activity.getActivityId().longValue()).list();

		return learners;
	}

    @Override
    public Integer getNumUsersAttemptedActivity(final Activity activity) {
		Object value = getSession().createQuery(COUNT_ATTEMPTED_ACTIVITY)
			.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
		Integer attempted = new Integer(((Number) value).intValue());
		return new Integer(attempted.intValue() + getNumUsersCompletedActivity(activity).intValue());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getLearnersHaveCompletedActivity(final Activity activity) {
		List<User> learners = null;

		learners = (List<User>) getSession().getNamedQuery("usersCompletedActivity")
				.setLong("activityId", activity.getActivityId().longValue()).list();
		return learners;
	}

	@Override
	public Integer getNumUsersCompletedActivity(final Activity activity) {
		Object value = getSession().createQuery(COUNT_COMPLETED_ACTIVITY)
				.setLong("activityId", activity.getActivityId().longValue()).uniqueResult();
		return new Integer(((Number) value).intValue());
	}

    @Override
    public Integer getNumAllLearnerProgress(final Long lessonId) {
		Object value = getSession().createQuery(COUNT_PROGRESS_BY_LESSON).setLong("lessonId", lessonId.longValue())
			.uniqueResult();
		return new Integer(((Number) value).intValue());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<LearnerProgress> getBatchLearnerProgress(final Long lessonId, final User lastUser, final int batchSize) {

		if (lastUser == null) {
			return (List<LearnerProgress>) getSession().createQuery(LOAD_PROGRESS_BY_LESSON)
					.setLong("lessonId", lessonId.longValue()).setMaxResults(batchSize).list();

		} else {
			return (List<LearnerProgress>) getSession().createQuery(LOAD_NEXT_BATCH_PROGRESS_BY_LESSON)
					.setLong("lessonId", lessonId.longValue()).setString("lastUserLastName", lastUser.getLastName())
					.setString("lastUserFirstName", lastUser.getFirstName())
					.setInteger("lastUserId", lastUser.getUserId().intValue()).setMaxResults(batchSize).list();
		}
	}

}
