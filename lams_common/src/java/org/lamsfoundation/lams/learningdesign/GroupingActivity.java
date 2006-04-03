/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.GroupingActivityStrategy;


/**
 * @hibernate.class
 * 
 * A GroupingActivity creates Grouping.
 * @author Manpreet Minhas
 */
public class GroupingActivity extends SimpleActivity implements Serializable
{
	/** The grouping_ui_id of the Grouping that this activity creates */
	private Integer createGroupingUIID;
	
    /** The grouping that this activity creates */
    public Grouping createGrouping;
    
    /** full constructor */
    public GroupingActivity(Long activityId, 
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
            Grouping createGrouping,
			Integer grouping_ui_id,
			Integer create_grouping_ui_id,
			String languageFile)
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
				languageFile);
        this.createGrouping = createGrouping;        
        this.createGroupingUIID = create_grouping_ui_id;
        super.simpleActivityStrategy = new GroupingActivityStrategy(this);
    }
    
    /** default constructor */
    public GroupingActivity()
    {
    	super.simpleActivityStrategy = new GroupingActivityStrategy(this);
    }
    
    /** minimal constructor */
    public GroupingActivity(Long activityId, 
            Boolean defineLater,
            java.util.Date createDateTime,
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
            org.lamsfoundation.lams.learningdesign.Activity parentActivity,
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
            org.lamsfoundation.lams.learningdesign.Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Grouping createGrouping,
			Integer grouping_ui_id,
			Integer create_grouping_ui_id)
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
				transitionFrom);
        this.createGrouping = createGrouping;        
        this.createGroupingUIID = create_grouping_ui_id;
        super.simpleActivityStrategy = new GroupingActivityStrategy(this);
    }
    /**
     * This function creates a deep copy of the GroupingActivity of the current
     * object. However each time a GroupingActivity is deep copied
     * it would result in creation of new Groups as well. 
     * <p>
     * Note: the grouping
     * object will be created here but the calling class will need to insert
     * it into the database otherwise it won't be persisted (can't easily set
     * it up to cascade). Call getCreateGrouping() to get the grouping object
     * to be stored via the session.
     * 
     * @return GroupActivity, which is copy of this activity. 
     */
    public Activity createCopy(){
    	GroupingActivity groupingActivity = new GroupingActivity();
    	
    	groupingActivity.setActivityUIID(this.getActivityUIID());
    	groupingActivity.setDescription(this.getDescription());
    	groupingActivity.setTitle(this.getTitle());
    	groupingActivity.setHelpText(this.getHelpText());
    	groupingActivity.setXcoord(this.getXcoord());
    	groupingActivity.setYcoord(this.getYcoord());    	    
    	groupingActivity.setActivityTypeId(this.getActivityTypeId());
    	
    	groupingActivity.setGroupingSupportType(this.getGroupingSupportType());
    	groupingActivity.setApplyGrouping(this.getApplyGrouping());
    	groupingActivity.setActivityCategoryID(this.getActivityCategoryID());
    	
    	groupingActivity.setDefineLater(this.getDefineLater());    	
    	groupingActivity.setCreateDateTime(new Date()); 
    	groupingActivity.setRunOffline(this.getRunOffline());
    	groupingActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());
    	groupingActivity.setLanguageFile(this.getLanguageFile());

    	Grouping currentGrouping = this.getCreateGrouping();
    	Grouping newGrouping = currentGrouping.createCopy();
    	groupingActivity.setCreateGrouping(newGrouping);

    	return groupingActivity;    	
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }
    
	/**
	 * @return Returns the createGrouping.
	 */
	public Grouping getCreateGrouping() {
		return createGrouping;
	}
	/**
	 * @param createGrouping The createGrouping to set.
	 */
	public void setCreateGrouping(Grouping createGrouping) {
		this.createGrouping = createGrouping;
	}
	public Integer getCreateGroupingUIID() {
		return createGroupingUIID;
	}
	public void setCreateGroupingUIID(Integer create_grouping_ui_id) {
		this.createGroupingUIID = create_grouping_ui_id;
	}

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull()
    {
        return false;
    }
}
