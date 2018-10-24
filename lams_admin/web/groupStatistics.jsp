<%@ include file="/taglibs.jsp"%>

<h4>${groupStatisticsDTO.name}</h4>

<table class="table table-striped table-condensed" >
	<tr>
		<td><fmt:message key="admin.statistics.totalUsers" /></td>
		<td width="150px">${groupStatisticsDTO.totalUsers}</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.statistics.lessons" /></td>
		<td>${groupStatisticsDTO.lessons}</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.statistics.group.monitors" /></td>
		<td>${groupStatisticsDTO.monitors}</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.statistics.group.authors" /></td>
		<td>${groupStatisticsDTO.authors}</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.statistics.group.learners" /></td>
		<td>${groupStatisticsDTO.learners}</td>
	</tr>
</table>

<c:if test="${not empty  groupStatisticsDTO.subGroups}">
	<div style="margin-left: 4%"><c:forEach
		var="subGroupStatisticsDTO" items="${groupStatisticsDTO.subGroups}">

		<h4>${subGroupStatisticsDTO.name}</h4>

		<table class="table table-striped table-condensed" >
			<tr>
				<td><fmt:message key="admin.statistics.totalUsers" /></td>
				<td width="150px">${subGroupStatisticsDTO.totalUsers}</td>
			</tr>
			<tr>
				<td><fmt:message key="admin.statistics.lessons" /></td>
				<td>${subGroupStatisticsDTO.lessons}</td>
			</tr>
			<tr>
				<td><fmt:message key="admin.statistics.group.monitors" /></td>
				<td>${subGroupStatisticsDTO.monitors}</td>
			</tr>
			<tr>
				<td><fmt:message key="admin.statistics.group.authors" /></td>
				<td>${subGroupStatisticsDTO.authors}</td>
			</tr>
			<tr>
				<td><fmt:message key="admin.statistics.group.learners" /></td>
				<td>${subGroupStatisticsDTO.learners}</td>
			</tr>
		</table>
	</c:forEach></div>
</c:if>
