<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />


<table cellspacing="0" class="alternative-color">
	<tr>
		<th class="first">
			?Session Name?	
		</th>
		
		<th class="first">
			?Number of Learners?
		</th>
		
		<th class="first">
			?Number of Votes?
		</th>
	</tr>
	<c:forEach var="session" items="${dto.sessionDTOs}">
		<tr>
			<td>
				${session.sessionName}
			</td>
			<td>
				${session.numberOfLearners}			
			</td>
			<td>
				${session.numberOfVotes}
			</td>
		</tr>
	</c:forEach>
</table>
