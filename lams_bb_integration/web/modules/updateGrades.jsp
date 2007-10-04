<%@ page import="java.util.*,
                java.text.*,
                blackboard.data.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
                blackboard.persist.*,
                blackboard.persist.course.*,
                blackboard.persist.user.*,
				blackboard.persist.gradebook.*,
				blackboard.data.gradebook.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.log.*,
                blackboard.platform.session.*,
                blackboard.platform.persistence.*,                              
                blackboard.platform.BbServiceManager.*,                
                blackboard.platform.context.*,
         		blackboard.base.BbList.*,
                blackboard.platform.plugin.PlugInUtil,
				java.util.Calendar,
				org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil,
				blackboard.platform.security.*"
        errorPage="/error.jsp"
 %>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>

<bbData:context id="ctx">

<%
	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	UserDbLoader uLoader = UserDbLoader.Default.getInstance();
	CourseDbLoader cLoader = CourseDbLoader.Default.getInstance();
	CourseMembershipDbLoader memLoader =(CourseMembershipDbLoader) bbPm.getLoader(CourseMembershipDbLoader.TYPE);
	ScoreDbLoader scoreLoader = (ScoreDbLoader) bbPm.getLoader(ScoreDbLoader.TYPE);
	ScoreDbPersister scorePersister = (ScoreDbPersister) bbPm.getPersister(ScoreDbPersister.TYPE);
	String courseId = request.getParameter("course_id");
	String lineitemId = request.getParameter("lineitem_id");
	
	
%>


<bbUI:docTemplate>
<head>

</head>

<bbUI:breadcrumbBar handle="control_panel" isContent="true" >
    <bbUI:breadcrumb>LAMS Update Grades</bbUI:breadcrumb>
</bbUI:breadcrumbBar>
<bbUI:titleBar iconUrl ="/images/ci/icons/bookopen_u.gif">LAMS Update Grades</bbUI:titleBar>

LAMS user progress data successfully updated in GradeBook

</bbUI:docTemplate>	
</bbData:context>


