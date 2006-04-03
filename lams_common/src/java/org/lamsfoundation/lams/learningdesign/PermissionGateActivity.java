/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
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
            LearningLibrary learningLibrary,
            Activity parentActivity,
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign,
            Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            String languageFile,
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
                learningLibrary,
                parentActivity,
				libraryActivity,
				parentUIID,
                learningDesign,
                grouping,
                activityTypeId,
                transitionTo,
				transitionFrom,
				languageFile,
                gateActivityLevelId,
                waitingLearners);
        super.simpleActivityStrategy = new PermissionGateActivityStrategy(this);
    }
    
    /** default constructor */
    public PermissionGateActivity()
    {
    	super.simpleActivityStrategy = new PermissionGateActivityStrategy(this);
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
        super.simpleActivityStrategy = new PermissionGateActivityStrategy(this);
    }
    /**
     * Makes a copy of the PermissionGateActivity for authoring, preview and monitoring enviornment 
     * @return PermissionGateActivity Returns a deep-copy of the originalActivity
     */
    public Activity createCopy(){
    	PermissionGateActivity newPermissionGateActivity = new PermissionGateActivity();
    	
    	newPermissionGateActivity.setGateOpen(new Boolean(false));
    	newPermissionGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
    	
    	newPermissionGateActivity.setActivityUIID(this.getActivityUIID());
    	newPermissionGateActivity.setDescription(this.getDescription());
    	newPermissionGateActivity.setTitle(this.getTitle());
    	newPermissionGateActivity.setHelpText(this.getHelpText());
    	newPermissionGateActivity.setXcoord(this.getXcoord());
    	newPermissionGateActivity.setYcoord(this.getYcoord());
    	newPermissionGateActivity.setActivityTypeId(this.getActivityTypeId());
    	
    	newPermissionGateActivity.setGroupingSupportType(this.getGroupingSupportType());
    	newPermissionGateActivity.setApplyGrouping(this.getApplyGrouping());
    	newPermissionGateActivity.setActivityCategoryID(this.getActivityCategoryID());
    	
    	newPermissionGateActivity.setGrouping(this.getGrouping());
    	newPermissionGateActivity.setGroupingUIID(this.getGroupingUIID());
    	newPermissionGateActivity.setLearningLibrary(this.getLearningLibrary());    	
    	newPermissionGateActivity.setDefineLater(this.getDefineLater());
    	newPermissionGateActivity.setCreateDateTime(new Date());
    	newPermissionGateActivity.setRunOffline(this.getRunOffline());
    	newPermissionGateActivity.setLibraryActivity(this.getLibraryActivity());
    	newPermissionGateActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());
    	newPermissionGateActivity.setLanguageFile(this.getLanguageFile());
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
