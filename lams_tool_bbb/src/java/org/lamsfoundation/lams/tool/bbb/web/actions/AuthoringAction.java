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


package org.lamsfoundation.lams.tool.bbb.web.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.bbb.model.Bbb;
import org.lamsfoundation.lams.tool.bbb.service.BbbServiceProxy;
import org.lamsfoundation.lams.tool.bbb.service.IBbbService;
import org.lamsfoundation.lams.tool.bbb.util.Constants;
import org.lamsfoundation.lams.tool.bbb.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 * @version
 *
 *
 *
 *
 */
public class AuthoringAction extends DispatchAction {

    // private static final Logger logger = Logger.getLogger(AuthoringAction.class);

    private IBbbService bbbService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up bbbService
	bbbService = BbbServiceProxy.getBbbService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     *
     * @throws ServletException
     *
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Bbb with given toolContentID
	Bbb bbb = bbbService.getBbbByContentId(toolContentID);
	if (bbb == null) {
	    bbb = bbbService.copyDefaultContent(toolContentID);
	    bbb.setCreateDate(new Date());
	    bbbService.saveOrUpdateBbb(bbb);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we are editing. This flag is released when updateContent is
	    // called.
	    bbb.setDefineLater(true);
	    bbbService.saveOrUpdateBbb(bbb);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	copyProperties(authForm, bbb);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(bbb, mode, contentFolderID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(Constants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get bbb content.
	Bbb bbb = bbbService.getBbbByContentId((Long) map.get(Constants.KEY_TOOL_CONTENT_ID));

	// update bbb content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(Constants.KEY_MODE);
	copyProperties(bbb, authForm, mode);

	// set the update date
	bbb.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	bbb.setDefineLater(false);

	bbbService.saveOrUpdateBbb(bbb);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(Constants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    /* ========== Private Methods */

    /**
     * Updates Bbb content using AuthoringForm inputs.
     *
     * @param authForm
     * @param mode
     * @return
     */
    private void copyProperties(Bbb bbb, AuthoringForm authForm, ToolAccessMode mode) {
	bbb.setTitle(authForm.getTitle());
	bbb.setInstructions(authForm.getInstructions());
	if (mode.isAuthor()) { // Teacher cannot modify following
	    bbb.setReflectOnActivity(authForm.isReflectOnActivity());
	    bbb.setReflectInstructions(authForm.getReflectInstructions());
	    bbb.setLockOnFinished(authForm.isLockOnFinished());

	}
    }

    /**
     * Updates AuthoringForm using Bbb content.
     *
     * @param bbb
     * @param authForm
     * @return
     * @throws ServletException
     */
    private void copyProperties(AuthoringForm authForm, Bbb bbb) throws ServletException {
	try {
	    BeanUtils.copyProperties(authForm, bbb);
	} catch (IllegalAccessException e) {
	    throw new ServletException(e);
	} catch (InvocationTargetException e) {
	    throw new ServletException(e);
	}
    }

    /**
     * Updates SessionMap using Bbb content.
     *
     * @param bbb
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Bbb bbb, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(Constants.KEY_MODE, mode);
	map.put(Constants.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(Constants.KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     *
     * @param request
     * @param authForm
     * @return
     */
    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }
}
