package org.lamsfoundation.lams.usermanagement.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Fei Yang
 * 
 * @struts:form name="OrganisationActionForm"
 * 		include-pk="true"
 * 		include-all="true"
 */
public class OrganisationActionForm extends ActionForm {

	private static Logger log = Logger.getLogger(OrganisationActionForm.class);
	public static final String formName = "OrganisationActionForm"; // must match name in @struts:action section above

	private Integer orgId; 	
	private String name;
	private String description;
	private Integer parentOrgId; // display only - don't update
	private String parentOrgName; //display only - don't update
	
	public OrganisationActionForm()
	{
		orgId = new Integer(-1);
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
	 * Sets the sid.
	 * @param sid The sid to set
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
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
		setOrgId(new Integer(-1)); 	
		setName(null);
		setDescription(null);
    }

   /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     * <p>
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
       ActionErrors errors = super.validate(mapping, request);

       if (errors==null) errors = new ActionErrors();

		if ( getName() == null || getName().length()==0)
		{
			errors.add("name",	new ActionMessage("error.name.required"));
		}
		if ( getDescription() == null || getDescription().length()==0)
		{
			errors.add("description",	new ActionMessage("error.description.required"));
		}
       if (errors.isEmpty()) return null;
       else return errors;

    }
    
	/**
	 * Returns the parentOrgName.
	 * @return String
	 */
	public String getParentOrgName() {
		return parentOrgName;
	}

	/**
	 * Returns the parentOrgOrgId.
	 * @return Integer
	 */
	public Integer getParentOrgId() {
		return parentOrgId;
	}

	/**
	 * Sets the parentOrgName.
	 * @param parentOrgName The parentOrgName to set
	 */
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	/**
	 * Sets the parentOrgId.
	 * @param parentOrgId The parentOrgId to set
	 */
	public void setParentOrgId(Integer parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

}
