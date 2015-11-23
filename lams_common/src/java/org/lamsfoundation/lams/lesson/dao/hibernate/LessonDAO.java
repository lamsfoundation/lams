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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Hibernate implementation of ILessonDAO
 * 
 * @author chris
 */
public class LessonDAO extends BaseDAO implements ILessonDAO {
    private final static String FIND_LESSON_BY_CREATOR = "from " + Lesson.class.getName()
	    + " lesson where lesson.user.userId=? and lesson.lessonStateId <= 6 and "
	    + " lesson.learningDesign.copyTypeID=" + LearningDesign.COPY_TYPE_LESSON;
    private final static String FIND_PREVIEW_BEFORE_START_DATE = "from " + Lesson.class.getName()
	    + " lesson where lesson.learningDesign.copyTypeID=" + LearningDesign.COPY_TYPE_PREVIEW
	    + "and lesson.startDateTime is not null and lesson.startDateTime < ?";
    private final static String COUNT_ACTIVE_LEARNERS = "select count(distinct progress.user.id)" + " from "
	    + LearnerProgress.class.getName() + " progress" + " where progress.lesson.id = :lessonId";
    private final static String FIND_LESSON_FOR_ACTIVITY = "select lesson from " + Lesson.class.getName() + " lesson, "
	    + Activity.class.getName() + " activity "
	    + " where activity.activityId=:activityId and activity.learningDesign=lesson.learningDesign";
    private final static String LESSONS_WITH_ORIGINAL_LEARNING_DESIGN = "select l from " + Lesson.class.getName()
	    + " l " + "where l.learningDesign.originalLearningDesign.learningDesignId = ? "
	    + "and l.learningDesign.copyTypeID != " + LearningDesign.COPY_TYPE_PREVIEW + " " + "and l.lessonStateId = "
	    + Lesson.STARTED_STATE + " " + "and l.organisation.organisationId = ? " + " order by l.lessonName";
    private final static String LESSONS_BY_GROUP = "from " + Lesson.class.getName()
	    + " where organisation.organisationId=? and lessonStateId <= 6";
    private final static String LESSON_BY_SESSION_ID = "select lesson from Lesson lesson, ToolSession session where "
	    + "session.lesson=lesson and session.toolSessionId=:toolSessionID";

    private final static String LOAD_LEARNERS_BY_LESSON = "FROM Lesson AS lesson "
	    + "INNER JOIN lesson.lessonClass AS lessonClass INNER JOIN lessonClass.groups AS groups "
	    + "INNER JOIN groups.users AS users "
	    + "WHERE lesson.id = :lessonId AND groups.groupName NOT LIKE '%Staff%'";

    /**
     * Retrieves the Lesson. Used in instances where it cannot be lazy loaded so it forces an initialize.
     * 
     * @param lessonId
     *            identifies the lesson to get
     * @return the lesson
     */
    @Override
    public Lesson getLesson(Long lessonId) {
	Lesson lesson = (Lesson) getHibernateTemplate().get(Lesson.class, lessonId);
	return lesson;
    }

