package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang.builder.ToStringBuilder;


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
    }


    /** default constructor */
    public SequenceActivity() {
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
    }
    public static SequenceActivity createCopy(SequenceActivity originalActivity){
    	SequenceActivity newSequenceActivity = new SequenceActivity();
    	
    	newSequenceActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newSequenceActivity.setDescription(originalActivity.getDescription());
    	newSequenceActivity.setTitle(originalActivity.getTitle());
    	newSequenceActivity.setXcoord(originalActivity.getXcoord());
    	newSequenceActivity.setYcoord(originalActivity.getYcoord());
    	newSequenceActivity.setDefineLater(originalActivity.getDefineLater());
    	newSequenceActivity.setCreateDateTime(new Date());
    	newSequenceActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newSequenceActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newSequenceActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newSequenceActivity.setActivities(new HashSet());
    	
    	/** TODO */
    	//newSequenceActivity.setTransitionTo();
		//newSequenceActivity.setTransitionFrom();
    	
    	return newSequenceActivity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
