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
<%@ page import="blackboard.persist.gradebook.*"%>
<%@ page import="blackboard.data.gradebook.*"%>
<%@ page import="blackboard.db.*"%>
<%@ page import="blackboard.base.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Options" ctxId="ctx">
<%
    // SECURITY!
    // Authorise current user for Course Access (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForCourse(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }
    
    // Get the LAMS access URLs
    String lsid = request.getParameter("lsid");
    String learnerUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + lsid;
    String monitorUrl = LamsSecurityUtil.generateRequestURL(ctx, "monitor") + "&lsid=" + lsid;
    String liveEditUrl = LamsSecurityUtil.generateRequestURL(ctx, "author");
	
    // Get Course ID and Session User ID
    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
    String course_idstr = request.getParameter("course_id");    
    Id course_id = bbPm.generateId(Course.DATA_TYPE, course_idstr);
    User sessionUser = ctx.getUser();
    Id sessionUserId = sessionUser.getId();

    // Get the membership data to determine the User's Role
    CourseMembership courseMembership = null;
    CourseMembership.Role courseRole = null;
    boolean isActive = false;
    
    CourseMembershipDbLoader sessionCourseMembershipLoader = (CourseMembershipDbLoader) bbPm.getLoader(CourseMembershipDbLoader.TYPE);
    try {  
        courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, sessionUserId);
        courseRole = courseMembership.getRole();
        isActive = courseMembership.getIsAvailable();
    } catch (KeyNotFoundException e) {
        // There is no membership record.
        e.printStackTrace();
    } catch (PersistenceException pe) {
        // There is no membership record.
        pe.printStackTrace();
    }

    // Is the User an Instructor of Teaching Assistant
    boolean instructorstr=false;
    if (courseRole.equals(CourseMembership.Role.INSTRUCTOR)||courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)) {
        instructorstr=true;
    } else if (!courseRole.equals(CourseMembership.Role.STUDENT)) {
        // The user is not an Instructor, Teaching Assistant or Student - Access Denied 
        response.sendRedirect("notAllowed.jsp");
    }
    
    // Are they active in the course?  If not let Blackboard handle the redirect
    if (!isActive) {
        PlugInUtil.sendAccessDeniedRedirect(request, response);
    }
    
    String strIsDisplayDesignImage = request.getParameter("isDisplayDesignImage");
    boolean isDisplayDesignImage = "true".equals(strIsDisplayDesignImage)?true:false;
    
    String learningDesignImageUrl = "";
    if (isDisplayDesignImage) {
		String strLearningDesignId = request.getParameter("ldid").trim();
	    long learningDesignId = Long.parseLong(strLearningDesignId);
	    
	    learningDesignImageUrl = LamsSecurityUtil.generateRequestLearningDesignImage(ctx, false) + "&ldId=" + learningDesignId;
    }
    
    //check whether user has score for this lesson
    Score current_score = null;
    String strLineitemId = request.getParameter("lineitemid");
	if (strLineitemId != null) { // there won't be "lineitemid" parameter in case lesson had been created in LAMS building block version prior to 1.2 
	    
	    Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE, strLineitemId.trim());
	    ScoreDbLoader scoreLoader = (ScoreDbLoader) bbPm.getLoader(ScoreDbLoader.TYPE);
		try {
		    current_score = scoreLoader.loadByCourseMembershipIdAndLineitemId(courseMembership.getId(), lineitemId);
		} catch (KeyNotFoundException c) {
		    //no score availalbe
		}
	}

    boolean isScoreAvailable = (current_score != null);
%>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="true">
        <bbNG:breadcrumb title="LAMS Options" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="LAMS Options"/>
    </bbNG:pageHeader>

    <%-- Action Control Bar --%>
    <bbNG:actionControlBar>
        <bbNG:actionButton id="open_learner" url="javascript:openLearner();" title="Open Lesson" primary="true"/>       <%-- Access the Lesson as a Learner --%>
        <% if(instructorstr) { %>
            <bbNG:actionButton id="open_monitor" url="javascript:openMonitor();" title="Open Monitor" primary="true"/>  <%-- Access the Monitor --%>
        <% } %>
        <bbNG:actionButton id="cancel" url="javascript:back();" title="Cancel" primary="false"/>                        <%-- Cancel (Go Back) --%>
    </bbNG:actionControlBar>
    
    <% if(request.getParameter("title") != null) { %>
	    <h2>
	    	<%=request.getParameter("title")%>
	    </h2>
    <% } %>
    
    <% if(request.getParameter("description") != null) { %>
	    <h4> 
	    	<%=request.getParameter("description")%> 
	    </h4>
    <% } %>
    
    <% if(isDisplayDesignImage) { %>
    	<div style="text-align: center; margin-top: 10px;">
   			<img src="<%=learningDesignImageUrl%>">
   		</div>
    <% } %>
     
    <% if(isScoreAvailable) { %>
    	<div style="text-align: center; margin-top: 10px;">
   			You have completed this lesson.
   		</div>
    <% } %>
    

    <bbNG:jsBlock>
        <script language="JavaScript" type="text/javascript">
        <!--
            var learnerWin = null;
            var monitorWin = null;
            var liveEditUrl= null;
            var learnerUrl = null;
            var monitorUrl = null;
                
            // Go back one page if the user clicks the Cancel Button
            function back() {
                history.go(-1);
            }
                
            // Open the Lesson as a Learner
            function openLearner() {
                learnerUrl = '<%=learnerUrl%>'; 
                if(learnerWin && learnerWin.open && !learnerWin.closed){
                    try {
                        learnerWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                } else {
                    try {
                        learnerWin = window.open(learnerUrl,'lWin','width=1024,height=768,resizable=1');
                        learnerWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
            }
            
            // Open the Lesson Monitor                
            function openMonitor() {
                monitorUrl = '<%=monitorUrl%>'; 
                if(monitorWin && monitorWin.open && !monitorWin.closed){
                    try {
                        monitorWin.focus();
                    } catch(e) {
                            // popups blocked by a 3rd party
                            alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                } else {
                    try {
                        monitorWin = window.open(monitorUrl,'aWin','width=800,height=600,resizable=1');
                        monitorWin.opener = self;
                        monitorWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");    
                    }
                }
            }
           
        //-->
        </script>
    </bbNG:jsBlock>
        
</bbNG:genericPage>