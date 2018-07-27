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


package org.lamsfoundation.lams.admin.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.dto.BulkUpdateResultDTO;
import org.lamsfoundation.lams.usermanagement.service.ILdapService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;

/**
 * @author jliew
 *
 *
 *
 *
 *
 */
public class LdapConfigAction extends Action {

    private static Logger log = Logger.getLogger(LdapConfigAction.class);
    private static IUserManagementService service;
    private static LdapService ldapService;
    private static MessageService messageService;
    private static Configuration configurationService;

    private IUserManagementService getService() {
	if (service == null) {
	    service = AdminServiceProxy.getService(getServlet().getServletContext());
	}
	return service;
    }

    private LdapService getLdapService() {
	if (ldapService == null) {
	    ldapService = AdminServiceProxy.getLdapService(getServlet().getServletContext());
	}
	return ldapService;
    }

    private MessageService getMessageService() {
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}
	return messageService;
    }

    private Configuration getConfiguration() {
	if (configurationService == null) {
	    configurationService = AdminServiceProxy.getConfiguration(getServlet().getServletContext());
	}
	return configurationService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String action = WebUtil.readStrParam(request, "action", true);
	if (action != null) {
	    if (StringUtils.equals(action, "sync")) {
		return sync(mapping, form, request, response);
	    }
	    if (StringUtils.equals(action, "waiting")) {
		return waiting(mapping, form, request, response);
	    }
	    if (StringUtils.equals(action, "results")) {
		return results(mapping, form, request, response);
	    }
	}

	request.setAttribute("config", getConfiguration().arrangeItems(Configuration.ITEMS_ONLY_LDAP));

	int numLdapUsers = getNumLdapUsers();
	request.setAttribute("numLdapUsersMsg", getNumLdapUsersMsg(numLdapUsers));

	return mapping.findForward("ldap");
    }

    public ActionForward sync(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String sessionId = SessionManager.getSession().getId();
	Thread t = new Thread(new LdapSyncThread(sessionId));
	t.start();

	request.setAttribute("wait", getMessageService().getMessage("msg.ldap.synchronise.wait"));

	return mapping.findForward("ldap");
    }

    public ActionForward waiting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("wait", getMessageService().getMessage("msg.ldap.synchronise.wait"));

	return mapping.findForward("ldap");
    }

    public ActionForward results(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	HttpSession ss = SessionManager.getSession();
	Object o = ss.getAttribute(ILdapService.SYNC_RESULTS);
	if (o instanceof BulkUpdateResultDTO) {
	    BulkUpdateResultDTO dto = (BulkUpdateResultDTO) o;

	    int numLdapUsers = getNumLdapUsers();
	    request.setAttribute("numLdapUsersMsg", getNumLdapUsersMsg(numLdapUsers));

	    request.setAttribute("numSearchResults", getNumSearchResultsUsersMsg(dto.getNumSearchResults()));
	    request.setAttribute("numLdapUsersCreated", getNumCreatedUsersMsg(dto.getNumUsersCreated()));
	    request.setAttribute("numLdapUsersUpdated", getNumUpdatedUsersMsg(dto.getNumUsersUpdated()));
	    request.setAttribute("numLdapUsersDisabled", getNumDisabledUsersMsg(dto.getNumUsersDisabled()));
	    request.setAttribute("messages", dto.getMessages());
	    request.setAttribute("done", getMessageService().getMessage("msg.done"));
	} else {
	    ArrayList<String> list = new ArrayList<String>();
	    list.add((String) o);
	    request.setAttribute("messages", list);
	    request.setAttribute("done", getMessageService().getMessage("msg.done"));
	}

	// remove session variable that flags bulk update as done
	ss.removeAttribute(ILdapService.SYNC_RESULTS);

	return mapping.findForward("ldap");
    }

    private int getNumLdapUsers() {
	Integer count = getService().getCountUsers(AuthenticationMethod.LDAP);
	return (count != null ? count.intValue() : -1);
    }

    private String getNumLdapUsersMsg(int numLdapUsers) {
	String[] args = new String[1];
	args[0] = String.valueOf(numLdapUsers);
	return getMessageService().getMessage("msg.num.ldap.users", args);
    }

    private String getNumSearchResultsUsersMsg(int searchResults) {
	String[] args = new String[1];
	args[0] = String.valueOf(searchResults);
	return getMessageService().getMessage("msg.num.search.results.users", args);
    }

    private String getNumCreatedUsersMsg(int created) {
	String[] args = new String[1];
	args[0] = String.valueOf(created);
	return getMessageService().getMessage("msg.num.created.users", args);
    }

    private String getNumUpdatedUsersMsg(int updated) {
	String[] args = new String[1];
	args[0] = String.valueOf(updated);
	return getMessageService().getMessage("msg.num.updated.users", args);
    }

    private String getNumDisabledUsersMsg(int disabled) {
	String[] args = new String[1];
	args[0] = String.valueOf(disabled);
	return getMessageService().getMessage("msg.num.disabled.users", args);
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
		BulkUpdateResultDTO dto = getLdapService().bulkUpdate();
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
}
