package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;

import java.util.ArrayList;

public class ListingLearningDesignProcessor extends LearningDesignProcessor {

    ArrayList<Activity> activityList;

    public ListingLearningDesignProcessor(LearningDesign design, IActivityDAO activityDAO) {
	super(design, activityDAO);
	activityList = new ArrayList<>();
    }

    @Override
    public boolean startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	return true;
    }

    @Override
    public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	activityList.add(activity);
    }

    @Override
    public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
    }

    @Override
    public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	activityList.add(activity);
    }

    public ArrayList<Activity> getActivityList() {
	return activityList;
    }
}