    @Override
    public Lesson getLessonWithJoinFetchedProgress(final Long lessonId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (Lesson) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createCriteria(Lesson.class).add(Restrictions.like("lessonId", lessonId))
			.setFetchMode("learnerProgresses", FetchMode.JOIN).uniqueResult();
	    }
	});
    }

    /** Get all the lessons in the database. This includes the disabled lessons. */
    @Override
    public List getAllLessons() {
	return getHibernateTemplate().loadAll(Lesson.class);
    }

    /**
     * Gets all lessons that are active for a learner.
     * 
     * @param learner
     *            a User that identifies the learner.
     * @return a List with all active lessons in it.
     */
    @Override
    public List getActiveLessonsForLearner(final User learner) {
	List lessons = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	lessons = (List) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.getNamedQuery("activeLessonsAllOrganisations");
		query.setInteger("userId", learner.getUserId().intValue());
		List result = query.list();
		return result;
	    }
	});
	return lessons;
    }

    /**
     * Gets all lessons that are active for a learner, in a given organisation
     * 
     * @param learnerId
     *            a User that identifies the learner.
     * @param organisationId
     *            the desired organisation.
     * @return a List with all active lessons in it.
     */
    @Override
    public List<Lesson> getActiveLessonsForLearner(final Integer learnerId, final Integer organisationId) {
	List lessons = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	lessons = (List) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.getNamedQuery("activeLessons");
		query.setInteger("userId", learnerId);
		query.setInteger("organisationId", organisationId);
		List result = query.list();
		return result;
	    }
	});
	return lessons;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getActiveLearnerByLesson(long)
     */
    @Override
    public List getActiveLearnerByLesson(final long lessonId) {
	List learners = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	learners = (List) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.getNamedQuery("activeLearners");
		query.setLong("lessonId", lessonId);
		List result = query.list();
		return result;
	    }
	});
	return learners;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getActiveLearnerByLesson(long) Note: Hibernate 3.1
     *      query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
     */
    @Override
    public Integer getCountActiveLearnerByLesson(final long lessonId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(LessonDAO.COUNT_ACTIVE_LEARNERS);
		query.setLong("lessonId", lessonId);
		Object value = query.uniqueResult();
		return ((Number) value).intValue();
	    }
	});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByLesson(final Long lessonId, String searchPhrase, final Integer limit,
	    final Integer offset, boolean orderAscending) {
	final String queryText = LessonDAO.buildLearnersByLessonQuery(false, searchPhrase, orderAscending);

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

    @Override
    public Integer getCountLearnersByLesson(final long lessonId, String searchPhrase) {
	final String queryText = LessonDAO.buildLearnersByLessonQuery(true, searchPhrase, true);
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(queryText).setLong("lessonId", lessonId);
		Object value = query.uniqueResult();
		return ((Number) value).intValue();
	    }
	});
    }

    /**
     * f Saves or Updates a Lesson.
     * 
     * @param lesson
     */
    @Override
    public void saveLesson(Lesson lesson) {
	getHibernateTemplate().save(lesson);
    }

    /**
     * Deletes a Lesson <b>permanently</b>.
     * 
     * @param lesson
     */
    @Override
    public void deleteLesson(Lesson lesson) {
	getHibernateTemplate().delete(lesson);
    }

    /**
     * Update the lesson data
     * 
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#updateLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    @Override
    public void updateLesson(Lesson lesson) {
	getHibernateTemplate().update(lesson);
    }

    /**
     * Returns the list of available Lessons created by a given user. Does not return disabled lessons or preview
     * lessons.
     * 
     * @param userID
     *            The user_id of the user
     * @return List The list of Lessons for the given user
     */
    @Override
    public List getLessonsCreatedByUser(Integer userID) {
	List lessons = this.getHibernateTemplate().find(LessonDAO.FIND_LESSON_BY_CREATOR, userID);
	return lessons;
    }

    /**
     * Gets all lessons in the given organisation, for which this user is in the staff group. Does not return disabled
     * lessons or preview lessons. This is the list of lessons that a user may monitor/moderate/manage.
     * 
     * @param user
     *            a User that identifies the teacher/staff member.
     * @return a List with all appropriate lessons in it.
     */
    @Override
    public List getLessonsForMonitoring(final int userID, final int organisationID) {
	List lessons = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	lessons = (List) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.getNamedQuery("lessonsForMonitoring");
		query.setInteger("userId", userID);
		query.setInteger("organisationId", organisationID);
		List result = query.list();
		return result;
	    }
	});
	return lessons;
    }

    /**
     * Get all the preview lessons more with the creation date before the given date.
     * 
     * @param startDate
     *            UTC date
     * @return the list of Lessons
     */
    @Override
    public List getPreviewLessonsBeforeDate(final Date startDate) {
	List lessons = this.getHibernateTemplate().find(LessonDAO.FIND_PREVIEW_BEFORE_START_DATE, startDate);
	return lessons;
    }

    /**
     * Get the lesson that applies to this activity. Not all activities have an attached lesson.
     */
    @Override
    public Lesson getLessonForActivity(final long activityId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Lesson) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(LessonDAO.FIND_LESSON_FOR_ACTIVITY);
		query.setLong("activityId", activityId);
		return query.uniqueResult();
	    }
	});
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonsByOrgAndUserWithCompletedFlag(Integer, Integer,
     *      boolean)
     */
    @Override
    public List getLessonsByOrgAndUserWithCompletedFlag(final Integer userId, final Integer orgId,
	    final Integer userRole) {
	List dtos = null;

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	dtos = (List) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {

		String queryName;
		if (Role.ROLE_MONITOR.equals(userRole)) {
		    queryName = "staffLessonsByOrgAndUserWithCompletedFlag";
		} else if (Role.ROLE_LEARNER.equals(userRole)) {
		    queryName = "learnerLessonsByOrgAndUserWithCompletedFlag";
		} else {
		    // in case of Role.ROLE_GROUP_MANAGER
		    queryName = "allLessonsByOrgAndUserWithCompletedFlag";
		}

		Query query = session.getNamedQuery(queryName);
		query.setInteger("userId", userId.intValue());
		query.setInteger("orgId", orgId.intValue());
		List result = query.list();
		return result;
	    }
	});
	return dtos;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonsByOrgAndUserWithCompletedFlag(Integer, Integer,
     *      boolean)
     */
    @Override
    public List getLessonsByGroupAndUser(final Integer userId, final Integer orgId) {
	List dtos = null;
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	dtos = (List) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.getNamedQuery("lessonsByOrgAndUserWithChildOrgs");
		query.setInteger("userId", userId.intValue());
		query.setInteger("orgId", orgId.intValue());
		List result = query.list();
		return result;
	    }
	});
	return dtos;
    }

    @Override
    public List getLessonsByGroup(final Integer orgId) {
	return this.getHibernateTemplate().find(LessonDAO.LESSONS_BY_GROUP, orgId);
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonsByOriginalLearningDesign(Integer)
     */
    @Override
    public List getLessonsByOriginalLearningDesign(final Long ldId, final Integer orgId) {
	Object[] args = { ldId.longValue(), orgId.intValue() };
	List lessons = this.getHibernateTemplate().find(LessonDAO.LESSONS_WITH_ORIGINAL_LEARNING_DESIGN, args);
	return lessons;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getMonitorsByToolSessionId(Long)
     */
    @Override
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return this.getHibernateTemplate().findByNamedQueryAndNamedParam("monitorsByToolSessionId", "sessionId",
		sessionId);
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonDetailsFromSessionID(java.lang.Long)
     */
    @Override
    public Lesson getLessonFromSessionID(final Long toolSessionID) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Lesson) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(LessonDAO.LESSON_BY_SESSION_ID);
		query.setLong("toolSessionID", toolSessionID);
		return query.uniqueResult();
	    }
	});
    }

    private static String buildLearnersByLessonQuery(boolean count, String searchPhrase, boolean orderAscending) {
	StringBuilder queryText = new StringBuilder("SELECT ").append(count ? "COUNT(*) " : "users ")
		.append(LessonDAO.LOAD_LEARNERS_BY_LESSON);
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryText.append(" AND (users.firstName LIKE '%").append(token).append("%' OR users.lastName LIKE '%")
			.append(token).append("%' OR users.login LIKE '%").append(token).append("%')");
	    }
	}
	if (!count) {
	    String order = orderAscending ? "ASC" : "DESC";
	    queryText.append(" ORDER BY users.firstName ").append(order).append(", users.lastName ").append(order)
		    .append(", users.login ").append(order);
	}
	return queryText.toString();
    }
}