<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${requestScope.monitoringDTO}" />

<c:forEach var="session" items="${dto.sessionDTOs}">

<c:if test="${isGroupedActivity}">
	<div class="panel panel-default" >
       <div class="panel-heading" id="stats${session.sessionID}">
  	    	<span class="panel-title">
			${session.sessionName}</a>
			</span>
      	</div>
</c:if>

	<table class="table table-condensed">
		<tr>
			<td><strong> <fmt:message key="heading.totalLearners" /> </strong></td>
			<td>${session.numberOfLearners}	</td>
		</tr>
		<tr>
			<td><strong> <fmt:message key="heading.numberOfVotes" /> </strong></td>
			<td>${session.numberOfVotes}</td>
		</tr>
	</table>

<c:if test="${isGroupedActivity}">
	</div>
</c:if>

</c:forEach>

