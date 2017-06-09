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

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
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
	List<ScratchieSession> sessions = (List<ScratchieSession>) doFind(FIND_BY_CONTENT_ID, toolContentId);

	Set<ScratchieSession> sortedSessions = new TreeSet<ScratchieSession>(new ScratchieSessionComparator());
	sortedSessions.addAll(sessions);

	return new ArrayList<ScratchieSession>(sortedSessions);
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
	SQLQuery query = getSession().createSQLQuery(LOAD_MARKS);
	query.setLong("toolContentId", toolContentId);
	List<Number> list = query.list();
	return list;
    }
    
    @Override
    public Object[] getStatsMarksForLeaders(Long toolContentId) {
	Query query = getSession().createSQLQuery(FIND_MARK_STATS)
		.addScalar("min_grade", FloatType.INSTANCE)
		.addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE)
		.addScalar("num_complete", IntegerType.INSTANCE);
	query.setLong("toolContentId", toolContentId);
	@SuppressWarnings("rawtypes")
	List list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }



}
