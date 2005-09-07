/*
 * Created on 2005-1-28
 *
 * Last modified on 2005-1-28
 */
package org.lamsfoundation.lams.admin.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="AdminPreparer.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AdminPreparer {

	public static void prepare(Organisation org, HttpServletRequest request, UserManagementService service){
		UserOrganisationRole userOrgRole = null;
		if(org.getParentOrganisation()!=null){
			userOrgRole = service.getUserOrganisationRole(request.getRemoteUser(),org.getParentOrganisation().getOrganisationId(),Role.ADMIN);	
		}
		List childOrgs = service.getChildOrganisations(org);
		for(int i=0; i<childOrgs.size();i++){
			Organisation childOrg = (Organisation)childOrgs.get(i);
			if(service.getUserOrganisationRole(request.getRemoteUser(),childOrg.getOrganisationId(),Role.ADMIN)==null){
				childOrgs.remove(i);
			}
		}
		if(childOrgs.size()!=0){
			Set childOrganisations = new HashSet();
			childOrganisations.addAll(childOrgs);
			org.setChildOrganisations(childOrganisations);
		}else{
			org.setChildOrganisations(null);
		}
		request.setAttribute(AttributeNames.ADMIN_ORGANISATION,org);
		if(userOrgRole!=null){
			request.setAttribute(AttributeNames.ADMIN_PARENT_ACCESS,"true");
		}
	}
}
