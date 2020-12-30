/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieSessionComparator;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchieSessionDAOHibernate extends LAMSBaseDAO implements ScratchieSessionDAO {

    private static final String FIND_BY_SESSION_ID = "from " + ScratchieSession.class.getName()
	    + " as p where p.sessionId=?";
    private static final String FIND_BY_CONTENT_ID = "from " + ScratchieSession.class.getName()
	    + " as p where p.scratchie.contentId=? order by p.sessionName asc";

    private static final String LOAD_MARKS = "SELECT mark FROM tl_lascrt11_session session "
	    + " JOIN tl_lascrt11_scratchie scratchie ON session.scratchie_uid = scratchie.uid "
	    + " WHERE session.scratching_finished = 1 AND scratchie.content_id = :toolContentId";
    private static final String FIND_MARK_STATS = "SELECT MIN(mark) min_grade, AVG(mark) avg_grade, MAX(mark) max_grade, COUNT(mark) num_complete "
	    + " FROM tl_lascrt11_session session "
	    + " JOIN tl_lascrt11_scratchie scratchie ON session.scratchie_uid = scratchie.uid "
	    + " WHERE session.scratching_finished = 1 AND scratchie.content_id = :toolContentId";

    @SuppressWarnings("rawtypes")
    @Override
    public ScratchieSession getSessionBySessionId(Long sessionId) {
	List list = doFind(FIND_BY_SESSION_ID, sessionId);
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieSession) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ScratchieSession> getByContentId(Long toolContentId) {
	List<ScratchieSession> sessions = doFind(FIND_BY_CONTENT_ID, toolContentId);

	Set<ScratchieSession> sortedSessions = new TreeSet<>(new ScratchieSessionComparator());
	sortedSessions.addAll(sessions);

	return new ArrayList<>(sortedSessions);
    }

    @Override
    public void delete(ScratchieSession session) {
	getSession().delete(session);
    }

    @Override
    public void deleteBySessionId(Long toolSessionId) {
	this.removeObject(ScratchieSession.class, toolSessionId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Number> getRawLeaderMarksByToolContentId(Long toolContentId) {
	NativeQuery<?> query = getSession().createNativeQuery(LOAD_MARKS);
	query.setParameter("toolContentId", toolContentId);
	return (List<Number>) query.list();
    }

    @Override
    public Object[] getStatsMarksForLeaders(Long toolContentId) {
	NativeQuery<?> query = getSession().createNativeQuery(FIND_MARK_STATS)
		.addScalar("min_grade", FloatType.INSTANCE).addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE).addScalar("num_complete", IntegerType.INSTANCE);
	query.setParameter("toolContentId", toolContentId);
	@SuppressWarnings("unchecked")
	List<Object[]> list = (List<Object[]>) query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public List<Long> getSessionIdsByQbQuestion(Long qbQuestionUid, String answer) {
	final String FIND_BY_QBQUESTION_AND_FINISHED = "SELECT DISTINCT session.sessionId FROM "
		+ ScratchieItem.class.getName() + " AS item, " + ScratchieSession.class.getName() + " AS session, "
		+ ScratchieAnswerVisitLog.class.getName()
		+ " AS visitLog WHERE session.scratchie.uid = item.scratchieUid AND item.qbQuestion.uid =:qbQuestionUid"
		+ " AND session.sessionId = visitLog.sessionId AND REGEXP_REPLACE(visitLog.answer, '"
		+ IScratchieService.VSA_ANSWER_NORMALISE_SQL_REG_EXP + "', '') = :answer";

	Query<Long> q = getSession().createQuery(FIND_BY_QBQUESTION_AND_FINISHED, Long.class);
	q.setParameter("qbQuestionUid", qbQuestionUid);
	String normalisedAnswer = answer.replaceAll(IScratchieService.VSA_ANSWER_NORMALISE_JAVA_REG_EXP, "");
	q.setParameter("answer", normalisedAnswer);
	return q.list();
    }
}