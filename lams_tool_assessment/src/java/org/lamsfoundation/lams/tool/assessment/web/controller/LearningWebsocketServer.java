package org.lamsfoundation.lams.tool.assessment.web.controller;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.web.controller.AbstractTimeLimitWebsocketServer;
import org.lamsfoundation.lams.web.controller.AbstractTimeLimitWebsocketServer.EndpointConfigurator;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Controls Assessment time limits
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint(value = "/learningWebsocket", configurator = EndpointConfigurator.class)
public class LearningWebsocketServer extends AbstractTimeLimitWebsocketServer {

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private IAssessmentService assessmentService;

    public LearningWebsocketServer() {
	if (assessmentService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    assessmentService = (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
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
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	TimeCache existingTimeSettings = new TimeCache();

	existingTimeSettings.absoluteTimeLimit = assessment.getAbsoluteTimeLimit();
	existingTimeSettings.relativeTimeLimit = assessment.getRelativeTimeLimit() * 60;
	existingTimeSettings.timeLimitAdjustment = assessment.getTimeLimitAdjustments();

	for (Integer userId : userIds) {
	    AssessmentResult result = assessmentService.getLastAssessmentResult(assessment.getUid(),
		    userId.longValue());
	    if (result != null && result.getTimeLimitLaunchedDate() != null) {
		existingTimeSettings.timeLimitLaunchedDate.put(userId, result.getTimeLimitLaunchedDate());
	    }
	}

	return existingTimeSettings;
    }

    @Override
    protected LocalDateTime launchUserTimeLimit(long toolContentId, int userId) {
	return assessmentService.launchTimeLimit(toolContentId, userId);
    }

    public static LearningWebsocketServer getInstance() {
	LearningWebsocketServer result = (LearningWebsocketServer) AbstractTimeLimitWebsocketServer
		.getInstance(LearningWebsocketServer.class.getName());
	if (result == null) {
	    result = new LearningWebsocketServer();
	    AbstractTimeLimitWebsocketServer.registerInstance(result);
	}
	return result;
    }

    /**
     * Fetches or creates a singleton of this websocket server.
     */
    public static Long getSecondsLeft(long toolContentId, int userId) {
	LearningWebsocketServer instance = LearningWebsocketServer.getInstance();
	return AbstractTimeLimitWebsocketServer.getSecondsLeft(instance, toolContentId, userId, true);
    }
}