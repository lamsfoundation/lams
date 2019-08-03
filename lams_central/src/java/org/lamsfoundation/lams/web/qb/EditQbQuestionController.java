package org.lamsfoundation.lams.web.qb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

@Controller
@RequestMapping("/qb/edit")
public class EditQbQuestionController {
    private static Logger log = Logger.getLogger(EditQbQuestionController.class);

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;
    @Autowired
    private IQbService qbService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    WebApplicationContext applicationcontext;

    /**
     * Display empty page for new question.
     */
    @RequestMapping("/initNewQuestion")
    public String initNewQuestion(@ModelAttribute("assessmentQuestionForm") QbQuestionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(required = false) Long collectionUid) throws ServletException, IOException {

	form.setUid(-1L);//which signifies it's a new question
	form.setQuestionId(qbService.generateNextQuestionId()); // generate a new question ID right away, so another user won't "take it"
	form.setMaxMark(1);
	form.setPenaltyFactor("0");
	form.setAnswerRequired(true);

	// generate a new contentFolderID for new question
	String contentFolderId = FileUtil.generateUniqueContentFolderID();
	form.setContentFolderID(contentFolderId);

	List<QbOption> optionList = new ArrayList<>();
	for (int i = 0; i < QbConstants.INITIAL_OPTIONS_NUMBER; i++) {
	    QbOption option = new QbOption();
	    option.setDisplayOrder(i + 1);
	    optionList.add(option);
	}
	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);

	List<QbQuestionUnit> unitList = new ArrayList<>();
	QbQuestionUnit unit = new QbQuestionUnit();
	unit.setDisplayOrder(1);
	unit.setMultiplier(1);
	unitList.add(unit);
	for (int i = 1; i < QbConstants.INITIAL_UNITS_NUMBER; i++) {
	    unit = new QbQuestionUnit();
	    unit.setDisplayOrder(i + 1);
	    unit.setMultiplier(0);
	    unitList.add(unit);
	}
	request.setAttribute(QbConstants.ATTR_UNIT_LIST, unitList);

	QbUtils.fillFormWithUserCollections(qbService, form, null);

