<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>	
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/free.ui.jqgrid.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<style type="text/css">
		td {
			vertical-align: top;
		}
		
		#downloadFrame {
			display: none;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/popper.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		function showRecipients(notificationUid, link) {
			$(link).closest('td').empty()
							     .append($('<table />').attr('id', 'grid' + notificationUid));
			$("#grid" + notificationUid).jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	url: "<c:url value='/emailNotifications/getArchivedRecipients.do'/>?emailNotificationUid=" + notificationUid,
				datatype: "json",
				colNames: ['<spring:escapeBody javaScriptEscape="true"><fmt:message key="email.notifications.user.name" /></spring:escapeBody>'],
			   	colModel:[
			        {
					   'name'     : 'name', 
					   'index'    : 'name',
					   'sortable' : false,
					   'title'    : false
					}
			   	],
			   	rowNum: 10,
			   	pager: true,
			    height:'100%',
			    width: '210px',
			    ignoreCase: true
			});
		}

		function exportNotification(notificationUid) {
			$('#downloadFrame').attr('src',
					"<c:url value='/emailNotifications/exportArchivedNotification.do'/>?emailNotificationUid=" + notificationUid);
		}
	</script>

</lams:head>

<body>
	<h4>
		<fmt:message key="email.notifications.archived.messages.list"/>
	</h4>

	<table class="table table-condensed table-striped">
		<thead>
			<tr>
				<th class="text-left" width="25%">
					<fmt:message key="email.notifications.archived.messages.list.sent.date"/>		
				</td>
				<th class="text-left">
					<fmt:message key="email.notify.students.that"/>	
				</td>
				<th class="text-left">
					<fmt:message key="email.notifications.archived.messages.list.sent.count" />
				</td>
				<th class="text-left">
					<fmt:message key="email.notifications.scheduled.messages.list.email.body"/>		
				</td>
			</tr>
		</thead>
		<c:forEach var="email" items="${notifications}">
			<tr>
				<td>
					<lams:Date value="${email.sentOn}"/><br />
					<a href="#" class="btn btn-secondary btn-sm" 
						onclick="javascript:exportNotification(${email.uid})">
						<i class="fa fa-download"></i> <fmt:message key="email.notifications.archived.export.button" />
					</a>
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
					<a href="#" onClick="javascript:showRecipients(${email.uid}, this)">
						${fn:length(email.recipients)}&nbsp;<fmt:message key="email.notifications.archived.messages.list.learners" />
					</a>
				</td>
				<td>
					${email.body}
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<a href="<c:url value='/emailNotifications/'/>${lessonID == null ? 'getCourseView.do?newUI=true&organisationID='.concat(organisationID)
																	   : 'getLessonView.do?newUI=true&lessonID='.concat(lessonID)}"
	   class="btn btn-primary pull-right">
		<fmt:message key="email.notifications.scheduled.messages.list.back" />
	</a>
	
	<iframe id="downloadFrame"></iframe>
</body>
</lams:html>