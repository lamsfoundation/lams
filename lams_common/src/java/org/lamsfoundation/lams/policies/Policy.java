package org.lamsfoundation.lams.policies;

import java.util.Date;
import java.util.Set;

import org.lamsfoundation.lams.usermanagement.User;

public class Policy {
    
    /** active policy */
    public static final Integer STATUS_ACTIVE = new Integer(1);
    /** inactive policy */
    public static final Integer STATUS_INACTIVE = new Integer(2);
    /** The state for draft policy */
    public static final Integer STATUS_DRAFT = new Integer(3);
    
    /** active policy */
    public static final Integer TYPE_SITE_POLICY = new Integer(1);
    /** inactive policy */
    public static final Integer TYPE_PRIVACY_POLICY = new Integer(2);
    /** The state for draft policy */
    public static final Integer TYPE_THIRD_PARTIES_POLICY = new Integer(3);
    /** The state for draft policy */
    public static final Integer TYPE_OTHER = new Integer(4);
    
    /** identifier field */
    private Long uid;
    
    /**
     * Shared by all policies created out of one parent one. 
     */
    private Long policyId;
    
    /** persistent field */
    private String policyName;
    
    /** persistent field */
    private String version;
    
    /** persistent field */
    private String summary;
    
    /** persistent field */
    private String fullPolicy;
    
    /** persistent field */
    private Integer policyStateId;
    
    /** persistent field */
    private Integer policyTypeId;
    
    private User createdBy;
    
    /** Date this policy was modified the last time */
    private Date lastModified;
    
    private Set<PolicyConsent> consents;
    
    // *************** NON Persistent Fields ********************
    
    private boolean hasPreviousVersions;
    
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

}
