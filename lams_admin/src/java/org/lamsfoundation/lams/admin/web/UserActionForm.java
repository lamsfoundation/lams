package org.lamsfoundation.lams.admin.web;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.usermanagement.Role;

/**
 * @author Fei Yang
 * 
 * @struts:form name="UserActionForm" include-pk="true" include-all="true"
 */
public class UserActionForm extends ActionForm {

	public static final String formName = "UserActionForm";

	private static Logger log = Logger.getLogger(UserActionForm.class);
	
	private Integer orgId;

	private Integer userId;

	private String login;

	private String password;

	private String passwordConfirm;

	private String title;

	private String firstName;

	private String lastName;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String city;

	private String state;

	private String country;

	private String dayPhone;

	private String evePhone;

	private String mobPhone;

	private String fax;

	private String email;

	private Boolean disabledFlag;
	
	private Date createDate;

	private String authMethodName;

	private boolean createNew;

	private String roleNames[];

	private String allRoleNames[] = {Role.ADMIN, Role.AUTHOR, Role.STAFF, Role.LEARNER};
	
	private List allAuthMethods; // AuthenticationMethod set

	private List otherMemberships; // UserOrganisation - other memberships
	
	private Integer newMembershipOrganisationId;

	private String newMembershipOrgName;

	public UserActionForm() {
		userId = new Integer(-1);
		setCreateNew(true);
		setNewMembershipOrganisationId(new Integer(-1));
		setRoleNames(new String[0]);
	}

	/**
	 * @return Returns the allAuthMethods.
	 */
	public List getAllAuthMethods() {
		return allAuthMethods;
	}
	/**
	 * @return Returns the allRoleNames.
	 */
	public String[] getAllRoleNames() {
		return allRoleNames;
	}
	/**
	 * @param allAuthMethods The allAuthMethods to set.
	 */
	public void setAllAuthMethods(List allAuthMethods) {
		this.allAuthMethods = allAuthMethods;
	}
	/**
	 * @return Returns the addressLine1.
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *            The addressLine1 to set.
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = StringUtils.trimToEmpty(addressLine1);
	}

	/**
	 * @return Returns the addressLine2.
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *            The addressLine2 to set.
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = StringUtils.trimToEmpty(addressLine2);
	}

	/**
	 * @return Returns the addressLine3.
	 */
	public String getAddressLine3() {
		return addressLine3;
	}

