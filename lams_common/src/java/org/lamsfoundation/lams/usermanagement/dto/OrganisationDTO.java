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
/* $Id$ */
package org.lamsfoundation.lams.usermanagement.dto;

import java.util.Collection;
import java.util.TreeMap;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class OrganisationDTO {

	private Integer organisationID;
	private TreeMap<Integer,OrganisationDTO> childOrganisations;
	private String name;
	private String description;
	private Integer parentID;
	
	public OrganisationDTO(){
		
	}	
	public OrganisationDTO(Integer organisationID, Integer parentID, String description, String name) {
		super();
		this.organisationID = organisationID;
		this.name = name;
		this.description = description;
		this.childOrganisations = new TreeMap<Integer,OrganisationDTO>();
		this.parentID = parentID;
	}
	
	public OrganisationDTO(Organisation organisation){
		this.organisationID = organisation.getOrganisationId();
		this.name = organisation.getName();
		this.description = organisation.getDescription();
		this.childOrganisations = new TreeMap<Integer,OrganisationDTO>();
		if ( organisation.getParentOrganisation() != null )
			this.parentID = organisation.getParentOrganisation().getOrganisationId();
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

	/** Gets all the child organisations. This is a recursive structure */
	public Collection<OrganisationDTO> getChildOrganisations() {
		return childOrganisations.values();
	}
	
	/** Add a child organisation. Adds it into a sorted map so that the values always come out 
	 * in the same order. */
	public void addChildOrganisation(OrganisationDTO orgDTO) {
		childOrganisations.put(orgDTO.getOrganisationID(),orgDTO);
	}
	public Integer getParentID() {
		return parentID;
	}
	
}
