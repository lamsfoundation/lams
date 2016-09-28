/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.admin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * <p>
 * <a href="ServerMaintainAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ServerMaintainAction extends LamsDispatchAction {

    @SuppressWarnings("unchecked")
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm extServerForm = (DynaActionForm) form;
	Integer sid = WebUtil.readIntParam(request, "sid", true);
	if (sid != null) {
	    ExtServer map = AdminServiceProxy.getIntegrationService(getServlet().getServletContext())
		    .getExtServer(sid);
	    BeanUtils.copyProperties(extServerForm, map);
	}
	return mapping.findForward("servermaintain");
    }

    public ActionForward disable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IIntegrationService service = AdminServiceProxy.getIntegrationService(getServlet().getServletContext());
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = service.getExtServer(sid);
	map.setDisabled(true);
	service.saveExtServer(map);
	return mapping.findForward("serverlist");
    }

    public ActionForward enable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IIntegrationService service = AdminServiceProxy.getIntegrationService(getServlet().getServletContext());
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = service.getExtServer(sid);
	map.setDisabled(false);
	service.saveExtServer(map);
	return mapping.findForward("serverlist");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	AdminServiceProxy.getService(getServlet().getServletContext()).deleteById(ExtServer.class, sid);
	return mapping.findForward("serverlist");
    }

}
