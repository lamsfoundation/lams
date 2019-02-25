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

package org.lamsfoundation.lams.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolAdapterContentManager;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.LamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	int questionType;
	boolean questionTypeHidden;
	if (CommonConstants.TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature) || CommonConstants.TOOL_SIGNATURE_MCQ.equals(toolSignature)) {
	    questionType = QbQuestion.TYPE_MULTIPLE_CHOICE_SINGLE_ANSWER;
	    questionTypeHidden = true;
	    
	} else {
	    questionType = 2;
	    questionTypeHidden = false;
	}
	request.setAttribute("questionType", questionType);
	request.setAttribute("questionTypeHidden", questionTypeHidden);
	
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
    private String getPagedQuestions(HttpServletRequest request, HttpServletResponse response) {
	
	Integer questionType = WebUtil.readIntParam(request, "questionType");
//	boolean isPrivate = WebUtil.readBooleanParam(request, "isPrivate");

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
	List<QbQuestion> questions = qbService.getPagedQbQuestions(questionType, page - 1, rowLimit, sortBy, sortOrder,
		searchString);
	int countQuestions = qbService.getCountQbQuestions(questionType, searchString);
	int totalPages = Double.valueOf(Math.ceil(Double.valueOf(countQuestions) / Double.valueOf(rowLimit)))
		.intValue();

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 1;
	for (QbQuestion question : questions) {

	    ArrayNode questionData = JsonNodeFactory.instance.arrayNode();
	    questionData.add(question.getUid());
	    questionData.add(HtmlUtils.htmlEscape(question.getName()));
	    String description = question.getDescription() == null ? "" : question.getDescription().replaceAll("\\<.*?\\>", "");
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
	
	return "qb/qbQuestionDetails";
    }
    
    @RequestMapping("/addQuestion")
    private String addQuestion(HttpServletRequest request) {
	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	
	return "qb/";
    }
}