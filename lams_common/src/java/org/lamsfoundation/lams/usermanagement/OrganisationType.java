package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="lams_organisation_type"
 *     
*/
public class OrganisationType implements Serializable {
	
	public static final String ROOT = "ROOT ORGANISATION";
	
	public static final String BASE = "BASE ORGANISATION";
	
	public static final String SUB = "SUB-ORGANISATION";

    /** identifier field */
    private Integer organisationTypeId;

    /** persistent field */
    private String name;

    /** persistent field */
    private String description;

    /** persistent field */
    private Set organisations;

    /** full constructor */
    public OrganisationType(String name, String description, Set organisations) {
    	this.name = name;
        this.description = description;
        this.organisations = organisations;
    }

    /** default constructor */
    public OrganisationType() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="increment"
     *             type="java.lang.Integer"
     *             column="organisation_type_id"
     *         
     */
    public Integer getOrganisationTypeId() {
        return this.organisationTypeId;
    }

    public void setOrganisationTypeId(Integer organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             unique="true"
     *             length="64"
     *             not-null="true"
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
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="organisation_type_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.Organisation"
     *         
     */
    public Set getOrganisations() {
        return this.organisations;
    }

    public void setOrganisations(Set organisations) {
        this.organisations = organisations;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("organisationTypeId", getOrganisationTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof OrganisationType) ) return false;
        OrganisationType castOther = (OrganisationType) other;
        return new EqualsBuilder()
            .append(this.getOrganisationTypeId(), castOther.getOrganisationTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrganisationTypeId())
            .toHashCode();
    }

}
