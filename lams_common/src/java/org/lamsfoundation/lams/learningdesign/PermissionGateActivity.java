package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
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
            Integer gateActivityLevelId)
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
                gateActivityLevelId);
    }
    
    /** default constructor */
    public PermissionGateActivity()
    {
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
            Integer gateActivityLevelId)
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
                gateActivityLevelId);
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
    	newPermissionGateActivity.setGrouping(originalActivity.getGrouping());
    	newPermissionGateActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	newPermissionGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());    	
    	newPermissionGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newPermissionGateActivity.setCreateDateTime(new Date());
    	newPermissionGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
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
