<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">

	<h2>
		${session.sessionName}
	</h2>

	<table>
		<tr>
			<td class="field-name" width="40%">
				<fmt:message>heading.totalLearnersInGroup</fmt:message>
			</td>
			<td width="70%">
				${session.numberOfLearners}
			</td>
		</tr>
		<tr>
			<td class="field-name" width="40%">
				<fmt:message>heading.totalFinishedLearnersInGroup</fmt:message>
			</td>
			<td width="70%">
				${session.numberOfFinishedLearners}
			</td>
		</tr>
	</table>
</c:forEach>
