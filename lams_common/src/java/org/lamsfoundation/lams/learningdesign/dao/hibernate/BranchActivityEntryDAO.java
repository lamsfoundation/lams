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

package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.dao.IBranchActivityEntryDAO;
import org.springframework.stereotype.Repository;

@Repository
public class BranchActivityEntryDAO extends LAMSBaseDAO implements IBranchActivityEntryDAO {

    private final static String ENTRIES_FOR_LEARNING_DESIGN = "select entry from " + BranchActivityEntry.class.getName()
	    + " entry, " + Activity.class.getName() + " branchingActivity "
	    + " where branchingActivity.learningDesign.id = ? " + " and entry.branchingActivity = branchingActivity";

    private final static String CONDITION_BY_ID = "FROM " + BranchCondition.class.getName()
	    + " con WHERE con.conditionId = ?";

    /*
     * (non-Javadoc)
     *
     * @see
     * org.lamsfoundation.lams.learningdesign.dao.hibernate.IBranchActivityEntryDAO#getEntriesByLearningDesign(java.lang
     * .Long)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<BranchActivityEntry> getEntriesByLearningDesign(Long learningDesignId) {
	return this.doFindCacheable(BranchActivityEntryDAO.ENTRIES_FOR_LEARNING_DESIGN, learningDesignId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BranchCondition getConditionByID(Long conditionID) {
	List<BranchCondition> result = this.doFindCacheable(BranchActivityEntryDAO.CONDITION_BY_ID, conditionID);
	if (result == null || result.isEmpty()) {
	    return null;
	}
	return result.get(0);
    }
}