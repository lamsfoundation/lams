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
	<style type="text/css">
		td {
			vertical-align: top;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
	
	</script>

</lams:head>

<body class="stripes">

	<lams:Page title="${title}" type="admin">

	<h4>
		<fmt:message key="email.notifications.archived.messages.list"/>
	</h4>

	<table class="table table-condensed table-striped">
		<thead>
			<tr>
				<th class="text-left" width="25%">
					<fmt:message key="email.notifications.archived.messages.list.sent.date"/>		
				</td>
				<th  class="text-left">
					<fmt:message key="email.notify.students.that"/>	
				</td>
				<th  class="text-left">
					<fmt:message key="email.notifications.archived.messages.list.sent.count" />
				</td>
				<th  class="text-left">
					<fmt:message key="email.notifications.scheduled.messages.list.email.body"/>		
				</td>
			</tr>
		</thead>
		<c:forEach var="email" items="${notifications}">
			<tr>
				<td>
					<lams:Date value="${email.sentOn}"/>
				</td>
				<td>
					<c:choose>
						<c:when test="${email.searchType == 0}"><fmt:message key="email.notifications.user.search.property.0" /></c:when>
						<c:when test="${email.searchType == 1}"><fmt:message key="email.notifications.user.search.property.1" /></c:when>
						<c:when test="${email.searchType == 2}"><fmt:message key="email.notifications.user.search.property.2" /></c:when>
						<c:when test="${email.searchType == 3}"><fmt:message key="email.notifications.user.search.property.3" /></c:when>
						<c:when test="${email.searchType == 4}"><fmt:message key="email.notifications.user.search.property.4" /></c:when>
						<c:when test="${email.searchType == 5}"><fmt:message key="email.notifications.user.search.property.5" /></c:when>
						<c:when test="${email.searchType == 6}"><fmt:message key="email.notifications.user.search.property.6" /></c:when>
						<c:when test="${email.searchType == 7}"><fmt:message key="email.notifications.user.search.property.7" /></c:when>
						<c:when test="${email.searchType == 8}"><fmt:message key="email.notifications.user.search.property.8" /></c:when>
						<c:when test="${email.searchType == 9}"><fmt:message key="email.notifications.user.search.property.9" /></c:when>
						<c:when test="${email.searchType == 10}"><fmt:message key="email.notifications.user.search.property.10" /></c:when>
					</c:choose>
				</td>
				<td>
					<a href="#" onClick="javascript:showLearners(${email.uid}')">
						${fn:length(email.recipients)}&nbsp;<fmt:message key="email.notifications.archived.messages.list.learners" />
					</a>
				</td>
				<td>
					${email.body}
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<a href="<c:url value='/emailNotifications.do'/>${lessonID == null ? '?method=getCourseView&organisationID='.concat(organisationID)
																	   : '?method=getLessonView&lessonID='.concat(lessonID)}"
	   class="btn btn-primary pull-right">
		<fmt:message key="email.notifications.scheduled.messages.list.back" />
	</a>
	</lams:Page>
	
</body>
</lams:html>
