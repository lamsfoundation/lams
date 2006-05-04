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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.monitoring;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ContributeDTOFactory {

	private static Logger log = Logger.getLogger(LearningDesignProcessor.class);

	/** Get the Contribute DTO for this activity. As a SimpleActivity
	 * it only returns a contribute DTO if there is a contribution entry.
	 */ 
	public static ContributeActivityDTO getContributeActivityDTO(SimpleActivity activity)
	{
		ContributeActivityDTO dto = null;
		SimpleActivityStrategy strategy = activity.getSimpleActivityStrategy();
		if ( strategy != null ) {
			Integer[] contributionType = activity.getSimpleActivityStrategy().getContributionType();
			if ( contributionType.length > 0 ) {
				dto = new ContributeActivityDTO(activity);
				for(int i=0;i<contributionType.length;i++){
					Integer contributionTypeEntry = contributionType[i];
					String url = getURL(activity, contributionTypeEntry);
					dto.addContribution(contributionTypeEntry,url);
				}
			}
		}
		return dto;
	}
    public static final Integer MODERATION = new Integer(1);
    public static final Integer DEFINE_LATER = new Integer(2);
    public static final Integer CONTRIBUTION = new Integer(7);

	private static String getURL(SimpleActivity activity, Integer contributionTypeEntry) {
		// TODO implement rest of urls.
		String url = null;
		if ( contributionTypeEntry.equals(ContributionTypes.MODERATION) ) { 
			url = getActivityModerateURL(activity);
		} else if ( contributionTypeEntry.equals(ContributionTypes.DEFINE_LATER) ) { 
			url = getActivityDefineLaterURL(activity);
		} else if ( contributionTypeEntry.equals(ContributionTypes.PERMISSION_GATE) ) {
		} else if ( contributionTypeEntry.equals(ContributionTypes.SYNC_GATE) ) {
		} else if ( contributionTypeEntry.equals(ContributionTypes.SCHEDULE_GATE) ) {
		} else if ( contributionTypeEntry.equals(ContributionTypes.CHOSEN_GROUPING) ) {
    	} else if ( contributionTypeEntry.equals(ContributionTypes.CONTRIBUTION) ) { 
			url = getActivityContributionURL(activity);
		} else {
			log.warn("Unexpected contribution type for tool activity. Contribution type: "+contributionTypeEntry+" activity: "+activity);
		}
		return url != null ? WebUtil.convertToFullURL(url) : null;
	}

	/** Get the Contribute DTO for this activity. As a complex activity it always returns 
	 * a DTO - as it may be needed to enclose a child activity.
	 */ 
	public static ContributeActivityDTO getContributeActivityDTO(ComplexActivity activity)
	{
		return new ContributeActivityDTO(activity);
		
	}

	private static String addToolContentID(ToolActivity toolActivity, String url) {
		String retUrl = url;
		if ( retUrl != null ) {
			Long toolContentId = toolActivity.getToolContentId();
			if ( toolContentId != null ) {
				retUrl = WebUtil.appendParameterToURL(url,
						AttributeNames.PARAM_TOOL_CONTENT_ID,
						toolActivity.getToolContentId().toString());
			} else {
				log.error("Tool activity missing tool content id. Contribution URL is goign to be wrong. Tool activity: "+toolActivity);
			}
		}
		return retUrl;
	}

	/** Get the url for a contribution call. Only useful for Tool Activities - everything else returns null */
	public static String getActivityContributionURL(Activity activity) {
		if(activity.isToolActivity()){
			ToolActivity toolActivity = (ToolActivity)activity;
			return addToolContentID( toolActivity, toolActivity.getTool().getContributeUrl() );
		} else  {
			return null;
		}
	}

	/** Get the url for a define later call. Only useful for Tool Activities - everything else returns null */
	public static String getActivityDefineLaterURL(Activity activity) {
		if(activity.isToolActivity()){
			ToolActivity toolActivity = (ToolActivity)activity;
			return addToolContentID( toolActivity, toolActivity.getTool().getDefineLaterUrl());
		} else  {
			return null;
		}
	}

	/** Get the url for a moderate call. Only useful for Tool Activities - everything else returns null */
	public static String getActivityModerateURL(Activity activity) {
		if(activity.isToolActivity()){
			ToolActivity toolActivity = (ToolActivity)activity;
			return addToolContentID( toolActivity, toolActivity.getTool().getModerationUrl());
		} else  {
			return null;
		}
	}
}
