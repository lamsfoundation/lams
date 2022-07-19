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

package org.lamsfoundation.lams.monitoring.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy;
import org.lamsfoundation.lams.learningdesign.strategy.IContributionTypeStrategy;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO.ContributeEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.util.WebUtil;

public class ContributeDTOFactory {

    /**
     * Get the Contribute DTO for this activity. As a SimpleActivity it only returns a contribute DTO if there is a
     * contribution entry.
     *
     * @throws LamsToolServiceException
     */
    public static ContributeActivityDTO getContributeActivityDTO(Long lessonID, SimpleActivity activity,
	    ILamsCoreToolService toolService) throws LamsToolServiceException {
	ContributeActivityDTO dto = null;
	SimpleActivityStrategy strategy = activity.getSimpleActivityStrategy();
	if (strategy != null) {
	    dto = ContributeDTOFactory.addContributionURLS(lessonID, activity, strategy, toolService);
	}
	return dto;
    }

    private static ContributeActivityDTO addContributionURLS(Long lessonID, Activity activity,
	    IContributionTypeStrategy strategy, ILamsCoreToolService toolService) {
	ContributeActivityDTO dto = null;
	LinkedList<Integer> contributionType = new LinkedList<>();
	Collections.addAll(contributionType, strategy.getContributionType());

	// check for activities being edited by Monitor
	if (toolService.isContentEdited(activity)) {
	    contributionType.add(ContributionTypes.CONTENT_EDITED);
	}

	if (!contributionType.isEmpty()) {
	    dto = new ContributeActivityDTO(activity);
	    for (Integer contributionTypeEntry : contributionType) {
		String url = ContributeDTOFactory.getURL(lessonID, activity, contributionTypeEntry, toolService);
		if (ContributionTypes.CONTRIBUTION.equals(contributionTypeEntry) && url == null) {
		    continue;
		}
		ContributeEntry entry = dto.addContribution(contributionTypeEntry, url);

		// once a gate was opened, it does not require attention
		if ((ContributionTypes.PERMISSION_GATE.equals(contributionTypeEntry)
			|| ContributionTypes.PASSWORD_GATE.equals(contributionTypeEntry))
			&& Boolean.TRUE.equals(((GateActivity) activity).getGateOpen())) {
		    entry.setIsComplete(true);

		} else if (ContributionTypes.CHOSEN_GROUPING.equals(contributionTypeEntry)) {
		    // check if there are at least 2 groups with added learners
		    GroupingActivity groupingActivity = (GroupingActivity) activity;
		    Grouping grouping = groupingActivity.getCreateGrouping();
		    if ((grouping != null) && (grouping.getGroups() != null)) {
			int groupsWithLearnersAdded = 0;
			for (Group group : grouping.getGroups()) {
			    if (!group.getUsers().isEmpty()) {
				groupsWithLearnersAdded++;
				if (groupsWithLearnersAdded >= 2) {
				    entry.setIsComplete(true);
				    break;
				}
			    }
			}
		    }
		}
	    }
	}

	return dto;
    }

    private static String getURL(Long lessonID, Activity activity, Integer contributionTypeEntry,
	    ILamsCoreToolService toolService) throws LamsToolServiceException {

	String url = null;
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    if (contributionTypeEntry.equals(ContributionTypes.CONTENT_EDITED)) {
		url = toolService.getToolAuthorURL(lessonID, toolActivity, ToolAccessMode.TEACHER);
	    }
	}

	if (url == null) {
	    /*
	     * PERMISSION_GATE || SYNC_GATE || SCHEDULE_GATE || CHOSEN_GROUPING || CONTRIBUTION || CHOSEN_BRANCHING ||
	     * Unknown contribution type || (!ToolActivity && MODERATION)
	     */
	    url = toolService.getToolContributionURL(lessonID, activity);
	}
	return url != null ? WebUtil.convertToFullURL(url) : null;
    }

    /**
     * Get the Contribute DTO for this activity. As a complex activity it always returns a DTO - as it may be needed to
     * enclose a child activity.
     */
    public static ContributeActivityDTO getContributeActivityDTO(Long lessonID, ComplexActivity activity,
	    ILamsCoreToolService toolService, Vector<ContributeActivityDTO> childActivities) {
	ContributeActivityDTO dto = null;
	ComplexActivityStrategy strategy = activity.getComplexActivityStrategy();
	if (strategy != null) {
	    dto = ContributeDTOFactory.addContributionURLS(lessonID, activity, strategy, toolService);
	    if ((childActivities != null) && (childActivities.size() > 0)) {
		if (dto == null) {
		    dto = new ContributeActivityDTO(activity);
		}
		dto.setChildActivities(childActivities);
	    }
	}
	return dto;
    }
}