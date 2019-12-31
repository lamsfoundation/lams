<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain.external.servers"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<p>
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
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
				<c:forEach items="${servers}" var="server">
				<tr>
					<td><c:out value="${server.serverid}" /></td>
					<td><c:out value="${server.serverkey}" /></td>
					<td><c:out value="${server.servername}" /></td>
					<td><c:out value="${server.serverdesc}" /></td>
					<td><c:out value="${server.prefix}" /></td>
					<td>
						<c:choose>
						<c:when test="${server.disabled}" >
							<fmt:message key="label.yes" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.no" />
						</c:otherwise>
						</c:choose>
					</td>
					<td>
						<a style="display: inline-block;" class="btn btn-primary btn-xs" id="edit_<c:out value='${server.serverid}'/>" href="<lams:LAMSURL/>admin/extserver/edit.do?sid=${server.sid}"><fmt:message key="admin.edit" /></a>
						&nbsp;
						<c:choose>
							<c:when test="${server.disabled}">
                                <csrf:form style="display: inline-block;" id="enable_${server.serverid}" method="post" action="/lams/admin/extserver/enable.do"><input type="hidden" name="sid" value="${server.sid}"/><input type="submit" class="btn btn-success btn-xs" value="<fmt:message key="admin.enable" />"/></csrf:form>
							</c:when>
							<c:otherwise>
                                <csrf:form style="display: inline-block;" id="disable_${server.serverid}" method="post" action="/lams/admin/extserver/disable.do"><input type="hidden" name="sid" value="${server.sid}"/><input type="submit" class="btn btn-primary btn-xs" value="<fmt:message key="admin.disable" />"/></csrf:form>
							</c:otherwise>
						</c:choose>
						&nbsp;
                        <csrf:form id="delete_${server.serverid}" style="display: inline-block;" method="post" action="/lams/admin/extserver/delete.do"><input type="hidden" name="sid" value="${server.sid}"/><input type="submit" class="btn btn-danger btn-xs" value="<fmt:message key="admin.delete" />"/></csrf:form>
					</td>
				</tr>
				</c:forEach>
			</table>
			<p>${fn:length(servers)}&nbsp;<fmt:message key="sysadmin.integrated.servers" /></p>
			
			<input class="btn btn-default pull-right" name="addnewserver" type="button" value="<fmt:message key='sysadmin.server.add' />" onClick="javascript:document.location='edit.do'" />

	</lams:Page>
</body>
</lams:html>
