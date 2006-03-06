<%@ include file="/includes/taglibs.jsp"%>
<div id="datatablecontainer">
	<table class="forms">
		<tr>
			<c:set var="monitoringURL">
				<html:rewrite page="/monitoring/" />
			</c:set>
			<td>
				<a href="<c:out value='${monitoringURL}'/>listContentUsers.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">
					<fmt:message key="monitoring.tab.summary"/>
				</a>
			</td>
			<td>
				<a href="<c:out value='${monitoringURL}'/>viewInstructions.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">
					<fmt:message key="monitoring.tab.instructions"/>
				</a>
			</td>
			<td>
				<a href="<c:out value='${monitoringURL}'/>editActivity.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">
					<fmt:message key="monitoring.tab.edit.activity"/>
				</a>
			</td>
			<td>
				<a href="<c:out value='${monitoringURL}'/>statistic.do?toolContentID=<c:out value='${sessionScope.toolContentID}'/>">
					<fmt:message key="monitoring.tab.edit.statistics"/>
					
				</a>
			</td>
		</tr>
	</table>
</div>
