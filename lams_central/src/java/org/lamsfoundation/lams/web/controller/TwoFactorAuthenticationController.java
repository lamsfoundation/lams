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

package org.lamsfoundation.lams.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * @author Andrey Balan
 */
@Controller
public class TwoFactorAuthenticationController {

    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("/twoFactorAuthentication")
    public String execute(HttpServletRequest request) throws Exception {

	// check if user needs to get his shared two-factor authorization secret
	User loggedInUser = userManagementService.getUserByLogin(request.getRemoteUser());
	if (loggedInUser.isTwoFactorAuthenticationEnabled()
		&& loggedInUser.getTwoFactorAuthenticationSecret() == null) {

	    GoogleAuthenticator gAuth = new GoogleAuthenticator();
	    final GoogleAuthenticatorKey key = gAuth.createCredentials();
	    String sharedSecret = key.getKey();

	    loggedInUser.setTwoFactorAuthenticationSecret(sharedSecret);
	    userManagementService.saveUser(loggedInUser);

	    request.setAttribute("sharedSecret", sharedSecret);
	    String QRCode = GoogleAuthenticatorQRGenerator.getOtpAuthURL(null,
		    "LAMS account: " + loggedInUser.getLogin(), key);
	    request.setAttribute("QRCode", QRCode);
	}

	return "twoFactorAuthSecret";
    }
}
