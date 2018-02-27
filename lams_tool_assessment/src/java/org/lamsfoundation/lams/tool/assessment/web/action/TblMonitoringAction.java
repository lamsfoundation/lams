package org.lamsfoundation.lams.tool.assessment.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentDTO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.SessionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionResultDTO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TblMonitoringAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(TblMonitoringAction.class.getName());
    
    private IAssessmentService assessmentService;
    
    /**
    * Shows ira page in case of Assessment activity
    */
   public ActionForward iraAssessment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
       initAssessmentService();

	Long toolContentId = WebUtil.readLongParam(request, "assessmentToolContentIds");
	String assessmentActivityTitle = request.getParameter("assessmentActivityTitles");
	
	String[] toolContentIds = new String[] {toolContentId.toString()};
	String[] activityTitles = new String[] {assessmentActivityTitle};
	
	List<TblAssessmentDTO> assessmentDtos = getAssessmentDtos(toolContentIds, activityTitles);
	request.setAttribute("assessmentDtos", assessmentDtos);
	
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	request.setAttribute("isIraAssessment", true);
	return mapping.findForward("assessment");
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
    public ActionForward aes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initAssessmentService();

	String[] toolContentIds = request.getParameter("assessmentToolContentIds").split(",");
	String[] activityTitles = request.getParameter("assessmentActivityTitles").split("\\,");

	List<TblAssessmentDTO> assessmentDtos = getAssessmentDtos(toolContentIds, activityTitles);
	request.setAttribute("assessmentDtos", assessmentDtos);
	
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, true);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);

	return mapping.findForward("assessment");
    }

    /**
     * Shows ira StudentChoices page
     */
    public ActionForward aesStudentChoices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initAssessmentService();

	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
	Map<Long, QuestionSummary> questionSummaries = assessmentService.getQuestionSummaryForExport(assessment);
	List<TblAssessmentQuestionDTO> tblQuestionDtos = new ArrayList<TblAssessmentQuestionDTO>();
	for (QuestionSummary questionSummary : questionSummaries.values()) {
	    AssessmentQuestion question = questionSummary.getQuestion();

	    TblAssessmentQuestionDTO tblQuestionDto = new TblAssessmentQuestionDTO();
	    tblQuestionDto.setQuestion(question);
	    tblQuestionDto.setQuestionTypeLabel(TblMonitoringAction.getAssessmentQuestionTypeLabel(question.getType()));
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
	return mapping.findForward("assessmentStudentChoices");
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
		    return new Boolean(question.getCorrectAnswer()).toString();

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

//    /**
//     * Excel Summary Export.
//     *
//     * @param mapping
//     * @param form
//     * @param request
//     * @param response
//     * @return
//     * @throws IOException
//     */
//    public ActionForward exportExcelAssessment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
//	    HttpServletResponse response) throws IOException, ServletException {
//	initAssessmentService();
//
//	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
//	Assessment assessment = assessmentService.getAssessmentByContentId(toolContentId);
//
//	List<SessionDTO> sessionDtos = assessmentService.getSessionDtos(toolContentId, false);
//	boolean showUserNames = true;
//	LinkedHashMap<String, ExcelCell[][]> dataToExport = assessmentService.exportSummary(assessment, sessionDtos,
//		showUserNames);
//
//	response.setContentType("application/x-download");
//	String fileName = "assessment_" + assessment.getUid() + "_export.xlsx";
//	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//	log.debug("Exporting assessment to a spreadsheet: " + assessment.getContentId());
//
//	ServletOutputStream out = response.getOutputStream();
//	ExcelUtil.createExcel(out, dataToExport, "Exported on", true);
//
//	return null;
//    }
    
    /**
     * Get ModalDialog for Teams tab.
     * 
     * @throws JSONException
     * @throws IOException
     */
    public ActionForward getModalDialogForTeamsTab(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	initAssessmentService();
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	
	AssessmentUser user = assessmentService.getUserByIdAndContent(userId, toolContentId);
	AssessmentResultDTO result = assessmentService.getUserMasterDetail(user.getSession().getSessionId(), userId);
	request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);
	
	
//	// release object from the cache (it's required when we have modified result object in the same request)
//	AssessmentResult result = assessmentService.getLastFinishedAssessmentResultNotFromChache(assessment.getUid(), userId);
//
//	for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
//	    for (QuestionDTO questionDto : questionsForOnePage) {
//
//		// find corresponding questionResult
//		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
//		    if (questionDto.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
//
//			// copy questionResult's info to the question
//			questionDto.setResponseSubmitted(questionResult.getFinishDate() != null);
//
//			// required for showing right/wrong answers icons on results page correctly
//			if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER
//				|| questionDto.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
//			    boolean isAnsweredCorrectly = false;
//			    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
//				if (optionDto.getUid().equals(questionResult.getSubmittedOptionUid())) {
//				    isAnsweredCorrectly = optionDto.getGrade() > 0;
//				    break;
//				}
//			    }
//			    questionDto.setAnswerBoolean(isAnsweredCorrectly);
//			}
//
//			// required for markandpenalty area and if it's on - on question's summary page
//			List<Object[]> questionResults = assessmentService.getAssessmentQuestionResultList(assessment.getUid(),
//				userId, questionDto.getUid());
//			questionDto.setQuestionResults(questionResults);
//		    }
//		}
//	    }
//	}
	
	return mapping.findForward("teams");
    }

