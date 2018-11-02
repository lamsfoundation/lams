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

package org.lamsfoundation.lams.learningdesign;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Grouping formed by staff members choice
 *
 * @author chris
 */
@Entity
@DiscriminatorValue("2")
public class ChosenGrouping extends Grouping {
    private static final long serialVersionUID = 3237424022482923008L;

    /** Creates a new instance of ChosenGrouping */
    public ChosenGrouping() {
	super.grouper = new ChosenGrouper();
    }

    /** full constructor */
    public ChosenGrouping(Long groupingId, Set<Group> groups, Set<Activity> activities) {
	super(groupingId, groups, activities, new ChosenGrouper());
    }

    /**
     * This method creates a deep copy of the Grouping
     *
     * @return ChosenGrouping The deep copied Grouping object
     */
    @Override
    public Grouping createCopy(int uiidOffset) {
	ChosenGrouping chosenGrouping = new ChosenGrouping();
	copyGroupingFields(chosenGrouping, uiidOffset);
	return chosenGrouping;
    }

    /**
     * This type of grouping doesn't have groups other than learner groups.
     * So it always return <code>true</code>.
     *
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     */
    @Override
    public boolean isLearnerGroup(Group group) {
	return true;
    }
}