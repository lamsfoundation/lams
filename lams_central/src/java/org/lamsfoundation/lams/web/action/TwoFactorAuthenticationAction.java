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

package org.lamsfoundation.lams.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * @author Andrey Balan
 */
public class TwoFactorAuthenticationAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	WebApplicationContext ctx = WebApplicationContextUtils
		.getWebApplicationContext(getServlet().getServletContext());
	UserManagementService userManagementService = (UserManagementService) ctx.getBean("userManagementService");
	
	// check if user needs to get his shared two-factor authorization secret
	User loggedInUser = userManagementService.getUserByLogin(request.getRemoteUser());
	if (loggedInUser.isTwoFactorAuthenticationEnabled() && loggedInUser.getTwoFactorAuthenticationSecret() == null) {
	    
	    GoogleAuthenticator gAuth = new GoogleAuthenticator();
	    final GoogleAuthenticatorKey key = gAuth.createCredentials();
	    String sharedSecret = key.getKey();
	    
	    loggedInUser.setTwoFactorAuthenticationSecret(sharedSecret);
	    userManagementService.saveUser(loggedInUser);
	    
	    request.setAttribute("sharedSecret", sharedSecret);
	    String QRCode = GoogleAuthenticatorQRGenerator.getOtpAuthURL(null, "LAMS account: " + loggedInUser.getLogin(), key);
	    request.setAttribute("QRCode", QRCode);
	}

	return mapping.findForward("secret");
    }
}
