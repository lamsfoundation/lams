package org.lamsfoundation.bb.integration.servlet;
/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.bb.integration.util.BlackboardUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.lamsfoundation.bb.integration.util.LamsServerException;
import org.lamsfoundation.bb.integration.util.LineitemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import blackboard.base.FormattedText;
import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.data.navigation.CourseToc;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;
import blackboard.util.StringUtil;

/**
 * 
 */
public class LinkToolsServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = LoggerFactory.getLogger(LinkToolsServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	// Authorise current user for Course Control Panel (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}
	
	String method = request.getParameter("method");
	if (method.equals("openAdminLinkTool")) {
	    openAdminLinkTool(request, response);
	    
	} else if (method.equals("openAuthorLinkTool")) {
	    openAuthorLinkTool(request, response);

	} else if (method.equals("openMonitorLinkTool")) {
	    openMonitorLinkTool(request, response);
	
	//open LAMS course gradebook page
	} else if (method.equals("openCourseGradebook")) {
	    openCourseGradebook(request, response);

	//Admin on BB side calls this servlet to clone old lesson that were copied to the new course.
	} else if (method.equals("cloneLessons")) {
	    cloneLessons(request, response);

	//Admin on BB side calls this servlet to import old lesson that were copied to the new course.
	} else if (method.equals("importLessons")) {
	    importLessons(request, response);

	//Admin on BB side calls this servlet to correct lineitems that have been screwed up while copying/importing courses.
	} else if (method.equals("correctLineitems")) {
	    correctLineitems(request, response);
	}
    }
    
    private void openAdminLinkTool(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.getRequestDispatcher("/links/admin.jsp").forward(request, response);
    }
    
    private void openAuthorLinkTool(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.getRequestDispatcher("/links/author.jsp").forward(request, response);
    }
    
    private void openMonitorLinkTool(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	Container bbContainer = bbPm.getContainer();
	Id courseId = new PkId(bbContainer, Course.DATA_TYPE, request.getParameter("course_id"));
	
	ContextManager ctxMgr = null;
	String strOut = "[[]]";
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.getContext();
	    ContentDbLoader cLoader = (ContentDbLoader) bbPm.getLoader(ContentDbLoader.TYPE);
	    CourseTocDbLoader ctLoader = (CourseTocDbLoader) bbPm.getLoader(CourseTocDbLoader.TYPE);

	    Course course = ctx.getCourse();
	    List<CourseToc> ctList = ctLoader.loadByCourseId(courseId);
	    CourseToc[] courseTocs = (CourseToc[]) ctList.toArray(new CourseToc[0]);
	    
	    int idx = 0;
	    StringBuilder strB = new StringBuilder();
	    strB.append("[{type:'Text', label:'" + course.getTitle().replace("'", "\\'") + "', id:0, children:[");
	    for (int i = 0; i < courseTocs.length; i++) {
		if (courseTocs[i].getTargetType().compareTo(CourseToc.Target.CONTENT) == 0) {
		    Content cont = cLoader.loadByTocId(courseTocs[i].getId());
		    strB.append(getChild(cont, cLoader));
		    idx = i;
		    break;
		}
	    }
	    for (int i = idx + 1; i < courseTocs.length; i++) {
		if (courseTocs[i].getTargetType().compareTo(CourseToc.Target.CONTENT) == 0) {
		    Content cont = cLoader.loadByTocId(courseTocs[i].getId());
		    strB.append(", ").append(getChild(cont, cLoader));
		}
	    }
	    strB.append("]}]");
	    strOut = strB.toString();
	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	}
	
	request.setAttribute("treeView", strOut);
	request.getRequestDispatcher("/links/monitor.jsp").forward(request, response);
    }

    private static String extractParameterValue(String url, String param) {
	if (url != null && param != null) {
	    int quotationMarkIndex = url.indexOf("?");
	    String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	    String[] paramEntries = queryPart.split("&");
	    for (String paramEntry : paramEntries) {
		String[] paramEntrySplitted = paramEntry.split("=");
		if ((paramEntrySplitted.length > 1) && param.equalsIgnoreCase(paramEntrySplitted[0])) {
		    return paramEntrySplitted[1];
		}
	    }
	}
	return null;
    }

    private String getChild(Content f, ContentDbLoader cLoader) {
	StringBuilder sb = new StringBuilder();
	try {

	    if (f.getIsFolder()) {

		List<Content> cList = cLoader.loadChildren(f.getId());
		Content[] cArray = cList.toArray(new Content[0]);
		//sort content by title
		Arrays.sort(cArray, new Comparator<Content>() {
		    @Override
		    public int compare(Content o1, Content o2) {
			if (o1 != null && o2 != null) {
			    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
			} else if (o1 != null)
			    return 1;
			else
			    return -1;
		    }
		});

		String title = f.getTitle();
		if (title.indexOf("'") != -1) {
		    title = title.replace("'", "\\'");
		}
		sb.append("{type:'Text', label:'" + title + "', id:0");

		if (cArray.length == 0) {
		    sb.append(", expanded:0, children:[{type:'HTML', html:'<i>null</i>', id:0}]}");
		    return sb.toString();

		} else {
		    sb.append(", children:[");
		    sb.append(getChild(cArray[0], cLoader));
		    for (int i = 1; i < cArray.length; i++) {
			sb.append(", ").append(getChild(cArray[i], cLoader));
		    }
		    sb.append("]}");
		}
		return sb.toString();

	    } else {

		if (f.getContentHandler().equals("resource/x-lams-lamscontent")) {
		    String strUrl = f.getUrl();
		    String strId = extractParameterValue(strUrl, "lsid");
		    String strTitle = f.getTitle().replace("'", "\\'");
		    sb.append("{type:'Text', label:'" + strTitle + "', id:'" + strId + "'}");
		    //			return sb.toString();

		} else if (f.getContentHandler().equals("resource/x-ntu-hdllams")) {
		    String strUrl = f.getUrl();
		    String strId = "0";
		    if (strUrl.indexOf("&seq_id=") != -1) {
			int pos1 = strUrl.indexOf("&seq_id=") + 8;
//				   int pos2 = strUrl.indexOf("&", pos1);
			strId = strUrl.substring(pos1);
		    }
		    String strTitle = f.getTitle().replace("'", "\\'");
		    sb.append("{type:'Text', label:'" + strTitle + "', id:'" + strId + "'}");

		} else {
		    //	        sb.append("{type:'HTML', html:'<i>null</i>', id:0}");
		}
		return sb.toString();
	    }

	} catch (Exception e) {
	    return sb.toString();
	}
    }
    
    private void openCourseGradebook(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	
//	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
//	Container bbContainer = bbPm.getContainer();
//	PkId courseId = new PkId(bbContainer, Course.DATA_TYPE, request.getParameter("course_id"));
//	
//	    // find a teacher that will be assigned as lesson's author on LAMS side
//	    User teacher = BlackboardUtil.getCourseTeacher(courseId);
	
	Context ctx = null;
	try {
	    // get Blackboard context
	    ContextManager ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    ctx = ctxMgr.getContext();

	    // construct Login Request URL for author LAMS Lessons (as it's the only possible method value to successfully pass authentication)
	    String courseGradebookUrl = LamsSecurityUtil.generateRequestURL(ctx, "author", null);
	    courseGradebookUrl += "&mode=gradebook";
//		courseGradebookUrl += "&courseid=" + cour;
//		courseGradebookUrl += "&redirectUrl=/gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=4";
//			https://translations.lamsinternational.com/lams/gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=4
	    response.sendRedirect(courseGradebookUrl);
	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	}


    }

    /**
     * Admin on BB side calls this servlet to clone old lesson that were copied to the new course.
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void cloneLessons(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	String _course_id = request.getParameter("course_id");
	if (StringUtil.isEmpty(_course_id)) {
	    throw new RuntimeException("Required parameters are missing. courseId: " + _course_id);
	}

	String newLessonIds = "";
	try {
	    newLessonIds = recreateLessonsAfterCourseCopy(_course_id);
	} catch (IllegalStateException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	}

	//prepare string to write out
	int newLessonsCounts = newLessonIds.length() - newLessonIds.replace(",", "").length();
	String resultStr = "Complete! " + newLessonsCounts + " lessons have been cloned.";
	//add all lessonIds (without the last comma)
	if (newLessonsCounts > 0) {
	    resultStr += " Their updated lessonIds: " + newLessonIds.substring(0, newLessonIds.length() - 2);
	}

	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.write(resultStr);
	out.flush();
	out.close();
    }
    
    /**
     * Recreates lessons after course has been copied. I.e. asks LAMS server to clone old lesson and then updates BB
     * link with the newly created lesson Id.
     * 
     * @param courseIdParam
     *            id of the course that has been copied
     * @return
     * @throws PersistenceException
     * @throws ValidationException
     * @throws IOException
     * @throws ServletException
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    private static String recreateLessonsAfterCourseCopy(String _course_id)
	    throws PersistenceException, ValidationException, ServletException, IOException, ParserConfigurationException, SAXException {
	String newLessonIds = "";

	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	ContentDbPersister persister = ContentDbPersister.Default.getInstance();
	CourseDbLoader courseLoader = CourseDbLoader.Default.getInstance();
	
	PkId courseId = (PkId) bbPm.generateId(Course.DATA_TYPE, _course_id);
	Course course = courseLoader.loadById(courseId);
	String courseIdStr = course.getCourseId();
	
	logger.debug("Starting clonning course lessons (courseId=" + courseId + ").");

	// find a teacher that will be assigned as lesson's author on LAMS side
	User teacher = BlackboardUtil.getCourseTeacher(courseId);

	//find all lessons that should be updated
	List<Content> lamsContents = BlackboardUtil.getLamsLessonsByCourse(courseId, false);
	for (Content content : lamsContents) {

	    String _content_id = content.getId().toExternalString();

	    String url = content.getUrl();
	    String urlLessonId = getParameterValue(url, "lsid");
	    String urlCourseId = getParameterValue(url, "course_id");
	    String urlContentId = getParameterValue(url, "content_id");

	    //in case when both courseId and contentId don't coincide with the ones from URL - means lesson needs to be cloned
	    if (!urlCourseId.equals(_course_id) && !urlContentId.equals(_content_id)) {

		final Long newLessonId = LamsSecurityUtil.cloneLesson(teacher, courseIdStr, urlLessonId);

		// update lesson id
		content.setLinkRef(Long.toString(newLessonId));

		// update URL
		url = replaceParameterValue(url, "lsid", Long.toString(newLessonId));
		url = replaceParameterValue(url, "course_id", _course_id);
		url = replaceParameterValue(url, "content_id", _content_id);
		content.setUrl(url);

		// persist updated content
		persister.persist(content);

		//update lineitem details
		LineitemUtil.updateLineitemLessonId(content, _course_id, newLessonId, teacher.getUserName());

		logger.debug("Lesson (lessonId=" + urlLessonId + ") was successfully cloned to the one (lessonId="
			+ newLessonId + ").");

		newLessonIds += newLessonId + ", ";
	    }

	}

	return newLessonIds;
    }
    
    /**
     * Admin on BB side calls this servlet to import old lesson that were copied to the new course.
     */
    private void importLessons(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String _course_id = request.getParameter("course_id");	
	if (StringUtil.isEmpty(_course_id)) {
	    throw new RuntimeException("Required parameters are missing. courseId: " + _course_id);
	}

	String newLessonIds = "";
	try {
	    BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	    ContentDbPersister persister = ContentDbPersister.Default.getInstance();
	    CourseDbLoader courseLoader = CourseDbLoader.Default.getInstance();

	    PkId courseId = (PkId) bbPm.generateId(Course.DATA_TYPE, _course_id);
	    Course course = courseLoader.loadById(courseId);
	    String courseIdStr = course.getCourseId();

	    logger.debug("Starting importing course lessons (courseId=" + courseId + ").");

	    // find a teacher that will be assigned as lesson's author on LAMS side
	    User teacher = BlackboardUtil.getCourseTeacher(courseId);

	    //find all lessons that should be updated
	    List<Content> lamsContents = BlackboardUtil.getLamsLessonsByCourse(courseId, false);
	    for (Content content : lamsContents) {

		String _content_id = content.getId().toExternalString();

		String url = content.getUrl();
		String urlLessonId = getParameterValue(url, "lsid");
		String urlCourseId = getParameterValue(url, "course_id");
		String urlContentId = getParameterValue(url, "content_id");
		String urlLdId = getParameterValue(url, "ldid");

		//in case when both courseId and contentId don't coincide with the ones from URL - means lesson needs to be imported
		if (!urlCourseId.equals(_course_id) && !urlContentId.equals(_content_id)) {

		    final Long newLdId = LamsSecurityUtil.importLearningDesign(teacher, courseIdStr, urlLessonId,
			    urlLdId);

		    logger.debug("Lesson (lessonId=" + urlLessonId
			    + ") was successfully imported to the one (learningDesignId=" + newLdId + ").");

		    // Start the Lesson in LAMS (via Webservices) and get back the lesson ID
		    String title = content.getTitle();
		    FormattedText descriptionFormatted = content.getBody();
		    String description = URLEncoder.encode(descriptionFormatted.getText(), "UTF-8");
		    final long newLessonId = LamsSecurityUtil.startLesson(teacher, courseIdStr, newLdId, title,
			    description, false, false);

		    // update lesson id
		    content.setLinkRef(Long.toString(newLessonId));

		    // update URL
		    url = replaceParameterValue(url, "ldid", Long.toString(newLdId));
		    url = replaceParameterValue(url, "lsid", Long.toString(newLessonId));
		    url = replaceParameterValue(url, "course_id", _course_id);
		    url = replaceParameterValue(url, "content_id", _content_id);
		    content.setUrl(url);

		    // persist updated content
		    persister.persist(content);

		    //update lineitem details
		    LineitemUtil.updateLineitemLessonId(content, _course_id, newLessonId, teacher.getUserName());

		    logger.debug("Lesson (lessonId=" + urlLessonId + ") was successfully imported to the one (lessonId="
			    + newLessonId + ").");

		    newLessonIds += newLessonId + ", ";
		}
	    }

	} catch (LamsServerException e) {
	    //printing out error cause
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("Failed! " + e.getMessage());
	    out.flush();
	    out.close();
	    return;
	} catch (IllegalStateException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	}
	
	//prepare string to write out
	int newLessonsCounts = newLessonIds.length() - newLessonIds.replace(",", "").length();
	String resultStr = "Complete! " + newLessonsCounts + " lessons have been imported.";
	//add all lessonIds (without the last comma)
	if (newLessonsCounts > 0) {
	    resultStr += " Their updated lessonIds: " + newLessonIds.substring(0, newLessonIds.length()-2);
	}
	logger.debug(resultStr);
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
        out.write(resultStr);
        out.flush();
        out.close();
    }
    
    /**
     * Admin on BB side calls this servlet to correct lineitems that have been screwed up while copying/importing courses.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void correctLineitems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String _course_id = request.getParameter("course_id");	
	if (StringUtil.isEmpty(_course_id)) {
	    throw new RuntimeException("Required parameters are missing. courseId: " + _course_id);
	}

	try {
	    BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	    PkId courseId = (PkId) bbPm.generateId(Course.DATA_TYPE, _course_id);

	    logger.debug("Starting clonning course lessons (courseId=" + courseId + ").");

	    // find a teacher that will be assigned as lesson's author on LAMS side
	    User teacher = BlackboardUtil.getCourseTeacher(courseId);

	    //find all lessons that should be updated
	    List<Content> lamsContents = BlackboardUtil.getLamsLessonsByCourse(courseId, false);
	    for (Content content : lamsContents) {

		// update lesson id
		String lessonId = content.getLinkRef();

		//update lineitem details
		LineitemUtil.updateLineitemLessonId(content, _course_id, Long.parseLong(lessonId),
			teacher.getUserName());
	    }

	} catch (IllegalStateException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (NumberFormatException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	}
	
	//prepare string to write out
	String resultStr = "Complete! All lineiems have been corrected.";
	logger.debug(resultStr);
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
        out.write(resultStr);
        out.flush();
        out.close();
    }
    
    /*
     * Returns param value, and empty string in case of there is no such param available
     * 
     * @param url
     * @param paramName
     * @return
     */
    private static String getParameterValue(String url, String paramName) {
	String paramValue = "";

	int quotationMarkIndex = url.indexOf("?");
	String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	String[] paramEntries = queryPart.split("&");
	for (String paramEntry : paramEntries) {
	    String[] paramEntrySplitted = paramEntry.split("=");
	    if ((paramEntrySplitted.length > 1) && paramName.equalsIgnoreCase(paramEntrySplitted[0])) {
		paramValue = paramEntrySplitted[1];
		break;
	    }
	}

	return paramValue;
    }
    
    private static String replaceParameterValue(String url, String paramName, String newParamValue) {
	String oldParamValue = "";

	int quotationMarkIndex = url.indexOf("?");
	String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	String[] paramEntries = queryPart.split("&");
	for (String paramEntry : paramEntries) {
	    String[] paramEntrySplitted = paramEntry.split("=");
	    if ((paramEntrySplitted.length > 1) && paramName.equalsIgnoreCase(paramEntrySplitted[0])) {
		oldParamValue = paramEntrySplitted[1];

		return url.replaceFirst(paramName + "=" + oldParamValue, paramName + "=" + newParamValue);
	    }
	}

	return url;
    }

}
