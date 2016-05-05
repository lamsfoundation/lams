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
package org.lamsfoundation.lams.tool.peerreview.web.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.tool.peerreview.web.form.PeerreviewForm;
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
	// -----------------------Peerreview Author function
	// ---------------------------
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
	    IPeerreviewService service = getPeerreviewService();
	    Peerreview peerreview = service.getPeerreviewByContentId(contentId);

	    peerreview.setDefineLater(true);
	    service.saveOrUpdatePeerreview(peerreview);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}

	return mapping.findForward(PeerreviewConstants.ERROR);
    }

    /**
     * Read peerreview data from database and put them into HttpSession. It will
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
	Long contentId = new Long(WebUtil.readLongParam(request, PeerreviewConstants.PARAM_TOOL_CONTENT_ID));

	// get back the peerreview and item list and display them on page
	IPeerreviewService service = getPeerreviewService();

	Peerreview peerreview = null;
	PeerreviewForm peerreviewForm = (PeerreviewForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	peerreviewForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	peerreviewForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    peerreview = service.getPeerreviewByContentId(contentId);
	    // if peerreview does not exist, try to use default content instead.
	    if (peerreview == null) {
		peerreview = service.getDefaultContent(contentId);
	    }

	    peerreviewForm.setPeerreview(peerreview);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	// get rating criterias from DB
	List<RatingCriteria> ratingCriterias = service.getRatingCriterias(contentId);
	sessionMap.put(AttributeNames.ATTR_RATING_CRITERIAS, ratingCriterias);

	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW_FORM, peerreviewForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return mapping.findForward(PeerreviewConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	PeerreviewForm existForm = (PeerreviewForm) sessionMap.get(PeerreviewConstants.ATTR_PEERREVIEW_FORM);

	PeerreviewForm peerreviewForm = (PeerreviewForm) form;
	try {
	    PropertyUtils.copyProperties(peerreviewForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(PeerreviewConstants.SUCCESS);
	} else {
	    return mapping.findForward(PeerreviewConstants.DEFINE_LATER);
	}
    }

    /**
     * This method will persist all inforamtion in this authoring page, include
     * all peerreview item, information etc.
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
	PeerreviewForm peerreviewForm = (PeerreviewForm) form;

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(peerreviewForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	ActionMessages errors = validate(peerreviewForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    if (mode.isAuthor()) {
		return mapping.findForward("author");
	    } else {
		return mapping.findForward("monitor");
	    }
	}

	Peerreview peerreview = peerreviewForm.getPeerreview();
	IPeerreviewService service = getPeerreviewService();

	// **********************************Get Peerreview
	// PO*********************
	Peerreview peerreviewPO = service.getPeerreviewByContentId(peerreviewForm.getPeerreview().getContentId());
	if (peerreviewPO == null) {
	    // new Peerreview, create it.
	    peerreviewPO = peerreview;
	    peerreviewPO.setCreated(new Timestamp(new Date().getTime()));
	    peerreviewPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    if (mode.isAuthor()) {
		Long uid = peerreviewPO.getUid();
		PropertyUtils.copyProperties(peerreviewPO, peerreview);
		// get back UID
		peerreviewPO.setUid(uid);
	    } else { // if it is Teacher, then just update basic tab content
		// (definelater)
		peerreviewPO.setInstructions(peerreview.getInstructions());
		peerreviewPO.setTitle(peerreview.getTitle());
		// change define later status
		peerreviewPO.setDefineLater(false);
	    }
	    peerreviewPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	PeerreviewUser peerreviewUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		peerreviewForm.getPeerreview().getContentId());
	if (peerreviewUser == null) {
	    peerreviewUser = new PeerreviewUser(user, peerreviewPO);
	}

	peerreviewPO.setCreatedBy(peerreviewUser);

	// finally persist peerreviewPO
	service.saveOrUpdatePeerreview(peerreviewPO);

	// ************************* Handle rating criterias *******************
	Long contentId = peerreview.getContentId();
	List<RatingCriteria> oldCriterias = (List<RatingCriteria>) sessionMap.get(AttributeNames.ATTR_RATING_CRITERIAS);
	service.saveRatingCriterias(request, oldCriterias, contentId);

	peerreviewForm.setPeerreview(peerreviewPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	} else {
	    return mapping.findForward("monitor");
	}
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return PeerreviewService bean.
     */
    private IPeerreviewService getPeerreviewService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IPeerreviewService) wac.getBean(PeerreviewConstants.PEERREVIEW_SERVICE);
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

    private ActionMessages validate(PeerreviewForm peerreviewForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	// if (StringUtils.isBlank(peerreviewForm.getPeerreview().getTitle())) {
	// ActionMessage error = new
	// ActionMessage("error.resource.item.title.blank");
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
