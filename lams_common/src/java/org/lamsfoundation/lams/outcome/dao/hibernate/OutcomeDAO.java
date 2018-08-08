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

package org.lamsfoundation.lams.outcome.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.springframework.stereotype.Repository;

@Repository
public class OutcomeDAO extends LAMSBaseDAO implements IOutcomeDAO {

    private static final String FIND_CONTENT_FOLDER_ID_BY_ORGANISATION = "SELECT * FROM (SELECT content_folder_id FROM lams_outcome WHERE organisation_id ? "
	    + "UNION SELECT content_folder_id FROM lams_outcome_scale WHERE organisation_id ?) AS a WHERE content_folder_id IS NOT NULL LIMIT 1";

    private static final String FIND_OUTCOMES_SORTED_BY_NAME = "FROM Outcome o WHERE o.organisation IS NULL ? ORDER BY o.name, o.code";

    /**
     * Finds an existing content folder ID for the given organisation outcomes or scales, or for global ones
     */
    public String getContentFolderID(Integer organisationId) {
	String queryString = FIND_CONTENT_FOLDER_ID_BY_ORGANISATION.replaceAll("\\?",
		organisationId == null ? "IS NULL" : "=" + organisationId);
	Query query = getSession().createSQLQuery(queryString);
	return (String) query.uniqueResult();
    }

    /**
     * Finds all global outcomes and ones for the given organisation
     */
    @SuppressWarnings("unchecked")
    public List<Outcome> getOutcomesSortedByName(Integer organisationId) {
	String queryString = FIND_OUTCOMES_SORTED_BY_NAME.replaceAll("\\?",
		organisationId == null ? "" : "OR o.organisation.organisationId = " + organisationId);
	return find(queryString);
    }
}
