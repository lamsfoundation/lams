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

import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieSessionComparator;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchieSessionDAOHibernate extends LAMSBaseDAO implements ScratchieSessionDAO {

    private static final String FIND_BY_SESSION_ID = "from " + ScratchieSession.class.getName()
	    + " as p where p.sessionId=?";
    private static final String FIND_BY_CONTENT_ID = "from " + ScratchieSession.class.getName()
	    + " as p where p.scratchie.contentId=? order by p.sessionName asc";

    private static final String LOAD_MARKS = "SELECT mark FROM tl_lascrt11_session session "
	    + " JOIN tl_lascrt11_scratchie scratchie ON session.scratchie_uid = scratchie.uid "
	    + " WHERE scratchie.content_id = :toolContentId";

    @SuppressWarnings("rawtypes")
    @Override
    public ScratchieSession getSessionBySessionId(Long sessionId) {
	List list = doFindCacheable(FIND_BY_SESSION_ID, sessionId);
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieSession) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ScratchieSession> getByContentId(Long toolContentId) {
	List<ScratchieSession> sessions = doFindCacheable(FIND_BY_CONTENT_ID, toolContentId);

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
    public List<Integer> getRawLeaderMarksByToolContentId(Long toolContentId) {
	NativeQuery<Integer> query = getSession().createNativeQuery(LOAD_MARKS);
	query.setParameter("toolContentId", toolContentId);
	return query.list();
    }

    @Override
    public List<Long> getSessionIdsByQbToolQuestion(Long toolQuestionUid, String answer) {
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

	final String FIND_BY_QBQUESTION_AND_FINISHED = "SELECT DISTINCT session.sessionId FROM "
		+ ScratchieItem.class.getName() + " AS item, " + ScratchieSession.class.getName() + " AS session, "
		+ ScratchieAnswerVisitLog.class.getName() + " AS visitLog, " + ToolActivity.class.getName()
		+ " AS a JOIN a.learningDesign.lessons AS l "
		+ "WHERE session.scratchie.uid = item.scratchieUid AND a.toolContentId = session.scratchie.contentId "
		+ "AND (l.organisation.organisationId = :organisationId OR "
		+ "     l.organisation.parentOrganisation.organisationId = :organisationId"
		+ (parentOrganisation == null ? ""
			: " OR l.organisation.organisationId = :parentOrganisationId OR "
				+ "l.organisation.parentOrganisation.organisationId = :parentOrganisationId")
		+ ") AND item.qbQuestion.uid =:qbQuestionUid "
		+ "AND session.sessionId = visitLog.sessionId AND REGEXP_REPLACE(visitLog.answer, '"
		+ IScratchieService.VSA_ANSWER_NORMALISE_SQL_REG_EXP + "', '') = :answer";

	Query<Long> q = getSession().createQuery(FIND_BY_QBQUESTION_AND_FINISHED, Long.class);
	q.setParameter("qbQuestionUid", qbToolQuestion.getQbQuestion().getUid());
	q.setParameter("organisationId", organisation.getOrganisationId());
	if (parentOrganisation != null) {
	    q.setParameter("parentOrganisationId", parentOrganisation.getOrganisationId());
	}
	String normalisedAnswer = answer.replaceAll(IScratchieService.VSA_ANSWER_NORMALISE_JAVA_REG_EXP, "");
	q.setParameter("answer", normalisedAnswer);
	return q.list();
    }
}