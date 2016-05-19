<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">

	<h4>
		<c:out value="${session.sessionName}" escapeXml="true"/>
	</h4>

	<table class="table table-striped table-condensed">
		<tr>
			<td>
				<fmt:message>heading.totalLearnersInGroup</fmt:message>
			</td>
			<td>
				${session.numberOfLearners}
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message>heading.totalFinishedLearnersInGroup</fmt:message>
			</td>
			<td>
				${session.numberOfFinishedLearners}
			</td>
		</tr>
	</table>
</c:forEach>
