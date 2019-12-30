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

package org.lamsfoundation.lams.tool.sbmt.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.web.form.AuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Manpreet Minhas
 * @author Steve Ni
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    @Autowired
    private ISubmitFilesService submitFilesService;

    /**
     * This page will display initial submit tool content. Or just a blank page if the toolContentID does not exist
     * before.
     *
     * <BR>
     * Define later will use this method to initial page as well.
     */
    @RequestMapping("/authoring")
    public String unspecified(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
	// if this content does not exist(empty without id), create a content by default content record.
	if (persistContent == null) {
	    persistContent = submitFilesService.createDefaultContent(contentID);
	}

	return readDatabaseData(authoringForm, persistContent, request, mode);
    }
    
    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
	persistContent.setDefineLater(true);
	submitFilesService.saveOrUpdateContent(persistContent);

	//audit log the teacher has started editing activity in monitor
	submitFilesService.auditLogStartEditingActivityInMonitor(contentID);

	return readDatabaseData(authoringForm, persistContent, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, SubmitFilesContent persistContent, HttpServletRequest request,
	    ToolAccessMode mode) {
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	sessionMap.put(AttributeNames.PARAM_MODE, mode);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	// set back STRUTS component value
	authoringForm.initContentValue(persistContent);
	// session map
	authoringForm.setSessionMapID(sessionMap.getSessionID());
	authoringForm.setContentFolderID(contentFolderID);

	return "authoring/authoring";
    }

    /**
     * Update all content for submit tool except online/offline instruction files list.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.PARAM_MODE);

	MultiValueMap<String, String> errorMap = validate(authoringForm, request);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "authoring/authoring";
	}

	SubmitFilesContent content = getContent(authoringForm);

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

	Long contentId = authoringForm.getToolContentID();
	SubmitUser user = submitFilesService.getContentUser(contentId, userDto.getUserID());
	if (user == null) {
	    user = submitFilesService.createContentUser(userDto, contentId);
	}
	persistContent.setCreatedBy(user);

	submitFilesService.saveOrUpdateContent(persistContent);

	// to jump to common success page in lams_central
	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return "authoring/authoring";
    }

    // ***********************************************************
    // Private/protected methods
    // ***********************************************************
    /**
     * The private method to get content from ActionForm parameters (web page).
     */
    private SubmitFilesContent getContent(AuthoringForm authoringForm) {
	Long contentId = authoringForm.getToolContentID();

	SubmitFilesContent content = new SubmitFilesContent();
	content.setContentID(contentId);
	content.setInstruction(authoringForm.getInstructions());
	content.setTitle(authoringForm.getTitle());
	content.setLockOnFinished(authoringForm.isLockOnFinished());
	content.setReflectInstructions(authoringForm.getReflectInstructions());
	content.setReflectOnActivity(authoringForm.isReflectOnActivity());
	content.setLimitUpload(authoringForm.isLimitUpload());
	content.setLimitUploadNumber(authoringForm.getLimitUploadNumber());
	content.setMinLimitUploadNumber(authoringForm.getMinLimitUploadNumber());
	content.setUseSelectLeaderToolOuput(authoringForm.isUseSelectLeaderToolOuput());
	content.setNotifyLearnersOnMarkRelease(authoringForm.isNotifyLearnersOnMarkRelease());
	content.setNotifyTeachersOnFileSubmit(authoringForm.isNotifyTeachersOnFileSubmit());
	return content;
    }

    private MultiValueMap<String, String> validate(AuthoringForm authoringForm, HttpServletRequest request) {

	// if (StringUtils.isBlank(sbmtForm.getTitle())) {
	// ActionMessage error = new ActionMessage("error.title.blank");
	// errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	// }
	// define it later mode(TEACHER) skip below validation.
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errorMap;
	}
	return errorMap;
    }

}