<%@ include file="/common/taglibs.jsp"%>

<div id="content">

<h1><fmt:message key="admin.title" /></h1>

<table>
	<tr>
		<td><fmt:message key="config.version" /></td>
		<td><c:out value="${configDTO.version}" /></td>
	</tr>
	<tr>
		<td><fmt:message key="config.standardServerURL" /></td>
		<td><c:out value="${configDTO.standardServerURL}" /></td>
	</tr>
	<tr>
		<td><fmt:message key="config.enterpriseServerURL" /></td>
		<td><c:out value="${configDTO.enterpriseServerURL}" /></td>
	</tr>
	<tr>
		<td><fmt:message key="config.adminPassword" /></td>
		<td><c:out value="${configDTO.adminPassword}" /></td>
	</tr>
</table>

<br>

<div class="align-right"><html:link action="admin/edit.do"
	styleClass="button">
	<fmt:message key="button.edit" />
</html:link></div>
</div>
