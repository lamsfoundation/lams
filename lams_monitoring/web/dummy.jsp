<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToShare = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../";

%>

<html>
  <head>
	  <link href="<%=pathToShare%>/css/default.css" rel="stylesheet" type="text/css"/>

<script language="JavaScript" type="text/JavaScript">
<!--
	function launchPopup(url) {
		var popup = window.open(url,'');
		popup.window.focus();
	}
//-->
</script>
	
<title>Lesson Management</title>
  <body>
    <H1>Lesson Management</H1>
    
    <p>This is a dummy page for Lesson Management. It is to be used until the 
    Flash interface is ready. It pull the urls from the database so if the 
    urls are invalid, you need to check the tool deployment scripts.</p>

	<H2>Start Lesson</H2>
	
	<P>Before you can start a lesson, you will need to create a learning design.
	At the moment, the only way to do this is to go into the authoring client
	and save a learning design containing your tool. Get the id of the learning 
	design from the database. Once that is done, you can start a lesson
	using the learning design id.
	
	<html:form action="/dummy" target="_self" method="POST">
	
	<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
		<tr><td class="formlabel">learningDesignId</td>
		<td class="formcontrol"><html:text property="learningDesignId" maxlength="4"  size="4"/></td></tr>
		<tr><td class="formlabel">title</td>
		<td class="formcontrol"><html:text property="title" maxlength="80"  size="80"/></td></tr>
		<tr><td class="formlabel">desc</td>
		<td class="formcontrol"><html:text property="desc" maxlength="80"  size="80"/></td></tr>
		<tr><td class="formlabel" colspan="2">
			<html:submit property="method" value="startLesson" styleClass="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" >
				Start Lesson 
			</html:submit>
		</td></tr>
	</table>
	
	</html:form>
	
	<H2>Monitor Lessons</H2>
	<p>All the links below should pop-up in new windows.</p>
	<c:forEach var="lesson" items="${lessons}">
	<TABLE border="1" summary="This table is being used for layout purposes only">
		<TR>
			<TD class="formlabel">lessonId</TD>
			<TD class="formcontrol"><c:out value="${lesson.lessonId}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">lessonName</TD>
			<TD class="formcontrol"><c:out value="${lesson.lessonName}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">lessonDescription</TD>
			<TD class="formcontrol"><c:out value="${lesson.lessonDescription}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">learningDesignId</TD>
			<TD class="formcontrol"><c:out value="${lesson.learningDesign.learningDesignId}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">createDateTime</TD>
			<TD class="formcontrol"><c:out value="${lesson.createDateTime}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">startDateTime</TD>
			<TD class="formcontrol"><c:out value="${lesson.startDateTime}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">endDateTime</TD>
			<TD class="formcontrol"><c:out value="${lesson.endDateTime}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">lessonStateId</TD>
			<TD class="formcontrol"><c:out value="${lesson.lessonStateId}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">Pretend to be learner:</TD>
			<TD class="formcontrol"><A HREF=javascript:launchPopup('/lams/learning/learner.do?method=joinLesson&userId=<lams:user property="userID"/>&lessonId=<c:out value="${lesson.lessonId}"/>');>Join Lesson as <lams:user property="login"/></a>
		</TR>
		<TR>
			<TD class="formlabel">Define Later &amp; Monitor</TD>
			<TD class="formcontrol">
			<c:forEach var="activity" items="${lesson.learningDesign.activities}">
				Activity <c:out value="${activity.activityId}"/> <c:out value="${activity.title}"/><BR>
				 <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoMonitoringActivityURL&activityID=<c:out value="${activity.activityId}"/>');">
				 Monitor</A> &nbsp;&nbsp;
				 <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoDefineLaterActivityURL&activityID=<c:out value="${activity.activityId}"/>');">
				 Define Later</A><BR>
			</c:forEach>
			</TD>
		</TR>
		<TR>
			<TD class="formlabel">Export Portfolio:</TD>
			<TD class="formcontrol"><A HREF=javascript:launchPopup('/lams/learning/exportWaitingPage.jsp?mode=teacher&lessonID=<c:out value="${lesson.lessonId}"/>');>Export Portfolio for all users</a><BR>
			<A HREF=javascript:launchPopup('/lams/learning/exportWaitingPage.jsp?mode=learner&lessonID=<c:out value="${lesson.lessonId}"/>');>Export Portfolio as <lams:user property="login"/></a>
		</TR>
		<c:forEach var="progress" items="${lesson.learnerProgresses}">
		<TR>
			<TD class="formlabel">User Progress:</TD>
			<TD>Login: <STRONG><c:out value="${progress.user.login}"/></STRONG><BR///>
			<c:if test="${!empty progress.currentActivity}">
					Current Activity <c:out value="${progress.currentActivity.title}"/> <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoLearnerActivityURL&userID=<c:out value="${progress.user.userId}"/>&activityID=<c:out value="${progress.currentActivity.activityId}"/>');">View</A> 
				</c:if><BR>
			Completed Activities:<BR>
			<c:forEach var="completed" items="${progress.completedActivities}">
				<A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoLearnerActivityURL&userID=<c:out value="${progress.user.userId}"/>&activityID=<c:out value="${completed.activityId}"/>');">View</A> <c:out value="${completed.title}"/><BR>
			</c:forEach>
			</TD>
		</TR>
		</c:forEach>
		<TR>
		</TR>
		<TR><TD colspan="4"></TD></TR>
	</TABLE>
	<HR>
	</c:forEach>
	</body>
</html>
