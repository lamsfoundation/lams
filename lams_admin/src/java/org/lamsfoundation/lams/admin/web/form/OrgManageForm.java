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
package org.lamsfoundation.lams.admin.web.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.admin.web.OrgManageBean;

/**
 * @version
 *
 *          <p>
 *          <a href="OrgManageActionForm.java.html"><i>View Source</i></a>
 *          </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 *         Created at 20:56:24 on 2006-6-5
 *
 * @struts.form name="OrgManageForm"
 */

public class OrgManageForm extends ActionForm {

    private static final long serialVersionUID = -3960695533993640297L;

    private List<OrgManageBean> orgManageBeans;

    private Integer type;

    private Integer parentId;

    private String parentName;

    private Integer stateId;

    public Integer getStateId() {
	return stateId;
    }

    public void setStateId(Integer stateId) {
	this.stateId = stateId;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public List<OrgManageBean> getOrgManageBeans() {
	return orgManageBeans;
    }

    public void setOrgManageBeans(List<OrgManageBean> orgManageBeans) {
	this.orgManageBeans = orgManageBeans;
    }

    public Integer getParentId() {
	return parentId;
    }

    public void setParentId(Integer parentId) {
	this.parentId = parentId;
    }

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }
}
