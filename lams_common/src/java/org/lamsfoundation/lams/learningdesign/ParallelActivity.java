package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang.builder.ToStringBuilder;

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
    }

    /** default constructor */
    public ParallelActivity() {
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
    }
    public static ParallelActivity createCopy(ParallelActivity originalActivity){
    	
    	ParallelActivity newParallelActivity = new ParallelActivity();
    	
    	newParallelActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newParallelActivity.setDescription(originalActivity.getDescription());
    	newParallelActivity.setTitle(originalActivity.getTitle());
    	newParallelActivity.setXcoord(originalActivity.getXcoord());
    	newParallelActivity.setYcoord(originalActivity.getYcoord());
    	newParallelActivity.setDefineLater(originalActivity.getDefineLater());
    	newParallelActivity.setCreateDateTime(new Date());
    	newParallelActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newParallelActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newParallelActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newParallelActivity.setActivities(new HashSet());
    	
    	/** TODO */
    	//newParallelActivity.setTransitionTo();
		//newParallelActivity.setTransitionFrom();
    	
    	return newParallelActivity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
