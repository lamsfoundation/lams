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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of ILessonDAO
 * 
 * @author chris
 */
@Repository
public class LessonDAO extends LAMSBaseDAO implements ILessonDAO {
    private final static String FIND_LESSON_BY_CREATOR = "from " + Lesson.class.getName()
	    + " lesson where lesson.user.userId=? and lesson.lessonStateId <= 6 and "
	    + " lesson.learningDesign.copyTypeID=" + LearningDesign.COPY_TYPE_LESSON;
    private final static String FIND_PREVIEW_BEFORE_START_DATE = "from " + Lesson.class.getName()
	    + " lesson where lesson.learningDesign.copyTypeID=" + LearningDesign.COPY_TYPE_PREVIEW
	    + "and lesson.startDateTime is not null and lesson.startDateTime < ?";
    private final static String COUNT_ACTIVE_LEARNERS = "select count(distinct progress.user.id)" + " from "
	    + LearnerProgress.class.getName() + " progress" + " where progress.lesson.id = :lessonId";
    private final static String LOAD_ACTIVE_LEARNERS = "select distinct progress.user from "
	    + LearnerProgress.class.getName() + " progress where progress.lesson.id = :lessonId";
    private final static String FIND_LESSON_FOR_ACTIVITY = "select lesson from " + Lesson.class.getName() + " lesson, "
	    + Activity.class.getName() + " activity "
	    + " where activity.activityId=:activityId and activity.learningDesign=lesson.learningDesign";
    private final static String LESSONS_WITH_ORIGINAL_LEARNING_DESIGN = "select l from " + Lesson.class.getName()
	    + " l " + "where l.learningDesign.originalLearningDesign.learningDesignId = ? "
	    + "and l.learningDesign.copyTypeID != " + LearningDesign.COPY_TYPE_PREVIEW + " " + "and l.lessonStateId = "
	    + Lesson.STARTED_STATE + " " + "and l.organisation.organisationId = ? " + " order by l.lessonName";
    private final static String LESSONS_BY_GROUP = "from " + Lesson.class.getName()
	    + " where organisation.organisationId=? and lessonStateId <= 6";

    private final static String LOAD_LEARNERS_BY_LESSON = "FROM Lesson AS lesson "
	    + "INNER JOIN lesson.lessonClass AS lessonClass INNER JOIN lessonClass.groups AS groups "
	    + "INNER JOIN groups.users AS users WHERE lesson.id = :lessonId AND lessonClass.staffGroup != groups";

    private final static String LOAD_USERS_WITH_LESSON_PARTICIPATION = "SELECT users.*, ug.user_id IS NOT NULL AS participant "
	    + "FROM lams_lesson AS l "
	    + "JOIN lams_user_organisation AS uo ON l.lesson_id = :lessonId AND l.organisation_id = uo.organisation_id "
	    + "JOIN lams_user_organisation_role AS r ON r.role_id = :roleId AND r.user_organisation_id = uo.user_organisation_id "
	    + "JOIN lams_user AS users ON uo.user_id = users.user_id "
	    + "JOIN lams_grouping AS ging ON l.class_grouping_id = ging.grouping_id "
	    + "JOIN lams_group AS g ON g.group_id <IS_STAFF> ging.staff_group_id AND g.grouping_id = ging.grouping_id "
	    + "LEFT JOIN lams_user_group AS ug ON ug.group_id = g.group_id AND users.user_id = ug.user_id";

    /**
     * Retrieves the Lesson. Used in instances where it cannot be lazy loaded so it forces an initialize.
     * 
     * @param lessonId
     *            identifies the lesson to get
     * @return the lesson
     */
    @Override
    public Lesson getLesson(Long lessonId) {
	Lesson lesson = (Lesson) getSession().get(Lesson.class, lessonId);
	return lesson;
    }

    @Override
    public Lesson getLessonWithJoinFetchedProgress(Long lessonId) {

	return (Lesson) getSession().createCriteria(Lesson.class).add(Restrictions.like("lessonId", lessonId))
		.setFetchMode("learnerProgresses", FetchMode.JOIN).uniqueResult();
    }

