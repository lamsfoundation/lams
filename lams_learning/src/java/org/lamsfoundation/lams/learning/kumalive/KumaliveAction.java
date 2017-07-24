package org.lamsfoundation.lams.learning.kumalive;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.service.IKumaliveService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Marcin Cieslak
 */
public class KumaliveAction extends DispatchAction {

    private static Logger log = Logger.getLogger(KumaliveAction.class);

    private static IKumaliveService kumaliveService;
    private static ISecurityService securityService;

    public ActionForward getRubrics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer userId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get rubrics", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	List<KumaliveRubric> rubrics = KumaliveAction.getKumaliveService().getRubrics(organisationId);
	JSONArray rubricsJSON = new JSONArray();
	for (KumaliveRubric rubric : rubrics) {
	    rubricsJSON.put(rubric.getName());
	}
	request.setAttribute("rubrics", rubricsJSON);

	return mapping.findForward("displayRubrics");
    }

    public ActionForward saveRubrics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	UserDTO userDTO = getUserDTO();
	Integer userId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get rubrics", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	JSONArray rubricsJSON = new JSONArray(WebUtil.readStrParam(request, "rubrics"));
	KumaliveAction.getKumaliveService().saveRubrics(organisationId, rubricsJSON);

	return null;
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private static IKumaliveService getKumaliveService() {
	if (kumaliveService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    kumaliveService = (IKumaliveService) ctx.getBean("kumaliveService");
	}
	return kumaliveService;
    }

    private static ISecurityService getSecurityService() {
	if (securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return securityService;
    }
}