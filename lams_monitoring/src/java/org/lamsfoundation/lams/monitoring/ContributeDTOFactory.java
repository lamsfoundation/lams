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

/* $Id$ */
package org.lamsfoundation.lams.monitoring;

import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy;
import org.lamsfoundation.lams.learningdesign.strategy.IContributionTypeStrategy;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
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
	    dto = addContributionURLS(lessonID, activity, strategy, toolService);
	}
	return dto;
    }

    private static ContributeActivityDTO addContributionURLS(Long lessonID, Activity activity,
	    IContributionTypeStrategy strategy, ILamsCoreToolService toolService) {
	ContributeActivityDTO dto = null;
	Integer[] contributionType = strategy.getContributionType();
	if (contributionType.length > 0) {
	    dto = new ContributeActivityDTO(activity);
	    for (int i = 0; i < contributionType.length; i++) {
		Integer contributionTypeEntry = contributionType[i];
		String url = getURL(lessonID, activity, contributionTypeEntry, toolService);
		dto.addContribution(contributionTypeEntry, url);
	    }
	}
	return dto;
    }

    private static String getURL(Long lessonID, Activity activity, Integer contributionTypeEntry,
	    ILamsCoreToolService toolService) throws LamsToolServiceException {

	String url = null;
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    if (contributionTypeEntry.equals(ContributionTypes.MODERATION)) {
		url = toolService.getToolModerateURL(toolActivity);
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
	    dto = addContributionURLS(lessonID, activity, strategy, toolService);
	    if (childActivities != null && childActivities.size() > 0) {
		if (dto == null) {
		    dto = new ContributeActivityDTO(activity);
		}
		dto.setChildActivities(childActivities);
	    }
	}
	return dto;

    }

}
