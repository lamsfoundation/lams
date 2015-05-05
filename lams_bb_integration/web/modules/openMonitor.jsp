<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
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
<%@ page import="blackboard.persist.gradebook.*"%>
<%@ page import="blackboard.data.gradebook.*"%>
<%@ page import="blackboard.db.*"%>
<%@ page import="blackboard.base.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page import="blackboard.portal.servlet.*"%>
<%@ page import="blackboard.portal.data.*"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="Open Author" ctxId="ctx">

<%
	// Authorise current user for Course Control Panel (automatic redirect)
	try{
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
	        return;
	} catch(PlugInException e) {
	    throw new RuntimeException(e);
	}

	// Get Course ID and Session User ID
	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	String course_idstr = request.getParameter("course_id");    
	Id course_id = bbPm.generateId(Course.DATA_TYPE, course_idstr);
	User sessionUser = ctx.getUser();
	Id sessionUserId = sessionUser.getId();
	// Get the membership data to determine the User's Role
	CourseMembership courseMembership = null;
	CourseMembership.Role courseRole = null;
	CourseMembershipDbLoader sessionCourseMembershipLoader = (CourseMembershipDbLoader) bbPm.getLoader(CourseMembershipDbLoader.TYPE);
	try {  
	    courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, sessionUserId);
	    courseRole = courseMembership.getRole();
	} catch (KeyNotFoundException e) {
	    // There is no membership record.
	    e.printStackTrace();
	} catch (PersistenceException pe) {
	    // There is no membership record.
	    pe.printStackTrace();
	}
	
	// if the user is not an Instructor or Teaching Assistant - Access Denied 
	if (!(courseRole.equals(CourseMembership.Role.INSTRUCTOR) || courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)
		|| courseRole.equals(CourseMembership.Role.COURSE_BUILDER))) {
	    response.sendRedirect("notAllowed.jsp");
	}

	// Get the Login Request URL for monitoring LAMS Lessons
	String lsid = request.getParameter("lsid");
	String monitorUrl = LamsSecurityUtil.generateRequestURL(ctx, "monitor", lsid);
		
    response.sendRedirect(monitorUrl);
%>

</bbNG:genericPage>
