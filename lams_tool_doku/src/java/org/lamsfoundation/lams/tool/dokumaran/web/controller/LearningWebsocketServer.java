package org.lamsfoundation.lams.tool.dokumaran.web.controller;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.web.controller.AbstractTimeLimitWebsocketServer;
import org.lamsfoundation.lams.web.controller.AbstractTimeLimitWebsocketServer.EndpointConfigurator;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Controls Dokumaran time limits
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint(value = "/learningWebsocket", configurator = EndpointConfigurator.class)
public class LearningWebsocketServer extends AbstractTimeLimitWebsocketServer {

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private IDokumaranService dokumaranService;

    public LearningWebsocketServer() {
	if (dokumaranService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    dokumaranService = (IDokumaranService) wac.getBean(DokumaranConstants.DOKUMARAN_SERVICE);
	}
    }

    @Override
    protected Logger getLog() {
	return LearningWebsocketServer.log;
    }

    /**
     * Gets settings from DB.
     */
    @Override
    protected TimeCache getExistingTimeSettings(long toolContentId, Collection<Integer> userIds) {
	Dokumaran dokumaran = dokumaranService.getDokumaranByContentId(toolContentId);
	TimeCache existingTimeSettings = new TimeCache();

	existingTimeSettings.absoluteTimeLimit = dokumaran.getAbsoluteTimeLimit();
	existingTimeSettings.relativeTimeLimit = dokumaran.getRelativeTimeLimit() * 60;
	existingTimeSettings.timeLimitAdjustment = dokumaran.getTimeLimitAdjustments();

	for (Integer userId : userIds) {

	    DokumaranUser user = dokumaranService.getLearnerByIDAndContent(userId.longValue(), toolContentId);
	    if (dokumaran.isUseSelectLeaderToolOuput()) {
		// if team leader is enabled, show consistent timer for all group members
		user = user.getSession().getGroupLeader();
	    }

	    if (user != null && user.getTimeLimitLaunchedDate() != null) {
		existingTimeSettings.timeLimitLaunchedDate.put(userId, user.getTimeLimitLaunchedDate());
	    }
	}

	return existingTimeSettings;
    }

    @Override
    protected LocalDateTime launchUserTimeLimit(long toolContentId, int userId) {
	return dokumaranService.launchTimeLimit(toolContentId, userId);
    }

    /**
     * Fetches or creates a singleton of this websocket server.
     */
    public static LearningWebsocketServer getInstance() {
	LearningWebsocketServer result = (LearningWebsocketServer) AbstractTimeLimitWebsocketServer
		.getInstance(LearningWebsocketServer.class.getName());
	if (result == null) {
	    result = new LearningWebsocketServer();
	    AbstractTimeLimitWebsocketServer.registerInstance(result);
	}
	return result;
    }

    public static Long getSecondsLeft(long toolContentId, int userId) {
	LearningWebsocketServer instance = LearningWebsocketServer.getInstance();
	return AbstractTimeLimitWebsocketServer.getSecondsLeft(instance, toolContentId, userId, true);
    }
}