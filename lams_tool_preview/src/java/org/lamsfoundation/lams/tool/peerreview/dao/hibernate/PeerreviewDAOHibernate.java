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

package org.lamsfoundation.lams.tool.peerreview.dao.hibernate;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewDAO;
import org.lamsfoundation.lams.tool.peerreview.dto.PeerreviewStatisticsDTO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class PeerreviewDAOHibernate extends LAMSBaseDAO implements PeerreviewDAO {
    private static final String GET_RESOURCE_BY_CONTENTID = "from " + Peerreview.class.getName()
	    + " as r where r.contentId=?";

    // ANY_VALUE is needed for Mysql setting ONLY_FULL_GROUP_BY - the sessionName will always be the same
    // as it is from the same table as sessionId.  (LDEV-4222)
    private static final String GET_STATS = "SELECT s.session_id as \"sessionId\", ANY_VALUE(s.session_name) as \"sessionName\", "
	    + " count(u.uid) as \"numLearnersInSession\", sum(u.session_finished) as \"numLearnersComplete\" "
	    + " FROM tl_laprev11_session s "
	    + " JOIN tl_laprev11_peerreview p ON p.content_id = :toolContentId AND s.peerreview_uid = p.uid "
	    + " LEFT JOIN tl_laprev11_user u ON u.session_uid = s.uid " + " GROUP BY session_id";

    @SuppressWarnings("rawtypes")
    @Override
    public Peerreview getByContentId(Long contentId) {
	List list = doFindCacheable(GET_RESOURCE_BY_CONTENTID, contentId);
	if (list.size() > 0) {
	    return (Peerreview) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Peerreview getByUid(Long peerreviewUid) {
	return (Peerreview) getObject(Peerreview.class, peerreviewUid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PeerreviewStatisticsDTO> getStatistics(Long toolContentId) {
	return getSession().createSQLQuery(GET_STATS).setParameter("toolContentId", toolContentId)
		.setResultTransformer(Transformers.aliasToBean(PeerreviewStatisticsDTO.class)).list();

    }
}
