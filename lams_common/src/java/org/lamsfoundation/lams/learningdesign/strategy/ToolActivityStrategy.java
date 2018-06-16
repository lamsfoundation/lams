/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

/**
 * Activity strategy that deals with the calculation specific to tool activity. The major part of this strategy will be
 * overiding the methods that defined in the abstract level.
 *
 * @author Jacky Fang
 * @author Minhas
 * @version 1.1
 */
public class ToolActivityStrategy extends SimpleActivityStrategy {

    protected ToolActivity toolActivity = null;

    public ToolActivityStrategy(ToolActivity toolActivity) {
	this.toolActivity = toolActivity;
    }

    @Override
    protected void setUpContributionType(ArrayList<Integer> contributionTypes) {
	contributionTypes.add(ContributionTypes.CONTRIBUTION);
    }

    @Override
    protected Activity getActivity() {
	return toolActivity;
    }

}