	Integer type = NumberUtils.toInt(request.getParameter(QbConstants.ATTR_QUESTION_TYPE));
	return findForwardByQuestionType(type);
    }

    /**
     * Display edit page for existing question.
     */
    @RequestMapping("/editQuestion")
    public String editQuestion(@ModelAttribute("assessmentQuestionForm") QbQuestionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	Long qbQuestionUid = WebUtil.readLongParam(request, "qbQuestionUid");
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	if (qbQuestion == null) {
	    throw new RuntimeException("QbQuestion with uid:" + qbQuestionUid + " was not found!");
	}

	//populate question information to its form for editing
	form.setUid(qbQuestion.getUid());
	form.setQuestionId(qbQuestion.getQuestionId());
	//TODO remove hardcoded value, once we transfer contentFolderId from old DB entries
	form.setContentFolderID(qbQuestion.getContentFolderId() == null ? "temp" : qbQuestion.getContentFolderId());
	form.setTitle(qbQuestion.getName());
	form.setDescription(qbQuestion.getDescription());
	form.setMaxMark(qbQuestion.getMaxMark() == null ? 1 : qbQuestion.getMaxMark());
	form.setPenaltyFactor(String.valueOf(qbQuestion.getPenaltyFactor()));
	form.setAnswerRequired(qbQuestion.isAnswerRequired());
	form.setFeedback(qbQuestion.getFeedback());
	form.setMultipleAnswersAllowed(qbQuestion.isMultipleAnswersAllowed());
	form.setIncorrectAnswerNullifiesMark(qbQuestion.isIncorrectAnswerNullifiesMark());
	form.setFeedbackOnCorrect(qbQuestion.getFeedbackOnCorrect());
	form.setFeedbackOnPartiallyCorrect(qbQuestion.getFeedbackOnPartiallyCorrect());
	form.setFeedbackOnIncorrect(qbQuestion.getFeedbackOnIncorrect());
	form.setShuffle(qbQuestion.isShuffle());
	form.setPrefixAnswersWithLetters(qbQuestion.isPrefixAnswersWithLetters());
	form.setCaseSensitive(qbQuestion.isCaseSensitive());
	form.setCorrectAnswer(qbQuestion.getCorrectAnswer());
	form.setAllowRichEditor(qbQuestion.isAllowRichEditor());
	form.setMaxWordsLimit(qbQuestion.getMaxWordsLimit());
	form.setMinWordsLimit(qbQuestion.getMinWordsLimit());
	form.setHedgingJustificationEnabled(qbQuestion.isHedgingJustificationEnabled());

	Integer questionType = qbQuestion.getType();
	if ((questionType == QbQuestion.TYPE_MULTIPLE_CHOICE) || (questionType == QbQuestion.TYPE_ORDERING)
		|| (questionType == QbQuestion.TYPE_MATCHING_PAIRS) || (questionType == QbQuestion.TYPE_SHORT_ANSWER)
		|| (questionType == QbQuestion.TYPE_NUMERICAL) || (questionType == QbQuestion.TYPE_MARK_HEDGING)) {
	    List<QbOption> optionList = qbQuestion.getQbOptions();
	    request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);
	}
	if (questionType == QbQuestion.TYPE_NUMERICAL) {
	    List<QbQuestionUnit> unitList = qbQuestion.getUnits();
	    request.setAttribute(QbConstants.ATTR_UNIT_LIST, unitList);
	}
	
	QbUtils.fillFormWithUserCollections(qbService, form, qbQuestionUid);

	return findForwardByQuestionType(qbQuestion.getType());
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @RequestMapping("/saveOrUpdateQuestion")
    public String saveOrUpdateQuestion(@ModelAttribute("assessmentQuestionForm") QbQuestionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	//find according question
	QbQuestion qbQuestion = null;
	Long oldQuestionUid = null;

	boolean isAddingQuestion = form.getUid() == -1;
	// add
	if (isAddingQuestion) {
	    qbQuestion = new QbQuestion();
	    qbQuestion.setType(form.getQuestionType());
	    qbQuestion.setQuestionId(form.getQuestionId());

	    // edit
	} else {
	    oldQuestionUid = form.getUid();
	    qbQuestion = qbService.getQuestionByUid(oldQuestionUid);
	    qbService.releaseFromCache(qbQuestion);
	}

	int questionModificationStatus = extractFormToQbQuestion(qbQuestion, form, request);
	switch (questionModificationStatus) {
	    case IQbService.QUESTION_MODIFIED_VERSION_BUMP: {
		// new version of the old question gets created
		qbQuestion = qbQuestion.clone();
		qbQuestion.clearID();
		qbQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()) + 1);
		qbQuestion.setCreateDate(new Date());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_ID_BUMP: {
		// new question gets created
		qbQuestion = qbQuestion.clone();
		qbQuestion.clearID();
		qbQuestion.setVersion(1);
		qbQuestion.setCreateDate(new Date());
		// no need to bump question ID as the new question already has a new ID generated in initNewQuestion()
	    }
		break;
	}
	userManagementService.save(qbQuestion);

	final boolean IS_REQUEST_CAME_FROM_ASSESSMENT_TOOL = StringUtils.isNotBlank(form.getSessionMapID());

	//take care about question's collections. add to collection first
	Long oldCollectionUid = form.getOldCollectionUid();
	Long newCollectionUid = form.getNewCollectionUid();
	if (isAddingQuestion || (IS_REQUEST_CAME_FROM_ASSESSMENT_TOOL && !newCollectionUid.equals(oldCollectionUid))) {
	    qbService.addQuestionToCollection(newCollectionUid, qbQuestion.getQuestionId(), false);
	}

	//remove from the old collection, if needed
	if (!isAddingQuestion && IS_REQUEST_CAME_FROM_ASSESSMENT_TOOL && !newCollectionUid.equals(oldCollectionUid)) {
	    qbService.removeQuestionFromCollectionByQuestionId(oldCollectionUid, qbQuestion.getQuestionId(), false);
	}

	if (IS_REQUEST_CAME_FROM_ASSESSMENT_TOOL) {
	    //forward to Assessment controller
	    String params = "?qbQuestionUid=" + qbQuestion.getUid();
	    params += "&questionModificationStatus=" + questionModificationStatus;

	    String serverURLContextPath = Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH);
	    serverURLContextPath = serverURLContextPath.startsWith("/") ? serverURLContextPath
		    : "/" + serverURLContextPath;
	    serverURLContextPath += serverURLContextPath.endsWith("/") ? "" : "/";
	    applicationcontext.getServletContext().getContext(serverURLContextPath + "tool/laasse10/")
		    .getRequestDispatcher("/authoring/saveOrUpdateReference.do" + params).forward(request, response);
	    return null;

	} else {

	    // in case adding new question - return nothing
	    if (isAddingQuestion) {
		return null;

		// edit question case - return question's uid
	    } else {
		return "forward:returnQuestionUid.do?qbQuestionUid=" + qbQuestion.getUid();
	    }
	}
    }

    @RequestMapping("/returnQuestionUid")
    @ResponseBody
    public String returnQuestionUid(HttpServletResponse response, @RequestParam Long qbQuestionUid) {
	response.setContentType("text/plain");
	response.setCharacterEncoding("UTF-8");
	return qbQuestionUid.toString();
    }

    /**
     * Extract web form content to QB question.
     *
     * BE CAREFUL: This method will copy necessary info from request form to an old or new AssessmentQuestion
     * instance. It gets all info EXCEPT AssessmentQuestion.createDate, which need be set when
     * persisting this assessment Question.
     *
     * @return qbQuestionModified
     */
    private int extractFormToQbQuestion(QbQuestion qbQuestion, QbQuestionForm form, HttpServletRequest request) {
	QbQuestion oldQuestion = qbQuestion.clone();
	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
	qbService.releaseFromCache(oldQuestion);

	qbQuestion.setName(form.getTitle());
	qbQuestion.setDescription(form.getDescription());

	if (!form.isAuthoringRestricted()) {
	    qbQuestion.setMaxMark(form.getMaxMark());
	}
	qbQuestion.setFeedback(form.getFeedback());
	qbQuestion.setAnswerRequired(form.isAnswerRequired());
	qbQuestion.setContentFolderId(form.getContentFolderID());

	Integer type = form.getQuestionType();
	if (type == QbQuestion.TYPE_MULTIPLE_CHOICE) {
	    qbQuestion.setMultipleAnswersAllowed(form.isMultipleAnswersAllowed());
	    boolean incorrectAnswerNullifiesMark = form.isMultipleAnswersAllowed()
		    ? form.isIncorrectAnswerNullifiesMark()
		    : false;
	    qbQuestion.setIncorrectAnswerNullifiesMark(incorrectAnswerNullifiesMark);
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setShuffle(form.isShuffle());
	    qbQuestion.setPrefixAnswersWithLetters(form.isPrefixAnswersWithLetters());
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnPartiallyCorrect(form.getFeedbackOnPartiallyCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());
	} else if ((type == QbQuestion.TYPE_MATCHING_PAIRS)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setShuffle(form.isShuffle());
	} else if ((type == QbQuestion.TYPE_SHORT_ANSWER)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setCaseSensitive(form.isCaseSensitive());
	} else if ((type == QbQuestion.TYPE_NUMERICAL)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	} else if ((type == QbQuestion.TYPE_TRUE_FALSE)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setCorrectAnswer(form.isCorrectAnswer());
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());
	} else if ((type == QbQuestion.TYPE_ESSAY)) {
	    qbQuestion.setAllowRichEditor(form.isAllowRichEditor());
	    qbQuestion.setMaxWordsLimit(form.getMaxWordsLimit());
	    qbQuestion.setMinWordsLimit(form.getMinWordsLimit());
	} else if (type == QbQuestion.TYPE_ORDERING) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());
	} else if (type == QbQuestion.TYPE_MARK_HEDGING) {
	    qbQuestion.setShuffle(form.isShuffle());
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnPartiallyCorrect(form.getFeedbackOnPartiallyCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());
	    qbQuestion.setHedgingJustificationEnabled(form.isHedgingJustificationEnabled());
	}

	// set options
	if ((type == QbQuestion.TYPE_MULTIPLE_CHOICE) || (type == QbQuestion.TYPE_ORDERING)
		|| (type == QbQuestion.TYPE_MATCHING_PAIRS) || (type == QbQuestion.TYPE_SHORT_ANSWER)
		|| (type == QbQuestion.TYPE_NUMERICAL) || (type == QbQuestion.TYPE_MARK_HEDGING)) {
	    Set<QbOption> optionList = getOptionsFromRequest(request, true);
	    List<QbOption> options = new ArrayList<>();
	    int displayOrder = 0;
	    for (QbOption option : optionList) {
		option.setDisplayOrder(displayOrder++);
		options.add(option);
	    }
	    qbQuestion.setQbOptions(options);
	}
	// set units
	if (type == QbQuestion.TYPE_NUMERICAL) {
	    Set<QbQuestionUnit> unitList = getUnitsFromRequest(request, true);
	    List<QbQuestionUnit> units = new ArrayList<>();
	    int displayOrder = 0;
	    for (QbQuestionUnit unit : unitList) {
		unit.setDisplayOrder(displayOrder++);
		units.add(unit);
	    }
	    qbQuestion.setUnits(units);
	}

	return qbQuestion.isQbQuestionModified(oldQuestion);
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/addOption")
    public String addOption(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	TreeSet<QbOption> optionList = getOptionsFromRequest(request, false);

	QbOption option = new QbOption();
	int maxSeq = 1;
	if ((optionList != null) && (optionList.size() > 0)) {
	    QbOption last = optionList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	option.setDisplayOrder(maxSeq);
	optionList.add(option);

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(QbConstants.ATTR_QUESTION_TYPE,
		WebUtil.readIntParam(request, QbConstants.ATTR_QUESTION_TYPE));
	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);
	return "qb/authoring/optionlist";
    }

    /**
     * Ajax call, will add one more input line for new Unit.
     */
    @RequestMapping("/newUnit")
    public String newUnit(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	TreeSet<QbQuestionUnit> unitList = getUnitsFromRequest(request, false);
	QbQuestionUnit unit = new QbQuestionUnit();
	int maxSeq = 1;
	if ((unitList != null) && (unitList.size() > 0)) {
	    QbQuestionUnit last = unitList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	unit.setDisplayOrder(maxSeq);
	unitList.add(unit);

	request.setAttribute(QbConstants.ATTR_UNIT_LIST, unitList);
	return "qb/authoring/unitlist";
    }

    /**
     * Get answer options from <code>HttpRequest</code>
     *
     * @param request
     * @param isForSaving
     *            whether the blank options will be preserved or not
     */
    private TreeSet<QbOption> getOptionsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = EditQbQuestionController.splitRequestParameter(request,
		QbConstants.ATTR_OPTION_LIST);

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_COUNT));
	int questionType = WebUtil.readIntParam(request, QbConstants.ATTR_QUESTION_TYPE);
	Integer correctOptionIndex = (paramMap.get(QbConstants.ATTR_OPTION_CORRECT) == null) ? null
		: NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_CORRECT));

	TreeSet<QbOption> optionList = new TreeSet<>();
	for (int i = 0; i < count; i++) {

	    String displayOrder = paramMap.get(QbConstants.ATTR_OPTION_DISPLAY_ORDER_PREFIX + i);
	    //displayOrder is null, in case this item was removed using Remove button
	    if (displayOrder == null) {
		continue;
	    }

	    QbOption option = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_OPTION_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		option = qbService.getOptionByUid(uid);

	    } else {
		option = new QbOption();
	    }
	    option.setDisplayOrder(NumberUtils.toInt(displayOrder));

	    if ((questionType == QbQuestion.TYPE_MULTIPLE_CHOICE) || (questionType == QbQuestion.TYPE_SHORT_ANSWER)) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);
		float maxMark = Float.valueOf(paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i));
		option.setMaxMark(maxMark);
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == QbQuestion.TYPE_MATCHING_PAIRS) {
		String matchingPair = paramMap.get(QbConstants.ATTR_MATCHING_PAIR_PREFIX + i);
		if ((matchingPair == null) && isForSaving) {
		    continue;
		}

		option.setName(paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i));
		option.setMatchingPair(matchingPair);

	    } else if (questionType == QbQuestion.TYPE_NUMERICAL) {
		String numericalOptionStr = paramMap.get(QbConstants.ATTR_NUMERICAL_OPTION_PREFIX + i);
		String acceptedErrorStr = paramMap.get(QbConstants.ATTR_OPTION_ACCEPTED_ERROR_PREFIX + i);
		String maxMarkStr = paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i);
		if (numericalOptionStr.equals("0.0") && numericalOptionStr.equals("0.0") && maxMarkStr.equals("0.0")
			&& isForSaving) {
		    continue;
		}

		try {
		    float numericalOption = Float.valueOf(numericalOptionStr);
		    option.setNumericalOption(numericalOption);
		} catch (Exception e) {
		    option.setNumericalOption(0);
		}
		try {
		    float acceptedError = Float.valueOf(acceptedErrorStr);
		    option.setAcceptedError(acceptedError);
		} catch (Exception e) {
		    option.setAcceptedError(0);
		}
		float maxMark = Float.valueOf(paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i));
		option.setMaxMark(maxMark);
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == QbQuestion.TYPE_ORDERING) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);

	    } else if (questionType == QbQuestion.TYPE_MARK_HEDGING) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);
		if ((correctOptionIndex != null) && correctOptionIndex.equals(Integer.valueOf(displayOrder))) {
		    option.setCorrect(true);
		}
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
	    }

	    optionList.add(option);
	}
	return optionList;
    }

    /**
     * Get units from <code>HttpRequest</code>
     *
     * @param request
     */
    private TreeSet<QbQuestionUnit> getUnitsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = EditQbQuestionController.splitRequestParameter(request,
		QbConstants.ATTR_UNIT_LIST);

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_UNIT_COUNT));
	TreeSet<QbQuestionUnit> unitList = new TreeSet<>();
	for (int i = 0; i < count; i++) {
	    String name = paramMap.get(QbConstants.ATTR_UNIT_NAME_PREFIX + i);
	    if (StringUtils.isBlank(name) && isForSaving) {
		continue;
	    }

	    QbQuestionUnit unit = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_UNIT_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		unit = qbService.getQuestionUnitByUid(uid);

	    } else {
		unit = new QbQuestionUnit();
	    }
	    String displayOrder = paramMap.get(QbConstants.ATTR_UNIT_DISPLAY_ORDER_PREFIX + i);
	    unit.setDisplayOrder(NumberUtils.toInt(displayOrder));
	    unit.setName(name);
	    float multiplier = Float.valueOf(paramMap.get(QbConstants.ATTR_UNIT_MULTIPLIER_PREFIX + i));
	    unit.setMultiplier(multiplier);
	    unitList.add(unit);
	}

	return unitList;
    }

    /**
     * Split Request Parameter from <code>HttpRequest</code>
     *
     * @param request
     * @param parameterName
     *            parameterName
     */
    private static Map<String, String> splitRequestParameter(HttpServletRequest request, String parameterName) {
	String list = request.getParameter(parameterName);
	if (list == null) {
	    return null;
	}

	String[] params = list.split("&");
	Map<String, String> paramMap = new HashMap<>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if ((pair == null) || (pair.length != 2)) {
		continue;
	    }
	    try {
		paramMap.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		log.error("Error occurs when decode instruction string:" + e.toString());
	    }
	}
	return paramMap;
    }

    /**
     * Get back jsp name.
     */
    private String findForwardByQuestionType(Integer type) {
	String forward;
	switch (type) {
	    case QbQuestion.TYPE_MULTIPLE_CHOICE:
		forward = "qb/authoring/addmultiplechoice";
		break;
	    case QbQuestion.TYPE_MATCHING_PAIRS:
		forward = "qb/authoring/addmatchingpairs";
		break;
	    case QbQuestion.TYPE_SHORT_ANSWER:
		forward = "qb/authoring/addshortanswer";
		break;
	    case QbQuestion.TYPE_NUMERICAL:
		forward = "qb/authoring/addnumerical";
		break;
	    case QbQuestion.TYPE_TRUE_FALSE:
		forward = "qb/authoring/addtruefalse";
		break;
	    case QbQuestion.TYPE_ESSAY:
		forward = "qb/authoring/addessay";
		break;
	    case QbQuestion.TYPE_ORDERING:
		forward = "qb/authoring/addordering";
		break;
	    case QbQuestion.TYPE_MARK_HEDGING:
		forward = "qb/authoring/addmarkhedging";
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

}