    /**
     * Gets all lessons that are active for a learner.
     * 
     * @param learner
     *            a User that identifies the learner.
     * @return a List with all active lessons in it.
     */
    @Override
    public List getActiveLessonsForLearner(User learner) {

	Query query = getSession().getNamedQuery("activeLessonsAllOrganisations");
	query.setInteger("userId", learner.getUserId().intValue());
	List result = query.list();
	return result;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getActiveLearnerByLesson(long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<User> getActiveLearnerByLesson(long lessonId) {
	return getSession().createQuery(LessonDAO.LOAD_ACTIVE_LEARNERS).setLong("lessonId", lessonId).list();
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getActiveLearnerByLesson(long) Note: Hibernate 3.1
     *      query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
     */
    @Override
    public Integer getCountActiveLearnerByLesson(long lessonId) {
	Query query = getSession().createQuery(LessonDAO.COUNT_ACTIVE_LEARNERS);
	query.setLong("lessonId", lessonId);
	Object value = query.uniqueResult();
	return new Integer(((Number) value).intValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getLearnersByLesson(Long lessonId, String searchPhrase, Integer limit, Integer offset,
	    boolean orderAscending) {
	StringBuilder queryTextBuilder = new StringBuilder("SELECT users ").append(LessonDAO.LOAD_LEARNERS_BY_LESSON);
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryTextBuilder.append(" AND (users.firstName LIKE '%").append(token)
			.append("%' OR users.lastName LIKE '%").append(token).append("%' OR users.login LIKE '%")
			.append(token).append("%')");
	    }
	}
	String order = orderAscending ? "ASC" : "DESC";
	queryTextBuilder.append(" ORDER BY users.firstName ").append(order).append(", users.lastName ").append(order)
		.append(", users.login ").append(order);
	Query query = getSession().createQuery(queryTextBuilder.toString()).setLong("lessonId", lessonId);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	return query.list();
    }

    @Override
    public Integer getCountLearnersByLesson(long lessonId, String searchPhrase) {
	StringBuilder queryTextBuilder = new StringBuilder("SELECT COUNT(*) ")
		.append(LessonDAO.LOAD_LEARNERS_BY_LESSON);
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryTextBuilder.append(" AND (users.firstName LIKE '%").append(token)
			.append("%' OR users.lastName LIKE '%").append(token).append("%' OR users.login LIKE '%")
			.append(token).append("%')");
	    }
	}

	Query query = getSession().createQuery(queryTextBuilder.toString()).setLong("lessonId", lessonId);
	Object value = query.uniqueResult();
	return ((Number) value).intValue();
    }

    /**
     * Saves or Updates a Lesson.
     * 
     * @param lesson
     */
    @Override
    public void saveLesson(Lesson lesson) {
	getSession().save(lesson);
    }

    /**
     * Deletes a Lesson <b>permanently</b>.
     * 
     * @param lesson
     */
    @Override
    public void deleteLesson(Lesson lesson) {
	getSession().delete(lesson);
    }

    /**
     * Update the lesson data
     * 
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#updateLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    @Override
    public void updateLesson(Lesson lesson) {
	getSession().update(lesson);
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
	List lessons = this.doFind(LessonDAO.FIND_LESSON_BY_CREATOR, userID);
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
    public List getPreviewLessonsBeforeDate(Date startDate) {
	List lessons = this.doFind(LessonDAO.FIND_PREVIEW_BEFORE_START_DATE, startDate);
	return lessons;
    }

    /**
     * Get the lesson that applies to this activity. Not all activities have an attached lesson.
     */
    @Override
    public Lesson getLessonForActivity(long activityId) {
	Query query = getSession().createQuery(LessonDAO.FIND_LESSON_FOR_ACTIVITY);
	query.setLong("activityId", activityId);
	return (Lesson) query.uniqueResult();
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonsByOrgAndUserWithCompletedFlag(Integer, Integer,
     *      boolean)
     */
    @Override
    public List getLessonsByOrgAndUserWithCompletedFlag(Integer userId, Integer orgId, Integer userRole) {

	String queryName;
	if (Role.ROLE_MONITOR.equals(userRole)) {
	    queryName = "staffLessonsByOrgAndUserWithCompletedFlag";
	} else if (Role.ROLE_LEARNER.equals(userRole)) {
	    queryName = "learnerLessonsByOrgAndUserWithCompletedFlag";
	} else {
	    // in case of Role.ROLE_GROUP_MANAGER
	    queryName = "allLessonsByOrgAndUserWithCompletedFlag";
	}

	Query query = getSession().getNamedQuery(queryName);
	query.setInteger("userId", userId.intValue());
	query.setInteger("orgId", orgId.intValue());
	List result = query.list();
	return result;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonsByOrgAndUserWithCompletedFlag(Integer, Integer,
     *      boolean)
     */
    @Override
    public List getLessonsByGroupAndUser(Integer userId, Integer orgId) {
	Query query = getSession().getNamedQuery("lessonsByOrgAndUserWithChildOrgs");
	query.setInteger("userId", userId.intValue());
	query.setInteger("orgId", orgId.intValue());
	List result = query.list();
	return result;
    }

    @Override
    public List getLessonsByGroup(Integer orgId) {
	return this.doFind(LessonDAO.LESSONS_BY_GROUP, orgId);
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getLessonsByOriginalLearningDesign(Integer)
     */
    @Override
    public List getLessonsByOriginalLearningDesign(Long ldId, Integer orgId) {
	Object[] args = { ldId.longValue(), orgId.intValue() };
	List lessons = this.doFind(LessonDAO.LESSONS_WITH_ORIGINAL_LEARNING_DESIGN, args);
	return lessons;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getMonitorsByToolSessionId(Long)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return (List<User>) this.doFindByNamedQueryAndNamedParam("monitorsByToolSessionId", "sessionId", sessionId);
    }

    /**
     * Maps users from an organisation with the given role to a boolean value saying whether they participate in the
     * given lesson.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<User, Boolean> getUsersWithLessonParticipation(final Long lessonId, final String role,
	    String searchPhrase, final Integer limit, final Integer offset, boolean orderAscending) {
	String queryTextBase = LessonDAO.LOAD_USERS_WITH_LESSON_PARTICIPATION;
	// whether to exclude staff group or make it the only group that counts
	queryTextBase = queryTextBase.replace("<IS_STAFF>", role.equals(Role.MONITOR) ? "=" : "<>");
	StringBuilder queryTextBuilder = new StringBuilder(queryTextBase);
	// is it a result of a search?
	if (!StringUtils.isBlank(searchPhrase)) {
	    queryTextBuilder.append(" WHERE");
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryTextBuilder.append(" (users.first_name LIKE '%").append(token)
			.append("%' OR users.last_name LIKE '%").append(token).append("%' OR users.login LIKE '%")
			.append(token).append("%') AND");
	    }
	    queryTextBuilder.delete(queryTextBuilder.length() - 4, queryTextBuilder.length());
	}
	String order = orderAscending ? "ASC" : "DESC";
	queryTextBuilder.append(" ORDER BY users.first_name ").append(order).append(", users.last_name ").append(order)
		.append(", users.login ").append(order);

	Query query = getSession().createSQLQuery(queryTextBuilder.toString()).addEntity(User.class)
		.addScalar("participant").setLong("lessonId", lessonId)
		.setInteger("roleId", role.equals(Role.MONITOR) ? Role.ROLE_MONITOR : Role.ROLE_LEARNER);
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	if (offset != null) {
	    query.setFirstResult(offset);
	}

	List<Object[]> resultQuery = query.list();

	// this map keeps the insertion order
	Map<User, Boolean> result = new LinkedHashMap<User, Boolean>();
	// make the result easier to process
	for (Object[] entry : resultQuery) {
	    result.put((User) entry[0], ((Integer) entry[1]).equals(1));
	}
	return result;
    }
}