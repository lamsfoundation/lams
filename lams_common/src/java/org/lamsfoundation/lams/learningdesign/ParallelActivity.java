package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.lesson.ParallelActivityStrategy;

/** 
 * @hibernate.class 
*/
public class ParallelActivity extends ComplexActivity implements Serializable {

    /** full constructor */
    public ParallelActivity(Long activityId, 
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
        super.activityStrategy = new ParallelActivityStrategy();
    }

    /** default constructor */
    public ParallelActivity() {
        super.activityStrategy = new ParallelActivityStrategy();
    }

    /** minimal constructor */
    public ParallelActivity(Long activityId, 
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
      super.activityStrategy = new ParallelActivityStrategy();
    }
    /**
     * Makes a copy of the ParallelActivity for authoring, preview and monitoring enviornment 
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return ParallelActivity Returns a deep-copy of the originalActivity
     */
    public static ParallelActivity createCopy(ParallelActivity originalActivity){
    	
    	ParallelActivity newParallelActivity = new ParallelActivity();
    	
    	newParallelActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newParallelActivity.setDescription(originalActivity.getDescription());
    	newParallelActivity.setTitle(originalActivity.getTitle());
    	newParallelActivity.setHelpText(originalActivity.getHelpText());
    	newParallelActivity.setXcoord(originalActivity.getXcoord());
    	newParallelActivity.setYcoord(originalActivity.getYcoord());
    	newParallelActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	
    	newParallelActivity.setGroupingSupportType(originalActivity.getGroupingSupportType());
    	newParallelActivity.setApplyGrouping(originalActivity.getApplyGrouping());
    	newParallelActivity.setActivityCategoryID(originalActivity.getActivityCategoryID());
    	
    	newParallelActivity.setGrouping(originalActivity.getGrouping());
    	newParallelActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newParallelActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	
    	newParallelActivity.setDefineLater(originalActivity.getDefineLater());
    	newParallelActivity.setCreateDateTime(new Date());
    	newParallelActivity.setRunOffline(originalActivity.getRunOffline());
    	
    	newParallelActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newParallelActivity.setOnlineInstructions(originalActivity.getOnlineInstructions());
    	
    	newParallelActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());    	
    	newParallelActivity.setLibraryActivity(originalActivity.getLibraryActivity());		
    	
    	return newParallelActivity;
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
