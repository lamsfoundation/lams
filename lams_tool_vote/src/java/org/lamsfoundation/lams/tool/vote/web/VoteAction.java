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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteAttachmentDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteToolContentHandler;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Ozgur Demirtas
 */
public class VoteAction extends LamsDispatchAction implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteAction.class.getName());

    private VoteToolContentHandler toolContentHandler;

    /**
     * 
     * main content/question content management and workflow logic
     * 
     * if the passed toolContentID exists in the db, we need to get the relevant data into the Map if not, create the
     * default Map
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	SessionMap sessionMap = new SessionMap();
	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);
	voteAuthoringForm.resetUserAction();
	return null;
    }

    /**
     * persists offline files
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOfflineFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, RepositoryCheckedException {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapOptionsContent = (Map) sessionMap.get(VoteAppConstants.MAP_OPTIONS_CONTENT_KEY);
	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	int maxIndex = mapOptionsContent.size();
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	String firstEntry = (String) mapOptionsContent.get("1");
	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

	/* determine whether the request is from Monitoring url Edit Activity */
	String sourceVoteStarter = (String) request.getAttribute(VoteAppConstants.SOURCE_VOTE_STARTER);
	String destination = VoteUtils.getDestination(sourceVoteStarter);

	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);

	VoteAttachmentDTO voteAttachmentDTO = AuthoringUtil.uploadFile(request, voteService, voteAuthoringForm, true,
		sessionMap);
	List listOfflineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_OFFLINEFILES_METADATA_KEY);

	if (voteAttachmentDTO != null) {
	    listOfflineFilesMetaData.add(voteAttachmentDTO);
	}

	sessionMap.put(VoteAppConstants.LIST_OFFLINEFILES_METADATA_KEY, listOfflineFilesMetaData);
	voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

	List listOnlineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_ONLINEFILES_METADATA_KEY);
	voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

	if (voteAttachmentDTO == null) {
	    ActionMessages errors = new ActionMessages();
	    errors = new ActionMessages();
	    voteGeneralAuthoringDTO.setUserExceptionFilenameEmpty(new Boolean(true).toString());
	    errors.add(Globals.ERROR_KEY, new ActionMessage("error.fileName.empty"));
	    saveErrors(request, errors);
	    voteAuthoringForm.resetUserAction();
	    persistInRequestError(request, "error.fileName.empty");

	    request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	    return mapping.findForward(destination);
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteAuthoringForm.resetUserAction();
	return mapping.findForward(destination);
    }

    /**
     * persists online files
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOnlineFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, RepositoryCheckedException {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapOptionsContent = (Map) sessionMap.get(VoteAppConstants.MAP_OPTIONS_CONTENT_KEY);
	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	int maxIndex = mapOptionsContent.size();
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	String firstEntry = (String) mapOptionsContent.get("1");
	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

	/* determine whether the request is from Monitoring url Edit Activity */
	String sourceVoteStarter = (String) request.getAttribute(VoteAppConstants.SOURCE_VOTE_STARTER);
	String destination = VoteUtils.getDestination(sourceVoteStarter);

	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);

	VoteAttachmentDTO voteAttachmentDTO = AuthoringUtil.uploadFile(request, voteService, voteAuthoringForm, false,
		sessionMap);
	List listOnlineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_ONLINEFILES_METADATA_KEY);

	if (voteAttachmentDTO != null) {
	    listOnlineFilesMetaData.add(voteAttachmentDTO);
	}

	sessionMap.put(VoteAppConstants.LIST_ONLINEFILES_METADATA_KEY, listOnlineFilesMetaData);
	voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

	List listOfflineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_OFFLINEFILES_METADATA_KEY);
	voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

	if (voteAttachmentDTO == null) {
	    ActionMessages errors = new ActionMessages();
	    errors = new ActionMessages();
	    voteGeneralAuthoringDTO.setUserExceptionFilenameEmpty(new Boolean(true).toString());
	    errors.add(Globals.ERROR_KEY, new ActionMessage("error.fileName.empty"));
	    saveErrors(request, errors);
	    voteAuthoringForm.resetUserAction();
	    persistInRequestError(request, "error.fileName.empty");

	    request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	    return mapping.findForward(destination);
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteAuthoringForm.resetUserAction();
	return mapping.findForward(destination);
    }

    /**
     * removes an offline file from the jsp
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException

    {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapOptionsContent = (Map) sessionMap.get(VoteAppConstants.MAP_OPTIONS_CONTENT_KEY);
	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	int maxIndex = mapOptionsContent.size();
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	String firstEntry = (String) mapOptionsContent.get("1");
	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

	/* determine whether the request is from Monitoring url Edit Activity */
	String sourceVoteStarter = (String) request.getAttribute(VoteAppConstants.SOURCE_VOTE_STARTER);
	String destination = VoteUtils.getDestination(sourceVoteStarter);

	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);

	String uuid = voteAuthoringForm.getUuid();

	List listOfflineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_OFFLINEFILES_METADATA_KEY);
	listOfflineFilesMetaData = AuthoringUtil.removeFileItem(listOfflineFilesMetaData, uuid);
	sessionMap.put(VoteAppConstants.LIST_OFFLINEFILES_METADATA_KEY, listOfflineFilesMetaData);

	voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

	List listOnlineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_ONLINEFILES_METADATA_KEY);
	voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteAuthoringForm.resetUserAction();

	return mapping.findForward(destination);
    }

    /**
     * deletes an online file from the jsp
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException

    {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapOptionsContent = (Map) sessionMap.get(VoteAppConstants.MAP_OPTIONS_CONTENT_KEY);
	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	int maxIndex = mapOptionsContent.size();
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	String firstEntry = (String) mapOptionsContent.get("1");
	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

	/* determine whether the request is from Monitoring url Edit Activity */
	String sourceVoteStarter = (String) request.getAttribute(VoteAppConstants.SOURCE_VOTE_STARTER);
	String destination = VoteUtils.getDestination(sourceVoteStarter);

	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);

	String uuid = voteAuthoringForm.getUuid();

	List listOnlineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_ONLINEFILES_METADATA_KEY);
	listOnlineFilesMetaData = AuthoringUtil.removeFileItem(listOnlineFilesMetaData, uuid);
	sessionMap.put(VoteAppConstants.LIST_ONLINEFILES_METADATA_KEY, listOnlineFilesMetaData);

	voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

	List listOfflineFilesMetaData = (List) sessionMap.get(VoteAppConstants.LIST_OFFLINEFILES_METADATA_KEY);
	voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteAuthoringForm.resetUserAction();

	return mapping.findForward(destination);
    }

    /**
     * used in define later mode to switch from view-only to editable screen
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

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	voteAuthoringForm.setHttpSessionID(httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = VoteAppConstants.DEFINE_LATER;

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
	voteAuthoringForm.setTitle(voteContent.getTitle());

	voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, voteContent.getTitle());
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());

	voteAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteUtils.setDefineLater(request, true, strToolContentID, voteService);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	List listNominationContentDTO = new LinkedList();

	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	while (queIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();

	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {

		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		// voteNominationContentDTO.setFeedback(voteQueContent.getFeedback());
		listNominationContentDTO.add(voteNominationContentDTO);
	    }
	}
	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * repopulateRequestParameters reads and saves request parameters
     * 
     * @param request
     * @param voteAuthoringForm
     * @param voteGeneralAuthoringDTO
     */
    protected void repopulateRequestParameters(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm,
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteAuthoringForm.setToolContentID(toolContentID);
	voteGeneralAuthoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	voteAuthoringForm.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	String httpSessionID = request.getParameter(VoteAppConstants.HTTP_SESSION_ID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	String defineLaterInEditMode = request.getParameter(VoteAppConstants.DEFINE_LATER_IN_EDIT_MODE);
	voteAuthoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
	voteGeneralAuthoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

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

        String minNominationCount=request.getParameter(MIN_NOMINATION_COUNT);
        voteAuthoringForm.setMinNominationCount(minNominationCount);
        voteGeneralAuthoringDTO.setMinNominationCount(minNominationCount);

	String reflect = request.getParameter("reflect");
	voteAuthoringForm.setReflect(reflect);
	voteGeneralAuthoringDTO.setReflect(reflect);

	String reflectionSubject = request.getParameter("reflectionSubject");
	voteAuthoringForm.setReflectionSubject(reflectionSubject);
	voteGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);

	String offlineInstructions = request.getParameter(VoteAppConstants.OFFLINE_INSTRUCTIONS);
	voteAuthoringForm.setOfflineInstructions(offlineInstructions);
	voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	String onlineInstructions = request.getParameter(VoteAppConstants.ONLINE_INSTRUCTIONS);
	voteAuthoringForm.setOnlineInstructions(onlineInstructions);
	voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	String maxInputs = request.getParameter(VoteAppConstants.MAX_INPUTS);
	if (maxInputs == null) {
	    maxInputs = "0";
	}
	voteAuthoringForm.setMaxInputs(new Short(maxInputs));
    }

    /**
     * persists error messages to request scope
     * 
     * @param request
     * @param message
     */
    public void persistInRequestError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	saveErrors(request, errors);
    }

    /**
     * moveNominationDown
     * 
     * moves a nomination down in the authoring list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	listNominationContentDTO = AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "down");
	listNominationContentDTO = AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
	    List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}


	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * moveNominationUp
     * 
     * moves a nomination up in the authoring list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	listNominationContentDTO = AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "up");

	listNominationContentDTO = AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
	    List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}


	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * removeNomination
     * 
     * removes a nomination from the authoring list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	VoteNominationContentDTO voteNominationContentDTO = null;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	voteNominationContentDTO.setNomination("");

	listNominationContentDTO = AuthoringUtil.reorderListNominationContentDTO(listNominationContentDTO,
		questionIndex);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
	    List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * newEditableNominationBox
     * 
     * enables editing a nomination
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	voteAuthoringForm.setEditableNominationIndex(questionIndex);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	String editableNomination = "";
	String editableFeedback = "";
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    String question = voteNominationContentDTO.getNomination();
	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = voteNominationContentDTO.getFeedback();
		    editableNomination = voteNominationContentDTO.getNomination();
		    break;
		}

	    }
	}
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	voteGeneralAuthoringDTO.setEditableNominationText(editableNomination);
	voteGeneralAuthoringDTO.setEditableNominationFeedback(editableFeedback);
	voteAuthoringForm.setFeedback(editableFeedback);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	return mapping.findForward("editNominationBox");
    }

    /**
     * newNominationBox
     * 
     * enables adding a new nomination
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
	return mapping.findForward("newNominationBox");
    }

    /**
     * addSingleNomination
     * 
     * enables adding a new nomination to the authoring nominations list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	String newNomination = request.getParameter("newNomination");

	String feedback = request.getParameter("feedback");

	int listSize = listNominationContentDTO.size();

	if (newNomination != null && newNomination.length() > 0) {
	    boolean duplicates = AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);

	    if (!duplicates) {
		VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();
		voteNominationContentDTO.setDisplayOrder(new Long(listSize + 1).toString());
		voteNominationContentDTO.setFeedback(feedback);
		voteNominationContentDTO.setNomination(newNomination);

		listNominationContentDTO.add(voteNominationContentDTO);
	    }
	}
	
	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String richTextTitle = (String) sessionMap.get(VoteAppConstants.ACTIVITY_TITLE_KEY);
	String richTextInstructions = (String) sessionMap.get(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);

	    List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	}

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * saveSingleNomination saves a new or updated nomination in the authoring nominations list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward saveSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String editNominationBoxRequest = request.getParameter("editNominationBoxRequest");

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	String newNomination = request.getParameter("newNomination");

	String feedback = request.getParameter("feedback");

	String editableNominationIndex = request.getParameter("editableNominationIndex");

	if (newNomination != null && newNomination.length() > 0) {
	    if (editNominationBoxRequest != null && editNominationBoxRequest.equals("false")) {
		boolean duplicates = AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);

		if (!duplicates) {
		    VoteNominationContentDTO voteNominationContentDTO = null;
		    Iterator listIterator = listNominationContentDTO.iterator();
		    while (listIterator.hasNext()) {
			voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

			String question = voteNominationContentDTO.getNomination();
			String displayOrder = voteNominationContentDTO.getDisplayOrder();

			if (displayOrder != null && !displayOrder.equals("")) {
			    if (displayOrder.equals(editableNominationIndex)) {
				break;
			    }

			}
		    }

		    voteNominationContentDTO.setQuestion(newNomination);
		    voteNominationContentDTO.setFeedback(feedback);
		    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);

		    listNominationContentDTO = AuthoringUtil.reorderUpdateListNominationContentDTO(
			    listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
		} else {
		    //duplicate question entry, not adding
		}
	    } else {
		//request for edit and save
		VoteNominationContentDTO voteNominationContentDTO = null;
		Iterator listIterator = listNominationContentDTO.iterator();
		while (listIterator.hasNext()) {
		    voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

		    String question = voteNominationContentDTO.getNomination();
		    String displayOrder = voteNominationContentDTO.getDisplayOrder();

		    if (displayOrder != null && !displayOrder.equals("")) {
			if (displayOrder.equals(editableNominationIndex)) {
			    break;
			}

		    }
		}

		voteNominationContentDTO.setNomination(newNomination);
		voteNominationContentDTO.setFeedback(feedback);
		voteNominationContentDTO.setDisplayOrder(editableNominationIndex);

		listNominationContentDTO = AuthoringUtil.reorderUpdateListNominationContentDTO(
			listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
	    }
	} else {
	    //entry blank, not adding
	}

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String richTextTitle = (String) sessionMap.get(VoteAppConstants.ACTIVITY_TITLE_KEY);
	String richTextInstructions = (String) sessionMap.get(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);

	    List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);

	}

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * submitAllContent
     * 
     * persists the nominations list and other user selections in the db.
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

	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	Map mapNominationContent = AuthoringUtil.extractMapNominationContent(listNominationContentDTO);

	Map mapFeedback = AuthoringUtil.extractMapFeedback(listNominationContentDTO);

	ActionMessages errors = new ActionMessages();

	if (mapNominationContent.size() == 0
		&& (voteAuthoringForm.getAssignedDataFlowObject() == null || voteAuthoringForm
			.getAssignedDataFlowObject() == 0)) {
	    ActionMessage error = new ActionMessage("nominations.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	String maxNomCount = voteAuthoringForm.getMaxNominationCount();

	if (activeModule != null) {
	    if (activeModule.equals(VoteAppConstants.AUTHORING)) {
		if (maxNomCount != null) {
		    if (maxNomCount.equals("0") || maxNomCount.contains("-")) {
			ActionMessage error = new ActionMessage("maxNomination.invalid");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		    }

		    try {
			int intMaxNomCount = new Integer(maxNomCount).intValue();
		    } catch (NumberFormatException e) {
			ActionMessage error = new ActionMessage("maxNomination.invalid");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		    }
		}
	    }
	}

	boolean nominationsDuplicate = AuthoringUtil.verifyDuplicateNominations(mapNominationContent);

	if (nominationsDuplicate == true) {
	    ActionMessage error = new ActionMessage("nominations.duplicate");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	DataFlowObject assignedDataFlowObject = null;

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    List attachmentListBackup = new ArrayList();
	    List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
	    attachmentListBackup = attachmentList;

	    List deletedAttachmentListBackup = new ArrayList();
	    List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	    deletedAttachmentListBackup = deletedAttachmentList;

	    String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	    voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);

	    List<DataFlowObject> dataFlowObjects = voteService.getDataFlowObjects(new Long(strToolContentID));
	    List<String> dataFlowObjectNames = null;
	    if (dataFlowObjects != null) {
		dataFlowObjectNames = new ArrayList<String>(dataFlowObjects.size());
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
	}

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	VoteContent voteContentTest = voteService.retrieveVote(new Long(strToolContentID));
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    VoteAction.logger.error("errors saved: " + errors);
	}

	VoteContent voteContent = voteContentTest;
	if (errors.isEmpty()) {
	    /* to remove deleted entries in the questions table based on mapNominationContent */
	    authoringUtil.removeRedundantNominations(mapNominationContent, voteService, voteAuthoringForm, request,
		    strToolContentID);

	    voteContent = authoringUtil.saveOrUpdateVoteContent(mapNominationContent, mapFeedback, voteService,
		    voteAuthoringForm, request, voteContentTest, strToolContentID, assignedDataFlowObject);

	    long defaultContentID = 0;
	    defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);

	    if (voteContent != null) {
		voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }

	    authoringUtil.reOrganizeDisplayOrder(mapNominationContent, voteService, voteAuthoringForm, voteContent);

	    if (activeModule.equals(VoteAppConstants.AUTHORING)) {
		//since it is authoring save the attachments

		List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
		List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

		List attachments = saveAttachments(voteContent, attachmentList, deletedAttachmentList, mapping, request);
	    }

	    VoteUtils.setDefineLater(request, false, strToolContentID, voteService);

	    if (activeModule.equals(VoteAppConstants.AUTHORING)) {
		//standard authoring close
		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    } else {
		//go back to view only screen
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	    }

	} else {

	    if (voteContent != null) {
		long defaultContentID = 0;
		defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);

		if (voteContent != null) {
		    voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

	    }
	}

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());

	voteAuthoringForm.resetUserAction();
	voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("1");

	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    private VoteToolContentHandler getToolContentHandler() {
	if (toolContentHandler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    toolContentHandler = (VoteToolContentHandler) wac.getBean("voteToolContentHandler");
	}
	return toolContentHandler;
    }

    /**
     * saveAttachments
     * 
     * persists uploaded files
     * 
     * @param voteContent
     * @param attachmentList
     * @param deletedAttachmentList
     * @param mapping
     * @param request
     * @return
     */
    private List saveAttachments(VoteContent voteContent, List attachmentList, List deletedAttachmentList,
	    ActionMapping mapping, HttpServletRequest request) {

	if (attachmentList == null || deletedAttachmentList == null) {
	    return null;
	}

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	if (deletedAttachmentList != null) {
	    Iterator iter = deletedAttachmentList.iterator();
	    while (iter.hasNext()) {
		VoteUploadedFile attachment = (VoteUploadedFile) iter.next();

		if (attachment.getSubmissionId() != null) {
		    voteService.removeFile(attachment.getSubmissionId());
		}
	    }
	    deletedAttachmentList.clear();
	}

	if (attachmentList != null) {
	    Iterator iter = attachmentList.iterator();
	    while (iter.hasNext()) {
		VoteUploadedFile attachment = (VoteUploadedFile) iter.next();

		if (attachment.getSubmissionId() == null) {
		    /* add entry to tool table - file already in content repository */
		    voteService.persistFile(voteContent, attachment);
		}
	    }
	}

	return deletedAttachmentList;
    }

    /**
     * deleteFile
     * 
     * removes an uploaded file
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
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = request.getParameter(VoteAppConstants.HTTP_SESSION_ID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	List listQuestionContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listQuestionContentDTO);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	String onlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	String offlineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	voteAuthoringForm.setOnlineInstructions(onlineInstructions);
	voteAuthoringForm.setOfflineInstructions(offlineInstructions);
	voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	String richTextTitle = (String) sessionMap.get(VoteAppConstants.ACTIVITY_TITLE_KEY);
	String richTextInstructions = (String) sessionMap.get(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);
	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	long uuid = WebUtil.readLongParam(request, VoteAppConstants.UUID);

	List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
	if (attachmentList == null) {
	    attachmentList = new ArrayList();
	}

	List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);
	if (deletedAttachmentList == null) {
	    deletedAttachmentList = new ArrayList();
	}

	/*
	 * move the file's details from the attachment collection to the deleted attachments collection the attachment
	 * will be delete on saving.
	 */

	deletedAttachmentList = VoteUtils.moveToDelete(Long.toString(uuid), attachmentList, deletedAttachmentList);
	sessionMap.put(VoteAppConstants.ATTACHMENT_LIST_KEY, attachmentList);
	sessionMap.put(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY, deletedAttachmentList);

	voteGeneralAuthoringDTO.setAttachmentList(attachmentList);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("3");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listQuestionContentDTO.size()));

	voteAuthoringForm.resetUserAction();
	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * addNewFile
     * 
     * adds a new uploaded file to the uploadedfiles list
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
	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String onlineInstructions = request.getParameter(VoteAppConstants.ONLINE_INSTRUCTIONS);

	String offlineInstructions = request.getParameter(VoteAppConstants.OFFLINE_INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY, onlineInstructions);
	sessionMap.put(VoteAppConstants.OFFLINE_INSTRUCTIONS, offlineInstructions);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	List attachmentList = (List) sessionMap.get(VoteAppConstants.ATTACHMENT_LIST_KEY);
	List deletedAttachmentList = (List) sessionMap.get(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY);

	addFileToContentRepository(request, voteAuthoringForm, attachmentList, deletedAttachmentList, sessionMap,
		voteGeneralAuthoringDTO);

	sessionMap.put(VoteAppConstants.ATTACHMENT_LIST_KEY, attachmentList);
	sessionMap.put(VoteAppConstants.DELETED_ATTACHMENT_LIST_KEY, deletedAttachmentList);

	voteGeneralAuthoringDTO.setAttachmentList(attachmentList);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setCurrentTab("3");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	voteAuthoringForm.resetUserAction();

	String strOnlineInstructions = request.getParameter("onlineInstructions");
	String strOfflineInstructions = request.getParameter("offlineInstructions");
	voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
	return mapping.findForward(VoteAppConstants.LOAD_QUESTIONS);
    }

    /**
     * persists files data in the content repository
     * 
     * @param request
     * @param voteAuthoringForm
     * @param attachmentList
     * @param deletedAttachmentList
     * @param sessionMap
     * @param voteGeneralAuthoringDTO
     */
    public void addFileToContentRepository(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm,
	    List attachmentList, List deletedAttachmentList, SessionMap sessionMap,
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	if (attachmentList == null) {
	    attachmentList = new ArrayList();
	}

	if (deletedAttachmentList == null) {
	    deletedAttachmentList = new ArrayList();
	}

	FormFile uploadedFile = null;
	boolean isOnlineFile = false;
	String fileType = null;
	if (voteAuthoringForm.getTheOfflineFile() != null && voteAuthoringForm.getTheOfflineFile().getFileSize() > 0) {
	    uploadedFile = voteAuthoringForm.getTheOfflineFile();
	    fileType = IToolContentHandler.TYPE_OFFLINE;
	} else if (voteAuthoringForm.getTheOnlineFile() != null
		&& voteAuthoringForm.getTheOnlineFile().getFileSize() > 0) {
	    uploadedFile = voteAuthoringForm.getTheOnlineFile();
	    isOnlineFile = true;
	    fileType = IToolContentHandler.TYPE_ONLINE;
	} else {
	    /* no file uploaded */
	    return;
	}

	// validate upload file size.
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(uploadedFile, true, errors);
	if (!errors.isEmpty()) {
	    this.saveErrors(request, errors);
	    return;
	}

	/* if a file with the same name already exists then move the old one to deleted */
	deletedAttachmentList = VoteUtils.moveToDelete(uploadedFile.getFileName(), isOnlineFile, attachmentList,
		deletedAttachmentList);

	try {
	    /*
	     * This is a new file and so is saved to the content repository. Add it to the attachments collection, but
	     * don't add it to the tool's tables yet.
	     */
	    NodeKey node = getToolContentHandler().uploadFile(uploadedFile.getInputStream(),
		    uploadedFile.getFileName(), uploadedFile.getContentType(), fileType);
	    VoteUploadedFile file = new VoteUploadedFile();
	    String fileName = uploadedFile.getFileName();

	    if (fileName != null && fileName.length() > 30) {
		fileName = fileName.substring(0, 31);
	    }

	    file.setFileName(fileName);
	    file.setFileOnline(isOnlineFile);
	    file.setUuid(node.getUuid().toString());
	    /* file.setVersionId(node.getVersion()); */

	    /* add the files to the attachment collection - if one existed, it should have already been removed. */
	    attachmentList.add(file);

	    /* reset the fields so that more files can be uploaded */
	    voteAuthoringForm.setTheOfflineFile(null);
	    voteAuthoringForm.setTheOnlineFile(null);
	} catch (FileNotFoundException e) {
	    VoteAction.logger.error("Unable to uploadfile", e);
	    throw new RuntimeException("Unable to upload file, exception was " + e.getMessage());
	} catch (IOException e) {
	    VoteAction.logger.error("Unable to uploadfile", e);
	    throw new RuntimeException("Unable to upload file, exception was " + e.getMessage());
	} catch (RepositoryCheckedException e) {
	    VoteAction.logger.error("Unable to uploadfile", e);
	    throw new RuntimeException("Unable to upload file, exception was " + e.getMessage());
	}
    }
}
