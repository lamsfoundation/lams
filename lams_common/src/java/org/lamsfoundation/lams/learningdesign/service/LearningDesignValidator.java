/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;

/**
 * Validates a learning design. Encapsulates all the validation rules that are "cross activity" for a learning design as
 * well as calling any activity specific validation functionality.
 *
 * Most of the rules will never trigger as the GUI client will stop them occurring, but they are checked again here just
 * in case.
 *
 * Users can save invalid design - mainly for things like missing transitions. But LAMS won't run an invalid design.
 *
 * @author Fiona Malikoff
 */
public class LearningDesignValidator {

    MessageService messageService;
    LearningDesign learningDesign;
    Vector<ValidationErrorDTO> errors;

    LearningDesignValidator(LearningDesign learningDesign, MessageService messageService) {
	this.messageService = messageService;
	this.learningDesign = learningDesign;
    }

    /** Run the validation */
    public Vector<ValidationErrorDTO> validate() {
	errors = new Vector<ValidationErrorDTO>(); // initialises the list of validation messages.

	if (!ValidationUtil.isOrgNameValid(learningDesign.getTitle())) {
	    errors.add(new ValidationErrorDTO(ValidationErrorDTO.TITLE_CHARACTERS_ERROR_CODE,
		    messageService.getMessage(ValidationErrorDTO.TITLE_CHARACTERS_ERROR_KEY)));
	}

	// check the design has a starting or first activity
	if (learningDesign.getFirstActivity() == null) {
	    errors.add(new ValidationErrorDTO(ValidationErrorDTO.FIRST_ACTIVITY_ERROR_CODE,
		    messageService.getMessage(ValidationErrorDTO.FIRST_ACTIVITY_ERROR_KEY)));
	}

	// check all activities have their necessary transitions. First check the
	// top level, then we need to check each branch inside a branching activity.
	Set<Activity> topLevelActivities = extractFloatingActivities(learningDesign.getParentActivities());
	validateActivityTransitionRules(topLevelActivities, learningDesign.getTransitions());

	Set<Activity> activities = learningDesign.getActivities();
	for (Activity activity : activities) {
	    if (!ValidationUtil.isOrgNameValid(activity.getTitle())) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.TITLE_CHARACTERS_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.TITLE_CHARACTERS_ERROR_KEY),
			activity.getActivityUIID()));
	    }

	    checkIfGroupingRequired(activity);
	    validateGroupingIfGroupingIsApplied(activity);
	    validateOptionalActivity(activity);
	    validateOptionsActivityOrderId(activity);
	    validateFloatingActivity(activity);
	    validateGroupingActivity(activity, activities);
	    Vector<ValidationErrorDTO> activityErrors = activity.validateActivity(messageService);
	    if (activityErrors != null && !activityErrors.isEmpty()) {
		errors.addAll(activityErrors);
	    }
	}

	cleanupValidationErrors();
	return errors;
    }

    /**
     * Removes all Floating Activity(s) from the top level collection for validation procressing.
     *
     * @param topLevelActivities
     *            Set of Top-level activities.
     */
    private Set<Activity> extractFloatingActivities(Set<Activity> topLevelActivities) {
	for (Activity activity : topLevelActivities) {
	    if (activity.isFloatingActivity()) {
		topLevelActivities.remove(activity);
		return topLevelActivities;
	    }
	}

	return topLevelActivities;
    }

    /**
     * Cleans up multiple and redundant error messages in the list.
     *
     * @param errors
     *            List of errors to cleanup.
     */
    private void cleanupValidationErrors() {
	Iterator it = errors.iterator();
	while (it.hasNext()) {
	    cleanupTransitionErrors(it);
	}
    }

    private void cleanupTransitionErrors(Iterator topIt) {
	ValidationErrorDTO nextError;
	ValidationErrorDTO currentError = (ValidationErrorDTO) topIt.next();
	Iterator it = errors.iterator();

	while (it.hasNext()) {
	    nextError = (ValidationErrorDTO) it.next();

	    if (currentError.getCode().equals(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE)
		    && (nextError.getCode().equals(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE)
			    || nextError.getCode().equals(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE))) {
		if (currentError.getUIID().equals(nextError.getUIID())) {
		    topIt.remove();
		    return;
		} else if (currentError.getCode().equals(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE)) {
		    if (nextError.getCode().equals(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE)) {
			if (currentError.getUIID().equals(nextError.getUIID())) {
			    it.remove();
			} else if (currentError.getCode().equals(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE)) {
			    if (nextError.getCode().equals(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE)) {
				if (currentError.getUIID().equals(nextError.getUIID())) {
				    it.remove();
				}
			    }
			}
		    }
		}
	    }

	}

    }

    /**
     * Perform transition related validations.
     *
     * All activities with no input transitions are added to the vector <code>noInputTransition</code>. If the size
     * of this list is greater than 1 (which violates the rule of having exactly one top level activity with no input
     * transition), then a ValidationErrorDTO will be created for each activity with no input transition. Similarly, the
     * same concept applies for activities with no output transition.
     *
     * @param activities
     *            A subset of the overall design. Will be just the top level activities.
     * @param transitions
     *            A transitions from the design
     *
     */
    private void validateActivityTransitionRules(Set<Activity> activities, Set<Transition> transitions) {
	validateTransitions(transitions);
	ArrayList<Activity> noInputTransition = new ArrayList<Activity>(); // a list to hold the activities which have
									   // no input transition
	ArrayList<Activity> noOuputTransition = new ArrayList<Activity>(); // a list to hold the activities which have
									   // no output transition
	int numOfTopLevelActivities = activities.size();

	// All the branching activities and optional activities we find are stored in this list, so we can process them
	// at the end.
	// We don't want to process them straight away or the branch activity errors would be mixed up with the previous
	// level's errors.
	// We need the optional activities, as they may contain a branching activity.
	ArrayList<ComplexActivity> complexActivitiesToProcess = null;

	for (Activity activity : activities) {
	    if (activity.isFloatingActivity()) {
		break;
	    }

	    checkActivityForTransition(activity, numOfTopLevelActivities);

	    if (activity.getTransitionFrom() == null) {
		noOuputTransition.add(activity);
	    }
	    if (activity.getTransitionTo() == null) {
		noInputTransition.add(activity);
	    }
	    complexActivitiesToProcess = checkActivityForFurtherProcessing(complexActivitiesToProcess, activity);
	}

	if (numOfTopLevelActivities > 0) {
	    if (noInputTransition.size() == 0) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE2_KEY)));
	    }

	    if (noInputTransition.size() > 1) {

		// put out an error for each activity, but skip the any activities that are the first activity in the
		// branch as they shouldn't have an input transition.
		for (Activity a : noInputTransition) {
		    errors.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE1_KEY),
			    a.getActivityUIID()));
		}
	    }

	    if (noOuputTransition.size() == 0) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE2_KEY)));
	    }

	    if (noOuputTransition.size() > 1) {
		// there is more than one activity with no output transitions
		for (Activity a : noOuputTransition) {
		    errors.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE1_KEY),
			    a.getActivityUIID()));
		}
	    }
	}

	processComplexActivitiesForTransitions(complexActivitiesToProcess);

    }

    private void processComplexActivitiesForTransitions(ArrayList<ComplexActivity> complexActivitiesToProcess) {

	if (complexActivitiesToProcess != null) {
	    for (ComplexActivity complex : complexActivitiesToProcess) {
		checkTransitionsInComplexActivity(complex);
	    }
	}

    }

    @SuppressWarnings("unchecked")
    private void checkTransitionsInComplexActivity(ComplexActivity complexActivity) {
	// All the branching activities and optional activities we find are stored in this list, so we can process them
	// at the end.
	// We don't want to process them straight away or the branch activity errors would be mixed up with the previous
	// level's errors.
	// We need the optional activities, as they may contain a branching activity.
	ArrayList<ComplexActivity> complexActivitiesToProcess = null;

	if (complexActivity.isBranchingActivity()) {
	    for (ComplexActivity sequence : (Set<ComplexActivity>) (Set<?>) complexActivity.getActivities()) {
		for (Activity activity : sequence.getActivities()) {
		    checkActivityForTransition(activity, sequence.getActivities().size());
		    complexActivitiesToProcess = checkActivityForFurtherProcessing(complexActivitiesToProcess,
			    activity);
		}
	    }
	} else {
	    for (Activity activity : complexActivity.getActivities()) {
		complexActivitiesToProcess = checkActivityForFurtherProcessing(complexActivitiesToProcess, activity);
	    }
	}

	processComplexActivitiesForTransitions(complexActivitiesToProcess);
    }

    /**
     * @param complexActivitiesToProcess
     * @param activity
     * @return
     */
    private ArrayList<ComplexActivity> checkActivityForFurtherProcessing(
	    ArrayList<ComplexActivity> complexActivitiesToProcess, Activity activity) {
	if (activity.isComplexActivity() && !activity.isParallelActivity() && !activity.isFloatingActivity()) {
	    if (complexActivitiesToProcess == null) {
		complexActivitiesToProcess = new ArrayList<ComplexActivity>();
	    }
	    complexActivitiesToProcess.add((ComplexActivity) activity);
	}
	return complexActivitiesToProcess;
    }

    private boolean isFirstActivityInBranch(Activity a) {
	ComplexActivity parentActivity = (ComplexActivity) a.getParentActivity();
	if (parentActivity == null || !parentActivity.isSequenceActivity()) {
	    return false;
	} else {
	    return parentActivity.getDefaultActivity() != null && parentActivity.getDefaultActivity().equals(a);
	}
    }

    /**
     * This method checks if each transition in the learning design has an activity before and after the transition.
     *
     * If there exists a transition which does not have an activity before or after it, the ValidationErrorDTO is added
     * to the list of validation messages.
     *
     * @param transitions
     *            the set of transitions to iterate through and validate
     */
    private void validateTransitions(Set transitions) {
	Iterator i = transitions.iterator();
	while (i.hasNext()) {
	    Transition transition = (Transition) i.next();
	    Activity fromActivity = transition.getFromActivity();
	    Activity toActivity = transition.getToActivity();
	    if (fromActivity == null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.TRANSITION_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY),
			transition.getTransitionUIID()));
	    } else if (toActivity == null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.TRANSITION_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY),
			transition.getTransitionUIID()));
	    }

	}

    }

    /**
     * For any learning design that has more than one activity then each activity should have at least an input or
     * output transition. If there is only one activity in the learning design, then that activity should not have any
     * transitions. This method will check if there is an activity that exists that has no transitions at all (if there
     * is more than one activity in the learning design)
     *
     * @param activity
     *            The Activity to validate
     * @param numOfActivities
     *            The number of activities in the learning design.
     */
    private void checkActivityForTransition(Activity activity, int numOfActivities) {
	// if one activity, then shouldn't have any transitions
	Transition inputTransition = activity.getTransitionTo();
	Transition outputTransition = activity.getTransitionFrom();

	if (numOfActivities > 1) {
	    if (inputTransition == null && outputTransition == null && !isFirstActivityInBranch(activity)) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY),
			activity.getActivityUIID()));
	    }
	}
	if (numOfActivities == 1) {
	    if (inputTransition != null || outputTransition != null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY),
			activity.getActivityUIID()));
	    }

	}

    }

    /**
     * If grouping support type is set to <code>GROUPING_SUPPORT_REQUIRED</code>, then the activity is validated to
     * ensure that the grouping exists. If grouping support type is set to <code>GROUPING_SUPPORT_NONE</code> then the
     * activity is validated to ensure that the grouping does not exist.
     *
     * If any validation fails, the message will be added to the list of validation messages.
     *
     * @param activity
     */
    private void checkIfGroupingRequired(Activity activity) {

	Integer groupingSupportType = activity.getGroupingSupportType();
	if (groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_REQUIRED) {
	    // make sure activity has been assigned a grouping
	    Grouping grouping = activity.getGrouping();
	    if (grouping == null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_REQUIRED_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.GROUPING_REQUIRED_ERROR_KEY),
			activity.getActivityUIID()));
	    }
	} else if (groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_NONE) {
	    Grouping grouping = activity.getGrouping();
	    if (grouping != null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR_KEY),
			activity.getActivityUIID()));
	    }
	}

    }

    /**
     * If this activity is an OptionalActivity, then it must contain one or more activities.
     *
     * @param parentActivity
     */
    private void validateOptionalActivity(Activity parentActivity) {

	if (parentActivity.isOptionsActivity()) {
	    // get the child activities and check how many there are.
	    OptionsActivity optionsActivity = (OptionsActivity) parentActivity;
	    Set childActivities = optionsActivity.getActivities();
	    int numOfChildActivities = childActivities.size();
	    if (numOfChildActivities == 0) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR_KEY),
			optionsActivity.getActivityUIID()));
	    }

	}

    }

    /**
     * If this activity is an FloatingActivity, then it must contain at least one activity and no more than the maximum
     * activities value.
     *
     * @param parentActivity
     */
    private void validateFloatingActivity(Activity parentActivity) {
	String[] args = new String[1];

	if (parentActivity.isFloatingActivity()) {
	    // get the child activities and check how many there are.
	    FloatingActivity floatingActivity = (FloatingActivity) parentActivity;
	    Set childActivities = floatingActivity.getActivities();
	    int numOfChildActivities = childActivities.size();
	    // require at least one floating activity
	    if (numOfChildActivities == 0) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.FLOATING_ACTIVITY_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.FLOATING_ACTIVITY_ERROR_KEY),
			floatingActivity.getActivityUIID()));
	    }
	    // collection cannot exceed max limit
	    if (numOfChildActivities > floatingActivity.getMaxNumberOfActivitiesNotNull()) {
		args[0] = floatingActivity.getMaxNumberOfActivities().toString();
		String errorMsg = messageService.getMessage(ValidationErrorDTO.FLOATING_ACTIVITY_MAX_ERROR_KEY, args);
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.FLOATING_ACTIVITY_MAX_ERROR_CODE, errorMsg,
			floatingActivity.getActivityUIID()));
	    }

	}

    }

    /**
     * If this activity is an GroupingActivity, the number of groups in the grouping records is greater than 0 and the
     * grouping has some groups, then the actual number of groups must no exceed the desired number of groups. If the
     * desired number of groups is 0 then don't check the number of actual groups as there is no semantic limit to the
     * number of groups.
     *
     * @param parentActivity
     */
    private void validateGroupingActivity(Activity activity, Set<Activity> activities) {

	if (activity.isGroupingActivity()) {
	    // get the child activities and check how many there are.
	    GroupingActivity groupingActivity = (GroupingActivity) activity;
	    Grouping grouping = groupingActivity.getCreateGrouping();
	    if (grouping == null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_ACTIVITY_MISSING_GROUPING_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.GROUPING_ACTIVITY_MISSING_GROUPING_KEY),
			activity.getActivityUIID()));
	    }
	    Integer numGroupsInteger = null;
	    if (grouping.isRandomGrouping()) {
		RandomGrouping random = (RandomGrouping) grouping;
		numGroupsInteger = random.getNumberOfGroups();
	    } else {
		numGroupsInteger = grouping.getMaxNumberOfGroups();
	    }
	    int maxNumGroups = numGroupsInteger == null ? 0 : numGroupsInteger.intValue();
	    if (maxNumGroups > 0 && grouping.getGroups() != null && grouping.getGroups().size() > maxNumGroups) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_KEY),
			activity.getActivityUIID()));
	    }

	    boolean used = false;
	    for (Activity groupedActivity : activities) {
		if (groupedActivity.getApplyGrouping()
			&& groupingActivity.getCreateGroupingUIID().equals(groupedActivity.getGroupingUIID())) {
		    used = true;
		    break;
		}
	    }

	    if (!used) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_NOT_USED_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.GROUPING_NOT_USED_ERROR_KEY),
			activity.getActivityUIID()));
	    }
	}
    }

    /**
     * This method ensures that the order id of the optional activities start from 1, are sequential and do not contain
     * any duplicates. It will iterate through the child activities of the OptionalActivity, and compare the current
     * activity order id with the previous activity order id. The currentActivityId should be 1 greater than the
     * previous activity order id.
     *
     * @param parentActivity
     */
    private void validateOptionsActivityOrderId(Activity parentActivity) {
	Integer thisActivityOrderId = null;
	Integer previousActivityOrderId = null;
	boolean validOrderId = true;
	if (parentActivity.isOptionsActivity()) {
	    OptionsActivity optionsActivity = (OptionsActivity) parentActivity;
	    Set childActivities = optionsActivity.getActivities(); // childActivities should be sorted according to
								   // order id (using the activityOrderComparator)
	    Iterator i = childActivities.iterator();
	    while (i.hasNext() && validOrderId) {
		Activity childActivity = (Activity) i.next();
		thisActivityOrderId = childActivity.getOrderId();
		if (previousActivityOrderId != null) {
		    // compare the two numbers
		    if (thisActivityOrderId == null) {
			validOrderId = false;
		    } else if (thisActivityOrderId.longValue() != previousActivityOrderId.longValue() + 1) {
			validOrderId = false;
		    }

		} else {
		    // this is the first activity, since the previousActivityId is null
		    if (thisActivityOrderId == null || thisActivityOrderId.longValue() != 1) {
			validOrderId = false;
		    }
		}
		previousActivityOrderId = thisActivityOrderId;
	    }

	    if (!validOrderId) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY),
			optionsActivity.getActivityUIID()));
	    }

	}
    }

    /**
     * If applyGrouping is set, then the grouping must exist
     *
     * @param activity
     */
    private void validateGroupingIfGroupingIsApplied(Activity activity) {
	if (activity.getApplyGrouping().booleanValue()) // if grouping is applied, ensure grouping exists
	{
	    if (activity.getGrouping() == null) {
		errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_SELECTED_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.GROUPING_SELECTED_ERROR_KEY),
			activity.getActivityUIID()));
	    }
	}

    }
}
