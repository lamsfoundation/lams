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

import org.apache.commons.lang.StringUtils;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.springframework.stereotype.Repository;

@Repository
public class OutcomeDAO extends LAMSBaseDAO implements IOutcomeDAO {

    private static final String FIND_OUTCOMES_SORTED_BY_NAME = "FROM Outcome o ? ORDER BY o.name, o.code";

    private static final String FIND_SCALES_SORTED_BY_NAME = "FROM OutcomeScale o ORDER BY o.name, o.code";

    /**
     * Finds all global outcomes and ones for the given organisation
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Outcome> getOutcomesSortedByName() {
	String queryString = FIND_OUTCOMES_SORTED_BY_NAME.replace("?", "");
	return find(queryString);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Outcome> getOutcomesSortedByName(String search) {
	String queryString = FIND_OUTCOMES_SORTED_BY_NAME;
	if (StringUtils.isNotBlank(search)) {
	    queryString = queryString.replace("?", "WHERE o.name LIKE :search OR o.code LIKE :search");
	}
	queryString = queryString.replace("?", "");

	Query<Outcome> query = getSession().createQuery(queryString);
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
    public List<OutcomeScale> getScalesSortedByName() {
	return find(FIND_SCALES_SORTED_BY_NAME);
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