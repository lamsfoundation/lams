<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />


<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${session.sessionID}">
			<span class="panel-title">
				<c:out value="${session.sessionName}" />
			</span>
        </div>
        <div class="panel-body">
	</c:if>

	<table class="table table-condensed table-no-border">
		<tbody>
			<tr>
				<td class="field-name" style="width: 30%;">
					<fmt:message>heading.totalLearners</fmt:message>
				</td>
				<td>
					${session.numberOfLearners}
				</td>
			</tr>

			<tr>
				<td class="field-name" style="width: 30%;">
					<fmt:message>heading.totalMessages</fmt:message>
				</td>
				<td>
					${session.numberOfPosts}
				</td>
			</tr>

			<tr>
				<th>
					<fmt:message>heading.learner</fmt:message>
				</th>
				<th>
					<fmt:message>heading.numPosts</fmt:message>
				</th>
			</tr>

			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						${user.nickname}
					</td>
					<td>
						${user.postCount}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<c:if test="${isGroupedActivity}">	
		</div>
	</c:if>
	
</c:forEach>
