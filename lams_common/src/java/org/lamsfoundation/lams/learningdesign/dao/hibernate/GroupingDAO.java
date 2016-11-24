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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class GroupingDAO extends LAMSBaseDAO implements IGroupingDAO {

    private final static String GROUPINGS_FOR_LEARNING_DESIGN_VIA_CREATE = "select grouping from "
	    + Grouping.class.getName() + " grouping, " + GroupingActivity.class.getName() + " grouping_activity "
	    + " where grouping_activity.learningDesign.id = ? " + " and grouping_activity.createGrouping = grouping";
    private final static String GROUPINGS_FOR_LEARNING_DESIGN_VIA_GROUPING = "select grouping from "
	    + Grouping.class.getName() + " grouping, " + Activity.class.getName() + " activity "
	    + " where activity.learningDesign.id = ? " + " and activity.grouping = grouping";

    /**
     * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupingDAO#getGroupingById(java.lang.Long)
     */
    @Override
    public Grouping getGroupingById(Long groupingID) {
	Grouping grouping = (Grouping) super.find(Grouping.class, groupingID);
	return getNonCGLibGrouping(grouping);
    }

    /**
     * Returns the list of groupings applicable for the given learning design. This is a combination of the groupings
     * defined
     * via a GroupingActivity.createGrouping (which may or may not be applied to any other activities in the design) and
     * groupings
     * related to branches and which are not attached to a GroupingActivity.
     */
    @Override
    public List<Grouping> getGroupingsByLearningDesign(Long learningDesignId) {
	List groupingsA = this.doFind(GroupingDAO.GROUPINGS_FOR_LEARNING_DESIGN_VIA_CREATE, learningDesignId);
	List groupingsB = this.doFind(GroupingDAO.GROUPINGS_FOR_LEARNING_DESIGN_VIA_GROUPING, learningDesignId);
	HashMap<Long, Grouping> realGroupings = new HashMap<Long, Grouping>();
	Iterator iter = groupingsA.iterator();
	while (iter.hasNext()) {
	    Grouping element = (Grouping) iter.next();
	    realGroupings.put(element.getGroupingId(), getNonCGLibGrouping(element));
	}
	iter = groupingsB.iterator();
	while (iter.hasNext()) {
	    Grouping element = (Grouping) iter.next();
	    if (!realGroupings.containsKey(element.getGroupingId())) {
		realGroupings.put(element.getGroupingId(), getNonCGLibGrouping(element));
	    }
	}
	return new ArrayList<Grouping>(realGroupings.values());
    }

    /**
     * we must return the real grouping, not a Hibernate proxy. So relook
     * it up. This should be quick as it should be in the cache.
     */
    private Grouping getNonCGLibGrouping(Grouping grouping) {
	if (grouping != null) {
	    if (grouping.isRandomGrouping()) {
		return (Grouping) super.find(RandomGrouping.class, grouping.getGroupingId());
	    } else if (grouping.isChosenGrouping()) {
		return (Grouping) super.find(ChosenGrouping.class, grouping.getGroupingId());
	    } else if (grouping.isLearnerChoiceGrouping()) {
		return (Grouping) super.find(LearnerChoiceGrouping.class, grouping.getGroupingId());
	    }
	    throw new HibernateException(
		    "Unable to get grouping as the grouping type is unknown or missing. Grouping object is "
			    + grouping);
	}
	return null;
    }
}