<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>
<jsp:include page="/LearnerMonitor" />

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
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>

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
	
	<bbNG:jsFile href="includes/javascript/openLamsPage.js" />
	
<%
    // Authorise current user for Course Access (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForCourse(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }
%>
    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="${title}"/>
    </bbNG:pageHeader>

    <%-- Action Control Bar --%>
    <bbNG:actionControlBar>
        <bbNG:actionButton id="open_learner" url="javascript:openLearner();" title="Open Lesson" primary="true"/>       <%-- Access the Lesson as a Learner --%>
        <c:if test="${isInstructor}">																				 <%-- Access the Monitor --%>
            <bbNG:actionButton id="open_monitor" url="javascript:openMonitor('${param.course_id}', '${param.lsid}');" title="Open Monitor" primary="true"/>  
        </c:if>
        <bbNG:actionButton id="cancel" title="Cancel" primary="false"                       							 
        		url="javascript:document.location='/webapps/blackboard/content/listContentEditable.jsp?content_id=${param.content_id}&course_id=${param.course_id}';"/><%-- Cancel (Go Back) --%> 
    </bbNG:actionControlBar>

	<c:if test='${description != null && description != ""}'>
	    <div class="vtbegenerated"> 
	    		${description} 
	    </div>
    </c:if>
    
    <c:if test="${isDisplayDesignImage}">
    		<div style="text-align: center; margin-top: 10px;">
   			<img src="${learningDesignImageUrl}">
   		</div>
    </c:if>
    
    <c:if test="${attemptedActivitiesCount > 0 || isLessonCompleted}">
	    <div id="progress-area">
	    	<div class="progress-header">
	    		Your Lesson Progress
	    	</div>
	    	
	    	<c:choose>
	    	<c:when test="${!isLessonCompleted}">
		    	<p class="left-text-align">
		    		Lesson is not yet completed.
		    	</p>
		    	
		    	<p class="left-text-align">
		    		You have completed: ${activitiesCompletedCount} out of approximately ${activitiesCount} activities 
		    		<span class="super">[*]</span>
		    	</p>
		    	
		    	<div class="smalltext">
		    		<span class="super">*</span>
		    		Total activities depend on your learning path.
		    	</div>
		</c:when>
		<c:otherwise>
	    	 	<p>
		    		You have completed this lesson.
		    	</p>
		</c:otherwise>
	    	</c:choose>
	    </div>
    </c:if>

    <bbNG:jsBlock>
        <script type="text/javascript">
            var learnerWin = null;
            var monitorWin = null;
                
            // Open the Lesson as a Learner
            function openLearner() {
            	var learnerUrl = '../openLamsPage?method=openLearner&course_id=${param.course_id}&content_id=${param.content_id}&lsid=${param.lsid}';
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
           
        </script>
    </bbNG:jsBlock>
        
</bbNG:genericPage>
