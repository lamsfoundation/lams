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

package org.lamsfoundation.lams.tool.dokumaran.web.action;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.web.form.DokumaranForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 */
public class AuthoringAction extends Action {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Dokumaran Author function---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = getAccessMode(request);
	    // teacher mode "check for new" button enter.
	    if (mode != null) {
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    } else {
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR.toString());
	    }
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IDokumaranService service = getDokumaranService();
	    Dokumaran dokumaran = service.getDokumaranByContentId(contentId);

	    dokumaran.setDefineLater(true);
	    service.saveOrUpdateDokumaran(dokumaran);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}

	return mapping.findForward(DokumaranConstants.ERROR);
    }

    /**
     * Read dokumaran data from database and put them into HttpSession. It will
     * redirect to init.do directly after this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item
     * lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, DokumaranConstants.PARAM_TOOL_CONTENT_ID));

	// get back the dokumaran and item list and display them on page
	IDokumaranService service = getDokumaranService();

	Dokumaran dokumaran = null;
	DokumaranForm dokumaranForm = (DokumaranForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	dokumaranForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	dokumaranForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    dokumaran = service.getDokumaranByContentId(contentId);
	    // if dokumaran does not exist, try to use default content instead.
	    if (dokumaran == null) {
		dokumaran = service.getDefaultContent(contentId);
	    }

	    dokumaranForm.setDokumaran(dokumaran);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	sessionMap.put(DokumaranConstants.ATTR_RESOURCE_FORM, dokumaranForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return mapping.findForward(DokumaranConstants.SUCCESS);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, DokumaranConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	DokumaranForm existForm = (DokumaranForm) sessionMap.get(DokumaranConstants.ATTR_RESOURCE_FORM);

	DokumaranForm dokumaranForm = (DokumaranForm) form;
	try {
	    PropertyUtils.copyProperties(dokumaranForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	
	return mapping.findForward(DokumaranConstants.SUCCESS);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include
     * all dokumaran item, information etc.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DokumaranForm dokumaranForm = (DokumaranForm) form;

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(dokumaranForm.getSessionMapID());
	ToolAccessMode mode = getAccessMode(request);
	Dokumaran dokumaran = dokumaranForm.getDokumaran();
	IDokumaranService service = getDokumaranService();

	// **********************************Get Dokumaran PO*********************
	Dokumaran dokumaranPO = service.getDokumaranByContentId(dokumaran.getContentId());
	if (dokumaranPO == null) {
	    // new Dokumaran, create it
	    dokumaranPO = dokumaran;
	    dokumaranPO.setCreated(new Timestamp(new Date().getTime()));
	    dokumaranPO.setUpdated(new Timestamp(new Date().getTime()));
	    
	} else {
	    Long uid = dokumaranPO.getUid();
	    PropertyUtils.copyProperties(dokumaranPO, dokumaran);

	    // copyProperties() above may result in "collection assigned to two objects in a session" exception
	    // Below we remove reference to one of Assessment objects,
	    // so maybe there will be just one object in session when save is done
	    // If this fails, we may have to evict the object from session using DAO
	    dokumaranForm.setDokumaran(null);
	    dokumaran = null;
	    // get back UID
	    dokumaranPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		dokumaranPO.setDefineLater(false);
	    }
	    dokumaranPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	DokumaranUser dokumaranUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		dokumaranPO.getContentId());
	if (dokumaranUser == null) {
	    dokumaranUser = new DokumaranUser(user, dokumaranPO);
	}

	dokumaranPO.setCreatedBy(dokumaranUser);

	// ***************************** finally persist dokumaranPO again
	service.saveOrUpdateDokumaran(dokumaranPO);

	dokumaranForm.setDokumaran(dokumaranPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	
	return mapping.findForward(DokumaranConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return DokumaranService bean.
     */
    private IDokumaranService getDokumaranService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IDokumaranService) wac.getBean(DokumaranConstants.RESOURCE_SERVICE);
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR
     * mode.
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
