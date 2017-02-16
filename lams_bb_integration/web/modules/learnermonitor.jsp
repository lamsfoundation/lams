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
<%@ page import="blackboard.platform.persistence.*"%>
<%@ page import="blackboard.portal.servlet.*"%>
<%@ page import="blackboard.portal.data.*"%>
<%@ page import="org.lamsfoundation.ld.integration.util.*"%>
<%@ page import="org.lamsfoundation.ld.integration.dto.LearnerProgressDTO"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Options" ctxId="ctx">

	<bbNG:cssBlock> 
	<style type="text/css"> 
		#progress-area {
			text-align: center; 
			border: #d3d3d3 1px solid;
			margin: 10px;
			padding: 10px 10px 2px;
		}
		.smalltext {
			text-align:right;
			font-size: 0.75em;
		}
		.super {
			position:relative;
			bottom:0.5em;
			color:red;
			font-size:0.8em;
		}
		.progress-header {
			text-align:center;
			font-weight: bold;
			padding-top: 5px; 
			padding-bottom:5px;
		}
		.left-text-align{
			text-align: left;
		}
	</style> 
	</bbNG:cssBlock>
 
<%
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	CourseMembershipDbLoader sessionCourseMembershipLoader = CourseMembershipDbLoader.Default.getInstance();

    // Authorise current user for Course Access (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForCourse(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }

	String strLessonId = request.getParameter("lsid").trim();
	long lessonId = Long.parseLong(strLessonId);
	String courseIdParam = request.getParameter("course_id");  
	
    // Get Course ID and User ID
    Id course_id = bbPm.generateId(Course.DATA_TYPE, courseIdParam);
    Id userId = ctx.getUser().getId();

    // Get the membership data to determine the User's Role
    CourseMembership courseMembership = null;
    CourseMembership.Role courseRole = null;
    boolean isActive = false;

    try {
        courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, userId);
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
    if (CourseMembership.Role.INSTRUCTOR.equals(courseRole) || CourseMembership.Role.TEACHING_ASSISTANT.equals(courseRole)
    	|| CourseMembership.Role.COURSE_BUILDER.equals(courseRole)) {
        instructorstr=true;
        
    } else if (!CourseMembership.Role.STUDENT.equals(courseRole)) {
        // The user is not an Instructor, Teaching Assistant or Student - Access Denied 
        response.sendRedirect("notAllowed.jsp");
    }
    
    // Are they active in the course?  If not let Blackboard handle the redirect
    if (!isActive) {
        PlugInUtil.sendAccessDeniedRedirect(request, response);
    }
    
    //Get lessson's title and description
    String title = "";
    String description = "";
    String strLineitemId = null;
    String position = "unknown";
    //contentId is available in versions after 1.2.3
    String contentIdParam = request.getParameter("content_id");
    if (contentIdParam != null) {
    	
        Container bbContainer = bbPm.getContainer();
        Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, contentIdParam);
        ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader(ContentDbLoader.TYPE);
        Content courseDoc = (Content) courseDocumentLoader.loadById(contentId);
        title = courseDoc.getTitle();
        description = courseDoc.getBody().getFormattedText();
        position = String.valueOf(courseDoc.getPosition());
        
        //get lineitemid from the storage 
	    Lineitem lineitem = LineitemUtil.getLineitem(contentIdParam, userId, strLessonId);
	    
    } else {

    	title = (request.getParameter("title") != null) ? request.getParameter("title") : "LAMS Options";
    	description = request.getParameter("description");
        strLineitemId = request.getParameter("lineitemid");
    }

    //display learning design image
 	String strIsDisplayDesignImage = request.getParameter("isDisplayDesignImage");
 	boolean isDisplayDesignImage = "true".equals(strIsDisplayDesignImage) ? true : false;
 	String learningDesignImageUrl = "";
 	if (isDisplayDesignImage) {
 	    String username = ctx.getUser().getUserName();
 	    learningDesignImageUrl = LamsSecurityUtil.generateRequestLearningDesignImage(username) + "&lsId=" + lessonId;
 	}

 	//check whether user has score for this lesson
 	Score current_score = null;
 	if (strLineitemId != null) { // there won't be "lineitemid" parameter in case lesson had been created in LAMS building block version prior to 1.2 

 	    //check if it was created in old versions (e.g. 1.2.1) where lineitemId parameter had the following format "PkId(key=_XXXX_1, %20dataType=blackboard.data.gradebook.impl.OutcomeDefinition, %20container=blackboard.persist.DatabaseContainer@xxxx)"
 	    //in which case transform it to the more regular one
 		if (strLineitemId.contains("key=")) {
 			int pos1 = strLineitemId.indexOf("key=") + 4;
	 		int pos2 = strLineitemId.indexOf(",", pos1);

 			if (pos1 != -1 && pos2 != -1 && pos1 <= pos2 && pos2 <= strLineitemId.length()) {
 			    strLineitemId = strLineitemId.substring(pos1, pos2);
	 		}
 		}

	    Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE, strLineitemId.trim());
	    ScoreDbLoader scoreLoader = (ScoreDbLoader) bbPm.getLoader(ScoreDbLoader.TYPE);
	    try {
 			current_score = scoreLoader.loadByCourseMembershipIdAndLineitemId(courseMembership.getId(),
 				lineitemId);
	    } catch (KeyNotFoundException c) {
 			//no score availalbe
	    }
	}
    boolean isScoreAvailable = (current_score != null);

    //prepare learnerProgressDto for displaying on jsp
	LearnerProgressDTO learnerProgressDto = LamsSecurityUtil.getLearnerProgress(ctx, lessonId);
