package org.lamsfoundation.lams.policies;

import java.util.Date;

public class PolicyDTO {
    private Long uid;
    
    private String policyName;
    
    private String version;
    
    private Integer policyTypeId;
    
    private String summary;
    
    private String fullPolicy;
    
    private boolean isConsentedByUser;
    
    private Date dateAgreedOn;
    
    /** default constructor */
    public PolicyDTO(Policy policy) {
	this.uid = policy.getUid();
	this.policyName = policy.getPolicyName();
	this.version = policy.getVersion();
	this.policyTypeId= policy.getPolicyTypeId();
	this.summary = policy.getSummary();
	this.fullPolicy= policy.getFullPolicy();
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
    
    public Integer getPolicyTypeId() {
	return this.policyTypeId;
    }

    public void setPolicyTypeId(Integer policyTypeId) {
	this.policyTypeId = policyTypeId;
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
    
    public void setConsentedByUser(boolean isConsentedByUser) {
	this.isConsentedByUser = isConsentedByUser;
    }

    public boolean isConsentedByUser() {
	return isConsentedByUser;
    }
    
    public Date getDateAgreedOn() {
	return dateAgreedOn;
    }

    public void setDateAgreedOn(Date dateAgreedOn) {
	this.dateAgreedOn = dateAgreedOn;
    }

}
