package org.lamsfoundation.lams.web.qb;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

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
    @Autowired
    ISecurityService securityService;

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
	    HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException, IllegalAccessException, InvocationTargetException {
	Long qbQuestionUid = WebUtil.readLongParam(request, "qbQuestionUid");
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	if (qbQuestion == null) {
	    throw new RuntimeException("QbQuestion with uid:" + qbQuestionUid + " was not found!");
	}
	Integer userId = getUserId();
	boolean editingAllowed = securityService.isAppadmin(userId, null, true)
		|| securityService.isSysadmin(userId, null, true)
		|| qbService.isQuestionInUserCollection(qbQuestion.getQuestionId(), userId)
		|| qbService.isQuestionInPublicCollection(qbQuestion.getQuestionId());
	if (!editingAllowed) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN,
		    "The user does not have access to given QB question editing");
	    return null;
	}

	//populate question information to its form for editing
	form.setUid(qbQuestion.getUid());
	form.setQuestionId(qbQuestion.getQuestionId());
	form.setContentFolderID(qbQuestion.getContentFolderId());
	form.setTitle(qbQuestion.getName());
	form.setDescription(qbQuestion.getDescription());
	form.setMaxMark(qbQuestion.getMaxMark() == null ? 1 : qbQuestion.getMaxMark());
	form.setPenaltyFactor(String.valueOf(qbQuestion.getPenaltyFactor()));
	form.setFeedback(qbQuestion.getFeedback());
	form.setMultipleAnswersAllowed(qbQuestion.isMultipleAnswersAllowed());
	form.setIncorrectAnswerNullifiesMark(qbQuestion.isIncorrectAnswerNullifiesMark());
	form.setFeedbackOnCorrect(qbQuestion.getFeedbackOnCorrect());
	form.setFeedbackOnPartiallyCorrect(qbQuestion.getFeedbackOnPartiallyCorrect());
	form.setFeedbackOnIncorrect(qbQuestion.getFeedbackOnIncorrect());
	form.setShuffle(qbQuestion.isShuffle());
	form.setPrefixAnswersWithLetters(qbQuestion.isPrefixAnswersWithLetters());
	form.setCaseSensitive(qbQuestion.isCaseSensitive());
	form.setExactMatch(qbQuestion.isExactMatch());
	form.setCorrectAnswer(qbQuestion.getCorrectAnswer());
	form.setAllowRichEditor(qbQuestion.isAllowRichEditor());
	form.setMaxWordsLimit(qbQuestion.getMaxWordsLimit());
	form.setMinWordsLimit(qbQuestion.getMinWordsLimit());
	form.setCodeStyle(qbQuestion.getCodeStyle());
	form.setHedgingJustificationEnabled(qbQuestion.isHedgingJustificationEnabled());
	//TODO check autocomplete is saved and then maybe remove other property copying
	BeanUtils.copyProperties(form, qbQuestion);

	Integer questionType = qbQuestion.getType();
	if ((questionType == QbQuestion.TYPE_MULTIPLE_CHOICE) || (questionType == QbQuestion.TYPE_ORDERING)
		|| (questionType == QbQuestion.TYPE_MATCHING_PAIRS)
		|| (questionType == QbQuestion.TYPE_VERY_SHORT_ANSWERS) || (questionType == QbQuestion.TYPE_NUMERICAL)
		|| (questionType == QbQuestion.TYPE_MARK_HEDGING)) {
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
    @RequestMapping(path = "/saveOrUpdateQuestion", method = RequestMethod.POST)
    public String saveOrUpdateQuestion(@ModelAttribute("assessmentQuestionForm") QbQuestionForm form,
	    @RequestParam(name = "newVersion", required = false) boolean enforceNewVersion, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
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
	}

	int questionModificationStatus = qbService.extractFormToQbQuestion(qbQuestion, form, request);
	if (questionModificationStatus < IQbService.QUESTION_MODIFIED_VERSION_BUMP && enforceNewVersion) {
	    questionModificationStatus = IQbService.QUESTION_MODIFIED_VERSION_BUMP;
	}
	switch (questionModificationStatus) {
	    case IQbService.QUESTION_MODIFIED_VERSION_BUMP: {
		// new version of the old question gets created
		qbQuestion = qbQuestion.clone();
		qbQuestion.clearID();
		qbQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()) + 1);
		qbQuestion.setCreateDate(new Date());
		qbQuestion.setUuid(UUID.randomUUID());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_ID_BUMP: {
		// new question gets created
		qbQuestion = qbQuestion.clone();
		qbQuestion.clearID();
		qbQuestion.setVersion(1);
		qbQuestion.setCreateDate(new Date());
		qbQuestion.setUuid(UUID.randomUUID());
		// no need to bump question ID as the new question already has a new ID generated in initNewQuestion()
	    }
		break;
	}
	userManagementService.save(qbQuestion);

	final boolean isRequestCameFromTool = StringUtils.isNotBlank(form.getSessionMapID());

	//take care about question's collections. add to collection first
	Long oldCollectionUid = form.getOldCollectionUid();
	Long newCollectionUid = form.getNewCollectionUid();
	if (isAddingQuestion
		|| (isRequestCameFromTool && oldCollectionUid != null && !newCollectionUid.equals(oldCollectionUid))) {
	    qbService.addQuestionToCollection(newCollectionUid, qbQuestion.getQuestionId(), false);
	}

	//remove from the old collection, if needed and the question is in users' collections
	if (!isAddingQuestion && isRequestCameFromTool && oldCollectionUid != null
		&& !newCollectionUid.equals(oldCollectionUid)) {
	    qbService.removeQuestionFromCollectionByQuestionId(oldCollectionUid, qbQuestion.getQuestionId(), false);
	}

	if (isRequestCameFromTool) {
	    //redirect to Assessment controller
	    String url = "redirect:" + Configuration.get(ConfigurationKeys.SERVER_URL)
		    + "tool/laasse10/authoring/saveOrUpdateReference.do";
	    url = WebUtil.appendParameterToURL(url, "uid", String.valueOf(form.getUid()));
	    url = WebUtil.appendParameterToURL(url, "sessionMapID", String.valueOf(form.getSessionMapID()));
	    url = WebUtil.appendParameterToURL(url, "qbQuestionUid", String.valueOf(qbQuestion.getUid()));
	    url = WebUtil.appendParameterToURL(url, "questionModificationStatus",
		    String.valueOf(questionModificationStatus));

	    return url;
	} else {
	    return "forward:returnQuestionUid.do?qbQuestionUid=" + qbQuestion.getUid();
	}
    }

    @RequestMapping(path = "/checkQuestionNewVersion", method = RequestMethod.POST)
    @ResponseBody
    public String checkQuestionNewVersion(@ModelAttribute("assessmentQuestionForm") QbQuestionForm form,
	    HttpServletRequest request) {
	boolean isAddingQuestion = form.getUid() == -1;
	if (isAddingQuestion) {
	    return "true";
	}

	QbQuestion qbQuestion = qbService.getQuestionByUid(form.getUid());
	return String.valueOf(qbService.extractFormToQbQuestion(qbQuestion, form,
		request) >= IQbService.QUESTION_MODIFIED_VERSION_BUMP);
    }

    @RequestMapping("/returnQuestionUid")
    @ResponseBody
    public String returnQuestionUid(HttpServletResponse response, @RequestParam Long qbQuestionUid) {
	response.setContentType("text/plain");
	response.setCharacterEncoding("UTF-8");
	return qbQuestionUid.toString();
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/addOption")
    public String addOption(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	TreeSet<QbOption> optionList = qbService.getOptionsFromRequest(request, false);

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
	TreeSet<QbQuestionUnit> unitList = qbService.getUnitsFromRequest(request, false);
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
     * Ajax call, will remove the given unit
     */
    @RequestMapping("/removeUnit")
    public String removeUnit(HttpServletRequest request, @RequestParam int unitToRemoveIndex)
	    throws ServletException, IOException {
	Set<QbQuestionUnit> unitList = qbService.getUnitsFromRequest(request, false);
	Set<QbQuestionUnit> newUnitList = new TreeSet<>();
	int displayOrder = 0;
	for (QbQuestionUnit unit : unitList) {
	    if (unitToRemoveIndex != unit.getDisplayOrder()) {
		unit.setDisplayOrder(displayOrder);
		displayOrder++;
		newUnitList.add(unit);
	    }
	}

	request.setAttribute(QbConstants.ATTR_UNIT_LIST, newUnitList);
	return "qb/authoring/unitlist";
    }

    @RequestMapping(path = "/checkQuestionExistsInToolActivities")
    @ResponseBody
    public String checkQuestionExistsInToolActivities(@RequestParam(name = "toolContentIds[]") Set<Long> toolContentIds,
	    @RequestParam long qbQuestionUid, HttpServletResponse response) {
	response.setContentType("application/json;charset=utf-8");
	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	Collection<ToolContent> toolContents = qbService.getQuestionActivities(qbQuestionUid, toolContentIds);
	for (ToolContent toolContent : toolContents) {
	    responseJSON.add(toolContent.getToolContentId());
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/replaceQuestionInToolActivities", method = RequestMethod.POST)
    @ResponseBody
    public void replaceQuestionInToolActivities(@RequestParam(name = "toolContentIds[]") Set<Long> toolContentIds,
	    @RequestParam long oldQbQuestionUid, @RequestParam long newQbQuestionUid) {
	qbService.replaceQuestionInToolActivities(toolContentIds, oldQbQuestionUid, newQbQuestionUid);
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
	    case QbQuestion.TYPE_VERY_SHORT_ANSWERS:
		forward = "qb/authoring/addVsa";
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

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}