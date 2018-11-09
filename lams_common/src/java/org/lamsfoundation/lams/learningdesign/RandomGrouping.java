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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * <p>
 * Grouping formed by random assignment. The random grouping can done in two
 * ways:
 * </p>
 * <li>1. Randomly group learners by the number of groups set by author.</li>
 * <li>2. Randomly group learners by the number of learners in a group.</li>
 *
 * <p>
 * <b>Note:</b> Only one way can be chosen by the author to carry out the
 * random grouping.
 * </p>
 *
 * @author chris
 * @author Jacky Fang
 */
@Entity
@DiscriminatorValue("1")
public class RandomGrouping extends Grouping {
    private static final long serialVersionUID = 364252053930917480L;

    @Column(name = "number_of_groups")
    private Integer numberOfGroups;

    @Column(name = "learners_per_group")
    private Integer learnersPerGroup;

    public Integer getNumberOfGroups() {
	return this.numberOfGroups;
    }

    public void setNumberOfGroups(Integer numberOfGroups) {
	this.numberOfGroups = numberOfGroups;
    }

    public Integer getLearnersPerGroup() {
	return this.learnersPerGroup;
    }

    public void setLearnersPerGroup(Integer learnersPerGroup) {
	this.learnersPerGroup = learnersPerGroup;
    }

    /** Creates a new instance of RandomGrouping */
    public RandomGrouping() {
	super.grouper = new RandomGrouper();
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

    /**
     * This method creates a deep copy of the Grouping
     *
     * @return RandomGrouping The deep copied Grouping object
     */
    @Override
    public Grouping createCopy(int uiidOffset) {
	RandomGrouping randomGrouping = new RandomGrouping();
	copyGroupingFields(randomGrouping, uiidOffset);

	randomGrouping.setNumberOfGroups(this.getNumberOfGroups());
	randomGrouping.setLearnersPerGroup(this.getLearnersPerGroup());

	return randomGrouping;
    }
}