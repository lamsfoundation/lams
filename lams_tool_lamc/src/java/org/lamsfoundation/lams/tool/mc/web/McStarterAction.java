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


package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * A Map data structure is used to present the UI.
 *
 * @author Ozgur Demirtas
 */
public class McStarterAction extends Action implements McAppConstants {
    private static Logger logger = Logger.getLogger(McStarterAction.class.getName());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, McApplicationException {

	McUtils.cleanUpSessionAbsolute(request);
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcAuthoringForm.resetRadioBoxes();

	IMcService mcService = null;
	if ((getServlet() == null) || (getServlet().getServletContext() == null)) {
	    mcService = mcAuthoringForm.getMcService();
	} else {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	mcGeneralAuthoringDTO.setCurrentTab("1");

	boolean validateSignature = validateDefaultContent(request, mapping, mcService, mcGeneralAuthoringDTO,
		mcAuthoringForm);
	if (validateSignature == false) {
	    logger.debug("error during validation");
	}

	/* the authoring url must be passed a tool content id */
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	// request is from monitoring module
	if (mode.isTeacher()) {
	    mcService.setDefineLater(strToolContentID, true);
	}
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	sessionMap.put(ACTIVITY_TITLE_KEY, "");
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, "");
	mcAuthoringForm.setHttpSessionID(sessionMap.getSessionID());
	mcGeneralAuthoringDTO.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	if ((strToolContentID == null) || (strToolContentID.equals(""))) {
	    McUtils.cleanUpSessionAbsolute(request);
	    return (mapping.findForward(ERROR_LIST));
	}

	mcAuthoringForm.setToolContentID(strToolContentID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	// if mcContent does not exist, try to use default content instead.
	if (mcContent == null) {
	    long defaultContentID = mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
	    mcContent = mcService.getMcContent(new Long(defaultContentID));
	    mcContent = McContent.newInstance(mcContent, new Long(strToolContentID));
	}

	prepareDTOandForm(request, mapping, mcAuthoringForm, new Long(strToolContentID).longValue(), mcContent,
		mcGeneralAuthoringDTO, sessionMap);

	List<McQuestionDTO> questionDtos = AuthoringUtil.buildDefaultQuestions(mcContent);
	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(questionDtos.size()));
	request.setAttribute(LIST_QUESTION_DTOS, questionDtos);
	sessionMap.put(LIST_QUESTION_DTOS, questionDtos);

	List<McQuestionDTO> listDeletedQuestionDTOs = new ArrayList<McQuestionDTO>();
	sessionMap.put(LIST_DELETED_QUESTION_DTOS, listDeletedQuestionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);
	mcGeneralAuthoringDTO.setMarkValue("1");

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDtos, true);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDtos);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String passMark = " ";

	if ((mcContent.getPassMark() != null) && (mcContent.getPassMark().intValue() != 0)) {
	    passMark = mcContent.getPassMark().toString();
	}

	mcGeneralAuthoringDTO.setPassMarkValue(passMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	return (mapping.findForward(LOAD_AUTHORING));
    }

    /**
     * prepares the data for presentation purposes.
     *
     * @param request
     * @param mapping
     * @param mcAuthoringForm
     * @param mapQuestionContent
     * @param toolContentID
     * @return ActionForward
     */
    protected McContent prepareDTOandForm(HttpServletRequest request, ActionMapping mapping,
	    McAuthoringForm mcAuthoringForm, long toolContentID, McContent mcContent,
	    McGeneralAuthoringDTO mcGeneralAuthoringDTO, SessionMap<String, Object> sessionMap) {

	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());

	mcAuthoringForm.setSln(mcContent.isShowReport() ? "1" : "0");
	mcAuthoringForm.setQuestionsSequenced(mcContent.isQuestionsSequenced() ? "1" : "0");
	mcAuthoringForm.setRandomize(mcContent.isRandomize() ? "1" : "0");
	mcAuthoringForm.setDisplayAnswers(mcContent.isDisplayAnswers() ? "1" : "0");
	mcAuthoringForm.setShowMarks(mcContent.isShowMarks() ? "1" : "0");
	mcAuthoringForm.setUseSelectLeaderToolOuput(mcContent.isUseSelectLeaderToolOuput() ? "1" : "0");
	mcAuthoringForm.setPrefixAnswersWithLetters(mcContent.isPrefixAnswersWithLetters() ? "1" : "0");

	mcAuthoringForm.setRetries(mcContent.isRetries() ? "1" : "0");
	mcAuthoringForm.setReflect(mcContent.isReflect() ? "1" : "0");
	mcAuthoringForm.setReflectionSubject(mcContent.getReflectionSubject());

	mcGeneralAuthoringDTO.setSln(mcContent.isShowReport() ? "1" : "0");
	mcGeneralAuthoringDTO.setQuestionsSequenced(mcContent.isQuestionsSequenced() ? "1" : "0");
	mcGeneralAuthoringDTO.setRandomize(mcContent.isRandomize() ? "1" : "0");
	mcGeneralAuthoringDTO.setDisplayAnswers(mcContent.isDisplayAnswers() ? "1" : "0");
	mcGeneralAuthoringDTO.setRetries(mcContent.isRetries() ? "1" : "0");
	mcGeneralAuthoringDTO.setReflect(mcContent.isReflect() ? "1" : "0");
	mcGeneralAuthoringDTO.setReflectionSubject(mcContent.getReflectionSubject());

	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcAuthoringForm.setTitle(mcContent.getTitle());

	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());
	mcAuthoringForm.setInstructions(mcContent.getInstructions());

	sessionMap.put(ACTIVITY_TITLE_KEY, mcGeneralAuthoringDTO.getActivityTitle());
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, mcGeneralAuthoringDTO.getActivityInstructions());

	mcAuthoringForm.resetUserAction();
	return mcContent;
    }

    /**
     * each tool has a signature. MC tool's signature is stored in MY_SIGNATURE. The default tool content id and other
     * depending content ids are obtained in this method. if all the default content has been setup properly the method
     * persists DEFAULT_CONTENT_ID in the session.
     *
     * @param request
     * @param mapping
     * @return ActionForward
     */
    private boolean validateDefaultContent(HttpServletRequest request, ActionMapping mapping, IMcService mcService,
	    McGeneralAuthoringDTO mcGeneralAuthoringDTO, McAuthoringForm mcAuthoringForm) {
	/*
	 * retrieve the default content id based on tool signature
	 */
	long defaultContentID = 0;
	try {
	    defaultContentID = mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
	    if (defaultContentID == 0) {
		// default content id has not been setup
		return false;
	    }
	} catch (Exception e) {
	    logger.debug("error getting the default content id: " + e.getMessage());
	    persistError(request, "error.defaultContent.notSetup");
	    return false;
	}

	try {
	    McContent mcContent = mcService.getMcContent(new Long(defaultContentID));
	    if (mcContent == null) {
		logger.debug("Exception occured: No default content");
		persistError(request, "error.defaultContent.notSetup");
		return false;
	    }
	} catch (Exception e) {
	    logger.debug("Exception occured: No default question content");
	    persistError(request, "error.defaultContent.notSetup");
	    return false;
	}

	return true;
    }

    /**
     * persists error messages to request scope
     *
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	saveErrors(request, errors);
    }
}
