<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>	
	
	<lams:css/>	
	<link type="text/css" href="<lams:LAMSURL/>/css/jquery-ui-smoothness-theme.css" rel="stylesheet" />
	
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
	<h4>
		<fmt:message key="email.notifications.scheduled.messages.list"/>
	</h4>

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

	<table class="table table-condensed table-striped">
		<thead>
			<tr>
				<th class="text-left" width="25%">
					<fmt:message key="email.notifications.scheduled.messages.list.scheduled.date"/>		
				</td>
				<th  class="text-left">
					<fmt:message key="email.notify.students.that"/>	
				</td>
				<th  class="text-left">
					<fmt:message key="email.notifications.scheduled.messages.list.email.body"/>		
				</td>
			</tr>
		</thead>
		<c:forEach var="emailJob" items="${scheduleList}">
			<tr>
				<td style="vertical-align: top;">
					<c:set var="tDate"><lams:Date value="${emailJob.triggerDate}"/></c:set>
					${tDate}<BR/>
					
					<a href="#" class="btn btn-default btn-sm" 
						onclick="javascript:deleteNotification('${emailJob.triggerName}', '${tDate}', '${deleteUrlParams}');">
						<i class="fa fa-fw fa-trash-o"></i> <fmt:message key="email.notifications.delete" />
					</a>
				</td>
				<td  style="vertical-align: top;">
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
				<td  style="vertical-align: top;">
					${emailJob.emailBody}
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<a href="<c:url value='/emailNotifications.do'/>${returnUrlParams}" class="btn btn-primary pull-right">
		<fmt:message key="email.notifications.scheduled.messages.list.back" />
	</a>

	
</body>
</lams:html>
