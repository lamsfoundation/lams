package org.lamsfoundation.lams.learning.kumalive;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.service.IKumaliveService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Marcin Cieslak
 */
public class KumaliveAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(KumaliveAction.class);

    private static IKumaliveService kumaliveService;
    private static ISecurityService securityService;

    public ActionForward getRubrics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get rubrics", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	List<KumaliveRubric> rubrics = KumaliveAction.getKumaliveService().getRubrics(organisationId);
	ArrayNode rubricsJSON = JsonNodeFactory.instance.arrayNode();
	for (KumaliveRubric rubric : rubrics) {
	    rubricsJSON.add(rubric.getName());
	}
	request.setAttribute("rubrics", rubricsJSON);

	return mapping.findForward("displayRubrics");
    }

    public ActionForward getReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	return mapping.findForward("displayReport");
    }

    public ActionForward getReportOrganisationData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report organisation data", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	int page = WebUtil.readIntParam(request, GradebookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradebookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);
	String sortColumn = WebUtil.readStrParam(request, GradebookConstants.PARAM_SIDX, true);

	ObjectNode resultJSON = KumaliveAction.getKumaliveService().getReportOrganisationData(organisationId,
		sortColumn, !"DESC".equalsIgnoreCase(sortOrder), rowLimit, page);
	writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_XML, LamsDispatchAction.ENCODING_UTF8,
		resultJSON.toString());
	return null;
    }

    public ActionForward getReportKumaliveRubrics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Long kumaliveId = WebUtil.readLongParam(request, "kumaliveId", false);
	Kumalive kumalive = KumaliveAction.getKumaliveService().getKumalive(kumaliveId);
	Organisation organisation = kumalive.getOrganisation();
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisation.getOrganisationId(), currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report kumalive rubrics", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation "
		    + organisation.getOrganisationId();
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (KumaliveRubric rubric : kumalive.getRubrics()) {
	    ArrayNode rubricJSON = JsonNodeFactory.instance.arrayNode();
	    rubricJSON.add(rubric.getRubricId());
	    rubricJSON.add(rubric.getName() == null ? "" : rubric.getName());
	    responseJSON.add(rubricJSON);
	}

	writeResponse(response, "text/json", LamsDispatchAction.ENCODING_UTF8, responseJSON.toString());
	return null;
    }

    public ActionForward getReportKumaliveData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Long kumaliveId = WebUtil.readLongParam(request, "kumaliveId", false);
	Kumalive kumalive = KumaliveAction.getKumaliveService().getKumalive(kumaliveId);
	Organisation organisation = kumalive.getOrganisation();
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisation.getOrganisationId(), currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report kumalive data", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation "
		    + organisation.getOrganisationId();
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);

	ObjectNode responseJSON = KumaliveAction.getKumaliveService().getReportKumaliveData(kumaliveId,
		!"DESC".equalsIgnoreCase(sortOrder));

	writeResponse(response, "text/json", LamsDispatchAction.ENCODING_UTF8, responseJSON.toString());
	return null;
    }

    public ActionForward getReportUserData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Long kumaliveId = WebUtil.readLongParam(request, "kumaliveId", false);
	Integer userId = WebUtil.readIntParam(request, "userId", false);
	Kumalive kumalive = KumaliveAction.getKumaliveService().getKumalive(kumaliveId);
	Organisation organisation = kumalive.getOrganisation();
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisation.getOrganisationId(), currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report user data", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation "
		    + organisation.getOrganisationId();
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	ObjectNode responseJSON = KumaliveAction.getKumaliveService().getReportUserData(kumaliveId, userId);

	writeResponse(response, "text/json", LamsDispatchAction.ENCODING_UTF8, responseJSON.toString());
	return null;
    }

    public ActionForward exportKumalives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	List<Long> kumaliveIds = null;
	if (organisationId == null) {
	    String kumaliveIdsParam = WebUtil.readStrParam(request, "kumaliveIds", false);

	    ArrayNode kumaliveIdsJSON = JsonUtil.readArray(kumaliveIdsParam);
	    kumaliveIds = new LinkedList<Long>();
	    for (JsonNode kumaliveIdJSON : kumaliveIdsJSON) {
		kumaliveIds.add(kumaliveIdJSON.asLong());
	    }

	    Kumalive kumalive = KumaliveAction.getKumaliveService().getKumalive(kumaliveIds.get(0));
	    organisationId = kumalive.getOrganisation().getOrganisationId();
	}

	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive export", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	LinkedHashMap<String, ExcelCell[][]> dataToExport = null;
	if (kumaliveIds == null) {
	    dataToExport = KumaliveAction.getKumaliveService().exportKumalives(organisationId);
	} else {
	    dataToExport = KumaliveAction.getKumaliveService().exportKumalives(kumaliveIds);
	}
	String fileName = "kumalive_report.xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ExcelUtil.createExcel(response.getOutputStream(), dataToExport, "Exported on:", true);

	return null;
    }

    public ActionForward saveRubrics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer userId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveAction.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get rubrics", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	ArrayNode rubricsJSON = JsonUtil.readArray(WebUtil.readStrParam(request, "rubrics"));
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