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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.ConfigForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.dto.BulkUpdateResultDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.ILdapService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
@RequestMapping("/ldap")
public class LdapConfigController {
    private static Logger log = Logger.getLogger(LdapConfigController.class);

    @Autowired
    private Configuration configurationService;
    @Autowired
    private LdapService ldapService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/start")
    public String execute(@ModelAttribute ConfigForm configForm, HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "open LDAP management panel", true);

	String action = WebUtil.readStrParam(request, "action", true);
	if (action != null) {
	    if (StringUtils.equals(action, "sync")) {
		return sync(request);
	    }
	    if (StringUtils.equals(action, "waiting")) {
		return waiting(request);
	    }
	    if (StringUtils.equals(action, "results")) {
		return results(request);
	    }
	}

	int numLdapUsers = getNumLdapUsers();
	request.setAttribute("numLdapUsersMsg", getNumLdapUsersMsg(numLdapUsers));

	request.setAttribute("config", configurationService.arrangeItems(Configuration.ITEMS_ONLY_LDAP));

	return "ldap";
    }

    @RequestMapping(path = "/sync")
    public String sync(HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "sync LDAP", true);

	String sessionId = SessionManager.getSession().getId();
	Thread t = new Thread(new LdapSyncThread(sessionId));
	t.start();

	request.setAttribute("wait", messageService.getMessage("msg.ldap.synchronise.wait"));

	return "ldap";
    }

    @RequestMapping(path = "/waiting")
    public String waiting(HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "sync LDAP wait", true);

	request.setAttribute("wait", messageService.getMessage("msg.ldap.synchronise.wait"));

	return "ldap";
    }

    @RequestMapping(path = "/results")
    public String results(HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "show LDAP sync results", true);

	HttpSession ss = SessionManager.getSession();
	Object o = ss.getAttribute(ILdapService.SYNC_RESULTS);
	request.setAttribute("syncResults", o);
	if (o instanceof BulkUpdateResultDTO) {
	    BulkUpdateResultDTO dto = (BulkUpdateResultDTO) o;

	    int numLdapUsers = getNumLdapUsers();
	    request.setAttribute("numLdapUsersMsg", getNumLdapUsersMsg(numLdapUsers));

	    request.setAttribute("numSearchResults", getNumSearchResultsUsersMsg(dto.getNumSearchResults()));
	    request.setAttribute("numLdapUsersCreated", getNumCreatedUsersMsg(dto.getNumUsersCreated()));
	    request.setAttribute("numLdapUsersUpdated", getNumUpdatedUsersMsg(dto.getNumUsersUpdated()));
	    request.setAttribute("numLdapUsersDisabled", getNumDisabledUsersMsg(dto.getNumUsersDisabled()));
	    request.setAttribute("messages", dto.getMessages());
	    request.setAttribute("done", messageService.getMessage("msg.done"));
	} else {
	    ArrayList<String> list = new ArrayList<>();
	    list.add((String) o);
	    request.setAttribute("messages", list);
	    request.setAttribute("done", messageService.getMessage("msg.done"));
	}

	// remove session variable that flags bulk update as done
	ss.removeAttribute(ILdapService.SYNC_RESULTS);

	return "ldap";
    }

    private int getNumLdapUsers() {
	Integer count = userManagementService.getCountUsers(AuthenticationMethod.LDAP);
	return (count != null ? count.intValue() : -1);
    }

    private String getNumLdapUsersMsg(int numLdapUsers) {
	String[] args = new String[1];
	args[0] = String.valueOf(numLdapUsers);
	return messageService.getMessage("msg.num.ldap.users", args);
    }

    private String getNumSearchResultsUsersMsg(int searchResults) {
	String[] args = new String[1];
	args[0] = String.valueOf(searchResults);
	return messageService.getMessage("msg.num.search.results.users", args);
    }

    private String getNumCreatedUsersMsg(int created) {
	String[] args = new String[1];
	args[0] = String.valueOf(created);
	return messageService.getMessage("msg.num.created.users", args);
    }

    private String getNumUpdatedUsersMsg(int updated) {
	String[] args = new String[1];
	args[0] = String.valueOf(updated);
	return messageService.getMessage("msg.num.updated.users", args);
    }

    private String getNumDisabledUsersMsg(int disabled) {
	String[] args = new String[1];
	args[0] = String.valueOf(disabled);
	return messageService.getMessage("msg.num.disabled.users", args);
    }

    private class LdapSyncThread implements Runnable {
	private String sessionId;

	private Logger log = Logger.getLogger(LdapSyncThread.class);

	public LdapSyncThread(String sessionId) {
	    this.sessionId = sessionId;
	}

	@Override
	public void run() {
	    this.log.info("=== Beginning LDAP user sync ===");
	    long start = System.currentTimeMillis();
	    try {
		BulkUpdateResultDTO dto = ldapService.bulkUpdate();
		long end = System.currentTimeMillis();
		this.log.info("=== Finished LDAP user sync ===");
		this.log.info("Bulk update took " + (end - start) / 1000 + " seconds.");
		SessionManager.getSession(sessionId).setAttribute(ILdapService.SYNC_RESULTS, dto);
	    } catch (Exception e) {
		String message = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
		SessionManager.getSession(sessionId).setAttribute(ILdapService.SYNC_RESULTS, message);
		e.printStackTrace();
	    }
	}
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}
