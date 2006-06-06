<!--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
-->

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<!-- two input values: lesson (Lesson) and activities (set of AuthoringActivityDTO objects) -->

<html>
  <head>
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
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

	<p>If you want to participate in the lesson as a student, or view the portfolio as if you were a student, please use the learner screen.</p>

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

	<H2>Monitoring Activities</H2>
	<p>All the links below should pop-up in new windows.</p>

	<p><A HREF=javascript:launchPopup('/lams/learning/exportWaitingPage.jsp?mode=teacher&lessonID=<c:out value="${lesson.lessonId}"/>');>Export Portfolio for all users</a> </p>

	<p>Note: These activities are in an arbitrary order, not in the order in authoring. In the Flash based interface, they will be displayed in the same order used in authoring.</p>

	<TABLE border="0" summary="This table is being used for layout purposes only">
		<c:forEach var="activity" items="${activities}">
		<c:choose>

		<c:when test="${activity.activityTypeID eq 6}">
		<!-- show the parallel activity as a heading, and its child activities. This code relies on the activities only being two deep at the moment. -->
		<!-- should use Activity.PARALLEL_ACTIVITY_TYPE but that doesn't work -->
		<TR>
			<TD class="formcontrol" colspan="3">
				Parallel Activity <c:out value="${activity.activityID}"/>: <c:out value="${activity.title}"/><BR/>

				<TABLE border="0" summary="This table is being used for layout purposes only">
				<!-- go through all the activities and see if any are the child of the parallel activity -->
				<c:forEach var="possibleChildActivity" items="${activities}">
				<c:if test="${possibleChildActivity.parentActivityID eq activity.activityID}">
				<TR>
					<TD class="formcontrol">
						--> Activity <c:out value="${possibleChildActivity.activityID}"/> <c:out value="${possibleChildActivity.title}"/>:
					</TD>
					<TD class="formcontrol">
						 <A HREF="javascript:launchPopup('/lams/monitoring/monitoring.do?method=getActivityMonitorURL&lessonID=<c:out value="${lesson.lessonId}"/>&activityID=<c:out value="${possibleChildActivity.activityID}"/>');">
						 Monitor</A>
					</TD>
					<TD class="formcontrol">
						 <A HREF="javascript:launchPopup('/lams/monitoring/monitoring.do?method=getActivityDefineLaterURL&lessonID=<c:out value="${lesson.lessonId}"/>&activityID=<c:out value="${possibleChildActivity.activityID}"/>');">
						 Define Later</A><BR>
					</TD>
				</TR>
				</c:if>
				</c:forEach>
				</TABLE>
			</TD>
		</TR>
		</c:when>

		<c:when test="${empty activity.parentActivityID}"> 
		<!-- Only show the activities without a parent. Those with a parent will be displayed by the parent activity -->
		<TR>
			<TD class="formcontrol">
				Activity <c:out value="${activity.activityID}"/> <c:out value="${activity.title}"/>:
			</TD>
			<TD class="formcontrol">
				 <A HREF="javascript:launchPopup('/lams/monitoring/monitoring.do?method=getActivityMonitorURL&lessonID=<c:out value="${lesson.lessonId}"/>&activityID=<c:out value="${activity.activityID}"/>');">
				 Monitor</A>
			</TD>
			<TD class="formcontrol">
				 <A HREF="javascript:launchPopup('/lams/monitoring/monitoring.do?method=getActivityDefineLaterURL&lessonID=<c:out value="${lesson.lessonId}"/>&activityID=<c:out value="${activity.activityID}"/>');">
				 Define Later</A><BR>
			</TD>
		</TR>
		</c:when> 
		</c:choose>
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
				<TD><c:out value="${progress.currentActivity.title}"/> <A HREF="javascript:launchPopup('/lams/monitoring/monitoring.do?method=getLearnerActivityURL&lessonID=<c:out value="${lesson.lessonId}"/>&userID=<c:out value="${progress.user.userId}"/>&activityID=<c:out value="${progress.currentActivity.activityId}"/>');">View</A> 
				</c:if></TD>
		</TR>
		<TR>
			<TD></TD>
			<TD valign="top">Completed Activities:</TD>
			<TD>
			<c:forEach var="completed" items="${progress.completedActivities}">
				<c:out value="${completed.title}"/> <A HREF="javascript:launchPopup('/lams/monitoring/monitoring.do?method=getLearnerActivityURL&lessonID=<c:out value="${lesson.lessonId}"/>&userID=<c:out value="${progress.user.userId}"/>&activityID=<c:out value="${completed.activityId}"/>');">View</A><BR>
			</c:forEach>
			</TD>
		</TR>
		</c:forEach>
		</TABLE>
	</body>
</html>
