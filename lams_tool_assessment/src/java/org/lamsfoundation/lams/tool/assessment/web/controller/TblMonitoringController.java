package org.lamsfoundation.lams.tool.assessment.web.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionResultDTO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentServiceImpl;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tblmonitoring")
public class TblMonitoringController {

    @Autowired
    @Qualifier("laasseAssessmentService")
    private IAssessmentService assessmentService;

    /**
     * Shows ira page in case of Assessment activity
     */
    @RequestMapping("iraAssessment")
    public String iraAssessment(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    Model model) {
	model.addAttribute("isIraAssessment", true);
	return assessment(toolContentId, model);
    }

    /**
     * Shows ira page in case of Assessment activity
     */
    @RequestMapping("iraAssessmentStudentChoices")
    public String iraAssessmentStudentChoices(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);

	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	request.setAttribute("groupsInAnsweredQuestionsChart", assessment.isUseSelectLeaderToolOuput());
	request.setAttribute("assessment", assessment);

	return "pages/tblmonitoring/iraAssessmentStudentChoices";
    }

    @RequestMapping("iraAssessmentStudentChoicesTable")
    public String iraAssessmentStudentChoicesTable(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);

	//prepare list of the questions, filtering out questions that aren't supposed to be answered
	Set<AssessmentQuestion> questionList = new LinkedHashSet<>();
	//in case there is at least one random question - we need to show all questions in a drop down select
	if (assessment.hasRandomQuestion()) {
	    questionList.addAll(assessment.getQuestions());

	    //show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : assessment.getQuestionReferences()) {
		questionList.add(reference.getQuestion());
	    }
	}
	//keep only MCQ type of questions
	Set<QuestionDTO> questionDtos = new LinkedHashSet<>();
	int maxOptionsInQuestion = 0;
	int displayOrder = 1;
	boolean vsaPresent = false;
	for (AssessmentQuestion question : questionList) {
	    if (QbQuestion.TYPE_MULTIPLE_CHOICE == question.getType()
		    || QbQuestion.TYPE_VERY_SHORT_ANSWERS == question.getType()) {
		questionDtos.add(new QuestionDTO(question, displayOrder++));

		//calculate maxOptionsInQuestion
		if (question.getQbQuestion().getQbOptions().size() > maxOptionsInQuestion) {
		    maxOptionsInQuestion = question.getQbQuestion().getQbOptions().size();
		}

		if (QbQuestion.TYPE_VERY_SHORT_ANSWERS == question.getType()) {
		    vsaPresent = true;
		}
	    }
	}
	request.setAttribute("maxOptionsInQuestion", maxOptionsInQuestion);
	request.setAttribute("vsaPresent", vsaPresent);

	int totalNumberOfUsers = assessmentService.getCountUsersByContentId(toolContentId);
	if (totalNumberOfUsers > 0) {
	    for (QuestionDTO questionDto : questionDtos) {

		// build candidate dtos
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    int optionAttemptCount = assessmentService.countAttemptsPerOption(toolContentId, optionDto.getUid(),
			    false);

		    float percentage = (float) (optionAttemptCount * 100) / totalNumberOfUsers;
		    optionDto.setPercentage(percentage);
		}
	    }
	}
	request.setAttribute("questions", questionDtos);

	return "pages/monitoring/parts/mcqStudentChoices";
    }

    private List<TblAssessmentDTO> getAssessmentDtos(String[] toolContentIds, String[] activityTitles) {
	List<TblAssessmentDTO> assessmentDtos = new ArrayList<>();
	int i = 0;
	for (String toolContentIdStr : toolContentIds) {
	    String activityTitle = activityTitles[i++];

	    //skip empty contentIds
	    if (toolContentIdStr.length() == 0) {
		continue;
	    }
	    Long toolContentId = Long.parseLong(toolContentIdStr);

	    TblAssessmentDTO assessmentDto = new TblAssessmentDTO();

	    int attemptedLearnersNumber = assessmentService.getCountUsersByContentId(toolContentId);
	    assessmentDto.setAttemptedLearnersNumber(attemptedLearnersNumber);
	    assessmentDto.setActivityTitle(activityTitle);

	    Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	    assessmentDto.setAssessment(assessment);
	    assessmentDto.setQuestions(prepareQuestionsList(assessment));
	    assessmentDtos.add(assessmentDto);
	}

	return assessmentDtos;
    }

    private Set<AssessmentQuestion> prepareQuestionsList(Assessment assessment) {
	// question list to display
	Set<AssessmentQuestion> questions = new TreeSet<>();
	boolean hasRandomQuestion = false;
	for (QuestionReference reference : assessment.getQuestionReferences()) {
	    hasRandomQuestion |= reference.isRandomQuestion();
	}
	// in case there is at least one random question - we need to show all questions
	if (hasRandomQuestion) {
	    questions.addAll(assessment.getQuestions());

	    // show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : assessment.getQuestionReferences()) {
		//sort questions the same way references are sorted (as per LKC request)
		AssessmentQuestion question = reference.getQuestion();
		question.setDisplayOrder(reference.getSequenceId());
		questions.add(question);
	    }
	}
	return questions;
    }

    /**
     * Shows assessment (iRAT or AE) page
     */
    @RequestMapping("assessment")
    public String assessment(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    Model model) {
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	model.addAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	model.addAttribute("allowDiscloseAnswers", assessment.isAllowDiscloseAnswers());

	int attemptedLearnersNumber = assessmentService.getCountUsersByContentId(toolContentId);
	model.addAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	return "pages/tblmonitoring/assessment";
    }

    /**
     * Shows ira StudentChoices page
     */
    @RequestMapping("aesStudentChoicesTable")
    public String aesStudentChoicesTable(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	Map<Long, QuestionSummary> questionSummaries = assessmentService.getQuestionSummaryForExport(assessment, false);
	List<TblAssessmentQuestionDTO> tblQuestionDtos = new ArrayList<>();
	for (QuestionSummary questionSummary : questionSummaries.values()) {
	    QuestionDTO questionDto = questionSummary.getQuestionDto();

	    TblAssessmentQuestionDTO tblQuestionDto = new TblAssessmentQuestionDTO();

	    List<TblAssessmentQuestionResultDTO> sessionQuestionResults = new ArrayList<>();
	    for (List<AssessmentQuestionResult> questionResultsPerSession : questionSummary
		    .getQuestionResultsPerSession()) {

		TblAssessmentQuestionResultDTO tblQuestionResultDto = new TblAssessmentQuestionResultDTO();
		String answer = "";
		boolean correct = false;
		if (!questionResultsPerSession.isEmpty()) {
		    AssessmentQuestionResult questionResult = questionResultsPerSession.get(0);
		    answer = AssessmentEscapeUtils.printResponsesForJqgrid(questionResult);

		    if (StringUtils.isNotBlank(questionResult.getJustification())) {
			answer += "<div style=\"clear: both; text-align: left;\" class=\"voffset20\"><i>"
				+ assessmentService.getMessage("label.answer.justification") + "</i><br>"
				+ questionResult.getJustificationEscaped() + "</div>";
		    }

		    if (questionResult.getMaxMark() != null) {
			if (questionResult.getMaxMark() == 0) {
			    // we can not rely of mark calculation when max mark is 0
			    // so we need to find an actual correct answer
			    Long chosenQuestionUid = null;
			    for (AssessmentOptionAnswer chosenAnswer : questionResult.getOptionAnswers()) {
				if (chosenAnswer.getAnswerBoolean()) {
				    chosenQuestionUid = chosenAnswer.getOptionUid();
				    break;
				}
			    }
			    if (chosenQuestionUid != null) {
				for (OptionDTO optionDto : questionDto.getOptionDtos()) {
				    if (optionDto.isCorrect() && chosenQuestionUid.equals(optionDto.getUid())) {
					correct = true;
					break;
				    }
				}
			    }
			} else {
			    correct = questionResult.getPenalty() + questionResult.getMark() + 0.1 >= questionResult
				    .getMaxMark();
			}
		    }

		}
		tblQuestionResultDto.setAnswer(answer);
		tblQuestionResultDto.setCorrect(correct);

		sessionQuestionResults.add(tblQuestionResultDto);
	    }
	    tblQuestionDto.setSessionQuestionResults(sessionQuestionResults);

	    tblQuestionDtos.add(tblQuestionDto);
	}

	SortedSet<AssessmentSession> sessions = new TreeSet<>(new AssessmentSessionComparator());
	sessions.addAll(assessmentService.getSessionsByContentId(assessment.getContentId()));

	request.setAttribute("sessions", sessions);
	request.setAttribute("questionDtos", tblQuestionDtos);

	return "pages/tblmonitoring/assessmentStudentChoicesTable";
    }

    @RequestMapping("aesStudentChoices")
    public String aesStudentChoices(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	Map<Long, QuestionSummary> questionSummaries = assessmentService.getQuestionSummaryForExport(assessment, false);
	List<TblAssessmentQuestionDTO> tblQuestionDtos = new ArrayList<>();
	for (QuestionSummary questionSummary : questionSummaries.values()) {
	    QuestionDTO questionDto = questionSummary.getQuestionDto();

	    TblAssessmentQuestionDTO tblQuestionDto = new TblAssessmentQuestionDTO();
	    tblQuestionDto.setTitle(questionDto.getTitle());
	    tblQuestionDto
		    .setQuestionTypeLabel(AssessmentServiceImpl.getQuestionTypeLanguageLabel(questionDto.getType()));
	    tblQuestionDto.setCorrectAnswer(getAssessmentCorrectAnswer(questionDto));

	    tblQuestionDtos.add(tblQuestionDto);
	}

	request.setAttribute("questionDtos", tblQuestionDtos);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	request.setAttribute("groupsInAnsweredQuestionsChart", assessment.isUseSelectLeaderToolOuput());
	request.setAttribute("assessment", assessment);

	return "pages/tblmonitoring/assessmentStudentChoices";
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    private String getAssessmentCorrectAnswer(QuestionDTO questionDto) {
	StringBuilder sb = new StringBuilder();

	if (questionDto != null) {
	    switch (questionDto.getType()) {
		case QbQuestion.TYPE_ESSAY:
		    return "N.A.";

		case QbQuestion.TYPE_MATCHING_PAIRS:
		    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			sb.append("<div><div style='float: left;'>").append(optionDto.getMatchingPair())
				.append("	</div><div style=' float: right; width: 50%;'> 		- ")
				.append(optionDto.getName().replaceAll("\\<.*?\\>", "")).append("</div></div><br>");
		    }
		    return sb.toString();

		case QbQuestion.TYPE_MULTIPLE_CHOICE:
		case QbQuestion.TYPE_VERY_SHORT_ANSWERS:
		    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			if (optionDto.getMaxMark() == 1f) {
			    return optionDto.getName();
			}
		    }
		    break;

		case QbQuestion.TYPE_NUMERICAL:
		    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			if (optionDto.getMaxMark() == 1f) {
			    return "" + optionDto.getNumericalOption();
			}
		    }
		    break;

		case QbQuestion.TYPE_ORDERING:
		    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			sb.append(optionDto.getName() + "\n");
		    }
		    return sb.toString();

		case QbQuestion.TYPE_TRUE_FALSE:
		    return String.valueOf(questionDto.getCorrectAnswer());

		case QbQuestion.TYPE_MARK_HEDGING:
		    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			if (optionDto.isCorrect()) {
			    return optionDto.getName();
			}
		    }
		    break;

		default:
		    return null;
	    }
	}

	return null;
    }

    /**
     * Get ModalDialog for Teams tab.
     */
    @RequestMapping("getModalDialogForTeamsTab")
    public String getModalDialogForTeamsTab(HttpServletRequest request) {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);

	AssessmentUser user = assessmentService.getUserByIdAndContent(userId, toolContentId);
	AssessmentResultDTO result = assessmentService.getUserMasterDetail(user.getSession().getSessionId(), userId);
	request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);

	return "pages/tblmonitoring/teams";
    }

}
