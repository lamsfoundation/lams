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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.admin.web.dto.LinkBean;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 *
 */
@Controller
public class AppAdminStartController {

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/appadminstart")
    public String execute(HttpServletRequest request) throws Exception {
	ArrayList<Object[]> groupedLinks = new ArrayList<>();
	ArrayList<LinkBean> links = new ArrayList<>();

	if (request.isUserInRole(Role.SYSADMIN)) {
	    links = new ArrayList<>();
	    links.add(new LinkBean("config.do", "sysadmin.config.settings.edit"));
	    links.add(new LinkBean("extserver/serverlist.do", "appadmin.maintain.external.servers"));
	    links.add(new LinkBean("ltiConsumerManagement/start.do", "label.manage.tool.consumers"));
	    // links.add(new LinkBean("ldap/start.do", "sysadmin.ldap.configuration"));
	    groupedLinks.add(new Object[] { AdminConstants.START_SYSADMIN_CONFIG_LINKS, links });
	}

	if (request.isUserInRole(Role.APPADMIN) || request.isUserInRole(Role.SYSADMIN)) {
	    links = new ArrayList<>();
	    links.add(new LinkBean("timezonemanagement/start.do", "admin.timezone.title"));
	    links.add(new LinkBean("loginmaintain.do", "appadmin.maintain.loginpage"));
	    links.add(new LinkBean("signupManagement/start.do", "admin.signup.title"));
	    links.add(new LinkBean("policyManagement/list.do", "admin.policies.title"));
	    links.add(new LinkBean("toolcontentlist/start.do", "appadmin.tool.management"));
	    links.add(new LinkBean("../outcome/outcomeManage.do", "admin.outcome.title"));
	    links.add(new LinkBean("themeManagement/start.do", "admin.themes.title"));
	    links.add(new LinkBean("sessionmaintain/list.do", "appadmin.maintain.session"));
	    groupedLinks.add(new Object[] { AdminConstants.START_APPADMIN_CONFIG_LINKS, links });

	    links = new ArrayList<>();
	    links.add(new LinkBean("logevent/start.do", "label.event.log"));
	    links.add(new LinkBean("cleanup/start.do", "sysadmin.cleanup"));
	    links.add(new LinkBean("cleanupPreviewLessons/start.do", "appadmin.batch.preview.lesson.delete"));
	    links.add(new LinkBean("statistics/start.do", "admin.statistics.title"));
	    groupedLinks.add(new Object[] { AdminConstants.START_MONITOR_LINKS, links });

	    links = new ArrayList<>();
	    links.add(new LinkBean("usersearch.do", "admin.user.find"));
	    links.add(new LinkBean("importgroups.do", "appadmin.import.groups.title"));
	    links.add(new LinkBean("importexcel.do", "admin.user.import"));
	    links.add(new LinkBean("disabledmanage.do", "admin.list.disabled.users"));
	    groupedLinks.add(new Object[] { AdminConstants.START_COURSE_LINKS, links });

	    // LKC-213
	    if (Configuration.getAsBoolean(ConfigurationKeys.WORKFLOW_AUTOMATION_ENABLE)) {
		links = new ArrayList<>();
		links.add(new LinkBean("../wa/blueprint/show.do", "admin.workflow.automation.module.management"));
		links.add(new LinkBean("../wa/container/list.do", "admin.workflow.automation.container.list"));
		groupedLinks.add(new Object[] { AdminConstants.START_WORKFLOW_AUTOMATION, links });
	    }

	    // LDEV-5375
	    boolean isAiEnabled = Configuration.isLamsModuleAvailable(Configuration.AI_MODULE_CLASS);
	    if (isAiEnabled) {
		links = new ArrayList<>();
		links.add(new LinkBean("../ai/admin/start.do", "ai.config.link"));
		groupedLinks.add(new Object[] { AdminConstants.START_AI, links });
	    }

	} else if (userManagementService.isUserGlobalGroupManager()) {
	    links.add(new LinkBean("usersearch.do", "admin.user.find"));
	    links.add(new LinkBean("importgroups.do", "appadmin.import.groups.title"));
	    links.add(new LinkBean("importexcel.do", "admin.user.import"));
	    links.add(new LinkBean("disabledmanage.do", "admin.list.disabled.users"));
	    groupedLinks.add(new Object[] { AdminConstants.START_COURSE_LINKS, links });

	} else {
	    request.setAttribute("errorName", "AppAdminStartAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	request.setAttribute("groupedLinks", groupedLinks);
	return "appadmin";
    }

}
