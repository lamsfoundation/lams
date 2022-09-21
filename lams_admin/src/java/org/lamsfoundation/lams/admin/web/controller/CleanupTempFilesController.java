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

package org.lamsfoundation.lams.admin.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.CleanupForm;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.TempDirectoryFilter;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
@RequestMapping("/cleanup")
public class CleanupTempFilesController {
    private static Logger log = Logger.getLogger(CleanupTempFilesController.class);

    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/start")
    public String execute(@ModelAttribute CleanupForm cleanupForm, HttpServletRequest request) throws Exception {

	// check user is appadmin
	if (!(request.isUserInRole(Role.APPADMIN))) {
	    request.setAttribute("errorName", "CleanupTempFilesAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.need.appadmin"));
	    return "error";
	}

	// check if url contains request for refresh folder sizes only
	String action = WebUtil.readStrParam(request, "action", true);
	if (action != null && StringUtils.equals(action, "refresh")) {
	    return refresh(cleanupForm, request);
	}

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	Integer numDays = cleanupForm.getNumDays();

	// delete directories if form has been submitted
	if (numDays != null) {
	    if (numDays >= 0) {
		int filesDeleted = FileUtil.cleanupOldFiles(FileUtil.getOldTempFiles(numDays));
		String args[] = new String[1];
		args[0] = new Integer(filesDeleted).toString();
		request.setAttribute("filesDeleted", messageService.getMessage("msg.cleanup.files.deleted", args));
	    } else {
		errorMap.add("numDays", messageService.getMessage("error.non.negative.number.required"));
	    }
	} else {
	    // recommended number of days to leave temp files
	    cleanupForm.setNumDays(1);
	}
	request.setAttribute("errorMap", errorMap);
	return "cleanup";
    }

    @RequestMapping("/refresh")
    public String refresh(@ModelAttribute CleanupForm cleanupForm, HttpServletRequest request) throws Exception {

	// display temp files info
	File oldFiles[] = FileUtil.getOldTempFiles(0);
	long zipTotal = 0;
	long tmpTotal = 0;
	if (oldFiles != null) {
	    for (int i = 0; i < oldFiles.length; i++) {
		if (oldFiles[i].getName().startsWith(TempDirectoryFilter.zip_prefix)) {
		    zipTotal += FileUtil.calculateFileSize(oldFiles[i]);
		} else if (oldFiles[i].getName().startsWith(TempDirectoryFilter.tmp_prefix)) {
		    tmpTotal += FileUtil.calculateFileSize(oldFiles[i]);
		}
	    }
	}
	request.setAttribute("zipTotal", zipTotal / 1024);
	request.setAttribute("tmpTotal", tmpTotal / 1024);

	// set default numDays
	Integer numDays = cleanupForm.getNumDays();
	if (numDays == null) {
	    cleanupForm.setNumDays(1);
	}

	return "cleanup";
    }

}
