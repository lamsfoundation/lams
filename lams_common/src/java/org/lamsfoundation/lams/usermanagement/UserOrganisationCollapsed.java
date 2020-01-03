package org.lamsfoundation.lams.usermanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Class holds boolean indicating whether according subcourse was collapsed by user.
 * 
 * @author Andrey Balan
 */
 @Entity
 @Table(name = "lams_user_organisation_collapsed")
public class UserOrganisationCollapsed {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;
    
    @Column
    private boolean collapsed = false;

    public Integer getUid() {
	return this.uid;
    }

    public void setUid(Integer uid) {
	this.uid = uid;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Organisation getOrganisation() {
	return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public boolean getCollapsed() {
	return this.collapsed;
    }

    public void setCollapsed(boolean collapsed) {
	this.collapsed = collapsed;
    }
}
