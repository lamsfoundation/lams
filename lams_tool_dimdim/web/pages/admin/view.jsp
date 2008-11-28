<%@ include file="/common/taglibs.jsp"%>

<div id="content">

<h1><fmt:message key="admin.title" /></h1>

<table>
	<tr>
		<td><fmt:message key="config.version" /></td>
		<td><c:out value="${configDTO.version}" /></td>
	</tr>
	<tr>
		<td><fmt:message key="config.serverURL" /></td>
		<td><c:out value="${configDTO.serverURL}" /></td>
	</tr>
</table>

<br>

<div class="align-right"><html:link action="admin/edit.do"
	styleClass="button">
	<fmt:message key="button.edit" />
</html:link></div>
</div>
