/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement.dto;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class OrganisationDTO {

	private Integer organisationID;
	private String name;
	private String description;
	
	public OrganisationDTO(){
		
	}	
	public OrganisationDTO(Integer organisationID, String name,
			String description) {
		super();
		this.organisationID = organisationID;
		this.name = name;
		this.description = description;
	}
	public OrganisationDTO(Organisation organisation){
		this.organisationID = organisation.getOrganisationId();
		this.name = organisation.getName();
		this.description = organisation.getDescription();
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name!=null?name:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the organisationID.
	 */
	public Integer getOrganisationID() {
		return organisationID!=null?organisationID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
}
