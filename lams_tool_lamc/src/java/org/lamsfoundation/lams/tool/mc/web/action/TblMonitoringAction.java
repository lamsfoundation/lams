package org.lamsfoundation.lams.tool.mc.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class TblMonitoringAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(TblMonitoringAction.class.getName());

    /**
     * Shows iRA page in case of MCQ activity
     */
    public ActionForward iraMcq(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(toolContentId);
	
	int attemptedLearnersNumber = 0;
	for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
	    attemptedLearnersNumber += session.getMcQueUsers().size();
	}
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	request.setAttribute("questions", mcContent.getMcQueContents());
	return mapping.findForward("mcq");
    }

    /**
     * Shows ira StudentChoices page
     */
    public ActionForward mcqStudentChoices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	
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

	ArrayList<McQuestionDTO> questionDtos = new ArrayList<McQuestionDTO>();
	for (McQueContent question : questions) {

	    // build candidate dtos
	    List<McOptionDTO> optionDtos = new LinkedList<McOptionDTO>();
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		int optionAttemptCount = mcService.getAttemptsCountPerOption(option.getUid());

		float percentage =  (float)(optionAttemptCount * 100) / totalNumberOfUsers;

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
	return mapping.findForward("mcqStudentChoices");
    }

    /**
     * Get ModalDialog for Teams tab.
     * 
     * @throws JSONException
     */
    public ActionForward getModalDialogForTeamsTab(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	
	McQueUsr user = mcService.getMcUserByContentId(userId, toolContentId);
	List<McUsrAttempt> finalizedUserAttempts = user == null ? new LinkedList<McUsrAttempt>()
		: mcService.getFinalizedUserAttempts(user);
	request.setAttribute("userAttempts", finalizedUserAttempts);
	
	return mapping.findForward("teams");
    }

}
