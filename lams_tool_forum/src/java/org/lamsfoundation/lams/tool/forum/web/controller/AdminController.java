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

package org.lamsfoundation.lams.tool.forum.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.forum.model.ForumConfigItem;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.web.forms.AdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Marcin Cieslak
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IForumService forumService;

    @RequestMapping("/start")
    public String start(@ModelAttribute AdminForm adminForm, HttpServletRequest request) {

	ForumConfigItem keepLearnerContent = forumService.getConfigItem(ForumConfigItem.KEY_KEEP_LEARNER_CONTENT);
	if (keepLearnerContent != null) {
	    adminForm.setKeepLearnerContent(Boolean.valueOf(keepLearnerContent.getConfigValue()));
	}

	request.setAttribute("error", false);
	return "jsps/admin/config";
    }

    @RequestMapping("/saveContent")
    public String saveContent(@ModelAttribute AdminForm adminForm, HttpServletRequest request) {

	ForumConfigItem keepLearnerContent = forumService.getConfigItem(ForumConfigItem.KEY_KEEP_LEARNER_CONTENT);
	keepLearnerContent.setConfigValue(String.valueOf(adminForm.getKeepLearnerContent()));
	forumService.saveForumConfigItem(keepLearnerContent);

	request.setAttribute("savedSuccess", true);
	return "jsps/admin/config";
    }
}