	/**
	 * @param addressLine3
	 *            The addressLine3 to set.
	 */
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = StringUtils.trimToEmpty(addressLine3);
	}

	/**
	 * @return Returns the authMethodName.
	 */
	public String getAuthMethodName() {
		return authMethodName;
	}

	/**
	 * @param authMethodName
	 *            The authMethodName to set.
	 */
	public void setAuthMethodName(String authMethodName) {
		this.authMethodName = StringUtils.trimToEmpty(authMethodName);
	}

	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            The city to set.
	 */
	public void setCity(String city) {
		this.city = StringUtils.trimToEmpty(city);
	}

	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            The country to set.
	 */
	public void setCountry(String country) {
		this.country = StringUtils.trimToEmpty(country);
	}

	/**
	 * @return Returns the createDate.
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate The createDate to set.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return Returns the createNew.
	 */
	public boolean isCreateNew() {
		return createNew;
	}

	/**
	 * @param createNew
	 *            The createNew to set.
	 */
	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

	/**
	 * @return Returns the dayPhone.
	 */
	public String getDayPhone() {
		return dayPhone;
	}

	/**
	 * @param dayPhone
	 *            The dayPhone to set.
	 */
	public void setDayPhone(String dayPhone) {
		this.dayPhone = StringUtils.trimToEmpty(dayPhone);
	}

	/**
	 * @return Returns the disabledFlag.
	 */
	public Boolean getDisabledFlag() {
		return disabledFlag;
	}

	/**
	 * @param disabledFlag The disabled to set.
	 */
	public void setDisabledFlag(Boolean disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = StringUtils.trimToEmpty(email);
	}

	/**
	 * @return Returns the evePhone.
	 */
	public String getEvePhone() {
		return evePhone;
	}

	/**
	 * @param evePhone
	 *            The evePhone to set.
	 */
	public void setEvePhone(String evePhone) {
		this.evePhone = StringUtils.trimToEmpty(evePhone);
	}

	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = StringUtils.trimToEmpty(fax);
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = StringUtils.trimToEmpty(firstName);
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = StringUtils.trimToEmpty(lastName);
	}

	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            The login to set.
	 */
	public void setLogin(String login) {
		this.login = StringUtils.trimToEmpty(login);
	}

	/**
	 * @return Returns the mobPhone.
	 */
	public String getMobPhone() {
		return mobPhone;
	}

	/**
	 * @param mobPhone
	 *            The mobPhone to set.
	 */
	public void setMobPhone(String mobPhone) {
		this.mobPhone = StringUtils.trimToEmpty(mobPhone);
	}

	/**
	 * @return Returns the newMembershipOrganisationId.
	 */
	public Integer getNewMembershipOrganisationId() {
		return newMembershipOrganisationId;
	}

	/**
	 * @param newMembershipOrganisationId
	 *            The newMembershipOrganisationId to set.
	 */
	public void setNewMembershipOrganisationId(
			Integer newMembershipOrganisationId) {
		this.newMembershipOrganisationId = newMembershipOrganisationId;
	}

	/**
	 * @return Returns the newMembershipOrgName.
	 */
	public String getNewMembershipOrgName() {
		return newMembershipOrgName;
	}

	/**
	 * @param newMembershipOrgName
	 *            The newMembershipOrgName to set.
	 */
	public void setNewMembershipOrgName(String newMembershipOrgName) {
		this.newMembershipOrgName = StringUtils
				.trimToEmpty(newMembershipOrgName);
	}

	/**
	 * @return Returns the orgId.
	 */
	public Integer getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the otherMemberships.
	 */
	public List getOtherMemberships() {
		return otherMemberships;
	}
	/**
	 * @param otherMemberships The otherMemberships to set.
	 */
	public void setOtherMemberships(List otherMemberships) {
		this.otherMemberships = otherMemberships;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = StringUtils.trimToEmpty(password);
	}

	/**
	 * @return Returns the passwordConfirm.
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * @param passwordConfirm
	 *            The passwordConfirm to set.
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = StringUtils.trimToEmpty(passwordConfirm);
	}

	/**
	 * @return Returns the roleNames.
	 */
	public String[] getRoleNames() {
		return roleNames;
	}

	/**
	 * @param roleNames
	 *            The roleNames to set.
	 */
	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}

	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(String state) {
		this.state = StringUtils.trimToEmpty(state);
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = StringUtils.trimToEmpty(title);
	}

	/**
	 * @return Returns the userId.
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Reset all properties to their default values. Only reset the values that
	 * will be overwritten by the form response - otherwise you will lose any
	 * display only values like Memberships.
	 * 
	 * Unfortunately, as this bean will be reused by other form calls, you have
	 * to get the setup write when you call the form for the first time.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setUserId(new Integer(-1));
		setPassword(null);
		setPasswordConfirm(null);
		setLogin(null);
		setFirstName(null);
		setLastName(null);
		setTitle(null);
		setAddressLine1(null);
		setAddressLine2(null);
		setAddressLine3(null);
		setDayPhone(null);
		setEvePhone(null);
		setMobPhone(null);
		setFax(null);
		setEmail(null);
		setCreateNew(true);
		setDisabledFlag(Boolean.FALSE);
		setAuthMethodName(null);
		setRoleNames(new String[0]);
		setNewMembershipOrganisationId(new Integer(-1));
		setNewMembershipOrgName(null);
	}

    /**
     * Return map of properties, for use with BeanUtil.populate().
     *
     * @return map of properties, for use with BeanUtil.populate().
     */
    public Map toMap() {
        Map map = new HashMap(20); 
        map.put("userId",getUserId());
        map.put("login",getLogin());
        map.put("password",getPassword());
        map.put("firstName",getFirstName());
        map.put("lastName",getLastName());
        map.put("title",getTitle());
        map.put("addressLine1",getAddressLine1());
        map.put("addressLine2",getAddressLine2());
        map.put("addressLine3",getAddressLine3());
        map.put("dayPhone",getDayPhone());
        map.put("evePhone",getEvePhone());
        map.put("mobPhone",getMobPhone());
        map.put("fax",getFax());
        map.put("email",getEmail());
        map.put("disabledFlag",getDisabledFlag());
        map.put("createDate",getCreateDate());
        map.put("createNew",new Boolean(isCreateNew()));
        map.put("roleNames",getRoleNames());
        map.put("authMethodName",getAuthMethodName());
        map.put("newMembershipOrganisationId",getNewMembershipOrganisationId());
        map.put("newMembershipOrgName",getNewMembershipOrgName());
        return map;
    }
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no
	 * recorded error messages.
	 * <p>
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		
		ActionErrors errors = super.validate(mapping, request);

		if (errors == null)
			errors = new ActionErrors();

		if (getLogin() == null || getLogin().length() == 0) {
			errors.add("login", new ActionMessage("error.username.required"));
		}
		if ((getAuthMethodName() == null)
				|| (getAuthMethodName().length() == 0)) {
			errors.add("authmethod", new ActionMessage(
					"error.authmethod.required"));
		}
		if ((getPassword() != null && getPassword().length() != 0)
				|| (getPasswordConfirm() != null && getPasswordConfirm()
						.length() != 0)) {
			// something entered in the password fields, make sure that they
			// both the same
			if (getPassword() == null
					|| !getPassword().equals(getPasswordConfirm())) {
				errors.add("password", new ActionMessage("error.password.match"));
			}
		}
		if (isCreateNew()
				&& (getPassword() == null || getPassword().length() == 0)) {
			errors.add("password", new ActionMessage("error.password.required"));
		}
		if (getRoleNames() == null || getRoleNames().length == 0) {
			errors.add("roles", new ActionMessage("error.role.required"));
		}

		if (errors.isEmpty())
			return null;
		else {
			// don't want to pass back what they had as the password
System.err.println("=====>UserActionForm validation errors: " + errors);			
			setPassword(null);
			setPasswordConfirm(null);
			return errors;
		}

	}

}