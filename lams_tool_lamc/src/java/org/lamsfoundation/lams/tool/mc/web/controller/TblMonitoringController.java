package org.lamsfoundation.lams.tool.mc.web.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tblmonitoring")
public class TblMonitoringController {

    private static Logger logger = Logger.getLogger(TblMonitoringController.class.getName());

    @Autowired
    private IMcService mcService;

    /**
     * Shows iRA page in case of MCQ activity
     */
    @RequestMapping("/iraMcq")
    public String iraMcq(HttpServletRequest request) {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(toolContentId);

	int attemptedLearnersNumber = 0;
	for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
	    attemptedLearnersNumber += session.getMcQueUsers().size();
	}
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	request.setAttribute("questions", mcContent.getMcQueContents());
	return "tblmonitoring/mcq";
    }

    /**
     * Shows ira StudentChoices page
     */
    @RequestMapping("/mcqStudentChoices")
    public String mcqStudentChoices(HttpServletRequest request) {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(toolContentId);

	// ======================================================= Report by question IRA page
	// =======================================

	Set<McQueContent> questions = mcContent.getMcQueContents();
	int maxOptionsInQuestion = 0;
	for (McQueContent question : questions) {
	    if (question.getMcOptionsContents().size() > maxOptionsInQuestion) {
		maxOptionsInQuestion = question.getMcOptionsContents().size();
	    }
	}
	request.setAttribute("maxOptionsInQuestion", maxOptionsInQuestion);

	int totalNumberOfUsers = 0;
	for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
	    totalNumberOfUsers += session.getMcQueUsers().size();
	}

	ArrayList<McQuestionDTO> questionDtos = new ArrayList<>();
	for (McQueContent question : questions) {

	    // build candidate dtos
	    List<McOptionDTO> optionDtos = new LinkedList<>();
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		int optionAttemptCount = mcService.getAttemptsCountPerOption(option.getUid());

		float percentage = (float) (optionAttemptCount * 100) / totalNumberOfUsers;

		McOptionDTO optionDTO = new McOptionDTO(option);
		optionDTO.setPercentage(percentage);
		optionDtos.add(optionDTO);
	    }

	    McQuestionDTO questionDto = new McQuestionDTO();
	    questionDto.setUid(question.getUid());
	    questionDto.setQuestion(question.getQuestion());
	    questionDto.setOptionDtos(optionDtos);

	    questionDtos.add(questionDto);
	}
	request.setAttribute("questionDtos", questionDtos);

	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	return "tblmonitoring/mcqStudentChoices";
    }

    /**
     * Get ModalDialog for Teams tab.
     */
    @RequestMapping("/getModalDialogForTeamsTab")
    public String getModalDialogForTeamsTab(HttpServletRequest request) {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);

	McQueUsr user = mcService.getMcUserByContentId(userId, toolContentId);
	List<McUsrAttempt> finalizedUserAttempts = user == null ? new LinkedList<>()
		: mcService.getFinalizedUserAttempts(user);
	request.setAttribute("userAttempts", finalizedUserAttempts);

	return "tblmonitoring/teams";
    }

}
