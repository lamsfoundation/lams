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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionDTO;
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

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	String sessionMapId = sessionMap.getSessionID();
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, strToolContentID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);

	mcAuthoringForm.resetRadioBoxes();

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	// request is from monitoring module
	if (mode.isTeacher()) {
	    mcService.setDefineLater(strToolContentID, true);
	}

	if ((strToolContentID == null) || (strToolContentID.equals(""))) {
	    return (mapping.findForward(ERROR_LIST));
	}

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	// if mcContent does not exist, try to use default content instead.
	if (mcContent == null) {
	    long defaultContentID = mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
	    mcContent = mcService.getMcContent(new Long(defaultContentID));
	    mcContent = McContent.newInstance(mcContent, new Long(strToolContentID));
	}

	prepareDTOandForm(request, mapping, mcAuthoringForm, mcContent, mcGeneralAuthoringDTO);

	List<McQuestionDTO> questionDtos = AuthoringUtil.buildDefaultQuestions(mcContent);
	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(questionDtos.size()));
	request.setAttribute(LIST_QUESTION_DTOS, questionDtos);
	sessionMap.put(LIST_QUESTION_DTOS, questionDtos);

	List<McQuestionDTO> listDeletedQuestionDTOs = new ArrayList<McQuestionDTO>();
	sessionMap.put(LIST_DELETED_QUESTION_DTOS, listDeletedQuestionDTOs);

	mcGeneralAuthoringDTO.setMarkValue("1");

	String totalMark = AuthoringUtil.getTotalMark(questionDtos);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String passMark = " ";
	if ((mcContent.getPassMark() != null) && (mcContent.getPassMark().intValue() != 0)) {
	    passMark = mcContent.getPassMark().toString();
	}
	mcGeneralAuthoringDTO.setPassMarkValue(passMark);

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
	    McAuthoringForm mcAuthoringForm, McContent mcContent,
	    McGeneralAuthoringDTO mcGeneralAuthoringDTO) {

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

	mcAuthoringForm.setTitle(mcContent.getTitle());
	mcAuthoringForm.setInstructions(mcContent.getInstructions());

	mcAuthoringForm.resetUserAction();
	return mcContent;
    }
}
