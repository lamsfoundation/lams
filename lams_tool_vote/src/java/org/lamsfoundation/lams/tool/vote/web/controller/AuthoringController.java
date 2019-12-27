/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/authoring")
public class AuthoringController implements VoteAppConstants {
    private static Logger logger = Logger.getLogger(AuthoringController.class.getName());

    @Autowired
    private IVoteService voteService;

    @Autowired
    @Qualifier("lavoteMessageService")
    private MessageService messageService;

    /**
     * repopulateRequestParameters reads and saves request parameters
     */
    private static void repopulateRequestParameters(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm,
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteAuthoringForm.setToolContentID(toolContentID);
	voteGeneralAuthoringDTO.setToolContentID(toolContentID);

	String httpSessionID = request.getParameter(VoteAppConstants.HTTP_SESSION_ID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	String lockOnFinish = request.getParameter(VoteAppConstants.LOCK_ON_FINISH);
	voteAuthoringForm.setLockOnFinish(lockOnFinish);
	voteGeneralAuthoringDTO.setLockOnFinish(lockOnFinish);

	String useSelectLeaderToolOuput = request.getParameter(VoteAppConstants.USE_SELECT_LEADER_TOOL_OUTPUT);
	voteAuthoringForm.setUseSelectLeaderToolOuput(useSelectLeaderToolOuput);
	voteGeneralAuthoringDTO.setUseSelectLeaderToolOuput(useSelectLeaderToolOuput);

	String allowText = request.getParameter(VoteAppConstants.ALLOW_TEXT);
	voteAuthoringForm.setAllowText(allowText);
	voteGeneralAuthoringDTO.setAllowText(allowText);

	String showResults = request.getParameter(VoteAppConstants.SHOW_RESULTS);
	voteAuthoringForm.setShowResults(showResults);
	voteGeneralAuthoringDTO.setShowResults(showResults);

	String maxNominationCount = request.getParameter(VoteAppConstants.MAX_NOMINATION_COUNT);
	voteAuthoringForm.setMaxNominationCount(maxNominationCount);
	voteGeneralAuthoringDTO.setMaxNominationCount(maxNominationCount);

	String minNominationCount = request.getParameter(MIN_NOMINATION_COUNT);
	voteAuthoringForm.setMinNominationCount(minNominationCount);
	voteGeneralAuthoringDTO.setMinNominationCount(minNominationCount);

	String reflect = request.getParameter("reflect");
	voteAuthoringForm.setReflect(reflect);
	voteGeneralAuthoringDTO.setReflect(reflect);

	String reflectionSubject = request.getParameter("reflectionSubject");
	voteAuthoringForm.setReflectionSubject(reflectionSubject);
	voteGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);

	String maxInputs = request.getParameter(VoteAppConstants.MAX_INPUTS);
	if (maxInputs == null) {
	    logger.info("Since minNomcount is equal to null hence setting it to '0'");
	    maxInputs = "0";
	}
	voteAuthoringForm.setMaxInputs(new Short(maxInputs));

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
    }

