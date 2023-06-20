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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentResultDAO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AssessmentResultDAOHibernate extends LAMSBaseDAO implements AssessmentResultDAO {

    private static final String FIND_LAST_BY_ASSESSMENT =
	    "FROM " + AssessmentResult.class.getName() + " AS r WHERE r.assessment.uid=:assessmentUid AND r.latest=1";

    private static final String FIND_LAST_BY_ASSESSMENT_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId =:userId AND r.assessment.uid=:assessmentUid AND r.latest=1";

    private static final String FIND_WHETHER_LAST_RESULT_FINISHED =
	    "SELECT COUNT(*) > 0 FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.user.userId =:userId AND r.assessment.uid=:assessmentUid AND r.latest=1 AND r.finishDate != null";

    private static final String FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = ? AND r.assessment.uid=? AND (r.finishDate != null) ORDER BY r.startDate ASC";

    private static final String FIND_LAST_FINISHED_BY_ASSESSMENT_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.assessment.uid=:assessmentUid AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_BY_SESSION_AND_USER =
	    "FROM " + AssessmentResult.class.getName() + " AS r WHERE r.user.userId = ? AND r.sessionId=?";

    private static final String FIND_BY_SESSION_AND_USER_AND_FINISHED = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = ? AND r.sessionId=? AND (r.finishDate != null) ORDER BY r.startDate ASC";

    private static final String FIND_LAST_FINISHED_BY_SESSION_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_LAST_FINISHED_RESULTS_BY_CONTENT_ID = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.assessment.contentId = :contentId AND (r.finishDate != null) AND r.latest = 1";

    private static final String FIND_ASSESSMENT_RESULT_COUNT_BY_ASSESSMENT_AND_USER =
	    "select COUNT(*) FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.user.userId=? AND r.assessment.uid=? AND (r.finishDate != null)";

    private static final String IS_ASSESSMENT_RESULT_EXIST_BY_ASSESSMENT =
	    "select COUNT(*) > 0 FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.assessment.uid=:assessmentUid";

    private static final String LAST_ASSESSMENT_RESULT_GRADE = "select r.grade FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId=:userId AND r.assessment.uid=:assessmentUid AND (r.finishDate != null) AND r.latest=1";

    private static final String LAST_ASSESSMENT_RESULT_GRADES_BY_CONTENT_ID =
	    "select r.user.userId, r.grade FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) AND r.latest=1";

    private static final String BEST_SCORE_BY_SESSION_AND_USER =
	    "SELECT MAX(r.grade) FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null)";

    private static final String BEST_SCORES_BY_CONTENT_ID =
	    "SELECT r.user.userId, MAX(r.grade) FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) GROUP BY r.user.userId";

    private static final String FIRST_SCORE_BY_SESSION_AND_USER =
	    "SELECT r.grade FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null) ORDER BY r.startDate ASC";

    private static final String AVERAGE_SCORE_BY_SESSION_AND_USER =
	    "SELECT AVG(r.grade) FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null)";

    private static final String AVERAGE_SCORES_BY_CONTENT_ID =
	    "SELECT r.user.userId, AVG(r.grade) FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) GROUP BY r.user.userId";

    private static final String FIND_LAST_ASSESSMENT_RESULT_TIME_TAKEN =
	    "select UNIX_TIMESTAMP(r.finishDate) - UNIX_TIMESTAMP(r.startDate) FROM " + AssessmentResult.class.getName()
		    + " AS r WHERE r.user.userId=? AND r.assessment.uid=? AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_BY_UID = "FROM " + AssessmentResult.class.getName() + " AS r WHERE r.uid = ?";

    private static final String ANSWERED_QUESTIONS_BY_USER =
	    "SELECT user_id, portrait_uuid, user_name, group_name, SUM(IF((type = 1 AND answer_boolean = 1) OR"
		    + "			(type = 2 AND answer_int <> -1) OR"
		    + "			((type BETWEEN 3 AND 6) AND (answer IS NOT NULL AND TRIM(answer) <> '')) OR"
		    + "			(type = 7 AND mark > 0) OR          "
		    + "			(type = 8 AND answer_int > 0)"
		    + "			,1, 0)) AS answered_question_count FROM"
		    + "		(SELECT u.user_id, BIN_TO_UUID(u.portrait_uuid) AS portrait_uuid, CONCAT(u.first_name, ' ', u.last_name) AS user_name,"
		    + "		 	s.session_name AS group_name, qbq.type, qr.mark, qbta.answer, oa.answer_boolean, oa.answer_int"
		    + "         FROM      tl_laasse10_assessment        AS a"
		    + "		JOIN      tl_laasse10_assessment_result AS ar   ON a.uid =  ar.assessment_uid"
		    + "         JOIN      tl_laasse10_user              AS au   ON ar.user_uid = au.uid"
		    + "         JOIN      lams_user              	AS u	USING (user_id)"
		    + "         JOIN      tl_laasse10_session           AS s    USING (session_id)"
		    + "		JOIN      tl_laasse10_question_result   AS qr   ON ar.uid = qr.result_uid"
		    + "		LEFT JOIN lams_qb_tool_answer           AS qbta ON qbta.answer_uid = qr.uid"
		    + "		LEFT JOIN lams_qb_tool_question         AS qbtq USING (tool_question_uid)"
		    + "		LEFT JOIN lams_qb_question              AS qbq  ON qbq.uid = qbtq.qb_question_uid"
		    + "		LEFT JOIN tl_laasse10_option_answer     AS oa"
		    + "			ON oa.question_result_uid = qr.uid"
		    + "			AND  ((qbq.type = 7 AND qr.mark > 0) OR (qbta.answer IS NOT NULL AND TRIM(qbta.answer) <> '')"
		    + "                        OR oa.answer_boolean IS NULL OR oa.answer_boolean = 1 OR (qbq.type = 2 AND answer_int <> -1))"
		    + "		WHERE ar.latest = 1"
		    + "         AND   (a.use_select_leader_tool_ouput = 0 OR s.group_leader_uid = ar.user_uid)"
		    + "		AND   a.content_id = :toolContentId"
		    + "		GROUP BY qr.uid, u.user_id) AS answered_questions GROUP BY user_id ORDER BY user_name";

    private static final String FINISHED_LEARNERS_FOR_COMPLETION_CHART =
	    "SELECT u.user_id, BIN_TO_UUID(u.portrait_uuid) AS portrait_uuid, CONCAT(u.first_name, ' ', u.last_name) AS user_name, "
		    + "  s.session_name AS group_name"
		    + "         FROM      tl_laasse10_assessment        AS a"
		    + "		JOIN      tl_laasse10_assessment_result AS ar   ON a.uid =  ar.assessment_uid"
		    + "         JOIN      tl_laasse10_user              AS au   ON ar.user_uid = au.uid"
		    + "         JOIN      lams_user              	AS u	USING (user_id)"
		    + "         JOIN      tl_laasse10_session           AS s    USING (session_id)"
		    + "	WHERE ar.latest = 1 AND ar.finish_date IS NOT NULL"
		    + "		AND   a.content_id = :toolContentId"
		    + "	ORDER BY user_name";

    @Override
    @SuppressWarnings("unchecked")
    public List<AssessmentResult> getAssessmentResults(Long assessmentUid, Long userId) {
	return doFind(FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED, new Object[] { userId, assessmentUid });
    }

    @Override
    public List<AssessmentResult> getAssessmentResultsByQbToolQuestionAndAnswer(Long toolQuestionUid, String answer) {
	if (StringUtils.isBlank(answer)) {
	    return List.of();
	}

	QbToolQuestion qbToolQuestion = find(QbToolQuestion.class, toolQuestionUid);
	ToolActivity toolActivity = findByProperty(ToolActivity.class, "toolContentId",
		qbToolQuestion.getToolContentId()).get(0);
	Organisation organisation = toolActivity.getLearningDesign().getLessons().iterator().next().getOrganisation();
	Organisation parentOrganisation = organisation.getParentOrganisation();
	if (parentOrganisation != null && parentOrganisation.getOrganisationType().getOrganisationTypeId()
		.equals(OrganisationType.ROOT_TYPE)) {
	    parentOrganisation = null;
	}

	String FIND_BY_QBQUESTION = "SELECT r FROM  " + AssessmentQuestionResult.class.getName() + " AS qr, "
		+ AssessmentResult.class.getName() + " AS r, " + ToolActivity.class.getName()
		+ " AS a JOIN a.learningDesign.lessons AS l "
		+ "WHERE qr.assessmentResult.uid = r.uid AND a.toolContentId = r.assessment.contentId "
		+ "AND (l.organisation.organisationId = :organisationId OR "
		+ "     l.organisation.parentOrganisation.organisationId = :organisationId" + (
		parentOrganisation == null
			? ""
			: " OR l.organisation.organisationId = :parentOrganisationId OR "
				+ "l.organisation.parentOrganisation.organisationId = :parentOrganisationId")
		+ ") AND qr.qbToolQuestion.qbQuestion.uid = :qbQuestionUid AND " + (qbToolQuestion.getQbQuestion()
		.isExactMatch()
		? "TRIM(qr.answer)"
		: "REGEXP_REPLACE(qr.answer, '" + QbUtils.VSA_ANSWER_NORMALISE_SQL_REG_EXP + "', '')")
		+ " = :answer ORDER BY r.startDate ASC";

	Query<AssessmentResult> q = getSession().createQuery(FIND_BY_QBQUESTION, AssessmentResult.class);
	q.setParameter("qbQuestionUid", qbToolQuestion.getQbQuestion().getUid());
	q.setParameter("organisationId", organisation.getOrganisationId());
	if (parentOrganisation != null) {
	    q.setParameter("parentOrganisationId", parentOrganisation.getOrganisationId());
	}
	String normalisedAnswer = QbUtils.normaliseVSAnswer(answer, qbToolQuestion.getQbQuestion().isExactMatch());
	q.setParameter("answer", normalisedAnswer);
	return q.list();
    }

    @Override
    public List<AssessmentResult> getFinishedAssessmentResultsByUser(Long sessionId, Long userId) {
	return doFind(FIND_BY_SESSION_AND_USER_AND_FINISHED, new Object[] { userId, sessionId });
    }

    @Override
    public List<AssessmentResult> getAssessmentResultsBySession(Long sessionId, Long userId) {
	return doFind(FIND_BY_SESSION_AND_USER, new Object[] { userId, sessionId });
    }

    @Override
    public List<AssessmentResult> getLastAssessmentResults(Long assessmentUid) {
	Query<AssessmentResult> q = getSession().createQuery(FIND_LAST_BY_ASSESSMENT, AssessmentResult.class);
	q.setParameter("assessmentUid", assessmentUid);
	return q.getResultList();
    }

    @Override
    public AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId) {
	Query<AssessmentResult> q = getSession().createQuery(FIND_LAST_BY_ASSESSMENT_AND_USER, AssessmentResult.class);
	q.setParameter("userId", userId);
	q.setParameter("assessmentUid", assessmentUid);
	return q.uniqueResult();
    }

    @Override
    public Boolean isLastAttemptFinishedByUser(AssessmentUser user) {
	Assessment assessment = user.getAssessment() == null ? user.getSession().getAssessment() : user.getAssessment();
	Query<Boolean> q = getSession().createQuery(FIND_WHETHER_LAST_RESULT_FINISHED, Boolean.class);
	q.setParameter("userId", user.getUserId());
	q.setParameter("assessmentUid", assessment.getUid());
	return q.uniqueResult();
    }

    @Override
    public AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId) {
	Query<AssessmentResult> q = getSession().createQuery(FIND_LAST_FINISHED_BY_ASSESSMENT_AND_USER,
		AssessmentResult.class);
	q.setParameter("userId", userId);
	q.setParameter("assessmentUid", assessmentUid);
	return q.uniqueResult();
    }

    @Override
    public Float getLastTotalScoreByUser(Long assessmentUid, Long userId) {
	Query<Float> q = getSession().createQuery(LAST_ASSESSMENT_RESULT_GRADE, Float.class);
	q.setParameter("userId", userId);
	q.setParameter("assessmentUid", assessmentUid);
	return q.uniqueResult();
    }

    @Override
    public List<AssessmentUserDTO> getLastTotalScoresByContentId(Long toolContentId) {
	List<Object[]> list = doFind(LAST_ASSESSMENT_RESULT_GRADES_BY_CONTENT_ID, new Object[] { toolContentId });
	return convertResultsToAssessmentUserDTOList(list);
    }

    @Override
    public Float getBestTotalScoreByUser(Long sessionId, Long userId) {
	Query<Float> q = getSession().createQuery(BEST_SCORE_BY_SESSION_AND_USER, Float.class);
	q.setParameter("userId", userId);
	q.setParameter("sessionId", sessionId);
	return q.uniqueResult();
    }

    @Override
    public List<AssessmentUserDTO> getBestTotalScoresByContentId(Long toolContentId) {
	List<Object[]> list = doFind(BEST_SCORES_BY_CONTENT_ID, new Object[] { toolContentId });
	return convertResultsToAssessmentUserDTOList(list);
    }

    @Override
    public Float getFirstTotalScoreByUser(Long sessionId, Long userId) {
	Query<Float> q = getSession().createQuery(FIRST_SCORE_BY_SESSION_AND_USER, Float.class);
	q.setParameter("userId", userId);
	q.setParameter("sessionId", sessionId);
	q.setMaxResults(1);
	return q.uniqueResult();
    }

    @Override
    public List<AssessmentUserDTO> getFirstTotalScoresByContentId(Long toolContentId) {
	final String FIRST_SCORES_BY_CONTENT_ID =
		"SELECT user.user_id, res.grade " + "FROM tl_laasse10_assessment_result AS res "
			+ "JOIN tl_laasse10_user AS user ON res.user_uid = user.uid "
			+ "JOIN tl_laasse10_assessment AS assess ON res.assessment_uid = assess.uid AND assess.content_id = :contentId "
			+ "INNER JOIN (SELECT user_uid, MIN(start_date) AS startDate FROM tl_laasse10_assessment_result WHERE finish_date IS NOT NULL GROUP BY user_uid) firstRes "
			+ "ON (res.user_uid = firstRes.user_uid AND res.start_date = firstRes.startDate) GROUP BY res.user_uid";

	NativeQuery<?> query = getSession().createNativeQuery(FIRST_SCORES_BY_CONTENT_ID);
	query.setParameter("contentId", toolContentId);
	@SuppressWarnings("unchecked")
	List<Object[]> list = (List<Object[]>) query.list();
	return convertResultsToAssessmentUserDTOList(list);
    }

    @Override
    public Float getAvergeTotalScoreByUser(Long sessionId, Long userId) {
	Query<Double> q = getSession().createQuery(AVERAGE_SCORE_BY_SESSION_AND_USER, Double.class);
	q.setParameter("userId", userId);
	q.setParameter("sessionId", sessionId);
	Double result = q.uniqueResult();
	return result == null ? null : result.floatValue();
    }

    @Override
    public List<AssessmentUserDTO> getAverageTotalScoresByContentId(Long toolContentId) {
	List<Object[]> list = doFind(AVERAGE_SCORES_BY_CONTENT_ID, new Object[] { toolContentId });
	return convertResultsToAssessmentUserDTOList(list);
    }

    @Override
    public Integer getLastFinishedAssessmentResultTimeTaken(Long assessmentUid, Long userId) {

	List list = doFind(FIND_LAST_ASSESSMENT_RESULT_TIME_TAKEN, new Object[] { userId, assessmentUid });
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @Override
    public AssessmentResult getLastFinishedAssessmentResultByUser(Long sessionId, Long userId) {
	Query<AssessmentResult> q = getSession().createQuery(FIND_LAST_FINISHED_BY_SESSION_AND_USER,
		AssessmentResult.class);
	q.setParameter("userId", userId);
	q.setParameter("sessionId", sessionId);
	return q.uniqueResult();
    }

    @Override
    public List<AssessmentResult> getLastFinishedAssessmentResults(Long contentId) {
	return getSession().createQuery(FIND_LAST_FINISHED_RESULTS_BY_CONTENT_ID, AssessmentResult.class)
		.setParameter("contentId", contentId).getResultList();
    }

    @Override
    public List<Object[]> getLastFinishedAssessmentResultsBySession(Long sessionId) {
	final String FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID =
		"SELECT r, u.portraitUuid FROM " + AssessmentResult.class.getName() + " AS r, " + User.class.getName()
			+ " as u WHERE r.sessionId=? AND (r.finishDate != null) AND r.latest=1 AND u.userId=r.user.userId";

	return doFind(FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID, new Object[] { sessionId });
    }

    @Override
    public List<Object[]> getLeadersLastFinishedAssessmentResults(Long contentId) {
	final String FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID =
		"SELECT r, u.portraitUuid FROM " + AssessmentResult.class.getName() + " AS r, " + User.class.getName()
			+ " as u WHERE r.user=r.user.session.groupLeader AND r.assessment.contentId=? AND (r.finishDate != null) AND r.latest=1 AND u.userId=r.user.userId";

	return doFind(FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID, new Object[] { contentId });
    }

    @Override
    public int getAssessmentResultCount(Long assessmentUid, Long userId) {
	List list = doFind(FIND_ASSESSMENT_RESULT_COUNT_BY_ASSESSMENT_AND_USER, new Object[] { userId, assessmentUid });
	if ((list == null) || (list.size() == 0)) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @Override
    public List<Object[]> getLearnersWithFinishedCurrentAttemptForCompletionChart(Long contentId) {
	List<Object[]> results = getSession().createNativeQuery(FINISHED_LEARNERS_FOR_COMPLETION_CHART)
		.setParameter("toolContentId", contentId).getResultList();
	return results;
    }

    @Override
    public boolean isAssessmentAttempted(Long assessmentUid) {
	Query<Boolean> q = getSession().createQuery(IS_ASSESSMENT_RESULT_EXIST_BY_ASSESSMENT, Boolean.class);
	q.setParameter("assessmentUid", assessmentUid);
	return q.uniqueResult();
    }

    @Override
    public AssessmentResult getAssessmentResultByUid(Long assessmentResultUid) {
	List list = doFind(FIND_BY_UID, new Object[] { assessmentResultUid });
	if ((list == null) || (list.size() == 0)) {
	    return null;
	}
	return (AssessmentResult) list.get(0);
    }

    @Override
    public int countAttemptsPerOption(Long toolContentId, Long optionUid, boolean finishedAttemptsOnly) {
	String COUNT_ATTEMPTS_BY_OPTION_UID = "SELECT count(*) " + "FROM tl_laasse10_assessment_result AS result "
		+ "JOIN tl_laasse10_assessment AS assessment ON assessment.uid = result.assessment_uid "
		+ "JOIN tl_laasse10_question_result AS questionResult ON questionResult.result_uid = result.uid "
		+ "JOIN lams_qb_tool_answer AS qbToolAnswer ON qbToolAnswer.answer_uid = questionResult.uid "
		+ "JOIN tl_laasse10_option_answer AS optionAnswer ON questionResult.uid = optionAnswer.question_result_uid "
		+ "WHERE " + (finishedAttemptsOnly ? "(result.finish_date IS NOT NULL) AND " : "")
		+ "result.latest=1 && assessment.content_id = :toolContentId AND optionAnswer.question_option_uid = :optionUid "
		+ "AND (optionAnswer.answer_boolean=1 OR qbToolAnswer.qb_option_uid = :optionUid)  ";

	NativeQuery<Number> query = getSession().createNativeQuery(COUNT_ATTEMPTS_BY_OPTION_UID);
	query.setParameter("toolContentId", toolContentId);
	query.setParameter("optionUid", optionUid);
	return query.uniqueResult().intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Integer, List<String[]>> getAnsweredQuestionsByUsersForCompletionChart(long toolContentId) {
	List<Object[]> results = getSession().createNativeQuery(ANSWERED_QUESTIONS_BY_USER)
		.setParameter("toolContentId", toolContentId).getResultList();
	return results.stream().collect(Collectors.groupingBy(r -> ((Number) r[4]).intValue(), Collectors.mapping(
		r -> new String[] { r[0].toString(), r[1] == null ? null : r[1].toString(),
			r[2] == null ? "" : r[2].toString(), r[3] == null ? null : r[3].toString() },
		Collectors.toList())));
    }

    private List<AssessmentUserDTO> convertResultsToAssessmentUserDTOList(List<Object[]> list) {
	List<AssessmentUserDTO> lastTotalScores = new ArrayList<>();

	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		float grade;
		try {
		    grade = ((Double) element[1]).floatValue();
		} catch (ClassCastException e) {
		    grade = (Float) element[1];
		}

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(userId);
		userDto.setGrade(grade);
		lastTotalScores.add(userDto);
	    }
	}

	return lastTotalScores;
    }
}