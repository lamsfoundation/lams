package org.lamsfoundation.lams.webservice.xml;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.lamsfoundation.lams.events.DeliveryMethodNotification;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.events.Subscription;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
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
 * Allows notifications access for integrated environments.
 *
 * @author Marcin Cieslak
 */
public class NotificationServlet extends HttpServlet {
    private static final long serialVersionUID = 4856874776383254865L;
    private static Logger log = Logger.getLogger(NotificationServlet.class);
    private static final Pattern anchorPattern = Pattern.compile("<a .*href=(['\"])(.*)\\1.*>(.*)</a>");
    private static DocumentBuilder docBuilder = null;

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IEventNotificationService eventNotificationService;

    static {
	try {
	    NotificationServlet.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	} catch (ParserConfigurationException e) {
	    NotificationServlet.log.error("Error while initialising XML document builder", e);
	}
    }

    /**
     * This method is called when a form has its tag value method equals to get.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
	String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username = request.getParameter(CentralConstants.PARAM_USERNAME);

	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    if ("getNotifications".equalsIgnoreCase(method)) {
		getNotifications(userMap.getUser().getUserId(), request, response);
	    }
	} catch (Exception e) {
	    log.error("Error while getting notifications", e);
	}
    }

    /**
     * This method is called when a form has its tag value method equals to post.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
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

    private void getNotifications(Integer userId, HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Document doc = NotificationServlet.docBuilder.newDocument();
	Element notificationsElement = doc.createElement("Notifications");
	doc.appendChild(notificationsElement);

	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, true);
	Integer limit = WebUtil.readIntParam(request, "limit", true);
	Integer offset = WebUtil.readIntParam(request, "offset", true);
	Boolean pendingOnly = WebUtil.readBooleanParam(request, "pendingOnly", true);

	List<Subscription> subscriptions = eventNotificationService
		.getNotificationSubscriptions(lessonId, userId, pendingOnly, limit, offset);
	for (Subscription subscription : subscriptions) {
	    Element notificationElement = doc.createElement("Notification");

	    notificationElement.setAttribute("id", subscription.getUid().toString());

	    Boolean pending = !DeliveryMethodNotification.LAST_OPERATION_SEEN
		    .equals(subscription.getLastOperationMessage());
	    notificationElement.setAttribute("pending", pending.toString());

	    Long notificationLessonId = subscription.getEvent().getEventSessionId();
	    if (notificationLessonId != null) {
		notificationElement.setAttribute("lessonId", notificationLessonId.toString());
	    }

	    String message = subscription.getEvent().getMessage();
	    Matcher matcher = NotificationServlet.anchorPattern.matcher(message);
	    if (matcher.find()) {
		String href = StringEscapeUtils.escapeXml(matcher.group(2));
		notificationElement.setAttribute("href", href);
		message = matcher.group(3);
	    }
	    notificationElement.appendChild(doc.createCDATASection(message));

	    notificationsElement.appendChild(notificationElement);
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