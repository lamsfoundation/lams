<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${leaderselectionDTO}" />

<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<div class="panel panel-default" >
        <div class="panel-heading" id="headingStats${session.sessionID}">
   	    	<span class="panel-title collapsable-icon-left">
	       		<a role="button" data-toggle="collapse" href="#collapseStats${session.sessionID}" 
						aria-expanded="false" aria-controls="collapseStats${session.sessionID}" >
					${session.sessionName}
				</a>
			</span>
       	</div>
       
        <div id="collapseStats${session.sessionID}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingStats${session.sessionID}">
	</c:if>

		<table class="table table-condensed">
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
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
		
</c:forEach>
