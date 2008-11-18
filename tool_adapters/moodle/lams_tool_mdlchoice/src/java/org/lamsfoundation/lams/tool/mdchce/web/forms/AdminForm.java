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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.tool.mdchce.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.form name="mdchce10AdminForm"
 */
public class AdminForm extends ActionForm {
    private static final long serialVersionUID = 8872637862875198L;

    String toolAdapterServlet;
    String extServerUrl;
    String serverIdMapping;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));
	return ac;
    }

    public String getToolAdapterServlet() {
	return toolAdapterServlet;
    }

    public void setToolAdapterServlet(String toolAdapterServlet) {
	this.toolAdapterServlet = toolAdapterServlet;
    }

    public String getServerIdMapping() {
	return serverIdMapping;
    }

    public void setServerIdMapping(String serverIdMapping) {
	this.serverIdMapping = serverIdMapping;
    }

    public String getExtServerUrl() {
	return extServerUrl;
    }

    public void setExtServerUrl(String extServerUrl) {
	this.extServerUrl = extServerUrl;
    }

    public static long getSerialVersionUID() {
	return serialVersionUID;
    }
}
