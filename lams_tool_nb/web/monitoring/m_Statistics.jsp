<%@ include file="/includes/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
 <c:if test="${isGroupedActivity}">
	<div class="ltable table-striped">
		<div>
			<fmt:message key="heading.totalLearnersInGroup" />
		</div>
	
		<c:forEach var="group" items="${monitoringDTO.groupStatsMap}">
			<div class="row align-items-center">
				<div class="col-md-2">
					<c:out value="${group.key}" />
				</div>
				<div class="col">
					<c:out value="${group.value}" />
				</div>
				
				<c:if test="${allowComments}">
					<div class="col">
						<c:url value="/monitoring/viewComments.do" var="commentURL">
							<c:param name="toolSessionID" value="${monitoringDTO.sessionIdMap[group.key]}" />
						</c:url>
						<button type="button" onclick="launchPopup('${commentURL}')" class="btn btn-secondary btn-sm float-end">
							<fmt:message key="label.view.comments" />
						</button>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</div>
</c:if>

<div class="ltable table-striped no-header">
	<div class="row align-items-center">
		<div class="col-md-2">
			<fmt:message key="heading.totalLearners" />
		</div>
		<div class="col">
			<c:out value="${monitoringDTO.totalLearners}" />
		</div>
		
		<c:if test="${allowComments && !isGroupedActivity}">
			<c:forEach var="group" items="${monitoringDTO.sessionIdMap}">
				<c:url value="/monitoring/viewComments.do" var="commentURL">
					<c:param name="toolSessionID" value="${group.value}" />
				</c:url>
				<div class="col">
					<button type="button" onclick="launchPopup('${commentURL}')" class="btn btn-secondary btn-sm float-end">
						<i class="fa-regular fa-comments me-1"></i>
						<fmt:message key="label.view.comments" />
					</button>
				</div>
			</c:forEach>
		</c:if>
	</div>
</div>
