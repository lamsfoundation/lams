<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
	  <lams:css/>

<script language="JavaScript" type="text/JavaScript">
<!--
	function launchPopup(url) {
		var popup = window.open(url,'');
		popup.window.focus();
	}
//-->
</script>
	
<title>Lesson Management</title>
</head>
  <body>
    <H1>Lesson Management</H1>
    
	<H2>Lesson Details: <c:out value="${lesson.lessonName}"/></H2>
	<TABLE border="0" summary="This table is being used for layout purposes only">
		<TR>
			<TD class="formlabel">Lesson Id:</TD>
			<TD class="formcontrol" colspan="2"><c:out value="${lesson.lessonId}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">Description:</TD>
			<TD class="formcontrol" colspan="2"><c:out value="${lesson.lessonDescription}"/></TD>
		</TR>
		<TR>
			<TD class="formlabel">State:</TD>
			<TD class="formcontrol" colspan="2">
			<c:choose>
				<c:when test="${lesson.lessonStateId==1}">Created</c:when>
				<c:when test="${lesson.lessonStateId==2}">Scheduled</c:when>
				<c:when test="${lesson.lessonStateId==3}">Started</c:when>
				<c:when test="${lesson.lessonStateId==4}">Suspended</c:when>
				<c:when test="${lesson.lessonStateId==5}">Finished</c:when>
				<c:when test="${lesson.lessonStateId==6}">Archived</c:when>
			</c:choose>
			</TD>
		</TR>
	</table>

	<H2>Monitoring</H2>
	<p>All the links below should pop-up in new windows.</p>
	<TABLE border="0" summary="This table is being used for layout purposes only">
		<TR>
			<TD class="formcontrol" colspan="3"><A HREF=javascript:launchPopup('/lams/learning/exportWaitingPage.jsp?mode=teacher&lessonID=<c:out value="${lesson.lessonId}"/>');>Export Portfolio for all users</a></TD>
		</TR>
		<TR>
			<TD class="formcontrol" colspan="3"><A HREF=javascript:launchPopup('/lams/learning/exportWaitingPage.jsp?mode=learner&lessonID=<c:out value="${lesson.lessonId}"/>');>Export Portfolio as <lams:user property="login"/></a>
		</TR>
		
		<TR>
			<TD class="formcontrol" colspan="3"><A HREF=javascript:launchPopup('/lams/learning/learner.do?method=joinLesson&userId=<lams:user property="userID"/>&lessonId=<c:out value="${lesson.lessonId}"/>');>Join Lesson as <lams:user property="login"/></a>
		</TR>
		<TR>
			<TD class="formcontrol" colspan="3">&nbsp;
		</TR>

		<c:forEach var="activity" items="${lesson.learningDesign.activities}">
		<TR>
			<TD class="formcontrol">
				Activity <c:out value="${activity.activityId}"/> <c:out value="${activity.title}"/>:
			</TD>
			<TD class="formcontrol">
				 <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoMonitoringActivityURL&activityID=<c:out value="${activity.activityId}"/>');">
				 Monitor</A>
			</TD>
			<TD class="formcontrol">
				 <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoDefineLaterActivityURL&activityID=<c:out value="${activity.activityId}"/>');">
				 Define Later</A><BR>
			</TD>
		</TR>
		</c:forEach>
		</table>

		<H2>User Progress</H2>
		<TABLE border="0" summary="This table is being used for layout purposes only">
		<TR>
			<TD>Login</TD>
			<TD>Progress</TD>
		</TR>
		<c:forEach var="progress" items="${lesson.learnerProgresses}">
		<TR>
			<TD><c:out value="${progress.user.login}"/></TD>
			<c:if test="${!empty progress.currentActivity}">
				<TD>Current Activity:</TD> 
				<TD><c:out value="${progress.currentActivity.title}"/> <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoLearnerActivityURL&userID=<c:out value="${progress.user.userId}"/>&activityID=<c:out value="${progress.currentActivity.activityId}"/>');">View</A> 
				</c:if></TD>
		</TR>
		<TR>
			<TD></TD>
			<TD valign="top">Completed Activities:</TD>
			<TD>
			<c:forEach var="completed" items="${progress.completedActivities}">
				<c:out value="${completed.title}"/> <A HREF="javascript:launchPopup('/lams/monitoring/dummy.do?method=gotoLearnerActivityURL&userID=<c:out value="${progress.user.userId}"/>&activityID=<c:out value="${completed.activityId}"/>');">View</A><BR>
			</c:forEach>
			</TD>
		</TR>
		</c:forEach>
		</TABLE>
	</body>
</html>
