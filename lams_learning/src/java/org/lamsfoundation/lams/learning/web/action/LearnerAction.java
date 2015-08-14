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
import java.io.PrintWriter;
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
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * <p>
 * The action servlet that interacts with learner to start a lams learner module, join a user to the lesson and allows a
 * user to exit a lesson.
 * </p>
 * 
 * <p>
 * It is also responsible for the interaction between lams server and flash. Flash will call method implemented in this
 * class to get progress data or trigger a lams server calculation here
 * </p>
 * 
 * <b>Note:</b>It needs to extend the <code>LamsDispatchAction</code> which has been customized to accomodate struts
 * features to solve duplicate submission problem.
 * 
 * @author Jacky Fang
 * @since 3/03/2005
 * @version 1.1
 * 
 *          ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/learner" parameter="method" validate="false"
 * @struts:action-forward name="displayActivity" path="/DisplayActivity.do"
 * @struts:action-forward name="displayProgress" path="/mobile/progress.jsp"
 * @struts:action-forward name="message" path=".message"
 */
public class LearnerAction extends LamsDispatchAction {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(LearnerAction.class);

    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------

    private static IAuditService auditService;

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
     * Handle an exception - either thrown by the service or by the web layer. Allows the exception to be logged
     * properly and ensure that an actual message goes back to Flash.
     * 
     * @param e
     * @param methodKey
     * @param learnerService
     * @return
     */
    protected FlashMessage handleException(Exception e, String methodKey, ICoreLearnerService learnerService) {
	LearnerAction.log.error("Exception thrown " + methodKey, e);
	String[] msg = new String[1];
	msg[0] = e.getMessage();

	getAuditService().log(LearnerAction.class.getName() + ":" + methodKey, e.toString());

	return new FlashMessage(methodKey, learnerService.getMessageService().getMessage("error.system.learner", msg),
		FlashMessage.CRITICAL_ERROR);
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
	    User user = (User) LearnerServiceProxy.getUserManagementService(getServlet().getServletContext()).findById(
		    User.class, learner);
	    if ((lesson.getLessonClass() == null) || !lesson.getLessonClass().getLearners().contains(user)) {
		request.setAttribute("messageKey", "User " + user.getLogin()
			+ " is not a learner in the requested lesson.");
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

	    ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet()
		    .getServletContext());
	    String url = "learning/" + activityMapping.getDisplayActivityAction(lessonID);

	    redirectToURL(mapping, response, url);

	} catch (Exception e) {
	    LearnerAction.log
		    .error("An error occurred while learner " + learner + " attempting to join the lesson.", e);
	    return mapping.findForward(ActivityMapping.ERROR);
	}

