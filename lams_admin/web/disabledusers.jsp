<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.list.disabled.users"/></c:set>
	<lams:Page type="admin" title="${title}">
	
		<p><a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-default"><fmt:message key="appadmin.maintain" /></a></p>

		<table class="table table-striped table-condensed">
		<tr>
			<th></th>
			<th><fmt:message key="admin.user.login"/></th>
			<th><fmt:message key="admin.user.title"/></th>
			<th><fmt:message key="admin.user.first_name"/></th>
			<th><fmt:message key="admin.user.last_name"/></th>
			<th></th>
		</tr>
		<c:forEach var="user" items="${users}">
			<tr>
				<td>
					<c:out value="${user.userId}" />
				</td>
				<td>
					<c:out value="${user.login}" />
				</td>
				<td>
					<c:out value="${user.title}" />
				</td>
				<td>
					<c:out value="${user.firstName}" />
				</td>
				<td>
					<c:out value="${user.lastName}" />
				</td>
				<td>
					<csrf:form style="display: inline-block;" id="enable_${ltiConsumer.sid}" method="post" action="/lams/admin/user/enable.do"><input type="hidden" name="userId" value="${user.userId}"/><button type="submit" class="btn btn-primary btn-xs"><fmt:message key="admin.enable" /></button></csrf:form>
				</td>		
			</tr>
		</c:forEach>
		</table>
	</lams:Page>

</body>
</lams:html>



