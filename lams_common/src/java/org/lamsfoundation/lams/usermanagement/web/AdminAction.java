package org.lamsfoundation.lams.usermanagement.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.util.ExcelUserImportFileParser;
import org.lamsfoundation.lams.usermanagement.util.AdminPreparer;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.HttpSessionManager;
import org.lamsfoundation.lams.web.AttributeNames;
import org.lamsfoundation.lams.util.UploadFileUtil;
import org.lamsfoundation.lams.usermanagement.web.UserActionForm;

/**
 * Main access calls for the normal user adminstration screens.
 * 
 * @author Fei Yang
 * 
 * @struts:action path="/admin" validate="false" parameter="method"
 * @struts:action-forward name="admin" path=".admin"
 * @struts:action-forward name="organisation" path=".admin.organisation"
 * @struts:action-forward name="user" path=".admin.user"
 * @struts:action-forward name="usersadd" path=".admin.usersadd"
 * @struts:action-forward name="usersremove" path=".admin.usersremove"
 * @struts:action-forward name="error" path=".admin.error"
 */
public class AdminAction extends DispatchAction {

	private static Logger log = Logger.getLogger(AdminAction.class);

	private static WebApplicationContext ctx = WebApplicationContextUtils
			.getWebApplicationContext(HttpSessionManager.getInstance()
					.getServletContext());

	private static UserManagementService service = (UserManagementService) ctx
			.getBean("userManagementServiceTarget");

	public ActionForward getAdmin(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		log.debug("getAdmin");
		Integer orgId = null;
		try {
			if (request.getParameter("orgId") != null) {
				orgId = new Integer(request.getParameter("orgId").trim());
			}
		} catch (NumberFormatException e) {
		}

		boolean error = false;

		if (orgId == null) {
			log.error("There is no 'orgId' parameter in the request");
			error = true;
		} else {//edit organisation
			Organisation org = service.getOrganisationById(orgId);
			if (org != null) {
				log.debug("Copying properties from org " + org.toString());
				AdminPreparer.prepare(org,request,service);
			} else {
				log.error("Organisation id " + orgId + " not found");
				error = true;
			}
		}

		return mapping.findForward(error ? "error" : "admin");
	}

	
	/**
	 * Initial call that loads up the form for editing the name/description of
	 * an organisation.
	 */
	public ActionForward getOrganisationEdit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("getOrganisationEdit");

		Integer orgId = null;
		try {
			if (request.getParameter("orgId") != null) {
				orgId = new Integer(request.getParameter("orgId").trim());
			}
		} catch (NumberFormatException e) {
		}

		boolean error = false;

		OrganisationActionForm orgForm = new OrganisationActionForm();

		if (orgId == null) {
			log.error("There is no 'orgId' parameter in the request");
			error = true;
		} else if (orgId.intValue() == -1) {//create child organisation
			orgForm.setOrgId(new Integer(-1));
			orgForm.setName("");
			orgForm.setDescription("");
			Integer parentOrgId = new Integer(request.getParameter(
					"parentOrgId").trim());
			Organisation parent = service.getOrganisationById(parentOrgId);
			if (parent != null) {
				orgForm.setParentOrgId(parent.getOrganisationId());
				orgForm.setParentOrgName(parent.getName());
			} else {
				log
						.error("Creating new top level organisation (parent organisation id not found)");
				error = true;
			}
		} else {//edit organisation
			Organisation org = service.getOrganisationById(orgId);
			if (org != null) {
				log.debug("Copying properties from org " + org.toString());
				orgForm.setOrgId(org.getOrganisationId());
				orgForm.setName(org.getName());
				orgForm.setDescription(org.getDescription());
			} else {
				log.error("Organisation id " + orgId + " not found");
				error = true;
			}
		}

