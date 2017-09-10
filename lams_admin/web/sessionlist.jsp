<%@ include file="/taglibs.jsp"%>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<table class="table table-striped">
	<tr>
		<th><fmt:message key="sysadmin.maintain.session.login" /></th>
		<th><fmt:message key="sysadmin.maintain.session.id" /></th>
	</tr>
	<c:forEach items="${sessions}" var="ses">
	<tr>
		<td><c:out value="${ses.key}" /></td>
		<td><c:out value="${ses.value}" /></td>
	</tr>
	</c:forEach>
</table>