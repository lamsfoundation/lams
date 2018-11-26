package org.lamsfoundation.lams.policies;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_policy")
public class Policy {

    /** active policy */
    public static final Integer STATUS_ACTIVE = new Integer(1);
    /** inactive policy */
    public static final Integer STATUS_INACTIVE = new Integer(2);

    /** site policy */
    public static final Integer TYPE_SITE_POLICY = new Integer(1);
    /** privacy policy */
    public static final Integer TYPE_PRIVACY_POLICY = new Integer(2);
    /** third party policy */
    public static final Integer TYPE_THIRD_PARTIES_POLICY = new Integer(3);
    /** other policy */
    public static final Integer TYPE_OTHER = new Integer(4);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    /**
     * Shared by all policies created out of one parent one.
     */
    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "policy_name")
    private String policyName;

    @Column
    private String version;

    @Column
    private String summary;

    @Column(name = "full_policy")
    private String fullPolicy;

    @Column(name = "policy_state_id")
    private Integer policyStateId;

    @Column(name = "policy_type_id")
    private Integer policyTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    /** Date this policy was modified the last time */
    @Column(name = "last_modified")
    private Date lastModified;

    // inverse = false
    @OneToMany(mappedBy = "policy")
    @OrderBy("dateAgreedOn ASC")
    private Set<PolicyConsent> consents;

    // *************** NON Persistent Fields ********************

    @Transient
    private boolean hasPreviousVersions;

    @Transient
    private int userConsentsCount;

    /** default constructor */
    public Policy() {
	lastModified = new Date(); // default value is set to when the object is created
    }

    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------
    public Long getUid() {
	return uid;
    }

    public void setUid(Long id) {
	this.uid = id;
    }

    public Long getPolicyId() {
	return policyId;
    }

    public void setPolicyId(Long policyId) {
	this.policyId = policyId;
    }

    public Integer getPolicyStateId() {
	return this.policyStateId;
    }

    public void setPolicyStateId(Integer policyStateId) {
	this.policyStateId = policyStateId;
    }

    public Integer getPolicyTypeId() {
	return this.policyTypeId;
    }

    public void setPolicyTypeId(Integer policyTypeId) {
	this.policyTypeId = policyTypeId;
    }

    public String getPolicyName() {
	return policyName;
    }

    public void setPolicyName(String policyName) {
	this.policyName = policyName;
    }

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    public String getFullPolicy() {
	return fullPolicy;
    }

    public void setFullPolicy(String fullPolicy) {
	this.fullPolicy = fullPolicy;
    }

    public User getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(User createdBy) {
	this.createdBy = createdBy;
    }

    public Date getLastModified() {
	return lastModified;
    }

    /* If lastModified is null, then it will default to the current date/time */
    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified != null ? lastModified : new Date();
    }

    public Set<PolicyConsent> getConsents() {
	return this.consents;
    }

    public void setConsents(Set<PolicyConsent> consents) {
	this.consents = consents;
    }

    public void setPreviousVersions(boolean hasPreviousVersions) {
	this.hasPreviousVersions = hasPreviousVersions;
    }

    public boolean hasPreviousVersions() {
	return hasPreviousVersions;
    }

    public void setUserConsentsCount(int userConsentsCount) {
	this.userConsentsCount = userConsentsCount;
    }

    public int getUserConsentsCount() {
	return userConsentsCount;
    }
}
