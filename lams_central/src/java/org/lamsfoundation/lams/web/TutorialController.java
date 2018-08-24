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

package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Manages tutorial videos - the ones displayed on top of pages, explaining how to use certain features of LAMS.
 *
 *
 */
@Controller
@RequestMapping("/tutorial")
public class TutorialController {
    private static Logger log = Logger.getLogger(TutorialController.class);

    @Autowired
    @Qualifier("userManagementService")
    private IUserManagementService service;

    /**
     * Invoked when an user chose not to show a certain video again.
     *
     * @param mapping
     * @param form
     * @param req
     * @param res
     * @return
     */
    @ResponseBody
    @RequestMapping("/disableSingleTutorialVideo")
    public void disableSingleTutorialVideo(HttpServletRequest req) {

	String pageString = WebUtil.readStrParam(req, AttributeNames.ATTR_PAGE_STR);

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = service.getUserByLogin(userDTO.getLogin());

	user.getPagesWithDisabledTutorials().add(pageString);
	service.saveUser(user);

	ss.setAttribute(AttributeNames.USER, user.getUserDTO());

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
    @ResponseBody
    @RequestMapping("/enableSingleTutorialVideo")
    public void enableSingleTutorialVideo(HttpServletRequest req) {

	String pageString = WebUtil.readStrParam(req, AttributeNames.ATTR_PAGE_STR);

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = service.getUserByLogin(userDTO.getLogin());

	user.getPagesWithDisabledTutorials().remove(pageString);
	service.saveUser(user);

	ss.setAttribute(AttributeNames.USER, user.getUserDTO());
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
    @ResponseBody
    @RequestMapping("/getDoNotShowAgainValue")
    public void getDoNotShowAgainValue(HttpServletRequest req, HttpServletResponse res) throws IOException {

	String pageString = WebUtil.readStrParam(req, AttributeNames.ATTR_PAGE_STR);

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Boolean doNotShowAgain = userDTO.getPagesWithDisabledTutorials() != null
		&& userDTO.getPagesWithDisabledTutorials().contains(pageString);
	res.setContentType("text/plain");
	PrintWriter writer = res.getWriter();
	writer.println(doNotShowAgain.toString());
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
    @ResponseBody
    @RequestMapping("/disableAllTutorialVideos")
    public void disableAllTutorialVideos(HttpServletRequest req) throws IOException {

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = service.getUserByLogin(userDTO.getLogin());

	user.setTutorialsDisabled(true);
	service.saveUser(user);

	ss.setAttribute(AttributeNames.USER, user.getUserDTO());
    }
}