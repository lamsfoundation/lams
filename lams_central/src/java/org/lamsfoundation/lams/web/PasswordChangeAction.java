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
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Fei Yang
 */
public class PasswordChangeAction extends Action {

    private static Logger log = Logger.getLogger(PasswordChangeAction.class);

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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// -- isCancelled?
	if (isCancelled(request)) {
	    request.getSession().removeAttribute(PasswordChangeActionForm.formName);
	    return mapping.findForward("cancelled");
	}

	ActionMessages errors = new ActionMessages();

	PasswordChangeActionForm passwordChangeForm = (PasswordChangeActionForm) form;

	if (errors.isEmpty()) {
	    try {

		String loggedInUser = request.getRemoteUser();
		String login = passwordChangeForm.getLogin();
		String oldPassword = passwordChangeForm.getOldPassword();
		String password = passwordChangeForm.getPassword();
		String passwordConfirm = passwordChangeForm.getPasswordConfirm();

		if ((loggedInUser == null) || !loggedInUser.equals(login)) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.authorisation"));
		} else {
		    // WebApplicationContext ctx =
		    // WebApplicationContextUtils.getWebApplicationContext(request.getSession(true).getServletContext());
		    WebApplicationContext ctx = WebApplicationContextUtils
			    .getWebApplicationContext(getServlet().getServletContext());
		    UserManagementService service = (UserManagementService) ctx.getBean("userManagementService");

		    User user = service.getUserByLogin(login);
		    String passwordHash = user.getPassword().length() == HashUtil.SHA1_HEX_LENGTH
			    ? HashUtil.sha1(oldPassword) : HashUtil.sha256(oldPassword, user.getSalt());

		    if (!user.getPassword().equals(passwordHash)) {
			errors.add("oldPassword", new ActionMessage("error.oldpassword.mismatch"));
			PasswordChangeAction.log.debug("old pass wrong");
		    }
		    if (!password.equals(passwordConfirm)) {
			errors.add("password", new ActionMessage("error.newpassword.mismatch"));
			PasswordChangeAction.log.debug("new pass wrong");
		    }
		    if ((password == null) || (password.length() == 0)) {
			errors.add("password", new ActionMessage("error.password.empty"));
			PasswordChangeAction.log.debug("new password cannot be empty");
		    }
		    if (errors.isEmpty()) {
			String salt = HashUtil.salt();
			user.setSalt(salt);
			user.setPassword(HashUtil.sha256(password, salt));
			user.setChangePassword(false);
			service.saveUser(user);

			// make 'password changed' audit log entry
			IAuditService auditService = (IAuditService) ctx.getBean("auditService");
			MessageService messageService = (MessageService) ctx.getBean("centralMessageService");
			String[] args = new String[1];
			args[0] = user.getLogin() + "(" + user.getUserId() + ")";
			String message = messageService.getMessage("audit.user.password.change", args);
			auditService.log(CentralConstants.MODULE_NAME, message);
		    }
		}

	    } catch (Exception e) {
		PasswordChangeAction.log.error("Exception occured ", e);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
	    }

	} // end if no errors

	// -- Report any errors
	if (!errors.isEmpty()) {
	    saveMessages(request, errors);
	    if (mapping.getInput() != null) {
		passwordChangeForm.reset(mapping, request);
		// return (new ActionForward(mapping.getInput()));
		return (mapping.findForward("errors"));
	    }
	    // If no input page, use error forwarding
	    return (mapping.findForward("error.system"));
	}
	request.setAttribute("redirectURL",passwordChangeForm.getRedirectURL());
	return mapping.findForward("okay");

    }
}
