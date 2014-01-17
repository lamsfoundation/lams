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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Ozgur Demirtas
 * 
 * VoteStarterAction loads the default content and initializes the presentation Map.
 * Initializes the tool's authoring mode
 * Requests can come either from authoring environment or from the monitoring environment for Edit Activity screen.
 */
public class VoteStarterAction extends Action implements VoteAppConstants {
    /*
     * This class is reused by defineLater and monitoring modules as well.
     */
    static Logger logger = Logger.getLogger(VoteStarterAction.class.getName());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, VoteApplicationException {

	VoteUtils.cleanUpSessionAbsolute(request);

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

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

	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	voteAuthoringForm.setSbmtSuccess(new Boolean(false).toString());

	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setSbmtSuccess(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String servletPath = request.getServletPath();
	if (servletPath.indexOf("authoringStarter") > 0) {
	    voteAuthoringForm.setActiveModule(VoteAppConstants.AUTHORING);
	    voteGeneralAuthoringDTO.setActiveModule(VoteAppConstants.AUTHORING);

	    voteAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	} else {
	    //request is for define later module. either direct or by monitoring module
	    voteAuthoringForm.setActiveModule(VoteAppConstants.DEFINE_LATER);
	    voteGeneralAuthoringDTO.setActiveModule(VoteAppConstants.DEFINE_LATER);

	    voteAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	}

	SessionMap sessionMap = new SessionMap();
	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, "");
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, "");
	voteAuthoringForm.setHttpSessionID(sessionMap.getSessionID());
	voteGeneralAuthoringDTO.setHttpSessionID(sessionMap.getSessionID());

	/*
	 * determine whether the request is from Monitoring url Edit Activity. null sourceVoteStarter indicates that the
	 * request is from authoring url.
	 */

	String sourceVoteStarter = (String) request.getAttribute(VoteAppConstants.SOURCE_VOTE_STARTER);

	voteAuthoringForm.resetRadioBoxes();
	voteAuthoringForm.setExceptionMaxNominationInvalid(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setExceptionMaxNominationInvalid(new Boolean(false).toString());

	ActionForward validateSignature = readSignature(request, mapping, voteService, voteAuthoringForm);
	if (validateSignature != null) {
	    return validateSignature;
	} else {
	    //no problems getting the default content, will render authoring screen
	    String strToolContentId = "";
	    /* the authoring url must be passed a tool content id */
	    strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	    /* this will be fixed when making changes to Monitoring module */
	    if (strToolContentId == null) {
		/*
		 * watch out for a possibility that the original request for authoring module is coming from monitoring
		 * url
		 */
		//we should IDEALLY not arrive here. The TOOL_CONTENT_ID is NOT available
		/* use default content instead of giving a warning */
		String defaultContentId = voteAuthoringForm.getDefaultContentId();
		strToolContentId = defaultContentId;
	    }

	    if (strToolContentId == null || strToolContentId.equals("")) {
		VoteUtils.cleanUpSessionAbsolute(request);
		// saveInRequestError(request,"error.contentId.required");
		VoteUtils.cleanUpSessionAbsolute(request);
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }

	    /*
	     * Process incoming tool content id. Either exists or not exists in the db yet, a toolContentID must be
	     * passed to the tool from the container
	     */
	    long toolContentID = 0;
	    try {
		toolContentID = new Long(strToolContentId).longValue();
		voteAuthoringForm.setToolContentID(new Long(strToolContentId).toString());
		voteGeneralAuthoringDTO.setToolContentID(new Long(strToolContentId).toString());
	    } catch (NumberFormatException e) {
		VoteUtils.cleanUpSessionAbsolute(request);
		saveInRequestError(request, "error.numberFormatException");
		VoteStarterAction.logger.error("forwarding to: " + VoteAppConstants.ERROR_LIST);
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }

	    /*
	     * find out if the passed tool content id exists in the db present user either a first timer screen with
	     * default content data or fetch the existing content.
	     * 
	     * if the toolcontentid does not exist in the db, create the default Map, there is no need to check if the
	     * content is in use in this case. It is always unlocked -> not in use since it is the default content.
	     */
	    Map mapOptionsContent = new TreeMap(new VoteComparator());
	    if (!existsContent(toolContentID, request, voteService)) {
		/* fetch default content */
		String defaultContentIdStr = voteAuthoringForm.getDefaultContentIdStr();
		retrieveContent(request, voteService, voteAuthoringForm, voteGeneralAuthoringDTO, mapOptionsContent,
			new Long(defaultContentIdStr).longValue(), sessionMap);

	    } else {
		/* it is possible that the content is in use by learners. */
		VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentId));
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
		    VoteUtils.cleanUpSessionAbsolute(request);
		    saveInRequestError(request, "error.content.inUse");
		    return mapping.findForward(VoteAppConstants.ERROR_LIST);
		}

