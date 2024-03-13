<%@ include file="/common/taglibs.jsp"%>

<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="false"> 

<c:forEach var="sessionDto" items="${sessionDTOs}" varStatus="status">
	<c:set var="sessionUid" value="${sessionDto.sessionUid}"/>

	<c:set var="buttonbar">
		<div class="float-end mb-3">
			<c:if test="${useSelectLeaderToolOuput and sessionDto.sessionUserCount > 0 and not sessionDto.sessionFinished}">
				<button type="button" class="btn btn-secondary me-2"
						onClick="showChangeLeaderModal(${sessionDto.toolSessionId})">
					<i class="fa-solid fa-user-pen me-1"></i>
					<fmt:message key='label.monitoring.change.leader'/>
				</button>
			</c:if>		
			
			<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${sessionDto.toolSessionId}&toolContentID=${toolContentID}" />
			<button type="button" class="fa fa-pie-chart text-primary btn btn-light me-2" 
					title="<fmt:message key='label.tip.displayPieChart'/>"
					onclick="drawChart('pie', 'chartDiv${sessionDto.toolSessionId}', '${chartURL}')"></button>
			
			<button type="button" class="fa fa-bar-chart text-primary btn btn-light me-2"
					title="<fmt:message key='label.tip.displayBarChart'/>" 
					onclick="drawChart('bar', 'chartDiv${sessionDto.toolSessionId}', '${chartURL}')"></button>
		</div>
	</c:set>						
	
	<c:choose>
		<c:when test="${isGroupedActivity}">
			<div class="lcard" >
		       <div class="card-header " id="heading${sessionUid}">
		  	    	<span class="card-title collapsable-icon-left">
			  	    	<button class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${sessionUid}" 
								aria-expanded="${status.first}" aria-controls="collapse${sessionUid}" >
							<c:out value="${sessionDto.sessionName}"/>
						</button>
					</span>  
					${buttonbar}
		       </div>
		       
		       <div id="collapse${sessionUid}" class="card-collapse collapse ${status.first ? 'show' : ''}">
		</c:when>
		<c:otherwise>
			${buttonbar}
		</c:otherwise>
	</c:choose>

	<table class="table table-condensed table-striped">
		<tr>
			<th><fmt:message key="label.nomination"/></th>
			<th style="width: 90px;"><fmt:message key="label.total.votes"/></th>
		</tr>
												
		<c:forEach var="currentNomination" items="${sessionDto.nominations}">
			<c:set var="questionUid" scope="request" value="${currentNomination.questionUid}"/>
			<tr>
				<td  valign=top class="align-left">
					<!-- Cannot escape as it was entered in a CKeditor -->
					${currentNomination.nomination}
				</td>
						
				<td  valign=top class="align-left">				  	 		
					<c:set var="viewVotesURL">
						<lams:WebAppURL/>monitoring/getVoteNomination.do?questionUid=${questionUid}&sessionUid=${sessionUid}
					</c:set>
					<a href="javascript:launchInstructionsPopup('${viewVotesURL}')">
						<c:out value="${currentNomination.numberOfVotes}"/>  
					</a>
					&nbsp;(<fmt:formatNumber type="number" maxFractionDigits="2" value="${currentNomination.percentageOfVotes}" /><fmt:message key="label.percent"/>) 
				</td>			
			</tr>	
		</c:forEach>	
		
		<c:if test="${allowText eq true}">
			<tr>
				<td  valign=top class="align-left">
					<fmt:message key='label.open.vote'/>
				</td>
						
				<td  valign=top class="align-left">				  	 		
					<c:set var="viewVotesURL">
						<lams:WebAppURL/>monitoring/OtherTextNominationViewer.jsp?&toolContentUID=${toolContentID}&sessionUid=${sessionUid}
					</c:set>
					<a href="javascript:launchInstructionsPopup('${viewVotesURL}')">
						<c:out value="${sessionDto.openTextNumberOfVotes}"/>  
					</a>
					&nbsp(<fmt:formatNumber type="number" maxFractionDigits="2" value="${sessionDto.openTextPercentageOfVotes}" />  <fmt:message key="label.percent"/>) 
				</td>			
			</tr>	
		</c:if>
	</table>

 	<p id="chartDiv${sessionDto.toolSessionId}" style="height: 220px; display: none;"></p>
 
 	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	
</c:forEach>				

</div> <!--  end panel group -->
