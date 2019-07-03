/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learning.progress;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolBranchingActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Creates a basic tree of the user's current progress - what is completed, urls to jump back to completed activities,
 * etc
 */
public class ProgressBuilder extends LearningDesignProcessor {

    private static Logger log = Logger.getLogger(ProgressBuilder.class);

    User user;
    LearnerProgress progress;
    ActivityMapping activityMapping;
    ArrayList<ActivityURL> mainActivityList;
    ArrayStack activityListStack;
    ArrayList<ActivityURL> currentActivityList;
    String forceLearnerURL;
    boolean previewMode;
    boolean isFloating;

    /**
     * Create the builder. Supply all the data that will be needed to parse the design..
     * 
     * If accessMode == LEARNER then progress and user must not be null. This will create a list of portfolio objects
     * for
     * all activities that the LEARNER has completed or attempted.
     * 
     * If accessMode == AUTHOR then progress and user must not be null and a few hacks must be done to let the user skip
     * ahead (to be done).
     * 
     * In all cases, all activities are included, whether they are started or not.
     * 
     * @param design
     * @param activityDAO
     * @param lamsCoreToolService
     * @param accessMode
     * @param lesson
     * @param progress
     * @param user
     */
    public ProgressBuilder(LearnerProgress progress, IActivityDAO activityDAO, ActivityMapping activityMapping) {
	super(progress.getLesson().getLearningDesign(), activityDAO);
	this.user = progress.getUser();
	this.progress = progress;
	this.activityMapping = activityMapping;

	this.mainActivityList = new ArrayList<ActivityURL>();
	this.currentActivityList = mainActivityList;

	this.activityListStack = new ArrayStack(5);

	Lesson lesson = progress.getLesson();
	previewMode = lesson.isPreviewLesson();
	isFloating = false;
	//if ( previewMode ) {
	// setup the basic call to the learner screen, ready just to put the activity id on the end. Saves calculating it for every activity
	this.forceLearnerURL = "learning/learner/forceMoveRedirect.do?lessonID=" + progress.getLesson().getLessonId()
		+ "&destActivityID=";
	//}

    }

    /** Prepares to process children */
    @Override
    public boolean startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	// Create a new current activity list, putting the old current one on the stack. 
	activityListStack.push(currentActivityList);
	currentActivityList = new ArrayList<ActivityURL>();

	if (activity.isFloatingActivity()) {
	    isFloating = true;
	}
	return true;
    }

    /** Creates an ActivityURL and sets up the list of its children. */
    @Override
    public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {

	// only want to show the overall heading level for branching if none of the child activities have been started
	// have to check the children as the branching activity is attempted but no child is attempted if a user
	// is sitting on the waiting screen.
	// if a branch has been attempted, then we only want to show its children.
	boolean branchStarted = false;
	// always display all branches if in Preview mode
	if (!previewMode && activity.isBranchingActivity() && !(activity.isToolBranchingActivity()
		&& ((ToolBranchingActivity) activity).getBranchingOrderedAsc() != null)) {
	    Iterator iter = currentActivityList.iterator();
	    while (!branchStarted && iter.hasNext()) {
		ActivityURL sequenceURL = (ActivityURL) iter.next();
		if (sequenceURL.getStatus() != LearnerProgress.ACTIVITY_NOT_ATTEMPTED) {
		    branchStarted = true;
		    currentActivityList = (ArrayList<ActivityURL>) activityListStack.pop();
		    currentActivityList.addAll(sequenceURL.getChildActivities());
		}
	    }
	}

	if (!branchStarted) {
	    ActivityURL complexActivityURL = createActivityURL(activity);
	    complexActivityURL.setChildActivities(currentActivityList);
	    currentActivityList = (ArrayList<ActivityURL>) activityListStack.pop();
	    currentActivityList.add(complexActivityURL);

	    if (activity.isFloatingActivity()) {
		isFloating = false;
	    }
	}
    }

    @Override
    public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	// everything done by endSimpleActivity
    }

    /** Creates an ActivityURL. */
    @Override
    public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {

	ActivityURL p = createActivityURL(activity);
	currentActivityList.add(p);

    }

    /**
     * Creates a progress object with properties activityId, activityName, activityDescription and progress status.
     * 
     * @param activity
     * @return a progress object
     * @throws ProgressException
     */
    protected ActivityURL createActivityURL(Activity activity) throws LearningDesignProcessorException {
	if (activity == null) {
	    String error = "Cannot create activity progress for this activity as the activity is null.";
	    log.error(error);
	    throw new LearningDesignProcessorException(error);
	}
	ActivityURL activityURL = activityMapping.getActivityURL(progress, activity, false, isFloating);
	if (activityURL.getStatus() == LearnerProgress.ACTIVITY_NOT_ATTEMPTED) {
	    activityURL.setUrl((previewMode || isFloating) ? forceLearnerURL + activity.getActivityId() : null);
	}
	if (activity.isFloatingActivity()) {
	    activityURL.setUrl(null);
	}
	if (activity.isToolBranchingActivity() && ((ToolBranchingActivity) activity).getBranchingOrderedAsc() != null) {
	    activityURL.setType(activityURL.getType() + "Ordered");
	}
	return activityURL;
    }

    /** Get the list of all the activity progress DTOs, which in turn may contain other activity progress DTOs */
    public ArrayList<ActivityURL> getActivityList() {
	return mainActivityList;
    }
}
