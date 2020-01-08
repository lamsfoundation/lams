package org.lamsfoundation.lams.tool.assessment.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionResultDTO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String iraAssessment(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, "toolContentID");

	String[] toolContentIds = new String[] { toolContentId.toString() };
	String[] activityTitles = new String[] { "" };
	List<TblAssessmentDTO> assessmentDtos = getAssessmentDtos(toolContentIds, activityTitles);
	request.setAttribute("assessmentDtos", assessmentDtos);

	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	request.setAttribute("isIraAssessment", true);
	return "pages/tblmonitoring/assessment";
    }

    /**
     * Shows ira page in case of Assessment activity
     */
    @RequestMapping("iraAssessmentStudentChoices")
    public String iraAssessmentStudentChoices(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);

	//prepare list of the questions, filtering out questions that aren't supposed to be answered
	Set<AssessmentQuestion> questionList = new TreeSet<AssessmentQuestion>();
	//in case there is at least one random question - we need to show all questions in a drop down select
	if (assessment.hasRandomQuestion()) {
	    questionList.addAll(assessment.getQuestions());

	    //show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
		questionList.add(reference.getQuestion());
	    }
	}
	//keep only MCQ type of questions
	Set<AssessmentQuestion> mcqQuestions = new TreeSet<AssessmentQuestion>();
	int maxOptionsInQuestion = 0;
	for (AssessmentQuestion question : questionList) {
	    if (AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE == question.getType()) {
		mcqQuestions.add(question);

		//calculate maxOptionsInQuestion
		if (question.getOptions().size() > maxOptionsInQuestion) {
		    maxOptionsInQuestion = question.getOptions().size();
		}
	    }
	}
	request.setAttribute("maxOptionsInQuestion", maxOptionsInQuestion);

	int totalNumberOfUsers = assessmentService.getCountUsersByContentId(toolContentId);
	for (AssessmentQuestion question : mcqQuestions) {

	    // build candidate dtos
	    for (AssessmentQuestionOption option : question.getOptions()) {
		int optionAttemptCount = assessmentService.countAttemptsPerOption(option.getUid());

		float percentage = (float) (optionAttemptCount * 100) / totalNumberOfUsers;
		option.setPercentage(percentage);
	    }
	}
	request.setAttribute("questions", mcqQuestions);

	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	return "pages/tblmonitoring/iraAssessmentStudentChoices";
    }

    private List<TblAssessmentDTO> getAssessmentDtos(String[] toolContentIds, String[] activityTitles) {
	List<TblAssessmentDTO> assessmentDtos = new ArrayList<TblAssessmentDTO>();
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
	Set<AssessmentQuestion> questions = new TreeSet<AssessmentQuestion>();
	boolean hasRandomQuestion = false;
	for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
	    hasRandomQuestion |= reference.isRandomQuestion();
	}
	// in case there is at least one random question - we need to show all questions
	if (hasRandomQuestion) {
	    questions.addAll(assessment.getQuestions());

	    // show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
		//sort questions the same way references are sorted (as per LKC request)
		AssessmentQuestion question = reference.getQuestion();
		question.setSequenceId(reference.getSequenceId());
		questions.add(question);
	    }
	}
	return questions;
    }

    /**
     * Shows aes page
     */
    @RequestMapping("aes")
    public String aes(HttpServletRequest request) {
	String[] toolContentIds = request.getParameter("assessmentToolContentIds").split(",");
	String[] activityTitles = request.getParameter("assessmentActivityTitles").split("\\,");

	List<TblAssessmentDTO> assessmentDtos = getAssessmentDtos(toolContentIds, activityTitles);
	request.setAttribute("assessmentDtos", assessmentDtos);

	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, true);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);

	return "pages/tblmonitoring/assessment";
    }

    /**
     * Shows ira StudentChoices page
     */
    @RequestMapping("aesStudentChoices")
    public String aesStudentChoices(HttpServletRequest request) {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	Map<Long, QuestionSummary> questionSummaries = assessmentService.getQuestionSummaryForExport(assessment);
	List<TblAssessmentQuestionDTO> tblQuestionDtos = new ArrayList<TblAssessmentQuestionDTO>();
	for (QuestionSummary questionSummary : questionSummaries.values()) {
	    AssessmentQuestion question = questionSummary.getQuestion();

	    TblAssessmentQuestionDTO tblQuestionDto = new TblAssessmentQuestionDTO();
	    tblQuestionDto.setQuestion(question);
	    tblQuestionDto.setQuestionTypeLabel(TblMonitoringController.getAssessmentQuestionTypeLabel(question.getType()));
	    tblQuestionDto.setCorrectAnswer(getAssessmentCorrectAnswer(question));

	    List<TblAssessmentQuestionResultDTO> sessionQuestionResults = new ArrayList<TblAssessmentQuestionResultDTO>();
	    for (List<AssessmentQuestionResult> questionResultsPerSession : questionSummary
		    .getQuestionResultsPerSession()) {

		TblAssessmentQuestionResultDTO tblQuestionResultDto = new TblAssessmentQuestionResultDTO();
		String answer = "";
		boolean correct = false;
		if (!questionResultsPerSession.isEmpty()) {
		    AssessmentQuestionResult questionResult = questionResultsPerSession.get(0);
		    answer = AssessmentEscapeUtils.printResponsesForJqgrid(questionResult);
		    correct = questionResult.getMaxMark() == null ? false
			    : (questionResult.getPenalty() + questionResult.getMark() + 0.1) >= questionResult
				    .getMaxMark();
		}
		tblQuestionResultDto.setAnswer(answer);
		tblQuestionResultDto.setCorrect(correct);

		sessionQuestionResults.add(tblQuestionResultDto);
	    }
	    tblQuestionDto.setSessionQuestionResults(sessionQuestionResults);

	    tblQuestionDtos.add(tblQuestionDto);
	}

	SortedSet<AssessmentSession> sessions = new TreeSet<AssessmentSession>(new AssessmentSessionComparator());
	sessions.addAll(assessmentService.getSessionsByContentId(assessment.getContentId()));

	request.setAttribute("sessions", sessions);
	request.setAttribute("questionDtos", tblQuestionDtos);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	return "pages/tblmonitoring/assessmentStudentChoices";
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    private static String getAssessmentQuestionTypeLabel(short type) {
	switch (type) {
	    case AssessmentConstants.QUESTION_TYPE_ESSAY:
		return "Essay";
	    case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		return "Matching Pairs";
	    case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
		return "Multiple Choice";
	    case AssessmentConstants.QUESTION_TYPE_NUMERICAL:
		return "Numerical";
	    case AssessmentConstants.QUESTION_TYPE_ORDERING:
		return "Ordering";
	    case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		return "Short Answer";
	    case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		return "True/False";
	    case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:
		return "Mark Hedging";
	    default:
		return null;
	}
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    private String getAssessmentCorrectAnswer(AssessmentQuestion question) {
	StringBuilder sb = new StringBuilder();

	if (question != null) {
	    switch (question.getType()) {
		case AssessmentConstants.QUESTION_TYPE_ESSAY:
		    return "N.A.";

		case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		    for (AssessmentQuestionOption option : question.getOptions()) {
			sb.append((option.getQuestion() + " - " + option.getOptionString()).replaceAll("\\<.*?\\>", "")
				+ " <br>");
		    }
		    return sb.toString();

		case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
		case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		    for (AssessmentQuestionOption option : question.getOptions()) {
			if (option.getGrade() == 1f) {
			    return option.getOptionString();
			}
		    }
		    break;

		case AssessmentConstants.QUESTION_TYPE_NUMERICAL:
		    for (AssessmentQuestionOption option : question.getOptions()) {
			if (option.getGrade() == 1f) {
			    return "" + option.getOptionFloat();
			}
		    }
		    break;

		case AssessmentConstants.QUESTION_TYPE_ORDERING:
		    TreeSet<AssessmentQuestionOption> correctOptionSet = new TreeSet<AssessmentQuestionOption>(
			    new SequencableComparator());
		    correctOptionSet.addAll(question.getOptions());

		    for (AssessmentQuestionOption option : question.getOptions()) {
			sb.append(option.getOptionString() + "\n");
		    }
		    return sb.toString();

		case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		    return String.valueOf(question.getCorrectAnswer());

		case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:
		    for (AssessmentQuestionOption option : question.getOptions()) {
			if (option.isCorrect()) {
			    return option.getOptionString();
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
