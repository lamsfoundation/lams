package org.lamsfoundation.lams.tool.mdscrm.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.tool.mdscrm.service.IMdlScormService;
import org.lamsfoundation.lams.tool.mdscrm.service.MdlScormServiceProxy;
import org.lamsfoundation.lams.tool.mdscrm.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 * 
 * @struts.action path="/mdscrm10admin" parameter="dispatch" scope="request"
 *                name="mdscrm10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IMdlScormService mdlScormService;
    public ILamsToolService toolService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlScormService
	if (mdlScormService == null) {
	    mdlScormService = MdlScormServiceProxy.getMdlScormService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	// Get a list of possible external servers to enable this tool for
	List<ExtServerOrgMap> serverList = mdlScormService.getExtServerList();
	List<ExtServerToolAdapterMap> mappedServers = mdlScormService.getMappedServers();
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

	// Save all the mappings
	mdlScormService.saveServerMappings(adminForm.getMappedServers());

	// Get a list of possible external servers to enable this tool for
	List<ExtServerOrgMap> serverList = mdlScormService.getExtServerList();
	List<ExtServerToolAdapterMap> mappedServers = mdlScormService.getMappedServers();
	populateServerList(adminForm, serverList, mappedServers);

	// set up mdlScormService
	if (mdlScormService == null) {
	    mdlScormService = MdlScormServiceProxy.getMdlScormService(this.getServlet().getServletContext());
	}

	return mapping.findForward("config");

    }
}
