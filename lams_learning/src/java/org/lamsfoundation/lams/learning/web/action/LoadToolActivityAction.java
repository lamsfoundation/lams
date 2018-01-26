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

package org.lamsfoundation.lams.learning.web.action;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.tool.exception.RequiredGroupMissingException;
import org.lamsfoundation.lams.web.action.LamsAction;

/**
 * Action class to forward the user to a Tool using an intermediate loading page. Can handle regular tools + grouping
 * and gates (system tools). Displays the activity that is in the request. This allows it to show any arbitrary
 * activity, not just the current activity.
 *
 * XDoclet definition:
 *
 *
 *
 *
 *
 *
 */
public class LoadToolActivityAction extends ActivityAction {

    public static final String PARAM_ACTIVITY_URL = "activityURL";
    public static final String PARAM_IS_BRANCHING = "isBranching";

    private static final Map<Long, Object> toolSessionCreationLocks = new TreeMap<Long, Object>();

    /**
     * Gets an activity from the request (attribute) and forwards onto a loading page.
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ActivityForm form = (ActivityForm) actionForm;
	ActivityMapping actionMappings = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());

	ICoreLearnerService learnerService = getLearnerService();
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);

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
	    LamsAction.log.warn(e.getMessage());
	    request.setAttribute("messageKey", e.getMessage());
	    return mapping.findForward("message");
	}

	form.setActivityID(activity.getActivityId());

	String mappingName = "displayTool";
	if (activity.isToolActivity() || activity.isSystemToolActivity()) {

	    String url = actionMappings.getLearnerToolURL(learnerProgress.getLesson(), activity,
		    learnerProgress.getUser());
	    form.addActivityURL(new ActivityURL(activity.getActivityId(), url));

	} else {
	    LamsAction.log.error(LamsAction.className + ": activity not ToolActivity");
	    return mapping.findForward(ActivityMapping.ERROR);
	}
	return mapping.findForward(mappingName);
    }
}