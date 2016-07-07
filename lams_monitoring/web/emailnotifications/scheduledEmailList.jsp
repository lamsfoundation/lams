<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<lams:head>
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>	
	
	<lams:css style="main" />	
	<link type="text/css" href="<lams:LAMSURL/>/css/jquery-ui-redmond-theme.css" rel="stylesheet" />	
	<style media="screen,projection" type="text/css">
		html {margin: 0;overflow:auto;background: none;}
		body {background: none;	min-height: 100%;width: 100%;}	
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
	var deleteConfirmationMessage1 = '<fmt:message key="email.notification.delete.alert1"><fmt:param>%replace%</fmt:param></fmt:message>';
	var deleteConfirmationMessage2 = '<fmt:message key="email.notification.delete.alert2"/>';
	
	function deleteNotification(triggerName, scheduledate, deleteUrlParams) {
		var msg = deleteConfirmationMessage1;
		msg = msg.replace('%replace%', scheduledate);
		if (confirm(msg+'\n\n'+deleteConfirmationMessage2)) {
 			$.ajax({
				async : false,
				url : '<c:url value="/emailNotifications.do"/>',
 				data : deleteUrlParams+'&triggerName=' + triggerName,
 				type : "POST",
				success : function(json) {
 					if (json.deleteNotification == 'true') {
						window.location.reload();
					} else {
						alert(json.deleteNotification);
					}
				}
			});
 		}
	}
	
	</script>
	
</lams:head>

<body>
	<h2 style="padding: 20px 25px 0;">
		<fmt:message key="email.notifications.scheduled.messages.list"/>
	</h2>

	<c:choose>
		<c:when test="${lessonID != null}">
			<c:set var="returnUrlParams">?method=getLessonView&lessonID=${lessonID}</c:set>
			<c:set var="deleteUrlParams">method=deleteNotification&lessonID=${lessonID}</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="returnUrlParams">?method=getCourseView&organisationID=${organisationID}</c:set>
			<c:set var="deleteUrlParams">method=deleteNotification&organisationID=${organisationID}</c:set>
		</c:otherwise>
	</c:choose>

	<table style="padding: 10px 30px;">
		<thead>
			<tr class="ui-widget-header">
				<td style="text-align: left; width: 20%;">
					<fmt:message key="email.notifications.scheduled.messages.list.scheduled.date"/>		
				</td>
				<td style="text-align: left; width: 30%;">
					Notify students that	
				</td>
				<td style="text-align: left;">
					<fmt:message key="email.notifications.scheduled.messages.list.email.body"/>		
				</td>
			</tr>
		</thead>
		<c:forEach var="emailJob" items="${scheduleList}">
			<tr>
				<td style="border-bottom: 1px solid #efefef;">
					<c:set var="tDate"><lams:Date value="${emailJob.triggerDate}"/></c:set>
					${tDate}<BR/>
					
					<a href="#" onclick="javascript:deleteNotification('${emailJob.triggerName}', '${tDate}', '${deleteUrlParams}');">
						<fmt:message key="email.notifications.delete" />
					</a>
				</td>
				<td style="border-bottom: 1px solid #efefef;">
					<c:choose>
						<c:when test="${emailJob.searchType == 0}"><fmt:message key="email.notifications.user.search.property.0" /></c:when>
						<c:when test="${emailJob.searchType == 1}"><fmt:message key="email.notifications.user.search.property.1" /></c:when>
						<c:when test="${emailJob.searchType == 2}"><fmt:message key="email.notifications.user.search.property.2" /></c:when>
						<c:when test="${emailJob.searchType == 3}"><fmt:message key="email.notifications.user.search.property.3" /></c:when>
						<c:when test="${emailJob.searchType == 4}"><fmt:message key="email.notifications.user.search.property.4" /></c:when>
						<c:when test="${emailJob.searchType == 5}"><fmt:message key="email.notifications.user.search.property.5" /></c:when>
						<c:when test="${emailJob.searchType == 6}"><fmt:message key="email.notifications.user.search.property.6" /></c:when>
						<c:when test="${emailJob.searchType == 7}"><fmt:message key="email.notifications.user.search.property.7" /></c:when>
						<c:when test="${emailJob.searchType == 8}"><fmt:message key="email.notifications.user.search.property.8" /></c:when>
						<c:when test="${emailJob.searchType == 9}"><fmt:message key="email.notifications.user.search.property.9" /></c:when>
						<c:when test="${emailJob.searchType == 10}"><fmt:message key="email.notifications.user.search.property.10" /></c:when>
					</c:choose>
				</td>
				<td style="border-bottom: 1px solid #efefef;">
					${emailJob.emailBody}
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<a href="<c:url value='/emailNotifications.do'/>${returnUrlParams}" style="margin: 30px 50px; float: right;">
		<fmt:message key="email.notifications.scheduled.messages.list.back" />
	</a>

	
</body>
</lams:html>
