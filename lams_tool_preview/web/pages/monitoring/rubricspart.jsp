<%@ include file="/common/taglibs.jsp"%>

<style>
	.rubrics-session-panel > div {
		padding: 10px;
	}
</style>

<%-- Display collapsible panels with group names only when activity is grouped --%>
<c:if test="${sessionMap.isGroupedActivity}">
	<div id="rubrics-session-panels" class="panel-group" role="tablist" aria-multiselectable="true">
</c:if>

	<c:forEach var="session" items="${rubricsData}">
	
		<c:if test="${sessionMap.isGroupedActivity}">
			<%-- Panel title with session name --%>
			<div class="panel panel-default rubrics-session-panel">
		       <div class="panel-heading" role="tab" id="rubrics-session-heading-${session.key.sessionId}">
		       	<span class="panel-title collapsable-icon-left">
		       		<a class="collapsed" role="button" data-toggle="collapse" href="#session-collapse-${session.key.sessionId}" 
							aria-expanded="false" aria-controls="session-collapse-${session.key.sessionId}"
							data-parent="#rubrics-session-panels">
						<c:out value="${session.key.sessionName}" escapeXml="true"/>
					</a>
				</span>
		       </div>
	       
		       <div id="session-collapse-${session.key.sessionId}" class="panel-collapse collapse" 
		       	    role="tabpanel" aria-labelledby="rubrics-session-heading-${session.key.sessionId}">
		       	    
	     </c:if>
	     
	       	    <div id="rubrics-user-panels-${session.key.sessionId}" class="panel-group" role="tablist" aria-multiselectable="true">
	       	    	<c:forEach var="learnerData" items="${session.value}">
	       	    	
	       	    		<%-- List learners in the given session --%>
						<div class="panel panel-default rubrics-user-panel">
					       <div class="panel-heading" role="tab" id="rubrics-user-heading-${learnerData.key.uid}">
					       	<span class="panel-title collapsable-icon-left">
					       		<a class="collapsed" role="button" data-toggle="collapse" href="#rubrics-user-collapse-${learnerData.key.uid}" 
										aria-expanded="false" aria-controls="rubrics-user-collapse-${learnerData.key.uid}"
										data-parent="#rubrics-users-panels-${session.key.sessionId}">
									<lams:Portrait userId="${learnerData.key.userId}" hover="false" />
									&nbsp;<c:out value="${learnerData.key.firstName}" escapeXml="true"/>
									&nbsp;<c:out value="${learnerData.key.lastName}" escapeXml="true"/>
								</a>
							</span>
							<button class="btn btn-default pull-right email-button btn-disable-on-submit"
									onClick="javascript:previewResultsForLearner(${session.key.sessionId}, ${learnerData.key.userId})">
								<fmt:message key="button.preview.results" />
							</button>
					       </div>
				       
				       	<div id="rubrics-user-collapse-${learnerData.key.uid}" class="panel-collapse collapse" 
				       	    role="tabpanel" aria-labelledby="rubrics-user-heading-${learnerData.key.uid}">
				       	    	<%-- Display ratings given to this user --%>
								<lams:StyledRating criteriaRatings="${learnerData.value}" showJustification="true" alwaysShowAverage="false" currentUserDisplay="true"/>
							</div>
						</div>
	       	    	</c:forEach>
				</div>
				
	<c:if test="${sessionMap.isGroupedActivity}">
			</div>
		</div>
	</c:if>
	
	</c:forEach>
	
<c:if test="${sessionMap.isGroupedActivity}">
</div>
</c:if>