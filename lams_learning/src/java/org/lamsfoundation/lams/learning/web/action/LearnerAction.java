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

/* $$Id$$ */
package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.presence.PresenceWebsocketServer;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * <p>
 * The action servlet that interacts with learner to start a lams learner module, join a user to the lesson and allows a
 * user to exit a lesson.
 * </p>
 * 
 * <p>
 * It is also responsible for the interaction between lams server and UI. UI will call method implemented in this class
 * to get progress data or trigger a lams server calculation here
 * </p>
 * 
 * <b>Note:</b>It needs to extend the <code>LamsDispatchAction</code> which has been customized to accomodate struts
 * features to solve duplicate submission problem.
 * 
 * @author Jacky Fang
 * @since 3/03/2005
 * @version 1.1
 * 
 *
 * 
 *
 *
 *
 *
 */
public class LearnerAction extends LamsDispatchAction {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(LearnerAction.class);

    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------

    private ActionForward redirectToURL(ActionMapping mapping, HttpServletResponse response, String url)
	    throws IOException, ServletException {
	if (url != null) {
	    String fullURL = WebUtil.convertToFullURL(url);
	    response.sendRedirect(response.encodeRedirectURL(fullURL));
	} else {
	    throw new ServletException("Tried to redirect to url but url is null");
	}
	return null;
    }

    /**
     * <p>
     * The structs dispatch action that joins a learner into a lesson. This call is used for a user to start a lesson.
     * </p>
     * 
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * 
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * 
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward joinLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// initialize service object
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	Integer learner = null;
	try {

	    // get user and lesson based on request.
	    learner = LearningWebUtil.getUserId();
	    long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    // security check
	    Lesson lesson = learnerService.getLesson(lessonID);
	    User user = (User) LearnerServiceProxy.getUserManagementService(getServlet().getServletContext())
		    .findById(User.class, learner);
	    if ((lesson.getLessonClass() == null) || !lesson.getLessonClass().getLearners().contains(user)) {
		request.setAttribute("messageKey",
			"User " + user.getLogin() + " is not a learner in the requested lesson.");
		return mapping.findForward("message");
	    }
	    // check lesson's state if its suitable for learner's access
	    if (!lesson.isLessonAccessibleForLearner()) {
		request.setAttribute("messageKey", "Lesson is inaccessible");
		return mapping.findForward("message");
	    }

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("The learner [" + learner + "] is joining the lesson [" + lessonID + "]");
	    }

	    // join user to the lesson on the server
	    LearnerProgress learnerProgress = learnerService.joinLesson(learner, lessonID);

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("The learner [" + learner + "] joined lesson. The" + "progress data is:"
			+ learnerProgress.toString());
	    }

	    LearningWebUtil.putLearnerProgressInRequest(request, learnerProgress);

	    ActivityMapping activityMapping = LearnerServiceProxy
		    .getActivityMapping(this.getServlet().getServletContext());
	    String url = "learning/" + activityMapping.getDisplayActivityAction(lessonID);

	    redirectToURL(mapping, response, url);

	} catch (Exception e) {
	    LearnerAction.log.error("An error occurred while learner " + learner + " attempting to join the lesson.",
		    e);
	    return mapping.findForward(ActivityMapping.ERROR);
	}

	return null;
    }

    /**
     * Produces necessary data for learner progress bar.
     */
    @SuppressWarnings("unchecked")
    public ActionForward getLearnerProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	Integer learnerId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, true);
	Integer monitorId = null;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userId = user != null ? user.getUserID() : null;
	if (learnerId == null) {
	    // get progress for current user
	    learnerId = userId;
	} else {
	    // monitor mode; get progress for user given in the parameter
	    monitorId = userId;
	}

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	Object[] ret = learnerService.getStructuredActivityURLs(learnerId, lessonId);
	if (ret == null) {
	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	    return null;
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("currentActivityId", ret[1]);
	responseJSON.put("isPreview", ret[2]);
	for (ActivityURL activity : (List<ActivityURL>) ret[0]) {
	    if (activity.getFloating()) {
		// these are support activities
		for (ActivityURL childActivity : activity.getChildActivities()) {
		    responseJSON.append("support",
			    activityProgressToJSON(childActivity, null, lessonId, learnerId, monitorId));
		}
	    } else {
		responseJSON.append("activities",
			activityProgressToJSON(activity, (Long) ret[1], lessonId, learnerId, monitorId));
	    }
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON.toString());

	return null;
    }

    /**
     * Gets the same url as getLearnerActivityURL() but forwards directly to the url.
     * 
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward displayProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Integer learnerId = LearningWebUtil.getUserId();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	Object[] ret = learnerService.getStructuredActivityURLs(learnerId, lessonId);
	;
	request.setAttribute("progressList", ret[0]);
	request.setAttribute("currentActivityID", ret[1]);

	return mapping.findForward("displayProgress");
    }

    public ActionForward getPresenceChatActiveUserCount(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	int count = PresenceWebsocketServer.getActiveUserCount(lessonId);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(count);
	return null;
    }

    /**
     * Converts an activity in learner progress to a JSON object.
     */
    private JSONObject activityProgressToJSON(ActivityURL activity, Long currentActivityId, Long lessonId,
	    Integer learnerId, Integer monitorId) throws JSONException, IOException {
	JSONObject activityJSON = new JSONObject();
	activityJSON.put("id", activity.getActivityId());
	activityJSON.put("name", activity.getTitle());
	activityJSON.put("status", activity.getActivityId().equals(currentActivityId) ? 0 : activity.getStatus());

	// URL in learner mode
	String url = activity.getUrl();
	if ((url != null) && (monitorId != null)) {
	    // URL in monitor mode
	    url = Configuration.get(ConfigurationKeys.SERVER_URL)
		    + "monitoring/monitoring.do?method=getLearnerActivityURL&lessonID=" + lessonId + "&activityID="
		    + activity.getActivityId() + "&userID=" + learnerId;
	}

	if (url != null) {
	    if (url.startsWith("learner.do")) {
		url = "learning/" + url;
	    }
	    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	    if (!url.startsWith(serverUrl)) {
		// monitor mode URLs should be prepended with server URL
		url = serverUrl + url;
	    }
	    activityJSON.put("url", url);
	}

	String actType = activity.getType().toLowerCase();
	String type = "a";
	if (actType.contains("gate")) {
	    type = "g";
	} else if (actType.contains("options")) {
	    type = "o";
	} else if (actType.contains("branching")) {
	    type = "b";
	}
	activityJSON.put("type", type);

	if (activity.getChildActivities() != null) {
	    for (ActivityURL childActivity : activity.getChildActivities()) {
		activityJSON.append("childActivities",
			activityProgressToJSON(childActivity, currentActivityId, lessonId, learnerId, monitorId));
	    }
	}

	return activityJSON;
    }
}