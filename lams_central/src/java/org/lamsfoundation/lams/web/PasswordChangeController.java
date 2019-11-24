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

package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Fei Yang
 */
@Controller
public class PasswordChangeController {

    private static Logger log = Logger.getLogger(PasswordChangeController.class);

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;
    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private IUserManagementService userManagementService;

    /**
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param actionForm
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     *
     */
    @RequestMapping(path = "/passwordChanged", method = RequestMethod.POST)
    public String execute(@ModelAttribute("PasswordChangeActionForm") PasswordChangeActionForm passwordChangeForm,
	    HttpServletRequest request) throws Exception {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if (errorMap.isEmpty()) {
	    try {

		String loggedInUser = request.getRemoteUser();
		String login = passwordChangeForm.getLogin();
		String oldPassword = passwordChangeForm.getOldPassword();
		String password = passwordChangeForm.getPassword();
		String passwordConfirm = passwordChangeForm.getPasswordConfirm();

		if ((loggedInUser == null) || !loggedInUser.equals(login)) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.authorisation"));
		    
		} else {
		    User user = userManagementService.getUserByLogin(login);
		    String passwordHash = user.getPassword().length() == HashUtil.SHA1_HEX_LENGTH
			    ? HashUtil.sha1(oldPassword)
			    : HashUtil.sha256(oldPassword, user.getSalt());

		    if (!user.getPassword().equals(passwordHash)) {
			errorMap.add("oldPassword", messageService.getMessage("error.oldpassword.mismatch"));
			PasswordChangeController.log.debug("old pass wrong");
		    }
		    if (!password.equals(passwordConfirm)) {
			errorMap.add("password", messageService.getMessage("error.newpassword.mismatch"));
			PasswordChangeController.log.debug("new pass wrong");
		    }
		    if ((password == null) || (password.length() == 0)) {
			errorMap.add("password", messageService.getMessage("error.password.empty"));
			PasswordChangeController.log.debug("new password cannot be empty");
		    }
		    if (!ValidationUtil.isPasswordValueValid(password, passwordConfirm)) {
			errorMap.add("password", messageService.getMessage("label.password.restrictions"));
			PasswordChangeController.log.debug("Password must follow the restrictions");
		    }

		    if (errorMap.isEmpty()) {
			String salt = HashUtil.salt();
			user.setSalt(salt);
			user.setPassword(HashUtil.sha256(password, salt));
			user.setChangePassword(false);
			userManagementService.saveUser(user);

			// make 'password changed' audit log entry
			String[] args = new String[1];
			args[0] = user.getLogin() + " (" + user.getUserId() + ")";
			String message = messageService.getMessage("audit.user.password.change", args);
			logEventService.logEvent(LogEvent.TYPE_PASSWORD_CHANGE, user.getUserId(), user.getUserId(), null, null,
				message);
		    }
		}

	    } catch (Exception e) {
		PasswordChangeController.log.error("Exception occured ", e);
		errorMap.add("GLOBAL", messageService.getMessage(e.getMessage()));
	    }

	} // end if no errors

	// -- Report any errors
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "passwordChangeContent";
	}
	request.setAttribute("redirectURL", passwordChangeForm.getRedirectURL());
	return "/passwordChangeOkContent";

    }
}