%>

    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="<%=title%>"/>
    </bbNG:pageHeader>

    <%-- Action Control Bar --%>
    <bbNG:actionControlBar>
        <bbNG:actionButton id="open_learner" url="javascript:openLearner();" title="Open Lesson" primary="true"/>       <%-- Access the Lesson as a Learner --%>
        <% if(instructorstr) { %>
            <bbNG:actionButton id="open_monitor" url="javascript:openMonitor();" title="Open Monitor" primary="true"/>  <%-- Access the Monitor --%>
        <% } %>
        <bbNG:actionButton id="cancel" url="javascript:back();" title="Cancel" primary="false"/>                        <%-- Cancel (Go Back) --%>
    </bbNG:actionControlBar>
    
	<!-- Position <%=position%> -->

    <% if((description != "") && (description != null)) { %>
	    <div class="vtbegenerated"> 
	    	<%=description%> 
	    </div>
    <% } %>
    
    <% if(isDisplayDesignImage) { %>
    	<div style="text-align: center; margin-top: 10px;">
   			<img src="<%=learningDesignImageUrl%>">
   		</div>
    <% } %>
    
    <% if(learnerProgressDto.getAttemptedActivities() > 0 || learnerProgressDto.getLessonComplete()) { %>
	    <div id="progress-area">
	    	<div class="progress-header">
	    		Your Lesson Progress
	    	</div>
	    	
	    	<% if(!learnerProgressDto.getLessonComplete()) { %>
		    	<p class="left-text-align">
		    		Lesson is not yet completed.
		    	</p>
		    	
		    	<p class="left-text-align">
		    		You have completed: <%=learnerProgressDto.getActivitiesCompleted()%> out of approximately <%=learnerProgressDto.getActivityCount()%> activities 
		    		<span class="super">[*]</span>
		    	</p>
		    	
		    	<div class="smalltext">
		    		<span class="super">*</span>
		    		Total activities depend on your learning path.
		    	</div>
		    	
	    	 <% } else { %>
	    	 	<p>
		    		You have completed this lesson.
		    	</p>
	    	 <% } %>
	    </div>
    <% } %>

    <bbNG:jsBlock>
        <script language="JavaScript" type="text/javascript">
            var learnerWin = null;
            var monitorWin = null;
                
            // Go back one page if the user clicks the Cancel Button
            function back() {
                history.go(-1);
            }
                
            // Open the Lesson as a Learner
            function openLearner() {
            	var learnerUrl = '../openLamsPage?method=openLearner&course_id=<%=request.getParameter("course_id")%>&content_id=<%=request.getParameter("content_id")%>&lsid=<%=request.getParameter("lsid")%>';
                if (learnerWin && !learnerWin.closed) {
                    try {
                        learnerWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                } else {
                    try {
                        learnerWin = window.open(learnerUrl,'lWin','width=1280,height=720,resizable=1,scrollbars=yes');
                        learnerWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
            }
            
            // Open the Lesson Monitor                
            function openMonitor() {
            	var monitorUrl = '../openLamsPage?method=openMonitor&course_id=<%=request.getParameter("course_id")%>&content_id=<%=request.getParameter("content_id")%>&lsid=<%=request.getParameter("lsid")%>';
                if(monitorWin && !monitorWin.closed) {
                    try {
                        monitorWin.focus();
                    } catch(e) {
                            // popups blocked by a 3rd party
                            alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                } else {
                    try {
                        monitorWin = window.open(monitorUrl,'aWin','width=1280,height=720,resizable=1,scrollbars=yes');
                        monitorWin.opener = self;
                        monitorWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");    
                    }
                }
            }
           
        </script>
    </bbNG:jsBlock>
        
</bbNG:genericPage>
