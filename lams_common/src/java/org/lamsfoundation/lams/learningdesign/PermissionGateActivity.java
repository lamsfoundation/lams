/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.PermissionGateActivityStrategy;


/**
 * @author Manpreet Minhas
 * @hibernate.class
 */
public class PermissionGateActivity extends GateActivity implements Serializable
{
    
    /** full constructor */
    public PermissionGateActivity(Long activityId,
            Integer id,
            String description,
            String title,
            Integer xcoord,
            Integer ycoord,
            Integer orderId,
            Boolean defineLater,
            java.util.Date createDateTime,
            String offlineInstructions,
            LearningLibrary learningLibrary,
            Activity parentActivity,
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign,
            Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId,
            Set waitingLearners)
    {
        super(activityId,
                id,
                description,
                title,
                xcoord,
                ycoord,
                orderId,
                defineLater,
                createDateTime,
                offlineInstructions,
                learningLibrary,
                parentActivity,
				libraryActivity,
				parentUIID,
                learningDesign,
                grouping,
                activityTypeId,
                transitionTo,
				transitionFrom,
                gateActivityLevelId,
                waitingLearners);
        super.simpleActivityStrategy = new PermissionGateActivityStrategy();
    }
    
    /** default constructor */
    public PermissionGateActivity()
    {
    	super.simpleActivityStrategy = new PermissionGateActivityStrategy();
    }
    
    /** minimal constructor */
    public PermissionGateActivity(Long activityId,
            Boolean defineLater,
            java.util.Date createDateTime,
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
            org.lamsfoundation.lams.learningdesign.Activity parentActivity,
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
            org.lamsfoundation.lams.learningdesign.Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId,
            Set waitingLearners)
    {
        super(activityId,
                defineLater,
                createDateTime,
                learningLibrary,
                parentActivity,
                learningDesign,
                grouping,
                activityTypeId,
                transitionTo,
				transitionFrom,
                gateActivityLevelId,
                waitingLearners);
        super.simpleActivityStrategy = new PermissionGateActivityStrategy();
    }
    /**
     * Makes a copy of the PermissionGateActivity for authoring, preview and monitoring enviornment 
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return PermissionGateActivity Returns a deep-copy of the originalActivity
     */
    public static PermissionGateActivity createCopy(PermissionGateActivity originalActivity){
    	PermissionGateActivity newPermissionGateActivity = new PermissionGateActivity();
    	
    	newPermissionGateActivity.setGateOpen(new Boolean(false));
    	newPermissionGateActivity.setGateActivityLevelId(originalActivity.getGateActivityLevelId());
    	
    	newPermissionGateActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newPermissionGateActivity.setDescription(originalActivity.getDescription());
    	newPermissionGateActivity.setTitle(originalActivity.getTitle());
    	newPermissionGateActivity.setHelpText(originalActivity.getHelpText());
    	newPermissionGateActivity.setXcoord(originalActivity.getXcoord());
    	newPermissionGateActivity.setYcoord(originalActivity.getYcoord());
    	newPermissionGateActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	
    	newPermissionGateActivity.setGroupingSupportType(originalActivity.getGroupingSupportType());
    	newPermissionGateActivity.setApplyGrouping(originalActivity.getApplyGrouping());
    	newPermissionGateActivity.setActivityCategoryID(originalActivity.getActivityCategoryID());
    	
    	newPermissionGateActivity.setGrouping(originalActivity.getGrouping());
    	newPermissionGateActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	newPermissionGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());    	
    	newPermissionGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newPermissionGateActivity.setCreateDateTime(new Date());
    	newPermissionGateActivity.setRunOffline(originalActivity.getRunOffline());
    	
    	newPermissionGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newPermissionGateActivity.setOnlineInstructions(originalActivity.getOnlineInstructions());
    	
    	newPermissionGateActivity.setLibraryActivity(originalActivity.getLibraryActivity());
    	newPermissionGateActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());    	
    	return newPermissionGateActivity;
    	
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull()
    {
        return false;
    }
    
}
