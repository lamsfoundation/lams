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

package org.lamsfoundation.lams.usermanagement.dao;

import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;

/**
 * @author jliew
 */
public interface IOrganisationDAO extends IBaseDAO {

    /**
     * Get list of orgIds of active groups.
     *
     * @param userId
     * @param isAppadmin
     * @param page
     * @param size
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return list of orgIds
     */
    List<OrganisationDTO> getActiveCoursesByUser(Integer userId, boolean isAppadmin, int page, int size,
	    String searchString);

    int getCountActiveCoursesByUser(Integer userId, boolean isAppadmin, String searchString);

    /**
     * Returns courses with specified type, state and parent course.
     *
     * @param parentOrgId
     * @param typeId
     * @param stateId
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return
     */
    List<Organisation> getPagedCourses(final Integer parentOrgId, final Integer typeId, final Integer stateId, int page,
	    int size, String sortBy, String sortOrder, String searchString);

    /**
     * Counts courses with specified type, state and parent course.
     *
     * @param parentOrgId
     * @param typeId
     * @param stateId
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return
     */
    int getCountCoursesByParentCourseAndTypeAndState(final Integer parentOrgId, final Integer typeId,
	    final Integer stateId, String searchString);

    /**
     * Returns list of UserOrganisationCollapsed, indicating which sub-courses was collapsed by the given user.
     *
     * @param parentOrganisationId
     * @param userId
     * @return
     */
    List<UserOrganisationCollapsed> getChildOrganisationsCollapsedByUser(Integer parentOrganisationId, Integer userId);

    List<Organisation> getOrganisationsByCodes(Collection<String> codes);
}
