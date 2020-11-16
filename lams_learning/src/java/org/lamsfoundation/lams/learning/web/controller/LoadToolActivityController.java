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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learning.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.AssertionFailure;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.tool.exception.RequiredGroupMissingException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class to forward the user to a Tool using an intermediate loading page. Can handle regular tools + grouping
 * and gates (system tools). Displays the activity that is in the request. This allows it to show any arbitrary
 * activity, not just the current activity.
 */
@Controller
public class LoadToolActivityController {
    private static Logger log = Logger.getLogger(LoadToolActivityController.class);
    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;

    public static final String PARAM_ACTIVITY_URL = "activityURL";
    public static final String PARAM_IS_BRANCHING = "isBranching";

    /**
     * Gets an activity from the request (attribute) and forwards onto a loading page.
     */
    @RequestMapping("/LoadToolActivity")
    public String execute(@ModelAttribute ActivityForm form, HttpServletRequest request, HttpServletResponse response) {
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);

	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = learnerService.getActivity(activityId);

	/*
	 * Synchronise calls to the same activity and attempt to create a session, if it does not exist.
	 * Even though the method creating sessions in LamsCoreToolService is synchronised,
	 * subsequent reads from DB are (probably) dirty,
	 * i.e. they show that session does not exist while it is already there,
	 * created by another, isolated transaction.
	 * Reattempting session creation from within transaction not only is broken (because of the dirty reads)
	 * but also does not make sense, because if the session already exists,
	 * there is no point in repeating a failed attempt to create it.
	 *
	 * The synchronisation code below prevents threads from creating sessions at the same time.
	 *
	 *
	 * Object toolSessionCreationLock = null;
	 * synchronized (LoadToolActivityAction.toolSessionCreationLocks) {
	 * toolSessionCreationLock = LoadToolActivityAction.toolSessionCreationLocks.get(activityID);
	 * if (toolSessionCreationLock == null) {
	 * toolSessionCreationLock = activityID;
	 * LoadToolActivityAction.toolSessionCreationLocks.put(activityID, toolSessionCreationLock);
	 * }
	 * }
	 * synchronized (toolSessionCreationLock) {
	 */
	try {
	    learnerService.createToolSessionsIfNecessary(activity, learnerProgress);

	    /*
	     * } catch (UnexpectedRollbackException e) {
	     * LamsAction.log.warn("Got exception while trying to create a tool session, but carrying on.", e);
	     */
	} catch (RequiredGroupMissingException e) {
	    //got here when activity requires existing grouping but no group for user exists yet
	    log.warn(e.getMessage());
	    request.setAttribute("messageKey", e.getMessage());
	    return "msgContent";
	} catch (AssertionFailure e) {
	    /*
	     * If this error is
	     * "null id in org.lamsfoundation.lams.tool.GroupedToolSession entry (don't flush the Session after an exception occurs)"
	     * then it is probably because two users tried to create a tool session and the same time.
	     * Our DB unique key kicked in and prevented a duplicate session.
	     * Our Transaction retry interceptor tried to repeat the transaction and failed because the transaction is
	     * marked as rollback and we flush it.
	     * This rollback should not happen as we create a new (nested) transaction for each retry, but it does not
	     * seem
	     * to work very well with current libraries.
	     * What we do now is just silence the error log.
	     * The exception we get means: there is already a session. And this is what we wanted - an existing session,
	     * so we can just carry on without showing the exception to the user.
	     */
	    log.warn("Error while creating a tool session: " + e.getMessage());
	}

	form.setActivityID(activity.getActivityId());

	if (activity.isToolActivity() || activity.isSystemToolActivity()) {
	    String url = activityMapping.getLearnerToolURL(learnerProgress.getLesson(), activity,
		    learnerProgress.getUser());
	    form.addActivityURL(new ActivityURL(activity.getActivityId(), url));

	} else {
	    log.error("activity not ToolActivity");
	    return "error";
	}
	return "loadToolActivity";
    }
}