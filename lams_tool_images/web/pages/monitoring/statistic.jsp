<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

	<c:if test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		
		<c:forEach var="summary" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<h2>
					<fmt:message key="monitoring.label.group" /> ${summary.sessionName}
				</h2> 
				
				<table cellspacing="3" class="alternative-color space-top">
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
			</c:if>
			
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
										isItemAuthoredByUser="true"
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
	</c:forEach>
