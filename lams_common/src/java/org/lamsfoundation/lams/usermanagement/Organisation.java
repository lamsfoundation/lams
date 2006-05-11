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
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;

/** 
 *        @hibernate.class
 *         table="lams_organisation"
 *     
*/
public class Organisation implements Serializable {

	
    /** identifier field */
    private Integer organisationId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Organisation parentOrganisation;

    /** persistent field */
    private Date createDate;

    /** persistent field */
    private Workspace workspace;

    /** persistent field */
    private OrganisationType organisationType;

    /** persistent field */
    private Set userOrganisations;

    /** persistent field */
    private Set childOrganisations;
    
    /** persistent field */
    private Set lessons;
    
    /** full constructor */
    public Organisation(String name, String description, Organisation parentOrganisation, Date createDate, Workspace workspace, OrganisationType organisationType, Set userOrganisations, Set childOrganisations, Set lessons) {
        this.name = name;
        this.description = description;
        this.parentOrganisation = parentOrganisation;
        this.createDate = createDate;
        this.workspace = workspace;
        this.organisationType = organisationType;
        this.userOrganisations = userOrganisations;
        this.childOrganisations = childOrganisations;
        this.lessons = lessons;
    }

    /** default constructor */
    public Organisation() {
    }

    /** minimal constructor */
    public Organisation(Date createDate, Workspace workspace, OrganisationType organisationType, Set userOrganisations, Set lessons) {
        this.createDate = createDate;
        this.workspace = workspace;
        this.organisationType = organisationType;
        this.userOrganisations = userOrganisations;
        this.lessons = lessons;
    }
    public Organisation(String name, 
    					String description,						
						Date createDate,						 
						OrganisationType organisationType) {
		super();
		this.name = name;
		this.description = description;		
		this.createDate = createDate;		
		this.organisationType = organisationType;
	}

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="organisation_id"
     *         
     */
    public Integer getOrganisationId() {
        return this.organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
        this.organisationId = organisationId;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="250"
     *         
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="250"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.many-to-one
     *            @hibernate.column name="parent_organisation_id"         
     *         
     */
    public Organisation getParentOrganisation() {
        return this.parentOrganisation;
    }

    public void setParentOrganisation(Organisation parentOrganisation) {
        this.parentOrganisation = parentOrganisation;
    }

    /** 
     *            @hibernate.property
     *             column="create_date"
     *             length="19"
     *             not-null="true"
     *         
     */
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="workspace_id"         
     *         
     */
    public Workspace getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="organisation_type_id"         
     *         
     */
    public OrganisationType getOrganisationType() {
        return this.organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {
        this.organisationType = organisationType;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="organisation_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.UserOrganisation"
     *         
     */
    public Set getUserOrganisations() {
        return this.userOrganisations;
    }

    public void setUserOrganisations(Set userOrganisations) {
        this.userOrganisations = userOrganisations;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="parent_organisation_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.Organisation"
     *         
     */
	public Set getChildOrganisations() {
		return childOrganisations;
	}

	public void setChildOrganisations(Set childOrganisations) {
		this.childOrganisations = childOrganisations;
	}
	
    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="organisation_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.lesson.Lesson"
     *         
     */
    public Set getLessons() {
        return this.lessons;
    }

    public void setLessons(Set lessons) {
        this.lessons = lessons;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("organisationId", getOrganisationId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Organisation) ) return false;
        Organisation castOther = (Organisation) other;
        return new EqualsBuilder()
            .append(this.getOrganisationId(), castOther.getOrganisationId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrganisationId())
            .toHashCode();
    }
    public OrganisationDTO getOrganisationDTO(){
    	return new OrganisationDTO(this);
    }

}
