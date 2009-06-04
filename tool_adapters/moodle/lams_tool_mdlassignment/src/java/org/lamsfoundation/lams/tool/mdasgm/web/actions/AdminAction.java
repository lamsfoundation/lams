package org.lamsfoundation.lams.tool.mdasgm.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.tool.mdasgm.service.IMdlAssignmentService;
import org.lamsfoundation.lams.tool.mdasgm.service.MdlAssignmentServiceProxy;
import org.lamsfoundation.lams.tool.mdasgm.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 * 
 * @struts.action path="/mdasgm10admin" parameter="dispatch" scope="request"
 *                name="mdasgm10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IMdlAssignmentService mdlAssignmentService;
    public ILamsToolService toolService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlAssignmentService
	if (mdlAssignmentService == null) {
	    mdlAssignmentService = MdlAssignmentServiceProxy.getMdlAssignmentService(this.getServlet()
		    .getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	if (toolService == null) {
	    toolService = mdlAssignmentService.getToolService();
	}

	// Get a list of possible external servers to enable this tool for
	List<ExtServerOrgMap> serverList = mdlAssignmentService.getExtServerList();
	List<ExtServerToolAdapterMap> mappedServers = mdlAssignmentService.getMappedServers();
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

	// set up mdlAssignmentService
	if (mdlAssignmentService == null) {
	    mdlAssignmentService = MdlAssignmentServiceProxy.getMdlAssignmentService(this.getServlet()
		    .getServletContext());
	}
	
	// Save all the mappings
	mdlAssignmentService.saveServerMappings(adminForm.getMappedServers());

	// Get a list of possible external servers to enable this tool for
	List<ExtServerOrgMap> serverList = mdlAssignmentService.getExtServerList();
	List<ExtServerToolAdapterMap> mappedServers = mdlAssignmentService.getMappedServers();
	populateServerList(adminForm, serverList, mappedServers);

	return mapping.findForward("config");

    }
}
