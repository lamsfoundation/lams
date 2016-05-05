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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Manages tutorial videos - the ones displayed on top of pages, explaining how to use certain features of LAMS.
 *
 *
 */
public class TutorialAction extends DispatchAction {
    private static Logger log = Logger.getLogger(TutorialAction.class);

    private static IUserManagementService service;

    /**
     * Invoked when an user chose not to show a certain video again.
     *
     * @param mapping
     * @param form
     * @param req
     * @param res
     * @return
     */
    public ActionForward disableSingleTutorialVideo(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) {

	String pageString = WebUtil.readStrParam(req, AttributeNames.ATTR_PAGE_STR);

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = getService().getUserByLogin(userDTO.getLogin());

	user.getPagesWithDisabledTutorials().add(pageString);
	getService().save(user);

	ss.setAttribute(AttributeNames.USER, user.getUserDTO());

	return null;
    }

    /**
     * Invoked when an user asked to show a certain video again.
     *
     * @param mapping
     * @param form
     * @param req
     * @param res
     * @return
     */
    public ActionForward enableSingleTutorialVideo(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) {

	String pageString = WebUtil.readStrParam(req, AttributeNames.ATTR_PAGE_STR);

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = getService().getUserByLogin(userDTO.getLogin());

	user.getPagesWithDisabledTutorials().remove(pageString);
	getService().save(user);

	ss.setAttribute(AttributeNames.USER, user.getUserDTO());

	return null;
    }

    /**
     * Gets the value for "Do not show again" checkbox for a cerain video.
     *
     * @param mapping
     * @param form
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    public ActionForward getDoNotShowAgainValue(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException {

	String pageString = WebUtil.readStrParam(req, AttributeNames.ATTR_PAGE_STR);

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Boolean doNotShowAgain = userDTO.getPagesWithDisabledTutorials() != null
		&& userDTO.getPagesWithDisabledTutorials().contains(pageString);
	res.setContentType("text/plain");
	PrintWriter writer = res.getWriter();
	writer.println(doNotShowAgain.toString());

	return null;
    }

    /**
     * Turns off tutorials. Same as going to user profile and turning them off. Used for dialog displayed after user's
     * first login.
     *
     * @param mapping
     * @param form
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    public ActionForward disableAllTutorialVideos(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException {

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = getService().getUserByLogin(userDTO.getLogin());

	user.setTutorialsDisabled(true);
	getService().save(user);

	ss.setAttribute(AttributeNames.USER, user.getUserDTO());

	return null;
    }

    private IUserManagementService getService() {
	if (TutorialAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    TutorialAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return TutorialAction.service;
    }
}