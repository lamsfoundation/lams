package org.lamsfoundation.lams.learning.discussion.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;
import org.lamsfoundation.lams.learning.discussion.service.IDiscussionSentimentService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/discussionSentiment")
public class DiscussionSentimentController {
    @Autowired
    private IDiscussionSentimentService discussionSentimentService;

    @Autowired
    private ILessonService lessonService;

    /**
     * Called via Page.tag.
     * Checks if there is a discussion running. If so, send the learner a command to show widget.
     */
    @RequestMapping("/checkLearner")
    @ResponseBody
    public void checkLearnerWidget(@RequestParam long lessonId, HttpServletResponse response) throws IOException {
	UserDTO user = DiscussionSentimentController.getCurrentUserDto();
	if (user == null) {
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	    return;
	}

	discussionSentimentService.startDiscussionForLearner(lessonId, user.getLogin());
    }

    /**
     * Called via Page.tag.
     * Sends a simple JSP page with JavaScript code to initialise the widget
     */
    @RequestMapping("/startLearner")
    public String startLearnerWidget(@RequestParam long lessonId, HttpServletResponse response, Model model)
	    throws IOException {
	UserDTO user = DiscussionSentimentController.getCurrentUserDto();
	if (user == null) {
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	    return null;
	}

	// tell learner what he already selected
	Integer selectedOption = discussionSentimentService.getDiscussionVoteSelectedOption(lessonId, user.getUserID());
	if (selectedOption != null) {
	    model.addAttribute("selectedOption", selectedOption);
	}
	return "/discussion/startLearner";
    }

    /**
     * Called via Page.tag.
     * Sends a simple JSP page with JavaScript code to remove the widget
     */
    @RequestMapping("/stopLearner")
    public String stopLearnerWidget() {
	return "/discussion/stopLearner";
    }

    @RequestMapping(path = "/vote", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> learnerVote(@RequestParam long lessonId, @RequestParam int selectedOption) {
	UserDTO user = DiscussionSentimentController.getCurrentUserDto();
	if (user == null) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	// it should always return true as there should be no widget displayed if no discussion is active
	boolean discussionActive = discussionSentimentService.addDiscussionVoteForLearner(lessonId, user.getUserID(),
		selectedOption);
	return ResponseEntity.ok(discussionActive ? "voted" : "stop");
    }

    /**
     * Sends discussion IDs (question UID, burning question UID)
     * where there is any data (active discussion, inactive discussion where someone voted).
     * It is used to reinitialised discussion widgets on monitoring screen.
     */
    @RequestMapping("/checkMonitor")
    @ResponseBody
    public ResponseEntity<String> checkMonitorWidget(@RequestParam long toolContentId) {
	Set<DiscussionSentimentVote> tokens = discussionSentimentService.getDiscussions(toolContentId);

	ArrayNode result = JsonNodeFactory.instance.arrayNode();
	for (DiscussionSentimentVote token : tokens) {
	    ObjectNode tokenJSON = JsonNodeFactory.instance.objectNode();
	    tokenJSON.put("toolQuestionUid", token.getToolQuestionUid());
	    tokenJSON.put("burningQuestionUid", token.getBurningQuestionUid());
	    result.add(tokenJSON);
	}

	return ResponseEntity.ok(result.toString());
    }

    /**
     * Fetches details to build a monitor widget.
     * If markAsActive is true, the monitor clicked "Start Discussion" or "Restart" button.
     * If markAsActive is false, this is monitor widget reinitialising after page load.
     */
    @RequestMapping("/startMonitor")
    public String startMonitorWidget(@RequestParam long toolQuestionUid,
	    @RequestParam(required = false) Long burningQuestionUid, @RequestParam boolean markAsActive, Model model) {
	Long lessonId = null;
	if (markAsActive) {
	    lessonId = discussionSentimentService.startDiscussionForMonitor(toolQuestionUid, burningQuestionUid);
	} else {
	    lessonId = discussionSentimentService.getLessonIdByQuestion(toolQuestionUid);
	}

	int learnerCount = lessonService.getCountLessonLearners(lessonId, null);
	model.addAttribute("learnerCount", learnerCount);
	return "/discussion/monitor";
    }

    @RequestMapping(path = "/stopMonitor", method = RequestMethod.POST)
    @ResponseBody
    public void stopMonitorWidget(@RequestParam long toolQuestionUid) {
	discussionSentimentService.stopDiscussionForMonitor(toolQuestionUid);
    }

    /**
     * Periodic refresh of discussion chart.
     */
    @RequestMapping("/getMonitorData")
    @ResponseBody
    public ResponseEntity<String> getMonitorData(@RequestParam long toolQuestionUid,
	    @RequestParam(required = false) Long burningQuestionUid) throws IOException {
	Map<Integer, Long> votes = discussionSentimentService.getDiscussionAggregatedVotes(toolQuestionUid,
		burningQuestionUid);

	DiscussionSentimentVote activeDiscussionToken = discussionSentimentService
		.getActiveDiscussionByQuestion(toolQuestionUid);
	ObjectNode response = JsonNodeFactory.instance.objectNode();
	// check if this discussion is active, so the widget knows if periodic refresh should keep running
	response.put("isActive",
		activeDiscussionToken != null && activeDiscussionToken.getToolQuestionUid().equals(toolQuestionUid)
			&& (activeDiscussionToken.getBurningQuestionUid() == null ? burningQuestionUid == null
				: activeDiscussionToken.getBurningQuestionUid().equals(burningQuestionUid)));
	response.set("votes", JsonUtil.readObject(votes));

	return ResponseEntity.ok(response.toString());
    }

    private static UserDTO getCurrentUserDto() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}