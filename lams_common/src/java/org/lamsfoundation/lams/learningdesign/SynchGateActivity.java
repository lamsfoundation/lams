package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class 
*/
public class SynchGateActivity extends GateActivity implements Serializable {

    /** full constructor */
    public SynchGateActivity(Long activityId, 
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
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId, 
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId,
            Set waitingLearners) {
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
    }

    /** default constructor */
    public SynchGateActivity() {
    }

    /** minimal constructor */
    public SynchGateActivity(Long activityId, 
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
    }
    
    /**
     * Makes a copy of the SynchGateActivity for authoring, preview and monitoring enviornment 
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return SynchGateActivity Returns a deep-copy of the originalActivity
     */    
    public static SynchGateActivity createCopy(SynchGateActivity originalActivity){
    	SynchGateActivity newSynchGateActivity = new SynchGateActivity();
    	
    	newSynchGateActivity.setGateActivityLevelId(originalActivity.getGateActivityLevelId());
    	newSynchGateActivity.setGateOpen(new Boolean(false));
    	
    	newSynchGateActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newSynchGateActivity.setDescription(originalActivity.getDescription());
    	newSynchGateActivity.setTitle(originalActivity.getTitle());
    	newSynchGateActivity.setHelpText(originalActivity.getHelpText());    	
    	newSynchGateActivity.setXcoord(originalActivity.getXcoord());
    	newSynchGateActivity.setYcoord(originalActivity.getYcoord());
    	newSynchGateActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	
    	newSynchGateActivity.setGroupingSupportType(originalActivity.getGroupingSupportType());
    	newSynchGateActivity.setApplyGrouping(originalActivity.getApplyGrouping());
    	newSynchGateActivity.setActivityCategoryID(originalActivity.getActivityCategoryID());
    	
    	newSynchGateActivity.setGrouping(originalActivity.getGrouping());
    	newSynchGateActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	newSynchGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newSynchGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newSynchGateActivity.setCreateDateTime(new Date());
    	newSynchGateActivity.setRunOffline(originalActivity.getRunOffline());
    	
    	newSynchGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newSynchGateActivity.setOnlineInstructions(originalActivity.getOnlineInstructions());
    	
    	newSynchGateActivity.setLibraryActivity(originalActivity.getLibraryActivity());
    	newSynchGateActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());
    	
    	return newSynchGateActivity;    	
    }

    public String toString() {
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
