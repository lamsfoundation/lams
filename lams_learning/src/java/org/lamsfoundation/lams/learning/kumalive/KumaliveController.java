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
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Marcin Cieslak
 */
@Controller
@RequestMapping("/kumalive")
public class KumaliveController {

    private static Logger log = Logger.getLogger(KumaliveController.class);

    private static IKumaliveService kumaliveService;
    private static ISecurityService securityService;

    @RequestMapping("/getRubrics")
    public String getRubrics(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get rubrics", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	List<KumaliveRubric> rubrics = KumaliveController.getKumaliveService().getRubrics(organisationId);
	ArrayNode rubricsJSON = JsonNodeFactory.instance.arrayNode();
	for (KumaliveRubric rubric : rubrics) {
	    rubricsJSON.add(rubric.getName());
	}
	request.setAttribute("rubrics", rubricsJSON);

	return "kumalive/kumaliveRubrics";
    }

    @RequestMapping("/getReport")
    public String getReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	return "/kumalive/kumaliveReport";
    }

    @RequestMapping("/getReportOrganisationData")
    @ResponseBody
    public String getReportOrganisationData(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisationId, currentUserId,
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

	ObjectNode resultJSON = KumaliveController.getKumaliveService().getReportOrganisationData(organisationId,
		sortColumn, !"DESC".equalsIgnoreCase(sortOrder), rowLimit, page);
	response.setContentType("text/xml;charset=utf-8");
	return resultJSON.toString();
    }

    @RequestMapping("/getReportKumaliveRubrics")
    @ResponseBody
    public String getReportKumaliveRubrics(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Long kumaliveId = WebUtil.readLongParam(request, "kumaliveId", false);
	Kumalive kumalive = KumaliveController.getKumaliveService().getKumalive(kumaliveId);
	Organisation organisation = kumalive.getOrganisation();
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisation.getOrganisationId(), currentUserId,
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
	response.setContentType("text/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping("/getReportKumaliveData")
    @ResponseBody
    public String getReportKumaliveData(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Long kumaliveId = WebUtil.readLongParam(request, "kumaliveId", false);
	Kumalive kumalive = KumaliveController.getKumaliveService().getKumalive(kumaliveId);
	Organisation organisation = kumalive.getOrganisation();
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisation.getOrganisationId(), currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report kumalive data", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation "
		    + organisation.getOrganisationId();
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);

	ObjectNode responseJSON = KumaliveController.getKumaliveService().getReportKumaliveData(kumaliveId,
		!"DESC".equalsIgnoreCase(sortOrder));
	response.setContentType("text/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping("/getReportUserData")
    @ResponseBody
    public String getReportUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Long kumaliveId = WebUtil.readLongParam(request, "kumaliveId", false);
	Integer userId = WebUtil.readIntParam(request, "userId", false);
	Kumalive kumalive = KumaliveController.getKumaliveService().getKumalive(kumaliveId);
	Organisation organisation = kumalive.getOrganisation();
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisation.getOrganisationId(), currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get report user data", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation "
		    + organisation.getOrganisationId();
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	ObjectNode responseJSON = KumaliveController.getKumaliveService().getReportUserData(kumaliveId, userId);
	response.setContentType("text/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping("/exportKumalives")
    @ResponseBody
    public void exportKumalives(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	List<Long> kumaliveIds = null;
	if (organisationId == null) {
	    String kumaliveIdsParam = WebUtil.readStrParam(request, "kumaliveIds", false);

	    ArrayNode kumaliveIdsJSON = JsonUtil.readArray(kumaliveIdsParam);
	    kumaliveIds = new LinkedList<>();
	    for (JsonNode kumaliveIdJSON : kumaliveIdsJSON) {
		kumaliveIds.add(kumaliveIdJSON.asLong());
	    }

	    Kumalive kumalive = KumaliveController.getKumaliveService().getKumalive(kumaliveIds.get(0));
	    organisationId = kumalive.getOrganisation().getOrganisationId();
	}

	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisationId, currentUserId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive export", false)) {
	    String warning = "User " + currentUserId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	}

	LinkedHashMap<String, ExcelCell[][]> dataToExport = null;
	if (kumaliveIds == null) {
	    dataToExport = KumaliveController.getKumaliveService().exportKumalives(organisationId);
	} else {
	    dataToExport = KumaliveController.getKumaliveService().exportKumalives(kumaliveIds);
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

    }

    @RequestMapping("/saveRubrics")
    @ResponseBody
    public void saveRubrics(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer userId = userDTO.getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, false);
	if (!Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE)) {
	    String warning = "Kumalives are disabled";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	}
	if (!KumaliveController.getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "kumalive get rubrics", false)) {
	    String warning = "User " + userId + " is not a monitor of organisation " + organisationId;
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	}

	ArrayNode rubricsJSON = JsonUtil.readArray(WebUtil.readStrParam(request, "rubrics"));
	KumaliveController.getKumaliveService().saveRubrics(organisationId, rubricsJSON);

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