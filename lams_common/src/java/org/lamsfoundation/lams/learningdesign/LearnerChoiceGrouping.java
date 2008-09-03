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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.util.Set;

/**
 * Grouping formed by learners' choice
 * @author chris
 */
public class LearnerChoiceGrouping extends Grouping {

	/** Creates a new instance of ChosenGrouping */
	public LearnerChoiceGrouping() {
		super.grouper = new LearnerChoiceGrouper();
	}

	/** full constructor */
	public LearnerChoiceGrouping(Long groupingId, Set groups, Set activities) {
		super(groupingId, groups, activities, new LearnerChoiceGrouper());
	}

	/**
	 * This method creates a deep copy of the Grouping 
	 * @return ChosenGrouping The deep copied Grouping object
	 */
	@Override
	public Grouping createCopy(int uiidOffset) {
		LearnerChoiceGrouping learnerChoiceGrouping = new LearnerChoiceGrouping();
		copyGroupingFields(learnerChoiceGrouping, uiidOffset);
		return learnerChoiceGrouping;
	}

	/**
	 * This type of grouping doesn't have groups other than learner groups.
	 * So it always return <code>true</code>.
	 * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
	 */
	@Override
	public boolean isLearnerGroup(Group group) {
		return true;
	}

}
