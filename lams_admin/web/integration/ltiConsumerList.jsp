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
								<a class="btn btn-default btn-xs" id="edit_${ltiConsumer.sid}" href="../ltiConsumerManagement/edit.do?sid=<c:out value='${ltiConsumer.sid}' />">
									<fmt:message key="admin.edit" />
								</a>
								&nbsp;
								<c:choose>
									<c:when test="${ltiConsumer.disabled}">
                                        <csrf:form style="display: inline-block;" id="enable_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/disable.do"><input type="hidden" name="sid" value="${ltiConsumer.sid}"/><input type="hidden" name="disable" value="false"/><input type="submit" class="btn btn-primary btn-xs" value="<fmt:message key="admin.enable" />"/></csrf:form>
									</c:when>
									<c:otherwise>
                                        <csrf:form style="display: inline-block;" id="disable_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/disable.do"><input type="hidden" name="sid" value="${ltiConsumer.sid}"/><input type="hidden" name="disable" value="true"/><input type="submit" class="btn btn-primary btn-xs" value="<fmt:message key="admin.disable" />"/></csrf:form>
									</c:otherwise>
								</c:choose>
								&nbsp;
                                <csrf:form style="display: inline-block;" id="delete_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/delete.do"><input type="hidden" name="sid" value="${ltiConsumer.sid}"/><input type="submit" class="btn btn-danger btn-xs" value="<fmt:message key="admin.delete" />"/></csrf:form>
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


