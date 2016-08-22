<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 2 For Creating a New LAMS Lesson
    Process the Lesson Information and add it to the Blackboard site

    Step 1 - create.jsp
    Step 2 - start_lesson_proc.jsp
--%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.base.FormattedText"%>
<%@ page import="blackboard.data.course.Course"%>
<%@ page import="blackboard.data.content.Content"%>
<%@ page import="blackboard.data.content.ContentFile"%>
<%@ page import="blackboard.data.content.ContentFolder"%>
<%@ page import="blackboard.data.content.CourseDocument"%>
<%@ page import="blackboard.data.user.User"%>
<%@ page import="blackboard.persist.*"%>
<%@ page import="blackboard.persist.content.*"%>
<%@ page import="blackboard.data.gradebook.impl.*"%>
<%@ page import="blackboard.platform.session.*"%>
<%@ page import="blackboard.persist.gradebook.ext.*"%>
<%@ page import="blackboard.persist.gradebook.impl.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.persistence.*"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="blackboard.platform.context.Context"%>
<%@ page import="blackboard.data.gradebook.Lineitem" %>
<%@ page import="blackboard.persist.gradebook.LineitemDbPersister" %>
<%@ page import="org.lamsfoundation.ld.integration.Constants" %>
<%@ page import="org.lamsfoundation.ld.integration.util.*"%>
<%@ page import="blackboard.portal.data.*" %>
<%@ page import="blackboard.data.content.ExternalLink" %>
<%@ page import="blackboard.portal.servlet.PortalUtil" %>
<%@ page import="blackboard.persist.PersistenceException" %>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Learning Activity Management System" ctxId="ctx">

    <%
    	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
    
    	//Set the new LAMS Lesson Content Object
    	CourseDocument bbContent = new blackboard.data.content.CourseDocument();
    
        // Authorise current user for Course Control Panel (automatic redirect)
        try{
            if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
                return;
        } catch(PlugInException e) {
            throw new RuntimeException(e);
        }
        
        User user = ctx.getUser();
        BlackboardUtil.storeBlackboardContent(request, response, user);
		
		String courseIdStr = request.getParameter("course_id");
		String contentIdStr = request.getParameter("content_id");
		// Internal Blackboard IDs for the course and parent content item
		Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);
		Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE, contentIdStr);
		
        String strReturnUrl = PlugInUtil.getEditableContentReturnURL(folderId, courseId);
	%>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Start A LAMS Lesson" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>
        <bbNG:pageTitleBar title="Start A LAMS Lesson"/>
    </bbNG:pageHeader>

    <%-- Receipt --%>
    <bbNG:receipt type="SUCCESS"
        iconUrl="/images/ci/icons/receiptsuccess_u.gif"
        title="Start A LAMS Lesson"
        recallUrl="<%=strReturnUrl%>">
        	<!-- LESSON START SUCCESS (DO NOT REMOVE: String is used to check for completion) --> 
            Content successfully added.
    </bbNG:receipt>

</bbNG:genericPage>
