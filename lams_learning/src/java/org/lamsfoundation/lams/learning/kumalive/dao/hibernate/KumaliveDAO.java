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

package org.lamsfoundation.lams.learning.kumalive.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learning.kumalive.dao.IKumaliveDAO;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveScore;
import org.springframework.stereotype.Repository;

@Repository
public class KumaliveDAO extends LAMSBaseDAO implements IKumaliveDAO {
    private static final String FIND_KUMALIVE_BY_ORGANISATION = "FROM " + Kumalive.class.getName()
	    + " AS k WHERE k.organisation.organisationId = ? AND k.finished = 0";
    private static final String FIND_KUMALIVES_BY_ORGANISATION = "FROM " + Kumalive.class.getName()
	    + " AS k WHERE k.organisation.organisationId = ? ORDER BY ";
    private static final String FIND_RUBRICS_BY_ORGANISATION = "FROM " + KumaliveRubric.class.getName()
	    + " AS r WHERE r.organisation.organisationId = ? AND r.kumalive IS NULL ORDER BY r.orderId ASC";
    private static final String FIND_SCORE_BY_KUMALIVE = "FROM " + KumaliveScore.class.getName()
	    + " AS s WHERE s.rubric.kumalive.kumaliveId = ? ORDER BY s.user.firstName ";
    private static final String FIND_SCORE_BY_KUMALIVE_AND_USER = "FROM " + KumaliveScore.class.getName()
	    + " AS s WHERE s.rubric.kumalive.kumaliveId = ? AND s.user.userId = ?";

    @Override
    @SuppressWarnings("unchecked")
    public Kumalive findKumalive(Integer organisationId) {
	List<Kumalive> result = (List<Kumalive>) doFind(FIND_KUMALIVE_BY_ORGANISATION, organisationId);
	return result.isEmpty() ? null : result.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KumaliveRubric> findRubrics(Integer organisationId) {
	return (List<KumaliveRubric>) doFind(FIND_RUBRICS_BY_ORGANISATION, organisationId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Kumalive> findKumalives(Integer organisationId, String sortColumn, boolean isAscending) {
	StringBuilder query = new StringBuilder(FIND_KUMALIVES_BY_ORGANISATION);
	switch (sortColumn) {
	    case "name":
		query.append("name");
		break;
	    default:
		query.append("kumaliveId");
		break;
	}
	if (!isAscending) {
	    query.append(" DESC");
	}

	return (List<Kumalive>) doFind(query.toString(), organisationId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KumaliveScore> findKumaliveScore(Long kumaliveId, boolean isAscending) {
	StringBuilder query = new StringBuilder(FIND_SCORE_BY_KUMALIVE);
	if (!isAscending) {
	    query.append(" DESC");
	}

	return (List<KumaliveScore>) doFind(query.toString(), kumaliveId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KumaliveScore> findKumaliveScore(Long kumaliveId, Integer userId) {
	return (List<KumaliveScore>) doFind(FIND_SCORE_BY_KUMALIVE_AND_USER, kumaliveId, userId);
    }
}