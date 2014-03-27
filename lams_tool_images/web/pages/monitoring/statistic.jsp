<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>


<table cellspacing="3">
	<c:if test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:forEach var="summary" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<tr>
					<td colspan="4">
						<B><fmt:message key="monitoring.label.group" /> ${summary.sessionName}</B> <SPAN style="font-size: 12px;"> <c:if test="${firstGroup.index==0}">
								<fmt:message key="monitoring.summary.note" />
							</c:if> </SPAN>
					</td>
				</tr>
				<tr>
					<th width="35%">
						<fmt:message key="monitoring.label.title" />
					</th>
					<th width="25%">
						<fmt:message key="monitoring.label.suggest" />
					</th>
					<c:choose>
						<c:when test="${sessionMap.imageGallery.allowRank == true}">
							<th width="85px" style="padding-left:0px; text-align:center;">
								<fmt:message key="label.monitoring.number.rated" />
							</th>				
							<th width="70px" style="padding-left:0px; text-align:center;">
								<fmt:message key="label.monitoring.average.rating" />
							</th>
						</c:when>
						<c:when test="${sessionMap.imageGallery.allowVote == true}">
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
					<td>
						<c:if test="${!summary.itemCreateByAuthor}">
							<c:out value="${summary.username}" escapeXml="true"/>
						</c:if>
					</td>
					<c:choose>
						<c:when test="${sessionMap.imageGallery.allowRank == true}">
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								${summary.numberRatings}
							</td>				
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								${summary.averageRating}
							</td>
						</c:when>
						<c:when test="${sessionMap.imageGallery.allowVote == true}">
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								${summary.numberOfVotes}
							</td>
						</c:when>
					</c:choose>	
				</tr>
			</c:if>
		</c:forEach>
	</c:forEach>
</table>
