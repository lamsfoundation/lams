/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web.qb;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/searchQB")
public class SearchQBController {

    private static Logger log = Logger.getLogger(SearchQBController.class);
    
    public static final String PARAM_SEARCH = "_search";
    public static final String PARAM_SEARCH_FIELD = "searchField";
    public static final String PARAM_SEARCH_OPERATION = "searchOper";
    public static final String PARAM_SEARCH_STRING = "searchString";

    @Autowired
    private IQbService qbService;

    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("/start")
    private String start(HttpServletRequest request) {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	ToolContent toolContent = (ToolContent) userManagementService.findById(ToolContent.class, toolContentId);
	if (toolContent == null) {
	    String error = "The toolContentID " + toolContentId
		    + " is not valid. No such record exists on the database.";
	    throw new DataMissingException(error);
	}
	Tool tool = toolContent.getTool();
	String toolSignature = tool.getToolSignature();

	//empty questionTypesAvailable means no other questionTypes available for this tool
	StringBuilder questionTypesAvailable = new StringBuilder(); 
	//by default show MCQ type of questions (except for Q&A tool)
	int questionTypeDefault = QbQuestion.TYPE_MULTIPLE_CHOICE;
	if (CommonConstants.TOOL_SIGNATURE_MCQ.equals(toolSignature)) {
	   
	} else if (CommonConstants.TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature)) {
	    questionTypesAvailable.append(QbQuestion.TYPE_MULTIPLE_CHOICE);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_VERY_SHORT_ANSWERS);
	    
	//CommonConstants.TOOL_SIGNATURE_SURVEY
	} else if ("lasurv11".equals(toolSignature)) {
	    questionTypesAvailable.append(QbQuestion.TYPE_MULTIPLE_CHOICE);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_ESSAY);
	    
	} else if (CommonConstants.TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature)) {
	    questionTypesAvailable.append(QbQuestion.TYPE_MULTIPLE_CHOICE);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_MATCHING_PAIRS);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_VERY_SHORT_ANSWERS);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_NUMERICAL);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_TRUE_FALSE);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_ESSAY);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_ORDERING);
	    questionTypesAvailable.append(",");
	    questionTypesAvailable.append(QbQuestion.TYPE_MARK_HEDGING);
	   
	//CommonConstants.TOOL_SIGNATURE_QA
	} else if ("laqa11".equals(toolSignature)) {
	    questionTypeDefault = QbQuestion.TYPE_ESSAY;
	}
	request.setAttribute("questionType", questionTypeDefault);
	request.setAttribute("questionTypesAvailable", questionTypesAvailable.toString());
	//let jsp know it's Scratchie, so we can disable VSA questions not compatible with TBL
	request.setAttribute("isScratchie", CommonConstants.TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature));
	
	//prepare data for displaying collections
	Integer userId = getUserId();
	Collection<QbCollection> userCollections = qbService.getUserCollections(userId);
	request.setAttribute("userCollections", userCollections);
	
	return "qb/search";
    }
    
    /**
     * Returns an xml representation of the lesson grid for a course for gradebook
     *
     * This has two modes, learnerView and monitorView
     *
     * Learner view will get the data specific to one user
     *
     * Monitor will get the data average for whole lessons.
     */
    @RequestMapping("/getPagedQuestions")
    @ResponseBody
    private String getPagedQuestions(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(required = false) String questionTypes,
	    @RequestParam(required = false) String collectionUids) {
	if (StringUtils.isEmpty(questionTypes)) {
	    questionTypes = null;
	}
	if (StringUtils.isEmpty(collectionUids)) {
	    collectionUids = null;
	}

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "searchString", true);

	// Get the user list from the db
	List<QbQuestion> questions = (List<QbQuestion>) qbService.getPagedQuestions(questionTypes, collectionUids,
		page - 1, rowLimit, sortBy, sortOrder, searchString);
	int countQuestions = qbService.getCountQuestions(questionTypes, collectionUids, searchString);
	int totalPages = Double.valueOf(Math.ceil(Double.valueOf(countQuestions) / Double.valueOf(rowLimit)))
		.intValue();

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 1;
	for (QbQuestion question : questions) {

	    ArrayNode questionData = JsonNodeFactory.instance.arrayNode();
	    questionData.add(question.getUid());
	    String title = question.getName() == null ? "" : question.getName().replaceAll("\\<.*?\\>", "").replaceAll("\\n", " ").trim();
	    questionData.add(HtmlUtils.htmlEscape(title));
	    String description = question.getDescription() == null ? "" : question.getDescription().replaceAll("\\<.*?\\>", "").trim();
	    questionData.add(HtmlUtils.htmlEscape(description));

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", questionData);

	    rows.add(userRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countQuestions);
	responseJSON.set("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }
    
    @RequestMapping("/displayQuestionDetails")
    private String displayQuestionDetails(HttpServletRequest request) {
	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	QbQuestion qbQuestion = (QbQuestion) userManagementService.findById(QbQuestion.class, questionUid);
	request.setAttribute("question", qbQuestion);
	
	boolean isVsaAndCompatibleWithTbl = qbQuestion.isVsaAndCompatibleWithTbl();
	request.setAttribute("isVsaAndCompatibleWithTbl", isVsaAndCompatibleWithTbl);
	boolean isScratchie = WebUtil.readBooleanParam(request, "isScratchie", false);
	request.setAttribute("isScratchie", isScratchie);
	
	List<QbQuestion> otherVersions = qbService.getQuestionsByQuestionId(qbQuestion.getQuestionId());
	request.setAttribute("otherVersions", otherVersions);
	
	return "qb/qbQuestionDetails";
    }
    
    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}