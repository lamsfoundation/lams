<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${leaderselectionDTO}" />

<c:forEach var="session" items="${dto.sessionDTOs}">
	<c:if test="${isGroupedActivity}">
		<div class="lcard" >
        <div class="card-header" id="headingStats${session.sessionID}">
   	    	<span class="card-title collapsable-icon-left">
	       		<button type="button" data-bs-toggle="collapse" class="btn btn-secondary-darker no-shadow" data-bs-target="#collapseStats${session.sessionID}" 
						aria-expanded="true" aria-controls="collapseStats${session.sessionID}" >
					${session.sessionName}
				</button>
			</span>
       	</div>
       
        <div id="collapseStats${session.sessionID}" class="card-body collapse show">
	</c:if>

	<div class="ltable">
		<div class="row">
			<div class="col">
				<fmt:message>heading.totalLearnersInGroup</fmt:message>
			</div>
			<div class="col">
				${session.numberOfLearners}
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message>heading.totalFinishedLearnersInGroup</fmt:message>
			</div>
			<div class="col">
				${session.numberOfFinishedLearners}
			</div>
		</div>
	</div>
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>		
</c:forEach>