//  /**
//   * Used only for excell export (for getUserSummaryData() method).
//   */
//  private static TBLAssessmentQuestionResult getAssessmentTBLQuestionResult(AssessmentQuestionResult questionResult) {
//	AssessmentQuestion question = questionResult.getAssessmentQuestion();
//	String answer = "";
//
//	switch (question.getType()) {
//	case AssessmentConstants.QUESTION_TYPE_ESSAY:
//	    answer = questionResult.getAnswerString();
//
//	case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
//	    for (AssessmentQuestionOption option : question.getOptions()) {
//		sb.append("[" + option.getQuestion() + ", " + option.getOptionString() + "] \n");
//	    }
//	    return sb.toString();
//
//	case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
//	    for (AssessmentQuestionOption option : question.getOptions()) {
//		if (option.getGrade() == 100f) {
//		    return option.getOptionString();
//		}
//	    }
//
//	case AssessmentConstants.QUESTION_TYPE_NUMERICAL:
//	    return question.getAnswerString();
//
//	case AssessmentConstants.QUESTION_TYPE_ORDERING:
//	    TreeSet<AssessmentQuestionOption> correctOptionSet = new TreeSet<AssessmentQuestionOption>(
//		    new SequencableComparator());
//	    correctOptionSet.addAll(question.getOptions());
//
//	    for (AssessmentQuestionOption option : question.getOptions()) {
//		sb.append(option.getOptionString() + "\n");
//	    }
//	    return sb.toString();
//
//	case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
//	    return question.getAnswerString();
//
//	case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
//	    return new Boolean(question.getAnswerBoolean()).toString();
//
//	case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:
//	    for (AssessmentQuestionOption option : question.getOptions()) {
//		if (option.isCorrect()) {
//		    return option.getOptionString();
//		}
//	    }
//
//	default:
//	    return null;
//	}
//
//	TBLAssessmentQuestionResult tblQuestionResult = new TBLAssessmentQuestionResult();
//	String answer = AssessmentEscapeUtils.printResponsesForJqgrid(questionResult);
//	tblQuestionResult.setAnswer(answer);
//	boolean correct = (questionResult.getPenalty() + questionResult.getMark() + 0.1) >= questionResult.getMaxMark();
//	tblQuestionResult.setCorrect(correct);
//	return tblQuestionResult;
//  }
    
    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IAssessmentService initAssessmentService() {
	if (assessmentService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    assessmentService = (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
	}
	return assessmentService;
    }

}
