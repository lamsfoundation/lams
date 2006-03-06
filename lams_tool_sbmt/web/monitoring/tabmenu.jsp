<%@ taglib uri="tags-lams" prefix="lams" %>
<!-- start tabs -->
<lams:Tabs>
	<lams:Tab id="1" value="Logon"/>
	<lams:Tab id="2" value="Instructions"/>
	<lams:Tab id="3" value="Edit Activity"/>
	<lams:Tab id="4" value="Statistics"/>
</lams:Tabs>
<!-- end tab buttons -->

<!--
<div id="datatablecontainer">
<table class="forms">
	<tr>
		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>
		<td><a
			href="<c:out value='${monitoringURL}'/>?method=userList&toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Monitoring
		Logon</a></td>
		<td><a
			href="<c:out value='${monitoringURL}'/>?method=instructions&toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Monitoring
		Instructions</a></td>
		<td><a
			href="<c:out value='${monitoringURL}'/>?method=showActivity&toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Edit
		Activity</a></td>
		<td><a
			href="<c:out value='${monitoringURL}'/>?method=statistic&toolContentID=<c:out value='${sessionScope.toolContentID}'/>">Monitoring
		Statistic</a></td>
	</tr>
</table>
</div>
-->
