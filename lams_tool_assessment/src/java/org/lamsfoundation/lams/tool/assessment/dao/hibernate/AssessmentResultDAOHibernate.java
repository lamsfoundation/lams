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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentResultDAO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentResultDAOHibernate extends LAMSBaseDAO implements AssessmentResultDAO {

    private static final String FIND_LAST_BY_ASSESSMENT_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId =:userId AND r.assessment.uid=:assessmentUid AND r.latest=1";
    
    private static final String FIND_WHETHER_LAST_RESULT_FINISHED = "SELECT COUNT(*) > 0 FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId =:userId AND r.assessment.uid=:assessmentUid AND r.latest=1 AND r.finishDate != null";

    private static final String FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = ? AND r.assessment.uid=? AND (r.finishDate != null) ORDER BY r.startDate ASC";

    private static final String FIND_LAST_FINISHED_BY_ASSESSMENT_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.assessment.uid=:assessmentUid AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_BY_SESSION_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = ? AND r.sessionId=?";

    private static final String FIND_BY_SESSION_AND_USER_AND_FINISHED = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = ? AND r.sessionId=? AND (r.finishDate != null) ORDER BY r.startDate ASC";

    private static final String FIND_LAST_FINISHED_BY_SESSION_AND_USER = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_LAST_FINISHED_RESULTS_BY_CONTENT_ID = "FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_ASSESSMENT_RESULT_COUNT_BY_ASSESSMENT_AND_USER = "select COUNT(*) FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId=? AND r.assessment.uid=? AND (r.finishDate != null)";
    
    private static final String IS_ASSESSMENT_RESULT_EXIST_BY_ASSESSMENT = "select COUNT(*) > 0 FROM "
	    + AssessmentResult.class.getName() + " AS r WHERE r.assessment.uid=:assessmentUid";

    private static final String LAST_ASSESSMENT_RESULT_GRADE = "select r.grade FROM " + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId=:userId AND r.assessment.uid=:assessmentUid AND (r.finishDate != null) AND r.latest=1";

    private static final String LAST_ASSESSMENT_RESULT_GRADES_BY_CONTENT_ID = "select r.user.userId, r.grade FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) AND r.latest=1";

    private static final String BEST_SCORE_BY_SESSION_AND_USER = "SELECT MAX(r.grade) FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null)";

    private static final String BEST_SCORES_BY_CONTENT_ID = "SELECT r.user.userId, MAX(r.grade) FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) GROUP BY r.user.userId";

    private static final String FIRST_SCORE_BY_SESSION_AND_USER = "SELECT r.grade FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null) ORDER BY r.startDate ASC";

    private static final String AVERAGE_SCORE_BY_SESSION_AND_USER = "SELECT AVG(r.grade) FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId = :userId AND r.sessionId=:sessionId AND (r.finishDate != null)";

    private static final String AVERAGE_SCORES_BY_CONTENT_ID = "SELECT r.user.userId, AVG(r.grade) FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.assessment.contentId=? AND (r.finishDate != null) GROUP BY r.user.userId";

    private static final String FIND_LAST_ASSESSMENT_RESULT_TIME_TAKEN = "select UNIX_TIMESTAMP(r.finishDate) - UNIX_TIMESTAMP(r.startDate) FROM "
	    + AssessmentResult.class.getName()
	    + " AS r WHERE r.user.userId=? AND r.assessment.uid=? AND (r.finishDate != null) AND r.latest=1";

    private static final String FIND_BY_UID = "FROM " + AssessmentResult.class.getName() + " AS r WHERE r.uid = ?";

    @Override
    @SuppressWarnings("unchecked")
    public List<AssessmentResult> getAssessmentResults(Long assessmentUid, Long userId) {
	return (List<AssessmentResult>) doFind(FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED,
		new Object[] { userId, assessmentUid });
    }

    @Override
    public List<AssessmentResult> getFinishedAssessmentResultsByUser(Long sessionId, Long userId) {
	return (List<AssessmentResult>) doFind(FIND_BY_SESSION_AND_USER_AND_FINISHED,
		new Object[] { userId, sessionId });
    }

    @Override
    public List<AssessmentResult> getAssessmentResultsBySession(Long sessionId, Long userId) {
	return (List<AssessmentResult>) doFind(FIND_BY_SESSION_AND_USER, new Object[] { userId, sessionId });
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
	return (AssessmentResult) q.uniqueResult();
    }

    @Override
    public Float getLastTotalScoreByUser(Long assessmentUid, Long userId) {
	Query<Float> q = getSession().createQuery(LAST_ASSESSMENT_RESULT_GRADE, Float.class);
	q.setParameter("userId", userId);
	q.setParameter("assessmentUid", assessmentUid);
	Float lastTotalScore = q.uniqueResult();
	
	return lastTotalScore == null ? 0 : lastTotalScore;
    }

    @Override
    public List<AssessmentUserDTO> getLastTotalScoresByContentId(Long toolContentId) {
	List<Object[]> list = (List<Object[]>) doFind(LAST_ASSESSMENT_RESULT_GRADES_BY_CONTENT_ID,
		new Object[] { toolContentId });
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
	List<Object[]> list = (List<Object[]>) doFind(BEST_SCORES_BY_CONTENT_ID, new Object[] { toolContentId });
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
	final String FIRST_SCORES_BY_CONTENT_ID = "SELECT user.user_id, res.grade "
		+ "FROM tl_laasse10_assessment_result AS res "
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
	List<Object[]> list = (List<Object[]>) doFind(AVERAGE_SCORES_BY_CONTENT_ID, new Object[] { toolContentId });
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
	return (List<AssessmentResult>) doFind(FIND_LAST_FINISHED_RESULTS_BY_CONTENT_ID, new Object[] { contentId });
    }
    
    @Override
    public List<Object[]> getLastFinishedAssessmentResultsBySession(Long sessionId) {
	final String FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID = "SELECT r, u.portraitUuid FROM " + AssessmentResult.class.getName()
		+ " AS r, " + User.class.getName() + " as u WHERE r.sessionId=? AND (r.finishDate != null) AND r.latest=1 AND u.userId=r.user.userId";
	
	return (List<Object[]>) doFind(FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID, new Object[] { sessionId });
    }
    
    @Override
    public List<Object[]> getLeadersLastFinishedAssessmentResults(Long contentId) {
	final String FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID = "SELECT r, u.portraitUuid FROM " + AssessmentResult.class.getName()
		+ " AS r, " + User.class.getName() + " as u WHERE r.user=r.user.session.groupLeader AND r.assessment.contentId=? AND (r.finishDate != null) AND r.latest=1 AND u.userId=r.user.userId";
	
	return (List<Object[]>) doFind(FIND_LAST_FINISHED_RESULTS_BY_SESSION_ID, new Object[] { contentId });
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
    public int countAttemptsPerOption(Long optionUid) {
	String COUNT_ATTEMPTS_BY_OPTION_UID = "SELECT count(*) "
		+ "FROM tl_laasse10_assessment_result AS result "
		+ "JOIN tl_laasse10_question_result AS questionResult ON result.uid = questionResult.result_uid "
		+ "JOIN tl_laasse10_option_answer AS optionAnswer ON questionResult.uid = optionAnswer.question_result_uid AND optionAnswer.answer_boolean=1 AND optionAnswer.question_option_uid = :optionUid "
		+ "WHERE (result.finish_date IS NOT NULL) AND result.latest=1";

	NativeQuery<?> query = getSession().createNativeQuery(COUNT_ATTEMPTS_BY_OPTION_UID);
	query.setParameter("optionUid", optionUid);
	List list = query.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private List<AssessmentUserDTO> convertResultsToAssessmentUserDTOList(List<Object[]> list) {
	List<AssessmentUserDTO> lastTotalScores = new ArrayList<AssessmentUserDTO>();
	
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
