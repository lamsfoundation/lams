/****************************************************************
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
 * ****************************************************************
 */



package org.lamsfoundation.lams.tool.sbmt.web;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.form.AuthoringForm;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Manpreet Minhas
 * @author Steve Ni
 */
public class AuthoringAction extends LamsDispatchAction {
    private Logger log = Logger.getLogger(AuthoringAction.class);

    public ISubmitFilesService submitFilesService;

    /**
     * This page will display initial submit tool content. Or just a blank page if the toolContentID does not exist
     * before.
     *
     * <BR>
     * Define later will use this method to initial page as well.
     *
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	sessionMap.put(AttributeNames.PARAM_MODE, mode);

	Long contentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	// get back the upload file list and display them on page
	submitFilesService = getService();

	SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);

	if (mode.isTeacher()) {
	    persistContent.setDefineLater(true);
	    submitFilesService.saveOrUpdateContent(persistContent);
	    
	    //audit log the teacher has started editing activity in monitor
	    submitFilesService.auditLogStartEditingActivityInMonitor(contentID);
	}

	// if this content does not exist(empty without id), create a content by default content record.
	if (persistContent == null) {
	    persistContent = submitFilesService.createDefaultContent(contentID);
	}

	// set back STRUTS component value
	AuthoringForm authForm = (AuthoringForm) form;
	authForm.initContentValue(persistContent);
	// session map
	authForm.setSessionMapID(sessionMap.getSessionID());
	authForm.setContentFolderID(contentFolderID);

	return mapping.findForward("success");
    }

    /**
     * Update all content for submit tool except online/offline instruction files list.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws Exception
     */
    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.PARAM_MODE);

	ActionMessages errors = validate(authForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    return mapping.getInputForward();
	}

	SubmitFilesContent content = getContent(form);

	submitFilesService = getService();
	SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(content.getContentID());

	if (persistContent == null) {
	    // new content
	    persistContent = content;
	    content.setCreated(new Date());
	} else {

	    Long uid = persistContent.getContentID();
	    PropertyUtils.copyProperties(persistContent, content);
	    persistContent.setContentID(uid);

	    // if it is Teacher (from monitor) - change define later status
	    if (mode.isTeacher()) {
		persistContent.setDefineLater(false);
	    }
	}
	// *******************************Handle user*******************
	// get session from shared session.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long contentId = authForm.getToolContentID();
	SubmitUser user = submitFilesService.getContentUser(contentId, userDto.getUserID());
	if (user == null) {
	    user = submitFilesService.createContentUser(userDto, contentId);
	}
	persistContent.setCreatedBy(user);

	submitFilesService.saveOrUpdateContent(persistContent);

	// to jump to common success page in lams_central
	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return mapping.findForward("success");
    }

    // ***********************************************************
    // Private/protected methods
    // ***********************************************************
    /**
     * The private method to get content from ActionForm parameters (web page).
     *
     * @param form
     * @return
     */
    private SubmitFilesContent getContent(ActionForm form) {
	AuthoringForm authForm = (AuthoringForm) form;
	Long contentId = authForm.getToolContentID();

	SubmitFilesContent content = new SubmitFilesContent();
	content.setContentID(contentId);
	content.setInstruction(authForm.getInstructions());
	content.setTitle(authForm.getTitle());
	content.setLockOnFinished(authForm.isLockOnFinished());
	content.setReflectInstructions(authForm.getReflectInstructions());
	content.setReflectOnActivity(authForm.isReflectOnActivity());
	content.setLimitUpload(authForm.isLimitUpload());
	content.setLimitUploadNumber(authForm.getLimitUploadNumber());
	content.setNotifyLearnersOnMarkRelease(authForm.isNotifyLearnersOnMarkRelease());
	content.setNotifyTeachersOnFileSubmit(authForm.isNotifyTeachersOnFileSubmit());
	return content;
    }

    /**
     * Get submit file service bean.
     *
     * @return
     */
    private ISubmitFilesService getService() {
	if (submitFilesService == null) {
	    return SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
	} else {
	    return submitFilesService;
	}
    }

    private ActionMessages validate(AuthoringForm sbmtForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	// if (StringUtils.isBlank(sbmtForm.getTitle())) {
	// ActionMessage error = new ActionMessage("error.title.blank");
	// errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	// }
	// define it later mode(TEACHER) skip below validation.
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errors;
	}

	// Some other validation outside basic Tab.

	return errors;
    }

}