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

/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.EditActivityDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.tool.mc.util.McToolContentHandler;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * Action class that controls the logic of tool behavior.
 * </p>
 * 
 * <p>
 * Note that Struts action class only has the responsibility to navigate page flow. All database operation should go to
 * service layer and data transformation from domain model to struts form bean should go to form bean class. This ensure
 * clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is identified. No system exception error
 * handling code should appear in the Struts action class as all of them are handled in
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 * @author Ozgur Demirtas
 * 
 *         <action path="/authoring" type="org.lamsfoundation.lams.tool.mc.web.McAction" name="McAuthoringForm"
 *         input="/AuthoringMaincontent.jsp" parameter="dispatch" scope="request" unknown="false" validate="false" >
 * 
 *         <forward name="load" path="/AuthoringMaincontent.jsp" redirect="false" />
 * 
 *         <forward name="loadMonitoring" path="/monitoring/MonitoringMaincontent.jsp" redirect="false" />
 * 
 *         <forward name="refreshMonitoring" path="/monitoring/MonitoringMaincontent.jsp" redirect="false" />
 * 
 *         <forward name="loadViewOnly" path="/authoring/AuthoringTabsHolder.jsp" redirect="false" />
 * 
 *         <forward name="newQuestionBox" path="/authoring/newQuestionBox.jsp" redirect="false" />
 * 
 *         <forward name="editQuestionBox" path="/authoring/editQuestionBox.jsp" redirect="false" />
 * 
 * 
 *         <forward name="starter" path="/index.jsp" redirect="false" /> </action>
 * 
 */
public class McAction extends LamsDispatchAction implements McAppConstants {
    static Logger logger = Logger.getLogger(McAction.class.getName());

    private McToolContentHandler toolContentHandler;

