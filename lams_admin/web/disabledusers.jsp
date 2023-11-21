<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="stripes">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.list.disabled.users"/></c:set>
	
	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">
	
		<table class="table table-striped table-bordered">
		<thead>
		<tr>
			<th>ID</th>
			<th><fmt:message key="admin.user.login"/></th>
			<th><fmt:message key="admin.user.first_name"/></th>
			<th><fmt:message key="admin.user.last_name"/></th>
			<th class="text-center"><fmt:message key="admin.actions"/></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td>
					<c:out value="${user.userId}" escapeXml="true"/>
				</td>
				<td>
					<c:out value="${user.login}" escapeXml="true" />
				</td>
				<td>
					<c:out value="${user.firstName}" escapeXml="true" />
				</td>
				<td>
					<c:out value="${user.lastName}" escapeXml="true" />
				</td>
				<td class="text-center">
					<csrf:form style="display: inline-block;" id="enable_${ltiConsumer.sid}" method="post" action="/lams/admin/user/enable.do">
						<input type="hidden" name="userId" value="${user.userId}"/>
						<button type="submit" class="btn btn-success text-white" title="<fmt:message key="admin.enable" />">
							<i class="fa fa-power-off"></i>
						</button>
					</csrf:form>
				</td>		
			</tr>
		</c:forEach>
		</tbody>
		</table>
		
		<div class="text-end">
			<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
				<fmt:message key="admin.cancel"/>
			</a>
		</div>	
	</lams:Page5>

</body>
</lams:html>



