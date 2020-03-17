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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.springframework.stereotype.Repository;

@Repository
public class OutcomeDAO extends LAMSBaseDAO implements IOutcomeDAO {

    private static final String FIND_CONTENT_FOLDER_ID_BY_ORGANISATION = "SELECT * FROM (SELECT content_folder_id FROM lams_outcome WHERE organisation_id ? "
	    + "UNION SELECT content_folder_id FROM lams_outcome_scale WHERE organisation_id ?) AS a WHERE content_folder_id IS NOT NULL LIMIT 1";

    private static final String FIND_OUTCOMES_SORTED_BY_NAME = "FROM Outcome o WHERE ? ORDER BY o.name, o.code";

    private static final String FIND_AUTHOR_ORGANISATIONS = "SELECT uor.userOrganisation.organisation.organisationId FROM UserOrganisationRole uor "
	    + "WHERE uor.userOrganisation.user.userId = ? AND uor.role.roleId = " + Role.ROLE_AUTHOR;

    private static final String FIND_SCALES_SORTED_BY_NAME = "FROM OutcomeScale o WHERE o.organisation IS NULL ? ORDER BY o.name, o.code";

    /**
     * Finds an existing content folder ID for the given organisation outcomes or scales, or for global ones
     */
    @SuppressWarnings("unchecked")
    @Override
    public String getContentFolderID(Integer organisationId) {
	String queryString = FIND_CONTENT_FOLDER_ID_BY_ORGANISATION.replace("?",
		organisationId == null ? "IS NULL" : "=" + organisationId);
	Query<String> query = getSession().createSQLQuery(queryString);
	return query.uniqueResult();
    }

    /**
     * Finds all global outcomes and ones for the given organisation
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Outcome> getOutcomesSortedByName(Integer organisationId) {
	String queryString = FIND_OUTCOMES_SORTED_BY_NAME.replace("?", organisationId == null ? "o.organisation IS NULL"
		: " o.organisation IS NULL OR o.organisation.organisationId = " + organisationId + ")");
	return find(queryString);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Outcome> getOutcomesSortedByName(String search, Set<Integer> organisationIds) {
	String queryString = FIND_OUTCOMES_SORTED_BY_NAME;
	if (StringUtils.isNotBlank(search)) {
	    queryString = queryString.replace("?", "(o.name LIKE :search OR o.code LIKE :search) AND ?");
	}
	queryString = queryString.replace("?", "(o.organisation IS NULL ?)");
	if (organisationIds != null && !organisationIds.isEmpty()) {
	    queryString = queryString.replace("?", "OR o.organisation.organisationId IN (:organisationIds)");
	}
	queryString = queryString.replace("?", "");

	Query<Outcome> query = getSession().createQuery(queryString);
	if (organisationIds != null && !organisationIds.isEmpty()) {
	    query.setParameterList("organisationIds", organisationIds);
	}
	if (StringUtils.isNotBlank(search)) {
	    query.setParameter("search", "%" + search + "%");
	}

	return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OutcomeMapping> getOutcomeMappings(Long lessonId, Long toolContentId, Long itemId) {
	Map<String, Object> properties = new HashMap<>();
	if (lessonId != null) {
	    properties.put("lessonId", lessonId);
	}
	if (toolContentId != null) {
	    properties.put("toolContentId", toolContentId);
	}
	if (itemId != null) {
	    properties.put("itemId", itemId);
	}
	return findByProperties(OutcomeMapping.class, properties);
    }

    /**
     * Finds all global scales and ones for the given organisation
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<OutcomeScale> getScalesSortedByName(Integer organisationId) {
	String queryString = FIND_SCALES_SORTED_BY_NAME.replace("?",
		organisationId == null ? "" : "OR o.organisation.organisationId = " + organisationId);
	return find(queryString);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> getAuthorOrganisations(Integer userId) {
	return find(FIND_AUTHOR_ORGANISATIONS, new Object[] { userId });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OutcomeResult> getOutcomeResults(Integer userId, Long lessonId, Long toolContentId, Long itemId) {
	Map<String, Object> properties = new HashMap<>();
	if (lessonId != null) {
	    properties.put("mapping.outcome.lessonId", lessonId);
	}
	if (toolContentId != null) {
	    properties.put("mapping.outcome.toolContentId", toolContentId);
	}
	if (itemId != null) {
	    properties.put("mapping.outcome.itemId", itemId);
	}
	if (userId != null) {
	    properties.put("user.userId", userId);
	}
	return findByProperties(OutcomeResult.class, properties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public OutcomeResult getOutcomeResult(Integer userId, Long mappingId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("user.userId", userId);
	properties.put("mapping.mappingId", mappingId);
	List<OutcomeResult> result = findByProperties(OutcomeResult.class, properties);
	return result.isEmpty() ? null : result.get(0);
    }
}