    /**
     * <p>
     * Struts dispatch method.
     * </p>
     * 
     * <p>
     * It is assuming that progress engine should pass in the tool access mode and the tool session id as http
     * parameters.
     * </p>
     * 
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * 
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     * @throws McApplicationException
     *             the known runtime exception
     * 
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * 
     * ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) throws IOException, ServletException
     * 
     * submits content into the tool database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAction.logger.debug("dispathcing submitAllContent :" + form);

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	Map mapQuestionContent = AuthoringUtil.extractMapQuestionContent(listQuestionContentDTO);
	McAction.logger.debug("extracted mapQuestionContent: " + mapQuestionContent);

	Map mapFeedback = AuthoringUtil.extractMapFeedback(listQuestionContentDTO);
	McAction.logger.debug("extracted mapFeedback: " + mapFeedback);

	Map mapWeights = new TreeMap(new McComparator());

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	Map mapMarks = AuthoringUtil.extractMapMarks(listQuestionContentDTO);
	McAction.logger.debug("extracted mapMarks: " + mapMarks);

	Map mapCandidatesList = AuthoringUtil.extractMapCandidatesList(listQuestionContentDTO);
	McAction.logger.debug("extracted mapCandidatesList: " + mapCandidatesList);

	ActionMessages errors = new ActionMessages();
	McAction.logger.debug("mapQuestionContent size: " + mapQuestionContent.size());

	if (mapQuestionContent.size() == 0) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}
	McAction.logger.debug("errors: " + errors);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    List attachmentListBackup = new ArrayList();
	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    attachmentListBackup = attachmentList;

	    List deletedAttachmentListBackup = new ArrayList();
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);
	    deletedAttachmentListBackup = deletedAttachmentList;

	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOfflineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);

	}

	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	McAction.logger.debug("richTextTitle: " + richTextTitle);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	mcGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);

	McAction.logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	McAction.logger.debug("there are no issues with input, continue and submit data");

	McContent mcContentTest = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContentTest: " + mcContentTest);

	McAction.logger.debug("errors: " + errors);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	McContent mcContent = mcContentTest;
	if (errors.isEmpty()) {
	    McAction.logger.debug("errors is empty: " + errors);
	    authoringUtil.removeRedundantQuestions(mapQuestionContent, mcService, mcAuthoringForm, request,
		    strToolContentID);
	    McAction.logger.debug("end of removing unused entries... ");

	    mcContent = authoringUtil.saveOrUpdateMcContent(mapQuestionContent, mapFeedback, mapWeights, mapMarks,
		    mapCandidatesList, mcService, mcAuthoringForm, request, mcContentTest, strToolContentID);
	    McAction.logger.debug("mcContent: " + mcContent);

	    long defaultContentID = 0;
	    McAction.logger.debug("attempt retrieving tool with signatute : " + McAppConstants.MY_SIGNATURE);
	    defaultContentID = mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    McAction.logger.debug("retrieved tool default contentId: " + defaultContentID);

	    if (mcContent != null) {
		mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }
	    McAction.logger.debug("updated mcGeneralAuthoringDTO to: " + mcGeneralAuthoringDTO);

	    authoringUtil.reOrganizeDisplayOrder(mapQuestionContent, mcService, mcAuthoringForm, mcContent);
	    McAction.logger.debug("post reOrganizeDisplayOrder: " + mcContent);

	    McAction.logger.debug("activeModule: " + activeModule);
	    if (activeModule.equals(McAppConstants.AUTHORING)) {

		List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
		McAction.logger.debug("attachmentList: " + attachmentList);

		List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);

		List attachments = saveAttachments(mcContent, attachmentList, deletedAttachmentList, mapping, request);
		McAction.logger.debug("attachments: " + attachments);
	    }

	    McAction.logger.debug("strToolContentID: " + strToolContentID);
	    McUtils.setDefineLater(request, false, strToolContentID, mcService);
	    McAction.logger.debug("define later set to false");

	    McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		    defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    if (activeModule.equals(McAppConstants.AUTHORING)) {
		McAction.logger.debug("standard authoring close");
		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    } else {
		McAction.logger.debug("go back to view only screen");
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	    }

	} else {
	    McAction.logger.debug("errors is not empty: " + errors);

	    if (mcContent != null) {
		long defaultContentID = 0;
		McAction.logger.debug("attempt retrieving tool with signatute : " + McAppConstants.MY_SIGNATURE);
		defaultContentID = mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
		McAction.logger.debug("retrieved tool default contentId: " + defaultContentID);

		if (mcContent != null) {
		    mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

		McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
			defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    }
	}

	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());

	mcAuthoringForm.resetUserAction();
	mcGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	McAction.logger.debug("forwarding to :" + McAppConstants.LOAD_QUESTIONS);
	return mapping.findForward(McAppConstants.LOAD_QUESTIONS);
    }

    /**
     * 
     * saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAction.logger.debug("dispathcing saveSingleQuestion");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);

	String passmark = request.getParameter("passmark");
	McAction.logger.debug("passmark: " + passmark);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	McAction.logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	caList = AuthoringUtil.removeBlankEntries(caList);
	McAction.logger.debug("caList after removing blank entries: " + caList);

	boolean validateSingleCorrectCandidate = authoringUtil.validateSingleCorrectCandidate(caList);
	McAction.logger.debug("validateSingleCorrectCandidate: " + validateSingleCorrectCandidate);

	boolean validateOnlyOneCorrectCandidate = authoringUtil.validateOnlyOneCorrectCandidate(caList);
	McAction.logger.debug("validateOnlyOneCorrectCandidate: " + validateOnlyOneCorrectCandidate);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!validateSingleCorrectCandidate) {
	    ActionMessage error = new ActionMessage("candidates.none.correct");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!validateOnlyOneCorrectCandidate) {
	    ActionMessage error = new ActionMessage("candidates.duplicate.correct");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	McAction.logger.debug("errors: " + errors);

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	McAction.logger.debug("entry using mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	if (errors.isEmpty()) {
	    McAction.logger.debug("errors is empty: " + errors);
	    McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	    McAction.logger.debug("mcContent: " + mcContent);

	    McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	    mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	    mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	    String newQuestion = request.getParameter("newQuestion");
	    McAction.logger.debug("newQuestion: " + newQuestion);

	    String feedback = request.getParameter("feedback");
	    McAction.logger.debug("feedback: " + feedback);

	    String editableQuestionIndex = request.getParameter("editableQuestionIndex");
	    McAction.logger.debug("editableQuestionIndex: " + editableQuestionIndex);
	    mcAuthoringForm.setQuestionIndex(editableQuestionIndex);

	    if ((newQuestion != null) && (newQuestion.length() > 0)) {
		if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false"))) {
		    McAction.logger.debug("request for add and save");
		    boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
		    McAction.logger.debug("duplicates: " + duplicates);

		    if (!duplicates) {
			McQuestionContentDTO mcQuestionContentDTO = null;
			Iterator listIterator = listQuestionContentDTO.iterator();
			while (listIterator.hasNext()) {
			    mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
			    McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
			    McAction.logger
				    .debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());

			    String question = mcQuestionContentDTO.getQuestion();
			    String displayOrder = mcQuestionContentDTO.getDisplayOrder();
			    McAction.logger.debug("displayOrder:" + displayOrder);

			    if ((displayOrder != null) && (!displayOrder.equals(""))) {
				if (displayOrder.equals(editableQuestionIndex)) {
				    break;
				}

			    }
			}
			McAction.logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);

			mcQuestionContentDTO.setQuestion(newQuestion);
			mcQuestionContentDTO.setFeedback(feedback);
			mcQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
			mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
			mcQuestionContentDTO.setMark(mark);

			McAction.logger
				.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
			mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
				.size()).toString());

			listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(
				listQuestionContentDTO, mcQuestionContentDTO, editableQuestionIndex);
			McAction.logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: "
				+ listQuestionContentDTO);
		    } else {
			McAction.logger.debug("duplicate question entry, not adding");
		    }
		} else {
		    McAction.logger.debug("request for edit and save.");
		    McQuestionContentDTO mcQuestionContentDTO = null;
		    Iterator listIterator = listQuestionContentDTO.iterator();
		    while (listIterator.hasNext()) {
			mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
			McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
			McAction.logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());

			String question = mcQuestionContentDTO.getQuestion();
			String displayOrder = mcQuestionContentDTO.getDisplayOrder();
			McAction.logger.debug("displayOrder:" + displayOrder);

			if ((displayOrder != null) && (!displayOrder.equals(""))) {
			    if (displayOrder.equals(editableQuestionIndex)) {
				break;
			    }

			}
		    }
		    McAction.logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);

		    mcQuestionContentDTO.setQuestion(newQuestion);
		    mcQuestionContentDTO.setFeedback(feedback);
		    mcQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
		    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		    mcQuestionContentDTO.setMark(mark);

		    McAction.logger.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
		    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
			    .size()).toString());

		    listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO,
			    mcQuestionContentDTO, editableQuestionIndex);
		    McAction.logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: "
			    + listQuestionContentDTO);
		}
	    } else {
		McAction.logger.debug("entry blank, not adding");
	    }

	    McAction.logger.debug("entryusing mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	    sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	    commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		    defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	    request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	    McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	    return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
	} else {
	    McAction.logger.debug("errors is not empty: " + errors);

	    commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		    defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	    McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	    Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    McAction.logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	    McAction.logger.debug("totalMark: " + totalMark);
	    mcAuthoringForm.setTotalMarks(totalMark);
	    mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	    request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	    sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	    request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());

	    McAction.logger.debug("forwarding using newEditableQuestionBox");
	    return newEditableQuestionBox(mapping, form, request, response);
	}

    }

    /**
     * Parses questions extracted from IMS QTI file and adds them to currently edited question.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward saveQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// big part of code was taken from addSingleQuestion() and saveSingleQuestion() methods
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);
	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	List<McQuestionContentDTO> listQuestionContentDTO = (List<McQuestionContentDTO>) sessionMap
		.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	// proper parsing
	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);

	for (Question question : questions) {
	    // quietly do same verification as in other question-adding methods
	    String questionText = question.getText();
	    if (StringUtils.isBlank(questionText)) {
		LamsDispatchAction.log.warn("Skipping a blank question.");
		continue;
	    }

	    questionText = QuestionParser.processHTMLField(questionText, false, contentFolderID,
		    question.getResourcesFolderPath());

	    if (AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, questionText)) {
		LamsDispatchAction.log.warn("Skipping duplicate question: " + questionText);
		continue;
	    }

	    List<McCandidateAnswersDTO> caList = new ArrayList<McCandidateAnswersDTO>();
	    String correctAnswer = null;
	    Integer correctAnswerScore = 1;

	    if (question.getAnswers() != null) {
		for (Answer answer : question.getAnswers()) {
		    McCandidateAnswersDTO mcCandidateAnswersDTO = new McCandidateAnswersDTO();
		    String answerText = QuestionParser.processHTMLField(answer.getText(), true, null, null);
		    if (answerText == null) {
			LamsDispatchAction.log.warn("Skipping a blank answer");
			continue;
		    }
		    if (correctAnswer != null && correctAnswer.equals(answerText)) {
			LamsDispatchAction.log.warn("Skipping an answer with same text as the correct answer: "
				+ answerText);

			continue;
		    }

		    mcCandidateAnswersDTO.setCandidateAnswer(answerText);

		    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			if (correctAnswer == null) {
			    mcCandidateAnswersDTO.setCorrect("Correct");
			    correctAnswer = mcCandidateAnswersDTO.getCandidateAnswer();
			    // marks are integer numbers
			    correctAnswerScore = Math.min(new Double(Math.ceil(answer.getScore())).intValue(), 10);
			} else {
			    // there can be only one correct answer in a MCQ question
			    LamsDispatchAction.log
				    .warn("Choosing only first correct answer, despite another one was found: "
					    + answerText);
			    mcCandidateAnswersDTO.setCorrect("Incorrect");
			}
		    } else {
			mcCandidateAnswersDTO.setCorrect("Incorrect");
		    }

		    caList.add(mcCandidateAnswersDTO);
		}
	    }

	    if (correctAnswer == null) {
		LamsDispatchAction.log.warn("No correct answer found for question: " + questionText);
		continue;
	    }

	    McQuestionContentDTO mcQuestionContentDTO = new McQuestionContentDTO();
	    mcQuestionContentDTO.setDisplayOrder(String.valueOf(listQuestionContentDTO.size() + 1));
	    mcQuestionContentDTO.setQuestion(questionText);
	    mcQuestionContentDTO.setFeedback(QuestionParser.processHTMLField(question.getFeedback(), true, null, null));
	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
	    mcQuestionContentDTO.setMark(correctAnswerScore.toString());
	    mcQuestionContentDTO.setCaCount(String.valueOf(caList.size()));

	    listQuestionContentDTO.add(mcQuestionContentDTO);

	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Added question: " + questionText);
	    }
	}

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	return (mapping.findForward(McAppConstants.LOAD));
    }

    /**
     * commonSaveCode(HttpServletRequest request, McGeneralAuthoringDTO mcGeneralAuthoringDTO, McAuthoringForm
     * mcAuthoringForm, SessionMap sessionMap, String activeModule, String strToolContentID, String defaultContentIdStr,
     * IMcService mcService, String httpSessionID, List listQuestionContentDTO)
     * 
     * @param request
     * @param mcGeneralAuthoringDTO
     * @param mcAuthoringForm
     * @param sessionMap
     * @param activeModule
     * @param strToolContentID
     * @param defaultContentIdStr
     * @param mcService
     * @param httpSessionID
     * @param listQuestionContentDTO
     */
    protected void commonSaveCode(HttpServletRequest request, McGeneralAuthoringDTO mcGeneralAuthoringDTO,
	    McAuthoringForm mcAuthoringForm, SessionMap sessionMap, String activeModule, String strToolContentID,
	    String defaultContentIdStr, IMcService mcService, String httpSessionID, List listQuestionContentDTO) {
	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	McAction.logger.debug("richTextTitle: " + richTextTitle);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);

	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOfflineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);

	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	McAction.logger.debug("httpSessionID: " + httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	request.getSession().setAttribute(httpSessionID, sessionMap);
	McAction.logger.debug("mcGeneralAuthoringDTO.getMapQuestionContent(); "
		+ mcGeneralAuthoringDTO.getMapQuestionContent());
    }

    /**
     * addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAction.logger.debug("dispathcing addSingleQuestion");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY);
	McAction.logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	McQuestionContentDTO mcQuestionContentDTONew = null;

	int listSize = listQuestionContentDTO.size();
	McAction.logger.debug("listSize: " + listSize);

	McAction.logger.debug("listAddableQuestionContentDTO now: " + listAddableQuestionContentDTO);
	request.setAttribute(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	String passmark = request.getParameter("passmark");
	McAction.logger.debug("passmark: " + passmark);
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	McAction.logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	caList = AuthoringUtil.removeBlankEntries(caList);
	McAction.logger.debug("caList after removing blank entries: " + caList);

	boolean validateSingleCorrectCandidate = authoringUtil.validateSingleCorrectCandidate(caList);
	McAction.logger.debug("validateSingleCorrectCandidate: " + validateSingleCorrectCandidate);

	boolean validateOnlyOneCorrectCandidate = authoringUtil.validateOnlyOneCorrectCandidate(caList);
	McAction.logger.debug("validateOnlyOneCorrectCandidate: " + validateOnlyOneCorrectCandidate);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!validateSingleCorrectCandidate) {
	    ActionMessage error = new ActionMessage("candidates.none.correct");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!validateOnlyOneCorrectCandidate) {
	    ActionMessage error = new ActionMessage("candidates.duplicate.correct");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	McAction.logger.debug("errors: " + errors);

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	McAction.logger.debug("errors saved: " + errors);

	if (errors.isEmpty()) {
	    if ((newQuestion != null) && (newQuestion.length() > 0)) {
		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
		McAction.logger.debug("duplicates: " + duplicates);

		if (!duplicates) {
		    McQuestionContentDTO mcQuestionContentDTO = new McQuestionContentDTO();
		    mcQuestionContentDTO.setDisplayOrder(new Long(listSize + 1).toString());
		    mcQuestionContentDTO.setFeedback(feedback);
		    mcQuestionContentDTO.setQuestion(newQuestion);
		    mcQuestionContentDTO.setMark(mark);

		    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		    McAction.logger.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
		    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
			    .size()).toString());

		    listQuestionContentDTO.add(mcQuestionContentDTO);
		    McAction.logger.debug("updated listQuestionContentDTO: " + listQuestionContentDTO);
		} else {
		    McAction.logger.debug("entry duplicate, not adding");
		}
	    } else {
		McAction.logger.debug("entry blank, not adding");
	    }
	} else {
	    McAction.logger.debug("errors, not adding");

	    McAction.logger.debug("errors is not empty: " + errors);

	    commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		    defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	    McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	    Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    McAction.logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	    McAction.logger.debug("totalMark: " + totalMark);
	    mcAuthoringForm.setTotalMarks(totalMark);
	    mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	    request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	    sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	    McAction.logger.debug("forwarding using newQuestionBox");
	    return newQuestionBox(mapping, form, request, response);
	}

	McAction.logger.debug("entry using mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("before forwarding mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);

	McAction.logger.debug("fwd LOAD_QUESTIONS");
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * opens up an new screen within the current page for adding a new question
     * 
     * newQuestionBox
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispathcing newQuestionBox");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	/* create default mcContent object*/
	McContent mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	McAction.logger.debug("richTextTitle: " + richTextTitle);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	String requestType = request.getParameter("requestType");
	McAction.logger.debug("requestType: " + requestType);

	List listAddableQuestionContentDTO = (List) sessionMap.get(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY);
	McAction.logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	if ((requestType != null) && (requestType.equals("direct"))) {
	    McAction.logger.debug("requestType is direct");
	    listAddableQuestionContentDTO = authoringUtil.buildDefaultQuestionContent(mcContent, mcService);
	    McAction.logger.debug("listAddableQuestionContentDTO from db: " + listAddableQuestionContentDTO);
	}

	request.setAttribute(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	McAction.logger.debug("final listQuestionContentDTO: " + listQuestionContentDTO);
	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);

	McAction.logger.debug("fwd ing to newQuestionBox: ");
	return (mapping.findForward("newQuestionBox"));
    }

    /**
     * newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * opens up an new screen within the current page for editing a question
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispathcing newEditableQuestionBox");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	request.setAttribute(McAppConstants.CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	mcAuthoringForm.setEditableQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	String editableQuestion = "";
	String editableFeedback = "";
	String editableMark = "";
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	    McAction.logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	    String question = mcQuestionContentDTO.getQuestion();
	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = mcQuestionContentDTO.getFeedback();
		    editableQuestion = mcQuestionContentDTO.getQuestion();
		    editableMark = mcQuestionContentDTO.getMark();
		    McAction.logger.debug("editableFeedback found :" + editableFeedback);

		    List candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
		    McAction.logger.debug("candidates found :" + candidates);

		    break;
		}

	    }
	}
	McAction.logger.debug("editableFeedback found :" + editableFeedback);
	McAction.logger.debug("editableQuestion found :" + editableQuestion);
	McAction.logger.debug("editableMark found :" + editableMark);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = (McGeneralAuthoringDTO) request
		.getAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO);
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);

	if (mcGeneralAuthoringDTO == null) {
	    mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	}

	mcGeneralAuthoringDTO.setMarkValue(editableMark);

	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	McAction.logger.debug("richTextTitle: " + richTextTitle);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setEditableQuestionText(editableQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionFeedback(editableFeedback);
	mcAuthoringForm.setFeedback(editableFeedback);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String requestNewEditableQuestionBox = (String) request.getAttribute("requestNewEditableQuestionBox");
	McAction.logger.debug("requestNewEditableQuestionBox: " + requestNewEditableQuestionBox);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);

	// if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false")))
	if ((requestNewEditableQuestionBox != null) && requestNewEditableQuestionBox.equals("true")) {
	    // String newQuestion=request.getParameter("newQuestion");
	    // logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	    String feedback = request.getParameter("feedback");
	    McAction.logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	}

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	McAction.logger.debug("final listQuestionContentDTO: " + listQuestionContentDTO);
	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	McAction.logger.debug("fwd ing to editQuestionBox: ");
	return (mapping.findForward("editQuestionBox"));
    }

    /**
     * 
     * ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) throws IOException, ServletException
     * 
     * removes a question from the questions map
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching removeQuestion");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	McQuestionContentDTO mcQuestionContentDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	    McAction.logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());

	    String question = mcQuestionContentDTO.getQuestion();
	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();
	    McAction.logger.debug("displayOrder:" + displayOrder);

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	McAction.logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
	mcQuestionContentDTO.setQuestion("");
	McAction.logger.debug("listQuestionContentDTO after remove:" + listQuestionContentDTO);

	listQuestionContentDTO = AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex);
	McAction.logger.debug("listQuestionContentDTO reordered:" + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	if (mcContent == null) {
	    McAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	}
	McAction.logger.debug("final mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("mcQuestionContentDTO now: " + mcQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * moves a question down in the list
     * 
     * moveQuestionDown
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching moveQuestionDown");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down");
	McAction.logger.debug("listQuestionContentDTO after swap: " + listQuestionContentDTO);

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO after reordersimple: " + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * *
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching moveQuestionUp");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up");
	McAction.logger.debug("listQuestionContentDTO after swap: " + listQuestionContentDTO);

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO after reordersimple: " + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * 
     * ActionForward addNewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response) throws IOException, ServletException
     * 
     * adds a new file to content repository
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addNewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispathching addNewFile");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String onlineInstructions = request.getParameter(McAppConstants.ONLINE_INSTRUCTIONS);
	McAction.logger.debug("onlineInstructions: " + onlineInstructions);

	String offlineInstructions = request.getParameter(McAppConstants.OFFLINE_INSTRUCTIONS);
	McAction.logger.debug("offlineInstructions: " + offlineInstructions);

	sessionMap.put(McAppConstants.ONLINE_INSTRUCTIONS_KEY, onlineInstructions);
	sessionMap.put(McAppConstants.OFFLINE_INSTRUCTIONS, offlineInstructions);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	McAction.logger.debug("richTextTitle: " + richTextTitle);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	McAction.logger.debug("attachmentList: " + attachmentList);
	List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	addFileToContentRepository(request, mcAuthoringForm, attachmentList, deletedAttachmentList, sessionMap,
		mcGeneralAuthoringDTO);
	McAction.logger.debug("post addFileToContentRepository, attachmentList: " + attachmentList);
	McAction.logger.debug("post addFileToContentRepository, deletedAttachmentList: " + deletedAttachmentList);

	sessionMap.put(McAppConstants.ATTACHMENT_LIST_KEY, attachmentList);
	sessionMap.put(McAppConstants.DELETED_ATTACHMENT_LIST_KEY, deletedAttachmentList);

	mcGeneralAuthoringDTO.setAttachmentList(attachmentList);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	mcAuthoringForm.resetUserAction();

	String strOnlineInstructions = request.getParameter("onlineInstructions");
	String strOfflineInstructions = request.getParameter("offlineInstructions");
	McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * 
     * ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response) throws IOException, ServletException
     * 
     * deletes a file from the content repository
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching deleteFile");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	McAction.logger.debug("onlineInstructions: " + onlineInstructions);

	String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	McAction.logger.debug("offlineInstructions: " + offlineInstructions);

	mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
	mcAuthoringForm.setOnlineInstructions(onlineInstructions);
	mcAuthoringForm.setOfflineInstructions(offlineInstructions);

	String richTextTitle = (String) sessionMap.get(McAppConstants.ACTIVITY_TITLE_KEY);
	String richTextInstructions = (String) sessionMap.get(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY);

	McAction.logger.debug("richTextTitle: " + richTextTitle);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	long uuid = WebUtil.readLongParam(request, McAppConstants.UUID);

	List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	McAction.logger.debug("attachmentList: " + attachmentList);

	if (attachmentList == null) {
	    attachmentList = new ArrayList();
	}

	List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	if (deletedAttachmentList == null) {
	    deletedAttachmentList = new ArrayList();
	}

	/* move the file's details from the attachment collection to the deleted attachments collection
	 the attachment will be delete on saving. */

	deletedAttachmentList = McUtils.moveToDelete(Long.toString(uuid), attachmentList, deletedAttachmentList);

	sessionMap.put(McAppConstants.ATTACHMENT_LIST_KEY, attachmentList);
	sessionMap.put(McAppConstants.DELETED_ATTACHMENT_LIST_KEY, deletedAttachmentList);

	mcGeneralAuthoringDTO.setAttachmentList(attachmentList);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	mcAuthoringForm.resetUserAction();
	McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    /**
     * persistError(HttpServletRequest request, String message)
     * 
     * persists error messages to request scope
     * 
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	McAction.logger.debug("add " + message + "  to ActionMessages:");
	saveErrors(request, errors);
    }

    /**
     * addFileToContentRepository(HttpServletRequest request, McAuthoringForm mcAuthoringForm, List attachmentList, List
     * deletedAttachmentList, SessionMap sessionMap, McGeneralAuthoringDTO mcGeneralAuthoringDTO)
     * 
     * @param request
     * @param mcAuthoringForm
     */
    public void addFileToContentRepository(HttpServletRequest request, McAuthoringForm mcAuthoringForm,
	    List attachmentList, List deletedAttachmentList, SessionMap sessionMap,
	    McGeneralAuthoringDTO mcGeneralAuthoringDTO) {
	McAction.logger.debug("attempt addFileToContentRepository");
	McAction.logger.debug("attachmentList: " + attachmentList);
	McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	if (attachmentList == null) {
	    attachmentList = new ArrayList();
	}

	if (deletedAttachmentList == null) {
	    deletedAttachmentList = new ArrayList();
	}

	FormFile uploadedFile = null;
	boolean isOnlineFile = false;
	String fileType = null;
	if ((mcAuthoringForm.getTheOfflineFile() != null) && (mcAuthoringForm.getTheOfflineFile().getFileSize() > 0)) {
	    McAction.logger.debug("theOfflineFile is available: ");
	    uploadedFile = mcAuthoringForm.getTheOfflineFile();
	    McAction.logger.debug("uploadedFile: " + uploadedFile);
	    fileType = IToolContentHandler.TYPE_OFFLINE;
	} else if ((mcAuthoringForm.getTheOnlineFile() != null)
		&& (mcAuthoringForm.getTheOnlineFile().getFileSize() > 0)) {
	    McAction.logger.debug("theOnlineFile is available: ");
	    uploadedFile = mcAuthoringForm.getTheOnlineFile();
	    McAction.logger.debug("uploadedFile: " + uploadedFile);
	    isOnlineFile = true;
	    fileType = IToolContentHandler.TYPE_ONLINE;
	} else {
	    /*no file uploaded*/
	    return;
	}

	// validate upload file size.
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(uploadedFile, true, errors);
	if (!errors.isEmpty()) {
	    this.saveErrors(request, errors);
	    return;
	}

	McAction.logger.debug("uploadedFile.getFileName(): " + uploadedFile.getFileName());

	/* if a file with the same name already exists then move the old one to deleted */
	deletedAttachmentList = McUtils.moveToDelete(uploadedFile.getFileName(), isOnlineFile, attachmentList,
		deletedAttachmentList);
	McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	try {
	    /* This is a new file and so is saved to the content repository. Add it to the 
	     attachments collection, but don't add it to the tool's tables yet.
	     */
	    NodeKey node = getToolContentHandler().uploadFile(uploadedFile.getInputStream(),
		    uploadedFile.getFileName(), uploadedFile.getContentType(), fileType);
	    McUploadedFile file = new McUploadedFile();
	    String fileName = uploadedFile.getFileName();
	    McAction.logger.debug("fileName: " + fileName);
	    McAction.logger.debug("fileName length: " + fileName.length());

	    if ((fileName != null) && (fileName.length() > 30)) {
		fileName = fileName.substring(0, 31);
		McAction.logger.debug("shortened fileName: " + fileName);
	    }

	    file.setFileName(fileName);
	    file.setFileOnline(isOnlineFile);
	    file.setUuid(node.getUuid().toString());
	    /* file.setVersionId(node.getVersion()); */

	    /* add the files to the attachment collection - if one existed, it should have already been removed. */
	    attachmentList.add(file);

	    /* reset the fields so that more files can be uploaded */
	    mcAuthoringForm.setTheOfflineFile(null);
	    mcAuthoringForm.setTheOnlineFile(null);
	} catch (FileNotFoundException e) {
	    McAction.logger.error("Unable to uploadfile", e);
	    throw new RuntimeException("Unable to upload file, exception was " + e.getMessage());
	} catch (IOException e) {
	    McAction.logger.error("Unable to uploadfile", e);
	    throw new RuntimeException("Unable to upload file, exception was " + e.getMessage());
	} catch (RepositoryCheckedException e) {
	    McAction.logger.error("Unable to uploadfile", e);
	    throw new RuntimeException("Unable to upload file, exception was " + e.getMessage());
	}
    }

    /**
     * McToolContentHandler getToolContentHandler()
     * 
     * @return
     */
    private McToolContentHandler getToolContentHandler() {
	if (toolContentHandler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    toolContentHandler = (McToolContentHandler) wac.getBean("mcToolContentHandler");
	}
	return toolContentHandler;
    }

    /**
     * 
     * Go through the attachments collections. Remove any content repository or tool objects matching entries in the the
     * deletedAttachments collection, add any new attachments in the attachments collection. Clear the
     * deletedAttachments collection, ready for new editing.
     * 
     * @param mcContent
     * @param attachmentList
     * @param deletedAttachmentList
     * @param mapping
     * @param request
     * @return
     */
    private List saveAttachments(McContent mcContent, List attachmentList, List deletedAttachmentList,
	    ActionMapping mapping, HttpServletRequest request) {

	McAction.logger.debug("start saveAttachments, mcContent " + mcContent);
	McAction.logger.debug("start saveAttachments, attachmentList " + attachmentList);
	McAction.logger.debug("start deletedAttachmentList, deletedAttachmentList " + deletedAttachmentList);

	if ((attachmentList == null) || (deletedAttachmentList == null)) {
	    return null;
	}

	IMcService voteService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("voteService: " + voteService);

	if (deletedAttachmentList != null) {
	    McAction.logger.debug("deletedAttachmentList is iterated...");
	    Iterator iter = deletedAttachmentList.iterator();
	    while (iter.hasNext()) {
		McUploadedFile attachment = (McUploadedFile) iter.next();
		McAction.logger.debug("attachment: " + attachment);

		if (attachment.getSubmissionId() != null) {
		    voteService.removeFile(attachment.getSubmissionId());
		}
	    }
	    deletedAttachmentList.clear();
	    McAction.logger.error("cleared attachment list.");
	}

	if (attachmentList != null) {
	    McAction.logger.debug("attachmentList is iterated...");
	    Iterator iter = attachmentList.iterator();
	    while (iter.hasNext()) {
		McUploadedFile attachment = (McUploadedFile) iter.next();
		McAction.logger.debug("attachment: " + attachment);
		McAction.logger.debug("attachment submission id: " + attachment.getSubmissionId());

		if (attachment.getSubmissionId() == null) {
		    /* add entry to tool table - file already in content repository */
		    McAction.logger.debug("calling persistFile with  attachment: " + attachment);
		    voteService.persistFile(mcContent, attachment);
		}
	    }
	}

	return deletedAttachmentList;
    }

    public ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching proxy editActivity...");
	return null;
    }

    /**
     * 
     * ActionForward editActivityQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) throws IOException, ServletException, ToolException
     * 
     * generates Edit Activity screen
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	McAction.logger.debug("dispatching editActivityQuestions...");

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	McAction.logger.debug("mcAuthoringForm: " + mcAuthoringForm);

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	McAction.logger.debug("title: " + mcContent.getTitle());
	McAction.logger.debug("instructions: " + mcContent.getInstructions());

	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcAuthoringForm.setTitle(mcContent.getTitle());

	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, mcContent.getTitle());
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, mcContent.getInstructions());

	/* determine whether the request is from Monitoring url Edit Activity*/
	String sourceMcStarter = (String) request.getAttribute(McAppConstants.SOURCE_MC_STARTER);
	McAction.logger.debug("sourceMcStarter: " + sourceMcStarter);

	mcAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	boolean isContentInUse = McUtils.isContentInUse(mcContent);
	McAction.logger.debug("isContentInUse:" + isContentInUse);

	mcGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    McAction.logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    persistError(request, "error.content.inUse");
	    mcGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	McAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(McAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	McUtils.setDefineLater(request, true, strToolContentID, mcService);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List listQuestionContentDTO = authoringUtil.buildDefaultQuestionContent(mcContent, mcService);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	request.getSession().setAttribute(httpSessionID, sessionMap);

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before fwding to jsp, mcAuthoringForm: " + mcAuthoringForm);
	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	McAction.logger.debug("forwarding to : " + McAppConstants.LOAD_QUESTIONS);
	return mapping.findForward(McAppConstants.LOAD_QUESTIONS);
    }

    /**
     * 
     * newEditableCaBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableCaBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching newEditableCaBox");
	return null;
    }

    /**
     * moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * moves a candidate dwn in the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching moveCandidateDown");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	McAction.logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();
	    String editableQuestion = "";
	    Iterator listIterator = listQuestionContentDTO.iterator();
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
		McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);

		McAction.logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
		String question = mcQuestionContentDTO.getQuestion();
		String displayOrder = mcQuestionContentDTO.getDisplayOrder();

		if ((displayOrder != null) && (!displayOrder.equals(""))) {
		    if (displayOrder.equals(questionIndex)) {
			McAction.logger.debug("displayOrder equals questionIndex :" + questionIndex);
			editableQuestion = mcQuestionContentDTO.getQuestion();

			candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
			McAction.logger.debug("candidates found :" + candidates);
			McAction.logger.debug("but we are using the repopulated caList here: " + caList);

			listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "down");
			McAction.logger.debug("swapped candidates :" + listCandidates);

			mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);

			break;
		    }

		}
	    }
	    McAction.logger.debug("candidates found :" + candidates);
	    McAction.logger.debug("swapped candidates is :" + listCandidates);
	}

	McAction.logger.debug("listQuestionContentDTO after swapped candidates :" + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return newEditableQuestionBox(mapping, form, request, response);
    }

    /**
     * moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * moves a candidate up in the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching moveCandidateUp");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	McAction.logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();
	    String editableQuestion = "";
	    Iterator listIterator = listQuestionContentDTO.iterator();
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
		McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);

		McAction.logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
		String question = mcQuestionContentDTO.getQuestion();
		String displayOrder = mcQuestionContentDTO.getDisplayOrder();

		if ((displayOrder != null) && (!displayOrder.equals(""))) {
		    if (displayOrder.equals(questionIndex)) {
			McAction.logger.debug("displayOrder equals questionIndex :" + questionIndex);
			editableQuestion = mcQuestionContentDTO.getQuestion();

			candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
			McAction.logger.debug("candidates found :" + candidates);

			McAction.logger.debug("using repopulated caList:" + caList);

			listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "up");
			McAction.logger.debug("swapped candidates :" + listCandidates);

			mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
			mcQuestionContentDTO.setCaCount(new Integer(listCandidates.size()).toString());

			break;
		    }

		}
	    }

	    McAction.logger.debug("candidates found :" + candidates);
	    McAction.logger.debug("swapped candidates is :" + listCandidates);
	}

	McAction.logger.debug("listQuestionContentDTO after swapped candidates :" + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return newEditableQuestionBox(mapping, form, request, response);
    }

    /**
     * removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * removes a candidate from the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching removeCandidate");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	McQuestionContentDTO mcQuestionContentDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	    McAction.logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());

	    String question = mcQuestionContentDTO.getQuestion();
	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();
	    McAction.logger.debug("displayOrder:" + displayOrder);

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	McAction.logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
	McAction.logger.debug("setting caList for the content:");
	mcQuestionContentDTO.setListCandidateAnswersDTO(caList);

	List candidateAnswers = mcQuestionContentDTO.getListCandidateAnswersDTO();
	McAction.logger.debug("candidateAnswers:" + candidateAnswers);

	McCandidateAnswersDTO mcCandidateAnswersDTO = null;
	Iterator listCaIterator = candidateAnswers.iterator();
	int caIndex = 0;
	while (listCaIterator.hasNext()) {
	    caIndex++;
	    McAction.logger.debug("caIndex:" + caIndex);
	    mcCandidateAnswersDTO = (McCandidateAnswersDTO) listCaIterator.next();
	    McAction.logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
	    McAction.logger.debug("mcCandidateAnswersDTO question:" + mcCandidateAnswersDTO.getCandidateAnswer());

	    if (caIndex == new Integer(candidateIndex).intValue()) {
		McAction.logger.debug("candidateIndex found");
		mcCandidateAnswersDTO.setCandidateAnswer("");

		break;
	    }
	}
	McAction.logger.debug("candidateAnswers after resetting answer" + candidateAnswers);

	candidateAnswers = AuthoringUtil.reorderListCandidatesDTO(candidateAnswers);
	McAction.logger.debug("candidateAnswers after reordering candidate nodes" + candidateAnswers);

	mcQuestionContentDTO.setListCandidateAnswersDTO(candidateAnswers);
	mcQuestionContentDTO.setCaCount(new Integer(candidateAnswers.size()).toString());

	McAction.logger.debug("listQuestionContentDTO after remove: " + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	if (mcContent == null) {
	    McAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	}
	McAction.logger.debug("final mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("mcQuestionContentDTO now: " + mcQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return newEditableQuestionBox(mapping, form, request, response);
    }

    /**
     * newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * enables adding a new candidate answer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching newCandidateBox");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, true);
	McAction.logger.debug("repopulated caList: " + caList);

	int caCount = caList.size();
	McAction.logger.debug("caCount: " + caCount);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);

	String passmark = request.getParameter("passmark");
	McAction.logger.debug("passmark: " + passmark);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);

	int currentQuestionCount = listQuestionContentDTO.size();
	McAction.logger.debug("currentQuestionCount: " + currentQuestionCount);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	McQuestionContentDTO mcQuestionContentDTOLocal = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    mcQuestionContentDTOLocal = (McQuestionContentDTO) listIterator.next();
	    McAction.logger.debug("mcQuestionContentDTOLocal:" + mcQuestionContentDTOLocal);
	    McAction.logger.debug("mcQuestionContentDTOLocal question:" + mcQuestionContentDTOLocal.getQuestion());

	    String question = mcQuestionContentDTOLocal.getQuestion();
	    String displayOrder = mcQuestionContentDTOLocal.getDisplayOrder();
	    McAction.logger.debug("displayOrder:" + displayOrder);

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	McAction.logger.debug("mcQuestionContentDTOLocal found:" + mcQuestionContentDTOLocal);

	if (mcQuestionContentDTOLocal != null) {
	    mcQuestionContentDTOLocal.setListCandidateAnswersDTO(caList);
	    mcQuestionContentDTOLocal.setCaCount(new Integer(caList.size()).toString());
	}

	McAction.logger.debug("listQuestionContentDTO after repopulating data: " + listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("mcQuestionContentDTOLocal now: " + mcQuestionContentDTOLocal);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return newEditableQuestionBox(mapping, form, request, response);
    }

    public ActionForward updateMarksList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching updateMarksList");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	McAction.logger.debug("questionIndex: " + questionIndex);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("2");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("fwd ing to LOAD_QUESTIONS: " + McAppConstants.LOAD_QUESTIONS);
	return (mapping.findForward(McAppConstants.LOAD_QUESTIONS));
    }

    public ActionForward moveAddedCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching moveAddedCandidateUp");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	McAction.logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY);
	McAction.logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();

	    Iterator listIterator = listAddableQuestionContentDTO.iterator();
	    /*there is only 1 question dto*/
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
		McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);

		candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
		McAction.logger.debug("candidates found :" + candidates);
		McAction.logger.debug("but we are using the repopulated caList here: " + caList);

		listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "up");
		McAction.logger.debug("swapped candidates :" + listCandidates);

		mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
	    }
	}

	McAction.logger.debug("listAddableQuestionContentDTO after swapping (up) candidates: "
		+ listAddableQuestionContentDTO);
	request.setAttribute(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	return newQuestionBox(mapping, form, request, response);

    }

    public ActionForward moveAddedCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching moveAddedCandidateDown");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	McAction.logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY);
	McAction.logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();

	    Iterator listIterator = listAddableQuestionContentDTO.iterator();
	    /*there is only 1 question dto*/
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
		McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);

		candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
		McAction.logger.debug("candidates found :" + candidates);
		McAction.logger.debug("but we are using the repopulated caList here: " + caList);

		listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "down");
		McAction.logger.debug("swapped candidates :" + listCandidates);

		mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
	    }
	}

	McAction.logger.debug("listAddableQuestionContentDTO after moving down candidates: ");
	request.setAttribute(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	return newQuestionBox(mapping, form, request, response);
    }

    public ActionForward removeAddedCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching removeAddedCandidate");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	McAction.logger.debug("repopulated caList: " + caList);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY);
	McAction.logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	List candidates = new LinkedList();
	List listCandidates = new LinkedList();

	Iterator listIterator = listAddableQuestionContentDTO.iterator();
	/*there is only 1 question dto*/
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);

	    candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
	    McAction.logger.debug("candidates found :" + candidates);

	    McAction.logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
	    McAction.logger.debug("setting caList for the content:");
	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);

	    List candidateAnswers = mcQuestionContentDTO.getListCandidateAnswersDTO();
	    McAction.logger.debug("candidateAnswers:" + candidateAnswers);

	    McCandidateAnswersDTO mcCandidateAnswersDTO = null;
	    Iterator listCaIterator = candidateAnswers.iterator();
	    int caIndex = 0;
	    while (listCaIterator.hasNext()) {
		caIndex++;
		McAction.logger.debug("caIndex:" + caIndex);
		mcCandidateAnswersDTO = (McCandidateAnswersDTO) listCaIterator.next();
		McAction.logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
		McAction.logger.debug("mcCandidateAnswersDTO question:" + mcCandidateAnswersDTO.getCandidateAnswer());

		if (caIndex == new Integer(candidateIndex).intValue()) {
		    McAction.logger.debug("candidateIndex found");
		    mcCandidateAnswersDTO.setCandidateAnswer("");

		    break;
		}
	    }
	    McAction.logger.debug("candidateAnswers after resetting answer" + candidateAnswers);

	    candidateAnswers = AuthoringUtil.reorderListCandidatesDTO(candidateAnswers);
	    McAction.logger.debug("candidateAnswers after reordering candidate nodes" + candidateAnswers);

	    mcQuestionContentDTO.setListCandidateAnswersDTO(candidateAnswers);
	    mcQuestionContentDTO.setCaCount(new Integer(candidateAnswers.size()).toString());

	    McAction.logger.debug("listQuestionContentDTO after remove: " + listQuestionContentDTO);
	}

	McAction.logger.debug("listAddableQuestionContentDTO : " + listAddableQuestionContentDTO);
	request.setAttribute(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McAction.logger.debug("mcContent: " + mcContent);

	if (mcContent == null) {
	    McAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	}
	McAction.logger.debug("final mcContent: " + mcContent);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	return newQuestionBox(mapping, form, request, response);

    }

    public ActionForward newAddedCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAction.logger.debug("dispatching newAddedCandidateBox");
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	McAction.logger.debug("mcService: " + mcService);

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	McAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	McAction.logger.debug("sessionMap: " + sessionMap);

	String candidateIndex = request.getParameter("candidateIndex");
	McAction.logger.debug("candidateIndex: " + candidateIndex);
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	McAction.logger.debug("totalMarks: " + totalMarks);

	List listQuestionContentDTO = (List) sessionMap.get(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	McAction.logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, true);
	McAction.logger.debug("repopulated caList: " + caList);

	int caCount = caList.size();
	McAction.logger.debug("caCount: " + caCount);

	String newQuestion = request.getParameter("newQuestion");
	McAction.logger.debug("newQuestion: " + newQuestion);

	String mark = request.getParameter("mark");
	McAction.logger.debug("mark: " + mark);

	String passmark = request.getParameter("passmark");
	McAction.logger.debug("passmark: " + passmark);

	String feedback = request.getParameter("feedback");
	McAction.logger.debug("feedback: " + feedback);

	int currentQuestionCount = listQuestionContentDTO.size();
	McAction.logger.debug("currentQuestionCount: " + currentQuestionCount);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	List listAddableQuestionContentDTO = (List) sessionMap.get(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY);
	McAction.logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	List candidates = new LinkedList();
	List listCandidates = new LinkedList();

	Iterator listIterator = listAddableQuestionContentDTO.iterator();
	/*there is only 1 question dto*/
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    McAction.logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);

	    McAction.logger.debug("caList:" + caList);
	    McAction.logger.debug("caList size:" + caList.size());
	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
	    mcQuestionContentDTO.setCaCount(new Integer(caList.size()).toString());
	}

	McAction.logger.debug("listAddableQuestionContentDTO after swapping (up) candidates: ");
	request.setAttribute(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(McAppConstants.NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	McAction.logger.debug("listQuestionContentDTO after repopulating data: " + listQuestionContentDTO);
	sessionMap.put(McAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	McAction.logger.debug("contentFolderID: " + contentFolderID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);
	McAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	McAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	McAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE))
		.toString();
	;
	McAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McAction.logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McAction.logger.debug("activeModule: " + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(McAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("onlineInstructions: " + onlineInstructions);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(McAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    McAction.logger.debug("offlineInstructions: " + offlineInstructions);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(McAppConstants.ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList = (List) sessionMap.get(McAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    McAction.logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    McAction.logger.debug("onlineInstructions: " + strOnlineInstructions);
	    McAction.logger.debug("offlineInstructions: " + strOnlineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	McAction.logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	McAction.logger.debug("marksMap: " + marksMap);
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	McAction.logger.debug("generating dyn pass map using listQuestionContentDTO: " + listQuestionContentDTO);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	McAction.logger.debug("passMarksMap: " + passMarksMap);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	McAction.logger.debug("totalMark: " + totalMark);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	McAction.logger.debug("correctMap: " + correctMap);
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	mcAuthoringForm.setFeedback(feedback);

	mcGeneralAuthoringDTO.setMarkValue(mark);

	McAction.logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McAction.logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	return newQuestionBox(mapping, form, request, response);

    }

    /**
     * boolean existsContent(long toolContentID, IMcService mcService)
     * 
     * @param toolContentID
     * @param mcService
     * @return
     */
    protected boolean existsContent(long toolContentID, IMcService mcService) {
	McContent mcContent = mcService.retrieveMc(new Long(toolContentID));
	if (mcContent == null) {
	    return false;
	}

	return true;
    }

}
