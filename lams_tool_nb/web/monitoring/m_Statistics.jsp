<%@ include file="/includes/taglibs.jsp"%>

<table cellpadding="0">
	<c:set var="groupCounter" scope="request" value="0" />
	<c:forEach var="group" items="${requestScope.groupStatsMap}">
		<c:set var="groupCounter" scope="request" value="${groupCounter+1}" />
		<tr>
			<td width="30%">
				<fmt:message key="heading.group">
					<fmt:param value="${groupCounter}" />
				</fmt:message>
			</td>

			<td>
				&nbsp;
			</td>
		</tr>

		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="heading.totalLearners" />
			</td>
			<td>
				<c:out value="${group.value}" />
			</td>
		</tr>
	</c:forEach>

	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="heading.totalLearnersInGroup" />
		</td>
		<td>
			<c:out value="${requestScope.totalLearners}" />
		</td>
	</tr>
</table>
