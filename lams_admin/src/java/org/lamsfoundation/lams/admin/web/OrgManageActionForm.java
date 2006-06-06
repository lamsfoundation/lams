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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.admin.web;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;

/**
 * @version
 *
 * <p>
 * <a href="OrgManageActionForm.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 20:56:24 on 2006-6-5
 */
public class OrgManageActionForm extends ActionForm {

	private static final long serialVersionUID = -3960695533993640297L;

	static class OrgManageBean {
	    private Integer organisationId;
	    private String name;
	    private String code;
	    private String description;
	    private Date createDate;
	    private boolean editable;
	    private String status;
		private String localeLanguage;
		private String localeCountry;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getLocaleCountry() {
			return localeCountry;
		}
		public void setLocaleCountry(String localeCountry) {
			this.localeCountry = localeCountry;
		}
		public String getLocaleLanguage() {
			return localeLanguage;
		}
		public void setLocaleLanguage(String localeLanguage) {
			this.localeLanguage = localeLanguage;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getOrganisationId() {
			return organisationId;
		}
		public void setOrganisationId(Integer organisationId) {
			this.organisationId = organisationId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public boolean isEditable() {
			return editable;
		}
		public void setEditable(boolean editable) {
			this.editable = editable;
		}
	}
	
	private List<OrgManageBean> orgManageBeans;
	
	private Integer type;
	
	private Organisation parentOrganisation;

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

	public Organisation getParentOrganisation() {
		return parentOrganisation;
	}

	public void setParentOrganisation(Organisation parentOrganisation) {
		this.parentOrganisation = parentOrganisation;
	}
}