    /**
     * moves a nomination down in the authoring list
     */
    @RequestMapping(path = "/moveNominationDown", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String moveNominationDown(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	questionDTOs = AuthoringController.swapQuestions(questionDTOs, questionIndex, "down");

	questionDTOs = AuthoringController.reorderQuestionDTOs(questionDTOs);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	return "/authoring/AuthoringMaincontent";
    }

    /**
     * moves a nomination up in the authoring list
     */
    @RequestMapping(path = "/moveNominationUp", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String moveNominationUp(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	questionDTOs = AuthoringController.swapQuestions(questionDTOs, questionIndex, "up");

	questionDTOs = AuthoringController.reorderQuestionDTOs(questionDTOs);

	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	return "/authoring/AuthoringMaincontent";
    }

    /**
     * removes a nomination from the authoring list
     */
    @RequestMapping(path = "/removeNomination", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String removeNomination(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndexToDelete = request.getParameter("questionIndex");
	logger.info("Question Index to delete" + questionIndexToDelete);
	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	List<VoteQuestionDTO> listFinalQuestionDTO = new LinkedList<>();
	int queIndex = 0;
	for (VoteQuestionDTO questionDTO : questionDTOs) {

	    String questionText = questionDTO.getNomination();
	    String displayOrder = questionDTO.getDisplayOrder();

	    if (questionText != null && !questionText.equals("") && (!displayOrder.equals(questionIndexToDelete))) {

		++queIndex;
		questionDTO.setDisplayOrder(new Integer(queIndex).toString());
		listFinalQuestionDTO.add(questionDTO);
	    }
	    if ((questionText != null) && (!questionText.isEmpty()) && displayOrder.equals(questionIndexToDelete)) {
		List<VoteQuestionDTO> deletedQuestionDTOs = (List<VoteQuestionDTO>) sessionMap
			.get(LIST_DELETED_QUESTION_DTOS);
		;
		deletedQuestionDTOs.add(questionDTO);
		sessionMap.put(LIST_DELETED_QUESTION_DTOS, deletedQuestionDTOs);
	    }
	}

	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, listFinalQuestionDTO);
	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, listFinalQuestionDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	return "/authoring/AuthoringMaincontent";
    }

    /**
     * enables editing a nomination
     */
    @RequestMapping("/newEditableNominationBox")
    @SuppressWarnings("unchecked")
    public String newEditableNominationBox(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	logger.info("Question Index" + questionIndex);
	voteAuthoringForm.setEditableNominationIndex(questionIndex);

	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	String editableNomination = "";
	Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();
	    // String question = voteQuestionDTO.getNomination();
	    String displayOrder = voteQuestionDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableNomination = voteQuestionDTO.getNomination();
		    break;
		}

	    }
	}
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	voteGeneralAuthoringDTO.setEditableNominationText(editableNomination);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	return "/authoring/editNominationBox";
    }

    /**
     * enables adding a new nomination
     */
    @RequestMapping("/newNominationBox")
    public String newNominationBox(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	return "/authoring/newNominationBox";
    }

    /**
     * enables adding a new nomination to the authoring nominations list
     */
    @RequestMapping(path = "/addSingleNomination", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String addSingleNomination(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	String newNomination = request.getParameter("newNomination");

	int listSize = questionDTOs.size();

	if (newNomination != null && newNomination.length() > 0) {
	    boolean duplicates = AuthoringController.checkDuplicateNominations(questionDTOs, newNomination);

	    if (!duplicates) {
		VoteQuestionDTO voteQuestionDTO = new VoteQuestionDTO();
		voteQuestionDTO.setDisplayOrder(new Long(listSize + 1).toString());
		voteQuestionDTO.setNomination(newNomination);

		questionDTOs.add(voteQuestionDTO);
	    }
	}

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	String richTextTitle = (String) sessionMap.get(VoteAppConstants.ACTIVITY_TITLE_KEY);
	String richTextInstructions = (String) sessionMap.get(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	return "/authoring/itemlist";
    }

    /**
     * saves a new or updated nomination in the authoring nominations list
     */
    @RequestMapping(path = "/saveSingleNomination", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String saveSingleNomination(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String editNominationBoxRequest = request.getParameter("editNominationBoxRequest");

	logger.info("Edit nomination box request" + editNominationBoxRequest);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	String newNomination = request.getParameter("newNomination");

	String editableNominationIndex = request.getParameter("editableNominationIndex");

	if (newNomination != null && newNomination.length() > 0) {
	    if (editNominationBoxRequest != null && editNominationBoxRequest.equals("false")) {
		boolean duplicates = AuthoringController.checkDuplicateNominations(questionDTOs, newNomination);

		if (!duplicates) {
		    VoteQuestionDTO voteQuestionDTO = null;
		    Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
		    while (iter.hasNext()) {
			voteQuestionDTO = iter.next();

			//String question = voteQuestionDTO.getNomination();
			String displayOrder = voteQuestionDTO.getDisplayOrder();

			if (displayOrder != null && !displayOrder.equals("")) {
			    if (displayOrder.equals(editableNominationIndex)) {
				break;
			    }

			}
		    }

		    voteQuestionDTO.setQuestion(newNomination);
		    voteQuestionDTO.setDisplayOrder(editableNominationIndex);

		    questionDTOs = AuthoringController.reorderUpdateListQuestionDTO(questionDTOs, voteQuestionDTO,
			    editableNominationIndex);
		} else {
		    logger.info("Duplicate question entry therefore not adding");
		    //duplicate question entry, not adding
		}
	    } else {
		logger.info("In Request for Save and Edit");
		//request for edit and save
		VoteQuestionDTO voteQuestionDTO = null;
		Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
		while (iter.hasNext()) {
		    voteQuestionDTO = iter.next();

		    // String question = voteQuestionDTO.getNomination();
		    String displayOrder = voteQuestionDTO.getDisplayOrder();

		    if (displayOrder != null && !displayOrder.equals("")) {
			if (displayOrder.equals(editableNominationIndex)) {
			    break;
			}

		    }
		}

		voteQuestionDTO.setNomination(newNomination);
		voteQuestionDTO.setDisplayOrder(editableNominationIndex);

		questionDTOs = AuthoringController.reorderUpdateListQuestionDTO(questionDTOs, voteQuestionDTO,
			editableNominationIndex);
	    }
	} else {
	    logger.info("newNomination entry is blank,therefore not adding");
	    //entry blank, not adding
	}

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	String richTextTitle = (String) sessionMap.get(VoteAppConstants.ACTIVITY_TITLE_KEY);
	String richTextInstructions = (String) sessionMap.get(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	return "/authoring/itemlist";
    }

    /**
     * persists the nominations list and other user selections in the db.
     */
    @RequestMapping(path = "/submitAllContent", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String submitAllContent(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	List<VoteQuestionDTO> questionDTOs = (List<VoteQuestionDTO>) sessionMap.get(VoteAppConstants.LIST_QUESTION_DTO);

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	if (questionDTOs.isEmpty() && (voteAuthoringForm.getAssignedDataFlowObject() == null
		|| voteAuthoringForm.getAssignedDataFlowObject() == 0)) {
	    errorMap.add("nominations", messageService.getMessage("nominations.none.submitted"));
	    logger.error("Nominations not submitted");
	}

	String maxNomCount = voteAuthoringForm.getMaxNominationCount();
	if (maxNomCount != null) {
	    if (maxNomCount.equals("0") || maxNomCount.contains("-")) {
		errorMap.add("maxNominationCount", messageService.getMessage("maxNomination.invalid"));
		logger.error("Maximum votes in Advance tab is invalid");
	    }

	    try {
		//int intMaxNomCount = new Integer(maxNomCount).intValue();
	    } catch (NumberFormatException e) {
		errorMap.add("maxNominationCount", messageService.getMessage("maxNomination.invalid"));
		logger.error("Maximum votes in Advance tab is invalid");
	    }
	}

	//verifyDuplicateNominations
	Map<String, String> mapQuestion = AuthoringController.extractMapQuestion(questionDTOs);
	int optionCount = 0;
	boolean isNominationsDuplicate = false;
	for (long i = 1; i <= VoteAppConstants.MAX_OPTION_COUNT; i++) {
	    String currentOption = mapQuestion.get(new Long(i).toString());

	    optionCount = 0;
	    for (long j = 1; j <= VoteAppConstants.MAX_OPTION_COUNT; j++) {
		String backedOption = mapQuestion.get(new Long(j).toString());

		if (currentOption != null && backedOption != null) {
		    if (currentOption.equals(backedOption)) {
			optionCount++;
		    }

		    if (optionCount > 1) {
			isNominationsDuplicate = true;
		    }
		}
	    }
	}

	if (isNominationsDuplicate == true) {
	    errorMap.add("nominations", messageService.getMessage("nominations.duplicate"));
	    logger.error("There are duplicate nomination entries.");
	}

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	DataFlowObject assignedDataFlowObject = null;

	List<DataFlowObject> dataFlowObjects = voteService.getDataFlowObjects(new Long(strToolContentID));
	List<String> dataFlowObjectNames = null;
	if (dataFlowObjects != null) {
	    dataFlowObjectNames = new ArrayList<>(dataFlowObjects.size());
	    int objectIndex = 1;
	    for (DataFlowObject dataFlowObject : dataFlowObjects) {
		dataFlowObjectNames.add(dataFlowObject.getDisplayName());
		if (VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID.equals(dataFlowObject.getToolAssigmentId())) {
		    voteAuthoringForm.setAssignedDataFlowObject(objectIndex);
		}
		objectIndex++;

	    }
	}
	voteGeneralAuthoringDTO.setDataFlowObjectNames(dataFlowObjectNames);

	if (voteAuthoringForm.getAssignedDataFlowObject() != null
		&& voteAuthoringForm.getAssignedDataFlowObject() != 0) {
	    assignedDataFlowObject = dataFlowObjects.get(voteAuthoringForm.getAssignedDataFlowObject() - 1);
	}

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	VoteContent voteContentTest = voteService.getVoteContent(new Long(strToolContentID));

	if (errorMap.isEmpty()) {
	    ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	    List<VoteQuestionDTO> deletedQuestionDTOs = (List<VoteQuestionDTO>) sessionMap
		    .get(LIST_DELETED_QUESTION_DTOS);

	    // in case request is from monitoring module - recalculate User Answers
	    if (mode.isTeacher()) {
		Set<VoteQueContent> oldQuestions = voteContentTest.getVoteQueContents();
		voteService.removeQuestionsFromCache(voteContentTest);
		VoteUtils.setDefineLater(request, false, strToolContentID, voteService);

		// audit log the teacher has started editing activity in monitor
		voteService.auditLogStartEditingActivityInMonitor(new Long(strToolContentID));

		// recalculate User Answers
		voteService.recalculateUserAnswers(voteContentTest, oldQuestions, questionDTOs, deletedQuestionDTOs);
	    }

	    // remove deleted questions
	    for (VoteQuestionDTO deletedQuestionDTO : deletedQuestionDTOs) {
		VoteQueContent removeableQuestion = voteService.getVoteQueContentByUID(deletedQuestionDTO.getUid());
		if (removeableQuestion != null) {
//		    Set<McUsrAttempt> attempts = removeableQuestion.getMcUsrAttempts();
//		    Iterator<McUsrAttempt> iter = attempts.iterator();
//		    while (iter.hasNext()) {
//			McUsrAttempt attempt = iter.next();
//			iter.remove();
//		    }
//		    mcService.updateQuestion(removeableQuestion);
		    voteContentTest.getVoteQueContents().remove(removeableQuestion);
		    voteService.removeVoteQueContent(removeableQuestion);
		}
	    }

	    // store content
	    VoteContent voteContent = saveOrUpdateVoteContent(voteAuthoringForm, request, voteContentTest,
		    strToolContentID);

	    //store questions
	    voteContent = voteService.createQuestions(questionDTOs, voteContent);

	    //store DataFlowObjectAssigment
	    voteService.saveDataFlowObjectAssigment(assignedDataFlowObject);

	    //reOrganizeDisplayOrder
	    List<VoteQueContent> sortedQuestions = voteService.getAllQuestionsSorted(voteContent.getUid().longValue());
	    Iterator<VoteQueContent> iter = sortedQuestions.iterator();
	    while (iter.hasNext()) {
		VoteQueContent question = iter.next();

		VoteQueContent existingQuestion = voteService.getQuestionByUid(question.getUid());
		voteService.saveOrUpdateVoteQueContent(existingQuestion);
	    }

	    // standard authoring close
	    request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	} else {
	    request.setAttribute("errorMap", errorMap);
	}

	voteAuthoringForm.resetUserAction();

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setCurrentTab("1");

	return "/authoring/AuthoringMaincontent";
    }

    @RequestMapping("/start")
    public String start(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	return readDatabaseData(voteAuthoringForm, request, mode);
    }
    
    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteUtils.setDefineLater(request, true, strToolContentId, voteService);

	return readDatabaseData(voteAuthoringForm, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request, ToolAccessMode mode) {
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	// model.addAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	AuthoringController.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	SessionMap<String, Object> sessionMap = new SessionMap<>();
	voteAuthoringForm.setHttpSessionID(sessionMap.getSessionID());
	voteGeneralAuthoringDTO.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	voteAuthoringForm.resetRadioBoxes();
	voteAuthoringForm.setExceptionMaxNominationInvalid(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setExceptionMaxNominationInvalid(new Boolean(false).toString());

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	boolean validateSignature = validateDefaultContent(voteAuthoringForm, errorMap);
	if (!validateSignature) {
	    request.setAttribute("errorMap", errorMap);
	    return "/error";
	}

	//no problems getting the default content, will render authoring screen

	/* the authoring url must be passed a tool content id */
	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	voteAuthoringForm.setToolContentID(new Long(strToolContentId).toString());
	voteGeneralAuthoringDTO.setToolContentID(new Long(strToolContentId).toString());

	if (strToolContentId == null || strToolContentId.equals("")) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.contentId.required"));
	    request.setAttribute("errorMap", errorMap);
	    return "/error";
	}

	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	VoteContent voteContent = voteService.getVoteContent(new Long(strToolContentId));

	// if mcContent does not exist, try to use default content instead.
	if (voteContent == null) {
	    long defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    voteContent = voteService.getVoteContent(defaultContentID);
	    voteContent = VoteContent.newInstance(voteContent, new Long(strToolContentId));
	}

	AuthoringController.prepareDTOandForm(request, voteContent, voteAuthoringForm, voteGeneralAuthoringDTO);
	if (voteContent.getTitle() == null) {
	    voteGeneralAuthoringDTO.setActivityTitle(VoteAppConstants.DEFAULT_VOTING_TITLE);
	    voteAuthoringForm.setTitle(VoteAppConstants.DEFAULT_VOTING_TITLE);
	} else {
	    voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
	    voteAuthoringForm.setTitle(voteContent.getTitle());
	}

	if (voteContent.getInstructions() == null) {
	    voteGeneralAuthoringDTO.setActivityInstructions(VoteAppConstants.DEFAULT_VOTING_INSTRUCTIONS);
	    voteAuthoringForm.setInstructions(VoteAppConstants.DEFAULT_VOTING_INSTRUCTIONS);
	} else {
	    voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());
	    voteAuthoringForm.setInstructions(voteContent.getInstructions());
	}

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, voteGeneralAuthoringDTO.getActivityTitle());
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, voteGeneralAuthoringDTO.getActivityInstructions());

	voteAuthoringForm.setReflectionSubject(voteContent.getReflectionSubject());
	voteGeneralAuthoringDTO.setReflectionSubject(voteContent.getReflectionSubject());

	List<DataFlowObject> dataFlowObjects = voteService.getDataFlowObjects(new Long(strToolContentId));
	if (dataFlowObjects != null) {
	    List<String> dataFlowObjectNames = new ArrayList<>(dataFlowObjects.size());
	    int objectIndex = 1;
	    for (DataFlowObject dataFlowObject : dataFlowObjects) {
		dataFlowObjectNames.add(dataFlowObject.getDisplayName());
		if (VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID.equals(dataFlowObject.getToolAssigmentId())) {
		    voteAuthoringForm.setAssignedDataFlowObject(objectIndex);
		}
		objectIndex++;

	    }
	    voteGeneralAuthoringDTO.setDataFlowObjectNames(dataFlowObjectNames);
	}

	List<VoteQuestionDTO> questionDTOs = new LinkedList<>();

	for (VoteQueContent question : voteContent.getVoteQueContents()) {
	    VoteQuestionDTO questionDTO = new VoteQuestionDTO();

	    questionDTO.setUid(question.getUid());
	    questionDTO.setQuestion(question.getQuestion());
	    questionDTO.setDisplayOrder(new Integer(question.getDisplayOrder()).toString());
	    questionDTOs.add(questionDTO);
	}

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, questionDTOs);

	Short maxInputs = voteContent.getMaxExternalInputs();
	if (maxInputs == null) {
	    maxInputs = 0;
	}
	voteAuthoringForm.setMaxInputs(maxInputs);
	voteAuthoringForm.resetUserAction();

	List<VoteQuestionDTO> listDeletedQuestionDTOs = new ArrayList<>();
	sessionMap.put(VoteAppConstants.LIST_DELETED_QUESTION_DTOS, listDeletedQuestionDTOs);

	voteAuthoringForm.resetUserAction();
	voteAuthoringForm.setCurrentTab("1");

	return "/authoring/AuthoringMaincontent";
    }

    /**
     * each tool has a signature. Voting tool's signature is stored in MY_SIGNATURE. The default tool content id and
     * other depending content ids are obtained in this method. if all the default content has been setup properly the
     * method saves DEFAULT_CONTENT_ID in the session.
     *
     * @param request
     * @param mapping
     * @return ActionForward
     */
    private boolean validateDefaultContent(VoteAuthoringForm voteAuthoringForm,
	    MultiValueMap<String, String> errorMap) {
	/*
	 * retrieve the default content id based on tool signature
	 */
	long defaultContentID = 0;
	try {
	    defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    if (defaultContentID == 0) {
		logger.error("Exception occured: No default content");
		errorMap.add("GLOBAL", messageService.getMessage("error.defaultContent.notSetup"));
		return false;
	    }
	} catch (Exception e) {
	    logger.error("error getting the default content id: " + e.getMessage());
	    errorMap.add("GLOBAL", messageService.getMessage("error.defaultContent.notSetup"));
	    return false;
	}

	try {
	    //retrieve uid of the content based on default content id determined above defaultContentID
	    VoteContent voteContent = voteService.getVoteContent(new Long(defaultContentID));
	    if (voteContent == null) {
		logger.error("Exception occured: No default content");
		errorMap.add("GLOBAL", messageService.getMessage("error.defaultContent.notSetup"));
		return false;
	    }
	} catch (Exception e) {
	    logger.error("other problems: " + e);
	    logger.error("Exception occured: No default question content");
	    errorMap.add("GLOBAL", messageService.getMessage("error.defaultContent.notSetup"));
	    return false;
	}

	return true;
    }

    private static void prepareDTOandForm(HttpServletRequest request, VoteContent voteContent,
	    VoteAuthoringForm voteAuthoringForm, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {

	voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

	voteAuthoringForm.setUseSelectLeaderToolOuput(voteContent.isUseSelectLeaderToolOuput() ? "1" : "0");
	voteAuthoringForm.setAllowText(voteContent.isAllowText() ? "1" : "0");
	voteAuthoringForm.setAllowTextEntry(voteContent.isAllowText() ? "1" : "0");

	voteAuthoringForm.setShowResults(voteContent.isShowResults() ? "1" : "0");

	voteAuthoringForm.setLockOnFinish(voteContent.isLockOnFinish() ? "1" : "0");
	voteAuthoringForm.setReflect(voteContent.isReflect() ? "1" : "0");

	voteGeneralAuthoringDTO.setUseSelectLeaderToolOuput(voteContent.isUseSelectLeaderToolOuput() ? "1" : "0");
	voteGeneralAuthoringDTO.setAllowText(voteContent.isAllowText() ? "1" : "0");
	voteGeneralAuthoringDTO.setLockOnFinish(voteContent.isLockOnFinish() ? "1" : "0");
	voteAuthoringForm.setReflect(voteContent.isReflect() ? "1" : "0");

	String maxNomcount = voteContent.getMaxNominationCount();
	if (maxNomcount.equals("")) {
	    logger.info("Since minNomcount is equal to null hence setting it to '0'");
	    maxNomcount = "0";
	}
	voteAuthoringForm.setMaxNominationCount(maxNomcount);
	voteGeneralAuthoringDTO.setMaxNominationCount(maxNomcount);

	String minNomcount = voteContent.getMinNominationCount();
	if ((minNomcount == null) || minNomcount.equals("")) {
	    logger.info("Since minNomcount is equal to null hence setting it to '0'");
	    minNomcount = "0";
	}
	voteAuthoringForm.setMinNominationCount(minNomcount);
	voteGeneralAuthoringDTO.setMinNominationCount(minNomcount);
    }

    private static List<VoteQuestionDTO> swapQuestions(List<VoteQuestionDTO> questionDTOs, String questionIndex,
	    String direction) {

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;

	int replacedQuestionIndex = 0;
	if (direction.equals("down")) {
	    replacedQuestionIndex = ++intQuestionIndex;
	} else {
	    replacedQuestionIndex = --intQuestionIndex;
	}

	VoteQuestionDTO mainQuestion = AuthoringController.getQuestionAtDisplayOrder(questionDTOs,
		intOriginalQuestionIndex);

	VoteQuestionDTO replacedQuestion = AuthoringController.getQuestionAtDisplayOrder(questionDTOs,
		replacedQuestionIndex);

	List<VoteQuestionDTO> newQuestionDtos = new LinkedList<VoteQuestionDTO>();

	Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO questionDTO = iter.next();
	    VoteQuestionDTO tempQuestion = new VoteQuestionDTO();

	    if (!questionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())
		    && !questionDTO.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		logger.info("Normal Copy");
		// normal copy
		tempQuestion = questionDTO;

	    } else if (questionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		// move type 1
		logger.info("Move type 1");
		tempQuestion = replacedQuestion;

	    } else if (questionDTO.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		// move type 1
		logger.info("Move type 1");
		tempQuestion = mainQuestion;
	    }

	    newQuestionDtos.add(tempQuestion);
	}

	return newQuestionDtos;
    }

    private static VoteQuestionDTO getQuestionAtDisplayOrder(List<VoteQuestionDTO> questionDTOs,
	    int intOriginalQuestionIndex) {

	Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();

	    if (new Integer(intOriginalQuestionIndex).toString().equals(voteQuestionDTO.getDisplayOrder())) {
		return voteQuestionDTO;
	    }
	}
	return null;
    }

    private static List<VoteQuestionDTO> reorderQuestionDTOs(List<VoteQuestionDTO> listQuestionDTO) {
	List<VoteQuestionDTO> listFinalQuestionDTO = new LinkedList<VoteQuestionDTO>();

	int queIndex = 0;
	Iterator<VoteQuestionDTO> iter = listQuestionDTO.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();

	    String question = voteQuestionDTO.getNomination();

	    //  String displayOrder = voteQuestionDTO.getDisplayOrder();

	    if (question != null && !question.equals("")) {
		++queIndex;

		voteQuestionDTO.setNomination(question);
		voteQuestionDTO.setDisplayOrder(new Integer(queIndex).toString());

		listFinalQuestionDTO.add(voteQuestionDTO);
	    }
	}

	return listFinalQuestionDTO;
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkDuplicateNominations(List<VoteQuestionDTO> listQuestionDTO, String newQuestion) {
	if (logger.isDebugEnabled()) {
	    logger.debug("New Question" + newQuestion);
	}

	Map<String, String> mapQuestion = AuthoringController.extractMapQuestion(listQuestionDTO);

	Iterator itMap = mapQuestion.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {

		if (pairs.getValue().equals(newQuestion)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private static Map<String, String> extractMapQuestion(List<VoteQuestionDTO> listQuestionDTO) {
	Map<String, String> mapQuestion = new TreeMap<String, String>(new VoteComparator());

	Iterator<VoteQuestionDTO> iter = listQuestionDTO.iterator();
	int queIndex = 0;
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();

	    queIndex++;
	    mapQuestion.put(new Integer(queIndex).toString(), voteQuestionDTO.getNomination());
	}
	return mapQuestion;
    }

    private static List<VoteQuestionDTO> reorderUpdateListQuestionDTO(List<VoteQuestionDTO> listQuestionDTO,
	    VoteQuestionDTO voteQuestionDTONew, String editableQuestionIndex) {

	List<VoteQuestionDTO> listFinalQuestionDTO = new LinkedList<VoteQuestionDTO>();

	Iterator<VoteQuestionDTO> iter = listQuestionDTO.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();

	    String question = voteQuestionDTO.getNomination();

	    String displayOrder = voteQuestionDTO.getDisplayOrder();

	    if (displayOrder.equals(editableQuestionIndex)) {
		voteQuestionDTO.setNomination(voteQuestionDTONew.getNomination());
		voteQuestionDTO.setDisplayOrder(voteQuestionDTONew.getDisplayOrder());

		listFinalQuestionDTO.add(voteQuestionDTO);
	    } else {
		voteQuestionDTO.setNomination(question);
		voteQuestionDTO.setDisplayOrder(displayOrder);

		listFinalQuestionDTO.add(voteQuestionDTO);
	    }
	}

	return listFinalQuestionDTO;
    }

    private VoteContent saveOrUpdateVoteContent(VoteAuthoringForm voteAuthoringForm, HttpServletRequest request,
	    VoteContent voteContent, String strToolContentID) {
	if (logger.isDebugEnabled()) {
	    logger.debug("ToolContentID" + strToolContentID);
	}
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	String lockOnFinish = request.getParameter("lockOnFinish");

	String allowTextEntry = request.getParameter("allowText");

	String showResults = request.getParameter("showResults");

	String maxInputs = request.getParameter("maxInputs");

	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");

	String reflect = request.getParameter(VoteAppConstants.REFLECT);

	String reflectionSubject = voteAuthoringForm.getReflectionSubject();

	String maxNomcount = voteAuthoringForm.getMaxNominationCount();

	String minNomcount = voteAuthoringForm.getMinNominationCount();

	boolean lockOnFinishBoolean = false;
	boolean allowTextEntryBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;
	boolean reflectBoolean = false;
	boolean showResultsBoolean = false;
	short maxInputsShort = 0;

	if (lockOnFinish != null && lockOnFinish.equalsIgnoreCase("1")) {
	    lockOnFinishBoolean = true;
	}

	if (allowTextEntry != null && allowTextEntry.equalsIgnoreCase("1")) {
	    allowTextEntryBoolean = true;
	}

	if (useSelectLeaderToolOuput != null && useSelectLeaderToolOuput.equalsIgnoreCase("1")) {
	    useSelectLeaderToolOuputBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}

	if (showResults != null && showResults.equalsIgnoreCase("1")) {
	    showResultsBoolean = true;
	}

	if (maxInputs != null && !"0".equals(maxInputs)) {
	    maxInputsShort = Short.parseShort(maxInputs);
	}

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		userId = 0;
	    }
	}

	boolean newContent = false;
	if (voteContent == null) {
	    voteContent = new VoteContent();
	    newContent = true;
	}

	voteContent.setVoteContentId(new Long(strToolContentID));
	voteContent.setTitle(richTextTitle);
	voteContent.setInstructions(richTextInstructions);
	voteContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	voteContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	voteContent.setLockOnFinish(lockOnFinishBoolean);
	voteContent.setAllowText(allowTextEntryBoolean);
	voteContent.setShowResults(showResultsBoolean);
	voteContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	voteContent.setReflect(reflectBoolean);
	voteContent.setMaxNominationCount(maxNomcount);
	voteContent.setMinNominationCount(minNomcount);

	voteContent.setReflectionSubject(reflectionSubject);

	voteContent.setMaxExternalInputs(maxInputsShort);

	if (newContent) {
	    logger.info("In New Content");
	    voteService.saveVoteContent(voteContent);
	} else {
	    voteService.updateVote(voteContent);
	}

	voteContent = voteService.getVoteContent(new Long(strToolContentID));

	return voteContent;
    }
}