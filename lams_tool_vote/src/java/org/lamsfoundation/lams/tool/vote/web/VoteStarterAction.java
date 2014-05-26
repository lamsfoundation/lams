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
package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteAuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * VoteStarterAction loads the default content and initializes the presentation Map. Initializes the tool's authoring
 * mode Requests can come either from authoring environment or from the monitoring environment for Edit Activity screen.
 * 
 * @author Ozgur Demirtas
 */
public class VoteStarterAction extends Action implements VoteAppConstants {
    /*
     * This class is reused by defineLater and monitoring modules as well.
     */
    private static Logger logger = Logger.getLogger(VoteStarterAction.class.getName());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, VoteApplicationException {

	VoteUtils.cleanUpUserExceptions(request);

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	VoteAction voteAction = new VoteAction();
	voteAction.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	IVoteService voteService = null;
	if (getServlet() != null) {
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	} else {
	    voteService = voteAuthoringForm.getVoteService();
	}

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	voteAuthoringForm.setHttpSessionID(sessionMap.getSessionID());
	voteGeneralAuthoringDTO.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	voteAuthoringForm.resetRadioBoxes();
	voteAuthoringForm.setExceptionMaxNominationInvalid(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setExceptionMaxNominationInvalid(new Boolean(false).toString());

	ActionForward validateSignature = validateDefaultContent(request, mapping, voteService, voteAuthoringForm);
	if (validateSignature != null) {
	    return validateSignature;
	}
	    
	//no problems getting the default content, will render authoring screen

	/* the authoring url must be passed a tool content id */
	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	voteAuthoringForm.setToolContentID(new Long(strToolContentId).toString());
	voteGeneralAuthoringDTO.setToolContentID(new Long(strToolContentId).toString());

	if (strToolContentId == null || strToolContentId.equals("")) {
	    VoteUtils.cleanUpUserExceptions(request);
	    // saveInRequestError(request,"error.contentId.required");
	    VoteUtils.cleanUpUserExceptions(request);
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}
	
	ToolAccessMode mode = getAccessMode(request);
	// request is from monitoring module
	if (mode.isTeacher()) {
	    VoteUtils.setDefineLater(request, true, strToolContentId, voteService);
	}
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	VoteContent voteContent = voteService.getVoteContent(new Long(strToolContentId));
	
	// if mcContent does not exist, try to use default content instead.
	if (voteContent == null) {
	    long defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    voteContent = voteService.getVoteContent(defaultContentID);
	    voteContent = VoteContent.newInstance(voteContent, new Long(strToolContentId));
	}

	VoteStarterAction.prepareDTOandForm(request, voteContent, voteAuthoringForm, voteGeneralAuthoringDTO);
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
	    List<String> dataFlowObjectNames = new ArrayList<String>(dataFlowObjects.size());
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

	List<VoteQuestionDTO> questionDTOs = new LinkedList<VoteQuestionDTO>();

	for (VoteQueContent question : (Set<VoteQueContent>)voteContent.getVoteQueContents()) {
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
	
	List<VoteQuestionDTO> listDeletedQuestionDTOs = new ArrayList<VoteQuestionDTO>();
	sessionMap.put(VoteAppConstants.LIST_DELETED_QUESTION_DTOS, listDeletedQuestionDTOs);

	voteAuthoringForm.resetUserAction();
	voteAuthoringForm.setCurrentTab("1");

	return mapping.findForward(LOAD_QUESTIONS);
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
    private ActionForward validateDefaultContent(HttpServletRequest request, ActionMapping mapping, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm) {
	/*
	 * retrieve the default content id based on tool signature
	 */
	long defaultContentID = 0;
	try {
	    defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    if (defaultContentID == 0) {
		VoteUtils.cleanUpUserExceptions(request);
		saveInRequestError(request, "error.defaultContent.notSetup");
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }
	} catch (Exception e) {
	    VoteUtils.cleanUpUserExceptions(request);
	    VoteStarterAction.logger.error("error getting the default content id: " + e.getMessage());
	    saveInRequestError(request, "error.defaultContent.notSetup");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	try {
	    //retrieve uid of the content based on default content id determined above defaultContentID
	    VoteContent voteContent = voteService.getVoteContent(new Long(defaultContentID));
	    if (voteContent == null) {
		VoteUtils.cleanUpUserExceptions(request);
		VoteStarterAction.logger.error("Exception occured: No default content");
		saveInRequestError(request, "error.defaultContent.notSetup");
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }
	} catch (Exception e) {
	    VoteStarterAction.logger.error("other problems: " + e);
	    VoteUtils.cleanUpUserExceptions(request);
	    VoteStarterAction.logger.error("Exception occured: No default question content");
	    saveInRequestError(request, "error.defaultContent.notSetup");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	return null;
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
	if (maxNomcount.equals(""))
	    maxNomcount = "0";
	voteAuthoringForm.setMaxNominationCount(maxNomcount);
	voteGeneralAuthoringDTO.setMaxNominationCount(maxNomcount);

	String minNomcount = voteContent.getMinNominationCount();
	if ((minNomcount == null) || minNomcount.equals(""))
	    minNomcount = "0";
	voteAuthoringForm.setMinNominationCount(minNomcount);
	voteGeneralAuthoringDTO.setMinNominationCount(minNomcount);
    }

    /**
     * saves error messages to request scope
     * 
     * @param request
     * @param message
     */
    private void saveInRequestError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	VoteStarterAction.logger.error("add " + message + "  to ActionMessages:");
	saveErrors(request, errors);
    }
    
    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     * 
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }
}
