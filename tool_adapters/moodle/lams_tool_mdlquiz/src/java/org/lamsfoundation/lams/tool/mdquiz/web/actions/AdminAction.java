/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mdquiz.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.tool.mdquiz.service.IMdlQuizService;
import org.lamsfoundation.lams.tool.mdquiz.service.MdlQuizServiceProxy;
import org.lamsfoundation.lams.tool.mdquiz.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 * 
 * @struts.action path="/mdquiz10admin" parameter="dispatch" scope="request"
 *                name="mdquiz10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IMdlQuizService mdlQuizService;
    public ILamsToolService toolService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlQuizService
	if (mdlQuizService == null) {
	    mdlQuizService = MdlQuizServiceProxy.getMdlQuizService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	// Get a list of possible external servers to enable this tool for
	List<ExtServerOrgMap> serverList = mdlQuizService.getExtServerList();
	List<ExtServerToolAdapterMap> mappedServers = mdlQuizService.getMappedServers();
	populateServerList(adminForm, serverList, mappedServers);

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public void populateServerList(AdminForm adminForm, List<ExtServerOrgMap> serverList,
	    List<ExtServerToolAdapterMap> mappedServers) {
	ArrayList<String> mappableServers = new ArrayList<String>();
	ArrayList<String> mappedServersInForm = new ArrayList<String>();

	if (serverList != null) {
	    for (ExtServerOrgMap serverMap : serverList) {
		mappableServers.add(serverMap.getServerid().toString());
		if (mappedServers != null) {
		    for (ExtServerToolAdapterMap mappedServer : mappedServers) {
			if (mappedServer.getExtServer().getServerid().equals(serverMap.getServerid())) {
			    mappedServersInForm.add(serverMap.getServerid().toString());
			}
		    }
		}
	    }
	}
	adminForm.setMappableServers((String[]) mappableServers.toArray(new String[mappableServers.size()]));
	adminForm.setMappedServers((String[]) mappedServersInForm.toArray(new String[mappedServersInForm.size()]));
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	// set up mdlQuizService
	if (mdlQuizService == null) {
	    mdlQuizService = MdlQuizServiceProxy.getMdlQuizService(this.getServlet().getServletContext());
	}
	// Save all the mappings
	mdlQuizService.saveServerMappings(adminForm.getMappedServers());

	// Get a list of possible external servers to enable this tool for
	List<ExtServerOrgMap> serverList = mdlQuizService.getExtServerList();
	List<ExtServerToolAdapterMap> mappedServers = mdlQuizService.getMappedServers();
	populateServerList(adminForm, serverList, mappedServers);

	return mapping.findForward("config");

    }
}
