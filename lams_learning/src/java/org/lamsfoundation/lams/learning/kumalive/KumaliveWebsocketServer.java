package org.lamsfoundation.lams.learning.kumalive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumalivePoll;
import org.lamsfoundation.lams.learning.kumalive.model.KumalivePollAnswer;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.service.IKumaliveService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Processes messages for Kumalive.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/kumaliveWebsocket")
public class KumaliveWebsocketServer {

    private class KumaliveUser {
	private class KumalivePollVote {
	    private final Long pollId;
	    private final Date time;
	    private final int answer;

	    private KumalivePollVote(Long pollId, Date time, int answer) {
		this.pollId = pollId;
		this.time = time;
		this.answer = answer;
	    }
	}

	private final UserDTO userDTO;
	private final Session websocket;
	private final boolean isTeacher;
	private final boolean roleTeacher;
	private KumalivePollVote vote;

	private KumaliveUser(User user, Session websocket, boolean isTeacher, boolean roleTeacher) {
	    this.userDTO = user.getUserDTO();
	    this.websocket = websocket;
	    this.isTeacher = isTeacher;
	    this.roleTeacher = roleTeacher;
	}
    }

    private class KumaliveDTO {
	private Long id;
	private String name;
	private UserDTO createdBy;
	private boolean raiseHandPrompt;
	private final List<Integer> raisedHand = new CopyOnWriteArrayList<>();
	private Integer speaker;
	private final Map<String, KumaliveUser> learners = new ConcurrentHashMap<>();
	private final JSONArray rubrics = new JSONArray();
	private KumalivePollDTO poll;

	private KumaliveDTO(Kumalive kumalive) throws JSONException {
	    this.id = kumalive.getKumaliveId();
	    this.name = kumalive.getName();
	    this.createdBy = kumalive.getCreatedBy().getUserDTO();
	    for (KumaliveRubric rubric : kumalive.getRubrics()) {
		JSONObject rubricJSON = new JSONObject();
		rubricJSON.put("id", rubric.getRubricId());
		rubricJSON.put("name", rubric.getName());
		rubrics.put(rubricJSON);
	    }
	}
    }

    private class KumalivePollDTO {
	private final JSONObject pollJSON = new JSONObject();
	private final ArrayList<List<UserDTO>> voters = new ArrayList<List<UserDTO>>();
	private final JSONArray votersJSON = new JSONArray();
	private boolean votesShown = false;
	private boolean votersShown = false;
    }

    private static Logger logger = Logger.getLogger(KumaliveWebsocketServer.class);

