<%@ page import="java.util.*,
                java.util.Date,
                java.text.SimpleDateFormat,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
                blackboard.persist.course.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.persist.navigation.CourseTocDbLoader,
                blackboard.db.*,
                blackboard.base.*,
                org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil,
                blackboard.platform.*,
                blackboard.platform.plugin.*"
	errorPage="/error.jsp"
%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">

<%
	String lsid = request.getParameter("lsid");
	String learnerUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + lsid;
	String monitorUrl = LamsSecurityUtil.generateRequestURL(ctx, "monitor") + "&lsid=" + lsid;
	
	String course_idstr = request.getParameter("course_id");	

	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	Id course_id = bbPm.generateId(Course.COURSE_DATA_TYPE, course_idstr);
	User sessionUser = ctx.getUser();
	Id sessionUserId = sessionUser.getId();
	

	//get the membership data to determine the User's Role
	CourseMembership courseMembership = null;
	CourseMembership.Role courseRole = null;
	CourseMembershipDbLoader sessionCourseMembershipLoader =
		(CourseMembershipDbLoader) bbPm.getLoader(CourseMembershipDbLoader.TYPE);
	try 
	{  
		courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, sessionUserId);
		courseRole = courseMembership.getRole();
	} 
	catch (KeyNotFoundException e) 
	{
		// There is no membership record.
		e.printStackTrace();
		
	}
	catch (PersistenceException pe) 
	{
		// There is no membership record.
		pe.printStackTrace();
	}


	String instructorstr="hidden";
	if (courseRole.equals(CourseMembership.Role.INSTRUCTOR)||courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)) 
	{
		// instructor or assistant
		// can choose to redirect to monitor or learner
		instructorstr="button";
	}
	else if (!courseRole.equals(CourseMembership.Role.STUDENT))
	{

		response.sendRedirect("notAllowed.jsp");
	}
%>

<bbUI:docTemplate>
<head>
	<link type="text/css" rel="stylesheet" href="css/bb.css" />
</head>
<script language="JavaScript" type="text/javascript">
		<!--
			var learnerWin = null;
			var monitorWin = null;
			var learnerUrl = null;
			var monitorUrl = null;
			
			function back()
			{
				history.go(-1);
			}
			
			function openLearner()
			{
				learnerUrl = '<%=learnerUrl%>'; 
		    	if(learnerWin && learnerWin.open && !learnerWin.closed){
		    	
		            learnerWin.focus();
		        }
		        else{
		            learnerWin = window.open(learnerUrl,'aWindow','width=800,height=600,resizable');
		            learnerWin.focus();
		        }
			}
			
			function openMonitor()
			{
				monitorUrl = '<%=monitorUrl%>'; 
		    	if(monitorWin && monitorWin.open && !monitorWin.closed){
		    	
		            learnerWin.focus();
		        }
		        else{
		            monitorWin = window.open(monitorUrl,'aWindow','width=800,height=600,resizable');
		            monitorWin.focus();
		        }
			}
		//-->
</script>



<bbUI:breadcrumbBar handle="control_panel" isContent="true" >
    <bbUI:breadcrumb>LAMS Options</bbUI:breadcrumb>
</bbUI:breadcrumbBar>

<bbUI:titleBar iconUrl ="/images/ci/icons/bookopen_u.gif">LAMS Options</bbUI:titleBar>


<form name="workspace_form" id="workspace_form" method="post">
	<br>
	<b>Please Choose an Option</b>
	<br><br>
	&nbsp&nbsp&nbsp&nbsp
	<input type="<%=instructorstr%>" class="button" name="OpenMonitor" onClick="openMonitor();" value="Open Monitor">
	<input type="button" class="button" name="OpenLearner" onClick="openLearner();" value="Open Lesson">
	<input type="button" class="button" name="Cancel" onClick="back();" value="Cancel">

</form>
	
</bbUI:docTemplate>						  
</bbData:context>