		request.getSession(true).setAttribute(OrganisationActionForm.formName,
				orgForm);
		return mapping.findForward(error ? "error" : "organisation");
	}

	/**
	 * import users form file
	 * 
	 * @return screen reference name - "error" or "admin"
	 */
	public ActionForward importUsersFromFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		boolean error = false;
		if (request.getParameter("orgId") == null) {
			error = true;
			log.error("There is no 'orgId' parameter in the reuqest");
		} else {
			Integer orgId = new Integer(request.getParameter("orgId").trim());
			Organisation org = service.getOrganisationById(orgId);
			FileItem fileToUpload = null;
			String errorMessage = null;
			List items = null;
			try {
				items = UploadFileUtil.getUploadItems(request, false, null);
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					log.debug("Request item: " + item.toString());
					if (!(item.getName() == null || item.getSize() == 0)) {
						fileToUpload = item;
					}
				}
				if (fileToUpload == null) {
					error = true;
					log.error("There is no file uploaded");
				} else {
					ExcelUserImportFileParser parser = new ExcelUserImportFileParser(
							service);
					//TODO Test if existingUserOnly works
					boolean existingUsersOnly = ((String) request
							.getParameter("existingUsersOnly")).equals("true");
					errorMessage = parser.parseUsersInOrganisation(
							fileToUpload, org, request.getRemoteUser(),
							existingUsersOnly);
				}
			} catch (FileUploadException fue) {
				log.error("Unable to upload file. Exception occured: ", fue);
				errorMessage = "Unable to upload file. Error was:"
						+ fue.getMessage();
			} catch (IOException e) {
				log
						.error("IOException happened when processing uploaded file: "
								+ e);
				errorMessage = "IOException happened when processing uploaded file: "
						+ e.getMessage();
			} catch (Exception e) {
				log.error("Exception happened when uploading file: " + e);
				errorMessage = "Exception happened when uploading file: "
						+ e.getMessage();
			}
			AdminPreparer.prepare(org,request,service);
			if (errorMessage != null) {
				request.setAttribute(AttributeNames.ADMIN_ERR_MSG,errorMessage);
			}
		}
		return mapping.findForward(error ? "error" : "admin");
	}

	/**
	 * Does the actual work that loads up the user form for editing.
	 * 
	 * @return Screen reference name - "error" or "userentry"
	 */
	public ActionForward getUserEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("getUserEdit");
		Integer editUserId = null; // the user to modify
		String adminUserLogin = null;

		try {
			if (request.getParameter("userid") != null) {
				editUserId = new Integer(request.getParameter("userid").trim());
			}
		} catch (NumberFormatException e) {
		}

		UserActionForm newForm = null;
		boolean error = false;

		try {

			adminUserLogin = request.getRemoteUser();

			Integer orgId = null;
			try {
				if (request.getParameter("orgId") != null) {
					orgId = new Integer(request.getParameter("orgId").trim());
				}
			} catch (NumberFormatException e) {
			}

			Organisation org = service.getOrganisationById(orgId);
			if (org == null) {
				log.error("implUserEdit: Organisation not found " + orgId);
				error = true;
			} else {
				request.setAttribute(AttributeNames.ADMIN_ORGANISATION, org);
				User user = null;
				if (editUserId == null) {
					editUserId = new Integer(-1);
				} else {
					user = service.getUserById(editUserId);
				}

				if (user != null) {
					newForm = setupEditUserActionForm(editUserId, org, user);
				} else {
					newForm = setupNewUserActionForm(org);
				}

			}

		} catch (IllegalAccessException e) {
			log.error("Exception occured ", e);
			error = true;
		} catch (InvocationTargetException e) {
			log.error("Exception occured ", e);
			error = true;
		}

		log.debug("getUserEdit: Form is " + newForm.toMap().toString());

		// Form bean has to go in the session otherwise end up with two form
		// beans
		// floating around the system.
		request.getSession(true).setAttribute(UserActionForm.formName, newForm);
		return mapping.findForward(error ? "error" : "user");
	}

	private UserActionForm setupNewUserActionForm(Organisation org) {
		UserActionForm newForm = new UserActionForm();

		newForm.setOrgId(org.getOrganisationId());
		newForm.setCreateNew(true);
		newForm.setDisabledFlag(new Boolean(false));
		newForm.setCreateDate(new Date());
		newForm.setRoleNames(new String[0]);
		newForm.setNewMembershipOrganisationId(org.getOrganisationId());
		newForm.setAllAuthMethods(service.getAllAuthenticationMethods());
		newForm.setNewMembershipOrgName(org.getName());
		return newForm;
	}

	private UserActionForm setupEditUserActionForm(Integer editUserId,
			Organisation org, User user) throws IllegalAccessException,
			InvocationTargetException {

		UserActionForm newForm = new UserActionForm();
		BeanUtils.copyProperties(newForm, user);
		newForm.setPasswordConfirm(newForm.getPassword());
		newForm.setOrgId(org.getOrganisationId());
		newForm.setOtherMemberships(service.getUserOrganisationsForUser(user));
		newForm.setAllAuthMethods(service.getAllAuthenticationMethods());
		newForm.setCreateNew(false);
		newForm.setRoleNames(getRoleNames(user, org.getOrganisationId()));
		return newForm;
	}

	// get the users roles
	private String[] getRoleNames(User user, Integer orgId) {

		String rolenames[] = new String[service.getRolesForUserByOrganisation(
				user, orgId).size()];
		Iterator iter = service.getRolesForUserByOrganisation(user, orgId)
				.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			rolenames[i] = role.getName();
			i++;
		}

		return rolenames;
	}

	/**
	 * Do the actual work that loads up the organisation add user form for
	 * editing
	 * 
	 * @return screen reference name - "error" or "usersadd"
	 */
	public ActionForward getUsersAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		log.debug("getUsersAdd");
		Integer orgId = null;
		try {
			if (request.getParameter("orgId") != null) {
				orgId = new Integer(request.getParameter("orgId").trim());
			}
		} catch (NumberFormatException e) {
		}

		UsersAddActionForm newForm = null;
		boolean error = false;

		if (orgId == null) {
			log.error("getUsersAdd: Missing parameter " + "orgId");
			error = true;
		} else {
			newForm = setupOrgAddUserForm(request, orgId);
		}

		if (newForm == null || error) {
			request.getSession(true).removeAttribute(UsersAddActionForm.formName);
			return mapping.findForward("error");
		} else {
			// has to go in the session otherwise end up with two form beans
			// floating around the system.
			request.getSession(true).setAttribute(UsersAddActionForm.formName,newForm);
			return mapping.findForward("usersadd");
		}
	}

	private UsersAddActionForm setupOrgAddUserForm(HttpServletRequest request,
			Integer orgId) {

		boolean error = false;
		UsersAddActionForm newForm = new UsersAddActionForm();
		try {
			Organisation org = service.getOrganisationById(orgId);
			if (org != null) {
				log.debug("Copying properties from org " + org.toString());
				newForm.setName(org.getName());
				newForm.setDescription(org.getDescription());
				newForm.setOrgId(org.getOrganisationId());
			} else {
				log.error("getUsersAdd: Organisation not found or not available to this user. Id #="+ orgId);
				error = true;
			}

			// get all the organisation that the user can select from.
			User adminUser = service.getUserByLogin(request.getRemoteUser());
			List userOrgs = service.getUserOrganisationsForUser(adminUser);
			newForm.setAvailableOrgs(getOrgsWithAdminRole(userOrgs, org));

			newForm.setSelectedOrgId(new Integer(-1));

		} catch (Exception e) {
			log.error("Exception occured ", e);
			error = true;
		}

		if (error) {
			newForm = null;
			return null;
		} else {
			return newForm;
		}
	}

	private List getOrgsWithAdminRole(List userOrgs, Organisation currentOrg) {
		List orgs = new ArrayList();
		Iterator iter = userOrgs.iterator();
		while (iter.hasNext()) {
			UserOrganisation userOrg = (UserOrganisation) iter.next();
			List roles = service.getRolesForUserByOrganisation(userOrg
					.getUser(), userOrg.getOrganisation().getOrganisationId());
			if (!userOrg.getOrganisation().getOrganisationId().equals(
					currentOrg.getOrganisationId())) {
				Iterator iter2 = roles.iterator();
				while (iter2.hasNext()) {
					Role role = (Role) iter2.next();
					if (role.getName().equals(Role.ADMIN)) {
						orgs.add(userOrg.getOrganisation());
					}
				}
			}
		}
		return orgs;
	}

	/**
	 * Initial call that loads up the organisation remove user form for editing
	 */
	public ActionForward getUsersRemove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("getUsersRemove");
		Integer orgId = null;
		try {
			if (request.getParameter("orgId") != null) {
				orgId = new Integer(request.getParameter("orgId").trim());
			}
		} catch (NumberFormatException e) {
		}

		UsersRemoveActionForm newForm = new UsersRemoveActionForm();
		log.debug("orgId=" + orgId);
		boolean error = false;

		if (orgId == null) {
			log.error("getOrganisationAddUsers: Missing parameter " + "orgId");
			error = true;
		} else {
			Organisation org = service.getOrganisationById(orgId);
			if (org != null) {
				log.debug("Copying properties from org " + org.toString());
				newForm.setName(org.getName());
				newForm.setDescription(org.getDescription());
				newForm.setOrgId(org.getOrganisationId());
				// need only the users who aren't home users!
				List users = service.getUsersFromOrganisation(orgId);
				for(int i=0; i<users.size(); i++) {
					User user = (User) users.get(i);
					if (user.getLogin().equals(request.getRemoteUser())) {
						users.remove(i);
					}
				}
				newForm.setUsers(users);
			} else {
				log.error("getUsersRemove: Organisation not found or not available to this user. Id #="
								+ orgId);
				error = true;
			}

		}

		// has to go in the session otherwise end up with two form beans
		// floating around the system.
		request.getSession(true).setAttribute(UsersRemoveActionForm.formName,
				newForm);

		return (mapping.findForward(error ? "error" : "usersremove"));
	}

}