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


package org.lamsfoundation.lams.tool.bbb.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.lamsfoundation.lams.tool.bbb.dto.ConfigDTO;
import org.lamsfoundation.lams.tool.bbb.model.BbbConfig;
import org.lamsfoundation.lams.tool.bbb.service.BbbServiceProxy;
import org.lamsfoundation.lams.tool.bbb.service.IBbbService;
import org.lamsfoundation.lams.tool.bbb.util.Constants;
import org.lamsfoundation.lams.tool.bbb.web.forms.AdminForm;

/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * @author Ernie Ghiglione
 *
 */
public class AdminAction extends MappingDispatchAction {

    private IBbbService bbbService;

    // private static final Logger logger = Logger.getLogger(AdminAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up bbbService
	bbbService = BbbServiceProxy.getBbbService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ConfigDTO configDTO = new ConfigDTO();

	configDTO.setServerURL(bbbService.getConfigValue(Constants.CFG_SERVER_URL));
	configDTO.setSecuritySalt(bbbService.getConfigValue(Constants.CFG_SECURITYSALT));

	request.setAttribute(Constants.ATTR_CONFIG_DTO, configDTO);
	return mapping.findForward("view-success");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AdminForm adminForm = (AdminForm) form;

	adminForm.setServerURL(bbbService.getConfigValue(Constants.CFG_SERVER_URL));
	adminForm.setSecuritySalt(bbbService.getConfigValue(Constants.CFG_SECURITYSALT));

	return mapping.findForward("edit-success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (!isCancelled(request)) {

	    AdminForm adminForm = (AdminForm) form;

	    String bbbServerURL = adminForm.getServerURL().trim();

	    if (!bbbServerURL.endsWith("/")) {
		bbbServerURL = bbbServerURL + "/";
	    }

	    updateConfig(Constants.CFG_SECURITYSALT, adminForm.getSecuritySalt());
	    updateConfig(Constants.CFG_SERVER_URL, bbbServerURL);

	}

	return mapping.findForward("save-success");
    }

    private void updateConfig(String key, String value) {

	BbbConfig config = bbbService.getConfig(key);

	if (config == null) {
	    config = new BbbConfig(key, value);
	} else {
	    config.setValue(value);
	}

	bbbService.saveOrUpdateConfigEntry(config);
    }

}