	return null;
    }

    /**
     * <p>
     * The structs dispatch action that joins a learner into a lesson. This call is used for a user to resume a lesson.
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
    public ActionForward resumeLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// initialize service object
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	FlashMessage message = null;
	try {

	    // get user and lesson based on request.
	    Integer learner = LearningWebUtil.getUserId();
	    long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("The learner [" + learner + "] is joining the lesson [" + lessonID + "]");
	    }

	    // join user to the lesson on the server
	    LearnerProgress learnerProgress = learnerService.joinLesson(learner, lessonID);

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("The learner [" + learner + "] joined lesson. The" + "porgress data is:"
			+ learnerProgress.toString());
	    }

	    LearningWebUtil.putLearnerProgressInRequest(request, learnerProgress);

	    // serialize a acknowledgement flash message with the path of display next
	    // activity

	    ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet()
		    .getServletContext());
	    message = new FlashMessage("joinLesson", activityMapping.getDisplayActivityAction(null));

	} catch (Exception e) {
	    message = handleException(e, "joinLesson", learnerService);
	}

	String wddxPacket = WDDXProcessor.serialize(message);
	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Sending Lesson joined acknowledge message to flash:" + wddxPacket);
	}

	// we hand over the control to flash.
	response.getWriter().print(wddxPacket);
	return null;
    }

    /**
     * <p>
     * Exit the current lesson that is running in the leaner window. It expects lesson id passed as parameter from flash
     * component.
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
    public ActionForward exitLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// initialize service object
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	FlashMessage message = null;
	try {
	    // get user and lesson based on request.
	    Integer learner = LearningWebUtil.getUserId();
	    Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("Exiting lesson, lesson id is: " + lessonID);
	    }

	    learnerService.exitLesson(learner, lessonID);

	    // send acknowledgment to flash as it is triggered by flash
	    message = new FlashMessage("exitLesson", true);

	} catch (Exception e) {
	    message = handleException(e, "exitLesson", learnerService);
	}

	String wddxPacket = WDDXProcessor.serialize(message);
	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Sending Exit Lesson acknowledge message to flash:" + wddxPacket);
	}
	response.getWriter().print(wddxPacket);
	return null;
    }

    /**
     * Gets the basic lesson details (name, descripton, etc) for a lesson. Contains a LessonDTO. Takes a single
     * parameter lessonID
     */
    public ActionForward getLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// initialize service object
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	FlashMessage message = null;
	try {

	    Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("get lesson..." + lessonID);
	    }

	    LessonDTO dto = learnerService.getLessonData(lessonID);

	    // send acknowledgment to flash as it is triggerred by flash
	    message = new FlashMessage("getLesson", dto);

	} catch (Exception e) {
	    message = handleException(e, "getLesson", learnerService);
	}

	String wddxPacket = WDDXProcessor.serialize(message);
	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Sending getLesson data message to flash:" + wddxPacket);
	}
	response.getWriter().print(wddxPacket);
	return null;
    }

    /**
     * <p>
     * The struts dispatch action to view the activity. This will be called by flash progress bar to check up the
     * activity component. The lams side will calculate the url and send a flash message back to the flash component.
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
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getLearnerActivityURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Getting url for learner activity...");
	}

	FlashMessage message = null;
	try {

	    // get the activity id and calculate the url for this activity.
	    long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    String url = getLearnerActivityURL(request, activityId);

	    // send data back to flash.
	    ProgressActivityDTO activityDTO = new ProgressActivityDTO(new Long(activityId), url);
	    message = new FlashMessage("getLearnerActivityURL", activityDTO);

	} catch (Exception e) {
	    message = handleException(e, "getLearnerActivityURL",
		    LearnerServiceProxy.getLearnerService(getServlet().getServletContext()));
	}

	String wddxPacket = WDDXProcessor.serialize(message);
	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Sending learner activity url data to flash:" + wddxPacket);
	}

	response.getWriter().print(wddxPacket);
	return null;
    }

    /**
     * @param request
     * @param activityId
     * @return
     */
    private String getLearnerActivityURL(HttpServletRequest request, long activityId) {
	// initialize service object
	ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	// getting requested object according to coming parameters
	Integer learnerId = LearningWebUtil.getUserId();
	User learner = (User) LearnerServiceProxy.getUserManagementService(getServlet().getServletContext()).findById(
		User.class, learnerId);

	Activity requestedActivity = learnerService.getActivity(new Long(activityId));
	Lesson lesson = learnerService.getLessonByActivity(requestedActivity);
	String url = activityMapping.calculateActivityURLForProgressView(lesson, learner, requestedActivity);
	return url;
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
     * Gets the same url as getLearnerActivityURL() but forwards directly to the url, rather than returning the url in a
     * Flash packet.
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

    /**
     * Forces a move to a destination Activity in the learning sequence, returning a WDDX packet
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
    public ActionForward forceMove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	FlashMessage flashMessage = null;
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	try {
	    String url;
	    Activity targetActivity = forceMoveShared(request, learnerService, lessonId);

	    if (!targetActivity.isFloating()) {
		url = activityMapping.getDisplayActivityAction(null);
	    } else {
		url = activityMapping.getActivityURL(targetActivity);
	    }

	    // TODO: update for moving to Floating Activity in Flash Learner
	    flashMessage = new FlashMessage("forceMove", activityMapping.getDisplayActivityAction(null));
	} catch (Exception e) {
	    flashMessage = handleException(e, "forceMove", learnerService);
	}

	PrintWriter writer = response.getWriter();
	writer.println(flashMessage.serializeMessage());
	return null;

    }

    /**
     * Forces a move to a destination Activity in the learning sequence, redirecting to the new page rather than
     * returning a WDDX packet.
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
    public ActionForward forceMoveRedirect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	try {
	    String url;
	    Activity targetActivity = forceMoveShared(request, learnerService, lessonId);

	    if (!targetActivity.isFloating()) {
		url = "/learning" + activityMapping.getDisplayActivityAction(lessonId);
	    } else {
		url = activityMapping.getActivityURL(targetActivity);
	    }

	    return redirectToURL(mapping, response, url);
	} catch (Exception e) {
	    LearnerAction.log.error("Exception throw doing force move", e);
	    throw new ServletException(e);
	}
    }

    /**
     * @param request
     * @return targetActivity The activity we are moving to.
     */
    private Activity forceMoveShared(HttpServletRequest request, ICoreLearnerService learnerService, Long lessonId) {

	// getting requested object according to coming parameters
	Integer learnerId = LearningWebUtil.getUserId();

	// get parameters
	Long fromActivityId = null;
	Long toActivityId = null;

	String fromActId = request.getParameter(AttributeNames.PARAM_CURRENT_ACTIVITY_ID);
	String toActId = request.getParameter(AttributeNames.PARAM_DEST_ACTIVITY_ID);
	if (fromActId != null) {
	    try {
		fromActivityId = new Long(Long.parseLong(fromActId));
	    } catch (Exception e) {
		fromActivityId = null;
	    }
	}

	if (toActId != null) {
	    try {
		toActivityId = new Long(Long.parseLong(toActId));
	    } catch (Exception e) {
		toActivityId = null;
	    }
	}

	Activity fromActivity = null;
	Activity toActivity = null;

	if (fromActivityId != null) {
	    fromActivity = learnerService.getActivity(fromActivityId);
	}

	if (toActivityId != null) {
	    toActivity = learnerService.getActivity(toActivityId);
	}

	learnerService.moveToActivity(learnerId, lessonId, fromActivity, toActivity);

	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Force move for learner " + learnerId + " lesson " + lessonId + ". ");
	}

	return toActivity;
    }

    /**
     * Get AuditService bean.
     * 
     * @return
     */
    private IAuditService getAuditService() {
	if (LearnerAction.auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    LearnerAction.auditService = (IAuditService) ctx.getBean("auditService");
	}
	return LearnerAction.auditService;
    }

    /**
     * Gets the same url as getLearnerActivityURL() but forwards directly to the url, rather than returning the url in a
     * Flash packet.
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
    public ActionForward forwardToLearnerActivityURL(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Forwarding to the url for learner activity..." + activityId);
	}

	String url = getLearnerActivityURL(request, activityId);
	return redirectToURL(mapping, response, url);
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