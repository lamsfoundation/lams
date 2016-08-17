<%@ page import="java.util.*, java.net.*,
                java.text.SimpleDateFormat,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
				blackboard.data.navigation.*,
                blackboard.persist.course.*,
				blackboard.persist.navigation.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.db.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.plugin.*,
                org.lamsfoundation.ld.integration.*,
				org.lamsfoundation.ld.integration.blackboard.*" errorPage="error.jsp" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">

<%
    //check permission
    if (!PlugInUtil.authorizeForCourseControlPanel(request, response)) {
        return;
    }
%>
<bbNG:learningSystemPage contextOverride="CTRL_PANEL">
	<bbNG:cssBlock> 
	<style type="text/css">
		#buttons {
			float:right;
		}
	</style> 
	</bbNG:cssBlock>

	<bbNG:jsFile href="includes/javascript/jquery.js" />
	<bbNG:jsFile href="includes/javascript/jquery.blockUI.js" />
	
	<bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="false">
		<bbNG:breadcrumb>LAMS Admin</bbNG:breadcrumb>
	</bbNG:breadcrumbBar>
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="LAMS Admin" />
	</bbNG:pageHeader>

	<%-- Monitor Button --%>
	<div id="buttons">
		<button id="clone-lessons-button" onclick="javascript:cloneLessons(); return false;">
			Update LAMS links after course copy
		</button>
		<br>
		
		<button id="import-lessons-button" onclick="javascript:importLessons(); return false;">
			Update LAMS links after course import
		</button>
		<br>
		
		<button id="import-lessons-button" onclick="javascript:correctLineitems(); return false;">
			Correct Grade center columns
		</button>
	</div>
	
	<div id="treeDiv"></div>

<bbNG:jsBlock>
	<script type="text/javascript">
    
	    var $j = jQuery.noConflict();
	    
	    // Open the LAMS Seuence Monitor Window
	    function cloneLessons() {
	    	//block #buttons
		    blockButtons('<h1 style="color:#fff";>Please, wait. Lessons are getting copied now.</h1>');
	    	
	        $j.ajax({
	        	async: true,
	            url: '../CloneLessons',
	            data : 'courseId=<%=ctx.getCourse().getCourseId()%>',
	            type: 'post',
	            success: function (response) {
	            	$j("#buttons").unblock();
	            	alert(response);
	            },
	            error: function (request, status, error) {
	            	$j("#buttons").unblock();
	                //alert(request.responseText);
	               // alert(request.status);
	                alert(error);
	            }
	       	});
	        
	        return false;
	    }

	    function importLessons() {
	    	//block #buttons
		    blockButtons('<h1 style="color:#fff";>Please, wait. Lessons are getting copied now.</h1>');
	    	
	        $j.ajax({
	        	async: true,
	            url: '../ImportLessons',
	            data : 'courseId=<%=ctx.getCourse().getCourseId()%>',
	            type: 'post',
	            success: function (response) {
	            	$j("#buttons").unblock();
	            	alert(response);
	            },
	            error: function (request, status, error) {
	            	$j("#buttons").unblock();
	                alert(error);
	            }
	       	});
	        
	        return false;		    
		}

	    function correctLineitems() {
	    	//block #buttons
		    blockButtons('<h1 style="color:#fff";>Please wait while Grade center columns are getting fixed.</h1>');
	    	
	        $j.ajax({
	        	async: true,
	            url: '../CorrectLineitems',
	            data : 'courseId=<%=ctx.getCourse().getCourseId()%>',
	            type: 'post',
	            success: function (response) {
	            	$j("#buttons").unblock();
	            	alert(response);
	            },
	            error: function (request, status, error) {
	            	$j("#buttons").unblock();
	                alert(error);
	            }
	       	});
	        
	        return false;		    
		}

	    //auxiliary method to block #buttons element
		function blockButtons(message){

	    	$j('#buttons').block({
	    		message: message,
	    		baseZ: 1000000,
	    		fadeIn:  0,
	    		css: {
	    			border: 'none',
	    		    padding: $j('#buttons').height() + 'px', 
	    		    backgroundColor: '#000', 
	    		    '-webkit-border-radius': '10px', 
	    		    '-moz-border-radius': '10px', 
	    		    opacity: .98 
	    		},
	    		overlayCSS: {
	    			opacity: 0
	    		}
	    	});
		}

	</script>
</bbNG:jsBlock>

</bbNG:learningSystemPage>
</bbData:context>