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

/* $Id$ */
package org.lamsfoundation.lams.admin.web.dto;

/**
 * @author jliew
 *
 */
public class V1OrganisationDTO {

	private String sid;
	private String name;
	private String description;
	private String account_organisation;
	
	public V1OrganisationDTO() {
	}
	
	public V1OrganisationDTO(String sid, String name, String description, String account_organisation) {
		this.sid = sid;
		this.name = name;
		this.description = description;
		this.account_organisation = account_organisation;
	}
	
	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAccountOrganisation() {
		return account_organisation;
	}
	
	public void setAccountOrganisation(String account_organisation) {
		this.account_organisation = account_organisation;
	}
}
