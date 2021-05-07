package org.lamsfoundation.lams.learning.discussion.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learning.discussion.service.IDiscussionSentimentService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
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

@Controller
@RequestMapping("/discussionSentiment")
public class DiscussionSentimentController {
    @Autowired
    private IDiscussionSentimentService discussionSentimentService;

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

    @RequestMapping(path = "/startMonitor", method = RequestMethod.POST)
    @ResponseBody
    public void startMonitorWidget(@RequestParam long toolQuestionUid,
	    @RequestParam(required = false) Long burningQuestionUid) {
	discussionSentimentService.startDiscussionForMonitor(toolQuestionUid, burningQuestionUid);
    }

    @RequestMapping(path = "/stopMonitor", method = RequestMethod.POST)
    @ResponseBody
    public void stopMonitorWidget(@RequestParam long toolQuestionUid) {
	discussionSentimentService.stopDiscussionForMonitor(toolQuestionUid);
    }

    private static UserDTO getCurrentUserDto() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}