		if (servletPath.indexOf("authoringStarter") > 0) {
		    boolean isDefineLater = VoteUtils.isDefineLater(voteContent);
		    if (isDefineLater == true) {
			VoteUtils.cleanUpSessionAbsolute(request);
			VoteStarterAction.logger.error("student activity occurred on this content:" + voteContent);
			saveInRequestError(request, "error.content.inUse");
			return mapping.findForward(VoteAppConstants.ERROR_LIST);

		    }
		}

		retrieveContent(request, voteService, voteAuthoringForm, voteGeneralAuthoringDTO, mapOptionsContent,
			new Long(strToolContentId).longValue(), sessionMap);
	    }
	}

	voteAuthoringForm.resetUserAction();

	if (voteAuthoringForm != null) {
	    voteAuthoringForm.setCurrentTab("1");
	}

	String destination = VoteUtils.getDestination(sourceVoteStarter);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	return mapping.findForward(destination);
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
    public ActionForward readSignature(HttpServletRequest request, ActionMapping mapping, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm) {
	/*
	 * retrieve the default content id based on tool signature
	 */
	long defaultContentID = 0;
	try {
	    defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    if (defaultContentID == 0) {
		VoteUtils.cleanUpSessionAbsolute(request);
		saveInRequestError(request, "error.defaultContent.notSetup");
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }
	} catch (Exception e) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    VoteStarterAction.logger.error("error getting the default content id: " + e.getMessage());
	    saveInRequestError(request, "error.defaultContent.notSetup");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	/* retrieve uid of the content based on default content id determined above */
	long contentUID = 0;
	try {
	    //retrieve uid of the content based on default content id determined above defaultContentID
	    VoteContent voteContent = voteService.retrieveVote(new Long(defaultContentID));
	    if (voteContent == null) {
		VoteUtils.cleanUpSessionAbsolute(request);
		VoteStarterAction.logger.error("Exception occured: No default content");
		saveInRequestError(request, "error.defaultContent.notSetup");
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }
	    contentUID = voteContent.getUid().longValue();
	} catch (Exception e) {
	    VoteStarterAction.logger.error("other problems: " + e);
	    VoteUtils.cleanUpSessionAbsolute(request);
	    VoteStarterAction.logger.error("Exception occured: No default question content");
	    saveInRequestError(request, "error.defaultContent.notSetup");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	voteAuthoringForm.setDefaultContentId(new Long(defaultContentID).toString());
	voteAuthoringForm.setDefaultContentIdStr(new Long(defaultContentID).toString());
	return null;
    }

    protected void retrieveContent(HttpServletRequest request, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO,
	    Map mapOptionsContent, long toolContentID, SessionMap sessionMap) {

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	VoteUtils.readContentValues(request, voteContent, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteAuthoringForm.setIsDefineLater(new Boolean(voteContent.isDefineLater()).toString());
	voteGeneralAuthoringDTO.setIsDefineLater(new Boolean(voteContent.isDefineLater()).toString());

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

	List<DataFlowObject> dataFlowObjects = voteService.getDataFlowObjects(new Long(toolContentID));

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

	List listNominationContentDTO = new LinkedList();

	/*
	 * get the nominations
	 */
	mapOptionsContent.clear();
	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	Long mapIndex = new Long(1);
	while (queIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();

	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		mapOptionsContent.put(mapIndex.toString(), voteQueContent.getQuestion());

		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listNominationContentDTO.add(voteNominationContentDTO);

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(mapOptionsContent.size()));
	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	sessionMap.put(VoteAppConstants.MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);

	int maxIndex = mapOptionsContent.size();
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	Short maxInputs = voteContent.getMaxExternalInputs();
	if (maxInputs == null) {
	    maxInputs = 0;
	}
	voteAuthoringForm.setMaxInputs(maxInputs);

	voteAuthoringForm.resetUserAction();
    }

    public ActionForward executeDefineLater(ActionMapping mapping, VoteAuthoringForm voteAuthoringForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException,
	    VoteApplicationException {
	return execute(mapping, voteAuthoringForm, request, response);
    }

    protected boolean existsContent(long toolContentID, HttpServletRequest request, IVoteService voteService) {
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	if (voteContent == null) {
	    return false;
	}

	return true;
    }

    /**
     * saves error messages to request scope
     * 
     * @param request
     * @param message
     */
    public void saveInRequestError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	VoteStarterAction.logger.error("add " + message + "  to ActionMessages:");
	saveErrors(request, errors);
    }
}
