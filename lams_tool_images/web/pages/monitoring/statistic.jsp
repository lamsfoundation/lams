<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<c:if test="${empty summaryList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:if>
	
<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">

	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel panel-default" >
			<div class="panel-heading">
				<span class="panel-title">
					<fmt:message key="monitoring.label.group" /> ${group[0].sessionName}
				</span>
			</div>
		</div>
	</c:if>
	
	<div class="panel-body">
	<table class="table table-condensed table-striped">
		<tr>
			<th width="35%">
				<fmt:message key="monitoring.label.title" />
			</th>
			<th width="25%" align="center">
				<fmt:message key="monitoring.label.suggest" />
			</th>
			<c:choose>
				<c:when test="${sessionMap.imageGallery.allowRank}">
					<th width="85px" style="padding-left:0px; text-align:center;">
						<fmt:message key="label.monitoring.average.rating" />
					</th>
				</c:when>
						
				<c:when test="${sessionMap.imageGallery.allowVote}">
					<th width="70px" style="padding-left:0px; text-align:center;">
						<fmt:message key="label.monitoring.number.votes" />
					</th>
				</c:when>
			</c:choose>						
		</tr>
			
		<c:forEach var="summary" items="${group}" varStatus="status">
			
			<c:if test="${summary.itemUid == -1}">
				<tr>
					<td colspan="4">
						<div align="left">
							<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
						</div>
					</td>
				</tr>
			</c:if>
			
			<c:if test="${summary.itemUid != -1}">
				<tr>
					<td>
						<c:out value="${summary.itemTitle}" escapeXml="true"/>
					</td>
					<td align="center">
						<c:if test="${!summary.itemCreateByAuthor}">
							<c:out value="${summary.username}" escapeXml="true"/>
						</c:if>
					</td>
					
					<c:choose>
						<c:when test="${sessionMap.imageGallery.allowRank}">	
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								<lams:Rating itemRatingDto="${summary.itemRatingDto}" disabled="true" 
										isDisplayOnly="true"
										maxRates="0" 
										countRatedItems="0" />
							</td>
						</c:when>
						
						<c:when test="${sessionMap.imageGallery.allowVote}">
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								${summary.numberOfVotes}
							</td>
						</c:when>
					</c:choose>	
				</tr>
			</c:if>
		</c:forEach>
	</table>
	</div>
</c:forEach>
