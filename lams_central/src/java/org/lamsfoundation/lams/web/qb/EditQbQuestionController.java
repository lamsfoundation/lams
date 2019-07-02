package org.lamsfoundation.lams.web.qb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.qb.service.QbUtils;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
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
     * Display empty page for new assessment question.
     */
    @RequestMapping("/newQuestionInit")
    public String newQuestionInit(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam Long collectionUid) throws ServletException, IOException {

	//TODO think about where do we need to get ContentFolderID, and whether it's a good idea to generate a new one each time
	String contentFolderID = FileUtil.generateUniqueContentFolderID();

	QbQuestionForm questionForm = new QbQuestionForm();
	//we need to set form as a request attribute, as long as we use jsps from another context from the Assessment tool
	request.setAttribute("assessmentQuestionForm", questionForm);
	questionForm.setDisplayOrder(-1);//which signifies it's a new question
	questionForm.setContentFolderID(contentFolderID);
	questionForm.setMaxMark("1");
	questionForm.setPenaltyFactor("0");
	questionForm.setAnswerRequired(true);
	questionForm.setCollectionUid(collectionUid);

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

	Integer type = NumberUtils.toInt(request.getParameter(QbConstants.ATTR_QUESTION_TYPE));
//	sessionMap.put(QbConstants.ATTR_QUESTION_TYPE, type);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	String jspPageName = EditQbQuestionController.getAuthoringJspByQuestionType(type);
	forwardToAssessmentJsp(jspPageName, request, response);
	return null;
    }

    /**
     * Display edit page for existed assessment question.
     */
    @RequestMapping("/editQuestion")
    public String editQuestion(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(defaultValue = "-1") Long collectionUid) throws ServletException, IOException {
	Long qbQuestionUid = WebUtil.readLongParam(request, "qbQuestionUid");
	QbQuestion qbQuestion = qbService.getQbQuestionByUid(qbQuestionUid);
	if (qbQuestion == null) {
	    throw new RuntimeException("QbQuestion with uid:" + qbQuestionUid + " was not found!");
	}
	QbQuestionForm questionForm = new QbQuestionForm();
	questionForm.setCollectionUid(collectionUid);
	//we need to set form as a request attribute, as long as we use jsps from another context from the Assessment tool
	request.setAttribute("assessmentQuestionForm", questionForm);
	QbUtils.fillFormWithQbQuestion(qbQuestion, questionForm, request);

	//store uid as displayOrder in order to use it later during question saving
	questionForm.setDisplayOrder(qbQuestionUid.intValue());

	//TODO think about where do we need to get ContentFolderID, and whether it's a good idea to generate a new one each time
	String contentFolderID = FileUtil.generateUniqueContentFolderID();
	questionForm.setContentFolderID(contentFolderID);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	String jspPageName = EditQbQuestionController.getAuthoringJspByQuestionType(qbQuestion.getType());
	forwardToAssessmentJsp(jspPageName, request, response);
	return null;
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     * 
     * @throws IOException
     */
    @RequestMapping("/saveOrUpdateQuestion")
    @ResponseBody
    public String saveOrUpdateQuestion(@ModelAttribute("assessmentQuestionForm") QbQuestionForm questionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	//find according question
	QbQuestion qbQuestion = null;
	Long oldQuestionUid = null;

	// add
	if (questionForm.getDisplayOrder() == -1) {
	    qbQuestion = new QbQuestion();
	    qbQuestion.setType(questionForm.getQuestionType());

	    // edit
	} else {
	    oldQuestionUid = Long.valueOf(questionForm.getDisplayOrder());
	    qbQuestion = qbService.getQbQuestionByUid(oldQuestionUid);
	}

	boolean IS_AUTHORING_RESTRICTED = false;
	int isQbQuestionModified = QbUtils.extractFormToQbQuestion(qbQuestion, questionForm, request, qbService,
		IS_AUTHORING_RESTRICTED);
	switch (isQbQuestionModified) {
	    case IQbService.QUESTION_MODIFIED_VERSION_BUMP: {
		// new version of the old question gets created
		qbQuestion = qbQuestion.clone();
		qbQuestion.clearID();
		qbQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()));
		qbQuestion.setCreateDate(new Date());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_ID_BUMP: {
		// new question gets created
		qbQuestion = qbQuestion.clone();
		qbQuestion.clearID();
		qbQuestion.setVersion(1);
		qbQuestion.setQuestionId(qbService.getMaxQuestionId() + 1);
		qbQuestion.setCreateDate(new Date());

	    }
		break;
	}
	boolean belongsToNoCollection = qbQuestion.getUid() == null;
	userManagementService.save(qbQuestion);

	//in case of new question - add it to specified collection
	if (belongsToNoCollection) {

	    Long collectionUid = questionForm.getCollectionUid();
	    //try to get collection from the old question
	    if (collectionUid != null && collectionUid.equals(-1L)) {
		Collection<QbCollection> existingCollections = qbService.getQuestionCollectionsByUid(oldQuestionUid);
		collectionUid = existingCollections.stream().findFirst().map(collection -> collection.getUid())
			.orElse(null);
	    }

	    if (collectionUid != null && !collectionUid.equals(-1L)) {
		qbService.addQuestionToCollection(collectionUid, qbQuestion.getQuestionId(), false);
	    }
	}

	// add question case - return nothing
	if (questionForm.getDisplayOrder() == -1) {
	    return null;

	    // edit question case - return question's uid
	} else {
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    return qbQuestion.getUid().toString();
	}
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/addOption")
    public String addOption(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	TreeSet<QbOption> optionList = QbUtils.getOptionsFromRequest(qbService, request, false);
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
	forwardToAssessmentJsp("optionlist.jsp", request, response);
	return null;
    }

    /**
     * Ajax call, will add one more input line for new Unit.
     */
    @RequestMapping("/newUnit")
    public String newUnit(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	TreeSet<QbQuestionUnit> unitList = QbUtils.getUnitsFromRequest(qbService, request, false);
	QbQuestionUnit unit = new QbQuestionUnit();
	int maxSeq = 1;
	if ((unitList != null) && (unitList.size() > 0)) {
	    QbQuestionUnit last = unitList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	unit.setDisplayOrder(maxSeq);
	unitList.add(unit);

	request.setAttribute(QbConstants.ATTR_UNIT_LIST, unitList);
	forwardToAssessmentJsp("unitlist.jsp", request, response);
	return null;
    }

    /**
     * Get back jsp name.
     */
    private static String getAuthoringJspByQuestionType(Integer type) {
	String jspName;
	switch (type) {
	    case QbQuestion.TYPE_MULTIPLE_CHOICE:
		jspName = "addmultiplechoice.jsp";
		break;
	    case QbQuestion.TYPE_MATCHING_PAIRS:
		jspName = "addmatchingpairs.jsp";
		break;
	    case QbQuestion.TYPE_SHORT_ANSWER:
		jspName = "addshortanswer.jsp";
		break;
	    case QbQuestion.TYPE_NUMERICAL:
		jspName = "addnumerical.jsp";
		break;
	    case QbQuestion.TYPE_TRUE_FALSE:
		jspName = "addtruefalse.jsp";
		break;
	    case QbQuestion.TYPE_ESSAY:
		jspName = "addessay.jsp";
		break;
	    case QbQuestion.TYPE_ORDERING:
		jspName = "addordering.jsp";
		break;
	    case QbQuestion.TYPE_MARK_HEDGING:
		jspName = "addmarkhedging.jsp";
		break;
	    default:
		jspName = null;
		break;
	}
	return jspName;
    }

    /**
     * Forwards to the specified jsp page from Assessment tool.
     */
    private void forwardToAssessmentJsp(String jspPageName, HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String serverURLContextPath = Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH);
	serverURLContextPath = serverURLContextPath.startsWith("/") ? serverURLContextPath : "/" + serverURLContextPath;
	serverURLContextPath += serverURLContextPath.endsWith("/") ? "" : "/";
	applicationcontext.getServletContext()
		.getContext(serverURLContextPath + "tool/" + CommonConstants.TOOL_SIGNATURE_ASSESSMENT)
		.getRequestDispatcher("/pages/authoring/parts/" + jspPageName + "?lessonID=1")
		.forward(request, response);
    }
}
