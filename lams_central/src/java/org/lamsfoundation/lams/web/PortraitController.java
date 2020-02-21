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

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class PortraitController {
    private static Logger log = Logger.getLogger(PortraitController.class);
    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("/portrait")
    public String execute(@ModelAttribute("PortraitActionForm") PortraitActionForm portraitForm,
	    HttpServletRequest request) {
	UUID portraitUuid = userManagementService.getUserByLogin(request.getRemoteUser()).getPortraitUuid();
	log.debug("using portraitUuid=" + portraitUuid);
	// if no portrait has been uploaded, set the uuid to 0
	portraitForm.setPortraitUuid(portraitUuid == null ? "0" : portraitUuid.toString());
	return "profile/portrait";
    }
}
