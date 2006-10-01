<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<table cellspacing="0">
		<tbody>
			<tr>
				<td colspan="2">
					<h2>${session.sessionName}</h2>
				</td>
			</tr>
		</tbody>
	</table>
	<hr/>
</c:forEach>
