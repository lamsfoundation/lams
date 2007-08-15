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

	boolean instructor=false;
	boolean student=false;
	
	if (courseRole.equals(CourseMembership.Role.INSTRUCTOR)||courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)) 
	{
		// instructor or assistant
		// can choose to redirect to monitor or learner
		instructor=true;
	}
	else if (courseRole.equals(CourseMembership.Role.STUDENT))
	{
		// student
		// can only access learner pages
		student=true;
	}
	else 
	{
		response.sendRedirect("notAllowed.jsp");
	}
	
%>

<bbUI:docTemplate>

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

<script language="JavaScript" type="text/javascript">
	 <!--
	 if (true) 
	 {
	 	openLearner();
	 	back();
	 }	
	 //-->
</script>

<form name="workspace_form" id="workspace_form" method="post">
	<input type="button" name="OpenMonitor" onClick="openMonitor();" value="Open Monitor">
	<input type="button" name="OpenLearner" onClick="openLearner();" value="Open Learner">
	<input type="button" name="Cancel" onClick="back();" value="Cancel">
<form>
	
	
	

</bbUI:docTemplate>						  
</bbData:context>

<%!
static public String getRoleString(String type, Object role) {
	// return a User Friendly String for the type Role passed in
	String uRole = "";
	if ( type.equals( "COURSE" ) ) {
		// get role based on coursemembershipRole (CourseMembership.Role)
		if( (CourseMembership.Role)role == CourseMembership.Role.COURSE_BUILDER ) {
			uRole="Course Builder";
		} else if ( (CourseMembership.Role)role == CourseMembership.Role.DEFAULT ){
			uRole="Student(Default)";
		} else if ( (CourseMembership.Role)role == CourseMembership.Role.GRADER ) {
			uRole="Grader";
		} else if ( (CourseMembership.Role)role == CourseMembership.Role.GUEST ) {
			uRole="Guest";
		} else if ( (CourseMembership.Role)role
					== CourseMembership.Role.INSTRUCTOR ) {
			uRole="Instructor";
		} else if ( (CourseMembership.Role)role == CourseMembership.Role.NONE) {
			uRole="No explicit role (NONE)";
		} else if ( (CourseMembership.Role)role == CourseMembership.Role.STUDENT) {
			uRole="Student";
		} else if ( (CourseMembership.Role)role 
				== CourseMembership.Role.TEACHING_ASSISTANT ) {
			uRole="Teaching Assistant";
		} else {
			uRole = "Cannot Identify Course Membership Role";
		}
	} else if ( type.equals( "SYSTEM" ) ) {
	    // get role based on SystemRole
		if( (User.SystemRole)role == User.SystemRole.ACCOUNT_ADMIN ) {
			uRole="Account Administrator";
		} else if ( (User.SystemRole)role == User.SystemRole.COURSE_CREATOR ) {
         		uRole = "Course creator";
        	} else if ( (User.SystemRole)role == User.SystemRole.COURSE_SUPPORT ) {
          		uRole = "Course support";
        	} else if ( (User.SystemRole)role == User.SystemRole.DEFAULT ) {
        		uRole = "User";
        	} else if ( (User.SystemRole)role == User.SystemRole.GUEST ) {
        		uRole = "Guest";
        	} else if ( (User.SystemRole)role == User.SystemRole.NONE ) {
        		uRole = "No explicit role (NONE)";
        	} else if ( (User.SystemRole)role == User.SystemRole.OBSERVER ) {
        		uRole = "Observer";
        	} else if ( (User.SystemRole)role == User.SystemRole.SYSTEM_ADMIN ) {
        		uRole = "System Administrator";
        	} else if ( (User.SystemRole)role == User.SystemRole.SYSTEM_SUPPORT ) {
        		uRole = "System support";
        	} else if ( (User.SystemRole)role == User.SystemRole.USER ) {
        		uRole = "User";
        	} else {
        		uRole = "Cannot Identify User System Role";
        	}
	} else {
		uRole = "TYPE not qualified in method.";
	}
	
	return uRole;
}

%>
