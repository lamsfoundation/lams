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
    
    /**
     * Makes a copy of the ToolActivity for authoring, preview and monitoring enviornment
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
    	newSequenceActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newSequenceActivity.setDefineLater(originalActivity.getDefineLater());
    	newSequenceActivity.setCreateDateTime(new Date());
    	newSequenceActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newSequenceActivity.setLibraryActivity(originalActivity.getLibraryActivity());
		newSequenceActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());    	
    	newSequenceActivity.setActivities(new HashSet());
    	return newSequenceActivity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
