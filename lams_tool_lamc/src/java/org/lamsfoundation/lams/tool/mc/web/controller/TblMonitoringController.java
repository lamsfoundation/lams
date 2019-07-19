package org.lamsfoundation.lams.tool.mc.web.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tblmonitoring")
public class TblMonitoringController {

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
	for (McSession session : mcContent.getMcSessions()) {
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

	// ======================================================= Report by questionDescription IRA page
	// =======================================

	Set<McQueContent> questions = mcContent.getMcQueContents();
	int maxOptionsInQuestion = 0;
	for (McQueContent question : questions) {
	    if (question.getQbQuestion().getQbOptions().size() > maxOptionsInQuestion) {
		maxOptionsInQuestion = question.getQbQuestion().getQbOptions().size();
	    }
	}
	request.setAttribute("maxOptionsInQuestion", maxOptionsInQuestion);

	int totalNumberOfUsers = 0;
	for (McSession session : mcContent.getMcSessions()) {
	    totalNumberOfUsers += session.getMcQueUsers().size();
	}

	ArrayList<McQuestionDTO> questionDtos = new ArrayList<>();
	for (McQueContent question : questions) {

	    // build candidate dtos
	    List<McOptionDTO> optionDtos = new LinkedList<>();
	    for (QbOption option : question.getQbQuestion().getQbOptions()) {
		int optionAttemptCount = mcService.getAttemptsCountPerOption(option.getUid(), question.getUid());

		float percentage = (float) (optionAttemptCount * 100) / totalNumberOfUsers;

		McOptionDTO optionDTO = new McOptionDTO(option);
		optionDTO.setPercentage(percentage);
		optionDtos.add(optionDTO);
	    }

	    McQuestionDTO questionDto = new McQuestionDTO();
	    questionDto.setUid(question.getUid());
	    questionDto.setName(question.getName());
	    questionDto.setDescription(question.getDescription());
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
