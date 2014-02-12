<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 3 For Creating a New LAMS Lesson
    Process the Lesson Information and add it to the Blackboard site

    Step 1 - create.jsp
    Step 2 - start_lesson.jsp
    Step 3 - start_lesson_proc.jsp
--%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.base.FormattedText"%>
<%@ page import="blackboard.data.course.Course"%>
<%@ page import="blackboard.data.content.Content"%>
<%@ page import="blackboard.data.content.ContentFile"%>
<%@ page import="blackboard.data.content.ContentFolder"%>
<%@ page import="blackboard.data.content.CourseDocument"%>
<%@ page import="blackboard.persist.Id"%>
<%@ page import="blackboard.persist.BbPersistenceManager"%>
<%@ page import="blackboard.persist.content.ContentDbPersister"%>
<%@ page import="blackboard.persist.content.ContentDbLoader"%>
<%@ page import="blackboard.platform.session.BbSessionManagerService"%>
<%@ page import="blackboard.platform.session.BbSession"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="blackboard.data.gradebook.Lineitem" %>
<%@ page import="blackboard.persist.gradebook.LineitemDbPersister" %>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>    
<%@ page import="org.lamsfoundation.ld.integration.Constants" %> 
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Learning Activity Management System" ctxId="ctx">

    <%
        // Authorise current user for Course Control Panel (automatic redirect)
        try{
            if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
                return;
        } catch(PlugInException e) {
            throw new RuntimeException(e);
        }
    
        // Get the form parameters and convert into correct data types
        String strTitle = request.getParameter("title").trim();
        String strDescription = request.getParameter("description").trim();
        FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
        
        String strLdId = request.getParameter("ldId").trim();
        long ldId = Long.parseLong(strLdId);               
        
        // Start the Lesson for preview in LAMS (via Webservices)
        // Capture the lesson ID
        Long lsId = null;
        try{  	
            lsId = LamsSecurityUtil.startLesson(ctx, ldId, strTitle, strDescription, true);
            //error checking
            if (lsId == -1) {
                response.sendRedirect("lamsServerDown.jsp");
                System.exit(1);
            }
        } catch (Exception e){
            throw new ServletException(e.getMessage(), e);
        }
        
        //redirect to preview lesson
        String previewUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + lsId;
        response.sendRedirect(previewUrl);
	%>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Preview A LAMS Sequence" />
    </bbNG:breadcrumbBar>
	
    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Preview A LAMS Sequence"/>
    </bbNG:pageHeader>
        
</bbNG:genericPage>