<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="admin.title" /></c:set>
<lams:Page type="admin" title="${title}">

	<table class="table table-no-border">
		<tr>
			<td width="25%"><fmt:message key="config.securitySalt" /></td>
			<td><c:out value="${configDTO.securitySalt}" /></td>
		</tr>
		<tr>
			<td><fmt:message key="config.serverURL" /></td>
			<td><c:out value="${configDTO.serverURL}" /></td>
		</tr>
	</table>
	
	<html:link action="admin/edit.do"
		styleClass="btn btn-primary pull-right">
		<fmt:message key="button.edit" />
	</html:link>

</lams:Page>