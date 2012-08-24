<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Handle LAMS Lesson Access
    Students - access lesson only
    Staff - additionally access the Lesson Monitor
--%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.data.*"%>
<%@ page import="blackboard.persist.*"%>
<%@ page import="blackboard.data.course.*"%>
<%@ page import="blackboard.data.user.*"%>
<%@ page import="blackboard.persist.course.*"%>
<%@ page import="blackboard.data.content.*"%>
<%@ page import="blackboard.persist.content.*"%>
<%@ page import="blackboard.persist.navigation.CourseTocDbLoader"%>
<%@ page import="blackboard.db.*"%>
<%@ page import="blackboard.base.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page import="blackboard.portal.data.*" %>
<%@ page import="blackboard.portal.servlet.PortalUtil" %>
<%@ page import="blackboard.persist.PersistenceException" %>
<%@ page import="blackboard.persist.gradebook.*" %>
<%@ page import="blackboard.data.gradebook.*" %>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Options" ctxId="ctx">
<%
    // SECURITY!
		// Authorise current user for Course Access (automatic redirect)
		try {
		    if (!PlugInUtil.authorizeForCourse(request, response))
			return;
		} catch (PlugInException e) {
		    throw new RuntimeException(e);
		}

		String internalLessonId = request.getParameter("content_id");
		String courseIdStr = request.getParameter("course_id");
		BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
		Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);

		//get stored internalContentId -> externalContentId. 
		PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
		ExtraInfo ei = pei.getExtraInfo();
		String externalLessonId = ei.getValue(internalLessonId);

		if (externalLessonId == null || "".equals(externalLessonId)) {
		    throw new ServletException("externalLessonId not found in PortalExtraInfo LamsStorage");
		}

		CourseDbLoader cLoader = CourseDbLoader.Default.getInstance();
		LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();

		//search for appropriate lineitem
		Lineitem lineitem = null;
		List<Lineitem> lineitems = lineitemLoader.loadByCourseId(courseId);

		for (Lineitem lineitemIter : lineitems) {
		    if (lineitemIter.getAssessmentId() != null && lineitemIter.getAssessmentId().equals(externalLessonId)) {
				lineitem = lineitemIter;
				break;
		    }
		}

		if (lineitem == null) {
		    throw new ServletException("lineitem not found");
		}

		LineitemDbPersister linePersister = (LineitemDbPersister) bbPm.getPersister(LineitemDbPersister.TYPE);
		linePersister.deleteById(lineitem.getId());
%>

    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="LAMS DELETE"/>
    </bbNG:pageHeader>

        
</bbNG:genericPage>