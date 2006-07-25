<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${notebookDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<table cellspacing="0">
		<tr>
			<th colspan="2">
				${session.sessionName}
			</th>
		</tr>

		<tr>
			<td class="field-name" width="30%">
				<fmt:message>heading.totalLearners</fmt:message>
			</td>
			<td>
				${session.numberOfLearners}
			</td>
		</tr>
	</table>
</c:forEach>
