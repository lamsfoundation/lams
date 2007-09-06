package org.lamsfoundation.lams.webservice.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LessonManagerServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(LessonManagerServlet.class);

	private static IntegrationService integrationService = null;

	private static IWorkspaceManagementService service = null;

	private static IMonitoringService monitoringService = null;

	/**
	 * Constructor of the object.
	 */
	public LessonManagerServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF8");
		
		String serverId 	= request.getParameter(CentralConstants.PARAM_SERVER_ID);
		String datetime 	= request.getParameter(CentralConstants.PARAM_DATE_TIME);
		String hashValue 	= request.getParameter(CentralConstants.PARAM_HASH_VALUE);
		String username  	= request.getParameter(CentralConstants.PARAM_USERNAME);
		String courseId 	= request.getParameter(CentralConstants.PARAM_COURSE_ID);
		String ldIdStr 		= request.getParameter(CentralConstants.PARAM_LEARNING_DESIGN_ID);
		String lsIdStr 		= request.getParameter(CentralConstants.PARAM_LESSON_ID);
		String country 		= request.getParameter(CentralConstants.PARAM_COUNTRY);
		String title 		= request.getParameter(CentralConstants.PARAM_TITLE);
		String desc 		= request.getParameter(CentralConstants.PARAM_DESC);
		String startDate 	= request.getParameter(CentralConstants.PARAM_STARTDATE);
		String lang 		= request.getParameter(CentralConstants.PARAM_LANG);
		String method 		= request.getParameter(CentralConstants.PARAM_METHOD);

		Long ldId = null;
		Long lsId = null;
		try {
			// TODO check input parameters are valid.

			// Create xml document
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element element = null;

			if (method.equals(CentralConstants.METHOD_START)) {
				ldId = new Long(ldIdStr);
				Long lessonId = startLesson(serverId, datetime, hashValue,
						username, ldId, courseId, title, desc, country, lang);

				element = document.createElement(CentralConstants.ELEM_LESSON);
				element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

			} else if (method.equals(CentralConstants.METHOD_SCHEDULE)) {
				ldId = new Long(ldIdStr);
				Long lessonId = scheduleLesson(serverId, datetime, hashValue,
						username, ldId, courseId, title, desc, startDate,
						country, lang);

				element = document.createElement(CentralConstants.ELEM_LESSON);
				element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

			} else if (method.equals(CentralConstants.METHOD_DELETE)) {
				lsId = new Long(lsIdStr);				
				Boolean deleted = deleteLesson(serverId, datetime, hashValue,
						username, lsId);

				element = document.createElement(CentralConstants.ELEM_LESSON);
				element.setAttribute(CentralConstants.ATTR_LESSON_ID, lsId.toString());
				element.setAttribute(CentralConstants.ATTR_DELETED, deleted.toString());

			} else {
				String msg = "Method :" + method + " is not recognised"; 
				log.error(msg);
				response.sendError(response.SC_BAD_REQUEST, msg);
			}

			document.appendChild(element);

			// write out the xml document.
			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			
			out.write(writer.toString());			
			
		} catch (NumberFormatException nfe) {
			log.error("lsId or ldId is not an integer" + lsIdStr + ldIdStr, nfe);
			response.sendError(response.SC_BAD_REQUEST, "lsId or ldId is not an integer");
		} catch (TransformerConfigurationException e) {
			log.error("Can not convert XML document to string", e);
			response.sendError(response.SC_INTERNAL_SERVER_ERROR);
		} catch (TransformerException e) {
			log.error("Can not convert XML document to string", e);
			response.sendError(response.SC_INTERNAL_SERVER_ERROR );
		} catch (ParserConfigurationException e) {
			log.error("Can not build XML document", e);
			response.sendError(response.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("Problem loading learning manager servlet request", e);
			response.sendError(response.SC_INTERNAL_SERVER_ERROR);
		}
		
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// TODO services should be implemented as POST
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.flush();
		out.close();
	}

	public Long startLesson(String serverId, String datetime, String hashValue,
			String username, long ldId, String courseId, String title,
			String desc, String countryIsoCode, String langIsoCode)
			throws RemoteException {
		try {
			ExtServerOrgMap serverMap = integrationService
					.getExtServerOrgMap(serverId);
			Authenticator
					.authenticate(serverMap, datetime, username, hashValue);
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(
					serverMap, username);
			ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(
					serverMap, userMap, courseId, countryIsoCode, langIsoCode);
			// 1. init lesson
			Lesson lesson = monitoringService
					.initializeLesson(title, desc, Boolean.TRUE, ldId, orgMap
							.getOrganisation().getOrganisationId(), userMap
							.getUser().getUserId());
			// 2. create lessonClass for lesson
			createLessonClass(lesson, orgMap.getOrganisation(), userMap
					.getUser());
			// 3. start lesson
			monitoringService.startLesson(lesson.getLessonId(), userMap
					.getUser().getUserId());

			return lesson.getLessonId();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public Long scheduleLesson(String serverId, String datetime,
			String hashValue, String username, long ldId, String courseId,
			String title, String desc, String startDate, String countryIsoCode,
			String langIsoCode) throws RemoteException {
		try {
			ExtServerOrgMap serverMap = integrationService
					.getExtServerOrgMap(serverId);
			Authenticator
					.authenticate(serverMap, datetime, username, hashValue);
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(
					serverMap, username);
			ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(
					serverMap, userMap, courseId, countryIsoCode, langIsoCode);
			// 1. init lesson
			Lesson lesson = monitoringService
					.initializeLesson(title, desc, Boolean.TRUE, ldId, orgMap
							.getOrganisation().getOrganisationId(), userMap
							.getUser().getUserId());
			// 2. create lessonClass for lesson
			createLessonClass(lesson, orgMap.getOrganisation(), userMap
					.getUser());
			// 3. schedule lesson
			Date date = DateUtil.convertFromLAMSFlashFormat(startDate);
			monitoringService.startLessonOnSchedule(lesson.getLessonId(), date,
					userMap.getUser().getUserId());
			return lesson.getLessonId();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public boolean deleteLesson(String serverId, String datetime,
			String hashValue, String username, long lsId)
			throws RemoteException {
		try {
			ExtServerOrgMap serverMap = integrationService
					.getExtServerOrgMap(serverId);
			Authenticator
					.authenticate(serverMap, datetime, username, hashValue);
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(
					serverMap, username);
			monitoringService.removeLesson(lsId, userMap.getUser().getUserId());
			return true;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	private void createLessonClass(Lesson lesson, Organisation organisation,
			User creator) {
		List<User> staffList = new LinkedList<User>();
		staffList.add(creator);
		List<User> learnerList = new LinkedList<User>();
		monitoringService.createLessonClassForLesson(lesson.getLessonId(),
				organisation, organisation.getName() + "Learners", learnerList,
				organisation.getName() + "Staff", staffList, creator
						.getUserId());

	}

	// private IMonitoringService getMonitoringService() {
	// if (monitoringService == null) {
	// monitoringService = (IMonitoringService) WebApplicationContextUtils
	// .getRequiredWebApplicationContext(getServletContext())
	// .getBean("monitoringService");
	// }
	// return monitoringService;
	// }
	//
	// private IntegrationService getIntegrationService() {
	// if (integrationService == null) {
	// integrationService = (IntegrationService) WebApplicationContextUtils
	// .getRequiredWebApplicationContext(getServletContext())
	// .getBean("integrationService");
	// }
	// return integrationService;
	// }
	//
	// private IWorkspaceManagementService getService() {
	// if (service == null) {
	// service = (IWorkspaceManagementService) WebApplicationContextUtils
	// .getRequiredWebApplicationContext(getServletContext())
	// .getBean("workspaceManagementService");
	// }
	// return service;
	// }
	//
	// private MessageService getMessageService() {
	// if (msgService == null) {
	// msgService = getService().getMessageService();
	// }
	// return msgService;
	// }

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		service = (IWorkspaceManagementService) WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext()).getBean(
						"workspaceManagementService");

		integrationService = (IntegrationService) WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext()).getBean(
						"integrationService");

		monitoringService = (IMonitoringService) WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext()).getBean(
						"monitoringService");
	}
}
