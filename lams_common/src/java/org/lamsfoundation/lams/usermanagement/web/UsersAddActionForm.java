package org.lamsfoundation.lams.usermanagement.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.usermanagement.Role;


/**
 * @author Fei Yang
 * 
 * @struts:form name="UsersAddActionForm"
 * 		include-pk="true"
 * 		include-all="true"
 */
public class UsersAddActionForm extends ActionForm {

	private static Logger log = Logger.getLogger(UsersAddActionForm.class);
	public static final String formName = "UsersAddActionForm"; // must match name in @struts:action section above
	
	private Integer orgId;
	private String name; 	// don't update - for display only
	private String description; 	// don't update - for display only

	// organisation info - list of organisation objects 
	private List availableOrgs;
	
	private Integer selectedOrgId;
	
	private List potentialUsers; // list of user objects
	
	private String[] potentialRoleNames; // map of user and rolenames pair 
	
	private String[] allRoleNames = {Role.ADMIN, Role.AUTHOR, Role.STAFF, Role.LEARNER};
	
	public UsersAddActionForm()
	{
		selectedOrgId = null;
	}
	
	/**
	 * Returns the name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description
	 * @return String
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = StringUtils.trimToEmpty(name);
	}

	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = StringUtils.trimToEmpty(description);
	}

	/**
	 * Sets the orgId.
	 * @param orgId The orgId to set
	 */
	public void setOrgId(Integer orgId) {
		log.debug("Setting orgId "+orgId);
		this.orgId = orgId;
	}

	/**
	 * Returns the orgId.
	 * @return Integer
	 */
	public Integer getOrgId() {
		return orgId;
	}

	/**
	 * Returns the potentialUsers.
	 * @return List of ShwaaUserData objects
	 */
	public List getPotentialUsers() {
		return potentialUsers;
	}

	/**
	 * Returns the selectedOrgId.
	 * @return Integer
	 */
	public Integer getSelectedOrgId() {
		return selectedOrgId;
	}

	/**
	 * Sets the potentialUsers.
	 * @param potentialUsers The potentialUsers (ShwaaUserData objects) to set
	 */
	public void setPotentialUsers(List potentialUsers) {
		this.potentialUsers = potentialUsers;
	}

	/**
	 * Sets the selectedOrgId.
	 * @param selectedOrgId The selectedOrgId to set
	 */
	public void setSelectedOrgId(Integer selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}

	/**
	 * Returns the availableOrgs - array of OrganisationRecursiveData objects
	 * @return ArrayList
	 */
	public List getAvailableOrgs() {
		return availableOrgs;
	}

	/**
	 * Sets the availableOrgs - Array of OrganisationRecursiveData objects
	 * @param availableOrgs The availableOrgs to set
	 */
	public void setAvailableOrgs(List availableOrgs) {
		this.availableOrgs = availableOrgs;
	}

	/**
	 * @return Returns the allRoleNames.
	 */
	public String[] getAllRoleNames() {
		return allRoleNames;
	}
	/**
	 * @param allRoleNames The allRoleNames to set.
	 */
	public void setAllRoleNames(String[] allRoleNames) {
		this.allRoleNames = allRoleNames;
	}
	/**
	 * @return Returns the potentialRoleNames.
	 */
	public String[] getPotentialRoleNames() {
		return potentialRoleNames;
	}
	/**
	 * @param potentialRoleNames The potentialRoleNames to set.
	 */
	public void setPotentialRoleNames(String[] potentialRoleNames) {
		this.potentialRoleNames = potentialRoleNames;
	}
	
    /**
     * Reset all properties to their default values. Don't reset the ones
     * that are display only.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
		selectedOrgId = null;
		potentialRoleNames = new String[0];
    }

	/** Validate method - doesn't actually check anything yet! */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) 
    {
       ActionErrors errors = super.validate(mapping, request);
       if (errors==null || errors.isEmpty()) return null;
       else return errors;

    }
}
