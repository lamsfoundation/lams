<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="label.manage.tool.consumers"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
				<p>
					<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default">
						<fmt:message key="sysadmin.maintain" />
					</a>
				</p>
				
				<table class="table table-striped">
				
					<tr>
						<th><fmt:message key="sysadmin.serverid" /></th>
						<th><fmt:message key="sysadmin.serverkey" /></th>
						<th><fmt:message key="sysadmin.servername" /></th>
						<th><fmt:message key="sysadmin.serverdesc" /></th>
						<th><fmt:message key="sysadmin.prefix" /></th>
						<th><fmt:message key="sysadmin.disabled" /></th>
						<th><fmt:message key="admin.actions"/></th>
					</tr>
					
					<c:forEach var="ltiConsumer" items="${ltiConsumers}">
						<tr>
							<td><c:out value="${ltiConsumer.serverid}" /></td>
							<td><c:out value="${ltiConsumer.serverkey}" /></td>
							<td><c:out value="${ltiConsumer.servername}" /></td>
							<td><c:out value="${ltiConsumer.serverdesc}" /></td>
							<td><c:out value="${ltiConsumer.prefix}" /></td>
							<td>
								<c:choose>
									<c:when test="${ltiConsumer.disabled}" >
										<fmt:message key="label.yes" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.no" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<a href="../ltiConsumerManagement/edit.do?sid=<c:out value='${ltiConsumer.sid}' />">
									<fmt:message key="admin.edit" />
								</a>
								&nbsp;
								<c:choose>
									<c:when test="${ltiConsumer.disabled}">
										<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/disable.do?disable=false&sid=<c:out value='${ltiConsumer.sid}' />">
											<fmt:message key="admin.enable" />
										</a>
									</c:when>
									<c:otherwise>
										<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/disable.do?disable=true&sid=<c:out value='${ltiConsumer.sid}' />">
											<fmt:message key="admin.disable" />
										</a>
									</c:otherwise>
								</c:choose>
								&nbsp;
								<a href="<lams:LAMSURL/>admin/ltiConsumerManagement/delete.do?sid=<c:out value='${ltiConsumer.sid}' />">
									<fmt:message key="admin.delete" />
								</a>
							</td>
						</tr>
					</c:forEach>
				</table>
				
				<p>${fn:length(ltiConsumers)}&nbsp;<fmt:message key="label.tool.consumers.count" /></p>
				
				<input class="btn btn-default pull-right" name="addNewLtiConsumer" type="button" value="<fmt:message key='label.add.tool.consumer' />" 
						onClick="javascript:document.location='../ltiConsumerManagement/edit.do'" />
				
	</lams:Page>
</body>
</lams:html>


