package org.lamsfoundation.lams.webservice.xml;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * Allows course groupings and groups access for integrated environments.
 *
 * @author Marcin Cieslak
 *
 */
public class OrganisationGroupServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(OrganisationGroupServlet.class);

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IUserManagementService userManagementService;

    private static DocumentBuilder docBuilder = null;

    static {
	try {
	    OrganisationGroupServlet.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	} catch (ParserConfigurationException e) {
	    log.error("Error while initialising XML document builder", e);
	}
    }
    
    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
	String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username = request.getParameter(CentralConstants.PARAM_USERNAME);

	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);

	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    if ("getGroupings".equalsIgnoreCase(method)) {
		Integer organisationId = WebUtil.readIntParam(request, CentralConstants.ATTR_COURSE_ID, false);
		getGroupings(organisationId, response);
	    } else {
		log.error("Unknown method: " + method);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown method: " + method);
	    }
	} catch (Exception e) {
	    log.error("Error while getting notifications", e);
	}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    @SuppressWarnings("unchecked")
    private void getGroupings(Integer organisationId, HttpServletResponse response) throws IOException {
	Document doc = OrganisationGroupServlet.docBuilder.newDocument();
	Element groupsElement = doc.createElement("groups");
	doc.appendChild(groupsElement);

	List<OrganisationGrouping> groupings = userManagementService.findByProperty(OrganisationGrouping.class,
		"organisationId", organisationId);
	for (OrganisationGrouping grouping : groupings) {
	    Element groupingElement = doc.createElement("grouping");
	    groupingElement.setAttribute("id", grouping.getGroupingId().toString());
	    groupingElement.setAttribute("name", StringEscapeUtils.escapeXml(grouping.getName()));
	    groupsElement.appendChild(groupingElement);
	    for (OrganisationGroup group : grouping.getGroups()) {
		Element groupElement = doc.createElement("group");
		groupElement.setAttribute("id", group.getGroupId().toString());
		groupElement.setAttribute("name", StringEscapeUtils.escapeXml(group.getName()));
		groupingElement.appendChild(groupElement);
		for (User user : group.getUsers()) {
		    Element userElement = doc.createElement("user");
		    userElement.setAttribute("id", user.getUserId().toString());
		    userElement.setAttribute("firstname", StringEscapeUtils.escapeXml(user.getFirstName()));
		    userElement.setAttribute("lastname", StringEscapeUtils.escapeXml(user.getLastName()));
		    groupElement.appendChild(userElement);
		}
	    }
	}

	response.setContentType("text/xml");
	response.setCharacterEncoding("UTF-8");

	DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
	LSSerializer lsSerializer = domImplementation.createLSSerializer();
	LSOutput lsOutput = domImplementation.createLSOutput();
	lsOutput.setEncoding("UTF-8");
	lsOutput.setByteStream(response.getOutputStream());
	lsSerializer.write(doc, lsOutput);
    }
}