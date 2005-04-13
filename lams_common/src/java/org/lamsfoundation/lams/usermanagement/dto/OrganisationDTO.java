/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dto;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
