package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

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
            Integer gateActivityLevelId) {
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
            Integer gateActivityLevelId) {
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
    public static SynchGateActivity createCopy(SynchGateActivity originalActivity){
    	SynchGateActivity newSynchGateActivity = new SynchGateActivity();
    	
    	newSynchGateActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newSynchGateActivity.setDescription(originalActivity.getDescription());
    	newSynchGateActivity.setTitle(originalActivity.getTitle());
    	newSynchGateActivity.setXcoord(originalActivity.getXcoord());
    	newSynchGateActivity.setYcoord(originalActivity.getYcoord());
    	newSynchGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newSynchGateActivity.setCreateDateTime(new Date());
    	newSynchGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newSynchGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newSynchGateActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newSynchGateActivity.setGateActivityLevelId(originalActivity.getGateActivityLevelId());
    	/**
    	 * TODO calculate how these two values would be set for COPY 
    	 * */
    	//newSynchGateActivity.setTransitionTo();
    	//newSynchGateActivity.setTransitionFrom();
    	
    	return newSynchGateActivity;    	
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
