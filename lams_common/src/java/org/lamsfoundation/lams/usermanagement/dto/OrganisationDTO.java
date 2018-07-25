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

package org.lamsfoundation.lams.usermanagement.dto;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * @author Manpreet Minhas
 */
public class OrganisationDTO {

    private Integer organisationID;
    private Integer parentID;
    private String name;
    private String description;
    private Vector<String> roleNames;
    private Vector<OrganisationDTO> nodes;
    private Integer organisationTypeId;

    public OrganisationDTO() {
    }
    
    public OrganisationDTO(Integer organisationID, String name) {
	super();
	this.organisationID = organisationID;
	this.name = name;
	this.roleNames = new Vector<String>();
	this.nodes = new Vector<OrganisationDTO>();
    }

    public OrganisationDTO(Integer organisationID, Integer parentID, String name, String description,
	    Integer organisationTypeId) {
	super();
	this.organisationID = organisationID;
	this.parentID = parentID;
	this.name = name;
	this.description = description;
	this.organisationTypeId = organisationTypeId;
	this.roleNames = new Vector<String>();
	this.nodes = new Vector<OrganisationDTO>();
    }

    public OrganisationDTO(Organisation organisation) {
	this.organisationID = organisation.getOrganisationId();
	this.parentID = (organisation.getParentOrganisation() == null) ? new Integer(-1)
		: organisation.getParentOrganisation().getOrganisationId();
	this.name = organisation.getName();
	this.description = organisation.getDescription();
	this.organisationTypeId = organisation.getOrganisationType().getOrganisationTypeId();
	this.roleNames = new Vector<String>();
	this.nodes = new Vector<OrganisationDTO>();
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @return Returns the organisationID.
     */
    public Integer getOrganisationID() {
	return organisationID;
    }

    /**
     * @return Returns the parent organisationID.
     */
    public Integer getParentID() {
	return parentID;
    }

    /** Get the organisation type id - 1 for root, 2 for course and 3 for class */
    public Integer getOrganisationTypeId() {
	return organisationTypeId;
    }

    public void addNode(OrganisationDTO organisation) {
	nodes.add(organisation);
    }

    public void addNodes(Collection<OrganisationDTO> list) {
	Iterator it = list.iterator();
	while (it.hasNext()) {
	    this.addNode((OrganisationDTO) it.next());
	}
    }

    public Vector<OrganisationDTO> getNodes() {
	return nodes;
    }

    public Vector<String> getRoleNames() {
	return roleNames;
    }

    public void addRoleName(String roleName) {
	roleNames.add(roleName);
    }

    /**
     * Two OrganisationDTOs are equals if both have a valid (not null) organisationID and the organisationID's are the
     * same.
     */
    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof OrganisationDTO)) {
	    return false;
	}
	OrganisationDTO castOther = (OrganisationDTO) other;
	return (this.getOrganisationID() != null) && this.getOrganisationID().equals(castOther.getOrganisationID());
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getOrganisationID()).toHashCode();
    }

}
