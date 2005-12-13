<%@ include file="/includes/taglibs.jsp"%>
<div id="datatablecontainer">
<table class="forms">
	<tr>
		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring/" />
		</c:set>
		<td><a
			href="<c:out value='${monitoringURL}'/>listContentUsers.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Monitoring
		Logon</a></td>
		<td><a
			href="<c:out value='${monitoringURL}'/>viewInstructions.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Monitoring
		Instructions</a></td>
		<td><a
			href="<c:out value='${monitoringURL}'/>viewActivity.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Edit
		Activity</a></td>
		<td><a
			href="<c:out value='${monitoringURL}'/>statistic.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Monitoring
		Statistic</a></td>
	</tr>
</table>
</div>
