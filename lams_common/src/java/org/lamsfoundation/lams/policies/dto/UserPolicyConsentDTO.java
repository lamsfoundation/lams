package org.lamsfoundation.lams.policies.dto;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.dto.UserBasicDTO;

/**
 * Used for displaying which users consented to the policy.
 * 
 * @author Andrey Balan
 */
public class UserPolicyConsentDTO extends UserBasicDTO {
    private boolean isConsentGivenByUser;
    
    private Date dateAgreedOn;

    public UserPolicyConsentDTO(Integer userID, String firstName, String lastName, String login) {
	super(userID, firstName, lastName, login);
    }
    
    public void setConsentGivenByUser(boolean isConsentGivenByUser) {
	this.isConsentGivenByUser = isConsentGivenByUser;
    }

    public boolean isConsentGivenByUser() {
	return isConsentGivenByUser;
    }
    
    public Date getDateAgreedOn() {
	return dateAgreedOn;
    }

    public void setDateAgreedOn(Date dateAgreedOn) {
	this.dateAgreedOn = dateAgreedOn;
    }

}