    private static IKumaliveService kumaliveService;
    private static ISecurityService securityService;
    private static IUserManagementService userManagementService;
    // mapping org ID -> Kumalive
    private static final Map<Integer, KumaliveDTO> kumalives = new TreeMap<>();

    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	Integer userId = getUser(websocket).getUserId();
	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER }, "register on kumalive", false)) {
	    // prevent unauthorised user from accessing Kumalive
	    String warning = "User " + userId + " is not a monitor nor a learner of organisation " + organisationId;
	    logger.warn(warning);
	    websocket.close(new CloseReason(CloseCodes.CANNOT_ACCEPT, warning));
	}
    }

    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) throws JSONException, IOException {
	String login = websocket.getUserPrincipal().getName();
	if (login == null) {
	    return;
	}

	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	KumaliveDTO kumalive = kumalives.get(organisationId);
	if (kumalive == null) {
	    return;
	}
	KumaliveUser user = kumalive.learners.remove(login);
	if (user != null) {
	    Integer userId = user.userDTO.getUserID();
	    if (kumalive.raisedHand != null) {
		kumalive.raisedHand.remove(userId);
	    }
	    if (userId.equals(kumalive.speaker)) {
		kumalive.speaker = null;
	    }
	}

	sendRefresh(kumalive);
    }

    @OnMessage
    public void receiveRequest(String input, Session session) throws JSONException, IOException {
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    logger.warn("Kumalives are disabled");
	    return;
	}
	if (StringUtils.isBlank(input)) {
	    return;
	}
	if (input.equalsIgnoreCase("ping")) {
	    // just a ping every few minutes
	    return;
	}

	JSONObject requestJSON = new JSONObject(input);
	switch (requestJSON.getString("type")) {
	    case "start":
		start(requestJSON, session);
		break;
	    case "join":
		join(requestJSON, session);
		break;
	    case "raiseHandPrompt":
		raiseHandPrompt(requestJSON, session);
		break;
	    case "downHandPrompt":
		downHandPrompt(requestJSON, session);
		break;
	    case "raiseHand":
		raiseHand(requestJSON, session);
		break;
	    case "downHand":
		downHand(requestJSON, session);
		break;
	    case "speak":
		speak(requestJSON, session);
		break;
	    case "score":
		score(requestJSON, session);
		break;
	    case "startPoll":
		startPoll(requestJSON, session);
		break;
	    case "finishPoll":
		finishPoll(requestJSON, session);
		break;
	    case "closePoll":
		closePoll(requestJSON, session);
		break;
	    case "finish":
		finish(requestJSON, session);
		break;
	}
    }

    /**
     * Fetches an existing Kumalive, creates it or tells teacher to create it
     */
    private void start(JSONObject requestJSON, Session websocket) throws JSONException, IOException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	KumaliveDTO kumaliveDTO = kumalives.get(organisationId);
	boolean isTeacher = false;
	if (kumaliveDTO == null) {
	    String name = requestJSON.optString("name");
	    JSONArray rubricsJSON = requestJSON.optJSONArray("rubrics");
	    String role = websocket.getRequestParameterMap().get(AttributeNames.PARAM_ROLE).get(0);
	    User user = getUser(websocket);
	    Integer userId = user.getUserId();
	    isTeacher = !Role.LEARNER.equalsIgnoreCase(role) && (KumaliveWebsocketServer.getUserManagementService()
		    .isUserInRole(userId, organisationId, Role.GROUP_MANAGER)
		    || KumaliveWebsocketServer.getUserManagementService().isUserInRole(userId, organisationId,
			    Role.MONITOR));
	    // if it kumalive does not exists and the user is not a teacher or he did not provide a name yet,
	    // kumalive will not get created
	    Kumalive kumalive = KumaliveWebsocketServer.getKumaliveService().startKumalive(organisationId, userId, name,
		    rubricsJSON, isTeacher && StringUtils.isNotBlank(name));
	    if (kumalive != null) {
		kumaliveDTO = new KumaliveDTO(kumalive);
		kumalives.put(organisationId, kumaliveDTO);

		KumalivePoll poll = KumaliveWebsocketServer.getKumaliveService()
			.getPollByKumaliveId(kumalive.getKumaliveId());
		if (poll != null) {
		    KumaliveWebsocketServer.pollToJSON(kumaliveDTO, poll);
		}
	    }
	}

	// tell teacher to provide a name for Kumalive and create it
	// or tell learner to join
	JSONObject responseJSON = new JSONObject();
	if (kumaliveDTO == null && isTeacher) {
	    responseJSON.put("type", "create");
	    List<KumaliveRubric> rubrics = KumaliveWebsocketServer.getKumaliveService().getRubrics(organisationId);
	    if (!rubrics.isEmpty()) {
		JSONArray rubricsJSON = new JSONArray();
		for (KumaliveRubric rubric : rubrics) {
		    rubricsJSON.put(rubric.getName());
		}
		responseJSON.put("rubrics", rubricsJSON);
	    }

	    websocket.getBasicRemote().sendText(responseJSON.toString());
	} else {
	    websocket.getBasicRemote().sendText("{ \"type\" : \"join\" }");
	}
    }

    /**
     * Adds a learner or a teacher to Kumalive
     */
    private void join(JSONObject requestJSON, Session websocket) throws JSONException, IOException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	KumaliveDTO kumalive = kumalives.get(organisationId);
	if (kumalive == null) {
	    websocket.getBasicRemote().sendText("{ \"type\" : \"start\"}");
	    return;
	}

	User user = getUser(websocket);
	Integer userId = user.getUserId();
	String login = user.getLogin();
	boolean isTeacher = KumaliveWebsocketServer.getUserManagementService().isUserInRole(userId, organisationId,
		Role.GROUP_MANAGER)
		|| KumaliveWebsocketServer.getUserManagementService().isUserInRole(userId, organisationId,
			Role.MONITOR);
	String role = websocket.getRequestParameterMap().get(AttributeNames.PARAM_ROLE).get(0);

	KumaliveUser learner = kumalive.learners.get(login);
	boolean roleTeacher = isTeacher && !Role.LEARNER.equalsIgnoreCase(role)
		&& ("teacher".equalsIgnoreCase(role) || learner == null || learner.roleTeacher);
	if (learner != null && !learner.websocket.getId().equals(websocket.getId())) {
	    // only one websocket per user
	    learner.websocket.close(
		    new CloseReason(CloseCodes.NOT_CONSISTENT, "Another websocket for same user was estabilished"));
	}

	learner = new KumaliveUser(user, websocket, isTeacher, roleTeacher);
	kumalive.learners.put(login, learner);

	sendInit(kumalive, learner);
	sendRefresh(kumalive);
    }

    private void sendInit(KumaliveDTO kumalive, KumaliveUser user) throws JSONException, IOException {
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("type", "init");
	// Kumalive title
	responseJSON.put("name", kumalive.name);
	responseJSON.put("isTeacher", user.isTeacher);
	responseJSON.put("roleTeacher", user.roleTeacher);
	// teacher details
	responseJSON.put("teacherId", kumalive.createdBy.getUserID());
	responseJSON.put("teacherName", kumalive.createdBy.getFirstName() + " " + kumalive.createdBy.getLastName());
	responseJSON.put("teacherPortraitUuid", kumalive.createdBy.getPortraitUuid());

	// rubric details
	responseJSON.put("rubrics", kumalive.rubrics);

	user.websocket.getBasicRemote().sendText(responseJSON.toString());
    }

    /**
     * Send full Kumalive state to all learners and teachers
     */
    private void sendRefresh(KumaliveDTO kumalive) throws JSONException, IOException {
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("type", "refresh");

	// current state of question and speaker
	responseJSON.put("raiseHandPrompt", kumalive.raiseHandPrompt);
	if (!kumalive.raisedHand.isEmpty()) {
	    responseJSON.put("raisedHand", new JSONArray(kumalive.raisedHand));
	}
	responseJSON.put("speaker", kumalive.speaker);

	// each learner's details
	JSONArray learnersJSON = new JSONArray();
	JSONObject loginsJSON = new JSONObject();
	for (KumaliveUser participant : kumalive.learners.values()) {
	    UserDTO participantDTO = participant.userDTO;

	    JSONObject participantJSON = KumaliveWebsocketServer.participantToJSON(participantDTO,
		    participant.roleTeacher);
	    loginsJSON.put("user" + participantDTO.getUserID(), participantDTO.getLogin());

	    learnersJSON.put(participantJSON);
	}
	responseJSON.put("learners", learnersJSON);

	JSONArray pollVotesJSON = null;
	// is there a poll running?
	if (kumalive.poll != null) {
	    // build votes count JSON
	    pollVotesJSON = new JSONArray();
	    for (int answerIndex = 0; answerIndex < kumalive.poll.voters.size(); answerIndex++) {
		List<UserDTO> voters = kumalive.poll.voters.get(answerIndex);
		// update voters JSON: add only new voters
		synchronized (voters) {
		    pollVotesJSON.put(voters.size());
		    JSONArray votersJSON = kumalive.poll.votersJSON.getJSONArray(answerIndex);
		    for (int voterIndex = votersJSON.length(); voterIndex < voters.size(); voterIndex++) {
			votersJSON.put(KumaliveWebsocketServer.participantToJSON(voters.get(voterIndex), null));
		    }
		}
	    }
	    // put them in response only if teacher released them
	    kumalive.poll.pollJSON.put("votes", kumalive.poll.votesShown ? pollVotesJSON : null);
	    kumalive.poll.pollJSON.put("voters", kumalive.poll.votersShown ? kumalive.poll.voters : null);
	    responseJSON.put("poll", kumalive.poll.pollJSON);
	}

	// send refresh to everyone
	Long pollId = kumalive.poll == null ? null : kumalive.poll.pollJSON.getLong("id");
	String teacherResponse = null;
	String learnerResponse = responseJSON.toString();
	for (KumaliveUser participant : kumalive.learners.values()) {
	    Basic channel = participant.websocket.getBasicRemote();
	    if (participant.isTeacher) {
		// send extra information to teachers
		if (teacherResponse == null) {
		    JSONObject teacherResponseJSON = new JSONObject(learnerResponse);
		    teacherResponseJSON.put("logins", loginsJSON);
		    // add poll votes and voters, if they were not already released and added for all users
		    if (kumalive.poll != null) {
			if (!kumalive.poll.votesShown) {
			    teacherResponseJSON.getJSONObject("poll").put("votes", pollVotesJSON);
			}
			if (!kumalive.poll.votersShown) {
			    teacherResponseJSON.getJSONObject("poll").put("voters", kumalive.poll.votersJSON);
			}
		    }
		    teacherResponse = teacherResponseJSON.toString();
		}
		channel.sendText(teacherResponse);
	    } else if (kumalive.poll == null || kumalive.poll.pollJSON.getBoolean("finished")) {
		channel.sendText(learnerResponse);
	    } else {
		// mark this learner as voted
		responseJSON.getJSONObject("poll").put("voted",
			participant.vote != null && pollId != null && participant.vote.pollId.equals(pollId));
		channel.sendText(responseJSON.toString());
	    }
	}
    }

    /**
     * Tell learners that the teacher asked a question
     */
    private void raiseHandPrompt(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive raise hand prompt", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	kumalive.raiseHandPrompt = true;
	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " asked a question in Kumalive " + kumalive.id);
	}
	sendRefresh(kumalive);
    }

    /**
     * Tell learners that the teacher finished a question
     */
    private void downHandPrompt(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive down hand prompt", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	kumalive.raiseHandPrompt = false;
	kumalive.raisedHand.clear();
	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " finished a question in Kumalive " + kumalive.id);
	}

	sendRefresh(kumalive);
    }

    /**
     * Tell learners that a learner raised hand
     */
    private void raiseHand(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER }, "kumalive raise hand", false)) {
	    String warning = "User " + userId + " is not a monitor nor a learner of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	if (!kumalive.raiseHandPrompt) {
	    logger.warn("Raise hand prompt was not sent by teacher yet for organisation " + organisationId);
	    return;
	}

	if (kumalive.raisedHand.contains(userId)) {
	    return;
	}

	kumalive.raisedHand.add(userId);
	if (logger.isDebugEnabled()) {
	    logger.debug("Learner " + userId + " raised hand in Kumalive " + kumalive.id);
	}

	sendRefresh(kumalive);
    }

    /**
     * Tell learners that a learner put hadn down
     */
    private void downHand(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER }, "kumalive down hand", false)) {
	    String warning = "User " + userId + " is not a monitor nor a learner of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	if (kumalive.raisedHand == null) {
	    return;
	}

	kumalive.raisedHand.remove(userId);
	if (logger.isDebugEnabled()) {
	    logger.debug("Learner " + userId + " put hand down in Kumalive " + kumalive.id);
	}

	sendRefresh(kumalive);
    }

    /**
     * Set up a speaker or remove him
     */
    private void speak(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive speak", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	kumalive.speaker = requestJSON.optInt("speaker");
	sendRefresh(kumalive);
    }

    /**
     * Save score for a learner
     */
    private void score(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive score", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}
	Long rubricId = requestJSON.getLong("rubricId");
	Integer learnerId = requestJSON.getInt(AttributeNames.PARAM_USER_ID);
	KumaliveWebsocketServer.getKumaliveService().scoreKumalive(rubricId, learnerId,
		Long.valueOf(requestJSON.getString("batch")), Short.valueOf(requestJSON.getString("score")));

	KumaliveDTO kumalive = kumalives.get(organisationId);
	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " marked rubric " + rubricId + " for learner " + learnerId
		    + " in Kumalive " + kumalive.id);
	}

	sendRefresh(kumalive);
    }

    /**
     * Tell learners that the teacher started a poll
     */
    private void startPoll(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive poll start", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	KumalivePoll existingPoll = KumaliveWebsocketServer.getKumaliveService().getPollByKumaliveId(kumalive.id);
	if (existingPoll != null) {
	    String warning = "User " + userId + " tried to start a poll in organisation " + organisationId
		    + " but there is already a running one with ID " + existingPoll.getPollId();
	    logger.warn(warning);
	    return;
	}

	JSONObject pollJSON = requestJSON.getJSONObject("poll");
	KumalivePoll poll = KumaliveWebsocketServer.getKumaliveService().startPoll(kumalive.id,
		pollJSON.getString("name"), pollJSON.getJSONArray("answers"));
	if (poll != null) {
	    KumaliveWebsocketServer.pollToJSON(kumalive, poll);
	}
	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " started poll " + poll.getPollId() + " in Kumalive " + kumalive.id);
	}
	sendRefresh(kumalive);
    }

    /**
     * Tell learners that the teacher started a poll
     */
    private void finishPoll(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive poll start", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	Long pollId = JsonUtil.optLong(requestJSON, "pollId");
	KumaliveWebsocketServer.getKumaliveService().finishPoll(pollId);

	KumaliveDTO kumalive = kumalives.get(organisationId);
	kumalive.poll.pollJSON.put("finished", true);

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " finished poll " + pollId + " in Kumalive " + kumalive.id);
	}
	sendRefresh(kumalive);
    }

    private void closePoll(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive poll start", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	kumalive.poll = null;

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " closed poll in Kumalive " + kumalive.id);
	}
	sendRefresh(kumalive);
    }

    /**
     * End Kumalive
     */
    private void finish(JSONObject requestJSON, Session websocket) throws IOException, JSONException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));

	User user = getUser(websocket);
	Integer userId = user.getUserId();

	if (!KumaliveWebsocketServer.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive finish", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    logger.warn(warning);
	    return;
	}

	KumaliveDTO kumalive = kumalives.get(organisationId);
	KumaliveWebsocketServer.getKumaliveService().finishKumalive(kumalive.id);
	kumalives.remove(organisationId);
	for (KumaliveUser participant : kumalive.learners.values()) {
	    participant.websocket.getBasicRemote().sendText("{ \"type\" : \"finish\"}");
	}

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " finished Kumalive " + kumalive.id);
	}
    }

    private User getUser(Session websocket) {
	return KumaliveWebsocketServer.getUserManagementService()
		.getUserByLogin(websocket.getUserPrincipal().getName());
    }

    private static JSONObject participantToJSON(UserDTO participantDTO, Boolean isTeacher) throws JSONException {
	JSONObject participantJSON = new JSONObject();
	participantJSON.put("id", participantDTO.getUserID());
	participantJSON.put("firstName", participantDTO.getFirstName());
	participantJSON.put("lastName", participantDTO.getLastName());
	participantJSON.put("portraitUuid", participantDTO.getPortraitUuid());
	if (isTeacher != null) {
	    participantJSON.put("roleTeacher", isTeacher);
	}
	return participantJSON;
    }

    private static void pollToJSON(KumaliveDTO kumaliveDTO, KumalivePoll poll) throws JSONException {
	kumaliveDTO.poll.pollJSON.put("id", poll.getPollId());
	kumaliveDTO.poll.pollJSON.put("name", poll.getName());
	JSONArray answersJSON = new JSONArray();
	for (KumalivePollAnswer answer : poll.getAnswers()) {
	    answersJSON.put(answer.getName());
	    kumaliveDTO.poll.voters.add(Collections.synchronizedList(new LinkedList<UserDTO>()));
	    kumaliveDTO.poll.votersJSON.put(new JSONArray());
	}
	kumaliveDTO.poll.pollJSON.put("answers", answersJSON);
    }

    private static IKumaliveService getKumaliveService() {
	if (kumaliveService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    kumaliveService = (IKumaliveService) ctx.getBean("kumaliveService");
	}
	return kumaliveService;
    }

    private static ISecurityService getSecurityService() {
	if (securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return securityService;
    }

    private static IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }
}