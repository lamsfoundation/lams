<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${requestScope.monitoringDTO}" />

<table cellspacing="0" class="alternative-color">
	<tr>
		<th class="first">
			<fmt:message key="heading.sessionName" />	
		</th>
		
		<th align="center">
			<fmt:message key="heading.totalLearners" />
		</th>
		
		<th align="center">
			<fmt:message key="heading.numberOfVotes" />
		</th>
	</tr>
	<c:forEach var="session" items="${dto.sessionDTOs}">
		<tr>
			<td align="left">
				${session.sessionName}
			</td>
			<td align="center">
				${session.numberOfLearners}			
			</td>
			<td align="center">
				${session.numberOfVotes}
			</td>
		</tr>
	</c:forEach>
</table>
