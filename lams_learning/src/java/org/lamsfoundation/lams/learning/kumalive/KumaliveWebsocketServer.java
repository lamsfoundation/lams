package org.lamsfoundation.lams.learning.kumalive;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Marcin Cieslak
 */
@ServerEndpoint("/kumaliveWebsocket")
public class KumaliveWebsocketServer {

    private class KumaliveLearner {
	private UserDTO userDTO;
	private Session websocket;

	private KumaliveLearner(User user, Session websocket) {
	    this.userDTO = user.getUserDTO();
	    this.websocket = websocket;
	}
    }

    private class KumaliveDTO {
	private Long id;
	private String name;
	private UserDTO createdBy;
	private final Map<String, KumaliveLearner> learners = new ConcurrentHashMap<>();

	private KumaliveDTO(Long id, String name, UserDTO createdBy) {
	    this.id = id;
	    this.name = name;
	    this.createdBy = createdBy;
	}
    }

    private static Logger log = Logger.getLogger(KumaliveWebsocketServer.class);

    private static ILearnerService learnerService;

    private static ISecurityService securityService;

    private static IUserManagementService userManagementService;

    private static final Map<Integer, KumaliveDTO> kumalives = new TreeMap<>();

    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	Integer userId = getUser(websocket).getUserId();
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER }, "join kumalive", false)) {
	    String warning = "User " + userId + " is not a monitor nor a learner of organisation " + organisationId;
	    log.warn(warning);
	    websocket.close(new CloseReason(CloseCodes.CANNOT_ACCEPT, warning));
	}
    }

    /**
     * Removes Learner websocket from the collection.
     */
    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) {
	String login = websocket.getUserPrincipal().getName();
	if (login == null) {
	    return;
	}

	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	KumaliveDTO kumalive = KumaliveWebsocketServer.kumalives.get(organisationId);
	if (kumalive == null) {
	    return;
	}

	kumalive.learners.remove(login);
    }

    @OnMessage
    public void receiveRequest(String input, Session session) throws JSONException, IOException {
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
		startKumalive(requestJSON, session);
		break;
	    case "join":
		joinKumalive(requestJSON, session);
		break;
	}
    }

    private void startKumalive(JSONObject requestJSON, Session websocket) throws JSONException, IOException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	String name = requestJSON.getString("name");
	User user = getUser(websocket);
	Long kumaliveId = KumaliveWebsocketServer.getLearnerService().startKumalive(organisationId, user.getUserId(),
		name);
	kumalives.put(organisationId, new KumaliveDTO(kumaliveId, name, user.getUserDTO()));

	websocket.getBasicRemote().sendText("{ \"type\" : \"join\" }");
    }

    private void joinKumalive(JSONObject requestJSON, Session websocket) throws JSONException, IOException {
	Integer organisationId = Integer
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_ORGANISATION_ID).get(0));
	KumaliveDTO kumalive = kumalives.get(organisationId);
	if (kumalive == null) {
	    websocket.getBasicRemote().sendText("{ \"type\" : \"start\"}");
	    return;
	}

	User user = getUser(websocket);
	Integer userId = user.getUserId();
	boolean isMonitor = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.MONITOR);

	Map<String, KumaliveLearner> learners = kumalive.learners;
	String login = user.getLogin();
	if (learners.containsKey(login)) {
	    KumaliveLearner learner = learners.get(login);
	    learner.websocket.close(
		    new CloseReason(CloseCodes.NOT_CONSISTENT, "Another websocket for same user was estabilished"));
	}
	KumaliveLearner learner = new KumaliveLearner(user, websocket);
	learners.put(login, learner);

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("type", "refresh");
	responseJSON.put("name", kumalive.name);
	responseJSON.put("teacherId", kumalive.createdBy.getUserID());
	responseJSON.put("teacherName", kumalive.createdBy.getFirstName() + " " + kumalive.createdBy.getLastName());
	responseJSON.put("teacherPortraitUuid", kumalive.createdBy.getPortraitUuid());
	
	if (isMonitor) {
	    responseJSON.put("isTeacher", true);
	}
	
	JSONArray learnersJSON = new JSONArray();
	for (KumaliveLearner participant : learners.values()) {
	    JSONObject learnerJSON = new JSONObject();
	    UserDTO participantDTO = participant.userDTO;
	    learnerJSON.put("id", participantDTO.getUserID());
	    learnerJSON.put("firstName", participantDTO.getFirstName());
	    learnerJSON.put("lastName", participantDTO.getLastName());
	    learnerJSON.put("portraitUuid", participantDTO.getPortraitUuid());
	    if (isMonitor) {
		learnerJSON.put("login", participantDTO.getLogin());
	    }
	    learnersJSON.put(learnerJSON);
	}
	responseJSON.put("learners", learnersJSON);

	for (KumaliveLearner participant : learners.values()) {
	    participant.websocket.getBasicRemote().sendText(responseJSON.toString());
	}
    }

    private User getUser(Session websocket) {
	return getUserManagementService().getUserByLogin(websocket.getUserPrincipal().getName());
    }

    private static ILearnerService getLearnerService() {
	if (learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    learnerService = (ILearnerService) ctx.getBean("learnerService");
	}
	return learnerService;
    }

    private ISecurityService getSecurityService() {
	if (securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return securityService;
    }

    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }
}