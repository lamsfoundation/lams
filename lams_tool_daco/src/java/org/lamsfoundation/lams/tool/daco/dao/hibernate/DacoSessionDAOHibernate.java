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

package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoSessionDAO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSessionStatsDTO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.springframework.stereotype.Repository;

@Repository
public class DacoSessionDAOHibernate extends LAMSBaseDAO implements DacoSessionDAO {

    private static final String FIND_BY_SESSION_ID = "from " + DacoSession.class.getName()
	    + " as p where p.sessionId=?";
    private static final String FIND_BY_CONTENT_ID = "from " + DacoSession.class.getName()
	    + " as p where p.daco.contentId=? ORDER BY p.sessionId";

    private static final String CALC_SESSION_STATS = "SELECT sessionId, sessionName, COUNT(user_uid) numberLearners, SUM(record_count) totalRecordCount FROM "
	    + " (SELECT user.uid user_uid, sess.uid sessionId, sess.session_name sessionName, COUNT(DISTINCT(record_id)) record_count "
	    + " FROM tl_ladaco10_users user "
	    + " JOIN tl_ladaco10_sessions sess ON sess.uid = user.session_uid AND sess.content_uid = :contentUid "
	    + " LEFT JOIN tl_ladaco10_answers ans ON ans.user_uid = user.uid " + " GROUP by user.uid) user_counts "
	    + " GROUP BY sessionId";

    @Override
    public DacoSession getSessionBySessionId(Long sessionId) {
	List<?> list = doFindCacheable(DacoSessionDAOHibernate.FIND_BY_SESSION_ID, sessionId);
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (DacoSession) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DacoSession> getByContentId(Long toolContentId) {
	return doFindCacheable(DacoSessionDAOHibernate.FIND_BY_CONTENT_ID, toolContentId);
    }

    @Override
    public void deleteBySessionId(Long toolSessionId) {
	this.removeObject(DacoSession.class, toolSessionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MonitoringSummarySessionDTO> statistics(Long toolContentUid) {
	NativeQuery<MonitoringSummarySessionDTO> query = getSession()
		.createNativeQuery(DacoSessionDAOHibernate.CALC_SESSION_STATS);
	query.addScalar("sessionId", LongType.INSTANCE).addScalar("sessionName", StringType.INSTANCE)
		.addScalar("numberLearners", IntegerType.INSTANCE).addScalar("totalRecordCount", IntegerType.INSTANCE)
		.setParameter("contentUid", toolContentUid)
		.setResultTransformer(Transformers.aliasToBean(MonitoringSessionStatsDTO.class));
	return query.list();

    }

}
