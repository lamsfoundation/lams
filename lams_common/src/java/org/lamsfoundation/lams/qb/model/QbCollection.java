package org.lamsfoundation.lams.qb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * A collection of Question Bank questions bound to an user.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_collection")
public class QbCollection implements Serializable {
    private static final long serialVersionUID = 8481910298794355970L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String name;

    // if it is true, then it is a private, one user only collection
    @Column
    private boolean personal = false;

    // if it is null, then this is a public collection
    @Column(name = "user_id")
    private Integer userId;

    @ManyToMany
    @JoinTable(name = "lams_qb_collection_organisation", joinColumns = @JoinColumn(name = "collection_uid"), inverseJoinColumns = @JoinColumn(name = "organisation_id"))
    @OrderBy("name")
    private List<Organisation> organisations = new ArrayList<>();

    public Long getUid() {
	return uid;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public boolean isPersonal() {
	return personal;
    }

    public void setPersonal(boolean personal) {
	this.personal = personal;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public List<Organisation> getOrganisations() {
	return organisations;
    }

    public void setOrganisations(List<Organisation> organisations) {
	this.organisations = organisations;
    }

    @Override
    public boolean equals(Object o) {
	QbCollection other = (QbCollection) o;
	return new EqualsBuilder().append(this.uid, other.uid).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(this.uid).toHashCode();
    }
}