package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.SequenceActivityStrategy;


/** 
 * @hibernate.class 
*/
public class SequenceActivity extends ComplexActivity implements Serializable {

    /** full constructor */
    public SequenceActivity(Long activityId, 
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
            java.util.Set activities) {
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
                activities);
        super.activityStrategy = new SequenceActivityStrategy();
    }


    /** default constructor */
    public SequenceActivity() {
        super.activityStrategy = new SequenceActivityStrategy();
    }

     /** minimal constructor */
    public SequenceActivity(Long activityId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            java.util.Set activities) {
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
              activities);
      super.activityStrategy = new SequenceActivityStrategy();
    }
    
    /**
     * Makes a copy of the SequenceActivity for authoring, preview and monitoring enviornment
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return SequenceActivity Returns a deep-copy of the originalActivity
     */
    public static SequenceActivity createCopy(SequenceActivity originalActivity){
    	SequenceActivity newSequenceActivity = new SequenceActivity();
    	
    	newSequenceActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newSequenceActivity.setDescription(originalActivity.getDescription());
    	newSequenceActivity.setTitle(originalActivity.getTitle());
    	newSequenceActivity.setHelpText(originalActivity.getHelpText());
    	newSequenceActivity.setXcoord(originalActivity.getXcoord());
    	newSequenceActivity.setYcoord(originalActivity.getYcoord());
    	newSequenceActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	
    	newSequenceActivity.setGroupingSupportType(originalActivity.getGroupingSupportType());
    	newSequenceActivity.setApplyGrouping(originalActivity.getApplyGrouping());
    	newSequenceActivity.setActivityCategoryID(originalActivity.getActivityCategoryID());
    	
    	newSequenceActivity.setGrouping(originalActivity.getGrouping());
    	newSequenceActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	newSequenceActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newSequenceActivity.setDefineLater(originalActivity.getDefineLater());
    	newSequenceActivity.setCreateDateTime(new Date());
    	newSequenceActivity.setRunOffline(originalActivity.getRunOffline());
    	
    	newSequenceActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newSequenceActivity.setOnlineInstructions(originalActivity.getOnlineInstructions());
    	
    	newSequenceActivity.setLibraryActivity(originalActivity.getLibraryActivity());
		newSequenceActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());    	
    	
		return newSequenceActivity;
    }

    /**
     * Retrieve the first activity in current sequence activity;
     * @return the first activity;
     */
    public Activity getFirstActivityInSequenceActivity()
    {
        return this.getNextActivityByParent(new NullActivity()); 
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
